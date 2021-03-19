



前言：文中出现的示例代码地址为：[gitee代码地址](https://gitee.com/gu_chun_bo/java-construct/tree/master/jvm%E5%AD%A6%E4%B9%A0/jvm)

# 1. class字节码的结构

使用javap -verbose 命令分析一个.class字节码文件时(以下简称字节码文件)，将会分析该字节码文件的魔数，版本号，常量池，类信息，类的构造方法，类中的方法信息，类变量与实例变量等信息。字节码文件是十六进制的数字，两个十六进制数的大小就是一个字节。

- intellij插件字节码查看工具：jclasslib http://github.com/ingokegel/jclasslib
- 利用命令 javap -verbose + 文件名对字节码文件进行反编译
- winhex软件 :用于查看十六进制数对应的ASCII值

## 1.1 java字节码整体结构

- 字节码文件的整体结构如下，其中的类型就是指数据类型。看不懂没关系，下面将对每一个组成部分进行细致的分析。

- ![1582167723572](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304120209-463078.png)



## 1.2 Class文件的数据类型

- 基本数据类型，即字节数组直接量，这是基本数据类型。共细分为u1，u2，u4，u8 共四种，分别代表连续的1个字节，2个字节，4个字节，8个字节组成的整体数据
- 表（也可叫数组），除了基本数据类型就是表类型，如下面介绍的常量池和上一张图中的field_info,cp_info等，表使用多个基本数据类型和其它表，按照既定顺序组成的大的数据集合。它的结构体现在：组成表的成分所在的位置和顺序都是完全定义好的。



## 1.3 魔数

  描述： U4类型，所有的字节码文件的前四个字节都是魔数，魔数的值是固定的，为：0xCAFEBABE。这是人家规定的，别问为什么。

## 1.4 版本号

描述：魔数之后的四个字节表示jdk版本号，前两个字节表示U2类型的次版本号（mihor version）后两个字节表示U2类型的主版本号（major version）。这里的为 00 00 00 34 ，换算成十进制，表示次版本号为0，主版本号为18。所以该文件的版本号为1.8.0 。

## 1.5 常量池表(constant pool)

描述：表类型，紧接着主版本号之后的就是常量池入口，一个java类定义的很多信息都是由常量池进行维护和描述的，可以将常量池看作Class文件的资源仓库。比如说Java类中定义的方法和变量信息都是存储在常量池中的。常量池主要保存两类常量：字面量和符号引用。字面量如文本字符串，java中声明为final的常量值，基本数据类型的值等。符号引用如**类和接口的全局限定名，字段的名称和描述符(什么是描述符？下面有讲到)，方法和接口的名称和描述符等**。

### 1.5.1 常量池表的总体结构

java类所对应的常量池主要由常量池数量和常量池数组（也称常量表，以下混用）这两部分共同构成。常量池数量紧跟在主版本号后面，占据两个字节。常量池数组则紧跟在常量池数量后面。常量池数组和一般的数组是不同的。常量池数组中的不同元素的类型，结构都是不相同的，长度也当然不相同，但是每一种元素的第一个数据都是一个u1类型，占据一个字节，该字节是一个标志位。jvm在解析常量池时，就会根据这个u1类型来获取元素的具体类型。值得注意的是，常量池数组中元素的个数 =  常量池数量 -1 （其中0暂时不用）。其根本原因在于，索引为0也是一个常量（是一个保留常量），只不过它不位于常量表中，这个常量就对应null值。所以常量池表索引从1开始而非从0开始。

- 下表描述了常量池表中的11种数据类型，在jdk1.7之后又增加了3种跟动态代理相关的类型，这样一共是14种。
- ![1582162861696](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304120214-401326.png)

### 1.5.2 描述符

- 在JVM规范中，每个变量/字段都有描述信息，描述信息的主要作用是描述**字段的数据类型**，**方法**的参数列表（包括数量，类型与顺序），与返回值。根据描述符规范，基本数据类型和代表无返回值的void类型都用一个大写字符表示，对象类型则使用字符加对象的全类限定名来表示。为了压缩字节码文件的体积，对于基本数据类型，JVM都只使用一个大写字母来表示，如下所示： B - byte , C -  char , D - double , F - float , I - int , J - long , S - short , Z - boolean, V - void ，L - 对象类型 如 Ljava/lang/String 
- 对于数组类型来说每个维度使用一个前置的[来表示，如int[]被记录为 [I , String[] [] 被记录为 [[Ljava/lang/String
- 用描述符描述方法时，按照先参数列表  ，后返回值的顺序来描述。参数列表按照参数的严格顺序被放在一组（）之内，如方法 String getRealName(int id , String name )  被描述为： (I , Ljava/lang/String)Ljava/lang/String ;

### 1.5.3 案例分析

编译一个MyTest1.java程序得到字节码文件。

- ```java
  public class MyTest1 {
      private int a = 1;
      public int getA() {
          return a;
      }
      public void setA(int a) {
          this.a = a;
      }
  }
  ```

- 以下结果是MyTest1.class的字节码文件根据上图常量池表数据结构的对应关系的翻译过来得到的，加了括号的是我自己添加上的注释。

- ```java
  Constant pool:
     #1 = Methodref          #4.#20 // java/lang/Object."<init>":()V
        							//（这是父类构造方法，声明构造方法的类描述符+ 名称 + 描述符）
     #2 = Fieldref           #3.#21 // com/gcb/jvm/bytecodetest/MyTest1.a:I  
         							// (声明字段的的类描述符+ 名称 + 描述符)
     #3 = Class              #22   // com/gcb/jvm/bytecodetest/MyTest1
         							//(类或接口的全局限定名)
     #4 = Class              #23   // java/lang/Object   
         							//(类或接口的全局限定名)
     #5 = Utf8               a
     #6 = Utf8               I
     #7 = Utf8               <init>
     #8 = Utf8               ()V   // （方法的描述符，方法返回类型为void）	
     #9 = Utf8               Code
    #10 = Utf8               LineNumberTable
    #11 = Utf8               LocalVariableTable
    #12 = Utf8               this
    #13 = Utf8               Lcom/gcb/jvm/bytecodetest/MyTest1;
    #14 = Utf8               getA   //  （方法名）
    #15 = Utf8               ()I	 // （方法的描述符，方法返回类型为int）	
    #16 = Utf8               setA    //  （方法名）
    #17 = Utf8               (I)V   // （方法的描述符，方法参数为int，方法返回类型为void）	
    #18 = Utf8               SourceFile
    #19 = Utf8               MyTest1.java   // （18，19描述了文件是由什么文件编译出来的）
    #20 = NameAndType        #7:#8          // "<init>":()V
    									//(方法的名称 + 描述符，名称是"<init>",描述符是()V)
    #21 = NameAndType        #5:#6          // a:I
    									// (字段的名称 + 描述符，名称是a,描述符是I)
    #22 = Utf8               com/gcb/jvm/bytecodetest/MyTest1  // （类完全限定名）
    #23 = Utf8               java/lang/Object    // （类完全限定名）

  ```
  
  

## 1.6 类的访问控制权限(access_flags)

![1582172615736](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304120544-233191.png)

描述： U2类型，访问表示信息包括该Class文件是类还是接口，是否被定义为public ，是否是abstract ，如果是类，是否被声明为final。并且在字节码中，如果一个类是public 和 final ，它的字节码是 0001 + 0010 = 0011 。   通过上面的源代码，我们应该知道是类并且是public。

![1582171506881](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304120223-557162.png) 

## 1.7 类名和父类名接口个数和接口名

![1582207491970](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304120335-889726.png)

如上所示，类名，父类名，接口名都是U2类型两个字节，并且代表指向常量池的索引（下文说的索引一般也指指向常量池的索引）。如果接口数为零，则没有字节码是代表接口名的（感觉有点罗嗦了，哈哈哈，因为这很符合常识啊！）

## 1.8 域表(fields)

![1582172648963](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304120351-825375.png)

域表用于描述类和接口中声明的变量。这里的字段包含了类级别的变量，以及实例变量，但是不包括方法内部声明的局部变量。关于域的描述如上所示，包括域的个数和域的表。其中域的表结构信息如下，表中的索引是指向常量池当中的。

![1582172527438](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304120408-720977.png)

## 1.9 方法表(methods)

![1582185114086](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304120411-370598.png)

关于方法的描述如上所示包括方法的个数和方法表，其中方法表的结构如下，表中的索引是指向常量池当中的。

![1582185823776](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304120416-73254.png)

方法表结构中的attributes 又是一个复合类型，attributes 的结构信息参考下面的附加属性表。 在JVM中预定了部分的attribute，但是编译器自己也可以实现自己的attribute写入class文件中，供运行时使用。不同的attribute通过attribute_name_index来区分。

![1582186340273](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304120418-25918.png)

attributes 中的其中一个attribute 叫做Code attribute ，是我们要重点研究的对象，它的作用是保存该方法的结构，它的结构如下（其实它的结构是attributes表的进一步展开时的结构）

![1582201739954](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304120424-95967.png)

Code attribute 的结构信息详解

- attribute_length 表示attribute所包含的字节数，不包含attribute_name_index 和 attribute_length 字段。
- max_stack 表示这个方法运行的任何时刻所能达到的操作数栈(此名词解释查看下面栈帧的名词解释)的最大深度
- max_locals 表示方法执行期间创建的局部变量的数目，包含用来表示传入的参数的局部变量
  - 在局部变量表中，至少有一个指向当前对象的局部变量this，具体原因下面有解释。
- code_length 表示该方法所包含的指令码码的数量，code_length 后面code_length长度的字节码代表具体的指令码，具体的指令码是指该方法被调用时，虚拟机的行为，每一个十六进制的字节码都对应一个指定的指令码。示例如下图所示，并且可以发现，1.在字节码文件中多出了自动生成的名字叫做init的方法，这就是我们的构造方法，并且在这个构造方法完成对实例变量的赋值，这里是我们平常不知道的，谁能想到实例变量是在构造方法里面赋值的呢，因为我们写代码的时候实例变量的赋值都可以不在构造方法里啊，所以说叫做“构造方法”。2.所有的静态变量的赋值和静态代码块都是合并到是在一个clinit方法里面进行的。 MyTest2.java
  - ![1582273306749](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304120428-599977.png)
  - 看下面的内容之前，需要清楚的前置知识点一：什么是符号引用：符号引用以一组符号来描述所引用的目标。符号引用可以是任何形式的字面量，只要使用时能无歧义地定位到目标即可，符号引用和虚拟机的布局无关。（在编译的时候一个每个java类都会被编译成一个class文件，但在编译的时候虚拟机并不知道所引用类的地址，多以就用符号引用来代替，而在这个解析阶段就是为了把部分可以确定地符号引用转化成为真正的地址的阶段。）什么是直接引用？直接引用：（1）直接指向目标的指针（指向对象，类变量和类方法的指针）（2）相对偏移量。（指向实例的变量或方法的指针）（3）一个间接定位到对象的句柄。
  - 二：符号引用如何转换为直接引用（即地址） ？ 有些符号引用是在类加载信息阶就会转换为直接引用，这种转化叫做**静态解析**。另外一些符号引用则是在每次运行期（要运行时）才转换为直接引用，这种转换叫做**动态链接**。这体现为java的多态性。
  - 前置知识三：什么是栈帧？栈帧是一种帮助虚拟机执行方法调用和方法执行的数据结构栈帧本身是一种数据结构，封装了**方法的局部变量表，动态链接信息，方法的返回地址以及操作数栈信息**(如 3-1=2操作就是操作数栈中进行的)
  - 指令码中有执行方法的指令码，一共有五种，分别是
    - invokeiinterface ：调用接口中的方法，实际上是在运行期决定的，决定到底调用哪个方法
    - invokestatic ：调用静态方法
    - invokespecial：调用实例方法，实例方法包括自己的私有方法和(<init>)构造方法以及父类方法
    - invokevirtual ：调用虚方法，运行期动态查找确定。
    - iinvokedynamic：动态调用方法
    - **静态解析**（即在编译阶段就可以确定具体方法的情况一共有四种，叫做非虚方法）：分别是调用静态方法，调用私有方法（私有方法无法被重写，故可以唯一确定），调用构造方法，以及调用父类方法。它们是在类加载阶段就可以将符号引用转换为直接引用的。
  - 静态分派和动态分派的区别
    - 前置知识：randpa g1 = new Father();上代码，变量g1 的静态类型是 Grandpa ，而变量的实际类型（真正指向的类型）是Father。可以得出这样一个结论：变量的静态类型是不会发生变化的，而变量的实际类型是以发生变化的（多态的一种体现），实际类型是在运行期才可以确定。
    - 静态分派，方法的重载体现了静态分派，是一种静态行为，即重载方法只关系传递参数g1 和 g2 的静态类型，而不关心参数它们的实际类型。MyTest5.java
    - 动态分派，方法的重写体现了动态分派，动态分派涉及到一个概念：方法接受者（即方法的调用者），动态分派关系到invokevirtual字节码指令的查找流程，查找流程的调用过程是这样的**（我不太懂，感觉这里没有说清楚）**，找到操作数栈顶元素的实际类型，如果在常量池的这个实际类型中找到了方法描述符和方法名称都和要调用的方法完全相同的方法，并且具相应的访问权限。那么就返回这个方法的直接引用，如果没有找到，则沿着继承体系从下往上查找。动态分派在字节码中的展示状态，是静态类型去调用方法，在运行期动态确认它的实际类型。 MyTest6.java
    - 结论：方法重载是静态的，是编译期行为；方法重写是动态的，是运行期行为。
    - 什么是虚方法表(这个知识点只是一笔带过)，针对于方法调用动态分派的过程，虚拟机会在类的方法区建立一个虚方法表的数据结构，而针对invokeinterface指令来说，虚拟机会建立一个叫做接口方法表的数据结构。虚方法表是每个程序的入口地址，如果子类重写了方法，那么表中将指向此被重写过的方法，并且如果重写了父类的方法，那么子类和父类方法表中的索引是一样的，这样可以提高查找效率；如果没重写，则指向父类的方法，而不是将该方法再复制一份。
  - JVM执行指令时所采取的方式是基于栈的指令集。
    基于栈的指令集主要有入栈和出栈两种；
    基于栈的指令集的缺点在主完成相同的操作,指令集通常要比基于寄存器的指令集要多，
    指令集是在内存中完成操作的，而基于寄存器的指令集是直接由CPU来执行的,是在高速缓冲区中进行的,
    速度要快很多.虽然虚拟机可以采用一些优化手段,但总体来说,基于栈的指令集的执行速度要慢一些；
    基手栈的指令集的优势在于它可以在不同平台之间移植,而基于寄存器的指令集是与硬件架构累密关联的,无法做到可移植。java程序中的java操作数栈就是个很好的例子。示例在MyTest8.java 
- exception_table 这里存放的是处理异常的信息，每个exception_table表，是由start_pc、end_pc、hangder_pc、catch_type组成
  - start_pc、end_pc 表示从start_pc到 end_pc 的指令抛出的异常如果是在catch_type里面的一类那么就由有这个表项处理（表项指的就是hangder_pc）
  - hangder_pc：表示处理异常的代码的开始处。
  - catch_type：表示会被处理的异常类型，它指向常量池中的一个异常类。当catch_type=0时，表示处理所有的异常。
  - 在MyTest3.java 中可以看到具体的实例，FileNotFoundException异常就是处理0 - 26 的指令异常，处理代码从37开始。其它异常捕获以此类推。并且通过读指令可以发现，当异常处理存在finally语句时，现代化的jvm采用finally语句块的字节码拼接到每一个catch块的后面，换句话说，我们存在多少个catch块，就会在每个catch语句块块字节码后面重复多少个finally语句块的字节码。 部分指令和全部异常表如下
    - ![1582286615836](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304120433-575506.png)
    - ![1582289027099](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304120451-357281.png)
- attributes 附加属性表  注意：这是Code attribute 结构 里面的！
  - attributes 表的结构在请看下面的说明。
  - Code attribute 的结构中的附加属性表中有属性叫做LineNumbeTable_attribute ，此处它的表结构也是 attributes 表的一种，此处将它拓展出来为
    - ![1582199775565](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304120437-701121.png)
  - 附加属性表中还有个属性叫做LocalVariableTable（局部变量表 ） ：结构类似于 LineNumbeTable_attribute
    对于Java中的任何一个非静态方法，至少会有一个局部变量，就是this，并且对于每一个非静态方法，都有一个隐藏的参数，就是this，跟python里的self差不多，有了这个this，我们就可以在java的示例方法中使用this来去访问当前对象的属性和其它方法。这个操作是在编译期间完成的即由javac编译器在编译的时候j将对this的访问转化为对一个普通实例方法的访问，接下来在运行期间，由jvm在调用实例方法时，自动向实例方法传入this参数，所以在局部变量表中，至少有一个指向当前对象的局部变量。

## 1.10 附加属性表(attributes)

![1582185857335](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304120505-887928.png)

关于附加属性表的描述如上所示，包括附加属性表的个数和附加属性表, 其中附加属性表的结构如下所示，表中的索引是指向常量池当中的。

![1582185811139](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304120457-746460.png)



newProxyInstance()方法的javadoc 文档

`Returns an instance of a proxy class for the specified interfaces that dispatches method invocations to the specified invocation handler.`  返回指定接口的代理类（proxy class）的实例，该接口将方法调用分派给指定的调用处理程序(invocation handler)。

- @param      
  - loader–定义代理类（proxy class）的类装入器 
  - interface-代理类（proxy class）要实现的接口列表 
  - h–将方法调用分派到的调用处理程序 

- return  返回指定接口的代理类（proxy class）的实例，该接口将方法调用分派给指定的调用处理程序。