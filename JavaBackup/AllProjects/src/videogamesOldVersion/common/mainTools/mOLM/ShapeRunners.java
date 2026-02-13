package videogamesOldVersion.common.mainTools.mOLM;

import java.awt.Point;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

import common.mainTools.mOLM.abstractClassesMOLM.ShapeRunner;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import tools.Comparators;
import tools.MathUtilities;
import videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM.AbstractShapeRunners;
import videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM.DoSomethingWithNode;

/**
 * Implementation of {@link AbstractShapeRunners}, using the Singleton
 * pattern.<br>
 * Also uses a pattern similar to Decorator to store instances of
 * {@link ShapeRunner}, that are the runners abstracted by the
 * {@link AbstractShapeRunners} interface.
 */
public class ShapeRunners implements AbstractShapeRunners {
	private static final long serialVersionUID = -362996090882828777L;

	/**
	 * Used to compute a new angle (usually in a static, unchangeable way) starting
	 * from the given one
	 * <p>
	 * p1_______________________p2<br>
	 * |........................|<br>
	 * |.........PCenter........|<br>
	 * |........................|<br>
	 * p4_______________________p3<br>
	 * let say "angCenterP1" the angle in degrees between PCenter and firstPoint.
	 * The angle between pcenter and secondPoint will be (180.0 - angCenterP1). p3 =
	 * angCenterP1 + 180 , p4 = 360-angCenterP1.
	 */
	protected static interface AngleRoteater extends Serializable {
		public double rotate(double angleToBeReferenced);
	}

	protected static final AngleRoteater[] ROTATERS_FOR_RECTANGLE_ROTATED_ON_CENTER = new AngleRoteater[] { //
			angleToBeReferenced -> angleToBeReferenced, //
			angleToBeReferenced -> 180.0 - angleToBeReferenced, //
			angleToBeReferenced -> 180.0 + angleToBeReferenced, //
			angleToBeReferenced -> -angleToBeReferenced };

	public static final double[] TRIANGLE_ANGLE_DEG_ADDED__FIND_CORNER = { +90.0, /*-30.0*/+90.0, /*-150.0*/+210 }//
			, /**
				 * See {@link MatrixObjectMapper#runOnTriangular_Equilatero_Border }
				 */
			TRIANGLE_ANGLE_DEG_ADDED__RUN_BORDER = { 300.0, 240.0, 0.0 },
			ARROW_ANGLE_DEG_ADDED__FIND_CORNER = { +90.0, /*-30.0*/+330.0, /*-150.0*/+210 }//
			, /**
				 * See {@link MatrixObjectMapper#runOnTriangular_Equilatero_Border }
				 */
			ARROW_ANGLE_DEG_ADDED__RUN_BORDER = { 300.0, 240.0, 0.0 };

	private static ShapeRunners instanceShapeRunners = null;

	protected ShapeRunners() {
		allRunners = new Map<>(AbstractShapeRunners.COMPARATOR_ShapesImplemented);

		allRunners.add(ShapesImplemented.Rectangle, this::runOnRectangle);
		allRunners.add(ShapesImplemented.Rectangle_Border, this::runOnRectangleBorder);
		allRunners.add(ShapesImplemented.Circumference, this::runOnCircumference);
		allRunners.add(ShapesImplemented.Circle, this::runOnCircle);
		allRunners.add(ShapesImplemented.Triangle, this::runOnEquilateralTriangle);
		allRunners.add(ShapesImplemented.Arrow, this::runOnArrow);
		allRunners.add(ShapesImplemented.ArrowBorderBodySameLength, this::runOnArrow_BorderBodySameLength);
		allRunners.add(ShapesImplemented.Cone, this::runOnCone);
		allRunners.add(ShapesImplemented.Line, this::runOnLine);
		allRunners.add(ShapesImplemented.Point, this::runOnPoint);
		allRunners.add(ShapesImplemented.EllipseNoRotation, this::runOnEllipseNoRotation);
		allRunners.add(ShapesImplemented.EllipseNoRotation_Border, this::runOnEllipseNoRotationBorder);
	}

	protected Map<ShapesImplemented, ShapeRunner> allRunners;

	//

	public static final ShapeRunners getInstance() {
		if (instanceShapeRunners == null)
			instanceShapeRunners = new ShapeRunners();
		return instanceShapeRunners;
	}

	public Map<ShapesImplemented, ShapeRunner> getAllRunners() {
		return allRunners;
	}

	@Override
	public ShapeRunner getRunner(ShapesImplemented si) {
		if (si == null)
			return null;
		return getAllRunners().fetch(si);
	}

	//

	//

	// TODO RECTANGLES

	//

	//

	@Override
	public boolean runOnRectangle(MatrixObjectLocationManager molm, int xx, int yy, int w, int h, double angleDeg,
			DoSomethingWithNode doswn) {
		int ipotenusa, xCenter, yCenter, hw, hh;
		NodeMatrix[][] matrix;
		matrix = molm.matrix;

		if (doswn == null || matrix == null || matrix.length == 0 || w < 1 || h < 1) {
			return false;
		}

		angleDeg = MathUtilities.adjustDegAngle(angleDeg);

		// xCenter = xx + (hw = (w >> 1));
		// yCenter = yy + (hh = (h >> 1));
		xCenter = xx;
		xx -= (hw = (w >> 1));
		yCenter = yy;
		yy -= (hh = (h >> 1));
		ipotenusa = (int) Math.ceil(Math.hypot(hw, hh));
		ipotenusa = Math.max(ipotenusa, Math.max(w, h));

		// System.out.println("ADDING AT x: " + xx + ", yy: " + yy + ", w: " + w
		// + ", h: " + h);

		if ((yCenter - ipotenusa) >= 0 && yCenter + ipotenusa < matrix.length && (xCenter - ipotenusa) >= 0
				&& xCenter + ipotenusa < matrix[0].length) {

			if (angleDeg == 0.0 || angleDeg == 180.0) {
				fillRectangle_Unsafe(molm, xx, yy, w, h, doswn);
				// fillRectangle_Unsafe(molm, xx - w, yy - h, w, h, doswn);
			} else if (angleDeg == 270.0 || angleDeg == 90.0) {
				// fillRectangle_Unsafe(molm, xx, yy - w, h, w, doswn);
				fillRectangle_Unsafe(molm, xx + hw - hh, yy + hh - hw, h, w, doswn);
			} else {
				fillRectangle_Unsafe(molm, xx, yy, w, h, doswn, angleDeg);
			}
		} else {

			if (angleDeg == 0.0 || angleDeg == 180.0) {
				fillRectangle_Safe(molm, xx, yy, w, h, doswn);
				// fillRectangle_Safe(molm, xx - w, yy - h, w, h, doswn);
			} else if (angleDeg == 270.0 || angleDeg == 90.0) {
				// fillRectangle_Safe(molm, xx, yy - w, h, w, doswn);
				fillRectangle_Safe(molm, xx + hw - hh, yy + hh - hw, h, w, doswn);
			} else {
				fillRectangle_Safe(molm, xx, yy, w, h, doswn, angleDeg);
			}
		}
		return true;
	}

	/**
	 * Angle = 0.0°<br>
	 * Unsafe out of bounds
	 */
	protected static void fillRectangle_Unsafe(MatrixObjectLocationManager molm, int xx, int yy, int w, int h,
			DoSomethingWithNode doswn) {
		int r, c, xtraslating;
		NodeMatrix[][] matrix;
		matrix = molm.matrix;
		NodeMatrix row[];

		if (w == 1) {
			if (h == 1) {
				doswn.doOnNode(molm, molm.getNodeMatrix(xx, yy), xx, yy);
			} else {
				runVerticalSpan_Unsafe(xx, yy, h, molm, doswn);
			}

		} else if (h == 1) {
			runHorizontalSpan_Unsafe(xx, yy, w, molm, doswn);
		} else {

			r = -1;
			while(++r < h && doswn.canContinueCycling()) {
				row = matrix[yy];
				c = -1;
				xtraslating = xx;
				while(++c < w && doswn.canContinueCycling()) {
					doswn.doOnNode(molm, row[xtraslating], xtraslating++, yy);
				}
				yy++;
			}
		}
	}

	public static void fillRectangle_Unsafe(MatrixObjectLocationManager molm, int xx, int yy, int w, int h,
			DoSomethingWithNode doswn, double angleDeg) {
		int xUpper, yUpper, xLeft, yLeft, xRight, yRight, xLower, yLower, xStart, xEnd;
		// ac = angular coefficient ..
		double acLeftUp, qLeftUp, acLeftLow, qLeftLow, acLowRight, qLowRight, acUpRight, qUpRight, acUsedOnLeft,
				qUsedOnLeft, acUsedOnRight, qUsedOnRight;
		NodeMatrix[][] matrix;
		xUpper = yUpper = xLeft = yLeft = xRight = yRight = xLower = yLower = xStart = xEnd = 0;
		acLeftUp = acLeftLow = acLowRight = acUpRight = 0;
		matrix = molm.getMatrix();
		angleDeg = MathUtilities.adjustDegAngle(angleDeg);
		// calculate the points
		{
			int hw, hh, xCenter, yCenter, i, len;
			double hypotenuse, angCenterP1, radTemp;
			Point allPoints[], ptemp;
			xCenter = xx + (hw = (w >> 1));
			yCenter = yy + (hh = (h >> 1));
			hypotenuse = Math.hypot(hw, hh);
			allPoints = new Point[len = ROTATERS_FOR_RECTANGLE_ROTATED_ON_CENTER.length];
			// See ROTATERS_FOR_RECTANGLE_ROTATED_ON_CENTER
			angCenterP1 = MathUtilities.angleDeg(xCenter, yCenter, xx, yy);
			// now let's rotate the rectangle using "angleDeg"

			// calculate all points
			i = -1;
			while(++i < len) {
				radTemp = Math.toRadians(MathUtilities
						.adjustDegAngle(ROTATERS_FOR_RECTANGLE_ROTATED_ON_CENTER[i].rotate(angCenterP1) + angleDeg));

				allPoints[i] = new Point(xCenter + (int) (hypotenuse * Math.cos(radTemp)),
						yCenter + (int) (hypotenuse * Math.sin(radTemp)));
			}
			/**
			 * Proprietà :
			 * <ul>
			 * <li>il punto più in alto non può essere anche quello più a destra o a
			 * sinistra</li>
			 * <li>ne deriva che esso si troverà "in mezzo", nell'asse delle x, ad almeno
			 * altri due punti</li>
			 * </ul>
			 * Quindi .. Calcolo i 4 punti , li metto in un array, lo ordino secondo l'asse
			 * y in senso decrescente (se y uguale, compara secondo la x , con x minore ->
			 * 1) .. Il punto numero 0 è il più alto, il 3 il più basso, gli 1 e 2
			 * bisogna confrontare le x ...
			 */
			Arrays.sort(allPoints, Comparators.POINT_COMPARATOR_HIGHEST_LEFTMOST_FIRST);
			// now we have all points .. get the highest
			ptemp = allPoints[0];
			xUpper = ptemp.x;
			yUpper = ptemp.y;
			// low
			ptemp = allPoints[3];
			xLower = ptemp.x;
			yLower = ptemp.y;

			ptemp = allPoints[2];
			if (ptemp.x < allPoints[1].x) {
				xLeft = ptemp.x;
				yLeft = ptemp.y;
				ptemp = allPoints[1];
				xRight = ptemp.x;
				yRight = ptemp.y;
			} else {
				xRight = ptemp.x;
				yRight = ptemp.y;
				ptemp = allPoints[1];
				xLeft = ptemp.x;
				yLeft = ptemp.y;
			}
		}
		/**
		 * Y = m*x + q<br>
		 * Q = y - m*x<br>
		 * X = (y-q)/m
		 */
		// calculate the equation of each border
		acLeftUp = MathUtilities.angularCoefficient(xLeft, yLeft, xUpper, yUpper);
		qLeftUp = yUpper - acLeftUp * xUpper;
		acLeftLow = MathUtilities.angularCoefficient(xLeft, yLeft, xLower, yLower);
		qLeftLow = yLower - acLeftLow * xLower;
		acUpRight = MathUtilities.angularCoefficient(xUpper, yUpper, xRight, yRight);
		qUpRight = yUpper - acUpRight * xUpper;
		acLowRight = MathUtilities.angularCoefficient(xLower, yLower, xRight, yRight);
		qLowRight = yLower - acLowRight * xLower;
		// after all calculus, start the cycle
		acUsedOnLeft = acLeftUp;
		qUsedOnLeft = qLeftUp;
		acUsedOnRight = acUpRight;
		qUsedOnRight = qUpRight;
		yy = yUpper + 1;

		// recycle vars
		while(doswn.canContinueCycling() && --yy >= yLower) {
			if (yy == yUpper) {
				doswn.doOnNode(molm, matrix[yy][xUpper], xUpper, yy);
			} else if (yy == yLower) {
				doswn.doOnNode(molm, matrix[yy][xLower], xLower, yy);
			} else {
				if (yy == yLeft) {
					xStart = xLeft;
					acUsedOnLeft = acLeftLow;
					qUsedOnLeft = qLeftLow;
				} else {
					xStart = (int) Math.round((yy - qUsedOnLeft) / acUsedOnLeft);
				}
				if (yy == yRight) {
					xEnd = xRight;
					acUsedOnRight = acLowRight;
					qUsedOnRight = qLowRight;
				} else {
					xEnd = (int) Math.round((yy - qUsedOnRight) / acUsedOnRight);
				}
				runHorizontalSpan_Unsafe(molm, xStart, xEnd, yy, doswn);
			}
		}
	}

	/**
	 * Angle = 0.0°<br>
	 * Unsafe out of bounds <br>
	 * BUG PRONE because of xx and yy "aggiustamenti"
	 */
	protected static void fillRectangle_Safe(MatrixObjectLocationManager molm, int xx, int yy, int w, int h,
			DoSomethingWithNode doswn) {
		int r, c, xtraslating, xlen, ylen;
		NodeMatrix row[];
		NodeMatrix[][] matrix;
		matrix = molm.matrix;
		xlen = matrix[0].length;
		ylen = matrix.length;

		r = -1;
		if (yy < 0) {
			h += yy;
			yy = 0;
		}
		if (xx < 0) {
			w += xx;
			xx = 0;
		}
		if (yy + h > ylen) {
			h = ylen - yy;
		}
		if (xx + w > xlen) {
			w = xlen - xx;
		}
		while(++r < h && doswn.canContinueCycling()) {
			if (yy < ylen) {
				row = matrix[yy];
				c = -1;
				xtraslating = xx;
				while(++c < w && doswn.canContinueCycling()) {
					// doswn.doOnRow(row, xtraslating++, yy);
					doswn.doOnNode(molm, row[xtraslating], xtraslating++, yy);
				}
				yy++;
			}
		}
	}

