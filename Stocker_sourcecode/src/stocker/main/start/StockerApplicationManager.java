package stocker.main.start;



import javax.swing.UIManager;

import stocker.controller.database.DatabaseController;
import stocker.controller.desktop.DesktopController;
import stocker.controller.inputoutput.PersistenceDataWrapper;
import stocker.model.database.DatabaseModel;
import stocker.model.properties.PropertiesModel;

/**
 * Stocker application manager, is primarily responsible for starting, constructing and terminating the application.
 * 
 * @author Christoph Kaplan
 */

public class StockerApplicationManager {
	private PropertiesModel propertiesModel;
	private DatabaseModel databaseModel;

	/**
	 * {@code StockerApplicationManager} constructor
	 */
	public StockerApplicationManager() {
		setUILookAndFeel();
	}

	/**
	 * Sets a cross platform "look and feel"
	 */
	private void setUILookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println("setUILookAndFeel(): "+e.getMessage());
			//e.printStackTrace();
		}
	}

	/**
	 * Starts the stocker application, loads persistent data, sets models and controllers
	 */
	public void startApplication() {
		PersistenceDataWrapper saveDataWrapper = PersistenceDataWrapper.load();

		//set models
		propertiesModel = saveDataWrapper.getPropertiesModel();
		databaseModel = saveDataWrapper.getDatabaseModel();
		
		//set controllers
		DatabaseController databaseController = new DatabaseController(propertiesModel, databaseModel);
		DesktopController.create(propertiesModel, databaseModel, this);
				
		//open all loaded frames
		propertiesModel.notifyAllLoadedFramesOnFrameAdd();	
		
		//boot database
		databaseController.bootDatabase();
	}

	/**
	 * Ends the stocker application Saves persistent data
	 */
	public void endApplication() {
		//update frame profiles
		propertiesModel.getFrameSCREENSTATE();
		
		//persist data
		PersistenceDataWrapper.save(propertiesModel, databaseModel);
		
		//end application
		System.exit(0);
	}

}
