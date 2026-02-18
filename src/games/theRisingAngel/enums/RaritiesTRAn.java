package games.theRisingAngel.enums;

import java.util.Comparator;
import java.util.Random;

import games.generic.controlModel.holders.RarityHolder;
import games.generic.controlModel.misc.IndexableObject;
import tools.Comparators;
import tools.ObjWithRarityWeight;
import tools.WeightedSetOfRandomOutcomes;

public enum RaritiesTRAn implements RarityHolder, ObjWithRarityWeight, IndexableObject {
	Scrap(200), Common(550), Good(280), Awesome(150), Rare(60), Epic(25), Legendary(10);

	public static final RaritiesTRAn[] ALL_RARITIES_TRAn;
	public static final IndexToObjectBackmapping INDEX_TO_RARITY_TRAn;
	public static final Comparator<RaritiesTRAn> COMPARATOR_RARITY_TRAn;
	private static WeightedSetOfRandomOutcomes RANDOM_WEIGTHED_INDEXES;

	static {
		RANDOM_WEIGTHED_INDEXES = null;
		ALL_RARITIES_TRAn = RaritiesTRAn.values();
		INDEX_TO_RARITY_TRAn = (int i) -> ALL_RARITIES_TRAn[i];
		COMPARATOR_RARITY_TRAn = (r1, r2) -> {
			if (r1 == r2) { return 0; }
			if (r1 == null) { return -1; }
			if (r2 == null) { return 1; }
			return Comparators.LONG_COMPARATOR.compare(r1.getID(), r2.getID());
		};
	}

	RaritiesTRAn() { this(0); }

	RaritiesTRAn(int h) { setRarityWeight(h); }

	protected int rarityWeight;

	@Override
	public int getRarityIndex() { return ordinal(); }

	@Override
	public RarityHolder setRarityIndex(int rarityIndex) { return this; }

	@Override
	public int getRarityWeight() { return this.rarityWeight; }

	@Override
	public ObjWithRarityWeight setRarityWeight(int weight) {
		this.rarityWeight = weight > 0 ? weight : 0;
		return this;
	}

	@Override
	public boolean setID(Long ID) { return false; }

	//

	private static void checkAndReinstanceRWI(Random r) {
		if (RANDOM_WEIGTHED_INDEXES == null) {
			RANDOM_WEIGTHED_INDEXES = new WeightedSetOfRandomOutcomes(RaritiesTRAn.values(), r);
		}
	}

	private static void checkAndReinstanceRWI(long seed) {
		if (RANDOM_WEIGTHED_INDEXES == null) {
			RANDOM_WEIGTHED_INDEXES = new WeightedSetOfRandomOutcomes(RaritiesTRAn.values(), new Random(seed));
		}
	}

	public static WeightedSetOfRandomOutcomes toWeightedIndexes() {
		checkAndReinstanceRWI(new Random());
		return RANDOM_WEIGTHED_INDEXES;
	}

	public static WeightedSetOfRandomOutcomes toWeightedIndexes(long seed) {
		checkAndReinstanceRWI(seed);
		return RANDOM_WEIGTHED_INDEXES;
	}

	public static WeightedSetOfRandomOutcomes toWeightedIndexes(Random r) {
		checkAndReinstanceRWI(r);
		return RANDOM_WEIGTHED_INDEXES;
	}

	@Override
	public Long getID() { return (long) this.ordinal(); }

	@Override
	public String getName() { return this.name(); }

	@Override
	public int getIndex() { return this.ordinal(); }

	@Override
	public IndexToObjectBackmapping getFromIndexBackmapping() { return INDEX_TO_RARITY_TRAn; }
}