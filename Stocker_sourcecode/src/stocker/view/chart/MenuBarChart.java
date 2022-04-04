package stocker.view.chart;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import stocker.controller.chart.ChartController;
import stocker.model.general.ChartType;
import stocker.model.general.TimeInterval;


/**
 * Menu bar of the chart view
 * @author Christoph Kaplan
 *
 */
public class MenuBarChart extends JMenuBar {
	private static final long serialVersionUID = 9000007311172714879L;

	private JMenu charttypMenu;
	private JMenu intervallMenu;
	private JMenu toolsMenu;
	private Map<JMenuItem, ChartType> charttypMenuItems;
	private Map<JMenuItem, TimeInterval> intervallMenuItems;
	private JMenuItem alarmsItem;
	private JMenuItem indicatorsItem;

	public MenuBarChart() {
		setUp();
	}

	/**
	 * Sets the components
	 */
	private void setUp() {
		charttypMenu = new JMenu("Charttyp");
		intervallMenu = new JMenu("Intervall");
		toolsMenu = new JMenu("Tools");
		charttypMenuItems = new HashMap<JMenuItem, ChartType>();
		intervallMenuItems = new HashMap<JMenuItem, TimeInterval>();
		alarmsItem = new JMenuItem("Alarms");
		indicatorsItem = new JMenuItem("Indicators");

		for (ChartType c : ChartType.values()) {
			JMenuItem newItem = new JMenuItem(c.toString());
			charttypMenu.add(newItem);
			charttypMenuItems.put(newItem, c);
		}
		for (TimeInterval r : TimeInterval.values()) {
			JMenuItem newItem = new JMenuItem(r.toString());
			intervallMenu.add(newItem);
			intervallMenuItems.put(newItem, r);
		}

		toolsMenu.add(alarmsItem);
		toolsMenu.add(indicatorsItem);

		add(charttypMenu);
		add(intervallMenu);
		add(toolsMenu);
	}

	/**
	 * Sets the listener
	 * @param controller the listener
	 * @param chartView the corresponding chart view
	 */
	void setUpListeners(ChartController controller, ChartView chartView) {
		for (JMenuItem i : charttypMenuItems.keySet()) {
			i.addActionListener(actionListenerKnowsChartView(controller, chartView));
		}
		for (JMenuItem i : intervallMenuItems.keySet()) {
			i.addActionListener(actionListenerKnowsChartView(controller, chartView));
		}
		alarmsItem.addActionListener(actionListenerKnowsChartView(controller, chartView));
		indicatorsItem.addActionListener(actionListenerKnowsChartView(controller, chartView));
	}

	/**
	 * Creates an action listener
	 * @param controller the listener
	 * @param chartView the corresponding chart view
	 * @return the action listener
	 */
	private ActionListener actionListenerKnowsChartView(ChartController controller, ChartView chartView) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.chartMenuBarActionPerformed(e, chartView);
			}
		};
	}

	/**
	 * Gets the menu item chart type map.
	 * @return the menu item chart type map
	 */
	public Map<JMenuItem, ChartType> getMenuItemCharttypeMap() {
		return this.charttypMenuItems;
	}

	/**
	 * Gets the menu item time interval map.
	 * @return the menu item time interval map
	 */
	public Map<JMenuItem, TimeInterval> getTimeIntervalMenuItemMap() {
		return this.intervallMenuItems;
	}

	/**
	 * Gets the alarm menu item instance
	 * @return the alarm menu item instance
	 */
	public JMenuItem getAlarmsItem() {
		return this.alarmsItem;
	}
	/**
	 * Gets the indicators menu item instance
	 * @return the indicators menu item instance
	 */
	public JMenuItem getIndicatorsItem() {
		return this.indicatorsItem;
	}

}