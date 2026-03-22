package tools.predicatesExpressions.filters;

import tools.predicatesExpressions.MyPredicate;

public abstract class TwoPredicatesJoined<E> implements MyPredicate<E> {
	private static final long serialVersionUID = 88878519002025L;

	protected TwoPredicatesJoined() {
		super();
	}

	public TwoPredicatesJoined(MyPredicate<E> f1, MyPredicate<E> f2) {
		this();
		if (f1 == null || f2 == null)
			throw new IllegalArgumentException("Some filter null");
		setFilter1(f1);
		setFilter2(f2);
	}

	protected MyPredicate<E> filter1, filter2;

	//

	// TODO GETTER

	public MyPredicate<E> getFilter1() {
		return filter1;
	}

	public MyPredicate<E> getFilter2() {
		return filter2;
	}

	//

	// TODO SETTER

	public TwoPredicatesJoined<E> setFilter1(MyPredicate<E> f1) {
		if (f1 != null && f1 != this && f1 != filter2)
			this.filter1 = f1;
		return this;
	}

	public TwoPredicatesJoined<E> setFilter2(MyPredicate<E> f2) {
		if (f2 != null && f2 != this && filter1 != f2)
			this.filter2 = f2;
		return this;
	}
}