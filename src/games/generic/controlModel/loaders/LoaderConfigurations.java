package games.generic.controlModel.loaders;

import games.generic.controlModel.GController;

public abstract class LoaderConfigurations extends LoaderGeneric {

	//

	@Override
	public LoadStatusResult loadInto(GController gc) {
//		throw new UnsupportedOperationException("Not Implemented");
		return LoadStatusResult.Success;
	}
}