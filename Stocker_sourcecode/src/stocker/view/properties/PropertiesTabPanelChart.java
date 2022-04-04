package stocker.view.properties;

import java.awt.GridBagConstraints;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import stocker.model.general.ChartType;
import stocker.model.general.TimeInterval;
import stocker.view.general.StockerColorField;

/**
 * The chart tab enables the user to change certain settings regarding the chart view
 * @author Christoph Kaplan
 *
 */
public class PropertiesTabPanelChart extends PropertiesTabPanelBase {
	private static final long serialVersionUID = -6826259217562220366L;

	private JComboBox<ChartType> chartTypBox;
	private JComboBox<TimeInterval> timeIntervalBox;
	private StockerColorField alarmColor;
	private StockerColorField indicatorColorSMA;
	private StockerColorField indicatorColorBB;

	
	/**
	 * {@code PropertiesTabPanel} constructor
	 * @param sizeX the size x
	 * @param sizeY the size y
	 */
	public PropertiesTabPanelChart(int sizeX, int sizeY) {
		super(sizeX, sizeY);
	}

	/**
	 * Sets the components and layout
	 */
	void setUp() {
		super.setUp();
		chartTypBox = new JComboBox<ChartType>(ChartType.values());
		timeIntervalBox = new JComboBox<TimeInterval>(TimeInterval.values());

		alarmColor = new StockerColorField();
		indicatorColorSMA = new StockerColorField();
		indicatorColorBB = new StockerColorField();

		JLabel label1 = new JLabel("Chart type");
		JLabel label2 = new JLabel("Interval type");
		JLabel label3 = new JLabel("Alarm color");
		JLabel label4 = new JLabel("Indicator color Simple Moving Average");
		JLabel label5 = new JLabel("Indicator color Bollinger Band");

		setToGridBag(new JLabel("Choose standard settings"), 0, 0, 2, 1, 0.0f, 0.0f, GridBagConstraints.NONE);
		
		setToGridBag(label1, 0, 1, 1, 1, 0.0f, 0.0f, GridBagConstraints.BOTH);
		setToGridBag(chartTypBox, 1, 1, 1, 1, 0.5f, 0.0f, GridBagConstraints.NONE);
		setToGridBag(label2, 0, 2, 1, 1, 0.0f, 0.0f, GridBagConstraints.BOTH);
		setToGridBag(timeIntervalBox, 1, 2, 1, 1, 0.5f, 0.0f, GridBagConstraints.NONE);

		setToGridBag(label3, 0, 3, 1, 1, 0.0f, 0.0f, GridBagConstraints.BOTH);
		setToGridBag(alarmColor, 1, 3, 1, 1, 0.5f, 0.0f, GridBagConstraints.NONE);
		setToGridBag(label4, 0, 4, 1, 1, 0.0f, 0.0f, GridBagConstraints.BOTH);
		setToGridBag(indicatorColorSMA, 1, 4, 1, 1, 0.5f, 0.0f, GridBagConstraints.NONE);
		setToGridBag(label5, 0, 5, 1, 1, 0.0f, 0.0f, GridBagConstraints.BOTH);
		setToGridBag(indicatorColorBB, 1, 5, 1, 1, 0.5f, 0.0f, GridBagConstraints.NONE);

		setToGridBag(new JLabel(""), 0, 6, 2, 1, 0.0f, 1.0f, GridBagConstraints.NONE);
	}

	/**
	 * Gets the chart type box instance
	 * @return the chart type box instance
	 */
	public JComboBox<ChartType> getChartTypBox() {
		return this.chartTypBox;
	}

	/**
	 * Gets the time interval box instance
	 * @return the time interval box instance
	 */
	public JComboBox<TimeInterval> getTimeIntervalBox() {
		return this.timeIntervalBox;
	}
	
	/**
	 * Gets the alarm color
	 * @return the alarm color
	 */
	
	public StockerColorField getAlarmColor() {
		return this.alarmColor;
	}
	
	/**
	 * Gets the sma indicator color
	 * @return the sma indicator color
	 */
	public StockerColorField getIndicatorColorSMA() {
		return this.indicatorColorSMA;
	}
	
	/**
	 * Gets the bollingerbands indicator color
	 * @return the bollingerbands indicator color
	 */
	public StockerColorField getIndicatorColorBB() {
		return this.indicatorColorBB;
	}
}