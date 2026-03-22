package games.generic.view.drawers;

import java.awt.Point;

import dataStructures.isom.InSpaceObjectsManager;
import games.generic.controlModel.GModality;
import games.generic.controlModel.GObjectsInSpaceManager;
import games.generic.controlModel.misc.GThread;
import games.generic.controlModel.subimpl.GModalityET;
import games.generic.view.DrawableObj;
import games.generic.view.Drawer;
import games.generic.view.GameView;
import games.generic.view.dataProviders.DrawableObjProvider;
import games.generic.view.guiSwing.ISOMViewSwing;
import games.generic.view.impl.GuiComponent;
import geometry.implementations.shapes.ShapeRectangle;

/**
 * A class that paints a {@link InSpaceObjectsManager}. <br>
 * Runs asynchronously and is used in {@link ISOMViewSwing}.
 */
public abstract class IsomPainter extends GuiComponent {

	public IsomPainter(GameView view, DrawableObjProvider drawableProvider) {
		super(view);
		this.drawableProvider = drawableProvider;
		cameraView = new ShapeRectangle();
//		objectPainted = null;
		iterationPainting = 1;
	}

	/** Works as a tick-counter. */
	protected long iterationPainting;
	protected GModalityET gModality;
	protected GThread threadPainter;
	protected ShapeRectangle cameraView;
//	protected Rectangle cameraView;
//	protected Map<Integer, ObjectInSpace> objectPainted;
	protected Drawer drawer;
	protected final DrawableObjProvider drawableProvider;

	public ShapeRectangle getCameraView() { return cameraView; }

	public Drawer getDrawer() { return drawer; }

	//

	public void setDrawer(Drawer drawer) { this.drawer = drawer; }

	/**
	 * Upon moving the main character, or just to see another part of the map, just
	 * move the camera position to a position (the given point is the top-left
	 * corner).
	 */
	public void setCameraPosition(int x, int y) {
		int w, h;
		w = this.cameraView.getWidth();
		h = this.cameraView.getHeight();
		this.cameraView.setCenter(x + (w >> 1), y + (h >> 1));
	}

	public void setCameraViewDimension(int width, int height) {
		int x, y;
		int w, h;
		ShapeRectangle sr;
		sr = this.cameraView;
		w = sr.getWidth();
		h = sr.getHeight();
		x = sr.getXCenter();
		y = sr.getYCenter();
		sr.setWidth(width);
		sr.setHeight(height);
		sr.setCenter(x + ((width - w) >> 1), y + ((height - h) >> 1));
	}

	//

	//

	/** Perform an action upon adding this component on the given view. */
	@Override
	public void onAddingOnView(GameView view) {
		GModality gm;
		GModalityET gmet;
		gm = getGameModality();
		if (!(gm instanceof GModalityET))
			return;
		gmet = (GModalityET) gm;
		if (threadPainter != null) { gmet.removeGameThread(threadPainter); }
		threadPainter = gmet.addGameThreadSimplyStoppable(this::paintCycleIteration);
		// other stuffs
//		if (objectPainted == null) {
//			objectPainted = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.INTEGER_COMPARATOR);
//		}
	}

	/** The reverse of {@link #onAddingOnView(GameView)}. */
	@Override
	public void onRemovingOnView(GameView view) {
		GModality gm;
		GModalityET gmet;
		gm = getGameModality();
		if (!(gm instanceof GModalityET))
			return;
		gmet = (GModalityET) gm;
		if (threadPainter != null) {
			gmet.removeGameThread(threadPainter);
			threadPainter = null;
		}
		// other stuffs
//		this.objectPainted = null;
	}

	/**
	 * Should uses {@link #getDrawer()} to paint this single object, which is
	 * located in the provided center's {@link Point}.
	 */
	protected void paintObject(Point locationCenterToPaint, DrawableObj d) {
		drawer.drawDrawable(d, locationCenterToPaint.x - this.cameraView.getXCenter(),
				locationCenterToPaint.y - this.cameraView.getYCenter());
	}

	/** Single painting iteration */
	protected void paintCycleIteration() {
//		final Map<Integer, ObjectInSpace> m;
		GModality gm;
		GObjectsInSpaceManager goism;
////	BiConsumer<Point, DrawableObj> oisPainter;
		if (this.drawableProvider == null)
			return;
		gm = this.getGameModality();
		if (gm == null)
			return;
		goism = gm.getGObjectInSpaceManager();
		// check if it's currently painting
		if ((goism == null) || (iterationPainting < 0)) // currently in painting
			return;
		// increase the tick
		if (++iterationPainting < 0) { // overflow
			iterationPainting = -1;
		} else {
			iterationPainting = -iterationPainting;
		}
		// actual paint

		// oisPainter = (p, d) -> paintObject(p, d);
		drawableProvider.forEachDrawableInArea(this.cameraView, this::paintObject);

		// painting process ends, mark it
		iterationPainting = -iterationPainting;
	}

//	/** The WHOLE cycle of painting, not just a single iteration! */
//	public void paintCycle() {	}
}