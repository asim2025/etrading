package scratchpad;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import scratchpad.BinaryTree;
import scratchpad.TreeNode;

/*
 * https://github.com/asim2025/etrading.git
 * 
 * @author asim2025
 */
public class BinaryTreeTest {
	private BinaryTree tree;
	
	@Before
	public void setup() {
		System.out.println("setup tree...");
		tree = new BinaryTree();
		tree.insert(100);
		tree.insert(50);
		tree.insert(200);
		tree.printTree();
	}
	
	@After
	public void cleanup() {
		System.out.println("cleanup...");
		tree = null;
	}
	
	@Test
	public void searchTreeFoundRoot() {
		TreeNode result = tree.search(100);
		System.out.println("search 100, result:" + result);
		assertNotNull(result);
	}
	
	@Test
	public void searchTreeFoundChild() {
		TreeNode result = tree.search(50);
		System.out.println("search 50, result:" + result);
		assertNotNull(result);
	}
	
	@Test
	public void searchTreeNotFound() {
		TreeNode result = tree.search(3000);
		System.out.println("search 3000, result:" + result);
		assertNull(result);
	}
	
	@Test
	public void insertLeaf() {
		System.out.println("insert 300");
		boolean result = tree.insert(300);
		tree.printTree();
		assertTrue(result);
	}
	
	@Test
	public void deleteEmptyTree() {
		BinaryTree localTree = new BinaryTree();
		localTree.printTree();
		boolean result = localTree.delete(100);
		System.out.println("delete empty tree, result:" + result);
		assertFalse(result);
	}
	
	@Test
	public void deleteLeaf() {
		boolean result = tree.delete(50);
		System.out.println("deleteLeftLeaf, result:" + result);
		tree.printTree();
		assertTrue(result);
		
		result = tree.delete(200);
		System.out.println("deleteRightLeaf, result:" + result);
		tree.printTree();
		assertTrue(result);
	}
	
	@Test
	public void deleteRoot() {
		boolean result = tree.delete(100);
		System.out.println("delete root, result:" + result);
		tree.printTree();
		assertTrue(result);
	}
	
	@Test
	public void deleteMiddleNodeWithTwoChildren() {
		System.out.println("add more children");
		tree.insert(40);
		tree.insert(60);
		tree.insert(250);
		tree.insert(150);
		tree.printTree();
		
		boolean result = tree.delete(50);
		System.out.println("delete leftchildwithchildren, result:" + result);
		tree.printTree();
		assertTrue(result);
		
		result=tree.delete(200);
		System.out.println("delete rightchildwithchildren, result:" + result);
		tree.printTree();
		assertTrue(result);
	}
	
	@Test
	public void deleteMiddleNodeWithSingleChild() {
		System.out.println("add more children");
		tree.insert(40);
		tree.insert(150);
		tree.printTree();
		
		boolean result = tree.delete(50);
		System.out.println("delete leftchildwithsinglechild, result:" + result);
		tree.printTree();
		assertTrue(result);
		
		result = tree.delete(200);
		System.out.println("delete rightchildwithsinglechild, result:" + result);
		tree.printTree();
		assertTrue(result);
	}
}
