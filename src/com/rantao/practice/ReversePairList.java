package com.rantao.practice;
import com.rantao.utilities.LinkedListNode;

public class ReversePairList {
	
	public static LinkedListNode reversePairList(LinkedListNode head){
		if (head == null)
			return null;
		LinkedListNode current = head;
		head = head.next;
		LinkedListNode previous = null;
		
		while(current != null && current.next != null){
			// Save the second and third node
			LinkedListNode next = current.next;
			LinkedListNode nextnext = next.next;
			
			// Second.next = current; current.next = third
			next.next = current;
			current.next = nextnext;
			
			// Update the next value of previous node
			if (previous != null){
				previous.next = next;
			}
			
			previous = current;
			current = nextnext;
		}		
		return head;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LinkedListNode head = com.rantao.utilities.AssortedMethods.randomLinkedList(6, 0, 2);
		System.out.println(head.printForward());
		LinkedListNode n = reversePairList(head);
		System.out.println(n.printForward());

	}

}
