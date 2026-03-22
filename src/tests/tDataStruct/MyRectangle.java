package tests.tDataStruct;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import stuffs.logic.AtomLogicProposition;
import tools.ObjectNamedID;

public class MyRectangle extends Rectangle implements ObjectNamedID {
	private static final long serialVersionUID = 1L;
	private static int idProg = 0;
	protected final Integer ID;
	protected String name = "";

	public MyRectangle(int x, int y, int width, int height) { super(x, y, width, height); }

	public MyRectangle(Point p, Dimension d) { super(p, d); }

	{ // initializer
		ID = idProg++;
	}

	@Override
	public Integer getID() { return ID; }

	@Override
	public String getName() { return name; }

	public MyRectangle setName(String name) {
		this.name = name;
		return this;
	}

	public MyRectangle setName(int nameID) {
		this.name = (new AtomLogicProposition(nameID >= 0, Math.abs(nameID))).getName();
		return this;
	}

	@Override
	public String toString() {
		return "MyR[n: " + name + ", ID=" + ID + ", x=" + x + ", y=" + y + ", width=" + width + ", height=" + height
				+ "]";
	}
}