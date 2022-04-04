package stocker.view.properties;

import java.awt.Dimension;
import java.awt.GridBagConstraints;

import javax.swing.JLabel;
import javax.swing.JTextField;

import stocker.view.general.StockerTextField;
import stocker.view.general.StockerTextField.FilterMethod;

/**
 * The General Tab allows the user to change certain settings regarding frame size and the like.
 * @author Christoph Kaplan
 *
 */
public class PropertiesTabPanelGeneral extends PropertiesTabPanelBase {
	private static final long serialVersionUID = 1L;

	private StockerTextField frameMinXSizeTextField;
	private StockerTextField frameMinYSizeTextField;
	private StockerTextField frameChartMinXSizeTextField;
	private StockerTextField frameChartMinYSizeTextField;

	
	/**
	 * {@code PropertiesTabPanelGeneral} constructor
	 * @param sizeX the size x
	 * @param sizeY the size y
	 */
	public PropertiesTabPanelGeneral(int sizeX, int sizeY) {
		super(sizeX, sizeY);
	}
	
	/**
	 * Sets the components and layout
	 */
	void setUp() {
		super.setUp();
		
		FilterMethod integerFilterMethod = new FilterMethod() {
			@Override
			public boolean isValid(String text) {
				try {
					Integer.parseInt(text);
					return true;
				} catch (Exception e) {
					return false;
				}
			}
		};
		
		frameMinXSizeTextField = new StockerTextField("",10,integerFilterMethod);
		frameMinYSizeTextField = new StockerTextField("",10,integerFilterMethod);
		frameChartMinXSizeTextField = new StockerTextField("",10,integerFilterMethod);
		frameChartMinYSizeTextField = new StockerTextField("",10,integerFilterMethod);

		Dimension d = new Dimension(50, 20);
		frameMinXSizeTextField.setPreferredSize(d);
		frameMinYSizeTextField.setPreferredSize(d);
		frameChartMinXSizeTextField.setPreferredSize(d);
		frameChartMinYSizeTextField.setPreferredSize(d);

		JLabel headlineLabel = new JLabel("Standard minimal window size");
		JLabel widthLabel = new JLabel("width:");
		JLabel heigthLabel = new JLabel("height:");
		JLabel headlineChartLabel = new JLabel("Standard minimal chartwindow size");
		JLabel widthChartLabel = new JLabel("width:");
		JLabel heigthChartLabel = new JLabel("height:");

		setToGridBag(headlineLabel, 0, 0, 3, 1, 0.5f, 0.0f, GridBagConstraints.NONE);

		setToGridBag(widthLabel, 0, 1, 1, 1, 0.0f, 0.0f, GridBagConstraints.NONE);
		setToGridBag(frameMinXSizeTextField, 1, 1, 1, 1, 0.0f, 0.0f, GridBagConstraints.NONE);

		setToGridBag(heigthLabel, 0, 2, 1, 1, 0.0f, 0.0f, GridBagConstraints.NONE);
		setToGridBag(frameMinYSizeTextField, 1, 2, 1, 1, 0.0f, 0.0f, GridBagConstraints.NONE);

		setToGridBag(headlineChartLabel, 0, 3, 3, 1, 0.5f, 0.0f, GridBagConstraints.NONE);

		setToGridBag(widthChartLabel, 0, 4, 1, 1, 0.0f, 0.0f, GridBagConstraints.NONE);
		setToGridBag(frameChartMinXSizeTextField, 1, 4, 1, 1, 0.0f, 0.0f, GridBagConstraints.NONE);

		setToGridBag(heigthChartLabel, 0, 5, 1, 1, 0.0f, 1.0f, GridBagConstraints.NONE);
		setToGridBag(frameChartMinYSizeTextField, 1, 5, 1, 1, 0.0f, 0.0f, GridBagConstraints.NONE);

	}

	/**
	 * Gets the minimum size x textfield instance.
	 * @return  the minimum size x textfield instance
	 */
	public JTextField getFrameMinXSizeTextField() {
		return this.frameMinXSizeTextField;
	}

	/**
	 * Gets the minimum size y textfield instance.
	 * @return  the minimum size y textfield instance
	 */
	public JTextField getFrameMinYSizeTextField() {
		return this.frameMinYSizeTextField;
	}

	/**
	 * Gets the minimum chart size x textfield instance
	 * @return  the minimum chart size x textfield instance
	 */
	public JTextField getFrameChartMinXSizeTextField() {
		return this.frameChartMinXSizeTextField;
	}

	/**
	 * Gets the minimum chart size y textfield instance
	 * @return  the minimum chart size y textfield instance
	 */
	public JTextField getFrameChartMinYSizeTextField() {
		return this.frameChartMinYSizeTextField;
	}
}