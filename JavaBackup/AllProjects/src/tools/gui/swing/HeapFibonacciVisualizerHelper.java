package tools.gui.swing;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import dataStructures.HeapFibonacci;
import dataStructures.HeapFibonacci.NodeHF;

public class HeapFibonacciVisualizerHelper {

	/**
	 * Functino that display the data stored in a {@link HeapFibonacci.NodeHF} and
	 * located using a {@link LocationData}.
	 */
	public static interface DataDisplayer<T> {
		public void displayDataAt(Map<Integer, LocationData<T>> mapLocations, T data, LocationData<T> location);
	}

	public static <T> ArrayList<LinkedList<LocationData<T>>> displayData(HeapFibonacci<T> h,
			DataDisplayer<T> dataDisplayer) {
		Map<Integer, LocationData<T>> nodesLocations;

		nodesLocations = getLocations(h);

		ArrayList<LinkedList<LocationData<T>>> matrix;
		matrix = new ArrayList<>(16);
		nodesLocations.forEach((id, loc) -> {
			LinkedList<LocationData<T>> horizontalList;
			if (matrix.isEmpty() || matrix.size() >= loc.y || matrix.get(loc.y) == null) {
				horizontalList = new LinkedList<>();
				matrix.add(loc.y, horizontalList);
			} else {
				horizontalList = matrix.get(loc.y);
			}
			horizontalList.add(loc);
		});

		matrix.forEach(l -> {
			l.forEach(loc -> {
				dataDisplayer.displayDataAt(nodesLocations, loc.node.getData(), loc);
			});
		});

		return matrix;
	}

	protected static <T> void fillMap(Map<Integer, LocationData<T>> m, HeapFibonacci.NodeHF<T> nodeCurrent,
			HFVHContext<T> c, LocationData<T> fatherLocation) {
		final LocationData<T> loc;

		loc = new LocationData<T>(c.id++, nodeCurrent, c.x, c.y, fatherLocation);
		m.put(loc.ID, loc);

		if (nodeCurrent.getChildrenAmount() > 0) {
			c.y++;
			nodeCurrent.forEachChild(n -> fillMap(m, n, c, loc));
			c.y--;
		}
		c.x++;
	}

	/**
	 * Assign to each node (and value) a location in space, evenly distributed, so
	 * that heach data-value can be drawn as a single node. The linked father can be
	 * retrieved through {@link LocationData#getID()}.
	 */
	public static <T> Map<Integer, LocationData<T>> getLocations(HeapFibonacci<T> heap) {
		Map<Integer, LocationData<T>> m;
		HFVHContext<T> c;
		if (heap == null || heap.isEmpty()) {
			return null;
		}
		m = new TreeMap<>(Integer::compareTo);
		c = new HFVHContext<>();
//		heap.forEachRoots(new MapFiller<>(m, heap, c, null));
		heap.forEachRoots(n -> fillMap(m, n, c, null));
		return m;
	}

//	protected static class MapFiller<T> implements Consumer<HeapFibonacci.NodeHF<T>> {
//		final Map<Integer, LocationData<T>> m;
//		final HeapFibonacci<T> heap;
//		final HFVHContext<T> c;
//		final LocationData<T> fatherLocation;
//
//		public MapFiller(Map<Integer, LocationData<T>> m, HeapFibonacci<T> heap, HFVHContext<T> c,
//				LocationData<T> fatherLocation) {
//			super();
//			this.m = m;
//			this.heap = heap;
//			this.c = c;
//			this.fatherLocation = fatherLocation;
//		}
//
//		@Override
//		public void accept(NodeHF<T> n) {
//			{
//				LocationData<T> loc;
//
//				loc = new LocationData<T>(c.id++, n, c.x, c.y, fatherLocation);
//				m.put(loc.ID, loc);
//
//				if (n.getChildrenAmount() > 0) {
//					c.y++;
//					n.forEachChild(new MapFiller<T>(m, heap, c, loc));
//				}
//
//				c.x++;
//			}
//		}
//	}

	protected static class HFVHContext<T> {
		protected int id, x, y;

		public HFVHContext() {
			super();
			this.id = this.x = this.y = 0;
		}

	}

	public static class LocationData<T> {
		public final int x, y;
		public final Integer ID;
		public final HeapFibonacci.NodeHF<T> node;
		public final LocationData<T> fatherLocation;

		public LocationData(Integer iD, NodeHF<T> node, int x, int y, LocationData<T> fatherLocation) {
			super();
			ID = iD;
			this.node = node;
			this.x = x;
			this.y = y;
			this.fatherLocation = fatherLocation;
		}

		/**
		 * @return
		 * @see dataStructures.HeapFibonacci.NodeHF#getData()
		 */
		public T getData() {
			return node.getData();
		}

		/**
		 * @return the iD
		 */
		public Integer getID() {
			return ID;
		}

		/**
		 * @return the node
		 */
		public HeapFibonacci.NodeHF<T> getNode() {
			return node;
		}

		/**
		 * @return the x
		 */
		public int getX() {
			return x;
		}

		/**
		 * @return the y
		 */
		public int getY() {
			return y;
		}
	}
}
