###1、JVM概念

虚拟机：通过软件模拟的具有完整硬件系统功能的、运行在一个完全隔离环境中的完整计算机系统

* 虚拟机分类：VMVare，Visual Box，JVM
	* VMVare，Visual Box使用软件模拟物理CPU的指令集
	* JVM使用软件模拟Java字节码的指令集
	
###2、Java语言规范
* 语法、词法
	* if（Expression）Statement
	* \u + 4个16进制数字，表示UTF-16
	* 行终结符：CR，LF or CR LF
	* 注释
	* 标识符
	* 关键字
* 类型、变量
	* 元类型（基本数据类型）
	* 变量初始值
	* 泛型
* Java内存模型
* 类加载连接的过程

###3、JVM规范
Java、Groovy、Clojure，Scala都可以在JVM上面运行。

* Class文件类型
* 运行时数据
	* returnAddress数据类型，指向操作码的指针
	* float的表示，支持IEEE754
		* seeeeeeeemmmmmmmmmmmmmmmmmmmmmmm
		* 指数：8 尾数：23
		* e全0，尾数附加位为0，否则尾数附加位为1
		* s*m*^(e-127)
* 帧栈
* 虚拟机的启动
* 虚拟机的指令集
	* 类型转化(l2i)
	* 出栈入栈操作(aload astore)
	* 运算(iadd isub)
	* 流程控制(ifeq ifne)
	* 函数调用(invokevirtual invokeinterface invokespecial invokestatic)
* JVM需要Java Library提供一下支持：
	* 反射 java.lang.reflect
	* **ClassLoader**
	* 初始化class和interface
	* 安全相关 java.security
	* 多线程
	* 弱引用
* JVM的编译
	* 源码到JVM指令的对应格式
	* Javap
	* JVM反汇编的格式
	<index><opcode>[<opearnd1>...][<comment>]

