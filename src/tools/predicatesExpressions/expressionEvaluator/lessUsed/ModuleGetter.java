package tools.predicatesExpressions.expressionEvaluator.lessUsed;

import java.math.BigDecimal;
import java.math.BigInteger;

import tools.predicatesExpressions.expressionEvaluator.sourceEvaluer.NumberDelegating;

public class ModuleGetter<E extends Number> extends NumberDelegating<E> {
	private static final long serialVersionUID = -62308328710388L;

	public ModuleGetter(E number, E divisor) {
		this.number = this.divisor = null;
		setNumberDivisor(number, divisor);
	}

	protected E number, divisor, module; // module act as a cache

	public E getNumber() {
		return number;
	}

	public E getDivisor() {
		return divisor;
	}

	public E getModule() {
		return module;
	}

	@Override
	public E getDelegator() {
		return module;
	}

	@Override
	public NumberDelegating<E> setDelegator(E delegator) {
		throw new UnsupportedOperationException("Cannot set the module");
	}

	//

	@SuppressWarnings("unchecked")
	public ModuleGetter<E> setNumberDivisor(E number, E divisor) {
		boolean notChanged;
		Class<? extends Number> cn;
		E oldDivisor;
		if (number == null)
			throw new IllegalArgumentException("Number must be not null");
		if (divisor == null)
			throw new IllegalArgumentException("Divisormust be not null");

		if ((cn = number.getClass()) != divisor.getClass())
			throw new IllegalArgumentException("Number and divisor must have the same class");
		notChanged = true;
		if (this.number != number) {
			notChanged = false;
			this.number = number;
		}
		oldDivisor = this.divisor;
		if (this.divisor != divisor) {
			notChanged = false;
			this.divisor = divisor;
		}
		if (notChanged)
			return this;
		if (cn.getName() == Integer.class.getName()) {
			if (((Integer) divisor) == 0) {
				this.divisor = oldDivisor;
				throw new IllegalArgumentException("The divisor cannot be 0");
			}
			this.module = (E) ((Integer) ((((Integer) number) % ((Integer) divisor))));
		} else if (cn.getName() == Double.class.getName()) {
			if (((Double) divisor) == 0) {
				this.divisor = oldDivisor;
				throw new IllegalArgumentException("The divisor cannot be 0");
			}
			this.module = (E) ((Double) ((((Double) number) % ((Double) divisor))));
		} else if (cn.getName() == Long.class.getName()) {
			if (((Long) divisor) == 0) {
				this.divisor = oldDivisor;
				throw new IllegalArgumentException("The divisor cannot be 0");
			}
			this.module = (E) ((Long) ((((Long) number) % ((Long) divisor))));
		} else if (cn.getName() == Byte.class.getName()) {
			if (((Byte) divisor) == 0) {
				this.divisor = oldDivisor;
				throw new IllegalArgumentException("The divisor cannot be 0");
			}
			this.module = (E) ((Byte) ((byte) ((((Byte) number) % ((Byte) divisor)))));
		} else if (cn.getName() == BigInteger.class.getName()) {
			if (((BigInteger) divisor).compareTo(BigInteger.ZERO) == 0) {
				this.divisor = oldDivisor;
				throw new IllegalArgumentException("The divisor cannot be 0");
			}
			this.module = (E) ((BigInteger) number).remainder((BigInteger) divisor);
		} else if (cn.getName() == BigDecimal.class.getName()) {
			if (((BigDecimal) divisor).compareTo(BigDecimal.ZERO) == 0) {
				this.divisor = oldDivisor;
				throw new IllegalArgumentException("The divisor cannot be 0");
			}
			this.module = (E) ((BigDecimal) number).remainder((BigDecimal) divisor);
		} else if (cn.getName() == Float.class.getName()) {
			if (((Float) divisor) == 0) {
				this.divisor = oldDivisor;
				throw new IllegalArgumentException("The divisor cannot be 0");
			}
			this.module = (E) ((Float) ((((Float) number) % ((Float) divisor))));
		} else if (cn.getName() == Short.class.getName()) {
			if (((Short) divisor) == 0) {
				this.divisor = oldDivisor;
				throw new IllegalArgumentException("The divisor cannot be 0");
			}
			this.module = (E) ((Integer) ((((Integer) number) % ((Integer) divisor))));
		} else
			throw new IllegalArgumentException("Unexpected value: " + cn);
		return this;
	}

	public ModuleGetter<E> setNumber(E number) {
		return this.setNumberDivisor(number, this.divisor);
	}

	public ModuleGetter<E> setDivisor(E divisor) {
		return this.setNumberDivisor(this.number, divisor);
	}

}