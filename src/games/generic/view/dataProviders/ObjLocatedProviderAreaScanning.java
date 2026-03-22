package games.generic.view.dataProviders;

import java.awt.Point;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import dataStructures.MapTreeAVL;
import dataStructures.isom.NodeIsom;
import games.generic.controlModel.GObjectsInSpaceManager;
import games.generic.controlModel.objects.ObjectInSpace;
import games.generic.view.GameView;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import tools.Comparators;

/**
 * Scan the WHOLE area searching for object and prove them.<br>
 */
public class ObjLocatedProviderAreaScanning extends ObjLocatedProvider {

	public ObjLocatedProviderAreaScanning(GameView gameView) {
		super(gameView);
		objectPainted = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.LONG_COMPARATOR);
	}

	protected Map<Long, ObjectInSpace> objectPainted;

	@Override
	public void forEachObjInArea(GObjectsInSpaceManager goism, AbstractShape2D shape,
			BiConsumer<Point, ObjectLocated> action) {
		final Map<Long, ObjectInSpace> m;
		Consumer<ObjectLocated> oisPainter;
//		InSpaceObjectsManager<Double> isom;
		if (goism == null)
			return;
//		isom = goism.getOIMManager();
//		isom.

		// * NOTE: beware of repetitions!
		(m = this.objectPainted).clear();
		oisPainter = ol -> {
			Long id;
			ObjectInSpace ois;
			ois = (ObjectInSpace) ol;
			id = ol.getID();
			if (!m.containsKey(id)) {
				m.put(id, ois);
				action.accept(ois.getLocation(), ois);
			}
		};
		goism.runOnShape(shape, p -> {
			NodeIsom<Double> n;
			n = goism.getNodeAt(p);
			if (n == null)
				return;
			n.forEachHeldObject(oisPainter);
		});
	}

}