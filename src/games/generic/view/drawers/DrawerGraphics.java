package games.generic.view.drawers;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import games.generic.view.Drawer;

public class DrawerGraphics implements Drawer {

	public DrawerGraphics(Graphics g) { this.graphics = g; }

	protected Graphics graphics;

	//

	public Graphics getGraphics() { return graphics; }

	public void setGraphics(Graphics graphics) { this.graphics = graphics; }

	//

	@Override
	public void drawImage(Image im, int x, int y) { graphics.drawImage(im, x, y, null); }

	@Override
	public void drawString(String str, int x, int y) { graphics.drawString(str, x, y); }

	@Override
	public void drawRectangle(Rectangle rect) { graphics.drawRect(rect.x, rect.y, rect.width, rect.height); }

	@Override
	public void drawLine(int x1, int y1, int x2, int y2) { graphics.drawLine(x1, y1, x2, y2); }

	@Override
	public void drawRectangle(Rectangle rect, int x, int y) { graphics.drawRect(x, y, rect.width, rect.height); }
}