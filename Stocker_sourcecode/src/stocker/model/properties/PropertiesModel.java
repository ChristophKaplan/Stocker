package stocker.model.properties;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.*;


import stocker.model.general.ChartType;
import stocker.model.general.DataProviderProfile;
import stocker.model.general.FrameProfile;
import stocker.model.general.FrameProfileBase;
import stocker.model.general.FrameProfileChart;
import stocker.model.general.ViewType;
import stocker.model.general.IndicatorBase;
import stocker.model.general.TimeInterval;

/**
 * The properties model contains administrative data (user data or server
 * connection data) as well as data relating to window-frames, indicators and
 * GUI standard settings. An observer pattern implements 2 lists for 2 different
 * observer types. The PropertiesFrameListener monitors changes in the frame
 * profiles including indicators. The PropertiesListener monitors changes in the
 * data that do not affect the frame profiles, e.g. B. Change of data
 * provider/server. The model also implements a list of the
 * {@code FrameProfileBase} type. The {@code FrameProfileBase} serves as the
 * basis for the derived classes {@code FrameProfile} and
 * {@code FrameProfileChart}, both of which are important elements of this
 * model. They contain the relevant frame information such as: size and
 * position. {@code FrameProfileChart}, contains additional chart window
 * information such as a list of the active indicators, the "symbol" of the
 * stock to be displayed, the chart type and the time interval considered. A
 * list of the type {@code DataProviderProfile}, a wrapper class for server
 * connection data, manages several possible connection data. Furthermore,
 * methods for creating new frames and chart frames as well as for removing /
 * adding frames and indicators are implemented. As soon as a change with regard
 * to frames or indicators occurs here, the relevant observers are notified.
 * There are also some variables for maintaining the data described above, such
 * as standard colors and sizes.
 * 
 * 
 * 
 * @author Christoph Kaplan
 *
 */
public class PropertiesModel extends PropertiesObserverPattern {
	private String userName;
	private String userEmail;
	private String userMatrNr;

	private DataProviderProfile currentDataProviderProfile;
	private ArrayList<DataProviderProfile> dataProviderProfiles = new ArrayList<DataProviderProfile>();

	private ChartType standardChartTyp;
	private TimeInterval standardInterval;

	private int minFrameSizeX;
	private int minFrameSizeY;
	private int minFrameChartSizeX;
	private int minFrameChartSizeY;

	private Color indicatorColorSMA;
	private Color indicatorColorBB;
	private Color alarmColor;
	
	
	private ArrayList<FrameProfileBase> frameProfiles = new ArrayList<FrameProfileBase>();

	/**
	 * Stores all the given parameters to this model.
	 * 
	 * @param userName                   user name
	 * @param userEmail                  user email
	 * @param userMatrNr                 user matrikel nr.
	 * @param currentDataProviderProfile current data provider
	 * @param dataProviderProfiles       available data providers
	 * @param minX                       standard minimum frame size X
	 * @param minY                       standard minimum frame size Y
	 * @param chartMinX                  standard minimum chart frame size X
	 * @param chartMinY                  standard minimum chart frame size Y
	 * @param chartType                  standard chart type
	 * @param timeInterval               standard time interval
	 * @param alarmColor                 standard alarm color
	 * @param indicatorColorSMA          standard simple moving average indicator color
	 * @param indicatorColorBB           standard bollinger bands indicator color
	 */
	public void setProperties(String userName, String userEmail, String userMatrNr,
			DataProviderProfile currentDataProviderProfile, ArrayList<DataProviderProfile> dataProviderProfiles,
			int minX, int minY, int chartMinX, int chartMinY, ChartType chartType, TimeInterval timeInterval,
			Color alarmColor, Color indicatorColorSMA, Color indicatorColorBB) {

		this.userName = userName;
		this.userEmail = userEmail;
		this.userMatrNr = userMatrNr;

		// this method sets the new provider and handles the change
		this.setCurrentDataProviderProfile(currentDataProviderProfile);

		this.dataProviderProfiles = dataProviderProfiles;

		this.minFrameSizeX = minX;
		this.minFrameSizeY = minY;

		this.minFrameChartSizeX = chartMinX;
		this.minFrameChartSizeY = chartMinY;

		this.indicatorColorSMA = indicatorColorSMA;
		this.indicatorColorBB = indicatorColorBB;
		this.alarmColor = alarmColor;
		
		this.standardChartTyp = chartType;
		this.standardInterval = timeInterval;

		
		// update the current frameprofiles
		this.onGetFrameSCREENSTATE();
		
		for (int i = 0; i < frameProfiles.size(); i++) {
			FrameProfileBase frame = frameProfiles.get(i);
			if (frame instanceof FrameProfileChart) {
				FrameProfileChart fpChart = (FrameProfileChart) frame;
				fpChart.setMinSizeX(chartMinX);
				fpChart.setMinSizeY(chartMinY);
			} else {
				FrameProfile fp = (FrameProfile) frame;
				fp.setMinSizeX(minX);
				fp.setMinSizeY(minY);
			}
			this.onFrameUpdate(frame);
		}
	}

