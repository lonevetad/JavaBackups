package games.theRisingAngel.inventory;

import java.util.Map;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.inventoryAbil.EquipmentType;
import tools.Comparators;

/**
 * Rings can occupy one single slot (a simple ring) or two (very big, bulky and
 * cumbersome ring, the biggest ones).
 */
public enum EquipmentTypesTRAn implements EquipmentType {
	Earrings, Head, Shoulder, // Shoulder includes "cloak" (= mantello)
	Hands, Chest, Arms, //
	Feet, Belt, Legs, //
	MainWeapon, Special, SecodaryWeapon, //
	Necklace, Bracelet, Ring(EquipItemFactory.DefaultEIF.RingFactory
//			(g, e, name) -> new EIRing(g, name)
	)//
	;

	public static final int HANDS_AMOUNT = 2, FINGERS_EACH_HAND = 5, //
			RING_SLOTS_EACH_FINGERS = 2, // previously was 2, but there could be too many rings in the character
			TOTAL_RINGS_SLOTS_AMOUNT, TOTAL_FINGERS_AMOUNT = (HANDS_AMOUNT * FINGERS_EACH_HAND), //
			BRACELET_AMOUNT = 2, NECKLACE_AMOUNT = 3, TOTAL_AMOUNT_EQUIPMENTS_WEARABLES;

	static {
		TOTAL_RINGS_SLOTS_AMOUNT = (TOTAL_FINGERS_AMOUNT * RING_SLOTS_EACH_FINGERS);
		TOTAL_AMOUNT_EQUIPMENTS_WEARABLES = (EquipmentTypesTRAn.values().length - 3) // 3 = Rings + Necklace + Bracelet
				+ TOTAL_RINGS_SLOTS_AMOUNT + BRACELET_AMOUNT + NECKLACE_AMOUNT; // = 27
		System.out.println("TOTAL_AMOUNT_EQUIPMENTS_WEARABLES: " + TOTAL_AMOUNT_EQUIPMENTS_WEARABLES);
	}

	public final EquipItemFactory factory;

	private EquipmentTypesTRAn(EquipItemFactory factory) { this.factory = factory; }

	private EquipmentTypesTRAn() {
		/*
		 * Apply the current settings (instead of a constructor-dependent) because right
		 * now only two EquipType-dependent classes exists. Further refinements could
		 * change this implementation.
		 */
		int i;
		i = ordinal();
		if (i == 0 || ((15 - i) <= 3)) {
			this.factory = EquipItemFactory.DefaultEIF.JewelryFactory;
		} else if (i == 9 || i == 11) {
			this.factory = EquipItemFactory.DefaultEIF.WeaponFactory;
		} else {
			this.factory = EquipItemFactory.DefaultEIF.NonJewelryFacory;
		}
	}

	@Override
	public int getIndex() { return ordinal(); }

	@Override
	public String getName() { return name(); }

	@Override
	public Integer getID() { return ordinal(); }

	//

	// copied from AttributesTRAr

	public static final EquipmentTypesTRAn[] VALUES = EquipmentTypesTRAn.values();
	private static Map<String, EquipmentTypesTRAn> attTRArByName = null;

	public static EquipmentTypesTRAn getEquipTypeTRArByName(String name) {
		EquipmentTypesTRAn e;
		Map<String, EquipmentTypesTRAn> m;
		if (name == null)
			throw new IllegalArgumentException("Name cannot be null");
		if (attTRArByName == null) {
			m = attTRArByName = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.STRING_COMPARATOR);
			for (EquipmentTypesTRAn eq : VALUES) {
				m.put(eq.name(), eq); // using name() instead of getName() just to be faster
			}
		}
		e = attTRArByName.get(name);
		if (e == null)
			throw new IllegalArgumentException("Invalid name for AttributesTRAr: " + name);
		return e;
	}

	public static EquipmentTypesTRAn getEquipTypeTRArByIndex(int index) { return VALUES[index]; }

	public static int getAmountItemsEquippables(EquipmentTypesTRAn et) {
		switch (et) {
		case Necklace: {
			return NECKLACE_AMOUNT;
		}
		case Bracelet: {
			return BRACELET_AMOUNT;
		}
		case Ring: {
			return TOTAL_RINGS_SLOTS_AMOUNT;
		}
		default:
		}
		return 1;
	}
}