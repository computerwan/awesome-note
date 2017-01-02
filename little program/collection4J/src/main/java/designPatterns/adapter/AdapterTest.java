package designPatterns.adapter;

/**
 * Created by pengcheng.wan on 2016/8/15.
 */
public class AdapterTest {
    public static void main(String[] args) {
        Targetable target = new Adapter();
        target.method1();
        target.method2();
    }
}
