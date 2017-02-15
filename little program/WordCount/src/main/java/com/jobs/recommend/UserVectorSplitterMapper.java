package com.jobs.recommend;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.lucene.util.PriorityQueue;
import org.apache.mahout.cf.taste.hadoop.item.IDReader;
import org.apache.mahout.cf.taste.hadoop.item.VectorOrPrefWritable;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.math.VarIntWritable;
import org.apache.mahout.math.VarLongWritable;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by pengcheng.wan on 2017/2/15.
 */
public final class UserVectorSplitterMapper extends
        Mapper<VarLongWritable,VectorWritable, VarIntWritable,VectorOrPrefWritable> {

    private static final Logger log = LoggerFactory.getLogger(org.apache.mahout.cf.taste.hadoop.item.UserVectorSplitterMapper.class);

    static final String USERS_FILE = "usersFile";
    static final String MAX_PREFS_PER_USER_CONSIDERED = "maxPrefsPerUserConsidered";
    static final int DEFAULT_MAX_PREFS_PER_USER_CONSIDERED = 10;

    private int maxPrefsPerUserConsidered;
    private FastIDSet usersToRecommendFor;

    private final VarIntWritable itemIndexWritable = new VarIntWritable();
    private final VectorOrPrefWritable vectorOrPref = new VectorOrPrefWritable();

    @Override
    protected void setup(Context context) throws IOException {
        Configuration jobConf = context.getConfiguration();
        maxPrefsPerUserConsidered = jobConf.getInt(MAX_PREFS_PER_USER_CONSIDERED, DEFAULT_MAX_PREFS_PER_USER_CONSIDERED);

        IDReader idReader = new IDReader (jobConf);
        idReader.readIDs();
        usersToRecommendFor = idReader.getUserIds();
    }

    @Override
    protected void map(VarLongWritable key,
                       VectorWritable value,
                       Context context) throws IOException, InterruptedException {
        long userID = key.get();

        log.info("UserID = {}", userID);

        if (usersToRecommendFor != null && !usersToRecommendFor.contains(userID)) {
            return;
        }
        Vector userVector = maybePruneUserVector(value.get());

        for (Vector.Element e : userVector.nonZeroes()) {
            itemIndexWritable.set(e.index());
            vectorOrPref.set(userID, (float) e.get());
            context.write(itemIndexWritable, vectorOrPref);
        }
    }

    private Vector maybePruneUserVector(Vector userVector) {
        if (userVector.getNumNondefaultElements() <= maxPrefsPerUserConsidered) {
            return userVector;
        }

        float smallestLargeValue = findSmallestLargeValue(userVector);

        // "Blank out" small-sized prefs to reduce the amount of partial products
        // generated later. They're not zeroed, but NaN-ed, so they come through
        // and can be used to exclude these items from prefs.
        for (Vector.Element e : userVector.nonZeroes()) {
            float absValue = Math.abs((float) e.get());
            if (absValue < smallestLargeValue) {
                e.set(Float.NaN);
            }
        }

        return userVector;
    }

    private float findSmallestLargeValue(Vector userVector) {

        PriorityQueue<Float> topPrefValues = new PriorityQueue<Float>(maxPrefsPerUserConsidered) {
            @Override
            protected boolean lessThan(Float f1, Float f2) {
                return f1 < f2;
            }
        };

        for (Vector.Element e : userVector.nonZeroes()) {
            float absValue = Math.abs((float) e.get());
            topPrefValues.insertWithOverflow(absValue);
        }
        return topPrefValues.top();
    }

}

