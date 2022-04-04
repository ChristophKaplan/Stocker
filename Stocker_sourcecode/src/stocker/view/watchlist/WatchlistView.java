package stocker.view.watchlist;


import java.awt.GridBagConstraints;
import javax.swing.JScrollPane;

import stocker.controller.watchlist.WatchlistController;
import stocker.model.database.AlarmWrapper;
import stocker.model.database.DatabaseModel;
import stocker.model.general.DisplayType;
import stocker.model.general.FrameProfile;
import stocker.model.stockdata.StockData;
import stocker.view.general.InternalViewBase;
import stocker.view.stockertable.StockerTable;



/**
 * Representation of information of the Watchlist to the user. 
 * Interacts with WatchlistController,WatchlistMenuBar and StockerTable
 * Part of the MVC View.
 * @author Christoph Kaplan
 */
public class WatchlistView extends InternalViewBase {
	private static final long serialVersionUID = 9039025029163030506L;
	private StockerTable table;
	private WatchlistMenuBar watchlistMenuBar;

	/**
	 * Constructor
	 * @param frameProfile the frame profile
	 * @param databaseModel the database model
	 */
	public WatchlistView(FrameProfile frameProfile, DatabaseModel databaseModel) {
		super(frameProfile, databaseModel);
	}
	
	/**
	 * Sets components and layout.
	 */
	public void setUp() {
		super.setUp();		
		table = new StockerTable(DisplayType.Watchlist);
		watchlistMenuBar = new WatchlistMenuBar();
		setJMenuBar(watchlistMenuBar);
		JScrollPane tableScrollPane = new JScrollPane(table);
		setToGridBag(tableScrollPane, 0, 0, 1, 1, 0.5f, 0.5f, GridBagConstraints.BOTH);
	}

	/**
	 * Adds listener
	 * @param controller the listener
	 */
	public void addListener(WatchlistController controller) {
		super.addListener(controller);
		watchlistMenuBar.addListener(controller);
	}

	/**
	 * Gets the table.
	 * @return the table
	 */
	public StockerTable getTable() {
		return this.table;
	}

	/**
	 * Gets the watchlist menu bar.
	 * @return the watchlist menu bar
	 */
	public WatchlistMenuBar getWatchlistMenuBar() {
		return watchlistMenuBar;
	}

	/**
	 * Invokes {@link stocker.view.stockertable.StockerTable#stockAdd}
	 */
	@Override
	public void onStockAdded(StockData stock) {
		if (stock.getDisplayType() != DisplayType.Watchlist)
			return;

		// System.out.println("onStockAdded" + stock.getDisplaySymbol());
		getTable().stockAdd(stock);
	}

	/**
	 * Invokes {@link stocker.view.stockertable.StockerTable#stockRemove}
	 */
	@Override
	public void onStockRemoved(StockData stock) {
		if (stock.getDisplayType() != DisplayType.Watchlist)
			return;
		// System.out.println("onStockRemoved" + stock.getDisplaySymbol());
		getTable().stockRemove(stock);
	}

	/**
	 * Invokes {@link stocker.view.stockertable.StockerTable#stockUpdate}
	 */
	@Override
	public void onStockUpdate(StockData stock) {
		if (stock.getDisplayType() != DisplayType.Watchlist)
			return;
		//System.out.println("onStockUpdate " + stock.getSymbol());
		getTable().stockUpdate(stock);
	}

	/**
	 * When stock data is calculated.
	 */
	@Override
	public void onStockCalculated(StockData stock) {
		if (stock.getDisplayType() != DisplayType.Watchlist)
			return;
		
	}
	
	
	
	/**
	 * Invokes {@link stocker.view.stockertable.StockerTable#stockDisplayTypeChange}
	 */
	@Override
	public void onStockDisplayTypeChange(StockData stock) {
		getTable().stockDisplayTypeChange(stock);
	}
	/**
	 * Does nothing yet
	 */
	@Override
	public void onAlarmAdded(AlarmWrapper alarm, double value) {
		// TODO Auto-generated method stub

	}
	/**
	 * Does nothing yet
	 */
	@Override
	public void onAlarmRemove(AlarmWrapper alarm, double value) {
		// TODO Auto-generated method stub

	}

}