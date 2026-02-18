package games.generic.controlModel.events;

import games.generic.controlModel.GModality;
import tools.UniqueIDProvider;

/**
 * Define an event fired during the game.<br>
 * The event MUST hold all informations needed to work with (except for
 * {@link GModality} instance, it's usually not needed)..
 * <p>
 * Could help implementing the event-oriented programming.
 */
public abstract class GEvent implements IGEvent {
	private static final long serialVersionUID = -53224778410585340L;

	private static UniqueIDProvider UIDP_EVENT = UniqueIDProvider.newBasicIDProvider();
	/*
	 * null; public static final UIDProviderLoadedListener
	 * UIDP_LOADED_LISTENER_EVENT = uidp -> { if (uidp != null) { UIDP_EVENT = uidp;
	 * } };
	 *
	 * public static UniqueIDProvider getUniqueIDProvider_Event() { return
	 * UIDP_EVENT; }
	 */
	//

	public GEvent() {
		super();
//		assignID();
		this.ID = UIDP_EVENT.getNewID();
	}

	protected final Long ID;

//	protected void assignID() { this.ID = UIDP_EVENT.getNewID(); }

	/**
	 * This is an INSTANCE identifier, i.e. two events of the same type are
	 * different thanks to this
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public Long getID() { return ID; }

	@Override
	public String getName() { return this.getClass().getSimpleName(); }

	/**
	 * The event will have effect to, presumably, the actual game, represented by
	 * the {@link GameModality}}.
	 */
	/* DEPRECATED since events DO NOT PERFORMS ACTIONS */
	// public void performEvent(GameModality gm);

	@Override
	public boolean setID(Long newID) { return false; }
}