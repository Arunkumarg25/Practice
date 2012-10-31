package com.rantao.practice;

import java.util.HashMap;

import com.rantao.utilities.LinkedListNode;

public class RemoveDuplicates {
	
	public static void removeDup(LinkedListNode head){
		LinkedListNode pointer;
		LinkedListNode previous;
		if(head == null || head.next == null){
			return;
		}
		pointer = head.next;
		previous = head;
		while(head !=null){
			while(pointer !=null){
				if(pointer.data == head.data){
					previous.next = pointer.next;
					pointer = pointer.next;					
				} else {
					pointer = pointer.next;
					previous = previous.next;
				}
			}
			head = head.next;
		}
	}
	
	public static void removeDup1(LinkedListNode head){
		if(head == null || head.next == null)
			return;
		LinkedListNode previous = head;
		HashMap<Integer, Boolean> map = new HashMap<Integer, Boolean>();
		while(head != null){
			if(map.get(head.data)){
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

	}

}
