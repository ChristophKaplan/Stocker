package stocker.controller.inputoutput;

import javax.swing.JOptionPane;

import stocker.model.externalclasses.PullData;
import stocker.model.externalclasses.PullQuoteData;
import stocker.model.externalclasses.PullSearchData;
import stocker.model.externalclasses.PushData;
import stocker.model.externalclasses.PushRequest;
import stocker.model.general.DataProviderProfile;
import stocker.model.general.TimeInterval;

/**
 * 
 * The abstract class {@code NetworkClient} is an extension of the class
 * {@code DatabaseController} and organizes the connection to a server as well as the
 * sending and receiving of various requests. It contains the two classes
 * {@code PushClient} and {@code PullClient} and extends the helper class {@code Serializer}.
 * 
 * 
 * 
 * @author Christoph Kaplan
 *
 */
public abstract class NetworkClient extends Serializer {
	private PullClient pullClient;
	private PushClient pushClient;
	private DataProviderProfile dataProviderProfile;

	public NetworkClient(DataProviderProfile dataProviderProfile) {
		this.dataProviderProfile = dataProviderProfile;
		// logging
		// System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY,
		// "ERROR");
		// The log levels are ERROR > WARN > INFO > DEBUG > TRACE

		pullClient = new PullClient();
	}

	/**
	 * Gets the push client.
	 * 
	 * @return the push client
	 */
	public PushClient getPushClient() {
		return this.pushClient;
	}

	/**
	 * Gets current {@code DataProviderProfile} object.
	 * 
	 * @return {@code DataProviderProfile} object
	 */
	public DataProviderProfile getDataProviderProfile() {
		return this.dataProviderProfile;
	}

	/**
	 * Sets current {@code DataProviderProfile} object.
	 * 
	 * @param {@code DataProviderProfile} object
	 */
	protected void setDataProviderProfile(DataProviderProfile dataProviderProfile) {
		if (dataProviderProfile == null)
			System.out.println("setDataProviderProfile -> null");
		else
			System.out.println("setDataProviderProfile -> " + dataProviderProfile.getName());

		this.dataProviderProfile = dataProviderProfile;
	}

	/**
	 * Requests historic data, translates it to a new {@code PullData} object.
	 * 
	 * @param symbol   of the {@code StockData} object whose historic data is
	 *                 requested
	 * @param interval considered {@code TimerInterval} value
	 * @return {@code PullData} object, the historic data
	 * @throws Exception throws exception when historic data is inaccessible, or the
	 *                   data's status is not "ok"
	 */
	protected PullData doDataRequest(String symbol, TimeInterval interval) throws Exception {
		//System.out.println("doDataRequest -> " + interval.toString()+ " " +interval.getResolutionCode() +" "+ interval.printFromTo());
				
		//recalculate the time window based on the amount of data we want
		interval.prepareTimeWindow(500);
				
		String q = dataProviderProfile.pullDataQueryURL(symbol, interval);
		try {
			String data = pullClient.doPullRequest(q);
			PullData pd = jsonToPullData(data);
			
			if (!pd.getStatus().equals("ok")) {
				throw new Exception(""+ pd.getStatus());
			}

			return pd;
		} catch (Exception ex) {
			throw new Exception("doDataRequest(" + symbol + ","+ interval.toString()+") -> " + ex.getMessage());
		}
	}

	/**
	 * Requests search data, translates it to a new {@code PullSearchData} object.
	 * 
	 * @param search the search-word
	 * @return {@code PullSearchData} object, the search data
	 * @throws Exception thrown when request error occurred
	 */
	protected PullSearchData doSearchRequest(String search) throws Exception {
		try {
			String request = dataProviderProfile.pullSearchQueryURL(search);
			String searchResult = pullClient.doPullRequest(request);
			PullSearchData psd = jsonToPullSearchData(searchResult);
			return psd;
		} catch (Exception ex) {
			throw new Exception("PullSearchData(): " + ex.getMessage());
		}
	}

	/**
	 * Requests quote data, translates it to a new {@code PullQuoteData} object.
	 * 
	 * @param symbol of the {@code StockData} object whose quote data is requested
	 * @return {@code PullQuoteData} object, the quote data
	 * @throws Exception thrown when request error occurred
	 */
	protected PullQuoteData doQuoteRequest(String symbol) throws Exception {
		try {
			String url = dataProviderProfile.pullQuoteURL(symbol);
			String quoteResult = pullClient.doPullRequest(url);
			PullQuoteData pqd = jsonToPullQuoteData(quoteResult);
			return pqd;
		} catch (Exception ex) {
			throw new Exception("PullSearchData():" + ex.getMessage());
		}

	}

	/**
	 * Connects to a data provider.
	 */
	protected void connectPushClient() {
		if (pushClient != null) {
			pushClient.close();
		}
		if (dataProviderProfile == null) {
			JOptionPane.showMessageDialog(null,	"no data provider profile set. please go to properties and set a new profile");
			setStatus("no data provider profile");
			return;
		}
		pushClient = new PushClient(dataProviderProfile.pushURL(), this);
		pushClient.doConnect();
	}

	/**
	 * Disconnects from a data provider
	 */
	protected void disconnectPushClient() {
		if (pushClient != null) {
			System.out.println("disconnectPushClient()");
			pushClient.close();
			pushClient = null;
		}

	}

	/**
	 * Subscribe a symbol
	 * 
	 * @param symbol the symbol of a {@code StockData} object
	 */
	protected void subscribeTo(String symbol) {
		// System.out.println("subscribeTo" + name);
		String request = pushRequestToJson(new PushRequest(true, symbol));
		pushClient.doPushRequest(request);
	}

	/**
	 * Unsubscribe a symbol
	 * 
	 * @param symbol the symbol of a {@code StockData} object
	 */
	protected void unsubscribeFrom(String symbol) {
		// System.out.println("unsubscribeFrom" + name);
		String request = pushRequestToJson(new PushRequest(false, symbol));
		pushClient.doPushRequest(request);
	}

	/**
	 * Receives a message from the {@code PushClient} when connected.
	 * 
	 * @param message from the {@code PushClient}
	 */
	public void pushClientMessage(String message) {
		PushData pd = jsonToPushData(message);
		onPushDataMessage(pd);
	}

	/**
	 * Combines status information and notifies listeners about the new status.
	 * 
	 * @param info additional status information
	 */
	public void setStatus(String info) {
		String status = "";

		if (pushClient != null && pushClient.isConnected()) {
			status += "# connected to: " + this.getDataProviderProfile().getPullURL() + " #";
		} else {
			status += "# no connection. #";
		}
		if(!info.isBlank()) {
			status += "# " + info + " #";
		}

		onStatusChange(status);
	}

	/**
	 * Receives real-time data from {@link #pushClientMessage} when connected.
	 * 
	 * @param pushData real-time data
	 */
	public abstract void onPushDataMessage(PushData pushData);

	/**
	 * Receives status from {@link #setStatus} when changed.
	 * 
	 * @param status current network client status
	 */
	public abstract void onStatusChange(String status);

}
