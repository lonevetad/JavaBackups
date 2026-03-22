package dataStructures.tupleSpace;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.function.BiConsumer;

public class TuplesSpace implements Serializable /* , Iterable<Tuple> */ {
	private static final long serialVersionUID = 4378584192947L;

	public TuplesSpace() {
		count = 0;
		levelOne = null;
	}

	private int count;
	private TuplaSingleDimension levelOne;

//

// TODO METHODS

	public int size() {
		return count;
	}

	/**
	 * Call {@link #forEach(BiConsumer, boolean)} passing {@code true} as second
	 * parameter.
	 */
	public void forEach(BiConsumer<Integer, Tuple> action) {
		forEach(action, true);
	}

	/**
	 * A simple for-each, providing also the level/dimension which each tupla
	 * belongs.<br>
	 * If the level/dimension is empty, the level is still used if the second
	 * parameter is {@code true} but a {@code null} tupla is provided.
	 */
	public void forEach(BiConsumer<Integer, Tuple> action, boolean performOnEachDimensionOnAbsentType) {
		boolean neverDoneAtThisLevel;
		int level, it, len;
		Queue<TuplaSingleDimension> queue;
		TuplaSingleType[] types;
		TuplaSingleType typeAtOneDimension;
		TuplaSingleDimension iter;
		Integer levelInt;
		if (action == null || count == 0)
			return;
		checkFirstLevel();
		iter = levelOne;
		queue = new LinkedList<>();
		queue.add(levelOne);
		while (!queue.isEmpty()) {
			iter = queue.remove();
			level = iter.dimensionLevel;
			levelInt = Integer.valueOf(level);
			neverDoneAtThisLevel = true;
			types = iter.types;
			it = -1;
			len = types.length;
			while (++it < len) {
				typeAtOneDimension = iter.types[it];
				if (typeAtOneDimension != null) {
					if (typeAtOneDimension.messages == null || typeAtOneDimension.messages.isEmpty()) {
						if (performOnEachDimensionOnAbsentType && neverDoneAtThisLevel) {
							neverDoneAtThisLevel = false;
							action.accept(levelInt, null);
						}
					} else
						for (Tuple t : typeAtOneDimension.messages)
							action.accept(levelInt, t);
					if (typeAtOneDimension.nextDimension != null)
						queue.add(typeAtOneDimension.nextDimension); // recursion, but in depth
				}
			}
			if (performOnEachDimensionOnAbsentType && neverDoneAtThisLevel)
				action.accept(levelInt, null);
		}
	}

	protected TuplaSingleDimension getDimensionHolding(TypesDataTupla[] types) {
		int i, len, lastIndex, c;
		TuplaSingleDimension dimensionIter;
		TypesDataTupla typeAtThisLevel;
		if (types == null)
			return null;
		checkFirstLevel();
		dimensionIter = levelOne;
		i = 0;
		lastIndex = (len = types.length) - 1;
		do {
			typeAtThisLevel = types[i];
			if ((c = dimensionIter.hasMessage(typeAtThisLevel, len)) < 0)
				return null;
			// the message could be held
			if (c > 0 && i == lastIndex)
				// the message is successfully held
				return dimensionIter;
			// not found, iterate
			if (i < lastIndex)
				dimensionIter = dimensionIter.getNextDimensionOf(typeAtThisLevel);
			/*
			 * else: nothing to do, because the "++i < len" in while will fail as like as
			 * this if because "lastIndex = len -1". This check is performed to avoid
			 * useless instantiations in "getNextDimensionOf"
			 */
		} while (++i < len && dimensionIter != null);
		// no message returned before
		return null;
	}

	/*
	 * Algorithm: only the exact space with the given tupla's dimensions and types
	 * holds the given tupla.
	 */
	public boolean addTupla(Tuple t) {
		int i, lastIndex;
		TuplaSingleDimension dimensionToAdd;
		if (t == null)
			return false;

		checkFirstLevel();
		dimensionToAdd = levelOne;
		i = -1;
		lastIndex = t.length() - 1;
		while (++i < lastIndex)
			dimensionToAdd = dimensionToAdd.getNextDimensionOf(t.getTypesDataTuplaAt(i));
		if (dimensionToAdd == null)
			return false;
		if (dimensionToAdd.addMessage(t)) {
			count++;
			return true;
		} else
			return false;
	}

