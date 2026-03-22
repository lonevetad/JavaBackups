package tests.tDataStruct;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.TextField;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import dataStructures.ListMapped;
import dataStructures.MapTreeAVL;
import dataStructures.isom.NodeIsom;
import geometry.ShapeRunnersImplemented;
import geometry.implementations.shapes.ShapeRectangle;
import geometry.pointTools.PointConsumer;
import tools.Comparators;
import tools.GraphicTools;

public class TestIsom extends TestIsomGeneric {
	public static final String[] MOUSE_CLICK_ACTIONS = { "Add Object", "Remove Obj", "Set Start (path)",
			"Set Destination (path)" };

	public TestIsom() {
		super();
	}

	@Override
	protected ControllerTestIsom_Generic newController() {
		return new ControllerTestIsom();
	}

	@Override
	protected GUI_TestIsom_Generic newGUI() {
		return new GUI_TestIsom();
	}

	//

	//

	//

	//

	//

	protected class ControllerTestIsom extends ControllerTestIsom_Generic {
		// xCursor is the location pointed by the cursor, reduced to MISOM's pixel
		protected int factionNewNode, xCursor, yCursor;
		protected ShapeRunnersImplemented[] shapesToTest;
		protected ShapeRunnersImplemented selectedShapeRunner; // the shape to create new nodes to add, remove,
																// pathfind, etc
		Point starPath, endPath;
		List<Point> path;

		ControllerTestIsom() {
			super();
			factionNewNode = 0;
			selectedShapeRunner = null;
			starPath = endPath = null;
		}

		public int getFactionNewNode() {
			return factionNewNode;
		}

		public ShapeRunnersImplemented getSelectedShapeRunner() {
			return selectedShapeRunner;
		}

		public List<Point> getPath() {
			return path;
		}

		public void setFactionNewNode(int factionNewNode) {
			this.factionNewNode = factionNewNode;
		}

		public void setSelectedShapeRunner(ShapeRunnersImplemented selectedShape) {
			this.selectedShapeRunner = selectedShape;
		}

		@Override
		protected void init() {
			// TODO Auto-generated method stub
			shapesToTest = new ShapeRunnersImplemented[] { ShapeRunnersImplemented.Disk,
					ShapeRunnersImplemented.Circumference, ShapeRunnersImplemented.Line,
					// ShapeRunnersImplemented.PolygonBorder, // no way to set points
					ShapeRunnersImplemented.RectangleBorder, ShapeRunnersImplemented.Rectangle, //
					ShapeRunnersImplemented.Triangle, ShapeRunnersImplemented.TriangleBorder };
		}

		@Override
		protected void addNewShapedObjectToMap() {
			// TODO Auto-generated method stub
		}

		public void recalculatePath() {
			List<Point> pathOriginal;
			pathOriginal = this.misom.getPath(starPath, endPath);
			this.path = pathOriginal;
		}
	}

	//

	//

	// TODO GUI CLASSES

	//

	//

	protected class GUI_TestIsom extends GUI_TestIsom_Generic {
//		BufferedImage bi;

		ControllerTestIsom cti;

		GUI_TestIsom() {
			super();
			molmPainter = new MolmPainter();
			this.mouseClickAction = 0;
		}

		int mouseClickAction;
		protected TextField tfWidth, tfHeight;
		protected MolmPainter molmPainter;
//		protected Component componentWindow;
		protected JLabel jlCursorLocation, jlMouseAction;
		protected JPanel jpWindow, jpShapeSettings, jpShapeSelectionAndSettings, jpMISOMVisualizer, jpOptions; // jpWindow
		protected JScrollPane jspMISOM;
		protected JButton jbReshape;
		JSlider jslClickMode;
		JComboBox<String> jcbFactions;
		JComboBox<ShapeRunnersImplemented> jcbShapes;
		Map<String, ShapeFieldsManager<?>> shapeFieldManagers;
		List<Point> pathEnlargetByPixelSize; // TODO paint me

		protected String getCurrentShapeSettingsName() {
			ShapeRunnersImplemented se;
			se = this.cti.getSelectedShapeRunner();
			return se == null ? null : se.name();
		}

		void reshapeMISOM() {
			int w, h;
			w = Integer.parseInt(tfWidth.getText());
			h = Integer.parseInt(tfHeight.getText());
			controller.setWidth(w);
			controller.setHeight(h);
			controller.recreateMISOM();
			w *= PIXEL_SIZE;
			h *= PIXEL_SIZE;
			jpMISOMVisualizer.setSize(w, h);
			jpMISOMVisualizer.setPreferredSize(jpMISOMVisualizer.getSize());
			jspMISOM.setSize(Math.min(w, 500), Math.min(h, 500));
			jspMISOM.setPreferredSize(jspMISOM.getSize());
			jpMISOMVisualizer.repaint();
		}

