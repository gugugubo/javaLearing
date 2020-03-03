# 一、基本概念

## 1、Near Realtime（NRT 近实时）

Elasticsearch 是一个近实时的搜索平台。这意味着从您索引一个文档开始直到它可以被查询时会有轻微的延迟时间（通常为一秒）。

## 2、Cluster（集群）

cluster（集群）是一个或者多个节点的集合，它们一起保存数据并且提供所有节点联合索引以及搜索功能。**集群存在一个唯一的名字身份且默认为** **elasticsearch**。这个名字非常重要，因为如果节点安装时通过它自己的名字加入到集群中的话，那么一个节点只能是一个集群中的一部分。

## 3、Node（节点）

node（节点）是一个单独的服务器，它是集群的一部分，存储数据，参与集群中的索引和搜索功能。像一个集群一样，一个节点通过一个在它启动时默认分配的一个随机的 UUID（通用唯一标识符）名称来识别。如果您不想使用默认名称您也可自定义任何节点名称。这个名字是要识别网络中的服务器对应这在您的 Elasticsearch 集群节点管理的目的是很重要的。

## 4、Index（索引）

索引是具有某种相似特征的文档的集合。例如，你可以有一个顾客数据索引，产品目录索引和订单数据索引。索引有一个名称（必须是小写的）标识，该名称用于在对其中的文档执行索引、搜索、更新和删除操作时引用索引。

## 5、Type（类型）

在 Index（索引）中，可以定义一个或多个类型。类似于MySQL中的Table；每一种类型的数据放在一起；

## 6、Document（文档）

保存在某个索引（Index）下，某种类型（Type）的一个数据（Document），文档是JSON格式的，Document就像是MySQL中的某个Table里面的内容

## 7、Shards & Replicas（分片 & 副本）

索引可以存储大量数据，可以超过单个节点的硬件限制。例如，十亿个文档占用了 1TB 的磁盘空间的单个索引可能不适合放在单个节点的磁盘上，并且从单个节点服务请求会变得很慢。

 为了解决这个问题，Elasticsearch提供了把 Index（索引）拆分到多个 Shard（分片）中的能力。在创建索引时，您可以简单的定义 Shard（分片）的数量。每个 Shard本身就是一个 fully-functional（全功能的）和独立“Index（索引）”，（Shard）它可以存储在集群中的任何节点上。

 Sharding（分片）非常重要两个理由是 : 

1）水平的拆分/扩展。

2）分布式和并行跨 Shard 操作（可能在多个节点），从而提高了性能/吞吐量。

在一个网络/云环境中随时都有可能出现故障，强烈推荐你有一个容灾机制。Elasticsearch允许你将一个或者多个索引分片复制到其它地方，这被称之为副本。复制之所以重要，有两个主要原因：

- 它提供了在一个shard/node失败是的高可用性。出于这个原因，很重要的一个点是一个副本从来不会被分配到与它复制的原始分片相同节点上。也就是说，副本是放到另外的节点上的。
- 它允许扩展搜索量/吞吐量，因为搜索可以在所有副本上并行执行。

每个索引可以被拆分成多个分片，一个索引可以设置 0 个（没有副本）或多个副本。开启副本后，每个索引将有主分片（被复制的原始分片）和副本分片（主分片的副本）。分片和副本的数量在索引被创建时都能够被指定。在创建索引后，您也可以在任何时候动态的改变副本的数量，但是不能够改变分片数量。

# 二、初步检索

## 1、_cat_

_GET /_cat/nodes：查看所有节点

GET /_cat/health：查看es健康状况

GET /_cat/master：查看主节点

GET /_cat/indices：查看所有索引

 

## 2、索引一个文档

保存一个数据，保存在哪个索引的哪个类型下，指定用哪个唯一标识

PUT customer/external/1；在customer索引下的external类型下保存1号数据为

```json
PUT customer/external/1
{
  "name": "John Doe"
}

```

PUT和POST都可以，

POST新增。如果不指定id，会自动生成id。指定id就会修改这个数据，并新增版本号PUT可以新增可以修改。PUT必须指定id；由于PUT需要指定id，我们一般都用来做修改操作，不指定id会报错。

## 3、查询文档

```json
   GET customer/external/1
```



## 4、更新文档

```json
POST customer/external/1/_update
{
  "doc":{
     "name": "John Doew"
  }
}
或者
PUT customer/external/1
{
  "name": "John Doe"
}
或者
POST customer/external/1
{
  "name": "John Doe2"
}

```

