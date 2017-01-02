package designPatterns.bridge;

/**
 * Created by pengcheng.wan on 2016/8/15.
 */
public class MyBridge extends Bridge {
    public void method() {
        getSource().method();
    }
}
