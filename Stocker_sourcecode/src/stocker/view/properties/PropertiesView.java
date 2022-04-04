package stocker.view.properties;

import java.awt.*;

import javax.swing.JButton;
import javax.swing.JTabbedPane;
import stocker.controller.properties.PropertiesController;
import stocker.model.database.AlarmWrapper;
import stocker.model.database.DatabaseModel;
import stocker.model.general.ChartType;
import stocker.model.general.FrameProfile;
import stocker.model.general.TimeInterval;
import stocker.model.properties.PropertiesModel;
import stocker.model.stockdata.StockData;
import stocker.view.general.InternalViewBase;


/**
 * The Properties view shows the general "settings" regarding the application as well as server connection settings.
 * @author Christoph Kaplan
 */
public class PropertiesView extends InternalViewBase {
	private static final long serialVersionUID = -2648072089058360745L;

	private PropertiesModel propertiesModel;
	private PropertiesTabPanelGeneral generalPanel;
	private PropertiesTabPanelData dataPanel;
	private PropertiesTabPanelChart chartPanel;

	private JButton saveButton;
	private JButton resetButton;
	private JButton cancleButton;

	/**
	 * {@code PropertiesView} constructor
	 * @param frameProfile the associated 
	 * @param databaseModel the database model
	 * @param propertiesModel the properties model
	 */
	public PropertiesView(FrameProfile frameProfile, DatabaseModel databaseModel, PropertiesModel propertiesModel) {
		super(frameProfile, databaseModel);
		this.propertiesModel = propertiesModel;
		// this.propertiesModel.addObserver(this);
				
	}

	/**
	 * Sets the components
	 */
	@Override
	public void setUp() {
		super.setUp();
		saveButton = new JButton("Save");
		resetButton = new JButton("Reset");
		cancleButton = new JButton("Cancel");

		int tabSizeX = this.getSize().width;
		int tabSizeY = this.getSize().height;
		
		generalPanel = new PropertiesTabPanelGeneral(tabSizeX, tabSizeY);	
		dataPanel = new PropertiesTabPanelData(tabSizeX, tabSizeY);
		chartPanel = new PropertiesTabPanelChart(tabSizeX, tabSizeY);
		
		JTabbedPane tabpane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);

		tabpane.addTab("Data provider panel", dataPanel);
		tabpane.addTab("Chart panel", chartPanel);
		tabpane.addTab("General panel", generalPanel);
		tabpane.setBounds(0, 0, tabSizeX, tabSizeY);
		
		setToGridBag(tabpane     , 0, 0, 3, 1,0.0f,1.0f,GridBagConstraints.BOTH);
		
		setToGridBag(saveButton  , 0, 1, 1, 1,0.5f,0.0f,GridBagConstraints.BOTH);
		setToGridBag(resetButton , 1, 1, 1, 1,0.5f,0.0f,GridBagConstraints.BOTH);
		setToGridBag(cancleButton, 2, 1, 1, 1,0.5f,0.0f,GridBagConstraints.BOTH);
		
		pack();
	}

	/**
	 * Adds the listener
	 * @param controller the listener
	 */
	public void setUpListener(PropertiesController controller) {
		super.addListener(controller);
		saveButton.addActionListener(controller);
		resetButton.addActionListener(controller);
		cancleButton.addActionListener(controller);
		
		dataPanel.setUpListeners(controller);
	}

	
	/**
	 * Loads the relevant data from the properties model to the view components.
	 */
	public void loadViewData() {
		dataPanel.initDataProviderProfiles(propertiesModel.getCurrentDataProviderProfile(),propertiesModel.getDataProviderProfiles());

		generalPanel.getFrameMinXSizeTextField().setText(propertiesModel.getMinFrameSizeX() + "");
		generalPanel.getFrameMinYSizeTextField().setText(propertiesModel.getMinFrameSizeY() + "");

		generalPanel.getFrameChartMinXSizeTextField().setText(propertiesModel.getMinFrameChartSizeX() + "");
		generalPanel.getFrameChartMinYSizeTextField().setText(propertiesModel.getMinFrameChartSizeY() + "");
		
		chartPanel.getChartTypBox().setSelectedItem(propertiesModel.getChartTyp());
		chartPanel.getTimeIntervalBox().setSelectedItem(propertiesModel.getStandardInterval());

		chartPanel.getAlarmColor().setColor(propertiesModel.getAlarmColor());
		chartPanel.getIndicatorColorSMA().setColor(propertiesModel.getIndicatorColorSMA());
		chartPanel.getIndicatorColorBB().setColor(propertiesModel.getIndicatorColorBB());
	}

	/**
	 * Saves the relevant data from the the view components to the properties model.
	 */
	public void saveViewData() {

		int minX = Integer.parseInt(generalPanel.getFrameMinXSizeTextField().getText());
		int minY = Integer.parseInt(generalPanel.getFrameMinYSizeTextField().getText());
		int chartMinX = Integer.parseInt(generalPanel.getFrameChartMinXSizeTextField().getText());
		int chartMinY = Integer.parseInt(generalPanel.getFrameChartMinYSizeTextField().getText());
		
		ChartType ct = (ChartType) chartPanel.getChartTypBox().getSelectedItem();

		TimeInterval rc = (TimeInterval) chartPanel.getTimeIntervalBox().getSelectedItem();

		Color alarmColor = chartPanel.getAlarmColor().getColor();
		Color indicatorColorGD = chartPanel.getIndicatorColorSMA().getColor();
		Color indicatorColorBB = chartPanel.getIndicatorColorBB().getColor();
		
		propertiesModel.setProperties(propertiesModel.getName(), propertiesModel.getEmail(), propertiesModel.getMatrNr(),dataPanel.getCurrentDataProviderProfileSCREENSTATE(), dataPanel.getDataProviderProfilesSCREENSTATE(), minX, minY,chartMinX,chartMinY, ct, rc, alarmColor, indicatorColorGD,indicatorColorBB);
	}

	
	/**
	 * Gets the general panel
	 * @return the general panel
	 */
	public PropertiesTabPanelGeneral getGeneralPanel() {
		return this.generalPanel;
	}
	/**
	 * Gets the data panel
	 * @return the data panel
	 */
	public PropertiesTabPanelData getDataPanel() {
		return this.dataPanel;
	}
	/**
	 * Gets the chart panel
	 * @return the chart panel
	 */
	public PropertiesTabPanelChart getChartPanel() {
		return this.chartPanel;
	}

	/**
	 * Gets the save button instance.
	 * @return the save button instance
	 */
	public JButton getSaveButton() {
		return this.saveButton;
	}
	/**
	 * Gets the reset button instance.
	 * @return the reset button instance
	 */
	public JButton getResetButton() {
		return this.resetButton;
	}
	/**
	 * Gets the cancle button instance.
	 * @return the cancle button instance
	 */
	public JButton getCancleButton() {
		return this.cancleButton;
	}

	
	
	
	@Override
	public void onStockAdded(StockData stock) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStockRemoved(StockData stock) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStockUpdate(StockData stock) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStockCalculated(StockData stock) {
		
	}
	
	
	@Override
	public void onStockDisplayTypeChange(StockData stock) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAlarmAdded(AlarmWrapper alarm, double value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAlarmRemove(AlarmWrapper alarm, double value) {
		// TODO Auto-generated method stub

	}


}
