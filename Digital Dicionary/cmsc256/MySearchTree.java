package cmsc256;
/**
 *   CMSC 256
 *   Computer Science Department
 *   College of Engineering
 *   Virginia Commonwealth University
 */
/**Braeden Ferguson
 * 07/06/2023
 * Project 7: Digital Dictionary
 * observes hashing algorithms and observing runtimes of both Hashmap and search tree iterators
 */

import bridges.base.BSTElement;
import bridges.connect.Bridges;

import java.lang.Comparable;
import java.security.Key;
import java.util.*;

public class MySearchTree<K extends Comparable<? super K>, V> implements java.lang.Iterable<Map.Entry<K, V>> {
	private BSTElement<K, V> root;

	public MySearchTree() {
		root = null;
	}

	///TODO: Implement iterator logic for MySearchTree
	class SearchTreeIterator implements Iterator<Map.Entry<K, V>> {
		private Stack<BSTElement<K, V>> treeStack;

		SearchTreeIterator(BSTElement<K, V> node) {
			treeStack = new Stack<>();
			treePushing(root);
		}

		public boolean hasNext() {
			return !treeStack.empty();
		}

		public Map.Entry<K, V> next() throws NoSuchElementException {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			BSTElement<K, V> nextNode = treeStack.pop();
			treeStack.push(nextNode.getRight());
			return new AbstractMap.SimpleEntry<K, V>(nextNode.getKey(), nextNode.getValue());
		}

		public void treePushing(BSTElement<K, V> node) {
			while (node != null) {
				treeStack.push(node);
				node = node.getLeft();
			}
		}
	}

	public Iterator<Map.Entry<K, V>> iterator() {
		return new SearchTreeIterator(root);
	}

	//TODO:
	public V get(K k) {
		if (k == null) {
			return null;
		}

		BSTElement<K, V> currentNode = root;
		while (currentNode != null) {

			if (currentNode.getKey().compareTo(k) < 0) {
				currentNode = currentNode.getRight();
			} else if (currentNode.getKey().compareTo(k) > 0) {
				currentNode = currentNode.getLeft();
			} else {
				return currentNode.getValue();
			}
		}
		return null;
	}

	//TODO:
	public void set(K k, V e) throws IllegalArgumentException {
		if (k == null) {
			throw new IllegalArgumentException();
		}

		root = recursiveSet(root, k, e);
	}

	private BSTElement<K, V> recursiveSet(BSTElement<K, V> node, K k, V e) {
		if (node == null) {
			return new BSTElement<>(k, e);
		}


		if (node.getKey().compareTo(k) < 0) {
			node.setRight(recursiveSet(node.getRight(), k, e));
		} else if (node.getKey().compareTo(k) > 0) {
			node.setLeft(recursiveSet(node.getLeft(), k, e));
		} else {
			node.setValue(e);
		}

		return node;

	}

	///visualization function
	public void visualize(Bridges bridges) {
		if (root != null) {
			bridges.setDataStructure(root);
			try {
				bridges.visualize();
			} catch (Exception e) {
				System.err.println("Exception :" + e.getMessage());
			}
		}
	}




			public static void main(String[] args) {
				MySearchTree<String, Integer> tree = new MySearchTree<>();

				// Test empty tree


				try {
					// Test getting value for non-existent key
					Integer value = tree.get("key");
					System.out.println("Value for 'key': " + value); // Expected output: null
				} catch (Exception e) {
					System.out.println("Exception occurred: " + e.getMessage());
				}

				try {
					// Test setting a key-value pair
					tree.set("key1", 1);
					tree.set("key2", 2);
					tree.set("key3", 3);


					// Test getting values
					System.out.println("Value for 'key1': " + tree.get("key1")); // Expected output: 1
					System.out.println("Value for 'key2': " + tree.get("key2")); // Expected output: 2
					System.out.println("Value for 'key3': " + tree.get("key3")); // Expected output: 3

					// Test setting a new value for an existing key
					tree.set("key2", 20);
					System.out.println("Value for 'key2': " + tree.get("key2")); // Expected output: 20
				} catch (Exception e) {
					System.out.println("Exception occurred: " + e.getMessage());
				}

				try {
					// Test setting null key
					tree.set(null, 10); // Expected output: IllegalArgumentException
				} catch (IllegalArgumentException e) {
					System.out.println("IllegalArgumentException occurred: " + e.getMessage());
				}

				try {
					// Test setting null value
					tree.set("key4", null); // Expected output: IllegalArgumentException
				} catch (IllegalArgumentException e) {
					System.out.println("IllegalArgumentException occurred: " + e.getMessage());
				}
			}
		}




