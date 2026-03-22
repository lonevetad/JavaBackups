package games.old.rechargeable.resources;

import java.util.Objects;

import games.generic.controlModel.GModality;
import games.generic.controlModel.objects.TimedObject;

/** Defines a way to heal a {@link HealingObject} over time. */
public abstract class IHealingResourcesOverTimeStrategy implements TimedObject {
	private static final long serialVersionUID = 564527785L;

	public IHealingResourcesOverTimeStrategy(HealingObject objToHeal) {
		super();
		this.objToHeal = Objects.requireNonNull(objToHeal);
	}

	protected final HealingObject objToHeal;

	public HealingObject getObjToHeal() { return objToHeal; }

	@Override
	public final void act(GModality modality, int timeUnits) { healOverTime(modality, timeUnits); }

	/**
	 * Performs the act of healing ALL of the resources of a {@link HealingObject}.
	 */
	public abstract void healOverTime(GModality modality, int timeUnits);

	//

	//

	protected void unsupportedOperationExc() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("This method should not be called");
	}

	@Override
	public void onAddedToGame(GModality gm) { unsupportedOperationExc(); }

	@Override
	public void onRemovedFromGame(GModality gm) { unsupportedOperationExc(); }

	@Override
	public Integer getID() {
		unsupportedOperationExc();
		return null;
	}

	@Override
	public String getName() {
		unsupportedOperationExc();
		return null;
	}
}