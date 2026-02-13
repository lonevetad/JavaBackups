package games.generic.view.guiSwing;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import games.generic.controlModel.GController;
import games.generic.controlModel.GModality;
import games.generic.view.drawers.IsomPainter;

public class ISOMViewSwing {
	public static final int GRID_SIZE_DEFAULT = 25;

	public ISOMViewSwing(GController gc) {
		if (gc == null)
			throw new IllegalArgumentException("Game controller cannot be null");
		this.gc = gc;
	}

	final GController gc;
	protected IsomPainter isomPainter;
	protected JPanel jpIsom;
	protected JScrollPane jspIsom;

	public GModality getGameModality() { return gc.getCurrentGameModality(); }

	// proxies

	public int getGridSize() {
		GModality gm;
		gm = getGameModality();
		return gm == null ? GRID_SIZE_DEFAULT : gm.getSpaceSubunitsEachMacrounits();
	}

}