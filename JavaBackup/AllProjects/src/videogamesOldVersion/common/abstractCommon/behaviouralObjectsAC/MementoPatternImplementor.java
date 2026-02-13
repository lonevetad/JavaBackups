package common.abstractCommon.behaviouralObjectsAC;

import java.io.Serializable;

import videogamesOldVersion.common.abstractCommon.Memento;

public interface MementoPatternImplementor extends Serializable {

	public Memento createMemento();

	/** Returns true if the reload operation was successfull. */
	public boolean reloadState(Memento m);
	// public <M extends Memento> boolean reloadState(M m);

}