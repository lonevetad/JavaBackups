package games.generic.controlModel.subimpl;

import java.util.SortedSet;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.attributes.AttributeModification;
import games.generic.controlModel.currency.CurrencySet;
import games.generic.controlModel.holders.RarityHolder;
import games.generic.controlModel.items.EquipmentItem;
import games.generic.controlModel.items.IEquipmentUpgrade;
import games.generic.controlModel.items.IEquipmentUpgradeCategory;
import games.theRisingAngel.enums.EquipmentUpgradeCategory;
import tools.ClosestMatch;
import tools.Comparators;

public abstract class EquipmentUpgradeImpl implements IEquipmentUpgrade {
	private static final long serialVersionUID = 780874070330924608L;

	public EquipmentUpgradeImpl(int rarityIndex, String name) {
		super();
		this.rarityIndex = rarityIndex;
		this.name = name;
		this.backMapAttrMods = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.STRING_COMPARATOR);
		this.attributesModifiers = backMapAttrMods.toSetValue(AttributeModification.KEY_EXTRACTOR);
		this.isPrefix = false;
		this.description = null;
		this.equipmentUpgradeCategory = null;
	}

	protected boolean isPrefix;
	protected int rarityIndex;
	protected String name, description;
	protected EquipmentUpgradeCategory equipmentUpgradeCategory;
	protected final MapTreeAVL<String, AttributeModification> backMapAttrMods;
	protected final SortedSet<AttributeModification> attributesModifiers;
	protected EquipmentItem equipmentAssigned;
	protected CurrencySet priceModifications;

	@Override
	public SortedSet<AttributeModification> getAttributesModifiers() {
		return this.attributesModifiers;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isPrefix() {
		return this.isPrefix;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public int getRarityIndex() {
		return rarityIndex;
	}

	@Override
	public EquipmentItem getEquipmentAssigned() {
		return equipmentAssigned;
	}

	@Override
	public CurrencySet getPricesModifications() {
		return priceModifications;
	}

	@Override
	public IEquipmentUpgradeCategory getUpgradeCategory() {
		return this.equipmentUpgradeCategory;
	}

	@Override
	public void setUpgradeCategory(IEquipmentUpgradeCategory upgradeCategory) {
		if (!(upgradeCategory instanceof EquipmentUpgradeCategory)) {
			throw new IllegalArgumentException(
					"Wrong instance of upgradeCategory: should be a EquipmentUpgradeCategory, but it's a: "
							+ (upgradeCategory == null ? "null" : upgradeCategory.getClass().getName()));
		}
		this.equipmentUpgradeCategory = (EquipmentUpgradeCategory) upgradeCategory;
	}

	//

	@Override
	public void setIsPrefix(boolean flag) {
		this.isPrefix = flag;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public RarityHolder setRarityIndex(int rarityIndex) {
		if (rarityIndex >= 0) {
			this.rarityIndex = rarityIndex;
		}
		return this;
	}

	@Override
	public void setEquipmentAssigned(EquipmentItem equipmentAssigned) {
		this.equipmentAssigned = equipmentAssigned;
	}

	@Override
	public void setPricesModifications(CurrencySet priceModifications) {
		this.priceModifications = priceModifications;
		if (priceModifications != null) {
			priceModifications.setCanFireCurrencyChangeEvent(false);
		}
	}

	/*
	 * @Override public String toString() { return
	 * "\tEquipmentUpgradeImpl [\n\t\tname=" + name + ", rarityIndex=" + rarityIndex
	 * + ",\n\t\tpriceModifications=" + priceModifications + (this.description !=
	 * null ? (",\n\t" + this.description) : "")// + ",\n\t\tattributesModifiers=" +
	 * attributesModifiersToString() + "]"; }
	 */

	public String attributesModifiersToString() {
		StringBuilder sb;
		if (attributesModifiers == null) {
			return "null";
		}
		if (attributesModifiers.isEmpty()) {
			return "";
		}
		sb = new StringBuilder(16);
		attributesModifiers.forEach(am -> sb.append("\n\t\t\t").append(am));
		return sb.toString();
	}

	@Override
	public ClosestMatch<AttributeModification> closestMatchOf(AttributeModification key) {
		var cm = backMapAttrMods.closestMatchOf(AttributeModification.KEY_EXTRACTOR.apply(key));
		return cm.convertTo(AttributeModification.COMPARATOR, e -> e.getValue());
	}
}