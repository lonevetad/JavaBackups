package games.theRisingAngel.inventory;

import java.awt.Point;

import games.generic.controlModel.GModality;
import games.generic.controlModel.items.EquipmentItem;
import games.generic.controlModel.items.EquipmentSet;
import games.theRisingAngel.enums.EquipmentTypesTRAn;

/**
 *
 * Each Equipment should implements the set of "character statistics modifiers"
 * as follows (maybe through a class")
 * <p>
 * Set of power ups, of statistics' modifiers (i.e., character's parameters'
 * modifiers).<br>
 * Instead of create an array (that mimics the character's statistics) and apply
 * to that array all modifiers, wasting memory in almost-empty arrays, just
 * collect all statistic modifications.
 */
public class EquipmentSetTRAn extends EquipmentSet {

	protected static final int firstIndexRings, firstIndexNecklace, firstIndexBracelets;
	static {
		firstIndexNecklace = EquipmentTypesTRAn.Necklace.ordinal();
		firstIndexBracelets = firstIndexNecklace + EquipmentTypesTRAn.NECKLACE_AMOUNT;
		firstIndexRings = firstIndexBracelets + EquipmentTypesTRAn.BRACELET_AMOUNT;
//		System.out.println("firstIndexNecklace: " + firstIndexNecklace);
//		System.out.println("firstIndexBracelets: " + firstIndexBracelets);
//		System.out.println("firstIndexRings: " + firstIndexRings);
	}

	protected boolean isNiceEquippingRings;
	protected final EquipmentItem[] equippedItems;

	public EquipmentSetTRAn() {
		equippedItems = new EquipmentItem[EquipmentTypesTRAn.TOTAL_AMOUNT_EQUIPMENTS_WEARABLES];
		this.isNiceEquippingRings = true;
	}

	@Override
	public EquipmentItem[] getEquippedItems() { return equippedItems; }

	/**
	 * Configuration-like setting for setting the way to displace rings upon
	 * equipping:
	 * <ul>
	 * <li><code>true</code>: rings are spread among fingers in a balanced way.</li>
	 * <li><code>false</code>: Rings are clustered in the first finger
	 * available</li>
	 * </ul>
	 */
	public boolean isNiceEquippingRings() { return isNiceEquippingRings; }

	@Override
	public EquipmentItem getEquippedItemAt(int index) { return equippedItems[index]; }

	public void setNiceEquippingRings(boolean isNiceEquippingRings) {
		this.isNiceEquippingRings = isNiceEquippingRings;
	}

//	/**Returns <code>true</code> if the equip process was successful, <code>false</code> otherwise (meaning that a {@link #swapEquipmentItem(GModality, EquipmentItem, EquipmentItem)} should be required*/
	public void addEquipmentItemAt(GModality gm, EquipmentItem ei, int index) {
		if (ei == null)
			return;
		if (equippedItems[index] == null) {
			performEquipAt(gm, ei, index);
		} else {
			swapEquipmentItem(gm, ei, equippedItems[index]);
		}
	}

	@Override
	public void swapEquipmentItem(GModality gm, EquipmentItem newEI, EquipmentItem oldEI) {
		// TODO
	}

	public void swapEquipmentItem(GModality gm, EquipmentItem newEI, int index, EquipmentItem oldEI) {
		// TODO
	}

	@Override
	public void addEquipmentItem(GModality gm, EquipmentItem ei) {
		EquipmentTypesTRAn et;
		if (ei == null)
			return;
		et = (EquipmentTypesTRAn) ei.getEquipmentType();
		switch (et) {
//		case Earrings:break; // are put at the beginning
		case Ring:
			addRing(gm, (EIRing) ei);
			break;
		case Necklace:
			addNecklaceOrBracelet(gm, ei, firstIndexNecklace, EquipmentTypesTRAn.NECKLACE_AMOUNT);
			break;
		case Bracelet:
			addNecklaceOrBracelet(gm, ei, firstIndexBracelets, EquipmentTypesTRAn.BRACELET_AMOUNT);
			break;
		default:
			int i;
			i = ei.getEquipmentType().getIndex(); // id as index
			// everybody else
			if (equippedItems[i] == null) {
				equippedItems[i] = ei;
				performEquipAt(gm, ei, i);
			} else {
				swapEquipmentItem(gm, ei, equippedItems[i]);
			}
			break;
		}
	}

