package stocker.model.externalclasses;

import com.google.gson.annotations.SerializedName;

/**
 * {@code PullData} class, temporarily contains the historic data, when received from provider.
 * Structure given by the funnhub.io data provider.
 * 
 * @author Christoph Kaplan
 */
public class PullData {
	
	/** close */
	@SerializedName("c")
	private double[] c;

	/** high */
	@SerializedName("h")
	private double[] h;

	/** low */
	@SerializedName("l")
	private double[] l;

	/** open */
	@SerializedName("o")
	private double[] o;

	/** status */
	@SerializedName("s")
	private String s;

	/** Unix timestamp, time*/
	@SerializedName("t")
	private long[] t;

	/** volume*/
	@SerializedName("v")
	private double[] v;

	/**
	 * Gets status
	 * @return the status
	 */
	public String getStatus() {
		return s;
	}

	/**
	 * Gets close price
	 * @return the close price
	 */
	public double[] getClose() {
		return this.c;
	}


	/**
	 * Gets high price
	 * @return the high price
	 */
	public double[] getHigh() {
		return this.h;
	}


	/**
	 * Gets low price
	 * @return the low price
	 */
	public double[] getLow() {
		return this.l;
	}


	/**
	 * Gets open price
	 * @return the open price
	 */
	public double[] getOpen() {
		return this.o;
	}


	/**
	 * Gets timestamp
	 * @return the timestamp
	 */
	public long[] getTimestamp() {
		return this.t;
	}

	/**
	 * Gets timestamp in milliseconds
	 * @return the timestamp in milliseconds
	 */
	public long[] getTimestampInMillies() {
		long[] millis = new long[t.length];
		for(int i = 0; i< t.length;i++) {
			millis[i] = t[i]*1000L;
		}
		return millis;
	}
	
	

	/**
	 * Gets volume
	 * @return the volume
	 */
	public double[] getVolume() {
		return this.v;
	}


}
