package common.mainTools;

import java.io.Serializable;
import java.util.SortedMap;
import java.util.function.Function;

public interface SortedMapIterable<K, V> extends Serializable, SortedMap<K, V>, Iterable<K>, Function<K, V> {

	public V add(K key, V value);

	public V delete(K key);

	public V fetch(K key);

	public boolean hasKey(K key);

	@Override
	public default V put(K key, V value) {
		return add(key, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public default V get(Object key) {
		V v;
		v = null;
		try {
			v = fetch(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return v;
	}

	@SuppressWarnings("unchecked")
	@Override
	public default V remove(Object key) {
		V v;
		v = null;
		try {
			v = delete(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return v;
	}

	/***
	 * Simply fetch. the value associated with the given key.0
	 * <p>
	 * {@inheritDoc}}
	 */
	@Override
	public default V apply(K key) {
		return fetch(key);
	}
}