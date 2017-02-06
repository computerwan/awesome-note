#### 一、两者的比较

![](https://github.com/computerwan/Android_Dev/blob/master/others/9.1.jpg)


#### 二、get方式传输中文需要注意编码问题

编码：URLEncoder.encode(number,"UTF-8")</br>
解码：number=new String(number.getBytes("iso8859-1"),"UTF-8")

#### 三、post编码

跟get方式相比，需要修改以下内容：
```java
conn.setRequestMethod("POST");
// Content-Type: application/x-www-form-urlencoded
conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
String data = "number="+URLEncoder.encode(number, "utf-8")+"&pwd="+URLEncoder.encode(pwd, "utf-8");
//Content-Length: 20
conn.setRequestProperty("Content-Length", data.length()+"");
//标志, 表明需要为服务器写数据了 
conn.setDoOutput(true);
conn.getOutputStream().write(data.getBytes());
```
#### 四、使用apache的httpClient
使用Ctrl+H，查看httpClient接口的继承关系</br>
![](https://github.com/computerwan/Android_Dev/blob/master/others/9.2.jpg)
###### 发送GET方式的请求
写在线程当中，替代原来使用URL获取。
通过HttpClient和HttpGet获得HttpResponse，通过response获得返回码code和输入流InputStream
```java
new Thread() {

            public void run() {

                try {

                    String path = "http://192.168.1.100:8080/web_login/login?";
                    path = path + "number=" + URLEncoder.encode(number, "UTF-8") + "&pwd=" + URLEncoder.encode(pwd, "UTF-8");

                    HttpClient client = new DefaultHttpClient();
                    HttpGet get = new HttpGet(path);
                    //执行get后将获得响应
                    HttpResponse response = client.execute(get);
                    //http的响应分为响应行，响应头，响应体
                    //获取返回码 200表示正常
                    int code = response.getStatusLine().getStatusCode();

                    if (code == 200) {
                        InputStream in = response.getEntity().getContent();
                        String s = StreamTool.decodeStream(in);
                        Message msg = Message.obtain();
                        msg.what = SUCCESS;
                        msg.obj = s;
                        mHandler.sendMessage(msg);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = Message.obtain();
                    msg.what = ERROR;
                    mHandler.sendMessage(msg);
                }

            }

            ;
        }.start();

```

###### 发送POST方式的请求
与GET不一样的是，通过一个List<NameValuePair>列表，将需要传入的数据放入实体

```java
       new Thread() {
            public void run() {
                try {
                    String path = "http://192.168.1.100:8080/web_login/login";

                    HttpClient client = new DefaultHttpClient();
                    HttpPost post = new HttpPost(path);

                    //想需要带过去的参数，放在NameValuePair中，然后放在list中，然后将list给需要带过去的数据实体
                    List<NameValuePair> list = new ArrayList<NameValuePair>();
                    list.add(new BasicNameValuePair("number",number));
                    list.add(new BasicNameValuePair("pwd",pwd));

                    //设置带给服务器数据的实体
                    post.setEntity(new UrlEncodedFormEntity(list,"UTF-8"));

                    //执行post后将获得响应
                    HttpResponse response = client.execute(post);
                    //http的响应分为响应行，响应头，响应体
                    //获取返回码 200表示正常
                    int code = response.getStatusLine().getStatusCode();

                    if (code == 200) {
                        InputStream in = response.getEntity().getContent();
                        String s = StreamTool.decodeStream(in);
                        Message msg = Message.obtain();
                        msg.what = SUCCESS;
                        msg.obj = s;
                        mHandler.sendMessage(msg);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = Message.obtain();
                    msg.what = ERROR;
                    mHandler.sendMessage(msg);
                }
            };
        }.start(); 
```

####　五、使用开源框架async_http_client

###### GET方法
```java
String path = "http://192.168.1.100:8080/web_login/login?number="+number+"&pwd="+pwd;

//封装了API，内部同上使用handler进行处理        
AsyncHttpClient client = new AsyncHttpClient();

client.get(path, new AsyncHttpResponseHandler() {    
	//请求成功的时会被调用
	@Override
	public void onSuccess(int statusCode, Header[] headers,
			byte[] responseBody) {
	//new String(responseBody)将响应体里面的数据转换成字符串
		tv_status.setText(new String(responseBody));
	}

	//请求失败的时会被调用
	@Override
	public void onFailure(int statusCode, Header[] headers,
			byte[] responseBody, Throwable error) {
		error.printStackTrace(System.out);
		Toast.makeText(MainActivity.this, "出错误了", 0).show();
	}
```

###### POST方法
```java
String path = "http://192.168.1.100:8080/web_login/login";
AsyncHttpClient client = new AsyncHttpClient();
	
//需要传入post里面的params数据
RequestParams params = new RequestParams();
params.add("number", number);
params.add("pwd", pwd);
	
client.post(path, params, new AsyncHttpResponseHandler(){
	@Override
	public void onSuccess(int statusCode, Header[] headers,
			byte[] responseBody) {
		tv_status.setText(new String(responseBody));
	}

	@Override
	public void onFailure(int statusCode, Header[] headers,
			byte[] responseBody, Throwable error) {
		error.printStackTrace(System.out);
		Toast.makeText(MainActivity.this, "出错误了 ", 0).show();
	}
	
});
```
