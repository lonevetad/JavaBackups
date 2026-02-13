package common.abstractCommon.behaviouralObjectsAC;

public interface ObjectMoving /* extends ObjectActingOnPassingTime */ {

	public boolean canMove();

	public int getVelocity();

	public int getX();

	public int getY();

	public ObjectMoving setCanMove(boolean canMove);

	public ObjectMoving setVelocity(int pixelPerSecond);

	public ObjectMoving setX(int x);

	public ObjectMoving setY(int y);

	/*
	 * @Override public default void act(int milliseconds) { move(milliseconds); }
	 */

	/**
	 * Consider the time passed by, hold into the integer parameter <code>milliseconds</code>, and
	 * then compute the moving calling the override-designed function [{@link #executeMoving(int)}.
	 */
	public default void move(int milliseconds) {
		if (canMove()) executeMoving(milliseconds);
	}

	/*
	 * OVERRIDE-DESIGNED <p> This function is used by {@link #move(int)} to apply the movement of
	 * this object having length (that is, the number of pixel run) equal to the integer parameter
	 * <code>lengthStepMicropixel</code>.<br> The parameter is usually used as a distance from the
	 * initial point to the destination, but could be reinterpretated.lengthStepMicropixel
	 */
	/**
	 * OVERRIDE-DESIGNED
	 * <p>
	 * This function is used by {@link #move(int)} to apply the movement of this object. The
	 * parameter is the amount of milliseconds passed since last move call. This amount should be
	 * used to compute how, where and how much move.
	 * <p>
	 * Should call {@link #updatePositionAfterMove()}.
	 *
	 * @param milliseconds
	 *            amount of milliseconds passed since last move call.
	 */
	public void executeMoving(int milliseconds);

	/**
	 * After the movement have been done (by {@link #executeMoving(int)}), the position could be
	 * updated by this funcion. <br>
	 * Could be ignored.
	 */
	public default void updatePositionAfterMove() {

	}

	public default void changeDirection(int newx, int newy) {

	}

	public default void arrivedToDestination() {

	}

}