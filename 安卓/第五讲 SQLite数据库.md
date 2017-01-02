知识点：

* 创建数据库的类需要继承SQLiteOpenHelper
* 创建表的时候不要使用int,auto_crement,要使用integer和autoincrement
* 更新表的时候，每次version的值不能比之前的低
* 获取数据库可使用getWritableDatabase和getReadableDatabase
* 数据SQL语句可使用execSQL（无返回值）和rawQuery（返回值cursor，类似于resultSet，使用cursor.moveToNext指向下一个，cursor.getString（index）获取内容）

如果保存的数据是有规律的，格式统一，就选择使用数据库保存

Android和其他主流的智能终端都使用：SQLite数据库

#### 1、创建数据库

* 每个应用都可以创建自己私有的数据库（同时创建表android_metadata用于国际化）

* 其中onUpgrade(SQLiteDatabase db, int i, int i1)传入两个int值，分别是oldversion和newversion，分别对不同版本之间的升级单独设置

```java
public class MySqliteHelper extends SQLiteOpenHelper {
    //context:应用上下文（为哪个应用创建数据库）
    // name：数据库名称；
    // factory：创建游标的工厂；
    // version：版本 （应用程序升级时候使用）
    public MySqliteHelper(Context context) {
        super(context, "mydb1", null, 1);
    }

    //在数据库首次被创建时候被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("onCreate 执行了");
        db.execSQL("create table students ( _id integer primary key autoincrement,name varchar(30), sex varchar(10))");
    }

    //在数据库升级时会调用,当版本比之前的版本高的时候就会调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        System.out.println("onUpgrade 执行了");
        db.execSQL("alter table students add number varchar(10)");
    }
}
```

在MyActivity调用下面程序：

```java
MySqliteHelper helper=new MySqliteHelper(MyActivity.this);
helper.getWritableDatabase();
```

#### 2、增删改查（CRUD）

界面main.xml：(其中ScrollView为其增加增加了滚动条)

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context=".MainActivity" >

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="学生管理系统" />

    <EditText
        android:id="@+id/ed_name"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:hint="请输入学生的姓名" />

    <RadioGroup
        android:id="@+id/rgb"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal" >

        <RadioButton
            android:id="@+id/male"
            android:layout_width="0dip"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:checked="true"
            android:text="男" />

        <RadioButton
            android:id="@+id/female"
            android:layout_width="0dip"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:text="女" />
    </RadioGroup>

    <Button
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:onClick="save"
        android:text="保存" />

    <ScrollView
        android:layout_width="fill_parent"
        android:layout_height="wrap_content" >

        <LinearLayout
            android:id="@+id/ll"
            android:layout_width="fill_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical" >
        </LinearLayout>
    </ScrollView>

</LinearLayout>
```

主程序：MyActivity.java

```java
public class MyActivity extends Activity {
    private EditText ed_name;
    private RadioGroup rgb;
    private LinearLayout ll;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //初始化
        ed_name = (EditText) findViewById(R.id.ed_name);
        rgb= (RadioGroup) findViewById(R.id.rgb);
        ll = (LinearLayout) findViewById(R.id.ll);

    }
    public void save(View v){
        String name = ed_name.getText().toString().trim();
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "学生姓名不能为空", Toast.LENGTH_SHORT).show();
        }
        String sex ="male";
        int id = rgb.getCheckedRadioButtonId();
        if(id==R.id.male){
            sex="male";
        }else{
            sex="female";
        }
        //将数据保存到数据库中
        //拿到dao
        StudentDao sdao = new StudentDao(this);
        Student st = new Student("01",name,sex);
        sdao.add(st);

        //将数据同步的显示到屏幕上去
         //查询现有的数据
         List<Student> students =sdao.getAll();
        for (Student student : students){
            TextView tv =new TextView(this);
            tv.setText(student.toString());
            ll.addView(tv);
        }

    }
}
```

studentDao.java

```java
public class StudentDao {
    StudentDbOpenHelper helper;
    public StudentDao(Context context) {
       //调用StudentDao时候，先调用StudentDbOpenHelper创建数据库，代码同上MySqliteHelper
        helper = new StudentDbOpenHelper(context);
    }

