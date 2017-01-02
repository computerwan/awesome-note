# 非关系型数据库：
* SQL做扩展的方法：使用缓存（Memcached）+ 主从复制（读写分离，容灾备份）+ 集群
* NoSQL：非关系型数据库，数据存储不需要固定的模式，更利于横向扩展

### 1. 分类：
+ KV键值对：redis，memcache
+ 文档数据库：MongoDB
+ 列存储数据库：Cassandra，HBase
+ 图关系型数据库：Neo4J

### 2. CAP和BASE

+ C:Consistency(强一致性)
+ A：Availability（可用性）
+ P：Partition tolerance（分区容错性）

大多网站架构：AP，分布式的必须要实现P，为了保证网站不挂要保证A

BASE就是为了解决关系数据库中强一致性引起的问题而引起的：
+ 基本可用（Basically Available）
+ 软状态（Soft state）
+ 最终一致（Eventually consistent）

### 3. 分布式和集群
[分布式和集群的区别](http://os.51cto.com/art/201408/448272.htm)
1. 分布式是并联工作，集群是串联工作。
2. 分布式是指将不同业务分布在不同的地方，集群是将几台服务器集中在一起，实现同一个业务
3. 分布式：利用多台机器的硬盘和cpu来处理数据，提升性能
4. 集群：主从复制，读写分离

# mongoDB
优点：更易扩展列，还能存放数组，自带主键_id

### 1. 简单操作
* 数据库：
	* show dbs;//显示数据库集
	* db;//查看当前库
	* use db;//使用某个数据库，如果不存在则新建
	* db.dropDatabase();//删除当前数据库

* 集合：
	* show collections;//显示集合
	* db.c1.insert({name:"caoh"})//新建集合(显示创建)
	* db.createCollection("c2");//新建集合(隐示创建)

* 增加：
	* db.c1.insert({name:"jack",age:16})

* 查询：
	* db.c1.find().explain() 具体情况millis参数显示时间
	* db.c1.find({"name":"wanwu",age=11})//并条件
	* db.c3.find().count()//查看数量
	* db.c3.findOne();
	* db.c3.find({age:{$gt:5}})
	* db.c3.find).skip(0).limit(10);//分页
	* db.c3.find({age:{$all:[15,20]}})//数组操作，$in,$nin,$or
	* db.c3.find({sex:{$exists:1}})//包含sex属性的，不包含用0
	* db.queue_1538.find({"errorMsg":{$regex:"仍然无法解析"}})//正则查询

* 更新：
	* db.c1.update({age:16},{$set:{name:"lily"}},1,1)
		* param1：条件
		* param2：修改后对象，如果不使用{$set:}则覆盖之前结果
		* param3：如果不存在则插入数据，默认是0
		* param4：是否更新多个，默认是0
	* 注意要使用$set,否则是覆盖原来的结果，$inc是增加，$unset是删除原来的键

* 删除：
	* db.c1.remove({name:"jack",age:16})

### 2. 索引：
* db.system.indexes.find();//显示所有索引
* db.c3.ensureIndex({age:1},{"background":true})//为age在**后台建立索引**，非阻塞方式
* db.c3.dropIndex({age:1})//删除索引
* db.c3.ensureIndex({age:1},{unique:true})//建立唯一索引

### 3. 备份和固定集合
* mongodump -h dbhost -d dbname -o dbdirectory 备份
* mongorestore -h dbhost -d dbname -directoryperdb dbdirectory 恢复
* mongoexport:导出数据到txt xls wps都可以(需要增加-c参数)
* mongoimport：导入数据

固定集合：创建时候就指定集合大小，特征：像环形队列，如果空间不足，最早的文档就会被删除。
* demo：db.createCollection("collectionName",{capped:true,size:100000,max:100});

### 4. 数据安全
首先需要提供一个安全账户，在admin里面创建，然后通过该账户为其他用户分配collection的权利。
* use admin//首先需要一个系统用户，在admin里面的就是系统用户
* db.addUsr("root","1234") //获取一个系统用户
* use collectionName //切换到其他数据库
* db.addUser("wan","1234") //为该数据库添加用户
* db.addUser("xi","1234",true)//添加只读用户

开启服务检查
* 服务器端启动服务的时候添加： --auth
* 客户端登陆： db.auth("wan","1234")
* db.logout()

### 5. 集群和副本集
集群：
* 主从复制：从节点相当于主节点的一个镜像，主节点干什么事情，从节点保持一致。
* 读写分离：读数据从从节点(slave)读取。增删改用主节点(Master)
* 服务器端启动服务的时候添加： --master （从服务器：--slave --source master的ip和port）

