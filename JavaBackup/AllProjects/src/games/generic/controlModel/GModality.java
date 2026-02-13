package games.generic.controlModel;

import java.util.Comparator;
import java.util.Random;
import java.util.function.Consumer;

import games.generic.GameOptions;
import games.generic.controlModel.events.GEvent;
import games.generic.controlModel.events.GEventInterface;
import games.generic.controlModel.events.GEventManager;
import games.generic.controlModel.holders.GModalityHolder;
import games.generic.controlModel.holders.GameObjectsProvidersHolder;
import games.generic.controlModel.loaders.LoaderGeneric;
import games.generic.controlModel.misc.CurrencySet;
import games.generic.controlModel.misc.GThread;
import games.generic.controlModel.objects.GameObjectGeneric;
import games.generic.controlModel.player.PlayerGeneric;
import games.generic.controlModel.player.UserAccountGeneric;
import tools.Comparators;
import tools.ObjectNamedID;
import tools.ObjectWithID;

/**
 * One of the core classes.
 * <p>
 * See differences with {@link GController}.<br>
 * Represents the real "game", how it works, its type, its modality. Implements
 * every dynamics, rules, win conditions, interactions, etc. Obviously, those
 * concepts could (and should) be defined in separated classes. <br>
 * This class (and its related components) should manage the event-firing
 * systems (i.e. {@link GEvent} and {@link GEventManager}), like "object moved",
 * "spawn creatures / projectiles", "stuffs dropped", "damage dealt", "someone
 * healed", etc through a set of specific methods (that could be wrapped in a
 * specific class).<br>
 * The following examples will provides an idea of what a "modality" is:
 * <ul>
 * <li>PvsIA, 1v1, Multi_VS_Multi, etc</li>
 * <li>chess like, usual RPG through maps, card game, real time strategy (RTS),
 * vehicle based, visual story, etc</li>
 * <li>Examples for RPG and/or RTS: dungeons, open world,
 * YouVsWawesOfEnemies</li>
 * </ul>
 * <p>
 * Useful classes/interfaces used here:
 * <ul>
 * <li>{@link GameObjectsManager} to delegate most of the object's management
 * (events, time progression, space and map, damage dealing and spawning)</li>
 * <li>{@link GameObjectsProvidersHolder}</li>
 * <li>{@link GThread}</li>
 * <li>{@link PlayerGeneric}</li>
 * <li>{@link GMap}</li>
 * </ul>
 */
public abstract class GModality implements ObjectNamed {
	public static final Comparator<GModality> COMPARATOR_GAME_MODALITY = (o1, o2) -> {
		if (o1 == o2) { return 0; }
		if (o1 == null) { return -1; }
		if (o2 == null) { return 1; }
		return Comparators.STRING_COMPARATOR.compare(o1.getName(), o2.getName());
	};

	protected boolean isRunning;
	protected final GController controller;
	protected GModel model;
	protected String modalityName;
	/** Used to suspend threads */
	protected PlayerGeneric player;
	protected final GameObjectsProvidersHolder gameObjectsProviderHolder;
	protected final GameObjectsManager gomDelegated;
	protected Random random;

	public GModality(GController controller, String modalityName) {
		this.controller = controller;
		this.modalityName = modalityName;
		this.model = newGameModel();
		this.gameObjectsProviderHolder = controller.newGameObjectProvidersHolderFor(this);
		this.gomDelegated = newGameObjectsManager(); // ((GControllerRPG) controller).get; //
		this.random = new Random();
		onCreate();
		// il game model deve avere anche l'holder dovuto dal "Misom"
		assert this.getModel()
				.containsObjHolder(this.getGameObjectsManager().getGObjectInSpaceManager().getNameGObjHolder()) : //
				"The model does not have a \"GObjHolder\" instance for ObjectLocated (which is an instance of GObjectInSpaceManager)";
	}

	//
	// getter and setter

	// getter

	/**
	 * Used to check if the game is SHUTTED-OFF. <br>
	 * Differs from {@link #isRunning()}, see it for differences.
	 */
	public boolean isAlive() { return this.controller.isAlive(); }

	/**
	 * Simply return a flag. Used to check if the game is running or not (i.e.: the
	 * game is NOT running if {@link #pause()} has been invoked).
	 *
	 * <br>
	 * Differs from {@link #isAlive()}, see it for differences.
	 */
	public boolean isRunning() { return this.isRunning; }

	public PlayerGeneric getPlayer() { return player; }

	public String getModalityName() { return modalityName; }

	@Override
	public String getName() { return this.modalityName; }

	public GController getGameController() { return controller; }

	public Random getRandom() { return random; }

	public GModel getModel() { return model; }

	public GMap getMapCurrent() { return model.getMapCurrent(); }

	/**
	 * Returns an instance of a holder of "providers", classes that are able to
	 * provide (and probably instantiating as a new object) kinds of objects,
	 * depending on the provider required.
	 */
	public GameObjectsProvidersHolder getGameObjectsProvider() { return gameObjectsProviderHolder; }

	/**
	 * Get the HUGE delegate of almost everything. See {@link GameObjectsManager} to
	 * understand what it hold.
	 */
	public GameObjectsManager getGameObjectsManager() { return gomDelegated; }

	/**
	 * Delegates the results to {@link GameObjectsManager} returned by
	 * {@link #getGameObjectsManager()}.
	 */
	public GObjectsInSpaceManager getGObjectInSpaceManager() {
		GameObjectsManager gom;
		gom = this.getGameObjectsManager();
		return gom == null ? null : gom.getGObjectInSpaceManager();
	}

