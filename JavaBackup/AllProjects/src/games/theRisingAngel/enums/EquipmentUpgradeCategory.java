package games.theRisingAngel.enums;

import games.generic.controlModel.misc.IndexableObject;

public enum EquipmentUpgradeCategory extends IndexableObject {
    Material, // (corrisponde alla definizione di "fatto con..."),
    Structural, // (good, light, ruined, encrusted, flexible,...),
    Refining, // ("rifiniture/raffinatezza", per esempio: rough, sharp, wornout, with art decoratino, broken, fragile, thinner, bended),
    Rune, //
    CharacterClass, //
    CharacterRace, //
    Blessings, //
    Coloring, //
    Incarnation, //,
    Focusing, //(es: "of Strength")
    Tribal,
    Misc // just ... anything undefined
    ;


    @Override
    public int getIndex() { return ordinal(); }

    @Override
    public String getName() { return name(); }

    @Override
    public Long getID() { return (long) ordinal(); }

}