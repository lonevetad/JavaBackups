package games.generic.controlModel.rechargeable.resources.holders;

import games.generic.controlModel.rechargeable.resources.RechargeableResourceType;
import tools.ObjectNamedID;

public interface StaminaHavingObject extends ObjectNamedID {

	public int getStamina();

	public int getStaminaMax();

	/**
	 * Shorthand to get the stamina regeneration.<br>
	 * Could be intended as "amount per second".
	 */
	public int getStaminaRegeneration();

	/**
	 * Raw setting, like assigning a variable's value.
	 */
	public void setStamina(int stamina);

	/**
	 * Raw setting, like assigning a variable's value, like {@link #setStamina(int).
	 * Could invoke this last method if the actual stamina (returned by
	 * {@link #getStamina()} is greater than the given parameter).
	 */
	public void setStaminaMax(int staminaMax);

	/** See {@link #getStaminaRegeneration()}. */
	public void setStaminaRegeneration(int staminaRegenation);

	public default void spendStamina(int amount) {
		amount = getStamina() - amount;
		setStamina(amount > 0 ? amount : 0);
	}

	public RechargeableResourceType getStaminaResourceType();
}