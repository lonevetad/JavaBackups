package tests.tGame;

import games.generic.controlModel.GController;
import games.generic.controlModel.GameLauncher;
import games.generic.view.GameView;
import tests.tGame.tgEvent1.GC_E1;

public class GL_E1 extends GameLauncher {

	@Override
	public GController newController() {
		return new GC_E1();
	}

	@Override
	public GameView newView(GController gc) {
		return new GView_E1(gc);
	}
}