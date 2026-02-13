package tests.tGame;

import java.util.Set;
import java.util.function.Consumer;

import games.generic.controlModel.GModality;
import games.generic.controlModel.GModel;
import games.generic.controlModel.holders.GObjectsHolder;
import games.generic.controlModel.objects.TimedObject;
import games.generic.controlModel.subimpl.GModelTimeBased;
import tools.ObjectWithID;

@Deprecated
public class TestGModel {

	public static void main(String[] args) {
		GModel gm;
		GModelTimeBased gmt;
		Consumer<ObjectWithID> printer;
		printer = o -> System.out.println(o.getID());
		gm = new GModel() {
			@Override
			public void onCreate() { System.out.println("CIAOOOO"); }

		};
		System.out.println("helooo " + gm.addObjHolder("TIME", new GOH()));
		System.out.println("FINE");
		gmt = new GModelTimeBased() {

			@Override
			public void onCreate() { System.out.println("REAL timed model"); }

		};
		System.out.println("helooo 2.0 " + gmt.addObjHolder("MEMORYLESS", new GOH()));
		gmt.addTimedObject(new TO(777));
		gmt.addTimedObject(new TO(12));
		gmt.add(() -> Long.valueOf(44));
		gmt.add(() -> Long.valueOf(33));
		gmt.add(() -> Long.valueOf(-55));
		System.out.println("fine 2, stampa di tutto");
		gmt.forEach(printer);
		System.out.println("ora stampa solo i timed by " + GModelTimeBased.TIMED_OBJECT_HOLDER_NAME);
		gmt.getObjHolder(GModelTimeBased.TIMED_OBJECT_HOLDER_NAME).forEach(printer);
		System.out.println("ora stampa solo i timed by MEMORYLESS");
		gmt.getObjHolder("MEMORYLESS").forEach(printer);
		System.out.println("\n\n in the end, print just GModel own owids");
		gmt.getObjects().forEach(printer);
	}

	static class TO implements TimedObject {
		private static final long serialVersionUID = 1L;
		Long id;

		public TO(long id) { this.id = id; }

		@Override
		public Long getID() { return id; }

		@Override
		public void act(GModality modality, int timeUnits) { System.out.println("time " + timeUnits); }

		@Override
		public void onAddedToGame(GModality gm) {}

		@Override
		public void onRemovedFromGame(GModality gm) {}

		@Override
		public String getName() { return null; }

		@Override
		public GModality getGameModality() { // TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setGameModality(GModality gameModality) { // TODO Auto-generated method stub
		}
	}

	static class GOH implements GObjectsHolder {
		@Override
		public boolean add(ObjectWithID o) {
			System.out.println("ehehe adding " + o.getID());
			return false;
		}

		@Override
		public boolean remove(ObjectWithID o) { return false; }

		@Override
		public boolean contains(ObjectWithID o) { return false; }

		@Override
		public void forEach(Consumer<ObjectWithID> action) {}

		@Override
		public ObjectWithID get(Long id) { return null; }

		@Override
		public Set<ObjectWithID> getObjects() { return null; }

		@Override
		public boolean removeAll() { return false; }

		@Override
		public int objectsHeldCount() { // TODO Auto-generated method stub
			return 0;
		}
	}
}