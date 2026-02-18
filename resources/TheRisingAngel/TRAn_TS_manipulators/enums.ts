import { RARITY_TRIBE_EQUIPMENT_PIECES, RARITY_TRIBE_EQUIPMENT_PIECES_AS_NUMBER } from "./constants";
import { AttributesVariation, PieceOfEquipmentSetData, TribeAttributesInfluence } from "./tribes_equip_set_upgrades_V03";
import { AttributeModifications_V02, EquipItem_V03, EquipUpgrade_V02 } from "./types";
import { enumKeys } from "./utils";

export function loadEnums() {
    console.log("ENUMS :D");
}

export enum RaritiesTRAn {
    Scrap = 0,
    Common,
    WellManifactured,
    HighQuality,
    Rare,
    Epic,
    Legendary
}

export enum EquipmentTypesTRAn {
    Earrings = "Earrings",
    Head = "Head",
    Shoulder = "Shoulder",
    Hands = "Hands",
    Chest = "Chest",
    Arms = "Arms",
    Feet = "Feet",
    Belt = "Belt",
    Legs = "Legs",
    MainWeapon = "MainWeapon",
    Special = "Special",
    SecodaryWeapon = "SecodaryWeapon",
    Necklace = "Necklace",
    Bracelet = "Bracelet",
    Ring = "Ring",
}

export enum AttributesTRAn {
    Strength = "Strength", Constitution = "Constitution", Health = "Health", //
    Defense = "Defense", Dexterity = "Dexterity", Precision = "Precision", //
    Intelligence = "Intelligence", Wisdom = "Wisdom", Faith = "Faith",
    //
    Luck = "Luck", Velocity = "Velocity", //
    LifeMax = "LifeMax", LifeRegen = "LifeRegen", ManaMax = "ManaMax", ManaRegen = "ManaRegen", ShieldMax = "ShieldMax", ShieldRegen = "ShieldRegen", StaminaMax = "StaminaMax", StaminaRegen = "StaminaRegen", //
    //
    PhysicalDamageBonus = "PhysicalDamageBonus", PhysicalDamageReduction = "PhysicalDamageReduction", //
    PhysicalProbabilityPerThousandHit = "PhysicalProbabilityPerThousandHit", PhysicalProbabilityPerThousandAvoid = "PhysicalProbabilityPerThousandAvoid", //
    VelocityAttackStrikePercentage = "VelocityAttackStrikePercentage", //
    MagicalDamageBonus = "MagicalDamageBonus", MagicalDamageReduction = "MagicalDamageReduction", //
    MagicalProbabilityPerThousandHit = "MagicalProbabilityPerThousandHit", MagicalProbabilityPerThousandAvoid = "MagicalProbabilityPerThousandAvoid", //
    VelocitySpellCastPercentage = "VelocitySpellCastPercentage", //
    CostCastReductionPercentage = "CostCastReductionPercentage", //
    //
    CriticalProbabilityPerThousandHit = "CriticalProbabilityPerThousandHit", CriticalMultiplierPercentage = "CriticalMultiplierPercentage", //
    CriticalProbabilityPerThousandAvoid = "CriticalProbabilityPerThousandAvoid", CriticalMultiplierPercentageReduction = "CriticalMultiplierPercentageReduction", //
    LifeLeechPercentage = "LifeLeechPercentage", ManaLeechPercentage = "ManaLeechPercentage", ShieldLeechPercentage0 = "ShieldLeechPercentage0", //
    ReflectionDamagePercentage = "ReflectionDamagePercentage", ExperienceBonusPercentage = "ExperienceBonusPercentage",
}


