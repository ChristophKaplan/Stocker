package stocker.view.chart;

import java.awt.GridBagConstraints;
import javax.swing.*;

import stocker.controller.chart.ChartController;
import stocker.model.database.AlarmWrapper;
import stocker.model.database.DatabaseModel;
import stocker.model.general.ChartType;
import stocker.model.general.FrameProfileChart;
import stocker.model.general.TimeInterval;
import stocker.model.stockdata.StockData;
import stocker.view.general.InternalViewBase;

/**
 * The chart view is responsible for the graphic display of the stock chart information.
 * 
 * @author Christoph Kaplan
 *
 */
public class ChartView extends InternalViewBase {
	private static final long serialVersionUID = 7976056982257020679L;

	private ChartRenderer chartRenderer;
	private JLabel stockInfo;
	private JLabel mouseInfo;
	private MenuBarChart chartMenuBar;

	private JCheckBox tooltipCheckBox;

	private StockData stock;

	private AlarmWrapper alarm;

	private int renderDataAmount = 50;

	/**
	 * {@code ChartView} constructor
	 * 
	 * @param frameProfileChart the chart frame profile
	 * @param alarmWrapper      the alarm wrapper
	 * @param databaseModel     the database model
	 */
	public ChartView(FrameProfileChart frameProfileChart, AlarmWrapper alarmWrapper, DatabaseModel databaseModel) {
		super(frameProfileChart, databaseModel);
		this.alarm = alarmWrapper;

		try {
			this.stock = databaseModel.getStockBySymbol(frameProfileChart.getSymbol());
			this.setTitle(frameProfileChart.getTitle());
			
		} catch (Exception e) {
			System.err.println("ChartView() missing equivalent Stock? " + e.getMessage());
			// databaseModel.searchForSpecificSymbol(frameProfile_Chart.getSymbol(), );
			e.printStackTrace();
		}
		updateChartRenderer();
	}

	/**
	 * Sets the components.
	 */
	@Override
	public void setUp() {
		super.setUp();
		
		this.setInset(0);
		
		chartMenuBar = new MenuBarChart();
		setJMenuBar(chartMenuBar);
		
		
		stockInfo = new JLabel("stock info:");
		stockInfo.setOpaque(false);

		mouseInfo = new JLabel("mouse info:");
		mouseInfo.setOpaque(false);
		
		tooltipCheckBox = new JCheckBox("show tooltip");
		tooltipCheckBox.setSelected(true);
		tooltipCheckBox.setOpaque(false);
		
		chartRenderer = new ChartRenderer(this);

		setToGridBag(chartRenderer,   0, 0, 4, 1, 1.0f, 1.0f, GridBagConstraints.BOTH);
		
		setToGridBag(new JLabel(""), 0, 1, 1, 1, 1.0f, 0.0f, GridBagConstraints.BOTH);
		setToGridBag(stockInfo,      1, 1, 1, 1, 1.0f, 0.0f, GridBagConstraints.BOTH);
		setToGridBag(mouseInfo,      2, 1, 1, 1, 1.0f, 0.0f, GridBagConstraints.BOTH);
		setToGridBag(tooltipCheckBox, 3, 1, 1, 1, 1.0f, 0.0f, GridBagConstraints.BOTH);

	}

	/**
	 * Gets the tooltip checkbox.
	 * 
	 * @return the tooltip checkbox
	 */
	public JCheckBox getTooltipCheckBox() {
		return this.tooltipCheckBox;
	}

	/**
	 * Sets listeners.
	 * 
	 * @param controller the listener
	 */
	public void setUpListeners(ChartController controller) {
		super.addListener(controller);
		chartMenuBar.setUpListeners(controller, this);
		chartRenderer.setUpListener(controller);
	}

	/**
	 * Gets the associated stock.
	 * 
	 * @return the associated stock
	 */
	public StockData getStock() {
		return this.stock;
	}

	/**
	 * Gets the time interval.
	 * 
	 * @return the time interval
	 */
	public TimeInterval getInterval() {
		return this.getFrameProfile().getTimeinterval();
	}