	/**
	 * Resets all variables to "factory settings"
	 */
	public void resetToFactorySettings() {
		this.userName = "Kaplan, Christoph";
		this.userEmail = "contact@christophkaplan.de";
		this.userMatrNr = "3285766";

		/*
			addAvailableDataProviderProfile(new DataProviderProfile("Kursdatengenerator", "q3285766",
					"http://localhost:8080", "ws://localhost:8090"));
			addAvailableDataProviderProfile(new DataProviderProfile("Finnhub", "c1goq9v48v6v8dn0dq90",
					"https://finnhub.io/api/v1", "wss://ws.finnhub.io/"));
			setCurrentDataProviderProfile(this.dataProviderProfiles.get(0));
		*/

		this.minFrameSizeX = 400;
		this.minFrameSizeY = 500;
		this.minFrameChartSizeX = 900;
		this.minFrameChartSizeY = 600;

		this.standardChartTyp = ChartType.CandleChart;
		this.standardInterval = TimeInterval.Day;

		this.indicatorColorSMA = new Color(67, 34, 165, 255);
		this.indicatorColorBB = new Color(247, 219, 135, 255);
		this.alarmColor = new Color(254, 72, 101);
		
	}

	/**
	 * Gets the standard {@code TimerInterval} value.
	 * 
	 * @return {@code TimerInterval} value
	 */
	public TimeInterval getStandardInterval() {
		return standardInterval;
	}

	/**
	 * Gets the standard minimum frame size X.
	 * 
	 * @return the standard minimum frame size X
	 */
	public int getMinFrameSizeX() {
		return minFrameSizeX;
	}

	/**
	 * Gets the standard minimum frame size Y.
	 * 
	 * @return the standard minimum frame size Y
	 */
	public int getMinFrameSizeY() {
		return minFrameSizeY;
	}

	/**
	 * Gets the standard minimum chart frame size X.
	 * 
	 * @return the standard minimum chart frame size X
	 */
	public int getMinFrameChartSizeX() {
		return minFrameChartSizeX;
	}

	/**
	 * Gets the standard minimum chart frame size Y.
	 * 
	 * @return the standard minimum chart frame size Y
	 */
	public int getMinFrameChartSizeY() {
		return minFrameChartSizeY;
	}

	/**
	 * Gets the standard {@code ChartType} value.
	 * 
	 * @return the standard {@code ChartType} value.
	 */
	public ChartType getChartTyp() {
		return standardChartTyp;
	}

	/**
	 * Gets the user name
	 * 
	 * @return the user name
	 */
	public String getName() {
		return this.userName;
	}

	/**
	 * Gets the email
	 * 
	 * @return the email
	 */
	public String getEmail() {
		return this.userEmail;
	}

	/**
	 * Gets the matrikel number
	 * 
	 * @return the matrikel number
	 */
	public String getMatrNr() {
		return this.userMatrNr;
	}

	/**
	 * Gets the list of available {@code DataProverProfile} objects.
	 * 
	 * @return the list of available {@code DataProverProfile} objects
	 */
	public ArrayList<DataProviderProfile> getDataProviderProfiles() {
		return this.dataProviderProfiles;
	}

	/**
	 * Gets the current {@code DataProverProfile} object.
	 * 
	 * @return the current {@code DataProverProfile} object
	 */
	public DataProviderProfile getCurrentDataProviderProfile() {
		return this.currentDataProviderProfile;
	}

