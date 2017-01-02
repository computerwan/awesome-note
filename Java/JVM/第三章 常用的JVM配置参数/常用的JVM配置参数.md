###1、Trace跟踪参数
######1.1打印GC的简要信息：
- -verbose:gc
- -XX：+printGC
	- [GC 4790K->374k(15872k),0.0001606 secs]
	- 4m空间，大小16m左右

######1.2打印GC详细信息：
- -XX：+PrintGCDetails
	- def new generation total 13824K，used 11223K[0x27e80000,0x28d80000,0x28d80000]
	- 新生代 [低边界（起始位置） 当前边界（当前位置） 最高边界（最多申请的位置）]
	- （最高边界-最低边界）/1024/1024=15m 表示分配15m

######1.3打印GC发生的时间戳:
- -XX：+PrintGCTimeStamps

######1.4指定GC log的地址，以文件输出:
- -Xloggc:log/gc.log
- 输出到当前目录下的log目录下的gc.log文件中

######1.5每次一次GC后，都打印堆信息:
- -XX:+PrintHeapAtGC

######1.6监控类的加载
- -XX:+TraceClassLoading

######1.7打印类的使用情况
- -XX:+PrintClassHistogram 按下Ctrl+Break后，会打印类的信息
- 分别显示：序号、实例数量、总大小、类型（B是byte数组，C是char数组的意思）

###2、堆的分配参数
######2.1指定最大堆和最小堆
* -Xmx20m -Xms5m
	* 设置最大堆空间20m，最小堆空间5m
* 查看：
	* 最大：Runtime.getRuntime().maxMemory()/1024.0/1024
	* 可用：Runtime.getRuntime().freeMemory()/1024.0/1024
	* 总：Runtime.getRuntime().totalMemory()/1024.0/1024
	
1. 当前分配到的空间5m，即总的空间为5m
2. 如果此时分配一个1m的数组，最大空间不变，可用空间减少1m，总空间是不变的
3. Java会尽可能维持在最小堆中运行
4. 当分配4m的数组，最大空间不变，可用空间没变（随着总空间的增加相应没变化），总空间增加到9m
5. 使用System.gc()后，可用空间增多

######2.2设置新生代大小
- -Xmn 

######2.3新生代（eden+2*s）和老年代（不包含永久带）的比值
- -XX:NewRation
- 4表示 新生代：老年代=1:4 ，即年轻代占堆的1/5

######2.4设置两个Survivor区和eden的比
- -XX：SurvivorRatio
- 8表示 两个Survivor：eden=2:8，即一个Survivor占年轻代的1/10

######2.5OOM时导出堆到文件
- -XX:+HeapDumpOnOutOfMemoryError

######2.6导出OM的路径
- -XX:HeapDumpPath

######2.7在OOM时，执行一个脚本
- -XX:OnOutOfMemoryError
- "-XX:OnOutOfMemoryError=D:/tools/printstack.bat %p"

####总结：
* 根据实际事情调整新生代和幸存代的大小
* 官方推荐新生代占堆的3/8
* 幸存代占新生代的1/10
* 在OOM时，记得Dump出堆，确保可以排查现场问题


###3、永久区分配参数
设置永久区的初始空间和最大空间，即一个系统可以容纳多少个类型
- -XX:PermSize -XX:MaxPermSize
例：使用CGLIB会产生大量的类，分配在永久带

如果堆空间没有用完也抛出OOM，有可能是永久区导致的

###4、栈的分配参数 
- -Xss
	* 通常只有几百k
	* 决定了函数调用的深度
	* 每个线程都有独立的栈空间
	* 局部变量、参数分配在栈上

java.lang.StackOverFlowError 栈溢出错误
