#### 1、远程调用服务

* 本地服务：服务就在应用内部
* 远程服务：服务是在其他的应用中，调用者和服务不在同一应用中。

IPC：Inter process communication

> 面试题:安卓如果实现IPC通讯？

> 1、A应用去激活B应用，实际就是一种IPC通信的体现。通过Intent实现IPC通讯。

> 2、AIDL是实现IPC通讯的一套技术，如果传递的是基本数据类型，可以直接写；如果传递的不是基本数据类型，**必须实现Parcelable接口**。 

#### 2、远程调用实例（游戏调用支付）

###### 2.1 alipayTest

* onBind 需要返回Agent对象
* Agent对象需要实现IAlipayService.Stub抽象类
* 将需要调用的系统里面的方法写入到Agent对象中

> [alipayService](https://github.com/computerwan/Android_Dev/blob/master/apps/alipayTest/src/cn/edu/usst/alipaytest/AlipayService.java)

* IAlipayService.aidl编译后，会在gen文件夹下面自动生成一个java类对象。
* 该接口中不需要存在public或者private

> [IAlipayService.aidl](https://github.com/computerwan/Android_Dev/blob/master/apps/alipayTest/src/cn/edu/usst/alipaytest/IAlipayService.aidl)

* 在[AndroidManifest.xml](https://github.com/computerwan/Android_Dev/blob/master/apps/alipayTest/AndroidManifest.xml)中需要添加以下内容：

```xml
<service android:name=".AlipayService">
    <intent-filter>
        <action android:name="cn.edu.usst.alipay"/>
    </intent-filter>
</service>
```
###### 2.2 gameTest

* 注意：需要把IAlipayService.aidl拷贝到该地址下面**（包名也必须与之前一模一样）**

* 在Layout定义两个Button，其onclick方法分别为bindService和call
* 绑定服务，需要调用Intent，setAction调用上面配置文件中定义的Action，再通过bindService启动服务。
* 定义MyConnection实现ServiceConnection，其中配置成功后，通过agent = IAlipayService.Stub.asInterface(iBinder)获得代理对象。
* 在call方法中，通过代理对象调用所要执行的方法。

> [MyActivity.java](https://github.com/computerwan/Android_Dev/blob/master/apps/gameTest/src/cn/edu/usst/gametest/MyActivity.java#L22)

>[main.xml](https://github.com/computerwan/Android_Dev/blob/master/apps/gameTest/res/layout/main.xml)

