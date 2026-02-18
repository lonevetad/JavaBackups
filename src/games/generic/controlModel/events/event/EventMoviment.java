package games.generic.controlModel.events.event;

import java.awt.Point;
import java.util.ArrayList;

import games.generic.controlModel.events.GEvent;
import geometry.ObjectLocated;

public class EventMoviment extends GEvent {
	private static final long serialVersionUID = 369578984148920210L;

	private EventMoviment() {}

	protected ObjectLocated objectMoved;
	protected Point from, to;

	//

//	public MovingObject getMovingObject() { return object; }

	public Point getFrom() { return from; }

	public ObjectLocated getObjectMoved() { return objectMoved; }

	public Point getTo() { return to; }

	//

	@Override
	public void onProcessingEnded() { pool.add(this); }

	//

	//

	// TODO STATIC
	public static EventMoviment newEventMoviment(ObjectLocated movingObject, Point from, Point to) {
		EventMoviment em;
		em = pool.isEmpty() ? new EventMoviment() : pool.remove(pool.size() - 1);
		em.objectMoved = movingObject;
		em.from = from;
		em.to = to;
		return em;
	}

	protected static ArrayList<EventMoviment> pool = new ArrayList<>(16);

}