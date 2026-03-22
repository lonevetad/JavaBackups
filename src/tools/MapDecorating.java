package tools;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A kind of map using decorator pattern to be easily extended from one starting
 * key to another
 */
public class MapDecorating<K, Intermediate, V> implements Serializable, Map<K, V> {
	private static final long serialVersionUID = -5465314508156L;

	public static <Kk, I, Vv> Map<Kk, Vv> map(Map<Kk, I> mapBack, Map<I, Vv> mapper) {
		return new MapDecorating<>(mapBack, mapper);
	}

	public MapDecorating(Map<K, Intermediate> mapBack, Map<Intermediate, V> mapper) {
		this.mapper = mapper;
		setMapAndMapper(mapBack, mapper);
	}

	protected Map<K, Intermediate> mapBack;
	protected Map<Intermediate, V> mapper;

	protected static boolean checkNoRecursion(Map<?, ?> map) {
		MapDecorating<?, ?, ?> md;
		Map<?, ?> child;
		if (map instanceof MapDecorating<?, ?, ?>) {
			md = (MapDecorating<?, ?, ?>) map;
			child = md.mapBack;
			while (// child!= null
			(child instanceof MapDecorating<?, ?, ?>)) {
				if (child == md) {
					md.mapBack = null;
					return false;
				}
				child = ((MapDecorating<?, ?, ?>) child).mapBack;
			}
			return true;
		} else
			throw new IllegalArgumentException("The given map is not decorative.");
	}

	protected static boolean checkNoCrossRecursion(Map<?, ?> mapFather) {
		boolean canGo1, canGo2;
		MapDecorating<?, ?, ?> mf, md1, md2, child1, child2;
		Map<?, ?> map1, map2;
		if (mapFather instanceof MapDecorating<?, ?, ?>) {
			mf = ((MapDecorating<?, ?, ?>) mapFather);
			map1 = mf.mapBack;
			map2 = mf.mapper;
			if ((map2 instanceof MapDecorating<?, ?, ?>) && (map2 instanceof MapDecorating<?, ?, ?>)) {
				md1 = (MapDecorating<?, ?, ?>) map1;
				child1 = (MapDecorating<?, ?, ?>) md1.mapBack;
				md2 = (MapDecorating<?, ?, ?>) map2;
				child2 = (MapDecorating<?, ?, ?>) md2.mapBack;
				canGo1 = canGo2 = true;
				while (// child!= null
				(canGo1 && (canGo1 = (child1 instanceof MapDecorating<?, ?, ?>)))
						| (canGo2 && (canGo2 = (child2 instanceof MapDecorating<?, ?, ?>)))) {

					if (canGo1) {
						if ((child1 == mf) || (child1 == md1) || (child1 == md2)) {
							md1.mapBack = null;
							return false;
						} else
							child1 = (MapDecorating<?, ?, ?>) child1.mapBack;
					}
					if (canGo2) {
						if ((child2 == mf) || (child2 == md1) || (child2 == md2)) {
							md1.mapBack = null;
							return false;
						} else
							child2 = (MapDecorating<?, ?, ?>) child2.mapBack;
					}
				}
			}
			return true;
		} else
			throw new IllegalArgumentException("The given map is not decorative.");
	}

	protected boolean setMapAndMapper(Map<K, Intermediate> mapBack, Map<Intermediate, V> mapper) {
		this.mapBack = mapBack;
		this.mapper = mapper;
		if (!checkNoCrossRecursion(this)) {
			throw new IllegalArgumentException("The given maps would provoke circular recursion.");
		}
		return true;
	}

	@Override
	public int size() {
		return mapBack.size();
	}

	@Override
	public boolean isEmpty() {
		return mapBack.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return mapBack.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return mapBack.containsValue(value);
	}

	@Override
	public V get(Object key) {
		return mapper.get(mapBack.get(key));
	}

