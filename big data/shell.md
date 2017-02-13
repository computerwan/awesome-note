### 1. 通过ssh命令实现粗略的时间同步

需要注意的问题：
1. 赋值语句的前后不能有空格
2. [从ssh获取结果的方法][1],注意使用single quotes，如果使用double quotes，远程的变量就会被本地的所取代
3. [时间计算][2],[格式][3]

```
#! /bin/bash
REMOTE_TIME=$(ssh root@10.100.2.92 'REMOTE_TIME=`date +%s`;echo $REMOTE_TIME')
echo "10.100.2.92 time is:"$REMOTE_TIME
LOCAL_TIME=`date +%s`
LOCAL_IP=`ifconfig eth0 | grep "inet addr" | awk '{ print $2}' | awk -F: '{print $2}'`
echo $LOCAL_IP" time is:"$LOCAL_TIME
DIFF_TIME=$(($REMOTE_TIME-$LOCAL_TIME))
echo $DIFF_TIME
if [ $DIFF_TIME -lt -600 ]||[ $DIFF_TIME -gt 600 ]; then
    TIME=$(ssh root@10.100.2.92 'TIME=`date +"%Y-%m-%d %H:%M:%S"`;echo $TIME')
    echo $TIME
    date --set "$TIME"
fi
```

### 2. 通过except实现简单的自动登录
需要事先安装except,[命令讲解][4]
```
#!/usr/bin/expect
set timeout 30
spawn ssh root@10.100.2.92
expect "password:"
send "123456\r"
interact
```

### 3. 通过配置秘钥，实现ssh的自动登录
1. 在A上生成公钥/私钥对
```$ ssh-keygen -t rsa -P ''```
敲击回车，在/home/hadoop下生成.ssh目录，.ssh下有id_rsa（私钥 ） 和id_rsa.pub（公钥 ）。

2. 把A下的id_rsa.pub复制到B下的~/.ssh/authorized_keys中
```$scp ~/.ssh/id_rsa.pub hadoop@remote_ip:/home/hadoop/id_rsa.pub.a```
在远程机器上，将公钥复制进去
```$cat id_rsa.pub.a >> ~/.ssh/authorized_keys。```


### 4. 使用ntpd和ntpdate实现时间的自动同步
```
yum install -y ntpdate
ntpdate us.pool.ntp.org
# 使用crontab实现同步命令
watch ntpq -p #查看同步的服务器情况
# 如果执行失败，需要启动ntpd或者ntpdate的service
```
[简单使用][5]
[常见错误][6]
[常见NTP服务器][7]

[1]: http://stackoverflow.com/questions/13976289/return-value-other-that-from-ssh-session
[2]: http://stackoverflow.com/questions/8903239/how-to-calculate-time-difference-in-bash-script
[3]:http://www.1987.name/203.html
[4]:http://www.cnblogs.com/lzrabbit/p/4298794.html
[5]:https://zhidao.baidu.com/question/1958660840127990300.html
[6]:http://www.blogjava.net/spray/archive/2008/07/10/213964.html
[7]:https://www.douban.com/note/171309770/
