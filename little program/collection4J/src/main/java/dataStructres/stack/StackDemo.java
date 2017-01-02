package dataStructres.stack;

import java.util.Stack;

/**
 * Created by Wan on 2016/8/27 0027.
 */
public class StackDemo {
    /**
     * 栈的压入和弹出序列（剑22）
     * i是指向pushA的指针，p是指向popA的指针
     */
    public boolean IsPopOrder(int[] pushA, int[] popA) {
        if (pushA.length == 0 || popA.length == 0) {
            return false;
        }
        Stack<Integer> stack = new Stack<Integer>();
        int p = 0;
        for (int i = 0; i < pushA.length; i++) {
            stack.push(pushA[i]);
            while (!stack.isEmpty() && stack.peek() == popA[p]) {
                stack.pop();
                p++;
            }
        }
        return stack.isEmpty();
    }
}