	public static void fillRectangle_Safe(MatrixObjectLocationManager molm, int xx, int yy, int w, int h,
			DoSomethingWithNode doswn, double angleDeg) {
		boolean xStartNotFound, xEndNotFound;
		int xUpper, yUpper, xLeft, yLeft, xRight, yRight, xLower, yLower, xStart, xEnd;
		double acLeftUp, qLeftUp, acLeftLow, qLeftLow, acLowRight, qLowRight, acUpRight, qUpRight, //
				acUsedOnLeft, qUsedOnLeft, acUsedOnRight, qUsedOnRight;
		NodeMatrix[][] matrix;

		xUpper = yUpper = xLeft = yLeft = xRight = yRight = xLower = yLower = xStart = xEnd = 0;
		acLeftUp = acLeftLow = acLowRight = acUpRight = 0;
		matrix = molm.getMatrix();
		angleDeg = MathUtilities.adjustDegAngle(angleDeg);
		{
			int hw, hh, xCenter, yCenter, i, len;
			double hypotenuse, angCenterP1, radTemp;
			Point allPoints[], ptemp;
			xCenter = xx + (hw = (w >> 1));
			yCenter = yy + (hh = (h >> 1));
			hypotenuse = Math.hypot(hw, hh);
			allPoints = new Point[len = ROTATERS_FOR_RECTANGLE_ROTATED_ON_CENTER.length];
			angCenterP1 = MathUtilities.angleDeg(xCenter, yCenter, xx, yy);
			i = -1;
			while(++i < len) {
				radTemp = Math.toRadians(MathUtilities
						.adjustDegAngle(ROTATERS_FOR_RECTANGLE_ROTATED_ON_CENTER[i].rotate(angCenterP1) + angleDeg));

				allPoints[i] = new Point(xCenter + (int) (hypotenuse * Math.cos(radTemp)),
						yCenter + (int) (hypotenuse * Math.sin(radTemp)));
			}
			Arrays.sort(allPoints, Comparators.POINT_COMPARATOR_HIGHEST_LEFTMOST_FIRST);
			ptemp = allPoints[0];
			xUpper = ptemp.x;
			yUpper = ptemp.y;
			ptemp = allPoints[3];
			xLower = ptemp.x;
			yLower = ptemp.y;
			ptemp = allPoints[2];
			if (ptemp.x < allPoints[1].x) {
				xLeft = ptemp.x;
				yLeft = ptemp.y;
				ptemp = allPoints[1];
				xRight = ptemp.x;
				yRight = ptemp.y;
			} else {
				xRight = ptemp.x;
				yRight = ptemp.y;
				ptemp = allPoints[1];
				xLeft = ptemp.x;
				yLeft = ptemp.y;
			}
		}

		acLeftUp = MathUtilities.angularCoefficient(xLeft, yLeft, xUpper, yUpper);
		qLeftUp = yUpper - acLeftUp * xUpper;
		acLeftLow = MathUtilities.angularCoefficient(xLeft, yLeft, xLower, yLower);
		qLeftLow = yLower - acLeftLow * xLower;
		acUpRight = MathUtilities.angularCoefficient(xUpper, yUpper, xRight, yRight);
		qUpRight = yUpper - acUpRight * xUpper;
		acLowRight = MathUtilities.angularCoefficient(xLower, yLower, xRight, yRight);
		qLowRight = yLower - acLowRight * xLower;

		acUsedOnLeft = acLeftUp;
		qUsedOnLeft = qLeftUp;
		acUsedOnRight = acUpRight;
		qUsedOnRight = qUpRight;
		yy = yUpper + 1;

		if (yy > molm.getHeight())
			yy = molm.getHeight();
		// if (yLower < 0) yLower = 0;
		while(doswn.canContinueCycling() && --yy >= yLower && yy >= 0) {

			if (yy == yUpper) {
				if (xUpper >= 0 && xUpper < molm.getWidth())
					doswn.doOnNode(molm, matrix[yy][xUpper], xUpper, yy);
			} else if (yy == yLower) {
				if (xLower >= 0 && xLower < molm.getWidth())
					doswn.doOnNode(molm, matrix[yy][xLower], xLower, yy);
			} else {
				xStartNotFound = xEndNotFound = true;
				if (yy == yLeft) {
					xStart = xLeft;
					xStartNotFound = false;
					acUsedOnLeft = acLeftLow;
					qUsedOnLeft = qLeftLow;
				}
				if (yy == yRight) {
					xEnd = xRight;
					xEndNotFound = false;
					acUsedOnRight = acLowRight;
					qUsedOnRight = qLowRight;
				}
				if (xStartNotFound) {
					xStart = (int) Math.round((yy - qUsedOnLeft) / acUsedOnLeft);
				}
				if (xEndNotFound) {
					xEnd = (int) Math.round((yy - qUsedOnRight) / acUsedOnRight);
				}
				if (xStart < 0)
					xStart = 0;
				if (xStart < molm.getWidth() && xEnd >= 0) {
					if (xEnd >= molm.getWidth())
						xEnd = molm.getWidth() - 1;
					runHorizontalSpan_Unsafe(molm, xStart, xEnd, yy, doswn);
				} // else error !
			}

		}
	}

	//

	//

	// TODO RECTANGLE BORDER

	//

	//

	@Override
	public boolean runOnRectangleBorder(MatrixObjectLocationManager molm, int xx, int yy, int w, int h, double angleDeg,
			DoSomethingWithNode doswn) {
		int ipotenusa, xCenter, yCenter, hw, hh;
		NodeMatrix[][] matrix;

		if (doswn == null || molm == null || (matrix = molm.matrix).length == 0 || h < 1 || w < 1) {
			return false;
		}
		if (Math.abs(angleDeg) >= 360.0)
			angleDeg %= 360.0;
		if (angleDeg < 0.0)
			angleDeg += 360.0;
		xCenter = xx;
		xx -= (hw = (w >> 1));
		yCenter = yy;
		yy -= (hh = (h >> 1));
		ipotenusa = (int) Math.ceil(Math.hypot(hw, hh));

		if ((yCenter - ipotenusa) >= 0 && yCenter + ipotenusa < matrix.length && (xCenter - ipotenusa) >= 0
				&& xCenter + ipotenusa < matrix[0].length) {

			if (angleDeg == 0.0 || angleDeg == 180.0) {
				drawRectangleBorder_Unsafe(molm, xx, yy, w, h, doswn);
				return true;
			} else if (angleDeg == 270.0 || angleDeg == 90.0) {
				drawRectangleBorder_Unsafe(molm, xCenter - hh, yCenter - hw, h, w, doswn);
				return true;
			} else
				drawRectangleBorder_Unsafe(molm, xCenter, yCenter, w, h, doswn, angleDeg);
		} else {
			if (angleDeg == 0.0 || angleDeg == 180.0) {
				drawRectangleBorder_Safe(molm, xx, yy, w, h, doswn);
				return true;
			} else if (angleDeg == 270.0 || angleDeg == 90.0) {
				drawRectangleBorder_Safe(molm, xCenter - hh, yCenter - hw, h, w, doswn);
				return true;
			} else {
				drawRectangleBorder_Safe(molm, xCenter, yCenter, w, h, doswn, angleDeg);
			}

		}
		// drawRectangleBorder_Safe(molm, xCenter, yCenter, w, h, doswn,
		// angleDeg);
		return true;
	}

	/** P(x,y) = BOTTOM-LEFT CORNER */
	protected static void drawRectangleBorder_Unsafe(MatrixObjectLocationManager molm, int xx, int yy, int w, int h,
			DoSomethingWithNode doswn) {
		int r, c;
		NodeMatrix[] row;
		NodeMatrix[][] matrix;

		matrix = molm.matrix;
		runHorizontalSpan_Unsafe(xx, yy, w, molm, doswn);
		runHorizontalSpan_Unsafe(xx, yy + (h - 1), w, molm, doswn);
		c = xx + (w - 1);
		h -= 2;
		r = -1;
		while(++r < h && doswn.canContinueCycling()) {
			row = matrix[++yy];
			// doswn.doOnRow(row, xx, yy);
			// doswn.doOnRow(row, c, yy);
			doswn.doOnNode(molm, row[xx], xx, yy);
			doswn.doOnNode(molm, row[c], c, yy);
		}
	}

	/**
	 * BUG: Did not draw the second "lowest" line (the second one having y nearest
	 * to zero).
	 */
	// usa l'idea sfruttata per riempire il rettangolo
	protected static void drawRectangleBorder_Unsafe_NOT_DRAW_SECOND_LINE(MatrixObjectLocationManager molm, int xx,
			int yy, int w, int h, DoSomethingWithNode doswn, double angleDeg) {
		int xUpper, yUpper, xLeft, yLeft, xRight, yRight, xLower, yLower, xStart, xEnd, xStartPrev, xEndPrev;
		double acLeftUp, qLeftUp, acLeftLow, qLeftLow, acLowRight, qLowRight, acUpRight, qUpRight, //
				// used on cycle
				acUsedOnLeft, qUsedOnLeft, acUsedOnRight, qUsedOnRight;
		NodeMatrix[][] matrix;
		// NodeMatrix[] row;

		xUpper = yUpper = xLeft = yLeft = xRight = yRight = xLower = yLower = xStart = xEnd = 0;
		acLeftUp = acLeftLow = acLowRight = acUpRight = 0;
		matrix = molm.getMatrix();
		angleDeg = MathUtilities.adjustDegAngle(angleDeg);
		{
			int hw, hh, xCenter, yCenter, i, len;
			double hypotenuse, angCenterP1, radTemp;
			Point allPoints[], ptemp;
			xCenter = xx + (hw = (w >> 1));
			yCenter = yy + (hh = (h >> 1));
			hypotenuse = Math.hypot(hw, hh);
			allPoints = new Point[len = ROTATERS_FOR_RECTANGLE_ROTATED_ON_CENTER.length];
			angCenterP1 = MathUtilities.angleDeg(xCenter, yCenter, xx, yy);
			i = -1;
			while(++i < len) {
				radTemp = Math.toRadians(MathUtilities
						.adjustDegAngle(ROTATERS_FOR_RECTANGLE_ROTATED_ON_CENTER[i].rotate(angCenterP1) + angleDeg));
				allPoints[i] = new Point(xCenter + (int) (hypotenuse * Math.cos(radTemp)),
						yCenter + (int) (hypotenuse * Math.sin(radTemp)));
			}
			Arrays.sort(allPoints, Comparators.POINT_COMPARATOR_HIGHEST_LEFTMOST_FIRST);
			ptemp = allPoints[0];
			xUpper = ptemp.x;
			yUpper = ptemp.y;
			ptemp = allPoints[3];
			xLower = ptemp.x;
			yLower = ptemp.y;
			ptemp = allPoints[2];
			if (ptemp.x < allPoints[1].x) {
				xLeft = ptemp.x;
				yLeft = ptemp.y;
				ptemp = allPoints[1];
				xRight = ptemp.x;
				yRight = ptemp.y;
			} else {
				xRight = ptemp.x;
				yRight = ptemp.y;
				ptemp = allPoints[1];
				xLeft = ptemp.x;
				yLeft = ptemp.y;
			}
		}
		acLeftUp = MathUtilities.angularCoefficient(xLeft, yLeft, xUpper, yUpper);
		qLeftUp = yUpper - acLeftUp * xUpper;
		acLeftLow = MathUtilities.angularCoefficient(xLeft, yLeft, xLower, yLower);
		qLeftLow = yLower - acLeftLow * xLower;
		acUpRight = MathUtilities.angularCoefficient(xUpper, yUpper, xRight, yRight);
		qUpRight = yUpper - acUpRight * xUpper;
		acLowRight = MathUtilities.angularCoefficient(xLower, yLower, xRight, yRight);
		qLowRight = yLower - acLowRight * xLower;

		acUsedOnLeft = acLeftUp;
		qUsedOnLeft = qLeftUp;
		acUsedOnRight = acUpRight;
		qUsedOnRight = qUpRight;
		yy = yUpper + 1;
		xStartPrev = xEndPrev = xUpper;
		while(doswn.canContinueCycling() && --yy >= yLower) {
			if (yy == yUpper) {
				doswn.doOnNode(molm, matrix[yy][xUpper], xUpper, yy);
			} else if (yy == yLower) {
				doswn.doOnNode(molm, matrix[yy][xLower], xLower, yy);
			} else {
				if (yy == yLeft) {
					xStart = xLeft;
					acUsedOnLeft = acLeftLow;
					qUsedOnLeft = qLeftLow;
				} else {
					xStart = (int) Math.round((yy - qUsedOnLeft) / acUsedOnLeft);
				}
				if (yy == yRight) {
					xEnd = xRight;
					acUsedOnRight = acLowRight;
					qUsedOnRight = qLowRight;
				} else {
					xEnd = (int) Math.round((yy - qUsedOnRight) / acUsedOnRight);
				}
				runHorizontalSpan_Unsafe(Math.min(xStartPrev, xStart), yy, Math.max(1, Math.abs(xStartPrev - xStart)),
						molm, doswn);
				// row = matrix[yy];
				// doswn.doOnNode(molm, row[xStart], xStart, yy);
				// doswn.doOnNode(molm, row[xEnd], xEnd, yy);
				runHorizontalSpan_Unsafe(Math.min(xEndPrev, xEnd), yy, Math.max(1, Math.abs(xEndPrev - xEnd)), molm,
						doswn);
				xStartPrev = xStart;
				xEndPrev = xEnd;
			}
		}
	}

	protected static void drawRectangleBorder_Safe(MatrixObjectLocationManager molm, int xx, int yy, int w, int h,
			DoSomethingWithNode argb, double angleDeg) {
		double rad, sin, cos, radAggiunto, sinAgg, cosAgg;

		angleDeg = MathUtilities.adjustDegAngle(angleDeg);
		/*
		 * this method draws the rectangle's borders using the point P(xx,yy) (the
		 * left-bottom point) as rotation center's point BUT i want to use as rotation
		 * point the rectangle's CENTER, so (xx,yy) must be translated
		 */
		// recycle vars
		radAggiunto = Math.hypot(cos = w / 2.0, sin = h / 2.0);
		rad = Math.toRadians(MathUtilities.adjustDegAngle(angleDeg + MathUtilities.angleDeg(cos, sin, 0, 0)));
		sin = Math.sin(rad);
		cos = Math.cos(rad);
		xx += radAggiunto * cos;
		yy += radAggiunto * sin;

		rad = Math.toRadians(angleDeg);
		sin = Math.sin(rad);
		cos = Math.cos(rad);
		runRotatedSpan_Safe(molm, xx, yy, w, argb, sin, cos);

		radAggiunto = Math.toRadians(MathUtilities.adjustDegAngle(angleDeg - 90));
		sinAgg = Math.sin(radAggiunto);
		cosAgg = Math.cos(radAggiunto);
		runRotatedSpan_Safe(molm, xx, yy, h, argb, sinAgg, cosAgg);

		runRotatedSpan_Safe(molm, xx + (int) Math.round(h * cosAgg), yy + (int) Math.round(h * sinAgg), w, argb, sin,
				cos);
		runRotatedSpan_Safe(molm, xx + (int) Math.round(w * cos), yy + (int) Math.round(w * sin), h, argb, sinAgg,
				cosAgg);
	}

