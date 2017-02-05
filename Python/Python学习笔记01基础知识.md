# Python学习笔记-基础知识
Python是解释性语言，交互式语言，面向对象的语言，本章包含基本安装，数据结构，控制语句及函数

### 1.1 安装运行和依赖管理
Python主要分为2.x和3.x两个版本，两种版本不兼容。[两者的区别][1]
因为原生的Python需要安装好多依赖包，本文采用已经预装常用依赖的[anaconda][2]，使用的IDE为
Jetbrains家的pycharm。

安装完成在命令行输入：python  以检查是否安装成功

三种运行方法：
1. 交互式命令行
2. 运行脚本：python script.py（linux下运行需要在开头加：#! /usr/bin/env python）
3. 集成开发环境（Pycharm）

依赖管理：
1. python直接将依赖（package）放到lib文件下即可
2. 使用python自带的pip
3. 使用anaconda种的conda

### 1.2 与Java的区别
1. Python中不使用括号，而是使用***行缩进***表示语句之间递进关系。
2. Python中单引号，双引号，三引号都可以表示字符串常量，其中三引号可以跨越多行。
3. Python的注释为：#
4. Python每句话结束不需要用分号(;),但如果想在同一行输入多个语句，可以使用
5. 如果文件中有中文，开头一定要加 ```# -*- coding: utf-8 -*-```
6. 变量直接赋值，不需要给定类型,例如：x=3

### 1.3 数据类型
数据类型主要包括：数字，序列（列表，元祖，字符串），字典和set

总结：
* 列表：a =[1, 2, 3]
* 元祖：b =(1, 2, 3)
* set: c ={1, 2, 3}
* map: d={'name':'lili','age':18}
* 布尔值：True和False（第一个字母一定要大写）

###### 1. 数字：
```
  x = 10000L #长整型
  y = 10.0 #浮点数
```

包括六种内建序列(sequence)：列表，元祖，字符串，Unicode字符串，buffer对象，xrange对象  
序列中可以存放不同类型的值，如```student =[1 , 'lili']```

通用的序列操作:
* 索引  
  每个元素被分配到一个序号，即元素的位置，叫做索引，从0开始。也可以从后向前表示，即最后一个元素为-1  
  可以直接对字符串字面值使用索引
  ```
  greeting ='Hello'
  print greeting[-1]
  print greeting[0]
  >>> o
  >>> H
  print raw_input("Year: ")[3]
  >>> Year: 2005
  >>> 5
  ```
* 分片  
  使用分片操作，访问一定范围内的元素,包括前面，不包括后面。冒号前后不一定都有值。同时分片还可以改变步长。
  ```
  greeting ='Hello'
  print greeting[0:-2]
  >>> Hel
  numbers = [1, 2, 3, 4, 5, 6, 7, 8]
  print numbers[2:3]
  print numbers[2:]
  print numbers[2:-1:2]
  >>> [3]
  >>> [3, 4, 5, 6, 7, 8]
  >>> [3, 5, 7]
  ```
* 相加和相乘
  ```
  print [1, 2, 3] + [4, 5, 6]
  print 'Hello ' + 'World!'
  print 'python' * 5
  print [11] * 10
  >>> [1, 2, 3, 4, 5, 6]
  >>> Hello World!
  >>> pythonpythonpythonpythonpython
  >>> [11, 11, 11, 11, 11, 11, 11, 11, 11, 11]
  ```
* 函数:in运算符检测是否在其中，len，min，max
  ```
  print 'w' in 'rw'
  >>> True
  number = [100, 34, 678]
  print len(number)
  print max(number)
  print +min(number)
  >>> 3
  >>> 678
  >>> 34
  ```

###### 2.1. 列表（list）
列表可以直接通过下标或者分片进行赋值或者删除,注意在中间插入的方法```number[1,1]=[2,3,4]```
  ```
  x = [1, 2, 1, 4, 5]
  x[1] = 2
  del x[3]
  print x
  x =list('hello')
  print x
  x[5:]=list('world')
  print x
  x[5:5]=list('insert')
  print x
  >>> [1, 2, 1, 5]
  >>> ['h', 'e', 'l', 'l', 'o']
  >>> ['h', 'e', 'l', 'l', 'o', 'w', 'o', 'r', 'l', 'd']
  >>> ['h', 'e', 'l', 'l', 'o', 'i', 'n', 's', 'e', 'r', 't', 'w', 'o', 'r', 'l', 'd']
  ```
