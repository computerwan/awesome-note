package designPatterns.abstractFactory;

/**
 * Created by Wan on 2016/8/13 0013.
 */
public class SendSmsFactory implements Provider {
    public Sender produce() {
        return new SmsSender();
    }
}
