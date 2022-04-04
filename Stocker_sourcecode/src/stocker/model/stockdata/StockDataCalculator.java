package stocker.model.stockdata;

import java.util.ArrayList;

import stocker.model.general.TimeInterval;


/**
 * The function of this class is, above all, the calculation, processing or preparation of real-time {@code StockDataRealtime}
 * and historical data {@code StockDataHistoric}. It implements the algorithms for calculating candles.
 * 
 * @author Christoph Kaplan
 *
 */
public class StockDataCalculator extends StockDataState {

	private StockDataHistoric historicStockData;
	private StockDataRealtime[] realtimeStockDataArray;

	private TimeInterval interval;
	private Candle[] allCandleArray;
	private double[] closePriceDataArray;

	/**
	 * {@code StockDataCalculator} empty constructor for testing
	 */
	public StockDataCalculator() {
		super(StockDataStateType.unclear);
	}

	/**
	 * {@code StockDataCalculator} constructor
	 * 
	 * @param historicData the historic data
	 * @param interval     the associated time interval
	 */
	public StockDataCalculator(StockDataHistoric historicData, TimeInterval interval) {
		super(StockDataStateType.access);
		this.historicStockData = historicData;
		this.interval = interval;
		
		
		this.allCandleArray = this.historicStockData.getCandles();
		resetClosePriceDataArray();
	}

	/**
	 * {@code StockDataCalculator} constructor
	 * 
	 * @param historicData the historic data
	 * @param realtimeData the real-time data
	 * @param interval     the associated time interval
	 */
	public StockDataCalculator(StockDataHistoric historicData, ArrayList<StockDataRealtime> realtimeData,TimeInterval interval) {
		super(StockDataStateType.access);
		this.interval = interval;
		this.historicStockData = historicData;
		
		
		
		//trim realtime data to get rid of "too old" values
		long newestTime = this.historicStockData.getTimestamp()[historicStockData.getTimestamp().length-1];
		
		ArrayList<StockDataRealtime> realtimeDataCopy = new ArrayList<StockDataRealtime>(realtimeData);
		for(int i = 0;i<realtimeDataCopy.size();i++) {
			if(realtimeDataCopy.get(i).getTime() <= newestTime) {
				realtimeDataCopy.remove(i);
			}
		}
				
		this.realtimeStockDataArray = realtimeDataCopy.toArray(new StockDataRealtime[realtimeDataCopy.size()]);
		
		Candle[] c1 = this.historicStockData.getCandles();		
		Candle[] c2 = convertTSToCandles(this.realtimeStockDataArray, interval);
		this.allCandleArray = joinCandles(c1, c2);
		resetClosePriceDataArray();
	}

	public void trimRealtimeData() {
		
	}
	
	
	/**
	 * Checks if the wanted amount of {@code Candle} objects is available, and how
	 * much is available otherwise.
	 * 
	 * @param wantedAmount the wanted amount of {@code Candle} objects
	 * @return if the wanted amount is to high return the max amount available.
	 */
	public int getAvailableAmount(int wantedAmount) {
		if (this.allCandleArray.length >= wantedAmount) {
			return wantedAmount;
		}
		return this.allCandleArray.length;
	}

	/**
	 * Gets a specific amount.from the latest on backwards, of {@code Candle}
	 * objects as array.
	 * 
	 * @param amount amount of {@code Candle} objects wanted
	 * @return the array of {@code Candle} objects
	 * @throws Exception thrown when no or not enough {@code Candle} objects are
	 *                   available.
	 */
	public Candle[] getLatestCandles(int amount) throws Exception {
		if (this.allCandleArray == null) {
			throw new Exception("error: this.allCandles == null");
		}
		if (this.allCandleArray.length < amount) {
			throw new Exception("not enough candles - have:" + this.allCandleArray.length + " needed:" + amount);
		}

		int start = this.allCandleArray.length - amount;
		Candle[] latestCandles = new Candle[amount];
		for (int i = 0; i < amount; i++) {
			latestCandles[i] = this.allCandleArray[start + i];
		}
		return latestCandles;
	}

	/**
	 * Gets the list of {@code Candle} objects.
	 * 
	 * @return the list of {@code Candle} objects
	 */
	public Candle[] getCandles() {
		return this.allCandleArray;
	}

