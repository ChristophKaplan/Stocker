package stocker.view.properties;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Base class for the properties tab panels.
 * @author Christoph Kaplan
 *
 */
public abstract class PropertiesTabPanelBase extends JPanel {

	private static final long serialVersionUID = 3709802720210363474L;
		
	private GridBagLayout gbl = new GridBagLayout();
	private GridBagConstraints gbc = new GridBagConstraints();
	private int sizeX;
	private int sizeY;

	
	/**
	 * {@code PropertiesTabPanelBase} constructor
	 * @param sizeX the size x
	 * @param sizeY the size y
	 */
	public PropertiesTabPanelBase(int sizeX, int sizeY) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		setUp();
	}

	/**
	 * Sets the components and layout
	 */
	void setUp() {
		Dimension dim = new Dimension(sizeX, sizeY);
		setPreferredSize(dim);
		setMinimumSize(dim);
		setSize(dim);

		gbl.location(0,0);
		setLayout(gbl);
		gbc.anchor =  GridBagConstraints.NORTHWEST;
		int ins = 5;
		gbc.insets = new Insets(ins, ins, ins, ins);
		
	}
	/**
	 * Sets the gridbag layout values for a component
	 * @param component the component
	 * @param x the gridx
	 * @param y the gridy
	 * @param w the gridwidth
	 * @param h the gridheight
	 * @param wx the weightx
	 * @param wy the weighty
	 * @param f the fill
	 */
	protected void setToGridBag(JComponent component, int x, int y, int w, int h,float wx, float wy,int f) {
		gbc.fill = f;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridheight = h;
		gbc.gridwidth = w;
		gbc.weightx = wx;
		gbc.weighty = wy;
		gbl.setConstraints(component, gbc);
		add(component);
	}
}