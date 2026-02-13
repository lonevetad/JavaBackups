package videogamesOldVersion.common.abstractCommon;

import java.io.Serializable;

import common.mainTools.mOLM.abstractClassesMOLM.ShapeSpecification;
import videogamesOldVersion.common.EnumElementTileImageFactory;
import videogamesOldVersion.common.GameObjectInMap;
import videogamesOldVersion.common.abstractCommon.behaviouralObjectsAC.GameObjectInMapFactory;
import videogamesOldVersion.common.abstractCommon.behaviouralObjectsAC.TileImageFactory;
import videogamesOldVersion.common.abstractCommon.shapedObject.AbstractObjectRectangleBoxed;
import videogamesOldVersion.common.gui.GameObjectInMapView;
import videogamesOldVersion.common.gui.MainGUI;
import videogamesOldVersion.common.gui.MapGameView;
import videogamesOldVersion.common.gui.TileImage;
import videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM.AbstractMatrixObjectLocationManager;

/**
 * "GOTI" stands for "GameObjectTileImage"<br>
 * This interface is used to mark an object as an element of a
 * <i>enumeration</i> of objects able to create a {@link GameObjectInMap} and
 * its View's part: {@link TileImage}, which contains the images and animations.
 * These instances are combined together to create a {@link GameObjectInMapView}
 * instance, used by graphic environment like {@link MapGameView}.<br>
 *
 * @see AbstractEnumGOTI
 */
public interface AbstractEnumElementGOTI extends Serializable {

	public static AbstractEnumElementGOTI newDefaultInstance() {
		return new EnumElementTileImageFactory();
	}

	public static ShapeSpecification newDefaultShapeSpec() {
		return ShapeSpecification.newRectangle(true, MainController.MICROPIXEL_EACH_TILE,
				MainController.MICROPIXEL_EACH_TILE);
	}

	//

	public boolean isNotSolid();

	public Integer getID();

	public String getName();

	public ShapeSpecification getShapeSpecification();

	public TileImageFactory getTileImageFactory();

	public GameObjectInMapFactory getGameObjectInMapFactory();

	/**
	 * Store as cache a {@link TileImage}. DON'T USE THIS DIRECTLY.<br>
	 * Use {@link #newGameObjectInMap(MainController)} and its overload(s) instead.
	 */
	public TileImage getTileImageCached();

	//

	// TODO SETTER

	public AbstractEnumElementGOTI setNotSolid(boolean isNotSolid);

	public AbstractEnumElementGOTI setID(Integer iD);

	public AbstractEnumElementGOTI setName(String name);

	public AbstractEnumElementGOTI setShapeSpecification(ShapeSpecification shapeSpecification);

	public AbstractEnumElementGOTI setTileImageFactory(TileImageFactory tileImageFactory);

	public AbstractEnumElementGOTI setGameObjectInMapFactory(GameObjectInMapFactory gameObjectInMapFactory);

	/** See {@link #getTileImageCached()}. */
	public AbstractEnumElementGOTI setTileImageCached(TileImage tileImageCached);

	//

	// instances

	/**
	 * Create a <b>new</b> {@link TileImage} and scale its image thanks to the
	 * informations provided with {@link MainController}, in particular
	 * {@link MainController#getMainGenericGUI()}.{@link MainGUI#getPixelEachMicropixel()}..<br>
	 * If there's a jet stored TileImage in cache, then a clone (a <b>new</b> one)
	 * of it is returned.<br>
	 * Otherwise, a <b>new</b> TileImage is created.<br>
	 * WARNING: If resizing the images is needed, resize all of TileImage got
	 * previously and stored in some way, don't use the returned one.
	 */
	public default TileImage newTileImage(MainController main) {
		TileImage ti;
		Integer id;
		String imagename;
		TileImageFactory tif;
		// LoaderGeneric loader;
		// before all, recycle the cache, not caring of the given loader
		ti = getTileImageCached();
		if (ti != null)
			return ti.clone();
		// no cache: try to create it
		id = getID();
		imagename = getName();
		tif = getTileImageFactory();
		if (main != null && id != null && imagename != null && tif != null) {
			// try instantiation
			ti = tif.newEmptyTileImage(main, imagename);
			if (ti != null) {
				ti.setImageName(imagename);// avoiding bugs and miss-set
				ti.setID(id);
				setTileImageCached(ti);
				scaleImageSizeTo(main.getMainGenericGUI().getPixelEachMicropixel());
			}
		}
		return ti;
	}

