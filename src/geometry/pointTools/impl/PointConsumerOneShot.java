package geometry.pointTools.impl;

import java.awt.Point;

public abstract class PointConsumerOneShot implements PointConsumerRestartable {
	private static final long serialVersionUID = -7885623078512L;
	public static final PointConsumerOneShot POINT_PRINTER_ONE_SHOT = new PointConsumerOneShot() {
		private static final long serialVersionUID = 1354992122480L;

		@Override
		public void acceptImpl(Point p) {
			System.out.println(p);
		}
	};

	//

	protected PointConsumerOneShot() {
		this.neverStarted = true; // do not call "restart", force the assignement
	}

	protected boolean neverStarted;

	///

	@Override
	public void restart() {
		this.neverStarted = true;
	}

	@Override
	public boolean canContinue() {
		return neverStarted;
	}

	@Override
	public final void accept(Point p) {
		if (neverStarted) {
			this.neverStarted = false;
			acceptImpl(p);
		}
	}

	public abstract void acceptImpl(Point p);
}