	/**
	 * Gets the {@code TimeInterval} value.
	 * 
	 * @return the {@code TimeInterval} value
	 */
	public TimeInterval getTimeinterval() {
		return this.interval;
	}

	/**
	 * Joins two {@code Candle} arrays.
	 * 
	 * @param a {@code Candle} array a
	 * @param b {@code Candle} array b
	 * @return joined {@code Candle} array (a&b)
	 */
	Candle[] joinCandles(Candle[] a, Candle[] b) {
		Candle[] newArray = new Candle[a.length + b.length];
		for (int i = 0; i < a.length; i++) {
			newArray[i] = a[i];
		}
		for (int i = 0; i < b.length; i++) {
			newArray[i + a.length] = b[i];
		}
		return newArray;
	}

	/**
	 * Sets all close prices, taken from the candle array, as an array of
	 * {@code double}
	 * 
	 * @return the array of {@code double}
	 */
	private void resetClosePriceDataArray() {
		this.closePriceDataArray = new double[this.allCandleArray.length];
		for (int i = 0; i < this.allCandleArray.length; i++) {
			this.closePriceDataArray[i] = this.allCandleArray[i].getClose();
		}
	}

	/**
	 * Gets close prices array
	 * (Used by the IStockerTester)
	 * 
	 * @return the close prices array
	 */
	public double[] getClosePriceDataArray() {
		return this.closePriceDataArray;
	}

	/**
	 * Converts an array of {@code StockDataRealtime} to an array of {@code Candle}
	 * 
	 * @param timesales the array of {@code StockDataRealtime}
	 * @param interval  the considered {@code TimeInterval}
	 * @return the array of {@code Candle}
	 */
	public Candle[] convertTSToCandles(StockDataRealtime[] timesales, TimeInterval interval) {
		if (timesales.length == 0) {
			return new Candle[0];
		}

		ArrayList<Candle> candels = new ArrayList<Candle>();

		long time = timesales[0].getTime();
		double low = timesales[0].getPrice();
		double high = low;
		double open = low;
		double close = low;

		double volume = timesales[0].getVolume();

		for (int i = 0; i < timesales.length; i++) {

			if (timesales[i].getTime() >= interval.addResToTimestamp(time, 1)) {

				candels.add(new Candle(close, high,low, open, time, volume));
				
				time = timesales[i].getTime();
				low = timesales[i].getPrice();
				high = low;
				open = low;
				volume = timesales[i].getVolume();
			}

			low = Math.min(low, timesales[i].getPrice());
			high = Math.max(high, timesales[i].getPrice());
			close = timesales[i].getPrice();

			volume += timesales[i].getVolume();
		}

		Candle lastCandle = new Candle(close, high,low, open, time, volume);
		candels.add(lastCandle);

		return candels.toArray(new Candle[candels.size()]);
	}

	/**
	 * Converts an array of {@code Candle} from a smaller {@code TimeInterval} to a
	 * bigger {@code TimeInterval}
	 * 
	 * @param candles  the array of {@code Candle}
	 * @param timeFrom the smaller {@code TimeInterval}
	 * @param timeTo   the bigger {@code TimeInterval}
	 * @return the result array of {@code Candle}
	 */
	public Candle[] convertCandlesToCandles(Candle[] candles, TimeInterval timeFrom, TimeInterval timeTo) {
		ArrayList<Candle> candels = new ArrayList<Candle>();

		if (timeFrom.ordinal() < timeTo.ordinal()) {
			// fix
			return null;
		}

		long time = candles[0].getTime();

		double low = candles[0].getLow();
		double high = candles[0].getHigh();
		double open = candles[0].getOpen();
		double close = candles[0].getClose();

		double volume = candles[0].getVolume();

		for (int i = 0; i < candles.length; i++) {

			if (candles[i].getTime() >= timeTo.addResToTimestamp(time, 1)) {

				candels.add(new Candle(close, high,low, open, time, volume));

				time = candles[i].getTime();
				low = candles[i].getLow();
				high = candles[i].getHigh();
				open = candles[i].getOpen();

				volume = candles[i].getVolume();
			}

			low = Math.min(low, candles[i].getLow());
			high = Math.max(high, candles[i].getHigh());
			close = candles[i].getClose();

			volume += candles[i].getVolume();
		}

		candels.add(new Candle(close, high,low, open, time, volume));

		return candels.toArray(new Candle[candels.size()]);
	}
}
