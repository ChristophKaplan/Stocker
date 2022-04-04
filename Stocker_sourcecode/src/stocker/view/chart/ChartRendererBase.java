package stocker.view.chart;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import stocker.controller.chart.ChartController;
import stocker.model.general.IndicatorBase;
import stocker.model.stockdata.Candle;
import stocker.model.stockdata.StockDataCalculator;
import stocker.model.stockdata.StockDataState.StockDataStateType;

/**
 * The abstract class ChartRendererBase defines the basic variables, helpers and
 * initialization methods as well as the signatures of the various render
 * passes. By deriving from JPanel, ChartRendererBase gets access to the
 * “Graphics” object and thus the possibility to generate graphics. A “Map
 * Range” function is also implemented.
 * {@link #mapRange(double, double, double, double, double)} This function is
 * used in other methods to linearly map a value from one coordinate system to
 * another. As an example, the mapping of a closing price on the y-axis of the
 * JPanel: The method {@link #recalculateScaling()} sets proportions in relation
 * and calculates offsets such as B. width of candles, distance between candles,
 * etc. {@link #recalculateRanges()} calculates the time and price axis segment.
 * The method {@link #setUpRenderer(int, StockDataCalculator, ArrayList)}
 * assigns the transferred stock data to the renderer and checks whether and how
 * many are available. The class {@code ChartRendererVisualProfile} contains all
 * declarations about the visual profile of the renderer, for example. the
 * colors and line thickness.
 * 
 * 
 * @author Christoph Kaplan
 *
 */
public abstract class ChartRendererBase extends JPanel implements MouseMotionListener {

	private static final long serialVersionUID = -7367015349269787947L;

	protected ChartView chartView;
	protected Graphics2D g2d;

	protected StockDataCalculator stockDataCalculator;
	protected Candle[] latestCandles;
	protected ArrayList<IndicatorBase> indicatorList;

	protected String information;

	protected StockDataStateType state;

	protected int mousePosX = 0;
	protected int mousePosY = 0;

	protected int panelSizeX;
	protected int panelSizeY;
	protected int panelInsetX = 100;
	protected int panelInsetY = 30;
	protected int borderLineOffsetX = 50;
	protected int borderLineOffsetY = 40;
	protected int borderLinePosX;
	protected int borderLinePosY;
	protected int indicatorLablePosX = 10;
	protected int indicatorLableSizeX = 50;
	protected long timeFrom;
	protected long timeTo;
	protected double minCandleValue = Double.POSITIVE_INFINITY;
	protected double maxCandleValue = Double.POSITIVE_INFINITY;

	protected int gridStepsY = 20;
	protected int dataAmount;

	protected int candleBodyWidth;
	protected int candleCellWidth;
	protected int candleGap;
	protected int dochtWidth;

	protected ChartRendererVisualProfile colorProfile = new ChartRendererVisualProfile();

	/**
	 * constructor
	 * 
	 * @param chartView the chart view instance
	 */
	public ChartRendererBase(ChartView chartView) {
		this.chartView = chartView;
		setUp();
	}

	/**
	 * Sets components
	 */
	private void setUp() {
		recalculateScaling();
		setBackground(colorProfile.getBackgroundColor());
		this.addMouseMotionListener(this);
	}

