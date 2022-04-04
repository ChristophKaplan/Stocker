package stocker.model.database;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import stocker.model.externalclasses.PullData;
import stocker.model.externalclasses.PullQuoteData;
import stocker.model.externalclasses.PullSearchData;
import stocker.model.externalclasses.PushData;
import stocker.model.general.DisplayType;
import stocker.model.general.TimeInterval;
import stocker.model.stockdata.StockDataRealtime;
import stocker.model.stockdata.StockDataState.StockDataStateType;
import stocker.model.stockdata.StockData;
import stocker.model.stockdata.StockDataCalculator;

/**
 * The database model is responsible for holding and processing the stock
 * related data. The model consists of 3 components, an observer pattern,
 * DatabaseObserverPattern, implements 3 lists for 3 different observer types. A
 * DatabaseStockListener listens to changes in the stock data. The
 * DatabaseActionListener listens to active requests from the model for input
 * and “subscribing” to stocks. And a DatabaseStatusListener observes status
 * changes in the database, e.g. whether and to which server there is a
 * connection. In addition, the database model implements a list of the
 * StockData type and an assignment table {@code HashMap <String, AlarmWrapper>}. The
 * StockData class has the function of dividing the stock data according to
 * individual stocks. The key element, of the String type, of the allocation
 * table stands for the “symbol” of the shares. The value element of the type
 * AlarmWrapper has the function of summarizing and saving all alarms of a
 * share. In addition, the database model contains various methods such as.
 * Getter to access stocks and alarms under certain aspects. Setters and "Adder"
 * to change the status of certain stocks or to add new data from the provider.
 * Remover to delete stocks or alarms and other methods of notifying the
 * observer.
 * 
 * @author Christoph Kaplan
 *
 */
public class DatabaseModel extends DatabaseObserverPattern {

	private ArrayList<StockData> databaseList = new ArrayList<StockData>();
	private HashMap<String, AlarmWrapper> alarmWrapperMap = new HashMap<String, AlarmWrapper>();

	public DatabaseModel() {

	}

	////////////////////////////////////////////////////////
	//
	// GETTERs
	//
	////////////////////////////////////////////////////////

	/**
	 * Gets the database list with {@code StockData} entries.
	 * 
	 * @return a list of {@code StockData}
	 */
	public ArrayList<StockData> getDatabaseList() {
		return databaseList;
	}

	/**
	 * Gets list of {@code StockData}, filtered by {@code DisplayType}.
	 * 
	 * @param displayType the {@code DisplayType} filter
	 * @return filtered list of {@code StockData}
	 */
	public ArrayList<StockData> getAllByDisplayType(DisplayType displayType) {
		ArrayList<StockData> filtered = new ArrayList<StockData>();
		for (StockData s : databaseList) {
			if (s.getDisplayType() == displayType)
				filtered.add(s);
		}
		return filtered;
	}

	/**
	 * Gets a list of symbols, filtered by {@code DisplayType}.
	 * 
	 * @param displayType a {@code DisplayType} filter
	 * @return filtered symbol list.
	 */
	public ArrayList<String> getSymbolListByDisplayType(DisplayType displayType) {
		ArrayList<String> lst = new ArrayList<String>();
		for (StockData s : databaseList) {
			if (s.getDisplayType() == displayType)
				lst.add(s.getSymbol());
		}
		return lst;
	}

	/**
	 * Checks, if a specific {@code StockData} object is present, by symbol.
	 * 
	 * @param symbol corresponding to a stock whose presence is checked.
	 * @return true if present, false if not.
	 */
	public boolean hasStock(String symbol) {
		for (StockData s : databaseList) {
			if (s.getSymbol().equals(symbol))
				return true;
		}
		return false;
	}

	/**
	 * Gets a specific {@code StockData} object by symbol.
	 * 
	 * @param symbol determines which {@code StockData} object is returned
	 * @return {@code StockData} object
	 * @throws Exception when no {@code StockData} object can be found
	 */
	public StockData getStockBySymbol(String symbol) throws Exception {
		for (StockData s : databaseList) {
			if (s.getSymbol().equals(symbol))
				return s;
		}
		throw new Exception("getStockBySymbol(" + symbol + "): cant find symbol");
	}

