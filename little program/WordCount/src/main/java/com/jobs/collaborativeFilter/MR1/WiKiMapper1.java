package com.jobs.collaborativeFilter.MR1;

/**
 * Created by pengcheng.wan on 2017/2/15.
 */

import java.io.IOException;

import com.jobs.collaborativeFilter.LongAndFloat;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.mahout.math.VarLongWritable;

public class WiKiMapper1 extends Mapper<LongWritable, Text, VarLongWritable, LongAndFloat> {

    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        VarLongWritable userID = new VarLongWritable();
        LongWritable itemID = new LongWritable();
        FloatWritable itemValue = new FloatWritable();
        String line = value.toString();
        String[] info = line.split(",");
        if (info.length != 3) {
            return;
        }
        userID.set(Long.parseLong(info[0]));
        itemID.set(Long.parseLong(info[1]));
        itemValue.set(Float.parseFloat(info[2]));
        context.write(userID, new LongAndFloat(itemID, itemValue));

    }
}
