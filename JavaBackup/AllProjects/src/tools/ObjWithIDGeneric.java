package tools;

public interface ObjWithIDGeneric<IDType> extends Stringable {
	public IDType getID();

	/**
	 * Set the ID. <br>
	 * Returns whether the ID has changed or not.<br>
	 * It should be performed just one time, for instance:<br>
	 *
	 * <pre>
	 * <code>
	.. previous code
	protected IDType ID = null; // instance field
	.. other code

	public boolean setID(IDType newID){
		if(this.ID != null || newID == null){ return false; }
		this.ID = newID;
		return true;
	}
	 * </code>
	 * </pre>
	 *
	 * @param ID the desired identifier, hopefully unique.
	 * @return <code>true</code> if the ID has changed afterwards,
	 *         <code>false</code> otherwise.
	 */
	public boolean setID(IDType newID);
}