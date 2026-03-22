package games.generic.view.dataProviders;

import java.awt.Point;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import dataStructures.MapTreeAVL;
import dataStructures.isom.InSpaceObjectsManager;
import games.generic.controlModel.GModality;
import games.generic.controlModel.GObjectsInSpaceManager;
import games.generic.view.GameView;
import games.generic.view.drawers.IsomPainter;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import geometry.implementations.shapes.ShapeRectangle;
import tools.Comparators;

/**
 * Class able to provide all {@link ObjectLocated} (and the {@link Point} in
 * space where it's located, probably its center, as described by that class)
 * held by a {@link InSpaceObjectsManager} (provided by
 * {@link GModality#getGObjectInSpaceManager()}) and present in some
 * {@link AbstractShape2D} (usually it's the camera: a {@link ShapeRectangle},
 * as defined in {@link IsomPainter}).
 * <p>
 * And, for those who are wondering: yes, it's NOT a IGuiComponent subclass, it
 * just shares the gameView field.
 */
public abstract class ObjLocatedProvider {

	public ObjLocatedProvider(GameView gameView) { this.gameView = gameView; }

	protected GameView gameView;

	//

	public GameView getGameView() { return gameView; }

	public void setGameView(GameView gameView) { this.gameView = gameView; }

	/**
	 * See {@link #forEachObjInArea(AbstractShape2D, Consumer)}, with the difference
	 * that uses the helper class {@link GObjectsInSpaceManager} that, usually,
	 * provides the {@link ObjectLocated} that this class iterates on.
	 */
	public abstract void forEachObjInArea(GObjectsInSpaceManager gameObjectInSpaceProvider, AbstractShape2D shape,
			BiConsumer<Point, ObjectLocated> action);

	/**
	 * The core method.<br>
	 * Should be preferred over the method that is, by default, called:
	 * {@link #forEachObjInArea(GObjectsInSpaceManager, AbstractShape2D, Consumer)}.
	 * <p>
	 * The possible implementations could be:
	 * <ul>
	 * <li>{@link ObjLocatedProviderAreaScanning}</li>
	 * <li>{@link ObjLocatedProviderObjFiltering}</li>
	 * </ul>
	 */
	public void forEachObjInArea(AbstractShape2D shape, BiConsumer<Point, ObjectLocated> action) {
		GModality gm;
		GObjectsInSpaceManager goism;
		gm = this.gameView.getCurrentModality();
		if (gm == null)
			return;
		goism = gm.getGObjectInSpaceManager();
		if (goism == null)
			return;
		forEachObjInArea(goism, shape, action);
	}

	public Set<ObjectLocated> getObjInArea(AbstractShape2D shape) {
		Set<ObjectLocated> s;
		MapTreeAVL<Long, ObjectLocated> collected;
		collected = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.LONG_COMPARATOR);
		s = collected.toSetValue(ObjectLocated.KEY_EXTRACTOR);
		this.forEachObjInArea(shape, (p, ol) -> { if (ol != null) { collected.put(ol.getID(), ol); } });
		return s;
	}
}