	/**
	 * Gets the alarmWrapper Map.
	 * 
	 * @return alarmWrapper Map
	 */
	public HashMap<String, AlarmWrapper> getAlarmWrapperMap() {
		return this.alarmWrapperMap;
	}

	/**
	 * Gets a list of all {@code AlarmWrapper} objects.
	 * 
	 * @return list of all {@code AlarmWrapper} objects
	 */
	public ArrayList<AlarmWrapper> getAlarmWrapperList() {
		ArrayList<AlarmWrapper> lst = new ArrayList<AlarmWrapper>();
		for (AlarmWrapper a : this.alarmWrapperMap.values()) {
			lst.add(a);
		}
		return lst;
	}

	/**
	 * Gets a list of alarms corresponding to a specific {@code StockData} object by
	 * symbol
	 * 
	 * @param symbol determines of whose {@code StockData} object, the alarms are
	 *               returned
	 * @return list of alarms corresponding to a specific {@code StockData} object
	 */
	public ArrayList<Double> getAlarms(String symbol) {
		if (!hasAlarm(symbol))
			return null;
		return this.getAlarm(symbol).getAlarms();
	}

	/**
	 * Gets an {@code AlarmWrapper} corresponding to a specific {@code StockData}
	 * object by symbol
	 * 
	 * @param symbol determines of which {@code StockData} object the
	 *               {@code AlarmWrapper} object is returned
	 * @return {@code AlarmWrapper} object corresponding to a specific
	 *         {@code StockData} by symbol
	 */
	public AlarmWrapper getAlarm(String symbol) {
		if (!hasAlarm(symbol))
			return null;
		return this.alarmWrapperMap.get(symbol);
	}

	/**
	 * Checks if a specific {@code StockData} object has an alarm or not.
	 * 
	 * @param symbol corresponding to a {@code StockData} object whose presence is
	 *               checked
	 * @return true if present, false if not.
	 */
	public boolean hasAlarm(String symbol) {
		return this.alarmWrapperMap.containsKey(symbol);
	}

	////////////////////////////////////////////////////////
	//
	// SETTERs
	//
	////////////////////////////////////////////////////////

	/**
	 * Sets the {@code DisplayType} of a specific {@code StockData} .
	 * 
	 * @param stock       {@code StockData} whose {@code StockData} is being changed
	 * @param displayType {@code StockData} value to set
	 */
	public void setDisplayType(StockData stock, DisplayType displayType) {
		if (stock.getDisplayType() == displayType)
			return; // no change
		stock.setDisplayType(displayType);
		onStockDisplayTypeChange(stock);
	}

	////////////////////////////////////////////////////////
	//
	// ADDERs
	//
	////////////////////////////////////////////////////////

	/**
	 * Wrapper method for {@link #createNewStock}, avoids a return value, and avoids
	 * double entries. Creates new {@code StockData} object and appends it to the
	 * database list.
	 * 
	 * @param symbol      of the new {@code StockData} object
	 * @param displayType the {@code DisplayType} value of the new {@code StockData}
	 *                    object
	 */
	public void addStock(String symbol, DisplayType displayType) {
		if (!hasStock(symbol))
			createNewStock(symbol, displayType);
	}

	/**
	 * Creates a new {@code StockData} object and appends it to the database list.
	 * 
	 * @param symbol      of the new {@code StockData} object
	 * @param displayType the {@code DisplayType} value of the new {@code StockData}
	 *                    object
	 * @return the new {@code StockData} object
	 */
	private StockData createNewStock(String symbol, DisplayType displayType) {
		StockData newStock = new StockData(symbol, displayType);
		databaseList.add(newStock);
		stockCreated(newStock);
		return newStock;
	}

	/**
	 * Adds search data. contained by {@code PullSearchData}, to the corresponding
	 * {@code StockData} object and {@code DisplayType}. {@code PullSearchData}
	 * object contains a list of {@code PullSearchData.Entry} Uses
	 * {@link #addPullSearchDataEntry} method to add {@code PullSearchData.Entry}
	 * objects.
	 * 
	 * @param pullSearchData of type {@code PullSearchData}, contains search data.
	 * @param displayType    the {@code DisplayType} value
	 */
	public void addPullSearchData(PullSearchData pullSearchData, DisplayType displayType) {
		if(!hasSearchNewInformation(pullSearchData)) {
			return;
		}
		
		for (PullSearchData.Entry entry : pullSearchData.getResults()) {
			addPullSearchDataEntry(entry, displayType);
		}
	}

