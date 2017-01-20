# hbase协处理器(Coprocessor)
Hbase0.92引入coprocessors，轻易建立二级索引，复杂过滤器，访问控制,[参考][1],[参考][3]


### 一. 工作原理
[工作原理][2]

分类：
1. 系统协处理器：全局导入region server上所有数据表
2. 表协处理器： 指定一张表使用协处理器

提供插件：
1. 观察者（observer）：类似于触发器，put操作前后分别有prePut和postPut两种钩子函数
  用户可以重载upcall方法，具体事件触发的callback方法由Hbase核心代码执行。
  三种观察者接口：
  * RegionObserver：提供客户端的数据操纵事件钩子：Get，Put,Delete,Scan等
  * WALObserver：提供WAL相关操作钩子
  * MasterObserver：提供DDL类型的操作钩子：创建、删除、修改数据表等

2. 终端（endpoint）：类似于存储过程,常用于聚集操作
  动态RPC插件的接口，实现代码被安装在服务器端，能够通过HBase RPC唤醒。

流程：
1. 定义一个新的protocol接口，必须继承CoprocessorProtocol.
2. 实现终端接口，该实现会被导入region环境执行。
3. 继承抽象类BaseEndpointCoprocessor.
4. 在客户端，终端可以被两个新的HBase Client API调用 。
  * 单个region：HTableInterface.coprocessorProxy(Class<T> protocol, byte[] row) 。
  * rigons区域：HTableInterface.coprocessorExec(Class<T> protocol, byte[] startKey, byte[] endKey, Batch.Call<T,R> callable)


启动：
1. 启动全局aggregation，能够操作所有表上面数据。修改hbase-site.xml文件
```xml
<property>
   <name>hbase.coprocessor.user.region.classes</name>
   <value>org.apache.hadoop.hbase.coprocessor.AggregateImplementation</value>
 </property>
```

2. 启动表aggregation，只对特定的表生效

```
hbase > disable 'mytable'
hbase > alter 'mytable',METHOD => 'table_att','coprocessor' =>'|org.apache.Hadoop.hbase.coprocessor.AggregateImplementation||'
hbase > enable 'mytable'
```


### 三. 案例实现

#### 3.1 统计行数：
```java
public class MyAggregationClient {
    private static final byte[] TABLE_NAME = Bytes.toBytes("mytable");
    private static final byte[] CF = Bytes.toBytes("vent");

    public static void main(String[] args) throws Throwable {
        Configuration customConf = new Configuration();
        customConf.setStrings("hbase.zookeeper.quorum", "node0,node1,node2");
        //提高RPC通信时长
        customConf.setLong("hbase.rpc.timeout", 600000);
        //设置Scan缓存
        customConf.setLong("hbase.client.scanner.caching", 1000);

        Configuration configuration = HBaseConfiguration.create(customConf);
        AggregationClient aggregationClient = new AggregationClient(configuration);
        Scan scan = new Scan();
        //指定扫描列族，唯一值
        scan.addFamily(CF);

        long rowCount = aggregationClient.rowCount(TABLE_NAME, null, scan);
        System.out.println("row count is " + rowCount);
    }
}
```

#### 3.2 使用Observer创建二级索引：
目的：查询指定店铺指定客户购买的订单
1. 首先有一张订单详情表，以被处理后的订单id作为rowkey
2. 以用户nick为rowkey的索引表
  rowkey ：dp_id+buy_nick1
  family : tid1:null tid2:null

使用：
1. 继承BaseRegionObserver类，实现prePut方法，在插入订单详情表之前，向索引表插入索引数据。
2. 查询时先在索引表里面获取tids，然后根据tids获取查询订单详情表

其中：put是需要写入的数据；WALEdit，writeToWAL都是跟日志相关；
```java
public class TestCoprocessor extends BaseRegionObserver {   
    @Override   
     public void prePut(final ObserverContext<RegionCoprocessorEnvironment> e,   
     final Put put, final WALEdit edit, final boolean writeToWAL)   
     throws IOException {   
         Configuration conf = new Configuration();   
         HTable table = new HTable(conf, "index_table");   
         List<KeyValue> kv = put.get("data".getBytes(), "name".getBytes());   
         Iterator<KeyValue> kvItor = kv.iterator();   
         while (kvItor.hasNext()) {   
             KeyValue tmp = kvItor.next();   
             Put indexPut = new Put(tmp.getValue());   
             indexPut.add("index".getBytes(), tmp.getRow(), Bytes.toBytes(System.currentTimeMillis()));   
             table.put(indexPut);   
         }   
         table.close();   
     }   
}   
```

### 四. 总结

注意：
1. 索引表示一张普通的hbase表，为安全考虑需要开启Hlog记录日志
2. 索引表的rowkey最好是不可变量，避免索引表中产生大量的脏数据
3. column是宽表，rowkey设计除了要考虑region均衡，也要考虑column数量，即表不要太宽。建议不超过3位数
4. 一个put操作实际上先后向两张表put数据，为了保证一致性，需要考虑异常处理，建议异常重试

[1]: http://blog.csdn.net/lifuxiangcaohui/article/details/39991183/
[2]: http://hbase.apache.org/book.html#coprocessors
[3]: http://www.ibm.com/developerworks/cn/opensource/os-cn-hbase-coprocessor1/index.html
