package stocker.view.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import stocker.model.database.AlarmWrapper;
import stocker.model.general.ChartType;
import stocker.model.general.IndicatorBase;
import stocker.model.general.IndicatorType;
import stocker.model.general.BollingerBands;
import stocker.model.general.SimpleMovingAverage;
import stocker.model.stockdata.Candle;
import stocker.model.stockdata.StockDataState.StockDataStateType;

/**
 * The ChartRenderer class inherits from ChartRendererBase, is responsible for
 * the actual drawing and implements the predefined render passes.
 * 
 * {@link #passAccessInformation()} Draws an message if e.g. B. no data is
 * available or loading. {@link #passIndicatorBackgrounds()} Draws the
 * indicators available in the chart frame profile of the chart view.
 * {@link #passAxes()} Draws the X / Y axis. 2 nested loops form a
 * two-dimensional grid. The grid of the X / Y axis must be set sensibly and
 * results from the number of data to be displayed. Iterating through the
 * existing data results in the time information on the x-axis that possibly
 * missing weekend data is not taken into account, which in my opinion is
 * visually preferable. Apart from the rasterization of the X / Y axis, there is
 * a certain "value range" for the respective axis, for this purpose the minimum
 * and maximum of the prices as well as the timestamps are searched for and
 * mapped onto it from the raster coordinates. {@link #passAlarm()} Draws the
 * alarms of the share {@link #passCandles()} Iterates over the candle array and
 * draws the corresponding candle using the drawCandleAt () method.
 * {@link #passLineChart()} Draws the line chart using the closing price of the
 * data. {@link #passCrosshair()} Draws the crosshair and a mouse hover
 * indicator. The corresponding index is determined by mapping the mouse
 * position to the grid position. {@link #passGeneralInformation()} Draws
 * general information that was set with the addLineToInformation (String line)
 * method.
 * 
 * 
 * 
 * 
 * @author Christoph Kaplan
 *
 */
public class ChartRenderer extends ChartRendererBase {
	private static final long serialVersionUID = 6078773011839405580L;

	public ChartRenderer(ChartView chartView) {
		super(chartView);
	}

	/**
	 * Appends a new line of String to the information
	 * 
	 * @param line new line
	 */
	private void addLineToInformation(String line) {
		this.information += line + "\n";
	}

	/**
	 * Clears the information string
	 */
	private void clearInformation() {
		this.information = "";
	}

	/**
	 * renders all render passes
	 */
	@Override
	public void render() {
		clearInformation();

		if (this.state == StockDataStateType.no_access || this.state == StockDataStateType.unclear) {
			passAccessInformation();
			return;
		}

		//render passes
		passBackgroundGrid();
		passIndicatorBackgrounds();
		passCandles();
		passLineChart();
		passIndicatorLines();
		passAlarm();
		passAxes();
		passCrosshair();
		passGeneralInformation();
	}

	/**
	 * Draws information
	 */
	@Override
	void passGeneralInformation() {
		//set color & fontsize
		g2d.setColor(colorProfile.getDescriptionColor());
		g2d.setFont(g2d.getFont().deriveFont(Font.BOLD,11f));
		
		//height offset
		int y = 10;
		
		//draw gathered text line by line
		for (String line : this.information.split("\n")) {
			y += g2d.getFontMetrics().getHeight() + 2;
			g2d.fillRect(10, y-10, 5, 10);

			g2d.drawString(line, 20, y);
		}
		
		
	}


