package tests.tGeom;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.TextField;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import dataStructures.MapTreeAVL;
import geometry.AbstractShape2D;
import geometry.AbstractShape2DFactory;
import geometry.AbstractShapeRunner;
import geometry.ShapeRunnersImplemented;
import geometry.implementations.ProviderAbstractShape2DImpl;
import geometry.implementations.ProviderShapeRunnerImpl;
import geometry.implementations.shapeRunners.ShapeRunnerPolygonBorder;
import geometry.implementations.shapes.ShapeCircle;
import geometry.implementations.shapes.ShapeLine;
import geometry.implementations.shapes.ShapePolygon;
import geometry.implementations.shapes.ShapeRectangle;
import geometry.pointTools.PointConsumer;
import tools.GraphicTools;

public class TestShapeRunners extends TestGeneric {
	static final int PIXEL_SQUARE_POINT = 15, MAX_SQUARE_PIXEL = 800; //

	public TestShapeRunners() {}

	protected class ShapeRunnersModel extends ShapeModel {
		protected ShapeRunnersModel() {
			super(new ShapeCircle(false));
			ctpoi = new ColorToPaintOnImage();

//			sPoly = new ShapePolygon(new Point2D[] { //
//					new Point2D.Double(5, 5), //
//					new Point2D.Double(37, 20), //
//					new Point2D.Double(25, 35), //
//					new Point2D.Double(100, 30), //
//					new Point2D.Double(29, 43), //
//					new Point2D.Double(24, 50), // 5
//					new Point2D.Double(77, 100), //
//					new Point2D.Double(88, 111), //
//					new Point2D.Double(15, 170), //
//					new Point2D.Double(10, 10) }, false);
//			sPoly = new ShapePolygon(new Point2D[] { //
//					new Point2D.Double(5, 5), //
//					new Point2D.Double(10, 10), //
//					new Point2D.Double(25, 10), //
//					new Point2D.Double(15, 8) }, false);
			sPoly = new ShapePolygon(new Point2D[] { //
					new Point2D.Double(8, 8), //
					new Point2D.Double(8, 16), //
					new Point2D.Double(16, 16), //
					new Point2D.Double(16, 8) }, false);
			sPoly.setCenter(10, 10);
			showWeirdRectangle = false;
		}

		boolean showWeirdRectangle;
		int diameter;
		double angle;
		BufferedImage bi;
		MyObserver<AbstractShape2D> shapeObserver;
		MyObserver<ColorToPaintOnImage> imageObserver;
		MyObserver<ShapeRunnersImplemented> shapeRunnersImplementedObserver;
		final ColorToPaintOnImage ctpoi;
		ShapeRunnersImplemented[] shapesToTest;
		ProviderShapeRunnerImpl providerShapeRunner;
		AbstractShape2DFactory providerShapes;
		AbstractShapeRunner runner;
		ShapeRunnersImplemented selectedShape;

		ShapePolygon sPoly;

		@Override
		void init() {
			providerShapes = ProviderAbstractShape2DImpl.getInstance();
			providerShapeRunner = ProviderShapeRunnerImpl.getInstance();
			shapesToTest = new ShapeRunnersImplemented[] { ShapeRunnersImplemented.Disk,
					ShapeRunnersImplemented.Circumference, ShapeRunnersImplemented.Line,
					// ShapeRunnersImplemented.PolygonBorder, // no way to set points
					ShapeRunnersImplemented.RectangleBorder, ShapeRunnersImplemented.Rectangle, //
					ShapeRunnersImplemented.Triangle, ShapeRunnersImplemented.TriangleBorder };
			ctpoi.bi = bi = new BufferedImage(MAX_SQUARE_PIXEL, MAX_SQUARE_PIXEL, BufferedImage.TYPE_INT_ARGB);
//			setDiameter(1);
		}

		public void setShapeObserver(MyObserver<AbstractShape2D> shapeObserver) { this.shapeObserver = shapeObserver; }

