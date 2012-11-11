package com.rantao.practice.tree;

public class MyTreeNode {
	MyTreeNode left;
	MyTreeNode right;
	int data;
	
	public void setLeft(MyTreeNode n){
		this.left = n;
	}
	
	public void setRight(MyTreeNode n){
		this.right = n;
	}
	
	public void setData(int i){
		this.data = i;
	}
	
	public Boolean isBalanced(MyTreeNode root){
		if(Math.abs(checkHeight(root.left) - checkHeight(root.right)) <=1)
			return true;
		else 
			return false;
	}
	
	public int checkHeight(MyTreeNode root){
		if(root == null)
			return 0;
		else if(root.left == null && root.right == null)
			return 1;
		else 
			return Math.max(checkHeight(root.left), checkHeight(root.right)) + 1;		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MyTreeNode a = new MyTreeNode();
		MyTreeNode b = new MyTreeNode();
		MyTreeNode c = new MyTreeNode();
		MyTreeNode d = new MyTreeNode();
		MyTreeNode e = new MyTreeNode();
		MyTreeNode f = new MyTreeNode();
		a.setLeft(b);
		a.setRight(c);
		b.setLeft(d);
		b.setRight(e);
		e.setLeft(f);
		
		System.out.println(a.checkHeight(a));
		System.out.println(a.isBalanced(a));

	}

}
