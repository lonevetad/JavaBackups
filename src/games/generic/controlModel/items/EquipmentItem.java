package games.generic.controlModel.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.function.Consumer;

import dataStructures.MapTreeAVL;
import dataStructures.mtAvl.MapTreeAVLLightweight.TreeAVLDelegator;
import games.generic.controlModel.GModality;
import games.generic.controlModel.abilities.AbilityGeneric;
import games.generic.controlModel.attributes.AttributeIdentifier;
import games.generic.controlModel.attributes.AttributeModification;
import games.generic.controlModel.attributes.MaxUpgradesPerCategory;
import games.generic.controlModel.holders.AbilitiesHolder;
import games.generic.controlModel.holders.AttributesHolder;
import games.generic.controlModel.holders.AttributesModificationsHolder;
import games.generic.controlModel.holders.GameObjectsProvidersHolder;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.objects.InteractingObj;
import games.generic.controlModel.objects.creature.BaseCreatureRPG;
import games.generic.controlModel.subimpl.GModalityRPG;
import tools.ClosestMatch;
import tools.Comparators;
import tools.ObjectWithID;
import tools.json.JSONTypes;
import tools.json.JSONValue;
import tools.json.JSONable;
import tools.json.types.JSONArray;
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
public abstract class EquipmentItem extends InventoryItem implements AbilitiesHolder, AttributesModificationsHolder {
	private static final long serialVersionUID = -55232021L;
	public static final String FIELD_EQUIPMENT_TYPE = "type";
	public static final String FIELD_MAX_UPGRADES_PER_CATEGORY = "maxUpgradesPerCategory";
	public static final String FIELD_UPGRADES = "upgrades";
	public static final String FIELD_ABILITIES = "abilities";

	protected EquipmentType equipmentType;
	protected transient EquipmentSet belongingEquipmentSet;
	protected MapTreeAVL<String, AttributeModification> backMapBaseAttributesModifiers;
	protected transient SortedSet<AttributeModification> baseAttributesModifiers;
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
		this.baseAttributesModifiers = null;
		this.equipmentType = equipmentType;
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
	public SortedSet<AttributeModification> getBaseAttributesModifiers() {
		this.checkBaseAttributesModifiers();
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
		this.checkUpgradeSet();
		return backMapEquipUpgrades;
	}

	public Set<IEquipmentUpgrade> getUpgrades() {
		this.checkUpgradeSet();
		return this.upgrades;
	}

	public MapTreeAVL<IEquipmentUpgradeCategory, MaxUpgradesPerCategory> getMaxUpgradesPerCategory() {
		this.checkMaxUpgradesPerCategory();
		return this.maxUpgradesPerCategory;
	}

	//

	@Override
	public boolean removeAttributeModifier(AttributeIdentifier attributeIdentifier) {

		this.checkBaseAttributesModifiers();
		String attrName = attributeIdentifier.getName();
		/*
		 * int indexAt = 0; boolean found = false; if
		 * (this.baseAttributesModifiers.isEmpty()) { return false; } for
		 * (AttributeModification am : this.baseAttributesModifiers) { if
		 * (attrName.equals(am.getName())) { found = true; break; } indexAt++; } if
		 * (!found) { return false; } this.baseAttributesModifiers.remove(indexAt);
		 */
		if (!this.backMapBaseAttributesModifiers.containsKey(attrName)) {
			return false;
		}
		this.backMapBaseAttributesModifiers.remove(attrName);
		return true;
	}

	@Override
	public ClosestMatch<AttributeModification> closestMatchOf(AttributeModification key) {
		throw new UnsupportedOperationException("No meaning here");
	}

	@Override
	public SortedSet<AttributeModification> getAttributesModifiers() {
		return this.getBaseAttributesModifiers();
	}

	protected void setEquipmentType(EquipmentType equipmentType) {
		this.equipmentType = equipmentType;
	}

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

