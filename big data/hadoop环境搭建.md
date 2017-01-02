### 1. 前言
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
