package stocker.view.stockertable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.Timer;
import javax.swing.table.AbstractTableModel;

import stocker.model.general.DisplayType;
import stocker.model.stockdata.StockData;

/**
 * The table model implements a list of the {@code StockData} type and overrides the
 * common {@code AbstractTableModel} methods in such a way that the list can be accessed
 * and displayed correctly. Each table row should be selectable by clicking on a
 * "checkbox". For this purpose, a {@code HashMap <String, Boolean>}
 * is implemented, which assigns the current selection status to the “symbol”.
 * In addition, the table should implement a short {@code<} 1s colored background for
 * the price value change. Another {@code HashMap <String, Timer>} is implemented for
 * this, whereby the timer is decisive for the duration of the “colored
 * background”. As soon as a stock is added / removed, a timer is also created /
 * removed. As soon as a stock receives an update, the associated timer may be
 * started.
 * 
 * @author Christoph Kaplan
 *
 */
public class StockerTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -4624547766854779852L;

	private String[] watchlistCols = { "Description", "Display Symbol", "Price", "% Change", "Select" };
	private String[] searchCols = { "Description", "Display Symbol", "Select" };

	private HashMap<String, Boolean> checkboxMap = new HashMap<String, Boolean>();
	private ArrayList<StockData> stockList = new ArrayList<StockData>();
	private HashMap<String, Timer> timerMap = new HashMap<String, Timer>();

	private DisplayType displayType;

	/**
	 * constructor
	 * 
	 * @param displayType the display type
	 */
	public StockerTableModel(DisplayType displayType) {
		this.displayType = displayType;
	}

	/**
	 * Gets the row index by stock data
	 * 
	 * @param stock the stock data
	 * @return the row index
	 */
	public int getRowIndexByStock(StockData stock) {
		return stockList.indexOf(stock);
	}

	/**
	 * Gets the length of the row
	 */
	@Override
	public int getRowCount() {
		return stockList.size();
	}

	/**
	 * Gets the length of the column
	 */
	@Override
	public int getColumnCount() {
		switch (this.displayType) {
		case Search:
			return searchCols.length;
		case Watchlist:
			return watchlistCols.length;
		default:
			return 0;
		}
	}

	/**
	 * Gets the name of a column by index
	 */
	@Override
	public String getColumnName(int columnIndex) {
		switch (this.displayType) {
		case Search:
			return searchCols[columnIndex];
		case Watchlist:
			return watchlistCols[columnIndex];
		default:
			return "";
		}

	}

	/**
	 * Sets the cell value at specific coordinate
	 */
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (columnIndex == (getColumnCount() - 1)) {
			StockData stock = stockList.get(rowIndex);
			checkboxMap.put(stock.getSymbol(), !checkboxMap.get(stock.getSymbol()));
		}
	}

	/**
	 * Gets the cell value at a specific coordinate Sends a new {@code PriceWrapper}
	 * object to the {@code StockerDefaultRenderer} can read
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		StockData stock = stockList.get(rowIndex);
		Boolean check = checkboxMap.get(stock.getSymbol());
		Timer timer = timerMap.get(stock.getSymbol());

		if (displayType == DisplayType.Watchlist) {
			switch (columnIndex) {
			case 0:
				return stock.getStockDataSearch().getDescription();
			case 1:
				return stock.getStockDataSearch().getDisplaySymbol();
			case 2:
				//price wrapper to get the needed information to the renderer.
				return new PriceWrapper(stock.getCurrentPrice(), stock.getPriceDiff(), timer.isRunning());
			case 3:
				return stock.getChangePercentage();
			case 4:
				return check;
			default:
				return null;
			}
		}
		if (displayType == DisplayType.Search) {
			switch (columnIndex) {
			case 0:
				return stock.getStockDataSearch().getDescription();
			case 1:
				return stock.getStockDataSearch().getDisplaySymbol();
			case 2:
				return check;
			default:
				return null;
			}
		}

		return null;
	}

	/**
	 * Gets true/false whether a specific cell is editable by coordinates
	 */
	@Override
	public boolean isCellEditable(int row, int col) {
		if (col == this.getColumnCount() - 1) {
			return true;
		}
		return false;
	}

	/**
	 * Gets the class of a specific column by index
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == this.getColumnCount() - 1) {
			return Boolean.class;
		}

		if (this.displayType == DisplayType.Watchlist) {
			if (columnIndex == 2) {
				return PriceWrapper.class;
			}
			if (columnIndex == 3) {
				return double.class;
			}
		}
		return super.getColumnClass(columnIndex);
	}

	/**
	 * Invoked when a stock data's display type changed
	 * 
	 * @param stock stock data whose display type changed
	 */
	public void displayTypeChange(StockData stock) {

		if (stock.getDisplayType() == this.displayType) {
			stockAdd(stock);
		} else {
			stockRemove(stock);
		}

	}

	/**
	 * Invoked when stock data is added
	 * 
	 * @param stock the added stock data
	 */
	public void stockAdd(StockData stock) {
		// System.out.println("stockAdd:"+stock.getSymbol());
		if (hasSymbol(stock)) {
			return;
		}
		this.stockList.add(stock);
		this.checkboxMap.put(stock.getSymbol(), false);

		timerMap.put(stock.getSymbol(), createTimer(stock));
		if (displayType == DisplayType.Watchlist) {
			onTimerStart(stock);
		}

		this.fireTableDataChanged();

	}

	/**
	 * Invoked when stock data is removed
	 * 
	 * @param stock the removed stock data
	 */
	public void stockRemove(StockData stock) {
		if (!hasSymbol(stock)) {
			return;
		}

		this.stockList.remove(stock);
		this.checkboxMap.remove(stock.getSymbol());
		timerMap.remove(stock.getSymbol());

		this.fireTableDataChanged();
	}

	/**
	 * Invoked when stock data is updated
	 * 
	 * @param stock the updated stock data
	 */
	public void stockUpdate(StockData stock) {
		if (!hasSymbol(stock)) {
			return;
		}

		if (displayType == DisplayType.Watchlist) {
			onTimerStart(stock);
		}

		this.fireTableDataChanged();
	}

	/**
	 * Checks if a specific stock data is present.
	 * 
	 * @param stock the specific stock data
	 * @return true if present, false otherwise
	 */
	boolean hasSymbol(StockData stock) {
		if (!this.stockList.contains(stock))
			return false;
		return true;
	}

	/**
	 * Gets a list of symbols that are selected.
	 * 
	 * @return the list of symbols that are selected
	 */
	public ArrayList<String> getSelectionList() {
		ArrayList<String> lst = new ArrayList<String>();
		for (String symbol : checkboxMap.keySet()) {
			if (checkboxMap.get(symbol)) {
				lst.add(symbol);
			}
		}
		return lst;
	}

	public void deselectAll() {
		for (String symbol : checkboxMap.keySet()) {
			if (checkboxMap.get(symbol)) {
				checkboxMap.put(symbol, false);
			}
		}
	}

	/**
	 * Gets the list of stock data.
	 * 
	 * @return the list of stock data
	 */
	public ArrayList<StockData> getStocks() {
		return this.stockList;
	}

	/**
	 * Clears a lists and maps of this table model
	 */
	public void clearAll() {
		this.stockList.clear();
		this.checkboxMap.clear();
		this.timerMap.clear();
	}

	/**
	 * Invoked when a timer starts
	 * 
	 * @param stock the stock data
	 */
	private void onTimerStart(StockData stock) {
		if (timerMap.containsKey(stock.getSymbol()))
			timerMap.get(stock.getSymbol()).start();
		this.fireTableDataChanged();

	}

	/**
	 * Invoked when a timer ends
	 * 
	 * @param stock the stock data
	 */
	private void onTimerEnd(StockData stock) {
		timerMap.get(stock.getSymbol()).stop();
		this.fireTableDataChanged();
	}

	/**
	 * Creates and sets a new timer.
	 * 
	 * @param stock the stock data
	 * @return the new timer
	 */
	private Timer createTimer(StockData stock) {
		ActionListener al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onTimerEnd(stock);
			}
		};
		Timer timer = new Timer(500, al);
		timer.setRepeats(false);
		return timer;
	}

}
