package designPatterns.visitor;

/**
 * Created by pengcheng.wan on 2016/8/17.
 */
public class MySubject implements Subject {
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String getSubject() {
        return "love";
    }
}
