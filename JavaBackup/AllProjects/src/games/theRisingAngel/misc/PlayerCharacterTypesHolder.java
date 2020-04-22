package games.theRisingAngel.misc;

import java.util.Arrays;

import games.generic.controlModel.misc.CreatureAttributes;
import games.theRisingAngel.PlayerTRAr;
import tools.ObjectNamedID;

public class PlayerCharacterTypesHolder {
	public static final int TOTAL_STARTING_ATTRIBUTES = 200, //
			HUMAN_MEAN_ATTRIBUTES = TOTAL_STARTING_ATTRIBUTES / AttributesTRAr.ATTRIBUTES_UPGRADABLE_COUNT, //
			HUMAN_MEAN_EXCESS_TOTAL = TOTAL_STARTING_ATTRIBUTES
					- (HUMAN_MEAN_ATTRIBUTES * AttributesTRAr.ATTRIBUTES_UPGRADABLE_COUNT);
	public static final AttributesTRAr[] ATTRIBUTES_HUMAN_RECEIVING_MEAN_EXCESS = { AttributesTRAr.Strength,
			AttributesTRAr.Intelligence, AttributesTRAr.Wisdom, AttributesTRAr.Defense, AttributesTRAr.Constitution,
			AttributesTRAr.Dexterity, AttributesTRAr.Health, AttributesTRAr.Precision, AttributesTRAr.Wisdom };

	private static final int[] HUMAN_STARTING_ATTRIBUTES;
	static {
		int[] attrMean;
		attrMean = new int[AttributesTRAr.ATTRIBUTES_UPGRADABLE_COUNT];
		Arrays.fill(attrMean, HUMAN_MEAN_ATTRIBUTES);
		for (int i = 0, n = HUMAN_MEAN_EXCESS_TOTAL; i < n; i++) {
			attrMean[ATTRIBUTES_HUMAN_RECEIVING_MEAN_EXCESS[i].getIndex()
					- AttributesTRAr.FIRST_INDEX_ATTRIBUTE_UPGRADABLE]++;
		}
		HUMAN_STARTING_ATTRIBUTES = attrMean;
	}

	private PlayerCharacterTypesHolder() {
	}

	public enum PlayerCharacterTypes implements ObjectNamedID {
		Human(HUMAN_STARTING_ATTRIBUTES), //
		Wizard(new int[] { 10, 10, 10, 10, 12, 12, 55, 43, 38 }), //
		Monk(new int[] { 12, 12, 11, 17, 14, 14, 30, 35, 55 }), //
		Ogre, Elf, Dwarf,
		/** A Necromancer-magus that uses its own blood and life to cast spells */
		Bloodgus;

		protected final int[] startingAttribues;

		private PlayerCharacterTypes() {
			this(null);
		}

		private PlayerCharacterTypes(int[] startingAttribues) {
			this.startingAttribues = startingAttribues;
		}

		public void applyStartingAttributes(PlayerTRAr player) {
			CreatureAttributes ca;
			ca = player.getAttributes();
			for (int i = 0, n = startingAttribues.length; i < n; i++) {
				ca.setOriginalValue(AttributesTRAr.FIRST_INDEX_ATTRIBUTE_UPGRADABLE + i, startingAttribues[i]);
			}
		}

		@Override
		public Integer getID() {
			return ordinal();
		}

		@Override
		public String getName() {
			return name();
		}
	}
}