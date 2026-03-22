package geometry.implementations;

import java.awt.Point;

import geometry.AbstractShape2D;
import geometry.pointTools.PointConsumer;

public abstract class AbstractPointConsumerInShape implements PointConsumer {
	private static final long serialVersionUID = -379589480023L;

	public AbstractPointConsumerInShape() {
		this(null);
	}

	public AbstractPointConsumerInShape(AbstractShape2D shape) {
		super();
		this.shape = shape;
	}

	protected AbstractShape2D shape;

	//

	public AbstractShape2D getShape() {
		return shape;
	}

	public AbstractPointConsumerInShape setShape(AbstractShape2D shape) {
		this.shape = shape;
		return this;
	}

	@Override
	public final void accept(Point p) {
		if (shape == null || shape.contains(p))
			acceptImpl(p);
	}

	public abstract void acceptImpl(Point ps);
}