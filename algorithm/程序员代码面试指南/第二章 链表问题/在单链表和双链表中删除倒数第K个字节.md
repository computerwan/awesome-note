###题目：
分别实现两个函数，一个可以删除单链表中倒数第K个字节，另一个可以删除双链表中倒数第K个节点。

####分析：
* 不存在：链表为null，k小于0，k的值大于链表的长度
* 由于链表是无序的，且长度位置，故首先通过遍历（每次遍历，k值减小1）。判断情况。
	* 如果k>0,说明链表小于k的值，不存在
	* 如果k=0,则说明删除第一个数。cur=cur.next()即可。
	* 如果k<0,比如要删出第二个数，先找到第一个数。然后将第三个数指向第一个数的next上面。（cur.next=cur.next.next）

####解答(单链表)：
```java
public class Node {
    public int value;
    public Node next;
    public Node(int data) {
        this.value = data;
    }

    public Node removeLastKthNode(Node head, int lastKth) {
        if (head == null || lastKth < 0) {
            return head;
        }
        Node cur = head;

        while (cur != null) {
            lastKth--;
            cur = cur.next;
        }
        if (lastKth == 0) {
            head = head.next; //删除第一个数
        }
        if (lastKth < 0) {
            cur = head;//指向链表的第一个数
            while (++lastKth != 0) { //因为当++lastKth==0时，实际上是指向-1的数，需要删除的是k=0的数
                cur = cur.next;
            }
            cur.next = cur.next.next;//删除中间的数，就是将A前一个数的next指向A后面的数
        }
        return head;

    }
}
```
####解答(双链表)：
```java
public class DoubleNode {
    public int value;
    public DoubleNode last;
    public DoubleNode next;

    public DoubleNode(int value) {
        this.value = value;
    }

    public DoubleNode removeLastKthNode(DoubleNode head,int lastKth){
        if(head==null||lastKth<0)
            return head;
        DoubleNode cur =head;
        while(cur!=null){
            lastKth--;
            cur=cur.next;
        }
        if(lastKth==0){
            head=head.next;
            head.last=null;
        }
        if(lastKth<0){
           cur=head;//初始化
            while(++lastKth!=0){
                cur=cur.next;
            }
            DoubleNode newNext = cur.next.next;
            cur.next=newNext;
            if(newNext!=null){
                newNext.last=cur;
            }
        }
        return head;
    }
}
```

