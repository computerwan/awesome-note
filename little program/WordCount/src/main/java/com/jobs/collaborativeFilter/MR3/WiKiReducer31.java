package com.jobs.collaborativeFilter.MR3;

/**
 * Created by pengcheng.wan on 2017/2/15.
 */
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.mahout.cf.taste.hadoop.item.VectorOrPrefWritable;

public class WiKiReducer31 extends Reducer<IntWritable ,VectorOrPrefWritable,IntWritable,VectorOrPrefWritable> {
    public void reduce(IntWritable key,Iterable<VectorOrPrefWritable> values ,Context context ) throws IOException, InterruptedException{

        for(VectorOrPrefWritable va:values){
            context.write(key, va);
        }
    }

}