package stocker.model.general;

/**
 * Enum to name and identify an indicator
 * 
 * @author Christoph Kaplan
 */
public enum IndicatorType {
	BollingerBands("Bollinger Bands"), SimpleMovingAverage("Simple Moving Average");
	private String display;
	IndicatorType(String display) {
		this.display = display;
	}
	@Override
	public String toString() {
		return display;
	}
}