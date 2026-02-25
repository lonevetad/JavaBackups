package games.theRisingAngel.creatures;

import games.generic.controlModel.misc.IEnumAlike;
import games.theRisingAngel.enums.CreatureTypesTRAn;

/**
 * Marker interface, used in the enum {@link CreatureTypesTRAn}.
 *
 * @author ottin
 *
 */
public interface CreatureTypeTRAn extends IEnumAlike {

	@Override
	public default boolean setID(Long ID) {
		return false;
	}
}