	/**
	 * Checks if the search result is either empty or if the available data is already stored
	 * @param pullSearchData search data
	 * @return true if search data is relevant
	 */
	private boolean hasSearchNewInformation(PullSearchData pullSearchData) {
		int newInformationCount = 0;
		for (PullSearchData.Entry entry : pullSearchData.getResults()) {
			if(!hasStock(entry.getSymbol())){
				newInformationCount++;
			}
		}
		if(newInformationCount > 0) {
			return true;
		}
		String msg = "available information already stored";
		if(	pullSearchData.getResults().length == 0) {
			msg = "no information found";
		}
		JOptionPane.showMessageDialog(null,"search done, " + msg); 
		return false;
	}
	
	/**
	 * Adds search data, which {@code PullSearchData.Entry} contains, to the
	 * corresponding {@code StockData} object and {@code DisplayType}. Creates a new
	 * {@code StockData} object if none exists.
	 * 
	 * @param entry       of type {@code PullSearchData.Entry}, contains search
	 *                    data.
	 * @param displayType the {@code DisplayType} value
	 */
	public void addPullSearchDataEntry(PullSearchData.Entry entry, DisplayType displayType) {
		StockData stock;
		try {
			stock = getStockBySymbol(entry.getSymbol());
		} catch (Exception e) {
			stock = createNewStock(entry.getSymbol(), displayType);
		}
		stock.addPullSearchDataEntry(entry);
		stockPullSearchUpdate(stock);
	}

	/**
	 * Adds historic data, which {@code PullData} contains, to the corresponding
	 * {@code StockData} and {@code  TimeInterval} by symbol.
	 * 
	 * @param symbol   identifies the relevant {@code StockData} object
	 * @param pullData {@code PullData} object that contains the historic data
	 * @param interval identifies the relevant {@code TimeInterval} value, which the
	 *                 {@code StockData} object is mapped to.
	 */
	public void addPullData(String symbol, PullData pullData, TimeInterval interval) {
		try {
			StockData stock = getStockBySymbol(symbol);
			stock.addPullData(pullData, interval);
			stockPullUpdate(stock);

		} catch (Exception e) {
			System.err.println("addPullData():" + e.getMessage());
			// e.printStackTrace();
		}

	}

	/**
	 * Adds quote data, which {@code PullQuoteData} contains, to the corresponding
	 * {@code StockData} by symbol.
	 * 
	 * @param symbol    identifies the relevant {@code StockData} object
	 * @param quoteData {@code PullQuoteData} object that contains the quote data
	 */
	public void addPullQuoteData(String symbol, PullQuoteData quoteData) {
		try {
			StockData stock = getStockBySymbol(symbol);
			stock.addPullQuoteData(quoteData);
			stockQuoteUpdate(stock);
		} catch (Exception e) {
			System.err.println("addPullQuoteData():" + e.getMessage());
		}
	}

	/**
	 * Resets the relevant {@code StockData} object when no provider data is
	 * available {@link stocker.model.stockdata.StockData#setStockDataSearchState}.
	 * 
	 * @param symbol identifies the relevant {@code StockData} object
	 */
	public void noPullSearchDataFound(String symbol) {
		try {
			StockData stock = getStockBySymbol(symbol);
			stock.setStockDataSearchState(StockDataStateType.no_access);
			stockPullSearchUpdate(stock);
		} catch (Exception e) {
			System.err.println("noPullSearchDataFound():" + e.getMessage());
		}
	}

	/**
	 * Resets the relevant {@code StockData} object when no provider data is
	 * available
	 * {@link stocker.model.stockdata.StockData#setStockDataHistoricState}.
	 * 
	 * @param symbol       identifies the relevant {@code StockData} object
	 * @param timeInterval the time interval
	 */
	public void noPullDataFound(String symbol, TimeInterval timeInterval) {
		try {
			StockData stock = getStockBySymbol(symbol);
			stock.setStockDataHistoricState(timeInterval, StockDataStateType.no_access);
			stockPullUpdate(stock);
		} catch (Exception e) {
			System.err.println("noPullDataFound():" + e.getMessage());
		}
	}

