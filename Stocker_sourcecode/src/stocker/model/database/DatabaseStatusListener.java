package stocker.model.database;

/**
 * Database status listener, used to notify the listener (i.e. the
 * {@code NetworkClient}) for data input or subscription
 * 
 * @author Christoph Kaplan
 */
public interface DatabaseStatusListener {

	/**
	 * Notify listeners of the database status.
	 * 
	 * @param status database status
	 */
	public void onStatusChange(String status);

	/**
	 * Notify listeners when alarm triggered
	 * 
	 * @param alarmWrapper the alarm wrapper
	 * @param value        the alarm value
	 */
	public void onAlarmTriggered(AlarmWrapper alarmWrapper, double value);
}
