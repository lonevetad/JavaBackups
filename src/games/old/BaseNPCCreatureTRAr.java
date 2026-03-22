package games.old;

import games.generic.controlModel.player.PlayerGeneric;
import games.generic.controlModel.subimpl.CreatureAttributesCaching;
import games.generic.controlModel.subimpl.GModalityRPG;
<<<<<<< HEAD:JavaBackup/AllProjects/src/games/old/BaseNPCCreatureTRAr.java
<<<<<<< HEAD:JavaBackup/AllProjects/src/games/old/BaseNPCCreatureTRAr.java
import games.theRisingAngel.creatures.BaseCreatureTRAn;
import games.theRisingAngel.enums.AttributesTRAn;
import games.theRisingAngel.inventory.EquipmentSetTRAn;
>>>>>>> master:JavaBackup/AllProjects/src/games/theRisingAngel/creatures/BaseNPCCreatureTRAr.java
=======
import games.theRisingAngel.creatures.BaseCreatureTRAn;

/**
 * This is NOT a {@link PlayerGeneric}, even if it's similar (but there's no
 * multiple inheritance, so ... interfaces and redundancy).
 */
<<<<<<< HEAD:JavaBackup/AllProjects/src/games/old/BaseNPCCreatureTRAr.java
<<<<<<< HEAD:JavaBackup/AllProjects/src/games/old/BaseNPCCreatureTRAr.java
@Deprecated
=======
>>>>>>> master:JavaBackup/AllProjects/src/games/theRisingAngel/creatures/BaseNPCCreatureTRAr.java
public class BaseNPCCreatureTRAr extends BaseCreatureTRAn {
=======
@Deprecated
public class BaseNPCCreatureTRAn extends BaseCreatureTRAn {
>>>>>>> develop:JavaBackup/AllProjects/src/games/theRisingAngel/creatures/BaseNPCCreatureTRAr.java
	private static final long serialVersionUID = 1L;

	public BaseNPCCreatureTRAn(GModalityRPG gModRPG, String name) {
		super(gModRPG, name);
		this.isDestroyed = false;
<<<<<<< HEAD:JavaBackup/AllProjects/src/games/old/BaseNPCCreatureTRAr.java
<<<<<<< HEAD:JavaBackup/AllProjects/src/games/old/BaseNPCCreatureTRAr.java
=======
		this.ID = CreatureUIDProvider.newID();
		this.equipmentSet = new EquipmentSetTRAn();
		this.equipmentSet.setCreatureWearingEquipments(this);
>>>>>>> master:JavaBackup/AllProjects/src/games/theRisingAngel/creatures/BaseNPCCreatureTRAr.java
=======
>>>>>>> develop:JavaBackup/AllProjects/src/games/theRisingAngel/creatures/BaseNPCCreatureTRAr.java
		this.attributes = new CreatureAttributesCaching(AttributesTRAn.VALUES.length);
		this.life = 1;
	}

	//

	//

}