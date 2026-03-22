package tests.tGame.tgEvent1;

import games.theRisingAngel.events.GEventInterfaceTRAn;
import tests.tGame.tgEvent1.oggettiDesempio.EventPrinter;

public class GEventInterface_E1 extends GEventInterfaceTRAn {

	public void firePrinterEvent(String text) {
		System.out.println("--- firing printer event with: " + text);
		this.getGameEventManager().fireEvent(new EventPrinter(text));
	}
}