# SPRING

### 1. DI（依赖注入）和AOP(面向切面编程)
* 容器主动将资源推送给它所管理的组件，组件所要做的仅是选择一种合适的方式接受资源
* 将核心代码和其他代码分离开来，解决原有代码混乱和分散的问题

### 2. 基于配置：ApplicationContext：
* FileSystemXmlApplicationContext：从文件的绝对路径加载配置文件
* ClassPathXmlApplicationContext：从classpath路径下面;默认根目录(WEB_INF/classes)
* XmlWebApplicationContext:专为web工程定制的方法，推荐Web项目中使用
* 使用外部属性配置：<context:property-placeholder location=""/>

### 3. 基于注解自动注入：
* 先配置：<context:component-scan base-package="" >
* @Autowired：根据类型匹配；通过@Qualifier注入名称
* @Resource：根据name匹配

### 4. 依赖注入的方式：
  * 属性注入（property）、构造器注入（constructor-arg）
  * 如果有引用关系，不用value，用ref；可以设内部类
  * 可以设置级联属性

### 4. Bean的作用域
* singleton:每个容器里面只有一个实例（默认，同时也不是线程安全的）
* prototype：一个bean的定义可以有多个实例

下面三个作用域仅在基于web的Spring ApplicationContext：
* request：每次http请求
* session：一个http session中
* global-session：在全局http session中，一个bean定义对应一个实例

### 5. AOP基本概念
* 切面（Aspect）-->抽出来的业务
* 通知（Advice）-->方法
* 目标（Target）-->被通知的对象，业务逻辑
* 代理（Proxy） --> 混合之后
* 连接点（JoinPoint）--> 执行程序的地方
* 切点（pointcut）-->查询条件

### 6. 通知分类
* 前置通知(Before)：目标方法执行前
* 后置通知（After）：目标方法执行后，无论是否发生错误；不能访问返回值
* 返回通知（AfterReturning）：方法正常结束后执行，可以访问返回值
* 异常通知(AfterThrowing)
* 环绕通知(Around)

### 7.AOP方法：
* 切面上面增加@Aspect和@Component
* @Before("execution(public int com.atguigu.spring.aop.ArithmeticCalculator.add(int, int))")
即把形参去掉
* 可以通过joinPoint访问到methodName和args


### 8. 如果要开启事务
* 配置事务管理器：transactionManager
* 启用事务注解： <tx:annotation-driven transaction-manager="transactionManager"/>
* 在对应的方法上面增加@Transactional


# Hibernate
session的特定方法能使对象从一个状态转换到另一个状态
* OID:类似于数据库中的主键，称为对象标识
* sessionFactory和EntityManager

### 1. 持久化对象的四种状态
  * 持久化状态（临时对象save或者persist）
  * 临时状态（new对象后）
  * 游离状态（持久对象close，update变为持久）
  * 删除状态（持久对象delete）

注:
  1. load和get的区别：get立即加载，null是返回null；load延迟加载，返回代理对象，为null抛异常
  2. 只有临时对象没有OID，只有持久化对象在session中；数据库中有持久化对象，可能有游离对象

### 2. 常见注解：
  * @Entity：实体类
  * @Table：表名
  * @Id：主键标识
  * @GeneratedValue：主键生成策略
  * @Column：列名
  * @Transient：忽略该字段的映射

### 3. 常见使用
* JDBCTemplate：（extends JdbcDaoSupport）
  * 增删改：update(sql,new Object[{xx,xx}])
  * 查：queryForList或者queryForInt
  * 注意：PreparedStatement
* Hibernate：（extends HibernateDaoSupport）
  * find(hql,new Object[{xx,xx}])
  * save(object)
  * update(object)
* JPA（extends CrudRepository）
  * save()
  * findAll()
  * findOne()
  * delete()
  * 以find|read|get开头，查询条件可以用And，关联查询用A_B
  * @Query注解
