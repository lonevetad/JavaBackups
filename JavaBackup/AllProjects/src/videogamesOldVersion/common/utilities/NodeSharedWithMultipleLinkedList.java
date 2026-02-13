package common.utilities;

import common.removed.LinkedListNode.Head_Setter;
import common.removed.LinkedListNode.Tail_Setter;

/**
 * Utilizzabile per definire dei nodi di liste doppiamente linkate.
 * <p>
 * Utilizzo tipico : da venir implementate nella classe di Equipaggiamento e di
 * Magie-Buff.<br>
 * Tenendo nella classe del giocatore generico (il quale estendera' sicuramente
 * na classe del tipo <code>ObjectLiving</code>) due puntatori alla testa (e
 * anche alla coda per comodita') ad un Euipaggiamento e ad una Magia-Buff, e'
 * possibile calcolare una particolare statistica dell'<code>ObjectLiving</code>
 * in questione che cambia valore dinamicamente (in base all'inventario e ai
 * buff temporaneamente attivi, appunto).<br>
 * Infatti, e' possibile iterare sulla lista estraendo la statistica ricercata e
 * inserendola in un contatore.
 */
public interface NodeSharedWithMultipleLinkedList<K extends NodeSharedWithMultipleLinkedList<K>> {
	// ? super .. e' un po' dubbio

	//

	// TODO UTILIZER-INTERFACES

	@SuppressWarnings("rawtypes")
	public static final DoSomeWithObject printer = (k) -> {
		System.out.println(k);
		return k;
	};

	//

	// TODO UTILIZER-INTERFACES

	/**
	 * This interface is designed to be used as a lambda-expression or a
	 * method-reference by the class that will manage instances of
	 * {@link NodeSharedWithMultipleLinkedList} (for example, the wrapper
	 * linked-list's class of the Node class that implements
	 * {@link NodeSharedWithMultipleLinkedList}).
	 */
	public interface HeadTailGetterSetter<J extends NodeSharedWithMultipleLinkedList<J>> {
		public NodeSharedWithMultipleLinkedList<J> getHead();

		public NodeSharedWithMultipleLinkedList<J> setHead(NodeSharedWithMultipleLinkedList<J> newHead);

		public NodeSharedWithMultipleLinkedList<J> getTail();

		public NodeSharedWithMultipleLinkedList<J> setTail(NodeSharedWithMultipleLinkedList<J> newTail);
	}

	/**
	 * Dato che la classe implementatrice e' un nodo condiviso da almeno una
	 * linked list (ma potenzialmente un numero infinito), allora bisogna dire a
	 * QUALI "prev" e "next" ci si sta riferendo. Come fare? Usare questa
	 * interfaccia. Ogni implementazione puo' referenziare ad un insieme di
	 * nodi, ergo ad una linked-list differente.
	 */
	public interface NextPrevGetterSetter<J extends NodeSharedWithMultipleLinkedList<J>> {

		public NodeSharedWithMultipleLinkedList<J> getPrev(NodeSharedWithMultipleLinkedList<J> thisNode);

		public NodeSharedWithMultipleLinkedList<J> getNext(NodeSharedWithMultipleLinkedList<J> thisNode);

		/**
		 * Similar to {@link #setPrev(NodeSharedWithMultipleLinkedList)}, but
		 * talking about the tail and
		 * {@link #setAfter(NodeSharedWithMultipleLinkedList, Tail_Setter)}.
		 */
		public NodeSharedWithMultipleLinkedList<J> setNext(NodeSharedWithMultipleLinkedList<J> thisNode,
				NodeSharedWithMultipleLinkedList<J> newNext);

		/**
		 * WARNING : this method is not designed to set the head of this link.
		 * For this purpose use
		 * {@link #setBefore(NodeSharedWithMultipleLinkedList, Head_Setter)}
		 * instead.<br>
		 * This method is designed ONLY to set a local variable, nothing more.
		 *
		 * @param thisNode
		 *            the node that needs to modify his local variables.
		 * @param newPrev
		 *            the value that will be setted into <code>thisNode</code>
		 *            local variables. <br>
		 *            On returning, considering the list from head to tail,
		 *            <code>newNode</code> will be placed before
		 *            <code>thisNode</code> node, BUT no <code>newNode</code>'s
		 *            local variables will be modified ! Use {@link #setBefore}
		 *            passing (<code>newNode</code>,<code>thisNode</code>) for
		 *            this purpose.
		 */
		public NodeSharedWithMultipleLinkedList<J> setPrev(NodeSharedWithMultipleLinkedList<J> thisNode,
				NodeSharedWithMultipleLinkedList<J> newPrev);
	}

