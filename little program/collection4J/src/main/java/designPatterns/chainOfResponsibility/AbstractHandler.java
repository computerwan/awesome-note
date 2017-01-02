package designPatterns.chainOfResponsibility;

/**
 * Created by pengcheng.wan on 2016/8/16.
 */
public abstract class AbstractHandler {
    private Handler handler;

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }
}
