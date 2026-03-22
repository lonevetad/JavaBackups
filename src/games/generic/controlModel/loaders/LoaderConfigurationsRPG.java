package games.generic.controlModel.loaders;

import games.generic.controlModel.GController;
import games.generic.controlModel.loaders.LoaderGeneric.LoadStatusResult;
import games.generic.controlModel.subimpl.GameOptionsRPG;

public abstract class LoaderConfigurationsRPG extends LoaderConfigurations {

	public LoaderConfigurationsRPG() { super(); }

	public abstract void loadGameOptions(GameOptionsRPG gopt);

	@Override
	public LoadStatusResult loadInto(GController gc) { return super.loadInto(gc); }
}