package dataStructres.binaryTree;

/**
 * Created by pengcheng.wan on 2016/8/17.
 */
public class CreateNode {
    public Node createTree(String exp) {
        Node[] nodes = new Node[3];
        //b是头节点，p是当前节点
        Node b, p = null;
        //k作为判断是左节点还是右节点 top表示行数 j是总节点数
        int top = -1, k = 0, j = 0;
        char[] exps = exp.toCharArray();
        char data = exps[j];
        b = null;
        while (j < exps.length - 1) {
            switch (data) {
                //每当遇到“（”，表面要创建左孩子节点
                //广义表中“（”的数量正好是二叉树的层数
                case '(':
                    top++;
                    nodes[top] = p;
                    k = 1;
                    break;
                //每当遇到“）”，表明要返回上一层节点
                case ')':
                    top--;
                    break;
                //每当遇到“，”，表明要创建右孩子节点
                case ',':
                    k = 2;
                    break;
                //每当遇到字母，将要创建节点
                default:
                    p = new Node(data, null, null);
                    if (b == null) {
                        b = p;
                    } else {
                        switch (k) {
                            case 1:
                                nodes[top].setLchild(p);
                                break;
                            case 2:
                                nodes[top].setRchild(p);
                                break;
                        }
                    }
            }
            j++;
            data = exps[j];
        }
        return b;//返回头结点
    }

    public static void main(String[] args) {
        String expression ="A(B(D(,G)),C(E,F))";
        CreateNode cn = new CreateNode();
        Node tree = cn.createTree(expression);
        Order order = new Order();
        /*order.preOrderNoRecursive(tree);//前序遍历 ABDGCEF
        order.inOrderNoRecursive(tree);//中序遍历 DGBAECF
        order.postOrderNoRecursive(tree);//后序遍历 GDBEFCA*/
        order.TreePrinter(tree);
    }
}
