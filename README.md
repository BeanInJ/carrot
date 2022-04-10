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

#### 使用说明


#### 参与贡献