	protected static void drawRectangleBorder_Unsafe(MatrixObjectLocationManager molm, int xx, int yy, int w, int h,
			DoSomethingWithNode argb, double angleDeg) {
		double rad, sin, cos, radAggiunto, sinAgg, cosAgg;

		angleDeg = MathUtilities.adjustDegAngle(angleDeg);
		/*
		 * this method draws the rectangle's borders using the point P(xx,yy) (the
		 * left-bottom point) as rotation center's point BUT i want to use as rotation
		 * point the rectangle's CENTER, so (xx,yy) must be translated
		 */
		// recycle vars
		radAggiunto = Math.hypot(cos = w / 2.0, sin = h / 2.0);
		rad = Math.toRadians(MathUtilities.adjustDegAngle(angleDeg + MathUtilities.angleDeg(cos, sin, 0, 0)));
		sin = Math.sin(rad);
		cos = Math.cos(rad);
		xx += radAggiunto * cos;
		yy += radAggiunto * sin;

		rad = Math.toRadians(angleDeg);
		sin = Math.sin(rad);
		cos = Math.cos(rad);
		runRotatedSpan_Unsafe(molm, xx, yy, w, argb, sin, cos);

		radAggiunto = Math.toRadians(MathUtilities.adjustDegAngle(angleDeg - 90));
		sinAgg = Math.sin(radAggiunto);
		cosAgg = Math.cos(radAggiunto);
		runRotatedSpan_Unsafe(molm, xx, yy, h, argb, sinAgg, cosAgg);

		runRotatedSpan_Unsafe(molm, xx + (int) Math.round(h * cosAgg), yy + (int) Math.round(h * sinAgg), w, argb, sin,
				cos);
		runRotatedSpan_Unsafe(molm, xx + (int) Math.round(w * cos), yy + (int) Math.round(w * sin), h, argb, sinAgg,
				cosAgg);
	}

	protected static void drawRectangleBorder_Safe(MatrixObjectLocationManager molm, int xx, int yy, int w, int h,
			DoSomethingWithNode doswn) {
		boolean xLeftInside, xRightInside;
		int r, c, increasingy;
		NodeMatrix row[];
		NodeMatrix[][] matrix;
		matrix = molm.matrix;
		if (h >= matrix.length) {
			h = matrix.length - 1;
		}

		// sfrutto increasingy come variabile temporanea
		if (w < 1 || h < 1 || (increasingy = (yy + h)) < 0 || yy >= matrix.length) {
			return;
		}

		if (yy >= 0) {
			runHorizontalSpan_Safe(xx, yy, w, molm, doswn);
		} else {
			h += yy;
			yy = 0;
			increasingy = --h;
		}

		if (increasingy < matrix.length) {
			runHorizontalSpan_Safe(xx, increasingy - 1, w, molm, doswn);
		} else {
			h = matrix.length - (yy + 1);
		}

		c = xx + (w - 1);
		h -= 2;
		r = -1;
		increasingy = yy;
		xLeftInside = xx >= 0 && xx < molm.getWidth();
		xRightInside = c >= 0 && c < molm.getWidth();
		if (xLeftInside || xRightInside) {

			if (xLeftInside == xRightInside) {
				// both true
				while(++r < h && doswn.canContinueCycling()) {
					row = matrix[++increasingy];
					doswn.doOnNode(molm, row[xx], xx, increasingy);
					doswn.doOnNode(molm, row[c], c, increasingy);
				}
			} else if (xLeftInside)
				while(++r < h && doswn.canContinueCycling()) {
					doswn.doOnNode(molm, matrix[++increasingy][xx], xx, increasingy);
				}
			else
				while(++r < h && doswn.canContinueCycling()) {
					doswn.doOnNode(molm, matrix[++increasingy][c], c, increasingy);
				}
		}
	}

	//

	//

	// TODO CIRCUMFERENCE

	//

	//

	@Override
	public boolean runOnCircumference(MatrixObjectLocationManager molm, int x, int y, int ray,
			DoSomethingWithNode doswn) {
		return runOnCircumference(molm, x, y, ray, doswn, false);
	}

	public boolean runOnCircumference(MatrixObjectLocationManager molm, int x, int y, int ray,
			DoSomethingWithNode doswn, boolean nullRowCheck) {
		boolean ok;
		int lastPoint, diameter;
		int r, rr, l;
		NodeMatrix[] riga;
		NodeMatrix[][] matrix;

		if (molm == null) {
			return false;
		}
		matrix = molm.matrix;
		diameter = (ray << 1) + 1;
		x -= ray;
		y -= ray;
		ok = ray > 0 && (x + diameter) > 0 && (y + diameter) > 0 && y < matrix.length;
		if (ok) {
			riga = matrix[y]; // controllo alla veloce
			ok = ((lastPoint = (x + diameter)) < riga.length) && ((y + diameter) < matrix.length);
			if (ok && nullRowCheck) {
				r = 1;
				rr = y + 1;
				l = riga.length;
				while(ok && r++ < diameter) {
					if (ok = (riga = matrix[rr++]) != null) {
						ok = lastPoint < (l = (riga.length < l ? riga.length : l));
					}
				}
			} /*
				 * else { System.err.println("second fail"); }
				 */
			if (ok) {
				runOnCircumference_Unsafe(molm, x, y, ray, doswn);
			} else {
				runOnCircumference_Safe(molm, x, y, ray, doswn);
			}
			ok = true;
		} /*
			 * else { System.err .println("first fail : x:" + x + ", y:" + y + ", ray:" +
			 * ray + ", matrix.length:" + matrix.length); }
			 */
		return ok;
	}

	/**
	 * Draw the border of a circle.<br>
	 * The painted circle will have <code>(ray*2 +1)</code> diameter, so beware.
	 * <br>
	 * The point P(x,y) is the left-bottom vertex of the bounding square, so beware
	 * and adjust. <br>
	 * The combination of coordinates and ray is considered SAFE inside the code, as
	 * like as the matrix isn't null and is rectangular .. so the method could throw
	 * <code>IndexOutOfBoundsException</code> and <code>NullPointerException</code>.
	 * <p>
	 * BEWARE : there is any kind of check!<br>
	 * If checks are neededs, call
	 * {@link GeometryDrawer#drawCircle_BetterAndSafe(int, int, int, int[][], int)}
	 * instead.
	 * <p>
	 * Name of the original algorithm in this class :
	 * {@link drawCircle_WithoutCkecks};
	 *
	 * @throw <code>IndexOutOfBoundsException</code> and
	 *        <code>NullPointerException</code> because this method do no kind of
	 *        check.
	 * @param isom the matrix where to paint the circle
	 * @param x      the x-coordinate of the left-bottom bunding box's vertex
	 * @param y      the y-coordinate of the left-bottom bunding box's vertex
	 * @param ray    the ray of the circle .. the diameter will be
	 *               <code>(ray*2 +1)</code>
	 * @param argb   the value (also a color, or whatever you want) to place into
	 *               the circle's border to draw it
	 */
	protected boolean runOnCircumference_Unsafe(MatrixObjectLocationManager molm, int x, int y, int ray,
			DoSomethingWithNode doswn) {
		boolean ret;
		NodeMatrix[] rigaSup = null, rigaInf = null, rigaCenterSup = null, rigaCenterInf = null;
		NodeMatrix[][] matrix;
		matrix = molm.matrix;

		if (ret = ray > 0) {
			if (ray == 1) {
				doswn.doOnNode(molm, molm.getNodeMatrix(x + 1, y), x + 1, y);
				rigaSup = matrix[++y];
				doswn.doOnNode(molm, rigaSup[x], x, y);
				doswn.doOnNode(molm, rigaSup[x + 2], x + 2, y);
				// doswn.doOnItem(matrix, x + 1, y + 2);
				doswn.doOnNode(molm, molm.getNodeMatrix(++x, ++y), x, y);
			} else {
				double rRay = ray + 0.5;
				int r = 0, c = 0, ray2 = ray << 1, ray_1 = ray - 1, halfRay = (ray >> 1) + (ray & 1), rInf,
						ray1 = ray + 1, horizontalSymmetricOldC, tx, ty, ray_r;
				// disegno i punti cardinali

				rigaSup = matrix[ty = ray + y];
				// doswn.doOnRow(rigaSup, x);
				doswn.doOnNode(molm, rigaSup[x], x, ty);
				// doswn.doOnRow(rigaSup, x + ray2);
				doswn.doOnNode(molm, rigaSup[tx = x + ray2], tx, ty);
				// doswn.doOnItem(matrix, ray + x, y);
				doswn.doOnNode(molm, matrix[y][tx = x + ray], tx, y);
				// doswn.doOnItem(matrix, ray + x, ray2 + y);
				doswn.doOnNode(molm, matrix[ty = ray2 + y][tx = x + ray], tx, ty);

				horizontalSymmetricOldC = ray1;
				rInf = ray2;
				c = ray_1;
				for (r = 0; r < halfRay && doswn.canContinueCycling(); r++, rInf--) {

					rigaSup = matrix[r + y];
					rigaInf = matrix[rInf + y];
					ray_r = ray - r;
					while(0 < c && doswn.canContinueCycling() && (Math.hypot(ray - c, ray_r) < rRay)) {

						// doswn.doOnRow(rigaSup, c + x);
						doswn.doOnNode(molm, rigaSup[tx = x + c], tx, ty = y + r);
						// doswn.doOnRow(rigaSup, horizontalSymmetricOldC + x);
						doswn.doOnNode(molm, rigaSup[tx = x + horizontalSymmetricOldC], tx, ty);
						// doswn.doOnRow(rigaInf, c + x);
						doswn.doOnNode(molm, rigaInf[tx = x + c], tx, ty = y + rInf);
						// doswn.doOnRow(rigaInf, horizontalSymmetricOldC + x);
						doswn.doOnNode(molm, rigaInf[tx = x + horizontalSymmetricOldC], tx, ty);

						// ottengo i puntatori alle righe per ottimizzare;
						rigaCenterSup = matrix[c + y];
						rigaCenterInf = matrix[horizontalSymmetricOldC + y];
						// coloro
						// doswn.doOnRow(rigaCenterSup, r + x);
						doswn.doOnNode(molm, rigaCenterSup[tx = x + r], tx, ty = y + c);
						// doswn.doOnRow(rigaCenterSup, rInf + x);
						doswn.doOnNode(molm, rigaCenterSup[tx = x + rInf], tx, ty);
						// doswn.doOnRow(rigaCenterInf, r + x);
						doswn.doOnNode(molm, rigaCenterInf[tx = x + r], tx, ty = y + horizontalSymmetricOldC);
						// doswn.doOnRow(rigaCenterInf, rInf + x);
						doswn.doOnNode(molm, rigaCenterInf[tx = x + rInf], tx, ty);

						horizontalSymmetricOldC++;
						c--;
					}
				} // fine ciclo r
			}
		}
		return ret;
	}

	/**
	 * Ray 1 is not OutOfBoundsSafe.<br>
	 * See {@link #drawCircle_WithoutCkecks(int[][], int, int, int, int)} for
	 * further informations.
	 * <p>
	 * Name of the original algorithm in this class :
	 * {@link drawCircle_WithoutCkecks};
	 */
	protected boolean runOnCircumference_Safe(MatrixObjectLocationManager molm, int x, int y, int ray,
			DoSomethingWithNode doswn) {
		boolean canUsErigaSup, canUsErigaInf, canUsErigaCenterSup, canUsErigaCenterInf, ret;
		int r, c, ray2, ray_1, halfRay, rInf, ray1, horizontalSymmetricOldC, c_x, hc_x, r_y, rInf_y, c_y, hc_y, r_x,
				rInf_x, ray_r;
		double rRay;
		NodeMatrix[] rigaSup, rigaInf, rigaCenterSup, rigaCenterInf, rrrr;
		NodeMatrix[][] matrix;
		matrix = molm.matrix;

		if (ret = ray > 0) {
			rigaSup = null;
			rigaInf = null;
			rigaCenterSup = null;
			rigaCenterInf = null;
			if (ray == 1) {
				int ty, tx;
				rigaSup = matrix[ty = y + 1];
				// doswn.doOnRow(rigaSup, x);
				doswn.doOnNode(molm, rigaSup[tx = x], tx, ty);
				// doswn.doOnRow(rigaSup, x + 2);
				doswn.doOnNode(molm, rigaSup[tx = ++x + 1], tx, ty);
				// doswn.doOnItem(matrix, x + 1, y);
				doswn.doOnNode(molm, matrix[y][x], x, y);
				// doswn.doOnItem(matrix, x + 1, y + 2);
				doswn.doOnNode(molm, matrix[++ty][x], x, ty);

			} else {
				r = c = 0;
				ray2 = ray << 1;
				ray_1 = ray - 1;
				halfRay = (ray >> 1) + ray % 2;
				ray1 = ray + 1;
				rRay = ray + 0.5;

				{// disegno i punti cardinali
					int ray_y = ray + y, ray2_x, ray_x, ray2_y;
					if (ray_y >= 0 && ray_y < matrix.length) {
						rigaSup = matrix[ray_y];
						if (x >= 0 && x < rigaSup.length) {
							// doswn.doOnRow(rigaSup, x);
							doswn.doOnNode(molm, rigaSup[x], x, ray_y);
						}
						ray2_x = ray2 + x;
						if (ray2_x >= 0 && ray2_x < rigaSup.length) {
							// doswn.doOnRow(rigaSup, ray2);
							doswn.doOnNode(molm, rigaSup[ray2], ray2, ray_y);
						}
					}
					ray_x = ray + x;
					if (ray_x >= 0) {
						if (y >= 0 && y < matrix.length) {
							rrrr = matrix[y];
							if (ray_x >= 0 && ray_x < rrrr.length) {
								// doswn.doOnRow(rrrr, ray_x);
								doswn.doOnNode(molm, rrrr[ray_x], ray_x, y);
							}
						}
						ray2_y = ray2 + y;
						if (ray2_y >= 0 && ray2_y < matrix.length) {
							rrrr = matrix[ray2_y];
							if (ray_x >= 0 && ray_x < rrrr.length) {
								// doswn.doOnRow(rrrr, ray_x);
								doswn.doOnNode(molm, rrrr[ray_x], ray_x, ray2_y);
							}
						}
					}
				} // fine blocco punti cardinali

				horizontalSymmetricOldC = ray1;
				rInf = ray2;
				c = ray_1;
				for (r = 0; r < halfRay && doswn.canContinueCycling(); r++, rInf--) {

					r_y = r + y;
					canUsErigaSup = r_y >= 0 && r_y < matrix.length;
					if (canUsErigaSup) {
						rigaSup = matrix[r_y];
					}

					rInf_y = rInf + y;
					canUsErigaInf = rInf_y >= 0 && rInf_y < matrix.length;
					if (canUsErigaInf) {
						rigaInf = matrix[rInf_y];
					}
					ray_r = ray - r;
					while(0 < c && doswn.canContinueCycling() && (Math.hypot(ray - c, ray_r) < rRay)) {

						c_x = c + x;
						hc_x = horizontalSymmetricOldC + x;
						if (canUsErigaSup) {
							if (c_x >= 0 && c_x < rigaSup.length) {
								// doswn.doOnRow(rigaSup, c_x);
								doswn.doOnNode(molm, rigaSup[c_x], c_x, r_y);
							}
							if (hc_x >= 0 && hc_x < rigaSup.length) {
								// doswn.doOnRow(rigaSup, hc_x);
								doswn.doOnNode(molm, rigaSup[hc_x], hc_x, r_y);
							}
						}
						if (canUsErigaInf) {
							if (c_x >= 0 && c_x < rigaInf.length) {
								// doswn.doOnRow(rigaInf, c_x);
								doswn.doOnNode(molm, rigaInf[c_x], c_x, rInf_y);
							}
							if (hc_x >= 0 && hc_x < rigaInf.length) {
								// doswn.doOnRow(rigaInf, hc_x);
								doswn.doOnNode(molm, rigaInf[hc_x], hc_x, rInf_y);
							}
						}

						c_y = c + y;
						canUsErigaCenterSup = c_y >= 0 && c_y < matrix.length;
						if (canUsErigaCenterSup) {
							rigaCenterSup = matrix[c_y];
						}
						hc_y = horizontalSymmetricOldC + y;
						canUsErigaCenterInf = hc_y >= 0 && hc_y < matrix.length;
						if (canUsErigaCenterInf) {
							rigaCenterInf = matrix[hc_y];
						}

						// coloro
						r_x = r + x;
						rInf_x = rInf + x;
						if (canUsErigaCenterSup) {
							if (r_x >= 0 && r_x < rigaCenterSup.length) {
								// doswn.doOnRow(rigaCenterSup, r_x);
								doswn.doOnNode(molm, rigaCenterSup[r_x], r_x, c_y);
							}
							if (rInf_x >= 0 && rInf_x < rigaCenterSup.length) {
								// doswn.doOnRow(rigaCenterSup, rInf_x);
								doswn.doOnNode(molm, rigaCenterSup[rInf_x], rInf_x, c_y);
							}
						}
						if (canUsErigaCenterInf) {
							if (r_x >= 0 && r_x < rigaCenterInf.length) {
								// doswn.doOnRow(rigaCenterInf, r_x);
								doswn.doOnNode(molm, rigaCenterInf[r_x], r_x, hc_y);
							}
							if (rInf_x >= 0 && rInf_x < rigaCenterInf.length) {
								// doswn.doOnRow(rigaCenterInf, rInf_x);
								doswn.doOnNode(molm, rigaCenterInf[rInf_x], rInf_x, hc_y);
							}
						}

						horizontalSymmetricOldC++;
						c--;
					}

				} // fine ciclo r
			}
		}
		return ret;
	}