方法函数：
* append在末尾添加新对象
* count统计某个元素在列表中出现的次数
* extend在列表末尾一次性追加另一个序列中的多个值。（类似于连接操作，区别extend是修改序列，连接是返回新序列）
* index在列表中找到某一个值第一次匹配的索引位置
* insert将对象插入到列表中
* pop移出列表中的一个元素（默认最后一个），**并返回该值**(唯一一个返回值的)
* remove移出列表中第一个匹配的值，不返回该值
* reverse将列表中元素反转
* sort对列表进行排序

```
# 注意append和extend区别：
x = [1, 2, 3]
y = [4, 5, 6]
x.append(y)
print x  # x = [1, 2, 3, [4, 5, 6]]
x.extend(y)
print x  # x = [1, 2, 3, 4, 5, 6]
print x.count(1) # 1
print x.index(2) # 1
x.insert(6, 7)
print x # x = [1, 2, 3, 4, 5, 6, 7]
print x.pop() # 7
x.remove(4)
print x # [1, 2, 3, 5, 6]
```
###### 2.2. 元祖（tuple）
列表可修改，**元祖不可修改**。创建元祖只需要直接用逗号分隔一些数,或者使用tuple。
```
y = 1, 2, 3
print y
>>> (1, 2, 3)
y = tuple([1, 2, 3])
print y
>>> (1, 2, 3)
z = tuple('abc')
print z
>>> ('a', 'b', 'c')
```
###### 2.3. 字符串
字符串是不可变的，不能对字符串进行二次赋值。
字符串格式化:可以通过转换说明符：%，标记需要插入转换值的位置。
```
format = "Hello %s %s !"
values = ("my", "World")
print format % values
>>>　Hello my World !
```

方法函数：
* find 在较长字符串中查找子串，返回子串所在位置最左端的索引，没有则返回-1，还可以选择起始点和结束点
* join 分别通过特定的连接符连接数组（与split相反，注意数组中必须都是字符串，否则报错）
* lower 返回小写字母（相关方法：title，upper）
* replace 查找并替换（translate功能类似，但只能处理单个字符，但效率更高）
* strip 去除两侧的空格

```
title = 'Hello my happy world!'
print title.find('my')  # 6
print title.find('my', 7)  # -1
seq = ['1', '2', '3', '4']
sep = '+'
res = sep.join(seq)
print res  # 1+2+3+4 注意是字符串的方法，不是数组的方法
print res.split(sep)  # ['1', '2', '3', '4']
print title.lower() # hello my happy world!
print title.lower().title() # Hello My Happy World!
print title.replace('o','?') # Hell? my happy w?rld!
```

###### 3.1. 字典（map）
类似于java中的map，即为一个映射mapping，一个key对应一个value
创建方法：
1. 通过大括号{}包括一个个的键值对，key和value之间用：相隔，两个键值对之间用，
2. 也可以用dict函数将其他类型进行映射。注意：**传入dict函数时，key值默认是字符串，传入的时候不需要加引号**
3. fromkeys将一个空字典传入key，value默认为None
```
items = {'name': 'lili', 'age': 18, 'length': 170}
print items  # {'length': 170, 'age': 18, 'name': 'lili'}
items2 = [('name', 'lili'), ('age', 18)]
d = dict(items2)
print d  # {'age': 18, 'name': 'lili'}
print d['name']  # lili
d2 = dict(name='lili', age=18)
print d2  # {'age': 18, 'name': 'lili'}
```

与sequence类似还有，len(d),d[k],d[k]=v,del d[k]等方法，需要注意的是：
* k in d(d为字典) 查找的是键，效率相对较高
* v in l（l为列表） 查找的是值
* 对键赋值的时候，该位置**必须先初始化过**，否则报错

方法函数：
* clear 清除字典中所有内容
* copy  返回具有相同内容的字典
* get 类似于d[k],不存在返回None，也可以传入默认值
* setdefault 类似于get，**如果存在则不修改，如果不存在则创建**
* has_key 检查是否含有特定的key
* items和iteritems 以数组形式返回key-value对
* keys和iterkeys  以数组形式返回key
* values和itervalues 以数组形式返回value
* pop 获得对应给定键的值，并出栈
* popitem **弹出不确定的项** 注意不是随机，因为弹出的值是固定的一个
* update 用一个字典更新另一个字典,**更新后是创建一个新的对象**

