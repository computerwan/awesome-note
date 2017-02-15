package com.jobs.collaborativeFilter.MR5;

/**
 * 针对MR4的输出的每一行中的每一个用户，用这个用户的评分值(value)去乘以项目之间的相似度向量
 *  combiner在中间将相似的UserID做一个累加
 *  reducer是将已经评过分的去掉，然后将评分从小到大排序
 *
 * Created by pengcheng.wan on 2017/2/15.
 */
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.mahout.cf.taste.hadoop.RecommendedItemsWritable;
import org.apache.mahout.math.VarLongWritable;
import org.apache.mahout.math.VectorWritable;

import static com.jobs.collaborativeFilter.WiKiUtils.PATH;

public class WiKiDriver5 {

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
            System.err.println("Usage: WiKiDriver5 <in> <out>");
            System.exit(2);
        }
        Job job1 = new Job(conf1, "wiki  job five");
        job1.setNumReduceTasks(1);
        job1.setJarByClass(WiKiDriver5.class);
        job1.setInputFormatClass(SequenceFileInputFormat.class);
        job1.setMapperClass(WiKiMapper5.class);
        job1.setMapOutputKeyClass(VarLongWritable.class);
        job1.setMapOutputValueClass(VectorWritable.class);

        job1.setCombinerClass(WiKiCombiner5.class);
        job1.setReducerClass(WiKiReducer5.class);
        job1.setOutputKeyClass(VarLongWritable.class);
        job1.setOutputValueClass(RecommendedItemsWritable.class);
        //   job1.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileInputFormat.addInputPath(job1, new Path(PATH+otherArgs[0]));

        FileOutputFormat.setOutputPath(job1, new Path(PATH+otherArgs[1]));
        if(!job1.waitForCompletion(true)){
            System.exit(1); // run error then exit
        }
    }
}