package games.generic.controlModel.abilities.impl;

import games.generic.controlModel.GModality;
import games.generic.controlModel.ObjectNamed;
import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.AttributeModification;

public class ASimpleFixedBufferVanishing extends AbilityAttributesModsVanishingOverTime {
	private static final long serialVersionUID = 6560887921022805536L;

	public ASimpleFixedBufferVanishing(GModality gm, String name, AttributeModification[] attributesMods) {
		super(gm, name, attributesMods);
	}

	public ASimpleFixedBufferVanishing(GModality gm, String name, AttributeModification[] attributesMods,
			boolean isCumulative) {
		this(null, name, attributesMods);
		this.setCumulative(isCumulative);
	}

	public ASimpleFixedBufferVanishing(GModality gameModality, String name, AttributeIdentifier[] attributesModified) {
		super(gameModality, name, attributesModified);
	}

	public ASimpleFixedBufferVanishing(GModality gameModality, String name, AttributeIdentifier[] attributesModified,
			boolean isCumulative) {
		this(gameModality, name, attributesModified);
		this.setCumulative(isCumulative);
	}

	protected int abilityEffectDuration, vanishingEffectDuration;

	//

	@Override
	public int getAbilityEffectDuration() { return abilityEffectDuration; }

	@Override
	public int getVanishingEffectDuration() { return vanishingEffectDuration; }

	@Override
	public void setAbilityEffectDuration(int abilityEffectDuration) {
		this.abilityEffectDuration = abilityEffectDuration;
	}

	@Override
	public void setVanishingEffectDuration(int vanishingEffectDuration) {
		this.vanishingEffectDuration = vanishingEffectDuration;
	}

	@Override
	public ASimpleFixedBufferVanishing addEventWatched(ObjectNamed objNamed) {
		this.eventsWatching.add(objNamed.getName());
		return this;
	}

	@Override
	public void doUponAbilityRefreshed() {}

	@Override
	public void doUponAbilityStartsVanishing() {}

	@Override
	public int getVanishingTimeThresholdUpdate() { return 0; }
}