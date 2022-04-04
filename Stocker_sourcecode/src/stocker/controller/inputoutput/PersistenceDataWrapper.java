package stocker.controller.inputoutput;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.google.gson.annotations.SerializedName;

import stocker.model.database.AlarmWrapper;
import stocker.model.database.DatabaseModel;
import stocker.model.general.ChartType;
import stocker.model.general.DataProviderProfile;
import stocker.model.general.DisplayType;
import stocker.model.general.FrameProfileBase;
import stocker.model.general.ViewType;
import stocker.model.general.TimeInterval;
import stocker.model.properties.PropertiesModel;

/**
 * The PersistenceDataWrapper class is responsible for persisting and restoring
 * the relevant model data.
 * 
 * @author Christoph Kaplan
 */

public class PersistenceDataWrapper extends Serializer {

	private transient final String filename = "stocker_3285766.json";
	private transient boolean persistentFileExists = false;

	// PROPERTIES to store
	@SerializedName("userName")
	private String userName;
	@SerializedName("userEmail")
	private String userEmail;
	@SerializedName("userMatrNr")
	private String userMatrNr;

	@SerializedName("currentDataProviderProfile")
	private DataProviderProfile currentDataProviderProfile;
	@SerializedName("dataProviderProfiles")
	private ArrayList<DataProviderProfile> dataProviderProfiles = new ArrayList<DataProviderProfile>();

	@SerializedName("minFrameSizeX")
	private int minFrameSizeX;

	@SerializedName("minFrameSizeY")
	private int minFrameSizeY;

	@SerializedName("minFrameChartSizeX")
	private int minFrameChartSizeX;

	@SerializedName("minFrameChartSizeY")
	private int minFrameChartSizeY;

	@SerializedName("timeFrom")
	private long timeFrom;
	@SerializedName("timeTo")
	private long timeTo;

	@SerializedName("chartTyp")
	private ChartType chartTyp;
	@SerializedName("interval")
	private TimeInterval interval;

	@SerializedName("alarmColor")
	private Color alarmColor;
	@SerializedName("indicatorColorGD")
	private Color indicatorColorGD;
	@SerializedName("indicatorColorBB")
	private Color indicatorColorBB;


	
	// DB
	@SerializedName("watchlistSymbols")
	private ArrayList<String> watchlistSymbols = new ArrayList<String>();
	@SerializedName("chartOnlySymbols")
	private ArrayList<String> chartOnlySymbols = new ArrayList<String>();
	@SerializedName("alarms")
	private ArrayList<AlarmWrapper> alarmList = new ArrayList<AlarmWrapper>();

	// UI
	@SerializedName("frameProfiles")
	private ArrayList<FrameProfileBase> frameProfiles = new ArrayList<FrameProfileBase>();
	@SerializedName("chartCount")
	private int chartCount;

	/**
	 * Factory method that creates a new {@code PersistenceDataWrapper} object.
	 * 
	 * @return the new {@code PersistenceDataWrapper} object
	 */
	public static PersistenceDataWrapper load() {
		PersistenceDataWrapper persitentData = new PersistenceDataWrapper();
		try {
			// load the saved data
			persitentData.loadSaveDataWrapper();
			System.out.println("file loaded");
			persitentData.setPersistentFileExists(true);
		} catch (Exception e) {
			System.out.println("new file " + e.getMessage());
			persitentData.setPersistentFileExists(false);
		}
		return persitentData;
	}

	/**
	 * Static method that stores the database and the properties model data, and
	 * persist it.
	 * 
	 * @param propertiesModel the database model
	 * @param databaseModel   the properties model
	 */
	public static void save(PropertiesModel propertiesModel, DatabaseModel databaseModel) {
		PersistenceDataWrapper persistenceDataWrapper = new PersistenceDataWrapper(propertiesModel, databaseModel);
		try {
			persistenceDataWrapper.saveSaveDataWrapper();
		} catch (Exception e) {
			// what to do when we cant save?
			System.err.println("Save error;" + e.getMessage());
			e.printStackTrace();
		}
	}

