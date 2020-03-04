# java基础





## 类和对象

### java面向对象编程的三大特征

#### 封装

封装把一个对象的属性私有化，同时提供一些可以被外界访问的属性的方法，如果属性不想被外界访问，我们大可不必提供方法给外界访问。但是如果一个类没有提供给外界访问的方法，那么这个类也没有什么意义了。

#### 继承

继承是使用已存在的类的定义作为基础建立新类的技术，新类的定义可以增加新的数据或新的功能，也可以用父类的功能，但不能选择性地继承父类。通过使用继承我们能够非常方便地复用以前的代码。

**关于继承如下 3 点请记住：**

1. 子类拥有父类对象所有的属性和方法（包括私有属性和私有方法），但是父类中的私有属性和方法子类是无法访问，**只是拥有**。
2. 子类可以拥有自己属性和方法，即子类可以对父类进行扩展。

#### 多态

所谓多态就是指程序中定义的引用变量所指向的具体类型和通过该引用变量发出的方法调用在编程时并不确定，而是在程序运行期间才确定，即一个引用变量到底会指向哪个类的实例对象，该引用变量发出的方法调用到底是哪个类中实现的方法，必须在由程序运行期间才能决定。

在Java中有两种形式可以实现多态：继承（多个子类对同一方法的重写）和接口（实现接口并覆盖接口中同一方法）。



### 为什么 Java 中只有值传递？

**按值调用(call by value)表示方法接收的是调用者提供的值，而按引用调用（call by reference)表示方法接收的是调用者提供的变量地址。一个方法可以修改传递引用所对应的变量值，而不能修改传递值调用所对应的变量值。** 它用来描述各种程序设计语言（不只是 Java)中方法参数传递方式。**Java 程序设计语言总是采用按值调用。也就是说，方法得到的是所有参数值的一个拷贝，也就是说，方法不能修改传递给它的任何参数变量的内容。**

**下面通过 3 个例子来给大家说明**

#### **example 1**  基本数据类型

```java
public static void main(String[] args) {
    int num1 = 10;
    int num2 = 20;

    swap(num1, num2);

    System.out.println("num1 = " + num1);
    System.out.println("num2 = " + num2);
}

public static void swap(int a, int b) {
    int temp = a;
    a = b;
    b = temp;

    System.out.println("a = " + a);
    System.out.println("b = " + b);
}
```

**结果：**

```java
a = 20
b = 10
num1 = 10
num2 = 20
```

**解析：**

