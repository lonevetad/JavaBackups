package geometry;

import java.io.Serializable;

/**
 * More abstract and compact version of {@link ShapeRunnersImplemented}, making
 * no distinction to borders (perimeters) and areas.
 */
public enum ShapesImplemented implements Serializable {
	/** That means: "unknown, nothing in particular, an abstract shape". */
	Point, Line, Rectangle, Circle, Triangle, Polygon;
}