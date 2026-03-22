package games.generic.controlModel.items;

import java.awt.Dimension;
import java.awt.Point;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import dataStructures.MapMapped;
import dataStructures.MapTreeAVL;
import games.generic.controlModel.GModality;
import games.generic.controlModel.holders.GModalityHolder;
import games.generic.controlModel.subimpl.GModalityRPG;
import tools.Comparators;

public abstract class InventoryItems implements GModalityHolder {

	public static enum ItemsAutomaticArrangementOrder {
		HorizontallyBiggerFirst, HorizontallySmallerFirst, VerticallyBiggerFirst, VerticallySmallerFirst,
	}

	public InventoryItems() { this(0); }

	public InventoryItems(int level) {
		this.level = level;
		this.automaticItemArrangementOrder = ItemsAutomaticArrangementOrder.VerticallyBiggerFirst;
		this.inventorySize = levelToInventoryDimension(level);
		this.inventoryGrid = new InventoryItem[this.inventorySize.height][this.inventorySize.width];
		this.allItemsLocated = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
				Comparators.LONG_COMPARATOR);
		MapMapped<Long, ItemInInventory, InventoryItem> mmItems;
		mmItems = new MapMapped<>(allItemsLocated, ItemInInventory::getItem);
		this.allItems = mmItems;
		mmItems.setReverseMapper(item -> {
			if (this.allItemsLocated.containsKey(item.getID())) {
				return this.allItemsLocated.get(item.getID());
			} else {
				ItemInInventory iii;
				iii = new ItemInInventory(item, -1, -1);
				this.allItemsLocated.put(item.getID(), iii);
				return iii;
			}
		});
		this.emptyGrid();
	}

	protected int level;
	protected Dimension inventorySize;
	protected InventoryItem[][] inventoryGrid;
	protected GModality gameModality;
	protected ItemsAutomaticArrangementOrder automaticItemArrangementOrder;
	protected final MapTreeAVL<Long, ItemInInventory> allItemsLocated;
	protected final Map<Long, InventoryItem> allItems;

	/**
	 * The size of the inventory, since it's a grid.
	 *
	 * @return
	 */
	public Dimension getInventorySize() { return inventorySize; }

	/**
	 * TODO BEWARE OF NOT TO MODIFY THE ROWS, should use {@link #forEachCell}
	 * instead;
	 */
	public InventoryItem[][] getInventoryGrid() { return inventoryGrid; }

	/**
	 * The size may depend on the level, which is returned.
	 *
	 * @return
	 */
	public int getLevelInventory() { return level; }

	@Override
	public GModality getGameModality() { return gameModality; }

	public ItemsAutomaticArrangementOrder getAutomaticItemArrangementOrder() { return automaticItemArrangementOrder; }

	public Map<Long, InventoryItem> getAllItems() { return allItems; }

	public Map<Long, ItemInInventory> getAllItemsLocated() { return allItemsLocated; }

	//

	public void setAutomaticItemArrangementOrder(ItemsAutomaticArrangementOrder automaticItemArrangementOrder) {
		if (automaticItemArrangementOrder != null) {
			this.automaticItemArrangementOrder = automaticItemArrangementOrder;
		}
	}

	@Override
	public void setGameModality(GModality gameModality) { this.gameModality = gameModality; }

	//

	protected void setLevel(int l) {
		if (l < 0) { l = 0; }
		this.level = l;
		this.inventorySize = levelToInventoryDimension(l);
		this.inventoryGrid = new InventoryItem[this.inventorySize.height][this.inventorySize.width];
	}

	/**
	 * Add the item without any check on the region denoted by the point (first two
	 * parameters) and {@link InventoryItem#getDimensionInInventory()}.
	 *
	 * @param row
	 * @param column
	 * @param item
	 */
	protected void addItemImpl(int row, int column, InventoryItem item) {
		int w, h;
		Dimension d;
		InventoryItem[] rowTemp;

		d = item.getDimensionInInventory();
		w = d.width;
		h = d.height;
		item.setLocationInInventory(new Point(column, row));
		for (int r = 0; r < h; r++) {
			rowTemp = this.inventoryGrid[row + r];
			for (int c = 0; c < w; c++) {
				rowTemp[column + c] = item;
			}
		}
		this.allItemsLocated.put(item.getID(), new ItemInInventory(item, column, row));
	}

	protected void removeItemImpl(InventoryItem item) {
		int w, h, row, column;
		Dimension d;
		Point locInInvent;
		InventoryItem[] rowTemp;

		d = item.getDimensionInInventory();
		locInInvent = item.getLocationInInventory();
		item.setLocationInInventory(null);
		row = locInInvent.y;
		column = locInInvent.x;
		w = d.width;
		h = d.height;
		// null-ify the area
		for (int r = 0; r < h; r++) {
			rowTemp = this.inventoryGrid[row + r];
			for (int c = 0; c < w; c++) {
				rowTemp[column + c] = null;
			}
		}
		allItemsLocated.remove(item.getID());
	}

	public boolean hasItem(InventoryItem item) { return this.allItems.containsKey(item.getID()); }

	/**
	 *
	 * @param item
	 * @return the location inside the inventory grid of the top-left corner of the
	 *         shape of the provided {@link InventoryItem}. {@code null} if the
	 *         addition has failed or the item already exists.
	 */
	public Point addItem(InventoryItem item) {
		Long id;
		Point additionLocation;
		if (item == null || this.allItems.containsKey(id = item.getID())) { return null; }
		if (this.automaticItemArrangementOrder == ItemsAutomaticArrangementOrder.HorizontallyBiggerFirst
				|| this.automaticItemArrangementOrder == ItemsAutomaticArrangementOrder.HorizontallySmallerFirst) {
			additionLocation = addAutomaticallyHorizontally(item);
		} else {
			additionLocation = addAutomaticallyVertically(item);
		}
		if (additionLocation != null && (!this.allItems.containsKey(id))) {
			this.allItemsLocated.put(id, new ItemInInventory(item, additionLocation.x, additionLocation.y));
			this.allItems.put(item.getID(), item);
		}
		return additionLocation;
	}

	public boolean removeItem(InventoryItem item) {
		if (item == null || (!this.allItemsLocated.containsKey(item.getID()))) { return false; }
		this.removeItemImpl(item);
//		allItems.remove(item.getID());
		return true;
	}

	//

	/**
	 * Defines the size (in {@link Dimension}) of this inventory depending on the
	 * provided level.
	 *
	 * @param levelTarget
	 * @return
	 */
	public abstract Dimension levelToInventoryDimension(int levelTarget);

	/**
	 * Calls {@link #resizeBy(Dimension)} using
	 * {@link #levelToInventoryDimension(int)}.
	 */
	public List<InventoryItem> resizeBy(int lev) { return resizeBy(levelToInventoryDimension(lev)); }

	/**
	 * Resize the current inventory to the provided dimension, if not null.<br>
	 * Returns the items that do no more fits. An automatic relocation may occur, if
	 * available.
	 *
	 * <p>
	 * The invoker of this method should call
	 * {@link GModalityRPG#dropItem(InventoryItem)} on the returned
	 * {@link InventoryItem}s (used to drop the object to the ground).
	 *
	 * @return all the dropped and removed items, if any
	 */
	public List<InventoryItem> resizeBy(Dimension d) {
		int w;
		InventoryItem[][] newGrid, originalGrid;
		MapTreeAVL<Long, InventoryItem> itemsToDropCollector;

		if (d == null || d.height <= 0 || (w = d.width) <= 0 || d.equals(this.inventorySize)) { return null; }
		if (allItems.isEmpty()) {
			this.inventorySize = d;
			this.inventoryGrid = new InventoryItem[d.height][w];
			return null;
		}

		itemsToDropCollector = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
				MapTreeAVL.BehaviourOnKeyCollision.KeepPrevious, Comparators.LONG_COMPARATOR);
		originalGrid = this.inventoryGrid;
		if (this.inventorySize.width == w) {
			newGrid = new InventoryItem[d.height][];
			// just change the array of this.inventorySize
			if (d.height >= this.inventorySize.height) {
				// just grow
				System.arraycopy(originalGrid, 0, newGrid, 0, originalGrid.length);
				for (int i = originalGrid.length; i < newGrid.length; i++) {
					newGrid[i] = new InventoryItem[w];
				}
			} else { // assert (this.inventorySize.height < d.height)
				System.arraycopy(originalGrid, 0, newGrid, 0, d.height);

				// clean the area at the bottom
				collectItemsToDropInArea(itemsToDropCollector, newGrid, 0, originalGrid.length,
						d.height - originalGrid.length, w);
			}
		} else {
			final int heightAreaCopied, widthAreaCopied;
			newGrid = new InventoryItem[d.height][w];

			// copy the area to be preserved
			heightAreaCopied = Math.min(d.height, originalGrid.length);
			widthAreaCopied = Math.min(w, this.inventorySize.width);
			for (int r = heightAreaCopied - 1; r >= 0; r--) {
				System.arraycopy(originalGrid[r], 0, newGrid[r], 0, widthAreaCopied);
			}

			// then collect any item that does not fits

			if (w < this.inventorySize.width) {
				collectItemsToDropInArea(itemsToDropCollector, originalGrid, //
						w, 0, //
						heightAreaCopied, this.inventorySize.width - w);
			}
			if (d.height < this.inventorySize.height) {
				collectItemsToDropInArea(itemsToDropCollector, originalGrid, //
						0, heightAreaCopied, //
						this.inventorySize.height - d.height, this.inventorySize.width);
			}
			// the rest is just new fresh area
		}

		this.inventoryGrid = newGrid;
		this.inventorySize = d;

		// TODO "GModalityRPG.drop" all exceeding items

		if (itemsToDropCollector.isEmpty()) { return null; }
		{
			// try to re-locate someone
			final LinkedList<Long> relocated;
			relocated = new LinkedList<>();
			itemsToDropCollector.forEach((id, item) -> {
				Point newLocation;
				removeItem(item);
				newLocation = addItem(item);
				if (newLocation != null && newLocation.x >= 0 && newLocation.y >= 0) {
					// addition NOT failed: it's relocated, then do NOT drop it
					relocated.add(item.getID());
				}
			});
			// forget about the relocated items
			relocated.forEach(itemsToDropCollector::remove);
		}
		final int[] index = { 0 };
		final InventoryItem[] itemsToDrop;
		itemsToDrop = new InventoryItem[itemsToDropCollector.size()];
		itemsToDropCollector.forEach((id, item) -> {
			itemsToDrop[index[0]++] = item;
			removeItemImpl(item);
		});
		return Arrays.asList(itemsToDrop);
	}

	/**
	 *
	 * @param itemsToDropCollector
	 * @param grid
	 * @param xStart
	 * @param yStart
	 * @param height
	 * @param width
	 */
	protected void collectItemsToDropInArea(MapTreeAVL<Long, InventoryItem> itemsToDropCollector,
			InventoryItem[][] grid, int xStart, int yStart, int height, int width) {
		int h, w, x, y;
		InventoryItem item, row[];
		Map<Long, ItemInInventory> locations;
		ItemInInventory iii;
		Long idItem;

		locations = this.allItemsLocated;
		for (h = 0; h < height; h++) {
			row = grid[y = yStart + h];
			for (w = 0; w < width; w++) {
				item = row[x = xStart + w];
				if (item != null) {
					iii = locations.get(idItem = item.getID());
					if (itemsToDropCollector.containsKey(idItem)) {
						// already collected: skip its width (at least)
						w += item.getDimensionInInventory().width - 1;
						// "-1" because of the "w++" at the end of the cycle
					} else {
						if (y != iii.yLocationInInventory || x != iii.xLocationInInventory) {
							/*
							 * the item has to be removed since the "space" it lies into (in the grid) is
							 * going to be erased (due to "resizeBy")
							 */
							itemsToDropCollector.put(idItem, item);
						}
					}
				}
			}
		}
	}

	/**
	 * Removes ALL objects stored
	 */
	public void emptyGrid() {
		for (InventoryItem[] rows : this.inventoryGrid) {
			for (int i = rows.length - 1; i >= 0; i--) {
				rows[i] = null;
			}
		}
	}

	public void forEachCell(InventoryCellConsumer consumer) {
		InventoryItem[] row, invent[];
		Point p;
		p = new Point(0, 0);
		invent = this.inventoryGrid;
		for (int r = 0, rr = invent.length; r < rr; r++) {
			p.y = r;
			p.x = 0;
			row = invent[r];
			for (int c = 0, cc = row.length; c < cc; c++) {
				p.x = c;
				consumer.perform(p, row[c]);
			}
		}
	}

	public void forEachItem(Consumer<InventoryItem> consumer) { this.allItems.forEach((k, it) -> consumer.accept(it)); }

	protected void sort(InventoryItem[] items) {
		Comparator<InventoryItem> itemComp;
		switch (this.automaticItemArrangementOrder) {
		case HorizontallyBiggerFirst: {
			itemComp = (i1, i2) -> {
				if (i1 == i2) { return 0; }
				if (i1 == null) { return -1; }
				if (i2 == null) { return 1; }
				return Comparators.DIMENSION_COMPARATOR_DESCENDING_WIDTH_FIRST.compare(i1.getDimensionInInventory(),
						i2.getDimensionInInventory());
			};
			break;
		}
		case HorizontallySmallerFirst: {
			itemComp = (i1, i2) -> {
				if (i1 == i2) { return 0; }
				if (i1 == null) { return -1; }
				if (i2 == null) { return 1; }
				return -Comparators.DIMENSION_COMPARATOR_DESCENDING_WIDTH_FIRST.compare(i1.getDimensionInInventory(),
						i2.getDimensionInInventory());
			};
			break;
		}
		case VerticallyBiggerFirst: {
			itemComp = (i1, i2) -> {
				if (i1 == i2) { return 0; }
				if (i1 == null) { return -1; }
				if (i2 == null) { return 1; }
				return Comparators.DIMENSION_COMPARATOR_DESCENDING_HEIGHT_FIRST.compare(i1.getDimensionInInventory(),
						i2.getDimensionInInventory());
			};
			break;
		}
		case VerticallySmallerFirst: {
			itemComp = (i1, i2) -> {
				if (i1 == i2) { return 0; }
				if (i1 == null) { return -1; }
				if (i2 == null) { return 1; }
				return -Comparators.DIMENSION_COMPARATOR_DESCENDING_HEIGHT_FIRST.compare(i1.getDimensionInInventory(),
						i2.getDimensionInInventory());
			};
			break;
		}
		default:
			throw new IllegalArgumentException(
					"Unexpected ItemsAutomaticArrangementOrder : " + this.automaticItemArrangementOrder);
		}
		Arrays.sort(items, itemComp);
	}

	public void compact() {
		int[] index = { 0 };
		InventoryItem items[];
		if (allItems.isEmpty()) { return; }

		items = new InventoryItem[allItems.size()];
		allItems.forEach((k, item) -> { items[index[0]++] = item; });

		this.sort(items);

		if (this.automaticItemArrangementOrder == ItemsAutomaticArrangementOrder.HorizontallyBiggerFirst
				|| this.automaticItemArrangementOrder == ItemsAutomaticArrangementOrder.HorizontallySmallerFirst) {
			for (InventoryItem item : items) {
				addAutomaticallyHorizontally(item);
			}
		} else {
			for (InventoryItem item : items) {
				addAutomaticallyVertically(item);
			}
		}
	}

	/**
	 *
	 * @param item
	 * @return the location inside the inventory grid of the top-left corner of the
	 *         shape of the provided {@link InventoryItem}. {@code null} if the
	 *         addition has failed.
	 */
	public Point addAutomaticallyHorizontally(InventoryItem item) {
		boolean notAddable = true, canBeAdded;
		int rStart, cStart, r, c, rows, cols;
		Dimension d;
		InventoryItem[] row, rowTemp;
		d = item.getDimensionInInventory();
		rStart = cStart = 0;
		rows = this.inventoryGrid.length - d.height;
		while (notAddable && rStart < rows) {
			row = this.inventoryGrid[rStart];
			cols = row.length - d.width;
			cStart = 0;
			while (notAddable && cStart < cols) {
				// scan the "inventory size": if any non-null Item exists in that region, then
				// "notAddable" = true, else false
				r = 0;
				canBeAdded = true;// assumption
				do {
					c = 0;
					rowTemp = this.inventoryGrid[rStart + r];
					while ((canBeAdded = (rowTemp[cStart + c++] == null)) && c <= d.width)
						;
				} while (canBeAdded && ++r <= d.height);
				notAddable = !canBeAdded;
				if (notAddable) { cStart++; }
			}
			if (notAddable) { rStart++; }
		}
		if (!notAddable) {
			this.addItemImpl(rStart, cStart, item);
			return new Point(cStart, rStart);
		} else {
			return null;
		}
	}

	/**
	 *
	 * @param item
	 * @return the location inside the inventory grid of the top-left corner of the
	 *         shape of the provided {@link InventoryItem}. {@code null} if the
	 *         addition has failed.
	 */
	public Point addAutomaticallyVertically(InventoryItem item) {
		boolean notAddable = true, canBeAdded;
		int rStart, cStart, r, c, rows, cols;
		Dimension d;
		InventoryItem[] rowTemp;
		d = item.getDimensionInInventory();
		rStart = cStart = 0;
		rows = this.inventoryGrid.length - d.height;
		cols = this.inventoryGrid[0].length - d.width;
		cStart = 0;
		while (notAddable && cStart < cols) {
			rStart = 0;
			while (notAddable && rStart < rows) {
				// scan the "inventory size": if any non-null Item exists in that region, then
				// "notAddable" = true, else false
				r = 0;
				canBeAdded = true;// assumption
				do {
					c = 0;
					rowTemp = this.inventoryGrid[rStart + r];
					while ((canBeAdded = (rowTemp[cStart + c++] == null)) && c <= d.width)
						;
				} while (canBeAdded && ++r <= d.height);
				notAddable = !canBeAdded;
				if (notAddable) { rStart++; }
			}
			if (notAddable) { cStart++; }
		}
		if (!notAddable) {
			this.addItemImpl(rStart, cStart, item);
			return new Point(cStart, rStart);
		} else {
			return null;
		}
	}