	public Tuple peekTuplaOf(TypesDataTupla[] types) {
		// convert an array of types to a tupla, so it can be easily searched through
		// messages
		TuplaSingleDimension dimensionHolding;
		if (count == 0 || types == null || types.length == 0)
			return null;
		dimensionHolding = getDimensionHolding(types);
		if (dimensionHolding == null)
			return null;
		return dimensionHolding.peekMessage(types[types.length - 1]);
	}

	/**
	 * Should not get used, prefere {@link #getTuplaOf(TypesDataTupla[])} instead.
	 */
	public Tuple peekTuplaOf(Tuple tuplaOfTypesNoValue) {
		return peekTuplaOf(tuplaOfTypesNoValue.getTypes());
	}

	public boolean hasTuplaOf(TypesDataTupla[] types) {
		return peekTuplaOf(types) != null;
	}

	public Tuple removeTupla(TypesDataTupla[] types) {
		Tuple t;
		TuplaSingleDimension dimensionToAdd;
		if (count == 0 || types == null || types.length == 0)
			return null;
		dimensionToAdd = getDimensionHolding(types);
		if (dimensionToAdd == null)
			return null;
		t = dimensionToAdd.removeMessage(types[types.length - 1]);
		if (t == null)
			return null;
		count--;
		return t;
	}

	/**
	 * Should not get used, prefere {@link #removeTupla(TypesDataTupla[])} instead.
	 */
	public Tuple removeTupla(Tuple tuplaOfTypesNoValue) {
		return removeTupla(tuplaOfTypesNoValue.getTypes());
	}

	// PROTECTED

	protected void checkFirstLevel() {
		if (levelOne == null)
			levelOne = new TuplaSingleDimension(null);
	}

	@Override
	public String toString() {
		StringBuilder sb;
		sb = new StringBuilder(count << 4);
		sb.append("Space of tuples, having ").append(count).append(" tuples: {");
		this.forEach(new BiConsumer<Integer, Tuple>() {
			int currentIndex = -1;

			@Override
			public void accept(Integer index, Tuple tupla) {
				if (currentIndex != index)
					sb.append("\n--dimension: ").append((currentIndex = index) + 1);
				sb.append('\n').append(tupla.toString());
			}
		}, false);
		return sb.append('\n').append('}').toString();
	}

//

//TODO CLASSES

	/**
	 * A holder of values of a specific level: it holds each values of each tuples
	 * added, for each possible types. <br>
	 * It forms a tree-like structure, where every level has pointers to a higher
	 * level, increasing the tuple's dimension.
	 */
	protected static class TuplaSingleDimension implements Serializable {
		private static final long serialVersionUID = -38769400291L;
		final int dimensionLevel;
		final TuplaSingleDimension previousDimension;
		TuplaSingleType[] types;

		protected TuplaSingleDimension(TuplaSingleDimension previousDimension) {
			this.previousDimension = previousDimension;
			this.dimensionLevel = previousDimension == null ? 0 : previousDimension.dimensionLevel + 1;
			// all lazy initialized
		}

		protected void checkTypesNullity() {
			if (types == null)
				types = new TuplaSingleType[TypesDataTupla.TYPES_COUNT];
		}

		protected boolean addMessage(Tuple tupla) {
			TuplaSingleType tst;
			if (tupla == null)
				return false;
			tst = checkAndGetOrInit(tupla.getTypesDataTuplaAt(dimensionLevel));
			return tst.addMessage(tupla);
		}

		/** Returns true if the cache has been emptied. */
		protected boolean freeTypeCache(TypesDataTupla type) {
			int indexType;
			TuplaSingleType tst;
			indexType = type.ordinal();
			checkTypesNullity();
			if (((tst = types[indexType]) == null) || !tst.messages.isEmpty())
				return false;
			types[indexType] = null; // free memory
			return true;
		}

		/**
		 * Returns {@code 1} if this dimension holds this tupla, {@code 0} if this type
		 * is the same of the tupla's ones at this index, {@code -1} otherwise
		 */
		protected int hasMessage(TypesDataTupla[] types) {
			return hasMessage(types[dimensionLevel], types.length);
		}

		protected int hasMessage(Tuple t) {
			return hasMessage(t.getTypesDataTuplaAt(dimensionLevel), t.length());
		}

		private int hasMessage(TypesDataTupla type, int len) {
			int r;
			r = hasMessage(type);
			if (r == 1)
				return 1;
			if (dimensionLevel < len - 1)
				return 0;
			return r;
		}

