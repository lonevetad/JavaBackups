package games.generic.controlModel.holders;

import games.generic.controlModel.items.EquipmentItem;
import tools.WeightedSetOfRandomOutcomes;

/**
 * An object, like an {@link EquipmentItem}'s ability, could be created randomly
 * (like those equipments abilities, that could be added randomly to random
 * objects, but some abilities could be stronger or weaker, so they could be
 * much or less rare than others).<br>
 * The value (usually positive) returned by {@link #getRarityIndex()} represents
 * this concept and (probably) the rarity index.
 * <p>
 * Could be used with the {@link WeightedSetOfRandomOutcomes}.
 */
public interface RarityHolder {
	/**
	 * Value that can be used to identify the "null value" of a rarity index.
	 */
	public static final int NO_RARITY_INDEX = -1;

	/** That's the INDEX of the rarity, like "common / rare / legendary". */
	public int getRarityIndex();

	public RarityHolder setRarityIndex(int rarityIndex);
}