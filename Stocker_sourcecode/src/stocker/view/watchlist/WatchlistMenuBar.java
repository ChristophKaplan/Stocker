package stocker.view.watchlist;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import stocker.controller.watchlist.WatchlistController;

/**
 * Defines the watchlist menu bar.
 * @author Christoph Kaplan
 *
 */
public class WatchlistMenuBar extends JMenuBar {
	private static final long serialVersionUID = 7379262383457325849L;

	private JMenu newMenu;
	private JMenuItem openSearch;

	private JMenu selection;
	private JMenuItem watchlistRemoveChecked;
	private JMenuItem watchlistShowChecked;

	/**
	 * Constructor
	 */
	public WatchlistMenuBar() {
		setUp();
	}

	/**
	 * Sets components.
	 */
	private void setUp() {
		newMenu = new JMenu("New");
		openSearch = new JMenuItem("Open search");

		selection = new JMenu("Selection");
		watchlistRemoveChecked = new JMenuItem("Remove selection");
		watchlistShowChecked = new JMenuItem("Show selection");

		newMenu.add(openSearch);
		add(newMenu);

		selection.add(watchlistShowChecked);
		selection.add(watchlistRemoveChecked);

		add(selection);
	}

	/**
	 * Adds listener.
	 * @param controller the listener
	 */
	void addListener(WatchlistController controller) {
		openSearch.addActionListener(controller);
		watchlistRemoveChecked.addActionListener(controller);
		watchlistShowChecked.addActionListener(controller);
	}

	
	
	/**
	 * Gets the open search instance.
	 * @return the open search instance
	 */
	public JMenuItem getOpenSearch() {
		return this.openSearch;
	}

	/**
	 * Gets the watchlist remove checked instance
	 * @return the watchlist remove checked instance
	 */
	
	public JMenuItem getWatchlistRemoveChecked() {
		return this.watchlistRemoveChecked;
	}

	/**
	 * Gets the watchlist show checked instance
	 * @return the watchlist show checked instance
	 */
	public JMenuItem getWatchlistShowChecked() {
		return this.watchlistShowChecked;
	}
}