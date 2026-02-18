package tests.tGeom;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import geometry.implementations.shapes.ShapeCircle;
import tools.MathUtilities;

public class TestCircleIntersection extends TestShapeIntersection {
	@Override
	ShapeModel newModel() {
		return new CircleIntersectionModel();
	}

	@Override
	ShapeView newView() {
		return new CirclesIntersectionView();
	}

	@Override
	void init() {

	}

	//
	protected class CircleIntersectionModel extends ShapeIntersectionModel {
		final int RADIUS_FIXED_CIRCLE = 200;

		protected CircleIntersectionModel() {
			super(new ShapeCircle(false), new ShapeCircle(false));

		}

		// used as cache
		ShapeCircle c, cToMove;

		public void moveCircleTo(int x, int y) {
			cToMove.setCenter(x, y);
			super.setIntersections(MathUtilities.getCircleToCircleIntersection(c.getCenter(), c.getRadius(),
					cToMove.getCenter(), cToMove.getRadius()));
		}

		@Override
		void init() {
			int r_1_5;
			c = (ShapeCircle) super.s1;
			cToMove = (ShapeCircle) super.s2;
			r_1_5 = RADIUS_FIXED_CIRCLE + (RADIUS_FIXED_CIRCLE >> 1);
			c.setCenter(r_1_5, r_1_5);
			c.setDiameter(RADIUS_FIXED_CIRCLE);
			cToMove.setDiameter(RADIUS_FIXED_CIRCLE);
		}
	}

	protected class CirclesIntersectionView extends ShapeIntersectionView {
		@Override
		void init() {
			GridBagConstraints c;
			CircleIntersectionModel m;
			JPanel jp;
			m = (CircleIntersectionModel) model;

			c = new GridBagConstraints();
			c.gridx = c.gridy = 0;
			c.weightx = c.weighty = 0;
			c.gridwidth = 10;
			c.gridheight = 10;

			jp = new JPanel() {
				private static final long serialVersionUID = -7775420L;

				@Override
				protected void paintComponent(Graphics g) {
					ShapeCircle circle;
					List<Point2D> intres;
					g.setColor(Color.GREEN);
					circle = m.c;
					g.drawOval(circle.getXCenter() - circle.getRadius(), circle.getYCenter() - circle.getRadius(), //
							circle.getDiameter(), circle.getDiameter());
					drawPoint(g, circle.getCenter(), "C1");
//
					g.setColor(Color.BLUE);
					circle = m.cToMove;
					g.drawOval(circle.getXCenter() - circle.getRadius(), circle.getYCenter() - circle.getRadius(), //
							circle.getDiameter(), circle.getDiameter());
					drawPoint(g, circle.getCenter(), "C2");

					g.setColor(Color.BLACK);
					intres = m.getIntersections();

					if (intres != null) {
						drawPoint(g, intres.get(0), "P3");
						if (intres.size() >= 2)
							drawPoint(g, intres.get(1), "P4");
					}
				}

				@Override
				public Dimension getSize() {
					return new Dimension(getWidth(), getHeight());
				}

				@Override
				public int getWidth() {
					return JPANEL_DIMENSION;

				}

				@Override
				public int getHeight() {
					return JPANEL_DIMENSION;
				}

				/*
				 * @Override public int getX() { return 0; }
				 *
				 * @Override public int getY() { return 0; }
				 */
			};
			setCIntersectionShower(jp);
			jp.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 5));
			jp.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					m.moveCircleTo(e.getX(), e.getY());
				}
			});
			m.setListPointIntersectionObserver(l -> {
				jp.repaint();
				fin.repaint();
			});
			pContainer.add(jp, c);
			jp.setLocation(0, 0);
			jp.setSize(JPANEL_DIMENSION, JPANEL_DIMENSION);
			jp.setPreferredSize(jp.getSize());
			jp.requestFocus();
			fin.setSize(jp.getSize());
			fin.setVisible(true);
		}

	}

	public static void main(String[] args) {
		TestCircleIntersection t;
		System.out.println("START");
		t = new TestCircleIntersection();
		t.startTest();
		System.out.println("init startTest");
	}
}