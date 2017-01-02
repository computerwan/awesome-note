package designPatterns.adapter;

/**
 * Created by pengcheng.wan on 2016/8/15.
 */
public class Adapter extends Source implements Targetable {
    public void method2() {
        System.out.println("this is the targetable method!");
    }
}
