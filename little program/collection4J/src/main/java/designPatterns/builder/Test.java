package designPatterns.builder;

/**
 * Created by Wan on 2016/8/14 0014.
 */
public class Test {
    public static void main(String[] args) {
        Builder builder = new Builder();
        builder.produceMailSender(5);
    }
}