export enum TribesNamesTRAn {
    Asexiso = "Asexiso",
    Buavoj = "Buavoj",
    Cobahir = "Cobahir",
    Cugujab = "Cugujab",
    Depewuv = "Depewuv",
    Difod = "Difod",
    Dokosok = "Dokosok",
    Domonoleg = "Domonoleg",
    Drovovomir = "Drovovomir",
    Fidnox = "Fidnox",
    Folottuj = "Folottuj",
    Gamaskov = "Gamaskov",
    Gangeco = "Gangeco",
    Gejenaff = "Gejenaff",
    Gizix = "Gizix",
    Gokuq = "Gokuq",
    Guwaddarg = "Guwaddarg",
    Hansyn = "Hansyn",
    Hapebz = "Hapebz",
    Haquaks = "Haquaks",
    Hitifa = "Hitifa",
    Hoxoxzeo = "Hoxoxzeo",
    Hugupad = "Hugupad",
    Icibiup = "Icibiup",
    Innizay = "Innizay",
    Jaonuvup = "Jaonuvup",
    Jawueqk = "Jawueqk",
    Jikitiwumo = "Jikitiwumo",
    Kantiw = "Kantiw",
    Kefmah = "Kefmah",
    Kemaf = "Kemaf",
    Lamahaya = "Lamahaya",
    Lezerto = "Lezerto",
    Lijislaer = "Lijislaer",
    Liwed = "Liwed",
    Maxibos = "Maxibos",
    Mijumey = "Mijumey",
    Monuhzum = "Monuhzum",
    Muffinex = "Muffinex",
    Muvuduk = "Muvuduk",
    Noguev = "Noguev",
    Poltodka = "Poltodka",
    Pomaquox = "Pomaquox",
    Puwazok = "Puwazok",
    Quapaq = "Quapaq",
    Quazahnbokt = "Quazahnbokt",
    Ruhujiqk = "Ruhujiqk",
    Ruwizodup = "Ruwizodup",
    Suzurt = "Suzurt",
    Tamawat = "Tamawat",
    Telleno = "Telleno",
    Tevesimag = "Tevesimag",
    Tocomh = "Tocomh",
    Udigutun = "Udigutun",
    Ujuymey = "Ujuymey",
    Ulubaw = "Ulubaw",
    Vajyrfo = "Vajyrfo",
    Viwuahz = "Viwuahz",
    Warfays = "Warfays",
    Weakimit = "Weakimit",
    Wujoweg = "Wujoweg",
    Wunocogdi = "Wunocogdi",
    Xakonokork = "Xakonokork",
    Xayewi = "Xayewi",
    Xecadyma = "Xecadyma",
    Xenagon = "Xenagon",
    Yaexuf = "Yaexuf",
    Yaqenohz = "Yaqenohz",
    Yikipq = "Yikipq",
    Zanomozosya = "Zanomozosya",
    Zasitecyo = "Zasitecyo",
    Zuwidon = "Zuwidon"
}

export enum TribeEquipmentPieces {
    Head = EquipmentTypesTRAn.Head,
    Shoulder = EquipmentTypesTRAn.Shoulder,
    Chest = EquipmentTypesTRAn.Chest,
    Hands = EquipmentTypesTRAn.Hands,
    Bracelet = EquipmentTypesTRAn.Bracelet,
    Legs = EquipmentTypesTRAn.Legs,
    Feet = EquipmentTypesTRAn.Feet,
}

//

