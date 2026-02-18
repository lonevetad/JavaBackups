package tools.json;

public enum JSONTypes {
	Int, Long, Double, String, Boolean,
	/**
	 * A.k.a.: a Map
	 */
	Object, ArrayMiscTypes("Array"), ArrayHomogeneousType("array");

	private String typeName = null;

	private JSONTypes(String tn) {
		this.typeName = tn;
	}

	private JSONTypes() {
		this(null);
	}

	public String getTypeName() {
		return this.typeName != null ? this.typeName : this.name();
	}
}
