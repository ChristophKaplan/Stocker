package stocker.model.stockdata;


/**
 * {@code StockDataRealtime} class, stores all relevant quote data, received from a data provider.
 * @author Christoph Kaplan
 *
 */
public class StockDataRealtime {
	
	private String symbol;
	private double price;
	private double volume;
	private long time;
	
	/**
	 * {@code StockDataRealtime} constructor
	 * @param s symbol
	 * @param p price
	 * @param t timestamp
	 * @param v volume
	 */
	public StockDataRealtime(String s,double p ,long t, double v) {
		this.symbol =s;
		this.price = p;
		this.time = t;
		this.volume = v;
	}
	
	/**
	 * Gets the symbol.
	 * @return the symbol
	 */
	public String getSymbol() {
		return this.symbol;
	}
	/**
	 * Gets the price.
	 * @return the price
	 */
	public double getPrice() {
		return this.price;
	}
	/**
	 * Gets timestamp.
	 * @return the timestamp
	 */
	public long getTime() {
		return this.time;
	}
	/**
	 * Gets the volume.
	 * @return the volume
	 */
	public double getVolume() {
		return this.volume;
	}
	
	
	
	
}
