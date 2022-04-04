package stocker.view.general;


import java.awt.Dimension;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;

import stocker.controller.general.ControllerBase;
import stocker.model.general.FrameProfile;
import stocker.view.desktop.MenuBarDesktop;

/**
 * Basic layout class for the desktop frame.
 * Part of the MVC View.
 * @author Christoph Kaplan
 */
public abstract class DesktopViewBase extends JFrame implements ViewBase{
	private static final long serialVersionUID = 7949445236342422080L;
	
	private FrameProfile frameProfile_SCREENSTATE; // SCREENSTATE
		
	/**
	 * Constructor
	 * @param frameProfile the associated frame profile
	 */
	public DesktopViewBase(FrameProfile frameProfile) {
		super(frameProfile.getName());
		this.frameProfile_SCREENSTATE = frameProfile;
		setFrameProfile(frameProfile_SCREENSTATE);
	}
	
	/**
	 * Gets the frame profile
	 */
	public FrameProfile getFrameProfile() {
		return frameProfile_SCREENSTATE;
	}
	
	/**
	 * Gets the id of the frame profile
	 */
	public int getID() {
		return this.frameProfile_SCREENSTATE.getID();
	}


	/**
	 * Sets the status, shown in the status bar.
	 * @param status the status
	 */
	public abstract void setStatus(String status);
	
	/**
	 * Gets the {@code JDesktopPane} object.
	 * @return the {@code JDesktopPane} object
	 */
	public abstract JDesktopPane getDeskPane();
	
	/**
	 * Gets the menu bar instance.
	 * @return the menu bar instance
	 */
	public abstract MenuBarDesktop getMainMenuBar();
	
	/**
	 * Updates the the menu items in the open window menu
	 * @param controller listener
	 */
	public abstract void updateOpenWindowMenu(ControllerBase controller);
	
	/**
	 * Sets the frame profile values to this frame.
	 */
	public void setFrameProfile(FrameProfile frameProfile) {
		Dimension minDimension = new Dimension(frameProfile.getMinSizeX(),frameProfile.getMinSizeY());
		setMinimumSize(minDimension);
		
		Dimension currentDimension = new Dimension(frameProfile.getSizeX(),frameProfile.getSizeY());
		setPreferredSize(currentDimension);
		setSize(currentDimension);
		
		setLocation(frameProfile.getPosX(), frameProfile.getPosY());
	}
	
	
	/**
	 * Updates the frame profile by the current size and location
	 */
	public void updateFrameProfile(){
		frameProfile_SCREENSTATE.setSizeX(this.getSize().width);
		frameProfile_SCREENSTATE.setSizeY(this.getSize().height);
		frameProfile_SCREENSTATE.setPosX(this.getLocation().x);
		frameProfile_SCREENSTATE.setPosY(this.getLocation().y);
	}
	
	/**
	 * Sets visible
	 */
	public void setShow() {
		this.setVisible(true);
	}

	/**
	 * Gets the middle position/location of this desktop frame
	 * @return the middle location
	 */
	public Dimension getMid() {
		int x = this.getLocation().x + (this.getSize().width/2);
		int y = this.getLocation().y + (this.getSize().height/2);
		return new Dimension(x,y);
	}

}