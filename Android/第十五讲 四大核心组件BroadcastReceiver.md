#### 1、具体实现步骤

1. 写一个继承BroadCastReceiver的类，类似于买了一个收音机；
2. 在清单Manifest文件中进行配置(recevier)，类似装电池；
3. 设置接受广播的类型(intent-filter中action)，类似于调节频道；

#### 2、案例

######2.1、SD卡插拔监听

继承的类：SdCardStatusReceiver.java

```java
//SD卡插拔状态的广播是由低层Linux系统监听后，通知framework层，其mediaServer发出
public class SdCardStatusReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        System.out.println("接收到sd卡的插拔状态修改了"+action);
    }
}
```
AndroidManifest.xml

```xml
<receiver android:name=".SdCardStatusReceiver" >
        <intent-filter>
            <action android:name="android.intent.action.MEDIA_MOUNTED"/>
            <data android:scheme="file"/>
        </intent-filter>
</receiver>
```
######2.2、开机监听

AndroidManifest.xml

```xml
<!-- 注意：需要设置权限-->
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>

<receiver android:name=".BootCompletionReceiver" >
        <intent-filter>
            <action android:name="android.intent.action.BOOT_COMPLETED"/>
        </intent-filter>
</receiver>
```

######2.3、IP拨号器案例

IpDialogReceiver.java

```java
public class IpDialogReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
       String data=getResultData();
        //获取sharedpreference
        SharedPreferences sp = context.getSharedPreferences("config", 0);
        String prefix = sp.getString("prefix","");

        if(data.startsWith("0")){
              //说明是打长途电话，就在拨打前添加prefix
                setResultData(prefix+data);
        }

    }
}
```

layout文件

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              android:orientation="vertical"
              android:layout_width="fill_parent"
              android:layout_height="fill_parent"
>
    <EditText
            android:id="@+id/ed_prefix"
            android:hint="请输入IP拨号的前缀"
            android:layout_width="fill_parent"
            android:layout_height="wrap_content"/>
    <Button
            android:layout_width="fill_parent"
            android:layout_height="wrap_content"
            android:onClick="save"
            android:text="保存"/>

</LinearLayout>
```

MyActivity.java

```java
public class MyActivity extends Activity {

     private EditText ed_prefix;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ed_prefix = (EditText) findViewById(R.id.ed_prefix);
    }
    public void save(View v){
        String prefix = ed_prefix.getText().toString().trim();
        if(TextUtils.isEmpty(prefix)){
            Toast.makeText(this,"IP拨号前缀不能为空",Toast.LENGTH_SHORT);
            return;
        }
        SharedPreferences sp = getSharedPreferences("config", 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("prefix",prefix);
        edit.commit();
        Toast.makeText(this,"IP拨号前缀保存成功",Toast.LENGTH_SHORT);
    }
}
```

AndroidManifest.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
          package="com.example.myapp"
          android:versionCode="1"
          android:versionName="1.0">
    <uses-sdk android:minSdkVersion="10"/>
     <uses-permission android:name="android.permission.PROCESS_OUTGOING_CALLS"/>
    <application android:label="@string/app_name">
        <activity android:name="MyActivity"
                  android:label="@string/app_name">
            <intent-filter>
                <action android:name="android.intent.action.MAIN"/>
                <category android:name="android.intent.category.LAUNCHER"/>
            </intent-filter>
        </activity>
        <receiver android:name=".IpDialogReceiver">
            <intent-filter>
                <action android:name="android.intent.action.NEW_OUTGOING_CALL"/>
            </intent-filter>
        </receiver>

    </application>
</manifest> 

```

#### 4、自定义广播和接收

分类：分为有序广播和无序广播</br>

区别：</br>

* 有序广播:可以在特定的接收者收到广播后修改广播的数据或取消广播的发送

* 无序广播：不能取消广播的发送

###### 4.1、无序广播

发送广播：

```java
public void sendBroadcast(View v){
    System.out.println("发出自定义广播");
    Intent intent = new Intent();
    intent.setAction("cn.edu.usst.guangbo.XWLB");
    sendBroadcast(intent);  // 发送广播出去 
}
```

接收广播：

AndroidManifest.xml：
```xml
<receiver android:name="com.itheima.receivebroadcast.MyBroadCastReciver">
    <intent-filter >
        <action android:name="cn.edu.usst.guangbo.XWLB"/>
    </intent-filter>
</receiver>

```
###### 4.2有序广播（通过priority（范围-1000~1000）由高到低传播）

发送广播：

```java
	// intent :　意图对象 
	// receiverPermission :　接收广播的组件需要什么样的权限  
	// resultReceiver : 广播事件的最终接收者 
	// scheduler :  调度器 
	// initialCode :　发送广播时的初始码  
	// initialData : 广播发出的原始数据  -- 未被修改过的原始数据
	// initialExtras: 广播发出时的一些额外的数据 
	
	Intent intent = new Intent();
	intent.setAction("cn.edu.usst.guangbo.XWLB");
	sendOrderedBroadcast(intent, null, new FinalResultReceiver(), null, 1, "测试数据", null);

```

接收广播：

```java 
	//获取广播数据
	String data = getResultData();
	//修改广播数据
	setResultData("需要更改的内容");
	//取消广播
	abortBroadcast();
```
```xml
<receiver android:name="项目全称">
	<intent-filter android:priority="-1000">
		<action android:name="cn.edu.usst.guangbo.XWLB"/>
	</intent-filter>
</receiver>
```

#### 5、开发手机短信窃听

[AndroidManifest.xml](https://github.com/computerwan/Android_Dev/blob/master/apps/smsTap/AndroidManifest.xml)

[SmsListener](https://github.com/computerwan/Android_Dev/blob/master/apps/smsTap/src/en/edu/usst/smsTap/SmsListener.java)


#### 6、监听手机屏幕解锁和锁屏

如果广播经常发出（如：手机锁屏、解锁、手机电量变化）不能通过配置文件去设置，而需要在源代码中设置广播接受者

MyActivity.java

```java
public class MyActivity extends Activity {

    private PhoneScreenListener phoneReceiver;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //注册监听手机解锁/锁屏的广播接收者 
        phoneReceiver = new PhoneScreenListener();
        
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.SCREEN_OFF");
        filter.addAction("android.intent.action.SCREEN_ON");
        
        //注册广播接收者 
        registerReceiver(phoneReceiver, filter);
        
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 解除注册的广播接收者 
        unregisterReceiver(phoneReceiver);
    }
    

}
```

PhoneScreenListener.java

```java
public class PhoneScreenListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        
        System.out.println("手机解锁或者锁屏了");
        
    }

}

```
	