package com.tracker.common.utils.chinese;

import org.apache.commons.collections.map.HashedMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pengcheng.wan on 2017/2/10.
 */
public class TrieNode<T> {
    private int level;
    private char key;
    private T value;
    private Map<Character, TrieNode<T>> children;
    private boolean leaf;


    public TrieNode(char key) {
        this.level = 0;
        this.key = key;
        this.children = new HashMap<Character, TrieNode<T>>();
    }

    public char getKey() {
        return key;
    }

    public void setKey(char key) {
        this.key = key;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public boolean isLeaf() {
        return leaf;
    }


    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public TrieNode<T> getChildren(char k) {
        return children.get(k);
    }

    public TrieNode<T> addChildren(char k) {
        TrieNode<T> node = new TrieNode<T>(k);
        node.level = this.level + 1;
        children.put(k, node);
        return node;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(key);
        if (value != null) {
            sb.append(":").append(value);
        }
        return sb.toString();
    }
}