```
# 原因：赋值操作是引用传递，修改了引用两者就没有联系，如果原地操作append则会有影响。
# clear操作
x1 = {'name': 'lili', 'age': 18, 'length': 170}
y = x1
x1.clear()
print x1  # {}
print y # {}
# 赋值操作
x2 = {'name': 'lili', 'age': 18, 'length': 170}
z = x2
x2 = {}
print x2  # {}
print z  # {'length': 170, 'age': 18, 'name': 'lili'}
# setdefault,get,pop功能
x3 = {'name': 'lili', 'age': 18, 'length': 170}
print x3.get('name') # lili
print x3.get('password') # None
print x3.get('password','default') # default
print x3.setdefault('address','N/A') # N/A
print x3.setdefault('name','admin') # lili
print x3  # {'length': 170, 'age': 18, 'name': 'lili', 'address': 'N/A'}
print x3.pop('age') # 18
print x3.popitem() # ('length', 170) s
print x3 # {'name': 'lili', 'address': 'N/A'}
```

直接赋值，浅拷贝和深拷贝的区别：
* Python对象中的赋值都是**引用传递**（pass by reference）
* 使用copy.copy(),可以对对象进行浅拷贝，只复制对象中的元素，引用不改变
* 如果需要复制对象和其中所有内容，可以使用copy.deepcopy()进行深拷贝
```
import copy
a = {'username': 'admin', 'machine': ['foo', 'bar', 'baz']}
o = {'password': '123456', 'username': 'lili'}
b = a.copy()
c = copy.copy(a)
d = copy.deepcopy(a)
a['machine'].append('apple')
a.update(o)
print a # {'username': 'lili', 'machine': ['foo', 'bar', 'baz', 'apple'], 'password': '123456'}
print b # {'username': 'admin', 'machine': ['foo', 'bar', 'baz', 'apple']}
print c # {'username': 'admin', 'machine': ['foo', 'bar', 'baz', 'apple']}
print d # {'username': 'admin', 'machine': ['foo', 'bar', 'baz']}
print '-----------'
print b.has_key('username') # True
print b.items() # [('username', 'admin'), ('machine', ['foo', 'bar', 'baz', 'apple'])]
print b.keys() # ['username', 'machine']
print b.values() # ['admin', ['foo', 'bar', 'baz', 'apple']]
```

###### 3.2. set
set与list相似，只是其中没有重复的项
```
a = {1, 2, 3, 4, 5}
print a
a.add(3)
print a
```


值被转化为字符串有两种机制：**str类型**（注意：str与int一样是类型，不是函数）和repr函数（创建一个字符串，然后以合法的表达式形式表示值）。
```
print 10000L
>>> 10000
print str(10000L)
>>> 10000
print repr(10000L)
>>> 10000L
```

### 1.4 控制语句

#### 1.4.1 条件语句(if)
以下内容会被解释器看作为假：False None 0 "" () [] {},其他一切均被看作真
```
if var == 200:
   print "1 - Got a true expression value"
   print var
elif var == 150:
   print "2 - Got a true expression value"
   print var
else:
   print "4 - Got a false expression value"
   print var
```
* 除了常见的==,!=,>=,<=之外，还有表示两者是否是同一个对象的：x is y or x is not y，还有表示x是否是y容器中成员的x in y or x not in y
* Python的比较运算时可以连接使用的，如：0<age<100
* ```assert 条件``` 如果不满足条件就会报错

#### 1.4.2. 循环语句(while和for)
其中：while循环或者for循环执行完，可以执行一个else
```
count = 0
while count < 5:
   print count, " is  less than 5"
   count = count + 1
else:
   print count, " is not less than 5"
```
其中：range是含首不含尾，同样range也可以换成一个list
```
for num in range(10,20):   
   for i in range(2,num):   
      if num%i == 0:       
         j=num/i          
         print '%d equals %d * %d' % (num,i,j)
         break            
   else:                   
      print num, 'is a prime number'
```
字典的遍历：
```
d = {'x': 1, 'y': 2, 'z': 3}
for key in d:
    print key, 'corresponse to', d[key]
```
break和continues:
* break 是结束循环
* continues 是结束当前循环，进入下一轮循环

