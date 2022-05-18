package com.easily.factory.aop;

import com.easily.factory.ClassMeta;
import com.easily.factory.Pool;
import com.easily.label.Aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * aop池：生产Map{匹配目标的正则,AopClass}
 *
 * 1、aop池解析一个类的方法时，只解析本类的方法，不解析父类的方法
 * 2、aop目标是不区分方法的重载的，也就是一个类里面有两个方法，方法名相同、参数类型不同，这样的两个方法会被aop识别为同一目标
 *
 * 名词解释：
 * aop目标方法：需要被切入功能的方法
 * aop功能方法：切入目标方法的功能，类型有：前置切入、后置切入、异常切入、最终切入
 * aop方法（AopMethod）：目标方法包裹着aop功能后，最终生成的实体。
 *
 * 注意：
 * 经过多层包裹的aop方法”最终切面“，会在当前层”后置“方法之后接着执行，也就是在下一层”后置“方法之前执行
 *
 *
 * aop包下几个类的作用：
 * AopPool ：接收工厂分发下来的原料，解析成“产品”，等待被使用
 * AopMethodCache： 临时缓存已经封装好的AopMethod，避免每次目标方法被调用都需要重新封装
 * AopMethod：经过aop封装的方法体，其invoke方法可直接调用
 * MethodBody：存储目标方法需要的类、参数、返回、异常等信息，每次AopMethod被调用时，会new一个
 * AopMethodActuator: AopMethod 内置执行器仅aop包内可调用，辅助AopMethod的切面调用、MethodBody的创建
 *
 */
public class AopPool implements Pool {

    private static final Logger log = Logger.getGlobal();

    private final Map<String[],Class<?>> aopMp = new LinkedHashMap<>();


    @Override
    public void put(ClassMeta classMeta) {
        Class<?> clazz = classMeta.getClazz();
        Aop annotationAop = clazz.getAnnotation(Aop.class);
        String[] values = annotationAop.value();
        if (values == null) return;

        List<String> newValues = new ArrayList<>();
        for (String value:values){
            newValues.add(parseAopValue(value));
        }
        aopMp.put(newValues.toArray(new String[0]), clazz);
    }

    @Override
    public Class<? extends Annotation> getLabel() {
        return Aop.class;
    }

    @Override
    public void end() {

    }

    private String parseAopValue(String aopValue) {
        if (aopValue.startsWith("method:")) {
            aopValue = aopValue.replace("method:", "[\\S]*");
        } else if (aopValue.startsWith("class:")) {
            aopValue = aopValue.replace("class:", "") + "[\\S]+";
        } else if (aopValue.startsWith("package:")) {
            aopValue = aopValue.replace("package:", "") + "[\\S]+";
        }else {
            aopValue = aopValue + "[\\S]*";
        }
        return aopValue;
    }

    /**
     * 获取aop类列表
     */
    private List<Class<?>> getAop(String targetMethodName){
        List<Class<?>> aopList = new ArrayList<>();
        // 循环map
        for (Map.Entry<String[],Class<?>> entry : aopMp.entrySet()) {
            // 匹配
            String[] aopValues = entry.getKey();
            for (String aopValue:aopValues){
                Pattern pattern = Pattern.compile(aopValue);
                Matcher matcher = pattern.matcher(targetMethodName);
                if (matcher.matches()){
                    aopList.add(entry.getValue());
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
