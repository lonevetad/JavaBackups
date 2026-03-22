package tests.tGeom;

import java.awt.geom.Point2D;

import tools.MathUtilities;

public class TestSlope {

	public TestSlope() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		Point2D p1, p2;
		p1 = new Point2D.Double(5, 5);
		p2 = new Point2D.Double(12, 8);
		performTest(p1, p2);

		p1 = new Point2D.Double(5, 25);
		System.out.println("\n\nchange p1 to: " + p1);
		performTest(p1, p2);
	}

	static void performTest(Point2D p1, Point2D p2) {
		double d;
		d = MathUtilities.slope(p1, p2);
		System.out.println("slope p1->p2: " + d);
		d = MathUtilities.slope(p2, p1);
		System.out.println("slope p2->p1: " + d);//
		d = MathUtilities.angleDegrees(p1, p2);
		System.out.println("angleDegrees p1->p2: " + d);
		d = MathUtilities.angleDegrees(p2, p1);
		System.out.println("angleDegrees p2->p1: " + d);

	}
}