	public interface DoSomeWithObject<J extends NodeSharedWithMultipleLinkedList<J>> {
		public Object doSome(NodeSharedWithMultipleLinkedList<J> k);
	}

	//

	// TODO GETTER

	/**
	 * Made just to give an implementation that could be used to define
	 * {@link Tail_Setter}.
	 */
	public default NodeSharedWithMultipleLinkedList<K> getTail(NextPrevGetterSetter<K> npgs) {
		NodeSharedWithMultipleLinkedList<K> iter;
		iter = this;
		if (iter.hasNext(npgs)) {
			while ((iter = npgs.getNext(iter)).hasNext(npgs))
				;
		}
		return iter;
	}

	/** Similar to {@link #getTail()}, but talking about {@link Head_Setter}. */
	public default NodeSharedWithMultipleLinkedList<K> getHead(NextPrevGetterSetter<K> npgs) {
		NodeSharedWithMultipleLinkedList<K> iter;
		iter = this;
		if (iter.hasPrev(npgs)) {
			while ((iter = npgs.getPrev(iter)).hasPrev(npgs))
				;
		}
		return iter;
	}

	//

	// TODO SETTER

	/**
	 * Set <code>this</code> instance before the
	 * <code>nodeThatWillBeAfter</code> instance, if
	 * <code>nodeThatWillBeAfter</code> exists. Adjust the head and the tail of
	 * the linked list if necessary.<br>
	 * <code>nodeThatWillBeAfter</code> is usually the head of the linked list.
	 * <p>
	 * Normal usage to insert a new node into a existing one:<br>
	 * <code>
	 * NodeSharedWithMultipleLinkedList<K> newNode, nodeJetExisting;<br><br>
	 * //nodeJetExisting is jet initialized<br><br>
	 * newNode = new _a_costructor_();<br>
	 * newNode.setMeBefore( nodeJetExisting , hs , npgs );<br><br>
	 * // hs is and instance of {@link HeadTailGetterSetter}.<br>
	 * // npgs is and instance of {@link NextPrevGetterSetter}.<br>
	 * </code>
	 *
	 * @param nodeThatWillBeAfter
	 *            the node that will be setted after <code>this</code> node. If
	 *            it's null or the head of the queue, <code>this</code> will be
	 *            the head.
	 */
	public default boolean setMeBefore(NodeSharedWithMultipleLinkedList<K> nodeThatWillBeAfter,
			/* Head_Getter<K> hg, */ HeadTailGetterSetter<K> hs, NextPrevGetterSetter<K> npgs) {
		NodeSharedWithMultipleLinkedList<K> n;

		if (nodeThatWillBeAfter == hs.getHead()) {
			System.out.println("setting head 1");
			hs.setHead(this);
			if (hs.getTail() == null) {
				System.out.println("tail setted 1");
				hs.setTail(this);
				/* so, k is null ... or belongs to another linked list o.o */
			} else {
				npgs.setPrev(nodeThatWillBeAfter, this);
				npgs.setNext(this, nodeThatWillBeAfter);
			}
		} else if (hs.getHead() == null) {
			System.out.println("setting head 2");
			hs.setHead(this);
			if (nodeThatWillBeAfter != null) {
				hs.setTail(nodeThatWillBeAfter);
				npgs.setPrev(nodeThatWillBeAfter, this);
				npgs.setNext(this, nodeThatWillBeAfter);
			} else {
				hs.setTail(this);
			}
		} else {
			//
			n = npgs.getPrev(nodeThatWillBeAfter);
			if (n == null) {
				// k must be the head .. WTF? copy-and-paste
				System.out.println("setting head 3");
				hs.setHead(this);
				if (hs.getTail() == null) {
					hs.setTail(nodeThatWillBeAfter == null ? this : nodeThatWillBeAfter);
					/*
					 * so, k is null ... or belongs to another linked list o.o
					 */
				} /* else { */
				npgs.setPrev(nodeThatWillBeAfter, this);
				npgs.setNext(this, nodeThatWillBeAfter);
				// }
			} else {
				// this should be the normal situation ..
				System.out.println("eh eh");
				npgs.setNext(n, this);
				npgs.setPrev(this, n);
				npgs.setPrev(nodeThatWillBeAfter, this);
				npgs.setNext(this, nodeThatWillBeAfter);
			}
		}
		return true;
	}

