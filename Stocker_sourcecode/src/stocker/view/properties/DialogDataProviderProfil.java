package stocker.view.properties;

import java.awt.GridBagConstraints;
import javax.swing.JButton;
import javax.swing.JLabel;
import stocker.controller.properties.PropertiesController;
import stocker.model.general.DataProviderProfile;
import stocker.view.chart.DialogViewBase;
import stocker.view.general.DesktopViewBase;
import stocker.view.general.StockerTextField;
import stocker.view.general.StockerTextField.FilterMethod;

/**
 * Dialog to set a new {@code DataProviderProfil} object
 * 
 * @author Christoph Kaplan
 */
public class DialogDataProviderProfil extends DialogViewBase {
	private static final long serialVersionUID = 7981538063837236674L;

	private StockerTextField nameTextField;
	private StockerTextField apiKeyTextField;
	private StockerTextField pullURLTextField;
	private StockerTextField pushURLTextField;
	private JButton okButton;
	private JButton cancelButton;

	/**
	 * {@code DialogDataProviderProfil} constructor
	 * 
	 * @param mainWindow the owner
	 */
	public DialogDataProviderProfil(DesktopViewBase mainWindow) {
		super("new Data Provider", 300, 300, mainWindow);
		setUp();
	}

	/**
	 * Sets the dialog components
	 */
	private void setUp() {

		FilterMethod regularFilterMethod = new FilterMethod() {
			@Override
			public boolean isValid(String text) {
				//define a filter for the data provider profile input characters
				if (text.isBlank())
					return false;
				return true;
			}
		};

		JLabel nameLabel = new JLabel("Name");
		nameTextField = new StockerTextField("", 50, regularFilterMethod);

		JLabel apiKeyLabel = new JLabel("api Key");
		apiKeyTextField = new StockerTextField("", 50, regularFilterMethod);

		JLabel pullURLLabel = new JLabel("pull URL");
		pullURLTextField = new StockerTextField("https://", 50, regularFilterMethod);

		JLabel pushURLLabel = new JLabel("push URL");
		pushURLTextField = new StockerTextField("wss://", 50, regularFilterMethod);

		okButton = new JButton("OK");
		cancelButton = new JButton("Cancel");

		setToGridBag(nameLabel, 0, 0, 2, 1, 0.5f, 0.0f, GridBagConstraints.NONE);
		setToGridBag(nameTextField, 0, 1, 2, 1, 0.5f, 0.0f, GridBagConstraints.HORIZONTAL);
		setToGridBag(apiKeyLabel, 0, 2, 2, 1, 0.5f, 0.0f, GridBagConstraints.NONE);
		setToGridBag(apiKeyTextField, 0, 3, 2, 1, 0.5f, 0.0f, GridBagConstraints.HORIZONTAL);
		setToGridBag(pullURLLabel, 0, 4, 2, 1, 0.5f, 0.0f, GridBagConstraints.NONE);
		setToGridBag(pullURLTextField, 0, 5, 2, 1, 0.5f, 0.0f, GridBagConstraints.HORIZONTAL);
		setToGridBag(pushURLLabel, 0, 6, 2, 1, 0.5f, 0.0f, GridBagConstraints.NONE);
		setToGridBag(pushURLTextField, 0, 7, 2, 1, 0.5f, 0.0f, GridBagConstraints.HORIZONTAL);
		setToGridBag(okButton, 0, 8, 1, 1, 0.5f, 0.0f, GridBagConstraints.NONE);
		setToGridBag(cancelButton, 1, 8, 1, 1, 0.5f, 0.0f, GridBagConstraints.NONE);
	}

	/**
	 * Sets the listener for the buttons
	 * 
	 * @param propertiesController the listener
	 */
	public void setUpListeners(PropertiesController propertiesController) {
		okButton.addActionListener(propertiesController);
		cancelButton.addActionListener(propertiesController);
	}

	/**
	 * Gets a new {@code DataProviderProfile} object based on the users input.
	 * 
	 * @return the {@code DataProviderProfile} object
	 */
	public DataProviderProfile getDataProviderProfile() {
		return new DataProviderProfile(nameTextField.getText(), apiKeyTextField.getText(), pullURLTextField.getText(),
				pushURLTextField.getText());
	}

	/**
	 * Gets the ok button instance
	 * 
	 * @return the ok button instance
	 */
	public JButton getOkButton() {
		return this.okButton;
	}

	/**
	 * Gets the cancel button instance
	 * 
	 * @return the cancle button instance
	 */
	public JButton getCancelButton() {
		return this.cancelButton;
	}
}
