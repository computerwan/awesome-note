package designPatterns.bridge;

/**
 * Created by pengcheng.wan on 2016/8/15.
 */
public class BridgeTest {
    public static void main(String[] args) {
        MyBridge bridge = new MyBridge();
        //调用第一个对象
        Sourceable source1 = new SourceSub1();
        bridge.setSource(source1);
        bridge.method();
        //调用第二个对象
        Sourceable source2 = new SourceSub2();
        bridge.setSource(source2);
        bridge.method();
    }
}
