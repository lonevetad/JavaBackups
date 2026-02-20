package games.theRisingAngel.enums;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.items.EquipmentType;
import games.theRisingAngel.inventory.EquipItemFactory;
import tools.Comparators;

/**
 * Rings can occupy one single slot (a simple ring) or two (very big, bulky and
 * cumbersome ring, the biggest ones).
 */
public enum EquipmentTypesTRAn implements EquipmentType {
	Earrings(2), Head, Shoulder, // Shoulder includes "cloak" (= mantello)
	Hands, Chest, Arms, // hands and arms are considered as a single pair, as like feet
	Feet, Belt, Legs, //
	MainWeapon, Special, SecodaryWeapon, //
	Necklace(3), Bracelet(2), Ring(-1)// rings will be counted later
	;

	public static final int HANDS_AMOUNT = 2, FINGERS_EACH_HAND = 5, //
			RING_SLOTS_EACH_FINGERS = 1, // previously was 2, but there could be too many rings in the character
			TOTAL_RINGS_SLOTS_AMOUNT, TOTAL_FINGERS_AMOUNT = (HANDS_AMOUNT * FINGERS_EACH_HAND), //
			BRACELET_AMOUNT = 2, NECKLACE_AMOUNT = 3, TOTAL_AMOUNT_EQUIPMENTS_WEARABLES;

	public static final EquipmentTypesTRAn[] ALL_EQUIP_TYPES_TRAn;
	public static final IndexToObjectBackmapping INDEX_TO_EQUIP_TYPE_TRAn;
	public static final Comparator<EquipmentTypesTRAn> COMPARATOR_EQUIP_TYPES_TRAn;
	private static Map<String, EquipmentTypesTRAn> attTRArByName = null;

	static {
		ALL_EQUIP_TYPES_TRAn = EquipmentTypesTRAn.values();
		TOTAL_RINGS_SLOTS_AMOUNT = (TOTAL_FINGERS_AMOUNT * RING_SLOTS_EACH_FINGERS);
		TOTAL_AMOUNT_EQUIPMENTS_WEARABLES = (ALL_EQUIP_TYPES_TRAn.length - 3) // 3 = Rings + Necklace + Bracelet
				+ TOTAL_RINGS_SLOTS_AMOUNT + BRACELET_AMOUNT + NECKLACE_AMOUNT; // = 27
		EquipmentTypesTRAn.Ring.amountEquippable = TOTAL_RINGS_SLOTS_AMOUNT;

		INDEX_TO_EQUIP_TYPE_TRAn = (int i) -> ALL_EQUIP_TYPES_TRAn[i];

		COMPARATOR_EQUIP_TYPES_TRAn = (e1, e2) -> {
			if (e1 == e2) {
				return 0;
			}
			if (e1 == null) {
				return -1;
			}
			if (e2 == null) {
				return 1;
			}
			return Comparators.LONG_COMPARATOR.compare(e1.getID(), e2.getID());
		};
	}

	private EquipmentTypesTRAn(int am) {
		this.amountEquippable = am;
	}

	private EquipmentTypesTRAn() {
		this(1);
	}

	private int amountEquippable;
	private EquipItemFactory factory = null;

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

	public EquipItemFactory getFactory() {
		if (this.factory == null) {
			this.factory = recalculateFactory(this);
		}
		return this.factory;
	}

	public int getAmountEquippable() {
		return amountEquippable;
	}

	//

	public static EquipItemFactory recalculateFactory(EquipmentTypesTRAn thisInstance) {
		/*
		 * Apply the current settings (instead of a constructor-dependent) because right
		 * now only two EquipType-dependent classes exists. Further refinements could
		 * change this implementation.
		 */
		return switch (thisInstance) {
			case Earrings, Necklace, Bracelet -> EquipItemFactory.DefaultEIF.JewelryFactory;
			case Ring -> EquipItemFactory.DefaultEIF.RingFactory;
			case MainWeapon, SecodaryWeapon -> EquipItemFactory.DefaultEIF.WeaponFactory;
			default -> EquipItemFactory.DefaultEIF.NonJewelryFacory;
		};
	}

	//

	// copied from AttributesTRAr

	public static EquipmentTypesTRAn getEquipTypeTRArByName(String name) {
		EquipmentTypesTRAn e;
		Map<String, EquipmentTypesTRAn> m;
		if (name == null)
			throw new IllegalArgumentException("Name cannot be null");
		if (attTRArByName == null) {
			m = attTRArByName = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.STRING_COMPARATOR);
			for (EquipmentTypesTRAn eq : ALL_EQUIP_TYPES_TRAn) {
				m.put(eq.name(), eq); // using name() instead of getName() just to be faster
			}
		}
		e = attTRArByName.get(name);
		if (e == null)
			throw new IllegalArgumentException("Invalid name for AttributesTRAr: " + name);
		return e;
	}

	public static EquipmentTypesTRAn getEquipTypeTRArByIndex(int index) {
		return ALL_EQUIP_TYPES_TRAn[index];
	}

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
				return 1;
		}
	}

	@Override
	public IndexToObjectBackmapping getFromIndexBackmapping() {
		return INDEX_TO_EQUIP_TYPE_TRAn;
	}

	public static boolean isJewelry(EquipmentTypesTRAn et) {
		Objects.requireNonNull(et);
		return (et == EquipmentTypesTRAn.Earrings || et == EquipmentTypesTRAn.Necklace
				|| et == EquipmentTypesTRAn.Bracelet || et == EquipmentTypesTRAn.Ring);
	}

}