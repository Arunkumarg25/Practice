package com.rantao.practice.tree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Queue;
import java.util.Stack;

public class PrintPaths {
	ArrayList<TreeNode> path = new ArrayList<TreeNode>();
	ArrayList<ArrayList<TreeNode>> paths = new ArrayList<ArrayList<TreeNode>>();
	HashSet<TreeNode> hs = new HashSet<TreeNode>();
	
	public ArrayList<ArrayList<TreeNode>> print(TreeNode root, int level){
		if(root == null){
			paths.add(path);
			path = new ArrayList<TreeNode>();			
		}
		else {
			path.add(root);
			print(root.left, level+1);
			print(root.right, level+1);
		}		
		return paths;
	}
	

	/**
	 * @param args
	 */
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
		ArrayList<ArrayList<TreeNode>> paths = pp.print(root, 0);
		Iterator<ArrayList<TreeNode>> it =paths.iterator();
		while(it.hasNext()){
			ArrayList<TreeNode> temp = it.next();
			for(int i = 0; i < temp.size(); i ++){
				System.out.print(temp.get(i).data + "->");
			}
			System.out.print("\n");
		}
		
	}

}
