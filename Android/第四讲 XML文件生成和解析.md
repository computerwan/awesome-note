知识点：

* 推荐使用serializer生成xml文件、使用pull parser解析xml文件
* 需要把文件放在assert里面，通过getAssets().open("rs2.xml")获取文件

## 学生信息管理系统

####1、界面

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              android:orientation="vertical"
              android:layout_width="fill_parent"
              android:layout_height="fill_parent"
>
    <TextView
            android:layout_width="fill_parent"
            android:layout_height="wrap_content"
            android:text="学生信息管理系统"
    />
    <EditText android:layout_width="fill_parent" android:layout_height="wrap_content"
                android:hint="请输入学生的姓名" android:id="@+id/studentname"/>
    <EditText android:layout_width="fill_parent" android:layout_height="wrap_content"
                android:hint="请输入学生的学号" android:id="@+id/studentnumber"/>
    <RadioGroup
            android:layout_width="fill_parent"
            android:layout_height="wrap_content" android:id="@+id/radiogb"
            android:orientation="horizontal">
        <RadioButton
                android:layout_width="0dip"
                android:layout_weight="1"
                android:layout_height="wrap_content"
                android:text="男"
                android:id="@+id/man" android:checked="false"/>
        <RadioButton
                android:layout_width="0dip"
                android:layout_weight="1"
                android:layout_height="wrap_content"
                android:text="女"
                android:id="@+id/female" android:checked="false"/>
        <RadioButton
                android:layout_width="0dip"
                android:layout_weight="1"
                android:layout_height="wrap_content"
                android:text="未知"
                android:id="@+id/unknown" android:checked="true"/>
    </RadioGroup>
    <Button android:layout_width="fill_parent" android:layout_height="wrap_content" android:text="保存" android:onClick="save"/>

</LinearLayout>
```

#### 2、生成XML文件（不推荐）

```java
public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private EditText ed_ssname;
    private EditText ed_ssnumber;
    private RadioGroup rgb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //控件初始化
        ed_ssname = (EditText) findViewById(R.id.studentname);
        ed_ssnumber = (EditText) findViewById(R.id.studentnumber);
        rgb = (RadioGroup) findViewById(R.id.radiogb);
    }

    public void save(View v) {
        String studentName = ed_ssname.getText().toString();
        String studentNumber = ed_ssnumber.getText().toString();
        if (TextUtils.isEmpty(studentName) || TextUtils.isEmpty(studentNumber)) {
            Toast.makeText(this,"学生的姓名和学号不能为空", Toast.LENGTH_LONG);
            return;
        }
        //获得学生的性别
        int id = rgb.getCheckedRadioButtonId();
        String sex = "默认";
        if (id == R.id.female) {
            sex = "女";
        } else if (id == R.id.man) {
            sex = "男";
        }
        //将学生的信息保存起来
            /*
            <?xml version="1.0" encoding="utf-8">
            <student>
                <name>张三</name>
                <number>12345</number>
                <sex>男</sex>
            </student>
             */
        OutputStream out = null;
        try {
            File file = new File(getFilesDir(), studentName + ".xml");
            StringBuilder sb = new StringBuilder();
            sb.append("<?xml version='1.0' encoding='utf-8'>");
            sb.append("<student>");
            sb.append("<name>");
            sb.append(studentName);
            sb.append("</name>");
            sb.append("<number>");
            sb.append(studentNumber);
            sb.append("</number>");
            sb.append("<sex>");
            sb.append(sex);
            sb.append("<sex>");
            sb.append("</student>");
            out = new FileOutputStream(file);
            out.write(sb.toString().getBytes());
            out.close();
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT);
        }


    }
}

