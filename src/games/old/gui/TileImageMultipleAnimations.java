package games.old.gui;

import dataStructures.MapTreeAVL;
import games.generic.view.TileImage;
import tools.Comparators;
import videogamesOldVersion.common.abstractCommon.AnimationName;

/**
 * Classe che tiene piu' animazioni per lo stesso oggetto. Sia che esse siano
 * immagini statiche sia che siano animazioni, esse devono essere della forma:
 * "{@link AnimationName#fileNameImage(TileImageMultipleAnimations)}".
 */
public class TileImageMultipleAnimations extends TileImage {
	private static final long serialVersionUID = 456091560770723L;

	public TileImageMultipleAnimations() { // EnumGameObjectTileImageCollection egotic) {
		/*
		 * this(null, egotic); } public TileImageMultipleAnimations(AnimationName[]
		 * animationsList, TileImageFactoryMultipleAnimation factory) { this.egotic =
		 * egotic;
		 */
		animations = MapTreeAVL.newMap(Comparators.STRING_COMPARATOR);
		/*
		 * if (animationsList != null && animationsList.length > 0) { for (AnimationName
		 * an : animationsList) { } }
		 */
	}

	protected String currentAnimationName;
	protected MapTreeAVL<String, TileImage> animations;
	// protected EnumGameObjectTileImageCollection egotic;

	//

	public String getCurrentAnimationName() { return currentAnimationName; }

	public MapTreeAVL<String, TileImage> getAnimations() { return animations; }

	//

	// TODO GETTER

	//

	// TODO sETTER

	public TileImageMultipleAnimations setCurrentAnimation(AnimationName currentAnimationName) {
		if (currentAnimationName != null) {
			return setCurrentAnimation(currentAnimationName.fileNameImage(this));
		} else
			return this;
	}

	public TileImageMultipleAnimations setCurrentAnimation(String currentAnimationName) {
		TileImage ti;
		this.currentAnimationName = currentAnimationName;
		if (currentAnimationName != null) {
			ti = getAnimationByName(currentAnimationName);
			if (ti != null)
				copyImagesFrom(ti);
		}
		return this;
	}

	//

	// TODO PUBLIC

	/**
	 * Calls <code>getAnimationByName(name.fileNameImage(this))</code>.<br>
	 * See{@link AnimationName#fileNameImage(TileImageMultipleAnimations)}.
	 */
	public TileImage getAnimationByName(AnimationName name) { return getAnimationByName(name.fileNameImage(this)); }

	/** See {@link #getAnimationByName(AnimationName)} for how to use it. */
	public TileImage getAnimationByName(String name) {
		return animations.fetch(name);
	}

	public TileImage addTileImageAnimation(AnimationName name, TileImage ti) {
		if (name == null || ti == null)
			return null;
		return addTileImageAnimation(name.name(), ti);
	}

	public TileImage addTileImageAnimation(String name, TileImage ti) {
		TileImage old;
		old = null;
		if (name != null && ti != null) {
			if (animations.isEmpty())
				copyImagesFrom(ti);
			animations.add(name, ti);
		}
		return old;
	}

	public TileImage removeTileImageAnimation(AnimationName name) {
		if (name == null)
			return null;
		return removeTileImageAnimation(name.name());
	}

	public TileImage removeTileImageAnimation(String name) {
		TileImage old;
		old = null;
		if (name != null) {
			old = animations.delete(name);
			if (animations.isEmpty()) {
				setImageResized(null);
				setImageOriginal(null);
				setAnimatedImage(null);
			}
		}
		return old;
	}
}