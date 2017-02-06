知识点：

* 为了实时显示，通常在界面中添加配适器lv.setAdapter(new MyAdapter());
	* 其中lv是需要显示的ListView空间，MyAdapter是继承实现ListAdapt接口的类。
* 在layout填充另外一个view对象的方法：View.inflate(Context，LayoutRes，ViewGroup)
    * 备注：一般context对象指在哪个layout中显示（通常:this或myActivity.this）
* 添加dialog对话框：通过new AlertDialog.Builder对象创建
    * 设置以下方法：setTitle，setMessage，setPositiveButton，setNegativeButton
    * 设置好了还需要用Builder.show()调用
* 几种优化（见下面）

#### 0、ListView是基于MVC设计思想

M：需要注入的数据</br>
V：ListView</br>
C：实现ListAdapt接口的类（BaseAdapt,SimpleAdapt,CursorAdapt）</br>

#### 1、对学生管理系统的改进（不推荐）

方法：在main.xml中将LinearLayout直接改为ListView

MyAdapter中要实现两个方法：getCount（一共需要显示的数量）和getView（在显示之前的需要的操作）

```java
public class MyActivity extends Activity {
    private EditText ed_name;
    private RadioGroup rgb;
    private ListView lv;

    private StudentDao sdao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //初始化
        ed_name = (EditText) findViewById(R.id.ed_name);
        rgb = (RadioGroup) findViewById(R.id.rgb);
        lv = (ListView) findViewById(R.id.lv);


    }

    public void save(View v) {
        String name = ed_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "学生姓名不能为空", Toast.LENGTH_SHORT).show();
        }
        String sex = "male";
        int id = rgb.getCheckedRadioButtonId();
        if (id == R.id.male) {
            sex = "male";
        } else {
            sex = "female";
        }
        //将数据保存到数据库中
        //拿到dao
        sdao = new StudentDao(this);
        Student st = new Student("01", name, sex);
        sdao.add(st);
        //查询现有数据
        refreshView();
    }

    List<Student> students;

    private void refreshView() {
        students = sdao.getAll();

        //为lv设置数据适配器
        lv.setAdapter(new MyAdapter());

    }

    private class MyAdapter extends BaseAdapter {

        //用于告诉控制器显示item数量
        public int getCount() {
            return students.size();
        }

        public Object getItem(int i) {
            return null;
        }

        public long getItemId(int i) {
            return 0;
        }

        //显示之前需要执行的操作
        public View getView(int i, View view, ViewGroup viewGroup) {
            LinearLayout ll = new LinearLayout(MyActivity.this);
            ll.setOrientation(LinearLayout.HORIZONTAL);

            //获得具体要显示list中哪个学生的信息
            Student student = students.get(i);
            TextView tv = new TextView(MyActivity.this);
            tv.setText(student.getName() + ",位置" + i);

            //如果是男同学，就显示一个男的图标，否则就显示一个代表女同学的图标
            String sex = student.getSex();
            ImageView iv = new ImageView(MyActivity.this);
            if ("male".equals(sex)) {
                //使用ImageView设置图片为男
                iv.setImageResource(R.mipmap.nan);
            } else {
                iv.setImageResource(R.mipmap.nan);
            }
            ll.addView(iv,20,30);
            ll.addView(tv);

            return ll;
        }
    }

}

```
####2、引入Items.xml插入main.xml中（推荐）

优化：</br>

1. 每次删除之后重置：不需要每次都要new对象，第一次new对象，之后通过notifyDataSetChanged 重置
2. 翻页的时候，第一次使用View.inflate，以后将getView中传入的view带入即可。（循环利用界面资源）
3. 设置好AlertDialog.Builder后，还需要调用Builder.show()将对话框调用


items.xml文件：

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              android:orientation="horizontal"
              android:layout_width="match_parent"
              android:layout_height="match_parent">

    <RelativeLayout
            android:gravity="center_vertical"
            android:layout_width="fill_parent"
            android:layout_height="wrap_content" >

        <ImageView
                android:layout_marginLeft="8dip"
                android:layout_centerVertical="true"
                android:layout_alignParentLeft="true"
                android:layout_width="20dip"
                android:layout_height="30dip"
                android:id="@+id/item_iv"/>
        <TextView
                android:layout_centerVertical="true"
                android:layout_toRightOf="@+id/item_iv"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:id="@+id/item_tv"/>
        <ImageView
                android:layout_marginRight="8dip"
                android:id="@+id/item_delete"
                android:layout_centerVertical="true"
                android:layout_alignParentRight="true"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:src="@mipmap/delete"/>
    </RelativeLayout>
