package games.generic.controlModel;

import games.generic.view.GameView;

public abstract class GameLauncher {
	public GameLauncher() {}

	protected GController controller;
	protected GameView view;

	public GController getController() { return controller; }

	public GameView getView() { return view; }

	public abstract GController newController();

	public abstract GameView newView(GController gc);

	public void initGame() {
		GController gc;
		GameView gv;
		gc = this.newController();
		this.controller = gc;
		gc.initNonFinalStuffs();
		gv = this.newView(gc);
		this.view = gv;

		gv.initAndShow();

		gc.loadAll();

		gc.getLogger().log("Game Launcher init complete");
	}

	// and ... the main? On concrete classes.
}