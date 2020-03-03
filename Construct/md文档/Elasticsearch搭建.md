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

## 3.配置application.properties文件

配置很简单，如果有多个elasticsearch服务器直接加入就可以了，超级简单阿

```properties
#ES配置
spring.elasticsearch.jest.uris[0]=http://47.94.110.49:9200

```



## 4. 使用Jest Client

我们将研究如何使用Jest client执行Elasticsearch中的常见任务。

​	要使用Jest client，我们只需使用 *JestClientFactory* 创建一个 *JestClient* 对象。这些对象的创建开销很高，而且是线程安全的，因此我们将创建一个可以在整个应用程序中共享的单例实例：

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

而我们这里直接从容器中获取我们配置好的就行了

```java

    @Autowired  // 一个jest客户端
    JestClient jestClient;

```

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

![1581056713092](D:\BaiduNetdiskDownload\markdown图片\1581056713092.png)

[具体的操作可以看这篇博客](https://www.cnblogs.com/liululee/p/11075432.html)

### 4.1 crud操作

```java
@SpringBootTest
class ElasticsearchApplicationTests {

    @Autowired  // 一个jest客户端
    JestClient jestClient;


    /**
     * 管理文档
     * @throws IOException
     */
    @Test
    public void  manage() throws IOException {
        // 检测引索是否存在
        JestResult result = jestClient.execute(new IndicesExists.Builder("employees").build());
        if(!result.isSucceeded()) {
            System.out.println(result.getErrorMessage());
        }

        // 创建一个引索
        jestClient.execute(new CreateIndex.Builder("employees").build());

        // 创建引索的时候同时增加设置
        Map<String, Object> settings = new HashMap<>();
        settings.put("number_of_shards", 11);
        settings.put("number_of_replicas", 2);
        jestClient.execute(new CreateIndex.Builder("employees").settings(settings).build());

        // 创建一个别名
        jestClient.execute(new ModifyAliases.Builder(
                new AddAliasMapping.Builder(
                        "employees","e").build())
                .build());

        JestResult jestResult = jestClient.execute(new ModifyAliases.Builder(
                new RemoveAliasMapping.Builder(
                        "employees", "e").build())
                .build());

        if(jestResult.isSucceeded()) {
            System.out.println("Success!");
        }
        else {
            System.out.println(jestResult.getErrorMessage());
        }

    }

    /**
     * 创建文档，；建立的时候要指定index和type，type不指定会报错，id不指定会自己设置一个随机的值
     * @throws IOException
     */
    @Test
    public void construct() throws IOException {
        // Sample JSON for indexing
        // {
        //  "name": "Michael Pratt",
        //  "title": "Java Developer",
        //  "skills": ["java", "spring", "elasticsearch"],
        //  "yearsOfService": 2
        // }
        // 创建一个文档，这里使用Jackson库来帮助创建json类
        ObjectMapper mapper = new ObjectMapper();
        JsonNode employeeJsonNode = mapper.createObjectNode()
                .put("name", "Michael Pratt")
                .put("title", "Java Developer")
                .put("yearsOfService", 2)
                .set("skills", mapper.createArrayNode()
                        .add("java")
                        .add("spring")
                        .add("elasticsearch"));
        System.out.println(employeeJsonNode.toString());
        jestClient.execute(new Index.Builder(employeeJsonNode.toString())
                .index("employees").type("external").id("1").build());

        // 使用Java Map 来表示JSON数据，并将其传递给索引操作
        Map<String, Object> employeeHashMap = new LinkedHashMap<>();
        employeeHashMap.put("name", "Michael Pratt");
        employeeHashMap.put("title", "Java Developer");
        employeeHashMap.put("yearsOfService", 2);
        employeeHashMap.put("skills", Arrays.asList("java", "spring", "elasticsearch"));
        System.out.println(employeeHashMap.toString());
        jestClient.execute(new Index.Builder(employeeHashMap)
                .index("employees").type("external").id("1").build());

        // 索引的文档的任何POJO
        Employee employee = new Employee();
        employee.setName("Michael Pratt");
        employee.setTitle("Java Developer");
        employee.setYearsOfService(2);
        employee.setSkills(Arrays.asList("java", "spring", "elasticsearch"));
        System.out.println(employee.toString());
        jestClient.execute(new Index.Builder(employee)
                .index("employees").type("external").id("1").build());

    }


    /**
     * 读取文档
     * @throws IOException
     */
    @Test
    public void read() throws IOException{
        // 直接通过id
        Employee getResult = jestClient.execute(new Get.Builder("employees", "1").build()).getSourceAsObject(Employee.class);
        if (getResult!=null){
            System.out.println(getResult.toString());
        }
        // 手动构建dsl语句
        String search = "{\n" +
                "  \"query\": {\n" +
                "    \"bool\": {\n" +
                "      \"must\": [\n" +
                "        { \"match\": { \"name\":   \"Michael Pratt\" }}\n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}";
        List<SearchResult.Hit<Employee, Void>> searchResults =
                jestClient.execute(new Search.Builder(search).build())
                        .getHits(Employee.class);

        searchResults.forEach(hit -> {
            System.out.println(String.format("Document %s has score %s", hit.id, hit.score));
        });
    }

    /**
     * 更新文档
     * @throws IOException
     */
    @Test
    public void update() throws IOException {
        Employee employee = new Employee();
        employee.setName("Michael Pratt");
        employee.setTitle("Java Developer");
        employee.setYearsOfService(2);
        employee.setSkills(Arrays.asList("java", "spring", "elasticsearch"));
        employee.setYearsOfService(3);
        jestClient.execute(new Update.Builder(employee).index("employees").id("1").build());
    }

    /**
     * 删除文档
     * @throws IOException
     */
    @Test
    public void delete() throws IOException {
        jestClient.execute(new Delete.Builder("1").type("external").index("employees") .build());
    }

    /**
     * 批量操作
     * @throws IOException
     */
    @Test
    public void bulk() throws IOException {
        Employee employeeObject1 = new Employee();
        employeeObject1.setName("John Smith");
        employeeObject1.setTitle("Python Developer");
        employeeObject1.setYearsOfService(10);
        employeeObject1.setSkills(Arrays.asList("python"));

        Employee employeeObject2 = new Employee();
        employeeObject2.setName("Kate Smith");
        employeeObject2.setTitle("Senior JavaScript Developer");
        employeeObject2.setYearsOfService(10);
        employeeObject2.setSkills(Arrays.asList("javascript", "angular"));

        jestClient.execute(new Bulk.Builder().defaultIndex("employees")
                .addAction(new Index.Builder(employeeObject1).build())
                .addAction(new Index.Builder(employeeObject2).build())
                .addAction(new Delete.Builder("3").build()) .build());
    }


    /**
     * 异步操作文档
     * @throws IOException
     */
    @Test
    public void async() {
        Employee employeeObject3 = new Employee();
        employeeObject3.setName("Jane Doe");
        employeeObject3.setTitle("Manager");
        employeeObject3.setYearsOfService(20);
        employeeObject3.setSkills(Arrays.asList("managing"));

        jestClient.executeAsync( new Index.Builder(employeeObject3).build(), new JestResultHandler<JestResult>() {
            @Override public void completed(JestResult result) {
                // handle result
            }
            @Override public void failed(Exception ex) {
                // handle exception
            }
        });
    }

    @Test
    public void testSearchSource(){
        SearchSourceBuilder builder = new SearchSourceBuilder();


        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchAllQuery());

        builder.query(boolQueryBuilder);

        String s = builder.toString();
        System.out.println(s);
    }


}

```

整合完毕，hhhh，就是如此简单

## 参考文档

https://www.cnblogs.com/liululee/p/11075432.html

https://www.cnblogs.com/enenen/p/9122053.html