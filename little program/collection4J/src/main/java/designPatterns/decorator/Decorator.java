package designPatterns.decorator;


/**
 * Created by pengcheng.wan on 2016/8/15.
 */
public class Decorator implements Sourceable {
    private Sourceable source;

    public Decorator(Sourceable source) {
        super();
        this.source = source;
    }

    public void method() {
        System.out.println("before designPatterns.decorator!");
        source.method();
        System.out.println("after designPatterns.decorator!");
    }
}
