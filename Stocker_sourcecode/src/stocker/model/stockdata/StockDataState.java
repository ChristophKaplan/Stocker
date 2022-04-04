package stocker.model.stockdata;

/**
 * This class implements mainly an Enum {@code StockDataStateType}, that regulates the various
 * availability states {@code access}, {@code no_access} and {@code unclear} of the (pull) data. The
 * status {@code unclear} exists when no data is available locally and should therefore
 * be requested. {@code no_access} if there is a connection to the provider but no
 * access to certain data is possible. {@code access} if the required data is available.
 * 
 * @author Christoph Kaplan
 *
 */
public abstract class StockDataState {
	/**
	 * Describes the state of accessibility of stock data
	 * 
	 * @author Christoph Kaplan
	 *
	 */
	public enum StockDataStateType {
		access("accessible"), no_access("inaccessible"), unclear("loading...");

		private String display;

		StockDataStateType(String display) {
			this.display = display;
		}

		@Override
		public String toString() {
			return display;
		}
	}

	private StockDataStateType stockDataState;

	/**
	 * Constructor initializes the state type
	 * 
	 * @param stockDataState the stock data state
	 */
	public StockDataState(StockDataStateType stockDataState) {
		this.stockDataState = stockDataState;
	}

	/**
	 * Gets the stock data state
	 * 
	 * @return the stock data state
	 */
	public StockDataStateType getStockDataState() {
		return this.stockDataState;
	}

	/**
	 * Sets the stock data state
	 * 
	 * @param stockDataState the stock data state
	 */
	public void setStockDataState(StockDataStateType stockDataState) {
		this.stockDataState = stockDataState;
	}
}
