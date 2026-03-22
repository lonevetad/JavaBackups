package games.generic.controlModel;

import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.CreatureAttributesBonusesCalculator;
import games.generic.controlModel.misc.IndexableObject.IndexToObjectBackmapping;

public abstract class CreatureAttributesBaseAndDerived extends CreatureAttributes {

	public CreatureAttributesBaseAndDerived(int attributesCount, IndexToObjectBackmapping itai) {
		super(attributesCount, itai);
		this.bonusCalculator = null;
	}

	protected CreatureAttributesBonusesCalculator bonusCalculator;

	//

	public CreatureAttributesBonusesCalculator getBonusCalculator() { return bonusCalculator; }

	public void setBonusCalculator(CreatureAttributesBonusesCalculator bonusCalculator) {
//		if (this.bonusCalculator != null)
//			this.bonusCalculator.setCreatureAttributesSet(null); // CANNOT BE NULL-ED
		this.bonusCalculator = bonusCalculator;
		if (bonusCalculator != null) { bonusCalculator.setCreatureAttributesSet(this); }
	}
}