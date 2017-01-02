package designPatterns.memento;

/**
 * Created by pengcheng.wan on 2016/8/17.
 */
public class Memento {
    private String value;

    public Memento(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
}
