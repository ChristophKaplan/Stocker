package stocker.view.chart;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

import stocker.model.general.IndicatorBase;
import stocker.model.general.IndicatorType;
import stocker.controller.chart.ChartController;
import stocker.model.general.BollingerBands;
import stocker.model.general.SimpleMovingAverage;
import stocker.view.general.DesktopViewBase;
import stocker.view.general.StockerColorField;
import stocker.view.general.StockerTextField;
import stocker.view.general.StockerTextField.FilterMethod;

/**
 * The indicator parameter dialog, to get user input for the indicator dialog.
 * 
 * @author Christoph Kaplan
 *
 */
public class DialogIndicatorParameter extends DialogViewBase {

	private static final long serialVersionUID = -5701579014284516093L;

	private JButton okButton;
	private JButton cancelButton;
	private StockerTextField indicatorFValueText;
	private StockerTextField indicatorNValueText;

	private StockerColorField colorField;
	private IndicatorType indicatorType;
	private Color indicatorColor;

	/**
	 * {@code DialogIndicatorParameter} constructor
	 * 
	 * @param mainWindow    the owner
	 * @param indicatorType the indicator type
	 * @param standardColor the standard color
	 */
	public DialogIndicatorParameter(DesktopViewBase mainWindow, IndicatorType indicatorType, Color standardColor) {
		super(indicatorType + " Setup", 300, 300, mainWindow);
		this.indicatorType = indicatorType;
		this.indicatorColor = standardColor;
		setUp();
	}

	/**
	 * Sets the components
	 */
	private void setUp() {
		// JPanel inputPanel = new JPanel();
		// JPanel buttonPanel = new JPanel();

		FilterMethod doubleFilterMethod = new FilterMethod() {
			@Override
			public boolean isValid(String text) {
				try {
					Double.parseDouble(text);
					return true;
				} catch (Exception e) {
					return false;
				}
			}
		};
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
		
		
		okButton = new JButton("OK");
		cancelButton = new JButton("Cancle");
				
		

		
		
		indicatorFValueText = new StockerTextField("2.0",5,doubleFilterMethod);
		indicatorNValueText = new StockerTextField("20",3,integerFilterMethod);
		colorField = new StockerColorField();

		colorField.setColor(indicatorColor);

		JLabel sizeLabel = new JLabel("size:");		
		setToGridBag(sizeLabel, 		  0, 0, 1, 1, 0.5f, 0.0f, GridBagConstraints.HORIZONTAL);
		setToGridBag(indicatorNValueText, 0, 1, 1, 1, 0.5f, 0.0f, GridBagConstraints.HORIZONTAL);

		JLabel colorLabel = new JLabel("color:");	
		setToGridBag(colorLabel, 1, 0, 1, 1, 0.5f, 0.0f, GridBagConstraints.NONE);
		setToGridBag(colorField, 1, 1, 1, 1, 0.5f, 0.0f, GridBagConstraints.NONE);

		if (this.indicatorType == IndicatorType.BollingerBands) {
			JLabel widthLabel = new JLabel("width:");
			
			setToGridBag(widthLabel,		  0, 2, 1, 1, 0.5f, 0.0f, GridBagConstraints.HORIZONTAL);			
			setToGridBag(indicatorFValueText, 0, 3, 1, 1, 0.5f, 0.0f, GridBagConstraints.HORIZONTAL);
		}


		setToGridBag(okButton, 0, 4, 1, 1, 0.0f, 0.0f, GridBagConstraints.HORIZONTAL);
		setToGridBag(cancelButton, 1, 4, 1, 1, 0.0f, 1.0f, GridBagConstraints.HORIZONTAL);
	}

	/**
	 * Sets the listener
	 * 
	 * @param chartController the listener
	 */
	public void setUpListeners(ChartController chartController) {
		okButton.addActionListener(getActionlistener(chartController));
		cancelButton.addActionListener(getActionlistener(chartController));
	}

	/**
	 * Creates an action listener
	 * 
	 * @param chartController the listener
	 * @return the action listener
	 */
	private ActionListener getActionlistener(ChartController chartController) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chartController.dialogIndicatorParameterActionPerformed(e);
			}
		};
	}

	/**
	 * Gets a new {@code IndicatorBase} object based on the user input
	 * 
	 * @return the {@code IndicatorBase} object
	 */
	public IndicatorBase getIndicator() {
		this.indicatorColor = colorField.getColor();
		if (getIndicatorType() == IndicatorType.BollingerBands) {
			return new BollingerBands(IndicatorType.BollingerBands, getFValue(), getNValue(), getNValue(), getColor());
		}
		if (getIndicatorType() == IndicatorType.SimpleMovingAverage) {
			return new SimpleMovingAverage(IndicatorType.SimpleMovingAverage, getNValue(), getColor());
		}
		return null;
	}

	/**
	 * Gets the {@code IndicatorType} value input.
	 * 
	 * @return the {@code IndicatorType} value
	 */
	private IndicatorType getIndicatorType() {
		return this.indicatorType;
	}

	/**
	 * Gets the color input.
	 * 
	 * @return the color
	 */
	private Color getColor() {
		return this.indicatorColor;
	}

	/**
	 * Gets the f value input.
	 * 
	 * @return the f value
	 */
	private double getFValue() {
		String s = indicatorFValueText.getText();
		try {
			return Double.parseDouble(s);
		} catch (Exception e) {
			System.err.println("getFValue():"+e.getMessage());
			return 0;
		}

	}

	/**
	 * Gets the n value input.
	 * 
	 * @return the n value
	 */
	private int getNValue() {
		String s = indicatorNValueText.getText();	
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
			System.err.println("getNValue():"+e.getMessage());
			return 0;
		}
	}

	/**
	 * Gets the ok button instance.
	 * 
	 * @return the ok button instance
	 */
	public JButton getOkButton() {
		return this.okButton;
	}

	/**
	 * Gets the cancle button instance.
	 * 
	 * @return the cancle button instance
	 */
	public JButton getCancelButton() {
		return this.cancelButton;
	}

}
