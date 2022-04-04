package stocker.model.general;

import java.awt.Color;

/**
 * The class class BollingerBands defines the indicator “Bollinger Bands”. It is
 * based on the simple moving average indicator and is therefore derived from
 * the {@code SimpleMovingAverage} class. It declares the variables f for the
 * “bandwidth” and m for the number of sums.
 * {@link #calcUpperBollingerBand(double, int, int, int, double[])} calculates
 * the “Upper Bollinger Band” at index i.
 * {@link #getUpperBollingerBand(int, double[])} calculates the “Upper Bollinger
 * Band” for multiple closing prices, over an entire range. From the total
 * number of data given - amountPrices to the most recent closing price. Very
 * similar methods are implemented for the “Lower Bollinger Band”.
 * 
 * 
 * @author Christoph Kaplan
 */
public class BollingerBands extends SimpleMovingAverage {
	private double f;
	private int m;

	/**
	 * {@code BollingerBands} constructor
	 * 
	 * @param type  the indicator type
	 * @param f     the f value
	 * @param n     the n value
	 * @param m     the m value
	 * @param color the indicator color
	 */
	public BollingerBands(IndicatorType type, double f, int n, int m, Color color) {
		super(type, n, color);
		this.f = f;
		this.m = m;
	}

	/**
	 * Gets the f value
	 * 
	 * @return the f value
	 */
	public double getFValue() {
		return this.f;
	}

	/**
	 * Gets the m value
	 * 
	 * @return the m value
	 */
	public int getMValue() {
		return this.m;
	}

	/**
	 * o method, is needed to calculate the "Bollinger Bands"
	 * 
	 * @param i    current index, the considered "spot"
	 * @param n    amount of prices for the simple moving average
	 * @param m    amount of sums (?), usually n=m
	 * @param data data array of {@code double} close price data
	 * @return {@code double} value, needed for "Bollinger Bands"
	 */
	private double o(int i, int n, int m, double[] data) {
		double sum = 0f;
		for (int j = 0; j < m; j++) {
			sum += Math.pow(predecessor(i, j, data) - calcSimpleMovingAverage(i, n, data), 2);
		}
		double result = Math.sqrt(sum / m);
		return result;
	}

	/**
	 * Calculates the "Upper Bollinger Band"
	 * 
	 * @param f    f value
	 * @param i    i index
	 * @param n    n amount
	 * @param m    m amount
	 * @param data data array of {@code double} close price data
	 * @return {@code double} result, the "Upper Bollinger Bands"
	 */
	protected double calcUpperBollingerBand(double f, int i, int n, int m, double[] data) {
		return calcSimpleMovingAverage(i, n, data) + (f * o(i, m, n, data));
	}

	/**
	 * Calculates the "Lower Bollinger Band"
	 * 
	 * @param f    f value
	 * @param i    i index
	 * @param n    n amount
	 * @param m    m amount
	 * @param data data array of {@code double} close price data
	 * @return {@code double} result, the "Lower Bollinger Bands"
	 */
	protected double calcLowerBollingerBand(double f, int i, int n, int m, double[] data) {
		return calcSimpleMovingAverage(i, n, data) - (f * o(i, n, m, data));
	}

	/**
	 * Calculates "Upper Bollinger Bands" over an amount of data
	 * 
	 * @param amount the amount of prices
	 * @param data   data array of {@code double} close price data
	 * @return resulting array of {@code double}
	 * @throws Exception thrown if not enough data is available
	 */
	public double[] getUpperBollingerBand(int amount, double[] data) throws Exception {
		if (!isEnoughDataAvailable(amount, data)) {
			throw new Exception("available data :" + data.length + "/" + (this.getNValue() - 1 + amount));
		}

		double[] calculatedUpperBB = new double[amount];
		int startIndex = data.length - amount;

		int j = 0;// arrayIndex
		for (int i = startIndex; i < data.length; i++) {
			double upper = calcUpperBollingerBand(this.getFValue(), i, this.getNValue(), this.getMValue(), data);
			calculatedUpperBB[j] = upper;
			j++;
		}
		return calculatedUpperBB;
	}

	/**
	 * Calculates "Lower Bollinger Bands" over an amount of data
	 * 
	 * @param amount the amount of prices
	 * @param data   data array of {@code double} close price data
	 * @return resulting array of {@code double}
	 * @throws Exception thrown if not enough data is available
	 */
	public double[] getLowerBollingerBand(int amount, double[] data) throws Exception {
		if (!isEnoughDataAvailable(amount, data)) {
			throw new Exception("available data :" + data.length + "/" + (this.getNValue() - 1 + amount));
		}

		double[] calculatedLowerBB = new double[amount];
		int startIndex = data.length - amount;

		int j = 0;// arrayIndex
		for (int i = startIndex; i < data.length; i++) {
			double upper = calcLowerBollingerBand(this.getFValue(), i, this.getNValue(), this.getMValue(), data);
			calculatedLowerBB[j] = upper;
			j++;
		}
		return calculatedLowerBB;
	}

}
