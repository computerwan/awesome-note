package designPatterns.observer;

/**
 * Created by pengcheng.wan on 2016/8/16.
 */
public class ObserverTest {
    public static void main(String[] args) {
        Subject sub = new MySubject();
        sub.add(new Observer1());
        sub.add(new Observer2());
        sub.operation();
    }
}
