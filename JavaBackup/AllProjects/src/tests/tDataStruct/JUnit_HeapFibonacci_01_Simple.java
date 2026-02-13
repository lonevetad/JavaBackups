package tests.tDataStruct;

import java.util.Comparator;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import dataStructures.HeapFibonacci;
import junit.framework.TestCase;
import tests.tDataStruct.JUnit_HeapFibonacci_01_Simple.HF_JUnit.NHF_JUnit;
import tools.Comparators;

public class JUnit_HeapFibonacci_01_Simple extends TestCase {
	static Integer[] sequenceToInsert_Random = null;

	@Override
	protected synchronized void setUp() {
		if (sequenceToInsert_Random == null)
			sequenceToInsert_Random = new Integer[] { 5, 2, 8, 3, 4, 6, 1 };
	}

	@Override
	protected void tearDown() {
		sequenceToInsert_Random = null;
	}

	// TODO HELPERS

	@SuppressWarnings("rawtypes")
	protected static NHF_JUnit[] addAndFill(HF_JUnit<Integer> h, int size) {
		NHF_JUnit[] nodes;
		nodes = new NHF_JUnit[size];
		for (int i = 0; i < size; i++) {
			nodes[i] = (NHF_JUnit<Integer>) h.insert(sequenceToInsert_Random[i]);
		}
		return nodes;
	}

	protected static HF_JUnit<Integer> newHeap(boolean nodeSearchPolicy) {
		return new HF_JUnit<Integer>(Comparators.INTEGER_COMPARATOR, nodeSearchPolicy);
	}

	// TODO TESTS

	// TODO : convertire tutto in test che hanno per parametro un heap, così da non
	// duplicarli

	@Test
	public void test_Add_OneElement(final HF_JUnit<Integer> h) {
		NHF_JUnit<Integer> node;

		setUp();
		Objects.requireNonNull(sequenceToInsert_Random);

		node = (NHF_JUnit<Integer>) h.insert(sequenceToInsert_Random[0]);

		assertEquals(1, h.size());

		assertNotNull("the root should not be null", h.getRoot());
		assertEquals(sequenceToInsert_Random[0], h.getRoot().getData());
		assertEquals(node, h.getRoot());

		assertNotNull("the min should not be null", h.getMinNode());
		assertEquals(sequenceToInsert_Random[0], h.getMin());
		assertEquals(node, h.getMinNode());

		assertEquals(h.getRoot(), h.getMinNode());

		assertEquals(node, node.getNext());
		assertEquals(node, node.getPrevious());
		assertNull(node.getFather());
		assertEquals(0, node.getChildrenAmount());
		assertNull(node.getLastChild());
	}

	@Test
	public void test_CleanAfter_Add_One(final HF_JUnit<Integer> h) {
		// assumption: testAdd_Two_Increasing gives no error
		NHF_JUnit<Integer> firstNode;

		setUp();
		Objects.requireNonNull(sequenceToInsert_Random);

		firstNode = (NHF_JUnit<Integer>) h.insert(sequenceToInsert_Random[1]); // 2

		h.cleanUp();

		assertEquals(firstNode, firstNode.getNext());
		assertEquals(firstNode, firstNode.getPrevious());

		assertEquals(0, firstNode.getChildrenAmount());
		assertNull(firstNode.getFather());
		assertNull(firstNode.getLastChild());
	}

	@Test
	public void test_Add_Two_Increasing(final HF_JUnit<Integer> h) {
		NHF_JUnit<Integer> firstNode, secondNode;

		setUp();
		Objects.requireNonNull(sequenceToInsert_Random);

		firstNode = (NHF_JUnit<Integer>) h.insert(sequenceToInsert_Random[1]); // 2
		secondNode = (NHF_JUnit<Integer>) h.insert(sequenceToInsert_Random[0]); // 5

		assertEquals(2, h.size());

		assertNotNull("the root should not be null", h.getRoot());
		assertEquals(sequenceToInsert_Random[0], h.getRoot().getData());
		assertEquals(secondNode, h.getRoot());

		assertNotNull("the min should not be null", h.getMinNode());
		assertEquals(sequenceToInsert_Random[1], h.getMin());
		assertEquals(firstNode, h.getMinNode());

		assertEquals(secondNode, firstNode.getNext());
		assertEquals(secondNode, firstNode.getPrevious());
		assertNull(firstNode.getFather());
		assertEquals(0, firstNode.getChildrenAmount());
		assertNull(firstNode.getLastChild());

		assertEquals(firstNode, secondNode.getNext());
		assertEquals(firstNode, secondNode.getPrevious());
		assertNull(secondNode.getFather());
		assertEquals(0, secondNode.getChildrenAmount());
		assertNull(secondNode.getLastChild());
	}

	@Test
	public void test_Add_Two_Decreasing(final HF_JUnit<Integer> h) {
		NHF_JUnit<Integer> firstNode, secondNode;

		setUp();
		Objects.requireNonNull(sequenceToInsert_Random);

		firstNode = (NHF_JUnit<Integer>) h.insert(sequenceToInsert_Random[0]); // 5
		secondNode = (NHF_JUnit<Integer>) h.insert(sequenceToInsert_Random[1]); // 2

		assertEquals(2, h.size());

		assertNotNull("the root should not be null", h.getRoot());
		assertEquals(sequenceToInsert_Random[1], h.getRoot().getData());
		assertEquals(secondNode, h.getRoot());

		assertNotNull("the min should not be null", h.getMinNode());
		assertEquals(sequenceToInsert_Random[1], h.getMin());
		assertEquals(secondNode, h.getMinNode());

		assertEquals(secondNode, firstNode.getNext());
		assertEquals(secondNode, firstNode.getPrevious());
		assertNull(firstNode.getFather());
		assertEquals(0, firstNode.getChildrenAmount());
		assertNull(firstNode.getLastChild());

		assertEquals(firstNode, secondNode.getNext());
		assertEquals(firstNode, secondNode.getPrevious());
		assertNull(secondNode.getFather());
		assertEquals(0, secondNode.getChildrenAmount());
		assertNull(secondNode.getLastChild());
	}

	@Test
	public void test_CleanAfter_Add_Two_Increasing(final HF_JUnit<Integer> h) {
		// assumption: testAdd_Two_Increasing gives no error
		NHF_JUnit<Integer> firstNode, secondNode;

		setUp();
		Objects.requireNonNull(sequenceToInsert_Random);

		firstNode = (NHF_JUnit<Integer>) h.insert(sequenceToInsert_Random[1]); // 2
		secondNode = (NHF_JUnit<Integer>) h.insert(sequenceToInsert_Random[0]); // 5

		h.cleanUp();

		assertEquals(firstNode, firstNode.getNext());
		assertEquals(firstNode, firstNode.getPrevious());

		assertEquals(secondNode, secondNode.getNext());
		assertEquals(secondNode, secondNode.getPrevious());

		assertEquals(1, firstNode.getChildrenAmount());
		assertEquals(0, secondNode.getChildrenAmount());
		assertNull(firstNode.getFather());
		assertEquals(firstNode, secondNode.getFather());
		assertEquals(secondNode, firstNode.getLastChild());
	}

