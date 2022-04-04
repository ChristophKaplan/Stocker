package stocker.model.externalclasses;

import com.google.gson.annotations.SerializedName;

/**
 * {@code PullQuoteData} class, temporarily contains the historic data, when
 * received from provider. Structure given by the funnhub.io data provider.
 * 
 * @author Christoph Kaplan
 */
public class PullQuoteData {

	/** current price of the day */
	@SerializedName("c")
	double c;

	/** high price of the day */
	@SerializedName("h")
	double h;

	/** low price of the day */
	@SerializedName("l")
	double l;

	/** open price of the day */
	@SerializedName("o")
	double o;

	/** previous close price */
	@SerializedName("pc")
	double pc;

	/** timestamp */
	@SerializedName("t")
	long t;

	/**
	 * Helper method, prints the content for debugging purposes
	 * @return the print content
	 */
	public String Print() {
		String s = "";
		s += "current price:" + c;
		s += " // pc:" + pc;
		return s;
	}

	/**
	 * Gets close price of the day
	 * @return the close price of the day
	 */
	public double getClose() {
		return this.c;
	}

	/**
	 * Gets high price of the day
	 * @return the high price of the day
	 */
	public double getHigh() {
		return this.h;
	}

	/**
	 * Gets low price of the day
	 * @return the low price of the day
	 */
	public double getLow() {
		return this.l;
	}

	/**
	 * Gets open price of the day
	 * @return the open price of the day
	 */
	public double getOpen() {
		return this.o;
	}

	/**
	 * Gets previous close price
	 * @return the previous close price
	 */
	public double getPreviousClose() {
		return this.pc;
	}

	/**
	 * Gets timestamp
	 * @return the timestamp
	 */
	public long getTime() {
		return this.t;
	}
}
