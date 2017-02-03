# -*- coding: utf-8 -*-

"""
lianjia对象
标题，户型，面积，区，小区名，房层，朝向, 建成时间, 距离地铁站等, 总价, 单价
@author:wan
@file:Lianjia.py
@time:2017/1/26
"""
from sqlalchemy import Column, String, BigInteger, Float
from sqlalchemy.ext.declarative import declarative_base

Base = declarative_base()


class Lianjia(Base):
    # 表的名字
    __tablename__ = 'lianjia'

    # 表的结构
    id = Column(BigInteger(), primary_key=True)
    title = Column(String(20))
    flat_type = Column(String(20))
    area = Column(Float)
    district = Column(String(20))
    community = Column(String(20))
    floor = Column(String(20))
    orientation = Column(String(20))
    build_time = Column(String(20))
    supplement = Column(String(20))
    total_price = Column(Float)
    price = Column(Float)

    def __init__(self, title, flat_type, area, district, community, floor, orientation, build_time, supplement,
                 total_price, price):
        self.title = title
        self.flat_type = flat_type
        self.area = area
        self.district = district
        self.community = community
        self.floor = floor
        self.orientation = orientation
        self.build_time = build_time
        self.supplement = supplement
        self.total_price = total_price
        self.price = price