	//

	// setter

	public void setModel(GModel model) { this.model = model; }

	public void setPlayer(PlayerGeneric player) {
		this.player = player;
		if (this.player != null) { this.removeGameObject(this.player); }
		if (player != null) {
			this.addGameObject(player);
//			player.setGameModality(this);
		}
	}

	// proxy
	public void setMapCurrent(GMap mapCurrent) { this.model.setMapCurrent(mapCurrent); }

	public void setRandomSeed(long seed) { this.random.setSeed(seed); }

	//

	// TODO ABSTRACT

	/** Override designed BUT call <code>super.</code>{@link #onCreate()}}. */
	public void onCreate() {
		GObjectsInSpaceManager goism;
		/*
		 * Upon setting everything, "gom" and its "GOISM" included, add the goism to the
		 * model as an "objects holder" because that's what it is: an holder of
		 * ObkectLocated
		 */
		goism = this.getGameObjectsManager().getGObjectInSpaceManager();
		this.getModel().addObjHolder(goism.getNameGObjHolder(), goism);
	}

	// NEW-STUFF METHODS

	public abstract GModel newGameModel();

	public abstract CurrencySet newCurrencyHolder();

	/** See {@link #newPlayerInGame(UserAccountGeneric, ObjectNamedID)}. */
	protected PlayerGeneric newPlayerInGame(UserAccountGeneric superPlayer) {
		return newPlayerInGame(superPlayer, null);
	}

	/**
	 * See {@link PlayerGeneric} to see what is meant, providing also a characer
	 * type.
	 */
	protected abstract PlayerGeneric newPlayerInGame(UserAccountGeneric superPlayer, ObjectNamedID characterType);

	/**
	 * Defines and returns the instance of {@link GameObjectsManager} that will be
	 * used in this game modality and supports it in defining the game.<br>
	 * Requires an {@link GEventInterface} as a parameter but it's optional, if the
	 * game modality does not use the events system.
	 * <p>
	 * It could be used by {@link GMap}
	 */
	protected abstract GameObjectsManager newGameObjectsManager();

	/**
	 * Create a new {@link GMap}, depending on its name.
	 */
	protected abstract GMap newGameMap(String mapName);

	/**
	 * Publish and fire the event in some way, if and only if this current Game
	 * Modality supports events, otherwise leave and empty implementation.
	 */
//	public abstract void fireEvent(GEvent event);

	// abstract methods

	public abstract void loadFrom(GController gc, GameOptions gameOpt, LoaderGeneric loader);

	//

	// game object handler

	//

	// TODO CONCRETE METHODS

	/** See {@link GObjectsInSpaceManager#getSpaceSubunitsEachMacrounits()}. */
	public int getSpaceSubunitsEachMacrounits() {
		return getGameObjectsManager().getGObjectInSpaceManager().getSpaceSubunitsEachMacrounits();
	}

	public void beginsPlayesInteraction(PlayerGeneric otherPlayer) {

	}

	/**
	 * Add a {@link ObjectWithID} to the {@link GModel} and fire the related event
	 * ({@link GameObjectGeneric#onAddedToGame(GModality)}) upon addition.
	 */
	public boolean addGameObject(GameObjectGeneric o) {
		GModel gm;
		if (o == null)
			return false;
		if (o instanceof GModalityHolder) { ((GModalityHolder) o).setGameModality(this); }
		gm = this.getModel();
		if (gm != null) {
			boolean added;
			added = gm.add(o);
			if (added) { o.onAddedToGame(this); }
			return added;
		}
		return false;
	}

	/**
	 * Removes the given object to this game instance and fire the related event
	 * ({@link GameObjectGeneric#onRemovedFromGame(GModality)}) upon removal.
	 *
	 * @param o object to be removed
	 * @return {@code true} if the object have been removes
	 */
	public boolean removeGameObject(GameObjectGeneric o) {
		GModel gm;
		if (o == null)
			return false;
		if (o instanceof GModalityHolder) { ((GModalityHolder) o).setGameModality(null); }
		gm = this.getModel();
		if (gm != null) {
			boolean removed;
			removed = gm.remove(o);
			if (removed)
				o.onRemovedFromGame(this);
			return removed;
		}
		return false;
	}

	public boolean removeAllGameObjects() {
		GModel gm;
		gm = this.getModel();
		if (gm != null) { return gm.removeAll(); }
		return false;
	}

	public boolean containsGameObject(ObjectWithID o) {
		GModel gm;
		if (o == null)
			return false;
		gm = this.getModel();
		if (gm != null) { return gm.contains(o); }
		return false;
	}

	public void forEachGameObject(Consumer<ObjectWithID> action) {
		GModel gm;
		if (action == null)
			return;
		gm = this.getModel();
		if (gm != null) { gm.forEach(action); }
	}

	//

	// TODO threads

	//

	/**
	 * Override designed - call this at the end on overrides.<br>
	 * Differs from {@link #resume()} because resume just change the state of the
	 * current match from stopped to running, in some way, while "start" create all
	 * instances, maps, handlers, threads, sockets, etc etc.
	 */
	public abstract void startGame();

	public void pause() { this.isRunning = false; }

	public void resume() { this.isRunning = true; }

	/** Override AND call the super implementation. */
	public void closeAll() { this.pause(); }
}