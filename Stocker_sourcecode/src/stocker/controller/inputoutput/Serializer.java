package stocker.controller.inputoutput;

import com.google.gson.*;

import stocker.model.externalclasses.PullData;
import stocker.model.externalclasses.PullQuoteData;
import stocker.model.externalclasses.PullSearchData;
import stocker.model.externalclasses.PushData;
import stocker.model.externalclasses.PushRequest;
import stocker.model.general.FrameProfileBase;
import stocker.model.general.IndicatorBase;

/**
 * The helper class Serializer is a collection of converter methods to convert JSON data
 * into objects and vice versa. It is used as an extension of the I / O classes
 * {@code PersistenceDataWrapper} and {@code NetworkClient}.
 * 
 * @author Christoph Kaplan
 *
 */
public class Serializer {

	public Serializer() {
	}

	/**
	 * Deserializes a JSON {@code String} to a {@code PullData} object
	 * 
	 * @param jsonData the JSON {@code String}
	 * @return the {@code PullData} object
	 */
	public PullData jsonToPullData(String jsonData) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		PullData d = gson.fromJson(jsonData, PullData.class);
		return d;
	}

	/**
	 * Deserializes a JSON {@code String} to a {@code PullSearchData} object
	 * 
	 * @param jsonData the JSON {@code String}
	 * @return the {@code PullSearchData} object
	 */
	public PullSearchData jsonToPullSearchData(String jsonData) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		PullSearchData d = gson.fromJson(jsonData, PullSearchData.class);
		return d;
	}

	/**
	 * Deserializes a JSON {@code String} to a {@code PullQuoteData} object
	 * 
	 * @param jsonData the JSON {@code String}
	 * @return the {@code PullQuoteData} object
	 */
	public PullQuoteData jsonToPullQuoteData(String jsonData) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		PullQuoteData d = gson.fromJson(jsonData, PullQuoteData.class);
		return d;
	}

	/**
	 * Deserializes a JSON {@code String} to a {@code PushData} object
	 * 
	 * @param jsonData the JSON {@code String}
	 * @return the {@code PushData} object
	 */
	public PushData jsonToPushData(String jsonData) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		PushData d = gson.fromJson(jsonData, PushData.class);
		return d;
	}

	/**
	 * Deserializes a JSON {@code String} to a {@code PersistenceDataWrapper} object
	 * 
	 * @param jsonData the JSON {@code String}
	 * @return the {@code PersistenceDataWrapper} object
	 */
	public PersistenceDataWrapper jsonToSaveDataWrapper(String jsonData) {
		// register the serializer adapter's
		Gson gson = new GsonBuilder().setPrettyPrinting()
				.registerTypeAdapter(IndicatorBase.class, new SerializerAdapter<IndicatorBase>())
				.registerTypeAdapter(FrameProfileBase.class, new SerializerAdapter<FrameProfileBase>()).create();
		return gson.fromJson(jsonData, PersistenceDataWrapper.class);
	}

	/**
	 * Serializes a {@code PushRequest} object to a JSON {@code String}
	 * 
	 * @param pushRequest the {@code PushRequest} object
	 * @return the JSON {@code String}
	 */
	public String pushRequestToJson(PushRequest pushRequest) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(pushRequest);
	}

	/**
	 * Serializes a {@code PersistenceDataWrapper} object to a JSON {@code String}
	 * 
	 * @param persistenceWrapper the {@code PersistenceDataWrapper} object
	 * @return the JSON {@code String}
	 */
	public String saveDataWrapperToJson(PersistenceDataWrapper persistenceWrapper) {
		// register the serializer adapter's
		Gson gson = new GsonBuilder().setPrettyPrinting()
				.registerTypeAdapter(IndicatorBase.class, new SerializerAdapter<IndicatorBase>())
				.registerTypeAdapter(FrameProfileBase.class, new SerializerAdapter<FrameProfileBase>()).create();
		return gson.toJson(persistenceWrapper);
	}
	//

}
