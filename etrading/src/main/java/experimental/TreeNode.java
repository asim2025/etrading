package experimental;

/*
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */
public class TreeNode {

	private TreeNode left;
	private TreeNode right;
	private int data;
	
	public TreeNode(int data, TreeNode left, TreeNode right) {
		this.data = data;
		this.left = left;
		this.right = right;
	}
	
	public TreeNode getLeft() {
		return left;
	}
	
	public void setLeft(TreeNode left) {
		this.left = left;
	}
	
	public TreeNode getRight() {
		return right;
	}
	
	public void setRight(TreeNode right) {
		this.right = right;
	}
	
	public int getData() {
		return data;		
	}
	
	public void setData(int data) {
		this.data = data;
	}
}
