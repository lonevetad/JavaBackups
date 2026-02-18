package tools;

import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;

import tools.json.JSONTypes;
import tools.json.JSONValue;
import tools.json.types.JSONLong;
import tools.json.types.JSONObject;

/**
 * Marks an object as identified with an "ID", which currently (2022/04/23) is a
 * {@link Long} instance.
 */
public interface ObjectWithID extends ObjWithIDGeneric<Long> {
	public static final Comparator<ObjectWithID> COMPARATOR_OWID = (o1, o2) -> {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			return -1;
		}
		if (o2 == null) {
			return 1;
		}
		return Comparators.LONG_COMPARATOR.compare(o1.getID(), o2.getID());
	};
	public static final Function<ObjectWithID, Long> KEY_EXTRACTOR = o -> o.getID();

	//

	// TODO methods

	/**
	 * The number-based integer identifier of this instance.
	 * <p>
	 * <b>BEWARE</b> of <i>non-uniqueness</i> caused by bad management of this
	 * identifier! <br>
	 * It's advised to use some helper classes to manage its generation, for
	 * instance {@link UniqueIDProvider}.
	 */
	@Override
	public Long getID();

	//

	// JSON

	//

	@Override
	public default JSONValue newJSONValueForID() {
		return new JSONLong(this.getID());
	}

	@Override
	public default void loadFromJSONMap(Map<String, Object> jsonMap) {
		if (jsonMap == null) {
			throw new IllegalArgumentException("Provided JSON map cannot be null");
		}

		if (!jsonMap.containsKey(FIELD_ID)) {
			this.raiseExceptionMissingField(FIELD_ID, JSONTypes.Long);
		}
		if (!((jsonMap.get(FIELD_ID) instanceof Integer)
				|| (jsonMap.get(FIELD_ID) instanceof Long))) {
			this.raiseExceptionIllegalTypeField(FIELD_ID, JSONTypes.Long, jsonMap.get(FIELD_ID));
		}
		this.setID((Long) jsonMap.get(FIELD_ID));
	}

	@Override
	public default void loadFromJSONObject(JSONObject wrapper) {
		if (!wrapper.hasField(FIELD_ID)) {
			this.raiseExceptionMissingField(FIELD_ID, JSONTypes.Long);
		}
		setID(wrapper.getFieldValue(FIELD_ID).asLong());
	}
}