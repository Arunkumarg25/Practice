package com.rantao.practice.tree;

import java.util.ArrayList;

public class PrintPaths {
	ArrayList<int[]> paths = new ArrayList<int[]>();
	
	public void print(TreeNode root, int[] path, int level){
		if(root == null){
			return;
		}
		path[level] = root.data;
		level++;
		if(root.left == null && root.right == null) {
			for(int i = 0; i < path.length; i++){
				System.out.print(path[i]+"->");
			}
			System.out.print("\n");
		}else {			
			print(root.left, path, level);
			print(root.right, path,	level);
		}	
	}
	
	public static void main(String[] args) {
		
		PrintPaths pp = new PrintPaths();
		TreeNode root = new TreeNode(1);
		TreeNode a = new TreeNode(2);
		TreeNode b = new TreeNode(3);
		TreeNode c = new TreeNode(4);
		TreeNode d = new TreeNode(5);
		TreeNode e = new TreeNode(6);
		TreeNode f = new TreeNode(7);
		root.setLeftChild(a);
		root.setRightChild(b);
		a.setLeftChild(c);
		b.setRightChild(d);
		a.setRightChild(e);
		b.setLeftChild(f);
		int[] path = new int[3];
		pp.print(root, path, 0);
		
	}

}
