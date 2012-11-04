package com.rantao.practice.list;
import java.util.Stack;
import com.rantao.utilities.LinkedListNode;

public class CheckPalindromeLinkedList {
	
	public static boolean isPalindromeList(LinkedListNode head){
		LinkedListNode slow = head;
		LinkedListNode fast = head;
		
		if (head == null || head.next == null){
			return true;
		}
		
		if(head.next.next == null)
			return false;
		
		Stack<LinkedListNode> st = new Stack<LinkedListNode>();
		while(fast != null && fast.next != null){
			st.push(slow);
			slow = slow.next;
			fast = fast.next.next;
		} 
		
		if(fast != null)
			slow = slow.next;
			
		while(slow != null){
			LinkedListNode temp = st.pop();			
			if(slow.data != temp.data)
				return false;
			slow = slow.next;
				
		}		
		return true;
		
	}
	
	public static void main(String[] args) {
		LinkedListNode a = new LinkedListNode(1, null, null);
		LinkedListNode b = new LinkedListNode(2, null, null);
		LinkedListNode c = new LinkedListNode(3, null, null);
		LinkedListNode d = new LinkedListNode(3, null, null);
		LinkedListNode e = new LinkedListNode(2, null, null);
		LinkedListNode f = new LinkedListNode(1, null, null);
		a.setNext(b);
		b.setNext(c);
		c.setNext(d);
		d.setNext(e);
		e.setNext(f);
		
		System.out.println(a.printForward());
		boolean result = isPalindromeList(a);
		System.out.println(result);
		
	}

}
