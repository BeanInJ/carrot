package com.easily.factory.aop;

import com.easily.factory.ProductContainer;

import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * aop容器：存储Map{匹配目标的正则,AopClass}
 *
 * Method 方法被执行时，进行aop正则判断，true -- 》 AopMethod初始化（有可能会被多层包裹）   -- 》 缓存 lru(定长)
 */
public class AopContainer implements ProductContainer {
    private static final Logger log = Logger.getGlobal();

    /**
     * 有序map
     */
    private final Map<Class<?>, List<String>> aopMp = new LinkedHashMap<>();

    protected void put(Class<?> clazz, List<String> aopValues){
        aopMp.put(clazz,aopValues);
    }

    /**
     * 获取aop类列表
     */
    private List<Class<?>> getAop(String targetMethodName){
        List<Class<?>> aopList = new ArrayList<>();
        // 循环map
        for (Map.Entry<Class<?>, List<String>> entry : aopMp.entrySet()) {
            // 匹配
            List<String> aopValues = entry.getValue();
            for (String aopValue:aopValues){
                Pattern pattern = Pattern.compile(aopValue);
                Matcher matcher = pattern.matcher(targetMethodName);
                if (matcher.matches()){
                    aopList.add(entry.getKey());
                    break;
                }
            }
        }
        return aopList;
    }

    /**
     * 获取aop方法
     *
     * 获取：
     * 1、先从aop缓存中获取
     * 2、如果没有，则从aop容器中解析
     *
     * 返回结果：
     * 1、如果返回null表示该目标没有aop方法
     * 2、如果过有返回该aop方法，并加入缓存
     */
    public AopMethod getAopMethod(Method targetMethod, Object targetObject) throws InstantiationException, IllegalAccessException {

        // 目标方法全名
        String targetMethodId = targetObject.getClass().getName() + "." + targetMethod.getName();

        // 从缓存中获取
        AopMethod aopMethod = AopMethodCache.get(targetMethodId);
        if (aopMethod != null) return aopMethod;

        // 缓存中没有,从容器获取
        List<Class<?>> aopClassList = this.getAop(targetMethodId);
        if (aopClassList.isEmpty()) return null;

        // 构建aop方法
        aopMethod = createAopMethod(targetMethod,targetObject,aopClassList);

        // 存入缓存
        if (aopMethod != null) AopMethodCache.put(aopMethod.getMethodTag(),aopMethod);

        return aopMethod;
    }

    /**
     * 构建aop方法
     * @param targetMethod 目标方法
     * @param aopClassList aop类列表
     * @return 构建完成的aop方法体
     */
    private AopMethod createAopMethod(Method targetMethod,Object targetObject,List<Class<?>> aopClassList) throws InstantiationException, IllegalAccessException {
        AopMethod aopMethod = null;
        for (Class<?> aopClass : aopClassList) {
            String aopObjectName = aopClass.getName();
            try {
                // aopObject aop方法所在的类
                Object aopObject = aopClass.newInstance();
                if (aopMethod == null) {
                    // 根据原始方法创建
                    aopMethod = new AopMethod(targetMethod, targetObject, aopObject);
                } else if (!aopObjectName.equals(aopMethod.getAopObject().getClass().getName())) {
                    // 检查非重复后，根据aop方法创建
                    aopMethod = new AopMethod(aopMethod, targetObject, aopObject);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.warning("Aop类构建异常,已跳过："+aopObjectName);
            }
        }

        return aopMethod;
    }
}
