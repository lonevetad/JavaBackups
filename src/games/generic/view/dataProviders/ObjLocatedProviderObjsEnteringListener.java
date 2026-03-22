package games.generic.view.dataProviders;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import games.generic.controlModel.GModality;
import games.generic.controlModel.GObjectsInSpaceManager;
import games.generic.controlModel.events.GEventObserver;
import games.generic.controlModel.events.IGEvent;
import games.generic.view.GameView;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import tools.ObjectNamedID;
import tools.UniqueIDProvider;

/**
 * Instead of simply scanning, just ... deprecated
 *
 * @deprecated 17/01/2022 Don't remember why
 */
@Deprecated
public abstract class ObjLocatedProviderObjsEnteringListener extends ObjLocatedProvider implements GEventObserver {
	protected static final UniqueIDProvider idProvider = UniqueIDProvider.newBasicIDProvider();

	public ObjLocatedProviderObjsEnteringListener(GameView gameView) {
		super(gameView);
		this.ID = idProvider.getNewID();
		eventsListened = new ArrayList<String>();
		eventsListened.add(getIdentifierObjEnteredInGame().getName());
		eventsListened.add(getIdentifierObjRemovedFromGame().getName());
	}

	protected Long ID;
	protected List<String> eventsListened;

	@Override
	public Long getID() { return ID; }

	@Override
	public List<String> getEventsWatching() { // TODO Auto-generated method stub
		return eventsListened;
	}

	//

	public abstract ObjectNamedID getIdentifierObjEnteredInGame();

	public abstract ObjectNamedID getIdentifierObjRemovedFromGame();

	@Override
	public void notifyEvent(GModality modality, IGEvent ge) { // TODO Auto-generated method stub
	}

	@Override
	public void forEachObjInArea(GObjectsInSpaceManager gameObjectInSpaceProvider, AbstractShape2D shape,
			BiConsumer<Point, ObjectLocated> action) {}

}
