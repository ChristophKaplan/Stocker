package stocker.model.externalclasses;

import com.google.gson.annotations.SerializedName;

/**
 * {@code PushData} class, temporarily contains the real-time data, when
 * received from provider. Structure given by the funnhub.io data provider.
 * 
 * @author Christoph Kaplan
 */
public class PushData {

	/**
	 * Inner class, {@code Entry} that stores the real-time data, corresponds with an entry of the JSON tag "data:"
	 * @author Christoph Kaplan
	 */
	public class Entry {
		/**
		 * condition array, (trading type)
		 */
		@SerializedName("c")
		String[] c;
		/**
		 * price
		 */
		@SerializedName("p")
		double p;
		/**
		 * symbol
		 */
		@SerializedName("s")
		String s;
		/**
		 * Unix timestamp in milliseconds 
		 */
		@SerializedName("t")
		long t; 
		/**
		 * volume
		 */
		@SerializedName("v")
		double v;

		
		/**
		 * Gets price.
		 * @return the price
		 */
		public double getPrice() {
			return this.p;
		}

		/**
		 * Gets symbol.
		 * @return the symbol
		 */
		public String getSymbol() {
			return this.s;
		}

		/**
		 * Gets timestamp.
		 * @return the timestamp
		 */
		public long getTime() {
			return this.t;
		}

		/**
		 * Gets volume.
		 * @return the volume
		 */
		public double getVolume() {
			return this.v;
		}

		/**
		 * Helper method, prints the entry-content for debugging purposes
		 * @return the content
		 */
		public String Print() {
			String st = "";
			st += "c:[";
			for (String s2 : c) {
				st += s2 + ",";
			}
			st += "]";

			st += "p:" + p;
			st += "s:" + s;
			st += "t:" + t;
			st += "v:" + v;
			return st;
		}
	}

	/**
	 * array of the {@code Entry} class, corresponds with the JSON tag "data:"
	 */
	@SerializedName("data")
	private Entry[] data;
	
	/**
	 * type, is i.e. "ping", "error" or "trade"
	 */
	@SerializedName("type")
	String type;

	/**
	 * Helper method, prints the entry-content for debugging purposes
	 */
	public void Print() {
		if (!isPing() && !isError()) {
			for (Entry e : getData()) {
				System.out.println("- " + e.Print());
			}
		}
		System.out.println("type:" + type);
	}

	/**
	 * Checks if received real-time data is of type "error"
	 * @return true for "error", otherwise false
	 */
	public boolean isError() {
		if (type.equals("error")) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if received real-time data is of type "ping"
	 * @return true for "ping", otherwise false
	 */
	public boolean isPing() {
		if (type.equals("ping")) {
			return true;
		}
		return false;
	}
	/**
	 * Gets the data array.
	 * @return the data array
	 */
	public Entry[] getData() {
		return data;
	}

}