export const TRIBES_NAME_ATTRIBUTES_INFLUENCE: { [tribeName: string]: TribeAttributesInfluence } =
    (() => {
        const m: { [tribeName: string]: TribeAttributesInfluence } = {};

        m[TribesNamesTRAn.Asexiso] = { tribeName: TribesNamesTRAn.Asexiso, attributeBonus: AttributesTRAn.Strength, attributeMalus: AttributesTRAn.Constitution };
        m[TribesNamesTRAn.Buavoj] = { tribeName: TribesNamesTRAn.Buavoj, attributeBonus: AttributesTRAn.Strength, attributeMalus: AttributesTRAn.Health };
        m[TribesNamesTRAn.Cobahir] = { tribeName: TribesNamesTRAn.Cobahir, attributeBonus: AttributesTRAn.Strength, attributeMalus: AttributesTRAn.Defense };
        m[TribesNamesTRAn.Cugujab] = { tribeName: TribesNamesTRAn.Cugujab, attributeBonus: AttributesTRAn.Strength, attributeMalus: AttributesTRAn.Dexterity };
        m[TribesNamesTRAn.Depewuv] = { tribeName: TribesNamesTRAn.Depewuv, attributeBonus: AttributesTRAn.Strength, attributeMalus: AttributesTRAn.Precision };
        m[TribesNamesTRAn.Difod] = { tribeName: TribesNamesTRAn.Difod, attributeBonus: AttributesTRAn.Strength, attributeMalus: AttributesTRAn.Intelligence };
        m[TribesNamesTRAn.Dokosok] = { tribeName: TribesNamesTRAn.Dokosok, attributeBonus: AttributesTRAn.Strength, attributeMalus: AttributesTRAn.Wisdom };
        m[TribesNamesTRAn.Domonoleg] = { tribeName: TribesNamesTRAn.Domonoleg, attributeBonus: AttributesTRAn.Strength, attributeMalus: AttributesTRAn.Faith };
        m[TribesNamesTRAn.Drovovomir] = { tribeName: TribesNamesTRAn.Drovovomir, attributeBonus: AttributesTRAn.Constitution, attributeMalus: AttributesTRAn.Strength };
        m[TribesNamesTRAn.Fidnox] = { tribeName: TribesNamesTRAn.Fidnox, attributeBonus: AttributesTRAn.Constitution, attributeMalus: AttributesTRAn.Health };
        m[TribesNamesTRAn.Folottuj] = { tribeName: TribesNamesTRAn.Folottuj, attributeBonus: AttributesTRAn.Constitution, attributeMalus: AttributesTRAn.Defense };
        m[TribesNamesTRAn.Gamaskov] = { tribeName: TribesNamesTRAn.Gamaskov, attributeBonus: AttributesTRAn.Constitution, attributeMalus: AttributesTRAn.Dexterity };
        m[TribesNamesTRAn.Gangeco] = { tribeName: TribesNamesTRAn.Gangeco, attributeBonus: AttributesTRAn.Constitution, attributeMalus: AttributesTRAn.Precision };
        m[TribesNamesTRAn.Gejenaff] = { tribeName: TribesNamesTRAn.Gejenaff, attributeBonus: AttributesTRAn.Constitution, attributeMalus: AttributesTRAn.Intelligence };
        m[TribesNamesTRAn.Gizix] = { tribeName: TribesNamesTRAn.Gizix, attributeBonus: AttributesTRAn.Constitution, attributeMalus: AttributesTRAn.Wisdom };
        m[TribesNamesTRAn.Gokuq] = { tribeName: TribesNamesTRAn.Gokuq, attributeBonus: AttributesTRAn.Constitution, attributeMalus: AttributesTRAn.Faith };
        m[TribesNamesTRAn.Guwaddarg] = { tribeName: TribesNamesTRAn.Guwaddarg, attributeBonus: AttributesTRAn.Health, attributeMalus: AttributesTRAn.Strength };
        m[TribesNamesTRAn.Hansyn] = { tribeName: TribesNamesTRAn.Hansyn, attributeBonus: AttributesTRAn.Health, attributeMalus: AttributesTRAn.Constitution };
        m[TribesNamesTRAn.Hapebz] = { tribeName: TribesNamesTRAn.Hapebz, attributeBonus: AttributesTRAn.Health, attributeMalus: AttributesTRAn.Defense };
        m[TribesNamesTRAn.Haquaks] = { tribeName: TribesNamesTRAn.Haquaks, attributeBonus: AttributesTRAn.Health, attributeMalus: AttributesTRAn.Dexterity };
        m[TribesNamesTRAn.Hitifa] = { tribeName: TribesNamesTRAn.Hitifa, attributeBonus: AttributesTRAn.Health, attributeMalus: AttributesTRAn.Precision };
        m[TribesNamesTRAn.Hoxoxzeo] = { tribeName: TribesNamesTRAn.Hoxoxzeo, attributeBonus: AttributesTRAn.Health, attributeMalus: AttributesTRAn.Intelligence };
        m[TribesNamesTRAn.Hugupad] = { tribeName: TribesNamesTRAn.Hugupad, attributeBonus: AttributesTRAn.Health, attributeMalus: AttributesTRAn.Wisdom };
        m[TribesNamesTRAn.Icibiup] = { tribeName: TribesNamesTRAn.Icibiup, attributeBonus: AttributesTRAn.Health, attributeMalus: AttributesTRAn.Faith };
        m[TribesNamesTRAn.Innizay] = { tribeName: TribesNamesTRAn.Innizay, attributeBonus: AttributesTRAn.Defense, attributeMalus: AttributesTRAn.Strength };
        m[TribesNamesTRAn.Jaonuvup] = { tribeName: TribesNamesTRAn.Jaonuvup, attributeBonus: AttributesTRAn.Defense, attributeMalus: AttributesTRAn.Constitution };
        m[TribesNamesTRAn.Jawueqk] = { tribeName: TribesNamesTRAn.Jawueqk, attributeBonus: AttributesTRAn.Defense, attributeMalus: AttributesTRAn.Health };
        m[TribesNamesTRAn.Jikitiwumo] = { tribeName: TribesNamesTRAn.Jikitiwumo, attributeBonus: AttributesTRAn.Defense, attributeMalus: AttributesTRAn.Dexterity };
        m[TribesNamesTRAn.Kantiw] = { tribeName: TribesNamesTRAn.Kantiw, attributeBonus: AttributesTRAn.Defense, attributeMalus: AttributesTRAn.Precision };
        m[TribesNamesTRAn.Kefmah] = { tribeName: TribesNamesTRAn.Kefmah, attributeBonus: AttributesTRAn.Defense, attributeMalus: AttributesTRAn.Intelligence };
        m[TribesNamesTRAn.Kemaf] = { tribeName: TribesNamesTRAn.Kemaf, attributeBonus: AttributesTRAn.Defense, attributeMalus: AttributesTRAn.Wisdom };
        m[TribesNamesTRAn.Lamahaya] = { tribeName: TribesNamesTRAn.Lamahaya, attributeBonus: AttributesTRAn.Defense, attributeMalus: AttributesTRAn.Faith };
        m[TribesNamesTRAn.Lezerto] = { tribeName: TribesNamesTRAn.Lezerto, attributeBonus: AttributesTRAn.Dexterity, attributeMalus: AttributesTRAn.Strength };
        m[TribesNamesTRAn.Lijislaer] = { tribeName: TribesNamesTRAn.Lijislaer, attributeBonus: AttributesTRAn.Dexterity, attributeMalus: AttributesTRAn.Constitution };
        m[TribesNamesTRAn.Liwed] = { tribeName: TribesNamesTRAn.Liwed, attributeBonus: AttributesTRAn.Dexterity, attributeMalus: AttributesTRAn.Health };
        m[TribesNamesTRAn.Maxibos] = { tribeName: TribesNamesTRAn.Maxibos, attributeBonus: AttributesTRAn.Dexterity, attributeMalus: AttributesTRAn.Defense };
        m[TribesNamesTRAn.Mijumey] = { tribeName: TribesNamesTRAn.Mijumey, attributeBonus: AttributesTRAn.Dexterity, attributeMalus: AttributesTRAn.Precision };
        m[TribesNamesTRAn.Monuhzum] = { tribeName: TribesNamesTRAn.Monuhzum, attributeBonus: AttributesTRAn.Dexterity, attributeMalus: AttributesTRAn.Intelligence };
        m[TribesNamesTRAn.Muffinex] = { tribeName: TribesNamesTRAn.Muffinex, attributeBonus: AttributesTRAn.Dexterity, attributeMalus: AttributesTRAn.Wisdom };
        m[TribesNamesTRAn.Muvuduk] = { tribeName: TribesNamesTRAn.Muvuduk, attributeBonus: AttributesTRAn.Dexterity, attributeMalus: AttributesTRAn.Faith };
        m[TribesNamesTRAn.Noguev] = { tribeName: TribesNamesTRAn.Noguev, attributeBonus: AttributesTRAn.Precision, attributeMalus: AttributesTRAn.Strength };
        m[TribesNamesTRAn.Poltodka] = { tribeName: TribesNamesTRAn.Poltodka, attributeBonus: AttributesTRAn.Precision, attributeMalus: AttributesTRAn.Constitution };
        m[TribesNamesTRAn.Pomaquox] = { tribeName: TribesNamesTRAn.Pomaquox, attributeBonus: AttributesTRAn.Precision, attributeMalus: AttributesTRAn.Health };
        m[TribesNamesTRAn.Puwazok] = { tribeName: TribesNamesTRAn.Puwazok, attributeBonus: AttributesTRAn.Precision, attributeMalus: AttributesTRAn.Defense };
        m[TribesNamesTRAn.Quapaq] = { tribeName: TribesNamesTRAn.Quapaq, attributeBonus: AttributesTRAn.Precision, attributeMalus: AttributesTRAn.Dexterity };
        m[TribesNamesTRAn.Quazahnbokt] = { tribeName: TribesNamesTRAn.Quazahnbokt, attributeBonus: AttributesTRAn.Precision, attributeMalus: AttributesTRAn.Intelligence };
        m[TribesNamesTRAn.Ruhujiqk] = { tribeName: TribesNamesTRAn.Ruhujiqk, attributeBonus: AttributesTRAn.Precision, attributeMalus: AttributesTRAn.Wisdom };
        m[TribesNamesTRAn.Ruwizodup] = { tribeName: TribesNamesTRAn.Ruwizodup, attributeBonus: AttributesTRAn.Precision, attributeMalus: AttributesTRAn.Faith };
        m[TribesNamesTRAn.Suzurt] = { tribeName: TribesNamesTRAn.Suzurt, attributeBonus: AttributesTRAn.Intelligence, attributeMalus: AttributesTRAn.Strength };
        m[TribesNamesTRAn.Tamawat] = { tribeName: TribesNamesTRAn.Tamawat, attributeBonus: AttributesTRAn.Intelligence, attributeMalus: AttributesTRAn.Constitution };
        m[TribesNamesTRAn.Telleno] = { tribeName: TribesNamesTRAn.Telleno, attributeBonus: AttributesTRAn.Intelligence, attributeMalus: AttributesTRAn.Health };
        m[TribesNamesTRAn.Tevesimag] = { tribeName: TribesNamesTRAn.Tevesimag, attributeBonus: AttributesTRAn.Intelligence, attributeMalus: AttributesTRAn.Defense };
        m[TribesNamesTRAn.Tocomh] = { tribeName: TribesNamesTRAn.Tocomh, attributeBonus: AttributesTRAn.Intelligence, attributeMalus: AttributesTRAn.Dexterity };
        m[TribesNamesTRAn.Udigutun] = { tribeName: TribesNamesTRAn.Udigutun, attributeBonus: AttributesTRAn.Intelligence, attributeMalus: AttributesTRAn.Precision };
        m[TribesNamesTRAn.Ujuymey] = { tribeName: TribesNamesTRAn.Ujuymey, attributeBonus: AttributesTRAn.Intelligence, attributeMalus: AttributesTRAn.Wisdom };
        m[TribesNamesTRAn.Ulubaw] = { tribeName: TribesNamesTRAn.Ulubaw, attributeBonus: AttributesTRAn.Intelligence, attributeMalus: AttributesTRAn.Faith };
        m[TribesNamesTRAn.Vajyrfo] = { tribeName: TribesNamesTRAn.Vajyrfo, attributeBonus: AttributesTRAn.Wisdom, attributeMalus: AttributesTRAn.Strength };
        m[TribesNamesTRAn.Viwuahz] = { tribeName: TribesNamesTRAn.Viwuahz, attributeBonus: AttributesTRAn.Wisdom, attributeMalus: AttributesTRAn.Constitution };
        m[TribesNamesTRAn.Warfays] = { tribeName: TribesNamesTRAn.Warfays, attributeBonus: AttributesTRAn.Wisdom, attributeMalus: AttributesTRAn.Health };
        m[TribesNamesTRAn.Weakimit] = { tribeName: TribesNamesTRAn.Weakimit, attributeBonus: AttributesTRAn.Wisdom, attributeMalus: AttributesTRAn.Defense };
        m[TribesNamesTRAn.Wujoweg] = { tribeName: TribesNamesTRAn.Wujoweg, attributeBonus: AttributesTRAn.Wisdom, attributeMalus: AttributesTRAn.Dexterity };
        m[TribesNamesTRAn.Wunocogdi] = { tribeName: TribesNamesTRAn.Wunocogdi, attributeBonus: AttributesTRAn.Wisdom, attributeMalus: AttributesTRAn.Precision };
        m[TribesNamesTRAn.Xakonokork] = { tribeName: TribesNamesTRAn.Xakonokork, attributeBonus: AttributesTRAn.Wisdom, attributeMalus: AttributesTRAn.Intelligence };
        m[TribesNamesTRAn.Xayewi] = { tribeName: TribesNamesTRAn.Xayewi, attributeBonus: AttributesTRAn.Wisdom, attributeMalus: AttributesTRAn.Faith };
        m[TribesNamesTRAn.Xecadyma] = { tribeName: TribesNamesTRAn.Xecadyma, attributeBonus: AttributesTRAn.Faith, attributeMalus: AttributesTRAn.Strength };
        m[TribesNamesTRAn.Xenagon] = { tribeName: TribesNamesTRAn.Xenagon, attributeBonus: AttributesTRAn.Faith, attributeMalus: AttributesTRAn.Constitution };
        m[TribesNamesTRAn.Yaexuf] = { tribeName: TribesNamesTRAn.Yaexuf, attributeBonus: AttributesTRAn.Faith, attributeMalus: AttributesTRAn.Health };
        m[TribesNamesTRAn.Yaqenohz] = { tribeName: TribesNamesTRAn.Yaqenohz, attributeBonus: AttributesTRAn.Faith, attributeMalus: AttributesTRAn.Defense };
        m[TribesNamesTRAn.Yikipq] = { tribeName: TribesNamesTRAn.Yikipq, attributeBonus: AttributesTRAn.Faith, attributeMalus: AttributesTRAn.Dexterity };
        m[TribesNamesTRAn.Zanomozosya] = { tribeName: TribesNamesTRAn.Zanomozosya, attributeBonus: AttributesTRAn.Faith, attributeMalus: AttributesTRAn.Precision };
        m[TribesNamesTRAn.Zasitecyo] = { tribeName: TribesNamesTRAn.Zasitecyo, attributeBonus: AttributesTRAn.Faith, attributeMalus: AttributesTRAn.Intelligence };
        m[TribesNamesTRAn.Zuwidon] = { tribeName: TribesNamesTRAn.Zuwidon, attributeBonus: AttributesTRAn.Faith, attributeMalus: AttributesTRAn.Wisdom };

        return m;
    })();
