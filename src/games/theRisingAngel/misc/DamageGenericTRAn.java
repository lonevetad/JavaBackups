package games.theRisingAngel.misc;

import games.generic.controlModel.GModality;
import games.generic.controlModel.damage.DamageGeneric;
import games.generic.controlModel.damage.DamageTypeGeneric;
import games.theRisingAngel.enums.DamageTypesTRAn;

public class DamageGenericTRAn extends DamageGeneric {
	private static final long serialVersionUID = -1084899890079L;

	public DamageGenericTRAn(int damageAmount, DamageTypeGeneric damageType) {
		super(damageAmount, damageType);
	}

	@Override
	public void loadInnerObjectNamedID(GModality gm, String typeName) {
		this.setType(DamageTypesTRAn.valueOf(typeName));
	}

}
