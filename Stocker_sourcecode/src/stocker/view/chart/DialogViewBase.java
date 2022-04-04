package stocker.view.chart;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JDialog;

import stocker.view.general.DesktopViewBase;

/**
 * Base class for all dialog frames
 * @author Christoph Kaplan
 *
 */
public abstract class DialogViewBase extends JDialog {
	private static final long serialVersionUID = -6900001594170762384L;
	
	private GridBagLayout gbl = new GridBagLayout();
	private GridBagConstraints gbc = new GridBagConstraints();
	
	/**
	 * {@code DialogViewBase} constructor
	 * @param title the title
	 * @param sizeX the size X
	 * @param sizeY the size Y
	 * @param mainWindow the owner of type {code@ DesktopViewBase}
	 */
	public DialogViewBase(String title,int sizeX,int sizeY,DesktopViewBase mainWindow) {
		super(mainWindow,title);
		
		// GridBag Settings
		setLayout(gbl);
		gbc.anchor =  GridBagConstraints.NORTHWEST;
		int ins = 5;
		gbc.insets = new Insets(ins, ins, ins, ins);
		
		Dimension d = new Dimension(sizeX,sizeY);
		setMinimumSize(d);
		setPreferredSize(d);
		
		int x =mainWindow.getMid().width - (sizeX/2);
		int y =mainWindow.getMid().height - (sizeY/2);
		setLocation(x,y);		

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
		
	/**
	 * Sets the dialog modal and visible
	 */
	public void setShow() {
		setModal(true);
		setVisible(true);
	}
	

}
