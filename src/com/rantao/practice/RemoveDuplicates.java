package com.rantao.practice;

import java.util.HashMap;

import com.rantao.utilities.LinkedListNode;

public class RemoveDuplicates {
	
	public static void removeDup(LinkedListNode head){
		LinkedListNode pointer;
		LinkedListNode current;
		if(head == null || head.next == null){
			return;
		}
		current = head;
		while(current !=null){
			pointer = current;
			while(pointer.next !=null){
				if(pointer.next.data == current.data){
					pointer.next = pointer.next.next;					
				} else {
					pointer = pointer.next;
				}
			}
			current = current.next;			
		}
	}
	
	public static void removeDup1(LinkedListNode head){
		if(head == null || head.next == null)
			return;
		LinkedListNode previous = head;
		HashMap<Integer, Boolean> map = new HashMap<Integer, Boolean>();
		while(head != null){
			if(map.containsKey(head.data)){
				previous.next = head.next;
			}else{
				map.put(head.data, true);
				previous = head;
			}
			head = head.next;			
		}		
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LinkedListNode head = com.rantao.utilities.AssortedMethods.randomLinkedList(10, 0, 2);
		//System.out.println(head.printForward());
		removeDup(head);
		System.out.println(head.printForward());

	}

}
