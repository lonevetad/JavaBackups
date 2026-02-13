package tests.tGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.GController;
import games.generic.controlModel.items.EquipmentItem;
import games.generic.controlModel.items.EquipmentUpgrade;
import games.generic.controlModel.loaders.LoaderGeneric;
import games.generic.controlModel.loaders.LoaderGeneric.LoadStatusResult;
import games.generic.controlModel.loaders.LoaderManager.LoadingObserver;
import games.generic.controlModel.misc.AttributeModification;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.CurrencySet;
import games.generic.controlModel.misc.FactoryObjGModalityBased;
import games.generic.controlModel.misc.GThread.GTRunnable;
import games.generic.controlModel.player.PlayerGeneric;
import games.generic.view.GameView;
import games.generic.view.ViewOptions;
import games.generic.view.guiSwing.LoggerMessagesJTextArea;
import games.generic.view.misc.LoadingProcessView;
import games.theRisingAngel.enums.AttributesTRAn;
import games.theRisingAngel.enums.RechargeableResourcesTRAn;
import games.theRisingAngel.loaders.LoaderEquipUpgradesTRAn;
import games.theRisingAngel.loaders.factories.FactoryEquip;
import games.theRisingAngel.misc.CurrencySetTRAn;
import games.theRisingAngel.providers.GameObjectsProvidersHolderTRAn;
import tests.tGame.tgEvent1.GC_E1;
import tools.Comparators;
import tools.gui.swing.JListModelTreeMap;

public class GView_E1 extends GameView {

	public GView_E1(GController gc) {
		super(gc);
		this.inspectorsViewNeverInit = true;

	}

	boolean inspectorsViewNeverInit;
	JFrame fin;
	JButton jbCloseAll, jbStartPause;
	JPanel jpBigContainer; // jpStatistics, jpCollectionsInspector, jpLog;
	List<Map.Entry<String, JPanel>> jpTabs;
	JTabbedPane jtpHoldingAll;
	JPanel jpStartStop, jpPlayerStats;
	JProgressBar jpbRechargeableResources[], jpLoader; // jpbPlayerLife, jpbPlayerMana;
//	JTextArea jtaPlayerStats;
//	JScrollPane jspPlayerAttributes;
	JLabel jlMoneyText, jlMoneyValue;
	JLabel[] jlPlayerStatText, jlPlayerStatValue;
//	TextArea taLog;
	JTextArea jtaLog;
	JTextArea taInspector;
	LoggerMessagesJTextArea logTextArea;
	RechargeableResourcesTRAn[] rechargeableResource = { RechargeableResourcesTRAn.Life, RechargeableResourcesTRAn.Mana,
			RechargeableResourcesTRAn.Shield };
	List<LoadingObserver> loaderObservers;
	InspectorElements<?>[] inspectors;
	JListModelTreeMap<String, InspectorElements<?>> listInspectorModel;
	JList<InspectorElements<?>> listInspectors;
	ListModel<String> listInspElemStrModel;
	JList<String> listInspectedElementString;

	@Override
	protected ViewOptions newViewOptions() { // TODO Auto-generated method stub
		return null;
	}

