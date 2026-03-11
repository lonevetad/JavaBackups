package games.generic.controlModel.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import dataStructures.MapTreeAVL;
import dataStructures.mtAvl.MapTreeAVLLightweight.TreeAVLDelegator;
import games.generic.controlModel.GModality;
import games.generic.controlModel.abilities.AbilityGeneric;
import games.generic.controlModel.attributes.AttributeModification;
import games.generic.controlModel.attributes.MaxUpgradesPerCategory;
import games.generic.controlModel.holders.AbilitiesHolder;
import games.generic.controlModel.holders.AttributesHolder;
import games.generic.controlModel.holders.GameObjectsProvidersHolder;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.objects.InteractingObj;
import games.generic.controlModel.objects.creature.BaseCreatureRPG;
import games.generic.controlModel.subimpl.GModalityRPG;
import tools.Comparators;
import tools.ObjectWithID;
import tools.json.types.JSONObject;
import tools.json.types.JSONString;

/**
 * Top class for equippable object.<br>
 * It's a base class.
 * <p>
 * Each Equipment should implements the set of "character attribute/statistics
 * modifiers" (i.e.: {@link AttributeModification}) as it's suggested by
 * {@link #getBaseAttributesModifiers()} and {@link #getUpgrades()}.
 * <p>
 * Note: Instead of creating an array of attributes (that mimics the character's
 * attributes) and apply to that array all modifiers, wasting memory in
 * almost-empty arrays, just collect all those {@link AttributeModification}
 * (into {@link #getBaseAttributesModifiers()} and {@link #getUpgrades()} that
 * provides a set of {@link AttributeModification}) and apply them one by one.
 */
public abstract class EquipmentItem extends InventoryItem implements AbilitiesHolder {
	private static final long serialVersionUID = -55232021L;
	public static final String FIELD_EQUIPMENT_TYPE = "equipmentType";
	public static final String FIELD_MAX_UPGRADES_PER_CATEGORY = "maxUpgradesPerCategory";
	public static final String FIELD_BASE_ATTRIBUTE_MODIFIERS = "baseAttributesModifiers";
	public static final String FIELD_UPGRADES = "upgrades";

	protected final EquipmentType equipmentType;
	protected transient EquipmentSet belongingEquipmentSet;
	protected final List<AttributeModification> baseAttributesModifiers;
	protected MapTreeAVL<String, AbilityGeneric> backMapAbilities;
	protected transient Set<AbilityGeneric> abilities;
	protected MapTreeAVL<String, IEquipmentUpgrade> backMapEquipUpgrades; //
	protected transient Set<IEquipmentUpgrade> upgrades;
	protected MapTreeAVL<IEquipmentUpgradeCategory, MaxUpgradesPerCategory> maxUpgradesPerCategory;

	public EquipmentItem(GModalityRPG gmrpg, EquipmentType equipmentType, String name) {
		this(gmrpg, equipmentType, name, null);
	}

	public EquipmentItem(GModalityRPG gmrpg, EquipmentType equipmentType, String name,
			AttributeModification[] baseAttributeMods) {
		super(gmrpg, name);
		this.belongingEquipmentSet = null;
		this.abilities = null;
		this.equipmentType = equipmentType;
		this.baseAttributesModifiers = //
				(baseAttributeMods == null) ? //
						Collections.unmodifiableList(new LinkedList<>())
						: Collections.unmodifiableList(Arrays.asList(baseAttributeMods));
		onCreate(gmrpg);
	}

	//

	public EquipmentType getEquipmentType() {
		return this.equipmentType;
	}

	public EquipmentSet getBelongingEquipmentSet() {
		return this.belongingEquipmentSet;
	}

	/**
	 * Immutable {@link List} of {@link AttributeModification} related to this
	 * equipment, that defines it. It's embedded in its definition and should not be
	 * modified. Use {@link #addUpgrade(IEquipmentUpgrade)} instead.
	 */
	public List<AttributeModification> getBaseAttributesModifiers() {
		return this.baseAttributesModifiers;
	}

	/** Beware: could return null if this item has no abilities. */
	public Set<AbilityGeneric> getAbilitiesSet() {
		checkAbilitiesSet();
		return this.abilities;
	}

	@Override
	public Map<String, AbilityGeneric> getAbilities() {
		checkAbilitiesSet();
		return this.backMapAbilities;
	}

	public Map<String, IEquipmentUpgrade> getUpgradesMap() {
		return backMapEquipUpgrades;
	}

	public Set<IEquipmentUpgrade> getUpgrades() {
		return this.upgrades;
	}

