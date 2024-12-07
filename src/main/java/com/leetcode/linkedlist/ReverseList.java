package com.leetcode.linkedlist;

/**
 * 反转链表
 *
 * @author gxyan
 * @date 2021/3/27
 */
public class ReverseList {
    /**
     * 递归
     */
    public ListNode recursion(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode next = head.next;
        head.next = null;
        ListNode newHead = recursion(next);
        next.next = head;
        return newHead;
    }

    /**
     * 头插法
     */
    public ListNode headInsert(ListNode head) {
        ListNode newList = new ListNode(-1);
        while (head != null) {
            ListNode next = head.next;
            head.next = newList.next;
            newList.next = head;
            head = next;
        }
        return newList.next;
    }
}
