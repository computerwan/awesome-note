package com.jobs.collaborativeFilter.MR4;

/**
 * Created by pengcheng.wan on 2017/2/15.
 */
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.mahout.cf.taste.hadoop.item.VectorOrPrefWritable;

public class WiKiMapper4 extends Mapper<IntWritable ,VectorOrPrefWritable,IntWritable,VectorOrPrefWritable> {

    public void map(IntWritable key,VectorOrPrefWritable value,Context context) throws IOException, InterruptedException{
        context.write(key, value);
    }
}
