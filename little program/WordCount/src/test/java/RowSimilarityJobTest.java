import junit.framework.TestCase;

import java.io.IOException;
import java.util.Map;

//import mahout.fansy.utils.read.ReadArbiKV;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Writable;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.hadoop.similarity.cooccurrence.Vectors;

/**
 * Created by pengcheng.wan on 2017/2/16.
 */
public class RowSimilarityJobTest extends TestCase {
/*    // 测试 weights 输出：
    public void testWeights() throws IOException {
        String path = "hdfs://ubuntu:9000/user/mahout/item/temp/weights/part-r-00000";
        Map<Writable, Writable> map = ReadArbiKV.readFromFile(path);
        System.out.println("weights=================");
        System.out.println(map);
    }

    //normsPath
    public void testNormsPath() throws IOException {
        String path = "hdfs://ubuntu:9000/user/mahout/item/temp/norms.bin";
        Vector map = getVector(path);
        System.out.println("normsPath=================");
        System.out.println(map);
    }

    //maxValues.bin
    public void testMaxValues() throws IOException {
        String path = "hdfs://ubuntu:9000/user/mahout/item/temp/maxValues.bin";
        Vector map = getVector(path);
        System.out.println("maxValues=================");
        System.out.println(map);
    }

    //numNonZeroEntries.bin
    public void testNumNonZeroEntries() throws IOException {
        String path = "hdfs://ubuntu:9000/user/mahout/item/temp/numNonZeroEntries.bin";
        Vector map = getVector(path);
        System.out.println("numNonZeroEntries=================");
        System.out.println(map);
    }

    //pairwiseSimilarityPath
    public void testPairwiseSimilarityPath() throws IOException {
        String path = "hdfs://ubuntu:9000/user/mahout/item/temp/pairwiseSimilarity/part-r-00000";

        Map<Writable, Writable> map = ReadArbiKV.readFromFile(path);
        System.out.println("pairwiseSimilarityPath=================");
        System.out.println(map);
    }

    //similarityMatrix
    public void testSimilarityMatrix() throws IOException {
        String path = "hdfs://ubuntu:9000/user/mahout/item/temp/similarityMatrix/part-r-00000";
        Map<Writable, Writable> map = ReadArbiKV.readFromFile(path);
        System.out.println("similarityMatrix=================");
        System.out.println(map);
    }

    // 读取.bin文件
    public Vector getVector(String path) {
        Configuration conf = new Configuration();
        conf.set("mapred.job.tracker", "ubuntu:9001");
        Vector vector = null;
        try {
            vector = Vectors.read(new Path(path), conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return vector;
    }*/
}
