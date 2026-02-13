package games.theRisingAngel.abilities;

import games.generic.controlModel.GModality;
import games.generic.controlModel.abilities.impl.ASimpleFixedBufferVanishing;
import games.generic.controlModel.misc.AttributeModification;
import games.theRisingAngel.GModalityTRAnBaseWorld;

public class ASimpleFixedBufferVanishingTRAn extends ASimpleFixedBufferVanishing {
	private static final long serialVersionUID = -5605240560L;
	public static final String NAME = "Wounded Berseker";
	public static final int RARITY = 2;

	public static ASimpleFixedBufferVanishingTRAn newInstanceTRAn(GModality gameModality, String name,
			boolean isCumulative, AttributeModification[] attributesMods) {
		ASimpleFixedBufferVanishingTRAn a;
		a = new ASimpleFixedBufferVanishingTRAn(gameModality, name, attributesMods);
		a.setCumulative(isCumulative);
		return a;
	}

	public ASimpleFixedBufferVanishingTRAn(GModality gameModality, String name,
			AttributeModification[] attributesMods) {
		super(gameModality, name, attributesMods);
	}

	@Override
	public int getVanishingTimeThresholdUpdate() { return GModalityTRAnBaseWorld.TIME_SUBUNITS_EACH_TIME_UNIT_TRAn; }
}