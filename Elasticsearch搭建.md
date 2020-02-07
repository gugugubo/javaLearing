## 1  集成方式

Spring Boot中集成Elasticsearch有4种方式：

1. REST Client
2. Jest
3. Spring Data
4. Spring Data Elasticsearch Repositories

任何使用过Elasticsearch的人都知道，使用基于rest的搜索API构建查询可能是单调乏味且容易出错的。

在本教程中，我们将研究Jest，一个用于Elasticsearch的HTTP Java客户端。Elasticsearch提供了自己原生的Java客户端，然而 Jest提供了更流畅的API和更容易使用的接口。

## 2. Maven 依赖

```xml
<!--jest客户端操作elasticsearch-->
        <dependency>
            <groupId>io.searchbox</groupId>
            <artifactId>jest</artifactId>
            <version>5.3.4</version>
        </dependency>
        <!-- elasticsearch-->
        <dependency>
            <groupId>org.elasticsearch</groupId>
            <artifactId>elasticsearch</artifactId>
            <version>5.6.11</version>
        </dependency>
```



## 3. 使用Jest Client

我们将研究如何使用Jest client执行Elasticsearch中的常见任务。

要使用Jest client，我们只需使用 *JestClientFactory* 创建一个 *JestClient* 对象。这些对象的创建开销很高，而且是线程安全的，因此我们将创建一个可以在整个应用程序中共享的单例实例：

```java
public JestClient jestClient() {
    JestClientFactory factory = new JestClientFactory();
    factory.setHttpClientConfig(
      new HttpClientConfig.Builder("http://localhost:9200")
        .multiThreaded(true)
        .defaultMaxTotalConnectionPerRoute(2)
        .maxTotalConnection(10)
        .build());
    return factory.getObject();
}
```

这里将创建一个Jest client，该客户端连接到本地运行的Elasticsearch。虽然这个连接示例很简单，但是Jest还完全支持代理、SSL、身份验证，甚至节点发现。

*JestClient* 类是通用类，只有少数公共方法。我们将使用的一个主要方法是execute，它接受Action接口的一个实例。Jest客户端提供了几个构建器类来帮助创建与Elasticsearch交互的不同操作。

所有Jest调用的结果都是JestResult的一个实例。 我们可以通过调用 `issucceeded` 方法来检查是否成功。对于失败的操作，我们可以调用`GetErrorMessage`方法来获取更多详细信息：

```java
JestResult jestResult = jestClient.execute(new Delete.Builder("1").index("employees").build());

if (jestResult.isSucceeded()) {
    System.out.println("Success!");
}
else {
    System.out.println("Error: " + jestResult.getErrorMessage());
}
```

对于所有的这些操作都是由相关的deletexxx，getxxx, putxxx等，看图

![1581006980230](D:\BaiduNetdiskDownload\markdown图片\1581006980230.png)



## 参考文档

https://www.cnblogs.com/liululee/p/11075432.html

https://www.cnblogs.com/enenen/p/9122053.html