副本集：就是有自动故障恢复功能的主从集群
* 主从集群和副本集最大的区别就是副本集没有固定的"主节点"；整个集群会选出一个"主节点"，当其挂掉后，又在剩下的从节点中选中其他节点为"主节点"
* 副本集总有一个活跃点(primary)和一个或多个备份节点(secondary)
* 服务器端启动服务的时候添加：
	* 主：--replSet c1/localhost:10002  --master
	* 其他：--replSet c1/localhost:10001  
	* 更多时候：--replSet c1/localhost:10001,localhost:10002（需要append地址后面）

### 6. 分布式存储：

* 分片(sharding)：将集合拆分成小块，让其分散在不同机器的过程。
* 需要在分片前运行一个路由进程：mongos，该路由器知道所有数据的存放位置，所以应用可以连接它来正常发送请求，通过路由进行转发。
* 设置分片的时候，需要从集合中选一个键，作为数据拆分的依据，称为片键（shard key），Mongo会重新平衡数据，使每片的流量都比较均衡，数据量也在合理范围内。

步骤：
1. 存放数据（4444,5555,6666）,存放配置（2222）都正常启动
2. 需要启动一个路由  mongos --port 3333 --configdb localhost:2222
3. 初始化配置：
	* 登录进入路由的admin：mongo localhost:3333/admin
	* 增加配置：db.runCommand({"addshard":"localhost:4444",allowLocal:true}) …都添加进去
		* 其中allowLocal的意思：允许客户端直接连到4444,5555,6666上面
4.启动操作：
* db.runCommand({"enablesharding":"test"})//启用
* db.runCommand({"shardcollection":"test.person","key":{age:1}})//指定片键

# Redis
高性能的(key/value)分布式内存数据库，内存型数据库

### 1. 数据类型
* string（jpg图片和序列化对象也能存储，最大512M）
* Hash（Value其实是一个键值对）
* list（链表实现，可以在头部或者尾部增加值）
* set（无序无重复，底层通过HashTable实现）
* sorted set（在每个元素前关联一个double类型分数，通过分数大小排序）

### 2. 配置文件redis.conf
includes|general|snapshotting|replication|security|limits|append only mode

### 3. 持久化
如果有aof和rdb，先指定aof，再执行rdb

* RDB(Redis DataBase)：
		* Redis会单独创建(fork)一个**子进程**来进行持久化，会将数据写入到一个临时文件中，待持久化过程都结束了，再用这个临时文件替换上次持久化好的文件。
		* 适合大规模数据恢复，但最后一次持久化数据可能丢失
		* 结果保存为dump.rdb，满足一定条件（配置：save the DB on the disk）或者shutdown，save（前台阻塞运行），bgsave（后台）时候都执行

* AOF(Append Only file)：
	* 以日志的形式来记录每个写操作，将Redis执行过的所有写指令都记录下来(不记录读指令)，只许追加文件但不可以改写文件，redis启动之初会读取该文件重新构建数据。
	* 结果保存为：appendonly.aof，当AOF内容超过一定阈值，就会进行压缩。

### 4. 事务
* 定义：可以一次执行多个命令，本质上是一组命令的集合。一个事务中的所有命令都会被序列化。按顺序地串行化执行而不会被其他命令插入，不许加塞。
* 命令：
	* MULTI：标记一个事务块的开始
	* EXEC：执行所有事务块内的命令
	* DISCARD：取消事务，放弃执行事务块内的所有命令
	* watch指令类似于乐观锁，如果值改变了需要先UNWATCH，再WATCH
		* UNWATCH:取消WATCH命令对所有key的监控
		* WATCH：监视一个或多个key

### 5. Redis的复制
步骤：
1. 配从不配主
2. 从库配置命令：slaveof 主库IP 主库端口
3. 修改配置文件细节操作（开启daemonize yes；pid文件名字；指定端口；Log文件名字；Dump.rdb名字）
只有主机能crud，从机只能读；主机死了恢复后照旧，从机死了要**重新配置**
复制原理：首次全量，之后都是增量


* 哨兵模式（sentinel）：


1. 建立sentinel.conf文件，名字绝对不能错
2. 配置哨兵：sentinel monitor被监控数据库的名字 127.0.0.1 6379 1
上面的1，表示主机挂掉了slave投票给让谁接替做主机，得票多少后成为主机
3. 启动哨兵
	redis-sentinel 目录/sentinel.conf
如果以前的主机挂掉后又回来了，挂在新主机下面
