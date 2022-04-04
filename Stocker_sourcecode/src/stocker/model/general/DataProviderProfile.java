package stocker.model.general;


/**
 * Stores the data used to connect to a data provider
 * @author Christoph Kaplan
 *
 */
public class DataProviderProfile implements Comparable<DataProviderProfile>{
	private String name;
	private String apiKey;
	private String basePullURL;
	private String basePushURL;

	/**
	 * {@code DataProviderProfile} constructor
	 * @param name the name of this profile
	 * @param apiKey the api key
	 * @param basePullURL the pull url
	 * @param basePushURL the push url
	 */
	public DataProviderProfile(String name, String apiKey, String basePullURL, String basePushURL) {
		this.name = name;
		this.apiKey = apiKey;
		this.basePullURL = basePullURL;
		this.basePushURL = basePushURL;
	}

	/**
	 * Gets the name
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Gets the api key
	 * @return the pi key
	 */
	public String getApiKey() {
		return this.apiKey;
	}

	/**
	 * Gets the pull url.
	 * @return the pull url
	 */
	public String getPullURL() {
		return this.basePullURL;
	}

	/**
	 * Gets the push url.
	 * @return the push url
	 */
	public String getPushURL() {
		return this.basePushURL;
	}

	
	/**
	 * Constructs a data query url, to ask for historic data.
	 * @param symbol the considered symbol
	 * @param interval the time interval (resolution)
	 * @return resulting url of type {@code String}
	 */
	public String pullDataQueryURL(String symbol, TimeInterval interval) {
		return getPullURL() + "/stock/candle?symbol=" + symbol + "&resolution=" + interval.getResolutionCode()
				+ "&from=" + (interval.getFrom()/1000L) + "&to=" + (interval.getTo()/1000L) + "&token=" + getApiKey();
	}
	
	/**
	 * Constructs a data query url, to ask for historic data.
	 * @param search the search word
	 * @return resulting url of type {@code String}
	 */
	public String pullSearchQueryURL(String search) {
		return getPullURL() + "/search/?q=" + search + "&token=" + getApiKey();
	}
	
	/**
	 * Constructs a data query url, to ask for quote data.
	 * @param symbol symbol the considered symbol
	 * @return resulting url of type {@code String}
	 */
	public String pullQuoteURL(String symbol) {
		return getPullURL() + "/quote?symbol=" + symbol + "&token=" + getApiKey();
	}
	
	/**
	 * Constructs the push url, to connect via websocket.
	 * @return resulting url of type {@code String}
	 */
	public String pushURL() {
		return getPushURL() + "?token=" + getApiKey();
	}


	/**
	 * Overrides compareTo()
	 */
	@Override
	public int compareTo(DataProviderProfile o) {
		// TODO Auto-generated method stub
		return this.name.compareTo(o.getName());
	}
	
	
	public boolean isValid() {
		if(getName().isBlank() || getApiKey().isBlank() || getPullURL().isBlank() || getPushURL().isBlank()) {
			return false;
		}
		return true;
	}
	
}
