package designPatterns.interpreter;



/**
 * Created by pengcheng.wan on 2016/8/17.
 */
public class Minus implements Expression {
    public int interpret(Context context) {
        return context.getNum1()-context.getNum2();
    }
}
