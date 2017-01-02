package designPatterns.factoryMethod;

/**
 * Created by Wan on 2016/8/13 0013.
 */
public class SendFactory {
    public Sender produce(String type) {
        if ("mail".equals(type)) {
            return new MailSender();
        } else if ("sms".equals(type)) {
            return new SmsSender();
        }else{
            System.out.println("��������ȷ�����ͣ�");
            return null;
        }
    }
}
