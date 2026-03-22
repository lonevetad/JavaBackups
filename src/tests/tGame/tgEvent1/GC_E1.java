package tests.tGame.tgEvent1;

import games.theRisingAngel.GControllerTRAn;
import tests.tGame.GModality_E1;

public class GC_E1 extends GControllerTRAn {
	public static final String GM_NAME = "TEST";

	public GC_E1() {
		super();
	}

	@Override
	protected void defineGameModalitiesFactories() {
		super.defineGameModalitiesFactories();
		this.getGameModalitiesFactories().put(GM_NAME, (name, gc) -> {
			return new GModality_E1(name, gc);
		});
	}

	@Override
	public void startGame() {
		super.setCurrentGameModality(this.newModalityByName(GM_NAME));
		super.startGame();
	}

	@Override
	public void closeAll() {
//		isAlive = false;
		super.closeAll();
//		this.getCurrentGameModality().closeAll(); // yet done in super
	}

}