package videogamesOldVersion.common.abstractCommon;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.net.Socket;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import tools.minorTools.StringSorter;
import videogamesOldVersion.common.EnumGameObjectTileImageCollection;
import videogamesOldVersion.common.GameMechanism;
import videogamesOldVersion.common.GameObjectInMap;
import videogamesOldVersion.common.MOLMManager;
import videogamesOldVersion.common.abstractCommon.LoaderGeneric.LoadWriteType;
import videogamesOldVersion.common.abstractCommon.behaviouralObjectsAC.AbstractMOLMManager;
import videogamesOldVersion.common.abstractCommon.behaviouralObjectsAC.ObjectActingOnPassingTime;
import videogamesOldVersion.common.abstractCommon.behaviouralObjectsAC.ObjectSerializableDoingNothingAfter;
import videogamesOldVersion.common.abstractCommon.referenceHolderAC.LoggerMessagesHolder;
import videogamesOldVersion.common.abstractCommon.referenceHolderAC.MainHolder;
import videogamesOldVersion.common.abstractCommon.referenceHolderAC.ThreadsHolder;
import videogamesOldVersion.common.gui.MainGUI;
import videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM.AbstractMatrixObjectLocationManager;
import videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM.DoSomethingWithNode;
import videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM.ObserverMolm;

/**
 * The Controller, the one who holds a Model (a data), interact with the View,
 * loads all necessary data through {@link AllLoader}, manage threads and
 * connections (like {@link Socket}), save/load objects like
 * {@link AbstractMapGame} and {@link AbstractSaveData} and holds the
 * {@link GameMechanism}.<br>
 * The real game's implementations should be put inside this last one.
 */
public abstract class MainController
		implements ObjectActingOnPassingTime, LoggerMessagesHolder, ThreadsHolder, ObjectSerializableDoingNothingAfter