不同：POST操作会对比源文档数据，如果相同不会有什么操作，文档version不增加, PUT操作总会将数据重新保存并增加version版本；带_update对比元数据如果一样就不进行任何操作。

看场景选择操作，对于大并发更新，不带update；对于大并发查询偶尔更新，带update, 对比更新，重新计算分配规则。

```json
//更新同时增加属性
POST customer/external/1/_update
{
  "doc": { "name": "Jane Doe", "age": 20 }
}
//简单脚本更新
POST customer/external/1/_update
{
  "script" : "ctx._source.age += 5"
}
```



## 5、删除文档&索引

| DELETE customer/external/1 |
| -------------------------- |
| DELETE customer            |

 

## 6、bulk批量API

```json
POST customer/external/_bulk
{"index":{"_id":"1"}}
{"name": "John Doe" }
{"index":{"_id":"2"}}
{"name": "Jane Doe" }

语法格式：
{ action: { metadata }}\n
{ request body        }\n

{ action: { metadata }}\n
{ request body        }\n
复杂实例：
POST /_bulk
{ "delete": { "_index": "website", "_type": "blog", "_id": "123" }} 
{ "create": { "_index": "website", "_type": "blog", "_id": "123" }}
{ "title":    "My first blog post" }
{ "index":  { "_index": "website", "_type": "blog" }}
{ "title":    "My second blog post" }
{ "update": { "_index": "website", "_type": "blog", "_id": "123", "_retry_on_conflict" : 3} }
{ "doc" : {"title" : "My updated blog post"} }


```

bulk API 以此按顺序执行所有的 action（动作）。如果一个单个的动作因任何原因而失败，它将继续处理它后面剩余的动作。当 bulk API 返回时，它将提供每个动作的状态（与发送的顺序相同），所以您可以检查是否一个指定的动作是不是失败了。

## 7、样本测试数据

我准备了一份顾客银行账户信息的虚构的 JSON 文档样本。每个文档都有下列的 schema（模式）:

```json
{
    "account_number": 0,
    "balance": 16623,
    "firstname": "Bradshaw",
    "lastname": "Mckenzie",
    "age": 29,
    "gender": "F",
    "address": "244 Columbus Place",
    "employer": "Euron",
    "email": "bradshawmckenzie@euron.com",
    "city": "Hobucken",
    "state": "CO"
}

```

https://github.com/elastic/elasticsearch/blob/master/docs/src/test/resources/accounts.json?raw=true 导入测试数据

POST bank/account/_bulk

# 三、进阶检索

ES支持两种基本方式检索 :

1. 一个是通过使用 REST request URI  发送搜索参数（uri+检索参数）
2. 另一个是通过使用  REST request body 来发送它们（uri+请求体）

## 1）、检索信息

一切检索从_search开始

```
GET bank/_search

GET bank/_search?q=*&sort=account_number:asc  
//我们在"bank"索引中检索，q=*参数表示匹配所有文档；sort=account_number:asc表示每个文档的account_number字段
//升序排序；pretty参数表示返回漂亮打印的JSON结果。
响应结果解释：
took - Elasticsearch 执行搜索的时间（毫秒）
time_out - 告诉我们搜索是否超时
_shards - 告诉我们多少个分片被搜索了，以及统计了成功/失败的搜索分片
hits - 搜索结果
hits.total - 搜索结果
hits.hits - 实际的搜索结果数组（默认为前 10 的文档）
sort - 结果的排序 key（键）（没有则按 score 排序）
score 和 max_score –相关性得分和最高得分（全文检索用）

```

uri+请求体进行检索

```json
GET bank/_search
{
  "query": {
    "match_all": {}
  },
  "sort": [
    {
      "account_number": {
        "order": "desc"
      }
    }
  ]
}

```

## 2、Query DSL

### 1）、基本语法格式

