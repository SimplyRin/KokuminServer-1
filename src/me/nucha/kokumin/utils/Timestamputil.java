package me.nucha.kokumin.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Timestamputil {

	public static String formattedTimestamp(Timestamp timestamp, String timeFormat) {
		return new SimpleDateFormat(timeFormat).format(timestamp);
	}

	public static Timestamp current() {
		return new Timestamp(System.currentTimeMillis());
	}

}
