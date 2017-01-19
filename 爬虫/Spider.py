# -*- coding: utf-8 -*-

"""
request用于发送HTTP请求，BeautifulSoup用于解析HTML响应
BeautifulSoup参考文档：http://beautifulsoup.readthedocs.io/zh_CN/latest/
@author:wan
@file:Spider.py
@time:2017/1/19
"""
import requests
import Queue
from bs4 import BeautifulSoup


def crawl(url):
    # 头文件和缓存
    # cookie = ""
    # header = {
    #     'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36',
    #     'Connection': 'keep-alive',
    #     'accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8',
    #     'Cookie': cookie}

    # 可以在这插入header,cookie,例如：.get(url, header=header)
    wbdata = requests.get(url).text

    soup = BeautifulSoup(wbdata, 'lxml')
    # print soup.prettify()

    titles = soup.select("div > h2 > a")

    for n in titles:
        title = n.get_text()
        print title


def extract_urls(url):
    url = ["http://sh.lianjia.com/ershoufang/d2", "http://sh.lianjia.com/ershoufang/d3"]
    return url


# 连接池
initial_page = "http://sh.lianjia.com/ershoufang"

# 初始化抓取网站
url_queue = Queue.Queue()
url_queue.put(initial_page)

# 存放已经抓取过的网站，目的去重
seen = set()
seen.add(initial_page)

while (True):
    if url_queue.not_empty:
        current_url = url_queue.get()  # 拿出队列中url
        crawl(current_url)  # 执行抓取主程序
        for next_url in extract_urls(current_url):
            if seen.__contains__(next_url):
                break
            else:
                seen.add(next_url)
                url_queue.put(next_url)
    else:
        break
