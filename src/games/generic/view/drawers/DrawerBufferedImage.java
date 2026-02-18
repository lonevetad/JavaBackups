package games.generic.view.drawers;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import games.generic.view.Drawer;

public class DrawerBufferedImage implements Drawer {

	public DrawerBufferedImage(BufferedImage backImage) { this.setBackImage(backImage); }

	protected BufferedImage backImage;
	protected DrawerGraphics dg; // delegator

	//

	public BufferedImage getBackImage() { return backImage; }

	public void setBackImage(BufferedImage backImage) {
		this.backImage = backImage;
		if (backImage == null) {
			this.dg = null;
		} else {
			this.dg = new DrawerGraphics(backImage.createGraphics());
		}
	}

	//

	@Override
	public void drawImage(Image im, int x, int y) {
		if (dg != null)
			dg.drawImage(im, x, y);
	}

	@Override
	public void drawString(String str, int x, int y) {
		if (dg != null)
			dg.drawString(str, x, y);
	}

	@Override
	public void drawRectangle(Rectangle rect) {
		if (dg != null)
			dg.drawRectangle(rect);
	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {
		if (dg != null)
			dg.drawLine(x1, y1, x2, y2);
	}

	@Override
	public void drawRectangle(Rectangle rect, int x, int y) {
		if (dg != null)
			dg.drawRectangle(rect, x, y);
	}
}