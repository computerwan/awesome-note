package com.jobs.collaborativeFilter.MR2;

/**
 * 不考虑userID,只考虑项目之间的相似度
 * 输出：
 * 101,{107:1.0,106:2.0,105:2.0,104:4.0,103:4.0,102:3.0,101:5.0}
 * 102,{106:1.0,105:1.0,104:2.0,103:3.0,102:3.0,101:3.0}
 *
 * Created by pengcheng.wan on 2017/2/15.
 */

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.mahout.math.VectorWritable;

import static com.jobs.collaborativeFilter.WiKiUtils.PATH;


public class WiKiDriver2 {

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
            System.err.println("Usage: WiKiDriver2 <in> <out>");
            System.exit(2);
        }
        Job job1 = new Job(conf1, "wiki  job two");
        job1.setNumReduceTasks(1);
        job1.setJarByClass(WiKiDriver2.class);
        job1.setInputFormatClass(SequenceFileInputFormat.class);
        job1.setMapperClass(WiKiMapper2.class);
        job1.setMapOutputKeyClass(IntWritable.class);
        job1.setMapOutputValueClass(IntWritable.class);
        job1.setReducerClass(WiKiReducer2.class);
        job1.setOutputKeyClass(IntWritable.class);
        job1.setOutputValueClass(VectorWritable.class);
        job1.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileInputFormat.addInputPath(job1, new Path(PATH + otherArgs[0]));
        SequenceFileOutputFormat.setOutputPath(job1, new Path(PATH + otherArgs[1]));
        if (!job1.waitForCompletion(true)) {
            System.exit(1); // run error then exit
        }
    }

}
