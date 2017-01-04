## 1. HBase shell
### 1.1. 进入shell，进入后可以通过debug命令切换到debug模式,ctrel+c退出
> hbase shell  

### 1.2. 查建表:
	list
	create '表名','列族名1','列族名2','列族名3'
	describe '表名'
	----
	exists '表名'
	disable '表名' 让表先不可用
	is_enabled '表名' 查看表是否可用
	drop '表名' --删除前需要先让表不可用
	truncate '表名' 清空表

### 1.3. 增删改查：
	put '表名','rowkey','列族：列','值'
	get '表名','rowkey'
	get '表名','rowkey','列族'
	get '表名','rowkey','列族：列'
	count '表名'
	delete '表名','rowkey'，'列族：列'
	delete '表名','rowkey'
	scan '表名' --查看整个表
更新：进行覆盖，hbase没有修改，都是追加

## 2. HBase在java中应用

## 3. HBase原理
特点：仅支持单行事务；表的行列无限大；面向列存储和权限控制；null列不占据空间，故表可以设置的非常稀疏；数据都是字符串，没有类型  
Hbase返回的所有数据都是排序的：行，列族，列，时间戳（反向排序）

### 3.1 数据模型
通过Row Key + Column Family + Column Qualifier + Time Stamp来定位一个cell单元，Time Stamp确定不同版本
1. Row Key：主键，保存字节数组（byte[]），且按照**字典序**（byte order）排序存储
	* 通过该特性将经常一起读取的行放在一起，如果要保持自然序，需要用0进行左填充
	* 访问hbase中的行，只有三种方式:（1）通过单个row key访问 （2）通过row key的range访问 （3）全表扫描