// AbstractMOLMManager_Delegating,
{
	private static final long serialVersionUID = 5290890L;

	public static final Dimension FULL_SCREEN = Toolkit.getDefaultToolkit().getScreenSize();
	public static final int MICROPIXEL_EACH_TILE = 4// 16//
			, MILLISECONDS_STANDARD_WAIT_AFTER_RESUME = 10, MILLISECONDS_HEACH_HEAL_BEAM = 1000,
			MAX_MILLISECONDS_EACH_GAME_FRAME = 10;

	public static final long MIN_MILLISECOND_EACH_CYCLE = 2;
	public static final char charSeparatorFolderPath = File.separatorChar;
	public static final Comparator<File> FILE_COMPARATOR = (f1, f2) -> {
		if (f1 == f2)
			return 0;
		if (f1 == null)
			return -1;
		if (f2 == null)
			return 1;
		return StringSorter.compareStr(f1.getName(), f2.getName());
	};

	//

	// TODO FIELDS

	MainGUI mainGUI;
	// MANAGEMENT
	protected boolean isPlaying = false;
	private boolean neverInitialized;
	protected boolean isWorking;
	Thread threadMusic;// , threadGame;
	// RunnableSuspendible runnableGame;
	Map<Long, Thread> poolThread;
	// MatrixObjectLocationManager molm;

	// GAME ONLY
	// HeadTailGetterSetter<ObjectInMap> htgs; // <br>
	// ObjectInMap allObjectsInMap_Head, allObjectsInMap_Tail; // <br>
	// allOIMsize
	/*
	 * public int // mapGameWidth_Micropixel = MIN_NUM_MICROPIXEL_PILM_HORIZONTAL,
	 * mapGameHeight_Micropixel = MIN_NUM_MICROPIXEL_PILM_VERTICAL// ;
	 */

	// UTILITIES for other classes and stuffs
	// GameModelGeneric gameModel;
	/** use it as cache during creating new GameMap */
	// protected MOLMManagerObservable observableMolmManager;
	/***/
	GameMechanism gameMechanism;
	LoaderGeneric loader;
	LoggerMessages log;
	EnumGameObjectTileImageCollection allGameObjectTileImage;
	List<String> mapsNames;// who should hold this field??? GameModel?
	AllLoader allLoader;

	DoSomethingWithNode mainSetterForMOLM;
	// public final DoSomethingWithSomeoneElse mainSetter;

	//

	public MainController(LoaderGeneric loaderGeneric, MainGUI mgg) {
		super();
		isWorking = true;
		// allObjectsInMap_Head = allObjectsInMap_Tail = null;
		// allOIMsize = 0;
		/*
		 * htgs = new
		 * NodeSharedWithMultipleLinkedList.HeadTailGetterSetter<ObjectInMap>() {
		 *
		 * @Override public NodeSharedWithMultipleLinkedList<ObjectInMap> setTail(
		 * NodeSharedWithMultipleLinkedList<ObjectInMap> newTail) { ObjectInMap o; o =
		 * null; if (newTail != null) { o = allObjectsInMap_Tail; allObjectsInMap_Tail =
		 * (ObjectInMap) newTail; } return o; }
		 *
		 * @Override public NodeSharedWithMultipleLinkedList<ObjectInMap> setHead(
		 * NodeSharedWithMultipleLinkedList<ObjectInMap> newHead) { ObjectInMap o; o =
		 * null; if (newHead != null) { o = allObjectsInMap_Head; allObjectsInMap_Head =
		 * (ObjectInMap) newHead; } return o; }
		 *
		 * @Override public NodeSharedWithMultipleLinkedList<ObjectInMap> getTail() {
		 * return allObjectsInMap_Tail; }
		 *
		 * @Override public NodeSharedWithMultipleLinkedList<ObjectInMap> getHead() {
		 * return allObjectsInMap_Head; } };
		 */
		this.loader = loaderGeneric;
		if (mgg != null)
			mainGUI = mgg.setMainController(this);

		/*
		 * JAVA is bugged, has problems with deserializing lambdas .. so there's the
		 * workaround
		 */
		/*
		 * MainController main; main = this; mainSetter = (to, args) ->
		 * main.setMainTo(to);
		 */
	}

	public static void main(String[] args) {
		MainController.main(args, null);
	}

	public static void main(String[] args, MainController m) {
		// TileList.initialize();
		System.out.println("STARTS :D");
		if (m != null)
			m.initialize();
		else
			System.out.println("main NULL !!!");
		// TileList.printALL_TILES();
		System.out.println("main ends");
	}

	//

	// TODO METHODS

	//

	// TODO GETTERS

	public abstract String getGameName();

	public GameMechanism getGameMechanism() {
		return gameMechanism;
	}

	/*
	 * public MOLMManagerObservable getObservableMolmManager() {return
	 * observableMolmManager; }
	 */

	public GameModelGeneric getGameModel() {
		return gameMechanism == null ? null : gameMechanism.getGameModel();
	}

	public AbstractMatrixObjectLocationManager[] getMOLMs() {
		GameModelGeneric gameModel;
		gameModel = getGameModel();
		return gameModel == null ? null : gameModel.getMolms();
	}

	public MainGUI getMainGenericGUI() {
		return mainGUI;
	}

	public Thread getThreadMusic() {
		return threadMusic;
	}

	public DoSomethingWithNode getMainSetterForMOLM() {
		return mainSetterForMOLM;
	}

	public int getMapGameWidth_Micropixel() {
		AbstractMapGame mg;
		GameModelGeneric gameModel;
		gameModel = getGameModel();
		mg = gameModel == null ? null : gameModel.getMapGameCurrent();
		return mg == null ? 0 : mg.getWidthMicropixel();
	}

	public int getMapGameHeight_Micropixel() {
		AbstractMapGame mg;
		GameModelGeneric gameModel;
		gameModel = getGameModel();
		mg = gameModel == null ? null : gameModel.getMapGameCurrent();
		return mg == null ? 0 : mg.getHeightMicropixel();
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public boolean isWorking() {
		return isWorking;
	}

	public LoaderGeneric getLoader() {
		if (loader == null)
			throw new NullPointerException("Loader null");
		return loader;
	}

	@Override
	public Map<Long, Thread> getPoolThread() {
		return poolThread;
	}

	public AbstractMOLMManager getMolmManager() {
		GameModelGeneric gameModel;
		gameModel = getGameModel();
		return gameModel == null ? null : gameModel.getMolmManagerDelegated();
	}

	public AbstractMOLMManager getMolmManagerDelegated() {
		return getMolmManager();
	}

	public boolean isNeverInitialized() {
		return neverInitialized;
	}

	@Override
	public LoggerMessages getLog() {
		return log;
	}

	public AbstractPlayer getPlayer() {
		GameModelGeneric gameModel;
		gameModel = getGameModel();
		return gameModel == null ? null : gameModel.getPlayer();
	}

	public EnumGameObjectTileImageCollection getAllGameObjectTileImage() {
		return allGameObjectTileImage;
	}

	public AbstractMapGame getMapGame() {
		GameModelGeneric gameModel;
		gameModel = getGameModel();
		return gameModel == null ? null : gameModel.getMapGameCurrent();
	}

	public String getMapGameName() {
		AbstractMapGame mg;
		mg = getMapGame();
		return mg == null ? null : mg.getMapName();
	}

	//

	// TODO SETTERS

	public List<String> getMapsNames() {
		if (mapsNames == null)
			reloadMapsNames();
		return mapsNames;
	}

	public MainController setGameModel(GameModelGeneric gameModel) {
		GameMechanism gameMech;
		gameMech = getGameMechanism();
		if (gameMech != null && gameModel != null) {
			gameMech.setGameModel(gameModel);
			gameModel.resetProgressiveIDGameObject();
		}
		return this;
	}

	public MainController setGameMechanism(GameMechanism gameMechanism) {
		this.gameMechanism = gameMechanism;
		return this;
	}

	@Override
	public MainController setLog(LoggerMessages log) {
		this.log = LoggerMessages.loggerOrDefault(log);
		return this;
	}

	public MainController setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
		return this;
	}

	@Override
	public ThreadsHolder setPoolThread(Map<Long, Thread> poolThread) {
		if (poolThread != this.poolThread) {
			this.poolThread = poolThread;
		}
		return this;
	}

	// public MainController setMolmManager(AbstractMOLMManager molmManager) {
	// // this.molmManager = molmManager;
	// this.setMolmManagerDelegated(molmManager);
	// return this;
	// }

	public void setNeverInitialized(boolean neverInitialized) {
		this.neverInitialized = neverInitialized;
	}

	public void setStillWorking(boolean isStillWorking) {
		this.isWorking = isStillWorking;
	}

	public MainController setLoader(LoaderGeneric l) {
		if (l != null)
			loader = l;
		return this;
	}

	public MainController setAllGameObjectTileImage(EnumGameObjectTileImageCollection allTileImage) {
		this.allGameObjectTileImage = allTileImage;
		return this;
	}

	public MainController setMainGenericGUI(MainGUI mainGenericGUI) {
		this.mainGUI = mainGenericGUI;
		return this;
	}

	public MainController setPlayer(AbstractPlayer player) {
		GameModelGeneric gameModel;
		gameModel = getGameModel();
		if (gameModel != null)
			gameModel.setPlayer(player);
		return this;
	}

	// public MainController setMolmManagerDelegated(AbstractMOLMManager
	// molmManagerDelegated) {
	// GameModelGeneric gameModel;
	// gameModel = getGameModel();
	// if (gameModel != null)
	// gameModel.setMolmManagerDelegated(molmManagerDelegated);
	// return this;
	// }

	//

	// TODO GRAPHIC METHODS

	public void initialize() {
		this.initNONGUIfields();
		this.mainGUI.initGUI();
		allocThreads();
		startThreads();
		System.out.println("Main Initialized");
	}

	public final void initNONGUIfields() {
		setLogSafe(null);
		setNeverInitialized(isWorking = true);
		mainSetterForMOLM = new MainSetterForMOLM(this);
		allLoader = newAllLoader();
		allLoader.loadAll(); // TODO fare caricamenti
		this.allGameObjectTileImage = newEnumGameObjectTileImageCollection(this);
		setGameModel(newGameModelGeneric(this));

		resetPoolThread();
		isPlaying = true;
		//
		continueInitNONGUIfields();
		//
		// allocThreads();
	}

	public abstract void continueInitNONGUIfields();

	/*
	 * public int getNumberMicropixelOnWidth() { return
	 * (this.gp.getMapGameVisualizer().getWidth() * Main.NUM_MICROPIXEL_PER_TILE) /
	 * sizeSquare; }
	 */

	//

	// TODO OTHER METHODS

	// ABSTRACT

	public abstract AllLoader newAllLoader();

	public abstract EnumGameObjectTileImageCollection newEnumGameObjectTileImageCollection(
			MainController mainController);

	public abstract AbstractSaveData newSaveData(GameModelGeneric gameModel);

	public abstract GameModelGeneric newGameModelGeneric(MainController mainController);

	// OTHER

	@Override
	public boolean allocThreads() {
		boolean allocated;
		allocated = this.mainGUI.allocThreads();
		//
		// this.addThreadToPool(threadMusic = new Thread(runnableMusic =
		// newRunnableMusic()));
		// delegated at the match creation
		// allocated |= gameMechanism.allocThreads();
		return allocated;
	}

	@Override
	public void startThreads() {
		this.mainGUI.startThreads();
		// gameMechanism.startThreads();
		if (gameMechanism != null)
			startThread(threadMusic);
	}

	@Override
	public void destroyThreads() {
		pauseThreads();
		this.mainGUI.destroyThreads();
		if (gameMechanism != null)
			gameMechanism.destroyThreads();
	}

	@Override
	public void pauseThreads() {
		// this.isPlaying = false;
		if (gameMechanism != null)
			gameMechanism.pauseThreads();
		this.mainGUI.pauseThreads();
	}

	@Override
	public void resumeThreads() {
		mainGUI.resumeThreads();
		gameMechanism.resumeThreads();
	}

	public void startThread(Thread t) {
		log.logAndPrint(t.getState().name());
		try {
			t.start();
		} catch (Exception e) {
			log.logException(e);
		}
	}

	public GameMechanism newGameMechanismFromType(GameMechanismType gmt) {
		GameMechanism gm;
		gm = gmt.newGameMechanism(this);
		gm.setMainController(this);
		gm.setGameTypeOrdinal(gmt.ordinal());
		return gm;
	}

	public GameMechanism setGameMechanismFromType(GameMechanismType gmt) {
		GameMechanism gm;
		setGameMechanism(gm = newGameMechanismFromType(gmt));
		return gm;
	}

	public void startEditingNewMap(String mapName, GameMechanismType gmt) {
		startEditingNewMap(mapName, gmt, null);
	}

	public void startEditingNewMap(String mapName, GameMechanismType gmt, ObserverMolm obs) {
		startEditingNewMap(mapName, gmt, obs, MOLMManager.DEFAULT_MOLM_WIDTH_MICROPIXEL,
				MOLMManager.DEFAULT_MOLM_HEIGHT_MICROPIXEL);
	}

	public void startEditingNewMap(String mapName, GameMechanismType gmt, ObserverMolm obs, int widthMicropixel,
			int heightMicropixel) {
		GameMechanism gm;
		// AbstractMapGame mg;
		gm = setGameMechanismFromType(gmt);
		// mg =
		gm.setNewMapGame(mapName, obs, widthMicropixel, heightMicropixel);
	}

	/*
	 * @Override public void doOnTimePassing(int millis) { }
	 */

	public boolean saveCurrentMatch() {
		if (loader == null) {
			getLog().log("ERROR: on save, Loader instance null");
			return false;
		}
		/*
		 * if (getPlayer() == null) {
		 * getLog().log("ERROR: on save, Player instance null"); return false; }
		 */
		if (getGameModel() == null) {
			getLog().log("ERROR: on save, GameModel instance null");
			return false;
		}
		save(newSaveData(getGameModel()));
		return true;
	}

	/** Salva la partita corrente */
	protected void save(AbstractSaveData saveData) {
		String error;// , path, savename, extension;
		LoaderGeneric lg;
		if (saveData == null) {
			getLog().log("ERROR: on save, SaveData instance null");
			return;
		}
		lg = getLoader();
		// path = lg.getPathSaves();
		// extension = lg.getSavesExtension();
		// savename = saveData.getSaveName();
		error = lg.saveSaveData(saveData, LoadWriteType.BinaryRaw);
		if (error != null) {
			getLog().log("ERROR while writing the save:n\t");
			getLog().log(error);
		}
	}

	public boolean setMapName(String s) {
		boolean saved;
		AbstractMapGame mg;
		GameModelGeneric gameModel;
		LoaderGeneric lg;
		saved = false;
		lg = loader;
		gameModel = getGameModel();
		if (s != null && gameModel != null && lg != null) {
			mg = getMapGame();
			if (mg != null && (!s.equals(mg.getMapName()))) {
				if (mainGUI.askConfirm("Save map", "Save current map required to change name.\nConfirm?")) {
					LoaderGeneric.deleteFile(lg.getPathMap() + mg.getMapName() + lg.getMapExtension());
					lg.saveMap(mg, LoaderGeneric.LoadWriteType.Memento);
					// TODO PERFORM SAVE
					saved = true;
				}
			}
		}
		return saved;
	}

	public boolean saveCurrentMap() {
		return saveMap(getMapGame());
	}

	public boolean saveMap(AbstractMapGame mg) {
		boolean saved;
		String path, error;
		File folderSave;
		LoaderGeneric load;

		saved = false;
		if (mg != null) {
			folderSave = LoaderGeneric.checkAndCreateFolder(path = (load = getLoader()).getPathMap());
			if (folderSave == null) {
				log("ERROR: on saveCurrentMap, cannot fetch the map path:\n\t");
				log(path);
			} else {
				error = load.saveMap(mg);
				// FileUtilities.writeObject(mapGame.createMemento(),
				// path + mapGame.getMapName() + load.getMapExtension());
				if (error != null)
					log(error);
				else
					saved = true;
			}
		}
		return saved;
	}

	/** Load the map and set it as the current one */
	public AbstractMapGame loadMap(String mapName) {

		return null;
	}

	// TODO MOLM OPERATIONS

	public boolean addGameObject(String name, int xMicropixelLocation, int yMicropixelLocation) {
		if (name == null)
			return false;
		return addGameObject(//
				this.allGameObjectTileImage.getGameObjectInMapByName(this, name)//
				, xMicropixelLocation, yMicropixelLocation);
	}

	public boolean addGameObject(String enumName, String objectName, int xMicropixelLocation, int yMicropixelLocation) {
		if (enumName == null || objectName == null)
			return false;
		return addGameObject(//
				this.allGameObjectTileImage.getGameObjectInMapByEnumnameImagename(this, enumName, objectName)//
				, xMicropixelLocation, yMicropixelLocation);
	}

	public boolean addGameObject(GameObjectInMap goim, int xMicropixelLocation, int yMicropixelLocation) {
		ShapeSpecification ss;
		if (goim == null)
			return false;
		ss = goim.getShapeSpecification();
		if (ss != null)
			ss.setLeftBottomCorner(xMicropixelLocation, yMicropixelLocation);
		return addGameObject(goim);
	}

	/**
	 * Calls {@link #addGameObject(GameObjectInMap, boolean)} giving
	 * <code>false</code> to "removeHittingOWID".
	 */
	public boolean addGameObject(GameObjectInMap goim) {
		return addGameObject(goim, false);
	}

	public boolean addGameObject(GameObjectInMap goim, boolean removeHittingOWID) {
		boolean ok;
		// AbstractMOLMManager mm;
		GameModelGeneric gm;
		ok = false;
		if (goim != null) {
			gm = this.getGameModel();
			// mm = getMolmManager();
			if (gm != null) {
				// if goim has no ID jet, then set it
				if (goim.getGameObjectID() == null)
					goim.setGameObjectID(gm.getAndNextIDGameObject());
				gm.addToMOLM(goim, removeHittingOWID);
				ok = true;
			}
		}
		return ok;
	}

	// TODO method stuffs

	public String setMainTo(Object someInstance) {
		if (someInstance == null)
			return "ERROR: on " + this.getClass().getSimpleName() + ".setMainTo, given instance is null";
		if (!(someInstance instanceof MainHolder))
			return "ERROR: on " + this.getClass().getSimpleName() + ".setMainTo, given instance is not a MainHolder";
		((MainHolder) someInstance).setMain(this);
		return null;
	}

	public void log(String text) {
		log.log(text);
	}

	public void terminateWithMessage(String s) {
		System.err.println(s);
		log(s);
		Thread.dumpStack();
		System.exit(-1);
	}

	//

	// protected lesser methods

	protected void reloadMapsNames() {
		int i, len;
		String path, filename;
		File folderSave, files[];
		LoaderGeneric load;

		path = (load = getLoader()).getPathMap();
		folderSave = LoaderGeneric.checkAndCreateFolder(path);
		if (folderSave != null) {
			if (mapsNames == null)
				this.mapsNames = new LinkedList<>();
			else
				mapsNames.clear();
			files = folderSave.listFiles();
			if (files != null && (len = files.length) > 0) {
				i = -1;
				while(++i < len) {
					// recycle
					folderSave = files[i];
					if ((filename = folderSave.getName()).endsWith(load.getMapExtension())) {
						mapsNames.add(filename.substring(0, filename.lastIndexOf('.')));
					}
				}
			}
		}
	}

	//

	// TODO CLASS

	//

	// classi stupide

	public static class MainSetterForMOLM implements DoSomethingWithNode {
		private static final long serialVersionUID = 2346525389526003L;
		MainController main;

		MainSetterForMOLM(MainController ma) {
			main = ma;
		}

		@Override
		public Object doOnItem(AbstractMatrixObjectLocationManager molm, ObjectWithID item, int x, int y) {
			// MainHolder oim;
			if (item != null && (item instanceof MainHolder))
				((MainHolder) item).setMain(main);
			return null;
		}
	}

	/*
	 * public static class MainSetterForMOLM_ResetBeforeEachRun extends
	 * MainSetterForMOLM implements DoSomethingWithNode { //RedBlackTree<Integer,
	 * ObjectWithID> visitedOb public
	 * MainSetterForMOLM_ResetBeforeEachRun(MainGenericController ma) { super(ma);
	 * // TODO Auto-generated constructor stub } }
	 */

}