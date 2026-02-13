package games.theRisingAngel.misc;

import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.CreatureAttributesBonusesCalculator;
import games.generic.controlModel.subimpl.CreatureAttributesBaseAndDerivedCaching;
import games.theRisingAngel.enums.AttributesTRAn;

/**
 * @deprecated Deprecated because in real game the values, due to abilities,
 *             could change frequently over time.
 */
@Deprecated
public class CreatureAttributesTRAn_OptimizeRareChanges extends CreatureAttributesBaseAndDerivedCaching {

	public CreatureAttributesTRAn_OptimizeRareChanges() {
		super(AttributesTRAn.ALL_ATTRIBUTES.length, AttributesTRAn.INDEX_TO_ATTRIBUTE_TRAn);
		super.setBonusCalculator(new CreatureAttributesBonusesCalculatorTRAn_OptimizeRareChanges());

	}

	@Override
	public int getValue(AttributeIdentifier identifier) {
		int v;
		v = super.getValue(identifier);
		return (identifier == AttributesTRAn.Velocity && v <= 0) ? 1 : v; // no less than 1 for velocity
	}

	@Override
	public int getValue(int index) { return this.getValue(AttributesTRAn.ALL_ATTRIBUTES[index]); }

	//

	@Override
	protected int[] newCacheValues(int length) { return null; }

	@Override
	protected void recalculateCache() {
		boolean bcNotNull;
		int i, ac;
		final int[] cv, ov, ama;
		CreatureAttributesBonusesCalculator bc;
		isCacheAvailable = true;
		cv = this.cacheValues;
		ov = super.originalValues;
		ama = this.attributesModificationsApplied;
		ac = attributesCount;
		i = AttributesTRAn.LAST_INDEX_ATTRIBUTE_UPGRADABLE + 1;
		// pre-load attribute upgradables
		while (--i >= AttributesTRAn.FIRST_INDEX_ATTRIBUTE_UPGRADABLE) {
			cv[i] = ov[i] + ama[i];
		}
		// then others
		if (bcNotNull = (bc = this.bonusCalculator) != null) { bc.markCacheAsDirty(); }
		if (bcNotNull) {
			i = AttributesTRAn.LAST_INDEX_ATTRIBUTE_UPGRADABLE;
			while (++i < ac) {
				cv[i] = 0;
			}
			// pre-load attribute upgradables
			i = AttributesTRAn.FIRST_INDEX_ATTRIBUTE_UPGRADABLE;
			while (--i >= 0) {
				cv[i] = 0;
			}
//			Arrays.fill(cv, 0);
			i = ac;
			while (--i >= 0) {// update the values
				cv[i] = ov[i] + ama[i] + bc.getBonusFor(i);
			}
		} else { // update others
			i = -1;
			while (++i < AttributesTRAn.FIRST_INDEX_ATTRIBUTE_UPGRADABLE) {
				cv[i] = ov[i] + ama[i];
			}
			i = AttributesTRAn.FIRST_INDEX_ATTRIBUTE_UPGRADABLE;
			while (++i < ac) {
				cv[i] = ov[i] + ama[i];
			}
		}
//		if (bcNotNull) {
//			i = attributesCount;
//			while (--i >= 0) // update the values
//				this.cacheValues[i] += bc.getBonusForValue(i);
//		}
	}

	@Override
	public String toString() {
		StringBuilder sb;
		sb = new StringBuilder(256);
		System.out.println("TO STRIIIIING");
		sb.append("CreatureAttributesTRAn [\n");
		for (int i = 0, n = getAttributesCount(); i < n; i++)
//			sb.append(getOriginalValue(i)).append(", ");
			sb.append('\t').append(AttributesTRAn.ALL_ATTRIBUTES[i].getName()).append(':').append(this.getValue(i))
					.append('\n');
		return sb.append(']').toString();
	}
}