    public void add(Student st){
        //拿到工具类的实例，然后操作数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //执行SQL语句
        db.execSQL("insert into students values(null,?,?)",new Object[]{st.getName(),st.getSex()});

    }
    public void delete(String id){
        SQLiteDatabase db = helper.getWritableDatabase();
        //执行SQL语句
        db.execSQL("delete from students where _id=?",new Object[]{id});
    }
    public void update(Student st){
        SQLiteDatabase db = helper.getWritableDatabase();
        //执行SQL语句
        db.execSQL("update students set name=?,sex=? where _id=?",new Object[]{st.getName(),st.getSex(),st.getId()});

    }



    public Student find(String id){
        SQLiteDatabase db = helper.getReadableDatabase();
        //调用execSQL是没有返回值，rawQuery有返回值
        Cursor cursor = db.rawQuery("select * from students where _id=?", new String[]{id});
        //javaWeb中返回的是ResultSet，这里返回的是游标（Cursor），两者结构一样
        // JavaWeb使用rs.next()指向下一个值，这里使用cursor.moveToNext()指向下一个值
        boolean result = cursor.moveToNext();
        Student st =null;
        if(result){
            st =new Student();
            //getString中可以直接填写0，1，2,这种写法是getColumnIndex返回_id所在的列
            String _id = cursor.getString(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String sex = cursor.getString(cursor.getColumnIndex("sex"));
            st =new Student(_id,name,sex);
        }
        //最后释放资源
        cursor.close();
        return st;
    }

   //查询返回所有的学生信息
    public List<Student> getAll() {
        List<Student> list=new ArrayList<Student>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from students", null);
        while(cursor.moveToNext()){
            String _id = cursor.getString(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String sex = cursor.getString(cursor.getColumnIndex("sex"));
            Student st =new Student(_id,name,sex);
            list.add(st);
        }
        cursor.close();
        return list;
    }
}
```

Student.java（JavaBean类）

```java
public class Student {
    private String id;
    private String name;
    private String sex;

    public Student(String id, String name, String sex) {
        this.id = id;
        this.name = name;
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }

    public Student() {

    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

TestStudentDao(测试类)

```java
public class TestStudentDao extends AndroidTestCase{
    public void testAdd(){
        Student st =new Student("1","张三","男");
        StudentDao sdao =new StudentDao(getContext());
        sdao.add(st);
    }
    public void testDelete(){
        StudentDao sdao =new StudentDao(getContext());
        sdao.delete("1");
    }
    public void testUpdate(){
        Student s =new Student();
        s.setId("2");
        s.setName("李四");
        s.setSex("女");
        StudentDao sdao =new StudentDao(getContext());
        sdao.update(s);
    }
    public Student testFind(){
        StudentDao sdao =new StudentDao(getContext());
        Student s = sdao.find("1");
        return s;
    }

}
```

注：使用测试类的时候不要忘记在AndroidManifest.xml中插入下面代码：

```xml
  <instrumentation android:name="android.test.InstrumentationTestRunner"
        android:targetPackage="com.example.sqliteDemo"></instrumentation>
 <uses-library android:name="android.test.runner"/>
```

#### 3、异常out of memory和memory leak

* out of memory 内存溢出
	* 内存不够用，就会出现OOM异常
* memory leak 内存泄漏
	* 忘记释放资源，没有管理好内存

#### 4,增删改查的第二种方法

* 增：

```java
ContentValues values=new ContentValues();
values.put("name",st.getName());
values.put("sex",st.getSex());
//分别对应：插入的表（table），指定哪一列为空，内容赋值（需要定义一个ContentValues）
db.insert("students",null,values);
```

对应内容：

```java db.execSQL("insert into students values(null,?,?)",new Object[]{st.getName(),st.getSex()}); ```


* 删：

```java
//分别对应：插入的表（table），条件（selection），条件赋值（selectionArgs）
db.delete("students","_id=?"",new String[]{id});
```

对应内容：

```java db.execSQL("delete from students where _id=?",new Object[]{id}); ```

* 改：

```java
ContentValues values =new ContentValues();
values.put("name",st.getName());
values.put("sex",st.getSex());
//分别对应：插入的表（table），内容赋值，条件（selection），条件赋值（selectionArgs）
db.update("students",values,"_id=?",new String[]{st.getId()});
```

对应内容：
```java db.execSQL("update students set name=?,sex=? where _id=?",new Object[]{st.getName(),st.getSex(),st.getId()}); ```


* 查：

```java
//分别对应：table, columns, selection,selectionArgs, groupBy, having, orderBy
db.query("students",new String[]{"_id","name","sex"},"_id=?",new String[]{id},null,null,null);
```

对应内容：
```java Cursor cursor = db.rawQuery("select * from students where _id=?", new String[]{id}); 
```
