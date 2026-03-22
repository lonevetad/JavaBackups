package games.generic.controlModel.abilities.impl;

public abstract class AbilityBaseWithCustomName extends AbilityBaseImpl {
	private static final long serialVersionUID = -54986284910023589L;

	public AbilityBaseWithCustomName(String name) { this(name, 0); }

	public AbilityBaseWithCustomName(String name, int level) {
		super(level);
		this.name = name;
	}

	protected String name;

	@Override
	public String getName() { return name; }
}