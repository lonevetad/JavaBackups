package games.generic.view.misc;

import games.generic.controlModel.loaders.LoaderManager.LoadingObserver;
import games.generic.view.GameView;
import games.generic.view.impl.GuiComponent;

public abstract class LoadingProcessView extends GuiComponent implements LoadingObserver {

	private static final long serialVersionUID = 1L;

	public LoadingProcessView(GameView view) { super(view); }

	// TODO : add a progress bar or something
}