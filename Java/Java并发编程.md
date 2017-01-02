# Java并发
1. 概念
2. 线程
3. 多线程
4. concurrent包(线程池等)

## 一、概念

### 1. 进程和线程区别？
1. 不同的进程地址空间是独立的，而同一个进程内的线程共享同一地址的空间。
2. 进程是资源分配和调度的单位，线程是基本调度单位
3. 资源是分配给进程的，线程只拥有很少资源

### 2. 进程之间和线程之间通信。
不同进程之间通过套接字、信号处理器、共享内存和信号量的实现数据交换
  * 套接字：源IP地址和目的IP地址以及源端口号和目的端口号
  * 信号：用于通知接收进程某个事件已经发生
  * 共享内存：映射一段能被其他进程所访问的内存
  * 信号量： 信号量是一个计数器，可以用来控制多个进程对共享资源的访问。是一种锁机制。

线程之间通信：锁机制(互斥锁、条件变量、读写锁);信号量机制;信号机制

### 3. 同步和异步什么区别?
* 同步：等待完成才返回
* 异步：马上就能返回，但程序会在后台继续执行，执行完返回通知
* 阻塞：一个线程占用了临界区的资源，那么其他所有需要这个资源的线程都必须在这个临界区进行等待。
* 非阻塞：允许多个线程同时进入临界区
* 并行(parallel)：多个cpu实例同时执行一段逻辑，是真正同时
* 并发（concurrent）：通过cpu调度算法，让用户看上去同时执行，实际从cpu层面看不是真正同时

### 4. Java对BIO、NIO、AIO的支持
* BIO：同步并阻塞(Blocking I/O)
* NIO：同步非阻塞(Non Blocking I/O)
  * Channels：类似于流，数据可以在Channel和Buffer中相互转换。
  * Buffers：包含除boolean型以外的各种基本类型。
  * Selectors：允许单线程处理多个 Channel
* AIO：异步非阻塞(asynchronous I/O)

## 二. 线程
### 5. 线程的创建
1. 继承Thread类，覆盖run()方法；创建线程对象并用start()方法启动线程
2. 实现Runnable接口，重写run方法；创建一个runnable对象传入到一个Thread中去，然后start启动。
3. 实现Callable接口，重写call方法；其有返回值：Future

## 三. 多线程
### 6. sleep和wait区别
Object类中wait,notify,notifyAll：
  * 只能在同步中使用
  * 释放锁
  * wait必须用notify,notifyAll唤醒

thread类中sleep
  * 可以自任何地方调用
  * 没有释放锁
  * 必须捕获异常
  * 可以自动唤醒

### 7. synchronized，volatile区别
不要将volatile用在getAndOperate场合，即类似于i++，这种包括：读-修改-写入的操作中。
volatile：
  * 本质告诉jvm当前变量在寄存器中的值是不确定的，需要从主内存读取；
  * 仅能在成员变量层面被使用
  * 仅实现变量修改的可见性，不具备原子性
  * 不会被编译器优化

synchronized：
  * 本质锁定当前变量，其他线程被阻塞
  * 可以使用变量和方法上面
  * 保证变量的可见性和原子性
  * 会被编译器优化

### 8. join和Thread.yield
* 一个线程调用join方法，等该线程完成后，再执行该线程下面的代码

```Java
Thread t1 = new Thread(计数线程一);  
Thread t2 = new Thread(计数线程二);  
t1.start();  
t1.join(); // 等待计数线程一执行完成，再执行计数线程二
t2.start();  
```
* Thread.yield：线程放弃运行，将CPU的控制权让出，以使得比该线程优先级相同或更高的线程有机会运行。该线程还可能被重新选中，故yield可能不起作用。

### 9. 线程同步的方法
1. synchronized修饰方法
2. 同步代码块（synchronized）
3. volatile关键字
4. ReentrantLock  
5. 使用局部变量（保证线程之间不相互影响）

### 10.Java线程的状态
* new：新创建一个线程
* runnable：创建后，等待调用start方法，等待获取cpu使用权
* running：获得cpu，执行代码
* blocked：因为某种原因放弃cpu使用权（sleep，join，wait，synchronized）
* dead：执行完退出，或者因为异常退出

## 四、concurrent包（线程池等）
### 11. 线程池
用一个线程池维护一些链接，需要使用的时候就进去取，不需要了就放回去。
优缺点：
  1. 避免线程创建和销毁带来的性能开销
  2. 减少任务等待时间，提高响应时间
  3. 避免多线程相互竞争抢占资源

通过Executors可以创建4中类型的线程池：
1. newCachedThreadPool：可缓存的线程池（可扩展，可缩小）
2. newSingleThreadExecutor： 创建单线程池
3. newFixedThreadPool：固定大小
4. newScheduledThreadPool：可以延迟或定时执行

注：顶级接口Executor，实现接口ExecutorService，默认实现ThreadPoolExecutor

线程关闭：
* shutdown() 不会立刻终止线程池，等所有任务完成再停止
* shutdownNow() 立即终止线程池，打断当前任务，清空缓存池，返回尚未执行任务

### 12. ThreadLocal变量
1. 创建一个只能被同一个线程读写的变量，即使多个线程同时执行该代码，也无法访问到对方的ThreadLocal变量
2. ThreadLocal提供了get和set方法，**使得每个使用该变量的线程都保存着一个单独的副本**，因此get总返回当前执行线程在调用set时设置的最新值，避免了线程之间的竞争关系

### 13. 原子类（AtomicInteger等）和Lock类
* 原子类线程安全，包含：incrementAndGet，getAndIncrement，getAndSet等方法
* Lock包含：ReentrantLock；ReentrantReadWriteLock.ReadLock；ReentrantReadWriteLock.WriteLock；
* Lock提供一种无条件，可轮询，定时以及可中断的锁获取操作，**所有加锁和解锁都是显式的**

### 14. 容器类（BlockingQueue和ConcurrentHashMap）
* BlockingQueue增加阻塞接口put和take，带超时的阻塞接口offer和poll
  * put会在队满的时候阻塞，直到有空间时被唤醒
  * take在队列空的时候阻塞，直到有东西拿的时候才被唤醒
