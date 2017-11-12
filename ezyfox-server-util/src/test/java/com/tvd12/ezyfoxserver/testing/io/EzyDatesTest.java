package com.tvd12.ezyfoxserver.testing.io;

import static org.testng.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.io.EzyDates;
import com.tvd12.test.base.BaseTest;

public class EzyDatesTest extends BaseTest {

	@Test
	public void test() {
		assertEquals(EzyDates.format((Date)null), null);
	}
	
	@Test
	public void test1() {
		EzyDates.format(LocalDateTime.of(2017, 05, 30, 12, 34, 56, 0));
		assertEquals(EzyDates.format(LocalDateTime.of(
				2017, 05, 30, 12, 34, 56, 0),
				"yyyy-MM-dd'T'HH:mm:ss"), 
				"2017-05-30T12:34:56");
		assertEquals(EzyDates.format(LocalDateTime.of(
				2017, 05, 30, 12, 34, 56, 0),
				"yyyy-MM-dd'T'HH:mm:ss"), 
				"2017-05-30T12:34:56");
		assertEquals(EzyDates.format((TemporalAccessor)null), null);
		assertEquals(EzyDates.parseDate("2017-05-30T12:34:56:000"), 
				LocalDate.of(2017, 05, 30));
		assertEquals(EzyDates.parseDate("2017-05-30", "yyyy-MM-dd"), 
				LocalDate.of(2017, 05, 30));
		assertEquals(EzyDates.parseDateTime("2017-05-30T12:34:56:000"), 
				LocalDateTime.of(2017, 05, 30, 12, 34, 56, 0));
		assertEquals(EzyDates.parseDateTime("2017-05-30T12:34:56", "yyyy-MM-dd'T'HH:mm:ss"), 
				LocalDateTime.of(2017, 05, 30, 12, 34, 56, 0));
		Date now = new Date();
		EzyDates.format(now.getTime()).equals(now);
	}
	
	@Test
	public void test2() {
		assertEquals(EzyDates.format((Date)null), null);
		assertEquals(EzyDates.format(new Date()).length() > 0, true);
		assertEquals(EzyDates.parse("2017-05-30T12:34:56:000").getTime() > 0, true);
	}
	
	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void test3() {
		EzyDates.parse("abcc");
	}
	
	@Test
	public void test4() {
		Date now = new Date();
		long nowTime = now.getTime();
		Date before = new Date(nowTime - 10000);
		Date after = new Date(nowTime + 10000);
		assert EzyDates.between(now, before, after);
		
		now = new Date(nowTime - 20000);
		assert !EzyDates.between(now, before, after);
		
		now = new Date(nowTime + 20000);
		assert !EzyDates.between(now, before, after);
	}
	
	@Test
	public void test5() {
		int value = EzyDates.formatAsInteger(new Date());
		System.out.println(value);
	}
	
	@Override
	public Class<?> getTestClass() {
		return EzyDates.class;
	}
	
}
