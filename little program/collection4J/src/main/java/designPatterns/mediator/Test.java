package designPatterns.mediator;

/**
 * Created by pengcheng.wan on 2016/8/17.
 */
public class Test {
    public static void main(String[] args) {
        Mediator mediator = new MyMediator();
        mediator.createMediator();
        mediator.workAll();
    }
}
