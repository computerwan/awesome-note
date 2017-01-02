#### 四大核心组件：
* Activity（活动）
* BroadcastReceiver（广播接受者）
* Service（服务）
* ContentProvider（内容提供者）


#### 1,生命周期（七项）
任何一个对象从创建到最后销毁这整个过程称之为生命周期</br>

生命周期的方法：一个对象从最开始创建到最后销毁整个过程中，特定的时间段会执行的方法叫做生命周期的方法。</br>

但凡设计到组件的生命周期和方法，只要以onXXX打头的，这些方法都是由系统调用（无需自己调用）</br>
	
* onCreate 创建对象时调用
	
* onDestroy 销毁对象时调用（按返回键）

	* 将退出时候的状态保存起来，以便下次进入的时候重写显示

```java
	public class MainActivity extends Activity {
		private EditText ed_content;
		
		SharedPreferences sp;
		
		//创建时调用
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			ed_content = (EditText) findViewById(R.id.ed_content);
			
			sp = getSharedPreferences("config", 0);
			String vl = sp.getString("content", "");
			
			ed_content.setText(vl);
		}
		//销毁时调用
		@Override
		protected void onDestroy() {
			super.onDestroy();
			
			String content = ed_content.getText().toString().trim();
			
			//将数据保存到sharedPrederence中
			sp = getSharedPreferences("config", 0);
			Editor edit = sp.edit();
			edit.putString("content", content);
			edit.commit();
			Toast.makeText(this, "已保存 ", 0).show();
		}
		
		
	}
```

* onStart 当Activity可见的时候回被调用
	* 使用时，电话接通，Activity即变为不可见
	* 开始视频播放时调用
	
* onStop 当Activity不可见的时候会被调用
	* 电话打完了，返回，Activity变为可见
	* 暂停视频播放时调用
	
* onPause 暂停
	* 当界面处于可见，失去焦点的时候会被调用
	* 如果一个界面透明，可以可见而不获得焦点
	* 在androidManifest.xml中配置Activity：
	* <!-- android:theme="@android:style/Theme.Translucent" -->
* onResume 继续
	* 当界面处于可见，获得焦点的时候调用

* onRestart 重新开始

打开后，按返回键退出的执行顺序：</br>
onCreate——> onstart——> onResume——>onPause  ——> onStop ——>onDestroy</br> 
	
如果按home键返回，再重新打开的时候</br>
onPause  ——> onStop ——>onRestart</br>

注：如果横竖屏切换时候，Activity生命的周期：销毁当前的Activity，然后重写创建Activity</br>
所以当前的游戏，是会影响用户的感受。控制Activity创建的时候，不要因为横竖屏切换等因素，而影响到其生命周期。</br>

解决方法：

1. Manifest中可以修改Activity的screenOrientation，其包括portrait（竖直）和landscape（横屏）

2. onConfigChanges配置更改，touchscreen，keyboard（不会因为出现软键盘），orientation（屏幕朝向），screenSize（屏幕大小改变）导致生命周期发生影响。

#### 2，启动模式（LaunchMode）（四种）
* Task（任务）：维护和记录了当前应用内存空间中有哪些组件在运行着
* stack（栈）：后进先出
* 任务栈：一种内存结构，存储了当前应用中各组件的运行状况

	
LaunchMode：启动模式</br>
在Manifest中设置：android:launchMode</br>
期间可以使用intent中的setflags更换启动模式</br>

四种模式：

* standard（default）

	每次收到intent，就会将新创建的Activity的实例放到任务栈中，返回一次就销毁一个最近的。
* singleTop（单一顶部模式）

	</br>如果发现当前的任务栈中栈顶是当前Activity的实例，那么就直接使用当前的Activity的实例，不再新创建</br>
	（常用于短信编辑界面）

* singleTask（单一任务栈模式）

	</br>表示当前的Activity只会在当前的任务栈中只有一个实例。</br>
	如果再次尝试启动当前的Activity的时候，当前Activity不是处于任务栈栈顶，会清空目标Activity之上的Activity。</br>
	应用场景：如果一个Activity启动的时候，占用cpu非常多，手机内存又非常珍贵，这时候建立将Activity设置为singleTask</br>
	（常用于浏览器）

* singleInstance（单一实例模式）

	</br>整个android手机中只会有一个实例,如果切换Activity，则会新建一个任务栈，存放新建的Activity。</br>
	对于一些整个系统中，永远只存在同一截面Activity时候使用</br>
	（常用于打电话界面）
	
	
