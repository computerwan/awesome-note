package com.jobs.collaborativeFilter.MR2;

/**
 * Created by pengcheng.wan on 2017/2/15.
 */
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

public class WiKiReducer2 extends Reducer<IntWritable,IntWritable,IntWritable,VectorWritable> {

    public void reduce(IntWritable itemIndex1,Iterable<IntWritable> itemPrefs,Context context) throws IOException, InterruptedException{
        // RandomAccessSparseVector(int cardinality, int initialCapacity)
        Vector itemVector=new RandomAccessSparseVector(Integer.MAX_VALUE,10);
        for(IntWritable itemPref:itemPrefs){
            int itemIndex2=itemPref.get();
            itemVector.set(itemIndex2, itemVector.get(itemIndex2)+1.0);
        }
        context.write(itemIndex1, new VectorWritable(itemVector));
        //      System.out.println(itemIndex1+","+itemVector);
    }
}
