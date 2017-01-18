## 1. 环境搭建

### 1.1 单机安装

[下载ES][1],然后直接解压缩。  

注：2.X需要需要JDK1.7,5.X需要JDK1.8，同时不能用root用户运行。

安装完成后,通过以下查看安装情况:
> curl -XGET 'http://dev-hadoop1:9200/?pretty'  

查看集群情况：
> curl -XGET 'http://dev-hadoop1:9200/_cluster/health?pretty'

### 1.2 文件配置+集群搭建
[参考配置][2],其中需要注意的：

1. cluster.name  
  集群名称，默认情况下ES会发现同一网段下，相同集群名字的其他ES，然后组成集群。原理是采用多播。

2. 	discovery.zen.ping.multicast.enabled: false  
discovery.zen.ping.unicast.hosts: ["dev-hadoop2", "dev-hadoop3"]  
    通过单播形式，指定集群内其他ES

3. network.host: 10.100.2.92
  设置服务器的IP地址

### 1.3 插件安装
[插件参考][3]

#### 1.3.1 head集群管理软件
直接安装：
> ./plugin install mobz/elasticsearch-head

如果有代理，将文件下载下来，用file导入本地地址，如：
> ./plugin install file:/home/hadoop/cluster/elasticsearch/elasticsearch-head-master.zip

可以通过./plugin list查看head插件是否安装成功，并通过查看：http://10.100.2.92:9200/_plugin/head/ 插件的运行情况，并可可视化操作ES中数据。

#### 1.3.2 bigdesk集群监控软件
直接安装， [参考][4]  
如果有代理：

1. 下载该zip，然后在plugin下面新建一个bigdesk目录，将其解压缩到文件里面的_site文件夹
2. 创建一个plugin-descriptor.properties，具体内容：
```
description=bigdesk  
version=bigdesk  
name=bigdesk  
site=true  
```
3. 修改Vim中_site/js/store/BigdeskStore.js中142行major == 1  改为  major >= 1

最后通过：http://10.100.2.92:9200/_plugin/bigdesk 查看ES集群的资源消耗情况，cpu，内存情况。

## 2. 原理
ElasticSearch和Solr本质上是基于Lucene来实现索引的查询过程。[两者性能区别][5]。  
应用：主要用于搜索和日志。其中[国内][6]主要有360，美团，58，苏宁等,国外主要有：维基百科，英国卫报，StackOverflow，GitHub。

特点：
1. 实时性：分布式实时文件存储和搜索，每一个字段都编入索引。
2. 可扩展性：默认集群，自带分布式协调管理功能，配置简单，可以扩展到上百台服务器，可处理PB级别的结构化或非结构化数据。
3. 容错性：采用Gateway概念，使得备份更简单；各节点组成对等网络结构，某个节点出现故障时会自动分配其他节点替代。
4. 只支持JSON文件格式；传统搜索没有Solr快

### 2.1 Lucene原理：

#### 2.1.1 基本概念：
* Document（载体，由多个Field组成）
* Field（包括name和value）
* Term（文本中一个词）
* Token（单个Term在所属Field中文本的呈现形式，包含Term的内容，类型，起始和偏移位置）

#### 2.1.2 数据结构
数据结构：倒排索引。即把索引中每个Term与相应的Document映射起来，即数据以Term为导向。
例如：
1. ElasticSearch Servier<doc 1>
2. Mastering ElasticSearch<doc 2>
3. Apache Solr 4 Cookbook(doc 3)

|Term|count|Docs|
|----|-----|----|
|4|1|<3>|
|Apache|1|<3>|
|Cookbook|1|<3>|
|ElasticSearch|2|<1><2>|
|Mastering|1|<1>|
|Server|1|<1>|
|Solr|1|<3>|

#### 2.1.3 实现：
通过文本分析组件analyzer将文本转换为Term。analyzer包括一个分词器（tokenizer）、若干过滤器（filter）和字符映射器（character mappers）。首先tokenier将文本拆成一个个Token，然后将其顺序排列，变成token stream，等待filter处理（例如大小写转换，去除乱码，同义词替换）。

索引被分为多个段（Segment），具有一次写入，多次读取的特点，写入后无法修改，但段可以通过配置策略或强制实现合并（segments merge）

搜索过程：用户输入的查询语句会被选中的查询解析器（query parser）解析，生成多个Query对象。可以通过AND，OR，NOT，+，-等将多个Term关联起来。

### 2.2 ElasticSearch原理：

#### 2.2.1 基本概念：

ES与关系型数据库对应关系：
* 关系数据库 >> 数据库 >> 表 >> 行 >> 列(Columns)  
* Elasticsearch >> 索引（Index） >> 类型（Type） >> 文档（Document，只能JSON格式） >> 域(Fields)

