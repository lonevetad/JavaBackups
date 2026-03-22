import json
import json5

filename = "equipUpgrades"  # _uncommented
MIN_RARITIES = 0
MAX_RARITIES = 5
total_attributes = {}
ta_by_rarity = {}
ta_by_category = {}
"""
ta_by_r_c = [{} for _ in range(MIN_RARITIES + 1) ]
"""
ta_by_r_c = [None] * ((MAX_RARITIES - MIN_RARITIES) + 1)
for rar in range(MIN_RARITIES, MAX_RARITIES + 1):
    ta_by_r_c[rar] = {}

print(f"START: ta_by_r_c -> {json.dumps(ta_by_r_c)}")


def add_category(cat: str, eu):
    for rar in range(MIN_RARITIES, MAX_RARITIES + 1):
        try:
            ta_by_r_c[rar][cat] = {}
        except Exception as e:
            print(e)
            raise Exception(
                f"Wrong rarity: {rarity} for object: {json.dumps(eu)}")


ta_by_r_pos = {
    "positive": {},
    "negative": {}
}


fn = f"{filename}.json"
with open(fn, "r") as f:
    data = json5.load(f)
    for eu in data:
        am = eu["attributesModifiers"]
        rarity = eu["rarity"]
        category = eu["category"]
        price = eu["price"][0]
        is_positive = price >= 0

        if rarity not in ta_by_rarity:
            ta_by_rarity[rarity] = {}
        if rarity < MIN_RARITIES or rarity > MAX_RARITIES:
            raise Exception(
                f"Wrong rarity: {rarity} for object: {json.dumps(eu)}")
        if category not in ta_by_category:
            ta_by_category[category] = {}
            add_category(category, eu)
        for a in am.keys():
            if a not in total_attributes:
                total_attributes[a] = 0
            total_attributes[a] += am[a]
            ta_by_rarity[rarity][a] = ta_by_rarity[rarity].get(a, 0) + am[a]
            ta_by_category[category][a] = ta_by_category[category].get(
                a, 0) + am[a]
            try:
                ta_by_r_c[rarity][category][a] = ta_by_r_c[rarity][category].get(
                    a, 0) + am[a]
            except Exception as e:
                print(e)
                print(
                    f"EXCEPTION WITH rarity: {rarity} for object: {json.dumps(eu)}")
            if is_positive:
                ta_by_r_pos["positive"][a] = ta_by_r_pos["positive"].get(
                    a, 0) + am[a]
            else:
                ta_by_r_pos["negative"][a] = ta_by_r_pos["negative"].get(
                    a, 0) + am[a]

print("Total attributes:")
with open(f"analysis_{filename}.json", "w") as f:
    json.dump({
        "total_attributes": total_attributes,
        "ta_by_rarity": ta_by_rarity,
        "ta_by_category": ta_by_category,
        "ta_by_r_pos": ta_by_r_pos,
        "ta_by_r_c": ta_by_r_c
    }, f, indent=2, sort_keys=True)
