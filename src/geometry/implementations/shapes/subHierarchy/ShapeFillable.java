package geometry.implementations.shapes.subHierarchy;

import java.io.Serializable;

public interface ShapeFillable extends Serializable { // AbstractShapeImpl {
//	private static final long serialVersionUID = 305619647438L;

//	public AbstractFillable(AbstractFillable s) {
//		super(s);
//		this.isFilled = s.isFilled;
//	}
//
//	public AbstractFillable(ShapeRunnersImplemented shapeImplementing, int x, int y, double angleRotation,
//			boolean isFilled) {
//		super(shapeImplementing, x, y, angleRotation);
//		this.isFilled = isFilled;
//	}
//
//	protected boolean isFilled;

	public boolean isFilled();

	public ShapeFillable setFilled(boolean isFilled);
}