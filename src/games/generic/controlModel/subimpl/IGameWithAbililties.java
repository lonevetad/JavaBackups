package games.generic.controlModel.subimpl;

import games.generic.controlModel.providers.AbilitiesProvider;

public interface IGameWithAbililties {

	public AbilitiesProvider getAbilitiesProvider();

	public void setAbilitiesProvider(AbilitiesProvider ap);
}