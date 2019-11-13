package me.nucha.kokumin.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.time.DurationFormatUtils;

public class TimeUtil {

	public static long untilTime(Date till) {
		Date date = new Date();
		long d1 = date.getTime();
		long d2 = till.getTime();
		long l1 = d2 - d1;
		return l1;
	}

	public static String formattedTimestamp(Timestamp timestamp, String timeFormat) {
		return new SimpleDateFormat(timeFormat).format(timestamp);
	}

	public static Timestamp current() {
		return new Timestamp(System.currentTimeMillis());
	}

	public static String toDiffTime(long startMillis, long endMillis, String format) {
		String diffTime = DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
		return diffTime;
	}

	public static String toDiffTime(Date startTime, Date endTime, String format) {
		String diffTime = DurationFormatUtils.formatPeriod(startTime.getTime(), endTime.getTime(), format);
		return diffTime;
	}

}
