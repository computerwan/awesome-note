# -*- coding: utf-8 -*-

"""
you should first install driver using conda：conda install mysql-connector-python
also see MySQLdb：http://blog.csdn.net/vincent_czz/article/details/7697039/ and http://www.runoob.com/python/python-mysql.html
@author:wan
@file:orm_utils.py
@time:2017/1/26
"""
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker


# 初始化数据库连接
def init():
    engine = create_engine('mysql+mysqlconnector://root:215683@localhost:3306/spider', echo=False)
    DBSession = sessionmaker(bind=engine)
    return DBSession

# 增数据
def insert(object):
    DBSession = init()
    session = DBSession()
    session.add(object)
    session.commit()
    session.close()