Elasticsearch 提供了一个可以执行查询的 Json 风格的 DSL**（**domain-specific language 领域特定语言）。这个被称为 [**Query DSL**](https://www.elastic.co/guide/en/elasticsearch/reference/5.0/query-dsl.html)。该查询语言非常全面，并且刚开始的时候感觉有点复杂，真正学好它的方法是从一些基础的示例开始的。

l  一个查询语句 的典型结构

```json
{
    QUERY_NAME: {
        ARGUMENT: VALUE,
        ARGUMENT: VALUE,...
    }
}
```

l  如果是针对某个字段，那么它的结构如下：

```json
{
    QUERY_NAME: {
        FIELD_NAME: {
            ARGUMENT: VALUE,
            ARGUMENT: VALUE,...
        }
    }
}

```

```
GET bank/_search
{
  "query": {
    "match_all": {}
  },
  "from": 0,
  "size": 5,
  "sort": [
    {
      "account_number": {
        "order": "desc"
      }
    }
  ]
}
	query 定义如何查询，
	match_all 查询类型【代表查询所有的所有】，es中可以在query中组合非常多的查询类型完成复杂查询
	除了 query 参数之外，我们也可以传递其它的参数以改变查询结果。如sort，size，注意：如果size没有指定，则默认是10
   from+size限定，可完成分页功能, 并且from参数（从0开始）指定从哪个文档索引开始，并且size参数指定从from开始返回多少条。这个特性在分页查询时非常有用。注意：如果没有指定from，则默认从0开始
	sort排序，多字段排序，会在前序字段相等时后续字段内部排序，否则以前序为准


```

### 2) 、 返回部分字段

```json
GET bank/_search
{
  "query": {
    "match_all": {}
  },
  "from": 0,
  "size": 5,
  "_source": ["age","balance"]
}

```

### 3）、match【匹配查询】

l  基本类型（非字符串），精确匹配

```json
GET bank/_search
{
  "query": {
    "match": {
      "account_number": "20"
    }
  }
}
//match返回account_number=20的
```

l  字符串，全文检索

```json
GET bank/_search
{
  "query": {
    "match": {
      "address": "mill"
    }
  }
}
//最终查询出address中包含mill单词的所有记录
//match当搜索字符串类型的时候，会进行全文检索，并且每条记录有相关性得分。

```

l  字符串，多个单词（分词+全文检索）

```json
GET bank/_search
{
  "query": {
    "match": {
      "address": "mill road"
    }
  }
}
//最终查询出address中包含mill或者road或者mill road的所有记录，并给出相关性得分
```

### 4）、match_phrase【短语匹配】

将需要匹配的值当成一个整体单词（不分词）进行检索

```json
GET bank/_search
{
  "query": {
    "match_phrase": {
      "address": "mill road"
    }
  }
}
//查出address中包含mill road的所有记录，并给出相关性得分
```

### 5）、multi_match【多字段匹配】

```json
GET bank/_search
{
  "query": {
    "multi_match": {
      "query": "mill",
      "fields": ["state","address"]
    }
  }
}
//state或者address包含mill
//term,terms是用来精确匹配的，trems可以放数组，对于一些精确的值的查询可以用tern。tern用于那些值不是文本的，如age:22,status:true。 但是如name:" "字符串不能用来term，term某个字段必须匹配某个值，terms可以匹配某个字段的多个值  注：每一个text类型的字段，都有一个 .keyword字段，代表非text类型，可以用来精确匹配

```

![1580993094008](D:\BaiduNetdiskDownload\markdown图片\1580993094008.png)

### 6）、bool【复合查询】

bool用来做复合查询：

复合语句可以合并 任何 其它查询语句，包括复合语句，了解这一点是很重要的。这就意味着，复合语句之间可以互相嵌套，可以表达非常复杂的逻辑。

#### 6.1 must

l  **must**：必须达到must列举的所有条件

```json
GET bank/_search
{
  "query": {
    "bool": {
      "must": [
        { "match": { "address": "mill" } },
        { "match": { "gender": "M" } }
      ]
    }
  }
}

```

#### 6.2 should

**should**：应该达到should列举的条件，如果达到会增加相关文档的评分，并不会改变查询的结果。如果query中只有should且只有一种匹配规则，那么should的条件就会被作为默认匹配条件而去改变查询结果

```json
GET bank/_search
{
  "query": {
    "bool": {
      "must": [ 
{ "match": { "address": "mill" } },
        { "match": { "gender": "M" } }
      ],
      "should": [
        {"match": { "address": "lane" }}
      ]
    }
  }
}


```

#### 6.3 must_not

**must_not**必须不是指定的情况

```json
GET bank/_search
{
  "query": {
    "bool": {
      "must": [
        { "match": { "address": "mill" } },
        { "match": { "gender": "M" } }
      ],
      "should": [
        {"match": { "address": "lane" }}
      ],
      "must_not": [
        {"match": { "email": "baluba.com" }}
      ]
    }
  }
}
//address包含mill，并且gender是M，如果address里面有lane最好不过，但是email必须不包含baluba.com
```

#### 6.4 filter

filter【结果过滤】并不是所有的查询都需要产生分数，特别是那些仅用于 “filtering”（过滤）的文档。为了不计算分数 Elasticsearch** 会自动检查场景并且优化查询的执行。

```json
GET bank/_search
{
  "query": {
     "bool": {
       "must": [
         {"match": { "address": "mill"}}
       ],
       "filter": {
         "range": {
           "balance": {
             "gte": 10000,
             "lte": 20000
           }
         }
       }
     }
  }
}

```

### 7）、aggregations（执行聚合）

聚合提供了从数据中分组和提取数据的能力。最简单的聚合方法大致等于 **SQL GROUP BY** 和 **SQL** 聚合函数。在 **Elasticsearch** 中，您有执行搜索返回 hits（命中结果），并且同时返回聚合结果，把一个响应中的所有 hits（命中结果）分隔开的能力。这是非常强大且有效的，您可以执行查询和多个聚合， 并且在一次使用中得到各自的（任何一个的）返回结果，使用一次简洁和简化的 API 来避免网络往返。

####  7.1 查询一

l  搜索address中包含mill的所有人的年龄分布以及平均年龄，但不显示这些人的详情。

```
GET bank/_search
{
  "query": {
    "match": {
      "address": "mill"
    }
  },
  "aggs": {
    "group_by_state": {
      "terms": {`
        "field": "age"
      }
    },
    "avg_age": {
      "avg": {
        "field": "age"
      }
    }
  },
  "size": 0
}
//size：0  不显示搜索数据
//aggs：执行聚合。聚合语法如下
"aggs": {
    "aggs_name这次聚合的名字，方便展示在结果集中": {
      "AGG_TYPE聚合的类型（avg,term,terms）（terms用来分组）": {}
    }
  },

