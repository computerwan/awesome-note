package designPatterns.builder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wan on 2016/8/14 0014.
 */
public class Builder {
    private List<Sender> list = new ArrayList<Sender>();
    public void produceMailSender(int count){
        for (int i = 0; i < count; i++) {
            list.add(new MailSender());
        }
    }
    public void produceSmsSender(int count){
        list.add(new SmsSender());
    }
}
