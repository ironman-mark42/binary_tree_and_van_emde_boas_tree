//vEBtree source file for assignment 1 

/*************************************************************************
 *
 *  Pace University
 *  Spring 2019
 *  Advanced Algorithms
 *
 *  Course: CS 701-801
 *  Name: ISHAN ANAND
 *  Collaborators: NONE
 *  References: https://mitpress.mit.edu/books/introduction-algorithms-third-edition
 *
 *  Assignment: 01
 *  Problem: (QUESTION – 5 OF ASSIGNMENT)Write a generic class vEBtree<T> with methods Extractmax(), Insert(value, priority) and IncreaseKey(value, priority) and a constructor to create the data structure and populate it with a parametric collection of n items.

 *  Description: Implementation of Priority Queue in a generic class of vEB tree where n elements are being inserted in randomized order and m operations are being performed at random that include extractMax(), insert(value, priority) and increaseKey(value, priority), where m ≥ n.
 *
 *  Input: n elements having a specific value and a unique priority in random order.
 *  Output: a sorted Binary Heap with a loop that extracts values that have maximum priorities and displaying build times and run times respectively.

 *  Visible data fields:
 *  A Van Emde Boas tree having u nodes to store values in terms of maximum priority where n has a value of 100 to 100000 elements.
 *
 *  Visible methods:
 *  Insert(value, priority), extractMax(), IncreaseKey(value, priority)
 *
 *
 *   Remarks
 *    *
	SOLUTION TO QUESTION - 3 OF ASSIGNMENT: (5 points) Make a conjecture for the asymptotic running time of adding n entries into an initially empty Priority Queue.
    In order to find the asymptotic running time of the following empty priority queue, let us examine what are the steps before calculating the running time –
	Take an array including random priority keys.
	Build an empty vEB Tree Priority Queue where elements in the priority queue are null.

    Now, make the priority queue - Once these are done, steps to calculate the running time can be done –
	Fill the priority Queue with random keys of unordered array.

	Set priority keys in order (Maximum/ Minimum priority) known as min/max.

    Hence, in order to find the asymptotic running time of the implementation, it is important to keep in mind that –
    In worst case, O(n) calls will be made by min/max heap heapify which can take upto log(M) time. But unlike Binary heap, a recursive call can be made with universe size that takes O(1) time.
    Hence, the running time of the following implementation is O(n log log u).


	SOLUTION TO QUESTION -4 OF ASSIGNMENT:  Solution to Answer-2 of Assignment: (5 points) Make a conjecture for the amortized asymptotic running time of performing m operations chosen at random among ExtractMax(), Insert(value, priority), and IncreaseKey(value, priority), in the Priority Queue of the previous question.


    Each element when will be inserted will give the worst case running time of O(log log u) when so ever it will call a Increasekey function. This gives a worst case runtime of O(log log U). Hence, for m operations, the total running time would be O(m log log U).
 *
 *
 *
 *
 *************************************************************************/