	//

	//

	// TODO FILL_CIRCLE

	//

	//

	@Override
	public boolean runOnCircle(MatrixObjectLocationManager molm, int x, int y, int ray, DoSomethingWithNode doswn) {
		boolean ok;
		int diameter;
		// int r, rr, l;
		// NodeMatrix[] riga;
		// NodeMatrix[][] matrix;

		if (molm == null) {
			return false;
		}
		// matrix = molm.matrix;
		diameter = (ray << 1) + 1;
		/*
		 * aggiustiamo le coordinate in quanto viente passato il "centro", ma i metodi
		 * operano sull'angolo in alto a destra
		 */
		x -= ray;
		y -= ray;
		ok = ray > 0 && (x + diameter) >= 0 && (y + diameter) >= 0 && y < molm.getHeight();
		if (ok) {
			// ok = x >= 0 && y >= 0 && ((x + diameter) < molm.getWidth()) &&
			// ((y + diameter) < molm.getHeight());
			/*
			 * if (ok) {
			 *
			 * r = 1; rr = y + 1; l = riga.length;
			 *
			 * while (ok && r++ < diameter) { if (ok = (riga = matrix[rr++]) != null) { ok =
			 * lastPoint < (l = (riga.length < l ? riga.length : l)); } } }
			 */
			if (x >= 0 && y >= 0 && ((x + diameter) < molm.getWidth()) && ((y + diameter) < molm.getHeight())) {
				// fillCircle_Safe_WorksPerfect(molm, x, y, ray, doswn);
				fillCircle_Unsafe_Tuned_OverRay8(molm, x, y, ray, doswn);
			} else {
				fillCircle_Safe_Tuned_OverRay8(molm, x, y, ray, doswn);
			}
		}
		return ok;
	}

	/**
	 * EUREKA FUNZIONA correttamente.<br>
	 * Presenta operazioni ridondanti solo su alcuni pixel, come documentato da
	 * {@link #fillCircle_Unsafe_Tuned_OverRay8(int[][], int, int, int, int)}.
	 * <p>
	 * N.B.: NO CHECK IS PERFORMED ABOUT {@link NullPointerException} AND
	 * {@link IndexOutOfBoundsException}.
	 *
	 * @param isom the integer matrix to write the disc
	 * @param x      the x-component of the bottom-left corner of the bounding box
	 *               of the disc
	 * @param y      the y-component of the bottom-left corner of the bounding box
	 *               of the disc
	 * @param ray    the ray of the circle. BEWARE: the diameter will be
	 *               <code>(2*ray)+1</code>.
	 * @param argb   the value used to write the disc
	 */
	protected static void fillCircle_Safe_WorksPerfect(MatrixObjectLocationManager molm, int x, int y, int ray,
			DoSomethingWithNode doswn) {
		int r, c, ray2, rInf, ray1, ray_1, horizontalSymmetricC, oldc, oldHorizC, halfRay, l,
				yPlusDiameter/* , logray */, height, ytemp, xtemp, ty;
		double rRay;
		NodeMatrix[] rigaSup, rigaInf, rigaCenterSup, rigaCenterInf;
		NodeMatrix[][] matrix;
		matrix = molm.matrix;

		height = matrix.length;
		if (ray > 0) {
			if (ray == 1) {
				int tx;

				// il punto in alto
				if (y >= 0 && y < matrix.length) {
					if (allInside(molm, x + 1, y, height)) {
						// if (allInside(molm, x + 1, y, height)) {
						// doswn.doOnItem(matrix, x + 1, y);
						doswn.doOnNode(molm, matrix[y][x + 1], x + 1, y);
					}
				}
				// la riga in centro
				if (MathUtilities.isAtMostPositive(ty = y + 1, height)) {
					rigaSup = matrix[ty];
					if (allInside(molm, x, ty, height)) {
						// doswn.doOnRow(rigaSup, x);
						doswn.doOnNode(molm, rigaSup[x], x, ty);
					}
					if (allInside(molm, tx = x + 1, ty, height)) {
						// doswn.doOnRow(rigaSup, x + 1);
						doswn.doOnNode(molm, rigaSup[tx], tx, ty);
					}
					if (allInside(molm, tx = x + 2, ty, height)) {
						// doswn.doOnRow(rigaSup, x + 2);
						doswn.doOnNode(molm, rigaSup[tx], tx, ty);
					}
				}
				// punto in basso
				if (MathUtilities.isAtMostPositive(ty = y + 2, height)) {
					if (allInside(molm, tx = x + 1, ty, height)) {
						// doswn.doOnItem(matrix, x + 1, y + 2);
						doswn.doOnNode(molm, matrix[ty][tx], tx, ty);
					}
				}
			} else {
				r = 0;
				c = 0;
				ray2 = ray << 1;
				ray_1 = ray - 1;
				halfRay = (ray >> 1) + ray % 2;
				ray1 = ray + 1;
				rRay = ray + 0.5;

				// disegno i punti cardinali
				if (MathUtilities.isAtMostPositive(ytemp = y + ray, height)) {
					rigaSup = matrix[ytemp];
					if (allInside(molm, x, ytemp, height)) {
						// doswn.doOnRow(rigaSup, x);
						doswn.doOnNode(molm, rigaSup[x], x, ytemp);
					}
					if (allInside(molm, xtemp = ray2 + x, ytemp, height)) {
						// doswn.doOnRow(rigaSup, xtemp);
						doswn.doOnNode(molm, rigaSup[xtemp], xtemp, ytemp);
					}
				}
				xtemp = ray + x;
				if (allInside(molm, xtemp, y, height)) {
					// doswn.doOnItem(matrix, xtemp, y);
					doswn.doOnNode(molm, matrix[y][xtemp], xtemp, y);
				}
				if (allInside(molm, xtemp, ytemp = y + ray2, height)) {
					// doswn.doOnItem(matrix, xtemp, ytemp);
					doswn.doOnNode(molm, matrix[ytemp][xtemp], xtemp, y);
				}
				/*
				 * aggiusto le righe in quanto inizio non dalla riga centrale, ma dalla prima e
				 * dal centro di essa rigaSup = m[0]; rigaInf = m[ray2];
				 */
				horizontalSymmetricC = ray1;
				rInf = ray2;
				c = ray_1;
				yPlusDiameter = y + ray2;

				// logray = MetodiVari.bitMinNeeded(ray);
				for (r = 0; r < halfRay && doswn.canContinueCycling(); r++, rInf--) {

					// aggiorno le righe su cui devo lavorare

					rigaSup = MathUtilities.isAtMostPositive(ytemp = y + r, height) ? matrix[ytemp] : null;
					rigaInf = MathUtilities.isAtMostPositive(ty = y + rInf, height) ? matrix[ty] : null;
					/*
					 * ora sposto il puntatore "c" dal centro verso l'origine, fino a quando non
					 * supero il cerchio ideale, al che rientro .. uso ray1 per questioni di
					 * approssimazione grafica.. dopodichè traccio la congiungente
					 */

					oldc = c;
					oldHorizC = horizontalSymmetricC;

					while(c > 0 && doswn.canContinueCycling() && (Math.hypot(ray - c, (ray - r)) < rRay)) {
						/*
						 * Sapendo che il cerchio l'ho diviso in otto spicchi, coloro prima la fetta che
						 * dall'(abituale) angolo 90 va fino al 135, poi tutti i simmetrici .. il primo
						 * è il più a sinistra di tutti, seguito da quello in range (90-45), a quello
						 * (270-225), poi (270-315)
						 */
						if (rigaSup != null) {
							if (MathUtilities.isAtMostPositive(xtemp = x + c, rigaSup.length)) {
								// doswn.doOnRow(rigaSup, xtemp);
								doswn.doOnNode(molm, rigaSup[xtemp], xtemp, ytemp);
							}
							if (MathUtilities.isAtMostPositive(xtemp = x + horizontalSymmetricC, rigaSup.length)) {
								// doswn.doOnRow(rigaSup, xtemp);
								doswn.doOnNode(molm, rigaSup[xtemp], xtemp, ytemp);
							}
						}
						if (rigaInf != null) {
							if (MathUtilities.isAtMostPositive(xtemp = x + c, rigaInf.length)) {
								// doswn.doOnRow(rigaInf, xtemp);
								doswn.doOnNode(molm, rigaInf[xtemp], xtemp, ty);
							}
							if (MathUtilities.isAtMostPositive(xtemp = x + horizontalSymmetricC, rigaInf.length)) {
								// doswn.doOnRow(rigaInf, xtemp);
								doswn.doOnNode(molm, rigaInf[xtemp], xtemp, ty);
							}
						}
						/*
						 * poi i VERTICALI D: ... prima (180-135) , poi (0-45), poi (180-225), poi
						 * (0-315)
						 */
						// ottengo i puntatori alle righe per ottimizzare;

						rigaCenterSup = MathUtilities.isAtMostPositive(ytemp = y + c, height) ? matrix[ytemp] : null;
						rigaCenterInf = MathUtilities.isAtMostPositive(ty = y + horizontalSymmetricC, height)
								? matrix[ty]
								: null;
						// coloro
						if (rigaCenterSup != null) {
							if (MathUtilities.isAtMostPositive(xtemp = x + r, rigaCenterSup.length)) {
								// doswn.doOnRow(rigaCenterSup, xtemp);
								doswn.doOnNode(molm, rigaCenterSup[xtemp], xtemp, ytemp);
							}
							if (MathUtilities.isAtMostPositive(xtemp = x + rInf, rigaCenterSup.length)) {
								// doswn.doOnRow(rigaCenterSup, xtemp);
								doswn.doOnNode(molm, rigaCenterSup[xtemp], xtemp, ytemp);
							}
						}
						if (rigaCenterInf != null) {
							if (MathUtilities.isAtMostPositive(xtemp = x + r, rigaCenterInf.length)) {
								// doswn.doOnRow(rigaCenterInf, xtemp);
								doswn.doOnNode(molm, rigaCenterInf[xtemp], xtemp, ty);
							}
							if (MathUtilities.isAtMostPositive(xtemp = x + rInf, rigaCenterInf.length)) {
								// doswn.doOnRow(rigaCenterInf, xtemp);
								doswn.doOnNode(molm, rigaCenterInf[xtemp], xtemp, ty);
							}
						}

						// disegno le righe vicine al centro

						// if (r <= logray) {
						l = (ray2 - (r << 1)) - 1;
						runHorizontalSpan_Safe(x + r + 1, y + c, l, molm, doswn);
						runHorizontalSpan_Safe(x + r + 1, yPlusDiameter - c, l, molm, doswn);
						// }
						horizontalSymmetricC++;
						c--;
					}

					l = (oldHorizC - oldc) - 1;
					runHorizontalSpan_Safe(x + oldc + 1, r + y, l, molm, doswn);
					runHorizontalSpan_Safe(x + oldc + 1, yPlusDiameter - r, l, molm, doswn);
				} // fine ciclo r
					// last line, the horizontal diameter
				runHorizontalSpan_Safe(x + 1, y + ray, ray2 - 1, molm, doswn);
			}
		}
	}

