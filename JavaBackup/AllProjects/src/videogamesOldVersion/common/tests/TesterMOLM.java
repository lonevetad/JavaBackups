package videogamesOldVersion.common.tests;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Comparator;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import common.mainTools.mOLM.abstractClassesMOLM.ShapeSpecification;
import common.removed.objectHierarchyFromMOLM.ObjectTiled;
import dataStructures.MyLinkedList;
import tools.Comparators;
import tools.FileUtilities;
import tools.GraphicTools;
import videogamesOldVersion.common.PainterMolm;
import videogamesOldVersion.common.abstractCommon.MainController;
import videogamesOldVersion.common.abstractCommon.behaviouralObjectsAC.AbstractPainter;
import videogamesOldVersion.common.abstractCommon.referenceHolderAC.MainHolder;
import videogamesOldVersion.common.gui.ListBoxView;
import videogamesOldVersion.common.gui.LoggerMessagesJTextArea;
import videogamesOldVersion.common.gui.MolmVisualizer;
import videogamesOldVersion.common.gui.MouseClickListenerAdapter;
import videogamesOldVersion.common.mainTools.mOLM.MatrixObjectLocationManager;
import videogamesOldVersion.common.mainTools.mOLM.NodeMatrix;
import videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM.AbstractMatrixObjectLocationManager;
import videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM.AbstractShapeRunners;
import videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM.DoNothingWithItem;
import videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM.DoSomethingWithNode;
import videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM.ObjectWithID_OLD_MOLM.MementoOWID;
import videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM.PainterMOLMNullItem;

public class TesterMOLM {
	public static final String WHERE_TO_SAVE_TRIALS, MAP_EXTENSION = ".ottyarray";
	public static final int MOLM_WIDTH = 96, MOLM_HEIGHT = 64, PIXEL_EACH_MICROPIXEL = 12//
			, MOLM_PIXEL_WIDTH = MOLM_WIDTH * PIXEL_EACH_MICROPIXEL,
			MOLM_PIXEL_HEIGHT = MOLM_HEIGHT * PIXEL_EACH_MICROPIXEL;
	// public static final Color[] COLORS_ACCEPTED = {Color.blue};

	public static enum ColorsAccepted implements Comparator<ColorsAccepted> {
		BLUE(Color.BLUE), RED(Color.RED, false), GRAY(Color.GRAY, false), PINK(Color.PINK, false), GREEN(Color.GREEN),
		ORANGE(Color.ORANGE);

		final boolean isNotSolid;
		final ColorObject color;

		ColorsAccepted(Color c) {
			this(c, true);
		}

		ColorsAccepted(Color c, boolean ins) {
			this.isNotSolid = ins;
			this.color = new ColorObject(c);
			this.color.setNotSolid(ins);
			color.setMustUpdateMolmsOnChanges(false);
			color.setLeftBottomCorner(-1, -1);
		}

		@Override
		public int compare(ColorsAccepted o1, ColorsAccepted o2) {
			return Comparators.COLOR_COMPARATOR.compare(o1.color.c, o2.color.c);
		}
	}

	static {
		File f;
		WHERE_TO_SAVE_TRIALS = getPathForMap() + "TestSalvataggio" + File.separatorChar;
		f = new File(WHERE_TO_SAVE_TRIALS);
		if (!f.exists()) {
			f.mkdirs();
			f = null;
		}
	}

	public TesterMOLM() {
	}

	// managing
	boolean showJSP_RightSection = false;
	int xStartOnMarixForShortPathTest, yStartOnMarixForShortPathTest, xEndOnMarixForShortPathTest,
			yEndOnMarixForShortPathTest;
	int[] xPath, yPath;
	LoggerMessagesJTextArea log;
	MatrixObjectLocationManager molm;
	MatrixObjectLocationManager[] molms;
	ListBoxView<File> listExistingMap;

