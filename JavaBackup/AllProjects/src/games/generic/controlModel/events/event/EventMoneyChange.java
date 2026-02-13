package games.generic.controlModel.events.event;

import games.generic.controlModel.events.ExampleGameEvents;
import games.generic.controlModel.events.GEvent;
import games.generic.controlModel.misc.Currency;
import games.generic.controlModel.player.PlayerGeneric;

public class EventMoneyChange extends GEvent {
	private static final long serialVersionUID = 6986301488631859L;
	protected int originalValue, newValue;
	protected PlayerGeneric player;
	protected Currency currency;

	public EventMoneyChange() { super(); }

	public EventMoneyChange(PlayerGeneric player, Currency currency, int originalValue, int newValue) {
		this();
		this.player = player;
		this.currency = currency;
		this.originalValue = originalValue;
		this.newValue = newValue;
	}

	//

	@Override
	public String getName() { return ExampleGameEvents.MoneyChanged.getName(); }

	public int getOriginalValue() { return originalValue; }

	public int getNewValue() { return newValue; }

	public Currency getCurrency() { return this.currency; }

	public PlayerGeneric getPlayer() { return player; }

	//

	public void setOriginalValue(int originalValue) { this.originalValue = originalValue; }

	public void setNewValue(int newValue) { this.newValue = newValue; }

	public void setCurrency(Currency currency) { this.currency = currency; }

	public void setPlayer(PlayerGeneric player) { this.player = player; }

	//

	public int getDifference() { return this.newValue - this.originalValue; }

}