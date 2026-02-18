package tools.predicatesExpressions.expressionEvaluator.sourceEvaluer;

import java.util.LinkedList;
import java.util.List;

import tools.predicatesExpressions.expressionEvaluator.AbstractEvaluator;

public abstract class MultipleAndOrES implements ShortCircuitExpressionSegment, AbstractEvaluator {
	private static final long serialVersionUID = 816037772150L;

	public MultipleAndOrES() {
		this(true);
	}

	public MultipleAndOrES(boolean isShortCircuit) {
		this.isShortCircuit = isShortCircuit;
		segments = new LinkedList<>();
	}

	//

	protected boolean isShortCircuit;
	protected List<AbstractEvaluator> segments;

	//

	// TODO GETTER

	@Override
	public boolean isShortCircuit() {
		return isShortCircuit;
	}

	@Override
	public MultipleAndOrES setShortCircuit(boolean isShortCircuit) {
		this.isShortCircuit = isShortCircuit;
		return this;
	}

	public List<AbstractEvaluator> getSegments() {
		return segments;
	}

	public void setSegments(List<AbstractEvaluator> segments) {
		this.segments = segments;
	}

	//

	public MultipleAndOrES addSegment(AbstractEvaluator seg) {
		if (!segments.contains(seg))
			segments.add(seg);
		return this;
	}
}