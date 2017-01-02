package designPatterns.abstractFactory;

/**
 * Created by Wan on 2016/8/13 0013.
 */
public class SendMailFactory implements Provider {
    public Sender produce() {
        return new MailSender();
    }
}
