package common;

import java.util.List;

import common.abstractCommon.behaviouralObjectsAC.ObjectActingOnPassingTime;
import common.abstractCommon.behaviouralObjectsAC.ObjectPerformingTasksOnPassingTime;
import common.mainTools.dataStruct.MyLinkedList;

/**
 * Class holding lots of {@link ObjectActingOnPassingTime}.<br>
 * Right now (23/08/2017) it's not usefull because the generic difficulty to
 * track and manage inserted ObjectActingOnPassingTime instances.
 */
public class TasksPerformerOnPassingTime implements ObjectPerformingTasksOnPassingTime {
	private static final long serialVersionUID = 62090871850303303L;

	public TasksPerformerOnPassingTime() {
		super();
	}

	MyLinkedList<ObjectActingOnPassingTime> tasksList;
	private Acter acter;

	@Override
	public List<ObjectActingOnPassingTime> getTasksList() {
		if (tasksList == null) tasksList = new MyLinkedList<>();
		return tasksList;
	}

	@Override
	public ObjectPerformingTasksOnPassingTime setTasksList(List<ObjectActingOnPassingTime> l) {
		if (l != null && l instanceof MyLinkedList<?>) tasksList = (MyLinkedList<ObjectActingOnPassingTime>) l;
		return this;
	}

	@Override
	public void act(int milliseconds) {
		MyLinkedList<ObjectActingOnPassingTime> l;
		Acter a;
		if ((l = (MyLinkedList<ObjectActingOnPassingTime>) getTasksList()) != null && l.size() > 0) {
			if ((a = acter) == null)
				a = acter = new Acter(this, milliseconds);
			else
				a.milliseconds = milliseconds;
			l.forEachElements(a);
		}
	}

	//

	//

	public static final class Acter implements MyLinkedList.DoSomethingWithElement<ObjectActingOnPassingTime> {
		private static final long serialVersionUID = 8940165207777L;
		int milliseconds;
		ObjectPerformingTasksOnPassingTime optopt;

		public Acter(ObjectPerformingTasksOnPassingTime optopt, int milliseconds) {
			this.optopt = optopt;
			this.milliseconds = milliseconds;
		}

		@Override
		public Object doSomething(ObjectActingOnPassingTime o) {
			if (o != null && o != optopt) o.act(milliseconds);
			return null;
		}
	}

}