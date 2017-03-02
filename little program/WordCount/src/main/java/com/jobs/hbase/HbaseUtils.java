package com.jobs.hbase;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;

import java.io.IOException;


/**
 * see in http://www.tuicool.com/articles/fIV3Qj
 * Created by pengcheng.wan on 2017/2/23.
 */
public class HbaseUtils {
    private static final String QUORUM = "10.100.2.94,10.100.2.93,10.100.2.92";
    private static final String CLIENTPORT = "2181";
    private static Configuration conf = null;
    private static HConnection conn = null;

    /**
     * 获取全局唯一的Configuration实例
     */
    public static synchronized Configuration getConfiguration() {
        if (conf == null) {
            conf = HBaseConfiguration.create();
            conf.set("hbase.zookeeper.quorum", QUORUM);
            conf.set("hbase.zookeeper.property.clientPort", CLIENTPORT);
        }
        return conf;
    }

    /**
     * 获取全局唯一的HConnection实例
     */
    public static synchronized HConnection getHConnection() throws IOException {
        if (conn == null) {
            conn = HConnectionManager.createConnection(getConfiguration());
        }
        return conn;
    }
}
