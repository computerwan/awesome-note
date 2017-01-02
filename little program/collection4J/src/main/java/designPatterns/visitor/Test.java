package designPatterns.visitor;

/**
 * Created by pengcheng.wan on 2016/8/17.
 */
public class Test {
    public static void main(String[] args) {
        Visitor visitor = new MyVisitor();
        Subject subject = new MySubject();
        subject.accept(visitor);
    }
}