	/**
	 * Sets the current {@code DataProverProfile} object.
	 * 
	 * @param currentDataProviderProfile {@code DataProverProfile} object to set.
	 */
	public void setCurrentDataProviderProfile(DataProviderProfile currentDataProviderProfile) {
		if (currentDataProviderProfile != null && !currentDataProviderProfile.isValid()) {
			System.out.println("setCurrentDataProviderProfile(): profile is not valid");
			return;
		}
		// checks if a new data provider is set
		boolean providerChangeFlag = false;
		if (this.currentDataProviderProfile != currentDataProviderProfile) {
			providerChangeFlag = true;
		}
		// set the new profile
		this.currentDataProviderProfile = currentDataProviderProfile;
		// notify the listeners
		if (providerChangeFlag) {
			onDataProviderChange(currentDataProviderProfile);
		}
	}

	/**
	 * Adds a data provider profile to the list of available profiles.
	 * 
	 * @param availableDataProviderProfile the data provider profile to add
	 */
	public void addAvailableDataProviderProfile(DataProviderProfile availableDataProviderProfile) {
		if (availableDataProviderProfile == null || !availableDataProviderProfile.isValid()) {
			System.out.println("addAvailableDataProviderProfile(): profile is not valid or null");
			return;
		}
		if (this.dataProviderProfiles.contains(availableDataProviderProfile))
			return;
		this.dataProviderProfiles.add(availableDataProviderProfile);
	}

	/**
	 * Removes a data provider profile to the list of available profiles.
	 * 
	 * @param availableDataProviderProfile the data provider profile to remove
	 */
	public void removeAvailableDataProviderProfile(DataProviderProfile availableDataProviderProfile) {
		if (!this.dataProviderProfiles.contains(availableDataProviderProfile))
			return;
		this.dataProviderProfiles.remove(availableDataProviderProfile);
	}

	/**
	 * Gets the standard alarm color.
	 * 
	 * @return the standard alarm color
	 */
	public Color getAlarmColor() {
		return this.alarmColor;
	}

	/**
	 * Gets the standard simple moving average indicator color.
	 * 
	 * @return the standard simple moving average indicator color
	 */
	public Color getIndicatorColorSMA() {
		return this.indicatorColorSMA;
	}

