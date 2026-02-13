package videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM;

import java.io.Serializable;
import java.util.Comparator;

import tools.Comparators;
import videogamesOldVersion.common.mainTools.mOLM.MatrixObjectLocationManager;
import videogamesOldVersion.common.mainTools.mOLM.ShapeRunners;

/**
 * TODO MODIFY RUNNERS: THE POINT GIVEN AS PARAMETER WILL BE THE SHAPE'S CENTER,
 * NOT THE LEFT-TOP CORNER, LIKE NOW.<br>
 * If not done, changes on Objects (adding (xOnCenter,yOnCenter)) will be
 * useless.
 * <p>
 *
 * N.B.: all center of rotations of each shape must be the "center of gravity"
 * of that shape, that is the point passed as parameter.
 */
public interface AbstractShapeRunners extends Serializable {
	public static enum ShapesImplemented implements Serializable, Comparator<ShapesImplemented> {
		Point, Rectangle, Rectangle_Border, Circumference, Circle, Triangle, // TriangleBorder,
		Arrow, ArrowBorderBodySameLength, Cone, // Cono_Border,
		Line, EllipseNoRotation, EllipseNoRotation_Border;

		public final Integer sequentialNumber;

		ShapesImplemented() {
			sequentialNumber = this.ordinal();
		}

		@Override
		public int compare(ShapesImplemented si1, ShapesImplemented si2) {
			if (si1 == si2)
				return 0;
			if (si1 == null)
				return -1;
			if (si2 == null)
				return 1;
			return Comparators.INTEGER_COMPARATOR.compare(si1.sequentialNumber, si2.sequentialNumber);
		}
	}

	public static final Comparator<ShapesImplemented> COMPARATOR_ShapesImplemented = ShapesImplemented.Point;

	public static AbstractShapeRunners getOrDefault(AbstractShapeRunners asr) {
		return asr != null ? asr : ShapeRunners.getInstance();
	}

	// public final ShapeRunner RECTANGLE ;

	public ShapeRunner getRunner(ShapesImplemented si);

	public default String runOnShape(MatrixObjectLocationManager molm, ShapeSpecification ss,
			DoSomethingWithNode dswi) {
		StringBuilder textError;
		ShapeRunner sr;
		ShapesImplemented si;

		textError = null;
		si = null;
		if (molm != null && ss != null && (si = ss.getShape()) != null && dswi != null) {

			// se possibile, ricicliamo lo ShapeRunner associato allo ShapeSpec
			// let's do cache
			if ((sr = ss.getShapeRunner()) == null) {
				ss.setShapeRunner(sr = getRunner(si));
			}
			if (sr != null) {
				sr.runArea(molm, ss, dswi);
			} else
				throw new IllegalArgumentException("Unrecognized ShapeRunner: " + si);

		} else {
			textError = new StringBuilder();
			if (molm == null) {
				textError.append("MatrixObjectLocationManager null");
			}
			if (ss == null) {
				if (textError.length() > 0)
					textError.append(", ");
				textError.append("ShapeSpecification null");
			}
			if (si == null) {
				if (textError.length() > 0)
					textError.append(", ");
				textError.append("Shape null");
			}
			if (dswi == null) {
				if (textError.length() > 0)
					textError.append(", ");
				textError.append("DoSomethingWithNode null");
			}
		}
		return (textError == null || textError.length() == 0) ? null : textError.toString();
	}

	//

	//

	public default boolean runOnPoint(MatrixObjectLocationManager molm, ShapeSpecification ss,
			DoSomethingWithNode dswi) {
		// SS_Point ssa;
		// ssa = (SS_Point) ss;
		return runOnPoint(molm, ss.getXLeftBottom(), ss.getYLeftBottom(), dswi);
	}

	public default boolean runOnPoint(MatrixObjectLocationManager molm, int x, int y, DoSomethingWithNode dswi) {
		if (molm != null && dswi != null && x >= 0 && y >= 0 && x < molm.getWidth() && y < molm.getHeight()) {
			dswi.doOnNode(molm, molm.getNodeMatrix(x, y), x, y);
			return true;
		}
		return false;
	}

	//

	public default boolean runOnLine(MatrixObjectLocationManager molm, ShapeSpecification ss,
			DoSomethingWithNode dswi) {
		SS_Line ssa;
		ssa = (SS_Line) ss;
		return runOnLine(molm, ss.getXCenter(), ss.getYCenter(), ssa.getLength(), ssa.getAngleDeg(), dswi);
	}

