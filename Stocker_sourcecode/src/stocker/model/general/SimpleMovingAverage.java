package stocker.model.general;

import java.awt.Color;

/**
 * The SimpleMovingAverage class defines the “Simple Moving Average” indicator.
 * It is derived from {@code IndicatorBase} and declares an additional variable
 * n, for the “size” of the SMA. Furthermore, the mathematical functions for
 * calculating the moving average are implemented.
 * {@link #predecessor(int, int, double[])} returns a value n index places, in
 * the given array, before the currently considered value with index i. So a
 * past closing price. {@link #calcSimpleMovingAverage(int, int, double[])}
 * calculates the moving average at index i and {@link #getSMA(int, double[])}
 * calculates the moving average for multiple closing prices, across an entire
 * range. From the total number of data given - amountPrices to the most recent
 * closing price.
 * 
 * @author Christoph Kaplan
 */
public class SimpleMovingAverage extends IndicatorBase {
	private int n;

	/**
	 * {@code SimpleMovingAverage} constructor
	 * 
	 * @param type  the indicator type
	 * @param n     the n value
	 * @param color the indicator color
	 */
	public SimpleMovingAverage(IndicatorType type, int n, Color color) {
		super(type, color);
		this.n = n;
	}

	/**
	 * Gets the n value
	 * 
	 * @return the n value
	 */
	public int getNValue() {
		return this.n;
	}

	/**
	 * predecessor
	 * 
	 * @param i    current index, the considered "spot"
	 * @param n    index of any price before i, must be < i
	 * @param data array of {@code double} close price data
	 * @return {@code double} close price data at position i-n
	 */
	protected double predecessor(int i, int n, double[] data) {
		if (i < n) {
			System.err.println("predecessor(): i:" + i + "< n:" + n);
			return 0f;
		}
		return data[i - n];
	}

	/**
	 * Calculates the "Simple Moving Average"
	 * 
	 * @param i    current index, the considered "spot"
	 * @param n    amount of all prices
	 * @param data array of {@code double} close price data
	 * @return {@code double} value result, simple moving average at i
	 */
	protected double calcSimpleMovingAverage(int i, int n, double[] data) {
		if (n == 0) {
			System.err.println("n is 0");
			return 0;
		}

		double sum = 0f;
		for (int j = 0; j < n; j++) {
			sum += predecessor(i, j, data);
		}

		return (sum / n);
	}

	/**
	 * Calculates "Simple Moving Average" over an amount of data
	 * 
	 * @param amount the amount of prices
	 * @param data   data array of {@code double} close price data
	 * @return resulting array of {@code double}
	 * @throws Exception thrown if not enough data is available
	 */
	public double[] getSMA(int amount, double[] data) throws Exception {
		if (!isEnoughDataAvailable(amount, data)) {
			throw new Exception("available data :" + data.length + "/" + (this.getNValue() - 1 + amount));
		}

		double[] calculatedSMA = new double[amount];
		int startIndex = data.length - amount;

		int j = 0; // arrayIndex
		for (int i = startIndex; i < data.length; i++) {
			double sma = calcSimpleMovingAverage(i, this.getNValue(), data);
			calculatedSMA[j] = sma;
			j++;
		}

		return calculatedSMA;
	}

	@Override
	public int getMaxAmout(double[] data) {
		int v = data.length - this.getNValue() + 1;
		return v;
	}

}
