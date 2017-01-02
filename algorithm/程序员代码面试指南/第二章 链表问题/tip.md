链表：无序，不知道长度。
如果要进行反序的操作，必须先遍历一遍。

```java
//单链表实现
public class Node {
    public int value;
    public Node next;

    public Node(int data) {
        this.value = data;
    }
}
//多链表实现
public class DoubleNode {
    public int value;
    public DoubleNode last;
    public DoubleNode next;

    public DoubleNode(int value) {
        this.value = value;
    }
}
```