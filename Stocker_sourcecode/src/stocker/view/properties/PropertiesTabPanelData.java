package stocker.view.properties;

import java.awt.Dimension;
import java.awt.GridBagConstraints;

import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import stocker.controller.properties.PropertiesController;
import stocker.model.general.DataProviderProfile;

/**
 * 
 * The data tab enables the user to create, select or remove new data provider profiles.
 * @author Christoph Kaplan
 *
 */
public class PropertiesTabPanelData extends PropertiesTabPanelBase {
	private static final long serialVersionUID = 5138745752966817200L;

	private JButton removeRowButton;
	private JButton addRowButton;
	private JPanel currentProviderPanel;
	private JLabel currentProviderLabel;
	private JTable table;
	private DataProviderProfileTableModel model;
	private DataProviderProfile currentDataProviderProfileSCREENSTATE;

	/**
	 * {@code PropertiesTabPanelData} constructor
	 * 
	 * @param sizeX the size x
	 * @param sizeY the size y
	 */
	public PropertiesTabPanelData(int sizeX, int sizeY) {
		super(sizeX, sizeY);
	}

	/**
	 * Sets the components and layout
	 */
	void setUp() {
		super.setUp();

		model = new DataProviderProfileTableModel();
		table = new JTable(model);
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting())
					onRowClicked(table.getSelectedRow());
			}
		});

		JScrollPane tableScrollPane = new JScrollPane(table);
		tableScrollPane.setMinimumSize(new Dimension(getSize().width / 2, getSize().height / 4));

		currentProviderLabel = new JLabel();
		currentProviderPanel = new JPanel();
		currentProviderPanel.add(currentProviderLabel);
		currentProviderPanel.setBorder(BorderFactory.createTitledBorder("Current selection"));

		addRowButton = new JButton("Add new");
		removeRowButton = new JButton("Remove selected");

		setToGridBag(currentProviderPanel, 0, 0, 2, 1, 0.5f, 0.0f, GridBagConstraints.HORIZONTAL);
		setToGridBag(tableScrollPane, 0, 1, 2, 1, 0.0f, 0.0f, GridBagConstraints.HORIZONTAL);
		setToGridBag(addRowButton, 0, 2, 1, 1, 0.0f, 0.0f, GridBagConstraints.NONE);
		setToGridBag(removeRowButton, 1, 2, 1, 1, 0.0f, 1.0f, GridBagConstraints.NONE);
	}

	/**
	 * Sets the listener
	 * 
	 * @param controller the listener
	 */
	public void setUpListeners(PropertiesController controller) {
		addRowButton.addActionListener(controller);
		removeRowButton.addActionListener(controller);
	}

	/**
	 * Initializes the current selected data provider profile and the list of
	 * available data provider profiles.
	 * 
	 * @param currentDataProviderProfile the current data provider profile
	 * @param dataProviderProfiles       the list of available data provider
	 *                                   profiles
	 */
	public void initDataProviderProfiles(DataProviderProfile currentDataProviderProfile,
			ArrayList<DataProviderProfile> dataProviderProfiles) {
		if (currentDataProviderProfile != null && !currentDataProviderProfile.isValid()) {
			return;
		}
		this.currentDataProviderProfileSCREENSTATE = currentDataProviderProfile;
		this.model.setDataProviderProfilesSCREENSTATE(dataProviderProfiles);
		showCurrentDataprovider();
	}

	/**
	 * Invoked when a row click event occurred at
	 * {@code DataProviderProfileTableModel}. When the selection is equal to null,
	 * sets the first entry as selected.
	 * 
	 * @param row clicked row
	 */
	void onRowClicked(int row) {
		// System.out.println("onRowClicked(" + row +")");
		DataProviderProfile selectedProvider = model.getDataProviderProfile(row);
		this.currentDataProviderProfileSCREENSTATE = selectedProvider;
		showCurrentDataprovider();
	}

	/**
	 * Gets the current data provider profile.
	 * 
	 * @return the current data provider profile
	 */
	DataProviderProfile getCurrentDataProviderProfileSCREENSTATE() {
		return this.currentDataProviderProfileSCREENSTATE;
	}

	/**
	 * Shows the current data provider
	 */
	private void showCurrentDataprovider() {
		if (getCurrentDataProviderProfileSCREENSTATE() == null) {
			currentProviderLabel.setText("no profile selected");
			return;
		}

		String s = "Selected: " + getCurrentDataProviderProfileSCREENSTATE().getName();
		currentProviderLabel.setText(s);
	}

	/**
	 * Gets the add row button instance.
	 * 
	 * @return the add row button instance
	 */
	public JButton getAddRowButton() {
		return this.addRowButton;
	}

	/**
	 * Gets the remove row button instance.
	 * 
	 * @return the remove row button instance
	 */
	public JButton getRemoveRowButton() {
		return this.removeRowButton;
	}

	/**
	 * Gets the list of available {@code DataProviderProfile} objects.
	 * 
	 * @return the available {@code DataProviderProfile} objects
	 */
	public ArrayList<DataProviderProfile> getDataProviderProfilesSCREENSTATE() {
		return model.getDataProviderProfilesSCREENSTATE();
	}

	/**
	 * Adds a {@code DataProviderProfile} object to the list.
	 * 
	 * @param dataProviderProfile the {@code DataProviderProfile} object
	 */
	public void addDataProviderProfile(DataProviderProfile dataProviderProfile) {
		if (dataProviderProfile == null || !dataProviderProfile.isValid()) {
			System.out.println("addDataProviderProfile(): profile is not valid or null");
			return;
		}
		model.addDataProviderProfile(dataProviderProfile);
	}

	/**
	 * Removes the selected {@code DataProviderProfile} object from the list
	 */
	public void removeDataProviderProfile() {
		if (this.currentDataProviderProfileSCREENSTATE == null) {
			return;
		}
		// remove selected
		System.out.println("remove this " + this.currentDataProviderProfileSCREENSTATE.getName());

		model.removeDataProviderProfile(this.currentDataProviderProfileSCREENSTATE);
		this.currentDataProviderProfileSCREENSTATE = null;

	}
}