package srl;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

//import dataStructures.MapTreeAVL;
import tools.Comparators;

public class SRLRegistersCollection implements Serializable, Cloneable {
	private static final long serialVersionUID = 89561514L;

	public SRLRegistersCollection() {
		this.registers = // MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
				new TreeMap<>(//
						Comparators.STRING_COMPARATOR);
	}

	protected Map<String, Register> registers;

	public boolean containsRegister(String name) { return this.registers.containsKey(name); }

	public void addRegister(String name) {
		if (!this.containsRegister(name)) { this.registers.put(name, new Register(name)); }
	}

	public Register getRegister(String name) { return this.registers.get(name); }

	public long getRegisterValue(String name) { return this.getRegister(name).value; }

	/** Should NOT be used, unless in tests. */
	public void setRegisterValue(String name, long v) {
		this.getRegister(name).value = v;
	}

	public void incrementRegister(String name) { this.getRegister(name).value++; }

	public void decrementRegister(String name) { this.getRegister(name).value--; }

	public void forEachRegister(Consumer<Register> action) { this.registers.forEach((n, r) -> action.accept(r)); }

	@Override
	public SRLRegistersCollection clone() {
		final SRLRegistersCollection r;
		r = new SRLRegistersCollection();
		this.registers.forEach((n, reg) -> r.registers.put(reg.name, reg.clone()));
		return r;
	}
//		public SRLCodeStatement clone();

	/**
	 * Disruptive operation: set ALL registers to zero. Should NOT be used, unless
	 * in tests.
	 */
	public void resetRegisters() {
//		this.forEachRegister(r -> r.value = 0);
		this.forEachRegister(Register::toZero);
	}

	@Override
	public String toString() {
		StringBuilder sb;
		String prefix;
		prefix = "SRLRegistersCollection [registers=\n";
		sb = new StringBuilder(prefix.length() + this.registers.size() * 23);
		forEachRegister(sb::append);
		return sb.append(']').toString();
	}

	//

	//

	public static class Register implements Serializable, Cloneable {
		private static final long serialVersionUID = -2015410865408L;
		public long value;
		public final String name;

		public Register(String name) {
			super();
			this.name = name;
			toZero();
		}

		protected Register(String name, long value) {
			this.name = name;
			this.value = value;
		}

		public void toZero() { this.value = 0; }

		@Override
		public String toString() { return "Register[" + name + " -> " + value + "]"; }

		@Override
		public Register clone() { return new Register(name, value); }
	}
}