```

#### 7.2 查询二

按照年龄聚合，并且请求这些年龄段的这些人的平均薪资

```json
GET bank/account/_search
{
  "query": {
    "match_all": {}
  },
  "aggs": {
    "age_avg": {
      "terms": {
        "field": "age",
        "size": 1000
      },
      "aggs": {
        "banlances_avg": {
          "avg": {
            "field": "balance"
          }
        }
      }
    }
  }
  ,
  "size": 1000
}

```

#### 7.3 查询三

复杂：查出所有年龄分布，并且这些年龄段中M的平均薪资和F的平均薪资以及这个年龄段的总体平均薪资

```json
GET bank/account/_search
{
  "query": {
    "match_all": {}
  },
  "aggs": {
    "age_agg": {
      "terms": {
        "field": "age",
        "size": 100
      },
      "aggs": {
        "gender_agg": {
          "terms": {
            "field": "gender.keyword",
            "size": 100
          },
          "aggs": {
            "balance_avg": {
              "avg": {
                "field": "balance"
              }
            }
          }
        },
        "balance_avg":{
          "avg": {
            "field": "balance"
          }
        }
      }
    }
  }
  ,
  "size": 1000
}

```

## 四.  知识点总结：

1.

![1580996778987](D:\BaiduNetdiskDownload\markdown图片\1580996778987.png)

2. 匹配查询中的term,terms是用来精确匹配的，trems可以放数组，对于一些精确的值的查询可以用tern。tern用于那些值不是文本的，如age:22,status:true。 但是如name:" "字符串不能用来term，term某个字段必须匹配某个值，terms可以匹配某个字段的多个值  注：每一个text类型的字段，都有一个 .keyword字段，代表非text类型，可以用来精确匹配

   ```json
   POST _search
   {
     "query": {
       "bool" : {
         "must": [
           {
             "term": {
               "productCategoryName.keyword": {
                 "value": "手机通讯"
               }
             }
           }
         ]
       }
     }
   }
   
   
   ```

   ```json
   POST _search
   {
     "query": {
       "bool" : {
         "must": [
           {
             "terms": {
               "id": [
                 "26",
                 "27",
                 "22"
               ]
             }
           }
         ]
       }
     }
   }
   ```

   

而在聚合查询中terms用来分组

## 五. 参考文档：

[官方文档](https://www.elastic.co/guide/index.html)

[一些详细知识点文档](https://www.cnblogs.com/xing901022/p/4704319.html)

[快速入门博客](https://www.cnblogs.com/cjsblog/p/9439331.html)

[快速入门视频](https://www.bilibili.com/video/av76369486?p=67)



