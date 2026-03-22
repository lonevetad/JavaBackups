package games.theRisingAngel.misc;

import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.CreatureAttributesBonusesCalculator;
import games.theRisingAngel.enums.AttributesTRAn;

/**
 * See {@link CreatureAttributesBonusesCalculatorTRAn}.
 *
 * @deprecated Deprecated because in real game the values, due to abilities,
 *             could change frequently over time.
 */
@Deprecated
public class CreatureAttributesBonusesCalculatorTRAn_OptimizeRareChanges
		implements CreatureAttributesBonusesCalculator {
	private static final CreatureAttributesBonusesCalculatorTRAn howToUpdateCache = new CreatureAttributesBonusesCalculatorTRAn();

	protected boolean isCacheDirty;
	protected int[] cache;
	protected CreatureAttributes creatureAttributesSet;

	@Override
	public CreatureAttributes getCreatureAttributesSet() { return creatureAttributesSet; }

	@Override
	public void setCreatureAttributesSet(CreatureAttributes creatureAttributesSet) {
		int n, c[];
		this.creatureAttributesSet = creatureAttributesSet;
		if (creatureAttributesSet == null)
			throw new IllegalArgumentException("Creaure Attribute Set is null");
		c = cache = new int[n = creatureAttributesSet.getAttributesCount()];
		while (n-- > 0)// clean memory
			c[n] = 0;
	}

	@Override
	public void markCacheAsDirty() { isCacheDirty = true; }

	@Override
	public int getBonusFor(int index) {
		if (isCacheDirty) { recalculateCache(); }
		return cache[index];
	}

	protected void recalculateCache() {
		int i;
		CreatureAttributes c;
		c = getCreatureAttributesSet();
		if (c == null)
			isCacheDirty = false;
//		cache[AttributesTRAn.Luck.ordinal()] =
		for (AttributesTRAn a : AttributesTRAn.ALL_ATTRIBUTES) {
			i = a.getIndex();
			cache[i] = howToUpdateCache.getBonusFor(i);
		}
	}
}