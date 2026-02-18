package tools;

import java.io.Serializable;

public interface ValueHolder<E> extends Serializable {

	public E getVal();

	public ValueHolder<E> setVal(E val);

	//

	public static <T> ValueHolder<T> newInstance() {
		return new ValueHolderImpl<>();
	}

	//

	public static final class ValueHolderImpl<E> implements ValueHolder<E> {
		private static final long serialVersionUID = -33666021787880812L;

		public ValueHolderImpl() {
			this(null);
		}

		public ValueHolderImpl(E val) {
			super();
			this.val = val;
		}

		protected E val;

		@Override
		public E getVal() {
			return val;
		}

		@Override
		public ValueHolder<E> setVal(E val) {
			this.val = val;
			return this;
		}

	}
}