```
上面方法会导致类似SQL注入的情况

#### 3、serializer生成XML（推荐）

```java
public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private EditText ed_ssname;
    private EditText ed_ssnumber;
    private RadioGroup rgb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //控件初始化
        ed_ssname = (EditText) findViewById(R.id.studentname);
        ed_ssnumber = (EditText) findViewById(R.id.studentnumber);
        rgb = (RadioGroup) findViewById(R.id.radiogb);
    }

    public void save(View v) {
        String studentName = ed_ssname.getText().toString();
        String studentNumber = ed_ssnumber.getText().toString();
        if (TextUtils.isEmpty(studentName) || TextUtils.isEmpty(studentNumber)) {
            Toast.makeText(this, "学生的姓名和学号不能为空", Toast.LENGTH_LONG);
            return;
        }
        //获得学生的性别
        int id = rgb.getCheckedRadioButtonId();
        String sex = "默认";
        if (id == R.id.female) {
            sex = "女";
        } else if (id == R.id.man) {
            sex = "男";
        }
        //将学生的信息保存起来
            /*
            <?xml version="1.0" encoding="utf-8">
            <student>
                <name>张三</name>
                <number>12345</number>
                <sex>男</sex>
            </student>
             */

        try {
            File file = new File(getFilesDir(), studentName + ".xml");
            OutputStream out = new FileOutputStream(file);
            //专门生成xml文件的序列化器
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(out, "UTF-8");
            //  <?xml version="1.0" encoding="utf-8">
            serializer.startDocument("UTF-8", true);
            serializer.startTag(null, "student");

            serializer.startTag(null, "name");
            serializer.text(studentName);
            serializer.endTag(null, "name");

            serializer.startTag(null, "number");
            serializer.text(studentNumber);
            serializer.endTag(null, "number");

            serializer.startTag(null, "sex");
            serializer.text(sex);
            serializer.endTag(null, "sex");

            serializer.endTag(null, "student");
            serializer.endDocument();
            out.close();
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT);
        }
    }
}
```
#### 4、解析生成的XML文件

pull parser：基于事件驱动的，内部维护了一个“指针”，你需要自己去挪动一下这个指针

```java
    public void processDocument(XmlPullParser xpp)
        throws XmlPullParserException, IOException
    {
	//事件的类型
        int eventType = xpp.getType();
        do {
            if(eventType == xpp.START_DOCUMENT) {
                System.out.println("Start document");
            } else if(eventType == xpp.END_DOCUMENT) {
                System.out.println("End document");
            } else if(eventType == xpp.START_TAG) {
                processStartElement(xpp);
            } else if(eventType == xpp.END_TAG) {
                processEndElement(xpp);
            } else if(eventType == xpp.TEXT) {
                processText(xpp);
            }
	//手动移动指针
            eventType = xpp.next();
        } while (eventType != xpp.END_DOCUMENT);
    }
```
#### 5，吉凶号码解析

```xml
<?xml version="1.0" encoding="gbk"?>
<smartresult>
    <product type="mobile">
        <phonenum>13512345678</phonenum>
        <location>重庆移动神州行卡</location>
        <phoneJx>有得有失，华而不实，须防劫财，始保平安 吉带凶</phoneJx>
    </product>
</smartresult>
```

备注：

* 要定义一个javaBean类存放Product
* 需要把文件放在assert里面，通过getAssets().open("rs2.xml")获取文件

```java
public class MyActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //使用xml pull解析器去解析xml文件的内容
        XmlPullParser pullParser = Xml.newPullParser();
        try {
            Product p= new Product();;
            //解析源
            InputStream in = getAssets().open("rs2.xml");
            pullParser.setInput(in, "gbk");
            //获得一个事件的类型
            int eventType =pullParser.getEventType();
            //指针依次下移的模版
            while(eventType!=XmlPullParser.END_DOCUMENT){
                if(eventType==XmlPullParser.START_TAG){
                    //判断是否是元素的开始，如果是,j获取当前解析的名称
                    if("product".equals(pullParser.getName())){
                        //准备product类的一个实例，去封装数据
                        String type = pullParser.getAttributeValue(0);
                        p.setType(type);
                    }else if("phonenum".equals(pullParser.getName())){
                        String phonenum = pullParser.nextText();
                        p.setPhonenum(phonenum);
                    }else if("location".equals(pullParser.getName())){
                        String location=pullParser.nextText();
                        p.setLocation(location);
                    }else if ("phoneJx".equals(pullParser.getName())){
                    String phoneJx =pullParser.nextText();
                        p.setPhoneJx(phoneJx);
                    }
                }
                //手动挪动“指针”
                eventType=pullParser.next();
            }
            if(p!=null){
                System.out.println(p.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```
