* **重点：ContentResolver需要两个值，Uri和Contentvalues；**

	* 其中Uri是以"Content://XXXX"传入秘钥，Contentvalues是以map格式存储需要传入的数据。

#### 1、实现步骤

	
> 应用（1）(需要留后台的应用)

* 新建一个类继承ContentProvider

	 * [BankBackDoor.java](https://github.com/computerwan/Android_Dev/blob/master/apps/bank/src/cn/edu/usst/bank/BankBackDoor.java)

* 可以引入UriMatcher来设置访问条件。(这里使用到数据库，需要引入类继承SQLiteOpenHelper)
	
	* [BankDBOpenHelper.java](https://github.com/computerwan/Android_Dev/blob/master/apps/bank/src/cn/edu/usst/bank/BankDBOpenHelper.java)

* 需要在AndroidManifest.xml去注册Provider
	
	* [AndroidManifest.xml](https://github.com/computerwan/Android_Dev/blob/master/apps/bank/AndroidManifest.xml)


> 应用（2）（访问后台的应用）

* 通过getContentResolver获取resolver
* 然后调用应用1里面的内容，其中URI将authority和秘钥传进来。
	* [MyActivity.java](https://github.com/computerwan/Android_Dev/blob/master/apps/bankVisitor/src/cn/edu/usst/bankVisitor/MyActivity.java)

##### 2、例：添加数据到系统的短信中

> 注：从lollipop开始，默认短信应用外的软件不能以写入短信数据库的形式(write sms)发短信，但应该能以send sms的形式发短信

```java
public class MyActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }


    public void addsms(View v){
        ContentResolver resolver = getContentResolver();
        //contentProvider使用得格式是content://，authorities是sms,
        Uri uri = Uri.parse("content://sms");
        ContentValues values =new ContentValues();
        values.put("address","13312345678");
        values.put("date",System.currentTimeMillis());
        values.put("type","1");
        values.put("body","测试短信");
        resolver.insert(uri,values);
        System.out.println("插入成功了");
       // Toast.makeText(this,"插入成功",Toast.LENGTH_SHORT);
    }
    public void delsms(View v){
        ContentResolver resolver = getContentResolver();
        Uri uri = Uri.parse("content://sms");
        resolver.delete(uri,"adress=?",new String[]{"13312345678"});
        System.out.println("删除成功了");
       // Toast.makeText(this,"删除成功",Toast.LENGTH_SHORT);
    }
}

```

权限：

```xml
    <uses-permission android:name="android.permission.SEND_SMS"/>
    <uses-permission android:name="android.permission.WRITE_SMS"/>

```