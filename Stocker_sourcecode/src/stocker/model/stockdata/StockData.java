package stocker.model.stockdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import stocker.model.externalclasses.PullData;
import stocker.model.externalclasses.PullQuoteData;
import stocker.model.externalclasses.PullSearchData;
import stocker.model.general.DisplayType;
import stocker.model.general.TimeInterval;
import stocker.model.stockdata.StockDataState.StockDataStateType;

/**
 * 
 * This class contains various data about a particular stock. Search query data {@code StockDataSearch},
 * data from the quote query {@code StockDataQuote}, real-time data, historical data and the data
 * calculated from it. The "calculated data" as well as the historical data {@code StockDataHistoric} are
 * always related to a certain time interval (TimeInterval), so an allocation
 * table for data management is ideal. Real-time data {@code StockDataRealtime} is kept as a list. Various
 * getter methods for querying the pull data create a new "dummy" object with
 * the data access status {@code StockDataStateType.unclear}.
 * {@link #getStockDataHistoric(TimeInterval)}, {@link #getStockDataQuote()} and {@link #getStockDataSearch()}
 * 
 *
 * 
 * @author Christoph Kaplan
 */
public class StockData {
	// stockID
	private String symbol;
	// DisplayType determines, where this stock object needs to be showed visually.
	private DisplayType displayType;

	// Data
	/** Wrapper class, that stores the retrieved {@code StockDataSearch} objects. */
	private StockDataSearch stockDataSearch;
	/** Wrapper class, that stores the retrieved {@code StockDataQuote} objects. */
	private StockDataQuote stockDataQuote;
	/**
	 * Hash-map that stores and maps a {@code TimeInterval} value with the
	 * corresponding {@code StockDataHistoric} object.
	 */
	private Map<TimeInterval, StockDataHistoric> stockDataHistoricMap;

	/** List that stores the retrieved {@code StockDataRealtime} objects. */
	private ArrayList<StockDataRealtime> stockDataRealtime;

	/**
	 * Hash-map that stores and maps a {@code TimeInterval} with the
	 * {@code StockDataCalculator} objects.
	 */
	private Map<TimeInterval, StockDataCalculator> stockDataCalculatorMap;

	// Constructor
	public StockData(String symbol, DisplayType displayType) {
		this.symbol = symbol;
		this.displayType = displayType;
		this.stockDataCalculatorMap = new HashMap<TimeInterval, StockDataCalculator>();
		this.stockDataHistoricMap = new HashMap<TimeInterval, StockDataHistoric>();
		this.stockDataRealtime = new ArrayList<StockDataRealtime>();
		this.stockDataQuote = new StockDataQuote();
		this.stockDataSearch = new StockDataSearch();
	}

	////////////////////////////////////////////////////////
	//
	// AVAILABLILITY CHECKERs
	//
	////////////////////////////////////////////////////////

	/**
	 * Checks if calculated data, for a specific interval, is available or not.
	 * 
	 * @param interval the {@code TimeInterval} value whose presence is to be
	 *                 checked.
	 * @return true if data, for the interval, is available and false if there is no
	 *         data available.
	 */
	public boolean isCalculatorDataAvailable(TimeInterval interval) {
		if (this.stockDataCalculatorMap != null && this.stockDataCalculatorMap.containsKey(interval))
			return true;
		else
			return false;
	}

	/**
	 * Checks if search data is available or not.
	 * 
	 * @return true if data is available and false if there is no data available.
	 */
	public boolean searchDataAvailable() {
		if (this.stockDataSearch != null)
			return true;
		else
			return false;
	}

