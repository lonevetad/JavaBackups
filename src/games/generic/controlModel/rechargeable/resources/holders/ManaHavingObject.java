package games.generic.controlModel.rechargeable.resources.holders;

import tools.ObjectNamedID;

public interface ManaHavingObject extends ObjectNamedID {

	public int getMana();

	public int getManaMax();

	/**
	 * Shorthand to get the mana regeneration.<br>
	 * Could be intended as "amount per second".
	 */
	public int getManaRegeneration();

	/**
	 * Raw setting, like assigning a variable's value.
	 */
	public void setMana(int mana);

	/**
	 * Raw setting, like assigning a variable's value, like {@link #setMana(int).
	 * Could invoke this last method if the actual mana (returned by
	 * {@link #getMana()} is greater than the given parameter).
	 */
	public void setManaMax(int manaMax);

	/** See {@link #getManaRegeneration()}. */
	public void setManaRegeneration(int manaRegenation);

	public default void spendMana(int amount) {
		amount = getMana() - amount;
		setMana(amount > 0 ? amount : 0);
	}
}