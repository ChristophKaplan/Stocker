package stocker.model.general;

import java.awt.Color;

/**
 * This abstract class is a basis of the indicators. An enum
 * {@code IndicatorType} is used to uniquely identify the indicator type and its name.
 * Furthermore, a color is defined that the user can freely choose inside in a
 * dialog window. An override of the toString () method returns the indicator
 * name / type and the given parameterization as a string. This is used for
 * visual representation.
 * 
 * @author Christoph Kaplan
 *
 */
public abstract class IndicatorBase {

	/**
	 * {@code IndicatorType} value, type or name of the indicator
	 */
	private IndicatorType type;

	/**
	 * {@code Color} indicator color
	 */
	private Color color;

	/**
	 * {@code IndicatorBase} constructor
	 * 
	 * @param type  {@code IndicatorType} value, type or name of the indicator
	 * @param color {@code Color} indicator color
	 */
	public IndicatorBase(IndicatorType type, Color color) {
		this.type = type;
		this.color = color;
	}

	/**
	 * Checks if enough data is available
	 * 
	 * @param askedAmount asked data amount
	 * @param data        data array of {@code double} close price data
	 */
	protected boolean isEnoughDataAvailable(int askedAmount, double[] data) {
		if (askedAmount > this.getMaxAmout(data)) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * /** Gets the maximum amount of indicator data
	 * 
	 * @param data stock data
	 * @return maximum amount of indicator data
	 */
	public abstract int getMaxAmout(double[] data);

	/**
	 * {@code IndicatorBase} constructor
	 * 
	 * @param type {@code IndicatorType} value, type or name of the indicator
	 */
	public IndicatorBase(IndicatorType type) {
		this.type = type;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public IndicatorType getType() {
		return this.type;
	}

	/**
	 * Gets the color.
	 * 
	 * @return the color
	 */
	public Color getColor() {
		return this.color;
	}

	/**
	 * Sets the color.
	 * 
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Overrides the toString() method
	 */
	@Override
	public String toString() {
		if (this.type == IndicatorType.SimpleMovingAverage) {
			return this.type.toString() + " (" + ((SimpleMovingAverage) this).getNValue() + ")";
		}
		return this.type.toString() + " (" + ((BollingerBands) this).getFValue() + ","
				+ ((BollingerBands) this).getNValue() + ")";
	}

}
