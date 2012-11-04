package com.rantao.practice.tree;

public class LCA {
	
	public LCA(){
		super.getClass();
	}	
	
	public Boolean isCovered(TreeNode node, TreeNode root){
		if(node == root)
			return true;
		if(node == null || root == null)
			return false;
		return isCovered(node, root.left) || isCovered(node, root.right);
	}
	
	public TreeNode hasLCA(TreeNode node1, TreeNode node2, TreeNode root){
		if(isCovered(node1, root.left) && isCovered(node2, root.left))
			return hasLCA(node1, node2, root.left);
		else if(isCovered(node1, root.right) && isCovered(node2, root.left))
			return hasLCA(node1, node2, root.right);
		else if(isCovered(node1, root) && isCovered(node2, root))
			return root;
		return null;
		
	}

}
