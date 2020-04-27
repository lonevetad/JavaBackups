package games.theRisingAngel.creatures;

import games.generic.controlModel.player.PlayerGeneric;
import games.generic.controlModel.subimpl.CreatureAttributesCaching;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.theRisingAngel.inventory.EquipmentSetTRAn;
import games.theRisingAngel.misc.AttributesTRAn;
import games.theRisingAngel.misc.CreatureUIDProvider;

/**
 * This is NOT a {@link PlayerGeneric}, even if it's similar (but there's no
 * multiple inheritance, so ... interfaces and redundancy).
 */
public class BaseNPCCreatureTRAr extends BaseCreatureTRAn {
	private static final long serialVersionUID = 1L;

	public BaseNPCCreatureTRAr(GModalityRPG gModRPG, String name) {
		super(gModRPG, name);
		this.isDestroyed = false;
		this.ID = CreatureUIDProvider.newID();
		this.equipmentSet = new EquipmentSetTRAn();
		this.equipmentSet.setCreatureWearingEquipments(this);
		this.attributes = new CreatureAttributesCaching(AttributesTRAn.VALUES.length);
		this.life = 1;
		ticks = 0;
		accumulatedTimeLifeRegen = 0;
	}

	//

	//

}