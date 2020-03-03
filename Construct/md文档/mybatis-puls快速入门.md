

# mybatis-plus简介：

Mybatis-Plus（简称MP）是一个 Mybatis 的增强工具，在 Mybatis 的基础上只做增强不做改变，为简化开发、提高效率而生。这是官方给的定义，关于mybatis-plus的更多介绍及特性，可以参考[mybatis-plus官网](http://mp.baomidou.com/#/)。那么它是怎么增强的呢？其实就是它已经封装好了一些crud方法，我们不需要再写xml了，直接调用这些方法就行，就类似于JPA。

## 逆向代码生成

配置参考：[官网链接](https://mp.baomidou.com/guide/generator.html#%E4%BD%BF%E7%94%A8%E6%95%99%E7%A8%8B)

#### 1.导入Maven依赖

```xml
  <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-generator</artifactId>
            <version>3.1.0</version>
        </dependency>
        <!--使用mybatis-plus生成代码所使用的模板引擎-->
        <dependency>
            <groupId>org.freemarker</groupId>
            <artifactId>freemarker</artifactId>
            <version>2.3.28</version>
        </dependency>
        <!--数据库驱动-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.47</version>
        </dependency>
        <!--使生成的代码使用lombok相关注解，防止lombok报错-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.2</version>
        </dependency>
        <!--使生成的代码带有swagger注解，防止swagger报错-->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger2</artifactId>
            <version>2.8.0</version>
        </dependency>

        <!--spring相关-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <version>2.2.4.RELEASE</version>
            <scope>compile</scope>
        </dependency>
        <!--end-->
```

#### 2.配置代码生成器

```java
package com.gcb.learning.mybatisplus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

/**
 * 需要修改的地方是 moduleName模块名  3.数据源配置  4.包配置
 * @author 古春波
 */
public class CodeGenerator {

    public static void main(String[] args) {

        String moduleName = "ums";

        // 1、创建代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 2、全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath  + "/src/main/java");
        // 设置作者
        gc.setAuthor("gcb");
        //生成后是否打开输出目录
        gc.setOpen(false);
        //重新生成时文件是否覆盖
        gc.setFileOverride(false);
        //service 命名方式，例如  %sBusiness 生成 UserBusiness（%s 为占位符）
        gc.setServiceName("%sService");
        //主键策略
        gc.setIdType(IdType.AUTO);
        //定义生成的实体类中日期类型
        gc.setDateType(DateType.ONLY_DATE);
        //开启Swagger2模式
        gc.setSwagger2(true);
        gc.setBaseColumnList(true);
        //生成每个xml的baseResultMap
        gc.setBaseResultMap(true);

        mpg.setGlobalConfig(gc);

        // 3、数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://47.94.110.49:3307/gmall_"+moduleName+"?useUnicode=true&useSSL=false&characterEncoding=utf8");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("root");
        dsc.setDbType(DbType.MYSQL);
        mpg.setDataSource(dsc);

        // 4、包配置
        PackageConfig pc = new PackageConfig();
        //模块名
        pc.setModuleName(moduleName);
        pc.setParent("com.gcb.learning.mybatisplus");
        pc.setController("controller");
        pc.setEntity("entity");
        pc.setService("service");
        pc.setMapper("mapper");
        mpg.setPackageInfo(pc);

        // 5、策略配置
        StrategyConfig strategy = new StrategyConfig();
        //设置需要包含的表名，允许正则表达式
        strategy.setInclude(moduleName + "_\\w*");
        //数据库表映射到实体的命名策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        //设置表前缀不生成
        strategy.setTablePrefix(pc.getModuleName() + "_");
        //是否生成实体时，生成字段注解
        strategy.setEntityTableFieldAnnotationEnable(true);

        //数据库表字段映射到实体的命名策略，未指定按照 naming 执行
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        // 【实体】是否为lombok模型（默认 false），lombok 模型 @Accessors(chain = true) setter链式操作
        strategy.setEntityLombokModel(true);

        //strategy.setLogicDeleteFieldName("is_deleted");//逻辑删除字段名
        //strategy.setEntityBooleanColumnRemoveIsPrefix(true);//去掉布尔值的is_前缀

        //自动填充
        //TableFill gmtCreate = new TableFill("gmt_create", FieldFill.INSERT);
        //TableFill gmtModified = new TableFill("gmt_modified", FieldFill.INSERT_UPDATE);
        //ArrayList<TableFill> tableFills = new ArrayList<>();
        //tableFills.add(gmtCreate);
        //tableFills.add(gmtModified);
        //strategy.setTableFillList(tableFills);
        //乐观锁列
        //strategy.setVersionFieldName("version");

        // 生成 @RestController 控制器
        strategy.setRestControllerStyle(true);
        //url中驼峰转连字符
        strategy.setControllerMappingHyphenStyle(true);
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        // 6、执行
        mpg.execute();
        System.out.println("生成完成....");
    }
}

```





只需简单的几步就可以实现配置了，更多详细配置，请参考官方文档[代码生成器配置](https://mp.baomidou.com/config/generator-config.html)一文。生成的代码注解相关解释可以参考[注解详解](https://mp.baomidou.com/guide/annotation.html)



## mybatis-plus的整合springboot

此处的代码是使用上面的代码逆向生成器生成的代码进行测试的

#### 1.导入maven依赖

```xml
		<!--mybatis-plus,其中包含了一个数据源，故可以不用另外配置-->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.1.0</version>
        </dependency>
        <!--数据库驱动-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.47</version>
        </dependency>
        <!--使生成的代码使用lombok相关注解，防止lombok报错-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.2</version>
        </dependency>
        <!--使生成的代码带有swagger注解，防止swagger报错-->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger2</artifactId>
            <version>2.8.0</version>
        </dependency>

        <!--spring相关-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <version>2.2.4.RELEASE</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <scope>test</scope>
        </dependency>
        <!--end-->
```



#### 2.mybatis-plus的Java配置

用于配置需要生成的mapper接口的路径

```java
package com.gcb.learning.mybatisplus.pms.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.gcb.learning.mybatisplus.pms.mapper")
public class mybatisConfig {
}

```

#### 3.springboot的application.properties配置

添加数据源配置和MyBatis的mapper.xml的路径配置。

```properties
#数据源配置
spring.datasource.url=jdbc:mysql://47.94.110.49:3307/gmall_pms
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=root

server.port=8081

#xml文件位置配置
mybatis-plus.mapper-locations=classpath:xml/*.xml
```

#### 4.进行单元测试

```java
package com.gcb.learning.mybatisplus;

import com.gcb.learning.mybatisplus.pms.entity.Product;
import com.gcb.learning.mybatisplus.pms.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class MybatisPlusApplicationTests {

    @Autowired
    ProductService productService;

    @Test
    void contextLoads() {
        Product product = productService.getById(1);
        System.out.println(product);
    }

}

```

#### 5. 进一步编写测试代码

对数据进行增删查改，[操作接口参考](https://mp.baomidou.com/guide/crud-interface.html)

参考博客：[简书博客](https://www.jianshu.com/p/ceb1df475021)

项目的具体代码请点击此处进行下载查看！

## 分页查询的实现

#### 1.导入maven

```xml
        <!--物理分页实现-->
        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper</artifactId>
            <version>5.1.8</version>
        </dependency>
```

#### 2.修改mybatis-plus的Java配置

```java
@Configuration
@MapperScan("com.gcb.learning.mybatisplus.pms.mapper")
public class mybatisConfig {

    /**
     * 设置分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        // paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        // paginationInterceptor.setLimit(500);
        // 开启 count 的 join 优化,只针对部分 left join
        //paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
        return paginationInterceptor;
    }
}
```



#### 3.通用的返回类

```java
@Data
@ApiModel
@AllArgsConstructor
@NoArgsConstructor
public class PageInfoVo implements Serializable {

    @ApiModelProperty("总记录数")
    private Long total;

    @ApiModelProperty("总页数")
    private Long totalpage;

    @ApiModelProperty("每页的数据条数")
    private Long pageSize;

    @ApiModelProperty("当前页码")
    private Long pageNum;

    @ApiModelProperty("当前页的数据")
    private List<? extends Object> list;

    public static PageInfoVo getVo(IPage iPage, Long pageSize){
        return new PageInfoVo(iPage.getTotal(),iPage.getPages(),pageSize,iPage.getCurrent(),iPage.getRecords());

    }
}

```

#### 4.查询代码

```java
    @Override
    public PageInfoVo listBrand(Integer pageNum, Integer pageSize) {
        IPage<ProductBrand> productBrandIPage = productBrandMapper.selectPage(new Page<ProductBrand>(pageNum, pageSize), null);
        PageInfoVo pageInfoVo = PageInfoVo.getVo(productBrandIPage,pageSize.longValue());
        return pageInfoVo;
    }

```



