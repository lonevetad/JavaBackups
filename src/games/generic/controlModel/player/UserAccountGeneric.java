package games.generic.controlModel.player;

import java.util.List;

import games.generic.controlModel.GModality;
import tools.ObjectNamedID;
import tools.impl.OWIDLongImpl;

/**
 * Abstraction of a user "outside a specific game modality" (i.e.
 * {@link GModality}), or "in menu, checking options, trading, managing
 * characters and saves, etc".
 */
public abstract class UserAccountGeneric extends OWIDLongImpl implements ObjectNamedID {
	private static final long serialVersionUID = 1L;

	public UserAccountGeneric() { super(); }

	protected String name;
	protected List<Object> allCharacters; // all characters played, all saves

	@Override
	public String getName() { return name; }

	public void setName(String name) { this.name = name; }
}