const ALL_TribeAttributesInfluence: Array<TribeAttributesInfluence>
    = Object
        .keys(TRIBES_NAME_ATTRIBUTES_INFLUENCE)
        .map((name, _, __) => TRIBES_NAME_ATTRIBUTES_INFLUENCE[name]);


//

export const RARITY_NAMES: { [rarity: number]: string } = {
    0: "Scrap",
    1: "Common",
    2: "WellManifactured",
    3: "HighQuality",
    4: "Rare",
    5: "Epic",
    6: "Legendary"
}

/*
export const RARITY_TO_TRIBE_ATTRIBUTES_VARIATIONS: { [rarity: number]: AttributesVariation } =
    (() => {
        var r: { [rarity: number]: AttributesVariation } = {
            0: {
                variationName: "Memory",
                canBeEquipUpgrade: true,
                bonus: 3,
                malus: -1,
                addedPrice: 4
            },
            1: {
                variationName: "Trade",
                canBeEquipUpgrade: true,
                bonus: 7,
                malus: -2,
                addedPrice: 8
            },
            2: {
                variationName: "Essence",
                canBeEquipUpgrade: true,
                bonus: 12,
                malus: -3,
                addedPrice: 15
            },
        };
        console.log(`RARITY_TRIBE_EQUIPMENT_PIECES as number gives: ${RARITY_TRIBE_EQUIPMENT_PIECES as number}.\n`);
        r[RARITY_TRIBE_EQUIPMENT_PIECES as number] = {
            variationName: "Original",
            canBeEquipUpgrade: false,
            bonus: 19,
            malus: -5,
            addedPrice: 20
        };
        return r;
    })();
*/