	private PersistenceDataWrapper() {

	}

	private PersistenceDataWrapper(PersistenceDataWrapper other) {
		setWrapper(other);
	}

	private PersistenceDataWrapper(PropertiesModel propertiesModel, DatabaseModel databaseModel) {
		setWrapper(propertiesModel, databaseModel);
	}

	private void setWrapper(PersistenceDataWrapper other) {
		this.userName = other.userName;
		this.userEmail = other.userEmail;
		this.userMatrNr = other.userMatrNr;
		this.currentDataProviderProfile = other.currentDataProviderProfile;
		this.dataProviderProfiles = other.dataProviderProfiles;

		this.minFrameSizeX = other.minFrameSizeX;
		this.minFrameSizeY = other.minFrameSizeY;
		this.minFrameChartSizeX = other.minFrameChartSizeX;
		this.minFrameChartSizeY = other.minFrameChartSizeY;

		this.chartTyp = other.chartTyp;
		this.interval = other.interval;
		this.watchlistSymbols = other.watchlistSymbols;
		this.chartOnlySymbols = other.chartOnlySymbols;
		this.alarmList = other.alarmList;
		this.frameProfiles = other.frameProfiles;
		this.chartCount = other.chartCount;
		this.alarmColor = other.alarmColor;
		this.indicatorColorGD = other.indicatorColorGD;
		this.indicatorColorBB = other.indicatorColorBB;
		
	}

	/**
	 * Initializes the persistence data wrapper with the models
	 * 
	 * @param propertiesModel the properties model
	 * @param databaseModel   the database model
	 */
	private void setWrapper(PropertiesModel propertiesModel, DatabaseModel databaseModel) {
		setPropertiesModel(propertiesModel);
		setDatabaseModel(databaseModel);
	}

	/**
	 * Initializes the persistence data wrapper with the database model
	 * 
	 * @param databaseModel the database model
	 */
	private void setDatabaseModel(DatabaseModel databaseModel) {
		this.watchlistSymbols = databaseModel.getSymbolListByDisplayType(DisplayType.Watchlist);
		this.chartOnlySymbols = databaseModel.getSymbolListByDisplayType(DisplayType.ChartOnly);
		this.alarmList = databaseModel.getAlarmWrapperList();
	}

	/**
	 * Initializes the persistence data wrapper with the properties model
	 * 
	 * @param propertiesModel the properties model
	 */
	private void setPropertiesModel(PropertiesModel propertiesModel) {
		System.out.println("setPropertiesModel()");

		this.frameProfiles = propertiesModel.getFrameProfileList();

		this.chartCount = propertiesModel.getChartCount();

		this.currentDataProviderProfile = propertiesModel.getCurrentDataProviderProfile();
		this.dataProviderProfiles = propertiesModel.getDataProviderProfiles();

		this.minFrameSizeX = propertiesModel.getMinFrameSizeX();
		this.minFrameSizeY = propertiesModel.getMinFrameSizeY();
		this.minFrameChartSizeX = propertiesModel.getMinFrameChartSizeX();
		this.minFrameChartSizeY = propertiesModel.getMinFrameChartSizeY();

		this.chartTyp = propertiesModel.getChartTyp();
		this.interval = propertiesModel.getStandardInterval();

		this.alarmColor = propertiesModel.getAlarmColor();
		this.indicatorColorGD = propertiesModel.getIndicatorColorSMA();
		this.indicatorColorBB = propertiesModel.getIndicatorColorBB();

	}

	/**
	 * Gets a new {@code DatabaseModel} object.
	 * 
	 * @return the new {@code DatabaseModel} object
	 */
	public DatabaseModel getDatabaseModel() {
		DatabaseModel databaseModel = new DatabaseModel();
		databaseModel.addPersistedData(getWatchlistSymbols(), getChartOnlySymbols(), getAlarmList());
		return databaseModel;
	}

