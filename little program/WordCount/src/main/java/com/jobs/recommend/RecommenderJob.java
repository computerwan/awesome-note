package com.jobs.recommend;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.cf.taste.hadoop.EntityEntityWritable;
import org.apache.mahout.cf.taste.hadoop.RecommendedItemsWritable;
import org.apache.mahout.cf.taste.hadoop.item.*;
import org.apache.mahout.cf.taste.hadoop.preparation.PreparePreferenceMatrixJob;
import org.apache.mahout.cf.taste.hadoop.similarity.item.ItemSimilarityJob;
import org.apache.mahout.common.AbstractJob;
import org.apache.mahout.common.HadoopUtil;
import org.apache.mahout.common.iterator.sequencefile.PathType;
import org.apache.mahout.math.VarIntWritable;
import org.apache.mahout.math.VarLongWritable;
import org.apache.mahout.math.hadoop.similarity.cooccurrence.RowSimilarityJob;
import org.apache.mahout.math.hadoop.similarity.cooccurrence.measures.VectorSimilarityMeasures;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 源码分析[系列]：http://blog.csdn.net/fansy1990/article/details/12589765
 * Created by pengcheng.wan on 2017/2/15.
 */
public final class RecommenderJob extends AbstractJob {

    public static final String BOOLEAN_DATA = "booleanData";
    public static final String DEFAULT_PREPARE_PATH = "preparePreferenceMatrix";

    private static final int DEFAULT_MAX_SIMILARITIES_PER_ITEM = 100;
    private static final int DEFAULT_MAX_PREFS = 500;
    private static final int DEFAULT_MIN_PREFS_PER_USER = 1;


    /**
     * 增加的其他类里面的常量
     */
    static final String USERS_FILE = "usersFile";
    static final String MAX_PREFS_PER_USER_CONSIDERED = "maxPrefsPerUserConsidered";
    static final int DEFAULT_MAX_PREFS_PER_USER_CONSIDERED = 10;
    static final String USER_ITEM_FILE = "userItemFile";
    static final String ITEMID_INDEX_PATH = "itemIDIndexPath";
    static final String NUM_RECOMMENDATIONS = "numRecommendations";
    static final int DEFAULT_NUM_RECOMMENDATIONS = 10;
    static final String ITEMS_FILE = "itemsFile";


