package com.jobs.collaborativeFilter.MR5;

/**
 * Created by pengcheng.wan on 2017/2/15.
 */
import java.io.IOException;

import org.apache.hadoop.mapreduce.Reducer;
import org.apache.mahout.math.VarLongWritable;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

public class WiKiCombiner5 extends Reducer<VarLongWritable,VectorWritable,VarLongWritable,VectorWritable> {
    public void reduce(VarLongWritable key, Iterable<VectorWritable> values,Context context) throws IOException, InterruptedException{
        Vector partial=null;
        for(VectorWritable v:values){
            partial=partial==null?v.get():partial.plus(v.get());
        }
        context.write(key, new VectorWritable(partial));
        System.out.println("userid:"+key.toString()+",vecotr:"+partial);//   here also should be the same as my paper's result
    }
}