	protected static void fillCircle_Unsafe_WorksPerfect(MatrixObjectLocationManager molm, int x, int y, int ray,
			DoSomethingWithNode doswn) {
		int r, c, ray2, rInf, ray1, ray_1, horizontalSymmetricC, oldc, oldHorizC, halfRay, l,
				yPlusDiameter/* , logray */, height, ytemp, xtemp, ty;
		double rRay;
		NodeMatrix[] rigaSup, rigaInf, rigaCenterSup, rigaCenterInf;
		NodeMatrix[][] matrix;
		matrix = molm.matrix;

		height = matrix.length;
		if (ray > 0) {
			if (ray == 1) {
				int tx;

				// il punto in alto
				if (y >= 0 && y < matrix.length) {
					if (allInside(molm, x + 1, y, height)) {
						// if (allInside(molm, x + 1, y, height)) {
						// doswn.doOnItem(matrix, x + 1, y);
						doswn.doOnNode(molm, matrix[y][x + 1], x + 1, y);
					}
				}
				// la riga in centro
				if (MathUtilities.isAtMostPositive(ty = y + 1, height)) {
					rigaSup = matrix[ty];
					if (allInside(molm, x, ty, height)) {
						// doswn.doOnRow(rigaSup, x);
						doswn.doOnNode(molm, rigaSup[x], x, ty);
					}
					if (allInside(molm, tx = x + 1, ty, height)) {
						// doswn.doOnRow(rigaSup, x + 1);
						doswn.doOnNode(molm, rigaSup[tx], tx, ty);
					}
					if (allInside(molm, tx = x + 2, ty, height)) {
						// doswn.doOnRow(rigaSup, x + 2);
						doswn.doOnNode(molm, rigaSup[tx], tx, ty);
					}
				}
				// punto in basso
				if (MathUtilities.isAtMostPositive(ty = y + 2, height)) {
					if (allInside(molm, tx = x + 1, ty, height)) {
						// doswn.doOnItem(matrix, x + 1, y + 2);
						doswn.doOnNode(molm, matrix[ty][tx], tx, ty);
					}
				}
			} else {
				r = 0;
				c = 0;
				ray2 = ray << 1;
				ray_1 = ray - 1;
				halfRay = (ray >> 1) + ray % 2;
				ray1 = ray + 1;
				rRay = ray + 0.5;

				// disegno i punti cardinali
				rigaSup = matrix[ytemp = y + ray];
				// doswn.doOnRow(rigaSup, x);
				doswn.doOnNode(molm, rigaSup[x], x, ytemp);
				// doswn.doOnRow(rigaSup, xtemp);
				doswn.doOnNode(molm, rigaSup[xtemp = ray2 + x], xtemp, ytemp);
				xtemp = ray + x;
				// doswn.doOnItem(matrix, xtemp, y);
				doswn.doOnNode(molm, matrix[y][xtemp], xtemp, y);
				// doswn.doOnItem(matrix, xtemp, ytemp);
				doswn.doOnNode(molm, matrix[ytemp = y + ray2][xtemp], xtemp, y);
				/*
				 * aggiusto le righe in quanto inizio non dalla riga centrale, ma dalla prima e
				 * dal centro di essa rigaSup = m[0]; rigaInf = m[ray2];
				 */
				horizontalSymmetricC = ray1;
				rInf = ray2;
				c = ray_1;
				yPlusDiameter = y + ray2;

				// logray = MetodiVari.bitMinNeeded(ray);
				for (r = 0; r < halfRay && doswn.canContinueCycling(); r++, rInf--) {

					// rigaSup = MathUtilities.isAtMostPositive(ytemp = y + r,
					// height) ? matrix[ytemp] : null;
					// rigaInf = MathUtilities.isAtMostPositive(ty = y + rInf,
					// height) ? matrix[ty] : null;
					rigaSup = matrix[ytemp = y + r];
					rigaInf = matrix[ty = y + rInf];

					oldc = c;
					oldHorizC = horizontalSymmetricC;

					while(c > 0 && doswn.canContinueCycling() && (Math.hypot(ray - c, (ray - r)) < rRay)) {
						/*
						 * Sapendo che il cerchio l'ho diviso in otto spicchi, coloro prima la fetta che
						 * dall'(abituale) angolo 90 va fino al 135, poi tutti i simmetrici .. il primo
						 * è il più a sinistra di tutti, seguito da quello in range (90-45), a quello
						 * (270-225), poi (270-315)
						 */
						// doswn.doOnRow(rigaSup, xtemp);
						doswn.doOnNode(molm, rigaSup[xtemp = x + c], xtemp, ytemp);
						// doswn.doOnRow(rigaSup, xtemp);
						doswn.doOnNode(molm, rigaSup[xtemp = x + horizontalSymmetricC], xtemp, ytemp);
						// doswn.doOnRow(rigaInf, xtemp);
						doswn.doOnNode(molm, rigaInf[xtemp = x + c], xtemp, ty);
						// doswn.doOnRow(rigaInf, xtemp);
						doswn.doOnNode(molm, rigaInf[xtemp = x + horizontalSymmetricC], xtemp, ty);
						/*
						 * rigaCenterSup = MathUtilities.isAtMostPositive(ytemp = y + c, height) ?
						 * matrix[ytemp] : null; rigaCenterInf = MathUtilities.isAtMostPositive(ty = y +
						 * horizontalSymmetricC, height) ? matrix[ty] : null;
						 */

						rigaCenterSup = matrix[ytemp = y + c];
						rigaCenterInf = matrix[ty = y + horizontalSymmetricC];
						// doswn.doOnRow(rigaCenterSup, xtemp);
						doswn.doOnNode(molm, rigaCenterSup[xtemp = x + r], xtemp, ytemp);
						// doswn.doOnRow(rigaCenterSup, xtemp);
						doswn.doOnNode(molm, rigaCenterSup[xtemp = x + rInf], xtemp, ytemp);
						// doswn.doOnRow(rigaCenterInf, xtemp);
						doswn.doOnNode(molm, rigaCenterInf[xtemp], xtemp, ty);
						// doswn.doOnRow(rigaCenterInf, xtemp);
						doswn.doOnNode(molm, rigaCenterInf[xtemp = x + r], xtemp, ty);
						// disegno le righe vicine al centro

						l = (ray2 - (r << 1)) - 1;
						runHorizontalSpan_Unsafe(x + r + 1, y + c, l, molm, doswn);
						runHorizontalSpan_Unsafe(x + r + 1, yPlusDiameter - c, l, molm, doswn);
						horizontalSymmetricC++;
						c--;
					}

					l = (oldHorizC - oldc) - 1;
					runHorizontalSpan_Unsafe(x + oldc + 1, r + y, l, molm, doswn);
					runHorizontalSpan_Unsafe(x + oldc + 1, yPlusDiameter - r, l, molm, doswn);
				} // fine ciclo r
					// last line, the horizontal diameter
				runHorizontalSpan_Unsafe(x + 1, y + ray, ray2 - 1, molm, doswn);
			}
		}

	}

	/**
	 * ULTIMO FATTO (28/06/2016), BELLISSIMO, MA SBAGLIA i primi 8 raggi.<br>
	 * Ottimizzato rispetto a
	 * {@link #fillCircle_Unsafe_WorksPerfect(int[][], int, int, int, int)} secondo
	 * il seguente test : dopo 500'000 scritture su una matrice, con raggio 75,
	 * questo metodo impiega circa 33 secondi, l'altro 32. Quindi, mediamente, è
	 * migliore di un secondo ogni 500'000 operazioni.<br>
	 * Lo scarto di tempo può essere significativo solo con raggi più grandi.
	 */
	protected static void fillCircle_Safe_Tuned_OverRay8(MatrixObjectLocationManager molm, int x, int y, int ray,
			DoSomethingWithNode doswn) {
		int r, c, halfray, diameter, horizontalSymmetricC, oldc, oldHorizC, yPlusDiameter, l, height, ytemp, xtemp, ty;
		double rRay;
		NodeMatrix[] rigaSup, rigaInf;
		NodeMatrix[][] matrix;
		matrix = molm.matrix;
		if (ray > 0) {
			if (ray <= 8) {
				fillCircle_Safe_WorksPerfect(molm, x, y, ray, doswn);
			} else {
				/**
				 * Idea : ciclo dalla cima fino a metà raggio, disegnando righe orizzontali. La
				 * sezione da metà raggio fino al "diametro orizzontale", è speculare alla
				 * prima, quindi prendo le coordinate similari e traccio le linee orizzontali
				 * rimanenti. Infatti, prima disegno la cima e il fondo del cerchio,
				 * espandendomi e avvicinandomi al centro, poi per simmetria disegno dai punti
				 * di estrema sinistra e destra avvicinandomi ai poli. <br>
				 * Infine, traccio il diametro orizzontale.<br>
				 * Per ogni riga, tengo traccia del punto (della "x") da cui sono partito e mi
				 * muovo in moto retrogrado fintanto che rimango "entro il cerchio". Nel mentre,
				 * disegno. Appena esco, traccio la linea orizzontale a partire da quel
				 * punto-traccia, poi lo aggiorno.
				 */
				halfray = (ray >> 1) - 1/* + (ray & 0x1) */;
				r = -1;
				c = oldc = ray - 1;
				horizontalSymmetricC = oldHorizC = ray + 1;
				diameter = (ray << 1) + 1;
				rRay = ray + 0.5;
				yPlusDiameter = (y + diameter) - 1;
				height = matrix.length;

				while(++r < halfray && doswn.canContinueCycling()) {

					rigaSup = MathUtilities.isAtMostPositive(ytemp = y + r, height) ? matrix[ytemp] : null;
					rigaInf = MathUtilities.isAtMostPositive(ty = yPlusDiameter - r, height) ? matrix[ty] : null;

					while(0 <= c && doswn.canContinueCycling() && (Math.hypot(ray - c, (ray - r)) < rRay)) {

						if (rigaSup != null) {
							if (MathUtilities.isAtMostPositive(xtemp = x + c, rigaSup.length)) {
								// doswn.doOnRow(rigaSup, xtemp);
								doswn.doOnNode(molm, rigaSup[xtemp], xtemp, ytemp);
							}
							if (MathUtilities.isAtMostPositive(xtemp = x + horizontalSymmetricC, rigaSup.length)) {
								// doswn.doOnRow(rigaSup, xtemp);
								doswn.doOnNode(molm, rigaSup[xtemp], xtemp, ytemp);
							}
						}
						if (rigaInf != null) {
							if (MathUtilities.isAtMostPositive(xtemp = x + c, rigaInf.length)) {
								// doswn.doOnRow(rigaInf, xtemp);
								doswn.doOnNode(molm, rigaInf[xtemp], xtemp, ty);
							}
							if (MathUtilities.isAtMostPositive(xtemp = x + horizontalSymmetricC, rigaInf.length)) {
								// doswn.doOnRow(rigaInf, xtemp);
								doswn.doOnNode(molm, rigaInf[xtemp], xtemp, ty);
							}
						}

						/*
						 * ora disegno il corrispettivo simmetrico a partire dalle estremità sinistra e
						 * destra
						 */
						// temp = m[yOfCenter - c];
						// temp[x + r] = temp[xPlusDiameter - r] );

						l = diameter - (r << 1);
						runHorizontalSpan_Safe(x + r, y + c, l, molm, doswn);
						// drawHorizontalSpan_Safe(x + r, y +
						// (horizontalSymmetricC -
						// 1), l, molm, doswn);
						runHorizontalSpan_Safe(x + r, yPlusDiameter - c, l, molm, doswn);

						c--;
						horizontalSymmetricC++;
					}

					/*
					 * drawHorizontalSpan_Safe(x + oldc, y + r, ((ray - c) << 1) + 1, molm, doswn);
					 */
					l = (oldHorizC - oldc) - 1;
					runHorizontalSpan_Safe(x + oldc + 1, y + r, l, molm, doswn);
					runHorizontalSpan_Safe(x + oldc + 1, yPlusDiameter - r, l, molm, doswn);

					oldc = c;
					oldHorizC = horizontalSymmetricC;

				}

				// diametro orizzontale
				runHorizontalSpan_Safe(x, y + ray, diameter, molm, doswn);
			}
		}
	}

	protected static void fillCircle_Unsafe_Tuned_OverRay8(MatrixObjectLocationManager molm, int x, int y, int ray,
			DoSomethingWithNode doswn) {
		int r, c, halfray, diameter, horizontalSymmetricC, oldc, oldHorizC, yPlusDiameter, l, ytemp, xtemp, ty;
		double rRay;
		NodeMatrix[] rigaSup, rigaInf;
		NodeMatrix[][] matrix;
		matrix = molm.matrix;
		if (ray > 0) {
			if (ray <= 8) {
				fillCircle_Unsafe_WorksPerfect(molm, x, y, ray, doswn);
			} else {
				halfray = (ray >> 1) - 1/* + (ray & 0x1) */;
				r = -1;
				c = oldc = ray - 1;
				horizontalSymmetricC = oldHorizC = ray + 1;
				diameter = (ray << 1) + 1;
				rRay = ray + 0.5;
				yPlusDiameter = (y + diameter) - 1;

				while(++r < halfray && doswn.canContinueCycling()) {

					// rigaSup = MathUtilities.isAtMostPositive(ytemp = y + r,
					// height) ? matrix[ytemp] : null;
					// rigaInf = MathUtilities.isAtMostPositive(ty =
					// yPlusDiameter - r, height) ? matrix[ty] : null;
					rigaSup = matrix[ytemp = y + r];
					rigaInf = matrix[ty = yPlusDiameter - r];

					while(0 <= c && doswn.canContinueCycling() && (Math.hypot(ray - c, (ray - r)) < rRay)) {
						// doswn.doOnRow(rigaSup, xtemp);
						doswn.doOnNode(molm, rigaSup[xtemp = x + c], xtemp, ytemp);
						doswn.doOnNode(molm, rigaSup[xtemp = x + horizontalSymmetricC], xtemp, ytemp);
						// doswn.doOnRow(rigaInf, xtemp);
						doswn.doOnNode(molm, rigaInf[xtemp = x + c], xtemp, ty);
						// doswn.doOnRow(rigaInf, xtemp);
						doswn.doOnNode(molm, rigaInf[xtemp = x + horizontalSymmetricC], xtemp, ty);

						l = diameter - (r << 1);
						runHorizontalSpan_Unsafe(x + r, y + c, l, molm, doswn);
						runHorizontalSpan_Unsafe(x + r, yPlusDiameter - c, l, molm, doswn);

						c--;
						horizontalSymmetricC++;
					}

					l = (oldHorizC - oldc) - 1;
					runHorizontalSpan_Unsafe(x + oldc + 1, y + r, l, molm, doswn);
					runHorizontalSpan_Unsafe(x + oldc + 1, yPlusDiameter - r, l, molm, doswn);

					oldc = c;
					oldHorizC = horizontalSymmetricC;
				}
				// diametro orizzontale
				runHorizontalSpan_Unsafe(x, y + ray, diameter, molm, doswn);
			}
		}
	}

	//

	//

	// TODO TRIANGLES

	//

	// IT: triangolo equilatero

	@Override
	public boolean runOnTriangle(MatrixObjectLocationManager molm, int x, int y, int borderLength, double angleDeg,
			DoSomethingWithNode doswn) {
		int heightTotal, xLeft, yLeft, xTop, yTop, xBottom, yBottom;
		if (borderLength < 1 || molm == null || doswn == null) {
			return false;
		}

		angleDeg = MathUtilities.adjustDegAngle(angleDeg) % 120.0;
		heightTotal = (int) Math.round(MathUtilities.TRIANGLE_HEIGHT_COEFFICIENT * borderLength);
		if (angleDeg == 0.0) {
			/**
			 * (x,y)./\<br>
			 * ..../....\<br>
			 * ../........\<br>
			 * /____________\
			 */
		} else if (angleDeg == 60.0) {
			/**
			 * (x,y).._________.<br>
			 * .......\......../<br>
			 * .........\..../<br>
			 * ...........\/
			 */
		} else {
			/*
			 * acLeftUp = MathUtilities.angularCoefficient(xLeft, yLeft, xUpper, yUpper);
			 * qLeftUp = yUpper - acLeftUp * xUpper; acLeftLow =
			 * MathUtilities.angularCoefficient(xLeft, yLeft, xLower, yLower); qLeftLow =
			 * yLower - acLeftLow * xLower; acUpRight =
			 * MathUtilities.angularCoefficient(xUpper, yUpper, xRight, yRight); qUpRight =
			 * yUpper - acUpRight * xUpper; acLowRight =
			 * MathUtilities.angularCoefficient(xLower, yLower, xRight, yRight);
			 */
		}
		return true;
	}

