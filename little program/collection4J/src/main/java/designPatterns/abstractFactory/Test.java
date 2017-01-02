package designPatterns.abstractFactory;

/**
 * Created by Wan on 2016/8/13 0013.
 */
public class Test {
    public static void main(String[] args) {
        Provider provider = new SendMailFactory();
        Sender sender = provider.produce();
        sender.send();
    }
}
