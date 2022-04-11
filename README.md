# Carrot

#### 介绍
通过 Carrot ，你可以轻松地构建一个简单的web程序。

Carrot 的优势： 不基于Servlet,不需要Tomcat，App.start(**.class) 一键启动，如果你想快速构建一个简单的web程序，Carrot 是你的选择。
#### 快速开始

添加maven依赖
```xml
<dependency>
    <groupId>com.vegetables</groupId>
    <artifactId>Carrot</artifactId>
    <version>0.0.1</version>
</dependency>
```

创建并启动
```java
public class Test {
    public static void main(String[] args){
        App.start(Test.class);
    }
}
```

![启动时日志截图](img/start.png)

#### 内置AOP介绍

Carrot 提供了一个内置的AOP实现，可以通过 @AOP 注解到你的类上，以完成对Controller方法执行前、后、环绕、异常、最终这个几个状态上的控制。

如果你想将AOP拦截器拓展到你的类上，可以通过 ```Nanny nanny = new Nanny(类名);``` 创建一个代理类对象。

AOP执行流程图，如下：

![AOP流程图](img/aop.png)。

#### 参与贡献




