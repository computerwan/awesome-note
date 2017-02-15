package com.jobs.collaborativeFilter;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.WritableComparable;

/**
 * Created by pengcheng.wan on 2017/2/15.
 */
public class LongAndFloat implements WritableComparable<LongAndFloat> {
    private LongWritable first;
    private FloatWritable second;

    public LongAndFloat() {
        set(new LongWritable(), new FloatWritable());
    }

    public LongAndFloat(LongWritable l, FloatWritable f) {
        set(l, f);
    }

    public void set(LongWritable longWritable, FloatWritable intWritable) {
        // TODO Auto-generated method stub
        this.first = longWritable;
        this.second = intWritable;
    }

    public LongWritable getFirst() {
        return first;
    }

    public FloatWritable getSecond() {
        return second;
    }

    public void readFields(DataInput arg0) throws IOException {
        // TODO Auto-generated method stub
        first.readFields(arg0);
        second.readFields(arg0);
    }

    public void write(DataOutput arg0) throws IOException {
        // TODO Auto-generated method stub
        first.write(arg0);
        second.write(arg0);
    }

    public int compareTo(LongAndFloat o) {
        // TODO Auto-generated method stub
        int cmp = first.compareTo(o.first);
        if (cmp != 0) {
            return cmp;
        }
        return second.compareTo(o.second);
    }
}