</LinearLayout>
```

MyActivity.java文件

```java
public class MyActivity extends Activity {
    private EditText ed_name;
    private RadioGroup rgb;
    private ListView lv;
    private ImageView del;

    private StudentDao sdao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //初始化
        ed_name = (EditText) findViewById(R.id.ed_name);
        rgb = (RadioGroup) findViewById(R.id.rgb);
        lv = (ListView) findViewById(R.id.lv);


    }

    public void save(View v) {
        String name = ed_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "学生姓名不能为空", Toast.LENGTH_SHORT).show();
        }
        String sex = "male";
        int id = rgb.getCheckedRadioButtonId();
        if (id == R.id.male) {
            sex = "male";
        } else {
            sex = "female";
        }
        //将数据保存到数据库中
        //拿到dao
        sdao = new StudentDao(this);
        Student st = new Student("01", name, sex);
        sdao.add(st);
        //查询现有数据
        refreshView();
    }

    List<Student> students;
    private MyAdapter myAdapter;

    private void refreshView() {
        //将现有的全部给清空一下
        students = sdao.getAll();
        //每次删除数据，不要重新的new对象，使用notifyDataSetChanged就行。
        if (myAdapter == null) {
            //为lv设置数据适配器
            myAdapter = new MyAdapter();
            lv.setAdapter(myAdapter);
        } else {
            //则说明myAdapter已经存在，需要做的是通知控制器Adapter去更新一下数据
            myAdapter.notifyDataSetChanged();
        }


    }

    private class MyAdapter extends BaseAdapter {

        //用于告诉控制器显示item数量
        public int getCount() {
            return students.size();
        }

        public Object getItem(int i) {
            return null;
        }

        public long getItemId(int i) {
            return 0;
        }

        //显示之前需要执行的操作
        //view:就是用来进行优化的,模仿找托
        //通过view，内存上只需要显示在屏幕上的textView了，从而实现了内存的优化（BaseAdapt的子类CursorAdapt中的getView是已经优化好的内容）
        public View getView(int i, View view, ViewGroup viewGroup) {
            //这里实际就是getView返回的textView对象，都是同一个类型的对象，并且你会发现convertView就是之前的textView
            if (view != null) {
                System.out.println("cv:" + view.toString());
            }
            //现在item已经定义好了，需要将硬盘上的item.xml布局文件转化为布局对象返回回去
            //打气筒对象去填充xml，使得xml变成一个view对象
            View v;

            if (view == null) {
                //对其进行优化，对数据量大的时候出现的异常进行优化
                v = View.inflate(MyActivity.this, R.layout.item, null);
            } else {
                v = view;
            }
            final Student st = students.get(i);

            //拿到iv和tv
            ImageView iv = (ImageView) v.findViewById(R.id.item_iv);
            String sex = st.getSex();
            if ("male".equals(sex)) {
                iv.setImageResource(R.mipmap.nan);
            } else {
                iv.setImageResource(R.mipmap.nv);
            }
            TextView tv = (TextView) v.findViewById(R.id.item_tv);
            String name = st.getName();
            tv.setText(name);
            tv.setHeight(40);
            System.out.println("tv:" + tv.toString());

            v.findViewById(R.id.item_delete).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    AlertDialog.Builder builder =new AlertDialog.Builder(MyActivity.this);
                    //设置标题
                    builder.setTitle("删除");
                    builder.setMessage("确定删除吗");
                    //设置点击确定的按钮
                    builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //获得当前点击的item，将数据删除
                            String id = st.getId();
                            sdao.delete(id);
                            Toast.makeText(MyActivity.this, "删除成功" + st.getName(), Toast.LENGTH_LONG).show();

                            //重新加载数据
                            refreshView();
                        }
                    });
                    //设置点击取消的按钮
                    builder.setNegativeButton("取消",null);

                    //需要调用相应的方法才能够显示出来
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    // 或者直接调用builder.show();
                }
            });

            return v;
        }
    }


}

```
