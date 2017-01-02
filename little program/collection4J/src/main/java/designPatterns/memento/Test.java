package designPatterns.memento;

/**
 * Created by pengcheng.wan on 2016/8/17.
 */
public class Test {
    public static void main(String[] args) {
        // 创建原始类
        Original origin = new Original("egg");

        // 创建备忘录
        Storage storage = new Storage(origin.createMemento());

        // 修改原始类的状态
        System.out.println("初始化状态为：" + origin.getValue());
        origin.setValue("niu");
        System.out.println("修改后的状态为：" + origin.getValue());

        // 回复原始类的状态
        origin.restoreMemento(storage.getMemento());
        System.out.println("恢复后的状态为：" + origin.getValue());
    }
}
