package geometry.implementations.shapes.subHierarchy;

import geometry.AbstractShape2D;
import geometry.ShapeRunnersImplemented;

public abstract class AbstractShapeRotated extends AbstractShape2D {
	private static final long serialVersionUID = -718739262045L;

	public AbstractShapeRotated(ShapeRunnersImplemented shapeImplementing) { this(shapeImplementing, 0.0); }

	public AbstractShapeRotated(AbstractShapeRotated s) { this(s.shapeImplementing, s.angleRotation); }

	public AbstractShapeRotated(ShapeRunnersImplemented shapeImplementing, double angleRotation) {
		super(shapeImplementing);
		this.angleRotation = angleRotation;
	}

	protected double angleRotation;

	/**
	 * Returns the angle of the shape's rotation, expressed in degrees, relative to
	 * the center.
	 */
	@Override
	public double getAngleRotation() { return angleRotation; }

	/** See {@link #getAngleRotation()}. */
	@Override
	public AbstractShapeRotated setAngleRotation(double angleRotation) {
		angleRotation %= 360.0;
		if (angleRotation < 0)
			angleRotation += 360.0;
		if (this.angleRotation != angleRotation) {
			this.polygonCache = null;
			this.angleRotation = angleRotation;
		}
		return this;
	}
}