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
    Incantation(new MaxUpgradesPerCategory(new RangedAmountInt(7, 20))), //
    Blessings(new MaxUpgradesPerCategory(new RangedAmountInt(2, 6))), //
    Incarnation(new MaxUpgradesPerCategory(new RangedAmountInt(1, 3))), // ,
    Focusing(new MaxUpgradesPerCategory(new RangedAmountInt(5, 10))), // (es: "of Strength")
    CharacterClass(new MaxUpgradesPerCategory(new RangedAmountInt(1, 3))), //
    CharacterRace(new MaxUpgradesPerCategory(new RangedAmountInt(1, 3))), //
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