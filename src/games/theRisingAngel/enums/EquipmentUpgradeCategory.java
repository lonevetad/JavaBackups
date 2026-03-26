package games.theRisingAngel.enums;

import java.util.Map;

import games.generic.controlModel.GModality;
import games.generic.controlModel.attributes.MaxUpgradesPerCategory;
import games.generic.controlModel.items.IEquipmentUpgradeCategory;
import games.generic.controlModel.misc.RangedAmountInt;
import games.generic.controlModel.providers.FactoryGeneric;
import games.theRisingAngel.misc.MaxUpgradesPerCategoryTRAn;
import tools.json.JSONValue;
import tools.json.types.JSONObject;
import tools.json.types.JSONString;

public enum EquipmentUpgradeCategory implements IEquipmentUpgradeCategory {
	// (corrisponde alla definizione di "fatto con..."),
	Material(new MaxUpgradesPerCategoryTRAn(new RangedAmountInt(3, 12))), //
	// (good, light, ruined, encrusted, flexible,...),
	Structural(new MaxUpgradesPerCategoryTRAn(new RangedAmountInt(2, 7))), //
	// ("rifiniture/raffinatezza", per esempio: rough, sharp, worn-out, with art
	// decoration, broken, fragile, thinner, bended),
	Refining(new MaxUpgradesPerCategoryTRAn(new RangedAmountInt(2, 7))), //
	Coloring(new MaxUpgradesPerCategoryTRAn(new RangedAmountInt(2, 4))), //
	Rune(new MaxUpgradesPerCategoryTRAn(new RangedAmountInt(3, 10))), //
	Environmental(new MaxUpgradesPerCategoryTRAn(new RangedAmountInt(1, 5))), //
	Incantation(new MaxUpgradesPerCategoryTRAn(new RangedAmountInt(7, 25))), // almost everything, even "of Traitors"
	Blessing(new MaxUpgradesPerCategoryTRAn(new RangedAmountInt(2, 8))), // "of the Sun", "Blessed by ..."
	Incarnation(new MaxUpgradesPerCategoryTRAn(new RangedAmountInt(1, 3))), // divine being, spirit, soul, essence, ...
	Elemental(new MaxUpgradesPerCategoryTRAn(new RangedAmountInt(3, 15))), // (es: "Fire", "Water", "Air", "Earth",
																			// "Fresh Air", "Cold Air", "Lightning",
																			// "Nature", "Darkness", "Light", "Chaos",
																			// "Order", "Time", "Space", "Void", ...)
	Focusing(new MaxUpgradesPerCategoryTRAn(new RangedAmountInt(2, 20))), // (es: "of Strength", ONLY Attributes)
	WorkersRolesRelated(new MaxUpgradesPerCategoryTRAn(new RangedAmountInt(5, 20))), // of King, Blacksmith, Enchanter,
																						// Alchemist, Jeweler, Merchant,
																						// Hunter, Farmer, Fisherman,
																						// Cook, Scholar, Scribe,
																						// Priest, Thief, Assassin,
																						// Warrior, Mage, Archer, ...
	CharacterClass(new MaxUpgradesPerCategoryTRAn(new RangedAmountInt(2, 7))), //
	CharacterRace(new MaxUpgradesPerCategoryTRAn(new RangedAmountInt(1, 5))), //
	Tribal(new MaxUpgradesPerCategoryTRAn(new RangedAmountInt(2, 3))), //
	Misc(new MaxUpgradesPerCategoryTRAn(new RangedAmountInt(5, 99))) // just ... anything undefined
	;

	public static final String NAME = "EquipmentUpgradeCategory";
	public static final EquipmentUpgradeCategory[] VALUES;
	public static final IndexToObjectBackmapping BACKMAPPING;
	public static final FactoryGeneric<EquipmentUpgradeCategory> FACTORY;
	static {
		VALUES = EquipmentUpgradeCategory.values();
		BACKMAPPING = (int i) -> VALUES[i];
		FACTORY = (GModality gm, Object nameOrID, Map<String, Object> constructorParameters) -> EquipmentUpgradeCategory
				.valueOf((String) nameOrID);
	}

	protected final MaxUpgradesPerCategory defaultMaxUpgradesPerCategory;

	private EquipmentUpgradeCategory(MaxUpgradesPerCategory defaultMaxUpgradesPerCategory) {
		this.defaultMaxUpgradesPerCategory = defaultMaxUpgradesPerCategory;
		defaultMaxUpgradesPerCategory.setUpgradeCategory(this);
	}

	@Override
	public int getIndex() {
		return ordinal();
	}

	@Override
	public String getName() {
		return name();
	}

	@Override
	public Long getID() {
		return (long) ordinal();
	}

	@Override
	public IndexToObjectBackmapping getFromIndexBackmapping() {
		return BACKMAPPING;
	}

	@Override
	public MaxUpgradesPerCategory getDefaultMaxUpgradesPerCategory() {
		return defaultMaxUpgradesPerCategory.clone();
	}

	@Override
	public boolean setID(Long newID) {
		// TODO CAN'T SET THE ID
		return false;
	}

	//

	// JSON-related

	//

	@Override
	public JSONValue toJSONValue() {
		return new JSONString(this.getName());
	}

	@Override
	public void toJSONValue(JSONObject wrapper) {
		wrapper.addField(FIELD_NAME, new JSONString(this.getName()));
	}
}