	public boolean runOnTriangle_OLD(MatrixObjectLocationManager molm, int x, int y, int borderLength, double angleDeg,
			DoSomethingWithNode doswn) {
		boolean isSafe;
		int xx, yy, i, shift;
		double radAngleTemp, distanceCenterCorner;

		if (borderLength < 1) {
			return false;
		}

		isSafe = x >= 0 && y >= 0 && (x + borderLength) < molm.getWidth() && (y + borderLength) < molm.getHeight();

		angleDeg = MathUtilities.adjustDegAngle(angleDeg);
		/**
		 * NOTES:
		 * <ul>
		 * <li>l'angolo tra 1 e 2 è (angleDeg - 60), ergo (angleDeg+300)</li>
		 * <li>l'angolo tra 1 e 3 è (angleDeg - 120), ergo (angleDeg+240)</li>
		 * <li>l'angolo tra 3 e 2 è semplicemente "angleDeg"</li>
		 * </ul>
		 */
		/**
		 * angleCorner1 = angleDeg + 90.0;//<br>
		 * angleCorner2 = angleDeg - 30.0;//<br>
		 * angleCorner3 = angleDeg - 150.0;//<br>
		 * MathUtilities.adjustDegAngle(angleCorner1);//<br>
		 * MathUtilities.adjustDegAngle(angleCorner2);//<br>
		 * MathUtilities.adjustDegAngle(angleCorner3);
		 */

		// shift=(borderLength>>1)+(borderLength&0x1);
		shift = (borderLength + 1) >> 1;
		x += shift;
		y += shift;

		distanceCenterCorner = MathUtilities.TRIANGLE_EQUILATERAL_RADIUS_CIRCUMSCRIBED_COEFFICIENT * borderLength;

		i = -1;
		while(++i <= 2) {
			// calcolo le coordinate del corner
			radAngleTemp = Math
					.toRadians(MathUtilities.adjustDegAngle(angleDeg + TRIANGLE_ANGLE_DEG_ADDED__FIND_CORNER[i]));
			xx = x + (int) (distanceCenterCorner * Math.cos(radAngleTemp));
			yy = y + (int) (distanceCenterCorner * Math.sin(radAngleTemp));

			// ora si scorre la linea
			radAngleTemp = Math
					.toRadians(MathUtilities.adjustDegAngle(angleDeg + TRIANGLE_ANGLE_DEG_ADDED__RUN_BORDER[i]));

			// using unsafe just to test
			if (isSafe)
				runRotatedSpan_Unsafe(molm, xx, yy, borderLength, doswn, Math.sin(radAngleTemp),
						Math.cos(radAngleTemp));
			else
				runRotatedSpan_Unsafe(molm, xx, yy, borderLength, doswn, Math.sin(radAngleTemp),
						Math.cos(radAngleTemp));
		}
		return true; // just for now
	}

	/** DID NOT WORK */
	/*
	 * @Deprecated protected boolean
	 * runOnTriangular_Equilatero_Filling(MatrixObjectLocationManager molm,
	 * ShapeSpecification gss, DoSomethingWithNode doswn) { SS_Triangle t; t =
	 * (SS_Triangle) gss; return runOnTriangular_Equilatero_Filling(molm,
	 * gss.getX(), gss.getY(), t.borderLenght, t.angleDeg, doswn); }
	 */

	/**
	 * NOT COMPLETED -> NOT WORKING <br>
	 * <br>
	 * _______________1 <br>
	 * ______________/_\ <br>
	 * ____________/_____\ <br>
	 * ___________/_______\ <br>
	 * _________/___________\ <br>
	 * ________/_____________\ <br>
	 * _______3______________2
	 *
	 * <p>
	 * NOTE:>ul>
	 * <li>Keeping in mind the figure above and to the classic axis system (that is
	 * the one where the origin O(0,0) is located at the left-bottom corner)
	 * differently from the system used to repaint the screen, we have:</li>
	 * <li>l'angolo tra 1 e 2 è (angleDeg - 60), ergo (angleDeg+300)</li>
	 * <li>l'angolo tra 1 e 3 è (angleDeg - 120), ergo (angleDeg+240)</li>
	 * <li>l'angolo tra 3 e 2 è semplicemente "angleDeg"</li>
	 * <li></li>
	 * </ul>
	 */
	@Deprecated
	protected boolean runOnTriangular_Equilatero_Filling(MatrixObjectLocationManager molm, int x, int y,
			int borderLength, double angleDeg, DoSomethingWithNode doswn) {
		// boolean isMediumAtLeft;
		int numberOfRowsToPaint, xleft, xright, i, j, tx, ty;
		double radAngleTemp, distanceCenterCorner, angleAboutMedium, angleAboutLow, cosBorderLeft, cosBorderRight;
		int[] xCorners, yCorners, previousPositionTracker;
		NodeMatrix[][] matrix;
		matrix = molm.matrix;

		if (borderLength < 1) {
			return false;
		}

		MathUtilities.adjustDegAngle(angleDeg);

		distanceCenterCorner = MathUtilities.TRIANGLE_EQUILATERAL_RADIUS_CIRCUMSCRIBED_COEFFICIENT * borderLength;

		// calcolo gli angoli
		xCorners = new int[3];
		yCorners = new int[3];
		previousPositionTracker = new int[3];

		i = -1;
		while(++i <= 2) {
			// calcolo le coordinate del corner
			// TRIANGLE_ANGLE_DEG_ADDED__FIND_CORNER
			radAngleTemp = Math
					.toRadians(MathUtilities.adjustDegAngle(angleDeg + ARROW_ANGLE_DEG_ADDED__FIND_CORNER[i]));
			xCorners[i] = x + (int) (distanceCenterCorner * Math.cos(radAngleTemp));
			yCorners[i] = y + (int) (distanceCenterCorner * Math.sin(radAngleTemp));
		}

		/**
		 * Idea : trovo il vertice piu' "in alto", poi quello piu' a "sinistra" e in
		 * fine quello piu' a "destra".<br>
		 * Dopodiche', verifico quale tra il sinistro e il destro si trova "a meta'",
		 * ossia e' il piu' alto (e quindi vicino, a proposito della coordinata y,
		 * all'angolo in alto).<br>
		 * Fatto cio', si calcolano gli angoli che collegano il vertice agli altri due
		 * punti (immaginado di tracciare i segmenti congiungenti), se ne calcolano seno
		 * e coseno e si inizia un primo ciclo : partendo dalla punta in alto, si scende
		 * verso il "secondo punto in ordine di altezza", trovando per ogni y utile
		 * (ossia, per ogni y compresa tra i due punti) si calcolano le "x" grazie ai
		 * coseni appena calcolati e si trovano i punti che stanno sulle congiungenti
		 * dopra citate. Ottenute tali x, si traccia una linea orizzontale.<br>
		 * Questo ciclo viene iterato fino al raggiungimento di uno dei due punti
		 * diversi da quello "pi' in alto".<br>
		 * Al suo termine, si inizia un secondo, pero' si ricalcola solo l'angolo tra il
		 * punto "appena raggiungo" e quello rimanente
		 */
		/*
		 * per convenzione, si ordinano i punti in senso decrescente sulla base delle y
		 * .. la posizione relativa, focalizzata sull'asse x, tra il punto in alto gli
		 * altri due e' indifferente
		 */
		/**
		 * Dopo l'ordinamento, nell'array previousPositionTracker, in posizione 0 c'e'
		 * l'indice in cui si trovava il vertice piu' in alto negli array Corners prima
		 * del loro ordinamento.<br>
		 * Analogamente per 1 e 2.<br>
		 * Ricordandosi tali indici, e' possibile recurepare i valori di
		 * "TRIANGLE_ANGLE_DEG_ADDED__RUN_BORDER" senza dover ricalcolare gli angoli con
		 * il metodo {@link MatrixObjectMapper#get angle deg 2 points }
		 * <p>
		 * Cio' puo' essere troppo incasinato, quindi per ora ricalcoliamo tutto
		 */

		// disegno almeno i vertici
		// doswn.doOnItem(matrix, x, y);
		doswn.doOnNode(molm, matrix[y][x], x, y);
		i = -1;
		while(++i <= 2) {
			// doswn.doOnItem(matrix, xCorners[i], yCorners[i]);
			tx = xCorners[i];
			ty = yCorners[i];
			doswn.doOnNode(molm, matrix[ty][tx], tx, ty);
		}

		// breakpoint on debug-mode
		// if (Math.random() < 2) return true;

		if (sortPoint_ForFillingTriangle(xCorners, yCorners, previousPositionTracker)) {

			// System.out.println("DOPO");
			// System.out.println(Arrays.toString(xCorners));
			// System.out.println(Arrays.toString(yCorners));
			/**
			 * the situation:<br>
			 *
			 * .........top<br>
			 * ........./.\<br>
			 * ......../...\<br>
			 * ......./.....\<br>
			 * ....medium....\<br>
			 * .......\_.....\<br>
			 * ..........\_...\<br>
			 * .............\_.\<br>
			 * ..............low<br>
			 */

			// x e y possono essere riciclati
			x = xCorners[0];
			y = yCorners[0];
			numberOfRowsToPaint = y - yCorners[1];

			angleAboutMedium =
					/*
					 * Math.toRadians(MathUtilities.adjustDegAngle(angleDeg +
					 * TRIANGLE_ANGLE_DEG_ADDED__RUN_BORDER[ previousPositionTracker[0]]));
					 */
					MathUtilities.angleDeg(xCorners[0], y, xCorners[1], yCorners[1]);

			angleAboutLow =
					/*
					 * Math.toRadians(MathUtilities.adjustDegAngle(angleDeg +
					 * TRIANGLE_ANGLE_DEG_ADDED__RUN_BORDER[ previousPositionTracker[0]]));
					 */
					MathUtilities.angleDeg(xCorners[0], y, xCorners[2], yCorners[2]);

			cosBorderLeft = Math.cos(Math.toRadians(angleAboutMedium));
			cosBorderRight = Math.cos(Math.toRadians(angleAboutLow));

			i = j = 0;

			while(++i < numberOfRowsToPaint) {
				xleft = (int) (i * cosBorderLeft);
				xright = (int) (++j * cosBorderRight);
				runHorizontalSpan_Unsafe(x + xleft, y + i, xright - xleft, molm, doswn);
			}

			/*
			 * angleAboutMedium verra' ricalcolato per connettere il punto medio con il
			 * punto basso
			 */
			angleAboutMedium = MathUtilities.angleDeg(xCorners[1], yCorners[1], xCorners[2], yCorners[2]);
			cosBorderLeft = Math.cos(Math.toRadians(angleAboutMedium));
			// sposto la y
			y += i - 1;
			i = -1;
			numberOfRowsToPaint = yCorners[1] - yCorners[2];
			while(++i < numberOfRowsToPaint) {
				xleft = xCorners[1] + (int) (i * cosBorderLeft);
				xright = x + (int) (++j * cosBorderRight);
				runHorizontalSpan_Unsafe(xleft, y + i, xright - xleft, molm, doswn);
			}

		} else {
			// situazione opposta
		}

		return true; // just for now
	}

	//

	//

	// TODO ARROW

	//

	//

	@Override
	public boolean runOnArrow_BorderBodySameLength(MatrixObjectLocationManager molm, int x, int y, int borderLength,
			double angleDeg, DoSomethingWithNode doswn) {
		boolean isSafe;
		int xx, yy, i, shift;
		double radAngleTemp, distanceCenterCorner;

		if (borderLength < 1) {
			return false;
		}

		isSafe = x >= 0 && y >= 0 && (x + borderLength) < molm.getWidth() && (y + borderLength) < molm.getHeight();
		MathUtilities.adjustDegAngle(angleDeg);
		// shift=(borderLength>>1)+(borderLength&0x1);
		shift = (borderLength + 1) >> 1;
		x += shift;
		y += shift;
		distanceCenterCorner = MathUtilities.TRIANGLE_EQUILATERAL_RADIUS_CIRCUMSCRIBED_COEFFICIENT * borderLength;

		i = -1;
		while(++i <= 2) {
			// calcolo le coordinate del corner
			radAngleTemp = Math
					.toRadians(MathUtilities.adjustDegAngle(angleDeg + ARROW_ANGLE_DEG_ADDED__FIND_CORNER[i]));
			xx = x + (int) (distanceCenterCorner * Math.cos(radAngleTemp));
			yy = y + (int) (distanceCenterCorner * Math.sin(radAngleTemp));

			// ora si scorre la linea
			radAngleTemp = Math
					.toRadians(MathUtilities.adjustDegAngle(angleDeg + ARROW_ANGLE_DEG_ADDED__RUN_BORDER[i]));

			// using unsafe just to test
			// runRotatedSpan_Unsafe(molm, xx, yy, borderLength, doswn,
			// Math.sin(radAngleTemp), Math.cos(radAngleTemp));
			if (isSafe)
				runRotatedSpan_Unsafe(molm, xx, yy, borderLength, doswn, Math.sin(radAngleTemp),
						Math.cos(radAngleTemp));
			else
				runRotatedSpan_Unsafe(molm, xx, yy, borderLength, doswn, Math.sin(radAngleTemp),
						Math.cos(radAngleTemp));
		}

		return true; // just for now
	}

	@Override
	public boolean runOnArrow(MatrixObjectLocationManager molm, int x, int y, int borderLength, double angleDegBorder,
			int bodyLength, double angleDeg, DoSomethingWithNode doswn) {
		int xEnd, yEnd, /* xHead, yHead, */ halfLength, restLength;
		double tempAngle, sin, cos, rad_angleHighestBorder;

		if (bodyLength < 1 || borderLength < 1)
			return false;
		angleDeg = MathUtilities.adjustDegAngle(angleDeg);
		angleDegBorder = MathUtilities.adjustDegAngle(angleDegBorder);

		// draw the body
		halfLength = (bodyLength + 1) >> 1;
		restLength = bodyLength - halfLength;
		/*
		 * commento l'incremento qua sotto perche' P(x,y) e' gia' il centro
		 */
		// x += halfLength; // riciclo x
		// y += halfLength; // riciclo y
		if (angleDeg != 0.0 && angleDeg != 180.0) {
			xEnd = x + (int) Math.round(
					halfLength * Math.cos(tempAngle = Math.toRadians(MathUtilities.adjustDegAngle(angleDeg + 180))));
			yEnd = y + (int) Math.round(halfLength * Math.sin(tempAngle));
			tempAngle = Math.toRadians(angleDeg);
			x += (int) Math.round(restLength * (cos = Math.cos(tempAngle)));
			y += (int) Math.round(restLength * (sin = Math.sin(tempAngle)));
			runSpanFrom(molm, xEnd, yEnd, x, y, bodyLength, tempAngle, sin, cos, doswn);
		} else {
			xEnd = x - halfLength;
			yEnd = y;
			// cos = 0;
			// sin = 1;
			runSpanFrom(molm, xEnd, yEnd, x + restLength, y, bodyLength, 0.0, 1, 0, doswn);
			if (angleDeg == 0.0)
				x += restLength;
			else
				x -= halfLength;
		}
		// xHead = xx;//riciclo xx come che xHead
		// yHead = yy;

		// ora i lati

		rad_angleHighestBorder = Math
				.toRadians(tempAngle = MathUtilities.adjustDegAngle((angleDeg + 180.0) - angleDegBorder));
		xEnd = x + (int) Math.round(borderLength
				* (cos = (tempAngle == 0.0 ? 1 : (tempAngle == 180.0 ? -1 : Math.cos(rad_angleHighestBorder)))));
		yEnd = y + (int) Math.round(
				borderLength * (sin = (tempAngle == 0.0 || tempAngle == 180.0 ? 0 : Math.sin(rad_angleHighestBorder))));
		// fai gli span
		runSpanFrom(molm, x, y, xEnd, yEnd, borderLength, tempAngle, sin, cos, doswn);
		// runSpanFrom(molm, xEnd, yEnd, x, y, borderLength, tempAngle, sin,
		// cos, doswn);

		rad_angleHighestBorder = Math
				.toRadians(tempAngle = MathUtilities.adjustDegAngle((angleDeg + 180.0) + angleDegBorder));
		xEnd = x + (int) Math
				.round(borderLength * (cos = (rad_angleHighestBorder == 0.0 ? 1 : Math.cos(rad_angleHighestBorder))));
		yEnd = y + (int) Math.round(
				borderLength * (sin = (tempAngle == 0.0 || tempAngle == 180.0 ? 0 : Math.sin(rad_angleHighestBorder))));
		// fai gli span
		runSpanFrom(molm, x, y, xEnd, yEnd, borderLength, tempAngle, sin, cos, doswn);
		// runSpanFrom(molm, xEnd, yEnd, x, y, borderLength, tempAngle, sin,
		// cos, doswn);

		return true;// for now
	}

