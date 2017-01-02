package designPatterns.proxy;

/**
 * Created by pengcheng.wan on 2016/8/15.
 */
public class ProxyTest {
    public static void main(String[] args) {
        Sourceable source = new Proxy();
        source.method();
    }
}
