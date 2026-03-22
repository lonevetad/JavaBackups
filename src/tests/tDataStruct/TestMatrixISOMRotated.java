package tests.tDataStruct;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import dataStructures.isom.MultiISOMPolygonalSubareas;
import dataStructures.isom.NodeIsom;
import dataStructures.isom.matrixBased.MISOM_SingleObjInNode;
import geometry.ObjectLocated;
import tools.GraphicTools;
import tools.NumberManager;
import tools.UniqueIDProvider;

public class TestMatrixISOMRotated {
	static final UniqueIDProvider uidp = UniqueIDProvider.newBasicIDProvider();
	static final int PIXEL_EACH_CELL = 25;

	//

	JFrame win;
	JPanel /* jp, jpNorthPart, */ jpDrawMultimap;
	//
	MultiISOMPolygonalSubareas<Double> multiPoly;

	//

	protected TestMatrixISOMRotated() {
		super();
		multiPoly = new MultiISOMPolygonalSubareas<>(4);
	}

	void buildAndShowGUI() {
//		KeyAdapter ka;
		win = new JFrame("Test Multi ISOM");
		win.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		jpDrawMultimap = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				paintRectsAndSections(g);
			}
		};
		win.add(jpDrawMultimap);
		win.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				Dimension d;
				d = win.getSize();
				d.width -= 10;
				d.height -= 35;
				jpDrawMultimap.setLocation(0, 0);
				jpDrawMultimap.setSize(d);
				jpDrawMultimap.setPreferredSize(d);
			}
		});
		MouseAdapter ma;
		ma = new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				Point p;
				p = e.getPoint();
				p.x /= PIXEL_EACH_CELL;
				p.y /= PIXEL_EACH_CELL;
//			applyMultiMapOffset(p);
				win.setTitle("Mouse at: (x: " + p.x + ", y: " + p.y + ")");
			}
		};
		win.addMouseMotionListener(ma);

		win.setSize(500, 500);
		win.setVisible(true);
	}

	// TODO paint
	void paintRectsAndSections(Graphics g) {
		multiPoly.forEachNode((n, p) -> printNode(g, n, p));
		g.setColor(Color.BLACK);
		GraphicTools.paintGrid(g, PIXEL_EACH_CELL * jpDrawMultimap.getWidth(),
				PIXEL_EACH_CELL * jpDrawMultimap.getHeight(), PIXEL_EACH_CELL);
	}

	void printNode(Graphics g, NodeIsom<Double> node, Point location) {
		node.forEachHeldObject(ol -> {
			ColoredOL col;
			col = (ColoredOL) ol;
			g.setColor((ol == null) ? Color.LIGHT_GRAY : col.color);
			g.fillRect(location.x * PIXEL_EACH_CELL, location.y * PIXEL_EACH_CELL, PIXEL_EACH_CELL, PIXEL_EACH_CELL);
		});
	}

	//

	//

	//

	//

	//

	public static void main(String[] args) {
		double rotationDegree;
		TestMatrixISOMRotated t;
		ColoredOL[] points;
		t = new TestMatrixISOMRotated();
		System.out.println("Start");
		//
		rotationDegree = 0.0;
//		rotationDegree = 57.5;

		t.multiPoly.addMap(//
				new MISOM_SingleObjInNode<Double>(false, 12, 8, NumberManager.getDoubleManager())
				//
				, 3, 7, rotationDegree);

		// check the add
		System.out.println("check maps added:");
		t.multiPoly.getIsomsHeldCenterLocated().forEach(
				e -> { System.out.println("map with ID: " + e.getKey().getID() + "\t lies at " + e.getValue()); });
//		t.multiPoly.forEachNode(action);

		//
		points = new ColoredOL[] { //
				new ColoredOL(Color.BLUE, 10, 10), //
				new ColoredOL(Color.GREEN, 12, 10), //
				new ColoredOL(Color.RED, 7, 11), //
				new ColoredOL(Color.ORANGE, 7, 7), //
				new ColoredOL(Color.PINK, 5, 8), //
				new ColoredOL(Color.GREEN.darker(), 9, 6), //
				new ColoredOL(Color.GREEN.brighter(), 3, 7),//
		};
		System.out.println("\n\nnow add objects");
		for (ObjectLocated ol : points) {
			boolean added;
			System.out.println(ol);
			added = t.multiPoly.add(ol);
			System.out.println("added: " + added + "\n\n");
		}
//		t.multiPoly.forEachNode(action);
//		ol=new ColoredOL(Color.BLUE, location)
		t.buildAndShowGUI();
		System.out.println("FINE");
	}

//	protected	static class

	protected static class ColoredOL implements ObjectLocated {
		private static final long serialVersionUID = 1L;
		Long ID;
		Color color;
		Point location;

		private ColoredOL(Color color) {
			super();
			this.ID = uidp.getNewID();
			this.color = color;
		}

		protected ColoredOL(Color color, Point location) {
			this(color);
			this.location = location;
		}

		protected ColoredOL(Color color, int x, int y) {
			this(color);
			this.setLocation(x, y);
		}

		@Override
		public Long getID() { return ID; }

		@Override
		public Point getLocation() { return location; }

		@Override
		public void setLocation(Point location) { this.location = location; }

		@Override
		public void setLocation(int x, int y) {
			if (location == null) {
				location = new Point(x, y);
			} else {
				location.x = x;
				location.y = y;
			}
		}

		@Override
		public String toString() { return "COL [ID=" + ID + ", color=" + color + ", location=" + location + "]"; }

	}

}