package stocker.model.general;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * The time interval determines the resolution and the time window of the
 * considered stock data
 * 
 * @author Christoph Kaplan
 */
public enum TimeInterval {
	Min1("1"), Min5("5"), Min15("15"), Min30("30"), Min60("60"), Day("D"), Week("W"), Month("M");
//10min , 4h
	private final String resolutionCode;
	private long timeFrom;
	private long timeTo;

	/**
	 * {@code TimeInterval} constructor
	 * 
	 * @param resolutionCode the associated resolution code
	 */
	TimeInterval(String resolutionCode) {
		this.resolutionCode = resolutionCode;
	}

	/**
	 * Calculates the needed time window (from - to) in order to have enough data available
	 * @param dataAmount the amount of data 
	 */
	public void prepareTimeWindow(int dataAmount) {
		
		//get timestamp of "now"
		this.timeTo = getCurrentTimeStamp();
		
		//get timestamp of dataAmount times the current interval, backwards in time.
		this.timeFrom = addResToTimestamp(this.timeTo, dataAmount * (-1));
				
		if (timeFrom == timeTo) {
			System.err.println("prepareTimeWindow() problem");
		}
	}

	/**
	 * Gets the resolution code
	 * 
	 * @return the resolution code
	 */
	public String getResolutionCode() {
		return this.resolutionCode;
	}

	/**
	 * Calculates a distant timestamp by a given timestamp and a multiplicator in
	 * regards this this interval value.
	 * 
	 * @param startTimestamp given timestamp
	 * @param times          the multiplicator
	 * @return result timestamp
	 */
	public long addResToTimestamp(long startTimestamp, int times) {

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(startTimestamp);

		if (this == TimeInterval.Min1) {
			cal.add(Calendar.MINUTE, 1 * times);
		}
		if (this == TimeInterval.Min5) {
			cal.add(Calendar.MINUTE, 5 * times);
		}
		if (this == TimeInterval.Min15) {
			cal.add(Calendar.MINUTE, 15 * times);
		}
		if (this == TimeInterval.Min30) {
			cal.add(Calendar.MINUTE, 30 * times);
		}
		if (this == TimeInterval.Min60) {
			cal.add(Calendar.MINUTE, 60 * times);
		}
		if (this == TimeInterval.Day) {
			cal.add(Calendar.DAY_OF_MONTH, 1 * times);
		}
		if (this == TimeInterval.Week) {
			cal.add(Calendar.WEEK_OF_YEAR, 1 * times);
		}
		if (this == TimeInterval.Month) {
			cal.add(Calendar.MONTH, 1 * times);
		}
		return cal.getTime().getTime();
	}

	/**
	 * Gets a {@code Date} object by a given timestamp
	 * 
	 * @param timestamp the given timestamp
	 * @return the {@code Date} object
	 */
	public Date getDate(long timestamp) {
		return new Date(timestamp);
	}

	/**
	 * Formats a given timestamp in a readable {@code} String.
	 * 
	 * @param timestamp the given timestamp
	 * @return the readable {@code} String
	 */
	public String timestampToString(long timestamp) {
		Date date = getDate(timestamp);
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		if ((this == TimeInterval.Min1) || (this == TimeInterval.Min5) || (this == TimeInterval.Min15)
				|| (this == TimeInterval.Min30) || (this == TimeInterval.Min60)) {
			formatter = new SimpleDateFormat("HH:mm");
		}
		if (this == TimeInterval.Day || this == TimeInterval.Week || this == TimeInterval.Month) {
			formatter = new SimpleDateFormat("dd/MM");
		}

		return formatter.format(date);
	}

	/**
	 * Gets a timestamp from given day,month and year
	 * 
	 * @param day   the day
	 * @param month the month
	 * @param year  the year
	 * @return the calculated timestamp
	 */
	public long getTimeStamp(int day, int month, int year) {
		month--; // month counts from 0 ¯\_(ツ)_/¯
		Calendar cal = new GregorianCalendar(year, month, day, 0, 0);
		Date date = cal.getTime();
		return date.getTime();
	}

	/**
	 * Gets the current timestamp
	 * 
	 * @return the current timestamp
	 */
	public long getCurrentTimeStamp() {
		Date date = new Date();
		return date.getTime();
	}

	/**
	 * Gets the timestamp from a {@code Date} object
	 * 
	 * @param date the {@code Date} object
	 * @return the timestamp
	 */
	public long getTimeStampFromDate(Date date) {
		return date.getTime();
	}

	/**
	 * Gets a {@code String} about the current time window
	 * 
	 * @return {@code String} about the current time window
	 */
	public String printFromTo() {
		return ("from:" + getFromDate() + " to:" + getToDate());
	}

	/**
	 * Gets the current from.
	 * 
	 * @return the current from
	 */
	public long getFrom() {
		return this.timeFrom;
	}

	/**
	 * Gets the current to.
	 * 
	 * @return the current to
	 */
	public long getTo() {
		return this.timeTo;
	}

	/**
	 * Gets a {@code Date} object with the current from
	 * 
	 * @return the {@code Date} object with the current from
	 */
	public Date getFromDate() {
		return getDate(timeFrom);
	}

	/**
	 * Gets a {@code Date} object with the current to
	 * 
	 * @return the {@code Date} object with the current to
	 */
	public Date getToDate() {
		return getDate(timeTo);
	}

}