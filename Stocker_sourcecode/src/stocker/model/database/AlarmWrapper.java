package stocker.model.database;

import java.util.ArrayList;

/**
 * The alarm wrapper stores a list of alarms of type {@code double} associated with a symbol
 * @author Christoph Kaplan
 *
 */
public class AlarmWrapper implements Comparable<AlarmWrapper>{
	
	private String symbol;
	private ArrayList<Double> alarms;
	
	/**
	 * {@code AlarmWrapper} constructor
	 * @param symbol the associated symbol
	 */
	public AlarmWrapper(String symbol) {
		this.symbol = symbol;
		this.alarms = new ArrayList<Double> ();
	}
	
	/**
	 * {@code AlarmWrapper} constructor
	 * @param symbol the associated symbol
	 * @param alarms the alarm list
	 */
	public AlarmWrapper(String symbol,ArrayList<Double> alarms) {
		this.symbol = symbol;
		this.alarms = alarms;
	}
	
	/** 
	 * Gets the symbol
	 * @return the symbol
	 */
	public String getSymbol() {
		return this.symbol;
	}
	
	/**
	 * Gets the alarm list
	 * @return the alarm list
	 */
	public ArrayList<Double> getAlarms(){
		return this.alarms;
	}
	
	/**
	 * Adds an alarm to the list
	 * @param value the alarm value
	 */
	public void addAlarm(double value) {
		if(!this.alarms.contains(value))	this.alarms.add(value);
	}
	
	/**
	 * Removes an alarm from list
	 * @param value the alarm value
	 */
	public void removeAlarm(double value) {
		this.alarms.remove(value);
	}
	
	/**
	 * Clears the alarm list
	 */
	public void clearAlarms() {
		this.alarms.clear();
	}
	
	/**
	 * Checks if an alarm lies between a current and a previous price.
	 * @param currentPrice the current price
	 * @param previousPrice the previous price
	 * @return returns the alarm if it lies between a current and a previous price
	 * @throws Exception use of "exception" when the alarm is not triggered
	 */
	public double isTriggered(double currentPrice,double previousPrice) throws Exception {
		for(double curAlarm : alarms) {
			if(previousPrice <= currentPrice) {
				if(previousPrice <= curAlarm && curAlarm <= currentPrice) {
					return curAlarm;
				}
			}
			else {
				if(previousPrice >= curAlarm && curAlarm >= currentPrice) {
					return curAlarm;
				}
			}
		}
		throw new Exception("alarm not triggered");
	}
	
	/**
	 * Overrides compareTo()
	 */
	@Override
	public int compareTo(AlarmWrapper o) {
		return this.symbol.compareTo(o.symbol);
	}
}
