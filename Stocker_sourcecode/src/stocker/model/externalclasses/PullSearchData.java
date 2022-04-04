package stocker.model.externalclasses;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

/**
 * {@code PullSearchData} class, temporarily contains the search data, when received from provider.
 * Structure given by the funnhub.io data provider.
 * 
 * @author Christoph Kaplan
 */
public class PullSearchData{
	/**
	 * Inner class, {@code Entry} that stores the search data, corresponds with an entry of the JSON tag "result:"
	 * @author Christoph Kaplan
	 *
	 */
	public class Entry{
		/** description: entry */
		@SerializedName("description") String description;
		
		/** displaySymbol: entry */
		@SerializedName("displaySymbol") String displaySymbol;
		
		/** symbol: entry used to identify the {@code StockData}*/
		@SerializedName("symbol") String symbol;
		
		/** type: entry */
		@SerializedName("type") String type;
				
		/**
		 * Gets description.
		 * @return the description
		 */
		public String getDescription() {
			return description;
		}
		/**
		 * Gets display symbol.
		 * @return the display symbol
		 */
		public String getDisplaySymbol() {
			return displaySymbol;
		}
		
		/**
		 * Gets symbol.
		 * @return the symbol
		 */
		public String getSymbol() {
			return symbol;
		}
		
		/** 
		 * Gets type
		 * @return the type;
		 */
		public String getType() {
			return type;
		}
		
		/**
		 * Helper method, prints the entry-content for debugging purposes
		 * @return content to print
		 */
		public String Print() {
			String s = "";
			s += " description: "+ description ;
			s += " displaySymbol: "+ displaySymbol;	
			s += " symbol: "+ symbol;	
			s += " type: "+ type;	
			return s;
		}
		
	}
	
	/**
	 * count of results
	 */
	@SerializedName("count")
	int count;
	
	/**
	 * result array of {@code Entry}
	 */
	@SerializedName("result")
	Entry[] result;
	
	/**
	 * Gets the result array.
	 * @return the result array
	 */
	public Entry[] getResults() {
		return this.result;
	}
	
	/**
	 * Gets the count.
	 * @return the count
	 */
	public int getCount() {
		return this.count;
	}

	/**
	 * Helper method, prints the content for debugging purposes
	 */
	public void Print() {
		System.out.println("count: "+ count);
		System.out.println("result: ");	
		for(Entry e: result) {
			System.out.println("- " + e.Print());	
		}
	}
	
	/**
	 * Gets the list of all symbols in the result array
	 * @return the list of all symbols in the result array
	 */
	public ArrayList<String> getSymbolList(){
		ArrayList<String> lst = new ArrayList<String>();
		for (Entry e : getResults()) {	
			lst.add(e.getSymbol());
		}
		return lst;
	}
	
	/**
	 * Gets a specific {@code Entry} object by symbol.
	 * @param symbol the symbol whose presence is checked
	 * @return the specific {@code Entry} object
	 * @throws Exception thrown when symbol not present
	 */
	public Entry getEntryBySymbol(String symbol) throws Exception{
		for (Entry e : getResults()) {	
			if(e.getSymbol().equals(symbol)) return e;
		}
		throw new Exception("findEntryBySymbol(): cant find symbol in searchData: "+ symbol);
	}
}