		public void setShapeRunnersImplementedObserver(
				MyObserver<ShapeRunnersImplemented> shapeRunnersImplementedObserver) {
			this.shapeRunnersImplementedObserver = shapeRunnersImplementedObserver;
		}

		void setImageObserver(MyObserver<ColorToPaintOnImage> imageObserver) { this.imageObserver = imageObserver; }

		public AbstractShapeRunner gerRunner() { return runner; }

		public void setSelectedShape(ShapeRunnersImplemented selectedShape) {
			int rr;
			this.selectedShape = selectedShape;
//cancel out the previous
			if (s1 != null) {
				ctpoi.c = Color.LIGHT_GRAY;
				if (imageObserver != null)
					imageObserver.update(ctpoi);
			}
			//
			this.s1 = this.providerShapes.newEmptyShape(selectedShape);
			if (s1 == null) {
				System.out.println("S1 null with " + selectedShape);
				return;
			}
			if (s1 instanceof ShapeRectangle) { ((ShapeRectangle) s1).setWidth(diameter).setHeight(diameter); }
			//
			this.runner = this.providerShapeRunner.getShapeRunner(selectedShape);
			// rr = 1 + (diameter >> 1);
			rr = (diameter * 3) >> 2;
			s1.setDiameter(diameter);
			s1.setCenter(rr, rr);
			s1.setAngleRotation(angle);
			ctpoi.c = Color.BLUE;
			if (imageObserver != null)
				imageObserver.update(ctpoi);
			if (shapeRunnersImplementedObserver != null)
				shapeRunnersImplementedObserver.update(this.selectedShape);
			updateShapeAfterChanges(null);
		}

		public void setDiameter(int diameter) {
			int rr;
			if (diameter < 1)
				return;
			this.diameter = diameter;
			// rr = 1 + (diameter >> 1);
			rr = (diameter * 3) >> 2;
			updateShapeAfterChanges(sh -> {
				s1.setDiameter(diameter);
				s1.setCenter(rr, rr);
			});
		}

		public void setAngle(double angle) {
			this.angle = angle;
//			s1.setAngleRotation(angle);
			updateShapeAfterChanges(sh -> sh.setAngleRotation(angle));
		}

		public void setCenter(int x, int y) {
			updateShapeAfterChanges(sh -> { s1.setCenter(x / PIXEL_SQUARE_POINT, y / PIXEL_SQUARE_POINT); });
		}

		public void updateShapeAfterChanges() { updateShapeAfterChanges(null); }

		public void updateShapeAfterChanges(Consumer<AbstractShape2D> shapeUpdater) {
			ctpoi.c = Color.LIGHT_GRAY;
			if (imageObserver != null)
				imageObserver.update(ctpoi);

			if (shapeUpdater != null)
				shapeUpdater.accept(s1);

			ctpoi.c = Color.BLUE;
			if (imageObserver != null)
				imageObserver.update(ctpoi);
			if (shapeObserver != null)
				shapeObserver.update(s1);
		}
	} // end model

	protected class ColorToPaintOnImage {
		Color c;
		BufferedImage bi;
	}

	protected class SquarePainter implements PointConsumer, MyObserver<ColorToPaintOnImage> {
		private static final long serialVersionUID = 1L;
		ShapeRunnersModel m;
		Graphics g;
		ColorToPaintOnImage ctpoi;

		@Override
		public void accept(Point p) {
			BufferedImage bi;
			int x, col, px, py;
			bi = this.ctpoi.bi;
			if (g == null || bi == null)
				return;
			col = ctpoi.c.getRGB();
			py = (int) p.getY() * PIXEL_SQUARE_POINT;
			x = (int) p.getX() * PIXEL_SQUARE_POINT;
			try {
				for (int r = 0; r < PIXEL_SQUARE_POINT; r++) {
					px = x;
					for (int c = 0; c < PIXEL_SQUARE_POINT; c++)
						bi.setRGB(px++, py, col);
					py++;
				}
			} catch (Exception e) {
			}
		}