	public MapTreeAVL<IEquipmentUpgradeCategory, MaxUpgradesPerCategory> getMaxUpgradesPerCategory() {
		this.checkMaxUpgradesPerCategory();
		return this.maxUpgradesPerCategory;
	}

	//

	public void setBelongingEquipmentSet(EquipmentSet belongingEquipmentSet) {
		this.belongingEquipmentSet = belongingEquipmentSet;
	}

	public void setMaxUpgradesPerCategory(
			MapTreeAVL<IEquipmentUpgradeCategory, MaxUpgradesPerCategory> maxUpgradesPerCategory) {
		this.maxUpgradesPerCategory = maxUpgradesPerCategory;
		this.checkMaxUpgradesPerCategory();
	}

	//

	//

	/**
	 * Function called on creation time to fill fields like particular abilities (if
	 * no already done in loading time)
	 */
	protected abstract void enrichEquipment(GModality gm, GameObjectsProvidersHolder providersHolder);

	@Override
	public void onCreate(GModality gm) {
		enrichEquipment(gm, gm.getGameObjectsProvider());
	}

	/** Just check the instances of sets and backmaps. */
	protected void checkAbilitiesSet() {
		if (this.abilities != null) {
			return;
		}
		this.backMapAbilities = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight,
				MapTreeAVL.BehaviourOnKeyCollision.KeepPrevious, Comparators.STRING_COMPARATOR);
		this.abilities = this.backMapAbilities.toSetValue(AbilityGeneric.NAME_EXTRACTOR);
	}

	protected void checkUpgradeSet() {
		if (this.upgrades != null) {
			return;
		}
		this.backMapEquipUpgrades = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight,
				Comparators.STRING_COMPARATOR);
		this.upgrades = this.backMapEquipUpgrades.toSetValue(IEquipmentUpgrade.KEY_EXTRACTOR);
	}

	protected void checkMaxUpgradesPerCategory() {
		if (this.maxUpgradesPerCategory != null) {
			return;
		}
		this.maxUpgradesPerCategory = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight,
				IEquipmentUpgradeCategory.COMPARATOR_IEQUIPMENT_UPGRADE_CATEGORY);
	}

	public void addUpgradeCategoryMax(IEquipmentUpgradeCategory category, MaxUpgradesPerCategory maxUpgrades) {
		this.checkMaxUpgradesPerCategory();
		this.maxUpgradesPerCategory.put(category, maxUpgrades);
	}

	public boolean removeUpgradeCategoryMax(IEquipmentUpgradeCategory category) {
		this.checkMaxUpgradesPerCategory();
		if (!this.maxUpgradesPerCategory.containsKey(category)) {
			return false;
		}
		this.maxUpgradesPerCategory.remove(category);
		return true;
	}

	//

	@Override
	public void resetStuffs() {
		// nothing to do here, right now
	}

	public BaseCreatureRPG getCreatureWearingEquipments() {
		EquipmentSet es;
		es = this.getBelongingEquipmentSet();
		return (es == null) ? null : es.getCreatureWearingEquipments();
	}

	@Override
	public InteractingObj getOwner() {
		return getCreatureWearingEquipments();
	}

	@Override
	public void setOwner(ObjectWithID owner) {
		EquipmentSet es;
		super.setOwner(owner);
		es = this.getBelongingEquipmentSet();
		if (es == null || (!(owner instanceof BaseCreatureRPG))) {
			return;
		}
		es.setCreatureWearingEquipments((BaseCreatureRPG) owner);
	}

	//

	/*
	 * public EquipmentItem addAttributeModifier(AttributeModification am) { if (am
	 * != null) { this.baseAttributesModifiers.add(am); } return this; }
	 */

	@Override
	public GModality getGameModality() {
		return getCreatureWearingEquipments().getGameModality();
	}

	public String getNameWithUpgrades() {
		Set<IEquipmentUpgrade> upg = this.getUpgrades();
		if (upg == null) {
			return super.getName();
		}
		StringBuilder sb = new StringBuilder();
		final List<IEquipmentUpgrade> prefixes = new ArrayList<>(), suffixes = new ArrayList<>();
		upg.forEach(up -> {
			(up.isPrefix() ? prefixes : suffixes).add(up);
		});
		boolean firstSuffixDone = false;
		for (IEquipmentUpgrade p : prefixes) {
			sb.append(p.getName()).append(' ');
		}
		sb.append(super.getName());
		for (IEquipmentUpgrade p : suffixes) {
			if (firstSuffixDone) {
				sb.append(',');
			} else {
				firstSuffixDone = true;
			}
			sb.append(' ').append(p.getName());
		}
		return sb.toString();
	}

	@Override
	public EquipmentItem addAbility(AbilityGeneric am) {
		if (am != null) {
			checkAbilitiesSet();
			this.abilities.add(am);
			// am.setEquipItem(this);
			BaseCreatureRPG owner = getCreatureWearingEquipments();
			if (owner != null) {
				am.setOwner(owner);
				if (owner.getGameModality() == null) {
					// make sure the adding operation is performed
					am.onAddingToOwner(getGameModality());
				}
			}
		}
		return this;
	}

	@Override
	public EquipmentItem removeAbility(AbilityGeneric am) {
		if (am != null) {
			checkAbilitiesSet();
			if (this.abilities.remove(am)) {
				// am.setEquipItem(null);
				if (getCreatureWearingEquipments() != null) {
					am.onRemovingFromOwner(getGameModality());
				}
			}
		}
		return this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public EquipmentItem removeAbilityByName(String name) {
		if (name != null) {
			TreeAVLDelegator<String, AbilityGeneric> backMapDeleg;
			MapTreeAVL<String, AbilityGeneric> backMap;
			AbilityGeneric am;
			checkAbilitiesSet();
			backMapDeleg = (TreeAVLDelegator<String, AbilityGeneric>) this.abilities;
			backMap = backMapDeleg.getBackTree();
			am = backMap.get(name);
			if (am != null) {
				backMap.remove(name);
				// am.setEquipItem(null);
				am.onRemovingFromOwner(getGameModality());
			}
		}
		return this;
	}

	public EquipmentItem addUpgrade(IEquipmentUpgrade up) {
		final AttributesHolder ah;
		final CreatureAttributes ca;
		if (up != null) {
			checkUpgradeSet();
			this.upgrades.add(up);
			up.setEquipmentAssigned(this);
			// apply currency monus/malus
			up.getPricesModifications().forEachCurrency((i, amount) -> {
				this.sellPrice.alterCurrencyAmount(i, amount);
			});
			// apply modifications
			ah = this.getCreatureWearingEquipments();
			if (ah != null) {
				ca = ah.getAttributes();
				up.getAttributesModifiers().forEach(eam -> ca.applyAttributeModifier(eam));
			}
		}
		return this;
	}

	public EquipmentItem removeUpgrade(IEquipmentUpgrade up) {
		final AttributesHolder ah;
		final CreatureAttributes ca;
		if (up != null) {
			checkUpgradeSet();
			if (this.upgrades.remove(up)) {
				up.setEquipmentAssigned(null);
			}
			up.getPricesModifications().forEachCurrency((i, amount) -> {
				this.sellPrice.alterCurrencyAmount(i, -amount);
			});
			// apply modifications
			ah = this.getCreatureWearingEquipments();
			if (ah != null) {
				ca = ah.getAttributes();
				up.getAttributesModifiers().forEach(eam -> ca.removeAttributeModifier(eam));
			}
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	public EquipmentItem removeUpgradeByName(String name) {
		if (name != null) {
			TreeAVLDelegator<String, IEquipmentUpgrade> backMapDeleg;
			MapTreeAVL<String, IEquipmentUpgrade> backMap;
			IEquipmentUpgrade eu;
			checkAbilitiesSet();
			backMapDeleg = (TreeAVLDelegator<String, IEquipmentUpgrade>) this.upgrades;
			backMap = backMapDeleg.getBackTree();
			eu = backMap.get(name);
			if (eu != null) {
				removeUpgrade(eu);
			}
		}
		return this;
	}

	/**
	 * The opposite of {@link #onUnEquip(GModality)}.
	 */
	public void onEquip(final GModality gm) {
		final AttributesHolder ah;
		final CreatureAttributes ca;
		List<AttributeModification> attmod;
		Set<IEquipmentUpgrade> upg;
		Consumer<AttributeModification> modifierApplier;
		ah = this.getCreatureWearingEquipments(); // assumed to be true
		ca = ah.getAttributes();
		modifierApplier = eam -> ca.applyAttributeModifier(eam);
		attmod = this.getBaseAttributesModifiers();
		this.onAddingToOwner(gm);
		if (attmod != null) {
			attmod.forEach(modifierApplier);
		}
		upg = this.getUpgrades();
		if (upg != null) {
			upg.forEach(up -> {
				// apply all upgrade's modifiers
				up.getAttributesModifiers().forEach(modifierApplier);
			});
		}
	}

	/**
	 * The opposite of {@link #onEquip(GModality)}.
	 */
	public void onUnEquipping(final GModality gm) {
		final AttributesHolder ah;
		final CreatureAttributes ca;
		List<AttributeModification> attmod;
		Set<IEquipmentUpgrade> upg;
		Consumer<AttributeModification> modifierRemover;

		ah = this.getCreatureWearingEquipments();
		ca = ah.getAttributes();
		modifierRemover = eam -> ca.removeAttributeModifier(eam);
		attmod = this.getBaseAttributesModifiers();

		gm.removeGameObject(this);
		this.onRemovingFromOwner(gm);
		if (attmod != null) {
			attmod.forEach(modifierRemover);
		}
		upg = this.getUpgrades();
		if (upg != null) {
			upg.forEach(up -> {
				// remove all upgrade's modifiers
				up.getAttributesModifiers().forEach(modifierRemover);
			});
		}
	}

	@Override
	public void onAddingToOwner(GModality gm) {
		Set<AbilityGeneric> abl;
		ObjectWithID o;
		o = getOwner();
		super.onAddingToOwner(gm);
		abl = this.getAbilitiesSet();
		if (abl != null) {
			// abl.forEach(ea -> ea.onEquip(gm));
			abl.forEach(ea -> {
				ea.setOwner(o);
				ea.onAddingToOwner(gm);
			});
		}
	}

	@Override
	public void onAddedToGame(GModality gm) {
		super.onAddedToGame(gm);
	}

	@Override
	public void onRemovingFromOwner(GModality gm) {
		Set<AbilityGeneric> abl;
		super.onRemovingFromOwner(gm);
		abl = this.getAbilitiesSet();
		if (abl != null) {
			// abl.forEach(ea -> ea.onUnEquipping(gm));
			abl.forEach(ea -> {
				ea.onRemovingFromOwner(gm);
			});
		}
	}

	@Override
	public void onRemovedFromGame(GModality gm) { //
		AbilitiesHolder.super.onRemovedFromGame(gm);
		super.onRemovedFromGame(gm);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [name=" + name + ", ID=" + getID() + ",\n\t rarityIndex, ="
				+ rarityIndex + ", equipmentType=" + equipmentType + ",\n\t prices to sell: " + this.sellPrice
				+ ",\n\tbaseAttributesModifiers=" + baseAttributesModifiers + ",\n\t upgrades=[" + upgradesToString()
				+ "],\n\t abilities=" + //
				abilitiesToString() + "]";
	}

	protected String abilitiesToString() {
		StringBuilder sb;
		if (abilities == null) {
			return "null";
		}
		sb = new StringBuilder(16);
		abilities.forEach(ea -> sb.append(ea));
		return sb.toString();
	}

	protected String upgradesToString() {
		StringBuilder sb;
		if (upgrades == null) {
			return "null";
		}
		if (upgrades.isEmpty()) {
			return "";
		}
		sb = new StringBuilder(16);
		sb.append('\n');
		upgrades.forEach(eu -> sb.append('\t').append(eu).append('\n'));
		return sb.toString();
	}

	//

	// JSON-related

	//

	@Override
	public void toJSONValue(JSONObject wrapper) {
		super.toJSONValue(wrapper);
		AbilitiesHolder.super.toJSONValue(wrapper);
		// equipment type
		wrapper.addField(FIELD_EQUIPMENT_TYPE, new JSONString(this.getEquipmentType().getName()));
		// maxUpgradesPerCategory
		final JSONObject maxUpgradesPerCategoryJSONed = new JSONObject();
		this.getMaxUpgradesPerCategory().forEach((category, maxUpgr) -> {
			maxUpgradesPerCategoryJSONed.addField(category.getName(), maxUpgr.toJSONValue());
		});
		wrapper.addField(FIELD_MAX_UPGRADES_PER_CATEGORY, maxUpgradesPerCategoryJSONed);
		// base attribute modifiers
		final JSONObject baseAttributesModifiersJSONed = new JSONObject();
		this.getBaseAttributesModifiers().forEach((am) -> {
			baseAttributesModifiersJSONed.addField(am.getName(), am.toJSONValue());
		});
		wrapper.addField(FIELD_BASE_ATTRIBUTE_MODIFIERS, maxUpgradesPerCategoryJSONed);
		// upgrades
		final JSONObject upgradesJSONed = new JSONObject();
		this.getUpgrades().forEach((eUpgrade) -> {
			upgradesJSONed.addField(eUpgrade.getName(), eUpgrade.toJSONValue());
		});
		wrapper.addField(FIELD_UPGRADES, upgradesJSONed);
	}

	@Override
	public default void loadFromJSONObject(GModality gm, JSONObject wrapper) {
		// TODO : da fare tutto
	}

	@Override
	public default void loadFromJSONMap(GModality gm, Map<String, Object> jsonMap) {
		// TODO : da fare tutto
	}
}