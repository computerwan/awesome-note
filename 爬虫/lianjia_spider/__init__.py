# -*- coding: utf-8 -*-
## 可以在一个类中初始化爬取的参数：http://python.jobbole.com/81351/

# 补充：
# 1. 增加定时功能
# 2. 不存在判断
# 3. 增加代理
# 4. 失败重试


# 头文件和缓存
# cookie = ""
# header = {
#     'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36',
#     'Connection': 'keep-alive',
#     'accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8',
#     'Cookie': cookie}


# 备用方法：
# def urls_pools(url):
#     crawl_pools = [url]
#     sub_content = create_content(url, 1)
#     crawl_pools = crawl_pools + sub_content
#     print crawl_pools
#     return crawl_pools
#
#
# # 初始化抓取网站，url_queue为抓取队列，seen为已抓取的页面
# initial_page = "http://sh.lianjia.com/ershoufang"
# url_queue = Queue.Queue()
# url_queue.put(initial_page)
# seen = set()
# seen.add(initial_page)
#
# while (True):
#     if url_queue.not_empty:
#         current_url = url_queue.get()  # 拿出队列中url
#         # crawl(current_url)  # 执行抓取主程序
#         for next_url in urls_pools(current_url):
#             if seen.__contains__(next_url):
#                 break
#             else:
#                 seen.add(next_url)
#                 url_queue.put(next_url)
#     else:
#         break