		@Override
		public void update(ColorToPaintOnImage ctpoi) {
			BufferedImage bi;
			AbstractShapeRunner sr;
			if (ctpoi == null)
				return;
			this.ctpoi = ctpoi;
			bi = this.ctpoi.bi;
			if (bi == null)
				return;
			this.g = bi.getGraphics();
			sr = m.gerRunner();
			if (m.showWeirdRectangle)
				ShapeRunnerPolygonBorder.getInstance().runShape(m.sPoly, this);
			if (sr != null) {
//				System.out.println(sr.getClass().getName());
				sr.runShape(m.s1, this);
			} else {
				System.out.println("RUNNER NULL");
			}
//			(m.c.isFilled() ? ShapeRunnerCircleFilled.getInstance() : ShapeRunnerCircleBorder.getInstance())
		}
	}

	protected abstract class ShapeFieldsManager {
		final ShapeRunnersModel m;
//		ShapeRunnersImplemented shapeManaging;
		final JPanel jpShapeFieldContainer;

		protected ShapeFieldsManager(ShapeRunnersModel m) {
			this.m = m;
			jpShapeFieldContainer = new JPanel();
			jpShapeFieldContainer.setLayout(new GridBagLayout());
//			jpShapeFieldContainer.setSize(200, 100);
//			jpShapeFieldContainer.setPreferredSize(jpShapeFieldContainer.getSize());
			jpShapeFieldContainer.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
		}

		public abstract AbstractShape2D getShape();

		public abstract void setShape(AbstractShape2D shape);

		public abstract void init();
	}

	// TODO ShapeRunnersView

	protected class ShapeRunnersView extends ShapeView implements MyObserver<ShapeRunnersImplemented> {
		protected ShapeRunnersView() {
			super();
			m = (ShapeRunnersModel) model;
			m.setShapeRunnersImplementedObserver(this);
		}

		ShapeRunnersModel m;
		SquarePainter squarePainter;
//		ShapeFieldsManager[]
		Map<ShapeRunnersImplemented, ShapeFieldsManager> shapeManagers;
		ShapeFieldsManager displayedShapeManager;
		JPanel jpGridViewer, jpShapeManagerContainer;
		JSpinner jsAngle;
		JComboBox<ShapeRunnersImplemented> jcbShapes;
		GridBagConstraints constraintsShapeManagerContainer;

		void addShapeFieldManager(ShapeRunnersImplemented sri, ShapeFieldsManager sfm) {
//			shapeManagers[sri.ordinal()] = sfm;
			shapeManagers.put(sri, sfm);
		}

		ShapeFieldsManager getShapeFieldManager(ShapeRunnersImplemented sri) {
//			return shapeManagers[sri.ordinal()] = sfm;
			return shapeManagers.get(sri);
		}

		@Override
		void init() {
			GridBagConstraints c;
			ShapeFieldsManager sfm;
//			JCheckBox jcb;

			//

//			shapeManagers = new ShapeFieldsManager[m.shapesToTest.length];
			shapeManagers = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight,
					(sri1, sri2) -> Integer.compare(sri1.ordinal(), sri2.ordinal()));
			sfm = new LineManager(m);
			sfm.init();
			addShapeFieldManager(ShapeRunnersImplemented.Line, sfm);
			//
			sfm = new CircleDiskManager(m);
			sfm.init();
			addShapeFieldManager(ShapeRunnersImplemented.Circumference, sfm);
			addShapeFieldManager(ShapeRunnersImplemented.Disk, sfm);
			//
			sfm = new RectangleManager(m);
			sfm.init();
			addShapeFieldManager(ShapeRunnersImplemented.RectangleBorder, sfm);
			addShapeFieldManager(ShapeRunnersImplemented.Rectangle, sfm);
			//
			sfm = null; // TODO not yet implemented
			addShapeFieldManager(ShapeRunnersImplemented.TriangleBorder, sfm);
			addShapeFieldManager(ShapeRunnersImplemented.Triangle, sfm);