	/**
	 * Resets the relevant {@code StockData} object when no provider data is
	 * available {@link stocker.model.stockdata.StockData#setStockDataQuoteState}.
	 * 
	 * @param symbol identifies the relevant {@code StockData} object
	 */
	public void noPullQuoteDataFound(String symbol) {
		try {
			StockData stock = getStockBySymbol(symbol);
			stock.setStockDataQuoteState(StockDataStateType.no_access);
			stockQuoteUpdate(stock);
		} catch (Exception e) {
			System.err.println("noPullQuoteDataFound():" + e.getMessage());
		}
	}

	/**
	 * Adds real-time data, which {@code PushData} contains, to the corresponding
	 * {@code StockData} object.
	 * 
	 * @param pushData {@code PushData} object that contains the real-time data
	 */
	public void addPushData(PushData pushData) {
		// System.out.println("addPushData()");
		for (PushData.Entry e : pushData.getData()) {
			try {
				StockData stock = getStockBySymbol(e.getSymbol());
				StockDataRealtime rcd = new StockDataRealtime(e.getSymbol(), e.getPrice(), e.getTime(),
						e.getVolume());
				stock.addPushData(rcd);
				stockPushUpdate(stock);

			} catch (Exception ex) {
				System.err.println("addPushData():" + ex.getMessage());
			}
		}

	}

	/**
	 * Adds an alarm to the {@code AlarmWrapper} hash-map. If there is no
	 * {@code AlarmWrapper} object present, it creates new one, otherwise adds to
	 * the existing {@code AlarmWrapper} object. Sends a notification to listeners.
	 * 
	 * @param symbol symbol whose the alarm is mapped to
	 * @param value  the actual alarm value
	 */
	public void addAlarm(String symbol, double value) {
		if (!hasAlarm(symbol))
			this.alarmWrapperMap.put(symbol, new AlarmWrapper(symbol));

		AlarmWrapper alarm = this.getAlarm(symbol);
		alarm.addAlarm(value);
		this.onAlarmAdded(alarm, value);
	}

	/**
	 * Adds an {@code AlarmWrapper} object .
	 * 
	 * @param alarmWrapper {@code AlarmWrapper} object to add
	 */
	public void addAlarm(AlarmWrapper alarmWrapper) {
		this.alarmWrapperMap.put(alarmWrapper.getSymbol(), alarmWrapper);
		// this.onAlarmAdded(alarm, value);
	}

	/**
	 * Adds 3 lists of persisted data, symbols of the "Watchlist" and "ChartsOnly"
	 * {@code DisplayType} and the list of {@code AlarmWrapper}.
	 * 
	 * @param watchlistSymbols  String list of symbols
	 * @param chartsOnlySymbols String list of symbols
	 * @param alarmList         list of {@code AlarmWrapper}
	 */
	public void addPersistedData(ArrayList<String> watchlistSymbols, ArrayList<String> chartsOnlySymbols,
			ArrayList<AlarmWrapper> alarmList) {
		for (String s : watchlistSymbols) {
			addStock(s, DisplayType.Watchlist);
		}
		for (String s : chartsOnlySymbols) {
			addStock(s, DisplayType.ChartOnly);
		}
		for (AlarmWrapper a : alarmList) {
			addAlarm(a);
		}
	}

	////////////////////////////////////////////////////////
	//
	// REMOVERs
	//
	////////////////////////////////////////////////////////

	/**
	 * Removes all {@code StockData} objects from the database list with a certain
	 * {@code DisplayType} value.
	 * 
	 * @param displayType {@code DisplayType} value that determines which
	 *                    {@code StockData} objects are cleared.
	 */
	public void clearDatabaseByDisplayType(DisplayType displayType) {
		for (int i = databaseList.size()-1; i >= 0; i--) {
			StockData stock = databaseList.get(i);
			if (stock.getDisplayType().equals(displayType)) {
				this.deleteStock(stock);
			}
		}
	}

