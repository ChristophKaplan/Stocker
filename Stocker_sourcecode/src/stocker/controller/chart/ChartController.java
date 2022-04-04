package stocker.controller.chart;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JMenuItem;
import stocker.controller.general.ControllerBaseInternal;
import stocker.model.database.AlarmWrapper;
import stocker.model.database.DatabaseModel;
import stocker.model.general.ChartType;
import stocker.model.general.DisplayType;
import stocker.model.general.FrameProfileBase;
import stocker.model.general.FrameProfileChart;
import stocker.model.general.IndicatorType;
import stocker.model.general.ViewType;
import stocker.model.general.TimeInterval;
import stocker.model.properties.PropertiesModel;
import stocker.view.chart.DialogAlarm;
import stocker.view.chart.ChartView;
import stocker.view.chart.DialogIndicator;
import stocker.view.chart.DialogIndicatorParameter;
import stocker.view.general.DesktopViewBase;
import stocker.view.general.InternalViewBase;

/**
 * 
 * 
 * The Chart Controller class is responsible for receiving, generating and
 * processing chart view events and, if necessary, manipulating data in the
 * model. It also organizes the indicator and alarm dialogs.
 * 
 * @author Christoph Kaplan
 *
 */
public class ChartController extends ControllerBaseInternal {

	private DialogIndicator indicatoDialogView;
	private DialogIndicatorParameter indicatorViewDialogParameter;
	private DialogAlarm alarmDialogView;
	private ArrayList<ChartView> viewList = new ArrayList<ChartView>();

	public ChartController(DesktopViewBase desktopViewBase, PropertiesModel propertiesModel,
			DatabaseModel databaseModel) {
		super(ViewType.Chart, desktopViewBase, propertiesModel, databaseModel);
	}

	/**
	 * Gets a {@code ChartView} object by its id.
	 * 
	 * @param id identifies the {@code ChartView} object
	 * @return {@code ChartView} object with the given id
	 * @throws Exception thrown when no {@code ChartView} object with the id is
	 *                   found
	 */
	private ChartView getChartViewById(int id) throws Exception {
		for (ChartView cv : viewList) {
			if (cv.getID() == id)
				return cv;
		}
		throw new Exception("getChartViewById(" + id + "): " + "cant find chart");
	}

	/**
	 * Differentiates between {@code ActionEvent} occurring in the menubar, and
	 * invokes further methods accordingly. Invoked from {@code MenuBarChart} when
	 * action occurs. {@link #onOpenAlarms} {@link #onOpenIndicators}
	 * {@link #onChartTypeChange} {@link #onTimeintervalChange}
	 * 
	 * @param e           {@code ActionEvent} that occurred at {@code MenuBarChart}
	 * @param chartWindow corresponding {@code ChartView} object where the action
	 *                    occurred
	 */
	public void chartMenuBarActionPerformed(ActionEvent e, ChartView chartWindow) {

		// open alarm dialog
		if (e.getSource() == chartWindow.getChartMenuBar().getAlarmsItem()) {
			onOpenAlarms(chartWindow);
		}
		// open indicator dialog
		if (e.getSource() == chartWindow.getChartMenuBar().getIndicatorsItem()) {
			onOpenIndicators(chartWindow);
		}
		// change chartType
		for (JMenuItem i : chartWindow.getChartMenuBar().getMenuItemCharttypeMap().keySet()) {
			if (e.getSource() == i) {
				ChartType c = chartWindow.getChartMenuBar().getMenuItemCharttypeMap().get(i);
				onChartTypeChange(chartWindow, c);
			}
		}
		// change timeInterval
		for (JMenuItem i : chartWindow.getChartMenuBar().getTimeIntervalMenuItemMap().keySet()) {
			if (e.getSource() == i) {
				TimeInterval res = chartWindow.getChartMenuBar().getTimeIntervalMenuItemMap().get(i);
				onTimeintervalChange(chartWindow, res);
			}
		}
	}

	/**
	 * Invoked from {@code ChartRendererBase} when action occurs.
	 * 
	 * @param e     {@code MouseEvent} that occurred at {@code ChartRendererBase}
	 * @param x     mouse x position
	 * @param y     mouse y position
	 * @param price current price that corresponds to the mouse y position
	 */
	public void chartRendererMouseclickPerformed(MouseEvent e, int x, int y, double price) {
		//System.out.println("mouseclickPerformedChartPainter: " + x + "/" + y + " - " + price);
	}

	/**
	 * Opens a dialog to set/remove an alarm
	 * 
	 * @param chartView {@code ChartView} object that the alarms corresponds to.
	 */
	public void onOpenAlarms(ChartView chartView) {
		AlarmWrapper alarmWrapper = chartView.getAlarmWrapper();
		double suggestStartValue = 0;
		alarmDialogView = new DialogAlarm(suggestStartValue, chartView.getStock().getSymbol(), alarmWrapper,
				getDesktopViewBase(), this);
		alarmDialogView.setShow();
	}

	/**
	 * Invoked from {@code DialogAlarm} when action occurs.
	 * 
	 * @param e {@code ActionEvent} that occurred at {@code DialogAlarm}
	 */
	public void dialogAlarmActionPerformed(ActionEvent e) {
		// closes the alarmDialogView
		if (e.getSource() == alarmDialogView.getOkButton()) {
			alarmDialogView.dispose();
		}
		// add alarm
		if (e.getSource() == alarmDialogView.getAddButton()) {
			double a = alarmDialogView.getNewAlarmValue();
			getDatabaseModel().addAlarm(alarmDialogView.getSymbol(), a);
			alarmDialogView.addAlarmToList(a);
		}
		// remove alarm
		if (e.getSource() == alarmDialogView.getRemoveButton()) {
			Double value = alarmDialogView.getSelectedAlarm();
			if (value != null) {
				getDatabaseModel().removeAlarm(alarmDialogView.getSymbol(), value);
				alarmDialogView.removeAlarmFromList(value);
			}
		}
	}