	/**
	 * Draws the crosshair
	 */
	@Override
	void passCrosshair() {
		//set color & stroke
		g2d.setStroke(colorProfile.getCrosshairStroke());
		g2d.setColor(colorProfile.getCrosshairColor());

		//draw crosshair
		g2d.drawLine(mousePosX, 0, mousePosX, panelSizeY);
		g2d.drawLine(0, mousePosY, panelSizeX, mousePosY);

		//set stock info in statusbar
		chartView.setStockInfo("symbol:" + chartView.getStock().getSymbol() + ", quote:"+ doubleFormatter(chartView.getStock().getCurrentPrice()) + ", available data:" + stockDataCalculator.getCandles().length);
				
		//get current candle at mouse pos
		Candle candle = getCandleByMousePos();		
		if(candle == null) {
			return;
		}
		//set mouse info in statusbar
		chartView.setMouseInfo("mouse at: " + chartView.getInterval().timestampToString(candle.getTime()) + " / "+ doubleFormatter(panelYToCandleValue(this.mousePosY)));
		
		// draw mouse hover indication
		passHoverIndicator(candle);
	}


	
	/**
	 * Draws an indicator that shows at which price the mouse is hovering
	 * 
	 * @param dataIndex calculated array index of the data (i.e. candle)
	 */
	private void passHoverIndicator(Candle candle) {
		int dataIndex = this.getDataIndexByMousePos();
		if(dataIndex == -1) {
			return;
		}
		if (chartView.getChartType() == ChartType.CandleChart) {
			//when candle chart mode, draw a thick outline arround canlde as indicator
			
			int distance = 1;
			int candleX = gridXToPanelX(dataIndex) - (this.candleBodyWidth / 2);

			int candleY = candleValueToPanelY(candle.getClose());
			if (!candle.upwardTrend()) 
				candleY = candleValueToPanelY(candle.getOpen());

			int candleHeight = Math.abs(candleValueToPanelY(candle.getClose()) - candleValueToPanelY(candle.getOpen()));
			
			
			g2d.setColor(colorProfile.getHoverColor());
			g2d.setStroke(colorProfile.getHoverStroke());
			g2d.drawRect(candleX - distance, candleY - distance, this.candleBodyWidth + (distance * 2),	candleHeight + (distance * 2));

			//draw tooltip if selected
			drawCandleTooltipAt(this.mousePosX+10, this.mousePosY+10, candle);
	
		} else {
			//when line chart mode, draw a circle as indicator
			g2d.setColor(colorProfile.getHoverColor());
			g2d.fillOval(gridXToPanelX(dataIndex) - 5, candleValueToPanelY(candle.getClose()) - 5, 10, 10);
		}
	}

	/**
	 * Checks weather the mouse cursor is within a distance to a candle
	 * @param dist the distance
	 * @param candle the candle
	 * @return true if cursor is within the distance
	 */
	private boolean isMouseInDistanceToCandle(float dist, Candle candle) {
		if((Math.abs(this.mousePosY-candleValueToPanelY(candle.getClose())) < dist) || (Math.abs(this.mousePosY-candleValueToPanelY(candle.getOpen())) < dist)){
			return true;
		}
		return false;
	}

	/** draws tooltip label */
	private void drawCandleTooltipAt(int x, int y, Candle candle) {
		if (!chartView.getTooltipCheckBox().isSelected()) {
			return;
		}
		if(!isMouseInDistanceToCandle(75,candle)) {
			return;
		}	
				
		Color hoverColor = colorProfile.getHoverColor();
		Color hoverLableTextColor = colorProfile.getHoverLableTextColor();
		BasicStroke hoverLableStroke = colorProfile.getHoverLableStroke();
		
		drawLableAt("h :" + doubleFormatter(candle.getHigh()), x + 10, y + 20, 70, hoverColor, hoverLableTextColor, hoverLableStroke);
		drawLableAt("c :" + doubleFormatter(candle.getClose()), x + 10, y + 40, 70, hoverColor, hoverLableTextColor, hoverLableStroke);
		drawLableAt("o :" + doubleFormatter(candle.getOpen()), x + 10, y + 60, 70, hoverColor, hoverLableTextColor, hoverLableStroke);
		drawLableAt("l :" + doubleFormatter(candle.getLow()), x + 10, y + 80, 70, hoverColor, hoverLableTextColor, hoverLableStroke);
		drawLableAt("t :" + chartView.getInterval().timestampToString(candle.getTime()), x + 10, y + 100, 70, hoverColor, hoverLableTextColor, hoverLableStroke);
		
		
	}

	// draws a lable at given position 
	private void drawLableAt(String text, int x, int y, int width, Color backgroundColor, Color textColor,	BasicStroke stroke) {
		int heigth = 16;
		g2d.setFont(g2d.getFont().deriveFont(Font.BOLD,10f));
		g2d.setColor(backgroundColor);
		g2d.fillRect(x, y - heigth, width, heigth);		
		g2d.setStroke(stroke);
		g2d.drawRect(x, y - heigth, width, heigth);
		g2d.setColor(textColor);
		g2d.drawString(text, x + 4, y - 4);
	}

	
	