	/**
	 * Resets all present {@code StockData} objects, in the database list, to a
	 * "no_data" state.
	 */
	public void resetStockDataState() {
		for (int i = 0; i < databaseList.size(); i++) {
			StockData stock = databaseList.get(i);
			stock.setStockDataHistoricStateAll(StockDataStateType.unclear);
			stock.setStockDataQuoteState(StockDataStateType.unclear);
			stock.setStockDataSearchState(StockDataStateType.unclear);
			
			stock.clearRealtimeData();
			stock.clearStockDataCalculatorMap();
		}
	}

	/**
	 * Removes a specific {@code StockData} object from the database list.
	 * 
	 * @param symbol identifies the {@code StockData} object
	 */
	public void deleteStock(String symbol) {
		try {
			StockData stock = getStockBySymbol(symbol);
			deleteStock(stock);
		} catch (Exception e) {
			System.out.println("deleteStock() cant delete stock:" + e.getMessage());
		}
	}

	/**
	 * Removes a specific {@code StockData} object from the database list.
	 * 
	 * @param stockData the {@code StockData} object to be removed.
	 */
	public void deleteStock(StockData stockData) {
		stockRemoved(stockData);
		databaseList.remove(stockData);
	}

	/**
	 * Removes a certain alarm value.
	 * 
	 * @param symbol identifies the {@code StockData} object
	 * @param value  identifies the alarm value to be removed from the given
	 *               {@code StockData} object
	 */
	public void removeAlarm(String symbol, double value) {
		AlarmWrapper alarm = this.getAlarm(symbol);
		alarm.removeAlarm(value);
		onAlarmRemove(alarm, value);
	}

	/**
	 * Removes all alarms from a specific {@code StockData} object.
	 * 
	 * @param symbol identifies the {@code StockData} object
	 */
	public void clearAlarms(String symbol) {
		this.alarmWrapperMap.get(symbol).clearAlarms();
		this.alarmWrapperMap.remove(symbol);
	}

	/**
	 * Removes all alarms from the alarm wrapper hash-map.
	 */
	public void clearAllAlarms() {
		this.alarmWrapperMap.clear();
	}

	/**
	 * Checks a specific {@code StockData} object if any alarm is triggered.
	 * 
	 * @param stockData {@code StockData} object whose alarms are checked.
	 */
	void isAlarmTriggered(StockData stockData) {
		String symbol = stockData.getSymbol();
		if (!this.alarmWrapperMap.containsKey(symbol))
			return;

		double currentPrice = stockData.getCurrentPrice();
		double previousPrice = stockData.getPreviousPrice();
		AlarmWrapper alarmWrapper = this.getAlarm(symbol);

		try {
			double triggered = alarmWrapper.isTriggered(currentPrice, previousPrice);
			this.onAlarmTriggered(alarmWrapper, triggered);
			alarmWrapper.removeAlarm(triggered);
		} catch (Exception e) {
			//
		}

	}

	////////////////////////////////////////////////////////
	//
	// CALCUATIONs
	//
	////////////////////////////////////////////////////////

	/**
	 * Gets {@code StockDataCalculator} object which contains the final data result
	 * of historic and real-time data combined, for a specific {@code TimeInterval}.
	 * If {@code StockDataStateType} is {@code unclear}, a new calculation is asked
	 * for {@link #calculateStockData}.
	 * 
	 * @param stockData {@code StockData} object whose calculated data is asked for
	 * @param interval  considered {@code TimeInterval}
	 * @return {@code StockDataCalculator} object that contains the final data
	 *         result of historic and real-time data combined, for a specific
	 *         {@code TimeInterval}
	 */
	public StockDataCalculator getStockDataCalculator(StockData stockData, TimeInterval interval) {
		// System.out.println("getStockDataCalculator (" + stockData.getSymbol() + ", "
		// + interval.toString()+ ") -> state:" +
		// stockData.getStockDataCalculator(interval).getStockDataState());

		StockDataCalculator temp = stockData.getStockDataCalculator(interval);
		if (temp.getStockDataState() == StockDataStateType.unclear) {
			calculateStockData(stockData, interval);
		}
		
		return stockData.getStockDataCalculator(interval);
	}

