package stocker.model.stockdata;



/**
 * {@code StockDataHistoric} class, stores all relevant historic data, received from a data provider.
 * @author Christoph Kaplan
 *
 */
public class StockDataHistoric extends StockDataState{
	
	//historic data stored here
	private double[] close;
	private double[] high;
	private double[] low;
	private double[] open;
	private long[] timestamp;
	private double[] volume;

	/**
	 * {@code StockDataHistoric} constructor
	 * @param close Close price
	 * @param high High price
	 * @param low Low price
	 * @param open Open price
	 * @param timestamp Time
	 * @param volume Volume
	 */
	public StockDataHistoric( double[] close, double[] high, double[] low, double[] open, long[] timestamp,	double[] volume) {
		super(StockDataStateType.access);
		this.close = close;
		this.high = high;
		this.low = low;
		this.open = open;
		this.timestamp = timestamp;
		this.volume = volume;
	}

	public StockDataHistoric() {
		super(StockDataStateType.unclear);
	}
	
	/**
	 * Creates a new {@code Candle} object at any given index.
	 * @param i index
	 * @return the new {@code Candle} object
	 */
	private Candle getCandleAt(int i) {
		if(!indexInRange(i)) return null;
		return new Candle(close[i], high[i], low[i], open[i], timestamp[i], volume[i]);
	}
	/**
	 * Checks if an index is within range of the arrays of this class.
	 * @param i the index
	 * @return true if in range, false if out of range
	 */
	private boolean indexInRange(int i) {
		if(i >= 0 && i<close.length) return true;
		System.err.println("indexInRange("+i+") index out of range!");
		return false;
	}
	
	/**
	 * Gets an array of new {@code Candle} objects, created from historic data.
	 * @return the new {@code Candle} objects
	 */
	public Candle[] getCandles() {
		int count = close.length;
		Candle[] newCandles = new Candle[count];
		for (int i = 0; i < count; i++) {
			newCandles[i] = getCandleAt(i);
		}
		return newCandles;
	}

	/**
	 * Gets close price.
	 * @return the close price
	 */
	public double[] getClose() {
		return this.close;
	}
	/**
	 * Gets high price.
	 * @return the high price
	 */
	public double[] getHigh() {
		return this.high;
	}
	/**
	 * Gets low price.
	 * @return the low price
	 */
	public double[] getLow() {
		return this.low;
	}
	/**
	 * Gets open price.
	 * @return the open price
	 */
	public double[] getOpen() {
		return this.open;
	}
	/**
	 * Gets timestamp.
	 * @return the timestamp
	 */
	public long[] getTimestamp() {
		return this.timestamp;
	}
	/**
	 * Gets volume.
	 * @return the volume
	 */
	public double[] getVolume() {
		return this.volume;
	}
	

	
}
