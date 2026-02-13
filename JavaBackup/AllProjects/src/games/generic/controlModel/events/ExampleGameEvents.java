package games.generic.controlModel.events;

public enum ExampleGameEvents implements IGEvent { // implements GameEventFactory
	Destroyed("OIS"), ObjectAdded("OIS"), ObjectRemoved("OIS"), ObjectMoved("OIS"), // OIS = Object In Space
	AttackPerformed("Attack"), //
	DamageInflicted("Dmg"), DamageReceived("Dmg"), HealReceived("Heal"), HealGiven("Heal"), //
	PickedUpMoney("Money"), MoneyChanged("Money"), PickedUpDrop("Drop"), DropReleased("Drop")
	//
	;

	protected final String type;

//	ExampleGameEvents(GameEventFactory del) {
//		this.delegate = del;
//	}
	ExampleGameEvents(String t) { this.type = t; }

	/** SHOULD NOT BE USED */
	@Deprecated
	public String getSuperType() { return type; }

	@Override
	public Long getID() { return (long) ordinal(); }

	@Override
	public String getName() { return name(); }

	@Override
	public boolean setID(Long newID) { return false; }

//	public GameEvent newGameEvent(Integer id, String name) {
//		return delegate.newGameEvent(ordinal(), name());
//	}

}