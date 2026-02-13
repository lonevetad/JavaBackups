package common.abstractCommon.behaviouralObjectsAC;

import java.util.LinkedList;
import java.util.List;

public interface MyObservable {
	public List<MyObserver> getMyObservers();

	public default void addObserver(MyObserver o) {
		List<MyObserver> obs;
		obs = getMyObservers();
		if (o == null) throw new NullPointerException();
		if (!obs.contains(o)) {
			obs.add(o);
		}
	}

	/**
	 * Deletes an observer from the set of observers of this object. Passing <CODE>null</CODE> to
	 * this method will have no effect.
	 *
	 * @param o
	 *            the observer to be deleted.
	 */
	public default void deleteObserver(MyObserver o) {
		List<MyObserver> obs;
		if (o != null) {
			obs = getMyObservers();
			if (obs != null) obs.remove(o);
		}
	}

	public default void notifyObservers() {
		notifyObservers(null);
	}

	public default void notifyObservers(Object arg) {
		List<MyObserver> obs;
		obs = getMyObservers();
		/*
		 * a temporary array buffer, used as a snapshot of the state of current Observers.
		 */

		for (MyObserver mo : obs)
			mo.update(this, arg);
	}

	public default void deleteObservers() {
		getMyObservers().clear();
	}

	public default int countObservers() {
		List<MyObserver> obs;
		obs = getMyObservers();
		return obs == null ? -1 : obs.size();
	}

	//

	//

	public static MyObservable newDefaultInstance() {
		return new MyObservableDEFAULT();
	}

	public static class MyObservableDEFAULT implements MyObservable {
		List<MyObserver> myObservers;

		MyObservableDEFAULT() {
			super();
			myObservers = new LinkedList<>();
		}

		@Override
		public List<MyObserver> getMyObservers() {
			return myObservers;
		}
	}
}