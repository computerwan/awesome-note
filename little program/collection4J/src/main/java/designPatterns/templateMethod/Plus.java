package designPatterns.templateMethod;

/**
 * Created by pengcheng.wan on 2016/8/16.
 */
public class Plus extends AbstractCalculator {
    @Override
    public int calculate(int num1, int num2) {
        return num1+num2;
    }
}
