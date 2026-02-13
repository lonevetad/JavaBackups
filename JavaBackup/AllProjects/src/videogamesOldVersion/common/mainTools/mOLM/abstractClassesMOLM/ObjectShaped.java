package common.mainTools.mOLM.abstractClassesMOLM;

import common.abstractCommon.Memento;
import common.abstractCommon.referenceHolderAC.ShapeSpecificationHolder;
import common.abstractCommon.shapedObject.AbstractObjectOnCartesianPlan;
import common.mainTools.mOLM.MatrixObjectLocationManager;
import common.mainTools.mOLM.abstractClassesMOLM.AbstractShapeRunners.ShapesImplemented;

/**
 * This class represent an {@link ObjectWithID} with a shape associated.
 */
public abstract class ObjectShaped extends ObjectWithID
		implements AbstractObjectOnCartesianPlan, ShapeSpecificationHolder {
	private static final long serialVersionUID = -111112389499L;

	public ObjectShaped() {
		super();
	}

	public ObjectShaped(ShapeSpecification ss) {
		this();
		this.setShapeSpecification(ss);
	}

	public ObjectShaped(ObjectShaped o) {
		super(o);
		this.setShapeSpecification(o.getShapeSpecification().clone());
	}

	public ObjectShaped(MementoOShaped o) {
		super(o);
	}

	// protected ShapeSpecification shapeSpecification;
	// protected MyObservable

	//

	// TODO GETTER

	// public abstract ShapeSpecification getShapeSpecification(); { return
	// shapeSpecification; }

	@Override
	public int getXCenter() {
		return getShapeSpecification() != null ? getShapeSpecification().getXCenter() : Integer.MIN_VALUE;
	}

	@Override
	public int getYCenter() {
		return getShapeSpecification() == null ? Integer.MIN_VALUE : getShapeSpecification().getYCenter();
	}

	@Override
	public int getXLeftBottom() {
		return getShapeSpecification() == null ? Integer.MIN_VALUE : getShapeSpecification().getXLeftBottom();
	}

	@Override
	public int getYLeftBottom() {
		return getShapeSpecification() == null ? Integer.MIN_VALUE : getShapeSpecification().getYLeftBottom();
	}

	public ShapesImplemented getShape() {
		return getShapeSpecification() == null ? null : getShapeSpecification().getShape();
	}

	public ShapeRunner getShapeRunner() {
		return getShapeSpecification() == null ? null : getShapeSpecification().getShapeRunner();
	}

	//

	// TODO SETTER

	public ObjectShaped setShapeRunner(ShapeRunner shapeRunner) {
		if (getShapeSpecification() != null) {
			getShapeSpecification().setShapeRunner(shapeRunner);
		}
		return this;
	}

	// public abstract ObjectShaped setShapeSpecification(ShapeSpecification
	// shapeSpecification);
	// { this.getShapeSpecification() = getShapeSpecification(); return this; }

	@Override
	public AbstractObjectOnCartesianPlan setXLeftBottom(int x) {
		if (getShapeSpecification() != null) {
			getShapeSpecification().setXLeftBottom(x);
		}
		return this;
	}

	@Override
	public AbstractObjectOnCartesianPlan setYLeftBottom(int y) {
		if (getShapeSpecification() != null) {
			getShapeSpecification().setYLeftBottom(y);
		}
		return this;
	}

	@Override
	public AbstractObjectOnCartesianPlan setLeftBottomCorner(int x, int y) {
		if (getShapeSpecification() != null) {
			getShapeSpecification().setLeftBottomCorner(x, y);
		}
		return this;
	}

	@Override
	public AbstractObjectOnCartesianPlan setXCenter(int x) {
		if (getShapeSpecification() != null) {
			getShapeSpecification().setXCenter(x);
		}
		return this;
	}

	@Override
	public AbstractObjectOnCartesianPlan setYCenter(int y) {
		if (getShapeSpecification() != null) {
			getShapeSpecification().setYCenter(y);
		}
		return this;
	}

	@Override
	public AbstractObjectOnCartesianPlan setCenter(int x, int y) {
		if (getShapeSpecification() != null) {
			getShapeSpecification().setCenter(x, y);
		}
		return this;
	}

	//

	// TODO MOLM OPERATIONS

	// public String addOnMOLM(AbstractMatrixObjectLocationManager molm) {
	// return DoSomethingWithMolmShapespecOwid.ADDER.doOnMolmShapespecOwid(molm,
	// getShapeSpecification(), this);
	// }

	// public String removeOnMOLM(AbstractMatrixObjectLocationManager molm) {
	// return DoSomethingWithMolmShapespecOwid.REMOVER.doOnMolmShapespecOwid(molm,
	// getShapeSpecification(), this);
	// }

	/**
	 * Move this instance (its {@link ShapeSpecification}) to the new location, removing previously
	 * this instance from the given {@link MatrixObjectLocationManager}.
	 * <p>
	 * BEWARE: the {@link ShapeSpecification} instance returned by {@link #getShapeSpecification()}
	 * will be modified !
	 *
	 * @param molm
	 *            {@link MatrixObjectLocationManager} instance.
	 * @param xNewCenterMicropixel
	 *            the x-coordinate of this {@link ShapeSpecification} instance's center, expressed
	 *            in {@link MatrixObjectLocationManager}'s micropixel, ad described in that class'
	 *            documentation.
	 * @param yNewCenterMicropixel
	 *            the y-coordinate, as {@code yNewCenterMicropixel}, described below
	 */
	// public abstract String moveToNewCenterOnMOLM(AbstractMatrixObjectLocationManager molm, int
	// xNewCenterMicropixel,
	// int yNewCenterMicropixel);

	//

	// TODO MEMENTO

	@Override
	public abstract MementoOShaped createMemento();

	/** {@inheritDoc} */
	@Override
	public boolean reloadState(Memento m) {
		MementoOShaped mos;
		if (m != null && m instanceof MementoOShaped) {
			mos = (MementoOShaped) m;
			super.reloadState(mos);
			setShapeSpecification(mos.shapeSpec);
			return true;
		}
		return false;
	}

	// class

	public static abstract class MementoOShaped extends MementoOWID {
		private static final long serialVersionUID = 9034L;
		ShapeSpecification shapeSpec;

		public MementoOShaped() {
			super();
		}

		public MementoOShaped(ObjectShaped o) {
			super(o);
			this.shapeSpec = o.getShapeSpecification();
		}
	}
}