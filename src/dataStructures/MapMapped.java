package dataStructures;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

import dataStructures.minorUtils.EntryImpl;

public class MapMapped<K, OriginalType, T> implements Map<K, T> {

	public MapMapped(Map<K, OriginalType> backMap, Function<OriginalType, T> newTypeExtractor) {
		this.backMap = backMap;
		this.newTypeExtractor = newTypeExtractor;
	}

	protected final Map<K, OriginalType> backMap;
	protected final Function<OriginalType, T> newTypeExtractor;
	protected Function<T, OriginalType> reverseMapper;

	public Function<OriginalType, T> getNewTypeExtractor() { return newTypeExtractor; }

	public Function<T, OriginalType> getReverseMapper() { return reverseMapper; }

	public MapMapped<K, OriginalType, T> setReverseMapper(Function<T, OriginalType> reverseMapper) {
		this.reverseMapper = reverseMapper;
		return this;
	}

	@Override
	public int size() { return this.backMap.size(); }

	@Override
	public void clear() { backMap.clear(); }

	@Override
	public boolean isEmpty() { return this.backMap.isEmpty(); }

//	public Object[] toArray() {
//		int[] i;
//		Object[] a;
//		a = new Object[backMap.size()];
//		i = new int[] { 0 };
//		backMap.forEach(o -> a[i[0]++] = newTypeExtractor.apply(o));
//		return a;
//	}

	@Override
	public void forEach(BiConsumer<? super K, ? super T> action) {
		this.backMap.forEach((k, oldTypeElem) -> action.accept(k, newTypeExtractor.apply(oldTypeElem)));
	}

//	public <Tt> Tt[] toArray(final Tt[] a) {
//		int len;
////		throw new UnsupportedOperationException("Too lazy to implement");
////        return backSet.toArray(a);
//		if (a == null)
//			return null;
//		if (a.length >= (len = size())) {
//			int[] i = { 0 };
//			this.forEach(elem -> { a[i[0]++] = (Tt) elem; });
//			return a;
//		} else {
//			int i = 0;
//			Tt[] newArray;
//			Iterator<T> iter = this.iterator();
//			newArray = (Tt[]) Array.newInstance(a.getClass(), len);
////			
//			while (i < len && iter.hasNext()) {
//				newArray[i++] = (Tt) iter.next();
//			}
//			return newArray;
//		}
//	}

	@Override
	public T put(K key, T value) {
		if (reverseMapper == null)
			throw new UnsupportedOperationException("Cannot modify the original set without a reverse-mapper");
		return this.newTypeExtractor.apply(this.backMap.put(key, reverseMapper.apply(value)));
	}

	@Override
	public T remove(Object key) {
		if (reverseMapper == null)
			throw new UnsupportedOperationException("Cannot modify the original set without a reverse-mapper");
		return this.newTypeExtractor.apply(this.backMap.remove(key));
	}

	/**
	 * Returns one element, if this set is not empty, chosen in a way dependent to
	 * its base implementation. In case no useful way could be used, it just use the
	 * iterator.
	 */
	public T pickOne() {
		T el = null;
		if (isEmpty())
			return null;
		if (this.backMap instanceof SortedMap<?, ?>) {
			OriginalType anElement;
			anElement = this.backMap.get(((SortedMap<K, OriginalType>) this.backMap).firstKey());
			el = this.newTypeExtractor.apply(anElement);
		} else {
			el = this.entrySet().iterator().next().getValue();
		}
		return el;
	}

	public T removeOne() {
		T el = null;
		K aKey;
		OriginalType anElement;
		if (isEmpty())
			return null;
		if (this.backMap instanceof SortedMap<?, ?>) {
			aKey = ((SortedMap<K, OriginalType>) this.backMap).firstKey();
		} else {
			aKey = this.backMap.entrySet().iterator().next().getKey();
		}
		anElement = this.backMap.get(aKey);
		el = this.newTypeExtractor.apply(anElement);
		this.backMap.remove(aKey);
		return el;
	}

	//

	@Override
	public boolean containsKey(Object key) { return this.backMap.containsKey(key); }

	@Override
	public boolean containsValue(Object value) { return this.backMap.containsValue(value); }

	@Override
	public T get(Object key) { return this.newTypeExtractor.apply(this.backMap.get(key)); }

	@Override
	public void putAll(Map<? extends K, ? extends T> m) { m.forEach(this::put); }

	@Override
	public Set<K> keySet() { return this.backMap.keySet(); }

	@Override
	public Collection<T> values() {
		Function<Entry<K, OriginalType>, T> toValueMapper;
		toValueMapper = (Entry<K, OriginalType> e) -> this.newTypeExtractor.apply(e.getValue());
		return new SetMapped<Entry<K, OriginalType>, T>(this.backMap.entrySet(), toValueMapper);
	}

	@Override
	public Set<Entry<K, T>> entrySet() {
		Function<Entry<K, OriginalType>, Entry<K, T>> toNewEntryMapper;
		toNewEntryMapper = (Entry<K, OriginalType> e) -> new EntryImpl<K, T>(e.getKey(),
				this.newTypeExtractor.apply(e.getValue()));
		return new SetMapped<Entry<K, OriginalType>, Entry<K, T>>(this.backMap.entrySet(), toNewEntryMapper);
	}

	@Override
	public boolean equals(Object o) { return this.backMap.equals(o); }

	@Override
	public int hashCode() { return this.backMap.hashCode(); }
}