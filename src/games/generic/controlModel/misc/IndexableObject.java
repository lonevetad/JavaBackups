package games.generic.controlModel.misc;

import java.util.Map;

import games.generic.controlModel.GModality;
import tools.ObjectNamedID;
import tools.json.types.JSONInt;
import tools.json.types.JSONObject;

/**
 * Identify an object that is unique through its index (beware: it's NOT an
 * "ID"!) and its name. <br>
 * See {@link #getIndex()} and {@link ObjectNamedID} for more details.
 */
public interface IndexableObject extends ObjectNamedID {
	public static final String FIELD_INDEX = "index";

	/**
	 * It differs substantially by {@link #getID()}: this is an <b>index</b>, not an
	 * <i>ID</i>, and it's defined in a greater environment (for instance,
	 * {@link Enum}'s {@link Enum#ordinal()})
	 */
	public int getIndex();

	/**
	 * Returns an instance of {@link IndexToObjectBackmapping}.
	 *
	 * @return an instance of {@link IndexToObjectBackmapping}.
	 */
	public IndexToObjectBackmapping getFromIndexBackmapping();

	/**
	 * Simple function that returns an {@link IndexableObject} given the index that
	 * instance would return by calling {@link IndexableObject#getIndex().}
	 *
	 * @author ottin
	 *
	 */
	public static interface IndexToObjectBackmapping {

		/**
		 * See {@link IndexToObjectBackmapping}.
		 */
		public IndexableObject fromIndex(int index);
	}

	//

	// JSON

	//

	@Override
	public default void toJSONValue(JSONObject wrapper) {
		ObjectNamedID.super.toJSONValue(wrapper);
		JSONInt jsonedIndex = new JSONInt(this.getIndex());
		wrapper.addField(FIELD_INDEX, jsonedIndex);
	}

	@Override
	public default void loadFromJSONObject(GModality gm, JSONObject wrapper) {
		ObjectNamedID.super.loadFromJSONObject(gm, wrapper);
		// index
		/*
		 * DELEGATED TO THE CALLER DUE TO THE IMPOSSIBILITY OF SETTING THE INDEX
		 * if (!wrapper.hasField(FIELD_INDEX)) {
		 * this.raiseExceptionMissingField(FIELD_INDEX, JSONTypes.Int);
		 * }
		 * JSONValue jsonedIndex_value = wrapper.getFieldValue(FIELD_INDEX);
		 * if (!jsonedIndex_value.isType(JSONTypes.Int)) {
		 * this.raiseExceptionIllegalTypeField(FIELD_INDEX, JSONTypes.Int,
		 * jsonedIndex_value);
		 * }
		 * JSONObject jsonedIndex = (JSONObject) jsonedIndex_value;
		 */
	}

	@Override
	public default void loadFromJSONMap(GModality gm, Map<String, Object> jsonMap) {
		ObjectNamedID.super.loadFromJSONMap(gm, jsonMap);
		// TODO
		/*
		 * DELEGATED TO THE CALLER DUE TO THE IMPOSSIBILITY OF SETTING THE INDEX
		 */
	}

}