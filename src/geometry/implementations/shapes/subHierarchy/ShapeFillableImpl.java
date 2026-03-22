package geometry.implementations.shapes.subHierarchy;

import geometry.AbstractShape2D;
import geometry.ShapeRunnersImplemented;

public abstract class ShapeFillableImpl extends AbstractShapeImpl implements ShapeFillable {
	private static final long serialVersionUID = 794613059417L;

	public ShapeFillableImpl(ShapeRunnersImplemented shapeImplementing) {
		super(shapeImplementing);
		this.isFilled = false;
	}

	public ShapeFillableImpl(ShapeFillableImpl s) {
		super(s);
		this.isFilled = s.isFilled;
	}

	public ShapeFillableImpl(ShapeRunnersImplemented shapeImplementing, double angleRotation, int xCenter,
			int yCenter, boolean isFilled) {
		super(shapeImplementing, angleRotation, xCenter, yCenter);
		this.isFilled = isFilled;
	}

	protected boolean isFilled;

	@Override
	public boolean isFilled() {
		return isFilled;
	}

	@Override
	public ShapeFillableImpl setFilled(boolean isFilled) {
		this.isFilled = isFilled;
		return this;
	}

	/**
	 * NOTE to users: just modifies this instance and returns itself. Cache the
	 * previous value of {@link #isFilled()} and restores it after the use.
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public AbstractShape2D toBorder() {
		this.isFilled = false;
		return this;
	}
}