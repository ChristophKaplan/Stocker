package stocker.model.stockdata;



/**
 * {@code StockDataSearch} class, stores all relevant search data, received from a data provider.
 * @author Christoph Kaplan
 *
 */
public class StockDataSearch extends StockDataState{
	private String description;
	private String displaySymbol;
	private String symbol;
	private String type;

	public StockDataSearch(String description, String displaySymbol, String symbol, String type) {
		super(StockDataStateType.access);
		this.description = description;
		this.displaySymbol = displaySymbol;
		this.symbol = symbol;
		this.type = type;
	}
	public StockDataSearch() {
		super(StockDataStateType.unclear);
	}

	/**
	 * Gets the type.
	 * @return the type
	 */
	public String getType() {
		return this.type;
	}
	
	
	/**
	 * Gets the description.
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * Gets the display symbol.
	 * @return the display symbol
	 */
	public String getDisplaySymbol() {
		return this.displaySymbol;
	}
	
	/**
	 * Gets the symbol.
	 * @return the symbol
	 */
	public String getSymbol() {
		return this.symbol;
	}
	
	@Override
	public void  setStockDataState(StockDataStateType stockDataState) {
		super.setStockDataState(stockDataState);
		if(stockDataState == StockDataStateType.unclear || stockDataState == StockDataStateType.no_access) {
			String info = stockDataState.toString() + "(" + this.symbol +")";
			this.description = info;
			this.displaySymbol = info;
		}
	}
}
