**重点**：

* 需要获取系统的组件，都需要通过getSystemService来获取。

* 都先需要获得NotificationManage对象，然后调用notify方法

#### 方法1：notification

低版本无法使用（无法调用Builder方法）

```java
	public void sendNotification(View v){
		
		// 获取NotificationManager的对象 
		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
		//构建一个Notification对象 
		
		// 链式调用
		Notification notification = new Notification.Builder(this)
        .setContentTitle("标题")
        .setContentText("内容")
        .setSmallIcon(R.drawable.ic_launcher)
        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
        .build();
		
		nm.notify(1, notification);
		
	}

```

#### 方法2：PendingIntent
* 方法过时
* 这里的intent，表示在点击Notification的时候所执行的动作（打电话）
* pendingIntent：包含另外一个intent，另外intent事是将来要做的，做不做取决于程序，或者来自用户的响应。 


```java

	public void sendNotification(View v){
		
		//获取NotificationManager的对象 
		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
		Notification notification = new Notification(R.drawable.ic_launcher, "标题", System.currentTimeMillis());
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_CALL);
		intent.setData(Uri.parse("tel://电话号码"));
		
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, 1);
		
		notification.setLatestEventInfo(this, "标题", "文本", pendingIntent);
		nm.notify(1, notification);
		
	}

```

不要忘记打电话权限：

```xml
<uses-permission android:name="android.permission.CALL_PHONE"/>
```