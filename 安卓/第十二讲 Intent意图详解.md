#### 1，Intent意图详解
在激活组件的时候，可以使用intent去激活，分为显式意图和隐式意图。
* 如果要激活的组件在本应用内部，推荐使用显式意图。
* 如果要激活的组件不在本应用内，推荐使用隐式意图。
* 如果应用内部配置了某个组件可以使用隐式意图去激活，那么在其他应用中，就可以使用这种显式意图的方式去激活组件。（不建议使用显式意图）
	
* 显式意图激活
	```java
	// 显示意图激活
    public void click01(View v) {
        System.out.println("显示意图激活");
        Intent intent = new Intent();
        
	//明确指定要激活在哪个应用中, 哪个activity
        intent.setClass(this, SecondActivity.class);
        startActivity(intent);
    }
	```
* 隐式意图激活
	<intent-filter 设置，通过隐式意图激活></br>
	action和category （动作和内容）</br>
```java
    public void click02(View v) {
        System.out.println("隐式意图激活");
        Intent intent = new Intent();
        intent.setAction("com.exmple.xxx");
        intent.addCategory("android.intent.category.DEFAULT");
        startActivity(intent);
    }
```
```xml
	<activity android:name="com.itheima.intentrelated.SecondActivity">
			<!-- 通过  intent-filter 来设置    SecondActivity 可以通过某个 隐式意图来激活 -->
			<intent-filter >
				<action android:name="com.exmple.xxx"/>
				<!-- category 都需要去声明
					android.intent.category.DEFAULT 是运行在手机,平板 
				-->
				<category android:name="android.intent.category.DEFAULT"/>
			</intent-filter>
	</activity>
```
可以引入其他参数：</br>
manifest中：
```xml
	<data android:scheme="协议名">
	<data android:host="主机名">
	<data android:mimeType="MimeType值">
	(MimeType值参考)[http://www.cnblogs.com/CraryPrimitiveMan/p/4337351.html]
```
activity中：
```java
intent.setData(Uri.parse("协议名://主机名"))
（ps：如果没有指定主机名，则主机名部分随意）
intent.setType("MimeType值")
```

#### 2，自动切换到浏览器
```java
	/*
	 * 
	<intent-filter>
			<action android:name="android.intent.action.VIEW" />
			<category android:name="android.intent.category.DEFAULT" />
			<category android:name="android.intent.category.BROWSABLE" />
			<data android:scheme="http" />
			<data android:scheme="https" />
			<data android:scheme="about" />
			<data android:scheme="javascript" />
	</intent-filter>
	 */
	link2go.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			//发送一个intent,连接浏览器去指定网址
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			intent.addCategory("android.intent.category.DEFAULT");
			intent.addCategory("android.intent.category.BROWSABLE");
			intent.setData(Uri.parse("http://www.baidu.com"));
			startActivity(intent);
		}
	});
```
