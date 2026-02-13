package games.generic.controlModel.events;

import java.awt.Point;

import games.generic.controlModel.GModality;
import games.generic.controlModel.damage.DamageDealerGeneric;
import games.generic.controlModel.damage.DamageGeneric;
import games.generic.controlModel.events.event.EventDamage;
import games.generic.controlModel.events.event.EventResourceRecharge;
import games.generic.controlModel.holders.ResourceRechargeableHolder;
import games.generic.controlModel.objects.LivingObject;
import games.generic.controlModel.player.PlayerGeneric;
import games.generic.controlModel.rechargeable.resources.ResourceAmountRecharged;
import games.generic.controlModel.subimpl.GModalityET;
import geometry.ObjectLocated;
import tools.ObjectWithID;

/**
 * Holder of ALL event-firing methods and the {@link GEventManager}.<br>
 * All of those methods are removed and separated from {@link GModality} just to
 * make it thinner and less confusing.
 * <p>
 * COMMON PATTERN of event-firing methods:
 * <code>void fireXXX({@link GModality} gm, {@link PlayerGeneric} player)</code>,
 * like {@link #firePlayerEnteringInMap(GModality, PlayerGeneric)}.
 */
public interface GEventInterface extends Cloneable {

	public void setNewGameEventManager(GModalityET gameModality);

	public GEventManager getGameEventManager();

//	public void setGameEventManager(GameEventManager gem);

	// TODO put here ALL methods

	public void fireGameObjectAdded(GModalityET gameModality, ObjectLocated o);

	public void fireGameObjectRemoved(GModalityET gameModality, ObjectLocated o);

	/**
	 * Fire an event representing the movement for an object from a point to its
	 * (new) location.
	 */
	public void fireGameObjectMoved(GModalityET gameModality, Point previousLocation, ObjectLocated o);

	/** After loading the map, creating stuffs and enemies, etc, fire this event */
	public void firePlayerEnteringInMap(GModalityET gameModality, PlayerGeneric p);

	/**
	 * Put as an example, most useful on RPG and RTS games, could be ignored and
	 * left empty if not needed. <br>
	 * TODO docs
	 */
	public EventDamage fireDamageDealtEvent(GModalityET gm, DamageDealerGeneric source, LivingObject target,
			DamageGeneric damage);

	/**
	 * Fire a damage event and returns it, so informations like reductions and malus
	 * can be applied by listeners and processed by the injured living object
	 * (usually, a creature).
	 *
	 * @param damageAmountToBeApplied: damage original subtracted by the amount of
	 *                                 damage reduction provided by the target
	 *                                 (usually, those two damages amounts are the
	 *                                 same)
	 */
	public EventDamage fireDamageReceivedEvent(GModalityET gm, DamageDealerGeneric source, LivingObject target,
			DamageGeneric originalDamage, int damageAmountToBeApplied);

	public EventDamage fireCriticalDamageDealtEvent(GModalityET gm, DamageDealerGeneric source, LivingObject target,
			DamageGeneric damage);

	/**
	 * See
	 * {@link #fireDamageReceivedEvent(GModalityET, DamageDealerGeneric, LivingObject, DamageGeneric, int)}.
	 */
	public EventDamage fireDamageCriticalReceivedEvent(GModalityET gm, DamageDealerGeneric source, LivingObject target,
			DamageGeneric originalDamage, int damageAmountToBeApplied);

	/**
	 * After someone's resource is being recharge, fire this event.<br>
	 *
	 * @param <SourceRecharge>           The type of the object is performing the
	 *                                   recharge operation.
	 * @param gaModality                 {@link GModalityET}
	 * @param whoIsPerformingTheRecharge The object who is performing the recharge
	 *                                   operation.
	 * @param receiver                   the {@link ResourceRechargeableHolder}
	 *                                   which is receiving the recharge.
	 * @param rechargeInstance
	 * @return the event {@link EventResourceRecharge} describing this recharge
	 */
	public <SourceRecharge extends ObjectWithID> EventResourceRecharge<SourceRecharge> fireResourceRechargeReceivedEvent(
			GModalityET gaModality, SourceRecharge whoIsPerformingTheRecharge, ResourceRechargeableHolder receiver,
			ResourceAmountRecharged rechargeInstance);

	/**
	 * Similar to
	 * {@link #fireResourceRechargeReceivedEvent(GModalityET, ObjectWithID, LivingObject, ResourceAmountRecharged)},
	 * but this event is fired upon recharging someone else (the source of the
	 * recharge is the fourth parameter while the second parameter is the target;
	 * the target is the same of
	 * {@link #fireResourceRechargeReceivedEvent(GModalityET, ObjectWithID, LivingObject, ResourceAmountRecharged)}).
	 *
	 *
	 * @param <SourceRecharge>           The type of the object is performing the
	 *                                   recharge operation.
	 * @param gaModality                 {@link GModalityET}
	 * @param whoIsPerformingTheRecharge The object who is performing the recharge
	 *                                   operation.
	 * @param receiver                   the {@link ResourceRechargeableHolder}
	 *                                   which is receiving the recharge.
	 * @return the event {@link EventResourceRecharge} describing this recharge
	 */
	public <SourceRecharge extends ObjectWithID> EventResourceRecharge<SourceRecharge> fireResourceRechargeGivenEvent(
			GModalityET gaModality, SourceRecharge whoIsPerformingTheRecharge, ResourceRechargeableHolder receiver,
			ResourceAmountRecharged rechargeInstance);

}