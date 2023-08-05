package cmsc256;
/**
 *   CMSC 256
 *   Computer Science Department
 *   College of Engineering
 *   Virginia Commonwealth University
 */
import bridges.base.Text;
import bridges.base.Polyline;
import bridges.base.Rectangle;
import bridges.base.SymbolCollection;
import bridges.connect.Bridges;
import bridges.validation.RateLimitException;

import java.util.*;
import java.lang.Comparable;
import java.io.IOException;

public class MyHashTable<K, V> implements java.lang.Iterable<Map.Entry<K, V>>, Dictionary<K,V> {
	private MapNode[] table;
	private int capacity;
	private double loadFactor;
	private int count;
	private final static int defaultCapacity = 30;
	private final static double defaultLoadFactor = 10.0;

	//	Default constructor uses the default values for both
//	capacity and load factor to create the hash table
	public MyHashTable() {
		this(defaultCapacity, defaultLoadFactor);
	}

	//	Single argument constructor uses the default values
//	for the load factor to create the hash table
	public MyHashTable(int capacity) {
		this(capacity, defaultLoadFactor);
	}

	public MyHashTable(int capacity, double loadFactor) {
		this.loadFactor = loadFactor;
		this.capacity = capacity;
		this.table = new MapNode[this.capacity];
		this.count = 0;
	}

	//TODO: implement iterator logic by providing any missing code needed
	/*
	 * Provides the implementation of the Iterator interface
	 * for the hash table
	 * Note: the optional remove method is not implemented
	 * https://docs.oracle.com/javase/8/docs/api/java/util/Iterator.html
	 */
	private class HashTableIterator implements Iterator<Map.Entry<K, V>> {
		private int currentIndex;

		//TODO: complete constructor implementation
		public HashTableIterator() {
			currentIndex = 0;
		}

		//TODO: return true if there is another entry in the table
		public boolean hasNext() {
			while (currentIndex < capacity && table[currentIndex] == null) {
				currentIndex++;
			}
			return currentIndex < capacity;
		}

		//TODO: returns the next entry in the table
		public Map.Entry<K, V> next() throws NoSuchElementException {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}

			MapNode<K, V> currentNode = table[currentIndex];
			currentIndex++; // Move to the next index for the next call to `next()`

