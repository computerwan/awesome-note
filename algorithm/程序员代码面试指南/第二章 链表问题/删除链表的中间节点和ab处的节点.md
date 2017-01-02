###题目：
给定链表的头节点head，实现删除链表的中间节点的函数。
例如：
不删除任何节点；
1->2,删除节点1；
1->2->3，删除节点2；
1->2->3->4，删除节点2；
1->2->3->4->5，删除节点3；

####进阶：
给定链表的头节点head、整数a和b，实现删除位于a/b处节点的函数
例如：
链表：1->2->3->4->5，假设a/b的值为r。
如果r等于0，不删除任何节点；
如果r在区间（0，1/5]上，删除节点1；
如果r在区间（1/5，2/5]上，删除节点2；
如果r在区间（2/5，3/5]上，删除节点3；
如果r在区间（3/5，4/5]上，删除节点4；
如果r在区间（4/5，1]上，删除节点5；
如果r大于1，不删除任何节点。

####分析：
首先要单独考虑一个，两个，三个的情况。
从第四个开始，链表的长度每增加2，要删除的节点就要后移一个节点。

####解答：
```java
public Node removeMidNode(Node head){
    if(head==null&&head.next==null){ //当链表为null，或者长度为1时（不删除）
        return head;
    }
    if(head.next.next==null){//当链表长度为2时
        return head.next;//删除第一个
    }
    Node pre =head;//指向要删除数的前面一个数
    Node cur=head.next.next;//初始化的时候cur指向最末位的数，目前是第三个
    while(cur.next!=null&&cur.next.next!=null){
        pre=pre.next;
        cur=cur.next.next;
    }
    pre.next=pre.next.next;//删除中间数的标准写法
    return head;
}

```

####进阶解答：
```java
public Node removeByRatio(Node head,int a,int b){
    if(a<1 ||a>b){
        return head;
    }
    int n =0;
    Node cur=head;
    //计算链表的长度
    while(cur!=null){
        n++;
        cur=cur.next;
    }
    n=(int)Math.ceil((double)(a*n)/(double)b);
    if(n==1){
        return head=head.next;
    }
    if(n>1){
        cur=head;
        while(--n!=1){
            cur=cur.next;
        }
        cur.next=cur.next.next;
    }
    return head;
}
```