package tests.tGeom;

import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.Random;

import geometry.pointTools.PolygonUtilities;

public class TestStressPolygonIsInside {

	public static void main(String[] args) {
		int vertexesAmount, repetitions, minCoordinates, maxCoordinates, deltaCoordinates;
		int[] xx, yy;
		long start, delta;
		Polygon p;
		Random r;
		Point2D pointInside, pointOutside;

//		System.out.println(PolygonUtilities
//				.areaPolygon2D(new Polygon(new int[] { 0, 10, 0, 10, 0 }, new int[] { 0, 0, 5, 10, 10 }, 5)));

		r = new Random();
		vertexesAmount = 10000;
		repetitions = 100000;
		minCoordinates = 1000;
		maxCoordinates = 5000;
		deltaCoordinates = maxCoordinates - minCoordinates;
		System.out.println("let's start creating");
		xx = new int[vertexesAmount];
		yy = new int[vertexesAmount];

		for (int i = 0; i < vertexesAmount; i++) {
			xx[i] = minCoordinates + r.nextInt(deltaCoordinates);
			yy[i] = minCoordinates + r.nextInt(deltaCoordinates);
		}
		p = new Polygon(xx, yy, vertexesAmount);
		System.out.println("end building, now create points");
		pointOutside = new Point2D.Double(0, 0);

		do {
			pointInside = new Point2D.Double(minCoordinates + r.nextInt(deltaCoordinates),
					minCoordinates + r.nextInt(deltaCoordinates));
		} while (!p.contains(pointInside));
		System.out.println("point inside obtained: " + pointInside);
		System.out.println("Now test the methods");

		for (int x = 0; x < 10; x++) {
			System.out.println("\n\n++++++\nrun number: " + x);

			System.out.println("---\nstart mine.. inside");
			start = System.currentTimeMillis();
			for (int i = 0; i < repetitions; i++){
				PolygonUtilities.isInside(pointInside, p);
			}
			delta = System.currentTimeMillis() - start;
			System.out.print("\tdelta: " + delta + "\noutside:\n\t");
			start = System.currentTimeMillis();
			for (int i = 0; i < repetitions; i++){
				PolygonUtilities.isInside(pointOutside, p);
			}
			delta = System.currentTimeMillis() - start;
			System.out.println(delta);

			System.out.println("---\nstart original.. inside");
			start = System.currentTimeMillis();
			for (int i = 0; i < repetitions; i++)
				p.contains(pointInside);
			delta = System.currentTimeMillis() - start;
			System.out.print("\tdelta: " + delta + "\noutside:\n\t");
			start = System.currentTimeMillis();
			for (int i = 0; i < repetitions; i++)
				p.contains(pointOutside);
			delta = System.currentTimeMillis() - start;
			System.out.println(delta);
			/**
			 * Results:
			 *
			 *
			 * run number: 0<br>
			 * start original.. inside<br>
			 * delta: 51965<br>
			 * outside:<br>
			 * 6<br>
			 * ---<br>
			 * start mine.. inside<br>
			 * delta: 22729<br>
			 * outside:<br>
			 * 14837<br>
			 * <br>
			 * <br>
			 * ++++++<br>
			 * run number: 1<br>
			 * start original.. inside<br>
			 * delta: 52570<br>
			 * outside:<br>
			 * 1<br>
			 * ---<br>
			 * start mine.. inside<br>
			 * delta: 22871<br>
			 * outside:<br>
			 * 14891<br>
			 * <br>
			 * <br>
			 * ++++++<br>
			 * run number: 2<br>
			 * start original.. inside<br>
			 * delta: 53367<br>
			 * outside:<br>
			 * 7<br>
			 * ---<br>
			 * start mine.. inside<br>
			 * delta: 22750<br>
			 * outside:<br>
			 * 14834<br>
			 * <br>
			 * <br>
			 * ++++++<br>
			 * run number: 3<br>
			 * start original.. inside<br>
			 * delta: 53310<br>
			 * outside:<br>
			 * 1<br>
			 * ---<br>
			 * start mine.. inside<br>
			 * delta: 22713<br>
			 * outside:<br>
			 * 14844<br>
			 * <br>
			 * <br>
			 * ++++++<br>
			 * run number: 4<br>
			 * start original.. inside<br>
			 * delta: 53252<br>
			 * outside:<br>
			 * 0<br>
			 * ---<br>
			 * start mine.. inside<br>
			 * delta: 22743<br>
			 * outside:<br>
			 * 14898<br>
			 * <br>
			 * <br>
			 * ++++++<br>
			 * run number: 5<br>
			 * start original.. inside<br>
			 * delta: 53299<br>
			 * outside:<br>
			 * 1<br>
			 * ---<br>
			 * start mine.. inside<br>
			 * delta: 22738<br>
			 * outside:<br>
			 * 14864<br>
			 * <br>
			 * <br>
			 * ++++++<br>
			 * run number: 6<br>
			 * start original.. inside<br>
			 * delta: 53343<br>
			 * outside:<br>
			 * 1<br>
			 * ---<br>
			 * start mine.. inside<br>
			 * delta: 22762<br>
			 * outside:<br>
			 * 14900<br>
			 * <br>
			 * <br>
			 * ++++++<br>
			 * run number: 7<br>
			 * start original.. inside<br>
			 * delta: 53357<br>
			 * outside:<br>
			 * 2<br>
			 * ---<br>
			 * start mine.. inside<br>
			 * delta: 22854<br>
			 * outside:<br>
			 * 14901<br>
			 * <br>
			 * <br>
			 * ++++++<br>
			 * run number: 8<br>
			 * start original.. inside<br>
			 * delta: 53269<br>
			 * outside:<br>
			 * 2<br>
			 * ---<br>
			 * start mine.. inside<br>
			 * delta: 22731<br>
			 * outside:<br>
			 * 14860<br>
			 * <br>
			 * <br>
			 * ++++++<br>
			 * run number: 9<br>
			 * start original.. inside<br>
			 * delta: 53247<br>
			 * outside:<br>
			 * 2<br>
			 * ---<br>
			 * start mine.. inside<br>
			 * delta: 22750<br>
			 * outside:<br>
			 * 14851<br>
			 *
			 */
		}
	}
}