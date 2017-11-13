package com.tvd12.ezyfoxserver.io;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

import org.apache.commons.lang3.time.FastDateFormat;
import org.joda.time.DateTime;

public final class EzyDates {

	private static final DateTimeFormatter DATE_TIME_FORMATTER
			= getDateTimeFormatter(getPattern());
	
	private EzyDates() {
	}
	
	// ============= java 8 ============
	public static String format(TemporalAccessor temporal) {
		return format(temporal, getDateTimeFormatter());
	}
	
	public static String format(TemporalAccessor temporal, String pattern) {
		return format(temporal, getDateTimeFormatter(pattern));
	}
	
	public static String format(TemporalAccessor temporal, DateTimeFormatter formatter) {
		return temporal == null ? null : formatter.format(temporal);
	}
	
	public static LocalDate parseDate(String source) {
		return parseDate(source, getDateTimeFormatter());
	}
	
	public static LocalDate parseDate(String source, String pattern) {
		return parseDate(source, getDateTimeFormatter(pattern));
	}
	
	public static LocalDate parseDate(String source, DateTimeFormatter formatter) {
		return LocalDate.parse(source, formatter);
	}
	
	public static LocalDateTime parseDateTime(String source) {
		return parseDateTime(source, getDateTimeFormatter());
	}
	
	public static LocalDateTime parseDateTime(String source, String pattern) {
		return parseDateTime(source, getDateTimeFormatter(pattern));
	}
	
	public static LocalDateTime parseDateTime(String source, DateTimeFormatter formatter) {
		return LocalDateTime.parse(source, formatter);
	}
	
	public static DateTimeFormatter getDateTimeFormatter() {
		return DATE_TIME_FORMATTER;
	}
	
	public static DateTimeFormatter getDateTimeFormatter(String pattern) {
		return DateTimeFormatter.ofPattern(pattern);
	}
	//=================================
	
	
	// =================== java 7 ===============
	public static String format(long millis) {
		return format(millis, getPattern());
	}
	
	public static String format(Date date) {
		return format(date, getPattern());
	}
	
	public static Date parse(String source) {
		return parse(source, getPattern());
	}
	
	public static String format(long millis, String pattern) {
		return FastDateFormat.getInstance(pattern).format(millis);
	}
	
	public static String format(Date date, String pattern) {
		return date == null ? null : FastDateFormat.getInstance(pattern).format(date);
	}
	
	public static Date parse(String source, String pattern) {
		try {
			return FastDateFormat.getInstance(pattern).parse(source);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public static String getPattern() {
		return "yyyy-MM-dd'T'HH:mm:ss:SSS";
	}
	
	// =========================================
	public static boolean between(Date date, Date before, Date after) {
		long time = date.getTime();
		return time >= before.getTime() && time <= after.getTime();
	}
	
	public static int formatAsInteger(Date date) {
		DateTime dateTime = new DateTime(date.getTime());
		int year = dateTime.getYear();
		int month = dateTime.getMonthOfYear();
		int day = dateTime.getDayOfMonth();
		return year * 10000 + month * 100 + day;
	}
	
}
