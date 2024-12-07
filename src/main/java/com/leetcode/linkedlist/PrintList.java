package com.leetcode.linkedlist;

import java.util.ArrayList;
import java.util.Stack;

/**
 * 从尾到头打印链表
 *
 * @author gxyan
 * @date 2021/3/27
 */
public class PrintList {
    /**
     * 递归
     */
    public ArrayList<Integer> recursion(ListNode listNode) {
        ArrayList<Integer> ret = new ArrayList<>();
        if (listNode != null) {
            ret.addAll(recursion(listNode.next));
            ret.add(listNode.val);
        }
        return ret;
    }

    /**
     * 头插法
     */
    public ArrayList<Integer> headInsert(ListNode listNode) {
        // 头插法构建逆序链表。引入了一个叫辅助节点（头节点），该节点不存储值，仅为了方便进行插入操作
        ListNode head = new ListNode(-1);
        while (listNode != null) {
            ListNode memo = listNode.next;
            listNode.next = head.next;
            head.next = listNode;
            listNode = memo;
        }
        // 构建 ArrayList
        ArrayList<Integer> ret = new ArrayList<>();
        head = head.next;
        while (head != null) {
            ret.add(head.val);
            head = head.next;
        }
        return ret;
    }

    /**
     * 栈
     */
    public ArrayList<Integer> stack(ListNode listNode) {
        Stack<Integer> stack = new Stack<>();
        while (listNode != null) {
            stack.add(listNode.val);
            listNode = listNode.next;
        }
        ArrayList<Integer> ret = new ArrayList<>();
        while (!stack.isEmpty()) {
            ret.add(stack.pop());
        }
        return ret;
    }
}
