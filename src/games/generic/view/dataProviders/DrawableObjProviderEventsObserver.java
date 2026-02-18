package games.generic.view.dataProviders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.GModality;
import games.generic.controlModel.events.GEventObserver;
import games.generic.controlModel.events.IGEvent;
import games.generic.controlModel.events.event.EventEnteringOnMap;
import games.generic.controlModel.events.event.EventLeavingMap;
import games.generic.controlModel.misc.uidp.UIDPCollector.UIDProviderLoadedListener;
import games.generic.view.DrawableInstantiator;
import games.generic.view.DrawableObj;
import geometry.ObjectLocated;
import tools.ObjectNamedID;
import tools.UniqueIDProvider;

/**
 * Class that provides {@link DrawableObj}, as described in superclass
 * {@link DrawableObjProvider}, to some purpose.
 *
 * <p>
 * This class relies on a helper class (the second constructor's parameter)
 * which create a {@link DrawableObj} given a {@link ObjectLocated}.
 */
public abstract class DrawableObjProviderEventsObserver extends DrawableObjProvider implements GEventObserver {
	private static final long serialVersionUID = 5641024520851L;

	private static UniqueIDProvider UIDP_DOPEO = null;
	public static final UIDProviderLoadedListener UIDP_LOADED_LISTENER_DOPEO = uidp -> {
		if (uidp != null) { UIDP_DOPEO = uidp; }
	};

	public static UniqueIDProvider getUniqueIDProvider_DOPEO() { return UIDP_DOPEO; }

	//

	/***/
	public DrawableObjProviderEventsObserver(ObjLocatedProvider objLocatedProvider,
			DrawableInstantiator drawableCreator) {
		super(objLocatedProvider);
		this.drawableCreator = Objects.requireNonNull(drawableCreator);
		this.ID = UIDP_DOPEO.getNewID();
		eventsListened = new ArrayList<String>();
		eventsListened.add(getIdentifierObjEnteredInGame().getName());
		eventsListened.add(getIdentifierObjRemovedFromGame().getName());
		drawablePartsForGameObjects = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, ObjectLocated.COMPARATOR);
	}

	protected final Long ID;
	protected final List<String> eventsListened;
	protected Map<ObjectLocated, DrawableObj> drawablePartsForGameObjects;
	protected DrawableInstantiator drawableCreator;

	//

	@Override
	public Long getID() { return ID; }

	@Override
	public List<String> getEventsWatching() { return eventsListened; }

	//

	public abstract ObjectNamedID getIdentifierObjEnteredInGame();

	public abstract ObjectNamedID getIdentifierObjRemovedFromGame();

	public boolean isEventEnteredInGame(IGEvent ge) { return ge instanceof EventEnteringOnMap; }

	public boolean isEventRemovedFromGame(IGEvent ge) { return ge instanceof EventLeavingMap; }

	//

	@Override
	public DrawableObj getDrawableFor(ObjectLocated ol) { return drawablePartsForGameObjects.get(ol); }

	@Override
	public boolean setID(Long newID) { return false; }

	//

	@Override
	public void notifyEvent(GModality modality, IGEvent ge) {
		if (isEventEnteredInGame(ge)) {
			ObjectLocated ol;
			EventEnteringOnMap eeom;
			eeom = (EventEnteringOnMap) ge;
			ol = eeom.getObjectInvolved();
			drawablePartsForGameObjects.put(ol, drawableCreator.apply(ol));
		} else if (isEventRemovedFromGame(ge)) {
			ObjectLocated ol;
			EventLeavingMap elm;
			elm = (EventLeavingMap) ge;
			ol = elm.getObjectInvolved();
			drawablePartsForGameObjects.remove(ol);
		}
	}
}