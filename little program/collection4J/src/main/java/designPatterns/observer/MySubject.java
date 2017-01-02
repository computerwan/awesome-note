package designPatterns.observer;

/**
 * Created by pengcheng.wan on 2016/8/16.
 */
public class MySubject extends AbstractSubject {
    public void operation() {
        System.out.println("update self!");
        notifyObservers();
    }
}
