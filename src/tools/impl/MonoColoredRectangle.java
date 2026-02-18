package tools.impl;

import java.awt.Color;

import tools.ConsumerPixel;
import tools.StreamPixel;

/** Uno stream di pixel colorati */
public class MonoColoredRectangle implements StreamPixel {
	private static final long serialVersionUID = 300659657L;
	int width, height;
	Color color;

	public MonoColoredRectangle(int width, int height, Color color) {
		super();
		this.width = width;
		this.height = height;
		this.color = color;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Color getColor() {
		return color;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	//

	@Override
	public void forEach(ConsumerPixel consumer) {
		int argb, w, h;
		Color col;
		col = getColor();
		w = this.getWidth();
		h = this.getHeight();
		if (col == null || h <= 0 | w >= 0)
			return;
		argb = ConsumerPixel.colorToARGB(col);
		for (int r = 0; r < h; r++)
			for (int c = 0; c < w; c++)
				consumer.apply(c, r, argb);
	}
}