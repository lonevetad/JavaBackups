package tests.staticAbilities;

import games.generic.controlModel.GModel;

public abstract class AbstractAbility extends SomethingWithIDName {
	private static final long serialVersionUID = 54000L;
	private static long serialID = 0;

	public AbstractAbility(String name) {
		super(name);
		this.instanceID = Long.valueOf(serialID++);
	}

	public abstract boolean canApply(GModel gm, Creature owner);

	public abstract void applyTo(GModel gm, Creature owner);
}
