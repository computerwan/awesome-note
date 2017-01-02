package designPatterns.adapter;

/**
 * Created by pengcheng.wan on 2016/8/15.
 */
public class Wrapper implements Targetable {
    private Source source;

    public Wrapper(Source source) {
        super();
        this.source = source;
    }

    public void method1() {
        source.method1();
    }

    public void method2() {
        System.out.println("this is the targetable method!");
    }
}
