package games.generic.controlModel.subimpl;

import java.util.Comparator;
import java.util.SortedSet;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.holders.RarityHolder;
import games.generic.controlModel.items.EquipmentItem;
import games.generic.controlModel.items.EquipmentUpgrade;
import games.generic.controlModel.misc.AttributeModification;
import games.generic.controlModel.misc.CurrencySet;
import tools.ClosestMatch;
import tools.Comparators;

public class EquipmentUpgradeImpl implements EquipmentUpgrade {

	public EquipmentUpgradeImpl(int rarityIndex, String name) {
		super();
		this.rarityIndex = rarityIndex;
		this.name = name;
		this.backMapAttrMods = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.STRING_COMPARATOR);
		this.attributeModifiers = backMapAttrMods.toSetValue(AttributeModification.KEY_EXTRACTOR);
		this.description = null;
	}

	protected int rarityIndex;
	protected String name, description;
	protected final MapTreeAVL<String, AttributeModification> backMapAttrMods;
	protected final SortedSet<AttributeModification> attributeModifiers;
	protected EquipmentItem equipmentAssigned;
	protected CurrencySet priceModifications;

	@Override
	public SortedSet<AttributeModification> getDelegator() { return attributeModifiers; }

	@Override
	public String getName() { return name; }

	@Override
	public String getDescription() { return description; }

	@Override
	public int getRarityIndex() { return rarityIndex; }

	@Override
	public EquipmentItem getEquipmentAssigned() { return equipmentAssigned; }

	@Override
	public CurrencySet getPricesModifications() { return priceModifications; }

	//

	@Override
	public void setDescription(String description) { this.description = description; }

	@Override
	public RarityHolder setRarityIndex(int rarityIndex) {
		if (rarityIndex >= 0)
			this.rarityIndex = rarityIndex;
		return this;
	}

	@Override
	public void setEquipmentAssigned(EquipmentItem equipmentAssigned) { this.equipmentAssigned = equipmentAssigned; }

	@Override
	public void setPricesModifications(CurrencySet priceModifications) {
		this.priceModifications = priceModifications;
		if (priceModifications != null) { priceModifications.setCanFireCurrencyChangeEvent(false); }
	}

	@Override
	public String toString() {
		return "\tEquipmentUpgradeImpl [\n\t\tname=" + name + ", rarityIndex=" + rarityIndex
				+ ",\n\t\tpriceModifications=" + priceModifications
				+ (this.description != null ? (",\n\t" + this.description) : "")//
				+ ",\n\t\tattributeModifiers=" + attributeModifiersToString() + "]";
	}

	public String attributeModifiersToString() {
		StringBuilder sb;
		if (attributeModifiers == null)
			return "null";
		if (attributeModifiers.isEmpty())
			return "";
		sb = new StringBuilder(16);
		attributeModifiers.forEach(am -> sb.append("\n\t\t\t").append(am));
		return sb.toString();
	}

	@Override
	public Comparator<AttributeModification> getKeyComparator() { return AttributeModification.COMPARATOR; }

	@Override
	public ClosestMatch<AttributeModification> closestMatchOf(AttributeModification key) {
		var cm = backMapAttrMods.closestMatchOf(AttributeModification.KEY_EXTRACTOR.apply(key));
		return cm.convertTo(AttributeModification.COMPARATOR, e -> e.getValue());
	}
}