package games.generic.view.guiSwing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EventListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import dataStructures.MapTreeAVL;
import tools.Comparators;
import tools.Comparators.MyComparator;

public abstract class ListBoxView<I> extends JScrollPane implements Iterable<I> {
	private static final long serialVersionUID = -180455L;

	/**
	 * Return a string that describes the item, as like as {@link Object#toString()}
	 * does.<br>
	 * By default, this returns <code>String.valueOf(this.getItem())</code>.
	 */
	public static interface DescriptionExtractor {
		public static final DescriptionExtractor DEFAULT_DescriptionExtractor = (o) -> String.valueOf(o);

		public String getDescription(Object item);
	}

	public static final Color COLOR_SELECTED = Color.BLUE;
	public static final Border BORDER_SELECTED = BorderFactory.createLineBorder(COLOR_SELECTED, 3),
			BORDER_UNSELECTED = LineBorder.createBlackLineBorder();
	public static final Dimension DEFAULT_DIMENSION = new Dimension(200, 35);

	public ListBoxView(MyComparator<I> comp) {
		this(comp, new // JLabel()
		JPanel(null));
	}

	private ListBoxView(MyComparator<I> comparator, JComponent view) {
		super(view, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		if (comparator == null)
			throw new IllegalArgumentException("Comparator is null");
		this.view = view;
		this.setViewportView(view);
		view.setLayout(null);
		selectItem = null;
		setBorderSelectedItem(BORDER_SELECTED);
		setBorderUnselectedItems(BORDER_UNSELECTED);
		setDescriptionExtractor(DescriptionExtractor.DEFAULT_DescriptionExtractor);
		this.comparator = comparator;
		allItems = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration, comparator);
		unselecter = new Unselecter();
		genericListenerItemVis = new LinkedList<>();
		adderEventListenerToJPIV = new AdderEventListenerToJPIV();
	}

	JComponent view;
	I selectItem;
	Map<I, /* ? extends */JPanelItemVisualizer> allItems;
	BiConsumer<I, JPanelItemVisualizer> resizer;
	MyComparator<I> comparator;
	Unselecter unselecter;
	// protected JPanelItemVisualizer selectedJPanel;
	Border borderSelectedItem, borderUnselectedItems;
	protected DescriptionExtractor descriptionExtractor;
	/*
	 * List<MouseListener> mouseListenerItemVis; List<MouseMotionListener>
	 * mouseMotionListenerItemVis; List<MouseWheelListener>
	 * mouseWheelListenerItemVis; List<KeyListener> keyListenerItemVis;
	 */
	List<EventListener> genericListenerItemVis;
	AdderEventListenerToJPIV adderEventListenerToJPIV;

	//

	//

	// TODO GETTER

	public I getSelectItem() { return selectItem; }

	public Unselecter getUnselecter() { return unselecter; }

	public Border getBorderSelectedItem() { return borderSelectedItem; }

	public Border getBorderUnselectedItems() { return borderUnselectedItems; }

	public DescriptionExtractor getItemDescriptionExtractor() { return descriptionExtractor; }

	public Comparator<? super I> comparator() { return comparator; }

	//

	// TODO SETTER

	public ListBoxView<I> setBorderSelectedItem(Border borderSelectedItem) {
		this.borderSelectedItem = borderSelectedItem == null ? BORDER_SELECTED : borderSelectedItem;
		return this;
	}

	public ListBoxView<I> setBorderUnselectedItems(Border borderUnselectedItems) {
		this.borderUnselectedItems = borderUnselectedItems;
		return this;
	}

	public ListBoxView<I> setDescriptionExtractor(DescriptionExtractor descriptionExtractor) {
		this.descriptionExtractor = descriptionExtractor == null ? DescriptionExtractor.DEFAULT_DescriptionExtractor
				: descriptionExtractor;
		return this;
	}

	//

	public abstract JPanelItemVisualizer newRow(I item);

	public boolean hasItems() { return !allItems.isEmpty(); }

	public int countItems() { return allItems.size(); }

	public void removeItems() {
		allItems.clear();
		view.removeAll();
		selectItem = null;
		repaint();
	}

	public boolean addNewItem(I item) {
		JPanelItemVisualizer jpe;
		if (item != null) {
			if (!allItems.containsKey(item)) {
				allItems.put(item, jpe = newRow(item));
				jpe.setVisible(true);
				// jpe.setSize(100, 50);
				jpe.updateItemShow();
				view.add(jpe);
				resizeAndRelocate();
			}
			return true;
		}
		return false;
	}

	public boolean removeItem(I item) {
		if (item != null) {
			if (allItems.containsKey(item)) {
				view.remove(allItems.remove(item));
				if (allItems.isEmpty())
					view.removeAll();
				resizeAndRelocate();
				return true;
			}
		}
		return false;
	}

	public JPanelItemVisualizer getItem(I item) { return allItems.get(item); }

	public boolean addListenerToItemVisualizers(EventListener el) {
		if (el != null) {
			if (genericListenerItemVis.size() <= 0 || (!genericListenerItemVis.contains(el))) {
				genericListenerItemVis.add(el);
				// add to prev item
				adderEventListenerToJPIV.el = el;
				if (allItems.size() > 0) { forEachJPanelItem(adderEventListenerToJPIV); }
			}
		}
		return false;
	}

