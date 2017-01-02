package designPatterns.proxy;

/**
 * Created by pengcheng.wan on 2016/8/15.
 */
public class Proxy implements Sourceable {
    private Source source;
    public Proxy() {
        super();
        source = new Source();
    }
    public void method() {
        before();
        source.method();
        after();
    }

    private void before() {
        System.out.println("before designPatterns.proxy!");
    }
    private void after(){
        System.out.println("after designPatterns.proxy!");
    }

}
