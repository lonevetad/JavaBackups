package tools.predicatesExpressions.expressionEvaluator;

import games.generic.controlModel.ObjectNamed;

public class AtomEvaluer implements ObjectNamed, AbstractEvaluator {
	private static final long serialVersionUID = -521514L;

	public AtomEvaluer() {
	}

	boolean value;
	String name;

	@Override
	public String getName() {
		return name;
	}

	public boolean isValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean evaluate() {
		return value;
	}
}