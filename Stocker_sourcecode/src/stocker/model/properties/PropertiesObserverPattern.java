package stocker.model.properties;

import java.util.ArrayList;

import stocker.model.general.DataProviderProfile;
import stocker.model.general.FrameProfileBase;
import stocker.model.general.FrameProfileChart;

/**
 * Observer pattern for the properties model (with 2 listener types
 * {@code PropertiesFrameListener} and {@code PropertiesListener})
 * 
 * @author Christoph Kaplan
 *
 */
public class PropertiesObserverPattern {

	protected ArrayList<PropertiesFrameListener> propertiesFrameObservers = new ArrayList<PropertiesFrameListener>();
	protected ArrayList<PropertiesListener> propertiesObservers = new ArrayList<PropertiesListener>();

	/**
	 * Adds a listener
	 * 
	 * @param obs the listener
	 */
	public void addFrameObserver(PropertiesFrameListener obs) {
		propertiesFrameObservers.add(obs);
	}

	/**
	 * Removes a listener
	 * 
	 * @param obs the listener
	 */
	public void removeFrameObserver(PropertiesFrameListener obs) {
		propertiesFrameObservers.remove(obs);
	}

	/**
	 * Adds a listener
	 * 
	 * @param obs the listener
	 */
	public void addPropertiesObserver(PropertiesListener obs) {
		propertiesObservers.add(obs);
	}

	/**
	 * Removes a listener
	 * 
	 * @param obs the listener
	 */
	public void removePropertiesObserver(PropertiesListener obs) {
		propertiesObservers.remove(obs);
	}

	/**
	 * Notify listeners when new {@code FrameProfileBase} object is added.
	 * 
	 * @param frameProfileBase the added {@code FrameProfileBase} object
	 */
	protected void onFrameAdd(FrameProfileBase frameProfileBase) {
		for (int i = 0; i < propertiesFrameObservers.size(); i++) {
			propertiesFrameObservers.get(i).onAddFrame(frameProfileBase);
		}
	}

	/**
	 * Notify listeners when new {@code FrameProfileBase} object is updated.
	 * 
	 * @param frameProfileBase the updated {@code FrameProfileBase} object
	 */
	protected void onFrameUpdate(FrameProfileBase frameProfileBase) {
		for (int i = 0; i < propertiesFrameObservers.size(); i++) {
			propertiesFrameObservers.get(i).onFrameUpdate(frameProfileBase);
		}
	}
		
	/**
	 * Notify listeners when {@code FrameProfileBase} object was removed.
	 * 
	 * @param frameProfileBase the removed {@code FrameProfileBase} object
	 */
	protected void onFrameRemove(FrameProfileBase frameProfileBase) {
		for (int i = 0; i < propertiesFrameObservers.size(); i++) {
			propertiesFrameObservers.get(i).onRemoveFrame(frameProfileBase);
		}
	}

	/**
	 * Notify listeners when a specific {@code FrameProfileBase} object needs to be put
	 * to front.
	 * 
	 * @param frameProfileBase the specific {@code FrameProfileBase} object
	 */
	protected void onMoveToFront(FrameProfileBase frameProfileBase) {
		for (int i = 0; i < propertiesFrameObservers.size(); i++) {
			propertiesFrameObservers.get(i).onMoveToFront(frameProfileBase);
		}
	}

	/**
	 * Notify listeners when an indicator was added or removed in specific
	 * {@code FrameProfileChart} object
	 * 
	 * @param frameProfileChart specific {@code FrameProfileChart} object
	 */
	protected void onUpdateIndicator(FrameProfileChart frameProfileChart) {
		for (int i = 0; i < propertiesFrameObservers.size(); i++) {
			propertiesFrameObservers.get(i).onUpdateIndicator(frameProfileChart);
		}
	}

	/**
	 * Notify listeners to update their {@code FrameProfile} object with the current
	 * frame data
	 */
	protected void onGetFrameSCREENSTATE() {
		for (int i = 0; i < propertiesFrameObservers.size(); i++) {
			propertiesFrameObservers.get(i).onGetFrameSCREENSTATE();
		}
	}

	/**
	 * Notify listeners when {@code DataProviderProfile} object has changed
	 * 
	 * @param dataProviderProfile the new {@code DataProviderProfile} object
	 */
	protected void onDataProviderChange(DataProviderProfile dataProviderProfile) {
		for (int i = 0; i < propertiesObservers.size(); i++) {
			propertiesObservers.get(i).onDataProviderChange(dataProviderProfile);
		}
	}
}