		void paintMISOM(Graphics g) {
			molmPainter.g = g;
			if (controller.misom != null) {
				controller.misom.runOnWholeMap(molmPainter);
				g.setColor(Color.BLUE.brighter());
				GraphicTools.paintGrid(g, controller.getWidth() * PIXEL_SIZE, controller.getHeight() * PIXEL_SIZE,
						PIXEL_SIZE);
			}
		}

		void setSelectedShape(ShapeRunnersImplemented selectedShape) {
//TODO metterci rova
			cti.setSelectedShapeRunner(selectedShape);
		}

		void setSelectedFaction(String colorFaction, int indexFaction) {
//TODO metterci rova
			cti.setFactionNewNode(indexFaction);
			System.out.println("Selecting " + colorFaction + " as faction");
		}

		void recalculatePath() {
			this.cti.recalculatePath();
			pathEnlargetByPixelSize = new ListMapped<>(cti.getPath(), p -> {
				return new Point((p.x * PIXEL_SIZE + (PIXEL_SIZE >> 1)), (p.y * PIXEL_SIZE + (PIXEL_SIZE >> 1)));
			});
		}

		@Override
		protected void init() {
			GridBagConstraints c;
			cti = (ControllerTestIsom) controller;
			shapeFieldManagers = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.STRING_COMPARATOR);
			// TODO fill it

			window = new JFrame("TEST ISOM");
			window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

			c = new GridBagConstraints();
			jpWindow = new JPanel(new GridBagLayout()) {
				private static final long serialVersionUID = 1L;

//				public Dimension getSize() {return window.getSize();}
//				public Dimension getPreferredSize() {return window.getPreferredSize();}
				@Override
				public int getWidth() {
					return window.getWidth();
				}

				@Override
				public int getHeight() {
					return window.getHeight();
				}
			};
//			componentWindow = window.getContentPane();
			window.setLayout(new BorderLayout());
			window.add(jpWindow, BorderLayout.CENTER);

			// parte superiore, dove si impostano forme e colori da aggiungere
			jpShapeSelectionAndSettings = new JPanel();
			jpShapeSelectionAndSettings.setLayout(new GridBagLayout());
			jpShapeSelectionAndSettings.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
			c.gridx = 0;
			c.gridy = 0;
			c.weighty = 0.0;
			c.weightx = 0.0;
			c.gridheight = 2;
			c.gridwidth = 8;
			jpWindow.add(jpShapeSelectionAndSettings, c);
			jcbShapes = new JComboBox<>(cti.shapesToTest);
			jcbShapes.addItemListener(e -> {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					setSelectedShape((ShapeRunnersImplemented) e.getItem());
				}
			});
			c.gridwidth = 2;
			jpShapeSelectionAndSettings.add(jcbShapes, c);
			jcbFactions = new JComboBox<>(COLORS_NAME);
			jcbFactions.addItemListener(e -> {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					setSelectedFaction((String) e.getItem(), jcbFactions.getSelectedIndex());
				}
			});
			c.gridy = 2;
			jpShapeSelectionAndSettings.add(jcbFactions, c);
			jpShapeSettings = new JPanel();
			jpShapeSettings.setLayout(new CardLayout()); // TODO : learn how to use card layout to swap setting managers
			jpShapeSettings.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));
			c.gridx = 2;
			c.gridy = 0;
			c.gridwidth = 8;
			c.gridheight = 4;
			jpShapeSelectionAndSettings.add(jpShapeSettings, c);

//
			// visualizer
			jpMISOMVisualizer = new JPanel() {
				private static final long serialVersionUID = 1L;

				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					paintMISOM(g);
				}
			};
			jspMISOM = new JScrollPane(jpMISOMVisualizer);
			jspMISOM.setViewportView(jpMISOMVisualizer);
			jlCursorLocation = new JLabel("Cursor location:");
			jspMISOM.setColumnHeaderView(jlCursorLocation);
			jspMISOM.setSize(new Dimension(200, 200));
			jspMISOM.setMinimumSize(jspMISOM.getMinimumSize());
			jspMISOM.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
			c.weighty = 1.0;
			c.weightx = 1.0;
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight = 8;
			c.gridwidth = 8;
			jpWindow.add(jspMISOM, c);
			addMouseListenerToMISOMVisualizer(jpMISOMVisualizer);

			jpOptions = new JPanel(); // on left
			jpOptions.setLayout(new GridBagLayout());
			c.gridx = 8;
			c.gridy = 0;
			c.weighty = 1.0;
			c.weightx = 0.125;
			c.gridheight = 10;
			c.gridwidth = 4;
			jpWindow.add(jpOptions, c);

			c.gridx = 0;
			c.gridy = 0;
			c.weighty = 0.0;
			c.gridheight = 1;
			c.gridwidth = 2;
			jpOptions.add(new JLabel("Width"), c);
			c.gridx = 2;
			tfWidth = new TextField("50");
			jpOptions.add(tfWidth, c);
			c.gridx = 0;
			c.gridy = c.gridheight;
			jpOptions.add(new JLabel("Heighy"), c);
			tfHeight = new TextField("50");
			c.gridx = 2;
			jpOptions.add(tfHeight, c);
			jbReshape = new JButton("Reshape MISOM");
			jbReshape.addActionListener(l -> {
				reshapeMISOM();
			});
			c.gridx = 0;
			c.gridy = c.gridheight << 1;
			c.gridwidth = 4;
			jpOptions.add(jbReshape, c);

			c.gridy += c.gridheight << 1;
			jlMouseAction = new JLabel("Action on click: " + MOUSE_CLICK_ACTIONS[0]);
			jpOptions.add(jlMouseAction, c);

			c.gridy += c.gridheight;
			jslClickMode = new JSlider(SwingConstants.HORIZONTAL, 0, MOUSE_CLICK_ACTIONS.length - 1, 0);
