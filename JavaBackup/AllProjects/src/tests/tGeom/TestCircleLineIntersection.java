package tests.tGeom;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import geometry.implementations.intersectors.LineToCircleIntersector;
import geometry.implementations.shapes.ShapeCircle;
import geometry.implementations.shapes.ShapeLine;
import tools.MathUtilities;

public class TestCircleLineIntersection extends TestShapeIntersection {

	protected class LineCircleIntersectionModel extends ShapeIntersectionModel {
		protected LineCircleIntersectionModel() {
			super(new ShapeLine(30, 200, 200, 50), //
					new ShapeCircle(250, 150, false, 200)/* .setCenter(110, 110) */);
		}

		// as model
		Line2D line;
		ShapeLine sl;
		ShapeCircle sc;

		public void setCoordinates(int px, int py, boolean isStart) {
			Point2D p;
			Line2D l;
			if (isStart)
				this.line.setLine(px, py, (p = this.line.getP2()).getX(), p.getY());
			else
				this.line.setLine((p = this.line.getP1()).getX(), p.getY(), px, py);
			this.s1 = this.sl = new ShapeLine(this.line);
			System.out.println("\n\nLine: " + line.getP1() + " - " + line.getP2() + " ....... ang: ");
			l = this.sl.toLine();
			System.out.println("ShapeLine: " + l.getP1() + " - " + l.getP2());
			updateIntersection();
//
		}
		/*
		 * double getAngDeg(Line2D l) { return 0; }
		 */

		public void setCoordinates_OLD(int px, int py, boolean isStart) {
			double angRad, slope, diameter, radius;
			Point2D pOld, pNew;
			Polygon p;
//			Line2D l;
//			l = line;
//			if (isStart) {
//				p = l.getP2();
//				l.setLine(px, py, p.getX(), p.getY());
//			} else {
//				p = l.getP1();
//				l.setLine(p.getX(), p.getY(), px, py);
//			}
			p = sl.toPolygon();
			if (isStart)
				pOld = new Point(p.xpoints[0], p.ypoints[0]);
			else
				pOld = new Point(p.xpoints[1], p.ypoints[1]);
			pNew = new Point(px, py);
			diameter = MathUtilities.distance(pOld, pNew);
			radius = diameter / 2.0;
			slope = MathUtilities.slope(pOld, pNew);
			angRad = Math.atan(slope);
			sl.setAngleRotation(Math.toDegrees(angRad));
			sl.setCenter(//
					(int) Math.round(pOld.getX() + radius * Math.cos(angRad)), //
					(int) Math.round(pOld.getY() + radius * Math.sin(angRad)));
			sl.setDiameter((int) Math.round(diameter));
			this.line = sl.toLine();
			updateIntersection();
//			pointIntersectionObserver.update(pointIntersection);
		}

		public void updateIntersection() {
//			setPointIntersection( // MathUtilities.areLinesIntersecting
//					getIntersect(line, line2));
//			this.setIntersections(LineToCircleIntersector.getInstance().computeIntersectionPoints(sl, sc));
			this.setIntersections(//
					LineToCircleIntersector.getInstance().computeIntersectionPoints(sl, sc)
//					MathUtilities.getCircleLineIntersections(this.sc.getCenter(), this.sc.getDiameter(), this.line)//
			);
		}

		@Override
		void init() {
			pointIntersection = null;
			sl = (ShapeLine) s1;
			sc = (ShapeCircle) s2;
			this.line = sl.toLine();

		}

	}

	protected class LineCircleIntersectionView extends ShapeIntersectionView {

		@Override
		void init() {
			JPanel jp;
			JLabel jl;
			final LineCircleIntersectionModel m;
			GridBagConstraints c;
//			final PointUpdater pu;
			m = (LineCircleIntersectionModel) model;

			c = new GridBagConstraints();
			c.gridx = c.gridy = 0;
			c.weightx = c.weighty = 0;
			c.gridwidth = 4;
			c.gridheight = 2;

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
					if (m.line != null) {
						g.drawLine((int) m.line.getX1(), (int) m.line.getY1(), (int) m.line.getX2(),
								(int) m.line.getY2());
						drawPoint(g, m.line.getP1(), "pStart1");
						drawPoint(g, m.line.getP2(), "pEnd1");
					}
					g.setColor(Color.GREEN);
					g.drawOval(m.sc.getXCenter() - (m.sc.getWidth() >> 1), m.sc.getYCenter() - (m.sc.getHeight() >> 1),
							m.sc.getWidth(), m.sc.getHeight());

					g.setColor(Color.BLACK);
					if (m.getPointIntersection() != null)
						drawPoint(g, m.getPointIntersection(), "+");

//					g.setColor(Color.PINK);
//					g.drawPolygon(m.p1);
//
//					g.setColor(Color.LIGHT_GRAY);
//					g.drawPolygon(m.p2);

					g.setColor(Color.BLUE);
					i = 0;
					inters = m.getIntersections();
					if (inters != null)
						for (Point2D p : inters)
							drawPoint(g, p, i++ + "");
					else
						System.out.println("intersection null");
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
			m.setListPointIntersectionObserver(l -> fin.repaint());
			jp.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					boolean isStart;
					int x, y;
					x = e.getX();
					y = e.getY();
					isStart = e.getButton() == MouseEvent.BUTTON1;
					m.setCoordinates(x, y, isStart);
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
		return new LineCircleIntersectionModel();
	}

	@Override
	ShapeView newView() {
		return new LineCircleIntersectionView();
	}

	@Override
	void init() {
	}

	//

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
		TestCircleLineIntersection tli;
		System.out.println("START");
		tli = new TestCircleLineIntersection();
		tli.startTest();
//		intersect();
	}
}