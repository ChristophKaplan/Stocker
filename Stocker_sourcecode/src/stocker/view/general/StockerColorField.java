package stocker.view.general;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JColorChooser;
import javax.swing.JLabel;

/**
 * Defines a label with the function to choose a color.
 * @author Christoph Kaplan
 *
 */
public class StockerColorField extends JLabel{

	private static final long serialVersionUID = -6115928451844485625L;
	
	private Color color;
	
	/**
	 * Constructor, sets the component and listener
	 */
	public StockerColorField(){
		this.setText("choose color");
		Dimension dim = new Dimension(90,30);
		this.setMinimumSize(dim);
		this.setPreferredSize(dim);
		this.setMaximumSize(dim);

		addMouseListener(new MouseAdapter()
	    {
	        public void mouseClicked(MouseEvent e)
	        {
	        	choose();
	        }
	    });
	}
	
	/**
	 * Opens a color chooser dialog, invoked on mouse clicking event.
	 */
	private void choose() {
    	color = JColorChooser.showDialog(this,"Choose Color", this.color);
    	setColor(color);
	}
	
	/**
	 * Sets a color.
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
		this.setBackground(color);
		this.setOpaque(true);
	}
	
	/**
	 * Gets a color.
	 * @return the color
	 */
	public Color getColor() {
		return this.color;
	}
}