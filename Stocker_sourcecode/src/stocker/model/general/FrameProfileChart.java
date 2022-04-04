package stocker.model.general;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

/**
 * The chart frame profiles are used to contain data about gui frames and
 * additional chart frame data i.e. symbol and indicators. They are also used to
 * determine if a frame is present or not.
 * 
 * @author Christoph Kaplan
 */
public class FrameProfileChart extends FrameProfile {

	@SerializedName("symbol")
	private final String symbol;
	@SerializedName("chartType")
	private ChartType chartType;
	@SerializedName("interval")
	private TimeInterval interval;
	@SerializedName("indicators")
	private ArrayList<IndicatorBase> indicators = new ArrayList<IndicatorBase>();

	/**
	 * {@code FrameProfileChart} constructor
	 * 
	 * @param symbol    the symbol
	 * @param chartType the chart type
	 * @param interval  the time interval
	 * @param id        the id
	 * @param viewType  the view type
	 * @param posX      the position X
	 * @param posY      the position Y
	 * @param sizeX     the size X
	 * @param sizeY     the size Y
	 * @param minSizeX  the minimum size X
	 * @param minSizeY  the minimum size Y
	 */
	public FrameProfileChart(String symbol, ChartType chartType, TimeInterval interval, int id, ViewType viewType,int posX, int posY, int sizeX, int sizeY, int minSizeX, int minSizeY) {
		super(id, symbol + " " + interval + " " + chartType + " " + (id - (ViewType.values().length-1)), viewType, posX, posY, sizeX, sizeY,minSizeX, minSizeY);
		this.symbol = symbol;
		this.chartType = chartType;
		this.interval = interval;
	}

	/**
	 * Gets a title based on symbol,interval and chart type
	 * 
	 * @return the title
	 */
	public String getTitle() {
		return symbol + " " + interval + " " + chartType + " " + (getID() - (ViewType.values().length-1));
	}

	/**
	 * Gets the symbol
	 * 
	 * @return the symbol
	 */
	public String getSymbol() {
		return this.symbol;
	}

	/**
	 * Gets the chart type
	 * 
	 * @return the chart type
	 */
	public ChartType getChartType() {
		return this.chartType;
	}

	/**
	 * Gets the time interval
	 * 
	 * @return the time interval
	 */
	public TimeInterval getTimeinterval() {
		return this.interval;
	}

	/**
	 * Sets the chart type
	 * 
	 * @param chartType the chart type
	 */
	public void setChartType(ChartType chartType) {
		this.chartType = chartType;
	}

	/**
	 * Sets the time interval
	 * 
	 * @param interval the time interval
	 */
	public void setTimeInterval(TimeInterval interval) {
		this.interval = interval;
	}

	/**
	 * Gets the list of indicators
	 * 
	 * @return the list of indicators
	 */
	public ArrayList<IndicatorBase> getIndicators() {
		return indicators;
	}

	/**
	 * Adds an indicator
	 * 
	 * @param indicator the indicator to add
	 */
	public void addIndicator(IndicatorBase indicator) {
		indicators.add(indicator);
	}

	/**
	 * Removes an indicator
	 * 
	 * @param indicator the indicator to remove
	 */
	public void removeIndicator(IndicatorBase indicator) {
		indicators.remove(indicator);
	}
}
