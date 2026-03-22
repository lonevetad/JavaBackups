package games.generic.controlModel.providers;

import java.util.HashMap;
import java.util.Map;

import games.generic.controlModel.GModality;
import tools.json.types.JSONObject;

/**
 * @deprecated because a lot of objects, especially Enums, are serialized by
 *             their name only, so it's impossible to get back their classes;
 *             therefore, {@link JSONObject} can't store their class name. So,
 *             this factory is unusable.
 */
@Deprecated
public abstract class FactoryByClassnameProvider {

	public FactoryByClassnameProvider() {
		this.factoriesByClassname = this.newFactoriesByClassname();
	}

	protected Map<String, FactoryGeneric<?>> factoriesByClassname;

	public Map<String, FactoryGeneric<?>> newFactoriesByClassname() {
		return new HashMap<>();
	}

	//

	public abstract void loadAllFactories(GModality gm);

	public <C> void addFactory(String className, FactoryGeneric<C> factory) {
		this.factoriesByClassname.put(className, factory);
	}

	public <C> void addFactory(Class<C> clazz, FactoryGeneric<C> factory) {
		this.addFactory(clazz.getName(), factory);
	}

	public FactoryGeneric<?> getFactory(String className) {
		return this.factoriesByClassname.get(className);
	}

	@SuppressWarnings("unchecked")
	public <C> FactoryGeneric<C> getFactory(Class<C> clazz) {
		return (FactoryGeneric<C>) this.getFactory(clazz.getName());
	}
}
