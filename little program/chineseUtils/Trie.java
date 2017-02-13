package com.tracker.common.utils.chinese;

/**
 * Created by pengcheng.wan on 2017/2/10.
 */


import org.apache.commons.lang.StringUtils;


public class Trie<T> {
    private TrieNode<T> root = new TrieNode<T>(' ');

    /**
     * 增加子节点
     */
    public void add(char[] w, T value) {
        if (w.length < 1) return;
        TrieNode<T> p = root;
        for (int i = 0; i < w.length; i++) {
            TrieNode<T> n = p.getChildren(w[i]);
            if (n == null) {
                n = p.addChildren(w[i]);
            }
            p = n;
        }
        p.setLeaf(true);
        p.setValue(value);
    }


    public void add(String s, T value) {
        if (StringUtils.isEmpty(s)) {
            return;
        }
        add(s.toCharArray(), value);
    }


    /**
     * 匹配词组
     */
    public TrieNode<T> match(char[] chars, int len) {
        TrieNode<T> node = root;
        for (int i = 0; i < len; i++) {
            node = node.getChildren(chars[i]);
            if (node != null) {
                if (node.isLeaf()) {
                    return node;
                }
            } else {
                break;
            }
        }
        return null;
    }

}
