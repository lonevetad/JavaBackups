package dataStructures;

import java.util.Comparator;
import java.util.Iterator;
import java.util.function.BiFunction;

import dataStructures.NodeComparable.DefaultNodeComparable;
import tools.Stringable;

/** See {@link NodeComparable}. */
public class TreeComparable<K> implements Stringable {
	private static final long serialVersionUID = 212454434863000L;
	protected final BiFunction<K, Comparator<K>, NodeComparable<K>> nodeSupplier;
	protected final Comparator<K> keyComparator;
	protected NodeComparable<K> root;

	public TreeComparable(Comparator<K> keyComparator) {
		this(NodeComparable::newDefaultNodeComparable, keyComparator);
	}

	public TreeComparable(BiFunction<K, Comparator<K>, NodeComparable<K>> nodeSupplier, Comparator<K> keyComparator) {
		super();
		this.nodeSupplier = nodeSupplier;
		this.keyComparator = keyComparator;
	}

	public BiFunction<K, Comparator<K>, NodeComparable<K>> getNodeSupplier() { return nodeSupplier; }

	public Comparator<K> getKeyComparator() { return keyComparator; }

	public NodeComparable<K> getRoot() { return root; }

	/** No null pointer checks are performed */
	public void addNode(K v, Iterable<K> path) {
		NodeComparable<K> newChild;
		Iterator<K> iter;
		newChild = nodeSupplier.apply(v, keyComparator);
		if (path == null || (!(iter = path.iterator()).hasNext())) {
			root = newChild;
		} else {
			boolean isAdd;
			NodeComparable<K> nIter, oldIter;
			DefaultNodeComparable<K> childIter;
			K step;
			oldIter = nIter = root;
			if (root == null)
				throw new NullPointerException();
			isAdd = this.nodeSupplier != null;
			step = iter.next();
			if (keyComparator.compare(root.getKeyIdentifier(), step) != 0)
				return;
			childIter = // (DefaultNodeComparable<K>) NodeComparable.newDefaultNodeComparable(v,
						// keyComparator);
					(DefaultNodeComparable<K>) nodeSupplier.apply(v, keyComparator);
//			System.out.print("ADDING OVER PATH: ");
//			for (K k : path) {
//				System.out.print(", " + k);
//			}
//			System.out.println();

			while (iter.hasNext()) {
				step = iter.next();
				childIter.setKeyIdentifier(step);
				oldIter = nIter;
//				System.out.println("STEP IS : " + step + ", iter.hasNext()_ " + iter.hasNext());
				nIter = nIter.getChildNCMostSimilarTo(childIter);
				if (nIter == null) {
					if (isAdd) {
//						System.out.println("porcodddd oldIter: " + oldIter);
//						System.out.println(this);
//						System.out.println("######## let's build up the fucking node");
						nIter = nodeSupplier.apply(step, keyComparator);
						oldIter.addChildNC(nIter);
					} else {
						throw new NullPointerException("Missing path node of key: " + step);
					}
				}
//				System.out.println("and now is: " + nIter);
			}
//			System.out.println("adding on iter: " + nIter.getKeyIdentifier() + ", the newChild: " + newChild);
//			System.out.println("nIter class: " + nIter.getClass().getName());
			nIter.addChildNC(newChild);
//			System.out.println("new nIter:");
//			System.out.println(nIter);
			if (!nIter.containsChildNC(newChild)) {
				System.out.println("WTF");
				nIter.getChildrenNC().forEach(System.out::println);
				System.out.println("You know what? I'll rage quit");
				throw new RuntimeException("WTF - " + nIter.getClass().getName());
			}
		}
//		System.out.println("END add");
	}

	public long computeDifference(TreeComparable<K> t) { return this.root.computeDissonanceAsLong(t.root); }

	//

	//

	//

	@Override
	public String toString() {
		StringBuilder sb;
		sb = new StringBuilder();
		sb.append("Tree:");
		if (root == null)
			sb.append(" null root");
		else
			toString(sb, root, 0);
		return sb.toString();
	}

	@Override
	public void toString(StringBuilder sb, int level) {
		sb.append('\n');
		addTab(sb, level, false);
		sb.append("TreeComp:");
		toString(sb, root, level);
	}

	public void toString(StringBuilder sb, NodeComparable<K> n, int level) {
		sb.append('\n');
		addTab(sb, level, false);
		K k;
		k = n.getKeyIdentifier();
		if (k instanceof Stringable) {
			((Stringable) k).addTab(sb, level + 1);
		} else {
			sb.append(k);
		}
		int lev = level + 1;
		n.forEachChildNC(c -> toString(sb, c, lev));
	}

	@Override
	public void addTab(StringBuilder sb, int tabLevel, boolean newLineNeeded) {
		if (sb != null) {
			if (newLineNeeded)
				sb.append('\n');
			sb.ensureCapacity(sb.length() + tabLevel);
			while (tabLevel-- > 0) {
				sb.append('\t');
			}
		}
	}
}