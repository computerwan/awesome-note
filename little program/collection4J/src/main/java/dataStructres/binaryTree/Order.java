package dataStructres.binaryTree;

import java.util.*;

/**
 * Created by pengcheng.wan on 2016/8/17.
 */

/**
 * 1、主要包括递归和非递归的前中后序遍历
 * 2、按层遍历
 * 注：pop出来的结果一定要 保存到root中去，然后才能去getLeft和getRight
 */

public class Order {

    /**
     * 递归遍历
     */

    public void PreOrder(Node node) {
        if (node == null) {
            return;
        } else {
            System.out.print(node.getData() + " ");
            PreOrder(node.getLchild());
            PreOrder(node.getRchild());
        }
    }

    public void InOrder(Node node) {
        if (node == null) {
            return;
        } else {
            InOrder(node.getLchild());
            System.out.print(node.getData() + " ");
            InOrder(node.getRchild());
        }
    }

    public void PostOrder(Node node) {
        if (node == null) {
            return;
        } else {
            PostOrder(node.getLchild());
            PostOrder(node.getRchild());
            System.out.print(node.getData() + " ");
        }
    }

    /**
     * 非递归遍历
     */

    //根节点都是先入栈，就立刻出栈，注意push子节点时先右后左
    public void preOrderNoRecursive(Node root) {
        if (root == null)
            return;
        Stack<Node> s = new Stack<Node>();
        s.push(root);
        while (!s.isEmpty()) {
            root = s.pop();
            System.out.print(root.getData() + " ");
            //要先push右边的，再push左边的
            if (root.getRchild() != null)
                s.push(root.getRchild());
            if (root.getLchild() != null)
                s.push(root.getLchild());
        }
    }

    //一直是左边先入栈，每入一次入栈其父节点其实就在其上面
    public void inOrderNoRecursive(Node root) {
        if (root == null)
            return;
        Stack<Node> s = new Stack<Node>();
        while (root != null || !s.isEmpty()) {
            while (root != null) {
                s.push(root);
                root = root.getLchild();
            }
            root = s.pop();
            System.out.print(root.getData() + " ");
            root = root.getRchild();
        }
    }

    public void postOrderNoRecursive(Node root) {
        if (root == null)
            return;
        Stack<Node> s1 = new Stack<Node>();
        Stack<Node> s2 = new Stack<Node>();
        s1.push(root);
        while (!s1.isEmpty()) {
            root = s1.pop();
            s2.push(root);
            if (root.getLchild() != null)
                s1.push(root.getLchild());
            if (root.getRchild() != null)
                s1.push(root.getRchild());
        }
        while (!s2.isEmpty()) {
            Node temp = s2.pop();
            System.out.print(temp.getData() + " ");
        }
    }

    /**
     * 二叉树按层打印（剑23）
     */

    public ArrayList<Character> TreePrinter(Node root) {
        ArrayList<Character> list = new ArrayList<Character>();
        char newLine = '#';//换行
        if (root == null) {
            return list;
        }
        Node last = root; //last则始终指向本行的最后一个数
        Node nLast = last; //nlast是随着指正遍历而遍历
        Queue<Node> queue = new LinkedList<Node>();
        queue.add(last);
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            list.add(node.getData());
            if (node.getLchild() != null) {
                nLast = node.getLchild();
                queue.add(nLast);
            }
            if (node.getRchild() != null) {
                nLast = node.getRchild();
                queue.add(nLast);
            }
            if (last == node) {
                last = nLast;
                list.add(newLine);
            }
        }
        return list;
    }

    /**
     * 判断二叉树是否对称（递归，剑59）
     */
    boolean isSymmetrical(TreeNode pRoot) {
        if (pRoot == null) {
            return true;
        }
        return leftAndRight(pRoot.left, pRoot.right);
    }

    boolean leftAndRight(TreeNode root1, TreeNode root2) {
        if (root1 == null && root2 == null) {
            return true;
        } else if (root1 != null && root2 != null) {
            if (root1.val != root2.val) {
                return false;
            } else {
                return leftAndRight(root1.left, root2.right) && leftAndRight(root1.right, root2.left); //注意left和right的位置
            }
        }
        return false;
    }
}
