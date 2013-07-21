package orders;

/*
 * OrderBook stores limits by price in a binary tree data structure.
 * 
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */
public class OrderBook {
	private Limit root = null;


	public Limit search(int limitPrice) {
		Limit current = root;
		while (current != null) {
			if (current.getLimitPrice() == limitPrice) {
				break;
			} else if (current.getLimitPrice() > limitPrice) {
				current = current.getLeft();
			} else if (current.getLimitPrice() < limitPrice) {
				current = current.getRight();
			}
		}
		return current;
	}
	
	public boolean delete(int limitPrice) {
		Limit parent = root;
		Limit current = parent;
		
		boolean isLeftChild = false;
		
		while (current != null) {
			if (current.getLimitPrice() > limitPrice) {
				parent = current;
				current = current.getLeft();
				isLeftChild = true;
			} else if (current.getLimitPrice() < limitPrice) {
				parent = current;
				current = current.getRight();
				isLeftChild = false;
			} else if (current.getLimitPrice() == limitPrice) {
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
		Limit successor = current.getRight();
		isLeftChild = false;
		parent = current;
		while (successor.getLeft() != null) {
			parent = successor;
			successor = successor.getLeft();
			isLeftChild = true;
		}
		current.setLimitPrice(successor.getLimitPrice());
		if (isLeftChild) parent.setLeft(null);
		else parent.setRight(null);
		return true;
	}
	
	
	public Limit insert(int limitPrice) {
		Limit node = new Limit(limitPrice);
		if (root == null) {
			root = node;
			return node;
		}
		
		Limit runner = root;
		while (true) {
			if (runner.getLimitPrice() == limitPrice) {
				return runner;
			} else if (runner.getLimitPrice() > limitPrice) {
				if (runner.getLeft() != null) runner = runner.getLeft();
				else { runner.setLeft(node); return node; }
			} else if (runner.getLimitPrice() < limitPrice) {
				if (runner.getRight() != null) runner = runner.getRight();
				else { runner.setRight(node); return node; }
			}
		}
	}
	
	public void printOrderBook() {
		printTree(root);
	}
	
	private void printTree(Limit node) {
		if (node == null) return;
		System.out.println("\tnode:" + node + "\t{" + node.getLimitPrice() + "," + node.getLeft() + "," + node.getRight() + "}");
		printTree(node.getLeft());
		printTree(node.getRight());
	}
	
}
