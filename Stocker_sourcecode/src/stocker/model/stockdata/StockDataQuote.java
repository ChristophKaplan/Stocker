package stocker.model.stockdata;



/**
 * {@code StockDataQuote} class, stores all relevant quote data, received from a data provider.
 * @author Christoph Kaplan
 *
 */
public class StockDataQuote extends StockDataState{
	
	// current price of the day
	private double c;
	// high price of the day
	private double h;
	// low price of the day
	private double l;
	// open price of the day
	private double o;
	// previous close price
	private double pc;
	// timestamp
	private long t;

	/**
	 *  {@code StockDataQuote} constructor 
	 * @param c close price of the day
	 * @param h high price of the day
	 * @param l low price of the day
	 * @param o open price of the day
	 * @param pc previous close price
	 * @param t timestamp
	 */
	public StockDataQuote(double c, double h, double l, double o, double pc, long t) {
		super(StockDataStateType.access);
		this.c = c;
		this.h = h;
		this.l = l;
		this.o = o;
		this.pc = pc;
		this.t = t;
	}
	
	public StockDataQuote() {
		super(StockDataStateType.unclear);
	}
	
	
	/**
	 * Gets the current price of the day.
	 * @return the current price of the day
	 */
	public double getCurrentPriceOfTheDay() {
		return c;
	}
	
	/**
	 * Gets the previous close price.
	 * @return the previous close price
	 */
	public double getPreviousClosePrice() {
		return pc;
	}
	

	/**
	 * Gets timestamp
	 * @return timestamp
	 */
	public long  getTime() {
		return this.t;
	}

	/**
	 * Gets high
	 * @return high
	 */
	public double  getHigh() {
		return this.h;
	}
	/**
	 * Gets low
	 * @return low
	 */
	public double  getLow() {
		return this.l;
	}
	/**
	 * Gets open
	 * @return open
	 */
	public double  getOpen() {
		return this.o;
	}
	
}