package games.generic.controlModel.abilities.impl;

import games.generic.controlModel.GModality;
import games.generic.controlModel.abilities.AbilityGeneric;
import games.generic.controlModel.objects.OrbitingInteractiveObject;
import geometry.ObjectShaped;

public abstract class AOrbitingSpawningBlobs extends OrbitingInteractiveObject implements AbilityGeneric {
	private static final long serialVersionUID = 787845125863202423L;

	public AOrbitingSpawningBlobs(GModality gameModality, String name) {
		super(gameModality, name);
		tempSpawnBlob = 0;
	}

	protected int rotationTimeMillis, orbitingObjectRadius, tempSpawnBlob, spawiningBlobTIme;

	@Override
	public int getRotationTimeMillis() { return rotationTimeMillis; }

	@Override
	public int getOrbitingObjectRadius(int index, ObjectShaped os) { return orbitingObjectRadius; }

	@Override
	public void resetAbility() { tempSpawnBlob = 0; }

	@Override
	public void act(GModality modality, int milliseconds) {
		super.act(modality, milliseconds);
		if ((tempSpawnBlob += milliseconds) >= spawiningBlobTIme) {
			tempSpawnBlob -= spawiningBlobTIme;
			spawnOrbitingObj(modality);
		}

		// TODO
	}

	/**
	 * Updates all positions AND activate the blob's abilities.
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public void performAbility(GModality gm, int levelTarget) { super.updateAndActOrbitingObjects(gm, 0); }

	//

	protected void spawnOrbitingObj(GModality gm) { this.orbitingObjects.add(newOrbitingObj()); }

	/** Creates a new orbiting object and add it to the list (spawn it). */
	protected abstract ObjectShaped newOrbitingObj();

}