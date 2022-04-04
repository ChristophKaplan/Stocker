package stocker.view.stockertable;

import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import stocker.model.general.DisplayType;
import stocker.model.stockdata.StockData;

/**
 * The class StockerTable is used in the watchlist and search view and is
 * derived from JTable. In addition to the swing table function, its main
 * function is to pass on the stock data arriving via listener in its associated
 * view to the table model {@code StockerTableModel}, which is derived from
 * {@code AbstractTableModel}.
 * 
 * 
 * @author Christoph Kaplan
 *
 */
public class StockerTable extends JTable {
	private static final long serialVersionUID = -8041375648105062397L;

	private DisplayType displayType;
	private StockerTableModel tableModel;

	/**
	 * Constructor Sets up the tables sorter and other settings.
	 * 
	 * @param displayType the display type
	 */
	public StockerTable(DisplayType displayType) {
		this.displayType = displayType;
		this.tableModel = new StockerTableModel(this.displayType);
		setModel(tableModel);

		setDefaultRenderer(Object.class, new StockerDefaultRenderer());
		setDefaultRenderer(Boolean.class, new StockerCheckBoxRenderer());

		setAutoCreateRowSorter(true);
		setCellSelectionEnabled(false);
		setFocusable(false);
	}

	/**
	 * Initialize the stock data
	 * 
	 * @param stockDataList the stock data
	 */
	public void initTable(ArrayList<StockData> stockDataList) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				for (StockData stock : stockDataList) {
					stockAdd(stock);
				}
			}
		});

	}

	/**
	 * Gets the list of symbols of the selected stock data
	 * 
	 * @return the list of symbols of the selected stock data
	 */
	public ArrayList<String> getSymbolSelectionList() {
		return tableModel.getSelectionList();
	}

	/**
	 * Gets the stock data list of the model
	 * 
	 * @return the stock data list
	 */
	public ArrayList<StockData> getStocks() {
		return tableModel.getStocks();
	}

	/**
	 * Clears all data in the table model
	 */
	public void clearAll() {
		tableModel.clearAll();
	}

	/**
	 * Invoked when a stock data is added
	 * 
	 * @param stock the added stock data
	 */
	public void stockAdd(StockData stock) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				tableModel.stockAdd(stock);
			}
		});
	}

	/**
	 * Invoked when a stock data is removed
	 * 
	 * @param stock the removed stock data
	 */
	public void stockRemove(StockData stock) {		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				tableModel.stockRemove(stock);
			}
		});

	}

	/**
	 * Invoked when a stock data is updated
	 * 
	 * @param stock the updated stock data
	 */
	public void stockUpdate(StockData stock) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				tableModel.stockUpdate(stock);
			}
		});

	}

	/**
	 * Invoked when a stock data's display type changed
	 * 
	 * @param stock the stock data
	 */
	public void stockDisplayTypeChange(StockData stock) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				tableModel.displayTypeChange(stock);
			}
		});

	}
}
