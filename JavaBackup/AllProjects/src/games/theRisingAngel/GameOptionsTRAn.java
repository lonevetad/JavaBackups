package games.theRisingAngel;

import games.generic.controlModel.holders.ProbabilityOfContextesHolders;
import games.generic.controlModel.subimpl.GameOptionsRPG;

/**
 * Configuration-like class, holding some settings of the games mechanic
 *
 * @author ottin
 *
 */
public class GameOptionsTRAn extends GameOptionsRPG {

	protected GameOptionsTRAn(GControllerTRAn gameController) {
		super(gameController);
		this.probabilitiesContexes = gameController.getProbabilityOfContextesHolders();
		this.shieldDelayMilliseconds = 4000; // as default
	}

	protected int pointsAttributeUpgradeOnLeveling, factorPriceEssenceExtracting, shieldDelayMilliseconds;
	protected final ProbabilityOfContextesHolders probabilitiesContexes;

	//

	public ProbabilityOfContextesHolders getProbabilitiesContexes() { return probabilitiesContexes; }

	public int getPointsAttributeUpgradeOnLeveling() { return pointsAttributeUpgradeOnLeveling; }

	public int getFactorPriceEssenceExtracting() { return factorPriceEssenceExtracting; }

	public int getShieldDelayMilliseconds() { return shieldDelayMilliseconds; }

	//

	public void setPointsAttributeUpgradeOnLeveling(int pointsAttributeUpgradeOnLeveling) {
		this.pointsAttributeUpgradeOnLeveling = pointsAttributeUpgradeOnLeveling;
	}

	public void setFactorPriceEssenceExtracting(int factorPriceEssenceExtracting) {
		this.factorPriceEssenceExtracting = factorPriceEssenceExtracting;
	}

	public void setShieldDelayMilliseconds(int shieldDelayMilliseconds) {
		this.shieldDelayMilliseconds = shieldDelayMilliseconds;
	}
}