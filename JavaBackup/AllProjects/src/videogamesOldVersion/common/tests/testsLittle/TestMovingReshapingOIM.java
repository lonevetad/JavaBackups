package common.tests.testsLittle;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import common.EnumGameObjectTileImageCollection;
import common.MainControllerEmpty;
import common.PainterMolm;
import common.PainterMolmPaintingOncePerRun;
import common.abstractCommon.LoaderGeneric;
import common.gui.LoggerMessagesJScrollPane;
import common.gui.MainGUI;
import common.gui.MolmVisualizer;
import common.mainTools.LoggerMessages;
import common.mainTools.mOLM.MatrixObjectLocationManager;
import common.mainTools.mOLM.NodeMatrix;
import common.mainTools.mOLM.abstractClassesMOLM.AbstractMatrixObjectLocationManager;
import common.mainTools.mOLM.abstractClassesMOLM.DoSomethingWithNode;
import common.mainTools.mOLM.abstractClassesMOLM.ObjectWithID;
import common.removed.objectHierarchyFromMOLM.ObjectInMap;
import common.tests.LoaderTests;

public class TestMovingReshapingOIM {

	public static final int MOLM_WIDTH = 40, MOLM_HEIGHT = (MOLM_WIDTH - (MOLM_WIDTH >> 2)), PIXEL_EACH_MICRO = 16,
			OIM_WIDTH = 5, OIM_HEIGHT = 5;
	public static final String IMAGE_NAME_OIM_TESTED = TestMovingReshapingOIM.class.getSimpleName();

	public TestMovingReshapingOIM() {
		isRunning = true;
	}

	public static enum FieldsOIM {
		X(6), Y(5), Width(10), Height(8), Angle;
		final int value;

		FieldsOIM() {
			this(0);
		}

		FieldsOIM(int v) {
			value = v;
		}
	}

	public static final FieldsOIM[] valuesFieldsOIM = FieldsOIM.values();

	boolean isRunning;
	double angleDeg;
	MatrixObjectLocationManager molm;
	MatrixObjectLocationManager[] molmsCache;
	ObjectInMap oim;
	Main_TPR main;
	LoaderGeneric loader;
	LoggerMessages log;
	PainterMolm painter;
	RunnableRepainter runnerRepainter;
	ExecutorService threads;

	JFrame fin;
	JPanel jpAll, jpRight, jpCommandsSection, jpCommandsOnly;
	JSlider jsAngle;
	// JTextArea jtaX, jtaY, jtaWidth, jtaHeight,
	JTextArea jtaAngle;
	FieldOIM[] fields;
	MolmVisualizer molmVisualizer;
	LoggerMessagesJScrollPane jspLog;
	// debug
	NonNullMicropixelCounter nnmc;

	void init() {
		initGUI();
		initNonGUI();
	}

