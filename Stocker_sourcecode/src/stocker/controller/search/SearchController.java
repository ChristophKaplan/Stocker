package stocker.controller.search;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.InternalFrameEvent;

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
import stocker.view.search.SearchView;


/**
 * The Search Controller class is responsible for receiving, generating and
 * processing search view events and, if necessary, manipulating data in the
 * model.
 * 
 * @author Christoph Kaplan
 *
 */
public class SearchController extends ControllerBaseInternal implements ActionListener  {
	private SearchView view;
	
	/**
	 * Constructor
	 * @param desktopViewBase the desktop view
	 * @param propertiesModel the properties model
	 * @param databaseModel the database model
	 */
	public SearchController(DesktopViewBase desktopViewBase,PropertiesModel propertiesModel, DatabaseModel databaseModel) {
		super(ViewType.Search,desktopViewBase,propertiesModel,databaseModel);	
	}

	/**
	 * 	Differentiates between {@code ActionEvent} occurring in the search view, and invokes further methods accordingly.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == view.startSearchButton) onStockSearch(view.searchTextField.getText());	
		if(e.getSource() == view.addWatchlistButton) onAddToWatchlist();
		if(e.getSource() == view.removeButton) onRemoveSelected();	
		if(e.getSource() == view.showChartButton) onShowAsChartOnly();
	}
	
	/**
	 * Invokes {@link stocker.model.database.DatabaseModel#askSearchDataForGeneral}
	 * @param search the search word
	 */
	public void onStockSearch(String search) {	
		clearSearch();
		getDatabaseModel().askSearchDataForGeneral(search,DisplayType.Search);
	}
	
	/**
	 * Adds all selected table entries to the watchlist view
	 */
	public void onAddToWatchlist() {
		for (String symbol : view.getSymbolSelectionList()) {
			try {
				StockData stock = getDatabaseModel().getStockBySymbol(symbol);
								
				getDatabaseModel().setDisplayType(stock,DisplayType.Watchlist);
				getDatabaseModel().askMissingDataStockAndSubscribe(stock,getPropertiesModel().getStandardInterval());
			} catch (Exception e) {
				System.out.println("onStockSearchAdd() "+e.getMessage());
			}
		}
		getPropertiesModel().addFrame(ViewType.Watchlist);
	}
	
	/**
	 * Removes all selected table entries.
	 */
	public void onRemoveSelected() {
		for (String symbol : view.getSymbolSelectionList()) {
			getDatabaseModel().deleteStock(symbol);
		}
	}
	
	/**
	 * Opens all selected table entries in a new chart view frame.
	 */
	public void onShowAsChartOnly() {
		for (String symbol : view.getSymbolSelectionList()) {
			try {
				StockData stock = getDatabaseModel().getStockBySymbol(symbol);
				getDatabaseModel().setDisplayType(stock,DisplayType.ChartOnly);
				getDatabaseModel().askMissingDataStockAndSubscribe(stock,getPropertiesModel().getStandardInterval());
				getPropertiesModel().addChartFrame(symbol);
			} catch (Exception e) {
				System.out.println("onStockSearchAdd() "+e.getMessage());
			}
		}
	}
	
	/**
	 * Clears all table entries with display type "Search"
	 */
	public void clearSearch() {
		for (StockData stock : view.getStocks()) {
			if(stock.getDisplayType() == DisplayType.Search) 
				getDatabaseModel().deleteStock(stock);
		}
	}
	
	
	/**
	 * Creates and sets a new search view
	 */
	@Override
	protected void createOwnView(FrameProfileBase frameProfileBase) {
		FrameProfile frameProfile = (FrameProfile) frameProfileBase;
		view = new SearchView(frameProfile,getDatabaseModel());	
		view.addListener(this);
		addFrameToDesktop(view);
		
		view.getTable().initTable(getDatabaseModel().getAllByDisplayType(DisplayType.Search));
		view.setShow();
	}

	/**
	 * Gets the search view
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

	/**
	 * Invokes {@link #clearSearch()} when search view is closed.
	 */
	@Override
	public void onInternalFrameClosed(InternalFrameEvent e) {
		super.onInternalFrameClosed(e);
		//remove all stocks when search frame is closing
		clearSearch();
	}


}
