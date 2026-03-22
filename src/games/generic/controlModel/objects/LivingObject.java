package games.generic.controlModel.objects;

import games.generic.controlModel.GModality;
import games.generic.controlModel.damage.DamageDealerGeneric;
import games.generic.controlModel.damage.DamageGeneric;
import games.generic.controlModel.damage.DamageReceiverGeneric;
import games.generic.controlModel.events.event.EventDamage;
import games.generic.controlModel.holders.ResourceRechargeableHolder;
import games.generic.controlModel.rechargeable.resources.RechargeableResourceType;
import games.generic.controlModel.rechargeable.resources.ResourceRechargeableStrategy;
import games.generic.controlModel.rechargeable.resources.holders.LifeHavingObject;
import games.generic.controlModel.subimpl.GEventInterfaceRPG;
import games.generic.controlModel.subimpl.GModalityRPG;

public interface LivingObject
		extends DestructibleObject, LifeHavingObject, DamageReceiverGeneric, ResourceRechargeableHolder {

	@Override
	public default int getLife() { return this.getAmount(getLifeResourceType()); }

	@Override
	public default int getLifeMax() { return this.getMaxAmount(getLifeResourceType()); }

	/**
	 * Shorthand to get the life regeneration.<br>
	 * Could be intended as "amount per second".
	 */
	public default int getLifeRegeneration() { return this.getRechargeAmount(getLifeResourceType()); }

	public RechargeableResourceType getLifeResourceType();

	//

	@Override
	public default void setLife(int life) { this.setAmount(getLifeResourceType(), life); }

	@Override
	public default void setLifeMax(int lifeMax) { this.setMaxAmount(getLifeResourceType(), lifeMax); }

	public default void setLifeRegeneration(int lifeRegenation) {
		this.setRechargeAmount(getLifeResourceType(), lifeRegenation);
	}

	//

	/**
	 * Simplest implementation of a way to recharge all resources: through
	 * {@link ResourceRechargeableStrategy #rechargeResources(java.util.Map).}
	 */
	public default void rechargeResources() {
		this.getResourceRechargeableStrategy().rechargeResources(getRechargableResources());
	}

	// TODO EVENTS

	/**
	 * Similar to {@link #fireDestructionEvent(GModality)}, upon receiving damage
	 * (that means: "during the {@link #receiveDamage(GModality, int)} call") this
	 * event should be fired, in case of complex games, to notify all objects that
	 * "responds to a damage-received event" that this kind of event has
	 * occurred.<br>
	 * A reply/reaction to the "raw damage received" could be a damage reduction.
	 */
	public default EventDamage fireDamageReceived(GModality gm, DamageGeneric originalDamage,
			DamageDealerGeneric source, int damageAmountToBeApplied) {
		GModalityRPG gmodrpg;
		GEventInterfaceRPG geie1;
//		GameObjectsManager gom;
		if (gm == null || (!(gm instanceof GModalityRPG)))
			return null;
		gmodrpg = (GModalityRPG) gm;
//		gom = gmodrpg.getGameObjectsManagerDelegated();
		geie1 = (GEventInterfaceRPG) gmodrpg.getGameObjectsManager().getGEventInterface();
		return geie1.fireDamageReceivedEvent(gmodrpg, source, this, originalDamage, damageAmountToBeApplied);
//		gom.dealsDamageTo(source, this, originalDamage);// cannot "deals" damage because it's already dealt
	}
	// , int actualDamageReceived);

	// , int actualHealingReceived);

	//

	//

	@Override
	public default boolean shouldBeDestroyed() { return this.getLife() <= 0; }

	@Override
	public default void initSetRechargeableResources() {
		ResourceRechargeableHolder.super.initSetRechargeableResources();
	}
}