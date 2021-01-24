前言：文中出现的示例代码地址为：[gitee代码地址](https://gitee.com/gu_chun_bo/java-construct/tree/master/jvm%E5%AD%A6%E4%B9%A0/jvm)

## 1. 类的生命周期

#### 1.1生命周期详解

生命周期中的类加载阶段可细分为类型的加载，连接，和初始化三个阶段。在java代码中，类型的加载，类型的连接和初始化过程都是在**程序运行期间**完成的，这使代码提供了更多的可能性(比如可以动态生成类呀)。

- 类型的加载，在这个阶段，虚拟机要完成三个步骤

  - 1.通过一个类的全限定名来获取定义此类的二进制字节流 （读取.class文件） 2.将这个字节流所代表的静态储存结构转化为方法区运行时数据结构 3. 在内存中创建一个代表这个类的java.lang.Class对象，Class对象封装了类在方法区的数据结构（虚拟机规范并没有说明Class对象放在哪里，HotSpot虚拟机将放在了方法区中，Class对象包含了类相关的许多信息），作为方法区对这个类的各种数据的访问入口。(不懂的什么是方法区小伙伴可以先记住这里的方法区就是虚拟机内存的一块区域而已)

  - #### 加载.class文件的方式

    - 从本地系统中加载
    - 从网络下载
    - 从zip jar 等归档文件中加载
    - 从专有的数据库中
    - **将java文件动态编译为.class文件，使用此方法最多的就是动态代理技术**

- 类型的连接，连接就是将已经读到内存中类的二进制数据合并到虚拟机的运行时环境中去。分成三个阶段，第一阶段是验证，验证类的正确性，第二阶段是准备，为类的**静态变量**分配内存，并依次执行自上而下将其初始化为默认值，基本类型如int为0，引用类型为null，第三阶段是解析，在类型的常量池中寻找类和接口和字段和方法的符号引用，把这些的符号转换为直接引用(其实就是转换为地址)

  - 类的验证包括 1）类文件的结构检查2）语义检查3）字节码验证4）二进制兼容性的验证

- 初始化，对类里面**静态变量**赋予正确的初始值(就是程序员显示赋予的值)，且类只会被初始化一次(classloadTest10.java)

  - （1）假如这个类还没有被加载和连接，那就先进行加载和连接
  - （2）假如类存在直接父类，并且这个父类还没有被初始化，那就先初始化直接父类(classloadTest1.java)
  - （3）假如类中存在初始化语句，包括静态变量声明语句和静态代码块，那就依次执行**自上而下**这些初始化语句(classloadTest6.java)
    当java虚拟机初始化一个类时，要求它的所有父类都已经被初始化，**但是这条规则不适用于接口**。因此，一个父接口并不会因为它的子接口或者实现类的初始化而初始化。**只有当程序首次使用特定的接口的静态变量时**，才会导致该接口的初始化。（classloadTest5.java）

- 使用

  - ## 类的使用

    #### 主动使用

    - 创建类的实例

    - 访问某个类或者接口的静态变量，或者对该静态变量赋值(classloadTest1.java)

    - 调用该类的静态方法(如果该静态方法定义在父类上，但是是使用子类调用的，被主动使用类是父类而不是子类)

    - 反射Class.forName方法（classloadTest12.java）

    - 初始化一个类的子类(classloadTest1.java)

    - java虚拟机启动时被标记为启动类的类

      除了主动使用剩下的都是被动使用，即其它使用java类的方式都不会导致类的初始化。**因为所有的java虚拟机实现必须在每个类或者接口被java程序主动使用时才初始化它们** 

    #### **被动使用**
    
    - 除了主动使用，剩下的都是被动使用
    - 新建数组对象不是对数组包含的元素的主动使用，因为对于数组实例来说，其Class对象是由jvm在运行期动态生成的，它并没有触发它包含的元素所属类型的初始化，但是触发的是一个名为class [Lcom.gcb.jvm.classload.Parent4 类的初始化，这不是一个合法的类名，这就是jvm动态生成的类的类名(classloadTest4.java)
    - 引用在编译期可以知道的类常量也不是主动使用，因为类将编译期就可以确定的常量放进了常量池中(classloadTest2.java, classloadTest3.java)
    - 通过子类引用父类的静态变量字段不是对子类的主动使用，而是对父类的主动使用。也就是说只有当程序访问的静态变量或者静态方法确实在当前类或当前接口时，才可以认为是对类的主动使用（主动使用和被动使用都做了实验验证，可以看代码文档(classloadTest1.java)）

- 卸载

#### 1.2 生命周期图

![img](https://my-blog-to-use.oss-cn-beijing.aliyuncs.com/2019-11/类加载过程-完善.png)

![1582708437476](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304115330-815834.png)

#### 1.3 生命周期时序图

![1581513570895](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304115323-982445.png)

#### 1.4 什么是类型

指的是具体地class，类或者接口

## 2. 类的加载器

#### 2.1 java虚拟机自带的加载器

- 根类加载器(Bootstrap)
- 拓展类加载器(Extemnsion)
- 系统类加载器(System)：加载classpath或者java.lang.path下的类，一般来说就由系统类加载我们写的类
- 以上几个加载器呈父子关系（classloadTest14.java）
  - ![1581559224452](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304115122-567156.png)
- ![1581517657115](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304115124-502372.png)
  - Bootstrap classloader 启动类加载器：加载的是$JAVE_HOME(就是jdk的目录)中jre/lib/rt.jar 或者系统属性sun.boot.class.path所指定的目录里所有的class（即我们jdk常有的一些类如Object类），由c++实现，不是classloader的子类
  - Extension classloader 拓展类加载器：负责加载java平台中拓展功能的一些jar包，包括$JAVE_HOME中jre/lib/*.jar 或 系统属性 java.ext.dirs 指定目录下的jar包(classloadTest19.java)
  - System classloader 系统类加载器： 负责加载classpath目录下的class 和系统属性java.class.path所指定目录的jar包（比如我们自己写的类）(classloadTest7.java) 
  - 加入将自己的.class文件打包成jar包放入上面的系统属性所指定的目录中，可以发现我们自定义的类也是可以被加载的！(classloadTest22.java)。但是如果将sun.boot.class.paht参数改为其它目录，那么将无法加载Object类，然而java所有的类都依赖于Object类，就会导致加载失败。
  - 那么类加载器是谁加载的呢？内建于java平台中的启动类加载器会加载java.lang.classloader类和其他的java平台类(即jre正常运行所需的基本组件，包括java.util 和java.lang包等等)。当jvm启动时，一块特殊的C++机器码会执行，它会加载拓展类加载器与系统类加载器（这两个加载器定义在Launcher类中，是Launcher的内部类，而跟类加载器相关的操作的定义在ClassLoader类中），这块特殊的机器码叫做启动类加载器。启动类加载器不是java类，而其它的类加载器是java类。(classloadTest23.java)
  - 实验代码在classloadTest18.java



#### 2.2 用户自定义加载器

- java.lang.ClassLoader的子类
- 自定义类加载器的一般操作是用户可以自定义类的加载方式，一般仅此而已，而不是说要重写很多方法什么的。

类加载器并不需要等到某个类被“首次主动使用”时再去加载它，jvm规范允许类加载器在预料某个类将要被使用时就预先加载它，如果在预先加载的过程中遇到了.class文件缺失或者存在错误，那么类加载器将在程序首次主动使用该类时才报告错误（LingkageError错误），如果一直都没有被程序主动使用，那么类加载器就不会报告错误。

#### 2.3 获取classloader的途径

![1581581884052](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304115144-297217.png)

- 1.当前上下文的classloader
  - getContextClassLoader 的javadoc文档如下：上下文类加载器由线程的创建者提供，供此线程中运行的代码在加载类和资源时使用。如果未设置，则默认为父线程的上下文类加载器。原始线程的上下文类加载器通常设置为用于加载应用程序的类加载器(即system classloader)。
  - ClassLoader contextClassLoader  = Thread.currentThread().getContextClassLoader();
- 2.获取当前类的classloader
  - Class<classloadTest13> classloadTest13Class = classloadTest13.class;
    ClassLoader classLoader = classloadTest13Class.getClassLoader();
- 3.获取系统的classloader
  - getSystemClassLoader 的javadoc文档如下：返回用于系统类加载器。这是新类加载器实例的默认委托双亲，是默认用于启动应用程序的类加载器。此方法首先在运行时的启动序列的早期调用，此时它将创建系统类加载器并将其设置为调用此方法的线程的上下文类加载器。如果在首次调用此方法时定义了系统属性“java.system.class.loader”(该系统属性可以指定一个自定义类加载器的类名)，则该属性的值将被视为将作为系统类加载器返回的类的名称，该系统属性指定的类变成系统类加载器。该系统属性指定的类将默认使用系统类加载器加载(这里不懂呀，原文是The class is loaded using the default system class loader and must define a public constructor that takes a single parameter of type ClassLoader which is used as the delegation parent.这里的The class 不知道指的是什么)，并且必须定义一个公共构造函数，该构造函数接受一个Classloader 类型的参数，此Classloader 作为此类的父加载器。然后使用此构造函数创建一个实例，并使用默认的系统类加载器作为参数。生成的类加载器被定义为系统类加载器。(classloadTest23.java)
  - ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
- 注意：类加载器加载的是.class文件生成对应类的Class对象，加载的作用就是将 .class文件加载到内存。

实验代码（classloadTest13.java, classloadTest12.java）

#### 2.4 Classloader 类的javadoc文档

A class loader is an object that is responsible for loading classes. The class ClassLoader is an abstract class. Given the binary name of a class, a class loader should attempt to locate or generate data that constitutes a definition for the class. A typical strategy is to transform the name into a file name and then read a "class file" of that name from a file system.
Every Class object contains a reference to the ClassLoader that defined it.
Class objects for array classes are not created by class loaders, but are created automatically as required by the Java runtime. The class loader for an array class, as returned by Class.getClassLoader() is the same as the class loader for its element type; if the element type is a primitive type, then the array class has no class loader.

The ClassLoader class uses a delegation model to search for classes and resources. Each instance of ClassLoader has an associated parent class loader. When requested to find a class or resource, a ClassLoader instance will delegate the search for the class or resource to its parent class loader before attempting to find the class or resource itself. The virtual machine's built-in class loader, called the "bootstrap class loader", does not itself have a parent but may serve as the parent of a ClassLoader instance.
Class loaders that support concurrent loading of classes are known as parallel capable class loaders and are required to register themselves at their class initialization time by invoking the ClassLoader.registerAsParallelCapable method. Note that the ClassLoader class is registered as parallel capable by default. However, its subclasses still need to register themselves if they are parallel capable. In environments in which the delegation model is not strictly hierarchical, class loaders need to be parallel capable, otherwise class loading can lead to deadlocks because the loader lock is held for the duration of the class loading process (see loadClass methods).

Normally, the Java virtual machine loads classes from the local file system in a platform-dependent manner. For example, on UNIX systems, the virtual machine loads classes from the directory defined by the CLASSPATH environment variable.
However, some classes may not originate from a file; they may originate from other sources, such as the network, or they could be constructed by an application. The method defineClass converts an array of bytes into an instance of class Class. Instances of this newly defined class can be created using Class.newInstance.

The methods and constructors of objects created by a class loader may reference other classes. To determine the class(es) referred to, the Java virtual machine invokes the loadClass method of the class loader that originally created the class.
For example, an application could create a network class loader to download class files from a server. Sample code might look like:
     ClassLoader loader = new NetworkClassLoader(host, port);
     Object main = loader.loadClass("Main", true).newInstance();
          . . .

The network class loader subclass must define the methods findClass and loadClassData to load a class from the network. Once it has downloaded the bytes that make up the class, it should use the method defineClass to create a class instance. A sample implementation is:

```java
class NetworkClassLoader extends ClassLoader {
        String host;
        int port;
       public Class findClass(String name) {
           byte[] b = loadClassData(name);
           return defineClass(name, b, 0, b.length);
       }
  
       private byte[] loadClassData(String name) {
           // load the class data from the connection
            . . .
       }
   }
```

类加载器是负责加载类的。类加载器是一个抽象类。给定类的二进制名称，类加载器应尝试查找或生成构成类定义的数据。典型的一种情况是是将名称转换为文件名，然后从文件系统中读取该名称的“.class文件”。

每个Class类对象都包含对定义它的类加载器的引用。（就是Class类中包含getClassLoader() 方法，用来获取类加载器）

**数组类的Class对象不是由类加载器创建的，而是根据Java运行时的需要自动创建的（注释：你看，这里就官方说明了数组类的实例化原理）。**方法getClassLoader（）返回的数组类的类加载器与元素类型的类加载器相同；如果元素类型是基元类型，则数组类没有类加载器。

**类加载器的父类的关系**，类加载器类使用委托模型来搜索类和资源。类加载器的每个实例都有一个关联的父类加载器。当请求查找类或资源时，类加载器实例将在试图查找类或资源本身之前，将对该类或资源的搜索委托给其父类加载器。虚拟机的内置类加载器称为“引导类加载器”，它本身没有父类，但可以用作类加载器实例的父类。支持并发加载类的类加载器称为支持并行加载的类加载器，需要在类初始化时通过调用ClassLoader.registeraspallelable方法来注册它们自己。注意，默认情况下ClassLoader类注册为parallel-capable。但是，如果ClassLoader子类具有并行能力，则仍需要注册它们自己。在委托模型不是严格分层的环境中，类加载器需要具有并行能力，否则类加载可能导致死锁，因为加载器锁在类加载过程的持续时间内被一直持有（请参阅loadClass方法）。

**类加载器加载类的过程**，通常，Java虚拟机以依赖于平台的方式从本地文件系统加载类。例如，在UNIX系统上，虚拟机从由CLASSPATH环境变量定义的目录加载类。但是，有些类可能不是源于文件，它们可能源于其他源，例如网络，或者可以由应用程序构造。取得字节数组之后，defineClass方法将字节数组转换为Class类的实例。可以使用Cass.newInstance创建这个新定义类的实例。

类加载器创建的对象的方法和构造函数可以引用其他类。为了确定引用的类，Java虚拟机调用最初创建该类的类加载器的loadClass方法。例如，应用程序可以创建一个网络类加载器来从服务器下载类文件。示例代码可能如下所示：

```java
     ClassLoader loader = new NetworkClassLoader(host, port);
     Object main = loader.loadClass("Main", true).newInstance();
     . . .
```

网络类加载器子类必须定义findClass和loadClassData方法才能从网络加载类。一旦下载了构成类的字节，它就应该使用defineClass方法创建一个Class 实例。示例实现是：

```java
       class NetworkClassLoader extends ClassLoader {
           String host;
           int port;
  
           public Class findClass(String name) {
               byte[] b = loadClassData(name);
               return defineClass(name, b, 0, b.length);
           }
  
           private byte[] loadClassData(String name) {
               // load the class data from the connection
                . . .
           }
       }
```

#### 2.5 自定义类加载器

继承ClassLoader类，只需要重写findClass方法，findClass方法返回一个Class对象，findClass里面真正起作用的是defineClass方法，关于fingClass()方法和defineClass()方法的javadoc文档如下：

- findclass方法的javadoc文档：
  查找具有指定二进制名称的类。此方法应被自定义的类加载器实现重写，
  并将在检查父类加载器以获取请求的类后由loadClass方法调用。默认实现引发ClassNotFoundException。
- findclass方法里面会调用defineClass方法，defineClass方法的javadoc文档为：
  将字节数组转换为Class类的实例。在Clsss类可以使用之前，必须对其进行解析。
  方法返回结果：从指定的类数据创建的Class对象。

自定义加载器在双亲委托机制的作用下，只用父加载器(默认是系统加载器)全都无法加载时才会使用自定义加载器加载类。自定义的类加载器结构如下(具体代码在classloadTest16.java的模块一)

```java
package com.gcb.jvm.classload;

import java.io.*;

/**
 * 自定义类加载器
 */
public class classloadTest16 extends ClassLoader {
    private String classLoaderName;
    private final String fileExtension = ".class";

    private String path ;
    public void setPath(String path) {
        this.path = path;
    }

    public classloadTest16(String classLoaderName){
        super();  // 调用父类的构造方法，父类的构造方法中设置了类的委托双亲
        this.classLoaderName = classLoaderName;
    }

    public classloadTest16(ClassLoader parent, String classLoaderName){
        super(parent);  // 手动设置委托双亲
        this.classLoaderName = classLoaderName;
    }

    /**
     * 此方法被findclass方法调用
     * @param name
     * @return
     */
    private byte[] loadClassData(String name ){
        InputStream is = null;
        byte[] data = null;
 		// .... 读取到class文件的字节流
        return data;
    }

    @Override
    public Class<?> findClass(String name) {
        System.out.println("正在使用自定义的类加载器进行加载类");
        byte[] b = loadClassData(name);
        return this.defineClass(name, b, 0, b.length);
    }

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        //测试代码
        classloadTest16 loader1 = new classloadTest16("loader1");
        loader1.setPath("C:\\Users\\古春波\\Desktop\\test");
        // 此方法的底层会调用我自己编写的findClass方法
        Class<?> aClass = loader1.loadClass("com.gcb.jvm.classload.classloadTest1");
        Object instance = aClass.newInstance();
        System.out.println(instance);
        System.out.println(aClass.hashCode());
        System.out.println(aClass.getClassLoader());
    }
}

```





​     

#### 2.6 类加载器的(双亲委派机制)父亲委托机制

![1581517870929](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304115226-969443.png)

先自下而上检查是否已经加载，再自上而下尝试加载类。特别注意如下（此观点证明代码可参考classloadTest16.java）：

![1582079158522](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304115148-998718.png)

#### 2.7 双亲委托模型的好处

- 可以确保java核心库的类型安全，所有的java应用都至少引用java.lang.Object类，也就是说在运行期，java.lang.Object这个类会被加载到java虚拟机中，用户自定义的类加载器不可能加载由父加载器加载的Object类，从而保证不可靠代码甚至恶意代码代替父加载器加载的可靠代码。
- 可以确保java核心类型所提供的类不会被自定义的类所覆盖掉(因为会先向上查找)
- 不同的类加载器可以为相同名称的类创建额外的命名空间，相同名称的类可以并存在java虚拟机中，只要用不同的类加载器来加载他们即可，不同的类加载器之间是不兼容的，这相当于java虚拟机内部创建了一个又一个的相互隔离的java类空间，这类技术在很多框架中都得到了实际应用

  


#### 2.8 类加载器的命名空间

- 每个类加载器都有自己的命名空间，命名空间由该加载器及所有父加载器所加载的类组成（一个类加载器加载的类只供它自己和它的子加载器加载的类使用）。如果一个类加载器已经加载过A类那么此加载器和此加载器的子类都不会再重复加载A类(子加载器可以访问父类加载的类)，但是父加载器是看不到子加载器加载的类，察觉不到子类加载器加载生成的的Class对象, 所以如果在父加载器加载的类中引用子加载器加载的Class对象就会报错（classloadTest16.java ，classloadTest17_1.class）
  - 上面的话正式一点说法如下：子加载器的命名空间包含所有父加载器的命名空间，因此子加载器能看见父加载器加载的类，例如系统加载器加载的类能看见根加载器加载的类。由父加载器加载的类无法看到子加载器加载的类，如果两个加载器没有直接或者间接的父子关系，那么 它们各自加载的类互不可见(classloadTest21.java)
  - 在运行期，一个java类的是由该类的完全限定名（binary name 二进制名）和用于加载该类的类加载器所共同决定的，如果同样名字（即相同的完全限定名）的类是由不同的类加载器加载的，那么这些类就是不同的，即便.class文件的字节码完全一样，并且从相同的目录下加载的也是一样
- 在同一个命名空间中，不会出现类的完整名字（包括类的包名）相同的两个类
- 在不同的命名空间中，有可能会出现类的完整名字（包括类的包名）相同的两个类

#### **2.9 类的上下文类加载器**

- 首先要明确知道一个知识点：如果一个类有类加载器A加载，那么这个类的依赖类也是由相同的类加载器加载的(classloadTest17_1模块二)

类的上下文类加载器就是当前加载当前线程的类加载器。

在双亲委托模型下，类加载器是自下而上的，即下层加载器会委托上层进行加载，但是比如对于SPI（service provider interface）来说，有些接口是由java核心库提供的（比如jdbc包），而java核心库是由启动类加载器加载的，而这些接口的实现却来自于不同的jar包（厂商提供），java的启动类加载器是不会加载其它来源的jar包，这样传统的双亲委托模型就无法满足SPI的需求。而通过上下文类加载器，**就可以由设置的上下文类加载器来实现对于接口实现类的加载。**

类Thread中的getContextClassLoader和setContextClassLoader分别用来获取和设置上下文类加载器。如果没有通过setContextClassLoader设置上下文类加载器的话，线程将继承父线程的上下文加载器，(如果没有进行任何设置的话线程的默认加载器为系统类加载器)  java应用运行时初始线程的上下文加载器是系统类加载器。父ClassLoader加载的类中可以使用Thread.getContextClassLoader 获取指定的ClassLoader加载类(比如在由根加载器加载的ServiceLoader类中先通过Thread.getContextClassLoader获取ClassLoader再通过反射方法Class.forName(....) 将获取的ClassLoader指定为加载类的类加载器， 这样就成功用指定的类加载器加载了类！)，就这改变了父ClassLoader 加载的类无法使用子ClassLoader或者其它没有直接父子关系的ClassLoader加载的类的情况，即改变了双亲委托模型。(classloadTest26.java)



## 3. 类的卸载

- 当MySample类被加载连接和初始化后它的生命周期就开始了，当代表MySample类的Class对象不再被引用，即不可触及时，Class对象就会结束周期，MyClass对象在方法区的数据也会被卸载，从而结束MySample的生命周期。
- 一个类何时结束它的生命周期取决于代表它的Class对象何时结束生命周期。
- 由java虚拟机自带的类加载器所加载的类，在虚拟机的生命周期中，始终不会被卸载。前面已经介绍过，java虚拟机自带的加载器包括根类加载器，系统加载器，拓展类加载器。java虚拟机本身会引用这些类加载器，而这些类加载器则是始终会引用它们所加载的Class对象，因此这些Class对象始终是可触及的。因此说，由用户自定义的类加载器所加载的类是可以被卸载的。
- 在示例程序中，Sample类由loader1加载，在类加载器的内部实现中，用一个java集合来存放所加载类的引用。另一方面一个Class对象总是会引用它的类加载器，调用Class对象的getClassLoader()方法，就能获得它的类加载器对象，由此可见，Sample类的Class对象与类加载器Loader1之间是双向关联关系。
- 一个类的实例总是引用代表这个类的Class对象，在Object类中定义了getClass()方法，这个方法返回代表对象的所属类的Class对象的引用，而所有的java类都有一个静态属性class，它引用代表这个类的Class对象



