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
// App.start(Test.class);   Carrot根据Test所在位置进行包扫描
// 当然你也可以通过在项目根目录下，添加配置文件 config.yml 配置需要进行扫描的包名。（app.start.package: **.**.**）
```

#### 个性化配置

你可以通过在项目根目录下，添加配置文件 config.yml，配置系统参数。

可配置参数示例：
```yaml
# 服务端口号
app.port: 8000
# 当期启动状态/子配置文件名
app.active: test
# 服务名
app.name: myWebServer
# 服务描述
app.desc: 我的web服务器
# 系统版本
app.version: 0.0.1
# 作者
app.author: BeanInJ
# 密钥
app.key: Q2Fycm90Cg==
# 请求缓存池大小（请根据服务器自身运行配置）
app.channel.size: 1024
# 待扫描包名（Controller、Service、Aop 不在待扫描包下无效）
app.start.package: com.excemple.test
```

#### 内置接口

用于测试的内置接口：


| 接口                            | 测试内容|理想返回内容|
|----|----|----|
| http://localhost:8081         | 控制器方法，正常返回                     | 服务注册名：carrot, 系统版本：1.0.0, Carrot author: BeanInJ |
| http://localhost:8081/author  | 控制器方法，正常返回                     | Carrot author: BeanInJ                          |
| http://localhost:8081/carrot  | 控制器方法，强制覆盖重复url                | 服务注册名：carrot, 系统版本：1.0.0                        |
| http://localhost:8081/version | AOP切面类：VersionControllerAop    | 1.0.1                                           |
| http://localhost:8081/?       | 前置拦截器：UrlIntercept             | 想做点什么呢？                                         |
| http://localhost:8081/404     | 后置拦截器：ResponseIntercept，404状态拦截 | 404 - Not Found : /404                          |
| http://localhost:8081/123     | 后置拦截器：ResponseIntercept，空返回拦截  | /123 - 返回 null                                  |


![启动时日志截图](img/start.png)

#### 内置AOP介绍

Carrot 提供了一个内置的AOP实现，可以通过 @AOP 注解到你的类上，以完成对Controller方法执行前、后、环绕、异常、最终这个几个状态上的控制。

如果你想将AOP拦截器拓展到你的类上，可以通过 ```Nanny nanny = new Nanny(类名);``` 创建一个代理类对象。

AOP执行流程图，如下：

![AOP流程图](img/aop.png)。

#### 参与贡献




