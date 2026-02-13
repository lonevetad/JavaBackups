package games.theRisingAngel.abilities;

import games.generic.controlModel.GModality;
import games.generic.controlModel.abilities.impl.ASimpleFixedBufferVanishing;
import games.generic.controlModel.events.IGEvent;
import games.generic.controlModel.events.event.EventDamage;
import games.generic.controlModel.misc.AttributeModification;
import games.generic.controlModel.objects.LivingObject;
import games.theRisingAngel.enums.AttributesTRAn;
import games.theRisingAngel.enums.EventsTRAn;

public class AVampireBerserker extends ASimpleFixedBufferVanishing {
	private static final long serialVersionUID = 624478230215L;
	public static final String NAME = "Vampire Hunt";
	public static final int RARITY = 4, BASE_LIFE_LEECH = 5;

	public AVampireBerserker(GModality gameModality) {
		super(gameModality, NAME,
				new AttributeModification[] {
						new AttributeModification(AttributesTRAn.LifeLeechPercentage, BASE_LIFE_LEECH),
						new AttributeModification(AttributesTRAn.LifeRegen, -1),
						new AttributeModification(AttributesTRAn.ManaRegen, 0) });
		setCumulative(true);
		addEventWatched(EventsTRAn.DamageReceived);
		setAbilityEffectDuration(5000);
		setVanishingEffectDuration(3000);
		setRarityIndex(RARITY);
		setMaxAmountStackedTriggerCharges(4);
	}

	@Override
	protected boolean isAcceptableEvent(IGEvent e) {
		return EventsTRAn.DamageReceived.getName() == e.getName() && //
		// I am the receiver?
				((EventDamage) e).isTarget((LivingObject) this.getOwner());
	}

	@Override
	public void doUponAbilityActivated() {
		super.doUponAbilityActivated();
//		removeAndNullifyEffects(); // jet done
		this.attributesToModify[0].setValue(BASE_LIFE_LEECH);
		this.attributesToModify[1].setValue(-1);
		this.attributesToModify[2].setValue(0);
	}

	@Override
	public void doUponAbilityRefreshed() {}

	@Override
	public void vanishEffect(int timeUnit) {// nothing
	}

	@Override
	public void doUponAbilityStartsVanishing() {
		removeAndNullifyEffects();
		this.attributesToModify[0].setValue(0);
		this.attributesToModify[1].setValue(1);
		this.attributesToModify[2].setValue(1);
	}

	@Override
	public void doUponAbilityEffectEnds() {
		super.doUponAbilityEffectEnds();
		removeAndNullifyEffects();
	}
}