export const RARITY_TO_TRIBE_ATTRIBUTES_VARIATIONS: { [rarity: number]: AttributesVariation } = {
    0: {
        variationName: "Memory",
        canBeEquipUpgrade: true,
        bonus: 3,
        malus: -1,
        addedPrice: 4
    },
    1: {
        variationName: "Trade",
        canBeEquipUpgrade: true,
        bonus: 7,
        malus: -2,
        addedPrice: 8
    },
    2: {
        variationName: "Essence",
        canBeEquipUpgrade: true,
        bonus: 12,
        malus: -3,
        addedPrice: 15
    },
};
console.log(`RARITY_TRIBE_EQUIPMENT_PIECES as number gives: ${RARITY_TRIBE_EQUIPMENT_PIECES as number}.`);
console.log(`RaritiesTRAn.HighQuality as number gives: ${RaritiesTRAn.HighQuality as number}.`);

RARITY_TO_TRIBE_ATTRIBUTES_VARIATIONS[RARITY_TRIBE_EQUIPMENT_PIECES_AS_NUMBER || (RaritiesTRAn.HighQuality as number)] = {
    variationName: "Original",
    canBeEquipUpgrade: false,
    bonus: 19,
    malus: -5,
    addedPrice: 20
};




//

export const TRIBE_PIECE_OF_EQUIPMENT_SET_DATA: { [equipPiece: string]: PieceOfEquipmentSetData } = (
    () => {
        const t: { [equipPiece: string]: PieceOfEquipmentSetData } = {};

        t[TribeEquipmentPieces.Head] = {
            namePrefix: "Hat",
            type: EquipmentTypesTRAn.Head,
            dimensionInventory: { width: 2, height: 2 },
            additionalAttributeModifiers: [
                { attribute: AttributesTRAn.LifeMax, value: 35 },
                { attribute: AttributesTRAn.ManaMax, value: 20 },
                { attribute: AttributesTRAn.PhysicalDamageReduction, value: 3 },
                { attribute: AttributesTRAn.MagicalDamageReduction, value: 3 },
                //
                { attribute: AttributesTRAn.PhysicalProbabilityPerThousandHit, value: -8 },
                { attribute: AttributesTRAn.MagicalProbabilityPerThousandHit, value: -8 },
            ]
        } as PieceOfEquipmentSetData;

        t[TribeEquipmentPieces.Shoulder] = {
            namePrefix: "Cloak",
            type: EquipmentTypesTRAn.Shoulder,
            dimensionInventory: { width: 2, height: 2 },
            additionalAttributeModifiers: [
                { attribute: AttributesTRAn.LifeMax, value: 15 },
                { attribute: AttributesTRAn.ManaMax, value: 40 },
                { attribute: AttributesTRAn.PhysicalDamageReduction, value: 2 },
                { attribute: AttributesTRAn.MagicalDamageReduction, value: 2 },
                //
                { attribute: AttributesTRAn.PhysicalProbabilityPerThousandAvoid, value: -12 },
                { attribute: AttributesTRAn.MagicalProbabilityPerThousandAvoid, value: -12 },
            ]
        } as PieceOfEquipmentSetData;

        t[TribeEquipmentPieces.Chest] = {
            namePrefix: "Jacket",
            type: EquipmentTypesTRAn.Chest,
            dimensionInventory: { width: 3, height: 3 },
            additionalAttributeModifiers: [
                { attribute: AttributesTRAn.LifeMax, value: 50 },
                { attribute: AttributesTRAn.ManaMax, value: 5 },
                { attribute: AttributesTRAn.PhysicalDamageReduction, value: 5 },
                { attribute: AttributesTRAn.MagicalDamageReduction, value: 5 },
                //
                { attribute: AttributesTRAn.Velocity, value: -15 },
            ]
        } as PieceOfEquipmentSetData;

        t[TribeEquipmentPieces.Hands] = {
            namePrefix: "Gloves",
            type: EquipmentTypesTRAn.Hands,
            dimensionInventory: { width: 1, height: 1 },
            additionalAttributeModifiers: [
                { attribute: AttributesTRAn.LifeMax, value: 10 },
                { attribute: AttributesTRAn.ManaMax, value: 45 },
                { attribute: AttributesTRAn.PhysicalDamageReduction, value: 2 },
                { attribute: AttributesTRAn.MagicalDamageReduction, value: 2 },
                //
                { attribute: AttributesTRAn.VelocityAttackStrikePercentage, value: 10 },
            ]
        } as PieceOfEquipmentSetData;

        t[TribeEquipmentPieces.Bracelet] = {
            namePrefix: "Sleeve",
            type: EquipmentTypesTRAn.Bracelet,
            dimensionInventory: { width: 1, height: 2 },
            additionalAttributeModifiers: [
                { attribute: AttributesTRAn.LifeMax, value: 45 },
                { attribute: AttributesTRAn.ManaMax, value: 10 },
                { attribute: AttributesTRAn.PhysicalDamageReduction, value: 2 },
                { attribute: AttributesTRAn.MagicalDamageReduction, value: 2 },
                //
                { attribute: AttributesTRAn.VelocitySpellCastPercentage, value: 10 },
            ]
        } as PieceOfEquipmentSetData;

        t[TribeEquipmentPieces.Legs] = {
            namePrefix: "Pants",
            type: EquipmentTypesTRAn.Legs,
            dimensionInventory: { width: 2, height: 3 },
            additionalAttributeModifiers: [
                { attribute: AttributesTRAn.LifeMax, value: 30 },
                { attribute: AttributesTRAn.ManaMax, value: 25 },
                { attribute: AttributesTRAn.PhysicalDamageReduction, value: 3 },
                { attribute: AttributesTRAn.MagicalDamageReduction, value: 3 },
                //
                { attribute: AttributesTRAn.StaminaMax, value: 5 },
                { attribute: AttributesTRAn.StaminaRegen, value: 1 },
            ]
        } as PieceOfEquipmentSetData;

        t[TribeEquipmentPieces.Feet] = {
            namePrefix: "Boots",
            type: EquipmentTypesTRAn.Feet,
            dimensionInventory: { width: 2, height: 2 },
            additionalAttributeModifiers: [
                { attribute: AttributesTRAn.LifeMax, value: 35 },
                { attribute: AttributesTRAn.ManaMax, value: 20 },
                { attribute: AttributesTRAn.PhysicalDamageReduction, value: 2 },
                { attribute: AttributesTRAn.MagicalDamageReduction, value: 2 },
                //
                { attribute: AttributesTRAn.Velocity, value: 10 },
            ]
        } as PieceOfEquipmentSetData;

        return t;
    }
)();

