# -*- coding: utf-8 -*-

from sqlalchemy import Column, BigInteger, Integer, engine
from sqlalchemy.ext.declarative import declarative_base

Base = declarative_base()


def init_db():
    Base.metadata.create_all(engine)


def drop_db():
    Base.metadata.drop_all(engine)


class LogIndex(Base):
    # 表的名字
    __tablename__ = 'log_index'

    # 表的结构
    ip = Column(BigInteger(), primary_key=True)
    frequency_times = Column(Integer())
    is_half = Column(Integer())
    is_spider = Column(Integer())
    num_night = Column(BigInteger())
    num_today = Column(BigInteger())

    def __init__(self, ip, frequency_times, is_half, is_spider, num_night, num_today):
        self.ip = ip
        self.frequency_times = frequency_times;
        self.is_half = is_half;
        self.is_spider = is_spider;
        self.num_night = num_night;
        self.num_today = num_today;
