package games.generic.controlModel.holders;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

import games.generic.controlModel.GModality;
import games.generic.controlModel.abilities.AbilityGeneric;
import games.generic.controlModel.objects.GameObjectGeneric;
import tools.json.JSONTypes;
import tools.json.JSONValue;
import tools.json.JSONable;
import tools.json.types.JSONArray;
import tools.json.types.JSONInt;
import tools.json.types.JSONObject;
import tools.json.types.JSONString;

/**
 * Marks the class as having a set of abilities. It's implemented as a
 * {@link Map} to make easier to check if this instance has a particular ability
 * and/or to remove it.
 */
public interface AbilitiesHolder extends GameObjectGeneric {
	public static final String FIELD_ABILITIES = "abilities";

	/**
	 * See this class documentation to understand why this is a {@link Map} and not
	 * a {@link Set}.
	 */
	public Map<String, AbilityGeneric> getAbilities();

	public default void forEachAbilities(BiConsumer<String, AbilityGeneric> action) {
		this.getAbilities().forEach(action);
	}

	@Override
	public default void addMeToGame(GModality gm) {
		GameObjectGeneric.super.addMeToGame(gm);
		forEachAbilities((n, a) -> a.addMeToGame(gm));
	}

	@Override
	public default void onAddedToGame(GModality gm) {
	}

	@Override
	public default void removeMeToGame(GModality gm) {
		GameObjectGeneric.super.removeMeToGame(gm);
		forEachAbilities((n, a) -> a.removeMeToGame(gm));
	}

	@Override
	public default void onRemovedFromGame(GModality gm) {
	}

	public default AbilitiesHolder addAbility(AbilityGeneric ability) {
		if (ability == null) {
			return this;
		}
		this.getAbilities().put(ability.getName(), ability);
		return this;
	}

	public default AbilitiesHolder removeAbility(AbilityGeneric ability) {
		if (ability == null) {
			return this;
		}
		this.removeAbilityByName(ability.getName());
		return this;
	}

	public default AbilitiesHolder removeAbilityByName(String abilityName) {
		if (abilityName == null) {
			return this;
		}
		this.getAbilities().remove(abilityName);
		return this;
	}

	//

	// JSON-related

	//

	@Override
	public default void toJSONValue(JSONObject wrapper) {
		GameObjectGeneric.super.toJSONValue(wrapper);
		//
		Map<String, AbilityGeneric> abil = this.getAbilities();
		JSONValue[] abilitiesArrayjsoned = new JSONValue[abil.size()];
		final int[] index = { 0 };
		// mapping each ability
		this.getAbilities().forEach((abilityName, ability) -> {
			JSONObject abilityJSONed = new JSONObject();
			ability.toJSONValue(abilityJSONed);
			abilitiesArrayjsoned[index[0]++] = abilityJSONed;
		});
		JSONArray jsonedAbilitiesArrayed = new JSONArray(JSONTypes.ArrayHomogeneousType, abilitiesArrayjsoned,
				JSONTypes.Object);
		wrapper.addField(FIELD_ABILITIES, jsonedAbilitiesArrayed);
	}

