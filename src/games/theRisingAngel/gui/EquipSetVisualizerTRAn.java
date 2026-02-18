package games.theRisingAngel.gui;

import games.generic.controlModel.items.EquipmentItem;
import games.generic.view.GameView;
import games.generic.view.dataProviders.EquipSetProviderGui;
import games.theRisingAngel.enums.EquipmentTypesTRAn;
import games.theRisingAngel.inventory.EquipmentSetTRAn;

/**
 * Should add, as a feature: each rarity-1 attribute that includes 1 Luck should
 * hide that luck bonus, as an easter-egg.<br>
 * In code:
 */
public class EquipSetVisualizerTRAn extends EquipSetProviderGui {

	public static final int FIRST_PAGE_WIDTH = 3, FIRST_PAGE_HEIGHT = 4, //
			SECOND_PAGE_WIDTH = 3, SECOND_PAGE_HEIGHT = EquipmentTypesTRAn.FINGERS_EACH_HAND;

	public EquipSetVisualizerTRAn(GameView view) {
		super(view);
		firstPage = new EquipmentItem[FIRST_PAGE_HEIGHT][FIRST_PAGE_WIDTH];
		secondJewelryPage = new EquipmentItem[SECOND_PAGE_HEIGHT][SECOND_PAGE_WIDTH];
	}

	protected final EquipmentItem[][] firstPage, secondJewelryPage;

	public EquipmentSetTRAn getEquipSetTRAn() { return (EquipmentSetTRAn) getEquipSet(); }

	public void updatePages() {
		int r, c, offsetEquipSet, handSize, handIndex, handOffset;
		EquipmentSetTRAn es;
		EquipmentItem[] row, allEquipments;
		offsetEquipSet = 0; // used as a index
		es = getEquipSetTRAn();
		if (es == null)
			return;
		allEquipments = es.getEquippedItems();
		r = -1;
		// the first page is sequential
		while (++r < FIRST_PAGE_HEIGHT) {
			row = firstPage[r];
			c = -1;
			while (++c < FIRST_PAGE_WIDTH) {
				row[c] = allEquipments[offsetEquipSet++];
			}
		}
		// second page: put in columns:
		// 1) necklaces and bracelets
		// 2) first hand, finger by finger
		// 3) second hand
		c = 0;
		r = -1;
		handSize = EquipmentTypesTRAn.RING_SLOTS_EACH_FINGERS * EquipmentTypesTRAn.FINGERS_EACH_HAND;
		while (++r < SECOND_PAGE_HEIGHT) {
			row = secondJewelryPage[r];
			row[0] = allEquipments[offsetEquipSet++]; // necklace or bracelet
			c = 0;
			handIndex = -1;
			while (++handIndex <= 1) {
				handOffset = handIndex * handSize;
				while (++c <= EquipmentTypesTRAn.RING_SLOTS_EACH_FINGERS) {
					// r works as "finger index
					row[c] = allEquipments[//
					handOffset + offsetEquipSet + (r * EquipmentTypesTRAn.RING_SLOTS_EACH_FINGERS) + c];
				}
			}
		}
	}

}