package dataStructres.binaryTree;

import java.util.*;

/**
 * Created by Wan on 2016/8/25 0025.
 */
public class NodeDemo {
    /**
     * 根据前序遍历和中序遍历重建二叉树(剑6，p171)
     * 1.前序遍历的第一个数为根节点。
     * 2.然后在中序中找到该节点，然后遍历。
     */
    public TreeNode reConstructBinaryTree(int[] pre, int[] in) {
        TreeNode root = reConstructBinaryTree(pre, 0, pre.length - 1, in, 0, in.length - 1);
        return root;
    }

    private TreeNode reConstructBinaryTree(int[] pre, int startPre, int endPre, int[] in, int startIn, int endIn) {
        if (startPre > endPre || startIn > endIn) {
            return null;
        }
        TreeNode root = new TreeNode(pre[startPre]);//获取根节点
        for (int i = startIn; i <= endIn; i++) {//注意等于号一定不能省略，否则会丢失根节点
            if (in[i] == pre[startPre]) {//在中序遍历的时候找到前序的根节点
                root.left = reConstructBinaryTree(pre, startPre + 1, startPre + i - startIn, in, startIn, i - 1);
                root.right = reConstructBinaryTree(pre, i - startIn + startPre + 1, endPre, in, i + 1, endIn);
            }
        }
        return root;
    }


    /**
     * 树的子结构（剑18，p140）
     */
    public boolean HasSubtree(TreeNode root1, TreeNode root2) {
        if (root1 == null || root2 == null) {
            return false;
        }
        return check(root1, root2) || HasSubtree(root1.left, root2) || HasSubtree(root1.right, root2);
    }

    public boolean check(TreeNode root1, TreeNode root2) {
        if (root2 == null) {
            return true;//t2遍历结束
        }
        if (root1 == null || root1.val != root2.val) {
            return false;
        }
        //能到这，说明两者都不为空，且父节点相同，注意用&&
        return check(root1.left, root2.left) && check(root1.right, root2.right);
    }

    /**
     * 二叉树的镜像（剑19）
     * 根节点相同，子节点交换了位置
     * 方法：中序遍历，同时交换左右节点的位置。
     */
    public void mirrorRecursive(TreeNode root) {
        if (root == null) {
            return;
        }
        Stack<TreeNode> stack = new Stack<TreeNode>();
        while (root != null || !stack.isEmpty()) {
            while (root != null) {
                //交换左右节点
                stack.push(root);
                TreeNode temp = root.left;
                root.left = root.right;
                root.right = temp;
                root = root.left;
            }
            root = stack.pop();
            System.out.print(root.val);
            root = root.right;
        }
    }

    /**
     * 二叉搜索树的后序遍历（剑24，p145）
     * 首先找到头节点，然后后面依次遍历
     */
    public boolean isPostArray(int[] arr) {
        if (arr == null || arr.length == 0) {
            return false;
        }
        if (arr.length == 1) {
            return true;
        }
        return isPost(arr, 0, arr.length - 1);
    }

    public boolean isPost(int[] arr, int start, int end) {
        if (start == end) {
            return true;
        }
        int less = -1;
        int more = end;
        for (int i = start; i < end; i++) {
            if (arr[end] > arr[i]) {
                less = i; //存放比根节点小的最后一个数
            } else {
                more = more == end ? i : more;//存放比根节点大的第一个数
            }
        }
        //只有一个子树的情况
        if (less == -1 || more == end) {
            return isPost(arr, start, end - 1);
        }
        if (less != more - 1) {
            return false;
        }
        return isPost(arr, start, less) && isPost(arr, more, end - 1);
    }

    /**
     * 二叉树中和为某值的路径(剑25)
     * 前序递归遍历,用list来存储路径。
     * 路径定义为从树的根结点开始往下一直到叶结点所经过的结点形成一条路径。
     */


