package stocker.controller.database;

import javax.swing.SwingWorker;

import stocker.controller.inputoutput.NetworkClient;
import stocker.model.database.DatabaseActionListener;
import stocker.model.database.DatabaseModel;
import stocker.model.externalclasses.PullData;
import stocker.model.externalclasses.PullQuoteData;
import stocker.model.externalclasses.PullSearchData;
import stocker.model.externalclasses.PushData;
import stocker.model.general.DataProviderProfile;
import stocker.model.general.DisplayType;
import stocker.model.general.TimeInterval;
import stocker.model.properties.PropertiesListener;
import stocker.model.properties.PropertiesModel;

/**
 * The database controller acts as an intermediary between the model of the data
 * provider and the database model. By expanding the {@code NetworkClient}
 * class, the database controller has the ability to import data via REST web
 * services (PullClient) and communicate via web sockets (PushClient). The
 * NetworkClient communicates directly with the provider and converts the raw
 * JSON data into the corresponding classes specified by the provider.
 * 
 * The methods {@link #retrieveSearchDataForGeneral(String, DisplayType)} and
 * {@link #retrieveSearchDataForSymbol(String, DisplayType)} implement a search
 * query for a general “search word” and for a special share via symbol.
 * {@link #retrievePullQuoteDataForSymbol(String)} sends a request for the
 * “Quote” data of a stock and
 * {@link #retrievePullDataForSymbol(String, TimeInterval)} sends a request for
 * stock data for a specific stock. These 4 called “retriever” methods use
 * {@code SwingWorker} threading for their respective HTTP requests. If the data
 * is received successfully, they transfer the data to the database model. If
 * the data cannot be accessed, this is also noted in the database model. The
 * method {@link #onPushDataMessage(PushData)} receives the real-time data,
 * checks its usability and transfers it to the data model.
 * 
 * In addition to methods for starting / stopping, connect / disconnect,
 * subscribe requests, there is a method,
 * {@link #onDataProviderChange(DataProviderProfile)}, which initiates the
 * server change. First, the new data provider is assigned, during “shutdown”
 * (shutdownDatabase ()) the connection to the server is also disconnected and
 * the existing data is set to the stock data state type {@code unclear}
 * (resetStockDataState ()). When “booting” (bootDatabase ()), the database is
 * first connected to the new server, then the new data provider is queried for
 * the existing stock names and, if necessary, the data is loaded.
 * 
 * 
 * 
 * 
 * @author Christoph Kaplan
 *
 */
public class DatabaseController extends NetworkClient implements DatabaseActionListener, PropertiesListener {

	private DatabaseModel databaseModel;
	private PropertiesModel propertiesModel;

	/**
	 * Constructor of {@code DatabaseController}
	 * 
	 * @param propertiesModel {@code PropertiesModel} object
	 * @param databaseModel   {@code DatabaseModel} object
	 */
	public DatabaseController(PropertiesModel propertiesModel, DatabaseModel databaseModel) {
		super(propertiesModel.getCurrentDataProviderProfile());
		
		this.propertiesModel = propertiesModel;
		this.propertiesModel.addPropertiesObserver(this);

		this.databaseModel = databaseModel;
		this.databaseModel.addDatabaseActionObserver(this);
	}

	/**
	 * Boots the database. Uses {@link #connectDatabase} and
	 * {@link stocker.model.database.DatabaseModel#askMissingDataAll}
	 */
	public void bootDatabase() {
		//System.out.println("bootDatabase():" + propertiesModel.getCurrentDataProviderProfile());
		//databaseModel.printDB();
		
		onStatusChange("# boot database #");
		
		if(propertiesModel.getCurrentDataProviderProfile() == null){
			setStatus("no data provider profile selected");
			return;
		}
		connectDatabase(true);
		databaseModel.resetStockDataState();
		databaseModel.askMissingDataAll(propertiesModel.getStandardInterval());
	}

	/**
	 * Shuts the database down. Uses {@link #connectDatabase} and
	 * {@link #clearDataOnly}
	 */
	private void shutdownDatabase() {
		onStatusChange("# shutdown database #");
		connectDatabase(false);
	}

	/**
	 * Connects database to the current data provider
	 * 
	 * @param connect, true for connect, false for disconnect
	 */
	private void connectDatabase(boolean connect) {
		//System.out.println("connectDatabase(" + connect + ")");
		
		if (connect) {
			onStatusChange("# connect database #");
			connectPushClient();}
		else {
			onStatusChange("# disconnect database #");
			disconnectPushClient();
		}
	}

	/**
	 * Receives real-time data from {@code NetworkClient} when connected.
	 */
	@Override
	public void onPushDataMessage(PushData pushData) {
		// System.out.println("pushClientMessage(): " + message);
		if (!pushData.isPing() && !pushData.isError()) {
			databaseModel.addPushData(pushData);
		}
	}

	/**
	 * Receives status from {@code NetworkClient} when changed.
	 */
	@Override
	public void onStatusChange(String status) {
		// System.out.println("onStatusInfoChange " + status);
		databaseModel.statusChange(status);
	}

