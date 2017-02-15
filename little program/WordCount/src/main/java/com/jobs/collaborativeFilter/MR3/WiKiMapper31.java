package com.jobs.collaborativeFilter.MR3;

/**
 * Created by pengcheng.wan on 2017/2/15.
 */
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.mahout.cf.taste.hadoop.item.VectorOrPrefWritable;
import org.apache.mahout.math.VectorWritable;

public class WiKiMapper31 extends Mapper<IntWritable ,VectorWritable,IntWritable,VectorOrPrefWritable>{

    public void map(IntWritable key,VectorWritable value,Context context) throws IOException, InterruptedException{

        context.write(key, new VectorOrPrefWritable(value.get()));
        //      System.out.println("key"+key.toString()+",vlaue"+value.get());
    }
}