    public ArrayList<ArrayList<Integer>> FindPath(TreeNode root, int target) {
        ArrayList<ArrayList<Integer>> listAll = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> list = new ArrayList<Integer>();
        if (root == null)
            return listAll;
        list.add(root.val);
        target -= root.val;
        if (target == 0 && root.left == null && root.right == null) {//根据路径的定义要到根节点
            listAll.add(new ArrayList<Integer>(list));
        }
        FindPath(root.left, target);
        FindPath(root.right, target);
        list.remove(list.size() - 1); //不要忘记叠加好了，就要把值去掉
        return listAll;
    }

    /**
     * 二叉树中找到累加和为指定值的最长路径长度（p115）
     * 前序遍历，通过preSum和curSum记录当前的和以及父节点的和
     * 方法可以参考（p355）
     */
    public int getMaxLength(Node head, int sum) {
        HashMap<Integer, Integer> sumMap = new HashMap<Integer, Integer>(); //key代表累加和，value该累加和最早出现的层数
        sumMap.put(0, 0);
        return preOrder(head, sum, 0, 1, 0, sumMap);

    }

    //sumMap其实是记录着路径中所有的累加和，以及其最早出现的时间。-->这样获得以该head结尾的数和为sum的值。
    private int preOrder(Node head, int sum, int preSum, int level, int maxLen, HashMap<Integer, Integer> sumMap) {
        if (head == null) {
            return maxLen;
        }
        int curSum = preSum + head.getData();
        if (!sumMap.containsKey(curSum)) {
            sumMap.put(curSum, level);
        }
        if (sumMap.containsKey(curSum - sum)) {
            maxLen = Math.max(level - sumMap.get(curSum - sum), maxLen);
        }
        maxLen = preOrder(head.getLchild(), sum, curSum, level + 1, maxLen, sumMap);
        maxLen = preOrder(head.getRchild(), sum, curSum, level + 1, maxLen, sumMap);
        if (level == sumMap.get(curSum)) {
            sumMap.remove(curSum);
        }
        return maxLen;
    }

    /**
     * 二叉搜索树和双向链表（剑27，p74）
     * 用队列等容器收集二叉树中间遍历的结果
     * right指针等价于next指针，left指针等价于last指针
     */
    public Node convert(Node head) {
        Queue<Node> queue = new LinkedList<Node>();
        inOrderToQueue(head, queue);
        if (queue.isEmpty()) {
            return head;
        }
        head = queue.poll();
        Node pre = head;
        pre.setLchild(null);
        Node cur = null;
        while (queue.isEmpty()) {
            cur = queue.poll();
            pre.setRchild(cur);
            cur.setLchild(pre);
            pre = cur;
        }
        pre.setRchild(null);
        return head;
    }

    public void inOrderToQueue(Node head, Queue<Node> queue) {
        if (head == null) {
            return;
        }
        inOrderToQueue(head.getLchild(), queue);
        queue.offer(head);
        inOrderToQueue(head.getRchild(), queue);
    }

    /**
     * 最小的K个数（剑30，p336）
     */

    /**
     * 二叉树的深度，是否是平衡二叉树（剑39，p144）
     * 思路：整体为二叉树的后序遍历，其中收集两个数据，lH和rH，先看起是否为平衡二叉树，如果是，则看两者的差是否大于1
     * (注：这里的res类似以全局的判断条件)
     */
    public boolean isBalance(Node head) {
        boolean[] res = new boolean[1];
        res[0] = true;
        getHeight(head, 1, res);
        return res[0];
    }

    private int getHeight(Node head, int level, boolean[] res) {
        if (head == null) {
            return level;
        }
        int lH = getHeight(head.getLchild(), level + 1, res);
        if (!res[0]) {
            return level;
        }
        int rH = getHeight(head.getRchild(), level + 1, res);
        if (!res[0]) {
            return level;
        }
        if (Math.abs(lH - rH) > 1) {
            res[0] = false;
        }
        return Math.max(lH, rH);
    }

    /**
     * 树中两个节点的最低公共祖先（剑50，p153）
     * 后序遍历，主要分为left和right两个子树
     * 1、如果两者都不为null，则该点就是
     * 2、两者为空，则都不是
     * 3、有一个是，则接着向上遍历
     */
    public Node lowestAncestor(Node head, Node o1, Node o2) {
        if (head == null || head == o1 || head == o2) {
            return head;
        }
        Node left = lowestAncestor(head.getLchild(), o1, o2);
        Node right = lowestAncestor(head.getRchild(), o1, o2);
        if (left != null && right != null) {
            return head;
        }
        return left != null ? left : right;
    }