	/**
	 * See
	 * {@link #setMeBefore(NodeSharedWithMultipleLinkedList, HeadTailGetterSetter, NextPrevGetterSetter)},
	 * because it's similar.<br>
	 * <code>nodeThatWillBeBefore</code> is usually the tail of the linked list.
	 */
	public default boolean setMeAfter(NodeSharedWithMultipleLinkedList<K> nodeThatWillBeBefore,
			/* Head_Getter<K> hg, */ HeadTailGetterSetter<K> hs, NextPrevGetterSetter<K> npgs) {
		NodeSharedWithMultipleLinkedList<K> n;

		if (nodeThatWillBeBefore == hs.getTail()) {
			System.out.println("setting tail 1");
			hs.setTail(this);
			if (hs.getHead() == null) {
				System.out.println("head setted 1");
				hs.setHead(this);
				/* so, k is null ... or belongs to another linked list o.o */
			} else {
				npgs.setNext(nodeThatWillBeBefore, this);
				npgs.setPrev(this, nodeThatWillBeBefore);
			}
		} else if (hs.getTail() == null) {
			System.out.println("setting tail 2");
			hs.setTail(this);
			if (nodeThatWillBeBefore != null) {
				hs.setHead(nodeThatWillBeBefore);
				npgs.setNext(nodeThatWillBeBefore, this);
				npgs.setPrev(this, nodeThatWillBeBefore);
			} else {
				hs.setHead(this);
			}
		} else {
			//
			n = npgs.getNext(nodeThatWillBeBefore);
			if (n == null) {
				// k must be the tail .. WTF? copy-and-paste
				System.out.println("setting tail 3");
				hs.setTail(this);
				if (hs.getHead() == null) {
					hs.setHead(nodeThatWillBeBefore == null ? this : nodeThatWillBeBefore);
					/*
					 * so, k is null ... or belongs to another linked list o.o
					 */
				} /* else { */
				npgs.setNext(nodeThatWillBeBefore, this);
				npgs.setPrev(this, nodeThatWillBeBefore);
				// }
			} else {
				// this should be the normal situation ..
				System.out.println("eh eh");
				npgs.setPrev(n, this);
				npgs.setNext(this, n);
				npgs.setNext(nodeThatWillBeBefore, this);
				npgs.setPrev(this, nodeThatWillBeBefore);
			}
		}
		return true;
	}

	//

	// TODO OTHERS

	public default boolean hasPrev(NextPrevGetterSetter<K> npgs) {
		return npgs.getPrev(this) != null;
	}

	public default boolean hasNext(NextPrevGetterSetter<K> npgs) {
		return npgs.getNext(this) != null;
	}

	public default void unlink(NextPrevGetterSetter<K> npgs) {
		NodeSharedWithMultipleLinkedList<K> n, p;

		n = npgs.getNext(this);
		p = npgs.getPrev(this);

		if (n != null) {
			npgs.setPrev(n, p);
			if (p != null) {
				npgs.setNext(p, n);
			}
		}
		if (p != null) {
			npgs.setNext(p, n);
			if (n != null) {
				npgs.setPrev(n, p);
			}
		}

		// alla fine di tutto
		npgs.setNext(this, null);
		npgs.setPrev(this, null);
	}

	/**
	 * Itero su tutti i precedenti, me incluso, e poi su tutti i successivi di
	 * me
	 */
	public default void forEach(DoSomeWithObject<K> d, NextPrevGetterSetter<K> npgs) {
		NodeSharedWithMultipleLinkedList<K> n, p;

		if (d == null) return;

		p = this;
		do {
			d.doSome(p);
		} while ((p = npgs.getPrev(p)) != null);

		n = npgs.getNext(this);
		if (n != null) {
			do {
				d.doSome(n);
			} while ((n = npgs.getNext(n)) != null);
		}
	}

	/** Iterate all list in descending order : from head to tail. */
	public default void forEach_FromHead(DoSomeWithObject<K> d, HeadTailGetterSetter<K> hg,
			NextPrevGetterSetter<K> npgs) {
		NodeSharedWithMultipleLinkedList<K> h;
		if (d == null || hg == null) return;
		h = hg.getHead();
		if (h != null) {
			h.forEach(d, npgs);
		}
	}

	/** Iterate all list in ascending order : from tail to head. */
	public default void forEach_FromTail(DoSomeWithObject<K> d, HeadTailGetterSetter<K> tg,
			NextPrevGetterSetter<K> npgs) {
		NodeSharedWithMultipleLinkedList<K> t;
		if (d == null || tg == null) return;
		t = tg.getTail();
		if (t != null) {
			t.forEach(d, npgs);
		}
	}
}