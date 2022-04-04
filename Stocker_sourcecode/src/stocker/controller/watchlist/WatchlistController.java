package stocker.controller.watchlist;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import stocker.controller.general.ControllerBaseInternal;
import stocker.model.database.DatabaseModel;
import stocker.model.general.DisplayType;
import stocker.model.general.FrameProfile;
import stocker.model.general.FrameProfileBase;
import stocker.model.general.ViewType;
import stocker.model.properties.PropertiesModel;
import stocker.model.stockdata.StockData;
import stocker.view.general.DesktopViewBase;
import stocker.view.general.InternalViewBase;
import stocker.view.stockertable.StockerTableModel;
import stocker.view.watchlist.WatchlistView;

/**
 * The Watchlist Controller class is responsible for receiving, generating and
 * processing watchlist view events and, if necessary, manipulating data in the
 * model.
 * @author Christoph Kaplan
 *
 */
public class WatchlistController extends ControllerBaseInternal implements ActionListener {

	private WatchlistView view;

	/**
	 * Constructor
	 * @param desktopViewBase the desktop view
	 * @param propertiesModel the properties model
	 * @param databaseModel the database model
	 */
	public WatchlistController(DesktopViewBase desktopViewBase,PropertiesModel propertiesModel, DatabaseModel databaseModel) {
		super(ViewType.Watchlist,desktopViewBase,propertiesModel,databaseModel);	
	}
	
	/**
	 * 	Differentiates between {@code ActionEvent} occurring in the watchlist menubar, and invokes further methods accordingly.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == view.getWatchlistMenuBar().getOpenSearch()) {
			watchlistStockSearch();
		}
		if (e.getSource() == view.getWatchlistMenuBar().getWatchlistRemoveChecked()) {
			watchlistRemoveChecked();
		}
		if (e.getSource() == view.getWatchlistMenuBar().getWatchlistShowChecked()) {
			watchlistShowChecked();
		}
	}

	/**
	 * Opens the search view by adding a search frame to the properties model.
	 */
	private void watchlistStockSearch() {
		System.out.println("watchlistStockSearch()");
		getPropertiesModel().addFrame(ViewType.Search);
	}

	/**
	 * Removes all selected entries in the table model.
	 */
	private void watchlistRemoveChecked() {
		StockerTableModel tableModel = (StockerTableModel) view.getTable().getModel();
		ArrayList<String> listOfChecked = tableModel.getSelectionList();

		for(int i = 0; i < listOfChecked.size();i++) {
			try {
				StockData s = getDatabaseModel().getStockBySymbol(listOfChecked.get(i));
				getDatabaseModel().deleteStock(s);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * Opens all selected entries of the table model as a new chart view by adding the associated frame to the properties model.
	 */
	private void watchlistShowChecked() {
		StockerTableModel tableModel = (StockerTableModel) view.getTable().getModel();
		ArrayList<String> listOfChecked = tableModel.getSelectionList();
		for (String symbol : listOfChecked) {
			getPropertiesModel().addChartFrame(symbol);
		}
		
		tableModel.deselectAll();
	}
	
	/**
	 * Creates and sets a new watchlist view
	 */
	@Override
	protected void createOwnView(FrameProfileBase frameProfileBase) {
		FrameProfile frameProfile = (FrameProfile) frameProfileBase;
		view = new WatchlistView(frameProfile, getDatabaseModel());
		view.addListener(this);
		addFrameToDesktop(view);
		
		view.getTable().initTable(getDatabaseModel().getAllByDisplayType(DisplayType.Watchlist));
		view.setShow();
	}
	
	/**
	 * Gets the watchlist view
	 */
	@Override
	protected InternalViewBase getOwnView(FrameProfileBase frameProfileBase) {
		return view;
	}

	/**
	 * Updates the frame profile with the relevant frame data.
	 */
	@Override
	public void onGetFrameSCREENSTATE() {
		if(view == null)return;
		view.updateFrameProfile();
	}





}
