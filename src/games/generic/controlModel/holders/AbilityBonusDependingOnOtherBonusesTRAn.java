package games.generic.controlModel.holders;

import games.generic.controlModel.GModality;
import games.generic.controlModel.abilities.impl.AbilityBonusDependingOnOtherBonuses;
import games.generic.controlModel.attributes.AttributeIdentifier;
import games.theRisingAngel.HelperWithAttributeModificationsTRAn;

public class AbilityBonusDependingOnOtherBonusesTRAn extends AbilityBonusDependingOnOtherBonuses
		implements HelperWithAttributeModificationsTRAn {

	public AbilityBonusDependingOnOtherBonusesTRAn(GModality gameModality, String name,
			AttributeIdentifier[] attributesToModify, AttributeIdentifier[][] modifcationsEachAttributes) {
		super(gameModality, name, attributesToModify, modifcationsEachAttributes);
	}

}
