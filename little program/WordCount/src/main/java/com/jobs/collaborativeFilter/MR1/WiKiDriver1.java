package com.jobs.collaborativeFilter.MR1;


import com.jobs.collaborativeFilter.LongAndFloat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.mahout.math.VarLongWritable;
import org.apache.mahout.math.VectorWritable;

import java.io.IOException;

import static com.jobs.collaborativeFilter.WiKiUtils.PATH;

/**
 * 目标信息整合，预期结果：
 * userid:1,vector:{103:2.5,102:3.0,101:5.0}
 * userid:2,vector:{104:2.0,103:5.0,102:2.5,101:2.0}
 * userid:3,vector:{107:5.0,105:4.5,104:4.0,101:2.5}
 * userid:4,vector:{106:4.0,104:4.5,103:3.0,101:5.0}
 * userid:5,vector:{106:4.0,105:3.5,104:4.0,103:2.0,102:3.0,101:4.0}
 * <p>
 * Created by pengcheng.wan on 2017/2/15.
 */

public class WiKiDriver1 {
    /**
     * @param args
     * @throws IOException
     * @throws InterruptedException
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        // TODO Auto-generated method stub
        Configuration conf1 = new Configuration();

        String[] otherArgs = new GenericOptionsParser(conf1, args).getRemainingArgs();
        if (otherArgs.length != 2) {
            System.err.println("Usage: WiKiDriver1 <in> <out>");
            System.exit(2);
        }
        Job job1 = new Job(conf1, "wiki  job one");
        job1.setOutputFormatClass(SequenceFileOutputFormat.class);
        job1.setNumReduceTasks(1);
        job1.setJarByClass(WiKiDriver1.class);
        job1.setMapperClass(WiKiMapper1.class);
        job1.setMapOutputKeyClass(VarLongWritable.class);
        job1.setMapOutputValueClass(LongAndFloat.class);
        job1.setReducerClass(WiKiReducer1.class);
        job1.setOutputKeyClass(VarLongWritable.class);
        job1.setOutputValueClass(VectorWritable.class);

        FileInputFormat.addInputPath(job1, new Path("hdfs://fansyonepc:9000/user/fansy/input/" + otherArgs[0]));
        SequenceFileOutputFormat.setOutputPath(job1, new Path(PATH + otherArgs[1]));
        if (!job1.waitForCompletion(true)) {
            System.exit(1); // run error then exit
        }
    }

}
