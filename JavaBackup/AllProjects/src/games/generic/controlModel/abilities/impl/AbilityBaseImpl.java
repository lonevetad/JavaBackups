package games.generic.controlModel.abilities.impl;

import games.generic.controlModel.GModality;
import games.generic.controlModel.abilities.AbilityGeneric;
import games.generic.controlModel.holders.GModalityHolder;
import tools.ObjectWithID;
import tools.UniqueIDProvider;

public abstract class AbilityBaseImpl implements AbilityGeneric {
	private static final long serialVersionUID = -8784886155L;

	public AbilityBaseImpl() { this(0); }

	public AbilityBaseImpl(int level) {
		this.level = level;
		this.assignID();
	}

	protected int level;
	protected Long ID;
	protected ObjectWithID owner;
	protected GModality gameModality;

	/**
	 * Overload-designed.
	 * <p>
	 * It assign an ID to the respective field through
	 * {@link UniqueIDProvider.GENERAL_UNIQUE_ID_PROVIDER}.<br>
	 * Other ways could be defined in order to provide a specific and non-shared
	 * {@link UniqueIDProvider}.
	 */
	protected void assignID() { this.ID = UIDP_ABILITY.getNewID(); }

	@Override
	public Long getID() { return ID; }

	@Override
	public String getName() { return null; }

	@Override
	public ObjectWithID getOwner() { return owner; }

	@Override
	public int getLevel() { return level; }

	@Override
	public GModality getGameModality() { return gameModality; }

	//

	@Override
	public void setLevel(int level) { this.level = level; }

	@Override
	public void setOwner(ObjectWithID owner) {
		this.owner = owner;
		if (owner != null && owner instanceof GModalityHolder) {
			onAddingToOwner(((GModalityHolder) owner).getGameModality());
		}
	}

	@Override
	public void setGameModality(GModality gameModality) { this.gameModality = gameModality; }

	@Override
	public boolean setID(Long newID) {
		if (this.ID != null || newID == null) { return false; }
		this.ID = newID;
		return true;
	}
}