			//

			c = new GridBagConstraints();
			squarePainter = new SquarePainter();
			squarePainter.m = m;
			m.setImageObserver(squarePainter);

			//

			jcbShapes = new JComboBox<>(m.shapesToTest);
			jcbShapes.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						m.setSelectedShape((ShapeRunnersImplemented) e.getItem());
					}
				}
			});
			c.gridx = 0;
			c.gridy = 0;

			c.weightx = 0;
			c.weighty = 0;
			c.gridwidth = 4;
			c.gridheight = 1;
			pContainer.add(jcbShapes, c);

			c.gridwidth = 2;
			c.gridy = 2;
			pContainer.add(new JLabel("Rotation angle"), c);
			jsAngle = new JSpinner(new SpinnerNumberModel(0, 0, 360, 5));
			c.gridx = 2;
			pContainer.add(jsAngle, c);

			c.gridx = 4;
			c.gridy = 0;
			c.gridwidth = 4;
			c.gridheight = 2;
			jpShapeManagerContainer = new JPanel();
			jpShapeManagerContainer.setSize(200, 100);
//			jpShapeManagerContainer.setPreferredSize(jpShapeManagerContainer.getSize());
			jpShapeManagerContainer.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
			pContainer.add(jpShapeManagerContainer, c);
			constraintsShapeManagerContainer = (GridBagConstraints) c.clone();
			constraintsShapeManagerContainer.gridx = 0;

//			jpShapeManagerContainer

			jpGridViewer = new JPanel() {
				private static final long serialVersionUID = -7775420L;

				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					if (squarePainter.ctpoi != null)
						g.drawImage(squarePainter.ctpoi.bi, 0, 0, null);
					g.setColor(Color.GREEN);
					GraphicTools.paintGrid(g, JPANEL_DIMENSION, JPANEL_DIMENSION, PIXEL_SQUARE_POINT);
//					squarePainter.g = g;
				}

				@Override
				public Dimension getSize() { return new Dimension(getWidth(), getHeight()); }

				@Override
				public int getWidth() { return JPANEL_DIMENSION; }

				@Override
				public int getHeight() { return JPANEL_DIMENSION; }
				/*
				 * @Override public int getX() { return 0; }
				 *
				 * @Override public int getY() { return 0; }
				 */
			};
			jpGridViewer.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 5));
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = c.weighty = c.gridwidth = c.gridheight = 8;
			pContainer.add(jpGridViewer, c);

			// listeners
			m.setShapeObserver(circ -> {
				jpGridViewer.repaint();
				fin.repaint();
			});

