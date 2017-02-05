# -*- coding: utf-8 -*-

"""
request用于发送HTTP请求，BeautifulSoup用于解析HTML响应
@author:wan
@file:Spider.py
@time:2017/1/19
"""
import requests
from bs4 import BeautifulSoup

from Lianjia import Lianjia
from orm_utils import insert


def create_content(url, num):
    sub_content = []
    wbdata = requests.get(url).text
    soup = BeautifulSoup(wbdata, 'lxml')

    if num == 1:
        content = []
        districts = soup.find_all("div", "gio_district")[0].select("a")
        for district in districts:
            if district.get('gahref') != "district-nolimit":
                content.append(district.get('gahref'))
                # print "http://sh.lianjia.com/ershoufang/" + district.get('gahref')
                create_content("http://sh.lianjia.com/ershoufang/" + district.get('gahref'), 2)
        print "一级目录为：" + str(content)
    elif num == 2:
        sub_districts = soup.find_all("div", "sub-option-list")[0].select("a")
        for sub_district in sub_districts:
            if sub_district.get('gahref') != "plate-nolimit":
                sub_content.append(sub_district.get('gahref'))
                create_content("http://sh.lianjia.com/ershoufang/" + sub_district.get('gahref'), 3)
        print "二级目录为：" + str(sub_content)
    elif num == 3:
        if len(soup.find_all(gahref='results_totalpage')) != 0:
            pages = soup.find_all(gahref='results_totalpage')[0].text
            print pages
            print url
            for page in range(0, int(pages)):
                 do_crawl(url + "/d" + str(page))
        else:
            do_crawl(url)


def do_crawl(url):
    # 可以在这插入header,cookie,例如：.get(url, header=header)
    wbdata = requests.get(url).text

    # 抽取过程
    soup = BeautifulSoup(wbdata, 'lxml')
    # 对象可以用.get_text()获取文本，.get('href')获取属性
    title = soup.select("ul[class=house-lst] li h2 a")  # 标题
    community = soup.select("ul[class=house-lst] li div[class=where] a span")  # 小区
    flat_type = soup.select("ul[class=house-lst] li div[class=where] span")  # 户型(span中第3i+1)
    area = soup.select("ul[class=house-lst] li div[class=where] span")  # 面积(span中第3i+2)
    district = soup.select("ul[class=house-lst] li div[class=other] a")  # 地区
    aggrs = soup.select("ul[class=house-lst] li div[class=other]")  # 层数,朝向,建成时间
    # supplement = soup.select("ul[class=house-lst] li span[class=fang-subway-ex]")  # 是否有地铁
    price = soup.select("ul[class=house-lst] li div[class=price-pre]")  # 单价
    total_price = soup.select("ul[class=house-lst] li div[class=price] span[class=num]")  # 总价

    for i in range(0, len(title)):
        orientation = ""
        build_time = ""
        aggr = aggrs[i].text.replace("\t", "").replace("\n", "").split("|")
        floor = aggr[1]  # 层数
        if len(aggr) == 4:
            orientation = aggr[2]
            build_time = aggr[3]
        elif len(aggr) == 3:
            if aggr[2].startswith(" 1") | aggr[2].startswith(" 2"):
                build_time = aggr[2]
            else:
                orientation = aggr[2]

        new_Lianjia = Lianjia(title[i].text, flat_type[3 * i + 1].text, area[3 * i + 2].text[:-3], district[i].text,
                              community[i].text, floor, orientation, build_time, "",
                              total_price[i].text,
                              price[i].text[:-3])
        insert(new_Lianjia)
        print "插入" + str(new_Lianjia) + "成功!"


if __name__ == '__main__':
    print "开始执行链家的下载!"
    create_content("http://sh.lianjia.com/ershoufang", 1)
