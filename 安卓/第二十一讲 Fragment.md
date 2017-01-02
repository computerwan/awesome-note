注意：

* LayoutInflater和inflate是用来找res/layout/下的xml布局文件，并且实例化；
* findViewById()是找xml布局文件下的具体widget控件(如Button、TextView等)。 

#### 1、Fragment是一个轻量级的Activity

* Fragment不需要再AndroidManifest.xml中注册

* [main.xml](https://github.com/computerwan/Android_Dev/blob/master/apps/Fragment/res/layout/main.xml)

* [displayfragment.xml](https://github.com/computerwan/Android_Dev/blob/master/apps/Fragment/res/layout/displayfragment.xml)
(其他同)

* [DisplayFragment.java](https://github.com/computerwan/Android_Dev/blob/master/apps/Fragment/src/cn/edu/usst/Fragment/DisplayFragment.java)(其他同)

	* 注：这里返回的View对象是通过inflater的inflate方法绑定视图。

* [MyActivity.java](https://github.com/computerwan/Android_Dev/blob/master/apps/Fragment/src/cn/edu/usst/Fragment/MyActivity.java)
	* 首先获得的具体的XXFragment对象
	* 获取FragmentManager对象
	* 通过manager对象启动FragmentTransaction事务
	* 调用事务，替换Fragment里面内容，并确定


#### 2、获得Activity中内容

* [generalfragment.xml](https://github.com/computerwan/Android_Dev/blob/master/apps/Fragment/res/layout/generalfragment.xml)

* [GeneralFragment.java](https://github.com/computerwan/Android_Dev/blob/master/apps/Fragment/src/cn/edu/usst/Fragment/GeneralFragment.java)
	* 首先获取需要返回的View对象
	* 通过view对象的findViewById获取按钮
	* 通过触发单击监听器设置。

> **在Fragment中,需要获取Activity中的内容，都需要增加getActivity()**

#### 3、Fragment的生命周期：

Fragment的生命周期与之前Activity一样，就多了一个oncreateView方法，其在oncreate之后调用，如果最小化后再次回到Fragment界面，不会重新创建，而是复用已有对象。

> 具体执行顺序 ：

> onCreate > onCreateView > onStart > onResume > onPause > onStop > onDestory

> 最小化后，恢复：

> onStart > onResume(不执行onCreate和onCreateView)

#### 4、如何向下兼容在低版本中使用Fragment框架

* 引入jar包： Android-support-v4.jar
* 将文件中FragmentActivity，FragmentManager，FragmentTransaction，Fragment都替换成v4的jar包内容