	// TODO initInspectors
	public void initInspectors() {
		final Consumer<String> stringifiedElementPrinter;
		final GC_E1 gc;
		final GameObjectsProvidersHolderTRAn goph;
		final GView_E1 view = this;
		gc = (GC_E1) this.getGameController();
		stringifiedElementPrinter = this::logInspectedElementStringified;

		goph = (GameObjectsProvidersHolderTRAn) gc.getGameObjectsProvidersHolder();

		this.inspectors = new InspectorElements[] { //
				new InspectorElements<FactoryObjGModalityBased<EquipmentUpgrade>>("Equipment Upgrades", //
						elemCons -> {
							System.out.println("GView_E1 - running inspector - equip upgrades - "
									+ goph.getEquipUpgradesProvider().getObjectsFactoriesCount() + " elements");
							goph.getEquipUpgradesProvider()
									.forEachFactory((nameEqUpgr, factory) -> elemCons.accept(factory));
						}, //
						factory -> {
							StringBuilder sb;
							final GModality_E1 gm;
							sb = new StringBuilder();

							gm = (GModality_E1) view.getGameController().getCurrentGameModality();
							Objects.requireNonNull(gm);
							goph.setGameModality(gm);

							LoaderEquipUpgradesTRAn.factoryToLinesString(gm, factory)
									.forEach(line -> sb.append(line).append('\n'));
							sb.append('\n').append('\n');
							return sb.toString();
						}, //
						stringifiedElementPrinter), //
				//
				new InspectorElements<FactoryObjGModalityBased<EquipmentItem>>("Equipments", //
						elemCons -> {
							System.out.println("GView_E1 - running inspector - equip - "
									+ goph.getEquipmentsProvider().getObjectsFactoriesCount() + " elements");
							goph.getEquipmentsProvider()
									.forEachFactory((nameEqUpgr, factory) -> elemCons.accept(factory));
						}, //
						factory -> {
							StringBuilder sb;
							FactoryEquip fe;
							fe = (FactoryEquip) factory;
							sb = new StringBuilder();

							sb.append(fe.getName());
							sb.append('\n').append('\t').append("rarity :").append(fe.getRarity());
							sb.append('\n').append('\t').append("type :").append(fe.getType().name());
							sb.append('\n').append('\t').append("price :").append(fe.getPrice()[0]);
							sb.append('\n').append('\t').append("dimension :")
									.append(fe.getDimensionInInventory().toString());
							sb.append('\n').append('\t').append("description :").append(fe.getDescription());
							sb.append('\n').append('\t').append("attribute modifications :\n");
							for (AttributeModification am : fe.attrMods) {
								sb.append('\t').append('\t').append(am.getName()).append(" -> ").append(am.getValue())
										.append('\n');
							}
							sb.append('\n').append('\t').append("abilities:\n");
							if (fe.abilities == null || fe.abilities.isEmpty()) {
								sb.append("\t\t[]\n");
							} else {
								fe.abilities.forEach(a -> { sb.append('\t').append('\t').append(a).append('\n'); });
							}
							sb.append('\n').append('\n');
							return sb.toString();
						}, //
						stringifiedElementPrinter), //

		};

	}

