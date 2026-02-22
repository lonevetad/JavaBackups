package games.generic.controlModel.misc;

import java.util.Map;

import games.generic.controlModel.GModality;
import tools.json.JSONTypes;
import tools.json.JSONValue;
import tools.json.JSONable;
import tools.json.types.JSONInt;
import tools.json.types.JSONObject;

public class RangedAmountInt implements JSONable {
	protected static final String FIELD_MIN = "min";
	protected static final String FIELD_MAX = "max";
	protected static final String FIELD_CURRENT = "current";
	private static final long serialVersionUID = 245247402524100085L;

	protected int min, max, current;

	public RangedAmountInt(int min, int max) {
		super();
		this.min = min;
		this.max = max;
		if (min > max) {
			throw new IllegalArgumentException(
					"The minimum amount (" + min + ") is lower than the maximum (" + max + ").");
		}
		this.current = min;
	}

	public RangedAmountInt(int max) {
		this(0, max);
	}

	//

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}

	public int getCurrent() {
		return current;
	}

	//

	public void setMin(int min) {
		this.min = min;
		if (this.max < min) {
			this.setMax(min);
		}
		if (this.current < min) {
			this.setCurrent(min);
		}
	}

	public void setMax(int max) {
		this.max = max;
		if (this.min > max) {
			this.setMin(max);
		}
	}

	public void setCurrent(int current) {
		if (current < this.getMin()) {
			throw new IllegalArgumentException(
					"Given current (" + current + ") is lower than the minimum (" + this.getMin() + ")");
		}
		if (current > this.getMax()) {
			throw new IllegalArgumentException(
					"Given current (" + current + ") is greater than the maximum (" + this.getMax() + ")");
		}
		this.current = current;
	}

	//

	// JSON-related

	//

	@Override
	public void toJSONValue(JSONObject wrapper) {
		JSONInt jsonedMin = new JSONInt(this.getMin());
		JSONInt jsonedMax = new JSONInt(this.getMax());
		JSONInt jsonedCurrent = new JSONInt(this.getCurrent());
		wrapper.addField(FIELD_MIN, jsonedMin);
		wrapper.addField(FIELD_MAX, jsonedMax);
		wrapper.addField(FIELD_CURRENT, jsonedCurrent);
	}

	@Override
	public void loadFromJSONObject(GModality gm, JSONObject wrapper) {
		JSONValue maybeMin = wrapper.getFieldValue(FIELD_MIN);
		if (maybeMin.isType(JSONTypes.Int)) {
			this.setMin(((JSONInt) maybeMin).asInt());
		} else {
			this.raiseExceptionIllegalTypeField(FIELD_MIN, JSONTypes.Int, maybeMin);
		}
		JSONValue maybeMax = wrapper.getFieldValue(FIELD_MAX);
		if (maybeMax.isType(JSONTypes.Int)) {
			this.setMin(((JSONInt) maybeMax).asInt());
		} else {
			this.raiseExceptionIllegalTypeField(FIELD_MAX, JSONTypes.Int, maybeMax);
		}
		if (wrapper.hasField(FIELD_CURRENT)) {
			JSONValue maybeCurrent = wrapper.getFieldValue(FIELD_CURRENT);
			if (maybeCurrent.isType(JSONTypes.Int)) {
				this.setCurrent(((JSONInt) maybeCurrent).asInt());
			} else {
				this.raiseExceptionIllegalTypeField(FIELD_CURRENT, JSONTypes.Int, maybeCurrent);
			}
		} else {
			this.setCurrent(this.getMin());
		}
	}

	@Override
	public void loadFromJSONMap(GModality gm, Map<String, Object> jsonMap) {
		if (jsonMap == null) {
			throw new IllegalArgumentException("Provided JSON map cannot be null");
		}
		for (String fieldNameInt : new String[] { FIELD_MIN, FIELD_MAX }) {
			if (!jsonMap.containsKey(fieldNameInt)) {
				this.raiseExceptionMissingField(fieldNameInt, JSONTypes.Int);
			}
		}
		Object valMin = jsonMap.get(FIELD_MIN);
		Object valMax = jsonMap.get(FIELD_MAX);
		if (!(valMin instanceof Integer)) {
			this.raiseExceptionIllegalTypeField(FIELD_MIN, JSONTypes.Int, valMin);
		}
		if (!(valMax instanceof Integer)) {
			this.raiseExceptionIllegalTypeField(FIELD_MAX, JSONTypes.Int, valMax);
		}
		this.setMin((Integer) valMin);
		this.setMax((Integer) valMax);
		Object valCurrent = jsonMap.get(FIELD_CURRENT);
		if (jsonMap.containsKey(FIELD_CURRENT)) {
			if (!(valCurrent instanceof Integer)) {
				try {
					this.raiseExceptionIllegalTypeField(FIELD_CURRENT, JSONTypes.Int, valCurrent);
				} finally {
					this.setCurrent(this.getMin());
				}
			}
			this.setCurrent((Integer) valCurrent);
		} else {
			this.setCurrent(this.getMin());
		}
	}

}