	/**
	 * Requests a {@code PullSearchData} object by a general "search word". Uses
	 * {@code SwingWorder} object for threading.
	 * 
	 * @param search      the search word
	 * @param displayType corresponding {@code DisplayType} value, where the data is
	 *                    shown.
	 */
	private void retrieveSearchDataForGeneral(String search, DisplayType displayType) {
		SwingWorker<PullSearchData, Void> worker = new SwingWorker<PullSearchData, Void>() {
			@Override
			protected PullSearchData doInBackground() throws Exception {
				return doSearchRequest(search);
			}

			@Override
			protected void done() {
				try {
					PullSearchData pullSearchData = get();
					databaseModel.addPullSearchData(pullSearchData, displayType);
				} catch (Exception ex) {
					databaseModel.noPullSearchDataFound(search);
					System.out.println("retrieveSearchDataForGeneral():" + ex.getMessage());
				}

			}
		};
		worker.execute();
	}

	/**
	 * Requests a {@code PullSearchData} object by a specific symbol. Uses
	 * {@code SwingWorder} object for threading.
	 * 
	 * @param symbol      the specific symbol
	 * @param displayType corresponding {@code DisplayType} value, where the data is
	 *                    shown.
	 */
	private void retrieveSearchDataForSymbol(String symbol, DisplayType displayType) {
		SwingWorker<PullSearchData, Void> worker = new SwingWorker<PullSearchData, Void>() {
			@Override
			protected PullSearchData doInBackground() throws Exception {
				return doSearchRequest(symbol);
			}

			@Override
			protected void done() {
				try {
					PullSearchData pullSearchData = get();
					PullSearchData.Entry e = pullSearchData.getEntryBySymbol(symbol);
					databaseModel.addPullSearchDataEntry(e, displayType);
				} catch (Exception ex) {
					databaseModel.noPullSearchDataFound(symbol);
					System.out.println("retrieveSearchDataForSymbol():" + ex.getMessage());
				}

			}
		};
		worker.execute();

	}

	/**
	 * Requests a {@code PullData} object by symbol. Uses {@code SwingWorder} object
	 * for threading.
	 * 
	 * @param symbol   the specific symbol
	 * @param interval corresponding {@code TimeInterval} value
	 */
	private void retrievePullDataForSymbol(String symbol, TimeInterval interval) {
		SwingWorker<PullData, Void> worker = new SwingWorker<PullData, Void>() {
			@Override
			protected PullData doInBackground() throws Exception {
				return doDataRequest(symbol, interval);
			}

			@Override
			protected void done() {
				try {
					PullData pd = get();
					databaseModel.addPullData(symbol, pd, interval);
				} catch (Exception ex) {
					databaseModel.noPullDataFound(symbol, interval);
					System.out.println("retrievePullDataForSymbol():" + ex.getMessage());
				}
			}
		};
		worker.execute();
	}

	/**
	 * Requests a {@code PullQuoteData} object by symbol. Uses {@code SwingWorder}
	 * object for threading.
	 * 
	 * @param symbol the specific symbol
	 */
	private void retrievePullQuoteDataForSymbol(String symbol) {
		SwingWorker<PullQuoteData, Void> worker = new SwingWorker<PullQuoteData, Void>() {
			@Override
			protected PullQuoteData doInBackground() throws Exception {
				return doQuoteRequest(symbol);
			}

			@Override
			protected void done() {
				try {
					PullQuoteData pqd = get();
					databaseModel.addPullQuoteData(symbol, pqd);
				} catch (Exception ex) {
					databaseModel.noPullQuoteDataFound(symbol);
					System.out.println("retrievePullQuoteDataForSymbol():" + ex.getMessage());
				}
			}
		};
		worker.execute();

	}

	/**
	 * Invoked by {@code DatabaseModel}, when asked for search data by symbol.
	 */
	@Override
	public void onAskSearchDataForSymbol(String symbol, DisplayType didsplayType) {
		retrieveSearchDataForSymbol(symbol, didsplayType);
	}

	/**
	 * Invoked by {@code DatabaseModel}, when asked for a general search data.
	 */
	@Override
	public void onAskSearchDataForGeneral(String search, DisplayType displayType) {
		retrieveSearchDataForGeneral(search, displayType);
	}

	/**
	 * Invoked by {@code DatabaseModel}, when asked for a historic data.
	 */
	@Override
	public void onAskHistoricDataForSymbol(String symbol, TimeInterval interval) {
		retrievePullDataForSymbol(symbol, interval);
	}

	/**
	 * Invoked by {@code DatabaseModel}, when asked for a quote data.
	 */
	@Override
	public void onAskQuoteDataForSymbol(String symbol) {
		retrievePullQuoteDataForSymbol(symbol);
	}

	/**
	 * Invoked by {@code DatabaseModel}, when asked to subscribe a symbol.
	 */
	@Override
	public void onSubscribe(String symbol) {
		this.subscribeTo(symbol);

	}

	/**
	 * Invoked by {@code DatabaseModel}, when asked to unsubscribe a symbol.
	 */
	@Override
	public void onUnsubscribe(String symbol) {
		this.unsubscribeFrom(symbol);
	}

	/**
	 * Invoked by {@code PropertiesModel}, when data provider change occurred.
	 */
	@Override
	public void onDataProviderChange(DataProviderProfile dataProviderProfile) {
		// System.out.println("onDataProviderChange() " + dataProviderProfile.getName());
		//sets to the network
		this.setDataProviderProfile(dataProviderProfile);
		
		//clear search
		this.databaseModel.clearDatabaseByDisplayType(DisplayType.Search);
		
		shutdownDatabase();
		bootDatabase();
		
	}

}
