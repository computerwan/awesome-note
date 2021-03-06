###1、GC的概念
* Garbage Collection垃圾收集
* GC的对象是堆空间和永久区

###2、GC算法
#####2.1、引用计数法
* 任何对象引用，就+1，引用失效，就-1，只要引用计数器的值为0，则对象不可能再被使用。
* 问题：引用和去引用伴随加法和减法，影响性能。同时很难处理循环引用

#####2.2、标记清除
* 分为两个阶段：标记阶段和清除阶段
* 标记阶段：通过根节点，标记可达对象。
* 清除阶段：未被标记的对象就是未被引用的垃圾对象，清除。

#####2.3、标记压缩
* 使用于存活对象比较多的场合，如**老年代**
* 从根节点开始，对所有可达对象做一次标记。但不简单清除未标记的对象，而是将所有的存活对象压缩到内存的一端。**之后清理边界外所有的空间**

#####2.4、复制算法
* 相对高校的回收方法，不适合存活对象多的，如老年代
* 将原有的内存空间分为两块，每次只使用其中一块，在垃圾回收时，将正在使用的内存中的存活对象复制到未使用的内存中，之后清除正在使用的内存块中的所有对象，交换两个内存的角色，完成垃圾回收。
* 问题：空间浪费
eden from to 三个空间

#####2.5、分代思想
* 依据对象的存活周期进行分类，短命对象归为新生代，长命对象归为老年代
* 根据不同代的特点，选择合适的收集算法
	* 少量对象存活，适合复制算法
	* 大量对象存活，适合标记清理或者标记压缩

###3、可触及性
* 可触及：从根节点可以触及到这个对象
* 可复活：一旦所有引用被释放，就是可复活状态
	* 因为在finalize()中可能复活该对象
* 不可触及
	* 在finalize（）后，可能会进入不可触及状态
	* 不可触及的对象不可能复活
	* 可以回收

经验：
* 避免使用finalize()，操作不慎可能导致错误
* 优先级低，何时被调用，不确定
* 可以使用try-catch-finally来代替它

根:
* 栈中引用的对象
* 方法区中静态成员或者常量引用的对象（全局对象）
* JNI方法栈中引用对象

###4、Stop-The-World
* 定义：Java中一种全局暂停的现象。
* 全局停顿，所有Java代码停止，native代码可以执行，但不能和JVM交互
* 垃圾清理的同时，还同时产生，永远清理不完，只有当大家停止了，才能将房间打扫干净。
* 还有引起的情况：
	* Dump线程
	* 死锁检查
	* 堆Dump