	/**
	 * Create a <b>new</b> {@link GameObjectInMap} calling
	 * {@link #newGameObjectInMap(MainController, String, ShapeSpecification, boolean)}
	 * and passing the local parameters.
	 */
	public default GameObjectInMap newGameObjectInMap(MainController main) {
		return newGameObjectInMap(main, getName(), getShapeSpecification(), isNotSolid());
	}

	public default GameObjectInMap newGameObjectInMap(MainController main, String name, ShapeSpecification ss,
			boolean isNotSolid) {
		GameObjectInMap goim;
		GameObjectInMapFactory goimf;
		goimf = getGameObjectInMapFactory();
		if (main == null || name == null || ss == null || goimf == null)
			return null;
		goim = goimf.newGameObjectInMap(main, name, ss, isNotSolid);
		if (goim != null) {
			if (getID() != null)
				goim.setIdEnumElement(getID());
			if (name != null)
				goim.setName(name);
			if (ss != null)
				goim.setShapeSpecification(ss);
			if (main != null)
				goim.setMain(main);
		}
		return goim;
	}

	/**
	 * Get all parts (the Model {@link GameObjectInMap} and the View
	 * {@link TileImage}) and pack them in a single wrapper:
	 * {@link GameObjectInMapView}.
	 */
	public default GameObjectInMapView newGameObjectInMapView(MainController main) {
		GameObjectInMapView goimv;
		GameObjectInMap goim;
		TileImage ti;
		goimv = null;
		if (main != null) {
			goim = newGameObjectInMap(main);
			if (goim != null) {
				ti = newTileImage(main);
				if (ti != null)
					goimv = new GameObjectInMapView(main.getMainGenericGUI(), goim, ti);
			}
		}
		return goimv;
	}

	//

	// other

	/**
	 * If and only if there's a image(s) stored (obtained through
	 * {@link #getTileImageCached()}), then that {@link TileImage} will be scaled
	 * depending of its {@link ShapeSpecification} (that shape is expressed id
	 * {@link AbstractMatrixObjectLocationManager}'s <i>micropixel</i> and works
	 * ONLY with {@link AbstractObjectRectangleBoxed} on this default method
	 * implementation) and the given parameter "pixelEachMicropixel": the amount of
	 * real graphical pixels represented by a single <i>micropixel</i>.<br>
	 * The stored image(s) will be scaled ready to be drawn on the View (screen).
	 * <p>
	 * WARNINGS: scaling the stored {@link TileImage} WON'T affect any other
	 * TileImage created through {@link #newGameObjectInMap(MainController)} and its
	 * overload(s), but only the one stored in {@link #getTileImageCached()}. Store
	 * that created TileImages in some way and resize them manually, one per one,
	 * calling {@link TileImage#scaleImages(int, int)}.
	 *
	 * @param pixelEachMicropixel amount of real graphical pixels represented by a
	 *                            single
	 *                            {@link AbstractMatrixObjectLocationManager}'s
	 *                            <i>micropixel</i>. This parameter should be setted
	 *                            with the value returned by
	 *                            {@link MainGUI#getPixelEachMicropixel()}.
	 */
	public default boolean scaleImageSizeTo(int pixelEachMicropixel) {
		boolean scaled;
		ShapeSpecification ss;
		AbstractObjectRectangleBoxed orb;
		TileImage ti;
		scaled = false;
		if (pixelEachMicropixel > 0) {
			ss = getShapeSpecification();
			if (ss != null && ss instanceof AbstractObjectRectangleBoxed) {
				orb = (AbstractObjectRectangleBoxed) ss;
				ti = getTileImageCached();
				if (ti != null) {
					ti.scaleImages(orb.getWidth() * pixelEachMicropixel, orb.getHeight() * pixelEachMicropixel);
				}
			}
		}
		return scaled;
	}
}