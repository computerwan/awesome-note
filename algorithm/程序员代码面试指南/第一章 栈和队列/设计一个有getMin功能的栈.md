###题目：
实现一个特殊的栈，在实现栈的基本功能的基础上，再实现返回栈中最小元素的操作。

####分析：
通过创建两个stack，第一个stack跟正常一样。第二个stack，先初始化min值，然后与第二个栈栈顶比较，**小于等于**的时候在第二个stack中压入新的最小值，
方法1：在大于的时候不压，取出时两值相等就弹出栈顶值，不相等时不操作。
方法2：在大于的时候重复压入之前的最小值，取出时两个stack同步。
获取最小值就从第二个栈的栈顶弹出数据就行。

####实现：
```java
/**
 * push是进栈，pop是出栈（返回值是出栈的值），peek是获取栈顶值
 */
public class myStack {
    private Stack<Integer> stackData;
    private Stack<Integer> stackMin;

    public myStack() {
        this.stackMin = new Stack<Integer>();
        this.stackData = new Stack<Integer>();
    }

    public void push(int newNum) {
        if (stackMin.isEmpty()) {
            stackMin.push(newNum);
        } else if (newNum <= getmin()) {
            stackMin.push(newNum);
        }
        stackData.push(newNum);

    }

    public int pop() {

        if (stackData.isEmpty()) {
            throw new RuntimeException("your stack is empty");
        }
        int value = stackData.pop();
        if (value == getmin()) {
            stackMin.pop();
        }
        return value;

    }

    public int getmin() {
        if (stackMin.isEmpty()) {
            throw new RuntimeException("your stack is empty");
        }
        return stackMin.peek();
    }

}
```
####测试：
```java
public class Solution {
    public static void main(String[] args) {
        myStack stack = new myStack();
        stack.push(3);
        stack.push(4);
        stack.push(5);
        stack.push(1);
        stack.push(2);
        stack.push(1);
        stack.pop();
        stack.pop();
        int i = stack.pop();
        i = stack.pop();
        System.out.println(i + " " + stack.getmin());
    }
}


```