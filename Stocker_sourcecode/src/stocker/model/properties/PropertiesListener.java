package stocker.model.properties;

import stocker.model.general.DataProviderProfile;


/**
 * Listens to changes in the properties model.
 * @author Christoph Kaplan
 *
 */
public interface PropertiesListener {
	/**
	 * Notify listeners when {@code DataProviderProfile} object has changed
	 * 
	 * @param dataProviderProfile the new {@code DataProviderProfile} object
	 */
	public void onDataProviderChange(DataProviderProfile dataProviderProfile);
}
