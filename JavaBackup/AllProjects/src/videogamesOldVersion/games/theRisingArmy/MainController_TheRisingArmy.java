package games.theRisingArmy;

import common.EnumGameObjectTileImageCollection;
import common.abstractCommon.AbstractSaveData;
import common.abstractCommon.GameModelGeneric;
import common.abstractCommon.MainController;
import games.theRisingArmy.guiTRAr.MainGUI_TRAr;
import games.theRisingArmy.main.GameModelTRAr;
import games.theRisingArmy.main.Loader_TRAr;
import games.theRisingArmy.tiles.EnumGameObjectTileImageCollection_TRAr;

public class MainController_TheRisingArmy extends MainController// _RealTime
{
	private static final long serialVersionUID = -5103047780087L;

	// GRAPHIC

	// GAME
	boolean canUserInteract;

	//

	// TODO MAIN COSTRUCTOR

	public MainController_TheRisingArmy() {
		super(Loader_TRAr.getInstance(), new MainGUI_TRAr());
	}

	//

	// TODO METHODS

	//

	// TODO GETTER

	@Override
	public String getGameName() {
		return "The Rising Angel";
	}

	public boolean isCanUserInteract() {
		return canUserInteract;
	}

	/*
	 * @Override public MatrixObjectLocationManager getMOLM() { return
	 * getMolmAllObjects(); }
	 */

	/*
	 * public MatrixObjectLocationManager getMolmBackground() { return
	 * molmBackground; } public MatrixObjectLocationManager getMolmAllObjects()
	 * { return molmAllObjects; }
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

	@Override
	public AbstractSaveData newSaveData(GameModelGeneric gameModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EnumGameObjectTileImageCollection newEnumGameObjectTileImageCollection(MainController mainController) {
		return new EnumGameObjectTileImageCollection_TRAr();
	}

	@Override
	public GameModelGeneric newGameModelGeneric(MainController mainController) {
		return (mainController instanceof MainController_TheRisingArmy)
				? new GameModelTRAr((MainController_TheRisingArmy) mainController) : null;
	}

	//

	// TODO CLASSES

	/*
	 * static class JFrame_Updating extends JFrame implements
	 * GraphicComponentUpdating { JFrame_Updating(){ super(); }
	 * JFrame_Updating(Main main){ } Main m;
	 *
	 * @Override public void updateValues() { } }
	 */

	//

	//

	public static void main(String[] args) {
		MainController.main(args, new MainController_TheRisingArmy());
	}

}