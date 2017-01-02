package designPatterns.visitor;


/**
 * Created by pengcheng.wan on 2016/8/17.
 */
public class MyVisitor implements Visitor {
    public void visit(Subject sub) {
        System.out.println("visit the subject："+sub.getSubject());
    }
}
