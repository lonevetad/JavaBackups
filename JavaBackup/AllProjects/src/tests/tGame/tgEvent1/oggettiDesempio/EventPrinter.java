package tests.tGame.tgEvent1.oggettiDesempio;

import games.generic.controlModel.events.GEvent;

public class EventPrinter extends GEvent {
	public static final String PRINTER_EVENT_NAME = "PRINTER";
	protected String text;

	public EventPrinter(String text) {
		super();
		this.text = text;
	}

	//

	public String getText() {
		return text;
	}

	@Override
	public String getName() {
		return PRINTER_EVENT_NAME;
	}

	@Override
	public String toString() {
		return "EventPrinter [ID=" + ID + ", getName()=" + getName() + ", text=" + text + "]";
	}
}