	// gui
	JFrame fin;
	JPanel jpMolmVisualizer, jpFileManager, jpLog//
			, jpMolmRightGUIControls, jpMolmRGUIC_ListsSection,
			/** Il pannello in cui si ridisegna il molm */
			// jpMolmRepainter,
			jpMolmRightSouthSection, jpMolmChecksSections, jpMolmButtonSections, jpShapeDetails;
	JButton jbClearMolm, jbRunShortPathTest, jbClearLog;
	// BorderLayout bl;
	JScrollPane jspLog, jspMolmRightGUIControls;
	MolmVisualizer molmVisualizer;
	JTabbedPane jtp;
	JTextArea jtaLog;
	JCheckBox jcbIsAdd, jcbIsStartPoint;
	JLabel jlShapeWidth, jlShapeHeight, jlShapeAngle;
	JTextField jtfShapeWidth, jtfShapeHeight, jtfShapeAngle;
	ListBoxView<ColorsAccepted> listColors;
	ListBoxView<AbstractShapeRunners.ShapesImplemented> listShapes;
	MouseClickListenerAdapter toDoOnClick_OnMolmRepainter;
	// Border borderSimple;
	JComponent[] toBeLogged;

	public static void main(String[] args) {
		TesterMOLM t;
		t = new TesterMOLM();
		t.init();

		System.out.println("tester molm initialized");
	}

	void init() {
		initGUI();
		initNOTGUI();
	}

