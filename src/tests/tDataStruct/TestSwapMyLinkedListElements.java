package tests.tDataStruct;

import dataStructures.MyLinkedList;

public class TestSwapMyLinkedListElements {

	public TestSwapMyLinkedListElements() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		int n, i, s;
		MyLinkedList<Integer> l;
//		Random r;

//		r = new Random();
		l = new MyLinkedList<Integer>();

		MyLinkedList.printList(l);

		i = -1;
		s = 10;
		while (++i < s) {
//			n = r.nextInt(s);
			n = i;
			System.out.println(i + " = " + n);
			l.add(n);
		}
		System.out.println();
		MyLinkedList.printList(l);

		i = 1;
		n = 3;
		System.out.println("\nSwap " + i + " & " + n);
		l.swapAt(i, n);
		MyLinkedList.printList(l);

		i = 0;
		n = 8;
		System.out.println("\nSwap " + i + " & " + n);
		l.swapAt(i, n);
		MyLinkedList.printList(l);

		i = 6;
		n = 7;
		System.out.println("\nSwap " + i + " & " + n);
		l.swapAt(i, n);
		MyLinkedList.printList(l);

		i = 4;
		n = 5;
		System.out.println("\nSwap " + i + " & " + n);
		l.swapAt(i, n);
		MyLinkedList.printList(l);

		i = 9;
		n = 2;
		System.out.println("\nSwap " + i + " & " + n);
		l.swapAt(i, n);
		MyLinkedList.printList(l);
	}

}
