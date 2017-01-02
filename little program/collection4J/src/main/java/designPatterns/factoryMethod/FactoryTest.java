package designPatterns.factoryMethod;

/**
 * Created by Wan on 2016/8/13 0013.
 */
public class FactoryTest {
    public static void main(String[] args) {
        SendFactory factory = new SendFactory();
        Sender sender = factory.produce("sms");
        sender.Sender();
    }
}
