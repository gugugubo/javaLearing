## serialVersionUID的作用

​	如果没有自定义serialVersionUID，那么serialVersionUID的取值是Java运行时环境根据类的内部细节自动生成的。在序列化时的serialVersionUID如果和反序列化时的serialVersionUID不同，就会导致报错。

- 如果对类的源代码作了修改，再重新编译，新生成的类文件的serialVersionUID的取值有可能也会发生变化，如添加一个变量，那么serialVersionUID就会变化。
- 如果显式地自定义一个serialVersionUID，serialVersionUID就不会改变。
  - 显式地定义serialVersionUID有两种用途：
    1. 在某些场合，希望类的不同版本对序列化兼容，因此需要确保类的不同版本具有相同的serialVersionUID；
    2. 在某些场合，不希望类的不同版本对序列化兼容，因此需要确保类的不同版本具有不同的serialVersionUID；
- [参考博客](https://www.cnblogs.com/xdp-gacl/p/3777987.html)

## **静态变量序列化**

这里要注意的是静态变量序列化，将对象序列化后，修改静态变量的数值，再将序列化对象读取出来，然后通过读取出来的对象获得静态变量的数值并打印出来。如下程序所示，那么输出的Customer中age的值是什么呢？是1还是25呢，最后的输出是 1，对于无法理解的读者认为，打印的 age是从读取的对象里获得的，应该是保存时的状态才对。之所以打印 1 的原因在于序列化时，并不保存静态变量，这其实比较容易理解，序列化保存的是对象的状态，静态变量属于类的状态，因此序列化并不保存静态变量。

```java
public class TestSerialversionUID {

    public static void main(String[] args) throws Exception {
        SerializeCustomer();// 序列化Customer对象
        Customer.age = 1; // 修改静态变量的值为1
        Customer customer = DeserializeCustomer();// 反序列Customer对象
        System.out.println(customer);
    }

    private static void SerializeCustomer() throws FileNotFoundException,
            IOException {
        Customer customer = new Customer("gacl",25);
        // ObjectOutputStream 对象输出流
        ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(
                new File("C:Customer.txt")));
        oo.writeObject(customer);
        System.out.println("Customer对象序列化成功！");
        oo.close();
    }

    private static Customer DeserializeCustomer() throws Exception, IOException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
                new File("C:Customer.txt")));
        Customer customer = (Customer) ois.readObject();
        System.out.println("Customer对象反序列化成功！");
        return customer;
    }
}
class Customer implements Serializable {
    //Customer类中没有定义serialVersionUID
    private String name;
    public static int age;
    public Customer(String name, int age1) {
        this.name = name;
        age = age1;
    }
    @Override
    public String toString() {
        return "name=" + name + ", age=" + age;
    }
}
```

