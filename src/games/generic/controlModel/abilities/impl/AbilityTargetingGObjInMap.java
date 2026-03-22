package games.generic.controlModel.abilities.impl;

import java.util.function.Predicate;

import dataStructures.isom.InSpaceObjectsManagerImpl;
import games.generic.controlModel.GObjectsInSpaceManager;
import games.generic.controlModel.abilities.AbilityGeneric;
import geometry.ObjectLocated;

public interface AbilityTargetingGObjInMap extends AbilityGeneric {
	/**
	 * Returns a filter that defines what instances will be affected by this ability
	 * and what won't.<br>
	 * Used in
	 * {@link InSpaceObjectsManagerImpl#fetch(geometry.AbstractShape2D, Predicate)} by
	 * {@link GObjectsInSpaceManager}
	 */
	public abstract Predicate<ObjectLocated> getTargetsFilter();
}