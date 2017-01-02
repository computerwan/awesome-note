###功能：
* 页面下载
* 链接提取
* URL管理
* 内容分析和持久化


具体类：
* Spider（整个爬虫的调度框架）
* Downloader（页面下载）
	模拟http请求：
	（1）HttpURLConnection
	（2）Apache HttpClient（定义http头（User-agent，cookie），自动redirect，连接复用，cookie保留，设置代理）
* PageProcessor（链接提取和页面分析）
	* 正则表达式（不能对HTML进行语法级解析）
	* Jsoup（支持jquery中css selector）
	* HtmlParser
	* Apache tika （支持PDF，Zip，java类）
	* HtmlCleaner和Xpath（通用标准，可以精确到属性）
* Scheduler（URL管理）
	已抓取URL和未抓取URL分开保存，并去重（hash方式，Bloom Filter）
	多线程使用：LinkedBlockingQueue和ConcurrentHashMap	
* Pipeline（离线分析和持久化）
	* 控制台输出
	* 文件持久化
	* 存到数据库，mongdb

HttpClientDownloader：整合HttpClient
Selector：整合CSS selector，Xpath，正则



---

###1.1 URL相关
URL：统一资源定位器（uniform resource locator）
三个部分组成：协议方案、主机名、资源名

###1.2 常见状态码
200 访问成功
404 资源未找到
500 服务器端错误
302 重定向（redirect）
304 缓存


###1.3 请求协议
1. 请求首行//请求方式 请求路径 请求协议/版本号
2. 请求头//压缩信息，语言，Accept权重，
3. 请求空行
4. 请求正文//GET请求没有正文，POST有正文

###1.4 响应协议
1. 响应首行 状态码
2. 响应头 meta标签就是模拟http响应头的功能
3. 响应空行
4. 响应正文（禁止缓存）

###1.5 基本步骤
1. 建立连接后，客户端发送一个http请求，常见的是get请求
（post请求可以额外增加数据，例如：购买商品或对网页进行编辑）
2. 两个任务：下载页面和发现URL

种子集合（seed）-->URL请求队列
下载一个页面，就对其进行解析，找到链接标签，包含URL地址。
单线程效率会很低，因为都在等待响应上。（等待DNS服务器响应，等待与服务器连接被确定，等待服务器发送数据）
礼貌策略（politeness policy）：不会在特定的网络服务器上一次抓取多个页面。
robots.txt

时新性：在http协议head头中有date数据，
年龄（age）

网站地图：priority后面数大于0.5就是希望你爬的


分布式：
（1）将爬虫程序放在采集信号的网站附近
（2）减少爬虫需要记住的网页数
（3）使用大量的计算资源

重复检测：
检验和（checksumming）：完全相同才行
指纹（分词处理，散列值存储）

去除噪音：
基于对HTML标签的观察
递归遍历DOM树