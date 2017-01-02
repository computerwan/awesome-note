package designPatterns.command;

/**
 * Created by pengcheng.wan on 2016/8/16.
 */
public class MyCommand implements Command {
    private Receiver receiver;

    public MyCommand(Receiver receiver) {
        this.receiver = receiver;
    }

    public void exe() {
        receiver.action();
    }
}
