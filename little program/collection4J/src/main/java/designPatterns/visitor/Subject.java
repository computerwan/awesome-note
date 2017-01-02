package designPatterns.visitor;

/**
 * Created by pengcheng.wan on 2016/8/17.
 */
public interface Subject {
    public void accept(Visitor visitor);
    public String getSubject();
}
