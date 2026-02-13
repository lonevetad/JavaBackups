package geometry.pointTools;

import java.awt.Polygon;

import geometry.AbstractObjectsInSpaceManager;
import geometry.AbstractShape2D;

public interface BoundingPolygonExtractor {
	public Polygon getBoundingPolygon(AbstractObjectsInSpaceManager oisp, AbstractShape2D whereToLookFor);
}