[![example 1 ](https://camo.githubusercontent.com/ab46506b1a5ce09a516051c35f981e55255337f9/687474703a2f2f6d792d626c6f672d746f2d7573652e6f73732d636e2d6265696a696e672e616c6979756e63732e636f6d2f31382d392d32372f32323139313334382e6a7067)](https://camo.githubusercontent.com/ab46506b1a5ce09a516051c35f981e55255337f9/687474703a2f2f6d792d626c6f672d746f2d7573652e6f73732d636e2d6265696a696e672e616c6979756e63732e636f6d2f31382d392d32372f32323139313334382e6a7067)

在 swap 方法中，a、b 的值进行交换，并不会影响到 num1、num2。因为，a、b 中的值，只是从 num1、num2 的复制过来的。也就是说，a、b 相当于 num1、num2 的副本，副本的内容无论怎么修改，都不会影响到原件本身。

**通过上面例子，我们已经知道了一个方法不能修改一个基本数据类型的参数，而对象引用作为参数就不一样，请看 example2.**

#### **example 2** 对象类型

```java
	public static void main(String[] args) {
		int[] arr = { 1, 2, 3, 4, 5 };
		System.out.println(arr[0]);
		change(arr);
		System.out.println(arr[0]);
	}

	public static void change(int[] array) {
		// 将数组的第一个元素变为0
		array[0] = 0;
	}
```

**结果：**

```
1
0
```

**解析：**

[![example 2](https://camo.githubusercontent.com/b7bad9506150c29bb8d7debd3905bd7a71cd6611/687474703a2f2f6d792d626c6f672d746f2d7573652e6f73732d636e2d6265696a696e672e616c6979756e63732e636f6d2f31382d392d32372f333832353230342e6a7067)](https://camo.githubusercontent.com/b7bad9506150c29bb8d7debd3905bd7a71cd6611/687474703a2f2f6d792d626c6f672d746f2d7573652e6f73732d636e2d6265696a696e672e616c6979756e63732e636f6d2f31382d392d32372f333832353230342e6a7067)

首先要知道，数组类型也是一个对象，array 是 arr 的拷贝也就是说array也是一个对象的引用，也就是说 array 和 arr 指向的是同一个数组对象。 因此，外部对引用对象的改变会反映到所对应的对象上。

![1583294988534](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304120949-623063.png)

**通过 上图 我们已经看到，实现一个改变对象类型参数状态的方法并不是一件难事。理由很简单，方法得到的是对象引用的拷贝，对象引用及其它的拷贝同时引用同一个对象。**

**很多程序设计语言（特别是，C++和  Pascal)提供了两种参数传递的方式：值调用和引用调用。有些程序员（甚至本书的作者）认为 Java  程序设计语言对对象采用的是引用调用，实际上，这种理解是不对的。由于这种误解具有一定的普遍性，所以下面给出一个反例来详细地阐述一下这个问题。**



#### **example 3**

```java
public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Student s1 = new Student("小张");
		Student s2 = new Student("小李");
		Test.swap(s1, s2);
		System.out.println("s1:" + s1.getName());
		System.out.println("s2:" + s2.getName());
	}

	public static void swap(Student x, Student y) {
		Student temp = x;
		x = y;
		y = temp;
		System.out.println("x:" + x.getName());
		System.out.println("y:" + y.getName());
	}
}
```

**结果：**

```java
x:小李
y:小张
s1:小张
s2:小李
```

**解析**：

**如果是按值传递，那么s1和s2所引用的对象应该被交换了，然而，方法并没有改变存储在变量 s1 和 s2 中的对象引用。swap 方法的参数 x 和 y 被初始化为两个对象引用的拷贝，这个方法交换的是这两个拷贝，在方法结束时参数变量X和y被丢弃了。原来的变量s1和s2仍然引用这个方法调用之前所引用的对象**

> **总结**

Java 程序设计语言对对象采用的不是引用调用，实际上，对象引用是按 值传递的。

下面再总结一下 Java 中方法参数的使用情况：

- 一个方法不能修改一个基本数据类型的参数（即数值型或布尔型）。
- 一个方法可以修改一个对象类型对的参数。
- 一个方法不能让对象参数引用一个新的对象。

**参考：**

《Java 核心技术卷 Ⅰ》基础知识第十版第四章 4.5 小节

### 访问权限控制符

四种访问权限，只有默认访问权限和public能够用来修饰类。修饰类的变量和方法四种权限都可以。

#### 1.修饰类

- 默认访问权限（包访问权限）：用来修饰类的话，表示该类只对同一个包中的其他类可见。
- public：用来修饰类的话，表示该类对其他所有的类都可见。

#### 2.修饰类的方法和变量

- 默认访问权限（包访问权限）：如果一个类的方法或变量被包访问权限修饰，也就意味着只能在同一个包中的其他类中显示地调用该类的方法或者变量，在不同包中的类中不能显示地调用该类的方法或变量。
- private：如果一个类的方法或者变量被private修饰，那么这个类的方法或者变量只能在该类本身中被访问，在类外以及其他类中都不能显示地进行访问。
- protected：如果一个类的方法或者变量被protected修饰，对于同一个包的类，这个类的方法或变量是可以被访问的。对于不同包的类，只有继承于该类的类才可以访问到该类的方法或者变量，**并且只有在继承该类的类中才可以访问！**
- public：被public修饰的方法或者变量，在任何地方都是可见的。

参考：https://www.cnblogs.com/dolphin0520/p/3734915.html





## 继承

### 重载和重写的区别

#### 重载

发生在同一个类中，方法名必须相同，参数类型不同、个数不同、顺序不同，方法返回值和访问修饰符可以不同。

下面是《Java核心技术》对重载这个概念的介绍：

![20200304121244-975223](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304160942-993615.png)

#### 重写

重写是子类对父类的允许访问的方法的实现过程进行重新编写

方法的重写（override）两同两小一大原则：

- 方法名相同，参数类型相同
- 子类返回类型小于等于父类方法返回类型
- 子类抛出异常小于等于父类方法抛出异常
- 子类访问权限大于等于父类方法访问权限

另外，如果父类方法访问修饰符为 private 则子类就不能重写该方法。**也就是说方法提供的行为改变，而方法的外貌并没有改变。**





### 类、超类和子类



### Object类

#### equals() 和 ==

**==** : **它的作用是判断两个对象的地址是不是相等。即，判断两个对象是不是同一个对象**,**关于equals() 和 ==记住此句话即可**(基本数据类型==比较的是值，引用数据类型==比较的是内存地址)。

**equals()** : **它的作用也是判断两个对象是否相等**。（下文中“相等”一词的意思与此处相同）但它一般有两种使用情况：

- 情况1：类没有覆盖 equals() 方法。则通过 equals() 比较该类的两个对象时，等价于通过“==”比较这两个对象。
- 情况2：类覆盖了 equals() 方法。一般，我们都覆盖 equals() 方法来比较两个对象的内容是否相等；若它们的内容相等，则返回 true (即，认为这两个对象相等)。

**举个例子：**

```java
public class test1 {
    public static void main(String[] args) {
        String a = new String("ab"); // a 为一个引用
        String b = new String("ab"); // b为另一个引用,对象的内容一样
        String aa = "ab"; // 放在常量池中
        String bb = "ab"; // 从常量池中查找
        if (aa == bb) // true
            System.out.println("aa==bb");
        if (a == b) // false，非同一对象
            System.out.println("a==b");
        if (a.equals(b)) // true
            System.out.println("aEQb");
        if (42 == 42.0) { // true
            System.out.println("true");
        }
    }
}
```

**说明：**

- String 中的 equals 方法是被重写过的，因为 object 的 equals 方法是比较的对象的内存地址，而 String 的 equals 方法比较的是对象的值。
-  采用  String aa = "ab" 方式 ，当创建 String 类型的对象时，虚拟机会在常量池中查找有没有已经存在的值和要创建的值相同的对象，如果有就把它赋给当前引用。如果没有就在常量池中重新创建一个 String 对象。



#### hashCode 与 equals (重要)

面试官可能会问你：“你重写过 hashcode 和 equals 么，为什么重写equals时必须重写hashCode方法？”

我们以“**类的用途**”来将“hashCode() 和 equals()的关系”分2种情况来说明。

 **第一种 不会创建“类对应的散列表”** 这里所说的“不会创建类对应的散列表”是说：我们不会在HashSet, Hashtable, HashMap等等这些本质是散列表的数据结构中，用到该类。例如，不会创建该类的HashSet集合。在这种情况下，该类的“hashCode() 和 equals() ”没有半毛钱关系的！这种情况下，equals() 用来比较该类的两个对象是否相等。而hashCode() 则根本没有任何作用，所以，不用理会hashCode()。

**第二种** **会创建“类对应的散列表，以下所有讨论都是建立在会创建散列表的前提下讨论的：”**

###### hashCode（）介绍

hashCode()  的作用是获取哈希码，也称为散列码；它实际上是返回一个int整数。这个哈希码的作用是确定该对象在哈希表中的索引位置。 虽然，每个Java类都包含hashCode() 函数。

我们都知道，散列表存储的是键值对(key-value)，它的特点是：能根据“键”快速的检索出对应的“值”。这其中就利用到了散列码！散列表的本质是通过数组实现的。当我们要获取散列表中的某个“值”时，实际上是要获取数组中的某个位置的元素。而数组的位置，就是通过“键”来获取的；更进一步说，数组的位置，是通过“键”对应的散列码计算得到的，下面将进一步详细说明hashCode的作用。

###### 为什么要有 hashCode

**我们先以“HashSet 如何检查重复”为例子来说明为什么要有 hashCode：**  当你把对象加入 HashSet 时，HashSet 会先计算对象的 hashcode 值来判断对象加入的位置，同时也会与其他已经加入的对象的  hashcode 值作比较，如果没有相符的hashcode，HashSet会假设对象没有重复出现。但是如果发现有相同 hashcode  值的对象，这时会调用 `equals()`方法来检查 hashcode  相等的对象是否真的相同。如果两者相同，HashSet  就不会让其加入操作成功。如果不同的话，就会重新散列到其他位置。（摘自我的Java启蒙书《Head first  java》第二版）。这样我们就大大减少了 equals 的次数，相应就大大提高了执行速度。如果 HashSet 在对比的时候，同样的 hashcode 有多个对象，它会使用 equals() 来判断是否真的相同。也就是说 hashcode 只是用来缩小查找成本。

通过我们可以看出：`hashCode()` 的作用就是获取哈希码，也称为散列码；它实际上是返回一个int整数。这个哈希码的作用是确定该对象在哈希表中的索引位置。hashCode()在散列表中才有用，在其它情况下没用。在散列表中hashCode() 的作用是获取对象的散列码，进而确定该对象在散列表中的位置。

###### 为什么重写equals时必须重写hashCode方法？

由上面的叙述可以知道如果不重写hashcode 方法，即使两个对象内容相等，那它们的hashCode()不等；所以，HashSet在添加两个相等元素的时候，认为它们不相等，导致HashSet中有重复元素，这是不行滴。实例代码如下

```java
import java.util.*;
import java.lang.Comparable;

/**
 * @desc 比较equals() 返回true 以及 返回false时， hashCode()的值。
 */
public class ConflictHashCodeTest1{

    public static void main(String[] args) {
        // 新建Person对象，
        Person p1 = new Person("eee", 100);
        Person p2 = new Person("eee", 100);
        Person p3 = new Person("aaa", 200);

        // 新建HashSet对象 
        HashSet set = new HashSet();
        set.add(p1);
        set.add(p2);
        set.add(p3);

        // 比较p1 和 p2， 并打印它们的hashCode()
        System.out.printf("p1.equals(p2) : %s; p1(%d) p2(%d)\n", p1.equals(p2), p1.hashCode(), p2.hashCode());
        // 打印set
        System.out.printf("set:%s\n", set);
    }

    /**
     * @desc Person类。
     */
    private static class Person {
        int age;
        String name;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String toString() {
            return "("+name + ", " +age+")";
        }

        /** 
         * @desc 覆盖equals方法 
         */  
        @Override
        public boolean equals(Object obj){  
            if(obj == null){  
                return false;  
            }  
              
            //如果是同一个对象返回true，反之返回false  
            if(this == obj){  
                return true;  
            }  
              
            //判断是否类型相同  
            if(this.getClass() != obj.getClass()){  
                return false;  
            }  
              
            Person person = (Person)obj;  
            return name.equals(person.name) && age==person.age;  
        } 
    }
}
```

**运行结果**：

```java
p1.equals(p2) : true; p1(1169863946) p2(1690552137)
set:[(eee, 100), (eee, 100), (aaa, 200)]
```

可以发现：我们重写了Person的equals()。但是，很奇怪的发现：HashSet中仍然有重复元素：p1 和 p2。由此验证了我们刚才的推理。

###### hashCode（）与equals（）的相关规定

1. 如果两个对象相等，则hashcode一定也是相同的
2. 两个对象相等,对两个对象分别调用equals方法都返回true
3. 两个对象有相同的hashcode值，它们也不一定是相等的（哈希冲突）
4. **因此，equals 方法被覆盖过，则 hashCode 方法也必须被覆盖**
5. hashCode() 的默认行为是对堆上的对象产生独特值。如果没有重写 hashCode()，则该 class 的两个对象无论如何都不会相等（即使这两个对象指向相同的数据）

推荐阅读：[Java hashCode() 和 equals()的若干问题解答](https://www.cnblogs.com/skywang12345/p/3324958.html)



### 枚举类





## 接口,lambda表达式与内部类



## 异常

### Java异常类层次结构图

![1583295038775](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304121039-879793.png)

在 Java 中，所有的异常都有一个共同的祖先java.lang包中的 **Throwable类**。Throwable： 有两个重要的子类：**Exception（异常）** 和 **Error（错误）** ，二者都是 Java 异常处理的重要子类，各自都包含大量子类。

**Error（错误）:是程序无法处理的错误**，表示运行应用程序中较严重问题。大多数错误与代码编写者执行的操作无关，而表示代码运行时  JVM（Java 虚拟机）出现的问题。例如，Java虚拟机运行错误（Virtual MachineError），当 JVM  执行操作所需的内存资源不足时，将出现 OutOfMemoryError。这些异常发生时，Java虚拟机（JVM）一般会选择线程终止。

这些错误表示故障发生于虚拟机自身、或者发生在虚拟机试图执行应用时，如Java虚拟机运行错误（Virtual  MachineError）、类定义错误（NoClassDefFoundError）等。这些错误是不可查的，因为它们在应用程序的控制和处理能力之  外，而且绝大多数是程序运行时不允许出现的状况。对于设计合理的应用程序来说，即使确实发生了错误，本质上也不应该试图去处理它所引起的异常状况。在  Java中，错误通过Error的子类描述。

**Exception（异常）:是程序本身可以处理的异常**。Exception 类有一个重要的子类 **RuntimeException**。RuntimeException 异常由Java虚拟机抛出。**NullPointerException**（要访问的变量没有引用任何对象时，抛出该异常）、**ArithmeticException**（算术运算异常，一个整数除以0时，抛出该异常）和 **ArrayIndexOutOfBoundsException** （下标越界异常）。

**注意：异常和错误的区别：异常能被程序本身处理，错误是无法处理。**

### Throwable类常用方法

- **public string getMessage()**:返回异常发生时的简要描述
- **public string toString()**:返回异常发生时的详细信息
- **public string getLocalizedMessage()**:返回异常对象的本地化信息。使用Throwable的子类覆盖这个方法，可以生成本地化信息。如果子类没有覆盖该方法，则该方法返回的信息与getMessage（）返回的结果相同
- **public void printStackTrace()**:在控制台上打印Throwable对象封装的异常信息

### 异常处理总结

- **try 块：** 用于捕获异常。其后可接零个或多个catch块，如果没有catch块，则必须跟一个finally块。
- **catch 块：** 用于处理try捕获到的异常。
- **finally 块：** 无论是否捕获或处理异常，finally块里的语句都会被执行。当在try块或catch块中遇到return 语句时，finally语句块将在方法返回之前被执行。

**在以下4种特殊情况下，finally块不会被执行：**

1. 在finally语句块第一行发生了异常。 因为在其他行，finally块还是会得到执行
2. 在前面的代码中用了System.exit(int)已退出程序。 exit是带参函数(exit是java关闭虚拟机的意思) ；若该语句在异常语句之后，finally会执行
3. 程序所在的线程死亡。
4. 关闭CPU。





## 泛型



## 序列化

如果没有自定义serialVersionUID，那么serialVersionUID的取值是Java运行时环境根据类的内部细节自动生成的。如果对类的源代码作了修改，再重新编译，新生成的类文件的serialVersionUID的取值有可能也会发生变化（如添加一个变量，那么serialVersionUID就会变化，但是如果修改了一个变量的值，那么serialVersionUID是不会变化的。如果修改）。





## 代理



## 杂项

### 字符型常量和字符串常量的区别?

1. 形式上: 字符常量是单引号引起的一个字符; 字符串常量是双引号引起的若干个字符

2. 含义上: 字符常量相当于一个整型值( ASCII 值),可以参加表达式运算; 字符串常量代表一个地址值(该字符串在内存中存放位置)

3. 占内存大小 字符常量只占2个字节; 字符串常量占若干个字节 (**注意： char在Java中占两个字节**)

   ![1583295067087](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304121107-887360.png)

### String StringBuffer 和 StringBuilder 的区别是什么? String 为什么是不可变的?

**可变性**

简单的来说：String 类中使用 final 关键字修饰字符数组来保存字符串，`private　final　char　value[]`，所以 String 对象是不可变的。而StringBuilder 与 StringBuffer 都继承自 AbstractStringBuilder 类，在 AbstractStringBuilder 中也是使用字符数组保存字符串`char[]value` 但是没有用 final 关键字修饰，所以这两种对象都是可变的。

StringBuilder 与 StringBuffer 的构造方法都是调用父类构造方法也就是 AbstractStringBuilder 实现的，大家可以自行查阅源码。

AbstractStringBuilder.java

```java
abstract class AbstractStringBuilder implements Appendable, CharSequence {
    char[] value;
    int count;
    AbstractStringBuilder() {
    }
    AbstractStringBuilder(int capacity) {
        value = new char[capacity];
    }
```

**线程安全性**

String  中的对象是不可变的，也就可以理解为常量，线程安全。AbstractStringBuilder 是 StringBuilder 与  StringBuffer 的公共父类，定义了一些字符串的基本操作，如 expandCapacity、append、insert、indexOf  等公共方法。StringBuffer 对方法加了同步锁或者对调用的方法加了同步锁，所以是线程安全的。StringBuilder  并没有对方法进行加同步锁，所以是非线程安全的。　

**性能**

每次对 String  类型进行改变的时候，都会生成一个新的 String 对象，然后将指针指向新的 String 对象。StringBuffer 每次都会对  StringBuffer 对象本身进行操作，而不是生成新的对象并改变对象引用。相同情况下使用 StringBuilder 相比使用  StringBuffer 仅能获得 10%~15% 左右的性能提升，但却要冒多线程不安全的风险。

**对于三者使用的总结：** 

1. 操作少量的数据: 适用String
2. 单线程操作字符串缓冲区下操作大量数据: 适用StringBuilder
3. 多线程操作字符串缓冲区下操作大量数据: 适用StringBuffer



### 获取用键盘输入常用的两种方法

```java
Scanner input = new Scanner(System.in);
String s  = input.nextLine();
input.close();
```

方法2：通过 BufferedReader 

```java
BufferedReader input = new BufferedReader(new InputStreamReader(System.in)); 
String s = input.readLine(); 
```



### 深拷贝 vs 浅拷贝

1. **浅拷贝**：对基本数据类型进行值传递，对引用数据类型进行引用传递般的拷贝，此为浅拷贝。(浅拷贝不是说只是像下面这样，而是也创建了一个新的对象，如下面的默认的clone方法一样)

   1. ```java
      Employee original = new Employee("John Public", 50000);
      Employee copy = original;
      ```

      

2. **深拷贝**：对基本数据类型进行值传递，对引用数据类型，创建一个新的对象，并复制其内容，此为深拷贝。

#### Cloneable接口

如果希望 copy 是一个新对象，它的初始状态与 original 相同， 但是之后它们各自会有自己不同的状态， 这种情况下就可以使用 clone 方法。

要想调用对象的clone方法，那么该对象对应的类应该实现Cloneable接口，这是一个标记接口，不实现则会报错，在java中Object类提供的clone方法默认是浅拷贝，并且clone是Object类中的一个 protected 方法，这说明你的代码不能
直接调用这个方法。只有 Employee 类可以克隆 Employee 对象。这个限制是有原因的。想想看 Object 类如何实clone。它对于这个对象一无所知，不能保证子类的实现者一定会修正 clone 方法让它正常工作, 所以只能对这个对象逐个域地进行拷贝。 如果对象中的所有数据域都是数值或其他基本类型，拷贝这些域没有任何问题、 但是如果对象包含子对象的引用，拷贝域就会得到相同子对象的另一个引用，这样一来，原对象和克隆的对象仍然会共享一些信息。所以这样一来，其实默认的clone方法是如下这样的。（这里的original 和copy是两个不同的对象来的，即if(copy == original）{System.out.println(1);}不会输出

浅拷贝会有什么影响吗？ 这要看具体情况。如果原对象和浅克隆对象共享的子对象是不可变的， 那么这种共享就是安全的。如果子对象属于一个不可变的类， 如 String, 就 是 这 种情况。或者在对象的生命期中， 子对象一直包含不变的常量， 没有更改器方法会改变它， 也没有方法会生成它的引用，这种情况下同样是安全的。不过， 通常子对象都是可变的， 必须重新定义 clone 方法来建立一个深拷贝， 同时克隆所有子对象。在这个例子中，hireDay 域是一个 Date , 这是可变的， 所以它也需要克隆。

![1583295087656](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304121128-948467.png)

#### Cloneable 实现深拷贝

要实现深拷贝，则必须在实现 Cloneable 接口的基础上还要，重新定义 clone 方法，并指定 public 访问修饰符以方便在别的类调用。

```java
public class Employee implements Cloneable
 {
 		//省略get,set
         private String name;
         private double salary;
         private Date hireDay;
	public Employee clone() throws CloneNotSupportedException
    {
        // call Object,clone0
        Employee cloned = (Employee) super.clone() ;
        // clone mutable(可变的) fields,Date类中已经实现了clone方法了！所以在这里可以直接调用！
        cloned.hireDay = (Date) hireDay.clone();
        return cloned;
    }

}   


```

如果在一个对象上调用 clone, 但这个对象的类并没有实现 Cloneable 接口， Object 类的 clone 方法就会拋出一个 CloneNotSupportedExceptionD 当然，Employee 和 Date 类实现了Cloneable 接口，所以不会抛出这个异常。 不过， 编译器并不了解这一点，因此，我们在上面代码中声明了这个异常。