package com.easily.core.bootstrap;

import com.easily.App;
import com.easily.core.AppCpu;
import com.easily.core.ChannelCollector;
import com.easily.core.ConfigCenter;
import com.easily.core.Container;
import com.easily.factory.ClassFactory;
import com.easily.factory.AnnotationScanner;
import com.easily.label.Carrot;
import com.easily.system.dict.CONFIG;
import com.easily.system.log.LogConfig;
import com.easily.system.util.StringUtils;

import java.lang.reflect.Method;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

/**
 * 程序入口（懒汉单例加载）
 */
public class Enter {
    private static final Logger log = Logger.getGlobal();
    private Class<?> mainClass;
    private ConfigCenter configCenter;

    private Container container;
    public void start(Class<?> mainClass) throws IllegalAccessException, InstantiationException {
        this.mainClass = mainClass;
        Actuator actuator = new Actuator();
        actuator.add(this::beforeStart);
        actuator.add(this::configCenterLoad);
        actuator.add(this::logLoad);
        actuator.add(this::classFactoryLoad);
        actuator.add(this::channelCollectorLoad);
        actuator.add(this::appCpuStart);

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

        // 外部包
        Object outerPackage = this.configCenter.get(CONFIG.APP_START_PACKAGE);
        if(StringUtils.isNotBlankOrNull(outerPackage)){
            if(!App.class.getPackage().getName().equals(outerPackage))
                packageNames.add(outerPackage.toString());
        }else {
            log.warning("无法获取main方法所在的包名，您可以在config.yml下手动配置main方法所在的包");
        }

        AnnotationScanner scanner = new AnnotationScanner(packageNames,Carrot.class);

        ClassFactory classFactory = new ClassFactory();
        classFactory.setScanner(scanner);
        classFactory.load(this.configCenter);
        classFactory.start();
        return new Container(classFactory);
    }

    /**
     * 开放socket通道
     */
    private ArrayBlockingQueue<SocketChannel> channelCollectorLoad(Object o){
        this.container = (Container) o;
        ChannelCollector channelCollector = ElementsSingleton.get(ChannelCollector.class);
        Integer port = this.configCenter.get(CONFIG.APP_PORT,Integer.class);
        Integer queueSize = this.configCenter.get(CONFIG.APP_CHANNEL_SIZE,Integer.class);
        port = port == null? CONFIG.APP_PORT_VALUE:port;
        queueSize = queueSize == null? CONFIG.APP_CHANNEL_SIZE_VALUE:queueSize;
        channelCollector.init(port,queueSize);
        return channelCollector.get();
    }

    private Object appCpuStart(Object o){
        ArrayBlockingQueue<SocketChannel> channels = (ArrayBlockingQueue<SocketChannel>) o;
        new AppCpu().start(channels,this.container);
        return null;
    }

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
