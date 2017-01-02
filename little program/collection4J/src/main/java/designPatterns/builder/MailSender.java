package designPatterns.builder;

/**
 * Created by Wan on 2016/8/13 0013.
 */
public class MailSender implements Sender {
    public void send() {
        System.out.println("this is mail sender!");
    }
}