//			jslClickMode.setModel(new BoundedRangeModel);
			jslClickMode.addChangeListener(e -> {
				JSlider source = (JSlider) e.getSource();
				if (!source.getValueIsAdjusting()) {
					mouseClickAction = source.getValue();
					jlMouseAction.setText("Action on click: " + MOUSE_CLICK_ACTIONS[mouseClickAction]);
				}
			});

			jpOptions.add(jslClickMode, c);

			//

			//
			window.setSize(700, 700);
			window.setVisible(true);
		}

		protected void addMouseListenerToMISOMVisualizer(JComponent comp) {
			MouseAdapter mouseAllListener;
			mouseAllListener = new MouseAdapter() {
				@Override
				public void mouseMoved(MouseEvent e) {
					ControllerTestIsom contr;
					contr = (ControllerTestIsom) controller;
					contr.xCursor = e.getX() / PIXEL_SIZE;
					contr.yCursor = e.getY() / PIXEL_SIZE;
					jlCursorLocation.setText("Cursor Location: (x: " + contr.xCursor + ", y: " + contr.yCursor + ")");

				}
			};
			comp.addMouseListener(mouseAllListener);
			comp.addMouseMotionListener(mouseAllListener);
			comp.addMouseWheelListener(mouseAllListener);
		}

		//

		protected class MolmPainter implements PointConsumer {
			private static final long serialVersionUID = 1L;
			Graphics g;

			@Override
			public void accept(Point p) {
				NodeIsom n;
				NodeShaped ns;
				n = controller.misom.getNodeAt(p);
				ns = ((NodeShaped) n.getObject(0));
				if (ns != null) {
					g.setColor(COLORS[ns.fazione]);
				} else {
					g.setColor(BACKGROUND_COLOR_MISOM);
				}
				g.fillRect(p.x * PIXEL_SIZE, p.y * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);
			}
		}

		class RectangularShapeManager extends ShapeFieldsManager<ShapeRectangle> {

			protected RectangularShapeManager(ControllerTestIsom m) {
				super(m);
			}

			JSpinner jsWidth, jsHeight;

			@Override
			public ShapeRectangle newShape() {
				return new ShapeRectangle();

			}

			@Override
			public void init() {
				GridBagConstraints c;
				c = new GridBagConstraints();
				c.weightx = 1;
				c.weighty = 1;
				c.gridwidth = 2;
				c.gridheight = 1;
				jsWidth = new JSpinner(new SpinnerNumberModel(16, 1, 1000, 1));
				jsHeight = new JSpinner(new SpinnerNumberModel(16, 1, 1000, 1));
				c.gridx = c.gridy = 0;
				super.jpShapeFieldContainer.add(new JLabel("width"), c);
				c.gridx = 2;
				super.jpShapeFieldContainer.add(jsWidth, c);
				c.gridx = 0;
				c.gridy = 2;
				super.jpShapeFieldContainer.add(new JLabel("height"), c);
				c.gridx = 2;
				super.jpShapeFieldContainer.add(jsHeight, c);

				jsWidth.addChangeListener(l -> {
//					shape.setWidth((Integer) jsWidth.getValue());
					// TODO
//					m.updateShapeAfterChanges(sh -> ((ShapeRectangle) sh).setWidth((Integer) jsWidth.getValue()));
				});
				jsHeight.addChangeListener(l -> {
//					shape.setHeight((Integer) jsHeight.getValue());
//					m.updateShapeAfterChanges(sh -> ((ShapeRectangle) sh).setHeight((Integer) jsHeight.getValue()));
				});
			}
		}
	}

	//

	//

	// TODO Auto-generated method stub

	public static void main(String[] args) {
		TestIsom t;
		t = new TestIsom();
		javax.swing.SwingUtilities.invokeLater(() -> t.init());
	}
}