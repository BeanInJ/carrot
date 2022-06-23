package com.easily.core.bootstrap;

import com.easily.App;
import com.easily.core.CarrotProvider;
import com.easily.core.CarrotServer;
import com.easily.core.ConfigCenter;
import com.easily.factory.ClassFactory;
import com.easily.factory.AnnotationScanner;
import com.easily.factory.Pools;
import com.easily.label.Carrot;
import com.easily.system.dict.CONFIG;
import com.easily.system.log.LogConfig;
import com.easily.system.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * 程序入口（懒汉单例加载）
 */
public class Enter {
    private static final Logger log = Logger.getGlobal();
    private Class<?> mainClass;
    private ConfigCenter configCenter;

    private Pools pools;
    public void start(Class<?> mainClass,String[] args) throws IllegalAccessException, InstantiationException {
        this.mainClass = mainClass;
        Actuator actuator = new Actuator();
        actuator.add(this::beforeStart);
        actuator.add(this::configCenterLoad);
        actuator.add(this::logLoad);
        actuator.add(this::classFactoryLoad);
        actuator.add(this::carrotServer);

        actuator.start();
    }

    /**
     * 加载配置中心
     */
    private Object configCenterLoad(Object o){
        ConfigCenter configCenter = ElementsSingleton.get(ConfigCenter.class);
        configCenter.load(this.mainClass);
        this.configCenter = configCenter;
        return o;
    }

    /**
     * 加载日志
     */
    private Object logLoad(Object o){
        LogConfig logConfig = new LogConfig();
        logConfig.load(this.configCenter);
        return o;
    }

    /**
     * 加载class工厂
     */
    private Object classFactoryLoad(Object o) throws IllegalAccessException, InstantiationException {
        Set<String> packageNames = new HashSet<>();

        // 内部包
        packageNames.add(CONFIG.APP_INNER_PACKAGE_VALUE);

        Object outerPackage = this.configCenter.get(CONFIG.APP_START_PACKAGE);
        if(StringUtils.isNotBlankOrNull(outerPackage)){
            if(App.class.getPackage().getName().equals(outerPackage)){

            }else {
                // 外部包
                packageNames.add(outerPackage.toString());
            }
        }else {
            log.warning("无法获取main方法所在的包名，您可以在config.yml下手动配置main方法所在的包");
        }

        AnnotationScanner scanner = new AnnotationScanner(packageNames,Carrot.class);
        ClassFactory classFactory = new ClassFactory(scanner,this.configCenter);
        classFactory.start();
        this.pools = classFactory.getPools();
        return o;
    }

    private Object carrotServer(Object o){
        CarrotProvider carrotProvider = new CarrotProvider();
        carrotProvider.addPools(this.pools);
        CarrotServer carrotServer = new CarrotServer();
        Integer port = this.configCenter.get(CONFIG.APP_PORT,Integer.class);
        try {
            carrotServer.openPort(port);
//            carrotServer.addPools(this.pools);
            CarrotServer.service.execute(carrotServer::loopGet);
            CarrotServer.service.execute(carrotServer::loopDeal);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 执行beforeStart开头的方法
     */
    private Object beforeStart(Object o) {
        // 执行beforeStart开头的方法
        Object main;
        try {
            main = mainClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }

        Object returnValue = null;
        Method[] methods = mainClass.getDeclaredMethods();
        for (Method method : methods) {
            String name = method.getName();
            // 需要提前执行的方法名，必须是beforeStart开头，且不能有参数
            if(name.startsWith("beforeStart") && method.getParameterCount() == 0){
                try {
                    returnValue = method.invoke(main);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return returnValue;
    }
}