2. Column Family:列族，和row key一样都是schema一部分，需要在表使用前定义，作为列的前缀
3. Column Qualifier：列，每个列必须包含在一个列族中
4. {row, column, version} 元组就是一个HBase中的一个 cell，不可分割
[hbase数据模型1](https://github.com/computerwan/awesome-note/blob/master/attachment/hbase%E6%95%B0%E6%8D%AE%E6%A8%A1%E5%9E%8B1.jpg)
[hbase数据模型2](https://github.com/computerwan/awesome-note/blob/master/attachment/hbase%E6%95%B0%E6%8D%AE%E6%A8%A1%E5%9E%8B2.jpg)

### 3.2 系统架构
由client，Zookeeper，HMaster，HRegionServer组成
[hbase系统架构图](https://github.com/computerwan/awesome-note/blob/master/attachment/hbase%E7%B3%BB%E7%BB%9F%E6%9E%B6%E6%9E%84%E5%9B%BE.jpg)
1. client通过RPC机制与HMaster和HRegionServer进行通信，管理是与HMaster，读写数据是与HRegionServer
2. Zookeeper存储了ROOT表(检索的索引表)，HMaster的地址，HRegionServer的注册信息，保证HRegionServer可以被HMaster感知（监控），且解决HMaster单点问题（集群只有一个master）
3. HMaster管理Table的增删改查，负责HRegionServer的负载均衡，扩展，容错
4. HRegionServer负责响应用户的I/O请求

### 3.3 存储原理
1. HRegion是Hbase中分布式存储和负载均衡的最小单元，同一个Hregion不可能存储在多台Server上。Table在行方向上分割为多个HRegion，开始默认只有一个，随着数据增加，超过一定阈值，HRegion会拆分为两个HRegion，然后不断增加。
2. HStore是存储的最小单元，一个或多个HStore组成一个HRegion。每个HStore保存一个Column Family，其又由一个MemStore和多个StoreFile组成。
3. storeFile以HFile格式保存在HDFS中，是HBase存储的核心。用户数据首先写入MemStore，当数据满了会Flush成一个storeFile，多个storeFile会进行Compact，split操作。
4. 其中通过HLog对象的WAL(Write Ahead Log)的类，进行恢复
[hbase存储原理图](https://github.com/computerwan/awesome-note/blob/master/attachment/hbase%E5%AD%98%E5%82%A8%E5%8E%9F%E7%90%86%E5%9B%BE.jpg)

### 3.4 存储格式
包括HFile和HLog File
1. HFile：KV格式，二进制格式文件,由6个部分组成
2. HLog File:Write Ahead Log的存储格式，物理上是Hadoop的Sequence File

### 3.5 关键算法和流程
Region定位，读写过程，Region Server上下线过程，Master上下线过程

* 写数据
	1. client向hregionserver发送写请求。
	2. hregionserver将数据写到hlog（write ahead log）。为了数据的持久化和恢复。
	3. hregionserver将数据写到内存（memstore）
	4. 反馈client写成功。
* 数据刷新
	1. 当memstore数据达到阈值（默认是64M），将数据刷到硬盘，将内存中的数据删除，同时删除Hlog中的历史数据。
	2. 并将数据存储到hdfs中。
	3. 在hlog中做标记点。
* 数据合并
	1. 当数据块达到4块，hmaster将数据块加载到本地，进行合并
	2. 当合并的数据超过256M，进行拆分，将拆分后的region分配给不同的hregionserver管理
	3. 当hregionser宕机后，将hregionserver上的hlog拆分，然后分配给不同的hregionserver加载，修改.META.    
	4. 注意：hlog会同步到hdfs
* 读数据
	1. 通过zookeeper和-ROOT- .META.表定位hregionserver。
	2. 数据从内存和硬盘合并后返回给client
	3. 数据块会缓存

## 4. HBase在MapReduce中使用
1. 实现方法  
Hbase对MapReduce提供支持，它实现了TableMapper类和TableReducer类，我们只需要继承这两个类即可。

	1、写个mapper继承TableMapper<Text, IntWritable>  
	参数：Text：mapper的输出key类型； IntWritable：mapper的输出value类型。  
		  其中的map方法如下：  
	map(ImmutableBytesWritable key, Result value,Context context)  
	 参数：key：rowKey；value： Result ，一行数据； context上下文  
	2、写个reduce继承TableReducer<Text, IntWritable, ImmutableBytesWritable>  
	参数：Text:reducer的输入key； IntWritable：reduce的输入value；  
	 ImmutableBytesWritable：reduce输出到hbase中的rowKey类型。  
		  其中的reduce方法如下：  
	reduce(Text key, Iterable<IntWritable> values,Context context)  
	参数： key：reduce的输入key；values：reduce的输入value；  

2. 准备表  
	1、建立数据来源表‘word’，包含一个列族‘content’  
	向表中添加数据，在列族中放入列‘info’，并将短文数据放入该列中，如此插入多行，行键为不同的数据即可  
	2、建立输出表‘stat’，包含一个列族‘content’  
	3、通过Mr操作Hbase的‘word’表，对‘content：info’中的短文做词频统计，并将统计结果写入‘stat’表的‘content：info中’，行键为单词  

```java
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
/**
* mapreduce操作hbase
* @author wilson
*
*/
public class HBaseMr {
    /**
     * 创建hbase配置
     */
    static Configuration config = null;
    static {
        config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", "slave1,slave2,slave3");
        config.set("hbase.zookeeper.property.clientPort", "2181");
    }
    /**
     * 表信息
     */
    public static final String tableName = "word";//表名1
    public static final String colf = "content";//列族
    public static final String col = "info";//列
    public static final String tableName2 = "stat";//表名2
    /**
     * 初始化表结构，及其数据
     */
    public static void initTB() {
        HTable table=null;
        HBaseAdmin admin=null;
        try {
            admin = new HBaseAdmin(config);//创建表管理
            /*删除表*/
            if (admin.tableExists(tableName)||admin.tableExists(tableName2)) {
                System.out.println("table is already exists!");
                admin.disableTable(tableName);
                admin.deleteTable(tableName);
                admin.disableTable(tableName2);
                admin.deleteTable(tableName2);
            }
            /*创建表*/
                HTableDescriptor desc = new HTableDescriptor(tableName);
                HColumnDescriptor family = new HColumnDescriptor(colf);
                desc.addFamily(family);
                admin.createTable(desc);
                HTableDescriptor desc2 = new HTableDescriptor(tableName2);
                HColumnDescriptor family2 = new HColumnDescriptor(colf);
                desc2.addFamily(family2);
                admin.createTable(desc2);
            /*插入数据*/
                table = new HTable(config,tableName);
                table.setAutoFlush(false);
                table.setWriteBufferSize(5);
                List<Put> lp = new ArrayList<Put>();
                Put p1 = new Put(Bytes.toBytes("1"));
                p1.add(colf.getBytes(), col.getBytes(), ("The Apache Hadoop software library is a framework").getBytes());
                lp.add(p1);
                Put p2 = new Put(Bytes.toBytes("2"));p2.add(colf.getBytes(),col.getBytes(),("The common utilities that support the other Hadoop modules").getBytes());
                lp.add(p2);
                Put p3 = new Put(Bytes.toBytes("3"));
                p3.add(colf.getBytes(), col.getBytes(),("Hadoop by reading the documentation").getBytes());
                lp.add(p3);
                Put p4 = new Put(Bytes.toBytes("4"));
                p4.add(colf.getBytes(), col.getBytes(),("Hadoop from the release page").getBytes());
                lp.add(p4);
                Put p5 = new Put(Bytes.toBytes("5"));
                p5.add(colf.getBytes(), col.getBytes(),("Hadoop on the mailing list").getBytes());
                lp.add(p5);
                table.put(lp);
                table.flushCommits();
                lp.clear();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(table!=null){
                    table.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * MyMapper 继承 TableMapper
     * TableMapper<Text,IntWritable>
     * Text:输出的key类型，
     * IntWritable：输出的value类型
     */
    public static class MyMapper extends TableMapper<Text, IntWritable> {
        private static IntWritable one = new IntWritable(1);
        private static Text word = new Text();
        @Override
        //输入的类型为：key：rowKey； value：一行数据的结果集Result
        protected void map(ImmutableBytesWritable key, Result value,
                Context context) throws IOException, InterruptedException {
            //获取一行数据中的colf：col
            String words = Bytes.toString(value.getValue(Bytes.toBytes(colf), Bytes.toBytes(col)));// 表里面只有一个列族，所以我就直接获取每一行的值
            //按空格分割
            String itr[] = words.toString().split(" ");
            //循环输出word和1
            for (int i = 0; i < itr.length; i++) {
                word.set(itr[i]);
                context.write(word, one);
            }
        }
    }
    /**
     * MyReducer 继承 TableReducer
     * TableReducer<Text,IntWritable>
     * Text:输入的key类型，
     * IntWritable：输入的value类型，
     * ImmutableBytesWritable：输出类型，表示rowkey的类型
     */
    public static class MyReducer extends
            TableReducer<Text, IntWritable, ImmutableBytesWritable> {
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values,
                Context context) throws IOException, InterruptedException {
            //对mapper的数据求和
            int sum = 0;
            for (IntWritable val : values) {//叠加
                sum += val.get();
            }
            // 创建put，设置rowkey为单词
            Put put = new Put(Bytes.toBytes(key.toString()));
            // 封装数据
            put.add(Bytes.toBytes(colf), Bytes.toBytes(col),Bytes.toBytes(String.valueOf(sum)));
            //写到hbase,需要指定rowkey、put
            context.write(new ImmutableBytesWritable(Bytes.toBytes(key.toString())),put);
        }
    }

    public static void main(String[] args) throws IOException,
            ClassNotFoundException, InterruptedException {
        config.set("df.default.name", "hdfs://master:9000/");//设置hdfs的默认路径
        config.set("hadoop.job.ugi", "hadoop,hadoop");//用户名，组
        config.set("mapred.job.tracker", "master:9001");//设置jobtracker在哪
        //初始化表
        initTB();//初始化表
        //创建job
        Job job = new Job(config, "HBaseMr");//job
        job.setJarByClass(HBaseMr.class);//主类
        //创建scan
        Scan scan = new Scan();
        //可以指定查询某一列
        scan.addColumn(Bytes.toBytes(colf), Bytes.toBytes(col));
        //创建查询hbase的mapper，设置表名、scan、mapper类、mapper的输出key、mapper的输出value
        TableMapReduceUtil.initTableMapperJob(tableName, scan, MyMapper.class,Text.class, IntWritable.class, job);
        //创建写入hbase的reducer，指定表名、reducer类、job
        TableMapReduceUtil.initTableReducerJob(tableName2, MyReducer.class, job);
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
```

### 参考：
[0.97.0官方文档中文版](http://abloz.com/hbase/book.html#standalone)  
[2.0官方文档](http://hbase.apache.org/book.html)

## 5.HBase新能调优（未完成）