	/**
	 * BEWARE: the point P(x,y) passed as parameter is both the center of the shape
	 * and the center of the shape's rotation.
	 */
	public boolean runOnLine(MatrixObjectLocationManager molm, int x, int y, int length, double angleDeg,
			DoSomethingWithNode dswi);
	//

	public default boolean runOnRectangle(MatrixObjectLocationManager molm, ShapeSpecification ss,
			DoSomethingWithNode dswi) {
		SS_Rectangular gr;
		gr = (SS_Rectangular) ss;
		return runOnRectangle(molm, gr.getXCenter(), gr.getYCenter(), gr.getWidth(), gr.getHeight(), gr.getAngleDeg(),
				dswi);
	}

	/**
	 * Draw the whole rectangle, not just the border.<br>
	 * <br>
	 * BEWARE: the point P(x,y) passed as parameter is both the center of the shape
	 * and the center of the shape's rotation..<br>
	 *
	 * @param molm     {@link MatrixObjectLocationManager} instance
	 * @param x        the x-coordinate of the center.
	 * @param y        the y-coordinate of the center.
	 * @param width    the width of the rectangle-
	 * @param height   the height of the rectangle-
	 * @param angleDeg the angle, expressed in Degrees, used to rotate the
	 *                 rectangle.<br>
	 *                 <b>BEWARE</b>: The center of the rotation IS the center of
	 *                 the rectangle!
	 * @param dswi     {@link DoSomethingWithNode} instance, specifying what to do
	 */
	public boolean runOnRectangle(MatrixObjectLocationManager molm, int x, int y, int width, int height,
			double angleDeg, DoSomethingWithNode dswi);

	public default boolean runOnRectangleBorder(MatrixObjectLocationManager molm, ShapeSpecification ss,
			DoSomethingWithNode dswi) {
		SS_Rectangular ssr;
		ssr = (SS_Rectangular) ss;
		return runOnRectangleBorder(molm, ssr.getXCenter(), ssr.getYCenter(), ssr.getWidth(), ssr.height, ssr.angleDeg,
				dswi);
	}

	/**
	 * Draw the border of a rectangle.<br>
	 * <br>
	 * BEWARE: the point P(x,y) passed as parameter is both the center of the shape
	 * and the center of the shape's rotation.<br>
	 *
	 * @param molm     {@link MatrixObjectLocationManager} instance
	 * @param x        the x-coordinate of the center.
	 * @param y        the y-coordinate of the center.
	 * @param width    the width of the rectangle-
	 * @param height   the height of the rectangle-
	 * @param angleDeg the angle, expressed in Degrees, used to rotate the
	 *                 rectangle.<br>
	 *                 <b>BEWARE</b>: The center of the rotation IS the center of
	 *                 the square!
	 * @param dswi     {@link DoSomethingWithNode} instance, specifying what to do
	 */
	public boolean runOnRectangleBorder(MatrixObjectLocationManager molm, int x, int y, int width, int height,
			double angleDeg, DoSomethingWithNode dswi);

	//

	/**
	 * Just the border, as described by
	 * {@link #runOnCircumference(MatrixObjectLocationManager, int, int, int, DoSomethingWithNode)}.
	 */
	public default boolean runOnCircumference(MatrixObjectLocationManager molm, ShapeSpecification ss,
			DoSomethingWithNode dswi) {
		return runOnCircumference(molm, ss.getXCenter(), ss.getYCenter(), ((SS_Circular) ss).ray, dswi);
	}

	/**
	 * Draw the border of a circle.<br>
	 * The painted circle will have <code>(ray*2 +1)</code> diameter, so beware.
	 * <br>
	 * BEWARE: the point P(x,y) passed as parameter is both the center of the shape
	 * and the center of the shape's rotation.<br>
	 *
	 * @param molm {@link MatrixObjectLocationManager} instance
	 * @param x    the x-coordinate of the center
	 * @param y    the y-coordinate of the center
	 * @param ray  the ray of the circle. The diameter will be
	 *             <code>(ray*2 +1)</code>
	 * @param dswi {@link DoSomethingWithNode} instance, specifying what to do
	 */
	public boolean runOnCircumference(MatrixObjectLocationManager molm, int x, int y, int ray,
			DoSomethingWithNode dswi);

	/**
	 * Fill the whole circle, as described by
	 * {@link #runOnCircle(MatrixObjectLocationManager, int, int, int, DoSomethingWithNode)}.
	 */
	public default boolean runOnCircle(MatrixObjectLocationManager molm, ShapeSpecification ss,
			DoSomethingWithNode dswi) {
		return runOnCircle(molm, ss.getXCenter(), ss.getYCenter(), ((SS_Circular) ss).getRay(), dswi);
	}

