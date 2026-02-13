package common.gui;

import java.awt.AWTException;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import common.EnumGameObjectTileImageCollection;
import common.GameObjectInMap;
import common.abstractCommon.AbstractMapGame;
import common.abstractCommon.MainController;
import common.abstractCommon.behaviouralObjectsAC.GraphComponentSizeAdaptive;
import common.abstractCommon.behaviouralObjectsAC.GraphicComponentUpdating;
import common.abstractCommon.behaviouralObjectsAC.ObjectGuiUpdatingOnFrame;
import common.abstractCommon.referenceHolderAC.FrameHolder;
import common.abstractCommon.referenceHolderAC.LoggerMessagesHolder;
import common.abstractCommon.referenceHolderAC.MainHolder;
import common.abstractCommon.referenceHolderAC.ThreadsHolder;
import common.mainTools.LoggerMessages;
import tools.RedBlackTree;

public abstract class MainGUI
		implements ObjectGuiUpdatingOnFrame, FrameHolder, ThreadsHolder, LoggerMessagesHolder, MainHolder, Observer {
	private static final long serialVersionUID = -65015800874L;

	/**
	 * Defines how the GUI must draw all {@link GameObjectInMap} instances:
	 * <ul>
	 * <li>CycleOverAllMolmsMicropixel</li>
	 * <li>BufferStrategy</li>
	 * <li>DoubleBufferWithBufferedImage</li>
	 * </ul>
	 */
	public static enum DrawGameObjectMethod {
		CycleOverAllMolmsMicropixel, BufferStrategy, DoubleBufferWithBufferedImage
	}

	public static enum SectionsShownOnCardLayout {
		InitialMenu, Option, NewSaves, ListSavesToLoad, MapEditor
	}

	public static final int JLFIN_WIDTH_MIN = 1000, JLFIN_HEIGHT_MIN = 650//
	// , LOG_MICROPIXEL_PER_TILE = Methods.log2(MICROPIXEL_EACH_TILE) // 3 // 4
			, PIXEL_EACH_MICROPIXEL = 10

			, MIN_TILE_X_AXIS = 16, MIN_TILE_Y_AXIS = 8
			// MAX_TILE_X_AXIS = 128, MAX_TILE_AXIS = 96,

			, MIN_MICROPIXEL_Y_AXIS = MIN_TILE_Y_AXIS * MainController.MICROPIXEL_EACH_TILE,
			MIN_MICROPIXEL_X_AXIS = MIN_TILE_X_AXIS * MainController.MICROPIXEL_EACH_TILE
			//

			, /***/
			MILLISECONDS_FOR_GET_IMAGE_AND_REPAINT = 50, MIN_MILLIS_BETWEEN_EACH_REPAINT = 15,
			MAX_MILLISECONDS_EACH_PAINT_FRAME = 25;

	protected MainGUI() {
		drawMethod = DrawGameObjectMethod.DoubleBufferWithBufferedImage;
		pixelEachMicropixel = PIXEL_EACH_MICROPIXEL;
		tilesXAxisOnGameMapView = MIN_TILE_X_AXIS * MainController.MICROPIXEL_EACH_TILE;
		tilesYAxisOnGameMapView = MIN_TILE_Y_AXIS * MainController.MICROPIXEL_EACH_TILE;
		log = LoggerMessages.LOGGER_DEFAULT;
	}

	public MainGUI(MainController mgc) {
		this();
		this.setMain(mgc);
	}

	// NON GUI FIELDS
	MainController mainController;
	int currentFrame, lastFrame, pixelEachMicropixel//
			, tilesYAxisOnGameMapView, tilesXAxisOnGameMapView//
			// , mapGameWidth_Tile = MIN_TILE_X_AXIS, mapGameHeight_Tile =
			// MIN_TILE_Y_AXIS//
			// , mapGameWidth_GraphicPixel, mapGameHeight_GraphicPixel//
			, xMouseMicropixel, yMouseMicropixel, xMouseRealPixel, yMouseRealPixel;
	Thread threadPaint, threadMapGameViewUpdater;
	RunnerPainter runnerPainter;
	LoggerMessages log;

	// GUI COMPONENTS
	public DrawGameObjectMethod drawMethod;
	// transient
	JFrame_Repaint fin;
	JPanel jpCardsLayoutHolder, jpInitialMenu, jpGoToSavesListSection;
	JPanelWithBackButton jpNewLoadSection;
	JButton jbStartNewSave, jbToSavesListNewSave, jbOpenMapEditor;
	JScrollPane jspFin, jspInitialMenu;
	JTabbedPane jtp;
	/** Such as menus, editors, ecc */
	GraphComponentSizeAdaptive[] allHugeComponents;
	GraphicComponentUpdating[] allUpdatingComponents;
	// List<JComponent> hugeSectionsShownOnMutex;
	CardLayout cardLayout;
	MapEditor mapEditor;
	MapGameView mapGameView;
	// to optimize game draw on MapGameView we need this
	// Canvas canvas;
	GraphicsConfiguration graphicsConfiguration;
	BufferStrategy strategy;

	//

	// TODO GETTER

	@Override
	public MainController getMain() {
		return getMainController();
	}

	public DrawGameObjectMethod getDrawMethod() {
		return drawMethod;
	}

	public MainController getMainController() {
		return mainController;
	}

	public Thread getThreadPaint() {
		return threadPaint;
	}

	@Override
	public RedBlackTree<Long, Thread> getPoolThread() {
		return mainController == null ? null : mainController.getPoolThread();
	}

	@Override
	public LoggerMessages getLog() {
		return log;
	}

	/** Returns the one stored by the Model */
	public AbstractMapGame getMapGame() {
		return this.mainController.getMapGame();
		// mapGameView_Battle == null ? null : mapGameView_Battle.getMapGame();
	}

	// gui-related

	public EnumGameObjectTileImageCollection getAllGameObjectTileImage() {
		return mainController.getAllGameObjectTileImage();
	}

	@Override
	public int getCurrentFrame() {
		return currentFrame;
	}

	public int getMicropixelEachTile() {
		return MainController.MICROPIXEL_EACH_TILE;
	}

	public int getPixelEachMicropixel() {
		return pixelEachMicropixel;
	}

	public int getxMouseMicropixel() {
		return xMouseMicropixel;
	}

	public int getyMouseMicropixel() {
		return yMouseMicropixel;
	}

	public int getTilesYAxisOnGameMapView() {
		return tilesYAxisOnGameMapView;
	}

	public int getTilesXAxisOnGameMapView() {
		return tilesXAxisOnGameMapView;
	}

	public int getMicropixelYAxisOnGameMapView() {
		return tilesYAxisOnGameMapView * MainController.MICROPIXEL_EACH_TILE;
	}

	public int getMicropixelXAxisOnGameMapView() {
		return tilesXAxisOnGameMapView * MainController.MICROPIXEL_EACH_TILE;
	}

	/**
	 * This is the one used for the real game, not the one used by the editor
	 */
	public MapGameView getMapGameView() {
		return mapGameView;
	}

	public JPanel getJpFin() {
		return jpCardsLayoutHolder;
	}

	public JFrame_Repaint getFin() {
		return fin;
	}

	public MapEditor getMapEditor() {
		return mapEditor;
	}

	public Dimension getMapGlassDimension() {
		Dimension d;
		// per ora, ritorniamo le dimensioni della finestra ..
		// TODO aggiornare getMapGlassDimension
		d = fin.getSize();
		d.height -= 25;
		return d;
	}

	public int getxMouseRealPixel() {
		return xMouseRealPixel;
	}

	public int getyMouseRealPixel() {
		return yMouseRealPixel;
	}

	public RunnerPainter getRunnerPainter() {
		return runnerPainter;
	}

	public GraphComponentSizeAdaptive[] getAllHugeComponents() {
		return allHugeComponents;
	}

	public GraphicComponentUpdating[] getAllUpdatingComponents() {
		return allUpdatingComponents;
	}

	@Override
	public int getLastFrame() {
		return this.lastFrame;
	}

	public int getXMouseMicropixel() {
		return xMouseMicropixel;
	}

	public int getYMouseMicropixel() {
		return yMouseMicropixel;
	}

	public GraphicsConfiguration getGraphicsConfiguration() {
		return graphicsConfiguration;
	}

	public BufferStrategy getStrategy() {
		return strategy;
	}

	//

	// TODO SETTER

	@Override
	public MainGUI setMain(MainController mainGenericController) {
		return setMainController(mainGenericController);
	}

	public MainGUI setMainController(MainController mainGenericController) {
		if (mainGenericController != null) {
			this.mainController = mainGenericController;
			if (runnerPainter != null) this.runnerPainter.setMainGUI(this);
			if (mapEditor != null) this.mapEditor.setMain(mainGenericController);
		}
		return this;
	}

	public MainGUI setDrawMethod(DrawGameObjectMethod drawMethod) {
		this.drawMethod = drawMethod;
		return this;
	}

	public MainGUI setAllGameObjectTileImage(EnumGameObjectTileImageCollection allTileImage) {
		this.mainController.setAllGameObjectTileImage(allTileImage);
		return this;
	}

	@Override
	public ThreadsHolder setPoolThread(RedBlackTree<Long, Thread> poolThread) {
		if (mainController != null) mainController.setPoolThread(poolThread);
		return this;
	}

	public MainGUI setXMouseMicropixel(int xMouseMicropixel) {
		this.xMouseMicropixel = xMouseMicropixel;
		setxMouseRealPixel_FromMicropixel(xMouseMicropixel);
		return this;
	}

	public MainGUI setYMouseMicropixel(int yMouseMicropixel) {
		this.yMouseMicropixel = yMouseMicropixel;
		setxMouseRealPixel_FromMicropixel(yMouseMicropixel);
		return this;
	}

	@Override
	public LoggerMessagesHolder setLog(LoggerMessages log) {
		this.log = LoggerMessages.loggerOrDefault(log);
		if (mainController != null) mainController.setLog(log);
		if (mapEditor != null) mapEditor.setLog(log);
		return this;
	}

	/** update every {@link MapGameView}'s reference on the calling method. */
	public MainGUI setMapGame(AbstractMapGame mapGame) {
		// if (mapGameView_Battle != null)
		// this.mapGameView_Battle.setMapGame(mapGame);
		this.mainController.getGameModel().setMapGameCurrent(mapGame);
		return this;
	}

	// gui-related

	public MainGUI setCurrentFrame(int currentFrame) {
		if (currentFrame >= 0) this.currentFrame = currentFrame;
		return this;
	}

	public MainGUI setPixelEachMicropixel(int pixelEachMicropixel) {
		if (pixelEachMicropixel > 0) {
			this.pixelEachMicropixel = pixelEachMicropixel;
		}
		return this;
	}

	public MainGUI setTilesXAxisOnGameMapView(int tilesXAxisOnGameMapView) {
		if (tilesYAxisOnGameMapView > MIN_TILE_X_AXIS) {
			this.tilesXAxisOnGameMapView = tilesXAxisOnGameMapView;
			recalculatePixelRelatedSizes(pixelEachMicropixel, tilesXAxisOnGameMapView, tilesYAxisOnGameMapView);
		}
		return this;
	}

	public MainGUI setTilesYAxisOnGameMapView(int tilesYAxisOnGameMapView) {
		if (tilesYAxisOnGameMapView > MIN_TILE_Y_AXIS) {
			this.tilesYAxisOnGameMapView = tilesYAxisOnGameMapView;
			recalculatePixelRelatedSizes(pixelEachMicropixel, tilesXAxisOnGameMapView, tilesYAxisOnGameMapView);
		}
		return this;
	}

	public MainGUI setxMouseRealPixel(int xMouseRealPixel) {
		this.xMouseRealPixel = xMouseRealPixel;
		return this;
	}

	public MainGUI setyMouseRealPixel(int yMouseRealPixel) {
		this.yMouseRealPixel = yMouseRealPixel;
		return this;
	}

	public MainGUI setxMouseRealPixel_FromMicropixel(int xMouseMicroPixel) {
		return setxMouseRealPixel(scaleToRealPixel(xMouseMicroPixel));
	}

	public MainGUI setyMouseRealPixel_FromMicropixel(int yMouseMicroPixel) {
		return setyMouseRealPixel(scaleToRealPixel(yMouseRealPixel));
	}

	public MainGUI setRunnerPainter(RunnerPainter runnerPainter) {
		this.runnerPainter = runnerPainter;
		return this;
	}

	public MainGUI setAllHugeComponents(GraphComponentSizeAdaptive[] allHugeComponents) {
		this.allHugeComponents = allHugeComponents;
		return this;
	}

	public MainGUI setAllUpdatingComponents(GraphicComponentUpdating[] allUpdatingComponents) {
		this.allUpdatingComponents = allUpdatingComponents;
		return this;
	}

	public MainGUI setThreadPaint(Thread threadPaint) {
		this.threadPaint = threadPaint;
		return this;
	}

	public MainGUI setMapEditor(MapEditor mapEditor) {
		/*
		 * if (this.mapEditor != null && hugeSectionsShownOnMutex != null) {
		 * hugeSectionsShownOnMutex.remove(this.mapEditor); }
		 */
		if ((this.mapEditor = mapEditor) != null) {
			mapEditor.setMain(this.getMainController());
			// if (hugeSectionsShownOnMutex != null)
			addSectionToCardLayout(mapEditor, SectionsShownOnCardLayout.MapEditor.name());
		}
		return this;
	}

	@Override
	public ObjectGuiUpdatingOnFrame setLastFrame(int frame) {
		this.lastFrame = frame;
		return this;
	}
	//

	// TODO OTHER METHODS

	// INIT

	/** Alloc all components BUT it doesn't add to the JFrame */
	public final void initGUI() {
		setLog(new LoggerMessagesJScrollPane());

		fin = new JFrame_Repaint(getMainController().getGameName());
		fin.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		fin.addWindowListener(getNewWindowListener_OnClosing());
		fin.setLocation(400, 100);
		jtp = new JTabbedPane();
		fin.add(jtp);

		jpCardsLayoutHolder = new JPanel(cardLayout = new CardLayout());
		jtp.addTab("Game", jpCardsLayoutHolder);
		jtp.addTab("Log", (LoggerMessagesJScrollPane) log);

		fin.addComponentListener(new ComponentListener_DoOnResize() {

			@Override
			public void componentResized(ComponentEvent e) {
				Dimension d;
				d = fin.getSize();
				d.height -= 24;
				jtp.setSize(d);
			}

		});

		// allocGUIComponent();
		// composeGUIComponent();
		// addGUIComponentBehaviour();

		graphicsConfiguration = fin.getGraphicsConfiguration();
		if (graphicsConfiguration == null) graphicsConfiguration = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice().getDefaultConfiguration();
		try {
			fin.createBufferStrategy(2, graphicsConfiguration.getBufferCapabilities());
			strategy = fin.getBufferStrategy();
		} catch (AWTException e1) {
			// e1.printStackTrace();
			getLog().logException(e1);
		}
		// require the strategy
		if (strategy == null)
			this.mainController.terminateWithMessage("No strategy created when request. Brutal exit.");

		// create main menus
		jpInitialMenu = new JPanel(GUIConstantsUtilities.INFINITE_ROWS);
		jspInitialMenu = new JScrollPane(jpInitialMenu);
		jspInitialMenu.setPreferredSize(new Dimension(200, 450));
		jspInitialMenu.setViewportView(jpInitialMenu);
		jspInitialMenu.setVisible(true);
		jpCardsLayoutHolder.add(jspInitialMenu);
		addSectionToCardLayout(jspInitialMenu, SectionsShownOnCardLayout.InitialMenu);

		jbStartNewSave = createJButtonShowingComponent(SectionsShownOnCardLayout.NewSaves, "New Match");
		jbToSavesListNewSave = createJButtonShowingComponent(SectionsShownOnCardLayout.ListSavesToLoad, "Load");
		jbOpenMapEditor = createJButtonShowingComponent(SectionsShownOnCardLayout.MapEditor, "Map Editor");
		jpInitialMenu.add(jbStartNewSave);
		jpInitialMenu.add(jbToSavesListNewSave);
		jpInitialMenu.add(jbOpenMapEditor);

		// load section
		jpNewLoadSection = new JPanelWithBackButton(this, SectionsShownOnCardLayout.InitialMenu);
		addSectionToCardLayout(jpNewLoadSection, SectionsShownOnCardLayout.NewSaves);
		jpGoToSavesListSection = new JPanelWithBackButton(this, SectionsShownOnCardLayout.InitialMenu);
		addSectionToCardLayout(jpGoToSavesListSection, SectionsShownOnCardLayout.ListSavesToLoad);
		mapGameView = newMapGameView_TheRealGame(this);
		mapEditor = newMapEditor(this);
		mapEditor.setMain(this.mainController);
		addSectionToCardLayout(mapEditor, SectionsShownOnCardLayout.MapEditor);
		mapEditor.initGUI();

		//
		continueInitGUI();
		//

		fin.setSize(800, 500);
		fin.setVisible(true);
	}

	public void allocGUIComponent() {

	}

	public void composeGUIComponent() {

	}

	public void addGUIComponentBehaviour() {

	}

	// TODO ABSTRACT

	public abstract void continueInitGUI();

	public abstract MapEditor newMapEditor(MainGUI mainGui);

	public abstract MapGameView newMapGameView_TheRealGame(MainGUI mainGui);

	public WindowListener getNewWindowListener_OnClosing() {
		return new WindowAdapter_DoOnClose(getMainController());
	}

	@Override
	public void update(Observable o, Object arg) {
	}

	public void showSectionCard(GuiCardComponent gcc) {
		cardLayout.show(jpCardsLayoutHolder, gcc.getNameCard());
	}

	public void registerSectionCard(GuiCardComponent gcc) {
		jpCardsLayoutHolder.add(gcc.getComponent(), gcc.getNameCard());
	}

	// OTHER

	// config

	public void setInitialConfiguration() {
		System.setProperty("sun.java2d.transaccel", "True");
		// System.setProperty("sun.java2d.trace", "timestamp,log,count");
		// System.setProperty("sun.java2d.opengl", "True");
		System.setProperty("sun.java2d.d3d", "True");
		System.setProperty("sun.java2d.ddforcevram", "True");
	}

	// TODO MANAGING METHOD

	@Override
	public boolean allocThreads() {
		boolean allocated;
		allocated = false;
		if (threadPaint == null) {
			addThreadToPool(threadPaint = new Thread(runnerPainter = new RunnerPainter(this)));
			allocated |= true;
		}
		// delegated on map creation
		/*
		 * if (this.mapGameView.allocThreads()) {
		 * addThreadToPool(threadMapGameViewUpdater = new
		 * Thread(mapGameView.getRunnableMapGameViewUpdater())); allocated |=
		 * true; }
		 */
		return allocated;
	}

	@Override
	public void destroyThreads() {
		pauseThreads();
		if (threadPaint != null) {
			removeThreadFromPool(threadPaint);
			threadPaint = null;
			runnerPainter = null;
		}
	}

	@Override
	public void startThreads() {
		allocThreads();
		if (threadPaint != null) mainController.startThread(threadPaint);
		if (threadMapGameViewUpdater != null) mainController.startThread(threadMapGameViewUpdater);
	}

	@Override
	public void pauseThreads() {
		// if (threadPaint == null) this.m
		// nothing to do : this threads depends on MainGenericController
		mapGameView.getRunnableMapGameViewUpdater().suspendRunnable();
	}

	@Override
	public void resumeThreads() {
		mapGameView.getRunnableMapGameViewUpdater().resumeRunnable();
	}

	// TODO GUI-METHOD

	protected void increaseCurrentFrame() {
		currentFrame++;
	}

	/**
	 * Re-compute the size of the {@link MapGameView}'s panel and, if
	 * {@link #getPixelEachMicropixel()} is changed due to the first parameter,
	 * then resize all images stored in {@link #getAllGameObjectTileImage()}.
	 */
	protected void recalculatePixelRelatedSizes(int pixelEachMicropixel, int tilesXAxisOnGameMapView,
			int tilesYAxisOnGameMapView) {
		boolean pemChanged;
		EnumGameObjectTileImageCollection egotic;
		pemChanged = this.pixelEachMicropixel != pixelEachMicropixel;
		// modify the View..
		if (pemChanged && this.tilesXAxisOnGameMapView != tilesXAxisOnGameMapView
				&& this.tilesYAxisOnGameMapView != tilesYAxisOnGameMapView) {
			this.pixelEachMicropixel = pixelEachMicropixel;
			this.tilesXAxisOnGameMapView = tilesXAxisOnGameMapView;
			this.tilesYAxisOnGameMapView = tilesYAxisOnGameMapView;
			this.mapGameView.setSizeJPanelGlass(tilesXAxisOnGameMapView * pixelEachMicropixel,
					tilesYAxisOnGameMapView * pixelEachMicropixel);
		}
		// ..then modify images
		if (pemChanged) {
			// scale the ones in cache (the enumerations' held) ..
			egotic = getAllGameObjectTileImage();
			if (egotic != null && egotic.holdsSomeEnum()) {
				// for (AbstractEnumGameObjectTileImage egoti : egotic) {}
				egotic.forEachEnumElement((imageName, enumElement) -> {
					enumElement.scaleImageSizeTo(pixelEachMicropixel);
				});
			}
			// .. and then the ones jet created
			if (mapGameView != null) {
				mapGameView.objectsInMap.forEach((id, goimv) -> {
					goimv.scaleImagesToShapeSpecAndSizeSquare();
				});
			}
		}
	}

	/**
	 * Add a GUI section panel (like a menu, setting option, the panel showing
	 * the saves to load, the character-classes or the map to play) to the
	 * collection.
	 */
	protected void addSectionToCardLayout(JComponent jc, SectionsShownOnCardLayout key) {
		addSectionToCardLayout(jc, key.name());
	}

	/**
	 * See
	 * {@link #addSectionToCardLayout(JComponent, SectionsShownOnCardLayout)}.
	 */
	protected void addSectionToCardLayout(JComponent jc, String key) {
		if (jc != null) {
			jpCardsLayoutHolder.add(jc, key);
		}
	}

	// public void showHugeSections(JComponent jc,String key) {
	protected void showHugeSections(SectionsShownOnCardLayout s) {
		showHugeSections(s.name());
	}

	protected void showHugeSections(String key) {
		// if (jc != null) {
		/*
		 * hideAllHugeSectionShownOnMutex(); jc.setVisible(true);
		 * jpCardsLayoutHolder.add(jc); jpCardsLayoutHolder.repaint();
		 */
		cardLayout.show(jpCardsLayoutHolder, key);
		jpCardsLayoutHolder.repaint();
		// }
	}

	/*
	 * public void hideAllHugeSectionShownOnMutex() { if
	 * (hugeSectionsShownOnMutex != null && hugeSectionsShownOnMutex.size() > 0)
	 * { jpCardsLayoutHolder.removeAll(); for (JComponent jc :
	 * hugeSectionsShownOnMutex) { if (jc != null) {
	 * jpCardsLayoutHolder.remove(jc); jc.setVisible(false); } } } }
	 */

	protected void onHideMapEditor() {
		// showHugeSections(SectionsShownOnCardLayout.InitialMenu);
		// mapGameView_Battle.observeMapGame(null); // release memory
		showHugeSections(SectionsShownOnCardLayout.InitialMenu);
	}

	protected void onShowingMapEditor() {
		// stuffs to do here
		mapEditor.doOnShow();
	}

	protected void setCoordinatesMouseOnMapMicropixel(MouseEvent me) {
		setCoordinatesMouseOnMapMicropixel(me.getX(), me.getY());
	}

	protected void setCoordinatesMouseOnMapMicropixel(int newxPixelGraphic, int newyPixelGraphic) {
		setXMouseMicropixel((newxPixelGraphic * MainController.MICROPIXEL_EACH_TILE) / this.getMicropixelEachTile());
		setYMouseMicropixel((newyPixelGraphic * MainController.MICROPIXEL_EACH_TILE) / this.getMicropixelEachTile());
		// System.out.println("mouse moved on pixel: (" + newxPixelGraphic + ","
		// + newyPixelGraphic + ")");
	}

	/**
	 * Return the amount of real-graphic pixel of each tile.<br>
	 * Note: Store the result in a variable to cache it.
	 */
	public int getPixelEachTile() {
		return pixelEachMicropixel * MainController.MICROPIXEL_EACH_TILE;
	}

	// TODO UPDATE GUI AND ANIMATION

	@Override
	public void performAnimation(int milliseconds) {
		if (allUpdatingComponents != null && allUpdatingComponents.length > 0)
			for (GraphicComponentUpdating g : allUpdatingComponents)
			if (g != null) g.updateGUI();
		if (mapEditor != null && mapEditor.isVisible()) mapEditor.updateGui(milliseconds);
		updateGameAnimations(milliseconds);
	}

	/**
	 * Update all objects present in game (like
	 * {@link ObjectGuiUpdatingOnFrame}) having some kind of animation.
	 */
	protected void updateGameAnimations(int milliseconds) {
		MapGameView mgv;
		mgv = getMapGameView();
		if (mgv != null) {
			mgv.updateGui(milliseconds);
		}
	}

	// SILLY METHOD

	public int scaleToRealPixel(int dimensionMicropixel) {
		return dimensionMicropixel * this.pixelEachMicropixel;
	}

	@Deprecated
	public int scaleToRealPixel_OLD(int dimensionMicropixel) {
		return scaleToRealPixel(dimensionMicropixel, getPixelEachTile());
	}

	protected static int scaleToRealPixel(int dimensionMicropixel, int numberPixelEachTile) {
		return (dimensionMicropixel * numberPixelEachTile) / MainController.MICROPIXEL_EACH_TILE;
	}

	public void showMessage(String text) {
		showMessage(text, "No title");
	}

	public void showMessage(String text, String title) {
		showMessage(text, title, JOptionPane.INFORMATION_MESSAGE);
	}

	public void showMessage(String text, String title, int messageType) {
		JOptionPane.showMessageDialog(fin, text, title, messageType);
		log(text);
	}

	public boolean askConfirm(String title, String question) {
		return JOptionPane.showConfirmDialog(fin, question, title, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
	}

	public void log(String text) {
		getMainController().log(text);
	}

	public JButton createJButtonShowingComponent(SectionsShownOnCardLayout cardLayoutSection, String textButton) {
		return createJButtonShowingComponent(this.cardLayout, jpCardsLayoutHolder, cardLayoutSection.name(),
				textButton);
	}

	// STATIC METHODS

	public static JButton createJButtonShowingComponent(CardLayout cl, JComponent parent, String cardLayoutSectionName,
			String textButton) {
		JButton jb;
		jb = null;
		if (cl != null && parent != null && cardLayoutSectionName != null && textButton != null) {
			jb = new JButton(textButton);
			jb.addActionListener(new ActionListenerJComponentShowingForCardLayout(cl, parent, cardLayoutSectionName));
			jb.setVisible(true);
		}
		return jb;
	}

	//

	// TODO CLASS

	public static class WindowAdapter_DoOnClose extends WindowAdapter {
		MainController m;

		WindowAdapter_DoOnClose(MainController mm) {
			m = mm;
		}

		@Override
		public void windowClosing(WindowEvent e) {
			// m.greatestObjectInMapProgressiveID = ObjectInMap.progressiveID;
			m.setStillWorking(false);
			/*
			 * System.out.println("configuration saved? : " +
			 * Loader.saveConfigurations(m));
			 */
		}
	}

	public static class JFrame_Repaint extends JFrame {

		private static final long serialVersionUID = -512303007489760L;
		public boolean canPaint = true;

		public JFrame_Repaint() throws HeadlessException {
			this("");
		}

		public JFrame_Repaint(String title) throws HeadlessException {
			super(title);
		}

		@Override
		public void paint(Graphics g) {
			if (canPaint) {
				super.paint(g);
			}
		}

		@Override
		public void paintComponents(Graphics g) {
			if (canPaint) {
				super.paintComponents(g);
			}
		}
	}

	public static class ThreadPainter extends Thread {
		// private static final long serialVersionUID = 5640198491L;

		public ThreadPainter(MainGUI m) {
			this(m, new RunnerPainter(m));
		}

		private ThreadPainter(MainGUI m, RunnerPainter rg) {
			super(m.runnerPainter = rg);
			this.rg = rg;
		}

		RunnerPainter rg;

		/*
		 * ThreadGame(MainGeneric m, Runnable r) { super(r); }
		 */
		public MainGUI getMainGUI() {
			return rg.getMainGUI();
		}

		public ThreadPainter setMainGUI(MainGUI main) {
			this.rg.setMainGUI(main);
			return this;
		}
	}

	public static class RunnerPainter implements Runnable {
		// private static final long serialVersionUID = 5640198490L;
		public boolean timeResetNeeded;
		// int lastFramesPerformed;
		long lastTime/* , millisLastFrame */;
		MainGUI mgg;
		MainController mgc;

		RunnerPainter(MainGUI mm) {
			setMainGUI(mm);
			resetTime();
		}

		public MainGUI getMainGUI() {
			return mgg;
		}

		public RunnerPainter setMainGUI(MainGUI main) {
			this.mgg = main;
			mgc = mgg.getMainController();
			return this;
		}

		/** Ritorna l'ammontare di frames calcolati nell'ultimo secondo */
		public int getLastFramePerformed() {
			return mgg.lastFrame;
			// lastFramesPerformed;
		}

		public void resetTime() {
			mgg.setCurrentFrame(mgg.lastFrame = 0);
			// deltaTimePassed = MILLISECONDS_STANDARD_WAIT_AFTER_RESUME;
			// millisLastFrame =
			lastTime = System.currentTimeMillis();
		}

		@Override
		public void run() {
			int deltaTimePassed, millisFrameCounterRefresh;
			long currentTime;

			System.out.println("Thread Game GUI painter started : isStillWorking:" + mgc.isWorking());
			while (mgc == null || mgc.isWorking()) {

				millisFrameCounterRefresh = 0;
				try {
					if (mgc != null) {
						while (mgc.isPlaying()) {

							currentTime = System.currentTimeMillis();
							if (timeResetNeeded) {
								timeResetNeeded = false;
								Thread.sleep(deltaTimePassed = MainController.MILLISECONDS_STANDARD_WAIT_AFTER_RESUME);
								// millisLastFrame =
								lastTime = currentTime;// System.currentTimeMillis();

								mgg.setCurrentFrame(mgg.lastFrame = 1);
							} else {
								deltaTimePassed = (int) (currentTime - lastTime);
								lastTime = currentTime;
							}

							if (deltaTimePassed < MainController.MILLISECONDS_STANDARD_WAIT_AFTER_RESUME) {
								Thread.sleep(deltaTimePassed = MainController.MILLISECONDS_STANDARD_WAIT_AFTER_RESUME);
								lastTime = System.currentTimeMillis();
							} else if (deltaTimePassed > MAX_MILLISECONDS_EACH_PAINT_FRAME)
								deltaTimePassed = MAX_MILLISECONDS_EACH_PAINT_FRAME;
							//

							// at the end of the stuffs, calculate FPS
							if ((millisFrameCounterRefresh += deltaTimePassed) >= 1000) {
								millisFrameCounterRefresh %= 1000;
								mgg.lastFrame = mgg.getCurrentFrame();
								mgg.fin.setTitle(mgc.getGameName() + " : " + mgg.lastFrame);
								// frames = 0
								mgg.setCurrentFrame(0);
							} else {
								// frames++;
								mgg.increaseCurrentFrame();
								// setCurrentFrame(mgg.getCurrentFrame() + 1);
							}
							//

							// update gui
							// mgc.act(deltaTimePassed);
							mgg.updateGui(deltaTimePassed);
						} // end of cycle
					}
					Thread.sleep(500);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	public static class ActionListenerJComponentShowingForCardLayout implements ActionListener {
		public ActionListenerJComponentShowingForCardLayout(CardLayout cl, JComponent parent,
				String cardLayoutSectionName) {
			super();
			this.cl = cl;
			this.parent = parent;
			this.cardLayoutSectionName = cardLayoutSectionName;
		}

		CardLayout cl;
		JComponent parent;
		String cardLayoutSectionName;

		@Override
		public void actionPerformed(ActionEvent e) {
			cl.show(parent, cardLayoutSectionName);
		}
	}

	@Deprecated
	public static class ActionListenerJComponentShowing_OLD implements ActionListener {
		MainGUI mgg;
		// JComponent jc;
		SectionsShownOnCardLayout section;

		ActionListenerJComponentShowing_OLD(MainGUI mgg, SectionsShownOnCardLayout section) {
			this.mgg = mgg;
			this.section = section;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			mgg.showHugeSections(section);
		}
	}

	public static class JPanelWithBackButton extends JPanel {
		private static final long serialVersionUID = -5409988915L;

		public JPanelWithBackButton(MainGUI mgg, SectionsShownOnCardLayout sectionToGoBack) {
			super();
			this.mgg = mgg;
			this.sectionToGoBack = sectionToGoBack;
			createGUI();
		}

		public JPanelWithBackButton(MainGUI mgg, SectionsShownOnCardLayout sectionToGoBack, LayoutManager layout) {
			super(layout);
			this.mgg = mgg;
			this.sectionToGoBack = sectionToGoBack;
			createGUI();
		}

		MainGUI mgg;
		SectionsShownOnCardLayout sectionToGoBack;
		JButton jbBack;

		//

		final void createBasicGUI() {
			jbBack = newJButton();
			jbBack.addActionListener(new ActionListenerJComponentShowingForCardLayout(mgg.cardLayout,
					mgg.jpCardsLayoutHolder, sectionToGoBack.name()));
			this.add(jbBack);
			jbBack.setLocation(0, 0);
			jbBack.setPreferredSize(new Dimension(50, 35));
		}

		final void createGUI() {
			createBasicGUI();
			createRemainingGUI();
		}

		// override-designed

		public void createRemainingGUI() {
		}

		public JButton newJButton() {
			return new JButton("Back");
		}
	}

}