	//

	//

	protected void performEquipAt(GModality gm, EquipmentItem ei, int index) {
		equippedItems[index] = ei;
		ei.setBelongingEquipmentSet(this);
		ei.setLocationInInventory(new Point(index, index)); // just to use it in some way ...
		ei.onEquip(gm);
	}

	protected void addNecklaceOrBracelet(GModality gm, EquipmentItem ei, int index, int equipItemSlotToCheck) {
		boolean notAdded;
		int firstIndex;
		firstIndex = index;
		notAdded = true;
		while (notAdded && equipItemSlotToCheck-- >= 0) {
			if (equippedItems[index] == null) {
//				equippedItems[index] = ei;
//				ei.setBelongingEquipmentSet(this);
				performEquipAt(gm, ei, index);
				notAdded = false;
			}
			equipItemSlotToCheck++;
			index++;
		}
		if (notAdded) { swapEquipmentItem(gm, ei, equippedItems[firstIndex]); }
	}

	protected void addRing(GModality gm, EIRing ring) {
		boolean notAdded;
		int s, i, fingersRemainingToCheck, startingIndexSlot, maxIndexSlotToStart, k;
		// cercare prima un posto libero
		notAdded = true;
		s = ring.getSlotFingerSize();
		fingersRemainingToCheck = EquipmentTypesTRAn.TOTAL_FINGERS_AMOUNT;
		startingIndexSlot = 0; // used as "starting slot
		maxIndexSlotToStart = 1 + (EquipmentTypesTRAn.RING_SLOTS_EACH_FINGERS - s);
		if (isNiceEquippingRings) {
			// poi se fallisce, cercare le prime due dita con un solo anello
			// se trovate, allora compattare ed inserire

			// looks for the first finger having enough "place"
			do {
				i = firstIndexRings + startingIndexSlot;
//				while(notAdded && i <= lastAvailableIndex) {
				while (notAdded && fingersRemainingToCheck-- >= 0) {
					// check all slots, an amount of slots equal to th ring's size
					k = 0;
					while (equippedItems[i + k] == null && ++k < s)
						;
					if (k == s) { // found enough space for the ring, i.e. where to place the item
						while (--k > 0) { // place the ring, ignore the 0: it's used in equipAt
							equippedItems[i + k] = ring;
						}
						performEquipAt(gm, ring, i); // setting for "k==0" is performed here
						notAdded = false;
					}
					i += EquipmentTypesTRAn.RING_SLOTS_EACH_FINGERS; // jump to the next finger
				}
			} while (notAdded && ++startingIndexSlot < maxIndexSlotToStart);
		} else {
			int fingerIndex;
			fingerIndex = 0;
			i = firstIndexRings;
			do {
				// for each index, find the first available slot
//				i = firstIndexRings + startingIndexSlot + (fingerIndex * EquipmentTypesTRAn.RING_SLOTS_EACH_FINGERS);
				/*
				 * find the first finger's index that, starting from that index, has enought
				 * available slots
				 */
				/*
				 * so, for each finger, find the first available index (i.e.: the first
				 * non-filled finger)
				 */
				startingIndexSlot = 0;
				while (equippedItems[i + startingIndexSlot] != null && ++startingIndexSlot < maxIndexSlotToStart)
					;
				if (startingIndexSlot < maxIndexSlotToStart) {
					// found non-filled
					k = 0;
					// count the amount of available slot
					while (equippedItems[i + startingIndexSlot + k] == null && ++k < s)
						;
					if (k == s) { // found enough space for the ring, i.e. where to place the item
						while (--k > 0) { // place the ring, ignore the 0: it's used in equipAt
							equippedItems[i + startingIndexSlot + k] = ring;
						}
						performEquipAt(gm, ring, i + startingIndexSlot); // setting for "k==0" is performed here
						notAdded = false;
					}
				}
				i += EquipmentTypesTRAn.RING_SLOTS_EACH_FINGERS; // jump to the next finger
			} while (notAdded && ++fingerIndex < fingersRemainingToCheck);
		}
	}

	@Override
	public void forEachEquipment(ConsumerEquipmentIndex consumer) {
		int i, n;
		n = equippedItems.length;
		i = -1;
		while (++i < n) {
			consumer.apply(equippedItems[i], i);
		}
	}
}