#### 1.4.2. 循环（迭代）
1. 并行迭代
  通过内建函数zip实现并行迭代
  ```
  names = ['anne', 'beth', 'george', 'damon']
  ages = [12, 45, 32, 102]

  for i in range(len(names)):
      print names[i], 'is', ages[i], 'years old'

  for name, age in zip(names, ages):
      print name, 'is', age, 'years old'
  ```
2. 按索引迭代
  在迭代过程中需要获取索引,可以自己设一个索引，也可以使用内建函数enumerate
  ```
  index = 0
  for string in strings:
      if 'xxx' in string:
          string[index] = '[null]'
      index += 1

  for index, string in enumerate(strings):
      if 'xxx' in string:
          string[index] = '[null]'
        ```
3. 列表推导式（list comprehensive）
```
print [x*x for x in range(10)] # [0, 1, 4, 9, 16, 25, 36, 49, 64, 81]
print [x*x for x in range(10) if x % 3 == 0] # [0, 9, 36, 81]
print [(x, y) for x in range(3) for y in range(3)] # [(0, 0), (0, 1), (0, 2), (1, 0), (1, 1), (1, 2), (2, 0), (2, 1), (2, 2)
```

### 1.5 数学函数，时间函数

数学函数：
* abs(x)    返回数字的绝对值，如abs(-10) 返回 10
* ceil(x)    返回数字的上入整数，如math.ceil(4.1) 返回 5
* cmp(x, y) 如果 x < y 返回 -1, 如果 x == y 返回 0, 如果 x > y 返回 1
* exp(x)    返回e的x次幂(ex),如math.exp(1) 返回2.718281828459045
* fabs(x)    返回数字的绝对值，如math.fabs(-10) 返回10.0
* floor(x) 返回数字的下舍整数，如math.floor(4.9)返回 4
* log(x)    如math.log(math.e)返回1.0,math.log(100,10)返回2.0
* log10(x) 返回以10为基数的x的对数，如math.log10(100)返回 2.0
* max(x1, x2,...)    返回给定参数的最大值，参数可以为序列。
* min(x1, x2,...)    返回给定参数的最小值，参数可以为序列。
* modf(x)    返回x的整数部分与小数部分，两部分的数值符号与x相同，整数部分以浮点型表示。
* pow(x, y)     x**y 运算后的值。X的y次方
* round(x [,n]) 返回浮点数x的四舍五入值，如给出n值，则代表舍入到小数点后的位数。
* sqrt(x)    返回数字x的平方根，数字可以为负数，返回类型为复数，如math.sqrt(4)返回 2+0j

导入cmath包，可以对复数进行操作
```
import cmath
cmath.sqrt(-1)
>>> 1j
```

### 1.6 自定义函数

### 1.7 模块控制和包文件

1. 全部导入：使用"import 模块"，然后通过"模块.函数"的方式调用，同时可以使用缩略,即"import 模块 as 缩略名",然后就可以直接调用缩略
```
import math
math.floor(32.9)
>>> 32.0
```
2. 部分导入：使用"from 模块 import 函数"
```
from math import floor
math.floor(32.9)
>>> 32.0
```
### 1.8 其他
1. 可以同时对多个变量赋值,称为序列解包（sequence unpacking）
2. print语句，可以用逗号相隔，打印多个表达式
3. 链式赋值（chained assignment):可以对多个变量连续赋值 x = y = function()
```
x, y, z = 1, 2, 3
x, y = y, x
print x, y, z
>>> 2 1 3
values =1, 2, 3
x, y, z =values
print x, y, z
>>> 1 2 3
# 应用
info = {'name': 'lili', 'age': 18}
key, value = info.popitem()
print key, value
>>> age 18
```
4. 如果程序没有完成，可以使用pass语句，跳过当前内容
5. 可以使用del x删除对象，类似于JVM中的垃圾回收机制
6. 在2.x中如果输入以下命令：（3.x中不存在该问题）
```
print 1/2
>>> 0
```
7. 如果需要执行普通除法：
其中：//表示整除，无论是否添加__future__，或者为浮点数
```
from __future__  import division
print 1/2
>>> 0.5
print 1.0//2.0
>>> 0
```


[1]: https://www.zhihu.com/question/19698598
[2]: https://www.continuum.io/downloads
