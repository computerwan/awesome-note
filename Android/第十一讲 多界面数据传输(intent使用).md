多个Activity来进行多个界面之间的切换</br>
通过**定义intent意图**，可以激活其他应用，或者传递数据到不同的组件

##步骤：

#### 1，每新建一个activity都需要在AndroidManifest中注册
```xml
	<activity android:name="SecondActivity">
		<!--
			这里的main表示是应用程序的入口 LAUNCHER 表示应用程序可以从laucher启动
		-->
		<intent-filter>
			<action android:name="android.intent.action.MAIN"/>
			<category android:name="android.intent.category.LAUNCHER"/>
		</intent-filter>
	</activity>
```

#### 2,新的activity都需要与一个页面绑定(在新的activity中添加)
```java
	setContentView(R.layout.main02);
```

#### 3,传出的activity：
	步骤：定义intent，设置需要传入的界面（setClass），存放要传入的数据（putExtra），确认
```java
	//通过定义intent意图，可以激活其他应用，或者传递数据到不同的组件
	Intent intent =new Intent();
	//先传入上下文（需要激活的应用），再传入需要激活到的界面
	intent.setClass(this,SecondActivity.class);
	//可以在激活SecondActivity的时候，带过去一些数据给SecondActivity
	intent.putExtra("data",data);
	startActivity(intent);
```

#### 4,获取的activity
	步骤：获取intent，获取数据（getXXXExtra）
```java
	Intent intent = getIntent();
	String data = intent.getStringExtra("data");
```

#### 5,传入其他内容：(多选框，图片)
```java
	传出：
	//多选框
	intent.putExtra("sex", rgb.getCheckedRadioButtonId());
	//图片
	intent.putExtra("img", BitmapFactory.decodeResource(getResources(), R.drawable.ouxiangpai));

	传入：
	int sex = intent.getIntExtra("sex", R.id.male);
	Bitmap img = intent.getParcelableExtra("img");
```


#### 7,备注（完整程序）
```java
public class MyActivity extends Activity {
    private EditText ed_name;
    private TextView tv_rp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果没有载入布局文件，调用findViewById会报空指针异常
        setContentView(R.layout.main);
        ed_name = (EditText) findViewById(R.id.ed_name);
        tv_rp= (TextView) findViewById(R.id.rp_value);

        //在其他页面上面载入空间
//        View v = View.inflate();
//        v.findViewById(R.id.)
    }

    public void cacl(View v) {
        String name = ed_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "姓名不能为空", Toast.LENGTH_LONG);
            return;
        }
        byte[] result = name.getBytes();
        int total = 0;
        for (byte b : result) {
            int data = b & 0xff;
            total = total + Math.abs(data);
        }
        int rp =total%100;
        String data=null;
        if(rp>90){
            data= "你的人品不错";
        }else if(rp>60){
            data="你的人品一般";
        }else{
            data="垃圾人品";
        }

        //有这里的界面带给SecondActivity界面
        //通过定义intent意图，可以激活其他应用，或者传递数据到不同的组件
        Intent intent =new Intent();
        //先传入上下文（需要激活的应用），再传入需要激活到的界面
        intent.setClass(this,SecondActivity.class);
        //可以在激活SecondActivity的时候，带过去一些数据给SecondActivity
        intent.putExtra("data",data);
        startActivity(intent);

    }
}
public class SecondActivity extends Activity {
    private TextView rp_value;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main02);
        rp_value = (TextView) findViewById(R.id.rp_value);

        //获取激活SecondActivity那个意图的对象
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");

        rp_value.setText(data);

    }
}
```
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              android:orientation="vertical"
              android:layout_width="fill_parent"
              android:layout_height="fill_parent">

    <LinearLayout
            android:layout_width="fill_parent"
            android:layout_height="wrap_content">
        <EditText
                android:id="@+id/ed_name"
                android:layout_width="0dip"
                android:layout_weight="3"
                android:layout_height="wrap_content"
        />
        <Button
                android:onClick="cacl"
                android:text="计算"
                android:layout_width="0dip"
                android:layout_weight="1"
                android:layout_height="wrap_content"/>
    </LinearLayout>


</LinearLayout>

<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              android:orientation="vertical"
              android:layout_width="fill_parent"
              android:layout_height="fill_parent"
>
    <TextView
            android:id="@+id/rp_value"
            android:layout_width="fill_parent"
            android:layout_height="wrap_content"
            android:text="第二页面"
    />
</LinearLayout>
```