package vEB;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class vEBtree<T extends Comparable<T>> {

	class Node {
		public int u;
		public SimpleEntry<T, T> min;
		public SimpleEntry<T, T> max;
		public Node summary;
		public Node[] cluster;

		public Node(int u) {
			this.u = u;
			min = NIL;
			max = NIL;

			initialize(u);
		}

		private void initialize(int u) {
			if (u <= 2) {
				summary = null;
				cluster = null;
			} else {
				int size = higherSquareRoot();

				summary = new Node(size);
				cluster = new vEBtree.Node[size];

				for (int i = 0; i < size; i++) {
					cluster[i] = new Node(size);
				}
			}
		}

		/**
		 * Higher Square Root
		 */
		private int higherSquareRoot() {
			return (int) Math.pow(2, Math.ceil((Math.log10(u) / Math.log10(2)) / 2));
		}
	}

	// Define NIL value to initialize min, max;
	private SimpleEntry<T, T> NIL;
	private T ONE;
	private T ZERO;
	private Node root;

	/*
	 * Construction method
	 */
	public vEBtree(int u, T NIL, T ONE, T ZERO) throws Exception {
		this.NIL = new SimpleEntry<T, T>(NIL, NIL);
		this.ONE = ONE;
		this.ZERO = ZERO;
		if (!isPowerOf2(u)) {
			throw new Exception("Tree size must be a power of 2!");
		}
		root = new Node(u);
	}

	/*
	 * Insert x
	 */
	public void insert(T value, T priority) {
		insert(root, value, priority);
	}

	/*
	 * Delete x
	 */
	public boolean decreaseKey(T value, T priority) {
		return decreaseKey(root, new SimpleEntry<T, T>(value, priority));
	}

	/*
	 * Returns the maximum value in the tree or -1 if the tree is empty.
	 */
	public SimpleEntry<T, T> extractMax() {
		SimpleEntry<T, T> max = root.max;
		decreaseKey(max.getKey(), max.getValue());
		return max;
	}

	private void insertEmptyNode(Node v, SimpleEntry<T, T> s) {
		v.min = s;
		v.max = s;
	}

	/**
	 * Insert new value to vEBTree
	 * 
	 * @param v        Root
	 * @param priority value to insert
	 */
	private void insert(Node v, T value, T priority) {
		SimpleEntry<T, T> x = new SimpleEntry<T, T>(value, priority);
		if (v.min == null) {
			insertEmptyNode(v, x);
			return;
		}

		increaseKey(v, x);

	}

	private void increaseKey(Node v, SimpleEntry<T, T> x) {
		if (compareTo(x, v.min) < 0) {
			// Exchange x with v.min
			SimpleEntry<T, T> temp = x;
			x = v.min;
			v.min = temp;
		}
		if (v.u > 2) {
			if (v.cluster[(int) high(v, x)].min == null) {
				insert(v.summary, x.getKey(), high(v, x));
				insertEmptyNode(v.cluster[(int) high(v, x)], new SimpleEntry<T, T>(x.getKey(), low(v, x)));
			} else {
				insert(v.cluster[(int) high(v, x)], x.getKey(), low(v, x));
			}
		}

		if (compareTo(x, v.max) > 0) {
			v.max = x;
		}
	}

	/**
	 * Compare 2 elements of heap
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	private int compareTo(SimpleEntry<T, T> min, SimpleEntry<T, T> max) {

		if (min.getValue().equals(max.getValue())) {
			return min.getKey().compareTo(max.getKey());
		}
		return min.getValue().compareTo(max.getValue());
	}

	/**
	 * Compare 2 elements of heap equals or not
	 * 
	 * @param x
	 * @param min
	 * @return
	 */
	private boolean equals(SimpleEntry<T, T> x, SimpleEntry<T, T> min) {
		if (x == null || min == null) {
			return false;
		}
		return x.getKey().equals(min.getKey()) && x.getValue().equals(min.getValue());
	}

	private boolean decreaseKey(Node v, SimpleEntry<T, T> x) {
		if (compareTo(v.min, v.max) == 0) {
			v.min = NIL;
			v.max = NIL;
			return false;
		}
		if (v.u == 2) {
			v.min = ZERO.equals(x.getValue()) ? new SimpleEntry<T, T>(x.getKey(), ONE)
					: new SimpleEntry<T, T>(x.getKey(), ZERO);
			v.max = v.min;
			return false;
		}
		if (!equals(x, v.min)) {
			return false;
		}

		SimpleEntry<T, T> first_cluster = v.summary.min;
		T priority = index(v, first_cluster, v.cluster[(int) first_cluster.getValue()].min);
		v.min = new SimpleEntry<T, T>(x.getValue(), priority);

		decreaseKey(v.cluster[(int) high(v, x)], new SimpleEntry<T, T>(x.getKey(), low(v, x)));
		if (v.cluster[(int) high(v, x)].min == null) {
			decreaseKey(v.summary, new SimpleEntry<T, T>(x.getKey(), high(v, x)));
			if (equals(x, v.max)) {
				SimpleEntry<T, T> summary_max = v.summary.max;
				if (summary_max == null) {
					v.max = v.min;
				} else {
					priority = index(v, summary_max, v.cluster[(int) summary_max.getValue()].max);
					v.max = new SimpleEntry<T, T>(x.getValue(), priority);
				}
			}
		} else if (equals(x, v.max)) {
			priority = index(v, new SimpleEntry<T, T>(x.getValue(), high(v, x)), v.cluster[(int) high(v, x)].max);
			v.max = new SimpleEntry<T, T>(x.getValue(), priority);
		}
		return true;

	}

	/*
	 * Returns the integer value of the first half of the bits of x.
	 */
	private T high(Node node, SimpleEntry<T, T> x) {
		return (T) new Integer((int) Math.floor((int) x.getValue() / lowerSquareRoot(node)));
	}

	/**
	 * The integer value of the second half of the bits of x.
	 */
	private T low(Node node, SimpleEntry<T, T> x) {
		return (T) new Integer((int) x.getValue() % (int) lowerSquareRoot(node));
	}

	/**
	 * The value of the least significant bits of x.
	 */
	private double lowerSquareRoot(Node node) {
		return Math.pow(2, Math.floor((Math.log10(node.u) / Math.log10(2)) / 2.0));
	}

	/**
	 * The index in the tree of the given value.
	 */
	private T index(Node node, SimpleEntry<T, T> first_cluster, SimpleEntry<T, T> min) {
		return (T) new Integer((int) ((int) first_cluster.getValue() * lowerSquareRoot(node) + (int) min.getValue()));
	}

	/**
	 * Returns true if x is a power of 2, false otherwise.
	 */
	private static boolean isPowerOf2(int x) {
		if (0 == x) {
			return false;
		}

		while (x % 2 == 0) {
			x = x / 2;
		}

		if (x > 1) {
			return false;
		}

		return true;
	}

	public static void main(String[] args) throws Exception {
		int n = 100000;
		long startTime = System.nanoTime();
		vEBtree<Integer> tree = new vEBtree<Integer>((int) Math.pow(2, 20), -1, 1, 0);
		List<Integer> values = new ArrayList<>();
		List<Integer> priorites = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			values.add(i);
			priorites.add(i);
		}
		Collections.shuffle(values);
		Collections.shuffle(priorites);
		long buildTime = System.nanoTime() - startTime;
		startTime = System.nanoTime();
		for (int i = 1; i < n; i++) {
			int value = values.get(i);
			int priority = priorites.get(i);
			tree.insert(value, priority);
			System.out.println(String.format("INSERT: value(%d), priority(%d)", value, priority));
		}

		SimpleEntry<Integer, Integer> max = tree.extractMax();
		System.out.println(String.format("EXTRACT MAX VALUE(%d), PRIORITY(%d)", max.getKey(), max.getValue()));
		System.out.println(String.format("BUILD TIME: %d ns", buildTime));
		System.out.println(String.format("RUNNING TIME: %d ns", System.nanoTime() - startTime));
	}
}