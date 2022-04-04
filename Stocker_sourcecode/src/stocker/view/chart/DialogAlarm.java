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
import stocker.model.database.AlarmWrapper;
import stocker.view.general.DesktopViewBase;
import stocker.view.general.StockerTextField;
import stocker.view.general.StockerTextField.FilterMethod;

/**
 * The alarm dialog gives the user the option to add or remove new alarms.
 * @author Christoph Kaplan
 *
 */
public class DialogAlarm extends DialogViewBase {
	private static final long serialVersionUID = -7611657714122215683L;
		
	private JButton okButton = new JButton("OK");
	private JButton addButton = new JButton("add");
	private JButton removeButton = new JButton("remove");

	private StockerTextField alarmInputText;

	private DefaultListModel<Double> addedAlarmListModel = new DefaultListModel<Double>();
	private JList<Double> addedAlarmList = new JList<Double>(addedAlarmListModel);
	
	private AlarmWrapper alarmWrapper;
	private double suggestedValue;
	private String symbol;

		
	/**
	 * {@code DialogAlarm} constructor
	 * @param suggestedValue  the value that is suggested to the user
	 * @param symbol the symbol
	 * @param alarmWrapper the alarm wrapper
	 * @param mainWindow the owner
	 * @param chartController the listener
	 */
	public DialogAlarm(double suggestedValue,String symbol,AlarmWrapper alarmWrapper, DesktopViewBase mainWindow, ChartController chartController) {
		super(symbol + "-Alarms", 300, 300, mainWindow);
		this.symbol = symbol;
		this.suggestedValue = suggestedValue;
		this.alarmWrapper = alarmWrapper;
		setUp();
		setUpListener(chartController);
	}

	/**
	 * Sets the components
	 */
	private void setUp() {
		FilterMethod doubleFilterMethod = new FilterMethod() {
			@Override
			public boolean isValid(String text) {
				try {
					Double.parseDouble(text);
					return true;
				} catch (Exception e) {
					return false;
				}
			}
		};
		alarmInputText = new StockerTextField("" + suggestedValue,10,doubleFilterMethod);
		
		initAlarmList();

		JLabel availableLabel = new JLabel("available Alarms");		
		JLabel addedLabel = new JLabel("added Alarms");

		setToGridBag(availableLabel, 0, 0, 3, 1,0.0f,0.0f,GridBagConstraints.NONE);
		setToGridBag(alarmInputText, 0, 1, 1, 1,0.5f,0.0f,GridBagConstraints.BOTH);
		setToGridBag(addButton     , 1, 1, 1, 1,0.5f,0.0f,GridBagConstraints.HORIZONTAL);
				
		setToGridBag(addedLabel    , 0, 2, 3, 1,0.0f,0.0f,GridBagConstraints.NONE);
		setToGridBag(addedAlarmList, 0, 3, 2, 1,0.5f,0.5f,GridBagConstraints.BOTH);
				
		setToGridBag(okButton      , 0, 4, 1, 1,0.0f,0.0f,GridBagConstraints.HORIZONTAL);
		setToGridBag(removeButton  , 1, 4, 1, 1,0.5f,1.0f,GridBagConstraints.HORIZONTAL);
	}

	/**
	 * Initializes the list of present alarms.
	 */
	private void initAlarmList() {
		if(alarmWrapper == null) return;
		for (double val : alarmWrapper.getAlarms()) {
			addAlarmToList(val);
		}
		addedAlarmList.setSelectedIndex(0);
		addedAlarmList.setPreferredSize(new Dimension(100,100));
	}
	
	/**
	 * Adds a alarm value to the list.
	 * @param val alarm value
	 */
	public void addAlarmToList(double val) {
		if(!addedAlarmListModel.contains(val)) addedAlarmListModel.addElement(val);
	}
	
	
	/**
	 * Removes a alarm value from the list.
	 * @param val alarm value
	 */
	public void removeAlarmFromList(double val) {
		addedAlarmListModel.removeElement(val);
	}
	
	
	/**
	 * Sets the listener.
	 * @param chartController the listener
	 */
	void setUpListener(ChartController chartController) {
		okButton.addActionListener(getActionListener(chartController));
		addButton.addActionListener(getActionListener(chartController));
		removeButton.addActionListener(getActionListener(chartController));
	}

	/**
	 * Creates an action listener
	 * @param chartController the listener
	 * @return the action listener
	 */
	ActionListener getActionListener(ChartController chartController) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chartController.dialogAlarmActionPerformed(e);
			}
		};
	}
	
	/**
	 * Gets the symbol.
	 * @return the symbol
	 */
	public String getSymbol() {
		return this.symbol;
	}
		
	/**
	 * Gets the ok button instance.
	 * @return the ok button instance
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
	 * Gets a new alarm value based on user input.
	 * @return new alarm value
	 */
	public double getNewAlarmValue () {
		String text = alarmInputText.getText();
		double alarmValue = Double.parseDouble(text);
		return alarmValue;
	}
	
	/**
	 * Gets the selected alarm.
	 * @return the selected alarm
	 */
	public Double getSelectedAlarm (){
		return addedAlarmList.getSelectedValue();
	}


	
	
}