	void initGUI() {
		Dimension d;
		// borderSimple = LineBorder.createBlackLineBorder();
		toDoOnClick_OnMolmRepainter = MouseClickListenerAdapter.newInstance(this::onClick_OnMOLMRepainter);

		fin = new JFrame("TESTER MatrixObjectLocationManager");
		fin.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		jtp = new JTabbedPane();
		fin.add(jtp);
		fin.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				Dimension dim;
				jtp.setSize(dim = fin.getSize());
				jtp.setPreferredSize(dim);
				// (dim = (Dimension) dim.clone())
				dim.height -= 25;
				jpFileManager.setPreferredSize(dim);
				dim.height -= 25;
				jpMolmVisualizer.setPreferredSize(dim);
				jpMolmVisualizer.setSize(dim);
				molmVisualizer.setSize(dim);
				/*
				 * d.height -= 5; jpMolmRightGUIControls.setSize(d);
				 * jpMolmRightGUIControls.setLocation(0, d.height);
				 */
				jpMolmRightGUIControls.repaint();
				// fin.pack();
				fin.repaint();
				// logSizes();
			}
		});

		// creo le schede
		// bl=new BorderLayout()
		jpMolmVisualizer = new JPanel(new BorderLayout());
		jtp.addTab("Molm", jpMolmVisualizer);
		jpFileManager = new JPanel(new BorderLayout());
		jtp.addTab("File", jpFileManager);
		jpLog = new JPanel(new BorderLayout());
		jtp.addTab("Log", jpLog);

		// popolo le schede
		log = new LoggerMessagesJTextArea(jtaLog = new JTextArea());
		jspLog = new JScrollPane(jtaLog);
		jspLog.setViewportView(jtaLog);
		jpLog.add(jspLog);
		jbClearLog = new JButton("Clear Log");
		jpLog.add(jbClearLog, BorderLayout.SOUTH);
		jbClearLog.addActionListener(l -> log.clearLog());

		// TODO AAAAAAAAAAAAAAA molm visualizer

		xStartOnMarixForShortPathTest = yStartOnMarixForShortPathTest = xEndOnMarixForShortPathTest = yEndOnMarixForShortPathTest = 0;
		// jpMolmRepainter = new JPanelMolmRepainter(this);
		molmVisualizer = new MolmVisualizer(false);
		/*
		 * new JScrollPane(jpMolmRepainter, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		 * JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		 * jspMolmRepainter.setViewportView(jpMolmRepainter);
		 */
		// jspMolmRepainter.setBorder(borderSimple);
		jpMolmVisualizer.add(molmVisualizer);
		molmVisualizer.setSizeSquare(PIXEL_EACH_MICROPIXEL);
		molmVisualizer.setSomethingToDoAfterPaint(this::paintPath);
		// molmVisualizer.getPainter().setPainterMOLMNullItem(PainterMOLMNullItem.newInstance_MicropixelPurpose());
		molmVisualizer.addMouseListener(toDoOnClick_OnMolmRepainter);

		d = new Dimension(MOLM_PIXEL_WIDTH, MOLM_PIXEL_HEIGHT);
		molmVisualizer.setSize(d);
		molmVisualizer.setPreferredSize(d);
		jpMolmRightGUIControls = new JPanel(new BorderLayout()) {
			private static final long serialVersionUID = 89051183330303003L;

			@Override
			public int getWidth() {
				return Math.min(super.getWidth(), 300);
			}
		};
		if (showJSP_RightSection) {
			jspMolmRightGUIControls = new JScrollPane(jpMolmRightGUIControls);
			jspMolmRightGUIControls.setViewportView(jpMolmRightGUIControls);
			jpMolmVisualizer.add(jspMolmRightGUIControls, BorderLayout.EAST);
		} else
			jpMolmVisualizer.add(jpMolmRightGUIControls, BorderLayout.EAST);
		// jpMolmRightGUIControls.setBorder(borderSimple);
		// riempamo il pannello a destra
		jpMolmRGUIC_ListsSection = new JPanel(new GridLayout(0, 1));
		jpMolmRightGUIControls.add(jpMolmRGUIC_ListsSection, BorderLayout.CENTER);

		// blue, the first i found
		listColors = ListBoxView.getDefaultImplementation(TesterMOLM.ColorsAccepted.BLUE);
		jpMolmRGUIC_ListsSection.add(listColors);
		// lambda :D
		listColors.setDescriptionExtractor(o -> {
			ColorsAccepted co;
			co = ((ColorsAccepted) o);
			return "solid: " + (co.isNotSolid ? 'F' : 'V') + ", " + co.name();
		});
		for (TesterMOLM.ColorsAccepted c : TesterMOLM.ColorsAccepted.values()) {
			listColors.addNewItem(c);
		}

		listShapes = ListBoxView.getDefaultImplementation(AbstractShapeRunners.COMPARATOR_ShapesImplemented);
		listShapes.setDescriptionExtractor(o -> ((ShapesImplemented) o).name());
		for (ShapesImplemented s : ShapesImplemented.values()) {
			listShapes.addNewItem(s);
		}
		/*
		 * if (ShapesImplemented.COMPARATOR_ShapesImplemented == null) throw new
		 * RuntimeException("ShapesImplemented.COMPARATOR null" );
		 */
		jpMolmRGUIC_ListsSection.add(listShapes);

		// SouthSection
		jpMolmRightSouthSection = new JPanel(new BorderLayout());
		jpMolmRightGUIControls.add(jpMolmRightSouthSection, BorderLayout.SOUTH);
		// asfvasbzdhjbvfv
		jpMolmChecksSections = new JPanel(new GridLayout(0, 1));
		jpMolmRightSouthSection.add(jpMolmChecksSections, BorderLayout.NORTH);
		jcbIsStartPoint = new JCheckBox("V= set start path, else end");
		jcbIsStartPoint.setSelected(true);
		jpMolmChecksSections.add(jcbIsStartPoint);
		jcbIsAdd = new JCheckBox("V= add shape, else remove");
		jcbIsAdd.setSelected(true);
		jpMolmChecksSections.add(jcbIsAdd);

		jpShapeDetails = new JPanel(new GridLayout(0, 2));
		jpMolmRightSouthSection.add(jpShapeDetails, BorderLayout.CENTER);
		jpShapeDetails.add(jlShapeWidth = new JLabel("Width shape"));
		jpShapeDetails.add(jtfShapeWidth = new JTextField("13"));
		jpShapeDetails.add(jlShapeHeight = new JLabel("Height shape"));
		jpShapeDetails.add(jtfShapeHeight = new JTextField("8"));
		jpShapeDetails.add(jlShapeAngle = new JLabel("Angle shape"));
		jpShapeDetails.add(jtfShapeAngle = new JTextField("0.0"));

		// bottoni
		jpMolmButtonSections = new JPanel(new GridLayout(1, 0));
		// jpMolmButtonSections.setBorder(borderSimple);
		// jpMolmButtonSections.setPreferredSize(new Dimension(200, 50));
		jpMolmRightSouthSection.add(jpMolmButtonSections, BorderLayout.SOUTH);
		jpMolmButtonSections.add(jbClearMolm = new JButton("Clear"));
		jpMolmButtonSections.add(jbRunShortPathTest = new JButton("ShortestPath"));

		jbClearMolm.addActionListener(l -> {
			molm.clearMatrix();
			fin.repaint();
		});
		jbRunShortPathTest.addActionListener(l -> this.extractShortestPath());

		// jbClearMolm.setSize(100, 50);
		// jbRunShortPathTest.setSize(100, 50);

		// TODO AAAAAAAAAAAAA file manager

		//

		toBeLogged = new JComponent[] { jtp, jpMolmVisualizer, molmVisualizer, jpMolmRightGUIControls,
				jpMolmButtonSections, jpFileManager };

		fin.pack();
		fin.setSize(1500, 700);
		fin.setVisible(true);
		jtp.setSelectedIndex(1);
		jtp.repaint();
		jtp.setSelectedIndex(0);
		log.log("GUI FINISH LOADING");
	}

	// TODO initNOTGUI
	void initNOTGUI() {
		ColorObject co;
		molm = MatrixObjectLocationManager.newDefaultInstance(MOLM_WIDTH, MOLM_HEIGHT);
		molm.setLog(log);
		molms = new MatrixObjectLocationManager[] { molm };
		molmVisualizer.setAllMolms(molms);

		for (ColorsAccepted ca : ColorsAccepted.values()) {
			co = ca.color;
			co.setMolms(molms);
			co.setScaleMicropixelToRealpixel(PIXEL_EACH_MICROPIXEL);
		}
		log.logAndPrint("NON GUI finished");
	}

	void logSizes() {
		int c;
		log.log("Sizes:");
		c = 0;
		System.out.println("start loggin sizes");
		for (JComponent jc : toBeLogged)
			log.log((c++) + " size:" + jc.getSize().toString() + ", pref size: " + jc.getPreferredSize().toString());

		System.out.println("end loggin sizes");
	}

	void logError(String errorText) {
		logError("undefined-method", errorText);
	}

	void logError(String methodName, String errorText) {
		log.log("ERROR: on " + methodName + ", \n\t" + errorText);
		jtp.setSelectedIndex(2);
	}

	public ShapeSpecExtracted extractShapeSpecification(ShapesImplemented si, int xx, int yy) {
		return extractShapeSpecification(si, xx, yy, null);
	}

	protected ShapeSpecExtracted extractShapeSpecification(ShapesImplemented si, int xx, int yy, MouseEvent me) {
		int w, h;
		double ang;
		ShapeSpecification ss;
		ShapeSpecExtracted sse;

		sse = new ShapeSpecExtracted(null, ShapeSpecExtracted.StatusSSE.OK);

		h = w = 0;
		ang = 0;
		try {
			w = Integer.parseInt(jtfShapeWidth.getText());
		} catch (Exception ex) {
			logError("onClick_OnMOLMRepainter", "bad format width: " + jtfShapeWidth.getText());
			// ok = false;
			sse.ssse = ShapeSpecExtracted.StatusSSE.ERROR;
		}
		try {
			h = Integer.parseInt(jtfShapeHeight.getText());
		} catch (Exception ex) {
			logError("onClick_OnMOLMRepainter", "bad format height: " + jtfShapeHeight.getText());
			// ok = false;
			sse.ssse = ShapeSpecExtracted.StatusSSE.ERROR;
		}
		try {
			ang = Double.parseDouble(jtfShapeAngle.getText());
		} catch (Exception ex) {
			logError("onClick_OnMOLMRepainter", "bad format degrees angle: " + jtfShapeAngle.getText());
			// ok = false;
			sse.ssse = ShapeSpecExtracted.StatusSSE.ERROR;
		}

		if (sse.ssse != ShapeSpecExtracted.StatusSSE.ERROR) {
			ss = null;
			try {
				switch (si) {
				case Arrow:
					ss = ShapeSpecification.newArrow(xx, yy, h, 30.0, w, ang);
					break;
				case ArrowBorderBodySameLength:
					ss = ShapeSpecification.newArrowBorderBodySameLength(xx, yy, w, ang);
					break;
				case Circle:
					ss = ShapeSpecification.newCircle(true, xx, yy, w);
					break;
				case Cone:
					ss = ShapeSpecification.newCone(xx, yy, 4, 3, w, h, ang);
					break;
				case Circumference:
					ss = ShapeSpecification.newCircle(false, xx, yy, w);
					break;
				case Line:
					ss = ShapeSpecification.newLine(xx, yy, w, ang);
					break;
				case Point:
					// eh eh
					ss = ShapeSpecification.newPoint(xx, yy);
					sse.ssse = ShapeSpecExtracted.StatusSSE.JUST_POINT;
					break;
				case Rectangle:
					ss = ShapeSpecification.newRectangle(true, xx, yy, w, h, ang);
					break;
				case Rectangle_Border:
					ss = ShapeSpecification.newRectangle(false, xx, yy, w, h, ang);
					break;
				case Triangle:
					ss = ShapeSpecification.newTriangle(xx, yy, w, ang);
					break;
				case EllipseNoRotation:
					ss = ShapeSpecification.newEllipseNoRotation(true, xx, yy, w, h);
					break;
				case EllipseNoRotation_Border:
					ss = ShapeSpecification.newEllipseNoRotation(false, xx, yy, w, h);
					break;
				default:
					ss = null;
					// ok = false;
					sse.ssse = ShapeSpecExtracted.StatusSSE.ERROR;
					break;
				}
				sse.ss = ss;
			} catch (Exception e) {
				logError("ExtractShape", e.getMessage());
			}
		}
		return sse;
	}

	// TODO onClick_OnMOLMRepainter
	void onClick_OnMOLMRepainter(MouseEvent me) {
		int xx, yy;
		String textError;
		ShapesImplemented si;
		ColorsAccepted ca;
		ShapeSpecification ss;
		AdderRemoverShape ars;
		ShapeSpecExtracted sse;

		if (molm != null && me != null) {
			si = listShapes.getSelectItem();
			ca = listColors.getSelectItem();
			if (si != null && ca != null) {

				xx = me.getX() / PIXEL_EACH_MICROPIXEL;
				yy = me.getY() / PIXEL_EACH_MICROPIXEL;

				ss = null;

				sse = extractShapeSpecification(si, xx, yy, me);

				switch (sse.ssse) {
				case ERROR:
					logError("onClick_OnMOLMRepainter", "no shape specification created");
					break;
				case JUST_POINT:
					if (jcbIsStartPoint.isSelected()) {
						this.xStartOnMarixForShortPathTest = xx;
						this.yStartOnMarixForShortPathTest = yy;
					} else {
						this.xEndOnMarixForShortPathTest = xx;
						this.yEndOnMarixForShortPathTest = yy;
					}
					this.molmVisualizer.repaint();
					break;
				case OK: {
					ss = sse.ss;

					log.log((jcbIsAdd.isSelected() ? "Adding" : "Removing") + " color: " + ca.name() + " on shape "
							+ ss.getShape().name());
					// ca.color.setShapeSpecification(ss);
					ars = new AdderRemoverShape(jcbIsAdd.isSelected(), // ca.color.clone()
							(ColorObject) ((ObjectTiled) (new ColorObject(ca.color, ss)).setLog(log)
									.setMustLogErrors(true).setNotSolid(ca.isNotSolid)).setMolms(molms));
					// molm.addOnShape(ss, co);
					// ca.color.setShapeSpecification(ss);

					textError = molm.runOnShape(ss, ars);
					// else
					// textError = molm.removeOnShape(ss, co);
					if (textError != null)
						logError("onClick_OnMOLMRepainter", "operation failed with:"//
								// + "\n\t\t xx: " + xx//
								// + "\n\t\t yy: " + yy//
								// + "\n\t\t w: " + w//
								// + "\n\t\t h: " + h//
								// + "\n\t\t ang: " + ang//
								+ "\n\t\t ss: " + ss.toString()//
								+ "\n\t\t color: " + ca.name()//
								+ "\n\\t\t error from molm:\n\t\t\t" + textError);
					else
						fin.repaint();
					// logError("SUCCESS");
				}
				default:
				}
			} else {
				if (si == null)
					logError("onClick_OnMOLMRepainter", "cannot run a null shape.");
				if (ca == null)
					logError("onClick_OnMOLMRepainter", "cannot color a shape with null color.");
			}
		}
	}

	// TODO extractShortestPath
	void extractShortestPath() {
		int i, l;
		int[] newXP, newYP;
		ShapeSpecExtracted sse;
		MyLinkedList<Point> path;
		MyLinkedList.NodeList<Point> n;
		Point p;
		ShapesImplemented si;

		log.log("extracting shortest path started");
		if (xStartOnMarixForShortPathTest != xEndOnMarixForShortPathTest
				|| yStartOnMarixForShortPathTest != yEndOnMarixForShortPathTest) {

			si = listShapes.getSelectItem();

			sse = extractShapeSpecification(si, xStartOnMarixForShortPathTest, yStartOnMarixForShortPathTest);
			if (sse.ssse != ShapeSpecExtracted.StatusSSE.ERROR) {

				log.log("extracting ...");
				path = (MyLinkedList<Point>) molm.extractShortestPath(xStartOnMarixForShortPathTest,
						yStartOnMarixForShortPathTest, xEndOnMarixForShortPathTest, yEndOnMarixForShortPathTest,
						sse.ss);

				if (path != null) {

					log.log("extracted");

					l = path.size();
					if (l > 1) {

						newXP = new int[l];
						newYP = new int[l];
						i = -1;
						log.log("len:" + l);
						// for (Point p : path) {
						n = path.getHead();
						if (n != null) {
							do {
								p = n.getItem();
								newXP[++i] = p.x * PIXEL_EACH_MICROPIXEL;
								newYP[i] = p.y * PIXEL_EACH_MICROPIXEL;
							} while((n = n.getNext()) != null);
						}
						xPath = newXP;
						yPath = newYP;
						molmVisualizer.repaint();
					} else
						logError("extractShortestPath", "path with incorrect length: " + l);
				} else {
					logError("extractShortestPath", "Path null");
					xPath = yPath = null;
				}
			}
		}
		log.log("extracting shortest path finished");
	}

	// TODO GUI

	public void paintPath(Graphics g) {

		g.setColor(Color.MAGENTA);
		g.fillRect(xStartOnMarixForShortPathTest * PIXEL_EACH_MICROPIXEL,
				yStartOnMarixForShortPathTest * PIXEL_EACH_MICROPIXEL, 4, 4);
		g.fillRect(xEndOnMarixForShortPathTest * PIXEL_EACH_MICROPIXEL,
				yEndOnMarixForShortPathTest * PIXEL_EACH_MICROPIXEL, 4, 4);
		if (xPath != null) {
			g.drawPolyline(xPath, yPath, xPath.length);
		}
	}

	//

	// TODO STATIC METHODS

	protected static String getPathForMap() {
		// int i, r;
		String s;
		s = FileUtilities.getPath(TesterMOLM.class);
		/*
		 * r = 3; i = s.length(); while (r > 0 && s != null) {
		 *
		 * i=s.lastIndexOf(File.separatorChar); /* while (--i >= 0 && s.charAt(i) !=
		 * File.separatorChar) ; if (i < 0) { s = null; } else { r--; }// if(i>0)r--; }
		 * if (s != null) { s = s.substring(0, i + 1); }
		 */
		System.out.println(s);
		return s;
	}

	//

	// TODO CLASSES

	public static class AdderRemoverShape implements DoSomethingWithNode, DoNothingWithItem {
		private static final long serialVersionUID = -95090177070707L;
		boolean add;
		ColorObject co;

		public AdderRemoverShape() {
			this(true, ColorsAccepted.BLUE.color);
		}

		public AdderRemoverShape(boolean add, ColorObject co) {
			super();
			this.add = add;
			this.co = co;
			// co.setWidth(PIXEL_EACH_MICROPIXEL);
			// co.setHeight(PIXEL_EACH_MICROPIXEL);
		}

		@Override
		public Object doOnNode(AbstractMatrixObjectLocationManager molm, NodeMatrix node, int x, int y) {
			// co.setXLeftBottom(x);
			// co.setYLeftBottom(y);
			if (add)
				node.setItem(co);
			else
				/* if (node.getItem() == co) */ node.setItem(null);
			return null;
		}
	}

	public static class JPanelMolmRepainter extends JPanel {
		private static final long serialVersionUID = 2301623010605078489L;
		public static final PainterMOLMNullItem PAINTER_NULLS = PainterMOLMNullItem.newInstance_MicropixelPurpose()
				.setWidth(PIXEL_EACH_MICROPIXEL);
		/*
		 * public void paintOn(Graphics g, int x, int y, int width, int height) { Color
		 * pc; pc = g.getColor(); g.setColor(Color.BLACK); g.fillRect(x *
		 * PIXEL_EACH_MICROPIXEL, y * PIXEL_EACH_MICROPIXEL, PIXEL_EACH_MICROPIXEL,
		 * PIXEL_EACH_MICROPIXEL); g.setColor(pc); }
		 */

		public JPanelMolmRepainter(TesterMOLM t) {
			super();
			this.t = t;
			painter = new PainterMolm(null, PAINTER_NULLS);
			this.addMouseListener(t.toDoOnClick_OnMolmRepainter);
		}

		TesterMOLM t;
		PainterMolm painter;
		Dimension d;

		@Override
		public void setSize(Dimension dd) {
			this.d = dd;
			super.setSize(dd);
			super.setPreferredSize(dd);
		}

		@Override
		public int getWidth() {
			return (d != null) ? d.width : super.getWidth();
		}

		@Override
		public int getHeight() {
			return (d != null) ? d.height : super.getHeight();
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			onPaint(g);
			// super.paint(g);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			onPaint(g);
			// super.paintComponent(g);
		}

		// TODO onPaint
		public void onPaint(Graphics g) {
			MatrixObjectLocationManager mol;
			// System.out.println("w: " + getWidth() + ", h: " + getHeight());

			painter.setGraphics(g);
			if ((mol = t.molm) != null)
				mol.forEach(painter);
			else
				t.logError("onPaint", "molm null");

			g.setColor(Color.GRAY);
			GraphicTools.paintGrid(g, 0, 0, getWidth(), getHeight(), PIXEL_EACH_MICROPIXEL);

			t.paintPath(g);
		}
	}

	public static class ColorObject extends ObjectTiled implements AbstractPainter, MainHolder {

		private static final long serialVersionUID = 2308409606987L;
		// public static final int
		private static Random rand = null;

		public ColorObject() {
			this((rand == null ? (rand = new Random()) : rand).nextInt(Integer.MAX_VALUE));
		}

		public ColorObject(int c) {
			this(new Color(c));
		}

		public ColorObject(Color c) {
			super(ShapeSpecification.newRectangle(true, -2, -2, 1, 1, 0));
			this.c = c;
			this.setScaleMicropixelToRealpixel(TesterMOLM.PIXEL_EACH_MICROPIXEL);
		}

		public ColorObject(ColorObject co) {
			this(co, co.getShapeSpecification().clone());
		}

		public ColorObject(ColorObject co, ShapeSpecification ss) {
			super(ss);
			this.c = co.c;
			this.setScaleMicropixelToRealpixel(co.getScaleMicropixelToRealpixel());
		}

		//

		MainController main;
		Color c;

		//

		@Override
		public void paintOn(Graphics g, int x, int y) {
			// this.setLeftBottomCorner(x, y);
			// paintOn(g, getXLeftBottomRealPixel(), getYLeftBottomRealPixel(),
			// getWidthRealPixel(), getHeightRealPixel());
			paintOn(g, x * PIXEL_EACH_MICROPIXEL, y * PIXEL_EACH_MICROPIXEL, PIXEL_EACH_MICROPIXEL,
					PIXEL_EACH_MICROPIXEL
			// getWidthRealPixel(), getHeightRealPixel()//
			);
		}

		@Override
		public void paintOn(Graphics g, int x, int y, int width, int height) {
			Color olc;
			olc = g.getColor();
			g.setColor(c);
			g.fillRect(x, y, width, height);
			g.setColor(olc);
		}

		@Override
		public boolean reloadImage() {
			return false;
		}

		@Override
		public String getImageFilename() {
			return null;
		}

		@Override
		public ColorObject clone() {
			return new ColorObject(this.c);
		}

		@Override
		public int getCurrentFrame() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public MementoOWID createMemento() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MainController getMain() {
			return main;
		}

		@Override
		public ColorObject setMain(MainController main) {
			this.main = main;
			return this;
		}
	}

	// lists

	public static class ListFileMapForMOLM extends ListBoxView<File> {
		private static final long serialVersionUID = -2308409606987L;

		public ListFileMapForMOLM() {
			this(Comparators.FILE_COMPARATOR);
		}

		public ListFileMapForMOLM(MyComparator<File> comp) {
			super(comp);
		}

		@Override
		public ListBoxView<File>.JPanelItemVisualizer newRow(File item) {
			return null;
		}

		class JP_File extends ListBoxView<File>.JPanelItemVisualizer {
			private static final long serialVersionUID = -2308409606986L;

			JP_File(File item) {
				super(item);
			}

			JLabel jl;

			@Override
			public void updateItemShow() {
				add(jl = new JLabel());
				setSize(200, 35);
			}

			@Override
			public void initGUI() {
				File f;
				if (jl != null && (f = getItem()) != null) {
					jl.setText(f.getName());
				}
			}
		}
	}

	public static final class ShapeSpecExtracted {
		public static enum StatusSSE {
			OK, ERROR, JUST_POINT
		}

		ShapeSpecification ss;
		StatusSSE ssse;

		public ShapeSpecExtracted() {
			this(null, StatusSSE.ERROR);
		}

		public ShapeSpecExtracted(ShapeSpecification ss, StatusSSE ssse) {
			super();
			this.ss = ss;
			this.ssse = ssse;
		}

	}

}