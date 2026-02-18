package games.theRisingAngel.enums;

import games.generic.controlModel.events.ExampleGameEvents;
import games.generic.controlModel.events.IGEvent;

/** Taken from {@link ExampleGameEvents}. */
public enum EventsTRAn implements IGEvent {
	Destroyed("OIS"), ObjectAdded("OIS"), ObjectRemoved("OIS"), ObjectMoved("OIS"), // OIS = Object In Space
	DamageInflicted("Dmg"), DamageReceived("Dmg"), DamageCriticalInflicted("Dmg"), DamageCriticalReceived("Dmg"),
	DamageMissed("Dmg"), DamageAvoided("Dmg"), //
	ResourceRechargeReceived("ResRech"), ResourceRechargeGiven("ResRech"), //
	PickedUpMoney("Money"), MoneyChanged("Money"), PickedUpDrop("Drop"), DropReleased("Drop"),
	//
	AttackPerformed("User"), SpellCasted("User"), //
	UserInteraction("User");

	protected final String type;

	EventsTRAn(String t) { this.type = t; }

	@Override
	public Long getID() { return (long) ordinal(); }

	public String getSuperType() { return type; }

	@Override
	public String getName() { return name(); }
}