package com.rantao.practice.list;

import com.rantao.utilities.LinkedListNode;

public class FindTheLoopStartNode {
	
	public static LinkedListNode startLoopNode(LinkedListNode n){
		LinkedListNode slow = n;
		LinkedListNode fast = n;
		if(n == null)
			return null;
		while(slow != fast && fast.next !=null){
			slow = slow.next;
			fast = fast.next.next;
		}
		if(fast == null || fast.next == null)
			return null;
		slow = n;
		while(slow != fast){
			slow = slow.next;
			fast = fast.next;
		}
		return slow;		
	}
}
