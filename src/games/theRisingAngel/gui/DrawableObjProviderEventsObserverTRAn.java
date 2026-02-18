package games.theRisingAngel.gui;

import games.generic.view.DrawableInstantiator;
import games.generic.view.dataProviders.DrawableObjProviderEventsObserver;
import games.generic.view.dataProviders.ObjLocatedProvider;
import games.theRisingAngel.enums.EventsTRAn;
import tools.ObjectNamedID;

public class DrawableObjProviderEventsObserverTRAn extends DrawableObjProviderEventsObserver {
	private static final long serialVersionUID = -154021422000L;

	public DrawableObjProviderEventsObserverTRAn(ObjLocatedProvider objLocatedProvider,
			DrawableInstantiator drawableCreator) {
		super(objLocatedProvider, drawableCreator);
	}

	@Override
	public ObjectNamedID getIdentifierObjEnteredInGame() { return EventsTRAn.ObjectAdded; }

	@Override
	public ObjectNamedID getIdentifierObjRemovedFromGame() { return EventsTRAn.ObjectRemoved; }

}