	@Test
	public void test_CleanAfter_Add_Two_Decreasing(final HF_JUnit<Integer> h) {
		// assumption: testAdd_Two_Increasing gives no error
		NHF_JUnit<Integer> firstNode, secondNode;

		setUp();
		Objects.requireNonNull(sequenceToInsert_Random);

		firstNode = (NHF_JUnit<Integer>) h.insert(sequenceToInsert_Random[0]); // 5
		secondNode = (NHF_JUnit<Integer>) h.insert(sequenceToInsert_Random[1]); // 2

		h.cleanUp();

		assertEquals(firstNode, firstNode.getNext());
		assertEquals(firstNode, firstNode.getPrevious());

		assertEquals(secondNode, secondNode.getNext());
		assertEquals(secondNode, secondNode.getPrevious());

		assertEquals(0, firstNode.getChildrenAmount());
		assertEquals(1, secondNode.getChildrenAmount());
		assertEquals(secondNode, firstNode.getFather());
		assertNull(secondNode.getFather());
		assertEquals(firstNode, secondNode.getLastChild());
	}

	@Test
	public void test_Add_Three(final HF_JUnit<Integer> h) {
		NHF_JUnit<Integer> n1, n2, n3;

		setUp();
		Objects.requireNonNull(sequenceToInsert_Random);

		n1 = (NHF_JUnit<Integer>) h.insert(sequenceToInsert_Random[0]); // 5
		n2 = (NHF_JUnit<Integer>) h.insert(sequenceToInsert_Random[1]); // 2
		n3 = (NHF_JUnit<Integer>) h.insert(sequenceToInsert_Random[2]); // 8

		assertEquals(3, h.size());

		assertNotNull("the root should not be null", h.getRoot());
		assertEquals(sequenceToInsert_Random[2], h.getRoot().getData());
		assertEquals(n3, h.getRoot());

		assertNotNull("the min should not be null", h.getMinNode());
		assertEquals(sequenceToInsert_Random[1], h.getMin());
		assertEquals(n2, h.getMinNode());

		assertEquals(n2, n1.getNext());
		assertEquals(n3, n1.getPrevious());
		assertNull(n1.getFather());
		assertEquals(0, n1.getChildrenAmount());
		assertNull(n1.getLastChild());

		assertEquals(n3, n2.getNext());
		assertEquals(n1, n2.getPrevious());
		assertNull(n2.getFather());
		assertEquals(0, n2.getChildrenAmount());
		assertNull(n2.getLastChild());

		assertEquals(n1, n3.getNext());
		assertEquals(n2, n3.getPrevious());
		assertNull(n1.getFather());
		assertEquals(0, n3.getChildrenAmount());
		assertNull(n3.getLastChild());
	}

	//

	public void test_ExtractMin_Three_AlreadyCleandUp(final HF_JUnit<Integer> h) {
		Integer removed, data1, data2, data3;
		NHF_JUnit<Integer> n1, n2, n3;

		data1 = 7;
		data2 = 9;
		data3 = 2;

		n1 = (NHF_JUnit<Integer>) h.newNode(data1);
		n2 = (NHF_JUnit<Integer>) h.newNode(data2);
		n3 = (NHF_JUnit<Integer>) h.newNode(data3);
		n3.addChildPublic(n2);
		n1.linkAfterPublic(n3);
		h.setRootPublic(n3);
		h.setMinPublic(n3);
		h.setSize(3);

		assertEquals(data3, h.getMin());
		assertEquals(n1, h.getRoot().getNext());

		removed = h.extractMin();
		assertEquals(data3, removed);

		assertEquals(data1, h.getMin());
		assertEquals(2, h.size());
		assertNotNull(h.getRoot());

//		assertEquals(n1, n2.getNext()); // without cleanup, this should be the result
//		assertEquals(n2, n1.getNext()); // without cleanup, this should be the result
		assertEquals(n1, n1.getNext());
		assertEquals(n2, n2.getNext());
		assertEquals(n3, n3.getNext());
		assertNull(n3.getLastChild());
		assertNull(n2.getFather());
		assertEquals(n1, h.getRoot());
		assertEquals(n1, n2.getFather());
		assertEquals(n2, n1.getLastChild());
	}

	//

	// TODO linear search

	@Test
	public void test_LinearSearch_Add_OneElement() {
		test_Add_OneElement(newHeap(false));
	}

	@Test
	public void test_LinearSearch_CleanAfter_Add_One() {
		test_CleanAfter_Add_One(newHeap(false));
	}

	@Test
	public void test_LinearSearch_Add_Two_Increasing() {
		test_Add_Two_Increasing(newHeap(false));
	}

	@Test
	public void test_LinearSearch_Add_Two_Decreasing() {
		test_CleanAfter_Add_Two_Increasing(newHeap(false));
	}

	@Test
	public void test_LinearSearch_CleanAfter_Add_Two_Increasing() {
		test_CleanAfter_Add_Two_Increasing(newHeap(false));
	}

	@Test
	public void test_LinearSearch_CleanAfter_Add_Two_Decreasing() {
		test_CleanAfter_Add_Two_Decreasing(newHeap(false));
	}

	@Test
	public void test_LinearSearch_Add_Three() {
		test_Add_Three(newHeap(false));
	}

