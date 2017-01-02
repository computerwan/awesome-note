package designPatterns.iterator;

/**
 * Created by pengcheng.wan on 2016/8/16.
 */
public class IteratorTest {
    public static void main(String[] args) {
        MyCollection collection = new MyCollection();
        Iterator it = collection.iterator();
        while(it.hasNext()){
            System.out.println(it.next());
        }
    }
}
