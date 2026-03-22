package games.generic.view;

import java.util.function.Function;

import geometry.ObjectLocated;

/**
 * Class that provides {@link DrawableObj} picking them in some way: from File
 * System, a Database, from URL, creating by code, etc.
 */
public abstract class DrawableInstantiator implements Function<ObjectLocated, DrawableObj> {

	public DrawableInstantiator() {}

}