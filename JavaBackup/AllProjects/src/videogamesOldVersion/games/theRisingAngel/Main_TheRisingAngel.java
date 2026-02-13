package videogamesOldVersion.games.theRisingAngel;

import java.awt.event.MouseEvent;

import tools.FileUtilities;
import videogamesOldVersion.common.abstractCommon.AbstractPlayer;
import videogamesOldVersion.common.abstractCommon.AbstractSaveData;
import videogamesOldVersion.common.abstractCommon.MainController;
import videogamesOldVersion.common.gui.MainGUI;
import videogamesOldVersion.games.theRisingAngel.main.Loader_TRAn;
import videogamesOldVersion.games.theRisingArmy.guiTRAr.MapEditorTRAr;

public class Main_TheRisingAngel extends MainController// _RealTime
{
	private static final long serialVersionUID = -5103047780087L;

	// GRAPHIC

	// GAME
	boolean canUserInteract;

	//

	// TODO MAIN COSTRUCTOR

	public Main_TheRisingAngel() {
		super(Loader_TRAn.getInstance(), new MainGUI_TRAn());
		MainGUI mgg;
		(mgg = getMainGenericGUI()).setMapEditor(new MapEditorTRAr());
		mgg.setMainController(this);
	}

	//

	// TODO METHODS

	public static void main(String[] args) {
		MainController.main(args, new Main_TheRisingAngel());
	}

	//

	// TODO GETTER
	@Override
	public String getGameName() {
		return "The Rising Angel";
	}

	/*
	 * @Override public MatrixObjectLocationManager getMOLM() { return
	 * getMolmAllObjects(); }
	 */

	public boolean isCanUserInteract() {
		return canUserInteract;
	}

	/*
	 * public MatrixObjectLocationManager getMolmBackground() { return
	 * molmBackground; } public MatrixObjectLocationManager getMolmAllObjects() {
	 * return molmAllObjects; }
	 */
	//

	// TODO SETTER

	public void setCanUserInteract(boolean canUserInteract) {
		this.canUserInteract = canUserInteract;
	}

	/*
	 * public Main_TheRisingArmy setMolmBackground(MatrixObjectLocationManager
	 * molmBackground) { this.molmBackground = molmBackground; return this; }
	 */

	/*
	 * public Main_TheRisingArmy setMolmAllObjects(MatrixObjectLocationManager
	 * molmAllObjects) { this.molmAllObjects = molmAllObjects; return this; }
	 */

	//

	// TODO OTHER METHODS

	@Override
	public void act(int millis) {
	}

	@Override
	public void continueInitNONGUIfields() {
		if (isNeverInitialized()) {
			setNeverInitialized(false);
		}
	}

	public void setCoordinatesMouseOnMapMicropixel(MouseEvent me) {
		setCoordinatesMouseOnMapMicropixel(me.getX(), me.getY());
	}

	public void setCoordinatesMouseOnMapMicropixel(int newxPixelGraphic, int newyPixelGraphic) {
		setXMouseMicropixel(
				(newxPixelGraphic * MainGUI.MICROPIXEL_EACH_TILE) / this.getMainGenericGUI().getMicropixelEachTile());
		setYMouseMicropixel(
				(newyPixelGraphic * MainGUI.MICROPIXEL_EACH_TILE) / this.getMainGenericGUI().getMicropixelEachTile());

		// System.out.println("mouse moved on pixel: (" + newxPixelGraphic + ","
		// + newyPixelGraphic + ")");
	}

	@Override
	public AbstractSaveData newSaveData(AbstractPlayer player) {
		return null;
	}

	@Override
	protected void savePlayerData(StringBuilder errorHolder, String starttextError) {
		String path, error;
		AbstractSaveData sd;
		sd = newSaveData(getPlayer());
		if (sd == null) {
			if (errorHolder.length() == 0)
				errorHolder.append(starttextError);
			errorHolder.append("- SaveData empty");
		}
		path = getLoader().getPathSaves();
		error = FileUtilities.writeObject(sd, path);
		if (error != null) {
			if (errorHolder.length() == 0)
				errorHolder.append(starttextError);
			errorHolder.append("- error while writing:n\t");
			errorHolder.append(error);
		}
	}

	//

	// TODO CLASSES

	/*
	 * static class JFrame_Updating extends JFrame implements
	 * GraphicComponentUpdating { JFrame_Updating(){ super(); } JFrame_Updating(Main
	 * main){ } Main m;
	 *
	 * @Override public void updateValues() { } }
	 */
}