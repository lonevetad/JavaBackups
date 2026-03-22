package games.generic.view;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import geometry.pointTools.PolygonUtilities;

/**
 * Adapter and Proxy patterns implementation.<br>
 * Defines a way to draw {@link Drawable} stuffs, wrapping other classes like
 * {@link Graphics} or {@link BufferedImage} for example.
 * <p>
 * NOTE: <i>usually</i> coordinates are to be considered as <i>origin is at the
 * <code>top-left</code> corner</i>, <b>but</b> the
 * {@link #drawDrawable(DrawableObj)} (and its derivated:
 * {@link #drawDrawable(DrawableObj, int, int)}) considers the coordinates as
 * <b><code>about the center</code></b>.
 *
 * @author ottin
 */
public interface Drawer {
//	public Point getLeftTopCornerCameraLocation() // to be moved in a class using this one

	/** Coordinates are top-left. */
	public void drawImage(Image im, int x, int y);

	public void drawString(String str, int x, int y);

	public default void drawRectangle(Rectangle rect) { drawRectangle(rect, rect.x, rect.y); }

	public void drawRectangle(Rectangle rect, int x, int y);

//	public default void drawDrawable(DrawableObj drawable) { drawDrawable(drawable, drawable.getX(), drawable.getY()); }

	public default void drawDrawable(DrawableObj drawable) {
		drawImage(drawable.getCurrentImage(), drawable.getX(), drawable.getY());
	}

	public default void drawDrawable(DrawableObj drawable, int x, int y) {
		drawImage(drawable.getCurrentImage(), x, y);
	}

	public void drawLine(int x1, int y1, int x2, int y2);

	public default void drawPolygon(Polygon polygon) {
		PolygonUtilities.forEachEdge(polygon, (p1, p2) -> drawLine(p1.x, p1.y, p2.x, p2.y));
	}
}