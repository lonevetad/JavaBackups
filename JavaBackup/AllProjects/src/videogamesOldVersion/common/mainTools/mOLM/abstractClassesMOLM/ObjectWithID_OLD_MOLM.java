package videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM;

import java.io.Serializable;

import tools.Comparators;
import videogamesOldVersion.common.abstractCommon.Memento;
import videogamesOldVersion.common.abstractCommon.behaviouralObjectsAC.MementoPatternImplementor;
import videogamesOldVersion.common.abstractCommon.behaviouralObjectsAC.ObjectSerializableDoingNothingAfter;
import videogamesOldVersion.common.abstractCommon.referenceHolderAC.LoggerMessagesHolder;

public abstract class ObjectWithID_OLD_MOLM
		implements Serializable, ObjectSerializableDoingNothingAfter, LoggerMessagesHolder, MementoPatternImplementor {
	private static final long serialVersionUID = -28065206360L;
	private static int progressiveID = 0;
	public static final MyComparator<ObjectWithID_OLD_MOLM> COMPARATOR_OWID = (o1, o2) -> {
		if (o1 == o2)
			return 0;
		if (o1 == null)
			return -1;
		if (o2 == null)
			return 1;
		return Comparators.INTEGER_COMPARATOR.compare(o1.ID, o2.ID);
	};

	/**
	 * Questo ID puo' essere usato nell'algoritmo di raccolta di oggetti in una data
	 * area : per verificare che un oggetto non sia gia' presente, si ricerca in un
	 * RedBlackTree usando questo campo come chiave. Così inserzione e ricerca sono
	 * di ordine O(log(n)).
	 */

	public ObjectWithID_OLD_MOLM() {
		super();
		ID = ++progressiveID;
		setLog(null);
	}

	public ObjectWithID_OLD_MOLM(ObjectWithID_OLD_MOLM o) {
		this();
		this.isNotSolid = o.isNotSolid;
		this.log = o.log;
	}

	public ObjectWithID_OLD_MOLM(MementoOWID o) {
		super();
		setLog(null);
		this.reloadState(o);
	}

	protected boolean isNotSolid;
	protected Integer ID;
	protected transient LoggerMessages log;

	//

	// TODO GETTER

	public Integer getID() {
		return ID;
	}

	/**
	 * If <code>true</code>, this object is part of the background and is
	 * "walkable", like a tile of grass, sand or street.<br>
	 * If <code>false</code>, then it's something physical, like a tree, a stone, a
	 * creature, a NPC, a building or a wall.
	 */
	public boolean isNotSolid() {
		return isNotSolid;
	}

	@Override
	public LoggerMessages getLog() {
		return log;
	}

	//

	// TODO SETTER

	public ObjectWithID_OLD_MOLM setNotSolid(boolean isNotSolid) {
		this.isNotSolid = isNotSolid;
		return this;
	}

	@Override
	public ObjectWithID_OLD_MOLM setLog(LoggerMessages log) {
		this.log = LoggerMessages.loggerOrDefault(log);
		return this;
	}

	//

	// TODO STATIC METHODS

	public static int getProgressiveID() {
		return progressiveID;
	}

	/**
	 * @return <code>true</code> if the given object is <code>null</code>,<br>
	 *         <code>theGivenParameter.{@link #isNotSolid()}</code> otherwise.
	 */
	public static boolean extractIsNotSolid(ObjectWithID_OLD_MOLM o) {
		return o == null ? true : o.isNotSolid();
	}

	//

	// TODO OTHER METHODS

	@Override
	public String toString() {
		return "ObjectWithID_OLD_MOLM [ID=" + ID + ", isNotSolid=" + isNotSolid() + "]";
	}

	public boolean canAddMeToMolm(AbstractMatrixObjectLocationManager molm) {
		if (molm == null)
			return false;
		return false;
	}

	public String addMeToMolm(AbstractMatrixObjectLocationManager molm) {
		if (molm == null)
			return "ERROR: on ObjectWithID_OLD_MOLM.addMeToMolm, molm is null";
		return null;
	}

	public String removeMeToMolm(AbstractMatrixObjectLocationManager molm) {
		if (molm == null)
			return "ERROR: on ObjectWithID_OLD_MOLM.removeMeToMolm, molm is null";
		return null;
	}

	/*
	 * public boolean canAddMeToMolm(MatrixObjectLocationManager molm, int xx, int
	 * yy) { if (molm == null) return false; return false; } public boolean
	 * addMeToMolm(MatrixObjectLocationManager molm, int xx, int yy) { if (molm ==
	 * null) return false; return false; } public boolean
	 * removeMeToMolm(MatrixObjectLocationManager molm, int xx, int yy) { if (molm
	 * == null) return false; return false; }
	 */

	// MEMENTO

	@Override
	public boolean reloadState(Memento me) {
		MementoOWID m;
		if (me != null && me instanceof MementoOWID) {
			m = (MementoOWID) me;
			this.ID = m.ID;
			this.isNotSolid = m.isNotSolid;
			if (log == null)
				log = LoggerMessages.LOGGER_DEFAULT;
			return true;
		}
		return false;
	}

	@Override
	public abstract MementoOWID createMemento();

	//

	// class

	public static abstract class MementoOWID extends Memento {
		private static final long serialVersionUID = 6521065188070707070L;
		protected boolean isNotSolid;
		protected Integer ID;

		public MementoOWID() {
			super();
		}

		public MementoOWID(ObjectWithID_OLD_MOLM o) {
			this();
			this.ID = o.ID;
			this.isNotSolid = o.isNotSolid;
		}

		public boolean isNotSolid() {
			return isNotSolid;
		}

		public Integer getID() {
			return ID;
		}
	}
}