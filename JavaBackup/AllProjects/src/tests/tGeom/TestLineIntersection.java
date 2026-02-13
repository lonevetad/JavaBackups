package tests.tGeom;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import geometry.implementations.intersectors.PolygonToPolygonIntersection;
import geometry.implementations.shapes.ShapePolygon;
import geometry.implementations.shapes.ShapePolygonRegular;
import tools.MathUtilities;

public class TestLineIntersection extends TestShapeIntersection {

	protected class LineIntersectionModel extends ShapeIntersectionModel {
		protected LineIntersectionModel() {
			super(new ShapePolygonRegular(0, 200, 200, true, 300, 7), //
					(new ShapePolygon(new Point2D[] { // new Point2D.Double(5, 5), //<br>
							new Point2D.Double(374, 20), // <br>
							new Point2D.Double(25, 356), // <br>
							new Point2D.Double(200, 300), // <br>
							new Point2D.Double(239, 432), // <br>
							new Point2D.Double(234, 50), // 5<br>
							new Point2D.Double(77, 100), // <br>
							new Point2D.Double(188, 111), // <br>
							new Point2D.Double(15, 200), // <br>
							new Point2D.Double(10, 10) }, false))/* .setCenter(110, 110) */);
		}

		// as model
		Line2D line1, line2;

		public void setCoordinates(int px, int py, boolean isStart, boolean isFirst) {
			Point2D p;
			Line2D l;
			l = (isFirst) ? line1 : line2;
			if (isStart) {
				p = l.getP2();
				l.setLine(px, py, p.getX(), p.getY());
			} else {
				p = l.getP1();
				l.setLine(p.getX(), p.getY(), px, py);
			}
			updateIntersection();
			pointIntersectionObserver.update(pointIntersection);
		}

		public void updateIntersection() {
			setPointIntersection( // MathUtilities.areLinesIntersecting
					getIntersect(line1, line2));
		}

		@Override
		void init() {
			pointIntersection = null;
			line1 = new Line2D.Double(0.0, 0.0, 2.0, 2.0);
			line2 = new Line2D.Double(2.0, 0.0, 0.0, 2.0);

			p1 = s1.toPolygon();
			p2 = s2.toPolygon();
			intersections = PolygonToPolygonIntersection.getInstance().computeIntersectionPoints(s1, s2);
		}

	}

	protected class LineIntersectionView extends ShapeIntersectionView {

		@Override
		void init() {
			JPanel jp;
			JCheckBox jcb;
			JLabel jl;
			final LineIntersectionModel m;
			GridBagConstraints c;
//			final PointUpdater pu;
			m = (LineIntersectionModel) model;

			jcb = new JCheckBox("Select to set the first line");

			c = new GridBagConstraints();
			c.gridx = c.gridy = 0;
			c.weightx = c.weighty = 0;
			c.gridwidth = 4;
			c.gridheight = 2;
			pContainer.add(jcb, c);

			jl = new JLabel("no intersection");
			c.gridx = 4;
			pContainer.add(jl, c);

			jp = new JPanel() {
				private static final long serialVersionUID = -7775420L;

				@Override
				protected void paintComponent(Graphics g) {
					int i;
					List<Point2D> inters;
					g.setColor(Color.BLUE);
					g.drawLine(//
							(int) m.line1.getX1(), //
							(int) m.line1.getY1(), //
							(int) m.line1.getX2(), (int) m.line1.getY2());
					g.setColor(Color.GREEN);
					g.drawLine((int) m.line2.getX1(), (int) m.line2.getY1(), (int) m.line2.getX2(),
							(int) m.line2.getY2());

					g.setColor(Color.BLACK);
					drawPoint(g, m.line1.getP1(), "pStart1");
					drawPoint(g, m.line1.getP2(), "pEnd1");
					drawPoint(g, m.line2.getP1(), "pStart2");
					drawPoint(g, m.line2.getP2(), "pEnd2");
					if (m.getPointIntersection() != null)
						drawPoint(g, m.getPointIntersection(), "+");

					g.setColor(Color.PINK);
					g.drawPolygon(m.p1);

					g.setColor(Color.LIGHT_GRAY);
					g.drawPolygon(m.p2);

					g.setColor(Color.BLUE);
					i = 0;
					inters = m.getIntersections();
					if (inters != null)
						for (Point2D p : inters)
							drawPoint(g, p, i++ + "");
				}

				@Override
				public Dimension getSize() {
					return new Dimension(getWidth(), getHeight());
				}

				@Override
				public int getWidth() {
					return 500;

				}

				@Override
				public int getHeight() {
					return 500;
				}
			};
			setCIntersectionShower(jp);
			jp.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 5));
			m.setPointIntersectionObserver((p) -> {
				jl.setText(p == null ? "no intersection" : p.toString());
				jp.repaint();
				fin.repaint();
			});
			jp.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					boolean isStart, isFirst;
					int x, y;
					x = e.getX();
					y = e.getY();
					isFirst = jcb.isSelected();
					isStart = e.getButton() == MouseEvent.BUTTON1;
					m.setCoordinates(x, y, isStart, isFirst);
				}
			});
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = c.weighty = 8;
			c.gridwidth = 8;
			c.gridheight = 8;
			pContainer.add(jp, c);
//			fin.add(pContainer);
//			fin.pack();
			jp.setSize(500, 500);
			jp.setPreferredSize(jp.getSize());
			fin.setSize(jp.getSize());
			fin.setVisible(true);
			System.out.println(jp.getSize());

		}

	}

	// TODO END CLASSES

	@Override
	ShapeModel newModel() {
		return new LineIntersectionModel();
	}

	@Override
	ShapeView newView() {
		return new LineIntersectionView();
	}

	@Override
	void init() {
	}

	//

	static Point2D getIntersect(Line2D l1, Line2D l2) {
		return MathUtilities.areLinesIntersecting(l1.getP1(), l1.getP2(), l2.getP1(), l2.getP2());
	}

	//

	//

	/*
	 * static class PointUpdater implements MyObserver { boolean isStart, isFirst;
	 * final Point2D p; final ShapeModel model; final Component compToUpdate; public
	 * PointUpdater(Component compToUpdate, ShapeModel model) { super();
	 * this.compToUpdate = compToUpdate; this.model = model; this.p = new
	 * Point2D.Double(); } void setStuffs(int px, int py, boolean isStart, boolean
	 * isFirst) { this.p.setLocation(px, py); this.isFirst = isFirst; this.isStart =
	 * isStart; }
	 *
	 * @Override public void update() { model.setCoordinates(p, isStart, isFirst); }
	 * }
	 */

	public static void main(String[] args) {
		TestLineIntersection tli;
		System.out.println("START");
		tli = new TestLineIntersection();
		tli.startTest();
//		intersect();
	}

}