/*
export enum TribesTRAn {
}
*/


//

export function getPieceOfEquipmentSetDataByPieceType(et: EquipmentTypesTRAn): PieceOfEquipmentSetData {
    return TRIBE_PIECE_OF_EQUIPMENT_SET_DATA[et];
}


export function forEachTribeAttributesInfluence(action: (pair: TribeAttributesInfluence) => void): void {
    ALL_TribeAttributesInfluence.forEach((p, _, __) => action(p));
}

export function forEachTribeEquipmentPieces(action: (equipPiece: TribeEquipmentPieces) => void) {
    for (const piece of enumKeys(TribeEquipmentPieces)) {
        action(TribeEquipmentPieces[piece]);
    }
}

export function forEachRaritiesOfTribeAttributesVariations(action: ((rarity: RaritiesTRAn, attrVar: AttributesVariation) => void)) {
    Object.keys(RARITY_TO_TRIBE_ATTRIBUTES_VARIATIONS).forEach(
        (r, _, __) => {
            const rarity: RaritiesTRAn = Number(r) as RaritiesTRAn;
            action(rarity, RARITY_TO_TRIBE_ATTRIBUTES_VARIATIONS[rarity]);
        }
    );
}

export function getAttributeInfluenceByTribe(name: TribesNamesTRAn): TribeAttributesInfluence {
    return TRIBES_NAME_ATTRIBUTES_INFLUENCE[name];
}

