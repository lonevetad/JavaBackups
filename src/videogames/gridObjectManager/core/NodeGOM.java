package videogames.gridObjectManager.core;

import java.io.Serializable;

import tools.ObjectWithID;

public class NodeGOM implements Serializable {
	private static final long serialVersionUID = 1618033989L;

	protected int x, y;
	protected ObjectWithID element;

	public NodeGOM() {
	}

	//

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public ObjectWithID getElement() {
		return element;
	}

	public NodeGOM setX(int x) {
		this.x = x;
		return this;
	}

	public NodeGOM setY(int y) {
		this.y = y;
		return this;
	}

	public NodeGOM setElement(ObjectWithID element) {
		this.element = element;
		return this;
	}
}