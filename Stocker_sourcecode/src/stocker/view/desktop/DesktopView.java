package stocker.view.desktop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import stocker.controller.general.ControllerBase;
import stocker.controller.general.ControllerBaseDesktop;
import stocker.model.general.FrameProfile;
import stocker.view.general.DesktopViewBase;

/**
 * Defines the desktop view
 * @author Christoph Kaplan
 *
 */
public class DesktopView extends DesktopViewBase {

	private static final long serialVersionUID = -5754927940273810336L;
	
	private JLabel statusInfo;
	private JDesktopPane desktop;
	private MenuBarDesktop mainMenuBar;

	/**
	 * Constructor
	 * @param frameProfile the frame profile
	 */
	public DesktopView(FrameProfile frameProfile) {
		super(frameProfile);
		setUp();
		setShow();
	}

	/**
	 * gets the {@code JDesktopPane} object.
	 */
	public JDesktopPane getDeskPane() {
		return desktop;
	};

	/**
	 * gets the menu bar instance
	 */
	public MenuBarDesktop getMainMenuBar() {
		return mainMenuBar;
	};

	/**
	 * Sets the components and layout.
	 */
	public void setUp() {
		mainMenuBar = new MenuBarDesktop();
		setJMenuBar(mainMenuBar);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		desktop = new JDesktopPane();
		desktop.setBackground(new Color(15, 65, 83,255));
		
		JPanel statusBarPanel = new JPanel();
		statusInfo = new JLabel("# boot #");
		statusBarPanel.add(statusInfo);
		
		add(desktop);
		add(statusBarPanel, BorderLayout.SOUTH);
		
		setVisible(true);
	}

	/**
	 * Sets the status text in the status bar.
	 */
	public void setStatus(String status) {
		statusInfo.setText(status);
	}

	/**
	 * Adds listener
	 */
	@Override
	public void addListener(ControllerBase controller) {
		// muss eigentlich ein DesktopController einfügen
		mainMenuBar.addListener((ControllerBaseDesktop) controller);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				((ControllerBaseDesktop) controller).onCloseApplication();
			}
		});

	}

	/**
	 * Updates the open window menu and sets listener
	 */
	@Override
	public void updateOpenWindowMenu(ControllerBase controller) {
		mainMenuBar.updateOpenWindowMenu(controller, desktop.getAllFrames());
	}

}