	/**
	 * Draw the whole circle, not only the border.<br>
	 * The painted circle will have <code>(ray*2 +1)</code> diameter, so beware.
	 * <br>
	 * BEWARE: the point P(x,y) passed as parameter is both the center of the shape
	 * and the center of the shape's rotation.<br>
	 *
	 * @param molm {@link MatrixObjectLocationManager} instance
	 * @param x    the x-coordinate of the center
	 * @param y    the y-coordinate of the center
	 * @param ray  the ray of the circle. The diameter will be
	 *             <code>(ray*2 +1)</code>
	 * @param dswi {@link DoSomethingWithNode} instance, specifying what to do
	 */
	public boolean runOnCircle(MatrixObjectLocationManager molm, int x, int y, int ray, DoSomethingWithNode dswi);

	public default boolean runOnEquilateralTriangle(MatrixObjectLocationManager molm, ShapeSpecification ss,
			DoSomethingWithNode dswi) {
		SS_Triangle sst;
		sst = (SS_Triangle) ss;
		return runOnTriangle(molm, ss.getXCenter(), ss.getYCenter(), sst.getBorderLength(), sst.getAngleDeg(), dswi);
	}

	/**
	 * The coordinates P(x,y) passed as parameter IS the center of the triangle.<br>
	 * <br>
	 * _______________1 <br>
	 * ______________/_\ <br>
	 * ____________/_____\ <br>
	 * ___________/_______\ <br>
	 * _________/___(x,y)___\ <br>
	 * ________/_____________\ <br>
	 * _______3_______________2
	 *
	 * @param molm         {@link MatrixObjectLocationManager} instance
	 *
	 * @param x            the x-coordinate of the center
	 * @param y            the y-coordinate of the center
	 * @param lengthBorder the Length of the triangle's border.
	 * @param angleDeg     the angle, expressed in Degrees, used to rotate the
	 *                     rectangle.<br>
	 *                     <b>BEWARE</b>: The center of the rotation IS the center
	 *                     of the square!
	 * @param dswi         {@link DoSomethingWithNode} instance, specifying what to
	 *                     do
	 */
	public boolean runOnTriangle(MatrixObjectLocationManager molm, int x, int y, int lengthBorder, double angleDeg,
			DoSomethingWithNode dswi);

	//

	public default boolean runOnArrow_BorderBodySameLength(MatrixObjectLocationManager molm, ShapeSpecification ss,
			DoSomethingWithNode dswi) {
		SS_Arrow sst;
		sst = (SS_Arrow) ss;
		return runOnArrow_BorderBodySameLength(molm, ss.getXCenter(), ss.getYCenter(), sst.getBorderLength(),
				sst.getAngleDeg(), dswi);
	}

	/**
	 * The arrow's border and the line, that is the body, has the same length.
	 * Usually this should not happen.
	 * <p>
	 * Later will be implemented a method to draw an arrow with custom body's and
	 * borders' lengths and border's angles
	 */
	public boolean runOnArrow_BorderBodySameLength(MatrixObjectLocationManager molm, int x, int y, int borderLength,
			double angleDeg, DoSomethingWithNode dswi);

	//

	public default boolean runOnArrow(MatrixObjectLocationManager molm, ShapeSpecification ss,
			DoSomethingWithNode dswi) {
		SS_Arrow ssa;
		ssa = (SS_Arrow) ss;
		return runOnArrow(molm, ss.getXCenter(), ss.getYCenter(), ssa.getBorderLength(), ssa.getAngleDegBorder(),
				ssa.getBodyLength(), ssa.getAngleDeg(), dswi);
	}