	/**
	 * This method first checks whether historical data is available for the desired
	 * time interval, since the historical data represent a minimum requirement for
	 * the graphic preparation. If it does not exist, the relevant observers are
	 * notified to request the desired data from the provider and the method is
	 * terminated. If the desired historical data has status
	 * {@code StockDataStateType} {@code no_access}, the status is transferred to
	 * the {@code StockDataCalculator} object and the method is terminated. However,
	 * if historical data is available, the next step is to check for real-time
	 * data. A newly created {@code StockDataCalculator} object is assigned both
	 * data types (real-time & historical) or only the historical data, depending on
	 * availability.
	 * 
	 * 
	 *
	 * @param stockData {@code StockData} object whose calculator data is asked for
	 * @param interval  considered {@code TimeInterval}
	 */
	private void calculateStockData(StockData stockData, TimeInterval interval) {
		// history data is minimum needed !

		if (stockData.getStockDataHistoric(interval).getStockDataState() == StockDataStateType.no_access) {
			// set Calculator Data state to no access
			stockData.getStockDataCalculator(interval).setStockDataState(StockDataStateType.no_access);
			return;
		}

		if (stockData.getStockDataHistoric(interval).getStockDataState() == StockDataStateType.unclear) {
			askHistoricDataForSymbol(stockData.getSymbol(), interval);
			return;
		}

		StockDataCalculator newCandleData;
		// if real-time data is missing we can use historic data only
		if (!stockData.realtimeDataAvailable()) {
			// System.out.println("calculateCandles() only for historic data" +
			// stockData.getStockDataHistoric(interval).getStockDataState());
			newCandleData = new StockDataCalculator(stockData.getStockDataHistoric(interval), interval);
		} else {
			// System.out.println("calculateCandles() success, ");
			newCandleData = new StockDataCalculator(stockData.getStockDataHistoric(interval), stockData.getRealtimeStockData(), interval);
		}

		stockData.putStockDataCalculatorMap(interval, newCandleData);

		// notifiy
		stockCalculated(stockData);
	}

	/**
	 * Updates the {@code StockDataCalculator} objects for every present
	 * {@code TimeInterval} in the case of newly arriving data.
	 * {@code StockDataCalculator} objects are recalculated using method {@link #calculateStockData}.
	 * 
	 * @param stockData {@code StockData} object whose calculator data is asked for
	 */
	private void updateStockDataCalculator(StockData stockData) {
		//System.out.println("updateStockDataCalculator()" + stockData.getSymbol());
		for (TimeInterval t : stockData.getStockDataCalculatorMap().keySet()) {

			//System.out.println("----> " + stockData.getStockDataCalculatorMap().get(t).getStockDataState());
			StockDataStateType state = stockData.getStockDataCalculatorMap().get(t).getStockDataState();
			if (state == StockDataStateType.unclear || state == StockDataStateType.access) {
				try {
					calculateStockData(stockData, t);
				} catch (Exception e) {
					continue;
				}
			}

		}

	}

	////////////////////////////////////////////////////////
	//
	// Observer Notifiers
	//
	////////////////////////////////////////////////////////

	/**
	 * Asks listeners for general search data input.
	 * 
	 * @param search      the "search word"
	 * @param displayType associated {@code DisplayType}
	 */
	public void askSearchDataForGeneral(String search, DisplayType displayType) {
		onAskSearchDataForGeneral(search, displayType);
	}

	/**
	 * Asks listeners for symbol search data input.
	 * 
	 * @param symbol      to be searched for
	 * @param displayType associated {@code DisplayType}
	 */
	public void askSearchDataForSymbol(String symbol, DisplayType displayType) {
		onAskSearchDataForSymbol(symbol, displayType);
	}

	/**
	 * Asks listeners for historic data input.
	 * 
	 * @param symbol   to be searched for
	 * @param interval associated {@code TimeInterval}
	 */
	public void askHistoricDataForSymbol(String symbol, TimeInterval interval) {
		onAskHistoricDataForSymbol(symbol, interval);
	}

	/**
	 * Asks listeners for quote data input.
	 * 
	 * @param symbol to be searched for
	 */
	public void askQuoteDataForSymbol(String symbol) {
		onAskQuoteDataForSymbol(symbol);
	}

	/**
	 * Notify listeners to subscribe a symbol for real-time data.
	 * 
	 * @param symbol    to subscribe
	 * @param subscribe true for subscribe, false for unsubscribe
	 */
	public void subscribeStock(String symbol, boolean subscribe) {
		if (subscribe)
			this.onSubscribe(symbol);
		else
			this.onUnsubscribe(symbol);
	}

