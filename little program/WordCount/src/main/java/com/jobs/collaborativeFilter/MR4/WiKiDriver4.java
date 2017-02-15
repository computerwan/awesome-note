package com.jobs.collaborativeFilter.MR4;

/**
 * Mapper不做操作，Reduce将MR(31)和MR(32)的相同的itemID整合一下
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
import org.apache.mahout.cf.taste.hadoop.item.VectorAndPrefsWritable;
import org.apache.mahout.cf.taste.hadoop.item.VectorOrPrefWritable;

import static com.jobs.collaborativeFilter.WiKiUtils.PATH;

public class WiKiDriver4 {

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
        if (otherArgs.length != 3) {
            System.err.println("Usage: WiKiDriver4 <in1><in2> <out>");
            System.exit(2);
        }
        Job job1 = new Job(conf1, "wiki  job four");
        job1.setNumReduceTasks(1);
        job1.setJarByClass(WiKiDriver4.class);
        job1.setInputFormatClass(SequenceFileInputFormat.class);
        job1.setMapperClass(WiKiMapper4.class);
        job1.setMapOutputKeyClass(IntWritable.class);
        job1.setMapOutputValueClass(VectorOrPrefWritable.class);
        job1.setReducerClass(WiKiReducer4.class);
        job1.setOutputKeyClass(IntWritable.class);
        job1.setOutputValueClass(VectorAndPrefsWritable.class);
        job1.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileInputFormat.addInputPath(job1, new Path(PATH+otherArgs[0]));
        SequenceFileInputFormat.addInputPath(job1, new Path(PATH+otherArgs[1]));
        SequenceFileOutputFormat.setOutputPath(job1, new Path(PATH+otherArgs[2]));
        if(!job1.waitForCompletion(true)){
            System.exit(1); // run error then exit
        }
    }
}
