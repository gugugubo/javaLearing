## 使用RedisTemplate访问Redis数据结构

#### 1. Redis 数据结构简介

Redis 可以存储键与5种不同数据结构类型之间的映射，这5种数据结构类型分别为String（字符串）、List（列表）、Set（集合）、Hash（散列）和 Zset（有序集合）。

![1580732569255](D:\BaiduNetdiskDownload\markdown图片\1580732569255.png)

#### 2.RedisTemplate对5种数据结构操作

```java
redisTemplate.opsForValue();//操作字符串
redisTemplate.opsForHash();//操作hash
redisTemplate.opsForList();//操作list
redisTemplate.opsForSet();//操作set
redisTemplate.opsForZSet();//操作有序set
```

#### 3.StringRedisTemplate与RedisTemplate

- 两者的关系是StringRedisTemplate继承RedisTemplate。

- 两者的数据是不共通的；也就是说StringRedisTemplate只能管理StringRedisTemplate里面的数据，RedisTemplate只能管理RedisTemplate中的数据。

- SDR默认采用的序列化策略有两种，一种是String的序列化策略，一种是JDK的序列化策略。

  StringRedisTemplate默认采用的是String的序列化策略，保存的key和value都是采用此策略序列化保存的。

  RedisTemplate默认采用的是JDK的序列化策略，保存的key和value都是采用此策略序列化保存的。

**注意：如果使用RedisTemplate需要更改序列化方式，改成json格式的序列化器保存，利于跨平台跨语言操作数据**

这里将讲解StringRedisTemplate的使用，如使用RedisTemplate要添加如下配置

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import java.net.UnknownHostException;
/**
 * 修改Redis的默认的序列化器，将它放在容器中就可以了
 */
@Configuration
public class RedisConfig {

    /**
     * 使用@Bean注解标注的方法，参数会自动从容器中获取
     * 此处修改了默认的序列化方式，原本默认使用的是的java的序列化方式进行序列化的
     * @param redisConnectionFactory
     * @return
     * @throws UnknownHostException
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(
            RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        //修改默认的序列化方式
        template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

}

```



#### 4.导入maven依赖

```xml
<!--redis依赖配置-->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

#### 5.修改SpringBoot配置文件

在application.properties中添加Redis的配置及Redis中自定义key的配置。

```properties
spring.redis.host=47.94.110.49
spring.redis.database=0
spring.redis.port=6379

# 自定义redis key
redis.key.prefix.authCode = "portal:authCode:"
# 验证码超期时间
redis.key.expire.authCode = 120
```

#### 6. 通用的redis接口

```java
package com.gcb.learning.mybatisplus.pms.service;

/**
 * redis操作Service,
 * 对象和数组都以json形式进行存储
 */
public interface RedisService {
    /**
     * 存储数据
     */
    void set(String key, String value);

    /**
     * 获取数据
     */
    String get(String key);

    /**
     * 设置超期时间
     */
    boolean expire(String key, long expire);

    /**
     * 删除数据
     */
    void remove(String key);

    /**
     * 自增操作
     * @param delta 自增步长
     */
    Long increment(String key, long delta);

}

```

实现类

```java
package com.gcb.learning.mybatisplus.pms.service.impl;

import com.gcb.learning.mybatisplus.pms.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * redis操作Service的实现类
 */
@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    @Override
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public boolean expire(String key, long expire) {
        return stringRedisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    @Override
    public void remove(String key) {
        stringRedisTemplate.delete(key);
    }

    @Override
    public Long increment(String key, long delta) {
        return stringRedisTemplate.opsForValue().increment(key,delta);
    }
}

```

#### 7.controller层

```java
package com.gcb.learning.mybatisplus.pms.controller;

import com.gcb.learning.mybatisplus.pms.common.CommonResult;
import com.gcb.learning.mybatisplus.pms.service.UmsMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 会员登录注册管理Controller
 */
@Controller
@Api(tags = "UmsMemberController", description = "会员登录注册管理")
@RequestMapping("/sso")
public class UmsMemberController {
    @Autowired
    private UmsMemberService memberService;

    @ApiOperation("获取验证码")
    @RequestMapping(value = "/getAuthCode", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getAuthCode(@RequestParam String telephone) {
        return memberService.generateAuthCode(telephone);
    }

    @ApiOperation("判断验证码是否正确")
    @RequestMapping(value = "/verifyAuthCode", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updatePassword(@RequestParam String telephone,
                                 @RequestParam String authCode) {
        return memberService.verifyAuthCode(telephone,authCode);
    }
}

```

#### 8.service层

redisTemplate[接口参考](https://www.jianshu.com/p/7bf5dc61ca06/)

service接口

```java
package com.gcb.learning.mybatisplus.pms.service;


import com.gcb.learning.mybatisplus.pms.common.CommonResult;

/**
 * 会员管理Service
 */
public interface UmsMemberService {

    /**
     * 生成验证码
     */
    CommonResult generateAuthCode(String telephone);

    /**
     * 判断验证码和手机号码是否匹配
     */
    CommonResult verifyAuthCode(String telephone, String authCode);

}

```

service实现

```java
package com.gcb.learning.mybatisplus.pms.service.impl;

import com.gcb.learning.mybatisplus.pms.common.CommonResult;
import com.gcb.learning.mybatisplus.pms.service.RedisService;
import com.gcb.learning.mybatisplus.pms.service.UmsMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Random;

/**
 * 会员管理Service实现类
 */
@Service
public class UmsMemberServiceImpl implements UmsMemberService {
    @Autowired
    private RedisService redisService;
    @Value("${redis.key.prefix.authCode}")
    private String REDIS_KEY_PREFIX_AUTH_CODE;
    @Value("${redis.key.expire.authCode}")
    private Long AUTH_CODE_EXPIRE_SECONDS;

    @Override
    public CommonResult generateAuthCode(String telephone) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        //验证码绑定手机号并存储到redis
        redisService.set(REDIS_KEY_PREFIX_AUTH_CODE + telephone, sb.toString());
        redisService.expire(REDIS_KEY_PREFIX_AUTH_CODE + telephone, AUTH_CODE_EXPIRE_SECONDS);
        return new CommonResult().success(sb.toString()+"获取验证码成功");
    }


    //对输入的验证码进行校验
    @Override
    public CommonResult verifyAuthCode(String telephone, String authCode) {
        if (StringUtils.isEmpty(authCode)) {
            return new CommonResult().failed();
        }
        String realAuthCode = redisService.get(REDIS_KEY_PREFIX_AUTH_CODE + telephone);
        boolean result = authCode.equals(realAuthCode);
        if (result) {
            return new CommonResult().success("验证码校验成功");
        } else {
            return new CommonResult().failed();
        }
    }

}

```

## 参考博客

[在java中使用jedis操作redis](https://www.cnblogs.com/xuwujing/p/7536707.html)

[在java中使用StringRedisTempalge操作redis](https://www.cnblogs.com/slowcity/p/9002660.html)

 [redis常用命令](https://blog.csdn.net/ithomer/article/details/9213185)