		private int hasMessage(TypesDataTupla type) {
			if (!checkTypePresence(type))
				return -1; // type absent : error
			// this level should hold the message !
			return checkAndGetOrInit(type).hasMessages() ? 1 : -1;
		}

		protected Tuple peekMessage(TypesDataTupla type) {
			if (!checkTypePresence(type)) {
				System.out.println("type " + type + " not present at level " + dimensionLevel);
				return null; // type absent : error
			}
			return checkAndGetOrInit(type).peekMessage();
		}

		protected Tuple removeMessage(TypesDataTupla type) {
			return checkAndGetOrInit(type).removeMessage();
		}

		protected TuplaSingleDimension getNextDimensionOf(TypesDataTupla type) {
			// lazy initialization performed here
			return checkAndGetOrInit(type).getNextDimension();
		}

		protected boolean checkTypePresence(TypesDataTupla type) {
			checkTypesNullity();
			return types[type.ordinal()] != null;
		}

		protected TuplaSingleType checkAndGetOrInit(TypesDataTupla type) {
			int indexType;
			TuplaSingleType tst;

			indexType = type.ordinal();
			checkTypesNullity();
			// lazy initialization performed here
			if ((tst = types[indexType]) == null) { // checkTypePresence()
				tst = types[indexType] = new TuplaSingleType(this, type);
			}
			return tst;
		}
	}

	/** Holder of all values from old messages */
	protected static class TuplaSingleType implements Serializable {
		private static final long serialVersionUID = -6096747185713123L;
		TypesDataTupla type;
		TuplaSingleDimension nextDimension, owningDimension;
		Queue<Tuple> messages; // holds ONLY messages of THIS dimension/level !!

		protected TuplaSingleType(TuplaSingleDimension owning, TypesDataTupla type) {
			this.owningDimension = owning;
			Objects.requireNonNull(type);
			this.type = type;
		}

		protected TuplaSingleDimension getNextDimension() {
			return (nextDimension != null) ? nextDimension
					// lazy initialization performed here
					: (nextDimension = new TuplaSingleDimension(owningDimension));
		}

		protected void checkMessageQueue() {
			if (messages == null)
				messages = new LinkedList<>();
			// MapTreeAVL.asQueue(MapTreeAVL.BehaviourOnKeyCollision.AddItsNotASet,
			// Tuple.COMPARATOR_TUPLE);
		}

		protected int getLevel() {
			return this.owningDimension.dimensionLevel;
		}

		/*
		 * @SuppressWarnings("unchecked") protected Tuple getMessage(Tuple t) { return
		 * (this.getLevel() != t.length() - 1) ? null :
		 * ((MapTreeAVL.TreeAVLDelegator<Tuple, Tuple>) messages).getBackTree().get(t);
		 * }
		 */

		protected boolean canAddRemoveCheckMessage(Tuple t) {
			int thisIndex;
			TypesDataTupla typeInput;
			thisIndex = this.getLevel();
			if (thisIndex != t.length() - 1)
				return false;
			typeInput = t.getTypesDataTuplaAt(thisIndex);
			if (type != typeInput) // maybe it's unecessary
				return false;
			return true;
		}

		protected boolean addMessage(Tuple t) {
			if (!canAddRemoveCheckMessage(t))
				return false;
			checkMessageQueue();
			System.out.println("Adding at index " + owningDimension.dimensionLevel + " and type " + type + " the " + t);
			messages.add(t);
			return true;
		}

		protected Tuple removeMessage() {
			Tuple t;
//			if (!canAddRemoveCheckMessage(t))return false;
			if (messages == null || messages.isEmpty())
				return null;
			checkMessageQueue();
			t = messages.remove();
			owningDimension.freeTypeCache(type);
			return t;
		}

		protected Tuple peekMessage() {
			Tuple t;
//			if (!canAddRemoveCheckMessage(t))return false;
			if (messages == null || messages.isEmpty())
				return null;
			checkMessageQueue();
			t = messages.peek();
			return t;
		}

		protected boolean hasMessages() {
			return messages != null && messages.size() > 0;
		}
		/*
		 * protected boolean hasMessage(Tuple t) { if (!canAddRemoveCheckMessage(t))
		 * return false; checkMessageQueue(); return messages.contains(t); }
		 */
	}

}