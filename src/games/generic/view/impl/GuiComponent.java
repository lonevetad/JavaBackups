package games.generic.view.impl;

import games.generic.view.GameView;
import games.generic.view.IGuiComponent;

/** Generic gui component */
public abstract class GuiComponent implements IGuiComponent {

	public GuiComponent(GameView view) {
		super();
		this.view = view;
	}

	protected GameView view;

	//

	@Override
	public GameView getView() { return view; }

	@Override
	public void setView(GameView view) { this.view = view; }

}