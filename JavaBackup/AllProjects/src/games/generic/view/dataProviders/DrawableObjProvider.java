package games.generic.view.dataProviders;

import java.awt.Point;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.events.GEventObserver;
import games.generic.view.DrawableObj;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import tools.Comparators;

/**
 * The core method to be designed is {@link #getDrawableFor(ObjectLocated)}.
 * <p>
 * Provides {@link DrawableObj} in a specific {@link AbstractShape2D} using the
 * support class {@link ObjLocatedProvider} to find what {@link DrawableObj}
 * needs to be painted. <br>
 * The {@link ObjLocatedProvider} is used because those {@link DrawableObj} are
 * linked and identified to the {@link ObjectLocated} (through
 * {@link DrawableObj#getRelatedObject()}), which are in fact obtained through
 * {@link ObjLocatedProvider#forEachObjInArea(AbstractShape2D, java.util.function.Consumer)}.
 * <p>
 * To implement this, could implement also {@link GEventObserver} to observe
 * when an object enters into or leave the <i>space</i> they are located on.
 */
public abstract class DrawableObjProvider {
	public static final Function<DrawableObj, Long> KEY_EXTRACTOR = d -> {
		ObjectLocated ol = d.getRelatedObject();
		return (ol == null) ? null : ol.getID();
	};

	protected DrawableObjProvider(ObjLocatedProvider objLocatedProvider) {
		super();
		this.objLocatedProvider = objLocatedProvider;
	}

	protected ObjLocatedProvider objLocatedProvider;

	//

	//

	public ObjLocatedProvider getObjLocatedProvider() { return objLocatedProvider; }

	public void setObjLocatedProvider(ObjLocatedProvider objLocatedProvider) {
		this.objLocatedProvider = objLocatedProvider;
	}

	/** The core method. Could use a map to achieve it. */
	public abstract DrawableObj getDrawableFor(ObjectLocated ol);

	public void forEachDrawableInArea(AbstractShape2D shape, BiConsumer<Point, DrawableObj> action) {
		ObjLocatedProvider olp;
		olp = getObjLocatedProvider();
		if (olp == null)
			throw new NullPointerException("An instance of ObjLocatedProvider is required");
		olp.forEachObjInArea(shape, (p, ol) -> {
			DrawableObj d;
			// no null check: special actions could be performed using specific objects
			d = getDrawableFor(ol);
			if (ol != null) { action.accept(p, d); }
		});
	}

	public Set<DrawableObj> getDrawablesInArea(AbstractShape2D shape) {
		Set<DrawableObj> s;
		MapTreeAVL<Long, DrawableObj> collected;
		collected = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.LONG_COMPARATOR);
		s = collected.toSetValue(KEY_EXTRACTOR);
		this.forEachDrawableInArea(shape, (p, d) -> {
			if (d != null)
				collected.put(KEY_EXTRACTOR.apply(d), d);
		});
		return s;
	}
}