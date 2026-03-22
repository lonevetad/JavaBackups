package tools;

import java.awt.Color;

public interface ConsumerPixel {
	/**
	 * Consume a point <code>(x; y)</code> (the first two parameters) and the
	 * <i>argb</i> value.
	 */
	public void apply(int x, int y, int argb);

	public static int colorToARGB(Color c) {
		return (c.getAlpha() << 24) + c.getRGB();
	}
}