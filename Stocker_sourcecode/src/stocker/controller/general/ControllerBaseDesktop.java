package stocker.controller.general;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import stocker.model.general.ViewType;
import stocker.model.properties.PropertiesModel;
import stocker.view.general.DesktopViewBase;

/**
 * This class sets up the base for the {@code DesktopController} class.
 * @author Christoph Kaplan
 *
 */
public abstract class ControllerBaseDesktop extends ControllerBase implements ActionListener {

	/**
	 * {@code ControllerBaseDesktop} constructor
	 * @param viewType desktop view type
	 * @param desktopViewBase the desktop view
	 * @param propertiesModel the properties model
	 */
	protected ControllerBaseDesktop(ViewType viewType,DesktopViewBase desktopViewBase, PropertiesModel propertiesModel) {
		super(viewType,desktopViewBase, propertiesModel);
	}

	
	//public abstract void onOpenSelf();

	/**
	 * Opens watchlist view.
	 */
	public abstract void onOpenWatchlist();
	
	/**
	 * Opens search view.
	 */
	public abstract void onOpenSearch();
	
	/**
	 * Opens properties view
	 */
	public abstract void onOpenProperties();
	
	/**
	 * Invokes a method that closes the application
	 */
	public abstract void onCloseApplication();
		
	/**
	 * Differentiates between {@code ActionEvent} occuring in the menubar, and invokes further methods accordingly.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getDesktopViewBase().getMainMenuBar().getQuitItem()) {
			onCloseApplication();
		} else if (e.getSource() == getDesktopViewBase().getMainMenuBar().getSearchItem()) {
			onOpenSearch();
		} else if (e.getSource() == getDesktopViewBase().getMainMenuBar().getPropertiesItem()) {
			onOpenProperties();
		} else if (e.getSource() == getDesktopViewBase().getMainMenuBar().getWatchlistItem()) {
			onOpenWatchlist();
		}
		
	}


}