	/**
	 * Draws the grid of coordinate system
	 */
	@Override
	void passBackgroundGrid() {
		addLineToInformation("From: " + chartView.getInterval().getDate(timeFrom)+", To:" + chartView.getInterval().getDate(timeTo));
		//addLineToInformation("available data: " + stockDataCalculator.getCandles().length);
		
		
		//draw a bit bigger to hint endless space
		int extraSpace = 0;
		for (int y = 0; y < gridStepsY; y++) {
			for (int x = 0-extraSpace; x < dataAmount+extraSpace; x++) {

				// get position
				int panelPosX = gridXToPanelX(x);
				int panelPosY = gridYToPanelY(y);

				// draw some helpers
				g2d.setColor(colorProfile.getDotsColor());
				g2d.fillOval(panelPosX - (colorProfile.getDotSize() / 2), panelPosY - (colorProfile.getDotSize() / 2),	colorProfile.getDotSize(), colorProfile.getDotSize());
				
			}
		}
	}

	/**
	 * Draws the axes of the coordinate system
	 */
	@Override
	void passAxes() {
		g2d.setFont(g2d.getFont().deriveFont(11f));
		g2d.setColor(colorProfile.getDescriptionColor());		
		
		
		g2d.setStroke(colorProfile.getBorderlineStroke());
		g2d.drawLine(borderLinePosX, panelInsetY, borderLinePosX, panelSizeY - panelInsetY);// right y axis
		g2d.drawLine(panelInsetX - this.candleBodyWidth, borderLinePosY, borderLinePosX, borderLinePosY);// bottom x axis
		
		
		g2d.setStroke(colorProfile.getCellStroke());
		
		g2d.drawLine(indicatorLablePosX, borderLinePosY, indicatorLableSizeX, borderLinePosY);// visual guide 
			
		
		// Y description
		for (int y = 0; y < gridStepsY; y++) {
			int panelY = gridYToPanelY(y);
			g2d.drawString(doubleFormatter(gridYToCandleValue(y)), borderLinePosX + 6, panelY);
			g2d.drawLine(borderLinePosX-5, panelY, borderLinePosX, panelY);
			
		}
		
		// X description
		for (int x = 0; x < dataAmount; x++) {
			int panelX = gridXToPanelX(x);
			
			if ((x % 3 == 0)) {
				//every third step
				g2d.drawLine(panelX,borderLinePosY-5,panelX, borderLinePosY);
			}
			if ((x % 5 == 0)) {
				//every fifth step
				Candle c = latestCandles[x];
				String time = chartView.getInterval().timestampToString(c.getTime());
				//int width = g2d.getFontMetrics().stringWidth(time);
				g2d.drawString(time, panelX - (candleBodyWidth / 2), borderLinePosY + 14);
			
			}

		}

	}

	/**
	 * Draws information when loading or error occurred
	 */
	@Override
	void passAccessInformation() {

		if (this.state == StockDataStateType.no_access) {
			g2d.setColor(colorProfile.getErrorColor());
		}
		if (this.state == StockDataStateType.unclear) {
			g2d.setColor(colorProfile.getHoverColor());
		}
		
		g2d.setFont(g2d.getFont().deriveFont(15f));
		g2d.drawString(this.state.toString(), (this.getSize().width / 2) - 30, this.getSize().height / 2);
		chartView.setStockInfo(this.state.toString());
	}

	/**
	 * Draws the line chart
	 */
	@Override
	void passLineChart() {
		if (chartView.getChartType() != ChartType.LineChart) {
			return;
		}

		
		//this.stockDataCalculator
		
		int[] x = new int[dataAmount];
		int[] y = new int[dataAmount];

		g2d.setColor(colorProfile.getLineChartColor());

		for (int i = 0; i < dataAmount; i++) {
			y[i] = candleValueToPanelY(latestCandles[i].getClose());
			x[i] = gridXToPanelX(i);
		}

		g2d.drawPolyline(x, y, dataAmount);
	}

	/**
	 * Draws the candles
	 */
	@Override
	void passCandles() {
		if (chartView.getChartType() != ChartType.CandleChart) {
			return;
		}
		
		for (int i = 0; i < dataAmount; i++) {
			int panelPosX = gridXToPanelX(i);
			drawCandleAt(latestCandles[i], panelPosX);
		}
	}

