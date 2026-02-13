package games.theRisingAngel.loaders.factories;

import java.util.Arrays;

import games.generic.controlModel.GModality;
import games.generic.controlModel.items.EquipmentUpgrade;
import games.generic.controlModel.misc.AttributeModification;
import games.generic.controlModel.misc.Currency;
import games.generic.controlModel.misc.CurrencySet;
import games.generic.controlModel.misc.FactoryObjGModalityBased;
import games.generic.controlModel.subimpl.EquipmentUpgradeImpl;

/**
 * A factory for {@link EquipmentUpgrade}.
 *
 * @author ottin
 *
 */
public class FactoryEquipUpgrade implements FactoryObjGModalityBased<EquipmentUpgrade> {
	public int rarity;
	public int[] bonusPriceSell = null;
	public String name, description = null;
	public AttributeModification[] attrMods = null;

	public FactoryEquipUpgrade() { super(); }

	@Override
	public EquipmentUpgrade newInstance(GModality gm) {
		EquipmentUpgrade eu;
		eu = new EquipmentUpgradeImpl(rarity, name);
		if (description != null) { eu.setDescription(description); }
		if (attrMods != null) {
			for (AttributeModification am : attrMods)
				eu.addAttributeModifier(am);
		}
		if (bonusPriceSell != null) {
			int n;
			CurrencySet cs;
			Currency[] currencies;
			cs = gm.newCurrencyHolder();
			currencies = cs.getCurrencies();
			cs.setGameModaliy(gm); // not needed
			n = bonusPriceSell.length;
			while (--n >= 0) {
				cs.setCurrencyAmount(currencies[n], bonusPriceSell[n]);
			}
			eu.setPricesModifications(cs);
		}
		return eu;
	}

	@Override
	public String toString() {
		return "FactoryEquipUpgrade [\n name=" + name + ",\n rarity=" + rarity + ",\n prices: "
				+ Arrays.toString(bonusPriceSell) + ",\n attrMods=\n\t" + Arrays.toString(attrMods) + "]";
	}
}