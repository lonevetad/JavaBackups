package tools.predicatesExpressions.filters;

import tools.predicatesExpressions.MyPredicate;

public class NotPredicate<E> implements MyPredicate<E> {
	private static final long serialVersionUID = 95490155050053L;

	public NotPredicate() {
	}

	public NotPredicate(MyPredicate<E> filter) {
		this();
		if (filter == null)
			throw new IllegalArgumentException("Filter null");
		this.setFilter(filter);
	}

	protected MyPredicate<E> filter;

	public NotPredicate<E> setFilter(MyPredicate<E> f1) {
		if (f1 != null && f1 != this)
			this.filter = f1;
		return this;
	}

	@Override
	public boolean test(E ao) {
		if (ao == null // || filter == null
		)
			return false;
		return !filter.test(ao);
	}
}