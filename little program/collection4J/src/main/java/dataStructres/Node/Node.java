package dataStructres.node;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Wan on 2016/8/22 0022.
 */

/**
 * 反转链表的时候都要把next的数先记录下来
 *
 */
public class Node {
    public int value;
    public Node next;
    public Node rand;//有条题目有，其他都没有

    public Node(int data) {
        this.value = data;
    }

    /**
     * 从尾到头打印链表（剑5）
     * 使用栈（或者递归）
     */
    public void reversePrintList(Node head) {
        Stack<Node> stack = new Stack<Node>();
        List<Node> list = new ArrayList<Node>();
        while (head != null) {
            stack.push(head);
            head = head.next;
        }
        while (!stack.isEmpty()) {
            list.add(stack.pop());
        }
    }


    /**
     * 在O(1)时间内删除链表节点（剑13，p81）
     *
     * @param node
     */
    public void removeNode(Node node) {
        if (node == null) {
            return;
        }
        Node next = node.next;
        if (next == null) {
            throw new RuntimeException("can not remove last node.");
        }
        node.value = next.value;
        node.next = next.next;
    }

    /**
     * 返回倒数第k个数（剑15,p35（删除倒数第k个节点））
     * 思路：用两个指针，A指针先走k-1步，然后一起走，需要考虑边界问题，如果k小于0，或者k大于链表长度。
     * 其实删除节点的方式：就是node=node.next（就把头节点删除了）
     */
    public Node removeLastKthNode(Node head, int lastKth) {
        if (head == null || lastKth < 1) {
            return null;
        }
        Node cur = head;
        while (cur != null) {//类似于第一个指针的功能，同时有大于k的判断效果
            lastKth--;
            cur = cur.next;
        }
        if (lastKth == 0) { //说明倒数第k个数就是头节点
            return head;
        }
        if (lastKth < 0) {//返回倒数第k个值
            cur = head;
            while (++lastKth != 0) {
                cur = cur.next;
            }
            cur = cur.next;
        }
        return cur;
    }

    /**
     * 反转单向链表（剑16；p40）
     * 思想：调整指正之前，需要把后面节点记录下来
     *
     * @param head
     * @return
     */
    public Node reverseList(Node head) {
        Node pre = null;
        Node next = null;
        while (head != null) {
            next = head.next;
            head.next = pre;
            pre = head;
            head = next;
        }
        return pre;
    }

    /**
     * 合并两个有序链表（剑17，p84）
     */
    public Node merge(Node head1, Node head2) {
        if (head1 == null | head2 == null) {
            return head1 != null ? head1 : head2;
        }
        Node head = head1.value < head2.value ? head1 : head2;
        Node cur1 = head == head1 ? head1 : head2;
        Node cur2 = head == head1 ? head2 : head1;
        Node pre = null;//上次指向的最后一个数
        Node next = null;
        while (cur1 != null && cur2 != null) {
            if (cur1.value <= cur2.value) { //注意等于号，否则重复项的得不到保存
                pre = cur1;
                cur1 = cur1.next;
            } else {
                next = cur2.next;//对节点进行修改时候，先保存其下面的节点（类似于反转）
                pre.next = cur2;
                cur2.next = cur1; //上面两步将cur2插入cur1中
                pre = cur2;
                cur2 = next;
            }
        }
        pre.next = cur1 == null ? cur2 : cur1;
        return head;
    }


    /**
     * 复制含有随机指针节点的链表（剑26，p56）
     * （1）根据原始链表的每个节点N创建对应的N'，
     * （2）设置复制出来值的引用，
     * （3）将长链表拆分为两个链表
     *
     * @param head
     * @return
     */
    public Node copyListWithRand(Node head) {
        if (head == null) {
            return null;
        }
        Node cur = head;
        Node next = null;
        //复制并链接每一个节点
        while (cur != null) {
            next = cur.next;//每次修改节点时，都先把后面一个值保存下来
            cur.next = new Node(cur.value);
            cur.next.next = next;
            cur = next;
        }
        cur = head;//复制完初始化
        Node curCopy = null;
        //设置复制节点的rand指针
        while (cur != null) {
            next = cur.next.next;//复制指针的下一个节点
            curCopy = cur.next;//复制指针
            curCopy.rand = cur.rand != null ? cur.rand.next : null;// cur.rand.next表示指向复制的随机数
            cur = next;
        }
        Node res = head.next; //复制Node的初始化
        cur = head;
        while (cur != null) {
            next = cur.next.next;
            curCopy = cur.next;
            cur.next = next;
            curCopy.next = next != null ? next.next : null;
            cur = next;
        }
        return res;
    }

    /**
     * 打印链表的相交部分（剑37，p62）
     * 无环，而且已知两者相交，则同时遍历到两个栈的尾节点，然后让较长的链表先走上若干步。
     */
    public Node getFirstNode(Node head1, Node head2) {
        if (head1 != null || head2 != null) {
            return null;
        }
        Node cur1 = head1;
        Node cur2 = head2;
        int n = 0;
        while (cur1.next != null) {
            n++;
            cur1 = cur1.next;
        }
        while (cur2.next != null) {
            n--;
            cur2 = cur2.next;
        }
        if (cur1 != cur2) {
            return null;//如果两个node的最后一个值不相等，说明两者不相交
        }
        cur1 = n > 0 ? head1 : head2;
        cur2 = cur1 == head1 ? head2 : head1;
        n = Math.abs(n);
        while (n != 0) {
            n--;
            cur1 = cur1.next;//长的先遍历
        }
        while (cur1 != cur2) {
            cur1 = cur1.next;
            cur2 = cur2.next;
        }
        return cur1;
    }


    /**
     * 判断是否有环，如果有环在哪里？（剑56）
     * 1、设两个指针，快指针每次走2步，慢指针每次走1步
     * 2、当slow和fast相遇时，fast指针移动到head的位置，fast改为一次一步
     * 3、当slow和fast再次相遇时，则是第一个入环口
     */
    public Node getLoopNode(Node head) {
        if (head == null || head.next == null || head.next.next == null) {
            return null;
        }
        Node n1 = head.next;//slow
        Node n2 = head.next.next;//fast
        while (n1 != n2) {
            if (n2.next == null || n2.next.next == null) {
                return null;//说明没环
            }
            n2 = n2.next.next;
            n1 = n1.next;
        }
        n2 = head;
        while (n1 != n2) {
            n1 = n1.next;
            n2 = n2.next;
        }
        return n1;
    }


    /**
     * 删除链表中重复的结点（剑57）
     * 注意
     * 1，先设一个节点first，防止头节点被删
     * 2，使用p指向当前的节点，last指向重复的前一个节点。
     */

    public ListNode deleteDuplication(ListNode pHead) {
        ListNode first = new ListNode(-1);//新建一个节点防止头节点被删
        first.next = pHead;

        ListNode p = pHead;
        ListNode last = first;
        while (p != null && p.next != null) {
            if (p.val == p.next.val) {
                int val = p.val;
                while (p != null && p.val == val) {
                    p = p.next;
                }
                last.next = p;
            } else {
                last = p;
                p = p.next;
            }
        }
        return first.next;
    }
}
