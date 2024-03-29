# Carrot

#### 介绍
通过 Carrot ，你可以轻松地构建一个简单的web程序。

Carrot 的优势：App.start(**.class) 一键启动，如果你想快速构建一个简单的web程序，Carrot 是你的选择。
#### 快速开始
> 详细示例在 carrot-example 项目中

添加maven依赖
```xml
        <dependency>
            <groupId>com.easily</groupId>
            <artifactId>carrot</artifactId>
            <version>0.0.4-jar-with-dependencies</version>
        </dependency>
```

创建并启动
```java
public class Application {
    public static void main(String[] args) {
        App.start(Application.class);
    }
}

// App.start(Application.class);   Carrot根据Application所在位置进行全包扫描
// 当然你也可以通过在项目根目录下（或resources目录下），添加配置文件 config.yml 配置需要进行扫描的包名。（app.start.package: **.**.**）
```

main方法中有了 App.start(##.class) ,你就可以开始尝试启动项目。Carrot中内置了很多测试接口，你可以通过项目日志查看，然后测试访问。

接下来添加一个Controller
```java
import com.easily.label.Controller;

@Controller("/test")
public class TestController {

    @Controller("/hello")
    public String hello(){
        return "Hello Carrot !";
    }
}
```

如上，我们成功添加了一个接口。

还可以通过下面这种方式可以添加接口：
```java
import com.easily.core.http.Request;
import com.easily.core.http.Response;
import com.easily.factory.controller.Urls;
import com.easily.label.AddUrls;
import com.easily.label.Prefix;
import com.easily.label.Service;
import org.example.user.service.UserService;

import java.util.HashMap;
import java.util.Map;

@AddUrls
public class UserController {

    @Service
    UserService userService;

    @Prefix("/user")
    public void userUrls(Urls urls){
        urls.add("/getAll",this::getAllUser);
        urls.add("/getOne",this::getOne);
    }
    
    public void getAllUser(Request request, Response response){
        Map<String,String> map = new HashMap<>();
        map.put("zhangsan","123");
        map.put("lisi","456");
        map.put("wangwu","789");
        response.setBody(map);
    }

    public void getOne(Request request, Response response){
        response.setBody(userService.getOne());
    }
}
```

在Carrot中，也有类似Spring boot中的IOC一样的功能，我们称它为类工厂，通过类工厂初始化的对象，可以使用@Service、@Controller、@ConfigValue、@Resouce等注解自动注入。

那么什么样的对象会被加入类工厂呢？通过@Service、@Controller、@Carrot、@Aop等注解标注的类，都会加入类工厂。

如下例，是一个service对象加入工厂、自动注入的使用示例：
```java

public interface UserService {
    String getOne();
}


@Service
public class UserServiceImpl implements UserService {
    public String getOne(){
        return "zhang:123";
    }
}


@Controller("/test")
public class TestController {
    @Service
    UserService userService;
    
    @Controller("service")
    public String getUserService(){
        return userService.getOne();
    }
}
```

#### 个性化配置

你可以通过在项目根目录下，添加配置文件 config.yml，配置系统参数。

可配置参数示例：
```yaml
# 服务端口号
app.port: 8000
# 系统环境
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

![AOP流程图](img/aop.png)

#### 请求流程与生命周期

系统启动过程：

```加载资源 --> 开放端口（打开socket） -->  开启线程池（一个请求进来，申请一个线程）```


加载资源过程：

```配置加载 -->  日志加载 --> 类工厂初始化 --> 扫描器初始化 --> 开启包扫描扫描 --> 类工厂分发资源到“类池” --> url映射器初始化```

> (类池只会等待被加载或调用,也就是它的"技能"是被动的)


url映射器：

```从 controller类池 解析url并保存```

一个请求进来（数据类型的传递）：

```线程1：  channel.read --> ByteBuffer  -->  baseHttp --> request --> response --> baseHttp --> ByteBuffer --> channel.write.close```


一个请求进来（程序流程，这里每一步都可以往后跳载）：

```线程1：channel.read --> 前置拦截器 -->  request --> url映射器 --> "控"制"器" -->  response --> 后置拦截器 --> 异常拦截器  ("代表aop) --> channel.write.close```




#### 构想、设计、优化

1、写一个示例程序，包含了各种功能的使用，以及mybatis的嵌入

~~2、优化请求线程结构                      √~~

~~3、优化配置中心~~

~~4、优化AOP切入逻辑                      ing（采用第二种实现方案）~~

~~5、实现IOC容器（控制反转、自动注入）~~

~~6、包结构重新调整                       ing~~

7、接入mybatis配置

~~8、实现缓存、Session                    ing~~

9、实现url加密、解密、缩短

10、测试本项目运行后所占内存大小，优化内存

11、目前url是字符串直接匹配，若设计为树结构，映射匹配和拦截匹配也许速度会更快

12、应该具备有提供系统参数、元数据输出与操作的接口

13、对于一些数据量较大的bean，应当考虑懒加载

14、bean的循环依赖问题，目前是两级缓存，应当考虑使用三级缓存，注入aop代理对象

15、采用cglib重写AOP部分


~~关于aop实现的两种方案：~~

~~1、提前解析aop方法，封装为aop方法体。~~

~~也就是在系统启动时解析切面类，最终得到“代理列表”（目标方法+切面方法体）。~~

~~当每次调用某个方法前，就去“代理列表”检查，该方法是否属于要切入的方法。~~

~~优点：切入方法时，执行速度更快，因为切面方法体都是提前解析好了的，直接调用即可。~~

~~缺点：当目标方法过多时，会解析出来很多aop方法体，占用内存。~~



~~2、不提前封装，目标方法调用时aop切入时再封装执行。~~

~~优点：不占用太多内存。~~

~~缺点：每个请求进来都需要解析aop类，当aop类过多时可能会拖慢响应速度。~~



#### 设计模式

单例模式：在本框架中，控制器类、过滤器类等的创建采用的是饿汉式单例模式。


工厂模式：所有从扫描器获取得到的类，都是统一经过类工厂去处理的。


装饰器模式：HTTP请求体和响应体的设计，采用了装饰器模式。


#### 分支说明

```xml

master：        主分支，最新版本总是在主分支上

Carrot-*.*.*：  某个保留版本的分支

fastCarrot:     一个轻量化、快速搭建版本的Carrot，目前正在探索中

```