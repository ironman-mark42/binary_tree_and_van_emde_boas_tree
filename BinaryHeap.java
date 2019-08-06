package assignment1;

//Binary tree source file of assignment 1

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
 *  Problem: (QUESTION – 5 OF ASSIGNMENT)Write a generic class BinaryHeap<T> with methods Extractmax(), Insert(value, priority) and IncreaseKey(value, priority) and a constructor to create the data structure and populate it with a parametric collection of n items.

 *  Description: Implementation of Priority Queue in a generic class of Max Binary Heap where n elements are being inserted in randomized order and m operations are being performed at random that include extractMax(), insert(value, priority) and increaseKey(value, priority), where m ≥ n.
 *
 *  Input: n elements having a specific value and a unique priority in random order.
 *  Output: a sorted Binary Heap with a loop that extracts values that have maximum priorities and displaying build times and run times respectively.

 *  Visible data fields:
 *  A Binary heap having n nodes to store values in terms of maximum priority where n has a value of 100 to 100000 elements.
 *
 *  Visible methods:
 *  Insert(value, priority), extractMax(), IncreaseKey(value, priority)
 *
 *
 *   Remarks
 *  
 *
 *   PUT ALL NON-CODING ANSWERS HERE
  1.1.	 * SOLUTION TO QUESTION - 1 OF ASSIGNMENT: (5 points) Make a conjecture for the asymptotic running time of adding n entries into an initially empty Priority Queue.
  In order to find the asymptotic running time of the following empty priority queue, let us examine what are the steps before calculating the running time –
  1.	Take an array including random priority keys.
  2.	Build an empty Binary heap Priority Queue where elements in the priority queue are null.

  Now, make the priority queue - Once these are done, steps to calculate the running time can be done –
  3.	Fill the priority Queue with random keys of unordered array.

  4.	Set priority keys in order (Maximum/ Minimum priority) known as min/max heap property (Heapify).

 Hence, in order to find the asymptotic running time of the implementation, it is important to keep in mind that –
 In worst case, O(n) calls will be made by min/max heap heapify which can take upto log(n) time.
 Hence, the running time of the following implementation is O(n log n).

 1.2.	 SOLUTION TO QUESTION -2 OF ASSIGNMENT:  Solution to Answer-2 of Assignment: (5 points) Make a conjecture for the amortized asymptotic running time of performing m operations chosen at random among ExtractMax(), Insert(value, priority), and IncreaseKey(value, priority), in the Priority Queue of the previous question.
 ASSUMPTION – Let ‘x’ be the heap with ‘y’ elements in which we have to perform all the functions
 Lets try to solve this using 2 cases-
 1.	CASE 1 – When the heap ‘x’ has only one element. (‘y’ =1)
 
 2.	CASE 2 – When the heap ‘x’ has more than one element(n). (‘y’ > 1), then when the element with maximum priority is being taken out, then the structure of the priority queue will dis-orient structure in reference to property of Binary heap.
 If this is the case, then the amortized cost by potential method is –
 Cost = cy + Φ(Dy)− Φ(Dy−1)
      = O(m) 
 *
 *************************************************************************/
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.AbstractMap.SimpleEntry;

public class BinaryHeap<T extends Comparable> {
	SimpleEntry<T, T>[] A;
	int size;
	int maxsize;
	T max;
	T min;

	public BinaryHeap(int maxsize, T max, T min) {
		this.maxsize = maxsize + 1;
		this.size = 0;
		A = (SimpleEntry<T, T>[]) Array.newInstance(SimpleEntry.class, this.maxsize);
		A[0] = new SimpleEntry<T, T>(max, max);
		this.max = max;
		this.min = min;
	}

	private int parent(int i) {
		return i / 2;
	}

	private int left(int i) {
		return (2 * i);
	}

	private int right(int j) {
		return (2 * j) + 1;
	}

	protected void swap(int i, int j) {
		SimpleEntry<T, T> tmp;
		tmp = A[i];
		A[i] = A[j];
		A[j] = tmp;
	}

	protected void maxHeapify(int i) {
		int l = left(i);
		int r = right(i);
		int largest = i;
		if (l <= size && compareTo(A[l], A[i]) > 0) {
			largest = l;
		} else {
			largest = i;
		}
		if (r <= size && compareTo(A[r], A[largest]) > 0) {
			largest = r;
		}

		if (largest != i) {
			swap(i, largest);
			maxHeapify(largest);
		}
	}

	/**
	 * Compare 2 elements of heap
	 * 
	 * @param entry1
	 * @param entry2
	 * @return
	 */
	private int compareTo(SimpleEntry<T, T> entry1, SimpleEntry<T, T> entry2) {
		if (entry1.getValue().equals(entry2.getValue())) {
			return entry1.getKey().compareTo(entry2.getKey());
		}
		return entry1.getValue().compareTo(entry2.getValue());
	}

	public void insert(T key, T priority) {
		size = size + 1;
		A[size] = new SimpleEntry<T, T>(this.min, this.min);
		SimpleEntry<T, T> newVal = new SimpleEntry<T, T>(key, priority);
		increaseKey(size, newVal);
	}

	private void increaseKey(int i, SimpleEntry<T, T> key) {
		if (compareTo(key, A[i]) < 0) {
			System.err.println("New key is smaller than current key");
			return;
		}
		A[i] = key;
		while (i > 1 && compareTo(A[parent(i)], A[i]) < 0) {
			swap(i, parent(i));
			i = parent(i);
		}
	}

	/**
	 * Get and remove max element
	 * 
	 * @return
	 */
	public SimpleEntry<T, T> extractMax() {
		if (size < 1) {
			System.err.println("Heap underflow");
			return null;
		}
		SimpleEntry<T, T> max = A[1];
		A[1] = A[size];
		size -= 1;
		maxHeapify(1);
		return max;
	}

	public static void main(String[] arg) {
		
		BinaryHeap<Integer> maxHeap = new BinaryHeap<Integer>(1000000, Integer.MAX_VALUE, Integer.MIN_VALUE);
		List<Integer> values = new ArrayList<>();
		List<Integer> priorites = new ArrayList<>();
		int n = 10000;
		for (int i = 0; i < n; i++) {
			values.add(i);
			priorites.add(i);
		}
		
		Collections.shuffle(values);
		Collections.shuffle(priorites);
		long startTime = System.nanoTime();
		long insertTime = System.nanoTime() - startTime;
		long extractMax = System.nanoTime() - startTime;
		startTime = System.nanoTime();
		for (int i = 0; i < n; i++) {
			int value = values.get(i);
			int priority = priorites.get(i);
			maxHeap.insert(value, priority);
			System.out.println(String.format("INSERT: value(%d), priority(%d)", value, priority));
		}
		
		SimpleEntry<Integer, Integer> max = maxHeap.extractMax();
		System.out.println(String.format("RUNNING TIME FOR INSERT FUNCTION: %d ns", System.nanoTime() - startTime));
		System.out.println(String.format("EXTRACT MAX VALUE(%d), PRIORITY(%d)", max.getKey(), max.getValue()));
		System.out.println(String.format("TIME TO EXTRACT MAX ELEMENT: %d ns", extractMax));
		System.out.println(String.format("INCREASE KEY FOR N ELEMENTS: %d ns", insertTime));
		System.out.println(String.format("TOTAL RUNNING TIME FOR ALGORITHM: %d ns", System.nanoTime() - startTime + extractMax + insertTime));

	}
}

