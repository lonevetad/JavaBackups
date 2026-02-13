package games.theRisingAngel.enums;

import java.util.ArrayList;
import java.util.List;

import games.generic.controlModel.misc.uidp.UIDPCollector.UIDProviderLoadedListener;
import games.theRisingAngel.creatures.CreatureTypeTRAn;
import tools.UniqueIDProvider;

/**
 * All available creature types in the game.
 * <p>
 * In order to define a <i>mod</i>, use {@link #ALL_CREATURE_TYPES_TRAn} instead
 * of {@link Enum#values()}.
 * <p>
 * NOTE: all "mods" that wants to extend the set of creatures HAS to use
 * {@link #getUniqueIDProvider_CreatureTypesTRAn()}.
 *
 * @author ottin
 *
 */
public enum CreatureTypesTRAn implements CreatureTypeTRAn {
	Weirdo, Genomorph, Human, Goblin, Elf, Dwarf, Giant, Ent;

	private static UniqueIDProvider UIDP_CREATURE_TYPES_TRAn = null;
	public static final UIDProviderLoadedListener UIDP_LOAD_LISTENER_CREATURE_TYPES_TRAn;

	/**
	 * It's a mutable list because it's designed to be expanded (<b>and used instead
	 * of {@link #values()} </b>)
	 */
	public static final List<CreatureTypeTRAn> ALL_CREATURE_TYPES_TRAn;
	public static final CreatureTypesTRAn[] BASE_CREATURE_TYPES_TRAn;
	public static final IndexToObjectBackmapping INDEX_TO_CREATURE_TYPE_TRAn;
	static {

		BASE_CREATURE_TYPES_TRAn = CreatureTypesTRAn.values();
		ALL_CREATURE_TYPES_TRAn = new ArrayList<>(BASE_CREATURE_TYPES_TRAn.length);
		INDEX_TO_CREATURE_TYPE_TRAn = ALL_CREATURE_TYPES_TRAn::get;

		UIDP_LOAD_LISTENER_CREATURE_TYPES_TRAn = uidp -> {
			UIDP_CREATURE_TYPES_TRAn = uidp;
			for (CreatureTypesTRAn resource : BASE_CREATURE_TYPES_TRAn) {
				resource.ID = UIDP_CREATURE_TYPES_TRAn.getNewID();
				ALL_CREATURE_TYPES_TRAn.add(resource);
			}
		};
	}

	public static UniqueIDProvider getUniqueIDProvider_CreatureTypesTRAn() { return UIDP_CREATURE_TYPES_TRAn; }

	//

	protected Long ID;

	@Override
	public Long getID() { return ID; }

	@Override
	public String getName() { return name(); }

	@Override
	public int getIndex() { return ordinal(); }

	@Override
	public IndexToObjectBackmapping getFromIndexBackmapping() { return INDEX_TO_CREATURE_TYPE_TRAn; }
}