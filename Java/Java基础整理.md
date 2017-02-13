# Java

### 1.面向对象的三个特征：
1. 封装：隐藏类的内部实现，仅对外提供公共访问方法
2. 继承：代码复用，子类直接访问父类的非私有的方法和成员
3. 多态：定义的引用变量在编译时不确定，只有在程序运行时才确定，即引用变量到底指向哪个类的实例对象。提高代码可扩展性，消除类之间的耦合关系
  * 重载(override)：父类的引用指向子类的对象，子类方法重载父类方法(动态分派)
  * 重写(overwrite)（静态分派）

### 2. Java初始化的过程
  * 静态成员（包括static修饰的静态块，先父后子，顺序执行，且只执行一次）
  * 父类（普通成员初始化-->构造方法调用）
  * 子类初始化（普通成员初始化-->构造方法调用）

### 3. 8种基本数据类型
  * boolean (1/8)| byte (1) | char (2) | short (2) | int (4) | long (8) | float (4) | double (8)
  * boolean不能转换成其他类型，范围大的转化为范围小的要强转

### 4. Java常见集合类
1. Collection接口
  * List --> ArrayList(数组实现，查找快，插入删除慢)|LinkedList（链表实现，查找慢，插入删除快）
  * Set(无重复数) --> HashSet(散列函数，无序，快速访问)|LinkedHashSet（线程安全）|TreeSet(红黑树，有序)
  * Queue -->LinkedList提供了接口的实现
2. Map接口
  * HashMap|TreeMap|LInkedHashMap|HashTable

3. 迭代器
  * 包含hasNext，next，remove(remove执行之前必须调用next)
  * map在调用Iterator的时候，必须先调用keySet，entrySet方法




### 5. HashMap为什么不是线程安全的？
> 可能出现线程不安全的地方：多个线程线程同时put到数组的时候，可能出现覆盖；多线程同时扩容，但是只有一个线程的值可以赋值给Table，又出现了丢失。

1. 数组的存储效率最高，通过key值先计算出hash值，获得对应在数组的位置。
2. HashMap内部存储是通过链地址法(链表，O(n))解决Hash冲突,Java8中默认超过8个元素(TREEIFY_THRESHOLD)时使用平衡树(O(logn))来解决。
3. 其内部的Node默认为16，如果超过LoadFactor*当前大小，会进行扩容。


### 6. HashMap，HashTable，ConcurrentHashMap区别及联系
1. HashTable使用的synchronized保证线程安全，但容易阻塞，效率低；ConcurrentHashMap使用Segment(段)及锁分离技术来控制，即每一小段都是一个HashTable，都有自己的锁，保证了可以并发执行。[Java8中利用了CAS算法]
2. HashMap允许存null，HashTable不允许。
3. 同时HashMap可以很好的转为LinkedHashMap。

注：
* CopyOnWriteArrayList:读少写多，底层创建一个副本，在副本上操作，并通过原子操作替换
* ReadAndWriteLock：读多写少，读读可以，其他都互斥

### 7. 常用的hash算法有哪些？
hash函数可以实现一个伪随机数生成器(PRNG)，其步骤：
1. 用散列函数将被查询的键转化为数组的一个索引。
2. 处理碰撞冲突：拉链法和线性探测法

散列函数：
* 基于加法和乘法的散列（RS，BKDR）
* 基于移位的散列（JS，PJW，ELF，SDBM，DJB，DEK，AP）

[参考](http://www.360doc.com/content/11/0817/12/18042_141130892.shtml)
### 8. 什么是一致性哈希？
一般的Hash算法做负载均衡的时候，容错性（机器不可用）和扩展性（增加机器）不好。而一致性哈希算法原理：
1. 将整个哈希值空间组织成一个虚拟的圆环，分别计算服务器和主机的hash值
2. 主机沿着环顺时针"行走"，第一个遇到的服务器就是其应该定位到的服务器
3. 可以通过增加虚拟节点，使得分布均匀一点。

[参考](http://blog.codinglabs.org/articles/consistent-hashing.html)


### 9.StringBuilder,StringBuffer和String的区别
1. String和StringBuffer是线程安全的，StringBuilder不是线程安全的
2. String是不可变的，即每次添加数据时候是创建新对象，其他是append
3. 执行效率，StringBuilder快于StringBuffer快于String

注：string两种创建方法：
  * 双引号：创建时现在string pool中查看有没有，没有才创建，有直接用
  * new对象：无论如何都创建，放在heap中；同时如果string pool中不存在，也会在其中创建

### 10. 正则
```java
Pattern pattern= Pattern.compile(".*?,(.*)");
Matcher matcher =pattern.matcher(result);
//部分匹配
if(matcher.find()){
  matcher.grounp(1);
}
//完全匹配
matcher.matches();
```

### 11. Java创建对象的几种方法
> new对象；工厂模式；反射；克隆

1. 通过调用工厂类中的方法，由工厂类创建对象。
2. 反射机制就是在运行过程中获取自身对象，一般通过Class.forName().newInstance()获取类对象；然后获取字段，方法以及构造器等。通过设置setAccessible，可以访问private对象。
3. 浅拷贝和深拷贝的区别
  * 浅拷贝是指变量值相同，但是所有引用还是指向原来的对象。
  * 深拷贝是指所有值和引用都是复制之后的新对象。

### 12. 异常分类和处理
* Throwable
  * Error
  * Exception
    * IOException
      * EOFException
      * FileNotFoundException
    * RuntimeException

同时就算遇到break和continue语句的时候，finally语句也会得到执行。除非出现System.exit()或者the JVM crashes first

```java
int ret = 0;
try{
  throw new Exception();
}
catch(Exception e){
  ret = 1;
  return ret;
}
finally{
  ret = 2;
}
```
主要的考点就是catch中的return在finally之后执行 但是会将return的值放到一个地方存起来，**所以finally中的ret=2会执行，但返回值是1。**


### 13.接口和抽象类的区别
1. 接口可以实现多重继承；抽象类不可以
2. 接口没有构造函数，且方法都没有实现；抽象类有构造方法，且可以实现一部分方法。
3. 接口不能有数据成员，只有有静态常量（public static）；抽象方法可以有
4. Java8中interface可以使用default method

其他：两者均不能实例化；接口的变量默认public static final；方法默认public abstract

### 14.匿名内部类是什么？如何访问在其外面定义的变量？
内部类主要分类：
1. 成员内部类：可以访问外围类的所有元素
2. 静态内部类：只可以访问外部类的静态成员和静态方法
3. 匿名内部类：
  * 没有构造方法；不能有静态成员或者类
  * 类不能用修饰符修饰；必须跟在new后面
4. 局部内部类：方法中的类，只能访问方法中定义了final类型的局部变量。
> 访问外面定义的变量：OuterClass.this.OuterClassMethod

### 15. Object方法有哪些？
  getClass：可以获取一个类的定义信息，然后使用反射去访问其全部信息(包括函数和字段)
  hashCode|equals|clone：重写equals方法之后都要重写hashCode
  toString：转换成字符串
  notify|notifyAll|wait：睡眠+唤醒
  finalize：垃圾回收

### 16. 关键字
* final基本数据类型为常量；对象类型为不可修改引用；类不可被继承；方法不可被重载
* static：类级别的，类加载时候就加载。static方法内部不能调用非static方法
* abstract： 不能和final、private、static共存

### 17. Java都是按值传递的
* 基本数据类型+String：传递的是值得拷贝
* 其他：传递的是地址的拷贝(在形参里面nwe一个对象后，两者不是指向同一个值)
