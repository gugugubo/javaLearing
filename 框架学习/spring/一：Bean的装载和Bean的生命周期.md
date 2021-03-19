# spring 常用注解 原理 逻辑 代码演示

[这是观看视频的笔记](https://www.bilibili.com/video/BV1oW41167AV?p=2)



### 文章目录

- [01 spring 常用注解 原理 逻辑 代码演示](file:///C:/Users/古春波/Desktop/新建文本文档.html#01_spring_____1)
- - [一、组件注册](file:///C:/Users/古春波/Desktop/新建文本文档.html#_6)
  - - [1.1-spring注解驱动开发](file:///C:/Users/古春波/Desktop/新建文本文档.html#11spring_8)
    - [1.2-组件注册 @Configuration](file:///C:/Users/古春波/Desktop/新建文本文档.html#12_Configuration_12)
    - [1.3-组件注册 Configuration、Bean、ComponentScan(s)、TypeFilter](file:///C:/Users/古春波/Desktop/新建文本文档.html#13_ConfigurationBeanComponentScansTypeFilter_36)
    - [1.4-组件注册 @Scope](file:///C:/Users/古春波/Desktop/新建文本文档.html#14_Scope_163)
    - [1.5-组件注册@Lazy-bean懒加载](file:///C:/Users/古春波/Desktop/新建文本文档.html#15Lazybean_208)
    - [1.6-组件注册 @Conditional 按照条件给容器注入Bean](file:///C:/Users/古春波/Desktop/新建文本文档.html#16_Conditional_Bean_223)
    - [1.7-组件注册 @Import快速导入](file:///C:/Users/古春波/Desktop/新建文本文档.html#17_Import_282)
    - - [1.7.1-组件注册 @ImportSelector](file:///C:/Users/古春波/Desktop/新建文本文档.html#171_ImportSelector_311)
      - [1.7.2-组件注册 @ImportBeanDefinationRegister](file:///C:/Users/古春波/Desktop/新建文本文档.html#172_ImportBeanDefinationRegister_361)
    - [1.8-组件注册 @FactoryBean](file:///C:/Users/古春波/Desktop/新建文本文档.html#18_FactoryBean_379)
  - [二、生命周期](file:///C:/Users/古春波/Desktop/新建文本文档.html#_423)
  - - [2.1-生命周期 @Bean指定初始化和销毁方法](file:///C:/Users/古春波/Desktop/新建文本文档.html#21_Bean_425)
    - [2.2-生命周期 InitializingBean和DisposableBean](file:///C:/Users/古春波/Desktop/新建文本文档.html#22_InitializingBeanDisposableBean_485)
    - [2.3-生命周期 @PostConstruct和@PreDestroy](file:///C:/Users/古春波/Desktop/新建文本文档.html#23_PostConstructPreDestroy_507)
    - [2.4-生命周期 BeanPostProcessor（后置处理器）](file:///C:/Users/古春波/Desktop/新建文本文档.html#24_BeanPostProcessor_525)
    - - [2.4.1-生命周期 BeanPostProcessor原理](file:///C:/Users/古春波/Desktop/新建文本文档.html#241_BeanPostProcessor_542)
      - [2.4.2-生命周期 spring底层对BeanPostProcessor的使用](file:///C:/Users/古春波/Desktop/新建文本文档.html#242_springBeanPostProcessor_625)
  - [三、属性赋值](file:///C:/Users/古春波/Desktop/新建文本文档.html#_717)
  - - [3.1-属性赋值 @Value](file:///C:/Users/古春波/Desktop/新建文本文档.html#31_Value_719)
    - [3.2-属性赋值 @PropertySource加载外部配置文件](file:///C:/Users/古春波/Desktop/新建文本文档.html#32_PropertySource_769)
  - [四、自动装配](file:///C:/Users/古春波/Desktop/新建文本文档.html#_797)
  - - [4.1-自动装配 @Autowired & @Qualifier & @Primary](file:///C:/Users/古春波/Desktop/新建文本文档.html#41_Autowired__Qualifier__Primary_799)
    - [4.2-自动装配 JSR250-@Resource、JSR330-@Inject](file:///C:/Users/古春波/Desktop/新建文本文档.html#42_JSR250ResourceJSR330Inject_893)
    - [4.3-自动装配 方法、构造器位置的自动装配 & Aware注入Spring底层组件 & 原理](file:///C:/Users/古春波/Desktop/新建文本文档.html#43___AwareSpring___911)
    - [4.4-自动装配 @Profile 根据环境注册Bean](file:///C:/Users/古春波/Desktop/新建文本文档.html#44_Profile_Bean_989)



## 一、组件注册

### 1.1-spring注解驱动开发

![image-20200511205219602](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTEyMDUyMTk2MDIucG5n?x-oss-process=image/format,png)

### 1.2-配置xml环境

```properties
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>4.3.12.RELEASE</version>
</dependency>
```

1.如果创建`beans.xml`没有如下内容，则为没有添加spring支持

![image-20200511215300998](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTEyMTUzMDA5OTgucG5n?x-oss-process=image/format,png)

2.则开启

![image-20200511215403953](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTEyMTU0MDM5NTMucG5n?x-oss-process=image/format,png)

3.

![image-20200511215507250](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTEyMTU1MDcyNTAucG5n?x-oss-process=image/format,png)

### 1.3-组件注册 Configuration、Bean、ComponentScan(s)、TypeFilter

#### bean:使用xml注册组件

1.创建一个`Person`类

![image-20200511215948782](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTEyMTU5NDg3ODIucG5n?x-oss-process=image/format,png)

2.配置`beans.xml`

3.给一个`id`方便从容器中获取

4.可以通过`property`作为一个属性的赋值
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200604113047288.png)

5.开始使用，写一个测试类

1. 通过`ClassPathXmlApplicationContext`,表示类路径下的一个`xml`配置文件。**会返回`IOC`容器**
2. 可通过`getBean`加上“`id`”进行获取。或是类型

![image-20200511220652858](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTEyMjA2NTI4NTgucG5n?x-oss-process=image/format,png)



#### @bean:使用配置文件注册组件

1. 以前配置文件的方式被替换为了配置类

   1. 建立一个`config.MainConfig`

   ![image-20200511221104414](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTEyMjExMDQ0MTQucG5n?x-oss-process=image/format,png)

   

2.回到`MainTest`,通过`AnnotationConfigApplicationContext`注解式的`config`,它传入的就是这个配置类。相当于是传配置类的位置。

![image-20200511221947155](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTEyMjE5NDcxNTUucG5n?x-oss-process=image/format,png)

3.通过`getBeanDefinitionNames`可获得`Bean`容器中组件的所有名称

![image-20200511222718056](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTEyMjI3MTgwNTYucG5n?x-oss-process=image/format,png)

4.也可通过`getBeanNamesForType`

![image-20200511222718056](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTEyMjI3MTgwNTYucG5n?x-oss-process=image/format,png)

5.也可通过getBeanNamesForType

![image-20200511222836873](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTEyMjI4MzY4NzMucG5n?x-oss-process=image/format,png)

6.通过上面的这个方法，我也可改变组件名称。要么改方法名，要么采用下面这种方式

![image-20200511223329264](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTEyMjMzMjkyNjQucG5n?x-oss-process=image/format,png)

6.在实际开发中，包的扫描写得比较多

1. ![image-20200511223832900](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTEyMjM4MzI5MDAucG5n?x-oss-process=image/format,png)

#### component-scan:xml配置包扫描

1.这是xml的写法（以前的方式）:

```java
<!--包扫描、只要标注了@Controller、@Service、@Repository、@Component，都会被自动扫描加入容器中-->
<context:component-scan base-package="top.p3wj"></context:component-scan>
```

![image-20200512125701035](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxMjU3MDEwMzUucG5n?x-oss-process=image/format,png)

#### @ComponentScan:配置类配置包扫描

1.写在配置类中

![image-20200512125756335](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxMjU3NTYzMzUucG5n?x-oss-process=image/format,png)

2.效果演示

![image-20200512130735176](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxMzA3MzUxNzYucG5n?x-oss-process=image/format,png)

 发现其中`mainConfig`也是一个组件，是因为`@Configuration`也是一个`@Component`

![image-20200512131106829](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxMzExMDY4MjkucG5n?x-oss-process=image/format,png)

3.`excludeFilter`,过滤不扫描的内容。

![image-20200512131533196](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxMzE1MzMxOTYucG5n?x-oss-process=image/format,png)

 ![image-20200512152116248](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxNTIxMTYyNDgucG5n?x-oss-process=image/format,png)

![image-20200512152146253](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxNTIxNDYyNTMucG5n?x-oss-process=image/format,png)

它是一个`Filter()`数组

![image-20200512152438561](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxNTI0Mzg1NjEucG5n?x-oss-process=image/format,png)

```java
//excludeFilters = Filter[] 指定扫描的时候按照规则排除哪些规则
//includeFilters = Filter[] 指定扫描的时候只需要包含哪些组件
//useDefaultFilters 默认为true,加载所有组件
```

![image-20200512152805361](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxNTI4MDUzNjEucG5n?x-oss-process=image/format,png)

#### @ComponentScans:配置类配置包扫描

![image-20200512152959793](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxNTI5NTk3OTMucG5n?x-oss-process=image/format,png)

 在8几以上中才可以

![image-20200512153417456](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxNTM0MTc0NTYucG5n?x-oss-process=image/format,png)

如果不是，就使用`@ComponentScan`，指定扫描策略

![image-20200512153632824](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxNTM2MzI4MjQucG5n?x-oss-process=image/format,png)

![image-20200512153718572](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxNTM3MTg1NzIucG5n?x-oss-process=image/format,png)

1. **FilterType.ASSIGNABLE_TYPE** 按照给定的类型
2. **FilterType.ASPECTJ** 使用ASPECTJ表达式（不太常用）

3. **FilterType.REGEX** 使用正则表达式

![image-20200512154654583](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxNTQ2NTQ1ODMucG5n?x-oss-process=image/format,png)

实现`TypeFilter`

![image-20200512155505125](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxNTU1MDUxMjUucG5n?x-oss-process=image/format,png)

![image-20200512155730049](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxNTU3MzAwNDkucG5n?x-oss-process=image/format,png)

**top.p3wj中的每一个类都会进入进行匹配**

### 1.4-组件注册 @Scope

![image-20200512160837963](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxNjA4Mzc5NjMucG5n?x-oss-process=image/format,png)

![image-20200512160917381](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxNjA5MTczODEucG5n?x-oss-process=image/format,png)

 **默认单实例**

![image-20200512174415394](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxNzQ0MTUzOTQucG5n?x-oss-process=image/format,png)

```java
/* ConfigurableBeanFactory#SCOPE_PROTOTYPE  prototype   多实例
* ConfigurableBeanFactory#SCOPE_SINGLETON  singleton   单实例(默认值)
* org.springframework.web.context.WebApplicationContext#SCOPE_REQUEST  request 同一次请求创建一个实例
* org.springframework.web.context.WebApplicationContext#SCOPE_SESSION  session 同一个session创建一个实例
*/
```

**改为多实例后**

![image-20200512174717502](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxNzQ3MTc1MDIucG5n?x-oss-process=image/format,png)

这其实就相当于在`xml`文件中，`Bean`里加上`scope`

![image-20200512174811173](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxNzQ4MTExNzMucG5n?x-oss-process=image/format,png)

**ioc容器启动会创建对象，放到ioc容器中，以后每次获取就是直接从容器(map.get())中拿**

**1.下面演示单实例**

![image-20200512175129601](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxNzUxMjk2MDEucG5n?x-oss-process=image/format,png)

把`test02`中下面的注释掉

![image-20200512175221757](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxNzUyMjE3NTcucG5n?x-oss-process=image/format,png)

**2.多实例情况**

![image-20200512180504798](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxODA1MDQ3OTgucG5n?x-oss-process=image/format,png)

就不打印“给容器中添加Person…”了

![image-20200512180656870](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxODA2NTY4NzAucG5n?x-oss-process=image/format,png)

`ioc`容器启动并不会去调用方法创建对象放在容器中。每次获取的时候才会调用方法创建对象。

### 1.5-组件注册@Lazy-bean懒加载

- 单实例bean，默认在容器启动的时候创建对象
- 懒加载：容器启动不创建对象，第一次使用（获取）Bean创建对象，并初始化

![image-20200512181332557](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxODEzMzI1NTcucG5n?x-oss-process=image/format,png)

![image-20200512181358877](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxODEzNTg4NzcucG5n?x-oss-process=image/format,png)

**即在第一次获得的时候才加载**

若取消懒加载

![image-20200512181536119](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxODE1MzYxMTkucG5n?x-oss-process=image/format,png)

### 1.6-组件注册 @Conditional 按照条件给容器注入Bean

```java
@Conditional ,按照一定的条件进行判断，满足条件给容器中注册Bean
```

先前准备：

![image-20200512182257589](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxODIyNTc1ODkucG5n?x-oss-process=image/format,png)

![image-20200512182319410](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxODIzMTk0MTAucG5n?x-oss-process=image/format,png)

要求：

```java
* 如果是MacOs,给容器注册 jobs
* 如果是linux，给容器注册linus
```

![image-20200512183810347](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxODM4MTAzNDcucG5n?x-oss-process=image/format,png)

通过applicationContext拿到一个运行的环境

![image-20200512183326595](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxODMzMjY1OTUucG5n?x-oss-process=image/format,png)

![image-20200512183431866](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxODM0MzE4NjYucG5n?x-oss-process=image/format,png)

要传入一个`Condition`数组。`@Conditional({})`

![image-20200512183517813](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxODM1MTc4MTMucG5n?x-oss-process=image/format,png)

配置两个实现了`Condition`的类

![image-20200512191747085](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxOTE3NDcwODUucG5n?x-oss-process=image/format,png)

![image-20200512191753422](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxOTE3NTM0MjIucG5n?x-oss-process=image/format,png)

设置一下参数![image-20200512192027729](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxOTIwMjc3MjkucG5n?x-oss-process=image/format,png)

![image-20200512192110595](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIxOTIxMTA1OTUucG5n?x-oss-process=image/format,png)

```java
//boolean pserson = registry.containsBeanDefinition("pserson");//也可判断容器中是否包含一个Bean。也可给容器中注册Bean
```

可以做非常多的判断条件

![image-20200512202421896](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIyMDI0MjE4OTYucG5n?x-oss-process=image/format,png)

也可放在类上，含义即满足当前条件，这个类中配置的 所有bean注册才能生效

**注意**：

若有多个，则为按照顺序判断（猜测）已经设置`-Dos.name=Linux`

![image-20200512202810229](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIyMDI4MTAyMjkucG5n?x-oss-process=image/format,png)

### 1.7-组件注册 @Import快速导入

```java
/**
 * 给容器中注册组件：
 * 1) 包扫描+组件标注注解 (@Controller/@Service/@Repository/@Component)[自己写的]
 * 2) @Bean[导入的第三方包里面的组件],但是它比较麻烦（需要return等）
 * 3) @Import[快速给容器导入一个组件]
 */
```

1.新建一个`color`类![image-20200512201028328](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIyMDEwMjgzMjgucG5n?x-oss-process=image/format,png)

2.使用`@Import`![image-20200512201044794](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIyMDEwNDQ3OTQucG5n?x-oss-process=image/format,png)

3.测试

![image-20200512202928632](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIyMDI5Mjg2MzIucG5n?x-oss-process=image/format,png)

```java
//导入组件，id默认是组件的全类名,@Import(要导入到容器中到组件)，容器中就会自动注册这个组件，id默认是全类名
```

![image-20200512203021101](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIyMDMwMjExMDEucG5n?x-oss-process=image/format,png)

可以导入多个，现写一个Red类

![image-20200512203140380](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTIyMDMxNDAzODAucG5n?x-oss-process=image/format,png)

#### 1.7.1-组件注册 @ImportSelector

```java
2)  ImportSelector：返回需要导入的组件的全数组
public interface ImportSelector {

   /**
    * Select and return the names of which class(es) should be imported based on
    * the {@link AnnotationMetadata} of the importing @{@link Configuration} class.
    */
   String[] selectImports(AnnotationMetadata importingClassMetadata);

}
```

前提条件：使用`@Import`注解导入`MyImportSelector`

![image-20200513205236065](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTMyMDUyMzYwNjUucG5n?x-oss-process=image/format,png)

![image-20200513205309622](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTMyMDUzMDk2MjIucG5n?x-oss-process=image/format,png)

打上断点进行调试

![image-20200513205336350](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTMyMDUzMzYzNTAucG5n?x-oss-process=image/format,png)

结果：

![image-20200513205350130](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTMyMDUzNTAxMzAucG5n?x-oss-process=image/format,png)

如果返回`null:---->return null;`会报空指针，因为在拿类名的时候

![image-20200513205605821](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTMyMDU2MDU4MjEucG5n?x-oss-process=image/format,png)

所以不要返回`null`，可以返回一个空数组

```java
return new String[]{};
```

![image-20200513210013639](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTMyMTAwMTM2MzkucG5n?x-oss-process=image/format,png)

可获取到所以注解信息及类相关的

因为是`return`的，所以也被导入了

![image-20200513211141399](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTMyMTExNDEzOTkucG5n?x-oss-process=image/format,png)

#### 1.7.2-组件注册 @ImportBeanDefinationRegister

```java
//ImportBeanDefinitionRegistrar 手动注册Bean
```

`command+alt+b`查看`BeanDefinition`实现类

![image-20200513213125884](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTMyMTMxMjU4ODQucG5n?x-oss-process=image/format,png)

注意，`import`方式注入的名称为全类名

![image-20200513235948165](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTMyMzU5NDgxNjUucG5n?x-oss-process=image/format,png)

![image-20200514000256305](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQwMDAyNTYzMDUucG5n?x-oss-process=image/format,png)

### 1.8-组件注册 @FactoryBean

![image-20200514000945452](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQwMDA5NDU0NTIucG5n?x-oss-process=image/format,png)

通过此方法把对象放到容器中：注意，在下面的实现中，设置为多例模式。

![image-20200514103628414](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxMDM2Mjg0MTQucG5n?x-oss-process=image/format,png)

![image-20200514103648956](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxMDM2NDg5NTYucG5n?x-oss-process=image/format,png)

结果：

![image-20200514103750174](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxMDM3NTAxNzQucG5n?x-oss-process=image/format,png)

```java
//工厂获取的是调用getObject创建的对象
@Override
public boolean isSingleton() {
    return false;
}
```

`isSingleton`是`false`情况下是多实例，每一次获取都调用`getObject`

![image-20200514104048192](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxMDQwNDgxOTIucG5n?x-oss-process=image/format,png)



![image-20200514104418286](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxMDQ0MTgyODYucG5n?x-oss-process=image/format,png)

`Reason:`（下面图中的翻译错了）将返回工厂，而不是工厂返回的示例

![image-20200514104829151](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxMDQ4MjkxNTEucG5n?x-oss-process=image/format,png)

```java
/*使用Spring提供的FactoryBean(工厂Bean)
*              1)  默认获取到到是工厂bean调用getObject创建的对象
*              2)  要获取工厂Bean本身，我们需要给id前面加一个&
*                  &colorFactoryBean
*/
```



### 1.9 总结

1. 通过xml的容器需要一个xml作为主配置类；通过配置的容器需要一个@Configuration注解的类作为主类
2. 然后通过主类配置包扫描，或者在到已经被包扫描扫到的类实例中使用@Bean，@Import，@Condition，@FactoryBean将实例加入容器。



## 二、生命周期

### 2.1-生命周期 @Bean指定初始化和销毁方法

在以前，可以指定初始化和销毁方法

1. 

![image-20200514105507290](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxMDU1MDcyOTAucG5n?x-oss-process=image/format,png)

![image-20200514114624653](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxMTQ2MjQ2NTMucG5n?x-oss-process=image/format,png)

创建`Car`

```java
public class MainConfigOfLifeCycle {
    @Bean
    public Car car(){
        return new Car();
    }
}
```

![image-20200514114824116](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxMTQ4MjQxMTYucG5n?x-oss-process=image/format,png)



![image-20200514115444620](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxMTU0NDQ2MjAucG5n?x-oss-process=image/format,png)

通过调用`close()`关闭

![image-20200514115552183](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxMTU1NTIxODMucG5n?x-oss-process=image/format,png)

以上针对单实例对象

2.当改为多实例`Bean`当时候

![image-20200514120208416](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxMjAyMDg0MTYucG5n?x-oss-process=image/format,png)

 2.1 当获取的时候才会初始化

![image-20200514120255213](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxMjAyNTUyMTMucG5n?x-oss-process=image/format,png)

 2.2 容器关闭后不会进行销毁

![image-20200514120338201](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxMjAzMzgyMDEucG5n?x-oss-process=image/format,png)

```java
/* bean的生命周期：
*      bean创建---初始化---销毁的过程
* 容器管理bean的生命周期：
* 我们可以自定义初始化和销毁方法；容器在  bean进行到当前生命周期的时候调用我们自定义的初始化和销毁方式
*构造（对象创建）
*      单实例：在容器启动的时候创建对象
*      多实例：在每次获取的时候创建对象
*初始化：
*      对象创建完成，并赋值好，调用初始化方法。。。
*销毁：
*      单实例：容器关闭的时候
*      多实例：容器不会管理这个bean,容器不会调用销毁方法;需要手动调用


* 以上是指定初始化方法的途径之一1)、指定初始化和销毁方法：
*          指定init-method和destroy-method方法
*/
```

### 2.2-生命周期 InitializingBean和DisposableBean

![image-20200514122845407](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxMjI4NDU0MDcucG5n?x-oss-process=image/format,png)

![image-20200514122926120](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxMjI5MjYxMjAucG5n?x-oss-process=image/format,png)



![image-20200514132432275](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxMzI0MzIyNzUucG5n?x-oss-process=image/format,png)

![image-20200514132500329](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxMzI1MDAzMjkucG5n?x-oss-process=image/format,png)

![image-20200514132603373](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxMzI2MDMzNzMucG5n?x-oss-process=image/format,png)

**通过包扫描的方式进行注册，同时通过实现接口进行初始化和销毁**

```java
 //以上是指定初始化方法的途径之二2)、通过让Bean实现InitializingBean（定义初始化逻辑）
```

### 2.3-生命周期 @PostConstruct和@PreDestroy

![image-20200514132924546](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxMzI5MjQ1NDYucG5n?x-oss-process=image/format,png)

![image-20200514140351680](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNDAzNTE2ODAucG5n?x-oss-process=image/format,png)

![image-20200514140414660](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNDA0MTQ2NjAucG5n?x-oss-process=image/format,png)

这是`java`规范的注解，目前`java8`能用

![image-20200514141103750](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNDExMDM3NTAucG5n?x-oss-process=image/format,png)

```java
/* 以上是指定初始化方法的途径之三3)、可以使用JSR250：
*          @PostConstruct：在bean创建完成并且属性赋值完成：来执行初始化方法
*          @PreDestroy:在容器销毁bean之前通知我们进行清理工作
*/
```

### 2.4-生命周期 BeanPostProcessor（后置处理器）

![image-20200514141319899](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNDEzMTk4OTkucG5n?x-oss-process=image/format,png)

![image-20200514142626963](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNDI2MjY5NjMucG5n?x-oss-process=image/format,png)

先创建对象-》〉》〉初始化

![image-20200514142738648](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNDI3Mzg2NDgucG5n?x-oss-process=image/format,png)

```java
/* 以上是指定初始化方法的途径之四4)、BeanPostProcessor【interface】,bean的后置处理器：
*          在bean初始化前后进行一些处理工作：
*          postProcessBeforeInitialization：在初始化之前工作
*          postProcessAfterInitialization：在初始化之后工作
*/
```

#### 2.4.1-生命周期 BeanPostProcessor原理

打断点debug一下

![image-20200514143005184](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNDMwMDUxODQucG5n?x-oss-process=image/format,png)

1.查看调用方法栈，往上依次看

![image-20200514143549752](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNDM1NDk3NTIucG5n?x-oss-process=image/format,png)

![image-20200514143607149](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNDM2MDcxNDkucG5n?x-oss-process=image/format,png)

1. 

![image-20200514143632531](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNDM2MzI1MzEucG5n?x-oss-process=image/format,png)

1. 

![image-20200514144004605](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNDQwMDQ2MDUucG5n?x-oss-process=image/format,png)

1. 

![image-20200514144033011](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNDQwMzMwMTEucG5n?x-oss-process=image/format,png)

1. 

![image-20200514144215598](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNDQyMTU1OTgucG5n?x-oss-process=image/format,png)

1. 

![image-20200514144334348](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNDQzMzQzNDgucG5n?x-oss-process=image/format,png)

1. 

![image-20200514144411012](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNDQ0MTEwMTIucG5n?x-oss-process=image/format,png)

1. 

![image-20200514144446397](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNDQ0NDYzOTcucG5n?x-oss-process=image/format,png)

1. 

![image-20200514144618656](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNDQ2MTg2NTYucG5n?x-oss-process=image/format,png)

10.

![image-20200514144654708](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNDQ2NTQ3MDgucG5n?x-oss-process=image/format,png)

1. 来看怎么创建的

![image-20200514144722127](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNDQ3MjIxMjcucG5n?x-oss-process=image/format,png)

12.**创建好后准备初始化**

![image-20200514144918297](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNDQ5MTgyOTcucG5n?x-oss-process=image/format,png)

![image-20200514145300044](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNDUzMDAwNDQucG5n?x-oss-process=image/format,png)

13.原理体现的地方

![image-20200514145545659](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNDU1NDU2NTkucG5n?x-oss-process=image/format,png)

点进去看一下，里面的内容

![image-20200514145718974](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNDU3MTg5NzQucG5n?x-oss-process=image/format,png)

```java
applyBeanPostProcessorsAfterInitialization 就不看了，类似的
*          遍历得到容器中所有的BeanPostProcessor：挨个执行beforeInitialization
*          一单返回null,跳出for循环，不会执行后面单BeanPostProcessor
*          populateBean(beanName, mbd, instanceWrapper); 给bean进行属性赋值的
*
*       initializeBean:
*      {
*          wrappedBean = this.applyBeanPostProcessorsBeforeInitialization(bean, beanName);
*          this.invokeInitMethods(beanName, wrappedBean, mbd);
*          wrappedBean = this.applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
*      }
```

#### 2.4.2-生命周期 `spring`底层对`BeanPostProcessor`的使用

**我们来看一下`ApplicationContextAwareProcessor`**，实现的`BeanPostProcessor`

![image-20200514154132580](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNTQxMzI1ODAucG5n?x-oss-process=image/format,png)

![image-20200514154831407](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNTQ4MzE0MDcucG5n?x-oss-process=image/format,png)

其实看看之前写的`MyBeanPostProcessor`,也是实现了它的方法

![image-20200514160401922](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNjA0MDE5MjIucG5n?x-oss-process=image/format,png)

`ApplicationContextAwareProcessor`是封装好了这些实现

**再来看下`ApplicationContextAware`**

1.

![image-20200514154526246](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNTQ1MjYyNDYucG5n?x-oss-process=image/format,png)

继续看`ApplicationContextAwareProcessor`

![image-20200514160625684](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNjA2MjU2ODQucG5n?x-oss-process=image/format,png)

如果是就调用下面的 `invokeAwareInterfaces(bean)`;

点进去查看

![image-20200514160937349](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNjA5MzczNDkucG5n?x-oss-process=image/format,png)

来`debug`看一下

准备如下：
 ![image-20200514161116770](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNjExMTY3NzAucG5n?x-oss-process=image/format,png)

`Debug`:

![image-20200514171109848](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNzExMDk4NDgucG5n?x-oss-process=image/format,png)

**并且会把ioc容器传进来，怎么传进来呢？接下来根据方法栈来看下之前调用的**

1.在这里调用 **`postProcessBeforeInitialization`**

![image-20200514171332661](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNzEzMzI2NjEucG5n?x-oss-process=image/format,png)

2.

![image-20200514171415726](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNzE0MTU3MjYucG5n?x-oss-process=image/format,png)

3.

![image-20200514171447377](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNzE0NDczNzcucG5n?x-oss-process=image/format,png)

再来看看 **`BeanValidationPostProcessor`**

![image-20200514171639061](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNzE2MzkwNjEucG5n?x-oss-process=image/format,png)

该`PostProcessor`是用来做数据校验的，在web用的比较多，也是通过`BeanPostProcessor`接口实现的

再来看看 **`InitDestroyAnnotationBeanPostProcessor`**

![image-20200514173814349](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNzM4MTQzNDkucG5n?x-oss-process=image/format,png)

为什么`Dog`中标注了这样的注解它就知道在哪执行呢？

![image-20200514173955545](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNzM5NTU1NDUucG5n?x-oss-process=image/format,png)

我们打一个断点来看一下

![image-20200514174035231](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQxNzQwMzUyMzEucG5n?x-oss-process=image/format,png)

**出了个问题，init不执行（以后解决）**发现的问题为：
 **我还重写了个`BeanPostProcessor`的`postProcessBeforeInitialization`方法**

由下面这张图可以发现，`@PostConstruct`也是用`BeanPostProcessor`接口实现的，是`bean`初始化之前执行的

![image-20200514203711376](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQyMDM3MTEzNzYucG5n?x-oss-process=image/format,png)

这里重写了：返回了`null`，造成后面的`BeanPostProcessor`不执行

![image-20200514203748694](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTQyMDM3NDg2OTQucG5n?x-oss-process=image/format,png)

再来看看 **`AutowiredAnnotationBeanPostProcessor`**

`@Autowired`也是通过这个来注值的

```java
/* spring底层对BeanPostProcessor的使用
*          bean赋值、注入其他组件,@Autowired,生命周期注解功能,@Async,等等
*          都是用BeanPostProcessor来完成的
/
```

## 三、属性赋值

### 3.1-属性赋值 @Value

#### @Value进行属性赋值

@Value`有三种方式

1. 直接加基本数值`@Value("张三")`
2. 可以写`spel`表达式`@Value("#{20-2}")`   此结果就是18
3. 可以写`${}`，读取配置文件中的值  `@Value(${xxxx})`

#### xml中进行属性赋值

下面的属性赋值演示都是针对`xml`来说的：

![image-20200515133831844](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTUxMzM4MzE4NDQucG5n?x-oss-process=image/format,png)

![image-20200515133819235](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTUxMzM4MTkyMzUucG5n?x-oss-process=image/format,png)

并没有赋值，在以前的`beans.xml`文件中：

![image-20200515133857543](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTUxMzM4NTc1NDMucG5n?x-oss-process=image/format,png)

是通过以上这样的方式

**如果要在`beans.xml`中使用`${}`取`properties`中的值就要配上这个名称空间**

![image-20200515134521784](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTUxMzQ1MjE3ODQucG5n?x-oss-process=image/format,png)

并采用以下方式：

```java
ApplicationContext applicationContext2 = new ClassPathXmlApplicationContext("beans.xml");
```

但是启动会报错：

![image-20200515141220002](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTUxNDEyMjAwMDIucG5n?x-oss-process=image/format,png)

这是因为少了context相关的解析文件。

![image-20200515141321466](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTUxNDEzMjE0NjYucG5n?x-oss-process=image/format,png)

解决如下，在 `xsi:schemaLocation` 中添加：

![image-20200515141440673](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTUxNDE0NDA2NzMucG5n?x-oss-process=image/format,png)

1.

![image-20200515141625033](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTUxNDE2MjUwMzMucG5n?x-oss-process=image/format,png)

2.

![image-20200515141729519](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTUxNDE3Mjk1MTkucG5n?x-oss-process=image/format,png)

3.

![image-20200515141715575](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTUxNDE3MTU1NzUucG5n?x-oss-process=image/format,png)

### 3.2-属性赋值 @PropertySource加载外部配置文件

```java
@PropertySource() 	//属性的来源
```

![image-20200515135417654](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTUxMzU0MTc2NTQucG5n?x-oss-process=image/format,png)

1.

![image-20200515141917082](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTUxNDE5MTcwODIucG5n?x-oss-process=image/format,png)

2.

![image-20200515141937236](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTUxNDE5MzcyMzYucG5n?x-oss-process=image/format,png)

3.

![image-20200515142005301](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTUxNDIwMDUzMDEucG5n?x-oss-process=image/format,png)

**因为是运行时候的变量，所以还可以用`applicationContext.getEnvironment`**

![image-20200515142306675](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTUxNDIzMDY2NzUucG5n?x-oss-process=image/format,png)

也可用`PropertySources`,是一个可重复标注的注解

![image-20200515142409537](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTUxNDI0MDk1MzcucG5n?x-oss-process=image/format,png)

## 四、自动装配

### 4.1-自动装配：@Autowired & @Qualifier & @Primary

#### @Autowired注解

1.

![image-20200515153256213](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTUxNTMyNTYyMTMucG5n?x-oss-process=image/format,png)

2.

![image-20200515153418632](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTUxNTM0MTg2MzIucG5n?x-oss-process=image/format,png)

3.

![image-20200515153439198](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTUxNTM0MzkxOTgucG5n?x-oss-process=image/format,png)

4.

![image-20200515153521079](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTUxNTM1MjEwNzkucG5n?x-oss-process=image/format,png)

此外注意，在此版本中：

![image-20200515152416877](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTUxNTI0MTY4NzcucG5n?x-oss-process=image/format,png)

准备：

![image-20200515152514854](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTUxNTI1MTQ4NTQucG5n?x-oss-process=image/format,png)

两个`Dao`,通过`labe`的设置看注入的是哪一个。相同类型，一个叫`bookDao`,一个叫`bookDao2`

![image-20200515152623823](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTUxNTI2MjM4MjMucG5n?x-oss-process=image/format,png)

你可以看见第二个报错了，因为按照`BookDao.class`去找的

`@Autowired` 如果找到相同类型组件，就需要按照属性名去寻找，如：`@Autowired BookDao bookDao`;就是按照`bookDao`去寻找

我现在按照名字去找，可以发现这两个`BookDao`是不一样的，即`@Repository`与`@Bean`返回 注入的两者不一样

![image-20200515154741721](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTUxNTQ3NDE3MjEucG5n?x-oss-process=image/format,png)

虽然在`BookService`通过`@Autowired`默认将方法名作为`id`注入，但是我们可以通过`@Qualifier`去改变

#### @Qualifier注解

使用@Qualifier注解之后就装配了bookDao2名字的组件

![image-20200515155107498](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTUxNTUxMDc0OTgucG5n?x-oss-process=image/format,png)

另外：将`BookDao`的`@Repository`注释掉

![image-20200515155517873](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTUxNTU1MTc4NzMucG5n?x-oss-process=image/format,png)

![image-20200515155727062](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTUxNTU3MjcwNjIucG5n?x-oss-process=image/format,png)

此时相当于容器中没有任何一个`BookDao`

运行时会报错

![image-20200515155947706](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTUxNTU5NDc3MDYucG5n?x-oss-process=image/format,png)

看一下`@Autowired`,我们要达到没有该`Bean`就不注入的效果

1.

![image-20200515155558544](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTUxNTU1NTg1NDQucG5n?x-oss-process=image/format,png)

2.

![image-20200515160044106](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTUxNjAwNDQxMDYucG5n?x-oss-process=image/format,png)

此时`service`就正常了

#### @Primary注解

我们发现如果容器同一个类型要用多个就要写多次`@Qualifier,`那么可以选用`@Primary`,即让`spring`进行自动装配的时候，默认使用首选的`bean`

1.

![image-20200515160942701](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTUxNjA5NDI3MDEucG5n?x-oss-process=image/format,png)

2.

![image-20200515161053012](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTUxNjEwNTMwMTIucG5n?x-oss-process=image/format,png)

3.

![image-20200515161343000](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTUxNjEzNDMwMDAucG5n?x-oss-process=image/format,png)

**总结**

![image-20200515162356891](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTUxNjIzNTY4OTEucG5n?x-oss-process=image/format,png)

### 4.2-自动装配：JSR250-@Resource、JSR330-@Inject

#### @Resource注解

`@Resource`是默认按照属性的名称，所以默认装配的是名为bookDao的组件，而不是bookDao

![image-20200517172739380](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTcxNzI3MzkzODAucG5n?x-oss-process=image/format,png)

可以指定注入的组件的名称

![image-20200517173723812](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTcxNzM3MjM4MTIucG5n?x-oss-process=image/format,png)

#### @Inject注解

`@Inject`需要导入`maven`依赖

![image-20200517174006293](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTcxNzQwMDYyOTMucG5n?x-oss-process=image/format,png)

![image-20200517181450653](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTcxODE0NTA2NTMucG5n?x-oss-process=image/format,png)

![image-20200517181854598](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTcxODE4NTQ1OTgucG5n?x-oss-process=image/format,png)

### 4.3-自动装配：方法,构造器位置的自动装配 & Aware注入Spring底层组件 & 原理

`@Autowired：`

![image-20200517182013544](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTcxODIwMTM1NDQucG5n?x-oss-process=image/format,png)

#### 1. 标注在方法位置

![image-20200517201930625](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTcyMDE5MzA2MjUucG5n?x-oss-process=image/format,png)

给`Car`也加上`@Component`，通过配置类`@ComponentScan`扫描进去

![image-20200517202000244](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTcyMDIwMDAyNDQucG5n?x-oss-process=image/format,png)

![image-20200517202043578](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTcyMDIwNDM1NzgucG5n?x-oss-process=image/format,png)

#### 2. 标注在构造方法上

默认加载`ioc`容器中的组件，容器启动会调用无参构造器创建对象，再进行初始化赋值等操作

![image-20200517202624049](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTcyMDI2MjQwNDkucG5n?x-oss-process=image/format,png)

#### 3. 标注在参数上

加到参数上，效果也一样

![image-20200517203018066](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTcyMDMwMTgwNjYucG5n?x-oss-process=image/format,png)

![image-20200517203541214](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTcyMDM1NDEyMTQucG5n?x-oss-process=image/format,png)

如果只有一个有参构造器，`@Autowired`可以不用写

![image-20200517205922549](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTcyMDU5MjI1NDkucG5n?x-oss-process=image/format,png)

#### 4. 以`@Bean`的方式注入

准备

1.

![image-20200517211806836](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTcyMTE4MDY4MzYucG5n?x-oss-process=image/format,png)

2.

![image-20200517211929701](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTcyMTE5Mjk3MDEucG5n?x-oss-process=image/format,png)

![image-20200517212034370](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTcyMTIwMzQzNzAucG5n?x-oss-process=image/format,png)

#### **5. 注入spring提供的组件原理**

![image-20200517222851303](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTcyMjI4NTEzMDMucG5n?x-oss-process=image/format,png)

传进来这个`applicationContext`我们就能用，类似这样的有很多

总接口是`Aware`

![image-20200517222941756](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTcyMjI5NDE3NTYucG5n?x-oss-process=image/format,png)

找几个`Aware`来看一下

![image-20200517225059305](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTcyMjUwNTkzMDUucG5n?x-oss-process=image/format,png)

解析字符串的值

![image-20200517225212076](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTcyMjUyMTIwNzYucG5n?x-oss-process=image/format,png)

这些`Aware`都是由相应的`XXXAwareProcessor`来处理的

我们来看一下怎么将`applicationContext`注入进来的

打一个断点：

![image-20200517225638654](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTcyMjU2Mzg2NTQucG5n?x-oss-process=image/format,png)

![image-20200517225904728](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTcyMjU5MDQ3MjgucG5n?x-oss-process=image/format,png)

跟之前是类似的

总结一下

![image-20200518205313640](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTgyMDUzMTM2NDAucG5n?x-oss-process=image/format,png)

### 4.4-自动装配 @Profile 根据环境注册Bean

引入`c3p0`和`mysql-connector`

![image-20200518205759283](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTgyMDU3NTkyODMucG5n?x-oss-process=image/format,png)

配置`dbconfig.properties`

![image-20200518210459825](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTgyMTA0NTk4MjUucG5n?x-oss-process=image/format,png)

并加载`@PropertySource(“classpath:/dbconfig.properties”)`

1.来自`spring`的黑科技

![image-20200518210826772](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTgyMTA4MjY3NzIucG5n?x-oss-process=image/format,png)

2.另一种方式,`Aware`接口

![image-20200518211504970](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTgyMTE1MDQ5NzAucG5n?x-oss-process=image/format,png)

![image-20200518211734753](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTgyMTE3MzQ3NTMucG5n?x-oss-process=image/format,png)

3.那么来看看`@Profile`

![image-20200518212000740](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTgyMTIwMDA3NDAucG5n?x-oss-process=image/format,png)

```java
/* @description Profile:
*                  Spring为我们提供的可以根据当前环境，动态地激活和切换一些列组件的功能:
* 开发环境、测试环境、生产环境：
* 数据源：（/A）（/B）（/C）
* @Profile 指定组件在哪个环境下才能被注册到容器中。不指定的话，任何环境下都能注册这个组件
* 1）、加了环境表示的bean,只有这个环境被激活的时候才能被注册到容器中,默认是default环境
*/
```

![image-20200518212915954](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTgyMTI5MTU5NTQucG5n?x-oss-process=image/format,png)

默认是`"default"`,可以看见只有标了`"default"`才会被加入到容器中

那么怎么切换环境呢？

1. 最简单的方法，使用命令行参数

![image-20200518213755544](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTgyMTM3NTU1NDQucG5n?x-oss-process=image/format,png)

![image-20200518213944958](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTgyMTM5NDQ5NTgucG5n?x-oss-process=image/format,png)

2.代码的方式

针对于AnnotationConfigApplicationContext

![image-20200518214440915](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTgyMTQ0NDA5MTUucG5n?x-oss-process=image/format,png)

配置类一注册进来，容器就启动刷新了，环境还没有设置好

![image-20200518215001987](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTgyMTUwMDE5ODcucG5n?x-oss-process=image/format,png)

所以使用下面的方法进行配置

![image-20200518215437570](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTgyMTU0Mzc1NzAucG5n?x-oss-process=image/format,png)





@Profile如果写在类上，就代表整个类里面的内容是否会被加载

![image-20200518215740276](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9jZG4uanNkZWxpdnIubmV0L2doLzEzOTI1MTcxMzgvaW1nUmVwb3NpdG9yeUBtYXN0ZXIvaW1hZ2UtMjAyMDA1MTgyMTU3NDAyNzYucG5n?x-oss-process=image/format,png)

```java
/* @Profile 指定组件在哪个环境下才能被注册到容器中。不指定任何环境下都能注册这个组件
* 1）、加了环境表示的bean,只有这个环境被激活的时候才能被注册到容器中，默认是default环境，就是任何环境下都能注册这个组件
* 2) 、写在配置类上，只有指定的环境的时候，整个配置类里面的所有配置才能开始生效。默认是default环境，就是任何环境下都能注册这个组件
*/
```