# 安卓学习笔记



#### 0、案例

1. [电话拨号](https://github.com/computerwan/Android_Dev/blob/master/%E7%AC%AC%E4%BA%8C%E8%AE%B2%20%E7%AC%AC%E4%B8%80%E4%B8%AA%E5%BA%94%E7%94%A8.md#2电话拨号器程序)
	* Button的OnClickListener监听器
	

2. [短信](https://github.com/computerwan/Android_Dev/blob/master/%E7%AC%AC%E4%BA%8C%E8%AE%B2%20%E7%AC%AC%E4%B8%80%E4%B8%AA%E5%BA%94%E7%94%A8.md#6发送一个短信)
	* SmsManager.getDefault()获取SmsManager对象
	* smsManager.sendTextMessage(目的地，源地址，发送的文本数据，发送成功的报告，对方开机后收到短信的报告)
	
3. [设置中心](https://github.com/computerwan/Android_Dev/blob/master/%E7%AC%AC%E4%B8%89%E8%AE%B2%20%E6%95%B0%E6%8D%AE%E7%9A%84%E5%AD%98%E5%8F%96%E4%B8%8E%E6%9D%83%E9%99%90.md#5模拟设置中心)

4. [学生管理系统](https://github.com/computerwan/Android_Dev/blob/master/%E7%AC%AC%E5%9B%9B%E8%AE%B2%20XML%E6%96%87%E4%BB%B6%E7%94%9F%E6%88%90%E5%92%8C%E8%A7%A3%E6%9E%90.md)
	* [增加增删改查功能](https://github.com/computerwan/Android_Dev/blob/master/%E7%AC%AC%E4%BA%94%E8%AE%B2%20SQLite%E6%95%B0%E6%8D%AE%E5%BA%93.md)
	* 测试类需要添加权限

5. [吉凶号码解析](https://github.com/computerwan/Android_Dev/blob/master/%E7%AC%AC%E5%9B%9B%E8%AE%B2%20XML%E6%96%87%E4%BB%B6%E7%94%9F%E6%88%90%E5%92%8C%E8%A7%A3%E6%9E%90.md#5吉凶号码解析)
	* 使用到pull parser解析xml

#### 0、重点知识点

1. 排版：width=0dip，weigth=权重
2. 注意权限的设置（AndroidManifest）
	* 短信，电话，上网
	* 测试类
3. Toast：消息提示
4. 判断字符串是否为空TextUtils.isEmpty


```xml
<uses-permission android:name="android.permission.CALL_PHONE"/>
<uses-permission android:name="android.permission.SEND_SMS"/>
<!-- 测试类权限-->
<instrumentation android:name="android.test.InstrumentationTestRunner"
       android:targetPackage="com.example.sqliteDemo">
</instrumentation>
<uses-library android:name="android.test.runner"/>
<uses-permission android:name="android.permission.READ_CONTACTS"/>
<uses-permission android:name="android.permission.WRITE_CONTACTS"/>
 
```


#### 1、数据存储的主要方式

1.1、 [应用内部的私有文件夹（第三讲）](https://github.com/computerwan/Android_Dev/blob/master/%E7%AC%AC%E4%B8%89%E8%AE%B2%20%E6%95%B0%E6%8D%AE%E7%9A%84%E5%AD%98%E5%8F%96%E4%B8%8E%E6%9D%83%E9%99%90.md#1将数据保存到应用程序中)

  * 文件存放地址：/data/data/cn.usst.edu.test/files
  
    * 使用方法：(File file = new File(this.getFilesDir(), "private.txt");)
    
  * 文件存放地址（缓存）：/data/data/cn.usst.edu.test/cache
  
    * 使用方法：(File file = new File(this.getCacheDir(), "private.txt");)
    
1.2、 [sharedPreference(第三讲)](https://github.com/computerwan/Android_Dev/blob/master/%E7%AC%AC%E4%B8%89%E8%AE%B2%20%E6%95%B0%E6%8D%AE%E7%9A%84%E5%AD%98%E5%8F%96%E4%B8%8E%E6%9D%83%E9%99%90.md#4使用sharedpreference保存数据)

  * 文件存放地址：/data/data/cn.usst.edu.test/shar_prefs
  	* 保存：获取getSharedPreferences；Edit；putXXX；commit
  	* 回显:获取getSharedPreferences；getXXX
 	

1.3、 [在SD卡公共的部分(第三讲)](https://github.com/computerwan/Android_Dev/blob/master/%E7%AC%AC%E4%B8%89%E8%AE%B2%20%E6%95%B0%E6%8D%AE%E7%9A%84%E5%AD%98%E5%8F%96%E4%B8%8E%E6%9D%83%E9%99%90.md#2将数据保存到公共的sd卡上)

  * 文件存放地址：/mnt/sdcard/
  
    * 动态地址：File file =new File(Environment.getExternalStorageDirectory(),"info.txt"); 
    * 需要权限：
    ```
	xml <android.permission.write_external_storage>
    ```
    
1.4、[存储到数据库里面(第五讲)](https://github.com/computerwan/Android_Dev/blob/master/%E7%AC%AC%E4%BA%94%E8%AE%B2%20SQLite%E6%95%B0%E6%8D%AE%E5%BA%93.md)

  * 需要继承SQLiteOpenHelper
 
  * [创建数据库](https://github.com/computerwan/Android_Dev/blob/master/%E7%AC%AC%E4%BA%94%E8%AE%B2%20SQLite%E6%95%B0%E6%8D%AE%E5%BA%93.md#1创建数据库)
  
  * [第一套方法：db.exeSQL（增删改）和db.rawQuery(查)](https://github.com/computerwan/Android_Dev/blob/master/%E7%AC%AC%E4%BA%94%E8%AE%B2%20SQLite%E6%95%B0%E6%8D%AE%E5%BA%93.md#2增删改查crud)
  
  * [第二套方法：db.insert（增），db.delete（删），db.update（改），db.query(查)](https://github.com/computerwan/Android_Dev/blob/master/%E7%AC%AC%E4%BA%94%E8%AE%B2%20SQLite%E6%95%B0%E6%8D%AE%E5%BA%93.md#4增删改查的第二种方法)


#### 2、Handler消息处理机制

 [Handler消息处理机制](https://github.com/computerwan/Android_Dev/blob/master/%E7%AC%AC%E4%B8%83%E8%AE%B2%20%E7%BD%91%E7%BB%9C%E7%BC%96%E7%A8%8B.md#3handle消息处理机制)，用于子线程与主线程之间的通信。
 

#### 3、文件解析（XML和Json）

* XML：重量级的数据交换格式

	* [逐行添加（不推荐）](https://github.com/computerwan/Android_Dev/blob/master/%E7%AC%AC%E5%9B%9B%E8%AE%B2%20XML%E6%96%87%E4%BB%B6%E7%94%9F%E6%88%90%E5%92%8C%E8%A7%A3%E6%9E%90.md#2生成xml文件不推荐)
	
	* [serializer生成XML(推荐)](https://github.com/computerwan/Android_Dev/blob/master/%E7%AC%AC%E5%9B%9B%E8%AE%B2%20XML%E6%96%87%E4%BB%B6%E7%94%9F%E6%88%90%E5%92%8C%E8%A7%A3%E6%9E%90.md#3serializer生成xml推荐)
		* newSerializer
		* startDocument和endDocument
		* startTag和endTag
		* text
	
	* [pull parser解析](https://github.com/computerwan/Android_Dev/blob/master/%E7%AC%AC%E5%9B%9B%E8%AE%B2%20XML%E6%96%87%E4%BB%B6%E7%94%9F%E6%88%90%E5%92%8C%E8%A7%A3%E6%9E%90.md#4解析生成的xml文件)
		* 标签同上
		* 使用XmlPullParser指针：获取类型（getType）和移动指针（next）
	

* Json：轻量级的数据交换格式

#### 4、与服务器交互——GET和POST

* [传统方法](https://github.com/computerwan/Android_Dev/blob/master/%E7%AC%AC%E4%B9%9D%E8%AE%B2%20Get%E5%92%8CPost%E4%BC%A0%E8%BE%93.md#二get方式传输中文需要注意编码问题)

* [Apache的HttpClient（已经集成进安卓）](https://github.com/computerwan/Android_Dev/blob/master/others/9.2.jpg)

* [async_http_client](https://github.com/computerwan/Android_Dev/blob/master/%E7%AC%AC%E4%B9%9D%E8%AE%B2%20Get%E5%92%8CPost%E4%BC%A0%E8%BE%93.md#五使用开源框架async_http_client)

#### 5、[ListView](https://github.com/computerwan/Android_Dev/blob/master/%E7%AC%AC%E5%85%AD%E8%AE%B2%20ListView.md)

* 需要设置Adapter继承BaseAdapter，ArrayAdapter
* 使用listview.setAdapter设置Adapter
* 如果Adapter存在，使用notifyDataSetChanged刷新
	

#### 6、intent

1. setAciton和setData
2. startActivity和startActivityForResult

#### 7、四大组件

###### 7.1、activity

###### 7.2 broadcastReceiver

###### 7.3 service

###### 7.4 contentProvider





