# Python学习笔记-应用

### 3.1 文件读写
input函数和raw_input的区别：input会假设用户输入的是合法的python表达式，raw_input则会把所有输入当成原始数据（raw data），然后放入字符串
```
name = input("What is your name?")
print "Hello. " + name + "!"
>>> What is your name? lili
>>> ...TypeError: cannot concatenate 'str' and 'int' objects
>>> What is your name? "lili"
>>> Hello. lili!
name =raw_input("What is your name?")
>>> What is your name? lili
>>> Hello. lili!
```
### 3.2 网络编程

### 3.3 垃圾回收


http://manjusaka.itscoder.com/2016/11/18/Someone-tell-me-that-you-think-Python-is-simple/
