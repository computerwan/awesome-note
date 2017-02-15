package com.jobs.collaborativeFilter.MR1;
/**
 * Created by pengcheng.wan on 2017/2/15.
 */

import java.io.IOException;

import com.jobs.collaborativeFilter.LongAndFloat;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.VarLongWritable;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;


public class WiKiReducer1 extends Reducer<VarLongWritable, LongAndFloat, VarLongWritable, VectorWritable> {

    public void reduce(VarLongWritable userID, Iterable<LongAndFloat> itemPrefs, Context context) throws IOException, InterruptedException {
        // RandomAccessSparseVector(int cardinality, int initialCapacity)
        Vector userVector = new RandomAccessSparseVector(Integer.MAX_VALUE, 10);
        for (LongAndFloat itemPref : itemPrefs) {
            userVector.set(Integer.parseInt(itemPref.getFirst().toString()), Float.parseFloat(itemPref.getSecond().toString()));
        }
        context.write(userID, new VectorWritable(userVector));
        //      System.out.println("userid:"+userID+",vector:"+userVector);
    }
}
