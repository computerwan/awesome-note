package designPatterns.strategy;

/**
 * Created by pengcheng.wan on 2016/8/16.
 */
public class Multiply extends AbstractCalculator implements ICalculator {
    public int calculate(String exp) {
        int arrayInt[] = split(exp, "\\*");
        return arrayInt[0] * arrayInt[1];
    }
}