	/**
	 * Gets the standard bollinger bands indicator color.
	 * 
	 * @return the standard bollinger bands indicator color
	 */
	public Color getIndicatorColorBB() {
		return this.indicatorColorBB;
	}
	
	
	/**
	 * Gets the chart count.
	 * 
	 * @return the chart count
	 */
	public int getChartCount() {
		int count = 0;
		for (FrameProfileBase fp : frameProfiles) {
			if (fp instanceof FrameProfileChart) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Notifies listeners via {@link #onFrameAdd} all currently stored frames. Used
	 * when application starts and all persistent frames are loaded.
	 */
	public void notifyAllLoadedFramesOnFrameAdd() {
		for (FrameProfileBase fp : frameProfiles) {
			this.onFrameAdd(fp);
		}
	}

	/**
	 * Sets the list of {@code FrameProfile} objects.
	 * 
	 * @param frameProfileList the list of {@code FrameProfile} objects.
	 */
	public void setFrameProfileList(ArrayList<FrameProfileBase> frameProfileList) {
		this.frameProfiles = frameProfileList;
	}

	/**
	 * Gets the list of {@code FrameProfile} objects.
	 * 
	 * @return the list of {@code FrameProfile} objects.
	 */
	public ArrayList<FrameProfileBase> getFrameProfileList() {
		return this.frameProfiles;
	}

	/**
	 * Checks if a frame is present.
	 * 
	 * @param id of the frame that is checked
	 * @return true if frame is present, otherwise false
	 */
	boolean frameAlreadyAdded(int id) {
		for (FrameProfileBase wp : frameProfiles) {
			if (wp.getID() == id) {
				System.out.println("already open");
				this.onMoveToFront(wp);
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets a frame by its id.
	 * 
	 * @param id of the frame
	 * @return the frame
	 * @throws Exception thrown when frame not found
	 */
	public FrameProfileBase getFrameProfileById(int id) throws Exception {
		for (FrameProfileBase wp : frameProfiles)
			if (wp.getID() == id)
				return wp;
		throw new Exception("getFrameProfileById(" + id + ") cant find frame by id");
	}

	/**
	 * Removes frame by id
	 * 
	 * @param id of the frame
	 */
	public void removeFrame(int id) {
		try {
			FrameProfileBase removeMe = getFrameProfileById(id);
			this.frameProfiles.remove(removeMe);
			this.onFrameRemove(removeMe);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Adds frame by {@code ViewType} value
	 * 
	 * @param viewType the {@code ViewType} value
	 */
	public void addFrame(ViewType viewType) {
		if (frameAlreadyAdded(viewType.ordinal()))
			return;
		FrameProfile newWindow = newFrameProfile(viewType);
		this.frameProfiles.add(newWindow);
		this.onFrameAdd(newWindow);
	}

	/**
	 * Creates a new {@code FrameProfile} object by {@code viewType} value
	 * 
	 * @param viewType the {@code viewType} value
	 * @return new {@code FrameProfile} object
	 */
	private FrameProfile newFrameProfile(ViewType viewType) {
		FrameProfile newWindow;
		if (viewType == ViewType.Desktop) {
			// MainWindow needs special sizes
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			String name = "Stoker_" + getName() + "_" + getMatrNr();
			
			int desktopPosX = screenSize.width / 4;
			int desktopPosY = screenSize.height / 4;
			int desktopSizeX = screenSize.width / 2;
			int desktopSizeY = screenSize.height / 2;
						
			newWindow = new FrameProfile(ViewType.Desktop.ordinal(), name, ViewType.Desktop, desktopPosX,desktopPosY, desktopSizeX, desktopSizeY, getMinFrameSizeX(), getMinFrameSizeY());
		} else {

			int dist = 10;
			int posX = getMinFrameSizeX() * (viewType.ordinal() - 1);
			int posY = dist;

			newWindow = new FrameProfile(viewType.ordinal(), viewType.toString(), viewType, posX, posY,
					getMinFrameSizeX(), getMinFrameSizeY(), getMinFrameSizeX(), getMinFrameSizeY());
		}
		return newWindow;
	}

	/**
	 * Adds a new {@code FrameProfileChart} object to the frame profile list, by
	 * symbol.
	 * 
	 * @param symbol of the {@code FrameProfileChart} object
	 */
	public void addChartFrame(String symbol) {
		FrameProfileChart newWindow = newFrameProfileChart(symbol);
		this.frameProfiles.add(newWindow);
		this.onFrameAdd(newWindow);
	}

	/**
	 * Creates a new {@code FrameProfileChart} object by symbol
	 * 
	 * @param symbol of the {@code FrameProfileChart} object
	 * @return new {@code FrameProfile} object
	 */
	private FrameProfileChart newFrameProfileChart(String symbol) {
		int chartCount = getChartCount();

		//calc position for the new chart frame 
		int posX = (chartCount + 1) * (getMinFrameChartSizeX() / 20);
		int posY = (chartCount + 1) * (getMinFrameChartSizeY() / 20);
		
		//calc chart frame id's 
		int id = chartCount + ViewType.values().length;

		return new FrameProfileChart(symbol, standardChartTyp, standardInterval, id, ViewType.Chart, posX, posY,
				getMinFrameChartSizeX(), getMinFrameChartSizeY(), getMinFrameChartSizeX(), getMinFrameChartSizeY());
	}

	/**
	 * Adds an indicator to a {@code FrameProfileChart} object by id
	 * 
	 * @param id        of the {@code FrameProfileChart} object
	 * @param indicator the indicator
	 */
	public void addIndicator(int id, IndicatorBase indicator) {
		try {
			FrameProfileChart chartProfile = (FrameProfileChart) getFrameProfileById(id);
			chartProfile.addIndicator(indicator);
			onUpdateIndicator(chartProfile);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Removes an indicator to a {@code FrameProfileChart} object by id
	 * 
	 * @param id        of the {@code FrameProfileChart} object
	 * @param indicator the indicator
	 */
	public void removeIndicator(int id, IndicatorBase indicator) {
		try {
			FrameProfileChart chartProfile = (FrameProfileChart) getFrameProfileById(id);
			chartProfile.removeIndicator(indicator);
			onUpdateIndicator(chartProfile);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	/**
	 * Updates all {@code FrameProfile} objects stored in each {@code ChartView}
	 * object. Invoked before persisting data.
	 */
	public void getFrameSCREENSTATE() {
		onGetFrameSCREENSTATE();
	}

}
