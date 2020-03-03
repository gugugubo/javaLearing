package com.gcb.learning.elasticsearch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcb.learning.elasticsearch.entity.Employee;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.client.JestResultHandler;
import io.searchbox.core.*;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.IndicesExists;
import io.searchbox.indices.aliases.AddAliasMapping;
import io.searchbox.indices.aliases.ModifyAliases;
import io.searchbox.indices.aliases.RemoveAliasMapping;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.*;

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


