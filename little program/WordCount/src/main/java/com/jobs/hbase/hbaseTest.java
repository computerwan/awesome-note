package com.jobs.hbase;


import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by pengcheng.wan on 2017/2/23.
 */
public class hbaseTest {
    private static final Logger log = LoggerFactory.getLogger(hbaseTest.class);
    private static int i = 0;

    public static void main(String[] args) {
        try {
            HConnection conn = HbaseUtils.getHConnection();

            //插入数据
            HTableInterface table = conn.getTable("ubas:resume_test");
            for (int j = 0; j < 100000; j++) {
                Put put = new Put(Bytes.toBytes("rows1" + j)); //rowKey
                put.add(Bytes.toBytes("basic_info")//指定列族
                        , Bytes.toBytes(String.valueOf("name")),//指定列名
                        Bytes.toBytes("values_" + j + ""));//指定列值
                put.add(Bytes.toBytes("behavior_info"),
                        Bytes.toBytes(String.valueOf("download")),
                        Bytes.toBytes("values_24" + j + ""));
                table.put(put);
                i++;
                log.info("insert {}",i);
            }

            table.close();
            log.info("add info success {}", i);


        } catch (IOException e) {
            log.error("unable to get info");
        }

    }


}
