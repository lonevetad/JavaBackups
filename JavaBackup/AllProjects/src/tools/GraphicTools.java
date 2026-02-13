package tools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

public class GraphicTools {

	private GraphicTools() {
	}

	/**
	 * BEWARE : NO CHECK PERFORMED TO THE PARAMETERS' CORRECTNESS !
	 *
	 * @param g          A {@link Graphics} instance. No null-check
	 * @param w          the width of the rectangle bounding the grid, usually
	 *                   graphic component's width associated with "g".
	 * @param h          the height of the rectangle bounding the grid, usually the
	 *                   graphic component's height associated with "g".
	 * @param sizeSquare the size of the grid's square.
	 */
	public static void paintGrid(Graphics g, int w, int h, int sizeSquare) {
		paintGrid(g, w, h, sizeSquare, true);
	}

	/** See {@link #paintGrid(Graphics, int, int, int)}. */
	public static void paintGrid(Graphics g, int x, int y, int w, int h, int sizeSquare) {
		paintGrid(g, x, y, w, h, sizeSquare, true);
	}

	public static void paintGrid(Graphics g, int w, int h, int sizeSquare, boolean performChecks) {
		paintGrid(g, 0, 0, w, h, sizeSquare, performChecks);
	}

	/** See {@link #paintGrid(Graphics, int, int, int)}. */
	public static void paintGrid(Graphics g, int x, int y, int w, int h, int sizeSquare, boolean performChecks) {
		int r, c, w_1, h_1;
		if (performChecks) {
			if (g == null)
				return;
			if (w < 1 || h < 1 || sizeSquare < 0)
				throw new IllegalArgumentException("ERROR: on paintGrid, Invalid parameters:\n\twidth: " + w
						+ ", height: " + h + ", sizeSquare: " + sizeSquare);
			if (x < 0) {
				w += x;
				x = 0;
			}
			if (y < 0) {
				h += y;
				y = 0;
			}
		}
		h_1 = y + (h - 1);
		// c = (x != 0) ? (x - (x % sizeSquare)) : 0;
		c = 0;
		while((c += sizeSquare) < w) {
			w_1 = x + c;// recycle
			g.drawLine(w_1, y, w_1, h_1);
		}

		w_1 = x + (w - 1);
		// r = (y != 0) ? (y - (y % sizeSquare)) : 0;
		r = 0;
		while((r += sizeSquare) < h) {
			h_1 = y + r;// recycle
			g.drawLine(x, h_1, w_1, h_1);
		}
	}

	/** See {@link #} */
	public static void paintGrid(BufferedImage g, Color c, int w, int h, int sizeSquare) {
		paintGrid(g, c, w, h, sizeSquare, true);
	}

	/** See {@link #paintGrid(Graphics, int, int, int)}. */
	public static void paintGrid(BufferedImage g, Color c, int x, int y, int w, int h, int sizeSquare) {
		paintGrid(g, c, x, y, w, h, sizeSquare, true);
	}

	public static void paintGrid(BufferedImage g, Color c, int w, int h, int sizeSquare, boolean performChecks) {
		paintGrid(g, c, 0, 0, w, h, sizeSquare, performChecks);
	}

	/** See {@link #paintGrid(Graphics, int, int, int)}. */
	public static void paintGrid(BufferedImage g, Color color, int x, int y, int w, int h, int sizeSquare,
			boolean performChecks) {
		int r, c, w_1, h_1, rgb;
		if (performChecks) {
			if (g == null)
				return;
			if (w < 1 || h < 1 || sizeSquare < 0)
				throw new IllegalArgumentException("ERROR: on paintGrid, Invalid parameters:\n\twidth: " + w
						+ ", height: " + h + ", sizeSquare: " + sizeSquare);
			if (x < 0) {
				w += x;
				x = 0;
			}
			if (y < 0) {
				h += y;
				y = 0;
			}
		}
		h_1 = y + (h - 1);
		// c = (x != 0) ? (x - (x % sizeSquare)) : 0;
		rgb = color.getRGB();
		c = 0;
		while((c += sizeSquare) < w) {
			w_1 = x + c;// recycle
//			g.drawLine(w_1, y, w_1, h_1);
			drawLine(g, rgb, w_1, y, false, h);
		}

		w_1 = x + (w - 1);
		// r = (y != 0) ? (y - (y % sizeSquare)) : 0;
		r = 0;
		while((r += sizeSquare) < h) {
			h_1 = y + r;// recycle
//			g.drawLine(x, h_1, w_1, h_1);
			drawLine(g, rgb, x, h_1, true, w);
		}
	}

	static final void drawLine(BufferedImage bi, int rgb, int x, int y, boolean isHorizontal, int length) {
		if (isHorizontal) {
			while(--length >= 0)
				bi.setRGB(x + length, y, rgb);
		} else {
			while(--length >= 0)
				bi.setRGB(x, y + length, rgb);
		}
	}

	@Deprecated
	public static BufferedImage rotate(BufferedImage image, double angle) {
		double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
		int w = image.getWidth(), h = image.getHeight();
		int neww = (int) Math.floor(w * cos + h * sin), newh = (int) Math.floor(h * cos + w * sin);
		GraphicsConfiguration gc = getDefaultConfiguration();
		BufferedImage result = gc.createCompatibleImage(neww, newh, Transparency.TRANSLUCENT);
		Graphics2D g = result.createGraphics();
		g.translate((neww - w) / 2, (newh - h) / 2);
		g.rotate(angle, w / 2, h / 2);
		g.drawRenderedImage(image, null);
		g.dispose();
		return result;
	}

	@Deprecated
	private static GraphicsConfiguration getDefaultConfiguration() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		return gd.getDefaultConfiguration();
	}

	/*
	 * @Deprecated static void paintRoeated_ChangeImageSize(Graphics g,
	 * AbstractObjectBoundingBoxedRealPixel obbrp) {
	 * g.drawImage(GraphicTools.rotate(obbrp.getImage(),
	 * Math.toRadians(obbrp.getAngleDeg())), obbrp.getXLeftBottomRealPixel(),
	 * obbrp.getYLeftBottomRealPixel(), obbrp.getWidthRealPixel(),
	 * obbrp.getHeightRealPixel(), null); }
	 */

	/*
	 * public static void paintRoeated(Graphics g,
	 * AbstractObjectBoundingBoxedRealPixel obbrp, BufferedImage image) { int x, y;
	 * Graphics2D g2d;
	 *
	 * // image = obbrp.getImageOriginal(); if (image != null) { g2d = (Graphics2D)
	 * g.create();
	 *
	 * g2d.setColor(Color.BLACK);
	 *
	 * // x = (getWidth() - image.getWidth()) / 2; // y = (getHeight() -
	 * image.getHeight()) / 2;
	 *
	 * x = obbrp.getXLeftBottomRealPixel(); y = obbrp.getYLeftBottomRealPixel();
	 * AffineTransform at = new AffineTransform();
	 * at.setToRotation(Math.toRadians(obbrp.getAngleDeg()), x + (image.getWidth()
	 * >> 1), y + (image.getHeight() >> 1)); at.translate(x, y);
	 * g2d.setTransform(at); g2d.drawImage(image, 0, 0, null); g2d.dispose(); } }
	 */

}