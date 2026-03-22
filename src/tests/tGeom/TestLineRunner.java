package tests.tGeom;

import geometry.implementations.shapeRunners.ShapeRunnerLine;
import geometry.implementations.shapes.ShapeLine;
import geometry.pointTools.PolygonUtilities;

public class TestLineRunner {
	public static void main(String[] args) {
		ShapeLine sl;
		sl = new ShapeLine(60.0, 100, 100, 20);
		System.out.println("center: " + sl.getCenter());
		System.out.println(PolygonUtilities.polygonToString(sl.toPolygon()));
		ShapeRunnerLine.getInstance().runShape(sl, p -> System.out.println(p));
	}
}