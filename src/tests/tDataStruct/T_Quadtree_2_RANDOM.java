package tests.tDataStruct;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Random;

import dataStructures.quadTree.Quadtree;

public class T_Quadtree_2_RANDOM<D> {

	public static Point2D.Double p(double x, double y) {
		return new Point2D.Double(x, y);
	}

	//

	//

	public static void main(String[] args) {
		int maxPointsPerSubsection, len;
		double maxSide;
		Quadtree<Point2D.Double, Object> qt;
		Point2D.Double[] points;
		Random r;

		maxPointsPerSubsection = 8;
		//

		maxSide = 1000;
		len = 1000000;
		r = new Random(161803);
		System.out.println("start, adding " + len + " points");

		points = new Point2D.Double[len];

		System.out.println("start creation");
		for (int i = 0; i < len; i++) {
			if (i % 100000 == 0) {
				System.out.println(i);
			}
			points[i] = new Point2D.Double(r.nextDouble(maxSide), r.nextDouble(maxSide));
		}

		long t = System.currentTimeMillis();
		qt = new Quadtree<>(points, null, maxPointsPerSubsection);
		t = System.currentTimeMillis() - t;
		System.out.println("\n\n\ncreated, took " + t + " milliseconds");

//		System.out.println(qt.toString());

//		System.out.println("\n\n\n adding a very distant point\n");
//		qt.addPoint(p(128, 128), null);
//		System.out.println("results in ...");
//		System.out.println(qt.toString());

		/*
		 */
		//

		double x, y, w, h;
		ArrayList<Entry<Point2D.Double, Object>> queryResult;

		x = 4;
		y = 5;
		w = 12;
		h = 7; // 20;
		System.out.printf("starting query: x: %f, y: %f, w: %f, h: %f\n", x, y, w, h);
		t = System.currentTimeMillis();
		queryResult = qt.query(x, y, w, h);
		t = System.currentTimeMillis() - t;
		System.out.println("collected " + queryResult.size() + " points in " + t + " milliseconds:");
		for (Entry<Point2D.Double, Object> e : queryResult) {
			System.out.println("point: " + e.getKey() + " -> data: " + e.getValue());
		}
	}

}