	public List<I> toList() {
		ToLister tl;
		tl = null;
		if (allItems != null && allItems.size() > 0) {
			tl = new ToLister();
			allItems.forEach(tl);
			return tl.list;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public void forEachJPanelItem(Consumer</* ? extends */JPanelItemVisualizer> action) {
		if (allItems != null && allItems.size() > 0) {
			// tl = new ToLister();
			if (action instanceof BiConsumer<?, ?>)
				allItems.forEach((BiConsumer<I, ListBoxView<I>.JPanelItemVisualizer>) action);
			else
				allItems.forEach((BiConsumer<I, JPanelItemVisualizer>) (k, v) -> action.accept(v));
		}
		// else System.out.println("ERROR all items null or empty");
	}

	@Override
	public Iterator<I> iterator() { return this.allItems.keySet().iterator(); }

	// protected

	/* alla aggiunta/rimozione */
	protected void resizeAndRelocate() {
		Dimension d;
		ResizerAndRelocater resizer;
		resizer = new ResizerAndRelocater();
		allItems.forEach(resizer);
		if (resizer.prev != null) {
			d = resizer.d;
			d.height += (resizer.prev.getHeight()) >> 1;
			// this.view.setSize(resizer.prev.getWidth(), resizer.prev.getY() +
			// resizer.prev.getHeight());
			this.view.setSize(d);
			this.view.setPreferredSize(d);// this.view.getSize());
		}
		this.repaint();
	}

	protected void unselectAll() {
		allItems.forEach(unselecter);
		selectItem = null;
	}

	protected void setMeSelected(JPanelItemVisualizer jpiv) {
		unselectAll();
		jpiv.setBorder(getBorderSelectedItem());
		selectItem = jpiv.getItem();
		// System.out.println(this.getItemDescriptionExtractor().getItemDescription(selectItem));
	}

	protected void addAllListenersToJPIV(JPanelItemVisualizer jpiv) {
		if (genericListenerItemVis.size() > 0) {
			for (EventListener el : genericListenerItemVis) {
				adderEventListenerToJPIV.el = el;
				adderEventListenerToJPIV.accept(jpiv);
			}
		}
	}

	/*
	 * @Override public Dimension getPreferredSize() { Dimension d, dv; d =
	 * super.getPreferredSize(); dv = view.getPreferredSize(); d.width =
	 * Math.min(d.width, dv.width); d.height = Math.min(d.height, dv.height); return
	 * d; }
	 */

	// TODO STATIC

	public static <I> ListBoxView<I> getDefaultImplementation(MyComparator<I> comp) {
		return new ListBoxView_DEFAULT<I>(comp);
	}

	// TODO CLASSES

	class Unselecter implements BiConsumer<I, JPanelItemVisualizer> {

		Unselecter() {}

		@Override
		public void accept(I key, JPanelItemVisualizer value) { value.setBorder(getBorderUnselectedItems()); }
	}

	class ToLister implements BiConsumer<I, JPanelItemVisualizer> {
		List<I> list;

		ToLister() { list = new /* Linked */ArrayList<>(); }

		@Override
		public void accept(I key, JPanelItemVisualizer value) { list.add(key); }
	}

	class ToListerJPanelItemVisualizer implements BiConsumer<I, JPanelItemVisualizer> {
		List<JPanelItemVisualizer> list;

		ToListerJPanelItemVisualizer() { list = new /* Linked */ArrayList<>(); }

		@Override
		public void accept(I key, JPanelItemVisualizer value) { list.add(value); }
	}

	public abstract class JPanelItemVisualizer extends JPanel {
		private static final long serialVersionUID = 503159561L;

		public JPanelItemVisualizer(I item) { this(item, new FlowLayout()); }

		public JPanelItemVisualizer(I item, LayoutManager layout) {
			super(layout);
			this.item = item;
			this.addMouseListener(onMouseClickSelectMe = new OnMouseClickSelectMe(this));
			initGUI();
			addAllListenersToJPIV(this);
			this.setBorder(getBorderUnselectedItems());
			// setItem(item);
			updateItemShow();
		}

		I item;
		OnMouseClickSelectMe onMouseClickSelectMe;

		public I getItem() { return item; }

		public void setItem(I item) {
			this.item = item;
			updateItemShow();
		}

		public OnMouseClickSelectMe getOnMouseClickSelectMe() { return onMouseClickSelectMe; }

		/** Aggiorna il contenuto di item da visualizzare */
		public abstract void updateItemShow();

		public abstract void initGUI();

		/**
		 * Return a string that describes the item, as like as {@link Object#toString()}
		 * does.<br>
		 */
		public String getItemDescription() { return getItemDescriptionExtractor().getDescription(this.getItem()); }

		@Override
		public void addMouseListener(MouseListener ml) {
			super.addMouseListener(ml);
			for (Component c : this.getComponents())
				c.addMouseListener(ml);
		}

		@Override
		public void addMouseMotionListener(MouseMotionListener ml) {
			super.addMouseMotionListener(ml);
			for (Component c : this.getComponents())
				c.addMouseMotionListener(ml);
		}

		@Override
		public void addMouseWheelListener(MouseWheelListener ml) {
			super.addMouseWheelListener(ml);
			for (Component c : this.getComponents())
				c.addMouseWheelListener(ml);
		}

		@Override
		public void addKeyListener(KeyListener ml) {
			super.addKeyListener(ml);
			for (Component c : this.getComponents())
				c.addKeyListener(ml);
		}
	}// fine classe JPanelItemVisualizer

	public class OnMouseClickSelectMe extends MouseAdapter {
		public OnMouseClickSelectMe(JPanelItemVisualizer jpiv) {
			super();
			this.jpiv = jpiv;
		}

		// ListBoxView<?> lbv;
		JPanelItemVisualizer jpiv;

		@Override
		public void mouseClicked(MouseEvent e) { setMeSelected(jpiv); }

	}

	protected class ResizerAndRelocater implements BiConsumer<I, JPanelItemVisualizer> {
		JPanelItemVisualizer prev;
		Dimension d;

		ResizerAndRelocater() { prev = null; }

		@Override
		public void accept(I key, ListBoxView<I>.JPanelItemVisualizer value) {
			if (prev == null) {
				value.setLocation(0, 0);
				d = value.getSize();// Preferred
			} else {
				int t;
				Dimension dd, ps;
				value.setLocation(0, prev.getY() + prev.getHeight());

				dd = d;
				ps = value.getPreferredSize();// Preferred
				if (dd.width < (t = (value.getX() + ps.width))) { dd.width = t; }
				if (dd.height < (t = (value.getY() + ps.height))) { dd.height = t; }
			}
			prev = value;
		}
	}

	protected class AdderEventListenerToJPIV implements Consumer</* ? extends */JPanelItemVisualizer>,
			BiConsumer<I, JPanelItemVisualizer>, Serializable {
		private static final long serialVersionUID = 48950516022030L;
		private EventListener el;

		AdderEventListenerToJPIV() { this(null); }

		AdderEventListenerToJPIV(EventListener el) { this.el = el; }

		void setEventListener(EventListener el) { if (el != null) { this.el = el; } }

		@Override
		public void accept(ListBoxView<I>.JPanelItemVisualizer t) {
			if (el instanceof MouseListener)
				t.addMouseListener((MouseListener) el);
			if (el instanceof MouseMotionListener)
				t.addMouseMotionListener((MouseMotionListener) el);
			if (el instanceof MouseWheelListener)
				t.addMouseWheelListener((MouseWheelListener) el);
			if (el instanceof KeyListener)
				t.addKeyListener((KeyListener) el);
			if (el instanceof ComponentListener)
				t.addComponentListener((ComponentListener) el);
			if (el instanceof ContainerListener)
				t.addContainerListener((ContainerListener) el);
		}

		@Override
		public void accept(I key, ListBoxView<I>.JPanelItemVisualizer value) { accept(value); }
	}

	//

	// TODO DEFAULT EXTENSION

	public static class ListBoxView_DEFAULT<I> extends ListBoxView<I> {
		private static final long serialVersionUID = 621031520150L;

		public ListBoxView_DEFAULT(MyComparator<I> comp) { super(comp); }

		@Override
		public JPanelItemVisualizer newRow(I item) { return new DefaultJPanelVisual(item); }

		public class DefaultJPanelVisual extends JPanelItemVisualizer {

			private static final long serialVersionUID = 11111214522L;

			DefaultJPanelVisual(I item) { super(item); }

			JLabel jl = null;

			@Override
			public void setSize(int width, int height) {
				super.setSize(width, height);
				this.jl.setSize(width, height);
			}

			@Override
			public void setPreferredSize(Dimension d) {
				super.setPreferredSize(d);
				this.jl.setPreferredSize(d);
			}

			@Override
			public void updateItemShow() { if (jl != null) { jl.setText(getItemDescription()); } }

			@Override
			public void initGUI() {
				this.setLayout(new GridLayout(0, 1));
				jl = new JLabel();
				jl.addMouseListener(getOnMouseClickSelectMe());
				this.add(jl);
				this.setSize(DEFAULT_DIMENSION);
				this.setVisible(true);
				// super.setItem(item);
			}
		}
	}

	//

	public static void main(String[] args) {
		int i, MAX;
		ListBoxView<Integer> l;
		JFrame fin;
		Random r;

		l = ListBoxView.getDefaultImplementation(Comparators.INTEGER_COMPARATOR);

		fin = new JFrame("axfjsbgdfvhjsbdfv");
		fin.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		fin.add(l);
		fin.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				Dimension d;
				d = fin.getSize();
				d.height -= 25;
				l.setSize(d);
			}
		});
		fin.setSize(500, 500);
		fin.setVisible(true);

		r = new Random();
		MAX = 25;
		i = -1;
		System.out.println("START");
		while (++i < MAX) {
			l.addNewItem(r.nextInt());
		}
		System.out.println("END");
	}
}