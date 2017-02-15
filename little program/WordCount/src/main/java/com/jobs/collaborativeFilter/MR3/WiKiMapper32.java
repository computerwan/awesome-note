package com.jobs.collaborativeFilter.MR3;

/**
 * Created by pengcheng.wan on 2017/2/15.
 */

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.mahout.cf.taste.hadoop.item.VectorOrPrefWritable;
import org.apache.mahout.math.VarLongWritable;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

public class WiKiMapper32 extends Mapper<VarLongWritable, VectorWritable, IntWritable, VectorOrPrefWritable> {

    public void map(VarLongWritable key, VectorWritable value, Context context) throws IOException, InterruptedException {

        long userID = key.get();
        Vector userVector = value.get();
        Iterator<Vector.Element> it = userVector.nonZeroes().iterator();
        IntWritable itemi = new IntWritable();
        while (it.hasNext()) {
            Vector.Element e = it.next();
            int itemIndex = e.index();
            float preferenceValue = (float) e.get();
            itemi.set(itemIndex);
            context.write(itemi, new VectorOrPrefWritable(userID, preferenceValue));
            //      System.out.println("item :"+itemi+",userand val:"+userID+","+preferenceValue);
        }
        //  System.out.println();

    }
}
