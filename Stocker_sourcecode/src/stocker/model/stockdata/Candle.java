package stocker.model.stockdata;


/**
 * Candle class, stores all relevant candle information.
 * 
 * @author Christoph Kaplan
 *
 */
public class Candle{
	/** Close price is stored here */
	private double c;
	/** High price is stored here */
	private double h;
	/** Low price is stored here */
	private double l;
	/** Open price is stored here */
	private double o;
	/** Unix-timestamp in seconds, time is stored here */
	private long t;
	/** volume is stored here */
	private double v;
	
	/**
	 * Candle constructor
	 * @param c close price
	 * @param h high price
	 * @param l low price
	 * @param o open price
	 * @param t time
	 * @param v volume
	 */
	public Candle(double c, double h, double l, double o, long t, double v) {
		this.c = c;
		this.h = h;
		this.l = l;
		this.o = o;
		this.t = t;
		this.v = v;
	}
		
	/**
	 * Checks if candle has an upward or downward trend.
	 * @return true if upward trends, false if downward
	 */
	public boolean upwardTrend() {
		if(o >= c) {
			return false;
		}
		return true;
	}
	
	/**
	 * Gets the close price
	 * @return close price
	 */
	public double getClose() {
		return c;
	}
	
	/**
	 * Gets the open price
	 * @return open price
	 */
	public double getOpen() {
		return o;
	}
	
	/**
	 * Gets the high price
	 * @return high price
	 */
	public double getHigh() {
		return h;
	}
	
	/**
	 * Gets the low price
	 * @return low price
	 */
	public double getLow() {
		return l;
	}
	
	/**
	 * Gets the time price
	 * @return time price
	 */
	public long getTime() {
		return t;
	}
	
	/**
	 * Gets the volume price
	 * @return volume price
	 */
	public double getVolume() {
		return v;
	}
	
	

}