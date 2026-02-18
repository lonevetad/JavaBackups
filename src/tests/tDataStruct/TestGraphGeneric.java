package tests.tDataStruct;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

import dataStructures.graph.GraphSimple;

public abstract class TestGraphGeneric {

	static Point2D meanPoint(Point2D p1, Point2D p2) {
		return new Point2D.Double((p1.getX() + p2.getX()) / 2.0, (p1.getY() + p2.getY()) / 2.0);
	}

	static String pts(Point2D p) {
		return "(" + p.getX() + ", " + p.getY() + ")";
	}

	static void drawPoint(Graphics g, Point2D p) {
		g.drawString(pts(p), (int) p.getX(), (int) p.getY());
	}

	static void printGraph(Graphics g, GraphSimple<Point2D, Double> gr) {
		gr.forEach((p1, n1) -> {
			drawPoint(g, p1);
			n1.forEachAdjacents((n2, d) -> {
				Point2D p2;
				p2 = n2.getElem();
				drawPoint(g, p2);
				g.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
				drawPoint(g, meanPoint(p1, p2));
			});
		});
	}

	//

	//

	public static class ViewGraphTest {
		ModelGraphTest model;

		public ViewGraphTest(ModelGraphTest model) {
			this.model = model;
		}

		public void drawGraphEdges(Graphics g, List<LineInfo> edges) {
			edges.forEach(l -> {
				g.drawString(l.s1, (int) l.p1.getX(), (int) l.p1.getY());
				g.drawString(l.s2, (int) l.p2.getX(), (int) l.p2.getY());
				g.drawString(l.smean, (int) l.pmean.getX(), (int) l.pmean.getY());
				g.drawLine((int) l.p1.getX(), (int) l.p1.getY(), (int) l.p2.getX(), (int) l.p2.getY());
			});
		}
	}

	public static class ModelGraphTest {
		GraphSimple<Point2D, Double> graph;
		List<LineInfo> edgesInfo;

		public void setGraph(GraphSimple<Point2D, Double> graph) {
			this.graph = graph;
			this.edgesInfo = new LinkedList<>();
			graph.forEach((p1, n1) -> {
				final String s1;
				s1 = pts(p1);
				n1.forEachAdjacents((n2, d) -> {
					Point2D p2;
					p2 = n2.getElem();
					edgesInfo.add(new LineInfo(p1, s1, p2));
				});
			});
		}
	}

	public static class LineInfo {
		Point2D p1, p2, pmean;
		String s1, s2, smean;

		public LineInfo(Point2D p1, Point2D p2) {
			this(p1, pts(p1), p2);
		}

		public LineInfo(Point2D p1, String s1, Point2D p2) {
			this.p1 = p1;
			this.p2 = p2;
			this.pmean = meanPoint(p1, p2);
			this.s1 = s1;
			this.s2 = pts(p2);
			this.smean = pts(pmean);

		}
	}
}