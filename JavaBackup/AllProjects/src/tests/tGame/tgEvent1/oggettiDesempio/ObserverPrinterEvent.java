package tests.tGame.tgEvent1.oggettiDesempio;

import java.util.ArrayList;
import java.util.List;

import games.generic.controlModel.GModality;
import games.generic.controlModel.events.GEventObserver;
import games.generic.controlModel.events.IGEvent;
import games.generic.controlModel.objects.GameObjectGeneric;
import tools.UniqueIDProvider;

public class ObserverPrinterEvent implements GEventObserver, GameObjectGeneric {
	protected Long ID;
	protected List<String> eventsWatching;

	public ObserverPrinterEvent() {
		this.ID = UniqueIDProvider.UDIP_GENERAL.getNewID();
		this.eventsWatching = new ArrayList<>(2);
		this.eventsWatching.add(EventPrinter.PRINTER_EVENT_NAME);
	}

	@Override
	public void notifyEvent(GModality modality, IGEvent ge) {
		EventPrinter ep;
		ep = (EventPrinter) ge;
		System.out.println("ObsPE receives a message: " + ep.getText());
	}

	@Override
	public Long getID() { return ID; }

	@Override
	public List<String> getEventsWatching() { return this.eventsWatching; }

	@Override
	public String getName() { return "ObserverPrinterEvent"; }

	@Override
	public void onAddedToGame(GModality gm) {}

	@Override
	public void onRemovedFromGame(GModality gm) {}
}