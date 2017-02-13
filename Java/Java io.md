# Java I/O流分析

### 一. summary
* 读操作用read，readline；写用write；
* 注意写完要用out.flush保存，结束要用close关闭
1. 读写文件
  * FileReader，FileWriter
  * BufferedReader，BufferedWriter
  * PrintWriter(包在bufferedWriter上面) -->写用out.print("...")
2. 读写字节流
  * InputStream和OutputStream
3. 转换
  * FileInputStream和FileOutputStream //File转为InputStream
  * InputStreamReader和OutputStreamWriter //InputStream转为Reader
  * ByteArrayInputStream(String.getBytes(StandardCharsets.UTF_8))； //字符串转为InputStream

[原理分析][1]
### 二. IO类库
分类：
* 基于字节：InputStream和OutputStream
* 基于字符：Writer和Reader
* 基于磁盘操作：File
* 基于网络操作：Socket


### 三. NIO类库



[1]:https://www.ibm.com/developerworks/cn/java/j-lo-javaio/
