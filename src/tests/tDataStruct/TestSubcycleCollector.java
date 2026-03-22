package tests.tDataStruct;

import java.awt.geom.Point2D;
import java.util.List;

import dataStructures.graph.GraphSimple;
import dataStructures.graph.GraphSimpleAsynchronized;
import dataStructures.graph.GraphSimpleGenerator;
import dataStructures.graph.cycles.SubcyclesCollector;
import dataStructures.graph.cycles.SubcyclesCollectorNaive;
import dataStructures.graph.pathfind.PathFinderDijkstra;
import tools.Comparators;
import tools.MathUtilities;

public class TestSubcycleCollector extends TestGraphGeneric {

	static final double[][] POINTS_COORD_GRAPH = new double[][] { //
			new double[] { 0, 10, 4, 8, 2, 7, // 5
					8, 5, 3, 2 //
			},
			// yy
			new double[] { 0, 0, 4, 6, 8, 1, // 5
					5, 5, 2, 11 } //
	};// 10 points
	static final Point2D[] POINTS_GRAPH;
	// indexes on array are "from", values are "to"
	static final int[][] LINK_INDEXES_TO = new int[][] { //
			new int[] { 2, 8 } // 0-2-3-5 .. -9
			, new int[] { 4 } // 1-4-7
			, new int[] { 3, 6 } //
			, new int[] { 5 } //
			, new int[] { 7 } //
			, new int[] { 0 } // 5
			, new int[] { 8 } // 6-8-2
			, new int[] { 1 } //
			, new int[] { 9, 2 } // 8-9-0
			, new int[] { 0 } // 9
	};
	static {
		int i, len;
		double[] xx, yy;
		len = POINTS_COORD_GRAPH[0].length;
		POINTS_GRAPH = new Point2D[len];
		xx = POINTS_COORD_GRAPH[0];
		yy = POINTS_COORD_GRAPH[1];
		i = -1;
		while (++i < len) {
			POINTS_GRAPH[i] = new Point2D.Double(xx[i], yy[i]);
		}
	}

	//

	//

	ModelGraphTest model;
	ViewGraphTest view;

	//

	//

	public static void main(String[] args) {
		int i, len;
		GraphSimple<Point2D, Double> g;
		Point2D ptemp, padj;
		List<GraphSimple<Point2D, Double>> subcycles;
//		PointEdgesIntersectionDetector peid;
//		IntersectionInstantiator<Point2D, Double> ii;
//		DistanceCalculator<Point2D, Double> dc;
		SubcyclesCollector<Point2D, Double> sc;
		GraphSimpleGenerator<Point2D, Double> gsg;

		System.out.println("START");
		g = new GraphSimpleAsynchronized<>(true, new PathFinderDijkstra<>(),
				Comparators.POINT_2D_COMPARATOR_LOWEST_LEFTMOST_FIRST);
		len = POINTS_GRAPH.length;
		i = -1;
		while (++i < len) {
			g.addNode(POINTS_GRAPH[i]);
		}
		// links
		len = LINK_INDEXES_TO.length;
		i = -1;
		while (++i < len) {
			ptemp = POINTS_GRAPH[i];
			for (int indexAdj : LINK_INDEXES_TO[i]) {
				padj = POINTS_GRAPH[indexAdj];
				g.addLink(ptemp, padj, MathUtilities.distance(ptemp, padj));
			}
		}

		// built

		System.out.println("BUILT:");
		System.out.println(g);
		System.out.println("\n\n NOW retrieve the subcycles ");
//		peid = new PointEdgesIntersectionDetector();
//		ii = new PointEdgesIntersectionDetector.PointIntersectionInstantiator();
//		dc = MathUtilities::distance;
		sc = new SubcyclesCollectorNaive<>();
		gsg = (b, pf, eComp) -> new GraphSimpleAsynchronized<>(b, pf, eComp);
		subcycles = sc.collectCycles(false, g, gsg);
//		g.splitAndCollect(peid, ii, dc, sc, false,gsg);
		if (subcycles == null) {
			System.out.println("ERROR, null subcycles ");
		} else {
			System.out.println("\n\n\n\n ......got:");
			for (GraphSimple<Point2D, Double> gs : subcycles) {
				System.out.println("---------------");
				System.out.println(gs);
			}
		}
	}

}