import fs from "fs";
import { loadConstants, RARITY_TRIBE_EQUIPMENT_PIECES } from "./constants";
import {
    EquipmentTypesTRAn,
    newEquipmentPieceForTribe,
    TribeEquipmentPieces,
    forEachRaritiesOfTribeAttributesVariations,
    forEachTribeAttributesInfluence,
    forEachTribeEquipmentPieces,
    newEquipUpgradesForTribeRarity,
    TRIBES_NAME_ATTRIBUTES_INFLUENCE,
    TRIBE_PIECE_OF_EQUIPMENT_SET_DATA,
    RARITY_NAMES,
    RARITY_TO_TRIBE_ATTRIBUTES_VARIATIONS,
    TribesNamesTRAn,
    loadEnums
} from "./enums";
import { AttributeMod_V01, DimensionInventory, EquipItem_V03, EquipUpgrade_V02, loadTypes } from "./types"
import { writeInFile } from "./utils";


// ----------------------

export type TribeAttributesInfluence = {
    tribeName: string;
    attributeBonus: string;
    attributeMalus: string;
}

/**
 * The AttributesVariation type is a bonus and a malus amounts designed to define
 * the TribeAttributesInfluence values
 */
export type AttributesVariation = {
    canBeEquipUpgrade: boolean;
    variationName: string;
    bonus: number;
    malus: number;
    addedPrice: number;
};

/**
 * The AttributesVariationsByRarity type is a mapping between rarities and AttributesVariation
 */
export type AttributesVariationsByRarity = {
    [rarity: number]: AttributesVariation
}

export type PieceOfEquipmentSetData = {
    namePrefix: string,
    type: EquipmentTypesTRAn,
    dimensionInventory: DimensionInventory,
    additionalAttributeModifiers: AttributeMod_V01[]
}



export type EquipUpgradeByRarity = {
    [rarity: number]: EquipUpgrade_V02
};



// ----------------------

/*
export const PAIR_GENERATOR: {
    newUpgradeSetByRarity: (pair: TribeAttributesInfluence) => EquipUpgradeByRarity,
    newEquipSet: (pair: TribeAttributesInfluence) => { [equipTypeName: string]: EquipItem_V03 }
} = {

    newUpgradeSetByRarity: (pair: TribeAttributesInfluence) => {
        const upgrades: { [rarity: number]: EquipUpgrade_V02 } = {};
        forEachRaritiesOfTribeAttributesVariations(
            (rarity: number, aen: AttributesVariation) => {
                const attrMod: AttributeModifications_V02 = {};
                const equipUp: EquipUpgrade_V02 = {
                    name: "",
                    rarity: 0,
                    price: [],
                    attributeModifiers: {}
                };

                attrMod[pair.attributeBonus] = aen.bonus;
                attrMod[pair.attributeMalus] = aen.malus;

                equipUp.name = `of ${pair.tribeName} ${aen.variationName}`;
                equipUp.rarity = rarity;
                equipUp.price = [aen.addedPrice];
                equipUp.attributeModifiers = attrMod;

                upgrades[rarity] = equipUp;
            }
        )
        return upgrades;
    },

    newEquipSet: (pair: TribeAttributesInfluence) => {
        const setOfEquips: { [equipTypeName: string]: EquipItem_V03 } = {};
        var equipPiece: EquipmentTypesTRAn;

        for (const piece of enumKeys(TribeEquipmentPieces)) {
            equipPiece = TribeEquipmentPieces[piece] as unknown as EquipmentTypesTRAn;
            setOfEquips[equipPiece] = newEquipmentPieceForTribe(pair.tribeName as TribesTRAn, equipPiece);
        }

        return setOfEquips;
    }
};
*/


// ----------------------




// ----------------------


export function generateJSONTribeInfluences(): void {
    writeInFile("./tribesName.json", Object.keys(TRIBES_NAME_ATTRIBUTES_INFLUENCE));
    writeInFile("./tribeAttributeInfluences.json", TRIBES_NAME_ATTRIBUTES_INFLUENCE);
}

