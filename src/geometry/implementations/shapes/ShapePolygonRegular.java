package geometry.implementations.shapes;

import geometry.AbstractShape2D;
import geometry.ShapeRunnersImplemented;
import geometry.implementations.shapes.subHierarchy.ShapeFillableImpl;

public class ShapePolygonRegular extends ShapeFillableImpl {
	private static final long serialVersionUID = 326178704L;

	public ShapePolygonRegular() {
		this(ShapeRunnersImplemented.PolygonRegularBorder);
	}

	public ShapePolygonRegular(ShapeRunnersImplemented shapeImplementing) {
		super(shapeImplementing == null ? ShapeRunnersImplemented.PolygonRegularBorder : shapeImplementing);
		this.diameter = 0;
		this.cornersAmounts = 0;
	}

	public ShapePolygonRegular(ShapePolygonRegular s) {
		super(s);
		this.diameter = s.diameter;
		this.cornersAmounts = s.cornersAmounts;
	}

	public ShapePolygonRegular(double angleRotation, int x, int y, boolean isFilled, int diameter, int cornersAmounts) {
		super(ShapeRunnersImplemented.PolygonRegularBorder, angleRotation, x, y, isFilled);
		if (diameter < 0)
			throw new IllegalArgumentException("Radius cannot be negative: " + diameter);
		this.diameter = diameter;
		this.cornersAmounts = cornersAmounts < 0 ? 0 : cornersAmounts;
	}

	protected int diameter, cornersAmounts;

	@Override
	public int getDiameter() {
		return diameter;
	}

	@Override
	public boolean isRegular() {
		return true;
	}

	@Override
	public int getCornersAmounts() {
		return cornersAmounts;
	}

	@Override
	public ShapePolygonRegular setDiameter(int diameter) {
		if (diameter >= 0) {
			if (this.diameter != diameter)
				this.polygonCache = null;
			this.diameter = diameter;
		}
		return this;
	}

	@Override
	public AbstractShape2D setCornersAmounts(int cornersAmounts) {
		if (cornersAmounts < 0)
			cornersAmounts = 0;
		if (this.cornersAmounts != cornersAmounts)
			this.polygonCache = null;
		this.cornersAmounts = cornersAmounts;
		return this;
	}

	@Override
	public AbstractShape2D clone() {
		return new ShapePolygonRegular(this);
	}

}