	/**
	 * Opens a dialog to set/remove indicators
	 * 
	 * @param chartView {@code ChartView} object that the indicators correspond to.
	 */
	public void onOpenIndicators(ChartView chartView) {
		indicatoDialogView = new DialogIndicator(chartView.getFrameProfile(), getDesktopViewBase(),
				getPropertiesModel());
		indicatoDialogView.setUpListener(this);
		indicatoDialogView.setShow();
	}

	/**
	 * Invoked from {@code DialogIndicator} when action occurs.
	 * 
	 * @param e {@code ActionEvent} that occurred at {@code DialogIndicator}
	 */
	public void dialogIndicatorActionPerformed(ActionEvent e) {
		// close indicatoDialogView
		if (e.getSource() == indicatoDialogView.getOkButton()) {
			indicatoDialogView.dispose();
		}
		// open indicatorViewDialogParameter dialog
		if (e.getSource() == indicatoDialogView.getAddButton()) {
			IndicatorType selected = indicatoDialogView.getSelectedIndicatorType();
			Color standardColor;
			if (selected == IndicatorType.BollingerBands) {
				standardColor = getPropertiesModel().getIndicatorColorBB();
			} else {
				standardColor = getPropertiesModel().getIndicatorColorSMA();
			}
			indicatorViewDialogParameter = new DialogIndicatorParameter(getDesktopViewBase(), selected, standardColor);
			indicatorViewDialogParameter.setUpListeners(this);
			indicatorViewDialogParameter.setShow();
		}
		// remove indicator
		if (e.getSource() == indicatoDialogView.getRemoveButton()) {
			indicatoDialogView.removeIndicator(indicatoDialogView.getSelectedIndicator());
		}
	}

	/**
	 * Invoked from {@code DialogIndicatorParameter} when action occurs.
	 * 
	 * @param e {@code ActionEvent} that occurred at
	 *          {@code DialogIndicatorParameter}
	 */
	public void dialogIndicatorParameterActionPerformed(ActionEvent e) {
		// add indicator
		if (e.getSource() == indicatorViewDialogParameter.getOkButton()) {
			indicatoDialogView.addIndicator(indicatorViewDialogParameter.getIndicator());
			indicatorViewDialogParameter.dispose();
		}
		// close indicatorViewDialogParameter
		if (e.getSource() == indicatorViewDialogParameter.getCancelButton()) {
			indicatorViewDialogParameter.dispose();
		}

	}

	/**
	 * Sets a {@code ChartType} value to {@code ChartView} object.
	 * 
	 * @param chartView the {@code ChartView} object
	 * @param chartType the {@code ChartType} value
	 */
	public void onChartTypeChange(ChartView chartView, ChartType chartType) {
		// System.out.println("change to:" + chartType.toString());
		chartView.setChartType(chartType);
	}

	/**
	 * Sets a {@code TimeInterval} value to {@code ChartView} object.
	 * 
	 * @param chart    the {@code ChartView} object
	 * @param interval the {@code TimeInterval} value
	 */
	public void onTimeintervalChange(ChartView chart, TimeInterval interval) {
		// System.out.println("change to:" + interval.getResolutionCode());
		chart.setInterval(interval);
	}

	/**
	 * Creates a new @code ChartView} object
	 */
	@Override
	protected void createOwnView(FrameProfileBase frameProfileBase) {
		FrameProfileChart frameProfileChart = (FrameProfileChart) frameProfileBase;
		
		AlarmWrapper alarm = getDatabaseModel().getAlarm(frameProfileChart.getSymbol());
		ChartView chart = new ChartView(frameProfileChart, alarm, getDatabaseModel());
		chart.setUpListeners(this);
		viewList.add(chart);
		addFrameToDesktop(chart);
		chart.setShow();
	}

	/**
	 * Returns an {@code ChartView} object, according to the given
	 * {@code FrameProfile}
	 */
	@Override
	protected InternalViewBase getOwnView(FrameProfileBase frameProfileBase) throws Exception {
		try {
			return getChartViewById(frameProfileBase.getID());
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Updates all {@code FrameProfileBase} objects stored in each {@code ChartView}
	 * object. Invoked by {@code PropertiesModel} before persisting data.
	 */
	@Override
	public void onGetFrameSCREENSTATE() {
		for (ChartView cv : viewList) {
			cv.updateFrameProfile();
		}
	}


	/**
	 * Invoked by {@code PropertiesModel} when {@code FrameProfileBase} object is
	 * removed.
	 */
	@Override
	public void onRemoveFrame(FrameProfileBase frameProfileBase) {
		super.onRemoveFrame(frameProfileBase);
		if (frameProfileBase.getViewType() != this.getViewType())
			return;

		unsubscribeAndRemoveChartOnlyStock(frameProfileBase);
	}

	/**
	 * Unsubscribes and removes a {@code StockData} symbol with {@code DisplayType}.ChartOnly, when its chart view is closed.
	 * @param frameProfileBase {@code FrameProfileBase} closed frame with {@code DisplayType}.ChartOnly
	 */
	private void unsubscribeAndRemoveChartOnlyStock(FrameProfileBase frameProfileBase) {
		try {
			ChartView view = getChartViewById(frameProfileBase.getID());
			if (view.getStock().getDisplayType() == DisplayType.ChartOnly) {
				getDatabaseModel().subscribeStock(view.getStock().getSymbol(), false);
				getDatabaseModel().deleteStock(view.getStock());
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			// e.printStackTrace();
		}
	}

}
