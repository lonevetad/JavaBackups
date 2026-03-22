package geometry.pointTools;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.Serializable;

public class PolarPoint extends Point2D implements Serializable {
	private static final long serialVersionUID = 1012964732058L;

	public static PolarPoint fromPoint(Point p) {
		if (p == null)
			return null;
		return (new PolarPoint()).setFromPoint(p);
	}

	public PolarPoint() {
		this(0.0, 0.0);
	}

	public PolarPoint(double radius, double theta) {
		this(true, radius, theta);
	}

	/**
	 * Initialise the polar point thanks to the given parameters. If the first
	 * parameter is <code>true</code>, then the other two parameters are considered
	 * as polar coordinates (i.e.: <code>radius</code> and <code>theta</code>), else
	 * are considered "usual" coordinates (i.e.: <code>x</code> and <code>y</code>).
	 */
	public PolarPoint(boolean arePolar, double radiusOrX, double thetaOrY) {
		this.cacheNotAvailable = true;
		if (arePolar) {
			this.radius = radiusOrX;
			this.theta = thetaOrY;
		} else
			setLocation(radiusOrX, thetaOrY);

	}

	protected boolean cacheNotAvailable;
	protected double radius, theta, sinCache, cosCache, xCache, yCache;

	//

	protected void recalculateCache() {
		double r;
		if (!cacheNotAvailable)
			return;
		if ((r = radius) != 0.0) {
			xCache = r * (sinCache = Math.sin(theta));
//			xCache = r * (sinCache = Math.sin(t = theta));
//			yCache = r * (cosCache = Math.cos(t));
			yCache = r * (cosCache = (1 - sinCache * sinCache));
//			super.setLocation(p);
			cacheNotAvailable = false;
		} else {
			sinCache = xCache = yCache = 0.0;
			cosCache = 1.0;
		}
	}

	//

	@Override
	public double getX() {
		if (cacheNotAvailable)
			recalculateCache();
		return xCache;
	}

	@Override
	public double getY() {
		if (cacheNotAvailable)
			recalculateCache();
		return yCache;
	}

	public double getRadius() {
		return radius;
	}

	public double getTheta() {
		return theta;
	}

	public PolarPoint setRadius(double radius) {
		if (this.radius != radius) {
			this.cacheNotAvailable = true;
			this.radius = radius;
			recalculateCache();
		}
		return this;
	}

	public PolarPoint setTheta(double theta) {
		if (this.theta != theta) {
			this.cacheNotAvailable = true;
			this.theta = theta;
			recalculateCache();
		}
		return this;
	}

	//

	public PolarPoint setFromPoint(Point p) {
		if (p == null)
			return this;
		setLocation(p.getX(), p.getY());
		return this;
	}

	@Override
	public void setLocation(double x, double y) {
		this.cacheNotAvailable = true;
		this.radius = Math.hypot(x, y);
		this.theta = Math.atan2(y, x);
		recalculateCache();
	}

	public Point toPoint() {
		return new Point((int) getX(), (int) getY());
	}
}