	protected void checkBaseAttributesModifiers() {
		if (this.baseAttributesModifiers != null) {
			return;
		}
		this.backMapBaseAttributesModifiers = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight,
				MapTreeAVL.BehaviourOnKeyCollision.KeepPrevious, Comparators.STRING_COMPARATOR);
		this.baseAttributesModifiers = this.backMapBaseAttributesModifiers
				.toSetValue(AttributeModification.KEY_EXTRACTOR);
	}

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

	public MaxUpgradesPerCategory getMaxUpgradeOfCategory(GModality gm, IEquipmentUpgradeCategory category) {
		Objects.requireNonNull(category);
		Map<IEquipmentUpgradeCategory, MaxUpgradesPerCategory> mucMap = this.getMaxUpgradesPerCategory();
		if (mucMap.containsKey(category)) {
			return mucMap.get(category);
		}
		MaxUpgradesPerCategory mup = newMaxUpgradesPerCategory(gm, category);
		mucMap.put(category, mup);
		return mup;
	}

	//

	// "onX" events

	//

	/**
	 * The opposite of {@link #onUnEquip(GModality)}.
	 */
	public void onEquip(final GModality gm) {
		final AttributesHolder ah;
		final CreatureAttributes ca;
		SortedSet<AttributeModification> attmod;
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
		SortedSet<AttributeModification> attmod;
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

	public abstract void loadEquipmentType(GModality gm, String typeName);

	public abstract IEquipmentUpgradeCategory getEquipmentUpgradeCategoryByName(GModality gm,
			String equipmentUpgradeCategoryName);

	public abstract MaxUpgradesPerCategory newMaxUpgradesPerCategory(GModality gm,
			IEquipmentUpgradeCategory equipUpgradeCategory);

	public abstract AttributeIdentifier getAttributeIdentifierByName(GModality gm, String attributeName);

	@Override
	public AttributeIdentifier loadAttributeIdentifier(GModality gm, String attributeName, int attributeValue) {
		return this.getAttributeIdentifierByName(gm, attributeName);
	}

	public abstract AttributeModification newAttributeModification(GModality gm, AttributeIdentifier attributeModified,
			int value);

	public abstract IEquipmentUpgrade newEquipmentUpgrade(GModality gm, String equipUpgradeName);

	public abstract AbilityGeneric newAbility(GModality gm, String abilityName);

	public abstract IEquipmentUpgradeCategory[] getAllEquipmentUpgradeCategory();

	@Override
	public void toJSONValue(JSONObject wrapper) {
		super.toJSONValue(wrapper);
		AbilitiesHolder.super.toJSONValue(wrapper);
		AttributesModificationsHolder.super.toJSONValue(wrapper);
		// equipment type
		wrapper.addField(FIELD_EQUIPMENT_TYPE, new JSONString(this.getEquipmentType().getName()));
		// maxUpgradesPerCategory
		final JSONObject maxUpgradesPerCategoryJSONed = new JSONObject();
		this.getMaxUpgradesPerCategory().forEach((category, maxUpgr) -> {
			maxUpgradesPerCategoryJSONed.addField(category.getName(), maxUpgr.toJSONValue());
		});
		wrapper.addField(FIELD_MAX_UPGRADES_PER_CATEGORY, maxUpgradesPerCategoryJSONed);
		// upgrades
		final JSONObject upgradesJSONed = new JSONObject();
		this.getUpgrades().forEach((eUpgrade) -> {
			upgradesJSONed.addField(eUpgrade.getName(), eUpgrade.toJSONValue());
		});
		wrapper.addField(FIELD_UPGRADES, upgradesJSONed);

		// abilities
		Map<String, AbilityGeneric> allAbilities = this.getAbilities();
		JSONValue[] abilitiesArrayJSONed = new JSONValue[allAbilities.size()];
		final int[] index = { 0 };
		allAbilities.forEach((name, abilGen) -> {
			abilitiesArrayJSONed[index[0]++] = abilGen.toJSONValue();
		});
		JSONArray abilitiesJSONed = new JSONArray(true, abilitiesArrayJSONed, JSONTypes.String);
		wrapper.addField(FIELD_ABILITIES, abilitiesJSONed);
	}

	@Override
	public void loadFromJSONObject(GModality gm, JSONObject wrapper) throws IllegalArgumentException {
		if (wrapper == null) {
			throw new IllegalArgumentException("Provided JSON wrapper cannot be null");
		}
		super.loadFromJSONObject(gm, wrapper);
		AbilitiesHolder.super.loadFromJSONObject(gm, wrapper);
		AttributesModificationsHolder.super.loadFromJSONObject(gm, wrapper);
		// equipmentType
		if (!wrapper.hasField(FIELD_EQUIPMENT_TYPE)) {
			this.raiseExceptionMissingField(FIELD_EQUIPMENT_TYPE, JSONTypes.String);
		}
		JSONValue equipTypeNameJSONed_value = wrapper.getFieldValue(FIELD_EQUIPMENT_TYPE);
		System.out.println("...... LOADING EQUIP with type: " + equipTypeNameJSONed_value.asString()
				+ " --- with name: " + this.getName());
		if (!equipTypeNameJSONed_value.isType(JSONTypes.String)) {
			this.raiseExceptionIllegalTypeField(FIELD_EQUIPMENT_TYPE, JSONTypes.String, equipTypeNameJSONed_value);
		}
		this.loadEquipmentType(gm, ((JSONString) equipTypeNameJSONed_value).asString());
		// maxUpgradesPerCategory
		boolean hasMaxUpgradesPerCat = wrapper.hasField(FIELD_MAX_UPGRADES_PER_CATEGORY);
		JSONValue maxUpgradesPerCategoryJSONed_value = null;
		JSONObject maxUpgradesPerCategoryJSONed = null; // it's a Map, whose keys are the categories
		final MapTreeAVL<IEquipmentUpgradeCategory, MaxUpgradesPerCategory> mupcMap = this.getMaxUpgradesPerCategory();
		if (hasMaxUpgradesPerCat) { // a loading is needed
			maxUpgradesPerCategoryJSONed_value = wrapper.getFieldValue(FIELD_MAX_UPGRADES_PER_CATEGORY);
			if (!maxUpgradesPerCategoryJSONed_value.isType(JSONTypes.Object)) {
				this.raiseExceptionIllegalTypeField(FIELD_MAX_UPGRADES_PER_CATEGORY, JSONTypes.Object,
						maxUpgradesPerCategoryJSONed_value);
			}
			maxUpgradesPerCategoryJSONed = (JSONObject) maxUpgradesPerCategoryJSONed_value;
		}
		for (IEquipmentUpgradeCategory ec : getAllEquipmentUpgradeCategory()) {
			MaxUpgradesPerCategory mup = null;
			if (hasMaxUpgradesPerCat) {
				// load it, if present
				String categoryName = ec.getName();
				if (maxUpgradesPerCategoryJSONed.hasField(categoryName)) { // load it
					JSONValue maxUpgradeJSONed_value = maxUpgradesPerCategoryJSONed.getFieldValue(categoryName);
					if (!maxUpgradeJSONed_value.isType(JSONTypes.Object)) {
						this.raiseExceptionIllegalTypeField(
								FIELD_MAX_UPGRADES_PER_CATEGORY + JSONable.SEPARATOR_FIELD + categoryName, //
								JSONTypes.Object, //
								maxUpgradeJSONed_value //
						);
					}
					mup = newMaxUpgradesPerCategory(gm, ec); // a new one, ready to be loaded
					mup.loadFromJSONObject(gm, (JSONObject) maxUpgradeJSONed_value);
					mup.setUpgradeCategory(ec);
				} // else -> not found -> get the default
			}
			if (mup == null) { // not found? -> default
				mup = ec.getDefaultMaxUpgradesPerCategory(); // newMaxUpgradesPerCategory(gm, ec);
			}
			mupcMap.put(ec, mup);
		}
		// upgrades
		if (wrapper.hasField(FIELD_UPGRADES)) { // optional
			// this.raiseExceptionMissingField(FIELD_UPGRADES, JSONTypes.Object);
			JSONValue upgradesJSONed_value = wrapper.getFieldValue(FIELD_UPGRADES);
			if (!upgradesJSONed_value.isType(JSONTypes.Object)) {
				this.raiseExceptionIllegalTypeField(FIELD_UPGRADES, JSONTypes.Object, upgradesJSONed_value);
			}
			JSONObject upgradesJSONed = (JSONObject) upgradesJSONed_value;
			final Map<String, IEquipmentUpgrade> upsMap = this.getUpgradesMap();
			upgradesJSONed.forEachField((equipUpgradeName, euValueJSONed_value) -> {
				if (!euValueJSONed_value.isType(JSONTypes.Object)) {
					this.raiseExceptionIllegalTypeField(FIELD_UPGRADES + JSONable.SEPARATOR_FIELD + equipUpgradeName,
							JSONTypes.Object, euValueJSONed_value);
				}
				IEquipmentUpgrade eu = newEquipmentUpgrade(gm, equipUpgradeName);
				eu.loadFromJSONObject(gm, (JSONObject) euValueJSONed_value);
				upsMap.put(equipUpgradeName, eu);
			});
		}
		// abilities
		if (wrapper.hasField(FIELD_ABILITIES)) { // optional
			JSONValue abilitiesJSONed_value = wrapper.getFieldValue(FIELD_ABILITIES);
			if (!abilitiesJSONed_value.isType(JSONTypes.ArrayHomogeneousType)) {
				this.raiseExceptionIllegalTypeField(FIELD_ABILITIES, JSONTypes.ArrayHomogeneousType,
						abilitiesJSONed_value);
			}
			JSONArray abilitiesJSONed = (JSONArray) abilitiesJSONed_value;
			final Map<String, AbilityGeneric> allAbilities = this.getAbilities();
			abilitiesJSONed.forEach((index, abilityJSONed_value) -> {
				if (!abilityJSONed_value.isType(JSONTypes.String)) {
					this.raiseExceptionIllegalTypeField(FIELD_UPGRADES + JSONable.SEPARATOR_INDEX + index,
							JSONTypes.String, abilityJSONed_value);
				}
				String abilityName = abilityJSONed_value.asString();
				allAbilities.put(abilityName, newAbility(gm, abilityName));
			});
		}
	}

	@Override
	public void loadFromJSONMap(GModality gm, Map<String, Object> jsonMap) throws IllegalArgumentException {
		if (jsonMap == null) {
			throw new IllegalArgumentException("Provided JSONObject map cannot be null");
		}
		super.loadFromJSONMap(gm, jsonMap);
		AbilitiesHolder.super.loadFromJSONMap(gm, jsonMap);
		AttributesModificationsHolder.super.loadFromJSONMap(gm, jsonMap);
		// equipmentType
		if (!jsonMap.containsKey(FIELD_EQUIPMENT_TYPE)) {
			this.raiseExceptionMissingField(FIELD_EQUIPMENT_TYPE, JSONTypes.String);
		}
		Object equipTypeNameJSONed_value = jsonMap.get(FIELD_EQUIPMENT_TYPE);
		if (!(equipTypeNameJSONed_value instanceof String)) {
			this.raiseExceptionIllegalTypeField(FIELD_EQUIPMENT_TYPE, JSONTypes.String, equipTypeNameJSONed_value);
		}
		this.loadEquipmentType(gm, ((JSONString) equipTypeNameJSONed_value).asString());
		// maxUpgradesPerCategory
		boolean hasMaxUpgradesPerCat = jsonMap.containsKey(FIELD_MAX_UPGRADES_PER_CATEGORY);
		Map<String, Object> maxUpgradesPerCategoryJSONed = null; // it's a Map, whose keys are the categories
		Object maxUpgradesPerCategoryJSONed_value = null;
		final MapTreeAVL<IEquipmentUpgradeCategory, MaxUpgradesPerCategory> mupcMap = this.getMaxUpgradesPerCategory();

		if (!jsonMap.containsKey(FIELD_MAX_UPGRADES_PER_CATEGORY)) {
			this.raiseExceptionMissingField(FIELD_MAX_UPGRADES_PER_CATEGORY, JSONTypes.Object);
		}
		maxUpgradesPerCategoryJSONed_value = jsonMap.get(FIELD_MAX_UPGRADES_PER_CATEGORY);
		if (!(maxUpgradesPerCategoryJSONed_value instanceof Map<?, ?>)) {
			this.raiseExceptionIllegalTypeField(FIELD_MAX_UPGRADES_PER_CATEGORY, JSONTypes.Object,
					maxUpgradesPerCategoryJSONed_value);
		}
		if (hasMaxUpgradesPerCat) { // a loading is needed
			maxUpgradesPerCategoryJSONed_value = jsonMap.get(FIELD_MAX_UPGRADES_PER_CATEGORY);
			if (!(maxUpgradesPerCategoryJSONed_value instanceof Map<?, ?>)) {
				this.raiseExceptionIllegalTypeField(FIELD_MAX_UPGRADES_PER_CATEGORY, JSONTypes.Object,
						maxUpgradesPerCategoryJSONed_value);
			}
			maxUpgradesPerCategoryJSONed = (Map<String, Object>) maxUpgradesPerCategoryJSONed_value;
		}
		for (IEquipmentUpgradeCategory ec : getAllEquipmentUpgradeCategory()) {
			MaxUpgradesPerCategory mup = null;
			if (hasMaxUpgradesPerCat) {
				// load it, if present
				String categoryName = ec.getName();
				if (maxUpgradesPerCategoryJSONed.containsKey(categoryName)) { // load it
					Object maxUpgradeJSONed_value = maxUpgradesPerCategoryJSONed.get(categoryName);
					if (!(maxUpgradeJSONed_value instanceof Map<?, ?>)) {
						this.raiseExceptionIllegalTypeField(
								FIELD_MAX_UPGRADES_PER_CATEGORY + JSONable.SEPARATOR_FIELD + categoryName, //
								JSONTypes.Object, //
								maxUpgradeJSONed_value //
						);
					}
					mup = newMaxUpgradesPerCategory(gm, ec); // a new one, ready to be loaded
					mup.loadFromJSONMap(gm, (Map<String, Object>) maxUpgradeJSONed_value);
				} // else -> not found -> get the default
			}
			if (mup == null) { // not found? -> default
				mup = ec.getDefaultMaxUpgradesPerCategory(); // newMaxUpgradesPerCategory(gm, ec);
			}
			mupcMap.put(ec, mup);
		}
		// upgrades
		if (jsonMap.containsKey(FIELD_UPGRADES)) { // optional
			// this.raiseExceptionMissingField(FIELD_UPGRADES, JSONTypes.Object);
			Object upgradesJSONed_value = jsonMap.get(FIELD_UPGRADES);
			if (!(upgradesJSONed_value instanceof Map<?, ?>)) {
				this.raiseExceptionIllegalTypeField(FIELD_UPGRADES, JSONTypes.Object, upgradesJSONed_value);
			}
			Map<String, Object> upgradesJSONed = (Map<String, Object>) upgradesJSONed_value;
			final Map<String, IEquipmentUpgrade> upsMap = this.getUpgradesMap();
			upgradesJSONed.forEach((equipUpgradeName, euValueJSONed_value) -> {
				if (!(euValueJSONed_value instanceof Map<?, ?>)) {
					this.raiseExceptionIllegalTypeField(FIELD_UPGRADES + JSONable.SEPARATOR_FIELD + equipUpgradeName,
							JSONTypes.Object, euValueJSONed_value);
				}
				IEquipmentUpgrade eu = newEquipmentUpgrade(gm, equipUpgradeName);
				eu.loadFromJSONMap(gm, (Map<String, Object>) euValueJSONed_value);
				upsMap.put(equipUpgradeName, eu);
			});
		}
		// abilities
		if (jsonMap.containsKey(FIELD_ABILITIES)) { // optional
			Object abilitiesJSONed_value = jsonMap.get(FIELD_ABILITIES);
			if (!(abilitiesJSONed_value instanceof String[])) {
				this.raiseExceptionIllegalTypeField(FIELD_ABILITIES, JSONTypes.ArrayHomogeneousType,
						abilitiesJSONed_value);
			}
			String[] abilitiesJSONed = (String[]) abilitiesJSONed_value;
			final Map<String, AbilityGeneric> allAbilities = this.getAbilities();
			for (String abilityName : abilitiesJSONed) {
				allAbilities.put(abilityName, newAbility(gm, abilityName));
			}
		}
	}
}