	/**
	 * The intermediate value must be added to the back-map before performing this
	 * operation.
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public V put(K key, V value) {
		Intermediate i;
		i = mapBack.get(key);
//		mapper.put(mapper.apply(key), value);
		return mapper.put(i, value);
	}

	@Override
	public V remove(Object key) {
		V v;
		v = mapper.remove(mapBack.get(key));
		mapBack.remove(key);
		return v;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		m.forEach((k, v) -> put(k, v));
//		mapBack.putAll(m);
	}

	@Override
	public void clear() {
		mapBack.clear();
		mapper.clear();
	}

	@Override
	public Set<K> keySet() {
		return mapBack.keySet();
	}

	@Override
	public Collection<V> values() {
		return mapper.values();
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		Set<Entry<K, V>> s;
//		Constructor<Entry<K, V>> con;
		s = new TreeSet<>();
//		con = Entry.class.getDeclaredConstructor(parameterTypes);
		mapBack.forEach((k, i) -> s.add(new EntryDecorating(k, mapper.get(i))));
		return s;
	}

	public V getOr(Object key, V value) {
		V v;
		v = mapper.get(mapBack.get(key));
		return v != null ? v : value;
	}

	@Override
	public void forEach(BiConsumer<? super K, ? super V> action) {
		mapBack.forEach((k, i) -> action.accept(k, mapper.get(i)));
	}

	@Override
	public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
//		mapBack.replaceAll(function);
		throw new UnsupportedOperationException("Cannot modify the backing-maps");
	}

	@Override
	public V putIfAbsent(K key, V value) {
//		return mapBack.putIfAbsent(key, value);
		throw new UnsupportedOperationException("Cannot modify the backing-maps");
	}

	@Override
	public boolean remove(Object key, Object value) {
		Intermediate i;
		i = mapBack.get(key);
		if (mapper.get(i) != null) {
			mapBack.remove(key);
			mapper.remove(i);
			return true;
		}
		return false;
	}

	@Override
	public boolean replace(K key, V oldValue, V newValue) {
		Intermediate i;
		V v;
		if (!mapBack.containsKey(key))
			return false;
		i = mapBack.get(key);
		v = mapper.get(i);
		if (!Objects.equals(v, oldValue))
			return false;
		mapper.remove(i);
		mapper.put(i, newValue);
		return true;
	}

	@Override
	public V replace(K key, V value) {
//		return mapBack.replace(key, value);
		throw new UnsupportedOperationException("Cannot modify the backing-maps");
	}

	@Override
	public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
		V v;
		Intermediate interm;
		v = null;
		if (key != null) {
			interm = mapBack.get(key);
			// if(interm!=null)
			v = mapper.get(interm);
		}
		if (v == null && mappingFunction != null) {
			try {
				v = mappingFunction.apply(key);
			} catch (Exception e) {
				throw e;
			}
		}
		return v;
	}

	@Override
	public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		if (!mapBack.containsKey(key))
			return null;
		return compute(key, remappingFunction);
	}

	@Override
	public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		V v, newv;
		Intermediate interm;
		newv = null;
		v = mapper.get(interm = mapBack.get(key));
		if (v != null && remappingFunction != null) {
			try {
				newv = remappingFunction.apply(key, v);
				if (newv == null) {
					mapBack.remove(key);
					mapper.remove(interm);
				} else if (v != newv) { // simple check to avoid useless computation
					mapper.remove(interm);
					mapper.put(interm, newv);
				}
			} catch (Exception e) {
				throw e;
			}
		}
		return newv;
	}

	@Override
	public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
		/*
		 * If the specified key is not already associated with a value or is associated
		 * with null, associates it with the given non-null value.
		 *
		 * Otherwise, replaces the associated value with the results of the given
		 * remapping function, or removes if the result is null. This method may be of
		 * use when combining multiple mapped values for a key. For example, to either
		 * create or append a String msg to a value mapping:
		 *
		 * map.merge(key, msg, String::concat) If the remapping function returns null,
		 * the mapping is removed. If the remapping function itself throws an
		 * (unchecked) exception, the exception is rethrown, and the current mapping is
		 * left unchanged.
		 */
		V v;
		Intermediate interm;
		v = null;
		interm = null;
		if (value != null) {
			if (mapBack.containsKey(key)) {
				v = mapper.get(interm = mapBack.get(key));
				if (v == null) {
					mapper.remove(interm);
					mapper.put(interm, value);
					return value;
				}
			} else {
				throw new UnsupportedOperationException(
						"Cannot merge if there is no mapping for the given key because a \"intermediate\" value is needed.");
			}
		}
		if (v == null)
			v = mapper.get(interm = mapBack.get(key));
		try {
			value = remappingFunction.apply(v, value);
		} catch (Exception e) {
			throw e;
		}
		if (value == null) {
			mapper.remove(interm);
			mapBack.remove(key);
			return null;
		}
		mapper.remove(interm);
		mapper.put(interm, value);
		return value;
	}

	//

	protected class EntryDecorating implements Serializable, Entry<K, V> {
		private static final long serialVersionUID = 98489408779653201L;

		public EntryDecorating(K key, V value) {
			super();
			this.key = key;
			this.value = value;
		}

		K key;
		V value;

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}

		public K setKey(K key) {
			K old;
			old = key;
			this.key = key;
			return old;
		}

		@Override
		public V setValue(V value) {
			V old;
			old = value;
			this.value = value;
			return old;
		}

	}

	//

	public static void main(String[] args) {

	}
}