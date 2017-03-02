# Hbase Master高可用(HA)
目标：为了避免单点故障，需要提高HBase Master的高可用性

1. HBase集群依赖于Zookeeper，一个实例中只能有一个HMaster，只有当一个HMaster挂掉了，Zookeeper服务器判断与当前HMaster通讯超时，然后才会去连接下一台HMaster
2. HBase本身不存储任何数据，实际数据存储在HDFS上，HDFS挂了，HBase也会跟着挂掉

其中包括：hbase-env.sh文件有个全局变量：
```
# 规定是否需要让HBase自己创建zookeeper实例，还是用系统存在的zookeeper实例
 export HBASE_MANAGES_ZK=false
```

故，有如下几个方法提高HBase Master的高可用，应该第三个比较方便，故着重讲backup Hmaster。

### 1. 通过zookeeper的Master Election机制
[参考1][1]  
在多台服务器上面启动HMaster，然后通过zookeeper的投票机制进行选择。

### 2. 使用HeartBeat
[参考2][2]  
节点之间用HeartBeat相互探测，其需要通过集群资源管理(Cluster Resource Manager)绑定用于启动和停止集群服务，选择Pacemaker

### 3. 使用backup Hmaster
[参考3][3]  [参考4][4]  [参考5][5]

步骤：
1. 在HMaster服务器安装目录下的conf文件中增加一个backup-masters文件，里面直接添加备用HMaster的IP地址
2. 在所有的备用HMaster中均添加该文件
3. 执行bin目录下面的，stop-hbase.sh和start-hbase.sh重启HBase集群
4. 可以在页面上（默认60010接口）上看到：bakcup Masters内容。例如：http://10.100.2.94:60010/master-status
5. 这时候，kill掉主Master的HMaster。例如：jps命令，找到HMaster的PID，然后kill该PID
6. 这时候会发现backup Master变成主的Master
7. 重启挂掉的HMaster，则会变为Backup Master


网上这方面的资料比较少，故追踪下配置文件：
1. 在hbase-config.sh中，读取配置文件
```
# List of hbase secondary masters.
HBASE_BACKUP_MASTERS="${HBASE_BACKUP_MASTERS:-$HBASE_CONF_DIR/backup-masters}"
```
2. 然后通过grep搜索bin文件中的HBASE_BACKUP_MASTERS关键词，可以发现master-backup.sh、rolling-restart.sh、和start-hbase.sh出现了该关键词
3. start-hbase.sh，rolling-restart.sh是传入参数调用master-backup.sh
```
--hosts "${HBASE_BACKUP_MASTERS}" stop master-backup
--hosts "${HBASE_BACKUP_MASTERS}" stop master-backup
--hosts "${HBASE_BACKUP_MASTERS}" $commandToRun master-backup
```
4. master-backup.sh中：
```
export HOSTLIST="${HBASE_BACKUP_MASTERS}"
if [ "$HOSTLIST" = "" ]; then
  if [ "$HBASE_BACKUP_MASTERS" = "" ]; then
    export HOSTLIST="${HBASE_CONF_DIR}/backup-masters"
  else
    export HOSTLIST="${HBASE_BACKUP_MASTERS}"
  fi
fi
```
可以看出所有的backup master都是传入到HOSTLIST中去。然后通过for循环，选择hmaster
```
if [ -f $HOSTLIST ]; then
  for hmaster in `cat "$HOSTLIST"`; do
   ssh $HBASE_SSH_OPTS $hmaster $"$args --backup" \
     2>&1 | sed "s/^/$hmaster: /" &
   if [ "$HBASE_SLAVE_SLEEP" != "" ]; then
     sleep $HBASE_SLAVE_SLEEP
   fi
  done
fi
```

因为我也只是上周四测试了下效果，具体也没有深入研究，如果上面内容存在什么错误或者不足，希望大家能多指教。
[Hbase的官方文档][6]

[1]: http://www.cnblogs.com/captainlucky/p/4710642.html
[2]: http://www.importnew.com/3020.html
[3]: http://xmaster.iteye.com/blog/1930271
[4]: http://blog.csdn.net/knowledgeaaa/article/details/47612897
[5]: http://www.cloudera.com/documentation/enterprise/5-5-x/topics/cdh_hag_hbase_config.html
[6]: https://hbase.apache.org/book.html