	@Override
	public default void loadFromJSONObject(final GModality gm, JSONObject wrapper) throws IllegalArgumentException {
		GameObjectGeneric.super.loadFromJSONObject(gm, wrapper);
		//
		if (!wrapper.hasField(FIELD_ABILITIES)) {
			this.raiseExceptionMissingField(FIELD_ABILITIES, JSONTypes.Object);
		}
		JSONValue jsonedAbilitiesArrayed = wrapper.getFieldValue(FIELD_ABILITIES);
		if (!jsonedAbilitiesArrayed.isType(JSONTypes.ArrayHomogeneousType)) {
			this.raiseExceptionIllegalTypeField(FIELD_ABILITIES, JSONTypes.ArrayHomogeneousType,
					jsonedAbilitiesArrayed);
		}
		// JSONValue[] abilitiesArrayjsoned ;
		((JSONArray) jsonedAbilitiesArrayed).forEach((index, abilityJSONed) -> {
			String abilityName = null;
			int level = 0;
			Map<String, Object> extraParameters = null;
			if (abilityJSONed.isType(JSONTypes.String)) {
				abilityName = ((JSONString) abilityJSONed).asString();
			} else if (abilityJSONed.isType(JSONTypes.Object)) {

				JSONObject ablJSON = (JSONObject) abilityJSONed;
				extraParameters = JSONable.newFieldValuesMap();
				// name
				JSONValue abilityNameJSONed = ablJSON.getFieldValue("name");
				if (!abilityNameJSONed.isType(JSONTypes.String)) {
					this.raiseExceptionIllegalTypeField("name", JSONTypes.String, abilityNameJSONed);
				}
				abilityName = ((JSONString) abilityNameJSONed).asString();
				// level
				JSONValue abilityLevelJSONed = ablJSON.getFieldValue(AbilityGeneric.FIELD_LEVEL);
				if (!abilityLevelJSONed.isType(JSONTypes.Int)) {
					this.raiseExceptionIllegalTypeField(AbilityGeneric.FIELD_LEVEL, JSONTypes.Int, abilityLevelJSONed);
				}
				Integer abilityLevel = ((JSONInt) abilityLevelJSONed).asInt();
				level = abilityLevel;
				extraParameters.put(AbilityGeneric.FIELD_LEVEL, abilityLevel);
			} else {
				this.raiseExceptionIllegalTypeField(FIELD_ABILITIES + JSONable.SEPARATOR_INDEX + index,
						JSONTypes.Object, abilityJSONed);
			}
			// create the Ability
			AbilityGeneric ability = gm.getGameObjectsProvider().newAbilityGeneric(abilityName, extraParameters);
			Objects.requireNonNull(ability);
			ability.setLevel(level);
			this.addAbility(ability);
		});
	}

	@Override
	public default void loadFromJSONMap(final GModality gm, Map<String, Object> jsonMap)
			throws IllegalArgumentException {
		if (jsonMap == null) {
			throw new IllegalArgumentException("Provided JSON map cannot be null");
		}
		GameObjectGeneric.super.loadFromJSONMap(gm, jsonMap);
		//
		if (!jsonMap.containsKey(FIELD_ABILITIES) /* || !(jsonMap.get(FIELD_ABILITIES) instanceof Map<?,?>) */) {
			this.raiseExceptionIllegalTypeField(FIELD_ABILITIES, JSONTypes.ArrayHomogeneousType,
					jsonMap.get(FIELD_ABILITIES));
		}
		Map<String, Object> abilitiesMap = (Map<String, Object>) jsonMap.get(FIELD_ABILITIES);
		abilitiesMap.forEach((name, abilityDataObj) -> {
			String abilityName = null;
			Map<String, Object> extraParameters = null;
			int level = 0;
			if (abilityDataObj instanceof String) {
				abilityName = (String) abilityDataObj;
			} else if (!(abilityDataObj instanceof Map<?, ?>)) {

				Map<String, Object> abilityDataMap = JSONable.newFieldValuesMap();
				if (abilityDataMap.containsKey(AbilityGeneric.FIELD_LEVEL)) { // set the level if possible (it should
																				// already be set by the
					// method above, but ... just in case)
					Object maybeLevel = abilityDataMap.get(AbilityGeneric.FIELD_LEVEL);
					if (maybeLevel instanceof Integer levelInt) {
						level = levelInt;
					}
				}
			} else {
				this.raiseExceptionIllegalTypeField(FIELD_NAME + "#(" + name + ")", JSONTypes.Object, abilityDataObj);
			}
			// create the Ability
			AbilityGeneric ability = gm.getGameObjectsProvider().newAbilityGeneric(name, extraParameters);
			Objects.requireNonNull(ability);
			ability.setLevel(level);
			this.addAbility(ability);
		});
	}

}