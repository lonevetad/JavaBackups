package tests.staticAbilities;

import java.io.Serializable;

public abstract class SomethingWithIDName implements Serializable {
	private static final long serialVersionUID = -6315601965L;

	/**An identifyer letting to distinghush from different instances of the same ability (i.e. having the same name)*/
	protected Long instanceID;
	protected String name;

	public SomethingWithIDName(String name) {
		super();
		this.name = name;
	}

	// TODO GETTER

	public String getName() {
		return name;
	}

	public Long getInstanceID() {
		return instanceID;
	}

	// TODO SETTER


	public void setName(String name) {
		this.name = name;
	}
}