	@Override
	protected LoadingProcessView newLoadingProcessView() { // TODO Auto-generated method stub
		return new LoadingProcessView(this) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onRemovingOnView(GameView view) { // TODO Auto-generated method stub
			}

			@Override
			public void onAddingOnView(GameView view) { // TODO Auto-generated method stub
			}

			@Override
			public void notifyLoadingProcessCompleted(LoaderGeneric loader, LoadStatusResult completitionResult) {
				System.out.println("LOADED " + loader.getClass().getName() + " --> " + completitionResult.name());
				jpLoader.setValue(jpLoader.getValue() + 1);
				jpLoader.setToolTipText(jpLoader.getValue() + " / " + jpLoader.getMaximum());
			}

			@Override
			public void notifyAllLoadingProcessStarted(int loadersAmount) {
				System.out.println("START Loading " + loadersAmount + " loaders (in LoadingProcessView class)");
				jpLoader.setMaximum(loadersAmount);
				jpLoader.setValue(0);
			}

			@Override
			public void notifyAllLoadingProcessEnded(List<LoaderGeneric> failedLoaders) {
				System.out.println("END LOADERS");
				System.out.print("\tfailed:");
				if (failedLoaders == null || failedLoaders.size() == 0) {
					System.out.println("NONE :D");
				} else {
					System.out.println(Arrays.toString(failedLoaders.toArray()));
				}
			}
		};
	}

	@Override
	public void initializeNonGUIParts() {
		this.loaderObservers = new LinkedList<>();
		this.initInspectors();
	}

	@Override
	public void beginsPlayesInteraction(PlayerGeneric thisPlayer, PlayerGeneric otherPlayer) {}

	@Override
	public List<LoaderGeneric> getAllViewRelatedLoaders() { // TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LoadingObserver> getAllLoadersObservers() { // TODO Auto-generated method stub
		return null;
	}

	@Override
	public void continueViewInitialization() {
//		Container panel;
		int n, startYStats;
		JPanel jpNorth;
		GController c;
		c = super.gameController;

		fin = new JFrame("Test Event loop 1");
		fin.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//		panel = fin.getContentPane();
		jpBigContainer = new JPanel();
//		jpBigContainer.setLayout(new BorderLayout());
		fin.add(jpBigContainer);

		jtpHoldingAll = new JTabbedPane();
		jpBigContainer.add(jtpHoldingAll);
		jpTabs = new ArrayList<>();

		fin.addComponentListener(new ComponentAdapter() {
			final BiConsumer<Dimension, JComponent> resizer = (size, e) -> {
				e.setSize(size);
				e.setPreferredSize(size);
				e.setLocation(0, 0);
			};
			final Function<Entry<String, JPanel>, JComponent> panelGetter = e -> e.getValue();

			@Override
			public void componentResized(ComponentEvent ce) {
				Dimension size;
				size = fin.getSize();
				jpBigContainer.setSize(fin.getSize());
				jpBigContainer.setLocation(0, 0);
				jpTabs.forEach(e -> this.resizer.accept(size, panelGetter.apply(e)));
			}
		});

		jtaLog = new JTextArea();
		this.logTextArea = new LoggerMessagesJTextArea(jtaLog);
		JPanel jpLog = new JPanel();
//		jpLog
		jpLog.add(logTextArea.getTextArea());
		jpTabs.add(Map.entry("Log", jpLog));

		JPanel jpStatistics = new JPanel();
		jpStatistics.setLayout(new BorderLayout());
		jpTabs.add(Map.entry("Statistics & log", jpStatistics));
//		jpStatistics,jpCollectionsInspector;

		//
		jpNorth = new JPanel();
		jpStatistics.add(jpNorth, BorderLayout.NORTH);
		jbStartPause = new JButton("Start");
		jpNorth.add(jbStartPause);
		jbCloseAll = new JButton("CLOSE ALL");
		jpNorth.add(jbCloseAll);

		jbStartPause.addActionListener(l -> startSimulation());
		jbCloseAll.addActionListener(l -> {
			System.out.println("CLOSING ALL");
			c.closeAll();
			jbStartPause.setText("START");
		});

		jpLoader = new JProgressBar(0, 2);
		jpNorth.add(jpLoader);
		jpLoader.setToolTipText("loading status");
		//

		GridBagConstraints constr;
		constr = new GridBagConstraints();
		jpPlayerStats = new JPanel(new GridBagLayout());
		jpStatistics.add(jpPlayerStats, BorderLayout.EAST);

		jpbRechargeableResources = new JProgressBar[rechargeableResource.length];

		constr.gridx = 0;
		constr.gridwidth = 2;
		constr.gridheight = 1;
		constr.weightx = constr.weighty = 1;
		for (int i = 0; i < rechargeableResource.length; i++) {
			JProgressBar jpb;
			jpbRechargeableResources[i] = jpb = new JProgressBar(0, 100);
			jpb.setStringPainted(true);
			constr.gridy = i;
			jpPlayerStats.add(jpb, constr);
		}

//		constr.gridheight = 9;
//		constr.weighty = 9;
//		jtaPlayerStats = new JTextArea();
//		jspPlayerAttributes = new JScrollPane(jtaPlayerStats);
//		jspPlayerAttributes.setViewportView(jtaPlayerStats);
//		jspPlayerAttributes.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
//		jpPlayerStats.add(jspPlayerAttributes, constr);
//		jtaPlayerStats.setLineWrap(false);
		constr.gridy++;
		constr.gridwidth = 1;
		jlMoneyText = new JLabel("Currency: ");
		jpPlayerStats.add(jlMoneyText, constr);
		constr.gridx = 1;
		jlMoneyValue = new JLabel("0");
		jpPlayerStats.add(jlMoneyValue, constr);
		jlPlayerStatText = new JLabel[AttributesTRAn.ALL_ATTRIBUTES.length];
		jlPlayerStatValue = new JLabel[AttributesTRAn.ALL_ATTRIBUTES.length];
		n = AttributesTRAn.ALL_ATTRIBUTES.length;
		startYStats = constr.gridy + 1;
		while (--n >= 0) {
			constr.gridy = startYStats + n;
			constr.gridx = 0;
			jlPlayerStatText[n] = new JLabel(AttributesTRAn.ALL_ATTRIBUTES[n].name());
			jpPlayerStats.add(jlPlayerStatText[n], constr);
			constr.gridx = 1;
			jlPlayerStatValue[n] = new JLabel("0");
			jpPlayerStats.add(jlPlayerStatValue[n], constr);
		}

//		taPlayerStats.setPreferredSize(new Dimension(100, 500));
		/*
		 * TODO add progress bar and textarea, then create a gui-thread to update player
		 * stats
		 */

		initInspectorsView();

		jpTabs.forEach(e -> { jtpHoldingAll.addTab(e.getKey(), e.getValue()); });
	}

	public void initInspectorsView() {
		JPanel jpInsp, jpInspectors;
		if (inspectorsViewNeverInit) {
			this.inspectorsViewNeverInit = false;
		} else {
			return;
		}
		jpInsp = new JPanel();
		jpInsp.setLayout(new BorderLayout());
		jpTabs.add(Map.entry("Inspector", jpInsp));

		taInspector = new JTextArea("test");
		JScrollPane jsp;
		jsp = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp.setViewportView(taInspector);
		jpInsp.add(jsp, BorderLayout.CENTER);
		taInspector.setSize(new Dimension(300, 400));
		taInspector.setPreferredSize(taInspector.getSize());

		jpInspectors = new JPanel();
//		jpInspectors.setLayout(null);
		jpInsp.add(jpInspectors, BorderLayout.WEST);
		jpInspectors.setSize(new Dimension(150, 400));
		jpInspectors.setPreferredSize(jpInspectors.getSize());

		for (int i = 0; i < inspectors.length; i++) {
			InspectorElements<?> ie;
			JButton jb;
			ie = inspectors[i];
			jb = new JButton(ie.collectionName);
			jb.setSize(150, 50);
			jb.setLocation(0, i * jb.getHeight());
			jpInspectors.add(jb);
			jb.addActionListener(new ActionListener() {
				final InspectorElements<?> inspectorCurrent = ie;

				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("RUNNING GView_E1.InspectorElements: " + inspectorCurrent.getCollectionName());
					inspectorCurrent.run();
				}
			});
		}

		// alternatives
