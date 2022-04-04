package stocker.model.externalclasses;

import com.google.gson.annotations.SerializedName;

/**
 * {@code PushRequest} class, used to subscribe/unsubscribe symbols, and is send
 * to provider. Structure given by the funnhub.io data provider.
 * 
 * @author Christoph Kaplan
 */
public class PushRequest {

	/**
	 * type = "subscribe" or = "unsubscribe"
	 */
	@SerializedName("type")
	String type;
	
	/**
	 * the symbol to subscribe/unsubscribe
	 */
	@SerializedName("symbol")
	String symbol;

	/**
	 * {@code PushRequest} constructor
	 * @param subscribe true for subscribe, false for unsubscribe
	 * @param symbol the considered symbol
	 */
	public PushRequest(boolean subscribe, String symbol) {
		if (subscribe)
			this.type = "subscribe";
		else
			this.type = "unsubscribe";

		this.symbol = symbol;
	}
}