	/**
	 * Run on a arrow shape.<br>
	 * NOTES:
	 * <ul>
	 * <li>BEWARE: the point P(x,y) passed as parameter is both the center of the
	 * shape and the center of the shape's rotation.</li>
	 * <li>The second double (<code>angleDeg</code>) is the angle, expressed in
	 * degrees, used to rotate the whole arrow. The center of the rotation is at the
	 * center of the body.</li>
	 * </ul>
	 * <p>
	 * Hypotesy: angleDeg = 0°<br>
	 * ........................................\<br>
	 * ..........................................\(border length)<br>
	 * ...........(border angle)...(a)\<br>
	 * ------P(x,y)(body length)---- ><br>
	 * ............................................./<br>
	 * .........................................../<br>
	 * ........................................./<br>
	 *
	 * @param molm         {@link MatrixObjectLocationManager} instance
	 *
	 * @param x            the x-coordinate of the center
	 * @param y            the y-coordinate of the center
	 * @param borderLength the Length of the arrow's borders.
	 * @param angleDeg     the angle, expressed in Degrees, used to rotate ONLY the
	 *                     borders.<br>
	 *                     <b>BEWARE</b>: The center of the rotation is the arrow's
	 *                     vertex.
	 * @param bodyLength   the Length of the arrow's body (the longest line,
	 *                     usually).
	 * @param angleDeg     the angle, expressed in Degrees, used to rotate the
	 *                     <i>whole</i> arrow.<br>
	 *                     <b>BEWARE</b>: The center of the rotation IS the center
	 *                     of the square!
	 * @param dswi         {@link DoSomethingWithNode} instance, specifying what to
	 *                     do
	 */
	public boolean runOnArrow(MatrixObjectLocationManager molm, int x, int y, int borderLength, double angleDegBorder,
			int bodyLength, double angleDeg, DoSomethingWithNode dswi);

	//
	public default boolean runOnCone(MatrixObjectLocationManager molm, ShapeSpecification ss,
			DoSomethingWithNode dswi) {
		SS_Cone ssa;
		ssa = (SS_Cone) ss;
		return runOnCone(molm, ss.getXCenter(), ss.getYCenter(), ssa.getNumberOfSteps(), ssa.getBaseHeight(),
				ssa.getWidthIncrement(), ssa.getHeightIncrement(), ssa.getAngleDeg(), dswi);
	}

	/**
	 * BEWARE: the point P(x,y) passed as parameter is both the center of the shape
	 * and the center of the shape's rotation.
	 *
	 * @param molm            {@link MatrixObjectLocationManager} instance
	 * @param xStart          x-coordinates of the center
	 * @param yStart          y-coordinates of the center
	 * @param numberOfSteps   The cone is a sequence of coaxial rectangles (imagine
	 *                        a pyramid, or a stair). This integer holds the number
	 *                        of steps/rectangles.
	 * @param baseHeight
	 * @param widthIncrement
	 * @param heightIncrement
	 * @param angleDeg
	 * @param                 dswi{@link DoSomethingWithNode} instance, describing
	 *                        what to do
	 * @return true if the paint operation succeeded
	 */
	public boolean runOnCone(MatrixObjectLocationManager molm, int xStart, int yStart, int numberOfSteps,
			int baseHeight, int widthIncrement, int heightIncrement, double angleDeg, DoSomethingWithNode dswi);

	public default boolean runOnEllipseNoRotation(MatrixObjectLocationManager molm, ShapeSpecification ss,
			DoSomethingWithNode dswi) {
		SS_Ellipse gr;
		gr = (SS_Ellipse) ss;
		return runOnEllipseNoRotation(molm, gr.getXCenter(), gr.getYCenter(), gr.getWidth(), gr.getHeight(), dswi);
	}

	/**
	 * Draw the whole ellipse, not just the border.<br>
	 * <br>
	 * BEWARE: the point P(x,y) passed as parameter is both the center of the shape
	 * and the center of the shape's rotation..<br>
	 *
	 * @param molm   {@link MatrixObjectLocationManager} instance
	 * @param x      the x-coordinate of the center.
	 * @param y      the y-coordinate of the center.
	 * @param width  the width of the rectangle-
	 * @param height the height of the rectangle-
	 * @param dswi   {@link DoSomethingWithNode} instance, specifying what to do
	 */
	public boolean runOnEllipseNoRotation(MatrixObjectLocationManager molm, int x, int y, int width, int height,
			DoSomethingWithNode dswi);

	public default boolean runOnEllipseNoRotationBorder(MatrixObjectLocationManager molm, ShapeSpecification ss,
			DoSomethingWithNode dswi) {
		SS_Ellipse gr;
		gr = (SS_Ellipse) ss;
		return runOnEllipseNoRotationBorder(molm, gr.getXCenter(), gr.getYCenter(), gr.getWidth(), gr.getHeight(),
				dswi);
	}

	/**
	 * See
	 * {@link AbstractShapeRunners#runOnEllipseNoRotation(MatrixObjectLocationManager, int, int, int, int, DoSomethingWithNode)},
	 * but runs only the border.
	 */
	public boolean runOnEllipseNoRotationBorder(MatrixObjectLocationManager molm, int x, int y, int width, int height,
			DoSomethingWithNode dswi);
}