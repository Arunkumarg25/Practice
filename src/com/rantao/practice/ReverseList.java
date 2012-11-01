package com.rantao.practice;

import com.rantao.utilities.LinkedListNode;

public class ReverseList {
	
	public static LinkedListNode reverse(LinkedListNode head){
		if(head == null)
			return null;
		if (head.next == null)
			return head;
		
		LinkedListNode previous = null;
		while(head != null){
			LinkedListNode next = head.next;
			head.next = previous;
			previous = head;
			head = next;
		}
		return previous;
		
	}
	
	public static void main(String[] args) {
		LinkedListNode head = com.rantao.utilities.AssortedMethods.randomLinkedList(5, 0, 2);
		System.out.println(head.printForward());
		LinkedListNode n = reverse(head);
		System.out.println(n.printForward());

	}

}