	private void drawCandleAt(Candle candle, int pos) {

		int open = candleValueToPanelY(candle.getOpen());
		int close = candleValueToPanelY(candle.getClose());
		int high = candleValueToPanelY(candle.getHigh());
		int low = candleValueToPanelY(candle.getLow());

		int height = Math.abs(close - open);

		int startBodyPosX = pos - (candleBodyWidth / 2);
		int startBodyPosY = open;

		Color fill = colorProfile.getDownCandleColor();
		// Color line = candleOutlineColor;
		GradientPaint gp = new GradientPaint(0, low, fill, 0, high, this.colorProfile.getShade(fill));

		// flip
		if (candle.upwardTrend()) {
			fill = colorProfile.getUpCandleColor();
			startBodyPosY = close;
			gp = new GradientPaint(0, low, this.colorProfile.getShade(fill), 0, high, fill);
		}

		/*
		 * g2d.setStroke(candleStroke); g2d.setColor(line); //g2d.drawLine(pos, low,
		 * pos, high);
		 * 
		 * g2d.drawRect(pos- dochtWidth/2, high, dochtWidth, low-high);
		 * g2d.drawRect(startBodyPosX, startBodyPosY, candleBodyWidth, height);
		 * g2d.setColor(fill);
		 */

		g2d.setPaint(gp);
		
		g2d.fillRect(pos - dochtWidth / 2, high, dochtWidth, low - high);
		g2d.fillRect(startBodyPosX, startBodyPosY, candleBodyWidth, height);

	}

	/**
	 * Draws the indicator lines
	 */
	@Override
	void passIndicatorLines() {
		for (IndicatorBase indicator : this.indicatorList) {
			if (indicator.getType() == IndicatorType.BollingerBands) {
				drawBollingerBands((BollingerBands) indicator);
			}
			if (indicator.getType() == IndicatorType.SimpleMovingAverage) {
				drawSimpleMoveingAverage((SimpleMovingAverage) indicator);
			}
		}

	}

	/**
	 * Draws the indicator backgrounds
	 */
	@Override
	void passIndicatorBackgrounds() {

		for (IndicatorBase indicator : this.indicatorList) {
			if (indicator.getType() == IndicatorType.BollingerBands) {
				drawBollingerBandsBackground((BollingerBands) indicator);
			}
		}

	}

	private void drawSimpleMoveingAverage(SimpleMovingAverage simpleMovingAverage) {
		try {
			double sma[] = simpleMovingAverage.getSMA(dataAmount, stockDataCalculator.getClosePriceDataArray());
		
			int[] x = new int[dataAmount];
			int[] y = new int[dataAmount];

			for (int i = 0; i < dataAmount; i++) {
				y[i] = candleValueToPanelY(sma[i]);
				x[i] = gridXToPanelX(i);
			}

			g2d.setStroke(this.colorProfile.getSMAStroke());
			g2d.setColor(simpleMovingAverage.getColor());
			g2d.drawPolyline(x, y, dataAmount);
			
			drawLableAt("SMA", indicatorLablePosX, y[0], indicatorLableSizeX, colorProfile.changeAlpha(simpleMovingAverage.getColor(), 80), simpleMovingAverage.getColor(), new BasicStroke(1f));
			addLineToInformation("Indicator:" + simpleMovingAverage.toString());

		} catch (Exception e) {
			addLineToInformation("Indicator: " + simpleMovingAverage.toString() +" ERROR: " + e.getMessage());
		}
	}