//	public set at, get at, place at, swap, add automatically, etc

	public InventoryItem getItemAt(Point p) { return this.getItemAt(p.y, p.x); }

	public InventoryItem getItemAt(int row, int column) {
		InventoryItem[] rowGrid;
		rowGrid = this.inventoryGrid[row];
		return rowGrid[column];
	}

	//

	public static interface InventoryCellConsumer {
		/**
		 *
		 * @param row         first parameter, the row of the grid
		 * @param column      second parameter, the column of the grid
		 * @param cellContent the actual content of the pointed cell. May be
		 *                    {@code null}.
		 */
		public void perform(int row, int column, InventoryItem cellContent);

		/**
		 * Beware: the point may be recycled, do a copy before comparison between
		 * different invocations of this method.
		 *
		 * @param p
		 * @param cellContent
		 */
		public default void perform(Point p, InventoryItem cellContent) { this.perform(p.x, p.y, cellContent); }
	}

	public static class ItemInInventory implements Serializable {
		private static final long serialVersionUID = 568794582003450L;

		public ItemInInventory(InventoryItem item, int xLocationInInventory, int yLocationInInventory) {
			super();
			this.item = item;
			this.xLocationInInventory = xLocationInInventory;
			this.yLocationInInventory = yLocationInInventory;
		}

		protected int xLocationInInventory, yLocationInInventory;
		protected InventoryItem item;

		//

		public int getxLocationInInventory() { return xLocationInInventory; }

		public int getyLocationInInventory() { return yLocationInInventory; }

		public InventoryItem getItem() { return item; }

		//

		public void setxLocationInInventory(int xLocationInInventory) {
			this.xLocationInInventory = xLocationInInventory;
		}

		public void setyLocationInInventory(int yLocationInInventory) {
			this.yLocationInInventory = yLocationInInventory;
		}

		public void setItem(InventoryItem item) { this.item = item; }
	}
}
