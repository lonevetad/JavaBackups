// ---------------------- 

import { AttributesTRAn, RaritiesTRAn } from "./enums";


export function loadTypes() {
    console.log("Types :D");
}


// V01 -------------------------------------------------------------------------------------


export type AttributeMod_V01 = {
    attribute: AttributesTRAn,
    value: number
}

export type NamedAttributesModifier_V01 = {
    name: string,
    rarity: RaritiesTRAn,
    price: Array<number>,
    attributeModifiers: Array<AttributeMod_V01>
}

export type DimensionInventory = { width: number, height: number };

export type EquipItem_V01 = NamedAttributesModifier_V01 & {
    type: string,
    dimensInvent: DimensionInventory,
    abilities: Array<string>
}


export type EquipUpgrade_V01 = NamedAttributesModifier_V01


// V02 -------------------------------------------------------------------------------------


export type AttributeModifications_V02 = {
    [attribute: string]: number
}

export type NamedAttributesModifier_V02 = {
    name: string,
    rarity: RaritiesTRAn,
    price: Array<number>,
    attributeModifiers: AttributeModifications_V02
}

export type EquipItem_V02 = NamedAttributesModifier_V02 & {
    type: string,
    dimensInvent: DimensionInventory,
    abilities: Array<string | any>
}

export type EquipUpgrade_V02 = NamedAttributesModifier_V02


// V03 -------------------------------------------------------------------------------------


export type Ability_V03 = {
    name: string,
    level?: number
}

export type EquipItem_V03 = NamedAttributesModifier_V02 & {
    type: string,
    dimensionInventory: DimensionInventory,
    abilities: Array<Ability_V03>
}


// V04 -------------------------------------------------------------------------------------
