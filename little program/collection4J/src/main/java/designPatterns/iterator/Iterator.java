package designPatterns.iterator;

/**
 * Created by pengcheng.wan on 2016/8/16.
 */
public interface Iterator {
    //前移
    public Object previous();
    //后移
    public Object next();
    public boolean hasNext();
    //取得第一个元素
    public Object first();
}