	/**
	 * Sets listener
	 * 
	 * @param chartController the listener
	 */
	public void setUpListener(ChartController chartController) {
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				chartController.chartRendererMouseclickPerformed(e, mousePosX, mousePosY,
						panelYToCandleValue(mousePosY));
			}
		});
	}

	/**
	 * Recalculate scaling and relations
	 */
	private void recalculateScaling() {
		this.panelSizeX = this.getSize().width;
		this.panelSizeY = this.getSize().height;

		// size of the whole panel minus the left and right inset divided by the
		// candleamount
		this.candleCellWidth = (panelSizeX - (panelInsetX * 2)) / (dataAmount - 1);
		this.candleGap = candleCellWidth / 5;
		this.candleBodyWidth = this.candleCellWidth - candleGap;
		this.dochtWidth = (int) clamp((candleBodyWidth / 50), 1.5, 5);

		this.borderLineOffsetX = panelInsetX - (this.candleCellWidth);
		this.borderLineOffsetY = panelInsetY;
		this.borderLinePosX = panelSizeX - borderLineOffsetX;
		this.borderLinePosY = panelSizeY - borderLineOffsetY;
	}

	/**
	 * Recalculate ranges of X/Y axis
	 */
	private void recalculateRanges() {
		this.timeFrom = getMinCandleTime(latestCandles);
		this.timeTo = getMaxCandleTime(latestCandles);

		// To choose the range of the y-axis, the extremes of the candle prices are
		// determined and two eighths of the distance are added. When the extremes
		// change, the size of the change is checked; If it exceeds a certain distance,
		// the y-axis is reset.
		
		double minPrice = getMinCandleValue(latestCandles);
		double maxPrice = getMaxCandleValue(latestCandles);
		double extraSpace = Math.abs(maxPrice - minPrice) / 8f;

		double newMin = minPrice - extraSpace;
		double newMax = maxPrice + extraSpace;

		double diffMin = Math.abs(newMin - this.minCandleValue);
		double diffMax = Math.abs(newMax - this.maxCandleValue);

		double threshold = extraSpace;
		
		if (diffMin > threshold || diffMax > threshold) {
			//System.out.println("change y-Axis values");
			this.minCandleValue = newMin;
			this.maxCandleValue = newMax;
		}

	}

	/**
	 * Sets the chart renderer, asks, database model, for new calculations
	 * 
	 * @param wantedAmount        how many stock data/candles to draw
	 * @param stockDataCalculator the calculated stock data
	 * @param indicatorList       list of indicators to draw
	 */
	public void setUpRenderer(int wantedAmount, StockDataCalculator stockDataCalculator, ArrayList<IndicatorBase> indicatorList) {
		// System.out.println("setUprenderer(" + wantedAmount + ") " + chartView.getStock().getSymbol());

		this.state = stockDataCalculator.getStockDataState();

		if (stockDataCalculator.getStockDataState() == StockDataStateType.no_access	|| stockDataCalculator.getStockDataState() == StockDataStateType.unclear) {
			// if loading or no access, return.
			return;
		}

		this.stockDataCalculator = stockDataCalculator;
		this.indicatorList = indicatorList;
		
		dataAmount = stockDataCalculator.getAvailableAmount(wantedAmount);
		
		this.gridStepsY =Math.floorDiv(dataAmount, 3);
		
		try {
			latestCandles = this.stockDataCalculator.getLatestCandles(dataAmount);
		} catch (Exception e) {
			// no acception here ?
			e.printStackTrace();
			this.state = StockDataStateType.no_access;
			return;
		}

		recalculateRanges();

	}

	/**
	 * Gets the latest timestamp available within an array of {@code Candle}.
	 * 
	 * @param candles the array of {@code Candle}
	 * @return the latest timestamp
	 */
	private long getMaxCandleTime(Candle[] candles) {
		return candles[candles.length - 1].getTime();
	}

	/**
	 * Gets the latest timestamp available within an array of {@code Candle}.
	 * 
	 * @param candles the array of {@code Candle}
	 * @return the latest timestamp
	 */
	private long getMinCandleTime(Candle[] candles) {
		return candles[0].getTime();
	}

	/**
	 * Gets the maximum of high prices available within an array of {@code Candle}.
	 * 
	 * @param candles the array of {@code Candle}
	 * @return the maximum high price
	 */
	private double getMaxCandleValue(Candle[] candles) {
		Candle maxCandle = null;
		double max = Double.NEGATIVE_INFINITY;

		for (int i = 0; i < candles.length; i++) {
			Candle c = candles[i];
			if (c.getHigh() > max) {
				maxCandle = c;
				max = maxCandle.getHigh();
			}
		}

		return maxCandle.getHigh();
	}

	/**
	 * Gets the minimum of low prices available within an array of {@code Candle}.
	 * 
	 * @param candles the array of {@code Candle}
	 * @return the minimum low price
	 */
	private double getMinCandleValue(Candle[] candles) {
		Candle minCandle = null;
		double min = Double.POSITIVE_INFINITY;

		for (int i = 0; i < candles.length; i++) {
			Candle c = candles[i];
			if (c.getLow() < min) {
				minCandle = c;
				min = minCandle.getLow();
			}
		}
		return minCandle.getLow();
	}

	/**
	 * Gets the data array index by mouse position
	 * 
	 * @return the data index
	 */
	protected int getDataIndexByMousePos() {
		int dataIndex = panelXToGridX(mousePosX + (candleCellWidth / 2));
		if (dataIndex < 0 || dataIndex >= dataAmount) {
			return -1;
		}
		return dataIndex;
	}

	/**
	 * Gets the Candle by mouse position
	 * 
	 * @return the candle
	 */
	protected Candle getCandleByMousePos() {
		int dataIndex = getDataIndexByMousePos();
		if (dataIndex == -1) {
			return null;
		}
		return latestCandles[dataIndex];
	}

	/**
	 * When repaint
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.g2d = (Graphics2D) g;

		recalculateScaling();
		render();
	}

	/**
	 * When mouse dragged
	 */
	@Override
	public void mouseDragged(MouseEvent e) {

	}

	/**
	 * Gets the mouse position
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		this.mousePosX = e.getX();
		this.mousePosY = e.getY();
		this.repaint();
	}

	/**
	 * Linear map range function
	 * 
	 * @param a1  start range a
	 * @param a2  end range a
	 * @param b1  start range b
	 * @param b2  end range b
	 * @param val the value to map
	 * @return the result
	 */
	private double mapRange(double val, double a1, double a2, double b1, double b2) {
		return b1 + ((val - a1) * (b2 - b1)) / (a2 - a1);
	}

	/**
	 * Maps from candle price value to panel y
	 * 
	 * @param val price value
	 * @return result
	 */
	protected int candleValueToPanelY(double val) {
		return (int) mapRange(val, minCandleValue, maxCandleValue, panelSizeY - panelInsetY, 0f + panelInsetY);
	}

	/**
	 * Maps from grid y to panel y
	 * 
	 * @param gridY grid y
	 * @return result
	 */
	protected int gridYToPanelY(int gridY) {
		return (int) mapRange(gridY, 0, gridStepsY - 1, panelSizeY - panelInsetY, 0f + panelInsetY);
	}

	/**
	 * Maps from grid y to price value
	 * 
	 * @param gridY grid y
	 * @return result
	 */
	protected double gridYToCandleValue(int gridY) {
		return mapRange(gridY, 0, gridStepsY - 1, minCandleValue, maxCandleValue);
	}

	/**
	 * Maps from panel y to price value
	 * 
	 * @param panelY panel y
	 * @return result
	 */
	protected double panelYToCandleValue(double panelY) {
		return mapRange(panelY, panelSizeY - panelInsetY, 0f + panelInsetY, minCandleValue, maxCandleValue);
	}

	/*
	 * protected int timestampToPanelX(long time) { return (int) mapRange(time,
	 * timeFrom, timeTo, 0f + panelInsetX, panelSizeX - panelInsetX); } protected
	 * long gridXToTimestamp(int gridX) { return (long) mapRange(gridX, 0,
	 * dataAmount - 1, timeFrom, timeTo); }
	 */

	/**
	 * Maps from grid x to panel x
	 * 
	 * @param gridX grid x
	 * @return result
	 */
	protected int gridXToPanelX(int gridX) {
		return (int) mapRange(gridX, 0, dataAmount - 1, 0f + panelInsetX, panelSizeX - panelInsetX);
	}

	/**
	 * Maps from panel x to grid x
	 * 
	 * @param panelX panel x
	 * @return
	 */
	protected int panelXToGridX(int panelX) {
		return (int) mapRange(panelX, 0f + panelInsetX, panelSizeX - panelInsetX, 0, dataAmount - 1);
	}

	/**
	 * Formats a {@code double} value to a readable {@code String} format
	 * 
	 * @param d the {@code double} value
	 * @return formatted {@code String}
	 */
	protected String doubleFormatter(double d) {
		return String.format("%,.3f", d);
	}

	/**
	 * Clamp function
	 * 
	 * @param val value
	 * @param min minimum
	 * @param max maximum
	 * @return result
	 */
	protected double clamp(double val, double min, double max) {
		return Math.max(min, Math.min(max, val));
	}

	/**
	 * Updates the rendering
	 */
	abstract void render();

	/**
	 * Draws information when error occurred
	 */
	abstract void passAccessInformation();

	/**
	 * Draws the grid of coordinate system
	 */
	abstract void passBackgroundGrid();

	/**
	 * Draws the axes of the coordinate system
	 */
	abstract void passAxes();

	/**
	 * Draws the alarms
	 */
	abstract void passAlarm();

	/**
	 * Draws the indicator lines
	 */
	abstract void passIndicatorLines();

	/**
	 * Draws the indicator backgrounds
	 */
	abstract void passIndicatorBackgrounds();

	/**
	 * Draws the candles
	 */
	abstract void passCandles();

	/**
	 * Draws the line chart
	 */
	abstract void passLineChart();

	/**
	 * Draws the crosshair
	 */
	abstract void passCrosshair();

	/**
	 * Draws information
	 */
	abstract void passGeneralInformation();

}