	/**
	 * Gets the chart type.
	 * 
	 * @return the chart type
	 */
	public ChartType getChartType() {
		return this.getFrameProfile().getChartType();
	}

	/**
	 * Gets the chart menu bar.
	 * 
	 * @return the chart menu bar
	 */
	public MenuBarChart getChartMenuBar() {
		return this.chartMenuBar;
	}

	/**
	 * Gets the chart renderer instance.
	 * 
	 * @return the chart painter instance
	 */
	public ChartRenderer getChartRenderer() {
		return this.chartRenderer;
	}

	/**
	 * Gets the chart frame profile.
	 */
	@Override
	public FrameProfileChart getFrameProfile() {
		return (FrameProfileChart) super.getFrameProfile();
	}

	/**
	 * Gets the alarm wrapper
	 * 
	 * @return the alarm wrapper
	 */
	public AlarmWrapper getAlarmWrapper() {
		return this.alarm;
	}
	
	
	/**
	 * Sets the chart type
	 * 
	 * @param chartType the chart type
	 */
	public void setChartType(ChartType chartType) {
		this.getFrameProfile().setChartType(chartType);
		this.setTitle(this.getFrameProfile().getTitle());
		chartRenderer.repaint();
	}

	/**
	 * Sets the interval
	 * 
	 * @param interval the interval
	 */
	public void setInterval(TimeInterval interval) {
		this.getFrameProfile().setTimeInterval(interval);
		this.setTitle(this.getFrameProfile().getTitle());
		updateChartRenderer();
	}

	/**
	 * Sets the stock info.
	 * 
	 * @param status the stock info
	 */
	public void setStockInfo(String status) {
		this.stockInfo.setText(status);
	}


	/**
	 * Sets the mouse info.
	 * 
	 * @param status the mouse info
	 */
	public void setMouseInfo(String status) {
		this.mouseInfo.setText(status);
	}
	
	
	/**
	 * Updates the chart renderer
	 */
	void updateChartRenderer() {
		chartRenderer.setUpRenderer(renderDataAmount, getDatabaseModel().getStockDataCalculator(getStock(), getInterval()),	getFrameProfile().getIndicators());
		chartRenderer.repaint();
	}

	/**
	 * When stock data gets updated, update the chart painter.
	 */
	@Override
	public void onStockUpdate(StockData stock) {
		if (!this.stock.getSymbol().equals(stock.getSymbol()))
			return;
		this.stock = stock;
		updateChartRenderer();
	}

	/**
	 * When stock data is calculated.
	 */
	@Override
	public void onStockCalculated(StockData stock) {
		if (!this.stock.getSymbol().equals(stock.getSymbol()))
			return;
		this.stock = stock;
		updateChartRenderer();
	}

	/**
	 * When stock data is added
	 */
	@Override
	public void onStockAdded(StockData stock) {
		if (!this.stock.getSymbol().equals(stock.getSymbol()))
			return;
		this.stock = stock;
	}

	/**
	 * When stock data is removed
	 */
	@Override
	public void onStockRemoved(StockData stock) {
		if (!this.stock.getSymbol().equals(stock.getSymbol()))
			return;
		
		//System.out.println("onStockRemoved(): "+ stock);
		this.doDefaultCloseAction();
		
		//this.stock = null;
	}

	/**
	 * When display type changed.
	 */
	@Override
	public void onStockDisplayTypeChange(StockData stock) {
		// TODO Auto-generated method stub

	}

	/**
	 * When alarm is added, update the chart painter.
	 */
	@Override
	public void onAlarmAdded(AlarmWrapper alarm, double value) {
		if (!alarm.getSymbol().equals(this.stock.getSymbol())) {
			return;
		}
		this.alarm = alarm;
		updateChartRenderer();
	}

	/**
	 * When alarm is removed, update the chart painter, set alarm to null
	 */
	@Override
	public void onAlarmRemove(AlarmWrapper alarm, double value) {
		if (!alarm.getSymbol().equals(this.stock.getSymbol())) {
			return;
		}
		this.alarm = alarm;
		updateChartRenderer();
	}

}
