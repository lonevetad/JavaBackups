package games.generic.view.dataProviders;

import java.awt.Point;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import games.generic.controlModel.GModality;
import games.generic.controlModel.GObjectsInSpaceManager;
import games.generic.view.GameView;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import geometry.ObjectShaped;
import geometry.ProviderShapesIntersectionDetector;

/**
 * Iterates over ALL object held into {@link GObjectsInSpaceManager} (obtained
 * through {@link GModality#getGObjectInSpaceManager()}) and, if they are inside
 * the given area, provide them.<br>
 * No repetitions should be provided (it depends on
 * {@link GObjectsInSpaceManager#forEach(Consumer)} implementation).
 */
public class ObjLocatedProviderObjFiltering extends ObjLocatedProvider {

	public ObjLocatedProviderObjFiltering(GameView gameView) { super(gameView); }

	@Override
	public void forEachObjInArea(GObjectsInSpaceManager goism, AbstractShape2D shape,
			BiConsumer<Point, ObjectLocated> action) {
		ProviderShapesIntersectionDetector psid;
//		InSpaceObjectsManager<Double> isom;
		psid = goism.getProviderShapesIntersectionDetector();
		if (psid == null)
			return;
		goism.forEach(owid -> {
			ObjectLocated ol;
			if (owid instanceof ObjectLocated) {
				ol = owid;
				if (ol instanceof ObjectShaped) {
					if (psid.areIntersecting(shape, ((ObjectShaped) ol).getShape()))
						action.accept(ol.getLocation(), ol);
				} else if (shape.contains(ol.getLocation())) { action.accept(ol.getLocation(), ol); }
			}
		});
	}
}
