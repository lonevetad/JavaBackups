import { RaritiesTRAn } from "./enums";

export const RARITY_TRIBE_EQUIPMENT_PIECES: RaritiesTRAn = RaritiesTRAn.HighQuality;
export const RARITY_TRIBE_EQUIPMENT_PIECES_AS_NUMBER: RaritiesTRAn = RARITY_TRIBE_EQUIPMENT_PIECES as number;


export function loadConstants() {
    console.log("CONSTANTS :D");
    console.log("RARITY_TRIBE_EQUIPMENT_PIECES: " + RARITY_TRIBE_EQUIPMENT_PIECES);
    console.log("RARITY_TRIBE_EQUIPMENT_PIECES_AS_NUMBER: " + RARITY_TRIBE_EQUIPMENT_PIECES_AS_NUMBER);

}