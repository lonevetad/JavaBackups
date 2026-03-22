package games.theRisingAngel.enums;

import games.theRisingAngel.GModalityTRAnBaseWorld;
import geometry.AbstractShape2D;
import geometry.implementations.shapes.ShapeRectangle;
import geometry.implementations.shapes.subHierarchy.ShapeFillable;
import tools.ObjectNamedID;

public class ShapesTRAn {
	private static final int HALF_SQUARE = GModalityTRAnBaseWorld.SPACE_SUB_UNITS_EVERY_UNIT_EXAMPLE_TRAN >> 1, //
			FULL_SQUARE = GModalityTRAnBaseWorld.SPACE_SUB_UNITS_EVERY_UNIT_EXAMPLE_TRAN, //
			ONE_HALF_SQUARE = HALF_SQUARE + FULL_SQUARE, //
			DOUBLE_SQUARE = FULL_SQUARE << 1;

	public static enum Shapes implements ObjectNamedID {
		SquareSmall(new ShapeRectangle(false, HALF_SQUARE, HALF_SQUARE)), //
		SquareMedium(new ShapeRectangle(false, FULL_SQUARE, FULL_SQUARE)), //
		SquareBig(new ShapeRectangle(false, ONE_HALF_SQUARE, ONE_HALF_SQUARE)), //
		SquareLarge(new ShapeRectangle(false, DOUBLE_SQUARE, DOUBLE_SQUARE)), //
		;

		Shapes(AbstractShape2D shape) { this.shape = shape; }

		protected final AbstractShape2D shape;

		public AbstractShape2D newShape() { return this.newShape(true); }

		public AbstractShape2D newShape(boolean isFilled) {
			AbstractShape2D s;
			s = this.shape.clone();
			if (s instanceof ShapeFillable) { ((ShapeFillable) s).setFilled(isFilled); }
			return s;
		}

		@Override
		public Long getID() { return (long) this.ordinal(); }

		@Override
		public String getName() { return this.name(); }

		@Override
		public boolean setID(Long ID) { return false; }
	}
}