* crud使用分页查询：（extends PagingAndSortingRepository）
    * 使用pagable传入：pageNo，pageSize，sort
    * 再将pagable传入条件中


### 4. hibernate的一级缓存和二级缓存：
* session缓存：只要session实例没有结束，就还在。
  * flush：将缓存的状态更新到数据库中
  * reflush：将数据库中的状态更新到缓存中
  * clear: 清空缓存
* sessionFactory缓存：很少被修改，不重要的

### 5. 关联关系
* 单向1-n：多的一端维护关系
* 双向1-n：先插入1的一端，再插入多的一端；需要在多的一方设置inverse=true；
* 双向1-1：
  * 按照外键映射：外键加unique约束，外键对应另外一个主键；先保存没有外键的一侧
  * 按照主键映射：一端的主键生成器使用foreign策略，表明"对方"的主键来生成自己的主键
* 单向多对多：使用连接表
* 双向多对多：使用连接表，必须把一边的inverse设为true，否则会发生冲突
* 继承映射：
  * subclass：父类子类共用一张表，增加一行，作为辨别者列(discriminator)
  * joined-subclass:子列单独部分存放在一张表
  * union-subclass：每个实体对象使用一个表

# Spring MVC
### 1. spring mvc的运行流程
> 重点：DispatcherServlet；HandlerMapping,HandlerAdapter,ViewResolver,View

DispatcherServlet 处于核心位置，负责协调和组织不同组件以完成请求处理并返回响应的工作
  * 对URL进行解析，通过handlerMapping调用相应的handler
  * 通过handler选择合适的handlerAdapter，然后进行配置进行HttpMessageConverter
  * handler处理完成后，向dispatcherServlet返回一个ModelAndView对象
  * 根据ModelAndView选择一个合适的ViewResolver，结合Model和View渲染视图，返回结果

### 2. 常见注解：
  * @requestMapping：对请求URL，请求方法，请求头等做限制
  * @PathVariable：支持Rest风格
  * @RequestParam：绑定请求参数
  * @ModelAttribute: 方法：在controller之前执行；属性上面绑定类型

### 3. 模型数据处理：
  * ModelAndView：包含模型和视图
    * 作为返回值，通过ModelAndView.addObject("time",new Date())传到session域中
  * Map和Model：只包含模型
    * 在参数列表中传入，然后通过map.put()传到session域中
  * @SessionAttributes
    * 可以在session域(即多个请求之间共享模型数据)，在控制器类上面加
  * @ModelAttribute
    * 被其修饰的**方法**，会在controller方法执行之前执行
    * 绑定模型：在方法前@ModelAttribute("hb"),则可以在jsp中指定items=${hb}
    * 被其修饰的**参数**，会绑定到指定的对象

### 4. 视图解析器：
  * 通过返回ModelAndView对象，然后借助视图解析器(viewResolver)得到最后的视图
  * 需要在配置文件中配置

### 5. RestFul风格：
  * form表单只支持get和post，如果需要支持put和delete，需要增加一个_method的隐藏域，value为put或delete
  * 需要在web.xml配置一个过滤器HidenHttpMethodFilter
  * 处理静态资源(html等)，需要在配置文件中配置<mvc:default-servlet-handler/>传给默认servlet处理

### 6. 完成json操作：
  * 类上面加@restCotroller(默认实现了@responseBody和@controller)或者在返回值前面加@responseBody
  * 需要直接返回Collection或者Object对象

### 7. 拦截器配置
* 实现HandlerInterceptor接口（实现三个方法，preHandle（目标方法调用前），postHandle（目标方法调用后），afterCompletion（渲染视图后））
* 在配置文件中配置<mvc:interceptors><bean class=""></mvc:interceptors>

### 8. 文件上传
文件上传，如果请求类型是Multipart将通过MultipartResolver进行文件上传解析；
  + RequestParam中配置属性MultipartFile
  + form表单配置：enctype="multipart/form-data"
