服务：能够长期在后台运行且不提供用户界面

#### 1、编写服务的步骤

1. 继承一个service类
2. 到清单文件中进行配置
3. 启动服务，关闭服务


* **注意**：如果在主线程启动服务，资源消耗大，则会引发应用程序ANR（application not responding）

	* Activity（默认5秒钟应用程序无响应）就会报ANR
	
	* Service（默认10秒钟应用程序无响应）就会报ANR

* 解决方法：在子线程中启动和关闭服务

#### 2、示例：

因为Service的父类是Activity的父类的父类。

故：Service是没有界面的Activity

* onCreate和onDestroy

	* 创建服务和销毁服务时候执行
	
* onStartCommand和onBind（onUnbind）

	* 客户端调用startService(Intent service)时启动onStartCommand方法
	
	* 客户端调用bindService(Intent service)时启动onBind方法

QuickStartService:
```java
public class QuickStartService extends Service {
    private boolean flag =true;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("服务创建成功" + Thread.currentThread().getName());
        new Thread() {
            @Override
            public void run() {
                while (flag) {
                    System.out.println("监听USB口是否插入了U盘设备");
                    SystemClock.sleep(2000);
                }
            }
        }.start();


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("服务收到开始指令的时候会调用");
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flag =false;
        System.out.println("服务已销毁");
    }


}

```

MyActivity:(调用的就是startService和stopService)

```java

    public void start(View v){
        Intent intent = new Intent();
        intent.setClass(this,QuickStartService.class);
        startService(intent);
    }
    public void stop(View v){
        Intent intent = new Intent();
        intent.setClass(this,QuickStartService.class);
        stopService(intent);
    }

```

配置文件AndroidManifest.xml

```xml
<service android:name=".QuickStartService">
</service>

```

#### 3、进程分类

进程：一块**独立**的内存空间，用来运行程序

安卓系统尽可能保持应用程序的进程一直存在，即使在应用程序退出之后。也仍然存在。

如果发现内存不够用了，要去启动新的进程时，就会按照进程的优先级顺序去杀死某些老的进程。

1. Foreground process（前台进程）
	* 可以与用户直接进行交互的就是前台进程（可以获得焦点）
2. Visible process（可视进程）
	* 可以看到，但是不能直接与用户进行交互
3. Service process（服务进程）
	* 进程中运行了一个服务，在后台运行着
4. Background process（后台进程）
	* 不被用户可见，但是在后台运行
5. Empty process（空进程）
	* 一个进程中没有服务，也没有Activity，整个应用程序已经退出
	
> 进程的重要优先级：前台>可视>服务>后台>空

#### 4、系统进程

服务可以在后台长期运行，与当前启动服务的Activity无关。

如果要与系统服务进行交互，需要通过暴露给应用层XXXmanager来进行控制

```java
getSystemService(Context.POWER_SERVICE);

//PackageManager 安装包管理
PackageManager packageManager = getPackageManager();

 //获得运营商，电话类型，是否属于漫游状态，SIM状态，设备编号，手机号码
TelephonyManager manager= (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);


```

#### 5、Service的生命周期

###### 5.1、onStart启动服务

* Service只会被创建一次，多次启动，只会重复调用onstartcommond，而不会创建新的Service

* Service也只会被关闭一次，通过调用onDestory进行销毁

* 弊端：使用onStart开启服务，调用Service里面的内容时，对象没有Context上下文（导致例如：Toast无法调用等问题）

###### 5.2、onBind绑定服务

*　方法的执行顺序：onCreate  onBind onUnBind onDestory
	
* 解绑（onUnBind）服务，系统直接销毁服务。

* 绑定服务的时候，如果绑定服务的应用退出了，服务也会被销毁。

* 而开启服务时，如果开启服务的应用退出了，但是这个被开启的服务仍然在后台运行，是一个后台的服务进程。


MyActivity:

* 通过bindService来绑定服务。
* 通信频道需要实现ServiceConnection接口
* 上面类中的onServiceConnected方法获得代理对象。

```java
public class MyActivity extends Activity {

    IService MyAgent = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void startService(View v) {
        Intent intent = new Intent();
        intent.setClass(this, TestService.class);

        //intent:指定绑定的服务
        //SericeConnection:通讯的频道
        //flags:BIND_AUTO_CREATE(绑定的时候就去创建服务)
        bindService(intent, new MyConnection(), BIND_AUTO_CREATE);
    }

    public void stopService(View v) {
        Intent intent = new Intent();
        intent.setClass(this,TestService.class);
        stopService(intent);
    }

    public void callMethod(View v) {
         MyAgent.callMethodInService();
    }

    private class MyConnection implements ServiceConnection {

        //成功绑定的时候调用
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //此处iBinder调用的是获得的就是TestService中的MyAgent
            MyAgent = (IService) iBinder;
        }

        //接触绑定的时候，会调用，释放资源
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }

}


```
TestService:

* TestService是Service类的实现类

* onBind方法中放入要返回的代理对象。MyActivity中MyConnection的onServiceConnected可以获取。

* 代理类myAgent需要实现Binder类（Binder类实现IBinder接口），其中将需要执行的服务类中的方法传入进去

* 通过引入IService接口，将需要暴露出来的方法放在IService中，其他不希望被引用的方法通过private类进行隐藏。

```java

public class TestService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return new myAgent();
    }
    
    @Override
    public IBinder onUnBind(Intent intent) {
        return super.onUnbind(intent);
    }

    //代理人,将所要执行的方法放入到代理中
    //通过接口IService，暴露方法callMethodInService，隐藏其中的方法otherMethod和otherMethod2。
    private class myAgent extends Binder implements IService {
        public void callMethodInService() {
            methodInService();
        }
        public void otherMethod(){

        }
        public void otherMethod2(){

        }
    }

    //需要调用的方法：Service中的Method
    public void methodInService() {

        Toast.makeText(TestService.this, "调用服务中方法成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("服务创建了");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("服务销毁了");
    }
}

```

IService.java

```java

public interface IService {

    public void callMethodInService();
}

```

#### 6、混合开启服务

* 绑定服务可以调用服务中的方法
* 开启服务可以让服务在后台运行

> 如果以后需要去在后台一直运行服务，并且又想调用服务中的方法，那么请严格按照如下步骤去走：

> 1. 开启服务
> </br>super.onCreate()
> 2. 绑定服务
> </br>需要返回代理对象
> 3. 调用服务的方法
> 4. 解绑服务
> </br>super.onUnBind();
> 5. 关闭服务
> </br>super.onDestroy();



