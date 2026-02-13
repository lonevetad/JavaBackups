import fs from "fs";
import { AttributeModifications_V02, EquipItem_V01, EquipItem_V02, EquipUpgrade_V01, EquipUpgrade_V02 } from "./types";
//const fs = require("fs")


var equipsUpgrades = JSON.parse(fs.readFileSync("./equipUpgradesTRAr_V1.json", { encoding: "utf-8" })) as Array<EquipUpgrade_V01>;
fs.writeFileSync(
    "./equipUpgradesTRAr.json",
    JSON.stringify(
        equipsUpgrades.map((e, _, __) => {
            var a: AttributeModifications_V02 = {}
            for (const m of e.attributeModifiers) {
                a[m.attribute] = m.value
            }
            return {
                name: e.name,
                rarity: e.rarity,
                price: e.price,
                attributeModifiers: a
            } as EquipUpgrade_V02
        })
        , null, 2));

var equips = JSON.parse(fs.readFileSync("./equipItems_V1.json", { encoding: "utf-8" })) as Array<EquipItem_V01>;
fs.writeFileSync(
    "./equipItems.json",
    JSON.stringify(
        equips.map((e, _, __) => {
            var a: AttributeModifications_V02 = {}
            for (const m of e.attributeModifiers) {
                a[m.attribute] = m.value
            }
            return {
                name: e.name,
                rarity: e.rarity,
                price: e.price,
                type: e.type,
                dimensInvent: e.dimensInvent,
                abilities: e.abilities,
                attributeModifiers: a
            } as EquipItem_V02
        })
        , null, 2));