	/**
	 * Asks listeners for missing data within all {@code StockData} objects, present
	 * in the database list.
	 * 
	 * @param interval associated {@code TimeInterval}
	 */
	public void askMissingDataAll(TimeInterval interval) {
		for (StockData stock : getDatabaseList()) {
			askMissingDataStockAndSubscribe(stock, interval);
			onStockAdded(stock);
		}
	}

	/**
	 * Asks listeners for all missing data within given {@code StockData} object.
	 * 
	 * @param stockData considered {@code StockData} object
	 * @param interval  associated {@code TimeInterval}
	 */
	public void askMissingDataStockAndSubscribe(StockData stockData, TimeInterval interval) {
		if (stockData.getStockDataSearch().getStockDataState() == StockDataStateType.unclear) {
			askSearchDataForSymbol(stockData.getSymbol(), stockData.getDisplayType());
		}
		if (stockData.getStockDataHistoric(interval).getStockDataState() == StockDataStateType.unclear) {
			askHistoricDataForSymbol(stockData.getSymbol(), interval);
		}
		if (stockData.getStockDataQuote().getStockDataState() == StockDataStateType.unclear) {
			askQuoteDataForSymbol(stockData.getSymbol());
		}
		subscribeStock(stockData.getSymbol(), true);
	}

	////////////////////////////////////////////////////////
	//
	// Notifiers (+ routing of Notifiers)
	//
	////////////////////////////////////////////////////////

	/**
	 * Notify listeners that a new {@code StockData} object was created
	 * 
	 * @param stock considered {@code StockData} object
	 */
	private void stockCreated(StockData stock) {
		onStockAdded(stock);
	}

	/**
	 * Notify listeners that a {@code StockData} object was removed
	 * 
	 * @param stock considered {@code StockData} object
	 */
	private void stockRemoved(StockData stock) {
		onStockRemoved(stock);
		subscribeStock(stock.getSymbol(), false);
	}

	/**
	 * Routes quote data update to {@code #finalStockUpdate}
	 * 
	 * @param stock considered {@code StockData} object
	 */
	private void stockQuoteUpdate(StockData stock) {	
		finalStockUpdate(stock);
	}

	/**
	 * Routes search data update to {@code #finalStockUpdate}
	 * 
	 * @param stock considered {@code StockData} object
	 */
	private void stockPullSearchUpdate(StockData stock) {
		finalStockUpdate(stock);
	}

	/**
	 * Routes historic data update to {@code #finalStockUpdate}
	 * 
	 * @param stock considered {@code StockData} object
	 */
	private void stockPullUpdate(StockData stock) {
		//System.out.println("stockPullUpdate():" + stock.getSymbol());
		updateStockDataCalculator(stock);
		finalStockUpdate(stock);
	}

	/**
	 * Routes push update to {@code #finalStockUpdate} and checks if alarm is
	 * triggered
	 * 
	 * @param stock considered {@code StockData} object
	 */
	private void stockPushUpdate(StockData stock) {
		//System.out.println("stockPushUpdate():" + stock.getSymbol());
		updateStockDataCalculator(stock);
		isAlarmTriggered(stock);
		finalStockUpdate(stock);
	}

	/**
	 * Notify listeners that a {@code StockData} object is updated
	 * 
	 * @param stock considered {@code StockData} object
	 */
	private void finalStockUpdate(StockData stock) {
		onStockUpdate(stock);
	}

	/**
	 * Notify listeners that a {@code StockData} object is calculated
	 * 
	 * @param stock considered {@code StockData} object
	 */
	private void stockCalculated(StockData stock) {
		onStockCalculated(stock);
	}

	////////////////////////////////////////////////////////
	//
	// OTHER
	//
	////////////////////////////////////////////////////////

	/**
	 * Notifies listener of the database status.
	 * 
	 * @param status database status
	 */
	public void statusChange(String status) {
		this.onStatusChange(status);
	}

	/**
	 * Helper method, print the database list content to console
	 */
	public void printDB() {
		System.out.println("printDB() size:" + databaseList.size());
		for (StockData s : databaseList) {
			System.out.println("-> " + s.getSymbol() + " - " + s.getDisplayType());
		}
	}

}
