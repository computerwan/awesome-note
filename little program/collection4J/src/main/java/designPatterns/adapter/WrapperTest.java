package designPatterns.adapter;

/**
 * Created by pengcheng.wan on 2016/8/15.
 */
public class WrapperTest {
    public static void main(String[] args) {
        Sourceable source1 = new SourceSub1();
        source1.method01();
    }
}
