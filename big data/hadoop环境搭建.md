> **手工置顶：命令行代理神器Proxychains**

因为公司分配了固定IP，且统一用代理IP访问外部网页，故在虚拟机和宿主机使用桥接模式进行连接后，虚拟机还需要单独配置Proxy，一般情况下通过在network中设置代理IP，通过/etc/profile中设置http_proxy中设置环境变量可以对http层面的访问进行代理，apt-get操作需要单独在/etc/apt中设置，而对于ping等命令行操作是没有效果的。
这里使用Proxychains能很好的解决该问题。
[参考1](http://www.tuicool.com/articles/rUNFF3)
[参考2](http://www.360doc.com/content/10/1007/12/296547_59036038.shtml)
[参考3](https://zhuanlan.zhihu.com/p/24358104)


### 1. vmware tools安装

本文使用的开发环境是Linux ubuntu kylin16.04，通过官方推荐的vmware tools安装过程中，执行vmware-install.pl是成功的，但reboot之后仍然无法启动工具，后台通过apt-get的方法远程下载了一个安装包，重启后成功完成工具的安装，具体方法如下：
> apt-get install open-vm-tools-desktop fuse
> reboot

### 2. 基于docker的hadoop集群安装
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
