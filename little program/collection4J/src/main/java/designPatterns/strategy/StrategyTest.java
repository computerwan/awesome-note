package designPatterns.strategy;

/**
 * Created by pengcheng.wan on 2016/8/16.
 */
public class StrategyTest {
    public static void main(String[] args) {
        String exp ="2+8";
        ICalculator  cal= new Plus();
        int result =cal.calculate(exp);
        System.out.println(result);
    }
}
