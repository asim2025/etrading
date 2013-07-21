package scratchpad;

/*
 * 
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */
public class BinaryTree {
	private TreeNode root = null;
	
	public TreeNode search(int data) {
		TreeNode current = root;
		while (current != null) {
			if (current.getData() == data) {
				break;
			} else if (current.getData() > data) {
				current = current.getLeft();
			} else if (current.getData() < data) {
				current = current.getRight();
			}
		}
		return current;
	}
	
	public boolean delete(int data) {
		TreeNode parent = root;
		TreeNode current = parent;
		
		boolean isLeftChild = false;
		
		while (current != null) {
			if (current.getData() > data) {
				parent = current;
				current = current.getLeft();
				isLeftChild = true;
			} else if (current.getData() < data) {
				parent = current;
				current = current.getRight();
				isLeftChild = false;
			} else if (current.getData() == data) {
				break;
			}			
		}
		
		//0. delete empty tree or no match
		if (current == null) return false;
		
		//1. delete single node tree
		//2. delete leaf node
		if (current.getLeft() == null && current.getRight() == null) {
			if (current == root) {
				root = null;
				return true;
			} else {
				if (isLeftChild) parent.setLeft(null);
				else parent.setRight(null);
				return true;
			}
		}
		
		//3. delete middle node with single left/right tree
		if (current.getLeft() == null && current.getRight()!=null) {
			if (isLeftChild) parent.setLeft(current.getRight());
			else parent.setRight(current.getRight());
			return true;
		}
		
		if (current.getLeft() != null && current.getRight() == null) {
			if (isLeftChild) parent.setLeft(current.getLeft());
			else parent.setRight(current.getLeft());
			return true;			
		}
		
		//4. delete root node with two sub-trees
		//5. delete middle node with two sub-trees
		TreeNode successor = current.getRight();
		isLeftChild = false;
		parent = current;
		while (successor.getLeft() != null) {
			parent = successor;
			successor = successor.getLeft();
			isLeftChild = true;
		}
		current.setData(successor.getData());
		if (isLeftChild) parent.setLeft(null);
		else parent.setRight(null);
		return true;
	}
	
	
	public boolean insert(int data) {
		TreeNode node = new TreeNode(data, null, null);
		if (root == null) {
			root = node;
			return true;
		}
		
		TreeNode runner = root;
		while (true) {
			if (runner.getData() == data) {
				return false; // dup data
			} else if (runner.getData() > data) {
				if (runner.getLeft() != null) runner = runner.getLeft();
				else { runner.setLeft(node); break; }
			} else if (runner.getData() < data) {
				if (runner.getRight() != null) runner = runner.getRight();
				else { runner.setRight(node); break; }
			}
		}
		return true;
	}
	
	public void printTree() {
		printTree(root);
	}
	
	private void printTree(TreeNode node) {
		if (node == null) return;
		System.out.println("\tnode:" + node + "\t{" + node.getData() + "," + node.getLeft() + "," + node.getRight() + "}");
		printTree(node.getLeft());
		printTree(node.getRight());
	}
}