	/**
	 * Checks if historic data, for a specific {@code TimeInterval} value, is
	 * available or not.
	 * 
	 * @param interval the {@code TimeInterval} value whose presence is to be
	 *                 checked.
	 * @return true if data, for the interval, is available and false if there is no
	 *         data available.
	 */
	public boolean historicDataAvailable(TimeInterval interval) {
		if (this.stockDataHistoricMap != null && this.stockDataHistoricMap.containsKey(interval)) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if real-time data is available or not.
	 * 
	 * @return true if data is available and false if there is no data available.
	 */
	public boolean realtimeDataAvailable() {
		if (this.stockDataRealtime != null && this.stockDataRealtime.size() > 1)
			return true;
		else
			return false;
	}

	/**
	 * Checks if quote data is available or not.
	 * 
	 * @return true if data is available and false if there is no data available.
	 */
	public boolean quoteDataAvailable() {
		if (this.stockDataQuote != null)
			return true;
		else
			return false;
	}

	////////////////////////////////////////////////////////
	//
	// GETTERs
	//
	////////////////////////////////////////////////////////
	/**
	 * Gets the symbol.
	 * 
	 * @return the symbol
	 */
	public String getSymbol() {
		return this.symbol;
	}

	/**
	 * Gets the current {@code DisplayType} value.
	 * 
	 * @return the current {@code DisplayType} value.
	 */
	public DisplayType getDisplayType() {
		return this.displayType;
	}

	/**
	 * Gets the current price.
	 * 
	 * @return current price If real-time data is available, returns the latest
	 *         real-time price. If real-time data is not available, returns the
	 *         "current price of the day" from quote data. Returns 0 is no data is
	 *         available.
	 */
	public double getCurrentPrice() {
		if (realtimeDataAvailable()) {
			int ind = stockDataRealtime.size() - 1;
			return stockDataRealtime.get(ind).getPrice();
		}
		if (quoteDataAvailable()) {
			return this.stockDataQuote.getCurrentPriceOfTheDay();
		}
		return 0f;
	}

	/**
	 * Gets the previous price.
	 * 
	 * @return previous price. If real-time data is available, returns the price
	 *         before the latest real-time entry. If real-time data is not
	 *         available, returns the previous close price from quote data. Returns
	 *         0 is no data is available.
	 */
	public double getPreviousPrice() {
		if (realtimeDataAvailable()) {
			int ind = stockDataRealtime.size() - 2;
			return stockDataRealtime.get(ind).getPrice();
		}
		if (quoteDataAvailable()) {
			return this.getPreviousClosePrice();
		}
		return 0f;
	}

	/**
	 * Gets the price difference between the current and the previous price.
	 * 
	 * @return the price difference between the current and the previous price
	 *         (getCurrentPrice() - getPreviousPrice()).
	 */
	public double getPriceDiff() {
		return getCurrentPrice() - getPreviousPrice();
	}

	/**
	 * Gets returns the "current price of the day" from quote data.
	 * 
	 * @return "current price of the day" from quote data, if quote data is
	 *         available, otherwise returns 0.
	 */
	public double getCurrentPriceOfTheDay() {
		if (quoteDataAvailable()) {
			return this.stockDataQuote.getCurrentPriceOfTheDay();
		}
		return 0f;
	}

	/**
	 * Gets returns the "previous close price" from quote data, the last price of
	 * the previous day.
	 * 
	 * @return "previous close price" from quote data, if quote data is available,
	 *         otherwise returns 0.
	 */
	public double getPreviousClosePrice() {
		if (quoteDataAvailable()) {
			return this.stockDataQuote.getPreviousClosePrice();
		}
		return 0f;
	}

	/**
	 * Gets the change percentage.
	 * 
	 * @return the change percentage. calculates the change price in percentage,
	 *         (100 * latestPrice / currentPriceOfTheDay) - 100.
	 */
	public double getChangePercentage() {
		double currentPriceOfTheDay = getCurrentPriceOfTheDay();
		if (currentPriceOfTheDay == 0) return 0;
		
		double latestPrice = getCurrentPrice();
		
		// return (100 * latestPrice / currentPriceOfTheDay) - 100;				
		return ((latestPrice - currentPriceOfTheDay) / currentPriceOfTheDay) * 100;
	}

	/**
	 * Gets the calculated stock data map.
	 * 
	 * @return hash-map stockDataCalculatorMap, the calculated stock data map.
	 */
	public Map<TimeInterval, StockDataCalculator> getStockDataCalculatorMap() {
		return this.stockDataCalculatorMap;
	}

	/**
	 * Gets {@code StockDataCalculator} object by {@code TimeInterval} value.
	 * 
	 * @param timeInterval {@code TimeInterval} value corresponding to the
	 *                     {@code StockDataCalculator} object.
	 * @return return {@code StockDataCalculator} object
	 */
	public StockDataCalculator getStockDataCalculator(TimeInterval timeInterval) {
		if (!isCalculatorDataAvailable(timeInterval)) {
			this.stockDataCalculatorMap.put(timeInterval, new StockDataCalculator());
		}
		return this.stockDataCalculatorMap.get(timeInterval);
	}

	/**
	 * Gets the list of {@code StockDataRealtime} objects.
	 * 
	 * @return the list of {@code StockDataRealtime} objects
	 */
	public ArrayList<StockDataRealtime> getRealtimeStockData() {
		return this.stockDataRealtime;
	}

	/**
	 * Gets the {@code StockDataRealtime} list as array
	 * 
	 * @return the {@code StockDataRealtime} list as array
	 */
	public StockDataRealtime[] getRealtimeStockDataArray() {
		return this.stockDataRealtime.toArray(new StockDataRealtime[this.stockDataRealtime.size()]);
	}

	/**
	 * Gets {@code StockDataHistoric} object by {@code TimeInterval} value. if not
	 * available, creates a new empty instance with state
	 * {@code StockDataStateType.unclear}
	 * 
	 * @param interval {@code TimeInterval} value corresponding to the
	 *                 {@code StockDataHistoric} object.
	 * @return return {@code StockDataHistoric} object
	 */
	public StockDataHistoric getStockDataHistoric(TimeInterval interval) {
		if (!this.historicDataAvailable(interval)) {
			this.stockDataHistoricMap.put(interval, new StockDataHistoric());
		}
		return this.stockDataHistoricMap.get(interval);
	}

	/**
	 * Gets the {@code StockDataSearch} object if not available, creates a new empty
	 * instance with state {@code StockDataStateType.unclear}
	 * 
	 * @return the {@code StockDataSearch} object
	 */
	public StockDataSearch getStockDataSearch() {
		if (!this.searchDataAvailable()) {
			this.stockDataSearch = new StockDataSearch();
		}
		return this.stockDataSearch;
	}

	/**
	 * Gets the {@code StockDataQuote} object if not available, creates a new empty
	 * instance with state {@code StockDataStateType.unclear}
	 * 
	 * @return the {@code StockDataQuote} object
	 */
	public StockDataQuote getStockDataQuote() {
		if (!this.quoteDataAvailable()) {
			this.stockDataQuote = new StockDataQuote();
		}
		return this.stockDataQuote;
	}

	////////////////////////////////////////////////////////
	//
	// SETTERs
	//
	////////////////////////////////////////////////////////

	/**
	 * Sets the {@code DisplayType} value.
	 * 
	 * @param displayType the {@code DisplayType} value to set.
	 */
	public void setDisplayType(DisplayType displayType) {
		this.displayType = displayType;
	}

	/**
	 * Wrapper method for the hash-map {@code stockDataCalculatorMap}
	 * 
	 * @param timeInterval        key with which the specified
	 *                            {@code StockDataCalculator} is to be associated.
	 * @param stockDataCalculator value to be associated with the specified
	 *                            {@code TimerInterval}.
	 */
	public void putStockDataCalculatorMap(TimeInterval timeInterval, StockDataCalculator stockDataCalculator) {
		this.stockDataCalculatorMap.put(timeInterval, stockDataCalculator);
	}

	/**
	 * Sets the stock data state of the {@code StockDataSearch} object
	 * 
	 * @param stockDataState the stock data state
	 */
	public void setStockDataSearchState(StockDataStateType stockDataState) {
		getStockDataSearch().setStockDataState(stockDataState);
	}

	/**
	 * Sets the stock data state of the {@code StockDataHistoric} object with a
	 * given time interval
	 * 
	 * @param interval       the time interval
	 * @param stockDataState the stock data state
	 */
	public void setStockDataHistoricState(TimeInterval interval, StockDataStateType stockDataState) {
		getStockDataHistoric(interval).setStockDataState(stockDataState);
	}

	/**
	 * Sets the stock data state of the {@code StockDataQuote} object
	 * 
	 * @param stockDataState the stock data state
	 */
	public void setStockDataQuoteState(StockDataStateType stockDataState) {
		getStockDataQuote().setStockDataState(stockDataState);
	}

	/**
	 * Sets the stock data state of a {@code StockDataHistoric} object for all time
	 * intervals
	 * 
	 * @param stockDataState the stock data state
	 */
	public void setStockDataHistoricStateAll(StockDataStateType stockDataState) {
		for (TimeInterval interval : this.stockDataHistoricMap.keySet()) {
			setStockDataHistoricState(interval, stockDataState);
		}
	}

	
	

	////////////////////////////////////////////////////////
	//
	// ADDERs
	//
	////////////////////////////////////////////////////////

	/**
	 * Adds the search data.
	 * 
	 * @param entry, an {@code PullSearchData.Entry} object contains the specific
	 *               search data to add.
	 */
	public void addPullSearchDataEntry(PullSearchData.Entry entry) {
		this.stockDataSearch = new StockDataSearch(entry.getDescription(), entry.getDisplaySymbol(), entry.getSymbol(),
				entry.getType());
	}

	/**
	 * Adds the historic data, {@code PullData} object, for a specific
	 * {@code TimeInterval}.
	 * 
	 * @param pullData contains the specific historic data to add.
	 * @param interval the specific timer interval.
	 */
	public void addPullData(PullData pullData, TimeInterval interval) {
				
		StockDataHistoric hist = new StockDataHistoric(pullData.getClose(), pullData.getHigh(), pullData.getLow(),
				pullData.getOpen(), pullData.getTimestampInMillies(), pullData.getVolume());
		this.stockDataHistoricMap.put(interval, hist);
	}

	/**
	 * Adds a {@code PullQuoteData} object, that contains the specific quote data.
	 * 
	 * @param pullQuoteData {@code PullQuoteData} object to add.
	 */
	public void addPullQuoteData(PullQuoteData pullQuoteData) {
		this.stockDataQuote = new StockDataQuote(pullQuoteData.getClose(), pullQuoteData.getHigh(),
				pullQuoteData.getLow(), pullQuoteData.getOpen(), pullQuoteData.getPreviousClose(),
				pullQuoteData.getTime());
	}

	/**
	 * Adds a {@code StockDataRealtime} object, that contains the specific real-time
	 * data.
	 * 
	 * @param realtimePushData {@code StockDataRealtime} object to add.
	 */
	public void addPushData(StockDataRealtime realtimePushData) {
		stockDataRealtime.add(realtimePushData);
	}
	
	
	////////////////////////////////////////////////////////
	//
	// REMOVERs
	//
	////////////////////////////////////////////////////////
	
	/**
	 * Clears real time data array
	 */
	public void clearRealtimeData() {
		this.stockDataRealtime.clear();
	}

	/**
	 * Clears the calculated stock data map
	 */
	public void clearStockDataCalculatorMap(){
		this.stockDataCalculatorMap.clear();
	}
}