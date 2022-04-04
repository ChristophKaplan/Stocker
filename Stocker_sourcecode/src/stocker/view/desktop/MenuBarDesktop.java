package stocker.view.desktop;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import stocker.controller.general.ControllerBase;
import stocker.controller.general.ControllerBaseDesktop;


/**
 * Defines the desktop menu bar
 * 
 * @author Christoph Kaplan
 *
 */
public class MenuBarDesktop extends JMenuBar {

	private static final long serialVersionUID = -6662612911502657816L;

	protected JMenu stockerMenu = new JMenu("Stocker");
	protected JMenu toolsMenu = new JMenu("Tools");
	protected JMenu windowsMenu = new JMenu("Windows");

	private JMenuItem quitItem = new JMenuItem("Quit");
	private JMenuItem searchItem = new JMenuItem("Search");
	private JMenuItem propertiesItem = new JMenuItem("Properties");
	private JMenuItem watchlistItem = new JMenuItem("Watchlist");

	private Map<JMenuItem, JInternalFrame> openWindowItems = new HashMap<JMenuItem, JInternalFrame>();

	/**
	 * Constructor
	 */
	public MenuBarDesktop() {
		setUp();
	}

	/**
	 * Sets the components
	 */
	private void setUp() {
		stockerMenu.add(propertiesItem);
		stockerMenu.add(quitItem);
		toolsMenu.add(watchlistItem);
		toolsMenu.add(searchItem);
		add(stockerMenu);
		add(toolsMenu);
		add(windowsMenu);
	}

	/**
	 * Adds the listener
	 * 
	 * @param controller the listener
	 */
	void addListener(ControllerBaseDesktop controller) {
		quitItem.addActionListener(controller);
		searchItem.addActionListener(controller);
		propertiesItem.addActionListener(controller);
		watchlistItem.addActionListener(controller);
	}

	/**
	 * Updates the open window menu, with all open frames as {@code JMenuItem} and
	 * signs a listener. Invoked by {@code InternalViewBase} when frame is closed,
	 * or from a subclass of {@code ControllerBaseInternal} when own view is
	 * created/removed, over {@code ControllerBase} (and then over
	 * {@code DesktopView})
	 * 
	 * @param controller listener
	 * @param allFrames  all open frames
	 */
	void updateOpenWindowMenu(ControllerBase controller, JInternalFrame[] allFrames) {

		openWindowItems.clear();
		windowsMenu.removeAll();

		if (allFrames.length == 0) {
			windowsMenu.setEnabled(false);
			return;
		}

		windowsMenu.setEnabled(true);

		for (JInternalFrame frame : allFrames) {
			JMenuItem jmi = new JMenuItem(frame.getTitle());
			windowsMenu.add(jmi);

			ActionListener action = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					controller.onOpenWindowMenuClicked(frame);
				}
			};
			jmi.addActionListener(action);
			openWindowItems.put(jmi, frame);
		}
	}

	/**
	 * Gets the quit instance
	 * 
	 * @return the quit instance
	 */
	public JMenuItem getQuitItem() {
		return this.quitItem;
	}

	/**
	 * Gets the search instance
	 * 
	 * @return the search instance
	 */
	public JMenuItem getSearchItem() {
		return this.searchItem;
	}

	/**
	 * Gets the properties instance
	 * 
	 * @return the properties instance
	 */
	public JMenuItem getPropertiesItem() {
		return this.propertiesItem;
	}

	/**
	 * Gets the watchlist instance
	 * 
	 * @return the wachtlist instance
	 */
	public JMenuItem getWatchlistItem() {
		return this.watchlistItem;
	}

}