package games.generic.controlModel.player;

import games.generic.controlModel.holders.ExperienceLevelHolder;
import games.generic.controlModel.subimpl.ExperienceLevelHolderImpl;
import games.generic.controlModel.subimpl.GEventInterfaceRPG;
import games.generic.controlModel.subimpl.GModalityET;
import games.generic.controlModel.subimpl.GModalityRPG;

/** Delegates to a {@link ExperienceLevelHolderImpl} and add some features. */
public interface PlayerWithExperience extends PlayerGeneric, ExperienceLevelHolder {

	public ExperienceLevelHolder getExpLevelHolder();

	public void setExpLevelHolder(ExperienceLevelHolder expLevelHolder);

	@Override
	public default int acquireExperience(int amount) { return getExpLevelHolder().acquireExperience(amount); }

	@Override
	public default int getExpToLevelUp() { return getExpLevelHolder().getExpToLevelUp(); }

	@Override
	public default ExperienceLevelHolder setLevel(int level) { return getExpLevelHolder().setLevel(level); }

	@Override
	public default ExperienceLevelHolder setExperienceNow(int experienceNow) {
		return getExpLevelHolder().setExperienceNow(experienceNow);
	}

	@Override
	public default ExperienceLevelHolder setExpToLevelUp(int experienceRequiredToLevelUp) {
		return getExpLevelHolder().setExpToLevelUp(experienceRequiredToLevelUp);
	}

	//

	public default int getExp() { return this.getExpLevelHolder().getExperienceNow(); }

	@Override
	public default int getLevel() { return this.getExpLevelHolder().getLevel(); }

	@Override
	public default void recalculateExpToLevelUp() { this.getExpLevelHolder().recalculateExpToLevelUp(); }

	/**
	 * See {@link ExperienceLevelHolder#acquireExperience(int)} AND earn attributes
	 * points.
	 */
	public default int gainExp(int exp) {
		int levelGained;
		GModalityET gm;
		GEventInterfaceRPG geirpg;
		gm = (GModalityET) this.getGameModality();
		levelGained = this.acquireExperience(exp);
		geirpg = (GEventInterfaceRPG) gm.getEventInterface();
		geirpg.fireExpGainedEvent(gm, exp);
		geirpg.fireLevelGainedEvent(gm, levelGained);
		if (levelGained > 0 && gm instanceof GModalityRPG) {
			GModalityRPG gmrpg;
			int attributesPointGained;
			gmrpg = (GModalityRPG) gm;
			attributesPointGained = levelGained * gmrpg.getAttributesPointGainedOnLevelingUp((BasePlayerRPG) this);
			((BasePlayerRPG) this).increasettributePointsLeftBy(attributesPointGained);
		}
		return levelGained;
	}

	//

}
