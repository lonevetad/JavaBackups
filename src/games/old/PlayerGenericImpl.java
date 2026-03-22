package games.old;

import java.util.List;

import games.generic.controlModel.GModality;
import games.generic.controlModel.events.IGEvent;
import games.generic.controlModel.player.PlayerGeneric;
import tools.UniqueIDProvider;

/**
 * Should not be used since a "in game player" should extend some "creature"
 * class.
 */
@Deprecated
public class PlayerGenericImpl implements PlayerGeneric {
	protected Integer ID;
	protected String name;
	protected GModality gameModality;

	public PlayerGenericImpl(GModality gameModality) {
		super();
		this.gameModality = gameModality;
		initializeID();
// TODO
	}

	protected void initializeID() {
		this.ID = UniqueIDProvider.UDIP_GENERAL.getNewID();
	}

	//

	@Override
	public GModality getGameModality() {
		return gameModality;
	}

	@Override
	public Integer getID() {
		return ID;
	}

	@Override
	public List<String> getEventsWatching() {
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

	//

	@Override
	public void setGameModality(GModality gameModality) {
		this.gameModality = gameModality;
	}

	/**
	 * Weird, but some {@link GModality} could need to set the identifier.
	 */
	public void setID(Integer iD) {
		ID = iD;
	}

	//

	@Override
	public void notifyEvent(GModality modality, IGEvent ge) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLeavingMap() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEnteringInGame(GModality gm) {
		// TODO Auto-generated method stub

	}

}
