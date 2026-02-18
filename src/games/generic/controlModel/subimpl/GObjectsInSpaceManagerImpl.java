package games.generic.controlModel.subimpl;

import java.util.Set;

import dataStructures.isom.InSpaceObjectsManager;
import games.generic.controlModel.GModality;
import games.generic.controlModel.GObjectsInSpaceManager;
import games.generic.controlModel.objects.ObjectInSpace;
import geometry.pointTools.HeuristicManhattan;
import oldToBeDeleted.PathFinderIsomAStar_Naive;

/**
 * Based on a {@link InSpaceObjectsManager}.
 */
public abstract class GObjectsInSpaceManagerImpl implements GObjectsInSpaceManager {

	public GObjectsInSpaceManagerImpl(InSpaceObjectsManager<Double> isom) {
		this.objectsInSpace = null;
		this.isom = isom;
		this.isom.setPathFinder(new PathFinderIsomAStar_Naive<Double>(this.isom, HeuristicManhattan.SINGLETON));
	}

//	protected Set<ObjectWithID> objWID;
	protected Set<ObjectInSpace> objectsInSpace;
	protected InSpaceObjectsManager<Double> isom;
	protected GModality gameModality;

	//

	@Override
	public InSpaceObjectsManager<Double> getOIMManager() { return isom; }

	@Override
	public GModality getGameModality() { return gameModality; }

	@Override
	public Set<ObjectInSpace> getObjects() {
		if (this.objectsInSpace == null) { this.objectsInSpace = GObjectsInSpaceManager.super.getObjects(); }
		return this.objectsInSpace;
	}

	//

	@Override
	public void setGameModality(GModality gameModality) { this.gameModality = gameModality; }

	//

	@Override
	public int objectsHeldCount() { return this.objectsInSpace.size(); }

	@Override
	public ObjectInSpace get(Long id) { return (ObjectInSpace) this.getOIMManager().getObjectLocated(id); }

	@Override
	public boolean contains(ObjectInSpace o) { return (o == null) ? false : this.getObjects().contains(o); }

	@Override
	public boolean containsObject(ObjectInSpace o) { return contains(o); }
}