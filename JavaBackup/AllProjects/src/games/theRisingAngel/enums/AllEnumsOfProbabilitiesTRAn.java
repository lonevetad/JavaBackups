package games.theRisingAngel.enums;

import games.generic.controlModel.misc.IndexableObject;
import games.theRisingAngel.GameOptionsTRAn;
import tools.ObjWithRarityWeight;

/**
 * Gathers ALL enums whose values may appear at random, in order to organize
 * them (for example, load their values upon loading through
 * {@link GameOptionsTRAn}).
 *
 * @author ottin
 *
 */
public enum AllEnumsOfProbabilitiesTRAn implements IndexableObject {
	ItemsRaritiesDefault(RaritiesTRAn.ALL_RARITIES_TRAn), //
//	CreaturesBase(CreatureTypesTRAn.BASE_CREATURE_TYPES_TRAn)
	//
	;

	public static final AllEnumsOfProbabilitiesTRAn[] ALL_ENUMS_OF_PROBABILITIES;
	public static final IndexToObjectBackmapping INDEX_TO_OBJECT_BACKMAP;

	static {
		ALL_ENUMS_OF_PROBABILITIES = AllEnumsOfProbabilitiesTRAn.values();
		INDEX_TO_OBJECT_BACKMAP = (int i) -> ALL_ENUMS_OF_PROBABILITIES[i];
	}

	AllEnumsOfProbabilitiesTRAn(ObjWithRarityWeight[] weightedOutcomes) { this.weightedOutcomes = weightedOutcomes; }

	protected final ObjWithRarityWeight[] weightedOutcomes;

	public ObjWithRarityWeight[] getWeightedOutcomes() { return weightedOutcomes; }

	@Override
	public Long getID() { return (long) this.ordinal(); }

	@Override
	public String getName() { return this.name(); }

	@Override
	public int getIndex() { return this.ordinal(); }

	@Override
	public IndexToObjectBackmapping getFromIndexBackmapping() { return INDEX_TO_OBJECT_BACKMAP; }

	@Override
	public boolean setID(Long ID) { return false; }
}
