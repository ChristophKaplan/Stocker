package stocker.controller.general;

import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameEvent;

import stocker.model.general.FrameProfileBase;
import stocker.model.general.FrameProfileChart;
import stocker.model.general.ViewType;
import stocker.model.properties.PropertiesFrameListener;
import stocker.model.properties.PropertiesModel;
import stocker.view.general.DesktopViewBase;
import stocker.view.general.InternalViewBase;

/**
 * This class forms the foundation of the two classes {@code ControllerBaseDesktop} and
 * {@code ControllerBaseInternal}. It implements the {@code PropertiesFrameListener}
 * 
 * @author Christoph Kaplan
 *
 */
public abstract class ControllerBase implements PropertiesFrameListener {
	private final ViewType viewType;
	private PropertiesModel propertiesModel;
	private DesktopViewBase desktopViewBase;

	protected ControllerBase(ViewType viewType, DesktopViewBase desktopViewBase, PropertiesModel propertiesModel) {
		this.viewType = viewType;
		this.desktopViewBase = desktopViewBase;
		this.propertiesModel = propertiesModel;
		this.propertiesModel.addFrameObserver(this);
	}

	/**
	 * Gets the {@code DesktopViewBase} object.
	 * 
	 * @return the {@code DesktopViewBase} object
	 */
	public DesktopViewBase getDesktopViewBase() {
		return desktopViewBase;
	}

	/**
	 * Gets the {@code ViewType} object.
	 * 
	 * @return the {@code ViewType} object
	 */
	public ViewType getViewType() {
		return this.viewType;
	}

	/**
	 * Gets the {@code PropertiesModel} object.
	 * 
	 * @return the {@code PropertiesModel} object
	 */
	public PropertiesModel getPropertiesModel() {
		return this.propertiesModel;
	}

	/**
	 * Adds a frame to the {@code JDesktopPane} of {@code DesktopViewBase}.
	 * 
	 * @param frame the frame to add
	 */
	public void addFrameToDesktop(InternalViewBase frame) {
		// System.out.println("addFrameToDesktop:" + frame);
		desktopViewBase.getDeskPane().add(frame);
		onUpdateDesktopMenu();
	}

	/**
	 * Removes a frame from the {@code JDesktopPane} of {@code DesktopViewBase}.
	 * 
	 * @param frame the frame to remove
	 */
	public void removeFrameFromDesktop(InternalViewBase frame) {
		desktopViewBase.getDeskPane().remove(frame);
		onUpdateDesktopMenu();
	}

	/**
	 * Invoked by a {@code InternalViewBase} object when frame closing event
	 * occurred.
	 * 
	 * @param e {@code InternalFrameEvent} event object
	 */
	public void onInternalFrameClosed(InternalFrameEvent e) {
		//System.out.println("internalFrameClosed " + e.getSource());
		InternalViewBase internalView = (InternalViewBase) e.getSource();
		internalView.updateFrameProfile();
		propertiesModel.removeFrame(internalView.getID());
		onUpdateDesktopMenu();
	}

	/**
	 * Updates the open window menu
	 * {@link stocker.view.general.DesktopViewBase#updateOpenWindowMenu}
	 */
	public void onUpdateDesktopMenu() {
		desktopViewBase.updateOpenWindowMenu(this);
	}

	/**
	 * Brings given {@code JInternalFrame} object to front visually, when event
	 * occurred. Invoked by a {@code MenuBarDesktop} object.
	 * 
	 * @param frame the {@code JInternalFrame} object
	 */
	public void onOpenWindowMenuClicked(JInternalFrame frame) {
		frame.moveToFront();
	}

	// Listeners
	@Override
	public void onAddFrame(FrameProfileBase frameProfileBase) {

	}

	@Override
	public void onRemoveFrame(FrameProfileBase frameProfileBase) {

	}

	@Override
	public void onMoveToFront(FrameProfileBase frameProfileBase) {

	}

	@Override
	public void onUpdateIndicator(FrameProfileChart frameProfile_Chart) {

	}

}