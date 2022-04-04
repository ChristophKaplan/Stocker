package stocker.model.properties;


import stocker.model.general.FrameProfileBase;
import stocker.model.general.FrameProfileChart;


/**
 * Listens to changes in regards to frame profiles or indicators.
 * @author Christoph Kaplan
 *
 */
public interface PropertiesFrameListener {
	
	/**
	 * Notify listeners when new {@code FrameProfileBase} object is added.
	 * 
	 * @param frameProfileBase the added {@code FrameProfileBase} object
	 */
	public void onAddFrame(FrameProfileBase frameProfileBase);

	/**
	 * Notify listeners when new {@code FrameProfileBase} object is updated.
	 * 
	 * @param frameProfileBase the updated {@code FrameProfileBase} object
	 */
	public void onFrameUpdate(FrameProfileBase frameProfileBase);
		
	/**
	 * Notify listeners when {@code FrameProfileBase} object was removed.
	 * 
	 * @param frameProfileBase the removed {@code FrameProfileBase} object
	 */
	public void onRemoveFrame(FrameProfileBase frameProfileBase);
	
	/**
	 * Notify listeners when a specific {@code FrameProfileBase} object needs to be put
	 * to front.
	 * 
	 * @param frameProfileBase the specific {@code FrameProfileBase} object
	 */
	public void onMoveToFront(FrameProfileBase frameProfileBase);
	
	/**
	 * Notify listeners when an indicator was added or removed in specific
	 * {@code FrameProfileChart} object
	 * 
	 * @param frameProfileChart specific {@code FrameProfileChart} object
	 */
	public void onUpdateIndicator(FrameProfileChart frameProfileChart);
	
	/**
	 * Notify listeners to update their {@code FrameProfile} object with the current
	 * frame data
	 */
	public void onGetFrameSCREENSTATE();
}
