> **手工置顶：命令行代理神器Proxychains**

因为公司分配了固定IP，且统一用代理IP访问外部网页，故在虚拟机和宿主机使用桥接模式进行连接后，虚拟机还需要单独配置Proxy，一般情况下通过在network中设置代理IP，通过/etc/profile中设置http_proxy中设置环境变量可以对http层面的访问进行代理，apt-get操作需要单独在/etc/apt中设置，而对于ping等命令行操作是没有效果的。  
这里使用Proxychains能很好的解决该问题。  
[参考1](http://www.tuicool.com/articles/rUNFF3)  
[参考2](http://www.360doc.com/content/10/1007/12/296547_59036038.shtml)  
[参考3](https://zhuanlan.zhihu.com/p/24358104)  
补充：apt-get的代理设置，在/etc/apt下面建一个文件apt.conf,里面增加如下内容（注意必须有;）
> Acquire::http::proxy "http://10.100.10.100:3128";

### 1. vmware tools安装

本文使用的开发环境是Linux ubuntu kylin16.04，通过官方推荐的vmware tools安装过程中，执行vmware-install.pl是成功的，但reboot之后仍然无法启动工具，后台通过apt-get的方法远程下载了一个安装包，重启后成功完成工具的安装，具体方法如下：
> apt-get install open-vm-tools-desktop fuse
> reboot


### 2. 下载和环境变量配置
首先需要安装JDK1.6以上的版本，并配置环境变量JAVA_HOME

[Hadoop官方下载镜像](http://hadoop.apache.org/releases.html)  
[Zookeeper官方下载镜像](http://zookeeper.apache.org/releases.html)  
[Hbase官方下载镜像](http://www.apache.org/dyn/closer.cgi/hbase/)

下载完成后，分别解压缩：
> tar xzf hadoop-x.y.z.tar.gz

到/usr/local文件夹下面,并将命令配置进/etc/profile,如下所示：
```
export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64
export HADOOP_INSTALL=/usr/local/hadoop
export ZOOKEEPER_INSTALL=/usr/lcaol/zookeeper
export HBASE_HOME=/usr/local/hbase
export CLASSPATH=.:$JAVA_HOME/lib:$JAVA_HOME/jre/lib:$CLASSPATH  
export PATH=$JAVA_HOME/bin:$JAVA_HOME/jre/bin:$HBASE_HOME/bin:$HADOOP_INSTALL/sbin:$HADOOP_INSTALL/bin:$ZOOKEEPER_INSTALL/bin:$PATH  
```
配合完成后，可以通过source /etc/profile刷新一下，并可通过echo $PATH检查变量是否声明成功。

### 3. Hadoop
  通过一下命令判断是否工作：
>hadoop version

### 4. Zookeeper
在运行服务之前，需要手动创建一个配置文件zoo.cfg放在Zookeeper/conf目录下面，具体内容如下：
```
tickTime=2000  
dataDir=/Users/wan/zookeeper  
clientPort=2181
```

其中tickTime是基本时间单元（单位毫秒）；dataDir是存储持久数据的本地文件系统位置；clientPort指定的监听客户端连接的端口

然后执行
> zkServer.sh start

发送ruok命令，如果返回imok则说明运行成功
> echo ruok|nc localhost 2181

其中ruok可以换成conf，envi，srvr，stat，srst，isro等信息返回服务器的基本状态（具体参见532页）

### 5. Hbase
启动一个**standalone**的Hbase实例:
> start-hbase.sh

进入Hbase的shell环境：
> hase shell
> list

在执行此任务之前，需要启动zkServer，如果没启动，在log中会输出：java.net.ConnectException: Connection refused的错误，启动后会出现端口2181端口被占用的错误:

> Could not start ZK at requested port of 2181. ZK was started at port:2182. Aborting as clients(e.g. shell) will not be able to find this ZK quorum.

所有对于standalone的zk需要进行一下配置：  
hbase-env.sh
```
export HBASE_MANAGES_ZK=false
```

hbase-site.xml  
 **hbase.zookeeper.quorum**是设置集群，如果分布式的用","分隔，这里单机版采用localhost
 hbase.rootdir设置Hbase数据的写入地址，默认是/tmp/hbase-${user.name}
```
<property>
  <name>hbase.zookeeper.quorum</name>
  <value>localhost</value>
  <description>The directory shared by RegionServers.
  </description>
</property>
<property>
  <name>hbase.zookeeper.property.dataDir</name>
  <value>/Users/wan/zookeeper</value>
  <description>Property from ZooKeeper config zoo.cfg.
  The directory where the snapshot is stored.
  </description>
</property>
<property>
  <name>hbase.rootdir</name>
  <value>file:///opt/hbase-0.98.20-hadoop2/data</value>
  <description>The directory shared by RegionServers.
  </description>
</property>
<property>
  <name>hbase.cluster.distributed</name>
  <value>true</value>
  <description>The mode the cluster will be in. Possible values are
    false: standalone and pseudo-distributed setups with managed Zookeeper
    true: fully-distributed with unmanaged Zookeeper Quorum (see hbase-env.sh)
  </description>
</property>
```

Java客户端中HBaseConfiguration会去寻找并读取配置文件hbase-site.xml和hbase-default。

### 6. 基于docker的hadoop集群安装
主要参考:
[hadoop-cluster-docker](https://github.com/kiwenlau/hadoop-cluster-docker)

1. pull docker image
> sudo docker pull kiwenlau/hadoop:1.0

2. clone github repository
> git clone https://github.com/kiwenlau/hadoop-cluster-docker

3. create hadoop network
> sudo docker network create --driver=bridge hadoop

4. start container
> cd hadoop-cluster-docker
sudo ./start-container.sh

output:
>start hadoop-master container...
start hadoop-slave1 container...
start hadoop-slave2 container...
root@hadoop-master:~#

启动后直接进入hadoop-master容器当中，可以直接进行操作，此时如果不小心退出容器，可以通过如下命令重新进入容器：
> sudo docker attach hadoop-master