//		listInspElemStrModel = new DefaultListModel<>();
	}

	@Deprecated
	public void initInspectorsView_V1() {
		JPanel jpInsp;
		if (inspectorsViewNeverInit) {
			this.inspectorsViewNeverInit = false;
		} else {
			return;
		}
		jpInsp = new JPanel();
		jpInsp.setLayout(new BorderLayout());
		jpTabs.add(Map.entry("Inspector", jpInsp));

		taInspector = new JTextArea();
		jpInsp.add(taInspector, BorderLayout.CENTER);
		taInspector.setSize(new Dimension(300, 400));
		taInspector.setPreferredSize(taInspector.getSize());

		listInspectorModel = JListModelTreeMap.newInstance(//
				MapTreeAVL.newMap( //
						MapTreeAVL.Optimizations.QueueFIFOIteration, //
						Comparators.STRING_COMPARATOR), //
//				InspectorElements::getCollectionName //
				(InspectorElements<?> ie) -> ie.getCollectionName() //
		);
		listInspectors = new JList<>(listInspectorModel);
		jpInsp.add(listInspectors, BorderLayout.WEST);
		listInspectorModel.addElements(inspectors);
//		listInspectorModel.addListDataListener(new ListDataListener() {
//			@Override
//			public void intervalRemoved(ListDataEvent e) { }
//			@Override
//			public void intervalAdded(ListDataEvent e) { }
//			@Override
//			public void contentsChanged(ListDataEvent e) { }
//		});
		listInspectors.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				taInspector.setText("");
				inspectors[e.getFirstIndex()].run();
			}
		});
		listInspectors.setCellRenderer(new ListCellRenderer<InspectorElements<?>>() {
			final JTextField jlCell = new JTextField();

			@Override
			public Component getListCellRendererComponent(JList<? extends InspectorElements<?>> list,
					InspectorElements<?> value, int index, boolean isSelected, boolean cellHasFocus) {
				if (cellHasFocus) {
					jlCell.setBorder(BorderFactory.createLineBorder(isSelected ? Color.RED : Color.BLUE, 2));
				} else if (isSelected) {
					jlCell.setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
				} else {
					jlCell.setBorder(null);
				}
				jlCell.setText(value.collectionName);
				return jlCell;
			}
		});
		listInspectors.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
	}

	@Override
	public void finishViewSetupAndShow() {

		fin.setSize(500, 700);
		jpBigContainer.setSize(fin.getSize());
		fin.setVisible(true);
//		fin.pack();
	}

	//

	protected void startSimulation() {
		if (gameController == null) {
			System.out.println("GView cannot start because gc is NULL");
			return;
		}
		System.out.println("gc.isAlive()? " + gameController.isAlive());
		if (gameController.isAlive()) {
			if (gameController.isPlaying()) {
				gameController.pauseGame();
				jbStartPause.setText("Resume");
			} else {
				gameController.resumeGame();
				jbStartPause.setText("Pause");
			}
		} else {
			System.out.println("view starting");
			gameController.startGame();
			System.out.println("view adding repainter");
			addRepainterThread();
			System.out.println("STARTED");
			jbStartPause.setText("Pause");
		}
	}

	void repaintPlayerInfo() {
		int max, v, n;
		Player_E1 p;
		GModality_E1 gmodalitye1;
		String textDisplayed;
//		StringBuilder sb;
		CreatureAttributes ca;
		JProgressBar jpb;
		RechargeableResourcesTRAn curRes;
		gmodalitye1 = (GModality_E1) super.gameController.getCurrentGameModality();
		if (gmodalitye1 == null)
			return;
		p = gmodalitye1.getPlayerRPG();
		for (int i = 0; i < rechargeableResource.length; i++) {
			jpb = jpbRechargeableResources[i];
			curRes = rechargeableResource[i];
			jpb.setMaximum(max = p.getMaxAmount(curRes));
			jpb.setValue(v = p.getAmount(curRes));
			textDisplayed = curRes.getName() + ": " + v + " / " + max;
			jpb.setToolTipText(textDisplayed);
			jpb.setString(textDisplayed);
			jpb.repaint();
		}

		ca = p.getAttributes();
//		sb = new StringBuilder(128);
//		for (int i = 0, n = ca.getAttributesCount(); i < n; i++) {
//			sb.append(AttributesTRAr.VALUES[i].name()).append("\t: ").append(ca.getValue(i)).append('\n');
//		}
//		jtaPlayerStats.setText(sb.toString());
		jlMoneyValue.setText(Integer.toString(p.getCurrencies().getCurrencyAmount( //
				p.getCurrencies().getCurrencies()[CurrencySet.BASE_CURRENCY_INDEX])));
		n = AttributesTRAn.ALL_ATTRIBUTES.length;
		while (--n >= 0) {
			jlPlayerStatValue[n].setText(Integer.toString(ca.getValue(AttributesTRAn.ALL_ATTRIBUTES[n])));
		}
		jpPlayerStats.repaint();
		// TODO
	}

