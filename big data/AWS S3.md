### 1. aws S3在Java中使用的Demo
Amazon S3是一个面向Internet的存储服务。

[参考](https://javatutorial.net/java-s3-example)

step1：导入依赖
```xml
<dependency>
    <groupId>com.amazonaws</groupId>
    <artifactId>aws-java-sdk</artifactId>
    <version>1.9.2</version>
</dependency>
```
step2：在安全证书中，创建访问密钥，推荐在IAM服务中创建单独的IAM用户，而不是使用根访问密钥

step3：Java程序编写
```java
AWSCredentials credentials = new ProfileCredentialsProvider().getCredentials();//导入密钥
AmazonS3 s3client = new AmazonS3Client(credentials);//创建client
```
可以通过client创建、显示和删除bucket，创建文件夹，上传和删除文件等操作。

[详见参考代码](https://github.com/computerwan/Java/blob/master/training/S3Service/src/main/java/S3SimpleDemo.java)

### 2.连接补充（S3Utils）

##### 1. ClientConfiguration
首先可以看下AmazonS3Client的重载函数，可以通过传入一个ClientConfiguration参数，这个配置文件可以设置Protocol（默认是https），proxy，maxConnections，connectionTimeout，useGzip等参数，AmazonS3Client本身维护了一个HTTP connection pool

```java
ClientConfiguration clientConfig = new ClientConfiguration();
clientConfig.setProtocol(Protocol.HTTP);
AmazonS3Client s3Client = new AmazonS3Client(credentials, clientConfig);
```
##### 2. AmazonS3Client
* 设置endpoint：默认是s3.amazonaws.com，但如果是自己搭建的环境，则修改为自己的URL地址
* 设置setS3ClientOptions：设置request access style，s3支持virtual-hosted-style和path-style access（默认不开启），path-style access即通过URL能访问到文件

```java
s3Client.setEndpoint(endpoint);
s3Client.setS3ClientOptions(new S3ClientOptions().withPathStyleAccess(true));
```

### 3. 代码分析
  [原码](https://github.com/computerwan/Java/tree/master/training/S3Service/src/main/java/s3Service)

  1. 将需要上传的内容包装成一个UploadTask类，其中需要包括上传文件的基本信息，status表示当前的状态，同时需要一个原子类tryCounter表示已重试次数
  2. 通过UploadConfig类定义一些常见配置参数，包括地址，并发数，上传工作队列的最大容量，重试次数和重试等待时间，失败等待时间，无任务休眠时间等。
  3. 通过FileUploadThreadTemplate继承Thread，然后重写run方法来实现多线程；MongoFileUploadThread将Service与Dao相连
  4. UploadTaskService接口和AsyncUploadTaskServiceImpl实现主要负责添加任务的工作
  5. UploadTaskDao接口和MongoUploadTaskDaoImpl主要是通过MongoTemplate与Mongo做交互

### 4. 实际生产中遇到的问题
1. 如何实现多线程并发上传，并共享AmazonS3Client的使用
> AmazonS3Client 本身就维护了一个HTTP connection pool，可以设置withMaxConnections，[参考](http://stackoverflow.com/questions/16354966/does-amazon-s3-have-a-connection-pool)

2. 如果上传失败了，重传机制的制定
> 首先先获取Task对象，然后上传，如果上传成功则移除该任务；如果重试次数少于指定**重试次数**则重试；如果重试次数大于指定次数则失败，移除该任务。
