### 一、基本类介绍
[源码分析][1]

#### 1.1 HTable，HTableInterface
HTablePool过时，通过HConnectionManager来替代

```java
HBaseConfiguration conf =  (HBaseConfiguration) HBaseConfiguration.create();
byte[] rowkey = Bytes.toBytes("cenyuhai");
byte[] family = Bytes.toBytes("f");
byte[] qualifier = Bytes.toBytes("name");
byte[] value = Bytes.toBytes("岑玉海");

HTable table = new HTable(conf, "test");
```

#### 1.1 PUT
```java
Put put = new Put(rowkey);
put.add(family,qualifier,value);
table.put(put);
```

#### 1.2 GET
```java
Get get = new Get(rowkey);
Result row = table.get(get);
```

#### 1.3 DELETE
```java
Delete del = new Delete(rowkey);
table.delete(del);
```

#### 1.4 SCAN
1. 可以通过设置：Batch和cache来提高效率  
RPCs = (Rows * Cols per Row) / Min(Cols per Row, Batch Size)/ Scanner Caching
2. setStartRow和setStopRow设置范围
3. setIsolationLevel设置隔离级别
4. RowFilter是行键过滤器，可以传入运算符和[比较器][2],实现正则，字符串，二进制等比较

```java
Scan scan = new Scan();
        //scan.setTimeRange(new Date("20140101").getTime(), new Date("20140429").getTime());
        scan.setBatch(10);
        scan.setCaching(10);
        scan.setStartRow(Bytes.toBytes("cenyuhai-00000-20140101"));
        scan.setStopRow(Bytes.toBytes("cenyuhai-zzzzz-201400429"));
        //如果设置为READ_COMMITTED，它会取当前的时间作为读的检查点，在这个时间点之后的就排除掉了
        scan.setIsolationLevel(IsolationLevel.READ_COMMITTED);
        RowFilter rowFilter = new RowFilter(CompareOp.EQUAL, new RegexStringComparator("pattern"));
        ResultScanner resultScanner = table.getScanner(scan);
        Result result = null;
        while ((result = resultScanner.next()) != null) {
            //自己处理去吧...
        }
```


#### 1.4 WAL
1. 客户端发送的每一次修改都会被封装到一个WALEdit实例中，通过日志级别来管理原子性。


### 二、程序实例



#### 零、补充
* HBaseDao（通过静态变量存放已经存在的表信息） --> HbaseClient（对Scan，Put..等进行包装，增加缓存） --> BasicService（CRUD）；ComplexService；CoprocessorService（统计，聚合）
* QueryExtInfo（参数，版本，时间戳，聚合，过滤等）；TableInfoHolder（表信息处理）

调用时候，直接调用HbaseDao


[1]: http://www.aboutyun.com/thread-7644-1-1.html
[2]: http://blog.chinaunix.net/uid-77311-id-4617954.html
