package tests.tGeom;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Polygon;
import java.awt.geom.Point2D;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import geometry.AbstractShape2D;

public abstract class TestGeneric {
	static final int JPANEL_DIMENSION = 700;

	public static interface MyObserver<E> {
		void update(E e);
	}

	protected abstract class ShapeModel {
		protected ShapeModel(AbstractShape2D s1) {
			this.s1 = s1;
//			init();
		}

		AbstractShape2D s1;
		Polygon p1;

		abstract void init();
	}

	protected abstract class ShapeView {
		ShapeView() {
			fin = new JFrame("Test Intersections");
			fin.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			pContainer = fin.getContentPane();// new JPanel();
			pContainer.setLayout(new GridBagLayout());
//			init();

		}

		final JFrame fin;
		Container pContainer;

		abstract void init();

		protected void drawPoint(Graphics g, Point2D p, String text) {
			int x, y;
			x = (int) p.getX();
			y = (int) p.getY();
			g.drawString(text + "(" + x + "," + y + ")", x, y);
		}
	}
	//

	public TestGeneric() {
		this.model = newModel();
		this.view = newView();
	}

	protected final ShapeModel model;
	protected final ShapeView view;

	//

	//

	final void startTest() {
		model.init();
		view.init();
		init();
	}

	abstract ShapeModel newModel();

	abstract ShapeView newView();

	abstract void init();

	//

	//

//	public static void main(String[] args) {
//		TestGeneric tg;
//		tg = new TestGeneric();
//		tg.startTest();
//	}
}