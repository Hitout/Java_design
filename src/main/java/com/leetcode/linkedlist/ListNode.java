package com.leetcode.linkedlist;

/**
 * @author gxyan
 * @date 2021/3/27
 */
public class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) {
        this.val = val;
    }
    ListNode(int val, ListNode next) {
        this.val = val; this.next = next;
    }
}
