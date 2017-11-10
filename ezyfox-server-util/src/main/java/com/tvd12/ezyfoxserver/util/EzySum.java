package com.tvd12.ezyfoxserver.util;

import java.util.function.Function;

public final class EzySum {

	private EzySum() {
	}
	
	public static int sumBytes(byte[] numbers) {
		int total = 0;
		for(int i = 0 ; i < numbers.length ; i++)
			total += numbers[i];
		return total;
	}
	
	public static double sumDoubles(double[] numbers) {
		double total = 0;
		for(int i = 0 ; i < numbers.length ; i++)
			total += numbers[i];
		return total;
	}
	
	public static float sumFloats(float[] numbers) {
		float total = 0;
		for(int i = 0 ; i < numbers.length ; i++)
			total += numbers[i];
		return total;
	}
	
	public static int sumInts(int[] numbers) {
		int total = 0;
		for(int i = 0 ; i < numbers.length ; i++)
			total += numbers[i];
		return total;
	}
	
	public static long sumLongs(long[] numbers) {
		long total = 0;
		for(int i = 0 ; i < numbers.length ; i++)
			total += numbers[i];
		return total;
	}
	
	public static int sumShorts(short[] numbers) {
		int total = 0;
		for(int i = 0 ; i < numbers.length ; i++)
			total += numbers[i];
		return total;
	}
	
	public static int sumBytes(Iterable<Byte> numbers) {
		int total = 0;
		for(Byte number : numbers)
			total += number;
		return total;
	}
	
	public static double sumDoubles(Iterable<Double> numbers) {
		double total = 0;
		for(Double number : numbers)
			total += number;
		return total;
	}
	
	public static float sumFloats(Iterable<Float> numbers) {
		float total = 0;
		for(Float number : numbers)
			total += number;
		return total;
	}
	
	public static int sumInts(Iterable<Integer> numbers) {
		int total = 0;
		for(Integer number : numbers)
			total += number;
		return total;
	}
	
	public static long sumLongs(Iterable<Long> numbers) {
		long total = 0;
		for(Long number : numbers)
			total += number;
		return total;
	}
	
	public static int sumShorts(Iterable<Short> numbers) {
		int total = 0;
		for(Short number : numbers)
			total += number;
		return total;
	}
	
	public static <T> int sumToInt(T[] array, Function<T, Integer> func) {
		int total = 0;
		for(T t : array)
			total += func.apply(t);
		return total;
	}
	
	public static <T> int sumToInt(Iterable<T> iterable, Function<T, Integer> func) {
		int total = 0;
		for(T t : iterable)
			total += func.apply(t);
		return total;
	}
	
	public static <T> long sumToLong(T[] array, Function<T, Long> func) {
		long total = 0;
		for(T t : array)
			total += func.apply(t);
		return total;
	}
	
	public static <T> long sumToLong(Iterable<T> iterable, Function<T, Long> func) {
		long total = 0;
		for(T t : iterable)
			total += func.apply(t);
		return total;
	}
	
}
