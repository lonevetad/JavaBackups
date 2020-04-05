package geometry.implementations.shapes.subHierarchy;

import geometry.AbstractShape2D;
import geometry.ShapeRunnersImplemented;

public abstract class AbstractShapeFillableImpl extends AbstractShapeImpl implements AbstractFillable {
	private static final long serialVersionUID = 794613059417L;

	public AbstractShapeFillableImpl(ShapeRunnersImplemented shapeImplementing) {
		super(shapeImplementing);
		this.isFilled = false;
	}

	public AbstractShapeFillableImpl(AbstractShapeFillableImpl s) {
		super(s);
		this.isFilled = s.isFilled;
	}

	public AbstractShapeFillableImpl(ShapeRunnersImplemented shapeImplementing, double angleRotation, int xCenter,
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
	public AbstractShapeFillableImpl setFilled(boolean isFilled) {
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