[JSON官网](http://json.org/)

知识点：

* JSONObject jsonObj = new JSONObject(data);通过流数据获取jsonObj
* getJSONObject（后面加key，获取value）
* getString（数组，后面加int获取数组值）
* getJSONArray（后面加key值）


JSON的格式：

* JSON对象 	{key:value,key:value,key:value}
	* key必须是字符串（key可以加引号，也可以不加引号）
* JSON数组  [值1，值2，值3]
* 嵌套：[{name:张三},{age:19}]


示例：获取天气的地址（返回类型JSON）</br>
http://wthrcdn.etouch.cn/weather_mini?city=深圳

```java
public class MyActivity extends Activity {

    private EditText ed_city;
    private TextView city_result1;
    private TextView city_result2;
    public static final int SUCCESS = 1;
    public static final int INVALID = 0;
    private JSONArray jsonArray;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    JSONArray data = (JSONArray) msg.obj;
                    try {
                        String one  = data.getString(0);
                        String two = data.getString(1);
                        city_result1.setText(one);
                        city_result2.setText(two);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MyActivity.this, "城市无效", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case INVALID:
                    Toast.makeText(MyActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
                    break;
            }


        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ed_city = (EditText) findViewById(R.id.ed_city);
        city_result1 = (TextView) findViewById(R.id.city_result1);
        city_result2 = (TextView) findViewById(R.id.city_result2);
    }

    String path;
    String city;

    public void searchCityWeather(View v) {
        city = ed_city.getText().toString().trim();
        try {
            path = "http://wthrcdn.etouch.cn/weather_mini?city=" + URLEncoder.encode(city, "UTF-8");
            if (TextUtils.isEmpty(path)) {
                Toast.makeText(this, "路径错误", Toast.LENGTH_SHORT);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //发起请求给网站：
        new Thread() {


            public void run() {
                try {
                    URL url = new URL(path);
                    //设置必要的参数信息
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    // conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");

                    //判断响应码
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream in = conn.getInputStream();
                        String data = StreamTool.decodeStream(in);
                        //解析json对象
                        JSONObject jsonObj = new JSONObject(data);

                        //获得desc的值
                        String result = jsonObj.getString("desc");
                        if ("OK".equals(result)) {
                            //城市有效,获取每天的天气
                            JSONObject dataObj = jsonObj.getJSONObject("data");
                            jsonArray = dataObj.getJSONArray("forecast");
                            Message msg = Message.obtain();
                            msg.obj = jsonArray;
                            msg.what = SUCCESS;
                            mHandler.sendMessage(msg);
                        } else {
                            //城市无效
                            Message msg = Message.obtain();
                            msg.obj = jsonArray;
                            msg.what = INVALID;
                            mHandler.sendMessage(msg);
                        }

                    }
                } catch (Exception e) {
                    Message msg = Message.obtain();
                    msg.obj = jsonArray;
                    msg.what = INVALID;
                    mHandler.sendMessage(msg);
                    e.printStackTrace();
                }

            }
        }.start();
    }
}


```