	//

	//

	// TODO CONO

	//

	//

	/*
	 * protected boolean runOn_Cone(MatrixObjectLocationManager molm,
	 * ShapeSpecification gss, DoSomethingWithNode doswn) { SS_Cone g; g = (SS_Cone)
	 * gss; if (doswn == null) return false; return runOn_Cone(molm, gss.getX(),
	 * gss.getY(), g.numberOfSteps, g.baseHeight, g.widthIncrement,
	 * g.heightIncrement, g.angleDeg, doswn); }
	 */

	@Override
	public boolean runOnCone(MatrixObjectLocationManager molm, int xStart, int yStart, int numberOfSteps,
			int baseHeight, int widthIncrement, int heightIncrement, double angleDeg, DoSomethingWithNode doswn) {

		if (baseHeight < 1 || heightIncrement < 1 || widthIncrement == 0 || numberOfSteps < 1)
			return false;
		angleDeg = MathUtilities.adjustDegAngle(angleDeg);
		if (angleDeg == 0.0)
			return runOn_Cone_Horizontal(molm, xStart, yStart, numberOfSteps, baseHeight, widthIncrement,
					heightIncrement, doswn);
		else if (angleDeg == 180.0)
			return runOn_Cone_Horizontal(molm, xStart, yStart, numberOfSteps, baseHeight, -widthIncrement,
					heightIncrement, doswn);
		else if (angleDeg == 270.0)
			return runOn_Cone_Vertical(molm, xStart, yStart, numberOfSteps, baseHeight, -widthIncrement,
					heightIncrement, doswn);
		else if (angleDeg == 90.0)
			return runOn_Cone_Vertical(molm, xStart, yStart, numberOfSteps, baseHeight, widthIncrement, heightIncrement,
					doswn);
		else {
			// do considering angle
			throw new NotImplementedException();
		}
		// return false;
	}

	/**
	 * Il punto (x,y) e' il punto centrale della base, da cui inizia il cono. <br>
	 * Considerando che questo metodo tratta uno sviluppo orizzontale, e solo
	 * quello, i due angoli permessi sono 0° e 180 e con "0°" si intende porre la
	 * punta [base] per i valori minori di X e lo sviluppo per i valori maggiori di
	 * X) , quindi widthIncrement puo' essere negativo (per rappresentare il caso
	 * dell'angolo 180°) cosi' che il cono abbia uno sviluppo "retrogrado".
	 * <p>
	 * ......widthIncrement<br>
	 * ......___________<br>
	 * .....|<br>
	 * base |<br>
	 * .....|___________<br>
	 * .................|<br>
	 * heightIncrement..|<br>
	 * .................|<br>
	 */
	protected static boolean runOn_Cone_Horizontal(MatrixObjectLocationManager molm, int xStart, int yStart,
			int numberOfSteps, int baseHeight, int widthIncrement, int heightIncrement, DoSomethingWithNode doswn) {
		int totalWidth, totalHeight, halfHeight;

		totalWidth = widthIncrement * numberOfSteps;
		totalHeight = ((heightIncrement * /* ( */numberOfSteps /*- 1)*/) << 1) + baseHeight;
		halfHeight = totalHeight >> 1;
		if (MathUtilities.isInside(xStart, yStart, molm) && MathUtilities.isInside(xStart, yStart + halfHeight, molm)
				&& MathUtilities.isInside(xStart, yStart - halfHeight, molm)
				&& MathUtilities.isInside(xStart + totalWidth, yStart + halfHeight, molm)
				&& MathUtilities.isInside(xStart + totalWidth, yStart - halfHeight, molm)) {
			runOn_Cone_Horizontal_Unsafe(molm, doswn, xStart, yStart, numberOfSteps, baseHeight, widthIncrement,
					heightIncrement, totalWidth, totalHeight);
		} else {
			runOn_Cone_Horizontal_Safe(molm, doswn, xStart, yStart, numberOfSteps, baseHeight, widthIncrement,
					heightIncrement, totalWidth, totalHeight);
		}

		return true;
	}

	protected static void runOn_Cone_Horizontal_Unsafe(MatrixObjectLocationManager molm, DoSomethingWithNode doswn,
			int xStart, int yStart, int numberOfSteps, int baseHeight, int widthIncrement, int heightIncrement,
			int totalWidth, int totalHeight) {
		int ySpecular;

		ySpecular = (yStart -= (baseHeight >> 1)) + baseHeight;

		if (widthIncrement > 0) {

			// la base
			fillRectangle_Unsafe(molm, xStart, yStart, totalWidth, baseHeight, doswn);

			// il resto

			// faccio questa sottrazione solo per ottimizzare dopo nel ciclo
			ySpecular -= heightIncrement;
			// un primo run e' stato fatto;
			while((totalWidth -= widthIncrement) > 0) {
				// riduco i futuri rettangoli da disegnare
				xStart += widthIncrement;
				yStart -= heightIncrement;
				ySpecular += heightIncrement;
				fillRectangle_Unsafe(molm, xStart, yStart, totalWidth, heightIncrement, doswn);
				fillRectangle_Unsafe(molm, xStart, ySpecular, totalWidth, heightIncrement, doswn);
			}
		} else {
			// copia ed incolla qua sopra, ma aggiusta il disegno dei rettangoli
			totalWidth = -totalWidth;
			// la base

			xStart -= totalWidth;
			fillRectangle_Unsafe(molm, xStart, yStart, totalWidth, baseHeight, doswn);

			// il resto

			// faccio questa sottrazione solo per ottimizzare dopo nel ciclo
			ySpecular -= heightIncrement;
			// un primo run e' stato fatto;
			while((totalWidth += widthIncrement) > 0) {
				yStart -= heightIncrement;
				ySpecular += heightIncrement;
				fillRectangle_Unsafe(molm, xStart, yStart, totalWidth, heightIncrement, doswn);
				fillRectangle_Unsafe(molm, xStart, ySpecular, totalWidth, heightIncrement, doswn);
			}
		}
	}

	protected static void runOn_Cone_Horizontal_Safe(MatrixObjectLocationManager molm, DoSomethingWithNode doswn,
			int xStart, int yStart, int numberOfSteps, int baseHeight, int widthIncrement, int heightIncrement,
			int totalWidth, int totalHeight) {
		int ySpecular;

		ySpecular = (yStart -= (baseHeight >> 1)) + baseHeight;

		if (widthIncrement > 0) {

			// la base
			fillRectangle_Safe(molm, xStart, yStart, totalWidth, baseHeight, doswn);

			// il resto

			// faccio questa sottrazione solo per ottimizzare dopo nel ciclo
			ySpecular -= heightIncrement;
			// un primo run e' stato fatto;
			while((totalWidth -= widthIncrement) > 0) {
				// riduco i futuri rettangoli da disegnare
				xStart += widthIncrement;
				yStart -= heightIncrement;
				ySpecular += heightIncrement;
				fillRectangle_Safe(molm, xStart, yStart, totalWidth, heightIncrement, doswn);
				fillRectangle_Safe(molm, xStart, ySpecular, totalWidth, heightIncrement, doswn);
			}
		} else {
			// copia ed incolla qua sopra, ma aggiusta il disegno dei rettangoli
			totalWidth = -totalWidth;
			// la base

			xStart -= totalWidth;
			fillRectangle_Safe(molm, xStart, yStart, totalWidth, baseHeight, doswn);

			// il resto

			// faccio questa sottrazione solo per ottimizzare dopo nel ciclo
			ySpecular -= heightIncrement;
			// un primo run e' stato fatto;
			while((totalWidth += widthIncrement) > 0) {
				yStart -= heightIncrement;
				ySpecular += heightIncrement;
				fillRectangle_Safe(molm, xStart, yStart, totalWidth, heightIncrement, doswn);
				fillRectangle_Safe(molm, xStart, ySpecular, totalWidth, heightIncrement, doswn);
			}
		}
	}

	protected boolean runOn_Cone_Vertical(MatrixObjectLocationManager molm, int xStart, int yStart, int numberOfSteps,
			int baseHeight, int widthIncrement, int heightIncrement, DoSomethingWithNode doswn) {
		int totalWidth, totalHeight, halfHeight;

		totalWidth = widthIncrement * numberOfSteps;
		// totalHeight = (heightIncrement * (numberOfSteps - 1) << 1) +
		// baseHeight;
		totalHeight = ((heightIncrement * /* ( */numberOfSteps /*- 1)*/) << 1) + baseHeight;
		halfHeight = totalHeight >> 1;
		if (MathUtilities.isInside(xStart, yStart, molm) && MathUtilities.isInside(xStart + halfHeight, yStart, molm)
				&& MathUtilities.isInside(xStart - halfHeight, yStart, molm)
				&& MathUtilities.isInside(xStart + halfHeight, yStart + totalWidth, molm)
				&& MathUtilities.isInside(xStart - halfHeight, yStart + totalWidth, molm)) {
			runOn_Cone_Vertical_Unsafe(molm, doswn, xStart, yStart, numberOfSteps, baseHeight, widthIncrement,
					heightIncrement, totalWidth, totalHeight);
		} else {
			runOn_Cone_Vertical_Safe(molm, doswn, xStart, yStart, numberOfSteps, baseHeight, widthIncrement,
					heightIncrement, totalWidth, totalHeight);
		}

		return true;
	}

	/** 90° -> punta in "basso", sviluppo ascendente */
	protected static void runOn_Cone_Vertical_Unsafe(MatrixObjectLocationManager molm, DoSomethingWithNode doswn,
			int xStart, int yStart, int numberOfSteps, int baseHeight, int widthIncrement, int heightIncrement,
			int totalWidth, int totalHeight) {
		int progressiveHeight, doubleHeightIncr;

		xStart -= (baseHeight >> 1);
		doubleHeightIncr = heightIncrement << 1;
		/*
		 * differentemente da quello orizzontale, conviene procedere aggiungendo
		 * rettangoli non "per lungo, lungo lo sviluppo", ma per "livelli", ossia
		 * disegno livello per livello, step by step, cosicche' si sfrutta meglio
		 * l'ottimizzazione sulle righe fornita da "fillRectangle"
		 */
		/*
		 * I nomi delle variabili "progressive" sono dati immaginando il cono come
		 * orizzontale, anche se qui lo si disegna verticale
		 */

		if (widthIncrement > 0) {
			yStart += widthIncrement;
			fillRectangle_Unsafe(molm, xStart, yStart, progressiveHeight = baseHeight, widthIncrement, doswn);

			// il resto
			while(numberOfSteps-- > 0) {
				xStart -= heightIncrement;
				yStart += widthIncrement;
				progressiveHeight += doubleHeightIncr;
				fillRectangle_Unsafe(molm, xStart, yStart, progressiveHeight, widthIncrement, doswn);
			}
		} else {
			// base in alto, sviluppo verso il basso
			widthIncrement = -widthIncrement;
			// la base
			fillRectangle_Unsafe(molm, xStart, yStart, progressiveHeight = baseHeight, widthIncrement, doswn);

			// il resto

			// un primo run e' stato fatto;
			while(numberOfSteps-- > 0) {
				// riduco i futuri rettangoli da disegnare
				xStart -= heightIncrement;
				yStart -= widthIncrement;
				// xSpecular += heightIncrement;
				progressiveHeight += doubleHeightIncr;
				fillRectangle_Unsafe(molm, xStart, yStart, progressiveHeight, widthIncrement, doswn);
			}
		}
	}

	protected static void runOn_Cone_Vertical_Safe(MatrixObjectLocationManager molm, DoSomethingWithNode doswn,
			int xStart, int yStart, int numberOfSteps, int baseHeight, int widthIncrement, int heightIncrement,
			int totalWidth, int totalHeight) {
		int progressiveHeight, doubleHeightIncr;

		xStart -= (baseHeight >> 1);
		doubleHeightIncr = heightIncrement << 1;
		/*
		 * differentemente da quello orizzontale, conviene procedere aggiungendo
		 * rettangoli non "per lungo, lungo lo sviluppo", ma per "livelli", ossia
		 * disegno livello per livello, step by step, cosicche' si sfrutta meglio
		 * l'ottimizzazione sulle righe fornita da "fillRectangle"
		 */
		/*
		 * I nomi delle variabili "progressive" sono dati immaginando il cono come
		 * orizzontale, anche se qui lo si disegna verticale
		 */

		if (widthIncrement > 0) {
			yStart += widthIncrement;
			fillRectangle_Safe(molm, xStart, yStart, progressiveHeight = baseHeight, widthIncrement, doswn);

			// il resto
			while(numberOfSteps-- > 0) {
				xStart -= heightIncrement;
				yStart += widthIncrement;
				progressiveHeight += doubleHeightIncr;
				fillRectangle_Safe(molm, xStart, yStart, progressiveHeight, widthIncrement, doswn);
			}
		} else {
			// base in alto, sviluppo verso il basso
			widthIncrement = -widthIncrement;
			// la base
			fillRectangle_Safe(molm, xStart, yStart, progressiveHeight = baseHeight, widthIncrement, doswn);

			// il resto

			// un primo run e' stato fatto;
			while(numberOfSteps-- > 0) {
				// riduco i futuri rettangoli da disegnare
				xStart -= heightIncrement;
				yStart -= widthIncrement;
				// xSpecular += heightIncrement;
				progressiveHeight += doubleHeightIncr;
				fillRectangle_Safe(molm, xStart, yStart, progressiveHeight, widthIncrement, doswn);
			}
		}
	}

	//

	//

	//

	// TODO LINE

	//

	//

	@Override
	public boolean runOnLine(MatrixObjectLocationManager molm, int x, int y, int length, double angleDeg,
			DoSomethingWithNode doswn) {
		double sin, cos, rad;
		rad = Math.toRadians(MathUtilities.adjustDegAngle(angleDeg));
		sin = Math.sin(rad);
		cos = Math.cos(rad);
		runSpanFrom(molm, x, y, (int) (x + cos * length), (int) (y + sin * length), length, angleDeg, sin, cos, doswn);
		return true;
	}

	//

	//

	// TODO ELLIPSE FILLING

	//