    /**
     * 基本参数初始化
     */
    public int run(String[] args) throws Exception {

        addInputOption();
        addOutputOption();
        addOption("numRecommendations", "n", "Number of recommendations per user",
                String.valueOf(DEFAULT_NUM_RECOMMENDATIONS));
        addOption("usersFile", null, "File of users to recommend for", null);
        addOption("itemsFile", null, "File of items to recommend for", null);
        addOption("filterFile", "f", "File containing comma-separated userID,itemID pairs. Used to exclude the item from "
                + "the recommendations for that user (optional)", null);
        addOption("userItemFile", "uif", "File containing comma-separated userID,itemID pairs (optional). "
                + "Used to include only these items into recommendations. "
                + "Cannot be used together with usersFile or itemsFile", null);
        addOption("booleanData", "b", "Treat input as without pref values", Boolean.FALSE.toString());
        addOption("maxPrefsPerUser", "mxp",
                "Maximum number of preferences considered per user in final recommendation phase",
                String.valueOf(DEFAULT_MAX_PREFS_PER_USER_CONSIDERED));
        addOption("minPrefsPerUser", "mp", "ignore users with less preferences than this in the similarity computation "
                + "(default: " + DEFAULT_MIN_PREFS_PER_USER + ')', String.valueOf(DEFAULT_MIN_PREFS_PER_USER));
        addOption("maxSimilaritiesPerItem", "m", "Maximum number of similarities considered per item ",
                String.valueOf(DEFAULT_MAX_SIMILARITIES_PER_ITEM));
        addOption("maxPrefsInItemSimilarity", "mpiis", "max number of preferences to consider per user or item in the "
                + "item similarity computation phase, users or items with more preferences will be sampled down (default: "
                + DEFAULT_MAX_PREFS + ')', String.valueOf(DEFAULT_MAX_PREFS));
        addOption("similarityClassname", "s", "Name of distributed similarity measures class to instantiate, "
                + "alternatively use one of the predefined similarities (" + VectorSimilarityMeasures.list() + ')', true);
        addOption("threshold", "tr", "discard item pairs with a similarity value below this", false);
        addOption("outputPathForSimilarityMatrix", "opfsm", "write the item similarity matrix to this path (optional)",
                false);
        addOption("randomSeed", null, "use this seed for sampling", false);
        addFlag("sequencefileOutput", null, "write the output into a SequenceFile instead of a text file");

        Map<String, List<String>> parsedArgs = parseArguments(args);
        if (parsedArgs == null) {
            return -1;
        }

        Path outputPath = getOutputPath();
        int numRecommendations = Integer.parseInt(getOption("numRecommendations"));
        String usersFile = getOption("usersFile");
        String itemsFile = getOption("itemsFile");
        String filterFile = getOption("filterFile");
        String userItemFile = getOption("userItemFile");
        boolean booleanData = Boolean.valueOf(getOption("booleanData"));
        int maxPrefsPerUser = Integer.parseInt(getOption("maxPrefsPerUser"));
        int minPrefsPerUser = Integer.parseInt(getOption("minPrefsPerUser"));
        int maxPrefsInItemSimilarity = Integer.parseInt(getOption("maxPrefsInItemSimilarity"));
        int maxSimilaritiesPerItem = Integer.parseInt(getOption("maxSimilaritiesPerItem"));
        String similarityClassname = getOption("similarityClassname");
        double threshold = hasOption("threshold")
                ? Double.parseDouble(getOption("threshold")) : RowSimilarityJob.NO_THRESHOLD;
        long randomSeed = hasOption("randomSeed")
                ? Long.parseLong(getOption("randomSeed")) : RowSimilarityJob.NO_FIXED_RANDOM_SEED;


        Path prepPath = getTempPath(DEFAULT_PREPARE_PATH);
        Path similarityMatrixPath = getTempPath("similarityMatrix");
        Path explicitFilterPath = getTempPath("explicitFilterPath");
        Path partialMultiplyPath = getTempPath("partialMultiply");

        AtomicInteger currentPhase = new AtomicInteger();

        int numberOfUsers = -1;

        /**
         * 调用：shouldRunNextPhase，根据phase和startPhase、endPhase值做比较
         * phase含义是数量不定的几个MR的集合，优点是如果前两个phase成功了，后面的phase失败了，可以直接把成功的跳掉，直接执行失败的
         */
        if (shouldRunNextPhase(parsedArgs, currentPhase)) {
            ToolRunner.run(getConf(), new PreparePreferenceMatrixJob(), new String[]{
                    "--input", getInputPath().toString(),
                    "--output", prepPath.toString(),
                    "--minPrefsPerUser", String.valueOf(minPrefsPerUser),
                    "--booleanData", String.valueOf(booleanData),
                    "--tempDir", getTempPath().toString(),
            });

            numberOfUsers = HadoopUtil.readInt(new Path(prepPath, PreparePreferenceMatrixJob.NUM_USERS), getConf());
        }

        /**
         * 调用：PreparePreferenceMatrixJob，其中包含三个prepareJob
         *  1. convert items to an internal index
         *  2. convert user preferences into a vector per user
         *  3. build the rating matrix
         */
        if (shouldRunNextPhase(parsedArgs, currentPhase)) {

      /* special behavior if phase 1 is skipped */
            if (numberOfUsers == -1) {
                numberOfUsers = (int) HadoopUtil.countRecords(new Path(prepPath, PreparePreferenceMatrixJob.USER_VECTORS),
                        PathType.LIST, null, getConf());
            }

            //calculate the co-occurrence matrix
            /** 调用：RowSimilarityJob，包含3个shouldRunNextPhase，里面有分别都有一个prepareJob函数
             *  作用：calculate the co-occurrence matrix
             *  1. weightsPath
             *  2. pairwiseSimilarityPath
             *  3. asMatrix
             */
            ToolRunner.run(getConf(), new RowSimilarityJob(), new String[]{
                    "--input", new Path(prepPath, PreparePreferenceMatrixJob.RATING_MATRIX).toString(),
                    "--output", similarityMatrixPath.toString(),
                    "--numberOfColumns", String.valueOf(numberOfUsers),
                    "--similarityClassname", similarityClassname,
                    "--maxObservationsPerRow", String.valueOf(maxPrefsInItemSimilarity),
                    "--maxObservationsPerColumn", String.valueOf(maxPrefsInItemSimilarity),
                    "--maxSimilaritiesPerRow", String.valueOf(maxSimilaritiesPerItem),
                    "--excludeSelfSimilarity", String.valueOf(Boolean.TRUE),
                    "--threshold", String.valueOf(threshold),
                    "--randomSeed", String.valueOf(randomSeed),
                    "--tempDir", getTempPath().toString(),
            });

            // write out the similarity matrix if the user specified that behavior
            /**
             * 计算co-occurrence matrix的乘法，下面有两个Job
             * 1. outputSimilarityMatrix
             * 2. partialMultiply
             */
            if (hasOption("outputPathForSimilarityMatrix")) {
                Path outputPathForSimilarityMatrix = new Path(getOption("outputPathForSimilarityMatrix"));

                Job outputSimilarityMatrix = prepareJob(similarityMatrixPath, outputPathForSimilarityMatrix,
                        SequenceFileInputFormat.class, ItemSimilarityJob.MostSimilarItemPairsMapper.class,
                        EntityEntityWritable.class, DoubleWritable.class, ItemSimilarityJob.MostSimilarItemPairsReducer.class,
                        EntityEntityWritable.class, DoubleWritable.class, TextOutputFormat.class);

                Configuration mostSimilarItemsConf = outputSimilarityMatrix.getConfiguration();
                mostSimilarItemsConf.set(ItemSimilarityJob.ITEM_ID_INDEX_PATH_STR,
                        new Path(prepPath, PreparePreferenceMatrixJob.ITEMID_INDEX).toString());
                mostSimilarItemsConf.setInt(ItemSimilarityJob.MAX_SIMILARITIES_PER_ITEM, maxSimilaritiesPerItem);
                outputSimilarityMatrix.waitForCompletion(true);
            }
        }

        //start the multiplication of the co-occurrence matrix by the user vectors
        if (shouldRunNextPhase(parsedArgs, currentPhase)) {
            Job partialMultiply = new Job(getConf(), "partialMultiply");
            Configuration partialMultiplyConf = partialMultiply.getConfiguration();

            MultipleInputs.addInputPath(partialMultiply, similarityMatrixPath, SequenceFileInputFormat.class,
                    SimilarityMatrixRowWrapperMapper.class);
            MultipleInputs.addInputPath(partialMultiply, new Path(prepPath, PreparePreferenceMatrixJob.USER_VECTORS),
                    SequenceFileInputFormat.class, UserVectorSplitterMapper.class);
            partialMultiply.setJarByClass(ToVectorAndPrefReducer.class);
            partialMultiply.setMapOutputKeyClass(VarIntWritable.class);
            partialMultiply.setMapOutputValueClass(VectorOrPrefWritable.class);
            partialMultiply.setReducerClass(ToVectorAndPrefReducer.class);
            partialMultiply.setOutputFormatClass(SequenceFileOutputFormat.class);
            partialMultiply.setOutputKeyClass(VarIntWritable.class);
            partialMultiply.setOutputValueClass(VectorAndPrefsWritable.class);
            partialMultiplyConf.setBoolean("mapred.compress.map.output", true);
            partialMultiplyConf.set("mapred.output.dir", partialMultiplyPath.toString());

            if (usersFile != null) {
                partialMultiplyConf.set(USERS_FILE, usersFile);
            }

            if (userItemFile != null) {
                partialMultiplyConf.set(USER_ITEM_FILE, userItemFile);
            }

            partialMultiplyConf.setInt(MAX_PREFS_PER_USER_CONSIDERED, maxPrefsPerUser);

            boolean succeeded = partialMultiply.waitForCompletion(true);
            if (!succeeded) {
                return -1;
            }
        }

        /**
         * itemFiltering：设置过滤器
         */
        if (shouldRunNextPhase(parsedArgs, currentPhase)) {
            //filter out any users we don't care about
      /* convert the user/item pairs to filter if a filterfile has been specified */
            if (filterFile != null) {
                Job itemFiltering = prepareJob(new Path(filterFile), explicitFilterPath, TextInputFormat.class,
                        ItemFilterMapper.class, VarLongWritable.class, VarLongWritable.class,
                        ItemFilterAsVectorAndPrefsReducer.class, VarIntWritable.class, VectorAndPrefsWritable.class,
                        SequenceFileOutputFormat.class);
                boolean succeeded = itemFiltering.waitForCompletion(true);
                if (!succeeded) {
                    return -1;
                }
            }

            String aggregateAndRecommendInput = partialMultiplyPath.toString();
            if (filterFile != null) {
                aggregateAndRecommendInput += "," + explicitFilterPath;
            }

            Class<? extends OutputFormat> outputFormat = parsedArgs.containsKey("--sequencefileOutput")
                    ? SequenceFileOutputFormat.class : TextOutputFormat.class;

            //extract out the recommendations
            /**
             * aggregateAndRecommend:合并和推荐
             */
            Job aggregateAndRecommend = prepareJob(
                    new Path(aggregateAndRecommendInput), outputPath, SequenceFileInputFormat.class,
                    PartialMultiplyMapper.class, VarLongWritable.class, PrefAndSimilarityColumnWritable.class,
                    AggregateAndRecommendReducer.class, VarLongWritable.class, RecommendedItemsWritable.class,
                    outputFormat);
            Configuration aggregateAndRecommendConf = aggregateAndRecommend.getConfiguration();
            if (itemsFile != null) {
                aggregateAndRecommendConf.set(ITEMS_FILE, itemsFile);
            }

            if (userItemFile != null) {
                aggregateAndRecommendConf.set(USER_ITEM_FILE, userItemFile);
            }

            if (filterFile != null) {
                setS3SafeCombinedInputPath(aggregateAndRecommend, getTempPath(), partialMultiplyPath, explicitFilterPath);
            }
            setIOSort(aggregateAndRecommend);
            aggregateAndRecommendConf.set(ITEMID_INDEX_PATH,
                    new Path(prepPath, PreparePreferenceMatrixJob.ITEMID_INDEX).toString());
            aggregateAndRecommendConf.setInt(NUM_RECOMMENDATIONS, numRecommendations);
            aggregateAndRecommendConf.setBoolean(BOOLEAN_DATA, booleanData);
            boolean succeeded = aggregateAndRecommend.waitForCompletion(true);
            if (!succeeded) {
                return -1;
            }
        }

        return 0;
    }

