package designPatterns.facade;

/**
 * Created by pengcheng.wan on 2016/8/15.
 */
public class User {
    public static void main(String[] args) {
        Computer computer = new Computer();
        computer.startup();
        computer.shutdown();
    }
}
