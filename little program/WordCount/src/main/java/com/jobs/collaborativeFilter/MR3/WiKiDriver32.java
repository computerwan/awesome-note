package com.jobs.collaborativeFilter.MR3;

/**
 * 针对MR(1)的输出，反转用户和项目的位置
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
import org.apache.mahout.cf.taste.hadoop.item.VectorOrPrefWritable;

import static com.jobs.collaborativeFilter.WiKiUtils.PATH;


public class WiKiDriver32 {

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
            System.err.println("Usage: WiKiDriver32 <in> <out>");
            System.exit(2);
        }
        Job job1 = new Job(conf1, "wiki  job one");
        job1.setOutputFormatClass(SequenceFileOutputFormat.class);
        job1.setInputFormatClass(SequenceFileInputFormat.class);
        job1.setNumReduceTasks(1);
        job1.setJarByClass(WiKiDriver32.class);
        job1.setMapperClass(WiKiMapper32.class);
        job1.setMapOutputKeyClass(IntWritable.class);
        job1.setMapOutputValueClass(VectorOrPrefWritable.class);

        job1.setReducerClass(WiKiReducer32.class);
        job1.setOutputKeyClass(IntWritable.class);
        job1.setOutputValueClass(VectorOrPrefWritable.class);

        // the WiKiDriver's out put is this one's input
        SequenceFileInputFormat.addInputPath(job1, new Path(PATH+otherArgs[0]));
        SequenceFileOutputFormat.setOutputPath(job1, new Path(PATH+otherArgs[1]));
        if(!job1.waitForCompletion(true)){
            System.exit(1); // run error then exit
        }
    }

}