其他概念：
* 分片索引（Shard）：ES将数据分发到多个存储Lucene索引的物理机上，索引是自动完成的。
* 索引副本（Replica）：为索引片创建一份新的拷贝，可以跟原先主分片一样处理用户搜索请求。
* 时间之门（Gateway）：ES搜集集群的状态、索引的参数等信息，存储到Gateway中。

#### 2.2.2 分片的概念
索引只是一个逻辑命名空间（logical namespace），它指向一个或多个分片（shards）  

索引分片就是把索引数据切分成多个小的索引块，然后分发到同一个集群中的不同节点。检索时，检索结果就是该索引每个分片上检索结果的总和。

ES会为每个索引创建5个主分片，这样产生的冗余称为**过度分配**。这样当新节点加入后，ES可以自动对集群进行负载均衡，只需要转移部分索引，而不需要全部索引。

分片分为主分片（primary）和从分片（replica shard），每个Document属于一个主分片，从分片只是主分片的一个副本，提供数据备份和搜索等只读操作

设置分片：(设置3主分片，1个从分片)
```
PUT /blogs
{
 "settings" : {
    "number_of_shards" : 3,
    "number_of_replicas" : 1
 }
}
```

## 3. 使用

注意：
1. 建立索引时候，索引名必须均是小写，且不能以下划线开头，不能包含逗号。
2. 索引中包含三个部分依次是_index(文档存储的地方)和_type(文档代表的对象种类)和_id(文档的唯一编号)
3. Bulk操作：每一个子请求都会被单独执行，所以一旦有一个子请求失败了，并不会影响到其他请求的成功执行

### 3.1 Restful api
增：
```
PUT /megacorp/employee/1?op_type=create
{
    "first_name" : "John",
    "last_name" :  "Smith",
    "age" :        25,
    "about" :      "I love to go rock climbing",
    "interests": [ "sports", "music" ]
}
```
其中：PUT后面是/索引/类型/ID，下面是需要index的内容
op_type=create是保证是创建，而不是更新，如果存在同名文件则返回409
```
POST /megacorp/employee/
{
    "first_name" : "John",
    "last_name" :  "Smith",
    "age" :        25,
    "about" :      "I love to go rock climbing",
    "interests": [ "sports", "music" ]
}
```

如果ID不给定，则表示为自增长的生成  
每执行一次更新后_version变量就会自增1，created变量会变成false

更新：  
乐观并发控制：通过_version来进行管理
```
PUT /megacorp/employee/1
{
    "first_name" : "John",
    "last_name" :  "Smith",
    "age" :        25,
    "about" :      "I love to go rock climbing",
    "interests": [ "sports", "music" ]
}
```
更新部分内容：
```
POST /website/blog/1/_update
{
   "doc" : {
      "tags" : [ "testing" ],
      "views": 0
   }
}
```
删：
```
DELETE /megacorp/employee/1
```
查：  
* 精确查询  
hits表示匹配中的总数量，hits数组中默认值保存前十个数据，took表示这次搜索的执行时间，shards表示参与查询的分片总数，timeout表示查询是否超时
```
GET /_search ##返回所有文件
GET /megacorp/employee/1
GET /megacorp/employee/1?_source ##只显示字段，不显示元信息
GET /megacorp/employee/1?_source=title,text ##只会显示ttile，text属性
GET /gb,us/user,tweet/_search ##搜索索引gb和 索引us中类型user以及类型tweet内的所有文档
GET /_search?size=5&from=10 ## size表示每次返回多数数据，默认是10；from表示偏移
HEAD /megacorp/employee/1 ##检查数据是否存在
```
* 简易查询    
使用_search端口，没指定文档的情况下，默认反馈前10个数值.深入可以将参数传给q=
```
GET /megacorp/employee/_search
GET /megacorp/employee/_search?q=last_name:Smith
```
*  Query DSL搜索
```
GET /megacorp/employee/_search
{
    "query" : {
        "match" : {
            "last_name" : "Smith"
        }
    }
}
```
* Filter搜索
```
GET /megacorp/employee/_search
{
    "query" : {
        "filtered" : {
            "filter" : {
                "range" : {
                    "age" : { "gt" : 30 } <1>
                }
            },
            "query" : {
                "match" : {
                    "last_name" : "Smith" <2>
                }
            }
        }
    }
}
```

### 3.2 Java api
[官方参考][7]

#### 3.2.1 pom.xml
```
<dependency>
    <groupId>org.elasticsearch.client</groupId>
    <artifactId>transport</artifactId>
    <version>5.1.1</version>
</dependency>
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-api</artifactId>
    <version>2.7</version>
</dependency>
```