export function newAttributeUpgradesForTribeRarity(pair: TribeAttributesInfluence, rarity: RaritiesTRAn): AttributeModifications_V02 {
    const variations = RARITY_TO_TRIBE_ATTRIBUTES_VARIATIONS[rarity];
    const attr: AttributeModifications_V02 = {};
    attr[pair.attributeBonus] = variations.bonus;
    attr[pair.attributeMalus] = variations.malus;
    return attr;
}
export function newEquipUpgradesForTribeRarity(tribe: TribeAttributesInfluence, rarity: RaritiesTRAn): EquipUpgrade_V02 {
    const variations = RARITY_TO_TRIBE_ATTRIBUTES_VARIATIONS[rarity];
    return {
        name: `of ${tribe.tribeName} ${variations.variationName}`,
        price: [variations.addedPrice],
        rarity: rarity,
        attributeModifiers: newAttributeUpgradesForTribeRarity(tribe, rarity)
    } as EquipUpgrade_V02;
}


export function newEquipmentPieceForTribe(tribe: TribesNamesTRAn, piece: EquipmentTypesTRAn): EquipItem_V03 {
    const data: PieceOfEquipmentSetData = TRIBE_PIECE_OF_EQUIPMENT_SET_DATA[piece];
    const variations: AttributesVariation = RARITY_TO_TRIBE_ATTRIBUTES_VARIATIONS[RARITY_TRIBE_EQUIPMENT_PIECES];
    const tribeInfluence: TribeAttributesInfluence = getAttributeInfluenceByTribe(tribe);

    const attrMods: AttributeModifications_V02 = newAttributeUpgradesForTribeRarity(tribeInfluence, RARITY_TRIBE_EQUIPMENT_PIECES);
    for (const attrMod of data.additionalAttributeModifiers) {
        attrMods[attrMod.attribute] = attrMod.value;
    }

    return {
        name: `${data.namePrefix} Cosplay of ${tribeInfluence.tribeName}`,
        type: piece,
        abilities: [],
        rarity: RARITY_TRIBE_EQUIPMENT_PIECES,
        price: [variations.addedPrice],
        dimensionInventory: data.dimensionInventory,
        attributeModifiers: attrMods,
    } as EquipItem_V03;

}