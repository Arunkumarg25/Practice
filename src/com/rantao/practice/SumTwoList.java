package com.rantao.practice;

import com.rantao.utilities.LinkedListNode;

public class SumTwoList {

	public static LinkedListNode sum(LinkedListNode a, LinkedListNode b,
			int carry) {
		LinkedListNode results = new LinkedListNode(0, null, null);
		if (a == null && b == null)
			return null;
		int value = 0;
		if (a != null) {
			value = value + a.data;
		}
		if (b != null) {
			value = value + b.data;
		}
		results.data = (value % 10) + carry;
		if (value >= 10) {
			carry = 1;
		} else {
			carry = 0;
		}

		LinkedListNode temp = sum(a == null ? null : a.next, 
								  b == null ? null: b.next, 
								  carry);
		results.next = temp;

		return results;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LinkedListNode a = com.rantao.utilities.AssortedMethods
				.randomLinkedList(3, 0, 9);
		LinkedListNode b = com.rantao.utilities.AssortedMethods
				.randomLinkedList(3, 0, 9);
		System.out.println(a.printForward());
		System.out.println(b.printForward());
		LinkedListNode c = sum(a, b, 0);
		System.out.println(c.printForward());
	}

}