#### 3.2.2 curd
```
public class ElasticSearch {
    public static void main(String[] args) throws Exception {
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

        //插入数据
        IndexResponse response = client.prepareIndex("log", "log", "5")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("data", "2014060112124140")
                        .field("phoneNum", "13381795000")
                        .field("gid", "93161")
                        .field("ip", "180,173,194,118")
                        .endObject()
                ).get();

        String _index = response.getIndex();
        String _type = response.getType();
        String _id = response.getId();
        long _version = response.getVersion();
        RestStatus status = response.status();

        System.out.println("_index = " + _index + "_type = " + _type + "_id = " + _id + "_version = " + _version + "status = " + status);

     //查询精确数据
        GetResponse response_get = client.prepareGet("log", "log", "3").get();
        Map<String, Object> map = response_get.getSource();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }


        //查询多条数据
    MultiGetResponse multiGetItemResponses = client.prepareMultiGet()
                .add("log", "log", "1")
                .add("log", "log", "2", "3", "4","5")
                .get();

        for (MultiGetItemResponse itemResponse : multiGetItemResponses) {
            GetResponse response_multi = itemResponse.getResponse();
            if (response_multi.isExists()) {
                String json_multi = response_multi.getSourceAsString();
                System.out.println(json_multi);
            }
        }

        //条件查询
        MatchQueryBuilder qb = QueryBuilders.matchQuery("data", "2014060112124140");//使用Query DSL设计查询条件
        // 还可通过.setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(18)) 设置Filter

        SearchResponse response_search = client.prepareSearch("log")
                .setTypes("log")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(qb)
                .setFrom(0)
                .setSize(60)
                .get();
        System.out.println("查询结果：" + response_search + "命中数：" + response_search.getHits().getTotalHits());
        for (SearchHit hit : response_search.getHits()) {
            String id = hit.getId();
            System.out.println(id);
            Iterator<Map.Entry<String, Object>> rpItor = hit.getSource().entrySet()
                    .iterator();
            while (rpItor.hasNext()) {
                Map.Entry<String, Object> rpEnt = rpItor.next();
                System.out.println(rpEnt.getKey() + " : " + rpEnt.getValue());
            }
        }


        //删除数据
        DeleteResponse response_delete = client.prepareDelete("log", "log", "1").get();
        System.out.println(response_delete.status());

       //更新数据
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index("log");
        updateRequest.type("log");
        updateRequest.id("1");
        updateRequest.doc(jsonBuilder()
                .startObject()
                .field("gender", "male")
                .endObject());
        UpdateResponse updateResponse = client.update(updateRequest).get();

        client.close();
    }
}
```

#### 3.3 性能优化
1. 通过Filters实现缓存，过滤前的结果可以进行复用，可以通过_chache参数设置开启关闭缓存，可以通过_cache_key属性对缓存起别名，可以清除特定缓，参数中添加：?filter_keys=year_1981_cache'
2. 事先配置分片副本。最佳的分片数量取决于节点数量。
3. 通过路由提供信息决定分片存储和查询，其可以控制ES由哪个分片存储文档
> curl -XPUT localhost:9200/documents/doc/1?routing=A -d '{ "title" : "Document" }'

4. 通过批处理：MultiGet，MultiSearch

注：因为暂时不涉及到模糊查询，故打分机制的内容暂时不考虑。

[讲解原理教程][8]  
[非常好的教程][9]  
[官方教程][10]
[单节点准实时索引的实现][11]
[ES原理][12]
[gateway][13]
[应用][14]


[1]: https://www.elastic.co/downloads/elasticsearch
[2]: http://blog.csdn.net/guo_jia_liang/article/details/52979431
[3]: http://blog.csdn.net/guo_jia_liang/article/details/52980716
[4]: https://github.com/lukas-vlcek/bigdesk/tree/master
[5]: http://www.cnblogs.com/chowmin/articles/4629220.html
[6]: https://yq.aliyun.com/articles/66480?spm=5176.100240.searchblog.108.Xk6N7F
[7]: https://www.elastic.co/guide/en/elasticsearch/client/index.html
[8]: https://wizardforcel.gitbooks.io/mastering-elasticsearch/content/chapter-1/123_README.html
[9]: http://www.learnes.net/getting_started/installing_es.html
[10]: https://www.elastic.co/guide/index.html
[11]: http://www.aichengxu.com/os/2440253.htm
[12]: http://blog.csdn.net/asia_kobe/article/details/51942809
[13]: http://log.medcl.net/item/2010/09/translation-search-engine-and-the-time-machine/
[14]: http://www.36dsj.com/archives/61886