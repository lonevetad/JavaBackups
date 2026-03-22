package games.generic.controlModel.misc;

import games.generic.controlModel.GModality;

/**
 * Factory instantiating a new object which could require an instance of
 * {@link GModality}.
 */
public interface FactoryObjGModalityBased<T> {
	public T newInstance(GModality gm);
}