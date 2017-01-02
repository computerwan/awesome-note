package designPatterns.chainOfResponsibility;

/**
 * Created by pengcheng.wan on 2016/8/16.
 */
public class MyHandler extends AbstractHandler implements Handler {
    private String name;
    public MyHandler(String name){
        this.name =name;
    }
    public void operator() {
        System.out.println(name+"deal!");
        if(getHandler()!=null){
            getHandler().operator();
        }
    }
}
