package stocker.model.database;

import java.util.ArrayList;

import stocker.model.general.DisplayType;
import stocker.model.general.TimeInterval;
import stocker.model.stockdata.StockData;

/**
 * Observer pattern for the database (with 3 listener types
 * {@code DatabaseStockListener}, {@code DatabaseActionListener} and
 * {@code DatabaseStatusListener})
 * 
 * @author Christoph Kaplan
 *
 */
public class DatabaseObserverPattern {

	protected ArrayList<DatabaseStockListener> databaseStockObservers = new ArrayList<DatabaseStockListener>();
	protected ArrayList<DatabaseActionListener> databaseActionObservers = new ArrayList<DatabaseActionListener>();
	protected ArrayList<DatabaseStatusListener> databaseStatusObservers = new ArrayList<DatabaseStatusListener>();

	/**
	 * Adds a listener
	 * 
	 * @param obs the listener
	 */
	public void addDatabaseStockObserver(DatabaseStockListener obs) {
		databaseStockObservers.add(obs);
	}

	/**
	 * Removes a listener
	 * 
	 * @param obs the listener
	 */
	public void removeDatabaseStockObserver(DatabaseStockListener obs) {
		databaseStockObservers.remove(obs);
	}

	/**
	 * Adds a listener
	 * 
	 * @param obs the listener
	 */
	public void addDatabaseActionObserver(DatabaseActionListener obs) {
		databaseActionObservers.add(obs);
	}

	/**
	 * Removes a listener
	 * 
	 * @param obs the listener
	 */
	public void removeDatabaseActionObserver(DatabaseActionListener obs) {
		databaseActionObservers.remove(obs);
	}

	/**
	 * Adds a listener
	 * 
	 * @param obs the listener
	 */
	public void addDatabaseStatusObserver(DatabaseStatusListener obs) {
		databaseStatusObservers.add(obs);
	}

	/**
	 * Removes a listener
	 * 
	 * @param obs the listener
	 */
	public void removeDatabaseStatusObserver(DatabaseStatusListener obs) {
		databaseStatusObservers.remove(obs);
	}

	/**
	 * Notify listeners that a new {@code StockData} object was created
	 * 
	 * @param stock considered {@code StockData} object
	 */
	protected void onStockAdded(StockData stock) {
		for (DatabaseStockListener obs : databaseStockObservers) {
			obs.onStockAdded(stock);
		}
	}

	/**
	 * Notify listeners that a {@code StockData} object was removed
	 * 
	 * @param stock considered {@code StockData} object
	 */
	protected void onStockRemoved(StockData stock) {
		for(int i = databaseStockObservers.size()-1;i>=0; i--) {
			DatabaseStockListener obs = databaseStockObservers.get(i);
			obs.onStockRemoved(stock);
		}
	}

	/**
	 * Notify listeners that a {@code StockData} object was updated
	 * 
	 * @param stock considered {@code StockData} object
	 */
	protected void onStockUpdate(StockData stock) {
		for (DatabaseStockListener obs : databaseStockObservers) {
			obs.onStockUpdate(stock);
		}
	}

	/**
	 * Notify listeners that {@code DisplayType} of a {@code StockData} object was
	 * changed
	 * 
	 * @param stock considered {@code StockData} object
	 */
	protected void onStockDisplayTypeChange(StockData stock) {
		for (DatabaseStockListener obs : databaseStockObservers) {
			obs.onStockDisplayTypeChange(stock);
		}
	}

	/**
	 * Notify listeners that a {@code StockData} object is calculated
	 * 
	 * @param stock considered {@code StockData} object
	 */
	protected void onStockCalculated(StockData stock) {
		for (DatabaseStockListener obs : databaseStockObservers) {
			obs.onStockCalculated(stock);
		}
	}

	/**
	 * Notify listeners that an alarm was added
	 * 
	 * @param alarm the considered {@code AlarmWrapper} object
	 * @param value the added alarm value
	 */
	protected void onAlarmAdded(AlarmWrapper alarm, double value) {
		for (DatabaseStockListener obs : databaseStockObservers) {
			obs.onAlarmAdded(alarm, value);
		}
	}

	/**
	 * Notify listeners that an alarm was removed
	 * 
	 * @param alarm the considered {@code AlarmWrapper} object
	 * @param value the removed alarm value
	 */
	protected void onAlarmRemove(AlarmWrapper alarm, double value) {
		for (DatabaseStockListener obs : databaseStockObservers) {
			obs.onAlarmRemove(alarm, value);
		}
	}

	/**
	 * Asks listeners for general search data input.
	 * 
	 * @param search      the "search word"
	 * @param displayType associated {@code DisplayType}
	 */
	protected void onAskSearchDataForGeneral(String search, DisplayType displayType) {
		for (DatabaseActionListener obs : databaseActionObservers) {
			obs.onAskSearchDataForGeneral(search, displayType);
		}
	}

	/**
	 * Asks listeners for symbol search data input.
	 * 
	 * @param symbol      to be searched for
	 * @param displayType associated {@code DisplayType}
	 */
	protected void onAskSearchDataForSymbol(String symbol, DisplayType displayType) {
		for (DatabaseActionListener obs : databaseActionObservers) {
			obs.onAskSearchDataForSymbol(symbol, displayType);
		}
	}

	/**
	 * Asks listeners for historic data input.
	 * 
	 * @param symbol   to be searched for
	 * @param interval associated {@code TimeInterval}
	 */
	protected void onAskHistoricDataForSymbol(String symbol, TimeInterval interval) {
		for (DatabaseActionListener obs : databaseActionObservers) {
			obs.onAskHistoricDataForSymbol(symbol, interval);
		}
	}

	/**
	 * Asks listeners for quote data input.
	 * 
	 * @param symbol to be searched for
	 */
	protected void onAskQuoteDataForSymbol(String symbol) {
		for (DatabaseActionListener obs : databaseActionObservers) {
			obs.onAskQuoteDataForSymbol(symbol);
		}
	}

	/**
	 * Notify listeners to subscribe a symbol for real-time data.
	 * 
	 * @param symbol to subscribe
	 */
	protected void onSubscribe(String symbol) {
		for (DatabaseActionListener obs : databaseActionObservers) {
			obs.onSubscribe(symbol);
			;
		}
	}

	/**
	 * Notify listeners to unsubscribe a symbol for real-time data.
	 * 
	 * @param symbol to unsubscribe
	 */
	protected void onUnsubscribe(String symbol) {
		for (DatabaseActionListener obs : databaseActionObservers) {
			obs.onUnsubscribe(symbol);
			;
		}
	}

	/**
	 * Notify listeners of the database status.
	 * 
	 * @param status database status
	 */
	protected void onStatusChange(String status) {
		for (DatabaseStatusListener obs : databaseStatusObservers) {
			obs.onStatusChange(status);
		}
	}

	/**
	 * Notify listeners when alarm triggered
	 * 
	 * @param alarmWrapper the alarm wrapper
	 * @param value        the alarm value
	 */
	protected void onAlarmTriggered(AlarmWrapper alarmWrapper, double value) {
		for (DatabaseStatusListener obs : databaseStatusObservers) {
			obs.onAlarmTriggered(alarmWrapper, value);
		}
	}

}
