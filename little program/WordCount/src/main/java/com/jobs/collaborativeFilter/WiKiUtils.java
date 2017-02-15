package com.jobs.collaborativeFilter;

/**
 * Created by pengcheng.wan on 2017/2/15.
 */

/**
 * 全局变量
 * Mahout实现协同过滤算法：http://blog.csdn.net/fansy1990/article/details/8065007
 * info文件里面：分别是用户ID，项目ID，打分
 *
 * 推荐系统：http://www.cnblogs.com/wentingtu/archive/2011/12/16/2289926.html
 */
public class WiKiUtils {

    public static final String PATH = "hdfs://fansyonepc:9000/user/fansy/date1012/wikifirst/";

    public static int RECOMMENDATIONSPERUSER = 5;

    public static String JOB1OUTPATH = PATH + "job1/part-r-00000";
}