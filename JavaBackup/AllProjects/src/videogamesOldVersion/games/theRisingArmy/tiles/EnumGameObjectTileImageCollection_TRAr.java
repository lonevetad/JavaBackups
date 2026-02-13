package games.theRisingArmy.tiles;

import common.removed.TileMap;
import games.old.gui.TileImage;
import videogamesOldVersion.common.EnumGameObjectTileImageCollection;
import videogamesOldVersion.games.theRisingArmy.MainController_TheRisingArmy;

/** Contains ALL enums of {@link TileMap} for the game "The Rising Army". */
public class EnumGameObjectTileImageCollection_TRAr extends EnumGameObjectTileImageCollection {
	private static final long serialVersionUID = 1234567890000L;

	public EnumGameObjectTileImageCollection_TRAr() {
		super();
		this.addEnumTileImage(EnumGOTI_TRAr_Nature.getInstance());
		this.addEnumTileImage(EnumGOTI_TRAr_Battlefield.getInstance());
		this.addEnumTileImage(EnumGOTI_TRAr_Creatures.getInstance());
		this.addEnumTileImage(EnumGOTI_TRAr_Magics.getInstance());
		this.addEnumTileImage(EnumGOTI_TRAr_Buildings.getInstance());

		// aggiungere tutte le altre
	}

	public static void main(String[] args) {
		EnumGameObjectTileImageCollection_TRAr t;
		MainController_TheRisingArmy m;

		m = new MainController_TheRisingArmy();
		t = new EnumGameObjectTileImageCollection_TRAr();

		System.out.println("\n\n START \n\n");
		printTM(t.getTileImageByEnumnameImagename(m, EnumGOTI_TRAr_Nature.getInstance().getNameEnum(),
				EnumGOTI_TRAr_Nature.Tiles_TRAr_Nature.GrassFatSimple.getName()));
		// printTM(t.getTileMapInstance(m, 2));
		// printTM(t.getTileMapInstance(m, 7));
		// printTM(t.getTileMapInstance(m, 0));
		// printTM(t.getTileMapInstance(m, 1));
		// printTM(t.getTileMapInstance(m, 12));

		System.out.println("\nAGAIN\n");

		// printTM(t.getTileMapInstance(m, 7));
		// printTM(t.getTileMapInstance(m, 12));
		// printTM(t.getTileMapInstance(m, 2));
		// printTM(t.getTileMapInstance(m, 4));
		// printTM(t.getTileMapInstance(m, 8));
	}

	static void printTM(TileImage t) {
		System.out.println(t == null ? "nulllll" : (t.getImageName() + ", ID: " + t.getID()));
	}
}