    /**
     * 在二叉树中找到一个节点的后继节点（剑58，p151）
     * next是指向其父节点的数，中序遍历的下一个就是其后继节点
     * 1、如果其有右子树，其后继节点即为右子树的最左边节点
     * 2、如果没有右子树，先获取其父节点
     * （1）如果其为父节点的左子树，则父节点为其后继节点
     * （2）如果不是，则向上遍历
     */
    public TreeLinkNode GetNext(TreeLinkNode pNode) {
        if (pNode == null) {
            return pNode;
        }
        if (pNode.right != null) {
            return getMostLeft(pNode.right);
        } else {
            TreeLinkNode parent = pNode.next;
            while (parent != null && parent.left != pNode) {
                pNode = parent;
                parent = pNode.next;
            }
            return parent;
        }
    }

    public TreeLinkNode getMostLeft(TreeLinkNode pNode) {
        if (pNode == null) {
            return pNode;
        }
        while (pNode.left != null) {
            pNode = pNode.left;
        }
        return pNode;
    }


    /**
     * 按之字形顺序打印二叉树(剑61，p129)
     */
    public ArrayList<ArrayList<Integer>> Print(TreeNode pRoot) {
        ArrayList<ArrayList<Integer>> ret = new ArrayList<ArrayList<Integer>>();
        if (pRoot == null) {
            return ret;
        }
        ArrayList<Integer> list = new ArrayList<Integer>();
        LinkedList<TreeNode> queue = new LinkedList<TreeNode>();
        queue.addLast(null);//层分割符
        queue.addLast(pRoot);
        boolean leftToRight = true;

        while (queue.size() != 1) {
            TreeNode node = queue.removeFirst();
            if (node == null) {//到达层分割符
                Iterator<TreeNode> iter = null;
                if (leftToRight) {
                    iter = queue.iterator();
                } else {
                    iter = queue.descendingIterator();
                }
                leftToRight = !leftToRight;
                while (iter.hasNext()) {
                    TreeNode temp = (TreeNode) iter.next();
                    list.add(temp.val);
                }
                ret.add(new ArrayList<Integer>(list));
                list.clear();
                queue.addLast(null);//添加层分割符
                continue;
            }
            if (node.left != null) {
                queue.addLast(node.left);
            }
            if (node.right != null) {
                queue.addLast(node.right);
            }
        }
        return ret;

    }

    /**
     * 序列化和反序列化二叉树(剑62 p103)
     */
    String Serialize(TreeNode root) {
        if (root == null) {
            return "#!";
        }
        String res = root.val + "!";
        res += Serialize(root.left);
        res += Serialize(root.right);
        return res;
    }

    TreeNode Deserialize(String str) {
        String[] values = str.split("!");
        Queue<String> queue = new LinkedList<String>();
        for (int i = 0; i != values.length; i++) {
            queue.offer(values[i]);
        }
        return reconPreOrder(queue);
    }

    public TreeNode reconPreOrder(Queue<String> queue) {
        String value = queue.poll();
        if (value.equals("#")) {
            return null;
        }
        TreeNode head = new TreeNode(Integer.valueOf(value));
        head.left = reconPreOrder(queue);
        head.right = reconPreOrder(queue);
        return head;
    }


    /**
     * 二叉搜索树的第k个节点（剑63）
     * 中序遍历，即为去按大小顺序排列的结果
     */
    TreeNode KthNode(TreeNode pRoot, int k) {
        if (k < 0 || pRoot == null) {
            return null;
        }
        Stack<TreeNode> stack = new Stack<TreeNode>();
        int count = 0;
        TreeNode p = pRoot;
        while (p != null || !stack.isEmpty()) {
            while (p != null) {
                stack.push(p);
                p = p.left;
            }
            p = stack.pop();
            count++;
            if (count == k) {
                return p;
            }
            p = p.right;
        }
        return null;
    }

}
