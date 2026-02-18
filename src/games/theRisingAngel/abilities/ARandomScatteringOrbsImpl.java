package games.theRisingAngel.abilities;

import java.awt.Point;

import games.generic.controlModel.damage.DamageGeneric;
import games.generic.controlModel.damage.DamageReceiverGeneric;
import games.theRisingAngel.GModalityTRAnBaseWorld;

public class ARandomScatteringOrbsImpl extends ARandomScatteringOrbs {
	private static final long serialVersionUID = 1L;

	public ARandomScatteringOrbsImpl(GModalityTRAnBaseWorld gameModality) { super(gameModality); }

	@Override
	public int getMaxAmountScatteringOrbs() { // TODO Auto-generated method stub
		return 0;
	}

	@Override
	public DamageGeneric getDamageOrbs(ScatteringOrb orb, DamageReceiverGeneric target) { // TODO Auto-generated method
																							// stub
		return null;
	}

	@Override
	public int getRadiusScattering() { // TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Point getCenterOfScattering() { // TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ScatteringOrb newOrb() { // TODO Auto-generated method stub
		return null;
	}
}