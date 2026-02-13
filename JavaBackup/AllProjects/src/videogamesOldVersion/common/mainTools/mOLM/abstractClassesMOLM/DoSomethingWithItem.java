package common.mainTools.mOLM.abstractClassesMOLM;

import java.io.Serializable;

public interface DoSomethingWithItem extends Serializable {

	/**
	 * @param item
	 *            the instance of {@link ObjectWithID} to be elaborated.
	 *
	 * @param x
	 *            the x-coordinates of <code>item</code> instance in
	 *            <code>molm</code>'s matrix.
	 * @param y
	 *            like x.
	 */
	public Object doOnItem(AbstractMatrixObjectLocationManager molm, ObjectWithID item, int x, int y);

	public default boolean canContinueCycling() {
		return true;
	}
}