package tools;

import java.util.List;

public interface ObservableSimple<T> {

	public List<ObserverSimple<T>> getObservers();

	public default void addObserver(ObserverSimple<T> o) {
		List<ObserverSimple<T>> os;
		if (o == null)
			return;
		os = this.getObservers();
		if (os == null || os.contains(o))
			return;
		os.add(o);
	}

	public default void removeObserver(ObserverSimple<T> o) {
		List<ObserverSimple<T>> os;
		if (o == null)
			return;
		os = this.getObservers();
		if (os == null)
			return;
		os.remove(o);
	}

	public default void notifyObservers(T something) {
		List<ObserverSimple<T>> os;
		os = this.getObservers();
		if (os == null || os.isEmpty())
			return;
		os.forEach(observer -> observer.notify(something));
	}
}