	void initGUI() {
		int i, len;
		FieldOIM f;

		fin = new JFrame("test painter roteated");
		fin.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		fin.setVisible(false);
		fin.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				doOnClose();
			}
		});
		fin.add(jpAll = new JPanel(new BorderLayout()));
		fin.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				jpAll.setSize(fin.getSize());
				fin.repaint();
			}
		});
		jpAll.add(molmVisualizer = new MolmVisualizer(), BorderLayout.CENTER);
		jpRight = new JPanel(new GridLayout(0, 1)) {
			private static final long serialVersionUID = -84987878979L;
			static final int MIN_WIDTH = 200;

			// public int getWidth() {
			// return Math.max(super.getWidth(), MIN_WIDTH);
			// }
			@Override
			public Dimension getPreferredSize() {
				Dimension d;
				d = super.getPreferredSize();
				d.width = Math.max(d.width, MIN_WIDTH);
				return d;
			}

		};
		jpAll.add(jpRight, BorderLayout.EAST);

		// command section
		jpCommandsSection = new JPanel(new BorderLayout());
		jpRight.add(jpCommandsSection);
		jpCommandsSection.add(jpCommandsOnly = new JPanel(new GridLayout(0, 2)));
		fields = new FieldOIM[len = valuesFieldsOIM.length];
		i = -1;
		while (++i < len) {
			fields[i] = f = new FieldOIM(valuesFieldsOIM[i]);
			jpCommandsOnly.add(f.jlField);
			jpCommandsOnly.add(f.jtaField);
		}
		jtaAngle = fields[FieldsOIM.Angle.ordinal()].jtaField;
		jtaAngle.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				try {
					updateAngle(Double.parseDouble(jtaAngle.getText()));
				} catch (Exception ex) {
					log.log(ex.getMessage());
				}
			}
		});
		jpCommandsSection.add(jsAngle = new JSlider(0, 359), BorderLayout.SOUTH);
		jsAngle.setMajorTickSpacing(15);
		jsAngle.addChangeListener(e -> {
			int v;
			jtaAngle.setText(Integer.toString(v = jsAngle.getValue()));
			updateAngle(v);
		}//
		);

		log = jspLog = new LoggerMessagesJScrollPane();
		jpRight.add(jspLog);

		//
		fin.setSize(700, 500);
		fin.setVisible(true);
	}

	void initNonGUI() {
		angleDeg = 0.0;
		molm = MatrixObjectLocationManager.newDefaultInstance(MOLM_WIDTH, MOLM_HEIGHT);
		main = new Main_TPR(loader = new LoaderTests(), null, null);
		molm.setLog(((LoggerMessagesJScrollPane) log).getInnerLogger());
		main.setLog(molm.getLog());
		molmsCache = new MatrixObjectLocationManager[] { molm };
		painter = PainterMolmPaintingOncePerRun.newInstance();
		// painter.setPainterMOLMNullItem(null);
		painter.setWidth(PIXEL_EACH_MICRO).setHeight(PIXEL_EACH_MICRO);
		molmVisualizer.setPainter(painter);
		molmVisualizer.setSizeSquare(PIXEL_EACH_MICRO);
		molmVisualizer.setAllMolms(molmsCache);

		oim = new ObjectInMap(main, IMAGE_NAME_OIM_TESTED);
		oim.setCenter(FieldsOIM.X.value, FieldsOIM.Y.value);
		oim.setWidth(FieldsOIM.Width.value);
		oim.setHeight(FieldsOIM.Height.value);
		oim.setAngleDeg(FieldsOIM.Angle.value);
		oim.setMolms(molmsCache);
		oim.setScaleMicropixelToRealpixel(PIXEL_EACH_MICRO);
		// molm.addOnShape(oim.getShapeSpecification(), oim);
		System.out.println(oim.getShapeSpecification().toString());
		nnmc = new NonNullMicropixelCounter();

		System.out.println(molm.addOnShape(oim.getShapeSpecification(), oim));
		countNonNullMicropixel();
		System.out.println(molm.runOnShape(oim.getShapeSpecification(), new Adder(oim)));
		countNonNullMicropixel();

		threads = Executors.newFixedThreadPool(1);
		threads.execute(runnerRepainter = new RunnableRepainter(this));
	}

	// graphic

	// non graphic

	void updateAngle(double d) {
		this.angleDeg = d;
		this.oim.setAngleDeg(d);
	}

	void doOnClose() {
		isRunning = false;
		if (threads != null) threads.shutdown();
	}

	void countNonNullMicropixel() {
		nnmc.reset();
		molm.forEach(nnmc);
		System.out.println("\nfound " + nnmc.c + " micropixel non null");
		nnmc.reset();
	}

	//

	public static void main(String[] args) {
		TestMovingReshapingOIM t;
		t = new TestMovingReshapingOIM();
		System.out.println("start main");
		t.init();
		System.out.println("end main");
	}

	//

	// TODO CLASSES

	// loader

	// main

	static class Main_TPR extends MainControllerEmpty {
		private static final long serialVersionUID = 54109888L;
		MatrixObjectLocationManager molm;

		public Main_TPR(LoaderGeneric loaderGeneric, MainGUI mgg, EnumGameObjectTileImageCollection allTiles) {
			super(loaderGeneric, mgg, allTiles);
			// TODO Auto-generated constructor stub
		}

		@Override
		public String getGameName() {
			return "TestPainterRoteated";
		}

	}

	//
	static class FieldOIM {
		static final int DEFAULT_VALUE = 15;
		static final String SEPARATOR = ": ", DEFAULT_TEXT = Integer.toString(DEFAULT_VALUE);
		FieldsOIM fieldName;
		JLabel jlField;
		JTextArea jtaField;

		FieldOIM(FieldsOIM f) {
			this(f, f.value);
		}

		FieldOIM(FieldsOIM f, int value) {
			fieldName = f;
			jlField = new JLabel(f.name() + SEPARATOR);
			jtaField = new JTextArea(value == DEFAULT_VALUE ? DEFAULT_TEXT : Integer.toString(value));
		}
	}

	static class RunnableRepainter implements Runnable {
		TestMovingReshapingOIM t;

		RunnableRepainter(TestMovingReshapingOIM t) {
			this.t = t;
		}

		@Override
		public void run() {
			while (t.isRunning) {
				t.jpAll.repaint();
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					e.printStackTrace();
					t.log.log(e.getMessage());
					return;
				}
			}
			System.out.println("END repainter");
		}
	}

	static class NonNullMicropixelCounter implements DoSomethingWithNode {
		private static final long serialVersionUID = 9666022605620L;
		int c, y;

		public NonNullMicropixelCounter() {
			reset();
		}

		public void reset() {
			c = y = 0;
		}

		@Override
		public Object doOnItem(AbstractMatrixObjectLocationManager molm, ObjectWithID item, int x, int y) {
			if (item != null) {
				c++;
				// System.out.print('O');
				System.out.print('<');
				System.out.print('>');
				// System.out.println("found " + item.getID() + " at (" + x +
				// "," + y + ")");
			} else {
				if (y != this.y) {
					this.y = y;
					System.out.println();
				}
				System.out.print('=');
				System.out.print('-');
			}
			return null;
		}
	}

	static class Adder implements DoSomethingWithNode {
		private static final long serialVersionUID = 9666022605620L;
		ObjectInMap oim;

		public Adder(ObjectInMap oim) {
			this.oim = oim;
		}

		@Override
		public Object doOnNode(AbstractMatrixObjectLocationManager molm, NodeMatrix node, int x, int y) {
			node.setItem(oim);
			return null;
		}

		@Override
		public Object doOnItem(AbstractMatrixObjectLocationManager molm, ObjectWithID item, int x, int y) {
			return null;
		}
	}
}