package me.loogeh.Hype.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import me.loogeh.Hype.Hype;

public class utilTime {
	public static Hype plugin;
	
	public static String timeStr() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(cal.getTime());
		
	}
	
	public static double convert(long time, TimeUnit unit, int decPoint) {
		if(unit == TimeUnit.BEST) {
			if(time < 60000L) unit = TimeUnit.SECONDS;
			else if(time < 3600000L) unit = TimeUnit.MINUTES;
			else if(time < 86400000L) unit = TimeUnit.HOURS;
			else unit = TimeUnit.DAYS;
		}
		if(unit == TimeUnit.SECONDS) return utilMath.round(time / 1000.0D, decPoint);
		if(unit == TimeUnit.MINUTES) return utilMath.round(time / 60000.0D, decPoint);
		if(unit == TimeUnit.HOURS) return utilMath.round(time / 3600000.0D, decPoint);
		if(unit == TimeUnit.DAYS) return utilMath.round(time / 86400000.0D, decPoint);
		return utilMath.round(time, decPoint);
	}
	
	public static String convertString(long time, TimeUnit unit, int decPoint) {
		if(unit == TimeUnit.BEST) {
			if(time < 60000L) unit = TimeUnit.SECONDS;
			else if(time < 3600000L) unit = TimeUnit.MINUTES;
			else if(time < 86400000L) unit = TimeUnit.HOURS;
			else unit = TimeUnit.DAYS;
		}
		if(unit == TimeUnit.SECONDS) return utilMath.round(time / 1000.0D, decPoint) + " Seconds";
		if(unit == TimeUnit.MINUTES) return utilMath.round(time / 60000.0D, decPoint) + " Minutes";
		if(unit == TimeUnit.HOURS) return utilMath.round(time / 3600000.0D, decPoint) + " Hours";
		if(unit == TimeUnit.DAYS) return utilMath.round(time / 86400000.0D, decPoint) + " Days";
		return utilMath.round(time, decPoint) + "Milliseconds";
	}
	
	public static String millisToReadable(long millis, boolean abbreviate) {
		int x = (int) (millis / 1000);
		int seconds = x % 60;
		x /= 60;
		int minutes = x % 60;
		x /= 60;
		int hours = x % 24;
		x /= 24;
		long days = x % 7;
		x /= 7;
		long weeks = x;
		if(!abbreviate) return weeks + " weeks " + days + " days " + hours + " hours " + minutes + " minutes " + seconds + " seconds";
		else return weeks + "w-" + days + "d-" + hours + "h-" + minutes + "m-" + seconds + "s";
	}
	
	public static enum TimeUnit {
		BEST(60),
		DAYS(86400000.0),
		HOURS(3600000.0),
		MINUTES(60000),
		SECONDS(1000),
		MILLISECONDS(1);
		
		private double value;
		
		TimeUnit(double value) {
			this.value = value;
		}
		
		public double getValue() {
			return value;
		}
		
		public double toMillis(int amount) {
			return amount * value;
		}
	}

}
