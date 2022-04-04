package stocker.view.properties;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import stocker.model.general.DataProviderProfile;

/**
 * Table model to render and handle the available {@code DataProviderProfile} objects
 * @author Christoph Kaplan
 *
 */
public class DataProviderProfileTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -2505916218634153050L;

	/**
	 * Column names
	 */
	private String[] cols = { "Name", "Api key", "Pull url", "Push url" };
	
	/**
	 * List of {@code DataProviderProfile} objects 
	 */
	private ArrayList<DataProviderProfile> availableDataProviderProfilesSCREENSTATE = new ArrayList<DataProviderProfile>();
	
	/**
	 * Gets the size of the list.
	 */
	@Override
	public int getRowCount() {
		return availableDataProviderProfilesSCREENSTATE.size();
	}
	
	/**
	 * Gets the length of the column array
	 */
	@Override
	public int getColumnCount() {
		return cols.length;
	}
	
	/**
	 * Gets the value of the column at an index
	 */
	@Override
	public String getColumnName(int columnIndex) {
		return cols[columnIndex];
	}
	
	/**
	 * Gets the value at rowIndex and columnIndex
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		DataProviderProfile dpp = availableDataProviderProfilesSCREENSTATE.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return dpp.getName();
		case 1:
			return dpp.getApiKey();
		case 2:
			return dpp.getPullURL();
		case 3:
			return dpp.getPushURL();
		default:
			return null;
		}
	}
	
	
	/**
	 * Gets a specific {@code DataProviderProfile} object by its index in the list.
	 * @param i the index
	 * @return the  {@code DataProviderProfile} object
	 */
	public DataProviderProfile getDataProviderProfile(int i) {
		if( this.availableDataProviderProfilesSCREENSTATE.size() > i && i >= 0) {
			return this.availableDataProviderProfilesSCREENSTATE.get(i);
		}
		return null;
	}	
	
	
	/**
	 * Adds a specific {@code DataProviderProfile} object from the list
	 * @param dataProviderProfile the {@code DataProviderProfile} object to add
	 */
	public void addDataProviderProfile(DataProviderProfile dataProviderProfile) {
		if(availableDataProviderProfilesSCREENSTATE.contains(dataProviderProfile)) {
			return;
		}
		availableDataProviderProfilesSCREENSTATE.add(dataProviderProfile);
		this.fireTableDataChanged();
	}	
	
	/**
	 * Removes a specific {@code DataProviderProfile} object from the list
	 * @param dataProviderProfile the {@code DataProviderProfile} object to remove
	 */
	public void removeDataProviderProfile(DataProviderProfile dataProviderProfile) {
		if(!availableDataProviderProfilesSCREENSTATE.contains(dataProviderProfile)) {
			return;
		}
		availableDataProviderProfilesSCREENSTATE.remove(dataProviderProfile);
		this.fireTableDataChanged();
	}	
	
	/**
	 * Sets the list of {@code DataProviderProfile}
	 * @param dataProviderProfiles the list of {@code DataProviderProfile}
	 */
	public void  setDataProviderProfilesSCREENSTATE(ArrayList<DataProviderProfile> dataProviderProfiles) {
		this.availableDataProviderProfilesSCREENSTATE = dataProviderProfiles;
	}
	
	/**
	 * Gets the list of {@code DataProviderProfile}
	 * @return the list of {@code DataProviderProfile}
	 */
	public ArrayList<DataProviderProfile>  getDataProviderProfilesSCREENSTATE() {
		return availableDataProviderProfilesSCREENSTATE;
	}
	
	/**
	 * Gets the first entry
	 * @return the first entry
	 */
	public DataProviderProfile getFirst() {
		if( this.availableDataProviderProfilesSCREENSTATE.size() != 0) {
			return this.availableDataProviderProfilesSCREENSTATE.get(0);
		}
		return null;
	}
}