package games.generic.controlModel.subimpl;

import java.util.Arrays;

import games.generic.controlModel.holders.ExperienceLevelHolder;

public class ExperienceLevelHolderImpl implements ExperienceLevelHolder {

	private static final long serialVersionUID = 114809520088009227L;
	public static final int DEFAULT_BASE_LINEAR_EXP_REQUIRED = 100;
	private static ExperienceLevelHolderImpl DEFAULT_INSTANCE = null;

	public static ExperienceLevelHolderImpl getDefaultInstance() {
		if (DEFAULT_INSTANCE == null) {
			DEFAULT_INSTANCE = new ExperienceLevelHolderImpl();
		}
		return DEFAULT_INSTANCE;
	}

	//

	public ExperienceLevelHolderImpl() {
		reset();
	}

	/** Copy constructor */
	public ExperienceLevelHolderImpl(ExperienceLevelHolderImpl e) {
		this.level = e.level;
		this.experienceNow = e.experienceNow;
		this.expToLevelUp = e.expToLevelUp;
	}

	protected int level, experienceNow, expToLevelUp;

	//

	// TODO GETTER

	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public int getExperienceNow() {
		return experienceNow;
	}

	@Override
	public int getExpToLevelUp() {
		return expToLevelUp;
	}

	//

	// TODO SETTER

	@Override
	public ExperienceLevelHolderImpl setLevel(int level) {
		if (level >= 0)
			this.level = level;
		return this;
	}

	@Override
	public ExperienceLevelHolderImpl setExperienceNow(int experienceNow) {
		if (experienceNow >= 0)
			this.experienceNow = experienceNow;
		return this;
	}

	@Override
	public ExperienceLevelHolderImpl setExpToLevelUp(int experienceRequiredToLevelUp) {
		if (experienceRequiredToLevelUp > 0)
			this.expToLevelUp = experienceRequiredToLevelUp;
		return this;
	}

	//

	// TODO OTHER

	public static ExperienceLevelHolderImpl newDefaultInstance() {
		return (DEFAULT_INSTANCE != null) ? DEFAULT_INSTANCE.clone().reset()
				: (DEFAULT_INSTANCE = new ExperienceLevelHolderImpl());
	}

	@Override
	public ExperienceLevelHolderImpl reset() {
		level = 0;
		experienceNow = 0;
		recalculateExpToLevelUp();
		return this;
	}

	@Override
	public void recalculateExpToLevelUp() {
		expToLevelUp = (level + 1) * DEFAULT_BASE_LINEAR_EXP_REQUIRED;
	}

	@Override
	public String toString() {
		StringBuilder sb;
		toString(sb = new StringBuilder(), 0);
		return sb.toString();
	}

	@Override
	public ExperienceLevelHolderImpl clone() {
		return new ExperienceLevelHolderImpl(this);
	}

	@Override
	public void toString(StringBuilder sb, int tabLevel) {
		if (sb != null) {
			addTab(sb, tabLevel++);
			sb.append("ExperienceLevelHolder");
			addTab(sb, tabLevel);
			sb.append("level: ").append(level);
			addTab(sb, tabLevel);
			sb.append("experienceNow: ").append(experienceNow);
			addTab(sb, tabLevel);
			sb.append("\n\texperienceRequiredToLevelUp: ").append(expToLevelUp);
		}
	}

	//

	public static void main(String[] args) {
		int[] exps;
		ExperienceLevelHolderImpl elh;

		elh = ExperienceLevelHolderImpl.DEFAULT_INSTANCE;

		exps = new int[] { 1250, 700, 10, 50, 500, 666, 10000, 424 };
		System.out.println(Arrays.toString(exps));

		System.out.println(elh);
		for (int exp : exps) {
			System.out.println("\nadding " + exp);
			System.out.println("gaining " + elh.acquireExperience(exp) + " levels");
			System.out.println(elh);
		}

	}
}