package games.theRisingAngel.enums;

import games.generic.controlModel.attributes.MaxUpgradesPerCategory;
import games.generic.controlModel.items.IEquipmentUpgradeCategory;
import games.generic.controlModel.misc.RangedAmountInt;

public enum EquipmentUpgradeCategory implements IEquipmentUpgradeCategory {
    // (corrisponde alla definizione di "fatto con..."),
    Material(new MaxUpgradesPerCategory(new RangedAmountInt(3, 7))), //
    // (good, light, ruined, encrusted, flexible,...),
    Structural(new MaxUpgradesPerCategory(new RangedAmountInt(2, 7))), //
    // ("rifiniture/raffinatezza", per esempio: rough, sharp, worn-out, with art
    // decoration, broken, fragile, thinner, bended),
    Refining(new MaxUpgradesPerCategory(new RangedAmountInt(4, 10))), //
    Coloring(new MaxUpgradesPerCategory(new RangedAmountInt(2, 4))), //
    Rune(new MaxUpgradesPerCategory(new RangedAmountInt(5, 10))), //
    Incantation(new MaxUpgradesPerCategory(new RangedAmountInt(7, 25))), // almost everything, even "of Traitors"
    Blessings(new MaxUpgradesPerCategory(new RangedAmountInt(2, 6))), //
    Incarnation(new MaxUpgradesPerCategory(new RangedAmountInt(1, 3))), // devine being, spirit, soul, essence, ...
    Elemental(new MaxUpgradesPerCategory(new RangedAmountInt(3, 15))), // (es: "Fire", "Water", "Air", "Earth", "Fresh Air", "Cold Air", "Lightning", "Nature", "Darkness", "Light", "Chaos", "Order", "Time", "Space", "Void", ...)
    Focusing(new MaxUpgradesPerCategory(new RangedAmountInt(5, 20))), // (es: "of Strength", ONLY Attributes)
    WorkersRolesRelated(new MaxUpgradesPerCategory(new RangedAmountInt(5, 20))), // of King, Blacksmith, Enchanter, Alchemist, Jeweler, Merchant, Hunter, Farmer, Fisherman, Cook, Scholar, Scribe, Priest, Thief, Assassin, Warrior, Mage, Archer, ...
    CharacterClass(new MaxUpgradesPerCategory(new RangedAmountInt(2, 7))), //
    CharacterRace(new MaxUpgradesPerCategory(new RangedAmountInt(1, 5))), //
    Tribal(new MaxUpgradesPerCategory(new RangedAmountInt(2, 3))), //
    Misc(new MaxUpgradesPerCategory(new RangedAmountInt(5, 99))) // just ... anything undefined
    ;

    protected static final IndexToObjectBackmapping itobm = (index) -> EquipmentUpgradeCategory.values()[index];

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
        return itobm;
    }

    public MaxUpgradesPerCategory getDefaultMaxUpgradesPerCategory() {
        return defaultMaxUpgradesPerCategory;
    }

    @Override
    public boolean setID(Long newID) {
        // TODO CAN'T SET THE ID
        return false;
    }

}