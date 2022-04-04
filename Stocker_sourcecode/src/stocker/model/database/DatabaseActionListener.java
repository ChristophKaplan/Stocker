package stocker.model.database;

import stocker.model.general.DisplayType;
import stocker.model.general.TimeInterval;

/**
 * Database action listener, used to ask the listener (i.e. the
 * {@code NetworkClient}) for data input or subscription
 * 
 * @author Christoph Kaplan
 */
public interface DatabaseActionListener {
	/**
	 * Asks listeners for general search data input.
	 * 
	 * @param search      the "search word"
	 * @param displayType associated {@code DisplayType}
	 */
	public void onAskSearchDataForGeneral(String search, DisplayType displayType);

	/**
	 * Asks listeners for symbol search data input.
	 * 
	 * @param symbol      to be searched for
	 * @param displayType associated {@code DisplayType}
	 */
	public void onAskSearchDataForSymbol(String symbol, DisplayType displayType);

	/**
	 * Asks listeners for historic data input.
	 * 
	 * @param symbol   to be searched for
	 * @param interval associated {@code TimeInterval}
	 */
	public void onAskHistoricDataForSymbol(String symbol, TimeInterval interval);

	/**
	 * Asks listeners for quote data input.
	 * 
	 * @param symbol to be searched for
	 */
	public void onAskQuoteDataForSymbol(String symbol);

	/**
	 * Notify listeners to subscribe a symbol for real-time data.
	 * 
	 * @param symbol to subscribe
	 */
	public void onSubscribe(String symbol);

	/**
	 * Notify listeners to unsubscribe a symbol for real-time data.
	 * 
	 * @param symbol to unsubscribe
	 */
	public void onUnsubscribe(String symbol);
}
