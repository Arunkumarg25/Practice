package com.rantao.practice;
import com.rantao.utilities.LinkedListNode;

public class FindKthToLastElement {
	
	public static LinkedListNode findKthToLast(LinkedListNode head, int k){
		LinkedListNode first = head;
		LinkedListNode second = head;
		while (k > 0){
			second = second.next;
			k--;
		}
		while(second != null){
			first = first.next;
			second = second.next;
		}		
		return first;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LinkedListNode head = com.rantao.utilities.AssortedMethods.randomLinkedList(10, 0, 2);
		System.out.println(head.printForward());
		LinkedListNode n = findKthToLast(head, 3);
		System.out.println(n.data);
	}

}
