### Python

https://github.com/lining0806/PythonSpiderNotes

爬虫一般分为：抓取，分析，存储
抓取：requests，httplib2
  * get和post请求
  * 分页爬取
  * 目录/内容页
  * 登录
  * 反爬：代理，爬取时间，模拟浏览器，验证码
  * 效率：多线程，分布式
分析：
  * BeautifulSoup，lxml，正则
  * 去重
存储：数据库

1. Beautiful Soup

导包：from bs4 import BeautifulSoup

```python
soup = BeautifulSoup(data, 'lxml')
```

四大对象种类：
* Tag：
  标签，通过soup.title直接获取标签内容（只返回第一个）
  还包括两个属性：name和attrs，可以通过soup.p['class']获取或者修改内容
* NavigableString：
  soup.p.string 获取标签内部的文字，类型是NavigableString
* BeautifulSoup：
  表示整个文档的全部内容
* Comment
