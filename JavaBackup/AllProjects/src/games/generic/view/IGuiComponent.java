package games.generic.view;

import games.generic.controlModel.GModality;
import games.generic.controlModel.items.InventoryItems;
import games.generic.controlModel.loaders.LoaderManager.LoadingObserver;
import games.generic.controlModel.player.UserAccountGeneric;

/**
 * A generic GUI (Graphic User Interface) component.<br>
 * It may be a simple text field, a button, the game camera, an
 * {@link InventoryItems} view, a settings panel, etc.<br>
 * This class implementation should not rely by its own on one or more GUI
 * packages, for example {@link javax.swing.JComponent}, but its further
 * implementation should do it in order to change the package in a easier way.
 * <p>
 * NOTE: if this class requires to show something that can't be pre-defined (or
 * defined in a static way), like a list of {@link UserAccountGeneric} saved
 * somewhere, then this class implementation should implement
 * {@link LoadingObserver}.
 *
 * @author ottin
 *
 */
public interface IGuiComponent {

	public GameView getView();

	public void setView(GameView view);

	public default GModality getGameModality() { return getView().getCurrentModality(); }

	/** Perform an action upon adding this component on the given view. */
	public void onAddingOnView(GameView view);

	/** The reverse of {@link #onAddingOnView(GameView)}. */
	public void onRemovingOnView(GameView view);
}