// TODO logInspectedElementStringified
	protected void logInspectedElementStringified(String text) {
		System.out.println(text);
		taInspector.append(text);
	}

	void singleRepaintCycle() {
		repaintPlayerInfo();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	void addRepainterThread() {
		GModality_E1 gmodalitye1;
//		GThread t;
		gmodalitye1 = (GModality_E1) super.gameController.getCurrentGameModality();
//		t = new GThread(new PlayerInfoRepainter(gmodalitye1));
//		gmodalitye1.addGameThread(t);
//		t.start();
		System.out.println("view on adding repainter");
		gmodalitye1.addGameThreadSimplyStoppable(this::singleRepaintCycle);
	}

	//

	protected class PlayerInfoRepainter implements GTRunnable {
		final GModality_E1 gmodalitye1;

		public PlayerInfoRepainter(GModality_E1 gmodalitye1) {
			super();
			this.gmodalitye1 = gmodalitye1;
		}

		@Override
		public void run() {
			while (gmodalitye1.isAlive()) {
				while (gmodalitye1.isRunningOrSleep()) {
					singleRepaintCycle();
				}
			}
			System.out.println("END view repainter");
		}

		@Override
		public void stopAndDie() {}

		@Override
		public void restart() {}
	}

	//

	// TODO inspector
	/**
	 * Class that inspect all elements on a class which has the ability E
	 *
	 * @author ottin
	 *
	 * @param <E>
	 */
	public static class InspectorElements<E> implements Runnable {
		public final String collectionName;
//		public final LoggerMessages log;
		public final Function<E, String> toStringer;
		public final Consumer<Consumer<E>> forEachCallFunction;
		public final Consumer<E> inspectedElementConsumer;
		public final Consumer<String> elementStringifiedConsumer;

		public InspectorElements(String collectionName, //
//				LoggerMessages log, //
				Consumer<Consumer<E>> forEachCallFunction, //
				Function<E, String> toStringer, //
				Consumer<String> elementStringifiedConsumer) {
			super();
			this.collectionName = collectionName;
//			this.log = log;
			this.toStringer = ( //
			(toStringer != null) ? toStringer : e -> e.toString() //
			);
			this.forEachCallFunction = forEachCallFunction;
			this.elementStringifiedConsumer = elementStringifiedConsumer;
			this.inspectedElementConsumer = e -> this.elementStringifiedConsumer.accept(this.toStringer.apply(e));
		}

		public String getCollectionName() { return collectionName; }

		public Function<E, String> getToStringer() { return toStringer; }

		public Consumer<Consumer<E>> getForEachCallFunction() { return forEachCallFunction; }

		public Consumer<E> getInspectedElementConsumer() { return inspectedElementConsumer; }

		@Override
		public void run() { this.forEachCallFunction.accept(inspectedElementConsumer); }
	}
}