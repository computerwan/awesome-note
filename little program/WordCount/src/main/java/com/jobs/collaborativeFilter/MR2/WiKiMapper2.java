package com.jobs.collaborativeFilter.MR2;

/**
 * Created by pengcheng.wan on 2017/2/15.
 */

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.mahout.math.VarLongWritable;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;


public class WiKiMapper2 extends Mapper<VarLongWritable, VectorWritable, IntWritable, IntWritable> {

    public void map(VarLongWritable userID, VectorWritable userVector, Context context) throws IOException, InterruptedException {
        Iterator<Vector.Element> it = userVector.get().nonZeroes().iterator();
        while (it.hasNext()) {
            int index1 = it.next().index();
            //      System.out.println("index1:"+index1);
            Iterator<Vector.Element> it2 = userVector.get().nonZeroes().iterator();
            while (it2.hasNext()) {
                int index2 = it2.next().index();

                //  test
                /*if(index1==101){
                    System.out.println("index1:"+index1+",index2:"+index2);
                }*/
                context.write(new IntWritable(index1), new IntWritable(index2));
            }
        }
    }
}
