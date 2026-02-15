package games.generic.controlModel.misc;

import tools.Stringable;

public class RangedAmountInt implements Stringable {
	private static final long serialVersionUID = 245247402524100085L;

	protected int min, max, current;
	
	public RangedAmountInt(int min, int max) {
		super();
		this.min = min;
		this.max = max;
		if(min > max) {
			throw new IllegalArgumentException("The minimum amount (" + min + ") is lower than the maximum (" + max + ").");
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
		if(this.max < min) {
			this.setMax(min);
		}
		if(this.current < min) {
			this.setCurrent(min);
		}
	}

	public void setMax(int max) {
		this.max = max;
		if(this.min > max) {
			this.setMin(max);
		}
	}

	public void setCurrent(int current) {
		if(current < this.getMin()) {
			throw new IllegalArgumentException("Given current (" + current + ") is lower than the minimum (" + this.getMin() + ")");
		}
		if(current > this.getMax()) {
			throw new IllegalArgumentException("Given current (" + current + ") is greater than the maximum (" + this.getMax() + ")");
		}
		this.current = current;
	}	
}
