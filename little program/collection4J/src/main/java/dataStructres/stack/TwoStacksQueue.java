package dataStructres.stack;

/**
 * Created by Wan on 2016/8/20 0020.
 */

import java.util.Stack;

/**
 * 题目：由两个栈实现队列（剑7，p5）
 * 思路：一个栈作为压入栈，一个栈作为弹出栈，压入和弹出的时候都要保证stack中为空
 */
public class TwoStacksQueue {
    public Stack<Integer> stackPush;
    public Stack<Integer> stackPop;

    public TwoStacksQueue() {
        stackPush = new Stack<Integer>();
        stackPop = new Stack<Integer>();
    }

    public void add(int pushInt) {
        stackPush.push(pushInt);
    }

    public int poll() {
        peek();
        return stackPop.pop();
    }

    public int peek() {
        if (stackPop.empty() && stackPush.empty()) {
            throw new RuntimeException("Queue is empty!");
        } else if (stackPop.empty()) {
            while (!stackPush.empty()) {
                stackPop.push(stackPush.pop());
            }
        }
        return stackPop.peek();
    }
}
