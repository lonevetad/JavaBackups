package dataStructures.mtAvl;

import java.util.Comparator;

public abstract class MapTreeAVLIndexable<K, V> extends MapTreeAVLLightweight<K, V> {
	private static final long serialVersionUID = 560487123330281818L;

	public MapTreeAVLIndexable(BehaviourOnKeyCollision b, Comparator<K> comp) throws IllegalArgumentException {
		super(b, comp);
	}

	public MapTreeAVLIndexable(Comparator<K> comp) throws IllegalArgumentException { super(comp); }

	//

	@Override
	@SuppressWarnings("unchecked")
	public Entry<K, V> getAt(int i) {
		NodeAVL_Indexable n;
		if (i < 0 || i >= size)
			throw new IndexOutOfBoundsException("Index: " + i);
		if (size == 0)
			return null;
		if (size == 1)
			return root;
		n = (NodeAVL_Indexable) root;
		while (i >= 0 && n != NIL) {
			if (i < n.sizeLeft) {
				n = (NodeAVL_Indexable) n.left;
			} else {
				i -= n.sizeLeft;
				if (i == 0)
					return n; // me :D
				else {
					i--; // another node ("n") has been "surpassed"
					n = (NodeAVL_Indexable) n.right;
				}
			}
		}
		return n;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void insertFixup(NodeAVL nnn) {
		int hl, hr, delta;
		NodeAVL_Indexable n, temp;
		n = (MapTreeAVLIndexable<K, V>.NodeAVL_Indexable) nnn;
		while (n != NIL) {
			n.height = (((hl = n.left.height) > (hr = n.right.height)) ? hl : hr) + 1;
			delta = hl - hr;
			// adjust sizes
			n.sizeLeft = hl = (temp = (NodeAVL_Indexable) n.left) == NIL ? 0 : (temp.sizeLeft + temp.sizeRight + 1);
			n.sizeRight = hr = (temp = (NodeAVL_Indexable) n.right) == NIL ? 0 : (temp.sizeLeft + temp.sizeRight + 1);
			// update father's size, could be usefull
			if ((temp = (NodeAVL_Indexable) n.father) != NIL) {
				if (temp.left == n)
					temp.sizeLeft = (hl + hr + 1);
				else
					temp.sizeRight = (hl + hr + 1);
			}
			if (delta < 2 && delta > -2) {
				// no rotation
				n = (NodeAVL_Indexable) n.father;
			} else {
				// copied from Lightweight
				n.rotate(delta >= 2);
				n = (MapTreeAVLIndexable<K, V>.NodeAVL_Indexable) n.father.father;
			}
		}
		((NodeAVL_Indexable) NIL).sizeLeft = ((NodeAVL_Indexable) NIL).sizeRight = -1;
		NIL.height = DEPTH_INITIAL;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected V delete(NodeAVL nnn) {
		int prevSize;
		prevSize = size;
		V v;
		v = super.delete(nnn);
		if (prevSize == Integer.MAX_VALUE) {
			prevSize = (1 + ((NodeAVL_Indexable) root).sizeLeft + ((NodeAVL_Indexable) root).sizeRight);
			// is there an overflow?
			if (prevSize < 0 || ((NodeAVL_Indexable) root).sizeLeft < 0 || ((NodeAVL_Indexable) root).sizeRight < 0) {
				prevSize = Integer.MAX_VALUE;
			} //
			size = prevSize;
		} // else nothing, everything is jet ok
		return v;
	}

	//

	protected class NodeAVL_Indexable extends NodeAVL {
		private static final long serialVersionUID = 1L;
		protected int sizeLeft, sizeRight;

		public NodeAVL_Indexable(K k, V v) {
			super(k, v);
			sizeRight = sizeLeft = 0;
		}

		@Override
		public void rotate(boolean isRight) {
			int hl, hr;
			NodeAVL_Indexable nSide, oldFather;
			oldFather = (NodeAVL_Indexable) father;
			if (isRight) {
				// right
				nSide = (NodeAVL_Indexable) left;
				if (nSide.right.height > nSide.left.height) {
					// left-right rotation (left on b, right on c)
					// three-rotation : ignoring this difference would cause the tree to be
					// umbalanced again. x,y,z and w might be (all or none) NIL
					//h  :   .   n=a
					//   .   ./  .  \.
					//h+1:  b.   .   x
					//h+2:y  .c
					//h+3:   z w
					// ->
					//h  :   c
					//   .  /.\
					//h+1: b . a
					//h+2:y z.w x.
					final NodeAVL_Indexable a, b, c;
					a = this;
					b = nSide;
					c = (NodeAVL_Indexable) b.right;
					if (oldFather.left == a)
						oldFather.left = c;
					else
						oldFather.right = c;
					c.father = oldFather;
//					}
					a.father = c;
					a.left = c.right;
					if (c.right != NIL)
						c.right.father = a;
					c.right = a;
					if (c.left.father != NIL)
						c.left.father = b;
					b.right = c.left;
					b.father = c;
					c.left = b;

					// recompute height
					c.height++;
					a.height -= 2;
					b.height--;
					NIL.left = NIL.right = NIL.father = NIL;
					if (a == root) {
						root = c;
						c.father = NIL; // not necessary, but done to be sure
					}
					a.sizeLeft = c.sizeRight;
					b.sizeRight = c.sizeLeft;
					c.sizeRight += 1 + a.sizeRight;
					c.sizeLeft += 1 + b.sizeLeft;
					return;
				}
				// right rotation on b
				//h  :   .   n=a
				//   .   ./  .  \.
				//h+1:  b.   .   x
				//h+2: c .y
				//h+3:z w
				// ->
				//h  :   b
				//   .  /.\
				//h+1: c . a
				//h+2:z w.y x.
		
				// note: oldFather pointers moved outside
				left = left.right; // a.left == y
				nSide.right.father = this;
				nSide.right = this; // b.right == a
				// adjust sizes
				sizeLeft = nSide.sizeRight;
				nSide.sizeRight += 1 + sizeRight;
			} else {
				// left
				nSide = (NodeAVL_Indexable) right;
				if (nSide.left.height > nSide.right.height) {
					// right-left rotation (right on b, left on c)
					// three-rotation : ignoring this difference would cause the tree to be
					// umbalanced again. x,y,z and w might be (all or none) NIL
					//h  :   .   n=a
					//   .   ./  .  \.
					//h+1:  x.   .   b
					//h+2:   .   .c  .  y
					//h+3:   .   z w
					// ->
					//h  :   c
					//   .  /.\
					//h+1: a . b
					//h+2:x z.w y.
					final NodeAVL_Indexable a, b, c;
					a = this;
					b = nSide;
					c = (NodeAVL_Indexable) b.left;
					// then
//					if (oldFather != NIL) {
					if (oldFather.left == a)
						oldFather.left = c;
					else
						oldFather.right = c;
					c.father = oldFather;
//					}
					a.father = c;
					a.right = c.left;
					if (c.left != NIL)
						c.left.father = a;
					c.left = a;
					if (c.right.father != NIL)
						c.right.father = b;
					b.left = c.right;
					b.father = c;
					c.right = b;

					// recompute height
					c.height++;
					a.height -= 2;
					b.height--;
					NIL.left = NIL.right = NIL.father = NIL;
					if (a == root) {
						root = c;
						c.father = NIL; // not necessary, but done to be sure
					}
					// adjust sizes
					a.sizeRight = c.sizeLeft;
					b.sizeLeft = c.sizeRight;
					c.sizeLeft += 1 + a.sizeLeft;
					c.sizeRight += 1 + b.sizeRight;
					return;
				}
				// left rotation on b
				//h  :   .   n=a
				//   .   ./  .  \.
				//h+1:  x.   .   b
				//h+2:   .   . y . c
				//h+3:   .   .   .z w
				// ->
				//h  :   b
				//   .  /.\
				//h+1: a . c
				//h+2:x y.z w.
		
				// note: oldFather pointers moved outside
				right = right.left;
				nSide.left.father = this;
				nSide.left = this;
				// adjust sizes
				sizeRight = nSide.sizeLeft;
				nSide.sizeLeft += 1 + sizeLeft;
			}
			if (/* isNullNIL */NIL == (oldFather))
				// i'm root .. ehm, no: i WAS root
				root = nSide;
			else {
				if (oldFather.left == this)
					oldFather.left = nSide;
				else
					oldFather.right = nSide;
			}
			father = nSide;
			nSide.father = oldFather;
			hl = left.height;
			hr = right.height;
			hl = height = (hl > hr ? hl : hr) + 1;
			hr = ((isRight) ? nSide.left : nSide.right).height;
			nSide.height = (hl > hr ? hl : hr) + 1;
		}

		@Override
		protected int index() {
			int i, indexNodeLeftInorder;
			i = 0;
			if (sizeLeft > 0){
				i = sizeLeft;
			}
			indexNodeLeftInorder = -1;
			NodeAVL_Indexable nodeLeftInOrder = this; 
			while( nodeLeftInOrder != NIL && nodeLeftInOrder.father != NIL && nodeLeftInOrder.father.right != nodeLeftInOrder) {
				nodeLeftInOrder = (MapTreeAVLIndexable<K, V>.NodeAVL_Indexable) nodeLeftInOrder.father;
			}
			if (nodeLeftInOrder != NIL && nodeLeftInOrder.father != NIL ){
				indexNodeLeftInorder = nodeLeftInOrder.father.index();
			}
			if( indexNodeLeftInorder >= 0) {
				i += indexNodeLeftInorder + 1;
			}
			return i;
		}

		@Override
		public String toString() { return super.toString() + ", sl:" + sizeLeft + ",sr:" + sizeRight; }
	}
}
