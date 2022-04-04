package stocker.model.database;

import stocker.model.stockdata.StockData;


/**
 * Database stock listener, used to notify the listener (i.e. the {@code CharView}) when {@code StockData} was added or updated.
 * 
 * @author Christoph Kaplan
 */
public interface DatabaseStockListener {
	
	/**
	 * Notify listeners that a new {@code StockData} object was created
	 * 
	 * @param stock considered {@code StockData} object
	 */
	public void onStockAdded(StockData stock); 
	
	/**
	 * Notify listeners that a {@code StockData} object was removed
	 * 
	 * @param stock considered {@code StockData} object
	 */
	public void onStockRemoved(StockData stock);
	
	/**
	 * Notify listeners that a {@code StockData} object was updated
	 * 
	 * @param stock considered {@code StockData} object
	 */
	public void onStockUpdate(StockData stock);
	
	/**
	 * Notify listeners that {@code DisplayType} of a {@code StockData} object was changed
	 * 
	 * @param stock considered {@code StockData} object
	 */
	public void onStockDisplayTypeChange(StockData stock);

	
	/**
	 * Notify listeners that a {@code StockData} object is calculated
	 * 
	 * @param stock considered {@code StockData} object
	 */
	public void onStockCalculated(StockData stock);
	
	
	/**
	 * Notify listeners that an alarm was added
	 * 
	 * @param alarm the considered {@code AlarmWrapper} object
	 * @param value the added alarm value
	 */
	public void onAlarmAdded(AlarmWrapper alarm, double value);
	
	/**
	 * Notify listeners that an alarm was removed
	 * 
	 * @param alarm the considered {@code AlarmWrapper} object
	 * @param value the removed alarm value
	 */
	public void onAlarmRemove(AlarmWrapper alarm, double value);

}
