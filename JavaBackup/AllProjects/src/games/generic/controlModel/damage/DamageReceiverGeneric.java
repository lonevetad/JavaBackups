package games.generic.controlModel.damage;

import games.generic.controlModel.GModality;
import games.generic.controlModel.objects.GameObjectGeneric;
import games.generic.controlModel.rechargeable.resources.holders.LifeHavingObject;

/**
 * Marker interface for an entity capable of receiving damage, analog and
 * "opposite" to {@link DamageDealerGeneric}.<br>
 * It's enriched with info useful for damage computation.
 */
public interface DamageReceiverGeneric extends LifeHavingObject, GameObjectGeneric {

	/**
	 * Make this object receiving a non-negative amount of damage, in a context
	 * expressed by {@link GModality}, which could be used to fire events.
	 */
	public void receiveDamage(GModality gm, DamageGeneric damage, DamageDealerGeneric source);

	/***
	 * NOTE: this is NOT a percentage (i.e. "%"), but a "per thousand" probability!!
	 * <p>
	 * Related to
	 * {@link DamageDealerGeneric#getProbabilityPerThousandHit(DamageTypeGeneric)}
	 */
	public int getProbabilityPerThousandAvoid(DamageTypeGeneric damageType);

	/**
	 * Returns the raw/absolute amount of damage reduction.
	 */
	public int getDamageReduction(DamageTypeGeneric damageType);

	/**
	 * Returns the relative (percentage) amount of damage reduction.
	 */
	public int getDamageReductionPercentage(DamageTypeGeneric damageType);

	/**
	 * Similar to
	 * {@link DamageDealerGeneric#getProbabilityPerThousandHit(DamageTypeGeneric)},
	 * but about avoiding the critical damage. <br>
	 * See it for further informations.
	 */
	public int getProbabilityPerThousandAvoidCritical(DamageTypeGeneric damageType);

	/**
	 * Percentage amount of multiplication of the damage. Should be positive but
	 * it's not mandatory.
	 */
	public int getPercentageCriticalStrikeReduction(DamageTypeGeneric damageType);

	/**
	 * Returns a "per-thousand" value, as
	 * {@link DamageDealerGeneric#getLuckPerThousand()}.
	 *
	 * @return a "per-thousand" value, as
	 *         {@link DamageDealerGeneric#getLuckPerThousand()}.
	 */
	public default int getLuckPerThousand() { return 0; }
}