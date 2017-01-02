package designPatterns.command;

/**
 * Created by pengcheng.wan on 2016/8/16.
 */
public class Invoker {
    private Command command;

    public Invoker(Command command) {
        this.command = command;
    }
    public void action(){
        command.exe();
    }
}
