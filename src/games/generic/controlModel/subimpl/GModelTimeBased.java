package games.generic.controlModel.subimpl;

import java.util.function.Consumer;

import games.generic.controlModel.GModel;
import games.generic.controlModel.holders.TimedObjectHolder;
import games.generic.controlModel.objects.TimedObject;

public abstract class GModelTimeBased extends GModel {
	public static final String TIMED_OBJECT_HOLDER_NAME = "toh";

	public GModelTimeBased() {
		super();
		this.timedObjectHolder = newTimedObjectHolder();
		super.addObjHolder(TIMED_OBJECT_HOLDER_NAME, this.timedObjectHolder);
	}

	protected TimedObjectHolder timedObjectHolder;

	//

	/** Override designed */
	public TimedObjectHolder newTimedObjectHolder() {
		return new TimedObjectHolder_Impl1();
	}

	//

	//

	public void addTimedObject(TimedObject to) {
		super.add(to);
	}

//	public Set<TimedObject> getTimedObjects() { return timedObjectHolder.getObjects()  }

	public void forEachTimedObject(Consumer<TimedObject> action) {
		this.timedObjectHolder.getObjects().forEach(owidButItsTO -> {
			action.accept((TimedObject) owidButItsTO);
		});
	}
}