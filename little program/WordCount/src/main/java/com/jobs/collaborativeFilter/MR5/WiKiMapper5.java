package com.jobs.collaborativeFilter.MR5;

/**
 * Created by pengcheng.wan on 2017/2/15.
 */

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.mahout.cf.taste.hadoop.item.VectorAndPrefsWritable;
import org.apache.mahout.math.VarLongWritable;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

public class WiKiMapper5 extends Mapper<IntWritable, VectorAndPrefsWritable, VarLongWritable, VectorWritable> {

    public void map(IntWritable key, VectorAndPrefsWritable vectorAndPref, Context context) throws IOException, InterruptedException {
        Vector coo = vectorAndPref.getVector();
        List<Long> userIds = vectorAndPref.getUserIDs();
        List<Float> prefValues = vectorAndPref.getValues();
        //System.out.println("alluserids:"+userIds);
        for (int i = 0; i < userIds.size(); i++) {
            long userID = userIds.get(i);
            float prefValue = prefValues.get(i);
            Vector par = coo.times(prefValue);
            context.write(new VarLongWritable(userID), new VectorWritable(par));
            //System.out.println(",userid:"+userID+",vector:"+par);  //  if the user id = 3 is the same as my paper then is right
        }
        //  System.out.println();
    }
}