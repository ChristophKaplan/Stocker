package stocker.controller.desktop;

import stocker.controller.chart.ChartController;
import stocker.controller.general.ControllerBaseDesktop;
import stocker.controller.properties.PropertiesController;
import stocker.controller.search.SearchController;
import stocker.controller.watchlist.WatchlistController;
import stocker.main.start.StockerApplicationManager;
import stocker.model.database.AlarmWrapper;
import stocker.model.database.DatabaseModel;
import stocker.model.database.DatabaseStatusListener;
import stocker.model.general.FrameProfile;
import stocker.model.general.FrameProfileBase;
import stocker.model.general.ViewType;
import stocker.model.properties.PropertiesModel;
import stocker.view.chart.DialogAlarmTriggered;
import stocker.view.desktop.DesktopView;

/**
 * The desktop controller class is responsible for constructing the GUI
 * controller classes and executing certain calls of the {@code ControllerBaseDesktop} class
 * 
 * @author Christoph Kaplan
 */
public class DesktopController extends ControllerBaseDesktop implements DatabaseStatusListener {
	//private DialogAlarmTriggered triggered;
	private StockerApplicationManager stockerApplicationManager;

	/**
	 * Factory method that constructs all gui view controllers. The main
	 * {@code DesktopController} and the "child" controllers
	 * {@code WatchlistController}, {@code SearchController},
	 * {@code ChartController} and {@code PropertiesController}.
	 * 
	 * @param propertiesModel           {@code PropertiesModel} object
	 * @param databaseModel             {@code DatabaseModel} object
	 * @param stockerApplicationManager {@code StockerApplicationManager} object
	 * @return {@code DesktopController}
	 */
	public static DesktopController create(PropertiesModel propertiesModel, DatabaseModel databaseModel,
			StockerApplicationManager stockerApplicationManager) {
		System.out.println("DesktopController.create()");

		try {
			// create the desktop view, which is shared upon all view controllers on a
			// controller base level.
			FrameProfile desktopFrameProfile = (FrameProfile) propertiesModel.getFrameProfileById(0);
			DesktopView desktopView = new DesktopView(desktopFrameProfile);
			DesktopController desktopController = new DesktopController(desktopView, propertiesModel, databaseModel,
					stockerApplicationManager);

			// create "child" view controllers
			new WatchlistController(desktopView, propertiesModel, databaseModel);
			new SearchController(desktopView, propertiesModel, databaseModel);
			new ChartController(desktopView, propertiesModel, databaseModel);
			new PropertiesController(desktopView, propertiesModel, databaseModel);

			return desktopController;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private DesktopController(DesktopView desktopView, PropertiesModel propertiesModel, DatabaseModel databaseModel,
			StockerApplicationManager stockerController) {
		super(ViewType.Desktop, desktopView, propertiesModel);
		this.stockerApplicationManager = stockerController;
		desktopView.addListener(this);
		desktopView.updateOpenWindowMenu(this);
		databaseModel.addDatabaseStatusObserver(this);
	}

	/*
	 * @Override public void onOpenSelf() {
	 * getPropertiesModel().addFrame(ViewType.Desktop); }
	 */

	/**
	 * Opens {@code WatchlistView} object by adding a watchlist frame to the
	 * {@code PropertiesModel}.
	 */
	@Override
	public void onOpenWatchlist() {
		getPropertiesModel().addFrame(ViewType.Watchlist);
	}

	/**
	 * Opens {@code SearchView} object by adding a search frame to the
	 * {@code PropertiesModel}.
	 */
	@Override
	public void onOpenSearch() {
		getPropertiesModel().addFrame(ViewType.Search);
	}

	/**
	 * Opens {@code PropertiesView} object by adding a properties frame to the
	 * {@code PropertiesModel}.
	 */
	@Override
	public void onOpenProperties() {
		getPropertiesModel().addFrame(ViewType.Properties);
	}

	/**
	 * Invokes {@link stocker.main.start.StockerApplicationManager#endApplication}
	 */
	@Override
	public void onCloseApplication() {
		stockerApplicationManager.endApplication();
	}

	/**
	 * Updates frame profile in {@code DesktopViewBase} object.
	 */
	@Override
	public void onGetFrameSCREENSTATE() {
		getDesktopViewBase().updateFrameProfile();
	}

	/**
	 * Updates status in {@code DesktopViewBase} object.
	 */
	@Override
	public void onStatusChange(String status) {
		this.getDesktopViewBase().setStatus(status);

	}

	/**
	 * Opens dialog that alarm is triggered.
	 */
	@Override
	public void onAlarmTriggered(AlarmWrapper alarm, double value) {
		new DialogAlarmTriggered("ALARM " + alarm.getSymbol() + "-" + value, getDesktopViewBase());
	}

	@Override
	public void onFrameUpdate(FrameProfileBase frameProfileBase) {
		// TODO Auto-generated method stub
		
	}

}