	@Test
	public void test_LinearSearch_CleanAfter_Add_Three() {
		HF_JUnit<Integer> h;
		NHF_JUnit<Integer> n1, n2, n3;
		setUp();

		h = newHeap(false);

		Objects.requireNonNull(sequenceToInsert_Random);

		n1 = (NHF_JUnit<Integer>) h.insert(sequenceToInsert_Random[0]); // 5
		n2 = (NHF_JUnit<Integer>) h.insert(sequenceToInsert_Random[1]); // 2
		n3 = (NHF_JUnit<Integer>) h.insert(sequenceToInsert_Random[2]); // 8

		assertEquals(3, h.size());

		h.cleanUp();
		assertEquals(3, h.size());

		// roots:
		// 8 .. 2
		// .... |
		// .... 5

		assertEquals(n1, n1.getNext());
		assertEquals(n1, n1.getPrevious());
		assertEquals(n3, n2.getNext());
		assertEquals(n3, n2.getPrevious());
		assertEquals(n2, n3.getNext());
		assertEquals(n2, n3.getPrevious());

		assertNull(n2.getFather());
		assertNull(n3.getFather());
		assertEquals(n2, n1.getFather());

		// should have:
		// at first, "n3" with "8" as data and as a singletone
		// at second, "n" with "2" as data, "5" as child, roots == n2 and min == "2"

		assertEquals(n2.getData(), h.getRoot().getData());
		assertEquals(n2, h.getRoot());
		assertEquals(n2.getData(), h.getMin());

		assertEquals(0, n1.getChildrenAmount());
		assertEquals(1, n2.getChildrenAmount());
		assertEquals(0, n3.getChildrenAmount());

		assertNull(n1.getLastChild());
		assertEquals(n1, n2.getLastChild());
		assertNull(n3.getLastChild());

		assertEquals(n2, n1.getFather());
		assertNull(n2.getFather());
		assertNull(n3.getFather());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_LinearSearch_Add_Four() {
		int size;
		HF_JUnit<Integer> h;
		NHF_JUnit<Integer> n, nodes[];

		setUp();

		h = newHeap(false);

		Objects.requireNonNull(sequenceToInsert_Random);

		size = 4;
		nodes = addAndFill(h, size);

		assertEquals(size, h.size());

		assertNotNull("the root should not be null", h.getRoot());
		assertEquals(sequenceToInsert_Random[size - 1], h.getRoot().getData());
		assertEquals(nodes[size - 1], h.getRoot());

		assertNotNull("the min should not be null", h.getMinNode());
		assertEquals(sequenceToInsert_Random[1], h.getMin());
		assertEquals(nodes[1], h.getMinNode());

		for (int i = 0; i < size; i++) {
			int nextIndex, prevIndex;
			nextIndex = (i + 1) % size;
			prevIndex = i - 1;
			if (prevIndex < 0) {
				prevIndex = size - 1;
			}
			n = nodes[i];
			assertEquals(nodes[nextIndex], n.getNext());
			assertEquals(nodes[prevIndex], n.getPrevious());
			assertNull(n.getFather());
			assertEquals(0, n.getChildrenAmount());
			assertNull(n.getLastChild());
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_LinearSearch_CleanAfter_Add_Four() {
		int size;
		HF_JUnit<Integer> h;
		NHF_JUnit<Integer> n, nodes[];

		setUp();

		h = newHeap(false);

		Objects.requireNonNull(sequenceToInsert_Random);

		size = 4;
		nodes = addAndFill(h, size); // 5, 2, 8, 3

		h.cleanUp();
		assertEquals(size, h.size());

		// roots:
		// . 2
		// ./ \
		// 5 . 3
		// ... |
		// ... 8
		assertEquals(nodes[1], h.getRoot());
		assertEquals(nodes[1], h.getMinNode());
		assertEquals(sequenceToInsert_Random[1], h.getMin());

		n = nodes[1];
		assertEquals(n, n.getNext());
		assertEquals(n, n.getPrevious());
		assertEquals(nodes[3], nodes[0].getNext());
		assertEquals(nodes[3], nodes[0].getPrevious());
		assertEquals(nodes[0], nodes[3].getNext());
		assertEquals(nodes[0], nodes[3].getPrevious());
		assertEquals(nodes[2], nodes[2].getNext());
		assertEquals(nodes[2], nodes[2].getPrevious());

		assertNull(n.getFather());
		assertEquals(n, nodes[0].getFather());
		assertEquals(n, nodes[3].getFather());
		assertEquals(nodes[3], nodes[2].getFather());

		assertEquals(2, n.getChildrenAmount());
		assertEquals(0, nodes[0].getChildrenAmount());
		assertEquals(1, nodes[3].getChildrenAmount());
		assertEquals(0, nodes[2].getChildrenAmount());

		assertEquals(nodes[3], n.getLastChild());
		assertEquals(nodes[0], n.getLastChild().getNext());
		assertNull(nodes[0].getLastChild());
		assertEquals(nodes[2], nodes[3].getLastChild());
		assertNull(nodes[2].getLastChild());
	}

	//

	@Test
	public void test_LinearSearch_NodeOf_Empty() {
		HF_JUnit<Integer> h;

		setUp();

		h = newHeap(false);

		assertNull(h.nodeOfPublic(5));
	}

	@Test
	public void test_LinearSearch_NodeOf_OnePresent() {
		HF_JUnit<Integer> h;
		NHF_JUnit<Integer> n;

		setUp();

		h = newHeap(false);

		n = (NHF_JUnit<Integer>) h.insert(7);

		assertEquals(n, h.nodeOfPublic(n.getData()));
	}

	@Test
	public void test_LinearSearch_NodeOf_OneAbsent() {
		Integer data;
		HF_JUnit<Integer> h;

		setUp();

		h = newHeap(false);

		data = 7;
		h.insert(data);

		assertNull(h.nodeOfPublic(data + 1));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_LinearSearch_NodeOf_TwoRootsPresent_First() {
		int size;
		HF_JUnit<Integer> h;
		NHF_JUnit<Integer> nodes[];

		setUp();

		h = newHeap(false);

		size = 2;
		nodes = addAndFill(h, size); // 5, 2,

		// h.roots == 2
		assertEquals(sequenceToInsert_Random[1], h.getRoot().getData());

		assertEquals(nodes[0], h.nodeOfPublic(sequenceToInsert_Random[0]));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_LinearSearch_NodeOf_TwoRootsPresent_Last() {
		int size;
		HF_JUnit<Integer> h;
		NHF_JUnit<Integer> nodes[];

		setUp();

		h = newHeap(false);

		size = 2;
		nodes = addAndFill(h, size);

		// h.roots == 2
		assertEquals(sequenceToInsert_Random[1], h.getRoot().getData());

		assertEquals(nodes[1], h.nodeOfPublic(sequenceToInsert_Random[1]));
	}

	@Test
	public void test_LinearSearch_NodeOf_TwoRootsAbsent() {
		int size;
		HF_JUnit<Integer> h;
		Integer dataNotPresent;

		setUp();

		h = newHeap(false);

		size = 2;
		addAndFill(h, size);

		// h.roots == 2
		assertEquals(sequenceToInsert_Random[1], h.getRoot().getData());

		dataNotPresent = 404;
		assertNull(h.nodeOfPublic(dataNotPresent));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_LinearSearch_NodeOf_TwoInColumnPresent_Root() {
		int size;
		HF_JUnit<Integer> h;
		NHF_JUnit<Integer> n;
		NHF_JUnit<Integer> nodes[];

		setUp();

		h = newHeap(false);

		size = 2;
		nodes = addAndFill(h, size);

		// h.roots == 2
		assertEquals(sequenceToInsert_Random[1], h.getRoot().getData());
		assertEquals(sequenceToInsert_Random[0], h.getRoot().getNext().getData());

		n = nodes[0];
		n.unlinkHorizPublic();
		h.getRoot().addChildPublic(n);

		assertEquals(1, h.getRoot().getChildrenAmount());
		assertEquals(0, n.getChildrenAmount());
		assertEquals(h.getRoot(), n.getFather());
		assertEquals(n, h.getRoot().getLastChild());

		assertEquals(h.getRoot(), h.nodeOfPublic(h.getRoot().getData()));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_LinearSearch_NodeOf_TwoInColumnPresent_Child() {
		int size;
		HF_JUnit<Integer> h;
		NHF_JUnit<Integer> n;
		NHF_JUnit<Integer> nodes[];

		setUp();

		h = newHeap(false);

		size = 2;
		nodes = addAndFill(h, size);

		// h.roots == 2
		assertEquals(sequenceToInsert_Random[1], h.getRoot().getData());
		assertEquals(sequenceToInsert_Random[0], h.getRoot().getNext().getData());

		n = nodes[0];
		n.unlinkHorizPublic();
		h.getRoot().addChildPublic(n);

		assertEquals(1, h.getRoot().getChildrenAmount());
		assertEquals(0, n.getChildrenAmount());
		assertEquals(h.getRoot(), n.getFather());
		assertEquals(n, h.getRoot().getLastChild());

		assertEquals(n, h.nodeOfPublic(h.getRoot().getLastChild().getData()));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_LinearSearch_NodeOf_TwoInColumnAbsent() {
		int size;
		HF_JUnit<Integer> h;
		NHF_JUnit<Integer> n;
		NHF_JUnit<Integer> nodes[];
		Integer dataNotPresent;

		setUp();

		h = newHeap(false);

		size = 2;
		nodes = addAndFill(h, size);

		// h.roots == 2
		assertEquals(sequenceToInsert_Random[1], h.getRoot().getData());
		assertEquals(sequenceToInsert_Random[0], h.getRoot().getNext().getData());

		n = nodes[0];
		n.unlinkHorizPublic();
		h.getRoot().addChildPublic(n);

		assertEquals(1, h.getRoot().getChildrenAmount());
		assertEquals(0, n.getChildrenAmount());
		assertEquals(h.getRoot(), n.getFather());
		assertEquals(n, h.getRoot().getLastChild());

		dataNotPresent = 404;
		assertNull(h.nodeOfPublic(dataNotPresent));
	}

	// 3

	@SuppressWarnings("unchecked")
	@Test
	public void test_LinearSearch_NodeOf_Three_TwoRoots_LoneRoot() {
		int size;
		HF_JUnit<Integer> h;
		NHF_JUnit<Integer> n;
		NHF_JUnit<Integer> nodes[];

		setUp();

		h = newHeap(false);

		size = 3;
		nodes = addAndFill(h, size); // 5, 2, 8

		h.cleanUp();
		// 8 ; 2->5

		n = h.nodeOfPublic(sequenceToInsert_Random[2]);
		assertNotNull(n);
		assertEquals(nodes[2], n);
		assertEquals(sequenceToInsert_Random[2], n.getData());
	}

	@Test
	public void test_LinearSearch_NodeOf_Three_TwoRoots_ColumnRootRoot() {
		int size;
		HF_JUnit<Integer> h;
		NHF_JUnit<Integer> n;

		setUp();

		h = newHeap(false);

		size = 3;
		addAndFill(h, size); // 5, 2, 8

		h.cleanUp();
		// 8 ; 2->5

		n = h.nodeOfPublic(sequenceToInsert_Random[1]);
		assertNotNull(n);
		assertEquals(h.getRoot(), n);
		assertEquals(h.getRoot().getData(), n.getData());
	}

	@Test
	public void test_LinearSearch_NodeOf_Three_TwoRoots_ColumnRootChild() {
		int size;
		HF_JUnit<Integer> h;
		NHF_JUnit<Integer> n;

		setUp();

		h = newHeap(false);

		size = 3;
		addAndFill(h, size); // 5, 2, 8

		h.cleanUp();
		// 8 ; 2->5

		n = h.nodeOfPublic(sequenceToInsert_Random[0]);
		assertNotNull(n);
		assertNotNull(h.getRoot().getLastChild());
		assertEquals(h.getRoot().getLastChild(), n);
		assertEquals(h.getRoot().getLastChild().getData(), n.getData());
	}

	@Test
	public void test_LinearSearch_NodeOf_Three_TwoRoots_Absent() {
		int size;
		HF_JUnit<Integer> h;
		NHF_JUnit<Integer> n;
		Integer notPresent;

		setUp();

		h = newHeap(false);

		size = 3;
		addAndFill(h, size); // 5, 2, 8

		h.cleanUp();
		// 8 ; 2->5

		notPresent = 404;
		n = h.nodeOfPublic(notPresent);
		assertNull(n);
	}

	// 4

	@Test
	public void test_LinearSearch_NodeOf_FourCompacted_Absent() {
		int size;
		HF_JUnit<Integer> h;
		NHF_JUnit<Integer> n;
		Integer notPresent;

		setUp();

		h = newHeap(false);

		size = 4;
		addAndFill(h, size);

		h.cleanUp();
		assertEquals(size, h.size());

		setUp();

		h = newHeap(false);

		// roots:
		// . 2
		// ./ \
		// 5 . 3
		// ... |
		// ... 8

		notPresent = 404;
		n = h.nodeOfPublic(notPresent);
		assertNull(n);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_LinearSearch_NodeOf_FourCompacted_Root() {
		int size;
		HF_JUnit<Integer> h;
		NHF_JUnit<Integer> n;
		NHF_JUnit<Integer> nodes[];

		setUp();

		h = newHeap(false);

		size = 4;
		nodes = addAndFill(h, size); // 5, 2, 8, 3

		h.cleanUp();
		assertEquals(size, h.size());

		// roots:
		// . 2
		// ./ \
		// 5 . 3
		// ... |
		// ... 8

		n = h.nodeOfPublic(sequenceToInsert_Random[1]);
		assertNotNull(n);
		assertEquals(nodes[1], n);
		assertEquals(sequenceToInsert_Random[1], n.getData());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_LinearSearch_NodeOf_FourCompacted_LonelyChild() {
		int size;
		HF_JUnit<Integer> h;
		NHF_JUnit<Integer> n;
		NHF_JUnit<Integer> nodes[];

		setUp();

		h = newHeap(false);

		size = 4;
		nodes = addAndFill(h, size); // 5, 2, 8, 3

		h.cleanUp();
		assertEquals(size, h.size());

		// roots:
		// . 2
		// ./ \
		// 5 . 3
		// ... |
		// ... 8

		n = h.nodeOfPublic(sequenceToInsert_Random[0]);
		assertNotNull(n);
		assertEquals(nodes[0], n);
		assertEquals(sequenceToInsert_Random[0], n.getData());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_LinearSearch_NodeOf_FourCompacted_BiggerChild() {
		int size;
		HF_JUnit<Integer> h;
		NHF_JUnit<Integer> n;
		NHF_JUnit<Integer> nodes[];

		setUp();

		h = newHeap(false);

		size = 4;
		nodes = addAndFill(h, size); // 5, 2, 8, 3

		h.cleanUp();
		assertEquals(size, h.size());

		// roots:
		// . 2
		// ./ \
		// 5 . 3
		// ... |
		// ... 8

		n = h.nodeOfPublic(sequenceToInsert_Random[3]);
		assertNotNull(n);
		assertEquals(nodes[3], n);
		assertEquals(sequenceToInsert_Random[3], n.getData());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_LinearSearch_NodeOf_FourCompacted_BiggerChildChild() {
		int size;
		HF_JUnit<Integer> h;
		NHF_JUnit<Integer> n;
		NHF_JUnit<Integer> nodes[];

		setUp();

		h = newHeap(false);

		size = 4;
		nodes = addAndFill(h, size); // 5, 2, 8, 3

		h.cleanUp();
		assertEquals(size, h.size());

		// roots:
		// . 2
		// ./ \
		// 5 . 3
		// ... |
		// ... 8

		n = h.nodeOfPublic(sequenceToInsert_Random[2]);
		assertNotNull(n);
		assertEquals(nodes[2], n);
		assertEquals(sequenceToInsert_Random[2], n.getData());
	}

	// EXTRACT MIN

	@Test
	public void test_LinearSearch_ExtractMin_Empty() {
		HF_JUnit<Integer> h;
		Integer data;

		h = newHeap(false);

		data = h.extractMin();

		assertNull(data);
		assertEquals(0, h.size());
		assertNull(h.getRoot());
		assertNull(h.getMinNode());
	}

	@Test
	public void testExtractMin_One() {
		HF_JUnit<Integer> h;
		Integer data;

		h = newHeap(false);

		data = 7;
		h.add(data);
		assertEquals(data, h.getMin());
		assertNotNull(h.getRoot());
		assertEquals(data, h.extractMin());

		assertEquals(0, h.size());
		assertNull(h.getRoot());
		assertNull(h.getMinNode());
	}

	@Test
	public void test_LinearSearch_ExtractMin_Two_InsertionCrescentOrder() {
		HF_JUnit<Integer> h;
		Integer data1, data2;

		h = newHeap(false);

		data1 = 7;
		data2 = 9;
		h.add(data1);
		System.out.println("root at first : " + h.getRoot().getData() + ", min: " + h.getMin());
		h.add(data2);
		System.out.println("then : " + h.getRoot().getData() + ", min: " + h.getMin());
		assertEquals(data1, h.getMin());
		assertNotNull(h.getRoot());
		assertEquals(data1, h.extractMin());
		System.out.println("new min: " + h.getMin());
		assertEquals(data2, h.getMin());
		assertEquals(1, h.size());
		assertNotNull(h.getRoot());
	}

	@Test
	public void test_LinearSearch_ExtractMin_Two_InsertionDecreasingOrder() {
		HF_JUnit<Integer> h;
		Integer data1, data2;

		h = newHeap(false);

		data1 = 17;
		data2 = 9;
		h.add(data1);
		h.add(data2);
		assertEquals(data2, h.getMin());
		assertNotNull(h.getRoot());
		assertEquals(data2, h.extractMin());
		assertEquals(data1, h.getMin());
		assertEquals(1, h.size());
		assertNotNull(h.getRoot());
	}

	@Test
	public void test_LinearSearch_ExtractMin_Two_FromColumn() {
		HF_JUnit<Integer> h;
		Integer data1, data2, removed;
		NHF_JUnit<Integer> n1, n2;

		h = newHeap(false);

		data1 = 7;
		data2 = 9;
		n1 = (NHF_JUnit<Integer>) h.insert(data1);
		n2 = (NHF_JUnit<Integer>) h.insert(data2);
		n2.unlinkHorizPublic();
		n1.addChildPublic(n2);
		h.setRootPublic(n1);

		assertEquals(data1, h.getMin());

		removed = h.extractMin();
		assertEquals(data1, removed);

		assertEquals(data2, h.getMin());
		assertEquals(1, h.size());
		assertNotNull(h.getRoot());
	}

	@Test
	public void test_LinearSearch_ExtractMin_Three_AlreadyCleandUp() {
		test_ExtractMin_Three_AlreadyCleandUp(new HF_JUnit<Integer>(Comparators.INTEGER_COMPARATOR, false));
	}

	//

	// TODO hashing nodes

	//

	@Test
	public void test_HashingNodes_Add_OneElement() {
		test_Add_OneElement(newHeap(true));
	}

	@Test
	public void test_HashingNodes_CleanAfter_Add_One() {
		test_CleanAfter_Add_One(newHeap(true));
	}

	@Test
	public void test_HashingNodes_Add_Two_Increasing() {
		test_Add_Two_Increasing(newHeap(true));
	}

	@Test
	public void test_HashingNodes_Add_Two_Decreasing() {
		test_Add_Two_Decreasing(newHeap(true));
	}

	@Test
	public void test_HashingNodes_CleanAfter_Add_Two_Increasing() {
		test_CleanAfter_Add_Two_Increasing(newHeap(true));
	}

	@Test
	public void test_HashingNodes_CleanAfter_Add_Two_Decreasing() {
		test_CleanAfter_Add_Two_Decreasing(newHeap(true));
	}

	@Test
	public void test_HashingNodes_Add_Three() {
		test_Add_Three(newHeap(true));
	}

	@Test
	public void test_HashingNodes_CleanAfter_Add_Three() {
		HF_JUnit<Integer> h;
		NHF_JUnit<Integer> n1, n2, n3;
		setUp();

		h = newHeap(true);

		Objects.requireNonNull(sequenceToInsert_Random);

		n1 = (NHF_JUnit<Integer>) h.insert(sequenceToInsert_Random[0]); // 5
		n2 = (NHF_JUnit<Integer>) h.insert(sequenceToInsert_Random[1]); // 2
		n3 = (NHF_JUnit<Integer>) h.insert(sequenceToInsert_Random[2]); // 8

		assertEquals(3, h.size());

		h.cleanUp();
		assertEquals(3, h.size());

		// roots:
		// 8 .. 2
		// .... |
		// .... 5

		assertEquals(n1, n1.getNext());
		assertEquals(n1, n1.getPrevious());
		assertEquals(n3, n2.getNext());
		assertEquals(n3, n2.getPrevious());
		assertEquals(n2, n3.getNext());
		assertEquals(n2, n3.getPrevious());

		assertNull(n2.getFather());
		assertNull(n3.getFather());
		assertEquals(n2, n1.getFather());

		// should have:
		// at first, "n3" with "8" as data and as a singletone
		// at second, "n" with "2" as data, "5" as child, roots == n2 and min == "2"

		assertEquals(n2.getData(), h.getRoot().getData());
		assertEquals(n2, h.getRoot());
		assertEquals(n2.getData(), h.getMin());

		assertEquals(0, n1.getChildrenAmount());
		assertEquals(1, n2.getChildrenAmount());
		assertEquals(0, n3.getChildrenAmount());

		assertNull(n1.getLastChild());
		assertEquals(n1, n2.getLastChild());
		assertNull(n3.getLastChild());

		assertEquals(n2, n1.getFather());
		assertNull(n2.getFather());
		assertNull(n3.getFather());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_HashingNodes_Add_Four() {
		int size;
		HF_JUnit<Integer> h;
		NHF_JUnit<Integer> n, nodes[];

		setUp();

		h = newHeap(true);

		Objects.requireNonNull(sequenceToInsert_Random);

		size = 4;
		nodes = addAndFill(h, size);

		assertEquals(size, h.size());

		assertNotNull("the root should not be null", h.getRoot());
		assertEquals(sequenceToInsert_Random[size - 1], h.getRoot().getData());
		assertEquals(nodes[size - 1], h.getRoot());

		assertNotNull("the min should not be null", h.getMinNode());
		assertEquals(sequenceToInsert_Random[1], h.getMin());
		assertEquals(nodes[1], h.getMinNode());

		for (int i = 0; i < size; i++) {
			int nextIndex, prevIndex;
			nextIndex = (i + 1) % size;
			prevIndex = i - 1;
			if (prevIndex < 0) {
				prevIndex = size - 1;
			}
			n = nodes[i];
			assertEquals(nodes[nextIndex], n.getNext());
			assertEquals(nodes[prevIndex], n.getPrevious());
			assertNull(n.getFather());
			assertEquals(0, n.getChildrenAmount());
			assertNull(n.getLastChild());
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_HashingNodes_CleanAfter_Add_Four() {
		int size;
		HF_JUnit<Integer> h;
		NHF_JUnit<Integer> n, nodes[];

		setUp();

		h = newHeap(true);

		Objects.requireNonNull(sequenceToInsert_Random);

		size = 4;
		nodes = addAndFill(h, size); // 5, 2, 8, 3

		h.cleanUp();
		assertEquals(size, h.size());

		// roots:
		// . 2
		// ./ \
		// 5 . 3
		// ... |
		// ... 8
		assertEquals(nodes[1], h.getRoot());
		assertEquals(nodes[1], h.getMinNode());
		assertEquals(sequenceToInsert_Random[1], h.getMin());

		n = nodes[1];
		assertEquals(n, n.getNext());
		assertEquals(n, n.getPrevious());
		assertEquals(nodes[3], nodes[0].getNext());
		assertEquals(nodes[3], nodes[0].getPrevious());
		assertEquals(nodes[0], nodes[3].getNext());
		assertEquals(nodes[0], nodes[3].getPrevious());
		assertEquals(nodes[2], nodes[2].getNext());
		assertEquals(nodes[2], nodes[2].getPrevious());

		assertNull(n.getFather());
		assertEquals(n, nodes[0].getFather());
		assertEquals(n, nodes[3].getFather());
		assertEquals(nodes[3], nodes[2].getFather());

		assertEquals(2, n.getChildrenAmount());
		assertEquals(0, nodes[0].getChildrenAmount());
		assertEquals(1, nodes[3].getChildrenAmount());
		assertEquals(0, nodes[2].getChildrenAmount());

		assertEquals(nodes[3], n.getLastChild());
		assertEquals(nodes[0], n.getLastChild().getNext());
		assertNull(nodes[0].getLastChild());
		assertEquals(nodes[2], nodes[3].getLastChild());
		assertNull(nodes[2].getLastChild());
	}

	//

	@Test
	public void test_HashingNodes_NodeOf_Empty() {
		HF_JUnit<Integer> h;

		setUp();

		h = newHeap(true);

		assertNull(h.nodeOfPublic(5));
	}

	@Test
	public void test_HashingNodes_NodeOf_OnePresent() {
		HF_JUnit<Integer> h;
		NHF_JUnit<Integer> n;

		setUp();

		h = newHeap(true);

		n = (NHF_JUnit<Integer>) h.insert(7);

		assertEquals(n, h.nodeOfPublic(n.getData()));
	}

	@Test
	public void test_HashingNodes_NodeOf_OneAbsent() {
		Integer data;
		HF_JUnit<Integer> h;

		setUp();

		h = newHeap(true);

		data = 7;
		h.insert(data);

		assertNull(h.nodeOfPublic(data + 1));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_HashingNodes_NodeOf_TwoRootsPresent_First() {
		int size;
		HF_JUnit<Integer> h;
		NHF_JUnit<Integer> nodes[];

		setUp();

		h = newHeap(true);

		size = 2;
		nodes = addAndFill(h, size); // 5, 2,

		// h.roots == 2
		assertEquals(sequenceToInsert_Random[1], h.getRoot().getData());

		assertEquals(nodes[0], h.nodeOfPublic(sequenceToInsert_Random[0]));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_HashingNodes_NodeOf_TwoRootsPresent_Last() {
		int size;
		HF_JUnit<Integer> h;
		NHF_JUnit<Integer> nodes[];

		setUp();

		h = newHeap(true);

		size = 2;
		nodes = addAndFill(h, size);

		// h.roots == 2
		assertEquals(sequenceToInsert_Random[1], h.getRoot().getData());

		assertEquals(nodes[1], h.nodeOfPublic(sequenceToInsert_Random[1]));
	}

	@Test
	public void test_HashingNodes_NodeOf_TwoRootsAbsent() {
		int size;
		HF_JUnit<Integer> h;
		Integer dataNotPresent;

		setUp();

		h = newHeap(true);

		size = 2;
		addAndFill(h, size);

		// h.roots == 2
		assertEquals(sequenceToInsert_Random[1], h.getRoot().getData());

		dataNotPresent = 404;
		assertNull(h.nodeOfPublic(dataNotPresent));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_HashingNodes_NodeOf_TwoInColumnPresent_Root() {
		int size;
		HF_JUnit<Integer> h;
		NHF_JUnit<Integer> n;
		NHF_JUnit<Integer> nodes[];

		setUp();

		h = newHeap(true);

		size = 2;
		nodes = addAndFill(h, size);

		// h.roots == 2
		assertEquals(sequenceToInsert_Random[1], h.getRoot().getData());
		assertEquals(sequenceToInsert_Random[0], h.getRoot().getNext().getData());

		n = nodes[0];
		n.unlinkHorizPublic();
		h.getRoot().addChildPublic(n);

		assertEquals(1, h.getRoot().getChildrenAmount());
		assertEquals(0, n.getChildrenAmount());
		assertEquals(h.getRoot(), n.getFather());
		assertEquals(n, h.getRoot().getLastChild());

		assertEquals(h.getRoot(), h.nodeOfPublic(h.getRoot().getData()));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_HashingNodes_NodeOf_TwoInColumnPresent_Child() {
		int size;
		HF_JUnit<Integer> h;
		NHF_JUnit<Integer> n;
		NHF_JUnit<Integer> nodes[];

		setUp();

		h = newHeap(true);

		size = 2;
		nodes = addAndFill(h, size);

		// h.roots == 2
		assertEquals(sequenceToInsert_Random[1], h.getRoot().getData());
		assertEquals(sequenceToInsert_Random[0], h.getRoot().getNext().getData());

		n = nodes[0];
		n.unlinkHorizPublic();
		h.getRoot().addChildPublic(n);

		assertEquals(1, h.getRoot().getChildrenAmount());
		assertEquals(0, n.getChildrenAmount());
		assertEquals(h.getRoot(), n.getFather());
		assertEquals(n, h.getRoot().getLastChild());

		assertEquals(n, h.nodeOfPublic(h.getRoot().getLastChild().getData()));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_HashingNodes_NodeOf_TwoInColumnAbsent() {
		int size;
		HF_JUnit<Integer> h;
		NHF_JUnit<Integer> n;
		NHF_JUnit<Integer> nodes[];
		Integer dataNotPresent;

		setUp();

		h = newHeap(true);

		size = 2;
		nodes = addAndFill(h, size);

		// h.roots == 2
		assertEquals(sequenceToInsert_Random[1], h.getRoot().getData());
		assertEquals(sequenceToInsert_Random[0], h.getRoot().getNext().getData());

		n = nodes[0];
		n.unlinkHorizPublic();
		h.getRoot().addChildPublic(n);

		assertEquals(1, h.getRoot().getChildrenAmount());
		assertEquals(0, n.getChildrenAmount());
		assertEquals(h.getRoot(), n.getFather());
		assertEquals(n, h.getRoot().getLastChild());

		dataNotPresent = 404;
		assertNull(h.nodeOfPublic(dataNotPresent));
	}

	// 3

	@SuppressWarnings("unchecked")
	@Test
	public void test_HashingNodes_NodeOf_Three_TwoRoots_LoneRoot() {
		int size;
		HF_JUnit<Integer> h;
		NHF_JUnit<Integer> n;
		NHF_JUnit<Integer> nodes[];

		setUp();

		h = newHeap(true);

		size = 3;
		nodes = addAndFill(h, size); // 5, 2, 8

		h.cleanUp();
		// 8 ; 2->5

		n = h.nodeOfPublic(sequenceToInsert_Random[2]);
		assertNotNull(n);
		assertEquals(nodes[2], n);
		assertEquals(sequenceToInsert_Random[2], n.getData());
	}

	@Test
	public void test_HashingNodes_NodeOf_Three_TwoRoots_ColumnRootRoot() {
		int size;
		HF_JUnit<Integer> h;
		NHF_JUnit<Integer> n;

		setUp();

		h = newHeap(true);

		size = 3;
		addAndFill(h, size); // 5, 2, 8

		h.cleanUp();
		// 8 ; 2->5

		n = h.nodeOfPublic(sequenceToInsert_Random[1]);
		assertNotNull(n);
		assertEquals(h.getRoot(), n);
		assertEquals(h.getRoot().getData(), n.getData());
	}

	@Test
	public void test_HashingNodes_NodeOf_Three_TwoRoots_ColumnRootChild() {
		int size;
		HF_JUnit<Integer> h;
		NHF_JUnit<Integer> n;

		setUp();

		h = newHeap(true);

		size = 3;
		addAndFill(h, size); // 5, 2, 8

		h.cleanUp();
		// 8 ; 2->5

		n = h.nodeOfPublic(sequenceToInsert_Random[0]);
		assertNotNull(n);
		assertNotNull(h.getRoot().getLastChild());
		assertEquals(h.getRoot().getLastChild(), n);
		assertEquals(h.getRoot().getLastChild().getData(), n.getData());
	}

	@Test
	public void test_HashingNodes_NodeOf_Three_TwoRoots_Absent() {
		int size;
		HF_JUnit<Integer> h;
		NHF_JUnit<Integer> n;
		Integer notPresent;

		setUp();

		h = newHeap(true);

		size = 3;
		addAndFill(h, size); // 5, 2, 8

		h.cleanUp();
		// 8 ; 2->5

		notPresent = 404;
		n = h.nodeOfPublic(notPresent);
		assertNull(n);
	}

	// 4

	@Test
	public void test_HashingNodes_NodeOf_FourCompacted_Absent() {
		int size;
		HF_JUnit<Integer> h;
		NHF_JUnit<Integer> n;
		Integer notPresent;

		setUp();

		h = newHeap(true);

		size = 4;
		addAndFill(h, size);

		h.cleanUp();
		assertEquals(size, h.size());

		setUp();

		h = newHeap(true);

		// roots:
		// . 2
		// ./ \
		// 5 . 3
		// ... |
		// ... 8

		notPresent = 404;
		n = h.nodeOfPublic(notPresent);
		assertNull(n);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_HashingNodes_NodeOf_FourCompacted_Root() {
		int size;
		HF_JUnit<Integer> h;
		NHF_JUnit<Integer> n;
		NHF_JUnit<Integer> nodes[];

		setUp();

		h = newHeap(true);

		size = 4;
		nodes = addAndFill(h, size); // 5, 2, 8, 3

		h.cleanUp();
		assertEquals(size, h.size());

		// roots:
		// . 2
		// ./ \
		// 5 . 3
		// ... |
		// ... 8

		n = h.nodeOfPublic(sequenceToInsert_Random[1]);
		assertNotNull(n);
		assertEquals(nodes[1], n);
		assertEquals(sequenceToInsert_Random[1], n.getData());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_HashingNodes_NodeOf_FourCompacted_LonelyChild() {
		int size;
		HF_JUnit<Integer> h;
		NHF_JUnit<Integer> n;
		NHF_JUnit<Integer> nodes[];

		setUp();

		h = newHeap(true);

		size = 4;
		nodes = addAndFill(h, size); // 5, 2, 8, 3

		h.cleanUp();
		assertEquals(size, h.size());

		// roots:
		// . 2
		// ./ \
		// 5 . 3
		// ... |
		// ... 8

		n = h.nodeOfPublic(sequenceToInsert_Random[0]);
		assertNotNull(n);
		assertEquals(nodes[0], n);
		assertEquals(sequenceToInsert_Random[0], n.getData());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_HashingNodes_NodeOf_FourCompacted_BiggerChild() {
		int size;
		HF_JUnit<Integer> h;
		NHF_JUnit<Integer> n;
		NHF_JUnit<Integer> nodes[];

		setUp();

		h = newHeap(true);

		size = 4;
		nodes = addAndFill(h, size); // 5, 2, 8, 3

		h.cleanUp();
		assertEquals(size, h.size());

		// roots:
		// . 2
		// ./ \
		// 5 . 3
		// ... |
		// ... 8

		n = h.nodeOfPublic(sequenceToInsert_Random[3]);
		assertNotNull(n);
		assertEquals(nodes[3], n);
		assertEquals(sequenceToInsert_Random[3], n.getData());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_HashingNodes_NodeOf_FourCompacted_BiggerChildChild() {
		int size;
		HF_JUnit<Integer> h;
		NHF_JUnit<Integer> n;
		NHF_JUnit<Integer> nodes[];

		setUp();

		h = newHeap(true);

		size = 4;
		nodes = addAndFill(h, size); // 5, 2, 8, 3

		h.cleanUp();
		assertEquals(size, h.size());

		// roots:
		// . 2
		// ./ \
		// 5 . 3
		// ... |
		// ... 8

		n = h.nodeOfPublic(sequenceToInsert_Random[2]);
		assertNotNull(n);
		assertEquals(nodes[2], n);
		assertEquals(sequenceToInsert_Random[2], n.getData());
	}

	// EXTRACT MIN

	@Test
	public void test_HashingNodes_ExtractMin_Empty() {
		HF_JUnit<Integer> h;
		Integer data;

		h = newHeap(true);

		data = h.extractMin();

		assertNull(data);
		assertEquals(0, h.size());
		assertNull(h.getRoot());
		assertNull(h.getMinNode());
	}

	@Test
	public void test_HashingNodes_ExtractMin__One() {
		HF_JUnit<Integer> h;
		Integer data;

		h = newHeap(true);

		data = 7;
		h.add(data);
		assertEquals(data, h.getMin());
		assertNotNull(h.getRoot());
		assertEquals(data, h.extractMin());

		assertEquals(0, h.size());
		assertNull(h.getRoot());
		assertNull(h.getMinNode());
	}

	@Test
	public void test_HashingNodes_ExtractMin_Two_InsertionCrescentOrder() {
		HF_JUnit<Integer> h;
		Integer data1, data2;

		h = newHeap(true);

		data1 = 7;
		data2 = 9;
		h.add(data1);
		h.add(data2);
		assertEquals(data1, h.getMin());
		assertNotNull(h.getRoot());
		assertEquals(data1, h.extractMin());
		assertEquals(data2, h.getMin());
		assertEquals(1, h.size());
		assertNotNull(h.getRoot());
	}

	@Test
	public void test_HashingNodes_ExtractMin_Two_InsertionDecreasingOrder() {
		HF_JUnit<Integer> h;
		Integer data1, data2;

		h = newHeap(true);

		data1 = 17;
		data2 = 9;
		h.add(data1);
		h.add(data2);
		assertEquals(data2, h.getMin());
		assertNotNull(h.getRoot());
		assertEquals(data2, h.extractMin());
		assertEquals(data1, h.getMin());
		assertEquals(1, h.size());
		assertNotNull(h.getRoot());
	}

	@Test
	public void test_HashingNodes_ExtractMin_Two_FromColumn() {
		HF_JUnit<Integer> h;
		Integer data1, data2, removed;
		NHF_JUnit<Integer> n1, n2;

		h = newHeap(true);

		data1 = 7;
		data2 = 9;
		n1 = (NHF_JUnit<Integer>) h.insert(data1);
		n2 = (NHF_JUnit<Integer>) h.insert(data2);
		n2.unlinkHorizPublic();
		n1.addChildPublic(n2);
		h.setRootPublic(n1);

		assertEquals(data1, h.getMin());

		removed = h.extractMin();
		assertEquals(data1, removed);

		assertEquals(data2, h.getMin());
		assertEquals(1, h.size());
		assertNotNull(h.getRoot());
	}

	@Test
	public void test_HashingNode_ExtractMin_Three_AlreadyCleandUp() {
		test_ExtractMin_Three_AlreadyCleandUp(new HF_JUnit<Integer>(Comparators.INTEGER_COMPARATOR, true));
	}

	//

	// TODO CLASSES

	//

	static class HF_JUnit<T> extends HeapFibonacci<T> {

		public HF_JUnit(Comparator<T> comparator, boolean cacheNodesByDataHash) {
			super(comparator, cacheNodesByDataHash);
		}

		public HF_JUnit(Comparator<T> comparator, int cacheNodesHashMapSize) {
			super(comparator, cacheNodesHashMapSize);
		}

		public HF_JUnit(Comparator<T> comparator) {
			super(comparator);
		}

		@Override
		protected NodeHF<T> newNode(T data) {
			return new NHF_JUnit<T>(this, data);
		}

		//

		public NHF_JUnit<T> getRoot() {
			return (NHF_JUnit<T>) this.roots;
		}

		public NHF_JUnit<T> getMinNode() {
			return (NHF_JUnit<T>) this.min;
		}

		//

		public void setSize(int size) {
			this.size = size;
		}

		public void setRootPublic(NHF_JUnit<T> newRoot) {
			this.roots = newRoot;
		}

		public void setMinPublic(NHF_JUnit<T> newMin) {
			this.min = newMin;
		}

		public NHF_JUnit<T> nodeOfPublic(T data) {
			return (NHF_JUnit<T>) super.nodeOf(data);
		}

		//

		static class NHF_JUnit<E> extends NodeHF<E> {

			public NHF_JUnit(HeapFibonacci<E> heap, E data) {
				super(heap, data);
			}

			public boolean isMarked() {
				return this.marked;
			}

			public NodeHF<E> getFather() {
				return this.father;
			}

			public void unlinkHorizPublic() {
				super.unlinkHorizontally();
			}

			public void addChildPublic(NHF_JUnit<E> n) {
				super.addChild(n);
			}

			public void linkAfterPublic(NHF_JUnit<E> n) {
				super.linkAfter(n);
			}
		}
	}
}