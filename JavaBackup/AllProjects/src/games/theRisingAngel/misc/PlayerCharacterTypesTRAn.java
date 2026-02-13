package games.theRisingAngel.misc;

import games.generic.controlModel.misc.AttributeModification;
import games.generic.controlModel.misc.CreatureAttributes;
import games.theRisingAngel.PlayerTRAn;
import games.theRisingAngel.enums.AttributesTRAn;
import tools.ObjectNamedID;

/**
 * TODO refactor giving a full set of starting values (like a set of
 * {@link AttributeModification}).
 */
public class PlayerCharacterTypesTRAn {
	/**
	 * Luck is NOT taken into account in this (except for Human)
	 */
	public static final int TOTAL_STARTING_ATTRIBUTES = 100;

	public static final AttributesTRAn[] ATTRIBUTES_HUMAN_RECEIVING_MEAN_EXCESS = { //
			AttributesTRAn.Strength, //
			AttributesTRAn.Intelligence, //
			AttributesTRAn.Wisdom, //
			AttributesTRAn.Defense, //
			AttributesTRAn.Constitution, //
			AttributesTRAn.Dexterity, //
			AttributesTRAn.Health, //
			AttributesTRAn.Precision, //
			AttributesTRAn.Faith, //
			AttributesTRAn.Luck //
	};
	public static final AttributeModification[] STARTING_ATTRIBUTES_OF_ALL_RACES = {
			new AttributeModification(AttributesTRAn.LifeMax, 50), //
			new AttributeModification(AttributesTRAn.ManaMax, 25), //
			new AttributeModification(AttributesTRAn.ManaRegen, 2), //
			new AttributeModification(AttributesTRAn.StaminaMax, 25), //
			new AttributeModification(AttributesTRAn.StaminaRegen, 5), //
	};
	private static final AttributeModification[] HUMAN_STARTING_ATTRIBUTES;

	static {
		AttributeModification attrMean[], am;
		final int attributesAmount, meanAttribute, excess;

		attributesAmount = AttributesTRAn.ATTRIBUTES_UPGRADABLE_COUNT + 1; // + Luck
		meanAttribute = TOTAL_STARTING_ATTRIBUTES / attributesAmount; //
		excess = TOTAL_STARTING_ATTRIBUTES - (meanAttribute * attributesAmount);

		attrMean = new AttributeModification[AttributesTRAn.ATTRIBUTES_UPGRADABLE_COUNT];
		for (int i = 0; i < attrMean.length; i++) {
			attrMean[i] = new AttributeModification(
					AttributesTRAn.ALL_ATTRIBUTES[AttributesTRAn.FIRST_INDEX_ATTRIBUTE_UPGRADABLE + i], //
					meanAttribute);
		}

		for (int i = 0, n = excess; i < n; i++) {
			am = attrMean[ATTRIBUTES_HUMAN_RECEIVING_MEAN_EXCESS[i].getIndex()
					- AttributesTRAn.FIRST_INDEX_ATTRIBUTE_UPGRADABLE];
			am.setValue(am.getValue() + 1);
		}
		HUMAN_STARTING_ATTRIBUTES = attrMean;
	}

	private PlayerCharacterTypesTRAn() {}

	public enum PlayerCharacterTypes implements ObjectNamedID {
		Human(HUMAN_STARTING_ATTRIBUTES), //
		Wizard(new AttributeModification[] { //
				new AttributeModification(AttributesTRAn.Strength, 0), //
				new AttributeModification(AttributesTRAn.Constitution, 0), //
				new AttributeModification(AttributesTRAn.Health, 0), //
				new AttributeModification(AttributesTRAn.Defense, 0), //
				new AttributeModification(AttributesTRAn.Dexterity, 2), //
				new AttributeModification(AttributesTRAn.Precision, 2), //
				new AttributeModification(AttributesTRAn.Intelligence, 40), //
				new AttributeModification(AttributesTRAn.Wisdom, 33), //
				new AttributeModification(AttributesTRAn.Faith, 23) //
		}), //
		// TODO 04/01/2022 refactor to the new TOTAL_STARTING_ATTRIBUTES
		// (which is 100, i.e. 11 each more or less)
		Priest(new AttributeModification[] { //
				new AttributeModification(AttributesTRAn.Strength, 2), //
				new AttributeModification(AttributesTRAn.Constitution, 2), //
				new AttributeModification(AttributesTRAn.Health, 1), //
				new AttributeModification(AttributesTRAn.Defense, 6), //
				new AttributeModification(AttributesTRAn.Dexterity, 3), //
				new AttributeModification(AttributesTRAn.Precision, 3), //
				new AttributeModification(AttributesTRAn.Intelligence, 30), //
				new AttributeModification(AttributesTRAn.Wisdom, 35), //
				new AttributeModification(AttributesTRAn.Faith, 55) //
		}), //
		Warrior, /* Ogre */ //
		Ranger /* Elf */, //
		Monk, //
		Miner /* Dwarf */,
		/** A Necromancer-magus that uses its own blood and life to cast spells */
		Bloodgus;

		protected final AttributeModification[] startingAttribues;

		private PlayerCharacterTypes() { this(null); }

		private PlayerCharacterTypes(AttributeModification[] startingAttribues) {
			//
			this.startingAttribues = startingAttribues;
		}

		//

		@Override
		public Long getID() { return (long) ordinal(); }

		@Override
		public String getName() { return name(); }

		//

		public void applyStartingAttributes(PlayerTRAn player) {
			CreatureAttributes ca;
			ca = player.getAttributes();
			if (startingAttribues != null) {
				for (int i = 0, n = startingAttribues.length; i < n; i++) {
					ca.setOriginalValue( //
							AttributesTRAn.ALL_ATTRIBUTES[AttributesTRAn.FIRST_INDEX_ATTRIBUTE_UPGRADABLE + i], //
							startingAttribues[i].getValue());
				}
			}
//			ca.setOriginalValue(AttributesTRAn.Velocity.getIndex(),
//					GModalityTRAnBaseWorld.SPACE_SUB_UNITS_EVERY_UNIT_EX AMPLE_TRAN * 2);

			for (AttributeModification am : STARTING_ATTRIBUTES_OF_ALL_RACES) {
				ca.setOriginalValue(am.getAttributeModified(), am.getValue());
			}
		}

		@Override
		public boolean setID(Long newID) { return false; }
	}
}