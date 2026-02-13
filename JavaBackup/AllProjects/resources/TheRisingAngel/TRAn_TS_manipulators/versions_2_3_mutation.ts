import fs from "fs";
import { Ability_V03, EquipItem_V02, EquipItem_V03 } from "./types";
//const fs = require("fs")

// ,..................


// -------------------------------------------------

var equips: Array<EquipItem_V02> | null = JSON.parse(fs.readFileSync("./equipItems_V2.json", { encoding: "utf-8" })) as
    Array<EquipItem_V02>;

// remove duplicates

type NewEquipV03Set = { [name: string]: EquipItem_V03 }
var newEquipSet: NewEquipV03Set | null = {};

equips.forEach((e, _, __) => {
    if (newEquipSet![e.name]) {
        console.log("duplicated: " + e.name);
    } else {

        newEquipSet![e.name] = {
            name: e.name,
            rarity: e.rarity,
            price: e.price,
            type: e.type,
            dimensionInventory: e.dimensInvent,
            abilities: e.abilities.map((abilOriginal, _, __) => {
                const a: Ability_V03 = {
                    name: "",
                    level: 0
                };
                if (abilOriginal instanceof String || (typeof abilOriginal) == "string") {
                    a.name = abilOriginal as string;
                } else {
                    a.name = abilOriginal["name"] as string;
                    if (abilOriginal["level"]) {
                        a.level = abilOriginal["level"] as number;
                    }
                }
                return a;
            }),
            attributeModifiers: e.attributeModifiers
        } as EquipItem_V03
    }
})

equips = null;

var newEquips: Array<EquipItem_V03> = Object.keys(newEquipSet!).map((nameNE, _, __) => newEquipSet![nameNE]);
newEquipSet = null;

fs.writeFileSync(
    "./equipItems.json",
    JSON.stringify(newEquips, null, 2)
);