export function generateJSONEquipmentUpgrades(): void {
    var equipUpgrades: Array<EquipUpgrade_V02> = [];
    forEachRaritiesOfTribeAttributesVariations((rarity: number, attrVar: AttributesVariation) => {
        if (attrVar.canBeEquipUpgrade) {
            forEachTribeAttributesInfluence(
                (attrInfluence) => {
                    equipUpgrades.push(newEquipUpgradesForTribeRarity(attrInfluence, rarity));
                }
            )
        }
    });
    writeInFile("./tribe_equipUpgrades.json", equipUpgrades);
}

export function generateJSONEquipments(): void {
    var equipPieces: Array<EquipItem_V03> = [];
    forEachTribeAttributesInfluence(tribe => {
        forEachTribeEquipmentPieces((equipPiece: TribeEquipmentPieces) => {
            equipPieces.push(
                newEquipmentPieceForTribe(tribe.tribeName as TribesNamesTRAn, equipPiece as unknown as EquipmentTypesTRAn)
            );
        });
    });
    writeInFile("./tribe_equipPieces.json", equipPieces);
}


export function toJava() {
    const SRC = "./java-gen/";
    if (!fs.existsSync(SRC)) {
        fs.mkdirSync(SRC);
    }

    // writeInFile(`${SRC}`, ``` ```);

    /*
        writeInFile(`${SRC}TribeEquipmentPiecesTRAn.java`,
    `
    package games.theRisingAngel.enums;
    
    import games.generic.controlModel.misc.IndexableObject;
    
    public enum TribeEquipmentPiecesTRAn implements IndexableObject {
        ${}(), //
        Head (${TribeEquipmentPieces.Head}), //
        Shoulder (${TribeEquipmentPieces.Shoulder}), //
        Chest (${TribeEquipmentPieces.Chest}), //
        Hands (${TribeEquipmentPieces.Hands}), //
        Bracelet (${TribeEquipmentPieces.Bracelet}), //
        Legs (${TribeEquipmentPieces.Legs}), //
        Feet (${TribeEquipmentPieces.Feet}) //
        ;
        public static final TribeEquipmentPiecesTRAn[] ALL_TRIBES_EQUIP_PIECES;
        public static final IndexToObjectBackmapping INDEX_TO_TRIBE_EQUIP_PIECES_TRAn;
        static {
            ALL_TRIBES_EQUIP_PIECES = TribeEquipmentPiecesTRAn.values();
            INDEX_TO_TRIBE_EQUIP_PIECES_TRAn = (int i) -> TribeEquipmentPiecesTRAn.ALL_TRIBES_EQUIP_PIECES[i];
        }
        
        @Override
        public int getIndex() { return this.ordinal(); }

        @Override
        public String getName() { return this.name(); }

        @Override
        public Long getID() { return (long) this.ordinal(); }

        @Override
        public IndexToObjectBackmapping getFromIndexBackmapping(){ return INDEX_TO_TRIBE_EQUIP_PIECES_TRAn; }
    }
    `
        );
    */

    writeInFile(`${SRC}TribesTRAn.java`,
        /*enumKeys(TribesNamesTRAn).join(", ") */
        `
package games.theRisingAngel.enums;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.awt.Dimension;

import dataStructures.MapTreeAVL;

import tools.Comparators;

import games.generic.controlModel.misc.IndexableObject;
import games.generic.controlModel.misc.IndexableObject.IndexToObjectBackmapping;
import games.generic.controlModel.misc.AttributeModification;
import games.generic.controlModel.misc.Currency;
import games.generic.controlModel.misc.CurrencySet;
import games.generic.controlModel.items.EquipmentItem;
import games.generic.controlModel.items.EquipmentUpgrade;
import games.generic.controlModel.subimpl.EquipmentUpgradeImpl;
import games.generic.controlModel.subimpl.GModalityRPG;

import games.theRisingAngel.inventory.EINotJewelry;

public class TribesTRAn {

    public static final Tribe[] ALL_TRIBES;
    public static final IndexToObjectBackmapping INDEX_TO_TRIBE_TRAn;
    public static final Map<TribeReligion, Tribe> MAP_RELIGION_TO_TRIBE;

    public static final RaritiesTRAn RARITY_TRIBE_EQUIPMENT_PIECES;
    public static final Map<RaritiesTRAn, AttributesVariationTribeEquip> MAP_RARITY_TO_ATTRIBUTE_UPGRADES_TRIBE;
    public static final Map<EquipmentTypesTRAn, PieceOfEquipmentSetData> MAP_EQUIPMENT_PIECE_TO_DATA_TRIBE;
    public static final Set<EquipmentTypesTRAn> ALL_EQUIP_TYPES_ON_TRIBE_SETS;
    public static final Set<RaritiesTRAn> ALL_EQUIP_UPGRADES_RARITIES;

    static {
        MapTreeAVL<RaritiesTRAn, AttributesVariationTribeEquip> mrta;
        MapTreeAVL<EquipmentTypesTRAn, PieceOfEquipmentSetData> metd;
        Map<TribeReligion, Tribe> mrtt;

        ALL_TRIBES = Tribe.values();
        INDEX_TO_TRIBE_TRAn = (int i) -> TribesTRAn.ALL_TRIBES[i];

        RARITY_TRIBE_EQUIPMENT_PIECES = RaritiesTRAn.HighQuality;

        mrta = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, RaritiesTRAn.COMPARATOR_RARITY_TRAn); 
        ${ //mapValues<{[equipPiece: string]: PieceOfEquipmentSetData},PieceOfEquipmentSetData>(TRIBE_PIECE_OF_EQUIPMENT_SET_DATA)
        Object.keys(RARITY_TO_TRIBE_ATTRIBUTES_VARIATIONS).map((rarityAsString, _, __) => Number(rarityAsString)).filter((n, _, __) => n !== undefined)
            .map((rarity, _, __) => {
                const av = RARITY_TO_TRIBE_ATTRIBUTES_VARIATIONS[rarity];
                return `
        mrta.put(RaritiesTRAn.${RARITY_NAMES[rarity]}, new AttributesVariationTribeEquip("${av.variationName}", ${av.bonus}, ${av.malus}, new int[]{${av.addedPrice}}, ${av.canBeEquipUpgrade}));`
            }
            ).join("")}
        ALL_EQUIP_UPGRADES_RARITIES = Collections.unmodifiableSet(mrta.keySet());
        MAP_RARITY_TO_ATTRIBUTE_UPGRADES_TRIBE = Collections.unmodifiableMap(mrta);

        metd = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, EquipmentTypesTRAn.COMPARATOR_EQUIP_TYPES_TRAn); 
        ${Object.keys(TRIBE_PIECE_OF_EQUIPMENT_SET_DATA)
            .map((equipName, _, __) => (equipName as unknown as TribeEquipmentPieces) as unknown as EquipmentTypesTRAn)
            .map((equipType, _, __) => {
                const ped: PieceOfEquipmentSetData = TRIBE_PIECE_OF_EQUIPMENT_SET_DATA[equipType];
                return `
        metd.put(EquipmentTypesTRAn.${ped.type},
            new PieceOfEquipmentSetData("${ped.namePrefix}", EquipmentTypesTRAn.${ped.type}, new Dimension(${ped.dimensionInventory.width}, ${ped.dimensionInventory.height}), //\n\t\tnew AttributeModification[]{${ped.additionalAttributeModifiers.map((am, _, __) => `new AttributeModification(AttributesTRAn.${am.attribute},${am.value})`).join(", ")}}));`
            }
            ).join("")}
        ALL_EQUIP_TYPES_ON_TRIBE_SETS = Collections.unmodifiableSet(metd.keySet());
        MAP_EQUIPMENT_PIECE_TO_DATA_TRIBE = Collections.unmodifiableMap(metd);

        mrtt = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, TribeReligion.COMPARATOR_TRIBE_RELIGION);
        for(Tribe t : ALL_TRIBES){
            mrtt.put(t.religion, t);
        }
        MAP_RELIGION_TO_TRIBE = Collections.unmodifiableMap(mrtt);
    }

    //

    public static enum Tribe implements IndexableObject {
        ${Object.keys(TRIBES_NAME_ATTRIBUTES_INFLUENCE).map((tribeName, _, __) => {
                const infl = TRIBES_NAME_ATTRIBUTES_INFLUENCE[tribeName];
                return `${tribeName}(new TribeReligion(AttributesTRAn.${infl.attributeBonus}, AttributesTRAn.${infl.attributeMalus}))`
            }).join(", //\n\t\t")};

        //

        Tribe(TribeReligion religion){
            this.religion = religion;
        }

        protected final TribeReligion religion;

        @Override
        public int getIndex() { return this.ordinal(); }

        @Override
        public String getName() { return this.name(); }

        @Override
        public Long getID() { return (long) this.ordinal(); }

        public TribeReligion getReligion(){ return this.religion; }

        @Override
        public IndexToObjectBackmapping getFromIndexBackmapping(){ return INDEX_TO_TRIBE_TRAn; }

        // GENERATORS

        public EquipmentUpgrade newEquipmentUpgradeForRarity(RaritiesTRAn rar, GModalityRPG gmrpg){
            int n;
            EquipmentUpgrade eu;
            AttributesVariationTribeEquip variation;
            CurrencySet cs;
            Currency[] currencies;
            TribeReligion rel;

            rel = this.religion;
            variation = MAP_RARITY_TO_ATTRIBUTE_UPGRADES_TRIBE.get(rar);
            eu = new EquipmentUpgradeImpl(rar.getIndex(), getNameEquipUgradeFor(this, rar));
            
            eu.addAttributeModifier(new AttributeModification(rel.religionDevotedTo, variation.bonus));
            eu.addAttributeModifier(new AttributeModification(rel.religionHated, variation.malus));
            
            cs = gmrpg.newCurrencyHolder();
            currencies = cs.getCurrencies();
            cs.setGameModaliy(gmrpg);
            n = variation.addedPrices.length;
            while (--n >= 0) {
                cs.setCurrencyAmount(currencies[n], variation.addedPrices[n]);
            }
            eu.setPricesModifications(cs);
            
            return eu;
        }
        
        public MapTreeAVL<EquipmentTypesTRAn, EquipmentItem > newEquipmentSet(GModalityRPG gmrpg){
            final MapTreeAVL<EquipmentTypesTRAn, EquipmentItem > m;
            final AttributesVariationTribeEquip variation;
            final Tribe thisTribe;
            TribeReligion rel;

            rel = (thisTribe = this).religion;
            
            m = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration, EquipmentTypesTRAn.COMPARATOR_EQUIP_TYPES_TRAn); 

            variation = MAP_RARITY_TO_ATTRIBUTE_UPGRADES_TRIBE.get(RARITY_TRIBE_EQUIPMENT_PIECES);
                
            MAP_EQUIPMENT_PIECE_TO_DATA_TRIBE.forEach( (eqType, ped) -> {
                int n;
                AttributeModification[] allAttributes;
                EquipmentItem equipPiece;
                CurrencySet cs;
                Currency[] currencies;

                allAttributes = new AttributeModification[2 + ped.additionalAttributeModifiers.length];
                allAttributes[0] = new AttributeModification(rel.religionDevotedTo, variation.bonus);
                allAttributes[1] = new AttributeModification(rel.religionHated, variation.malus);
                System.arraycopy(ped.additionalAttributeModifiers, 0, allAttributes, 2, ped.additionalAttributeModifiers.length);

                equipPiece = new EINotJewelry(gmrpg, eqType, getNameEquipFor(thisTribe, eqType), allAttributes);

                equipPiece.setDimensionInInventory(ped.dimensionInventory);

                cs = gmrpg.newCurrencyHolder();
                currencies = cs.getCurrencies();
                cs.setGameModaliy(gmrpg);
                n = variation.addedPrices.length;
                while (--n >= 0) {
                    cs.setCurrencyAmount(currencies[n], variation.addedPrices[n] << 1);
                }
                equipPiece.setSellPrice(cs);

                m.put(eqType, equipPiece);
            });

            return m;
        }

        
        public TribeWarStatusTRAn getWarsStatusWith(TribesTRAn.Tribe otherTribe) {
            TribeReligion tr, or;
            if (otherTribe == null || otherTribe == this) { return TribeWarStatusTRAn.Neutral; }
            tr = this.religion;
            or = otherTribe.religion;
            if (isWorstEnemy(otherTribe) || tr.religionDevotedTo == or.religionHated
                    || tr.religionHated == or.religionDevotedTo) {
                return TribeWarStatusTRAn.War;
            } else if (tr.religionDevotedTo == or.religionDevotedTo || tr.religionHated == or.religionHated) {
                return TribeWarStatusTRAn.Ally;
            }
            return TribeWarStatusTRAn.Neutral;
        }

        public boolean isWorstEnemy(TribesTRAn.Tribe otherTribe) {
            TribeReligion tr, or;
            tr = this.religion;
            or = otherTribe.religion;
            return (tr.religionDevotedTo == or.religionHated) && (tr.religionHated == or.religionDevotedTo);
        }
    }

    //

    
    public static enum TribeWarStatusTRAn implements IndexableObject {
        Neutral, War, Ally;

        public static final TribeWarStatusTRAn[] ALL_TRIBE_WAR_STATUS;
        public static final IndexToObjectBackmapping INDEX_TO_TRIBE_WAR_STATUS;

        static {
            ALL_TRIBE_WAR_STATUS = TribeWarStatusTRAn.values();
            INDEX_TO_TRIBE_WAR_STATUS = (int i) -> ALL_TRIBE_WAR_STATUS[i];
        }

        @Override
        public Long getID() { return (long) this.ordinal(); }

        @Override
        public String getName() { return this.name(); }

        @Override
        public int getIndex() { return this.ordinal(); }

        @Override
        public IndexToObjectBackmapping getFromIndexBackmapping() { return INDEX_TO_TRIBE_WAR_STATUS; }
    }

    //

    //

    public static Tribe getTribeByReligion(TribeReligion religion){
        if(religion == null){ return null; }
        return MAP_RELIGION_TO_TRIBE.get(religion);
    }

    public static String getNameEquipUgradeFor(Tribe tribe, RaritiesTRAn rarity){
        AttributesVariationTribeEquip variation;
        variation = MAP_RARITY_TO_ATTRIBUTE_UPGRADES_TRIBE.get(rarity);
        if(variation == null){ throw new IllegalArgumentException("Not accepted rarity: " + rarity); }
        return "of " + tribe.getName() + " Tribe " + variation.getVariationName();
    }


    public static String getNameEquipFor(Tribe tribe, EquipmentTypesTRAn equipType){
        PieceOfEquipmentSetData ped;
        ped = MAP_EQUIPMENT_PIECE_TO_DATA_TRIBE.get(equipType);
        if(ped == null){ throw new IllegalArgumentException("Not accepted equipment type: " + equipType); }
        return ped.namePrefix + " Cosplay of " + tribe.getName() + " Tribe";
    }

    //

    public static final class TribeReligion {
        public static final Comparator<TribeReligion> COMPARATOR_TRIBE_RELIGION = (r1, r2) -> {
            int res;
            if (r1 == r2) { return 0; }
            if (r1 == null) { return -1; }
            if (r2 == null) { return 1; }
            res = Comparators.LONG_COMPARATOR.compare(r1.religionDevotedTo.getID(), r2.religionDevotedTo.getID());
            if(res != 0){ return res; }
            return Comparators.LONG_COMPARATOR.compare(r1.religionHated.getID(), r2.religionHated.getID());
        };
        public TribeReligion(AttributesTRAn religionDevotedTo, AttributesTRAn religionHated) {
            super();
            if(religionDevotedTo == null || religionHated == null || religionDevotedTo == religionHated){
                throw new IllegalArgumentException("Can't praise and hate a null religion or the same religions: <"+ religionDevotedTo + ", " + religionHated + ">");
            }
            this.religionDevotedTo = religionDevotedTo;
            this.religionHated = religionHated;
        }
        protected final AttributesTRAn religionDevotedTo, religionHated;
        //
        public AttributesTRAn getReligionDevotedTo() { return religionDevotedTo; }
        public AttributesTRAn getReligionHated() { return religionHated; }
        @Override
        public String toString() {
            return "TribeReligion [religionDevotedTo=" + religionDevotedTo + ", religionHated=" + religionHated + "]";
        }
    }

    public static class AttributesVariationTribeEquip {
        public AttributesVariationTribeEquip(String variationName, int bonus, int malus, int[] addedPrices,
                boolean canBeEquipUpgrade) {
            super();
            this.variationName = variationName;
            this.bonus = bonus;
            this.malus = malus;
            this.addedPrices = addedPrices;
            this.canBeEquipUpgrade = canBeEquipUpgrade;
        }
        protected boolean canBeEquipUpgrade;
        protected int bonus;
        protected int malus;
        protected int[] addedPrices;
        protected String variationName;
        //
        public String getVariationName() { return variationName; }
        public int getBonus() { return bonus; }
        public int getMalus() { return malus; }
        public int[] getAddedPrices() { return addedPrices; }
        public boolean isCanBeEquipUpgrade() { return canBeEquipUpgrade; }
        //
//      public void setVariationName(String variationName) { this.variationName = variationName; }
//      public void setBonus(int bonus) { this.bonus = bonus; }
//      public void setMalus(int malus) { this.malus = malus; }
//      public void setAddedPrices(int[] addedPrices) { this.addedPrices = addedPrices; }
//      public void setCanBeEquipUpgrade(boolean canBeEquipUpgrade) { this.canBeEquipUpgrade = canBeEquipUpgrade; }
    }

    //

    public static class PieceOfEquipmentSetData {
        public PieceOfEquipmentSetData(String namePrefix, EquipmentTypesTRAn equipType, Dimension dimensionInventory, AttributeModification[] additionalAttributeModifiers) {
            super();
            this.namePrefix = namePrefix;
            this.equipType = equipType;
            this.dimensionInventory = dimensionInventory;
            this.additionalAttributeModifiers = additionalAttributeModifiers;
        }
        protected String namePrefix;
        protected EquipmentTypesTRAn equipType;
        protected Dimension dimensionInventory;
        protected AttributeModification[] additionalAttributeModifiers;
        //
        public String getNamePrefix() { return namePrefix; }
        public EquipmentTypesTRAn getEquipType() { return equipType; }
        public Dimension getDimensionInventory() { return dimensionInventory; }
        public AttributeModification[] getAdditionalAttributeModifiers() { return additionalAttributeModifiers; }
        //
        // public void setNamePrefix(String namePrefix) { this.namePrefix = namePrefix; }
        // public void setEquipType(EquipmentTypesTRAn equipType) { this.equipType = equipType; }
        // public void setDimensionInventory(Dimension dimensionInventory) { this.dimensionInventory = dimensionInventory; }
        // public void setAdditionalAttributeModifiers(AttributeModification[] additionalAttributeModifiers) { this.additionalAttributeModifiers = additionalAttributeModifiers; }
    }
}`);
}


//

console.log("START combinatory generator about TribeAttributesInfluence");

const loaders: Array<(() => void)> = [loadTypes, loadEnums, loadConstants];

for (const loader of loaders) {
    loader();
}

/*
generateJSONTribeInfluences();
generateJSONEquipmentUpgrades();
generateJSONEquipments();
*/
toJava();

console.log("END combinatory generator about TribeAttributesInfluence");