	private void drawBollingerBands(BollingerBands bollingerbands) {
		try {
			int[] x = new int[dataAmount];
			int[] yupper = new int[dataAmount];
			int[] ylower = new int[dataAmount];

			double upper[] = bollingerbands.getUpperBollingerBand(dataAmount,
					stockDataCalculator.getClosePriceDataArray());
			double lower[] = bollingerbands.getLowerBollingerBand(dataAmount,
					stockDataCalculator.getClosePriceDataArray());

			for (int i = 0; i < dataAmount; i++) {
				yupper[i] = candleValueToPanelY(upper[i]);
				ylower[i] = candleValueToPanelY(lower[i]);
				// x[i] = timestampToPanelX(latestCandles[i].time());
				x[i] = gridXToPanelX(i);
			}

			g2d.setStroke(this.colorProfile.getBBStroke());
			g2d.setColor(bollingerbands.getColor());
			g2d.drawPolyline(x, yupper, dataAmount);

			g2d.setColor(bollingerbands.getColor());
			g2d.drawPolyline(x, ylower, dataAmount);

			drawLableAt("BB upper", indicatorLablePosX, yupper[0], indicatorLableSizeX, colorProfile.changeAlpha(bollingerbands.getColor(), 80), bollingerbands.getColor(), new BasicStroke(1f));
			drawLableAt("BB lower", indicatorLablePosX, ylower[0], indicatorLableSizeX, colorProfile.changeAlpha(bollingerbands.getColor(), 80), bollingerbands.getColor(), new BasicStroke(1f));

			addLineToInformation("Indicator: " + bollingerbands.toString());
		} catch (Exception e) {
			addLineToInformation("Indicator: " + bollingerbands.toString()+" ERROR: " + e.getMessage());
		}
	}

	private void drawBollingerBandsBackground(BollingerBands bollingerbands) {
		try {
			int[] x = new int[dataAmount];
			int[] yupper = new int[dataAmount];
			int[] ylower = new int[dataAmount];

			double upper[] = bollingerbands.getUpperBollingerBand(dataAmount,
					stockDataCalculator.getClosePriceDataArray());
			double lower[] = bollingerbands.getLowerBollingerBand(dataAmount,
					stockDataCalculator.getClosePriceDataArray());

			for (int i = 0; i < dataAmount; i++) {
				yupper[i] = candleValueToPanelY(upper[i]);
				ylower[i] = candleValueToPanelY(lower[i]);
				x[i] = gridXToPanelX(i);
			}

			Color myAlphaAlpha1 = this.colorProfile.changeAlpha(bollingerbands.getColor(), 150);
			Color myAlphaAlpha2 = this.colorProfile.changeAlpha(bollingerbands.getColor(), 10);
			g2d.setColor(bollingerbands.getColor());

			GradientPaint gradientLeftRight = new GradientPaint(this.getWidth() / 10, 0, myAlphaAlpha2, this.getWidth(),
					0, myAlphaAlpha1);
			g2d.setPaint(gradientLeftRight);

			// version 1
			int[] xPoly = joinArrays(x, invert(x));
			int[] yPoly = joinArrays(ylower, invert(yupper));
			g2d.fillPolygon(xPoly, yPoly, xPoly.length);

		} catch (Exception e) {
			// addLineToInformation("ERROR: Bollinger Bands:" + e.getMessage());
		}
	}

	private int[] joinArrays(int[] a, int[] b) {
		int[] newArray = new int[a.length + b.length];
		for (int i = 0; i < a.length; i++) {
			newArray[i] = a[i];
		}
		for (int i = 0; i < b.length; i++) {
			newArray[i + a.length] = b[i];
		}
		return newArray;
	}

	private int[] invert(int[] array) {
		int[] invertedArray = new int[array.length];
		invertedArray = array.clone();

		for (int i = 0; i < invertedArray.length / 2; i++) {
			int temp = invertedArray[i];
			invertedArray[i] = invertedArray[invertedArray.length - 1 - i];
			invertedArray[invertedArray.length - 1 - i] = temp;
		}
		return invertedArray;
	}

	/**
	 * Draws the alarms
	 */
	@Override
	void passAlarm() {
		if (this.chartView.getAlarmWrapper() == null) {
			return;
		}

		AlarmWrapper alarm = this.chartView.getAlarmWrapper();

		for (double value : alarm.getAlarms()) {
			g2d.setColor(colorProfile.getAlarmColor());
			g2d.setStroke(new BasicStroke(1f));
			int y = candleValueToPanelY(value);

			g2d.drawLine(indicatorLablePosX, y, panelSizeX, y);

			drawLableAt(doubleFormatter(value), indicatorLablePosX, y, indicatorLableSizeX,colorProfile.changeAlpha(colorProfile.getAlarmColor(), 80), colorProfile.getAlarmColor(), new BasicStroke(1f));

			addLineToInformation("Alarm at: " + value);
		}
	}


}
