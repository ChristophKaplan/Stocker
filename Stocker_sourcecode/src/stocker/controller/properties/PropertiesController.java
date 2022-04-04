package stocker.controller.properties;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import stocker.controller.general.ControllerBaseInternal;
import stocker.model.database.DatabaseModel;
import stocker.model.general.FrameProfile;
import stocker.model.general.FrameProfileBase;
import stocker.model.general.ViewType;
import stocker.model.properties.PropertiesModel;
import stocker.view.general.DesktopViewBase;
import stocker.view.general.InternalViewBase;
import stocker.view.properties.DialogDataProviderProfil;
import stocker.view.properties.PropertiesView;

/**
 * The Properties Controller class is responsible for receiving, generating and
 * processing properties view events and, if necessary, manipulating data in the
 * model.
 * 
 * @author Christoph Kaplan
 *
 */
public class PropertiesController extends ControllerBaseInternal implements ActionListener {

	private PropertiesView view;
	private DialogDataProviderProfil dialogDataProviderProfil;

	/**
	 * Constructor
	 * 
	 * @param desktopViewBase the desktop view
	 * @param propertiesModel the properties model
	 * @param databaseModel   the database model
	 */
	public PropertiesController(DesktopViewBase desktopViewBase, PropertiesModel propertiesModel, DatabaseModel databaseModel) {
		super(ViewType.Properties, desktopViewBase, propertiesModel, databaseModel);
		getDataProviderProfileViaDialogIfNull();
	}

	private void getDataProviderProfileViaDialogIfNull() {
		// If there is no data provider profile, open up a dialog
		if (getPropertiesModel().getCurrentDataProviderProfile() == null) {
			// System.out.println("need a profile");
			this.openDataProviderProfileDialog();
			getPropertiesModel().setCurrentDataProviderProfile(dialogDataProviderProfil.getDataProviderProfile());
			getPropertiesModel().addAvailableDataProviderProfile(dialogDataProviderProfil.getDataProviderProfile());
		}
	}
	
	/**
	 * Saves the data input
	 */
	public void saveButton() {
		view.saveViewData();
	}

	/**
	 * Resets the data from model
	 */
	public void resetButton() {
		view.loadViewData();
	}

	/**
	 * Removes the properties view.
	 */
	public void cancleButton() {
		getPropertiesModel().removeFrame(ViewType.Properties.ordinal());
		view.dispose();
	}

	/**
	 * Differentiates between {@code ActionEvent} occuring in the properties view,
	 * and invokes further methods accordingly.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (view != null && e.getSource() == view.getSaveButton())
			saveButton();
		if (view != null && e.getSource() == view.getResetButton())
			resetButton();
		if (view != null && e.getSource() == view.getCancleButton())
			cancleButton();

		if (view != null && e.getSource() == view.getDataPanel().getAddRowButton()) {
			openDataProviderProfileDialog();
		}
		if (view != null && e.getSource() == view.getDataPanel().getRemoveRowButton()) {
			view.getDataPanel().removeDataProviderProfile();
		}
		if (dialogDataProviderProfil != null && e.getSource() == dialogDataProviderProfil.getOkButton()) {
			if (view != null)
				view.getDataPanel().addDataProviderProfile(dialogDataProviderProfil.getDataProviderProfile());
			dialogDataProviderProfil.dispose();
		}
		if (dialogDataProviderProfil != null && e.getSource() == dialogDataProviderProfil.getCancelButton()) {
			dialogDataProviderProfil.dispose();
		}

	}

	/**
	 * Opens the data provider profile dialog
	 */
	public void openDataProviderProfileDialog() {
		dialogDataProviderProfil = new DialogDataProviderProfil(this.getDesktopViewBase());
		dialogDataProviderProfil.setUpListeners(this);
		dialogDataProviderProfil.setShow();
	}

	/**
	 * Creates and sets a new properties view
	 */
	@Override
	protected void createOwnView(FrameProfileBase frameProfileBase) {
		FrameProfile frameProfile = (FrameProfile) frameProfileBase;
		view = new PropertiesView(frameProfile, getDatabaseModel(), getPropertiesModel());

		view.setUpListener(this);
		view.loadViewData(); // nochmal wenns changes gibt oder unnötig ?
		addFrameToDesktop(view);

		view.setShow();
	}

	/**
	 * Gets the properties view
	 */
	@Override
	protected InternalViewBase getOwnView(FrameProfileBase frameProfileBase) {
		return view;
	}

	/**
	 * Updates the frame profile with the relevant frame data.
	 */
	@Override
	public void onGetFrameSCREENSTATE() {
		if (view == null)
			return;
		view.updateFrameProfile();
	}

}
