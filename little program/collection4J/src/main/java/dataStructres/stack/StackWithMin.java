package dataStructres.stack;

/**
 * Created by Wan on 2016/8/25 0025.
 */

import java.util.Stack;

/**
 * 包含最小元素的栈(剑21，p1)
 * 实现方法：使用一个辅助栈，在旁边记录当前的最小值
 */
public class StackWithMin {
    private Stack<Integer> stackData;
    private Stack<Integer> stackMin;

    public StackWithMin() {
        stackData = new Stack<Integer>();
        stackMin = new Stack<Integer>();
    }

    public void push(int newNum) {
        if (stackMin.isEmpty()) {
            stackMin.push(newNum);
        } else if (newNum < this.getMin()) {
            stackMin.push(newNum);
        } else {
            int newMin = stackMin.peek();
            stackMin.push(newMin);
        }
    }

    public int pop() {
        if (stackData.isEmpty()) {
            throw new RuntimeException("Your stack is empty");
        }
        stackMin.pop();
        return stackData.pop();
    }

    public int getMin() {
        if (stackMin.isEmpty()) {
            throw new RuntimeException("Your stack is empty");
        }
        return stackMin.peek();
    }
}
