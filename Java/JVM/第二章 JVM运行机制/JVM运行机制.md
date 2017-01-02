###1、JVM启动流程
1. Java XXX
2. 装载配置（根据当前路径和系统版本寻找jvm.cfg)
3. 根据配置寻找JVM.dll(JVM.dll为JVM主要实现)
4. 初始化JVM获得JNIEnv接口（JNIEnv为JVM接口，findClass等操作通过它实现）
5. 找到main方法并运行

###2、JVM基本结构
[JVM的基本结构](https://github.com/computerwan/Java/blob/master/course/JVM/%E7%AC%AC%E4%BA%8C%E7%AB%A0%20JVM%E8%BF%90%E8%A1%8C%E6%9C%BA%E5%88%B6/JVM%E5%9F%BA%E6%9C%AC%E7%BB%93%E6%9E%84.jpg)
classLoader
内存空间，PC寄存器
垃圾收集器
执行引擎

###3、内存空间
* PC寄存器
	* 每个线程拥有一个PC寄存器
	* 在线程创建时创建
	* 指向下一条指令的地址
	* 执行本地方法时，PC的值为undefined
* 方法区
	* 保存装载类的信息（类型的常量池，字段方法信息，方法字节码）
	* 通常和永久区（Perm）关联在一起
	* JDK7 String已经移到堆里面（JDK6在方法中）
* Java堆
	* 和程序开发密切相关
	* 应用系统对象都保存在Java堆中
	* 所有线程共享Java堆
	* 对分代GC来说，堆也是分代的
	* GC的主要工作区间
	* 复制算法 |eden|s0|s1|tenured|
* Java栈
	* 线程私有
	* 栈由一系列帧组成（因此Java栈也叫做帧栈）
	* 帧保存一个方法的局部变量、操作数栈、常量池指针
	* 每一次方法调用创建一个帧，并压栈
	* 局部变量表（包含**参数**和局部变量）
	[局部变量表](https://github.com/computerwan/Java/blob/master/course/JVM/%E7%AC%AC%E4%BA%8C%E7%AB%A0%20JVM%E8%BF%90%E8%A1%8C%E6%9C%BA%E5%88%B6/%E5%B1%80%E9%83%A8%E5%8F%98%E9%87%8F%E8%A1%A8.jpg)
	* Java没有寄存器，所有参数传递使用操作数栈
	* 栈上分配
		* 栈上面永远不会存在内存泄漏（内存泄漏:分配空间，但没有释放会导致）
		* 小对象（一般几十个bytes）
		* 可以自动回收，减轻GC压力
		* 大对象或者逃逸对象无法栈上分配
		
####4、内存模型
* 每一个线程有一个工作内存和主存独立
* 工作内存存放主存中变量的值的拷贝

[内存模型](https://github.com/computerwan/Java/blob/master/course/JVM/%E7%AC%AC%E4%BA%8C%E7%AB%A0%20JVM%E8%BF%90%E8%A1%8C%E6%9C%BA%E5%88%B6/%E5%86%85%E5%AD%98%E6%A8%A1%E5%9E%8B.jpg)

当数据从主内存复制到工作存储时，必须出现两个动作：
1. 由主内存执行的读（read）操作；
2. 由工作内存执行的相应的load操作；

当数据从工作内存拷贝到主内存是，也出现两个操作：
1. 由工作内存执行的存储（store）操作
2. 由主内存执行的相应的写（Write）操作

每一个操作都是**原子**的，即执行期间不会被中断

对于普通变量，一个线程中更新的值，不能马上反应在其他变量中

如果需要在其他线程中立即可见，需要使用volatile关键字

###5、概念：volatile和可见性、有序性

#####5.1 volatile
不能代替锁
一般认为volatile比锁性能好（不绝对）
选择使用volatile的条件是：语义是否满足应用
[volatile](https://github.com/computerwan/Java/blob/master/course/JVM/%E7%AC%AC%E4%BA%8C%E7%AB%A0%20JVM%E8%BF%90%E8%A1%8C%E6%9C%BA%E5%88%B6/volatile.jpg)

#####5.2 可见性
一个线程修改了变量，其他线程可以立即知道
* volatile
* synchronized（unlock之前，写变量值回主存）
* final（一旦初始化完成，其他线程就可见）

#####5.2 有序性
* 在本线程内，操作都是有序的
* 在线程外观察，操作都是无序的（指令重排 或 主内存同步延时）
* 指令重排破坏有序性

#####5.3 指令重排的基本原则
* 程序顺序原则：一个线程内保证语义的串行性
* volatile规则：volatile变量的写，先发生于读
* 锁规则：解锁（unlock）必然发生在随后的加锁（lock）前
* 传递性：A先于B，B先于C，那么A必然先于C
* 线程的start方法先于它的每一个动作
* 线程的所有操作先于线程的终结（Thread.join()）
* 线程的中断（interrupt()）先于被中断线程的代码
* 对象的构造函数执行结束先于finalize()方法

###6、编译和解释运行的概念

* 解释运行
	* 以解释方式运行字节码
	* 读一句执行一句

* 编译运行（JIT）
	* 将字节码编译成机器码
	* 直接执行机器码
	* 运行时编译
	* 编译后性能有数量级的提升（10倍以上提升）