			return new AbstractMap.SimpleEntry<K, V> (currentNode.getKey(), currentNode.getValue());
		}
	}


	public Iterator<Map.Entry<K, V>> iterator() {
		return new HashTableIterator();
	}

	//TODO:
	public V get(K key) {
		HashTableIterator iterator = new HashTableIterator();

		while (iterator.hasNext()) {
			Map.Entry<K,V> currentEntry = iterator.next();
			if (currentEntry.getKey().equals(key)) {
				return currentEntry.getValue();
			}
		}
		return null;
	}

	//TODO
	public void set(K key, V value) throws IllegalArgumentException {
		if (key == null || value == null) {
			throw new IllegalArgumentException();
		}
		int index = getIndex(key, capacity);
		MapNode<K, V> currentNode = table[index];
		while (currentNode != null) {
			if (currentNode.getKey().equals(key)) {
				currentNode.setValue(value);
				return;
			}
			currentNode = currentNode.getNext();
		}

		// Key doesn't exist, new node  at the end
		MapNode<K, V> newNode = new MapNode<>(key, value);
		if (table[index] == null) {
			table[index] = newNode;
		} else {
			currentNode = table[index];
			while (currentNode.getNext() != null) {
				currentNode = currentNode.getNext();
			}
			currentNode.setNext(newNode);
		}

		count++;

		// Check if resizing is needed
		if ((double) count / capacity >= loadFactor) {
			resize(capacity * 2);
		}
	}



	//TODO
	private void resize(int capacity) {
		MapNode<K, V>[] newTable = new MapNode[capacity];

		// Rehash all existing entries into the new table
		for (Map.Entry<K, V> entry : this) {
			K key = entry.getKey();
			V value = entry.getValue();
			int newIndex = getIndex(key, capacity);

			MapNode<K, V> newNode = new MapNode<>(key, value);
			if (newTable[newIndex] == null) {
				newTable[newIndex] = newNode;
			} else {
				MapNode<K, V> current = newTable[newIndex];
				while (current.getNext() != null) {
					current = current.getNext();
				}
				current.setNext(newNode);
			}
		}

		table = newTable;
		this.capacity = capacity;
		}
	public int getIndex(K k, int capacity) {
		return Math.abs(k.hashCode() % capacity);
	}

	//visualization function
	public void visualize(Bridges bridgesInstance) throws IOException, RateLimitException {
		SymbolCollection vis = new SymbolCollection();
		Rectangle rect;
		Text label;
		Polyline line;

		int maxx = 0;
		float label_width = 100;
		float label_height = 25;
		float spacing_width = 50;


		for (int i = 0; i < this.table.length; ++i) {
			int x = 0;
			int y = (this.capacity - i) * 30;

			rect = new Rectangle(x, y, 25, 25);
			rect.setFillColor("white");
			vis.addSymbol(rect);

			label = new Text(String.valueOf(i));
			label.setAnchorLocation((float) (x + 25 / 2.), (float) (y + 25 / 2.));
			label.setFontSize(12);
			vis.addSymbol(label);

			x += 62.5;

			MapNode node = this.table[i];
			while (node != null) {
				rect = new Rectangle(x, y, label_width, label_height);
				rect.setFillColor("white");
				vis.addSymbol(rect);

				label = new Text(String.format("%s: %d", node.getKey(), node.getValue()));
				label.setAnchorLocation((float) (x + label_width / 2.), (float) (y + label_height / 2.));
				label.setFontSize(12);
				vis.addSymbol(label);

				line = new Polyline();
				line.addPoint((float) (x + label_width), (float) (y + label_height / 2.));
				line.addPoint((float) (x + label_width + spacing_width), (float) (y + label_height / 2.));
				line.addPoint((float) (x + label_width + 3. / 4. * spacing_width), (float) (y));
				line.addPoint((float) (x + label_width + spacing_width), (float) (y + label_height / 2.));
				line.addPoint((float) (x + label_width + 3. / 4. * spacing_width), (float) (y + label_height));
				line.setStrokeWidth(1);
				line.setStrokeColor("red");
				vis.addSymbol(line);

				x += label_width + spacing_width;
				node = node.getNext();
			}

			if (x > maxx)
				maxx = x;

			rect = new Rectangle(x, y, label_width, label_height);
			rect.setFillColor("white");
			vis.addSymbol(rect);

			label = new Text("null");
			label.setAnchorLocation((float) (x + label_width / 2.), (float) (y + label_height / 2.));
			label.setFontSize(12);
			vis.addSymbol(label);
		}

		vis.setViewport(-30.f, (float) (maxx + 200), -30.f, (this.capacity + 1) * 30.f);
		bridgesInstance.setDataStructure(vis);
		bridgesInstance.visualize();
	}


	class MapNode<K, V> {
		private K key;
		private V value;
		private MapNode next;

		public MapNode(K key, V value) {
			this.key = key;
			this.value = value;
			this.next = null;
		}

		public K getKey() {
			return key;
		}

		public void setKey(K key) {
			this.key = key;
		}

		public V getValue() {
			return value;
		}

		public void setValue(V value) {
			this.value = value;
		}

		public MapNode getNext() {
			return next;
		}

		public void setNext(MapNode next) {
			this.next = next;
		}
	}
	public static void main(String[] args) {
		MyHashTable<String, Integer> table = new MyHashTable<>();

		//Inserting and retrieving values
		table.set("apple", 3);
		table.set("banana", 5);
		table.set("cherry", 2);

		System.out.println("Get apple: " + table.get("apple"));       // Output: 3
		System.out.println("Get banana: " + table.get("banana"));     // Output: 5
		System.out.println("Get cherry: " + table.get("cherry"));     // Output: 2
		System.out.println("Get orange: " + table.get("orange"));     // Output: null

		//Updating existing value
		table.set("apple", 7);
		System.out.println("Get apple: " + table.get("apple"));       // Output: 7


		//non-existent key
		System.out.println("Get grape: " + table.get("grape"));       // return null

		//empty key
		table.set("", 1);
		System.out.println("Get empty key: " + table.get(""));       // Output: 1



		MyHashTable<String, Integer> table2 = new MyHashTable<>();

		// Insert some key-value pairs
		table.set("apple", 3);
		table.set("banana", 5);
		table.set("cherry", 2);

		// Test inner class iterator
		System.out.println("Iterating through MyHashTable using inner class iterator:");
		MyHashTable<String, Integer>.HashTableIterator iterator2 = table2.new HashTableIterator();
		while (iterator2.hasNext()) {
			Map.Entry<String, Integer> entry = iterator2.next();
			System.out.println("Key: " + entry.getKey().toString() + ", Value: " + entry.getValue().toString());
		}
	}

}
