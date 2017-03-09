# -*- coding: utf-8 -*-

"""
you should first install driver using conda：conda install mysql-connector-python
also see MySQLdb：http://blog.csdn.net/vincent_czz/article/details/7697039/ and http://www.runoob.com/python/python-mysql.html
see: http://www.jb51.net/article/49789.htm
@author:wan
@file:orm_utils.py
@time:2017/1/26
"""
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
import matplotlib.pyplot as plt

# 初始化数据库连接
from statistics.LogIndex import LogIndex

list = []


def init():
    engine = create_engine('mysql+mysqlconnector://hadoop:111111@10.100.2.94:3306/resume', echo=False)
    DBSession = sessionmaker(bind=engine)
    return DBSession


# 增数据
def insert(object):
    DBSession = init()
    session = DBSession()
    session.add(object)
    session.commit()
    session.close()


# 查数据
def select():
    DBSession = init()
    session = DBSession()
    # print session.execute('select * from log_index;').first()
    # print session.query(LogIndex).order_by(LogIndex.ip.desc())
    query = session.query(LogIndex)
    for i in query:
        print i.num_today
        list.append(i.num_today)

select()
plt.hist(list, 500)
plt.show()
