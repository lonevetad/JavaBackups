package tests.tDataStruct;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import dataStructures.MapTreeAVL;
import dataStructures.isom.matrixBased.MISOM_SingleObjInNode;
import dataStructures.isom.matrixBased.MatrixInSpaceObjectsManager;
import games.generic.controlModel.GModality;
import games.generic.controlModel.objects.ObjectInSpace;
import geometry.AbstractShape2D;
import geometry.ShapeRunnersImplemented;
import tests.tDataStruct.TestIsom.ControllerTestIsom;
import tools.Comparators;
import tools.NumberManager;
import tools.UniqueIDProvider;

public abstract class TestIsomGeneric {

	public static final int PIXEL_SIZE = 10;
	public static final Color[] COLORS = new Color[] { Color.BLUE, Color.GREEN, Color.RED, Color.ORANGE, Color.PINK,
			Color.GREEN.darker(), Color.CYAN, Color.MAGENTA };
	public static final String[] COLORS_NAME = { "BLUE", "GREEN", "RED", "ORANGE", "PINK", "GREEN darker", "CYAN",
			"MAGENTA" };
	public static final Color BACKGROUND_COLOR_MISOM = Color.LIGHT_GRAY;
	public static final UniqueIDProvider NODE_SHAPED_PROVIDER = UniqueIDProvider.newBasicIDProvider();

	public TestIsomGeneric() {
		controller = newController();
		gui = newGUI();
	}

	protected GUI_TestIsom_Generic gui;
	protected ControllerTestIsom_Generic controller;

	protected abstract ControllerTestIsom_Generic newController();

	protected abstract GUI_TestIsom_Generic newGUI();

	public void init() {
		controller.init();
		gui.init();
	}

	//

	//

	// TODO CLASSES

	//

	protected class NodeShaped implements ObjectInSpace {
		private static final long serialVersionUID = 1L;

		NodeShaped(int fazione) {
			this.fazione = fazione;
			this.ID = NODE_SHAPED_PROVIDER.getNewID();
		}

		protected final int fazione; // index of COLORS
		protected AbstractShape2D shape;
		protected Integer ID;

		@Override
		public AbstractShape2D getShape() { return shape; }

		@Override
		public Integer getID() { return ID; }

		@Override
		public void setShape(AbstractShape2D shape) { this.shape = shape; }

		@Override
		public void onAddedToGame(GModality gm) { // TODO Auto-generated method stub
		}

		@Override
		public void onRemovedFromGame(GModality gm) { // TODO Auto-generated method stub
		}

		@Override
		public String getName() { // TODO Auto-generated method stub
			return null;
		}
	}

	protected abstract class ControllerTestIsom_Generic {
		protected int width, height;
		protected MatrixInSpaceObjectsManager<Double> misom;
		protected Map<Integer, NodeShaped> objectsInMap;
		protected AbstractShape2D shapeSelected;

		protected ControllerTestIsom_Generic() { shapeSelected = null; }

		protected abstract void init();

		protected abstract void addNewShapedObjectToMap();

		public int getWidth() { return width; }

		public int getHeight() { return height; }

		public MatrixInSpaceObjectsManager<Double> getMisom() { return misom; }

		public void setWidth(int width) { this.width = width; }

		public void setHeight(int height) { this.height = height; }

		public AbstractShape2D getShapeSelected() { return shapeSelected; }

		public void setShapeSelected(AbstractShape2D shapeSelected) { this.shapeSelected = shapeSelected; }

		public void recreateMISOM() {
			misom = new MISOM_SingleObjInNode<>(false, width, height, NumberManager.getDoubleManager());
			objectsInMap = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.INTEGER_COMPARATOR);
		}
	}

	//

	// TODO GUI

	//

	protected abstract class GUI_TestIsom_Generic {
//		BufferedImage bi;

		GUI_TestIsom_Generic() {}

		protected JFrame window;

		protected abstract void init();
	}

	/** Gui component setting Shape's fields */
	protected abstract class ShapeFieldsManager<S extends AbstractShape2D> {
		final ControllerTestIsom m;
		ShapeRunnersImplemented shapeManaging;
		final JPanel jpShapeFieldContainer;
		S shape;

		protected ShapeFieldsManager(ControllerTestIsom m) {
			this.m = m;
			jpShapeFieldContainer = new JPanel();
//			jpShapeFieldContainer.setSize(200, 100);
			jpShapeFieldContainer.setLayout(new GridBagLayout());
//			jpShapeFieldContainer.setPreferredSize(jpShapeFieldContainer.getSize());
			jpShapeFieldContainer.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
		}

		public S getShape() { return shape; }

		public abstract S newShape();

		/** Invoke super.doOnShow() */
		public void doOnShow() { controller.setShapeSelected(getShape()); }

		public abstract void init();
	}
}