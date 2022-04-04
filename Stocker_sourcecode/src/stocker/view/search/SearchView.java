package stocker.view.search;


import java.awt.GridBagConstraints;
import java.util.ArrayList;
import javax.swing.*;

import stocker.controller.search.SearchController;
import stocker.model.database.AlarmWrapper;
import stocker.model.database.DatabaseModel;
import stocker.model.general.DisplayType;
import stocker.model.general.FrameProfile;
import stocker.model.stockdata.StockData;
import stocker.view.general.InternalViewBase;
import stocker.view.general.StockerTextField;
import stocker.view.general.StockerTextField.FilterMethod;
import stocker.view.stockertable.StockerTable;

/**
 * Representation of information of the Search to the user. 
 * @author Christoph Kaplan
 *
 */
public class SearchView extends InternalViewBase {
	private static final long serialVersionUID = 3597420452888402684L;

	public JLabel resultLabel;
	public JLabel searchLabel;
	public StockerTable table;
	public JButton startSearchButton;
	public JButton addWatchlistButton;
	public JButton showChartButton;
	public JButton removeButton;
	public StockerTextField searchTextField;
	public JScrollPane scrollPan;

	/**
	 * Constructor
	 * @param frameProfile  the frame profile
	 * @param databaseModel the database model
	 */
	public SearchView(FrameProfile frameProfile, DatabaseModel databaseModel) {
		super(frameProfile, databaseModel);
	}

	/**
	 * Sets the components and layout.
	 */
	@Override
	public void setUp() {
		super.setUp();
		table = new StockerTable(DisplayType.Search);
		
		resultLabel = new JLabel("Results");
		searchLabel = new JLabel("Search");

		startSearchButton = new JButton("Search");
		addWatchlistButton = new JButton("Add to watchlist");
		showChartButton = new JButton("Show chart");
		removeButton = new JButton("Remove");
		
		FilterMethod regularFilterMethod = new FilterMethod() {
			@Override
			public boolean isValid(String text) {
				//define a filter for the data provider profile input characters
				if (text.isBlank())
					return false;
				return true;
			}
		};
		
		searchTextField = new StockerTextField("",50, regularFilterMethod);
		scrollPan = new JScrollPane();
		JLabel resultLabel = new JLabel("Results");

		setToGridBag(searchLabel, 0, 0, 2, 1, 1.0f, 0.0f, GridBagConstraints.HORIZONTAL);
		setToGridBag(searchTextField, 0, 1, 3, 1, 0.0f, 0.0f, GridBagConstraints.BOTH);
		setToGridBag(startSearchButton, 3, 1, 1, 1, 0.0f, 0.0f, GridBagConstraints.NONE);

		setToGridBag(resultLabel, 0, 2, 2, 1, 1.0f, 0.0f, GridBagConstraints.NONE);
		JScrollPane tableScrollPane = new JScrollPane(table);

		setToGridBag(tableScrollPane, 0, 3, 4, 2, 1.0f, 1.0f, GridBagConstraints.BOTH);

		setToGridBag(showChartButton, 1, 5, 1, 1, 0.5f, 0.0f, GridBagConstraints.BOTH);
		setToGridBag(addWatchlistButton, 2, 5, 1, 1, 0.5f, 0.0f, GridBagConstraints.BOTH);
		setToGridBag(removeButton, 3, 5, 1, 1, 0.5f, 0.0f, GridBagConstraints.BOTH);
	}

	/**
	 * Adss listener.
	 * @param controller the listener
	 */
	public void addListener(SearchController controller) {
		super.addListener(controller);
		startSearchButton.addActionListener(controller);
		addWatchlistButton.addActionListener(controller);
		showChartButton.addActionListener(controller);
		removeButton.addActionListener(controller);
	}

	/**
	 * Gets the table's list of symbols of the selected stock data
	 * @return the list of symbols
	 */
	public ArrayList<String> getSymbolSelectionList() {
		return getTable().getSymbolSelectionList();
	}

	/**
	 * Gets the table's list of stock data
	 * @return the list of stock data
	 */
	public ArrayList<StockData> getStocks() {
		return getTable().getStocks();
	}

	/**
	 * Clears the table.
	 */
	public void clearAll() {
		getTable().clearAll();
	}

	
	/**
	 * Gets the table.
	 * @return the table
	 */
	public StockerTable getTable() {
		return this.table;
	}

	/**
	 * Invokes {@link stocker.view.stockertable.StockerTable#stockAdd}
	 */
	@Override
	public void onStockAdded(StockData stock) {
		if (stock.getDisplayType() != DisplayType.Search)
			return;
		getTable().stockAdd(stock);
	}

	/**
	 * Invokes {@link stocker.view.stockertable.StockerTable#stockRemove}
	 */
	@Override
	public void onStockRemoved(StockData stock) {
		if (stock.getDisplayType() != DisplayType.Search)
			return;
		getTable().stockRemove(stock);
	}

	
	
	/**
	 * When stock data is updated.
	 */
	@Override
	public void onStockUpdate(StockData stock) {
		if (stock.getDisplayType() != DisplayType.Search)
			return;
	}

	/**
	 * When stock data is calculated.
	 */
	@Override
	public void onStockCalculated(StockData stock) {
		if (stock.getDisplayType() != DisplayType.Search)
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