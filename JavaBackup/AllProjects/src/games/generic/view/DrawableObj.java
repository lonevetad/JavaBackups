package games.generic.view;

import java.awt.Point;
import java.awt.image.BufferedImage;

import games.generic.controlModel.objects.TimedObject;
import geometry.ObjectLocated;

/** Graphic part of a {@link ObjectLocated}. */
public interface DrawableObj {
// TODO un oggetto con forma e Drawable, tipo immagine o GIF

	public ObjectLocated getRelatedObject();

	public void setRelatedObject(ObjectLocated relatedObject);

	/**
	 * Both a static image or an animation (and possibly a 3D object) could have an
	 * image associated. This is that image.
	 */
	public BufferedImage getCurrentImage();

	/**
	 * Returns the location of this object in space, as described by
	 * {@link ObjectLocated} (obtained through {@link #getRelatedObject()}).
	 */
	public default Point getLocation() {
		ObjectLocated ol;
		ol = getRelatedObject();
		return (ol == null) ? null : ol.getLocation();
	}

	/** See {@link #getLocation()}. */
	public default int getX() {
		ObjectLocated ol;
		ol = getRelatedObject();
		return (ol == null) ? 0 : ol.getx();
	}

	/** See {@link #getLocation()}. */
	public default int getY() {
		ObjectLocated ol;
		ol = getRelatedObject();
		return (ol == null) ? 0 : ol.gety();
	}

	/**
	 * During the rendering process (that is probably performed cyclically and
	 * identified by a {@link long}: the first parameter) some images could require
	 * to change or update as time goes on. Similarly to what is specified in
	 * {@link TimedObject}'s documentation, the <i>amount of time elapsed</i> (the
	 * second parameter) could be considered as a <i>sub-unit</i>
	 */
	public BufferedImage progressImageByTime(long cyclePainting, int timeSubunits);

	/** Should not be used */
	public default void drawOn(Drawer drawer) {
		if (drawer != null) { drawer.drawImage(getCurrentImage(), getX(), getY()); }
	}
}