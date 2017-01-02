package designPatterns.decorator;

/**
 * Created by pengcheng.wan on 2016/8/15.
 */
public class DecoratorTest {
    public static void main(String[] args) {
        Sourceable source = new Source();
        Sourceable obj = new Decorator(source);
        obj.method();
    }
}
