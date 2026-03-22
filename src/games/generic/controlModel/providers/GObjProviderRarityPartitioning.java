package games.generic.controlModel.providers;

import java.util.Random;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.GModality;
import games.generic.controlModel.ObjectNamed;
import games.generic.controlModel.holders.RarityHolder;
import games.generic.controlModel.misc.FactoryObjGModalityBased;
import games.generic.controlModel.misc.GameObjectsProvider;
import tools.Comparators;
import tools.WeightedSetOfRandomOutcomes;

public abstract class GObjProviderRarityPartitioning<E extends RarityHolder & ObjectNamed>
		extends GameObjectsProvider<E> {
	public GObjProviderRarityPartitioning() {
		super();
		this.objsClusteredByRarity = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight,
				Comparators.INTEGER_COMPARATOR);
	}

	protected MapTreeAVL<Integer, MapTreeAVL<String, FactoryObjGModalityBased<E>>> objsClusteredByRarity;

	public MapTreeAVL<Integer, MapTreeAVL<String, FactoryObjGModalityBased<E>>> getObjsClusteredByRarity() {
		return objsClusteredByRarity;
	}

	/** Beware: may return <code>null</code> if the cluster is empty */
	public MapTreeAVL<String, FactoryObjGModalityBased<E>> getRarityCluster(Integer rarityIndex) {
		return this.objsClusteredByRarity.get(rarityIndex);
	}

	/**
	 * Overrode assigning <code>0</code> to the rarity index (the value returned by
	 * {@link RarityHolder#getRarityIndex()}).
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public void addObj(String name, FactoryObjGModalityBased<E> gm) {
		this.addObj(name, RarityHolder.NO_RARITY_INDEX, gm);
	}

	@Override
	public void addObj(String name, int rarityIndex, FactoryObjGModalityBased<E> gm) {
		MapTreeAVL<String, FactoryObjGModalityBased<E>> rarityCluster;
		Integer ri;
		super.addObj(name, gm);
		if (rarityIndex == RarityHolder.NO_RARITY_INDEX)
			return;
		ri = rarityIndex;
		rarityCluster = getRarityCluster(ri);
		if (rarityCluster == null) {
			rarityCluster = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
					Comparators.STRING_COMPARATOR);
			this.objsClusteredByRarity.put(ri, rarityCluster);
		}
	}

	//

	/**
	 * Returns a new instance of <code>E</code> (the parametric type) considering
	 * the following criteria:<br>
	 * -) It's draw using a weighted cluster (i.e.: a set of object, having all of
	 * them the same value of {@link RarityHolder#getRarityIndex()}) that is
	 * obtained using the weighted probability distribution represented by the given
	 * instance of {@link WeightedSetOfRandomOutcomes}. <br>
	 * -) Once that cluster is obtained, create the desired new object drawing it
	 * randomly from the cluster, using the given {@link Random} instance.
	 */
	public E newObjectByRarity(GModality gm, WeightedSetOfRandomOutcomes weightedProbabilities, Random randomForCluster) {
		int index;
		MapTreeAVL<String, FactoryObjGModalityBased<E>> rarityCluster;
		FactoryObjGModalityBased<E> factory;
		index = weightedProbabilities.next();
		rarityCluster = getRarityCluster(index);
		if (rarityCluster == null)
			return null;
		factory = rarityCluster.getAt(randomForCluster.nextInt(rarityCluster.size())).getValue();
		return factory.newInstance(gm);
	}
}