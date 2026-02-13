package games.theRisingArmy.main;

import java.awt.image.BufferedImage;

import common.utilities.Methods;
import videogamesOldVersion.common.GameMechanism;
import videogamesOldVersion.common.abstractCommon.GameMechanismType;
import videogamesOldVersion.common.abstractCommon.MainController;

public class SharedStuffs_TRAr {

	public static final ManaTypes[] MANA_TYPES = ManaTypes.values();
	public static final int MANA_TYPES_LENGTH = MANA_TYPES.length;
	public static final BufferedImage[] MANA_COLORED_IMAGES_ORIGINAL, MANA_UNCOLORED_ORIGINAL;
	private static final BufferedImage[] MANA_UNCOLORED_ORIGINAL_CACHE;
	public static final GameMechanismsTypes[] GAME_MECHANISMS_TYPES_VALUE = GameMechanismsTypes.values();

	private SharedStuffs_TRAr() {}

	//

	// TODO ENUMS

	public static enum ManaTypes {
		Bio("green"), Water("blue"), Fire("red"), Light("white"), Darkness("black"),
		/* Snow("snow"), */Undefined("colorless") {
			@Override
			public BufferedImage getImage(int sizeSquareImage) { return this.getImage(sizeSquareImage, 0); }

			@Override
			public BufferedImage getImage(int sizeSquareImage, int index) {
				BufferedImage temp;
				if (index < 0 || index >= MANA_UNCOLORED_ORIGINAL.length)
					return null;
				temp = null;
				if ((temp = MANA_UNCOLORED_ORIGINAL_CACHE[index]) == null || this.lastSize != sizeSquareImage) {
					if (this.lastSize != sizeSquareImage) {
						this.lastSize = sizeSquareImage;
						cleanUncoloredCache();
					}

					if (temp == null) {
						temp = MANA_UNCOLORED_ORIGINAL[index];
						if (temp == null)
							return null;
						// else : prendiamo quello originale
					} else {
						// la cache va bene, ma .. quello originale ha
						// un buon
						// size? usiamo this.biImageCache come variabile
						// temp
						// per vedere se l'immagine originale ha il
						// giusto size
						this.biImageCache = MANA_UNCOLORED_ORIGINAL[index];
						if (sizeSquareImage == this.biImageCache.getWidth()
								&& sizeSquareImage == this.biImageCache.getHeight())
							// aggiorniamo la cache e via, abbiamo il
							// risultato
							return MANA_UNCOLORED_ORIGINAL_CACHE[index] = this.biImageCache;
					}

					if (sizeSquareImage == temp.getWidth() && sizeSquareImage == temp.getHeight())
						MANA_UNCOLORED_ORIGINAL_CACHE[index] = this.biImageCache = temp;
					else
						MANA_UNCOLORED_ORIGINAL_CACHE[index] = this.biImageCache = temp = Methods
								.scaleImage(MANA_UNCOLORED_ORIGINAL[index], sizeSquareImage, sizeSquareImage);
				}
				return temp;
			}
		};

		//
		public final String colorName;
		public final int index;
		protected BufferedImage biImageCache;
		protected int lastSize;

		ManaTypes(String col) {
			colorName = col;
			index = StaticFields.numberOfFields++;
			biImageCache = null;
			lastSize = 0;
		}

		public BufferedImage getImage(int sizeSquareImage) {
			BufferedImage temp;
			if (biImageCache == null || lastSize != sizeSquareImage) {
				lastSize = sizeSquareImage;
				temp = MANA_COLORED_IMAGES_ORIGINAL[index];
				if (temp == null) {
					biImageCache = null;
				} else {
					if (sizeSquareImage == temp.getWidth() && sizeSquareImage == temp.getHeight())
						biImageCache = temp;
					else
						biImageCache = Methods.scaleImage(temp, sizeSquareImage, sizeSquareImage);
				}
			}
			return biImageCache;
		}

		public BufferedImage getImage(int sizeSquareImage, int index) { return getImage(sizeSquareImage); }

		public static int getNumberOfFields() { return StaticFields.numberOfFields; }

		static class StaticFields {
			static int numberOfFields = 0;
		}
	}

	/**
	 * Any kind of stuffs that a player could put onto the map (except for
	 * ground-tiles):
	 * <ul>
	 * <li>Player</li>
	 * <li>CrystalManaSource</li>
	 * <li>Creature</li>
	 * <li>Artifact</li>
	 * <li>Enchantment</li>
	 * <li>Sorcery</li>
	 * </ul>
	 * The player is a still a Creature (sub-class).
	 */
	public static enum SuperTypePlayableObjectTRAr {
		/** Character */
		Player, CrystalManaSource(), Creature, Artifact, Enchantment, Sorcery(false);

		SuperTypePlayableObjectTRAr() { this(true); }

		SuperTypePlayableObjectTRAr(boolean isPermanent) { this.isPermanent = isPermanent; }

		public final boolean isPermanent;

		public boolean isPermanent() { return isPermanent; }
	}

	public static enum SubTypePlayableObjectTRAr {
		Elf, Goblin, Human, Undead, Zombie, Skeleton, Ghost, Troll, Dwarft, Spirit, Soldier, Knight, Monk, Cleric,
		Pirate//
		, Wizard, Rogue, Dragon, Rat, Cat, Beas, Plant, Angel, Demon, Horror, Griffon, Bird, Werewolf, Wolf, Giant,
		Dijin//
		, Vampire, Golem, Robot, Fish
	}

	public static enum TurnPhasesTRAr {
		Beginning, RechargeAndTimeStep, Drawing, Playing, End
	}

	public static enum GameMechanismsTypes implements GameMechanismType {
		DEFAULT_GAME_MECH(m -> new GameMechanism_TrarDefault(m));

		private static interface GameMechanismFactory {
			public GameMechanism newGM(MainController m);
		}

		GameMechanismsTypes(GameMechanismFactory gmf) { this.gmf = gmf; }

		final GameMechanismFactory gmf;

		@Override
		public GameMechanism newGameMechanism(MainController mainController) { return gmf.newGM(mainController); }
	}

	// fine ENUMS

	//

	// TODO STATIC INITIALIZATION

	static {
		int i, l;
		BufferedImage bi;

		MANA_COLORED_IMAGES_ORIGINAL = new BufferedImage[MANA_TYPES_LENGTH - 1];
		MANA_UNCOLORED_ORIGINAL = new BufferedImage[10];
		MANA_UNCOLORED_ORIGINAL_CACHE = new BufferedImage[10];
		cleanUncoloredCache();

		i = -1;
		l = MANA_COLORED_IMAGES_ORIGINAL.length;
		while (++i < l) {
			bi = Loader_TRAr.readManaSymbol(MANA_TYPES[i]);
			if (bi == null) {
				System.out.println("Cannot load image of " + MANA_TYPES[i].name());
			} else
				MANA_COLORED_IMAGES_ORIGINAL[i] = bi;
		}

		i = -1;
		l = MANA_UNCOLORED_ORIGINAL.length;
		while (++i < l) {
			bi = Loader_TRAr.readManaSymbol(i);
			if (bi == null) {
				System.out.println("Cannot load image of " + ManaTypes.Undefined.name() + ' ' + i);
			} else
				MANA_UNCOLORED_ORIGINAL[i] = bi;
		}
	}

	private static void cleanUncoloredCache() {
		int i, l;
		i = -1;
		l = MANA_UNCOLORED_ORIGINAL_CACHE.length;
		while (++i < l) {
			MANA_UNCOLORED_ORIGINAL_CACHE[i] = null;
		}
	}
}