package stocker.view.chart;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;

import stocker.controller.chart.ChartController;
import stocker.model.general.FrameProfileChart;
import stocker.model.general.IndicatorBase;
import stocker.model.general.IndicatorType;
import stocker.model.properties.PropertiesModel;
import stocker.view.general.DesktopViewBase;

/**
 * The indicator dialog gives the user the option to add or remove new indicators.
 * @author Christoph Kaplan
 *
 */
public class DialogIndicator extends DialogViewBase {
	private static final long serialVersionUID = -5564586254549963680L;
	
	private JButton okButton = new JButton("OK");
	private JButton addButton = new JButton("add");
	private JButton removeButton = new JButton("remove");

	private DefaultListModel<IndicatorType> availableIndikatorListModel = new DefaultListModel<IndicatorType>();
	private JList<IndicatorType> availableIndicatorList = new JList<IndicatorType>(availableIndikatorListModel);

	private DefaultListModel<IndicatorBase> addedIndikatorListModel = new DefaultListModel<IndicatorBase>();
	private JList<IndicatorBase> addedIndicatorList = new JList<IndicatorBase>(addedIndikatorListModel);
	
	private PropertiesModel propertiesModel;
	private FrameProfileChart frameProfileChart;
	
	/**
	 * {@code DialogIndicator} constructor
	 * @param frameProfileChart the chart frame profile
	 * @param mainWindow the owner
	 * @param propertiesModel the properties model
	 */
	public DialogIndicator(FrameProfileChart frameProfileChart, DesktopViewBase mainWindow,PropertiesModel propertiesModel) {
		super(frameProfileChart.getSymbol() + "-Indicators", 300, 300, mainWindow);
		this.frameProfileChart = frameProfileChart;
		this.propertiesModel = propertiesModel;	
		setUp();
	}

	/**
	 * Sets the components
	 */
	private void setUp() {
		initAvailableIndicators();
		initIndikatorList();
				
		JLabel availableLabel = new JLabel("available Alarms");		
		JLabel addedLabel = new JLabel("added Alarms");

		setToGridBag(availableLabel, 0, 0, 3, 1,0.0f,0.0f,GridBagConstraints.NONE);
		setToGridBag(availableIndicatorList, 0, 1, 1, 1,0.5f,0.0f,GridBagConstraints.BOTH);
		setToGridBag(addButton     , 1, 1, 1, 1,0.5f,0.0f,GridBagConstraints.HORIZONTAL);
				
		setToGridBag(addedLabel    , 0, 2, 3, 1,0.0f,0.0f,GridBagConstraints.NONE);
		setToGridBag(addedIndicatorList, 0, 3, 1, 1,0.5f,0.0f,GridBagConstraints.BOTH);
		setToGridBag(removeButton  , 1, 3, 1, 1,0.5f,0.0f,GridBagConstraints.HORIZONTAL);
		
		setToGridBag(okButton      , 0, 4, 2, 1,0.0f,1.0f,GridBagConstraints.HORIZONTAL);
				
	}

	/**
	 * Sets the listener.
	 * @param chartController the listener
	 */
	public void setUpListener(ChartController chartController) {		
		okButton.addActionListener(getActionListener(chartController));
		addButton.addActionListener(getActionListener(chartController));
		removeButton.addActionListener(getActionListener(chartController));
	}
	
	/**
	 * Initializes the list of available indicators.
	 */
	private void initAvailableIndicators() {
		for(IndicatorType t : IndicatorType.values()) {
			availableIndikatorListModel.addElement(t);
		}
		availableIndicatorList.setSelectedIndex(0);
				
	}
	
	/**
	 * Initializes the list of added indicators.
	 */
	private void initIndikatorList() {
		for(IndicatorBase ct : frameProfileChart.getIndicators()) {
			 addToIndicatorList(ct);
		}
		addedIndicatorList.setSelectedIndex(0);
		addedIndicatorList.setPreferredSize(new Dimension(100,100));
	}
	
	/**
	 * Adds a specific indicator to the list of added indicators.
	 * @param indicator the specific indicator
	 */
	private void addToIndicatorList(IndicatorBase indicator) {
		if(!addedIndikatorListModel.contains(indicator)) addedIndikatorListModel.addElement(indicator);
	}
	
	/**
	 * Removes a specific indicator from the list of added indicators.
	 * @param indicator the specific indicator
	 */
	private void removeFromIndicatorList(IndicatorBase indicator) {
		addedIndikatorListModel.removeElement(indicator);
	}
	
	/**
	 * Adds specific indicator from properties model
	 * @param indicator the specific indicator to add
	 */
	public void addIndicator(IndicatorBase indicator ) {
		addToIndicatorList(indicator);
		propertiesModel.addIndicator(frameProfileChart.getID(),indicator);
	}
	
	/**
	 * Removes specific indicator from properties model
	 * @param indicator the specific indicator to remove
	 */
	public void removeIndicator(IndicatorBase indicator ) {
		propertiesModel.removeIndicator(frameProfileChart.getID(), indicator);
		removeFromIndicatorList(indicator);
	}
	
	/**
	 * Creates an action listener
	 * @param chartController the listener
	 * @return the action listener
	 */
	private ActionListener getActionListener(ChartController chartController) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chartController.dialogIndicatorActionPerformed(e);
			}	
		};
	}
	
	
	
	/**
	 * Gets the ok button instance.
	 * @return  the ok button instance
	 */
	public JButton getOkButton () {
		return this.okButton;
	}
	
	/**
	 * Gets the add button instance.
	 * @return the add button instance
	 */
	public JButton getAddButton () {
		return this.addButton;
	}
	
	/**
	 * Gets the remove button instance.
	 * @return the remove button instance
	 */
	public JButton getRemoveButton () {
		return this.removeButton;
	}
	
	/**
	 * Gets selected indicator type.
	 * @return the selected indicator type
	 */
	public IndicatorType getSelectedIndicatorType () {
		return availableIndicatorList.getSelectedValue();
	}
	
	/**
	 * Gets selected indicator.
	 * @return the selected indicator
	 */
	public IndicatorBase getSelectedIndicator () {
		return addedIndicatorList.getSelectedValue();
	}


	
	
}