//			jcb.addChangeListener(l -> {
//				if (m.s1 instanceof AbstractFillable)
//					((AbstractFillable) m.s1).setFilled(jcb.isSelected());
//			});
			jsAngle.addChangeListener(e -> { m.setAngle(((Integer) jsAngle.getValue()).doubleValue()); });
			jpGridViewer.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) { m.setCenter(e.getX(), e.getY()); }
			});

			// compose the end

			jpGridViewer.setLocation(0, 0);
			jpGridViewer.setSize(JPANEL_DIMENSION, JPANEL_DIMENSION);
			jpGridViewer.setPreferredSize(jpGridViewer.getSize());
			jpGridViewer.requestFocus();
			fin.setSize(jpGridViewer.getWidth() + 100, jpGridViewer.getHeight() + 100);
			fin.setVisible(true);
		}

		@Override
		public void update(ShapeRunnersImplemented e) {
			System.out.println("LEL RECEIVING: " + e.name());
			if (this.displayedShapeManager != null) {
				this.jpShapeManagerContainer.remove(this.displayedShapeManager.jpShapeFieldContainer);
			}
			this.displayedShapeManager = this.getShapeFieldManager(e);
			if (this.displayedShapeManager == null) {
				System.out.println("displayedShapeManager is null");
				return;
			}
			this.displayedShapeManager.setShape(m.s1);
			this.jpShapeManagerContainer.add(this.displayedShapeManager.jpShapeFieldContainer,
					constraintsShapeManagerContainer);
			System.out.println("jpShapeManagerContainer dimension: " + jpShapeManagerContainer.getSize()
					+ ", manager size: " + this.displayedShapeManager.jpShapeFieldContainer.getSize());
		}
	} // end View

	protected abstract class DiameterDefyningShapeManager extends ShapeFieldsManager {
		protected DiameterDefyningShapeManager(ShapeRunnersModel m) { super(m); }

		TextField tfDiameter;
		JButton jbDiameter;
		JSpinner jsDiameter;

		@Override
		public void init() {
			GridBagConstraints c;
			c = new GridBagConstraints();
			c.gridx = c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.gridwidth = 2;
			c.gridheight = 1;
			tfDiameter = new TextField("16");
			super.jpShapeFieldContainer.add(tfDiameter, c);
			c.gridx = 2;
			jbDiameter = new JButton("Set diameter");
			super.jpShapeFieldContainer.add(jbDiameter, c);
			c.gridx = 4;
			jsDiameter = new JSpinner(new SpinnerNumberModel(16, 1, 1000, 1));
			super.jpShapeFieldContainer.add(jsDiameter, c);
			//
			jbDiameter.addActionListener(l -> { m.setDiameter(Integer.parseInt(tfDiameter.getText())); });
			jsDiameter.addChangeListener(e -> { m.setDiameter((Integer) jsDiameter.getValue()); });
		}
	}

	protected class CircleDiskManager extends DiameterDefyningShapeManager {

		protected CircleDiskManager(ShapeRunnersModel m) { super(m); }

		ShapeCircle shape;

		@Override
		public AbstractShape2D getShape() { return shape; }

		@Override
		public void setShape(AbstractShape2D shape) { this.shape = (ShapeCircle) shape; }
	}

	protected class StarDiskManager extends DiameterDefyningShapeManager {

		protected StarDiskManager(ShapeRunnersModel m) { super(m); }

//		StarCircle shape;

		@Override
		public AbstractShape2D getShape() {
			return null; // shape;
		}

		@Override
		public void setShape(AbstractShape2D shape) {
			// this.shape = (StarCircle) shape;
		}
	}

	protected class LineManager extends DiameterDefyningShapeManager {

		protected LineManager(ShapeRunnersModel m) { super(m); }

		ShapeLine shape;

		@Override
		public AbstractShape2D getShape() { return shape; }

		@Override
		public void setShape(AbstractShape2D shape) { this.shape = (ShapeLine) shape; }
	}

	protected class RectangleManager extends ShapeFieldsManager {

		protected RectangleManager(ShapeRunnersModel m) { super(m); }

		ShapeRectangle shape;
		JSpinner jsWidth, jsHeight;

		@Override
		public AbstractShape2D getShape() { return shape; }

		@Override
		public void setShape(AbstractShape2D shape) { this.shape = (ShapeRectangle) shape; }

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
//				shape.setWidth((Integer) jsWidth.getValue());
				m.updateShapeAfterChanges(sh -> ((ShapeRectangle) sh).setWidth((Integer) jsWidth.getValue()));
			});
			jsHeight.addChangeListener(l -> {
//				shape.setHeight((Integer) jsHeight.getValue());
				m.updateShapeAfterChanges(sh -> ((ShapeRectangle) sh).setHeight((Integer) jsHeight.getValue()));
			});
		}
	}

	//

	// TODO END CLASSES

	@Override
	ShapeModel newModel() { return new ShapeRunnersModel(); }

	@Override
	ShapeView newView() { return new ShapeRunnersView(); }

	@Override
	void init() {}

	public static void main(String[] args) {
		TestShapeRunners t;
		t = new TestShapeRunners();
		t.startTest();
	}
}