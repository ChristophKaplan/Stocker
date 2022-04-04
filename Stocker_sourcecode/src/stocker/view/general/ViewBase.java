package stocker.view.general;

import stocker.controller.general.ControllerBase;
import stocker.model.general.FrameProfile;

/**
 * interface for all "MVC-View" frames/windows.
 * 
 * @author Christoph Kaplan
 */
public interface ViewBase {
	/**
	 * Getter for the FrameProflie instance.
	 * 
	 * @return returns the FrameProfile instance.
	 */
	FrameProfile getFrameProfile();

	/**
	 * Getter for the frame profile ID
	 * 
	 * @return returns the ID of the frame profile.
	 */
	int getID();

	/**
	 * Sets up the swing components for this frame.
	 */
	void setUp();

	/**
	 * Sets the information of the given frame profile (i.e. size, location) to the
	 * swing components (i.e. JInternalFrame).
	 * 
	 * @param frameProfile instance with the frame information.
	 */
	void setFrameProfile(FrameProfile frameProfile);

	/**
	 * Sets the current window information to the frame profile.
	 */
	void updateFrameProfile();

	/**
	 * Signs the individual listeners with the given controller.
	 * 
	 * @param controller that should be called via listeners.
	 */
	void addListener(ControllerBase controller);

	/**
	 * Triggers the swing methods to show this window.
	 */
	void setShow();
}