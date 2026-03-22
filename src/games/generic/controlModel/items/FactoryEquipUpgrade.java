package games.generic.controlModel.items;

import games.generic.controlModel.GModality;
import games.generic.controlModel.attributes.AttributeModification;
import games.generic.controlModel.currency.Currency;
import games.generic.controlModel.currency.CurrencySet;
import games.generic.controlModel.misc.FactoryObjGModalityBased;

/**
 * A factory for {@link IEquipmentUpgrade}.
 *
 * @author ottin
 *
 */
public abstract class FactoryEquipUpgrade implements FactoryObjGModalityBased<IEquipmentUpgrade> {
	/*
	 * public int rarity; public int[] bonusPriceSell = null; public String name,
	 * description = null; public AttributeModification[] attrMods = null;
	 */
	public IEquipmentUpgrade prototype;

	public FactoryEquipUpgrade() {
		super();
	}

	protected abstract IEquipmentUpgrade newEquipmentUpgrade(String equipUpgradeName, int rarityIndex);

	@Override
	public IEquipmentUpgrade newInstance(GModality gm) {
		IEquipmentUpgrade eu;
		eu = this.newEquipmentUpgrade(prototype.getName(), prototype.getRarityIndex());
		if (prototype.getDescription() != null) {
			eu.setDescription(prototype.getDescription());
		}
		if (prototype.getAttributesModifiers() != null) {
			for (AttributeModification am : prototype.getAttributesModifiers()) {
				eu.addAttributeModifier(am.deepClone());
			}
		}
		if (prototype.getPricesModifications() != null) {
			int n;
			CurrencySet cs, csPrototype;
			Currency[] currencies;
			csPrototype = prototype.getPricesModifications();
			cs = gm.getGameObjectsProvider().newCurrencyHolder();
			currencies = cs.getCurrencies();
			cs.setGameModaliy(gm); // not needed
			n = csPrototype.getCurrencies().length;
			while (--n >= 0) {
				cs.setCurrencyAmount(currencies[n], cs.getCurrencyAmount(currencies[n]));
			}
			eu.setPricesModifications(cs);
		}
		eu.setIsPrefix(prototype.isPrefix());
		eu.setUpgradeCategory(prototype.getUpgradeCategory());
		return eu;
	}

	/*
	 * @Override public String toString() { return "FactoryEquipUpgrade [\n name=" +
	 * name + ",\n rarity=" + rarity + ",\n prices: " +
	 * Arrays.toString(bonusPriceSell) + ",\n attrMods=\n\t" +
	 * Arrays.toString(attrMods) + "]"; }
	 */
}