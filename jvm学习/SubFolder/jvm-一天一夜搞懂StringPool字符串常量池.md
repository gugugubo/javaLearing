## 字符串常量池的概念

字符串常量池也有人叫字符串池（String Pool），字符串常量池由String类私有的维护。

在阅读此文章之前还要理解的一些概念，免得你看得神魂颠倒！

- `String str = “aa” ；`字符串常量指的是语句中的str，字符串对象或者说字符串实例指的是语句中的 “aa”。
- [常量表达式](https://www.cnblogs.com/iamzhoug37/p/4375684.html)是一个代表基本数据类型或者String数据类型的表达式，是在编译期间能计算出来的值。
- 字面量，字面值 指的是如 "aaa";

## 创建字符串对象的两种方式

### 使用字面量创建

```java
    public static void main(String[] args) {
       String str = "aaa";
       String str2 = "aaa";
       System.out.println(str == str2);// true
    }
```

使用字面量创建字符串对象时，jvm首先会在字符串常量池中查找是否存`"aaa"`这个对象，如果不存在，则在字符串常量池中创建`"aaa"`对象，然后将此对象的引用地址返回给字符串常量（如示例中的str），如果存在，则则不创建任何对象，直接将常量池中的对象的引用地址返回给字符串常量（如示例中的str2，故str2和str指向了同一个对象）

### 使用new关键字新建一个字符串对象

```java
    public static void main(String[] args) {
       String str = new String("aaa");
       String str2 =  new String("aaa");
       System.out.println(str == str2);// false
    }
```

使用new关键字创建对象时，jvm首先在字符串常量池中查找有没有"aaa"这个对象，如果不存在，则首先在字符串常量池中创建一个`"aaa"`对象，然后再在堆中创建一个`"aaa"`对象，最后把堆中创建的这个对象的引用地址返回给字符串常量str；如果存在，则只直接在堆中创建一个`"aaa"`对象，然后把堆中创建的这个对象的引用地址返回给字符串常量str2；因此，str与str2指向的是不同的两个对象。

   **Java语言规范（Java Language Specification）**中对字符串做出了如下说明：每一个字符串常量都是指向一个字符串类实例的引用。字符串对象有一个固定值。字符串常量，或者一般的说，常量表达式中的字符串都被使用方法
String.intern进行保留来共享唯一的实例。以上是Java语言规范中的原文，比较官方，用更通俗易懂的语言翻译过来主要说明了三点：1）每一个字符串常量都指向字符串池中或者堆内存中的一个字符串实例；2）字符串对象值是固定的，一旦创建就不能再修改；3）字符串常量或者常量表达式中的字符串都被使用方法String.intern()在字符串池中保留了唯一的实例。并且给出了测试程序如下：



## 字符串的计算

字符串常量在**编译时**计算和在**运行时**计算，其执行过程是不同的，得到的结果也是不同的。我们来看看下面这段代码：

```java
    public static void main(String[] args) {
        String test = "javalanguagespecification";
        String str = "java";
        String str2 = "language";
        String str3 = "specification";
        System.out.println(test == str + str2 + str3);
        System.out.println(test == "java" + "language" + "specification");
    }
```

字符串字面量的计算操作是在编译的时候就执行了的，也就是说编译器编译时，把"java"、"language"和"specification"这三个字面量进行"+"计算得到一个"javalanguagespecification"常量，并且直接将这个常量放入字符串常量池中，这样做实际上是一种优化，将3个字面量合成一个，避免了创建多余的字符串对象。而字符串引用进行"+"的计算是在Java运行期间执行的，即str + str2 + str3在程序执行期间才会进行计算，它会在堆内存中重新创建一个拼接后的字符串对象。总结来说就是：字符串字面量"+"拼接是在编译期间进行的，拼接后的字符串对象存放在字符串常量池中，而字符串引用进行"+"拼接计算是在运行时进行的，拼接后的字符串存放在堆中。

## intern()方法的使用

### intern()方法的javadoc 文档

```properties
Returns a canonical representation for the string object.
返回字符串对象的规范表示形式。
A pool of strings, initially empty, is maintained privately by the class String.
字符串池最初为空，由类字符串的私有方法维护。
When the intern method is invoked, if the pool already contains a string equal to this String object as determined by the equals(Object) method, then the string from the pool is returned. Otherwise, this String object is added to the pool and a reference to this String object is returned.
调用intern方法时，如果池中已包含由equals（object）方法确定的与此string对象相等的字符串，则返回池中的字符串。否则，此字符串对象将添加到池中，并返回对此字符串对象的引用。
It follows that for any two strings s and t, s.intern() == t.intern() is true if and only if s.equals(t) is true.
因此，对于任意两个字符串s和t，s.intern()==t.intern()在且仅当s.equals（t）为true时为true。
All literal strings and string-valued constant expressions are interned.
所有文本字符串和字符串值常量表达式都被保留。
```

### intern()方法实验

```java
    public static void main(String[] args) {
        String s1 = new String("hello");
        String intern1 = s1.intern();
        String s2 = "hello";
        System.out.println(s1 == s2);
        String s3 = new String("hello") + new String("hello");
        String intern3 = s3.intern();
        String s4 = "hellohello";
        System.out.println(s3 == s4);
    }
    /**
     * 　　在jdk1.6下运行的结果为：
     * 　　false,false
     * 　　在jdk1.7,1.8下运行的结果为：
     * 　　false,true
     */
```

jdk1.7以下（不包括1.7）字符串常量池是在永久区的，是与堆完全独立的空间，s1指向的是堆中的内容，而s2 指向的是字符串常量池中的内容，所以s1 和 s2 必不相等；当执行`s1.instern()`方法时，如果字符串常量池中不存在`"hello"`对象时，就在字符串常量池中创建该对象并返回该对象的引用，如果在字符串常量池中已经存在`"hello"`对象，那么就直接返回该对象的引用（所以这里的`intern1 == s2`）。

在jdk1.7及以上，字符串常量池已经转移到堆中了，是堆中的一部分，jvm设计人员对`intern()`方法进行了一些修改，当执行s3.intern()方法时，如果字符串常量池中不存在`"hellohello"`对象，则在字符串常量池中储存一份s3的引用，这个引用指向堆中的`"hellohello"`对象；如果在字符串常量池中已经存在`"hellohello"`对象，那么就直接返回该对象的引用。所以在运行到`String s4 = "hellohello"`时，发现字符串常量池中已经存在指向堆中"hellohello"`对象的引用，那么将返回这个将这个引用（所以s3的对象被返回，s3 == s4）；



## 参考文章

- [字符串常量池深度解析](https://www.cnblogs.com/fangfuhai/p/5500065.html)
- [字符串常量池实战](https://www.cnblogs.com/tongkey/p/8587060.html)
- 如果想了解动态常量池和静态常量池的区别，[请点击这里](https://blog.csdn.net/wangbiao007/article/details/78545189)