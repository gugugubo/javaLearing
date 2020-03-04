## Swagger和SpringFox

​	最受欢迎的API文档规范之一是OpenApi，以前称为Swagger。它允许您使用JSON或YAML元数据描述API的属性。它还提供了一个Web  UI，它可以将元数据转换为一个很好的HTML文档。此外，通过该UI，您不仅可以浏览有关API端点的信息，还可以将UI用作REST客户端 -  您可以调用任何端点，指定要发送的数据并检查响应。它非常方便。

​	然而，手动编写此类文档并在代码更改时保持更新是不现实的。这就是SpringFox发挥作用的地方。它是Spring   Framework的Swagger集成。它可以自动检查您的类，检测控制器，它们的方法，它们使用的模型类以及它们映射到的URL。没有任何手写文档，只需检查应用程序中的类，它就可以生成大量有关API的信息。

## 整合swagger-ui

#### 1添加maven依赖

```xml
<!--Swagger-UI API文档生产工具-->
<dependency>
  <groupId>io.springfox</groupId>
  <artifactId>springfox-swagger2</artifactId>
  <version>2.7.0</version>
</dependency>
```

#### 2. swagger-ui 的java配置

添加依赖项后，您需要提供一些基本的Spring配置。虽然您可以在技术上使用现有配置文件之一，但最好为其配置单独的文件。您需要提供的第一件事是*@ EnableSwagger2*注释。然后你需要提供一个Docket bean，它是用于配置SpringFox的主bean。

```java
@Configuration
@EnableSwagger2
public class SpringFoxConfig {
    @Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
}
```

当然，您可以提供更多配置设置，我们稍后会看到，但这是一个简约配置，它执行以下操作：

- *@ EnableSwagger2*支持Swagger 2的SpringFox支持。
- *DocumentationType.SWAGGER_2*告诉Docket bean我们正在使用Swagger规范的版本2。
- *select（）*创建一个构建器，用于定义哪些控制器及其生成的文档中应包含哪些方法。
- *apis（）*定义要包含的类（控制器和模型类）。这里我们包括所有这些，但您可以通过基础包，类注释等来限制它们。
- *paths（）*允许您根据路径映射定义应包含哪个控制器的方法。我们现在包括所有这些，但您可以使用正则表达式等限制它。

#### 3.添加UI

如果您现在部署应用程序，则已经生成了描述API的swagger元数据！你可以看看：

```c
http://localhost:8080/v2/api-docs
```

事实证明它只是一个很大的JSON，而不是人类可读的。但你已经可以验证它是否有效。只需转到[Swagger在线编辑器](https://editor.swagger.io/)并将JSON粘贴到那里。将生成的JSON粘贴到左侧面板，瞧！您现在可以将生成的文档视为HTML页面。不错，不是吗？将这些文档作为应用程序的一部分直接使用会更好。幸运的是，实现这一点非常容易。显示基于JSON输入的HTML文档的GUI称为*swagger-ui*。要启用它是一个Spring Boot应用程序，您只需要添加此依赖项：

```xml
<dependency>
  <groupId>io.springfox</groupId>
  <artifactId>springfox-swagger-ui</artifactId>
  <version>2.7.0</version>
</dependency>
```

#### 4.添加ApiInfo

  我们可以通过简单的配置更改来接口页面显示所有信息。在*SpringFoxConfiguration*文件中，我们需要添加*ApiInfo*对象，该对象提供有关API的一般信息，例如标题，版本，联系人或许可信息。

```java
package com.gcb.learning.mybatisplus.pms.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2API文档的配置
 */
@Configuration
@EnableSwagger2
public class swagger2Config {
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //为当前包下controller生成API文档
                .apis(RequestHandlerSelectors.basePackage("com.gcb.learning.mybatisplus.pms.controller"))
                //为有@Api注解的Controller生成API文档
//                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                //为有@ApiOperation注解的方法生成API文档
//                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SwaggerUI演示")
                .description("测试测试")
                .version("1.0")
                .build();
    }
}

```

#### 5.为controller层添加注解

###### 常用注解

- @Api：用于修饰Controller类，生成Controller相关文档信息
- @ApiOperation：用于修饰Controller类中的方法，生成接口方法相关文档信息
- @ApiParam：用于修饰接口中的参数，生成接口参数相关文档信息
- @ApiModelProperty：用于修饰实体类的属性，当实体类是请求参数或返回结果时，直接生成相关文档信息

```java
package com.gcb.learning.mybatisplus.pms.controller;


import com.gcb.learning.mybatisplus.pms.common.CommonResult;
import com.gcb.learning.mybatisplus.pms.common.PageInfoVo;
import com.gcb.learning.mybatisplus.pms.entity.ProductBrand;
import com.gcb.learning.mybatisplus.pms.service.ProductBrandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 商品的品牌表 前端控制器
 * </p>
 *
 * @author gcb
 * @since 2020-02-02
 */
@RestController
@RequestMapping("/pms/product-brand")
public class ProductBrandController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductBrandController.class);

    @Autowired
    private ProductBrandService demoService;

    @RequestMapping(value = "listAll", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getBrandList() {
        return new CommonResult().success(demoService.listAllBrand());
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult createBrand(@RequestBody ProductBrand pmsBrand) {
        CommonResult commonResult;
        int count = demoService.createBrand(pmsBrand);
        if (count == 1) {
            commonResult = new CommonResult().success(pmsBrand);
            LOGGER.debug("createBrand success:{}", pmsBrand);
        } else {
            commonResult = new CommonResult().failed();
            LOGGER.debug("createBrand failed:{}", pmsBrand);
        }
        return commonResult;
    }


    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateBrand(@PathVariable("id") Long id, @RequestBody ProductBrand pmsBrandDto, BindingResult result) {
        CommonResult commonResult;
        int count = demoService.updateBrand(id, pmsBrandDto);
        if (count == 1) {
            commonResult = new CommonResult().success(pmsBrandDto);
            LOGGER.debug("updateBrand success:{}", pmsBrandDto);
        } else {
            commonResult = new CommonResult().failed();
            LOGGER.debug("updateBrand failed:{}", pmsBrandDto);
        }
        return commonResult;
    }


    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult deleteBrand(@PathVariable("id") Long id) {
        int count = demoService.deleteBrand(id);
        if (count == 1) {
            LOGGER.debug("deleteBrand success :id={}", id);
            return new CommonResult().success(null);
        } else {
            LOGGER.debug("deleteBrand failed :id={}", id);
            return new CommonResult().failed();
        }
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult listBrand(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                        @RequestParam(value = "pageSize", defaultValue = "3") Integer pageSize) {
        PageInfoVo brandList = demoService.listBrand(pageNum, pageSize);
        return new CommonResult().success(brandList);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult brand(@PathVariable("id") Long id) {
        return new CommonResult().success(demoService.getBrand(id));
    }

}

```

#### 6.运行项目

查看控制器信息

![1580717481231](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304161232-123185.png)

可以查看接口传递的参数的注解

![1580717705066](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304161155-135043.png)

在接口文档进行测试

![1580718171443](https://gitee.com/gu_chun_bo/picture/raw/master/image/20200304161157-223405.png)

项目代码在这里