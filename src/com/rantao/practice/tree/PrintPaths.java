package com.rantao.practice.tree;

import java.util.ArrayList;
import java.util.Stack;

public class PrintPaths {
	Stack<TreeNode> path = new Stack<TreeNode>();
	ArrayList<Stack<TreeNode>> paths = new ArrayList<Stack<TreeNode>>();
	
	public void print(TreeNode root){
		if(root == null)
			return;
		path.push(root);
		if(root.left != null)
			print(root.left);
		if(root.right != null)
			print(root.right);		
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