	//

	@Override
	public boolean runOnEllipseNoRotation(MatrixObjectLocationManager molm, int x, int y, int width, int height,
			DoSomethingWithNode dswi) {
		int a, b;
		if (dswi != null) {
			a = width >> 1;
			b = height >> 1;
			if ((x - a) >= 0 && (x + (width - a)) < molm.getWidth() && (y - b) >= 0
					&& (y + (height - b)) < molm.getHeight())
				fillEllipse_Unsafe(molm, x, y, a, b, dswi);
			else
				fillEllipse_Safe(molm, x, y, a, b, dswi);
			return true;
		}
		return false;
	}

	/** (x,y) = center of ellipse */
	protected static void fillEllipse_Unsafe(MatrixObjectLocationManager molm, int x, int y, int a, int b,
			DoSomethingWithNode doswn) {
		int yy, xx, tx, ty, len;
		double bb;
		yy = -1;
		bb = b;
		while(++yy <= b) {
			xx = yy != 0 ? (int) Math.round(Math.cos(Math.asin((yy) / bb)) * a) : a;
			if (xx > 0) {
				runHorizontalSpan_Unsafe(tx = x - xx, y + yy, len = (xx << 1) + 1, molm, doswn);
				if (yy != 0)
					runHorizontalSpan_Unsafe(tx, y - yy, len, molm, doswn);
			}
		}
	}

	protected static void fillEllipse_Safe(MatrixObjectLocationManager molm, int x, int y, int a, int b,
			DoSomethingWithNode doswn) {
		int yy, xx, tx, ty, len;
		double bb;
		yy = -1;
		bb = b;
		while(++yy <= b) {
			xx = yy != 0 ? (int) Math.round(Math.cos(Math.asin((yy) / bb)) * a) : a;
			if (xx > 0) {
				runHorizontalSpan_Safe(tx = x - xx, y + yy, len = (xx << 1) + 1, molm, doswn);
				if (yy != 0)
					runHorizontalSpan_Safe(tx, y - yy, len, molm, doswn);
			}
		}
	}

	@Override
	public boolean runOnEllipseNoRotationBorder(MatrixObjectLocationManager molm, int x, int y, int width, int height,
			DoSomethingWithNode dswi) {
		int a, b;
		if (dswi != null) {
			a = width >> 1;
			b = height >> 1;
			if ((x - a) >= 0 && (x + (width - a)) < molm.getWidth() && (y - b) >= 0
					&& (y + (height - b)) < molm.getHeight())
				runEllipseNoRotationBorder_Unsafe(molm, x, y, a, b, dswi);
			else
				runEllipseNoRotationBorder_Safe(molm, x, y, a, b, dswi);
			return true;
		}
		return false;
	}

	/** (x,y) = center of ellipse */
	protected static void runEllipseNoRotationBorder_Unsafe(MatrixObjectLocationManager molm, int x, int y, int a,
			int b, DoSomethingWithNode doswn) {
		int yy, xx, tx, ty, len, oldc;// , xMinusA, xPlusA;
		double bb;
		NodeMatrix[] row, matrix[];
		matrix = molm.getMatrix();

		yy = -1;
		bb = b;
		oldc = 0;
		yy = b;
		row = matrix[y];
		doswn.doOnNode(molm, row[tx = x - a], tx, y);
		doswn.doOnNode(molm, row[tx = x + a], tx, y);
		while(--yy >= 0) {
			// if (yy < b) {
			xx = (int) Math.round(Math.cos(Math.asin((yy) / bb)) * a);
			if (xx > 0) {
				len = Math.max(1, xx - oldc);
				yy++;
				runHorizontalSpan_Unsafe(x + oldc, ty = y + yy, len, molm, doswn);
				runHorizontalSpan_Unsafe(tx = x - (oldc + len - 1), ty, len, molm, doswn);
				runHorizontalSpan_Unsafe(tx, ty = y - yy, len, molm, doswn);
				runHorizontalSpan_Unsafe(x + oldc, ty, len, molm, doswn);
				yy--;
			}
			oldc = xx;
		}
	}

	protected static void runEllipseNoRotationBorder_Safe(MatrixObjectLocationManager molm, int x, int y, int a, int b,
			DoSomethingWithNode doswn) {
		int yy, xx, tx, ty, len, oldc;// , xMinusA, xPlusA;
		double bb;
		NodeMatrix[] row, matrix[];
		matrix = molm.getMatrix();

		yy = -1;
		bb = b;
		oldc = 0;
		yy = b;
		if (y >= 0 && y < molm.getHeight()) {
			row = matrix[y];
			if ((tx = x - a) >= 0 && tx < molm.getWidth())
				doswn.doOnNode(molm, row[tx], tx, y);
			if ((tx = x + a) >= 0 && tx < molm.getWidth())
				doswn.doOnNode(molm, row[tx], tx, y);
		}
		while(--yy >= 0) {
			// if (yy < b) {
			xx = (int) Math.round(Math.cos(Math.asin((yy) / bb)) * a);
			if (xx > 0) {
				len = Math.max(1, xx - oldc);
				yy++;
				runHorizontalSpan_Safe(x + oldc, ty = y + yy, len, molm, doswn);
				runHorizontalSpan_Safe(tx = x - (oldc + len - 1), ty, len, molm, doswn);
				runHorizontalSpan_Safe(tx, ty = y - yy, len, molm, doswn);
				runHorizontalSpan_Safe(x + oldc, ty, len, molm, doswn);
				yy--;
			}
			oldc = xx;
		}
	}

	//

	// TODO UTILITIES

	//

	//

	protected static void runSpanFrom(MatrixObjectLocationManager molm, int xStart, int yStart, int xDest, int yDest,
			int spanLength, double angleDeg, double sin, double cos, DoSomethingWithNode doswn) {
		if (MathUtilities.isInside(xStart, yStart, molm) && MathUtilities.isInside(xDest, yDest, molm)) {
			if (angleDeg == 0.0 || angleDeg == 180.0)
				runHorizontalSpan_Unsafe(xStart, yStart, spanLength, molm, doswn);
			else if (angleDeg == 90.0 || angleDeg == 270.0)
				runVerticalSpan_Unsafe(xStart, yStart, spanLength, molm, doswn);
			else
				runRotatedSpan_Unsafe(molm, xStart, yStart, spanLength, doswn, sin, cos);
		} else {
			if (angleDeg == 0.0 || angleDeg == 180.0)
				runHorizontalSpan_Safe(xStart, yStart, spanLength, molm, doswn);
			else if (angleDeg == 90.0 || angleDeg == 270.0)
				runVerticalSpan_Safe(xStart, yStart, spanLength, molm, doswn);
			else
				runRotatedSpan_Safe(molm, xStart, yStart, spanLength, doswn, sin, cos);
		}
	}

	protected static void runRotatedSpan_Unsafe(MatrixObjectLocationManager molm, int xx, int yy, int w,
			DoSomethingWithNode doswn, double sin, double cos) {
		int c, xfloor, yfloor, tx;
		NodeMatrix row[];
		NodeMatrix[][] m;
		m = molm.matrix;
		c = -1;
		while(++c < w && doswn.canContinueCycling()) {
			yfloor = yy + (int) Math.round(c * sin);
			xfloor = xx + (int) Math.round(c * cos);

			row = m[yfloor];
			// doswn.doOnRow(row, xfloor, yfloor);
			doswn.doOnNode(molm, row[xfloor], xfloor, yfloor);
			// doswn.doOnRow(row, xfloor + 1, yfloor);
			doswn.doOnNode(molm, row[tx = xfloor + 1], tx, yfloor);

			row = m[++yfloor];// <br>
			// doswn.doOnRow(row, xfloor, yfloor);//<br>
			doswn.doOnNode(molm, row[xfloor], xfloor, yfloor);// <br>
			// doswn.doOnRow(row, xfloor + 1, yfloor);//<br>
			doswn.doOnNode(molm, row[tx = xfloor + 1], tx, yfloor);
		}
	}

	protected static void runRotatedSpan_Safe(MatrixObjectLocationManager molm, int xx, int yy, int w,
			DoSomethingWithNode doswn, double sin, double cos) {
		int c, xfloor, yfloor, ylen, xlen, xtemp;
		NodeMatrix row[];
		NodeMatrix[][] m;
		m = molm.matrix;
		c = -1;
		xlen = m[0].length;
		ylen = m.length;
		while(++c < w && doswn.canContinueCycling()) {
			yfloor = yy + (int) Math.round(c * sin);
			xfloor = xx + (int) Math.round(c * cos);

			if (yfloor >= 0 && yfloor < ylen) {
				row = m[yfloor];
				if (xfloor >= 0 && xfloor < xlen) {
					// doswn.doOnRow(row, xfloor, yfloor);
					doswn.doOnNode(molm, row[xfloor], xfloor, yfloor);
				}
				if ((xtemp = xfloor + 1) >= 0 && xtemp < xlen) {
					// doswn.doOnRow(row, xtemp, yfloor);
					doswn.doOnNode(molm, row[xtemp], xtemp, yfloor);
				}
			}

			yfloor++;
			if (yfloor >= 0 && yfloor < ylen) {
				row = m[yfloor];
				if (xfloor >= 0 && xfloor < xlen) {
					// doswn.doOnRow(row, xfloor, yfloor);
					doswn.doOnNode(molm, row[xfloor], xfloor, yfloor);
				}
				if ((xtemp = xfloor + 1) >= 0 && xtemp < xlen) {
					// doswn.doOnRow(row, xtemp, yfloor);
					doswn.doOnNode(molm, row[xtemp], xtemp, yfloor);
				}
			}
		}
	}

	protected static void runHorizontalSpan_Unsafe(MatrixObjectLocationManager molm, int xStart, int xDest, int y,
			DoSomethingWithNode doswn) {
		if (xDest >= xStart)
			runHorizontalSpan_Unsafe(xStart, y, (xDest - xStart) + 1, molm, doswn);
	}

	protected static void runHorizontalSpan_Unsafe(int x, int y, int l, MatrixObjectLocationManager molm,
			DoSomethingWithNode doswn) {
		// int i;
		NodeMatrix[] row;
		// NodeMatrix[][] m;
		// m = molm.matrix;
		row = molm.matrix[y];
		if (l <= 0)
			return;
		// throw new IllegalArgumentException("l " + l);
		// i = -1;
		while(// ++i < l
		l-- > 0 && doswn.canContinueCycling()) {
			// doswn.doOnRow(riga, x + i, y);
			doswn.doOnNode(molm, row[x], x++, y);
		}
	}

	/** Page-fault prone */
	protected static void runVerticalSpan_Unsafe(int x, int y, int l, MatrixObjectLocationManager molm,
			DoSomethingWithNode doswn) {
		NodeMatrix[][] matrix;
		matrix = molm.matrix;
		while(l-- > 0 && doswn.canContinueCycling()) {
			// doswn.doOnItem(matrix, x, y + i);
			doswn.doOnNode(molm, matrix[y][x], x, y++);
		}
	}

	protected static void runHorizontalSpan_Safe(int x, int y, int l, MatrixObjectLocationManager molm,
			DoSomethingWithNode doswn) {
		int len;
		NodeMatrix[] row;
		NodeMatrix[][] m;
		m = molm.matrix;

		if (y < 0 || y >= m.length) {
			return;
		}

		row = m[y];
		// i = -1;
		if (x < 0) {
			l += x;
			x = 0;
		}
		if (row == null || l <= 0 || x >= (len = row.length)) {
			return;
		}
		while(l-- > 0 && x < len && doswn.canContinueCycling()) {
			// doswn.doOnRow(riga, x++, y);
			doswn.doOnNode(molm, row[x], x++, y);
		}
	}

	protected static void runVerticalSpan_Safe(int x, int y, int l, MatrixObjectLocationManager molm,
			DoSomethingWithNode doswn) {
		int len;
		NodeMatrix[] row;
		NodeMatrix[][] matrix;
		matrix = molm.matrix;

		if (y < 0) {
			l += y;
			y = 0;
		}
		if (x < 0 || y + l >= (len = matrix.length)) {
			return;
		}

		// i = -1;
		while(l-- > 0 && y < len && doswn.canContinueCycling()) {
			row = matrix[y++];
			if (row != null && row.length > x) {
				// doswn.doOnRow(row, x, y - 1);
				doswn.doOnNode(molm, row[x], x++, y - 1);
			}
		}
	}

	// TODO OTHER UTILITIES

	protected static boolean allInside(MatrixObjectLocationManager molm, int x, int y, int height) {
		boolean b;
		NodeMatrix[] row;
		NodeMatrix[][] matrix;
		matrix = molm.matrix;

		if (b = x >= 0 && MathUtilities.isAtMostPositive(y, height)) {
			row = matrix[y];
			b = row != null && x < row.length;
		}
		return b;
	}

	/** sectionle sort, ipotizzando che il numero di elementi sia piccolo */
	protected static boolean sortPoint_ForFillingTriangle(int[] xx, int[] yy, int[] previousPositionTracker) {
		int imax, imedium, ilow;
		int[] t;

		t = new int[3];
		imax = imedium = ilow = 0;
		if (yy[0] > yy[1]) {
			if (yy[0] > yy[2]) {
				imax = 0;
				// ordine : 0, ?, ? ; ? € {1,2}
				if (yy[1] > yy[2]) {
					// gia' ordinato, lol
					imedium = 1;
					ilow = 2;
				} else {
					imedium = 2;
					ilow = 1;
				}
			} else {
				imax = 2;
				// ordine ; 2,0,1
				imedium = 0;
				ilow = 1;
			}

		} else {

			// ordine : ?, 1, ?, 0, ? ; ? € {null,0,2}

			if (yy[1] > yy[2]) {
				imax = 1;

				if (yy[0] > yy[2]) {
					imedium = 0;
					ilow = 2;
				} else {
					imedium = 2;
					ilow = 0;
				}
			} else {
				imax = 2;
				if (yy[0] > yy[1]) {
					imedium = 0;
					ilow = 1;
				} else {
					imedium = 1;
					ilow = 0;
				}
			}
		}

		// controllo sulle uguaglianze di valori
		if (yy[imax] == yy[imedium]) {
			if (xx[imax] > xx[imedium]) {
				// anomalia, imax e' a "destra" -> swap
				t[0] = imax;
				imax = imedium;
				imedium = t[0];
			}
		}
		if (yy[ilow] == yy[imedium]) {
			if (xx[ilow] < xx[imedium]) {
				// anomalia, ilow e' a "sinistra" -> swap
				t[0] = ilow;
				ilow = imedium;
				imedium = t[0];
			}
		}

		// copia
		t[0] = xx[0];
		t[1] = xx[1];
		t[2] = xx[2];

		// ordina
		xx[0] = t[imax];
		xx[1] = t[imedium];
		xx[2] = t[ilow];

		// copia
		t[0] = yy[0];
		t[1] = yy[1];
		t[2] = yy[2];

		// ordina
		yy[0] = t[imax];
		yy[1] = t[imedium];
		yy[2] = t[ilow];

		t = null;// un-alloc

		previousPositionTracker[0] = imax;
		previousPositionTracker[1] = imedium;
		previousPositionTracker[2] = ilow;

		return xx[1] < xx[2];
	}

}