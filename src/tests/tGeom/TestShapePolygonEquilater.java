package tests.tGeom;

import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import geometry.implementations.shapes.ShapePolygonRegular;
import geometry.pointTools.PolygonUtilities;

public class TestShapePolygonEquilater {
	static final double degreesPerClick = 5;
	static final Polygon SEMI_SQUARE;

	static {
		int[] xx, yy;
		xx = new int[] { 100, 200, 00, //
				0, 20, 20, 0, 0,
				//
				200, 100 };
		yy = new int[] { 50, 50, 100, //
				90, 90, 110, 110, 100,
				//
				150, 150 };
		SEMI_SQUARE = new Polygon(xx, yy, xx.length);
//		System.out.println(PolygonUtilities.areaPolygon2D(SEMI_SQUARE));
	}

	public static void main(String[] args) {
		ShapePolygonRegular pr;
		Polygon p;
		pr = new ShapePolygonRegular(30.0, 200, 200, true, 100, 8);

		System.out.println("Now create the polygon");
		p = pr.toPolygon();
		System.out.println("got it:");
		System.out.println(PolygonUtilities.polygonToString(p));
		printInFrame(pr, p);

	}

	static void printInFrame(final ShapePolygonRegular pr, final Polygon p) {
		JFrame fin;
		JPanel jp;
		Polygon[] polygonPointer;
		polygonPointer = new Polygon[] { p };
		fin = new JFrame("Polygon regular");
		fin.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		jp = new JPanel() {
			private static final long serialVersionUID = -7775420L;

			@Override
			protected void paintComponent(Graphics g) {
				g.drawPolygon(polygonPointer[0]);
//				PolygonUtilities.forEachPoint(p, (x,y)->{});
				g.drawPolygon(SEMI_SQUARE);
			}
		};
		jp.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				pr.setAngleRotation(pr.getAngleRotation() + e.getWheelRotation() * degreesPerClick);
				polygonPointer[0] = pr.toPolygon();
				System.out.println("angle: " + pr.getAngleRotation());
				System.out.println(PolygonUtilities.polygonToString(polygonPointer[0]));
//				System.out.println(PolygonUtilities.areaPolygon2D(polygonPointer[0]));
				jp.repaint();
				fin.repaint();
			}
		});
		fin.add(jp);
		fin.pack();
		jp.setSize(500, 500);
		fin.setSize(jp.getSize());
		fin.setVisible(true);
	}
}