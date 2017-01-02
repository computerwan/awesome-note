package dataStructres.binaryTree;

/**
 * Created by pengcheng.wan on 2016/8/17.
 */
public class Node {
    @Override
    public String toString() {
        return "node{" +
                "data=" + data +
                ", Lchild=" + Lchild +
                ", rchild=" + rchild +
                '}';
    }

    private char data;
    private Node Lchild;
    private Node rchild;

    public Node() {
    }

    public Node(char data, Node lchild, Node rchild) {
        this.data = data;
        Lchild = lchild;
        this.rchild = rchild;
    }

    public Node(char data) {
        this.data = data;
    }

    public char getData() {
        return data;
    }

    public void setData(char data) {
        this.data = data;
    }

    public Node getLchild() {
        return Lchild;
    }

    public void setLchild(Node lchild) {
        Lchild = lchild;
    }

    public Node getRchild() {
        return rchild;
    }

    public void setRchild(Node rchild) {
        this.rchild = rchild;
    }

}