    private static void setIOSort(JobContext job) {
        Configuration conf = job.getConfiguration();
        conf.setInt("io.sort.factor", 100);
        String javaOpts = conf.get("mapred.map.child.java.opts"); // new arg name
        if (javaOpts == null) {
            javaOpts = conf.get("mapred.child.java.opts"); // old arg name
        }
        int assumedHeapSize = 512;
        if (javaOpts != null) {
            Matcher m = Pattern.compile("-Xmx([0-9]+)([mMgG])").matcher(javaOpts);
            if (m.find()) {
                assumedHeapSize = Integer.parseInt(m.group(1));
                String megabyteOrGigabyte = m.group(2);
                if ("g".equalsIgnoreCase(megabyteOrGigabyte)) {
                    assumedHeapSize *= 1024;
                }
            }
        }
        // Cap this at 1024MB now; see https://issues.apache.org/jira/browse/MAPREDUCE-2308
        conf.setInt("io.sort.mb", Math.min(assumedHeapSize / 2, 1024));
        // For some reason the Merger doesn't report status for a long time; increase
        // timeout when running these jobs
        conf.setInt("mapred.task.timeout", 60 * 60 * 1000);
    }

    public static void main(String[] args) throws Exception {
        ToolRunner.run(new Configuration(), new org.apache.mahout.cf.taste.hadoop.item.RecommenderJob(), args);
    }
}

