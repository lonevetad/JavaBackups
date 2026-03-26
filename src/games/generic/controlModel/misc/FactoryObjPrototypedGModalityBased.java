package games.generic.controlModel.misc;

import games.generic.controlModel.GModality;

/**
 * Factory instantiating a new object which could require an instance of
 * {@link GModality}.
 */
public interface FactoryObjPrototypedGModalityBased<T> extends FactoryObjGModalityBased<T> {

	public T getPrototype();

	public void setPrototype(T prototype);

}