	/**
	 * Gets a new {@code PropertiesModel} object, tries to load persistent data.
	 * 
	 * @return the new {@code PropertiesModel} object
	 */
	public PropertiesModel getPropertiesModel() {
		PropertiesModel propertiesModel = new PropertiesModel();
		if (getPersistentFileExists()) {
			System.out.println("set properties from file...");

			// set the properties to the model
			propertiesModel.setProperties(userName, userEmail, userMatrNr, currentDataProviderProfile,
					dataProviderProfiles, minFrameSizeX, minFrameSizeY, minFrameChartSizeX, minFrameChartSizeY,
					chartTyp, interval, alarmColor, indicatorColorGD, indicatorColorBB);

			// set the merged list to the model
			propertiesModel.setFrameProfileList(frameProfiles);
		} else {
			System.out.println("set new properties");

			// create new "start" properties
			propertiesModel.resetToFactorySettings();

			// after reset!
			// add "start" windows/frames
			propertiesModel.addFrame(ViewType.Desktop);
			propertiesModel.addFrame(ViewType.Watchlist);
		}

		return propertiesModel;
	}

	/**
	 * Gets the watchlist symbol list
	 * 
	 * @return the watchlist symbol list
	 */
	public ArrayList<String> getWatchlistSymbols() {
		return this.watchlistSymbols;
	}

	/**
	 * Gets the chat only symbol list
	 * 
	 * @return the chat only symbol list
	 */
	public ArrayList<String> getChartOnlySymbols() {
		return this.chartOnlySymbols;
	}

	/**
	 * Gets the alarm wrapper list
	 * 
	 * @return the alarm wrapper list
	 */
	public ArrayList<AlarmWrapper> getAlarmList() {
		return this.alarmList;
	}

	/**
	 * Saves the this {@code PersistenceDataWrapper} object to a JSON file
	 * 
	 * @throws Exception when error occurred during saving
	 */
	private void saveSaveDataWrapper() throws Exception {
		System.out.println("save...");

		try {
			File myFile = new File(this.filename);

			if (myFile.exists() && myFile.isFile()) {
				System.out.println("overwrite " + this.filename + "...");
			} else {
				myFile.createNewFile();
				System.out.println("create new file " + myFile.getName());
			}

			String wrapper = saveDataWrapperToJson(this);
			FileWriter myWriter = new FileWriter(this.filename);
			myWriter.write(wrapper);
			myWriter.close();

			System.out.println("sucessfully saved to:" + this.filename);
		} catch (IOException e) {
			System.out.println("io error:" + e.toString());
			e.printStackTrace();
			throw new Exception("cant save file: " + this.filename);
		}
	}

	/**
	 * Loads the persistent data from the JSON file to this
	 * {@code PersistenceDataWrapper} object
	 * 
	 * @throws Exception when error occurred during loading
	 */
	private void loadSaveDataWrapper() throws Exception {
		System.out.println("try load file...");
		File myFile = new File(this.filename);

		if (myFile.exists() && myFile.isFile()) {

			String jsonProfile = "";
			try (BufferedReader br = new BufferedReader(new FileReader(this.filename))) {
				String strCurrentLine;
				while ((strCurrentLine = br.readLine()) != null) {
					jsonProfile += strCurrentLine + "\n";
				}
			} catch (IOException e) {
				System.err.println("io error:" + e.toString());
				e.printStackTrace();
			}

			try {
				PersistenceDataWrapper wrapper = jsonToSaveDataWrapper(jsonProfile);
				setWrapper(wrapper);
				System.out.println("load " + this.filename + " success");
			}
			catch(Exception e) {
				JOptionPane.showMessageDialog(null,this.filename + " is corrupted."); 
				throw new Exception(this.filename + " is corrupt ->" + e.toString());
			}

		} else {
			throw new Exception("load " + this.filename + " fail");
		}

	}

	/**
	 * Gets the persistentFileExists flag.
	 * 
	 * @return the persistentFileExists flag
	 */
	boolean getPersistentFileExists() {
		return this.persistentFileExists;
	}

	/**
	 * Sets the persistentFileExists flag.
	 * 
	 * @return the persistentFileExists flag
	 */
	void setPersistentFileExists(boolean flag) {
		this.persistentFileExists = flag;
	}
}
