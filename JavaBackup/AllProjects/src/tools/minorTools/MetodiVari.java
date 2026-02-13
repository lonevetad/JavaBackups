package tools.minorTools;

public class MetodiVari {

	public static int sqrtUsingLog2(int n) {
		int r, t;

		r = 0;
		if (n < 0) {
			r = -1;
		} else if (n > 0) {
			r = log2(n) - 1;
			while ((t = (++r * r)) < n)
				;
			if (t != n) {
				// approssimazione per difetto
				r--;
			}
		}
		return r;
	}

	public static int sqrtNormal(int n) {
		int r, t;

		r = 0;
		if (n < 0) {
			r = -1;
		} else if (n > 0) {
			while ((t = (++r * r)) < n)
				;
			if (t != n) {
				// approssimazione per difetto
				r--;
			}
		}
		return r;
	}

	public static boolean isParity(int x) {
		if (x < 0) {
			x = -x;
		}
		return (x & 0x1) == 0;
	}

	/**
	 * Calcola il logaritmo intero in base 2 di <code>x</code>.<br>
	 * Arrotonda per difetto.<br>
	 * La complessita' sarebbe pari a O( log<inf>2</inf>(<code>x</code>) ), ma
	 * <code>x</code> e' limitato a {@link Integer#MAX_VALUE}, ossia
	 * <code> 2<sup>31</sup>-1</code>, quindi sarebbe O(32) [32 per stare larghi,
	 * sarebbe sufficiente 31], ossia O(1).<br>
	 * All'atto pratico, conviene considerare questa funzione come richiedene 32
	 * passi, per stare larghi.
	 *
	 * @return <code>-1</code> if <code>x</code> is negative,
	 *         <code>floor(log2(x))</code> otherwise.
	 */
	public static int log2(int x) {
		int i;
		i = -1;
		if (x > 0) {
			do {
				i++;
			} while ((x >>= 1) > 0);
		}
		return i;
	}

	/**
	 * Ritorna il numero minimo di bit necessari per rappresentare x.<br>
	 * La complessita' e' quella di {@link #log2(int)}.
	 */
	public static int bitMinNeeded(int x) {
		int n/* , log */;
		if (x < 0) {
			n = -1;
		} else {
			/*
			 * log = log2(x); n = (x == (1 << log)) ? log : log + 1;
			 */
			n = log2(x) + 1;
		}
		return n;
	}

}
