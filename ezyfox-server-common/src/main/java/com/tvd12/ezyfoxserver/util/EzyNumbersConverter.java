package com.tvd12.ezyfoxserver.util;

import java.util.Collection;

import com.tvd12.ezyfoxserver.function.EzyNewArray;
import com.tvd12.ezyfoxserver.function.EzyNumber;

@SuppressWarnings({"rawtypes"})
public abstract class EzyNumbersConverter {

	private EzyNumbersConverter() {
	}
	
	// primitive
	public static byte[] numbersToPrimitiveBytes(Collection coll) {
		int index = 0;
		byte[] answer = new byte[coll.size()];
		for(Object obj : coll)
			answer[index ++] = ((Number)obj).byteValue();
		return answer;
	}
	
	public static char[] numbersToPrimitiveChars(Collection coll) {
		int index = 0;
		char[] answer = new char[coll.size()];
		for(Object obj : coll)
			answer[index ++] = (char)((Number)obj).byteValue();
		return answer;
	}
	
	public static double[] numbersToPrimitiveDoubles(Collection coll) {
		int index = 0;
		double[] answer = new double[coll.size()];
		for(Object obj : coll)
			answer[index ++] = ((Number)obj).doubleValue();
		return answer;
	}
	
	public static float[] numbersToPrimitiveFloats(Collection coll) {
		int index = 0;
		float[] answer = new float[coll.size()];
		for(Object obj : coll)
			answer[index ++] = ((Number)obj).floatValue();
		return answer;
	}
	
	public static int[] numbersToPrimitiveInts(Collection coll) {
		int index = 0;
		int[] answer = new int[coll.size()];
		for(Object obj : coll)
			answer[index ++] = ((Number)obj).intValue();
		return answer;
	}
	
	public static long[] numbersToPrimitiveLongs(Collection coll) {
		int index = 0;
		long[] answer = new long[coll.size()];
		for(Object obj : coll)
			answer[index ++] = ((Number)obj).longValue();
		return answer;
	}
	
	public static short[] numbersToPrimitiveShorts(Collection coll) {
		int index = 0;
		short[] answer = new short[coll.size()];
		for(Object obj : coll)
			answer[index ++] = ((Number)obj).shortValue();
		return answer;
	}
	
	// wrapper
	public static <T> T[] numbersToWrapperNumbers(
			Collection<T> coll, EzyNewArray<T> newer, EzyNumber<T> converter) {
		return coll.stream()
				.map((num)->convertNumber(num, converter))
				.toArray((size) -> newer.apply(size));
	}
	
	public static Byte[] numbersToWrapperBytes(Collection coll) {
		return numbersToWrapperNumbers(coll, Byte[]::new, (num) -> numberToByte(num));
	}
	
	public static Character[] numbersToWrapperChars(Collection coll) {
		return numbersToWrapperNumbers(coll, Character[]::new, (num) -> numberToChar(num));
	}
	
	public static Double[] numbersToWrapperDoubles(Collection coll) {
		return numbersToWrapperNumbers(coll, Double[]::new, (num) -> numberToDouble(num));
	}
	
	public static Float[] numbersToWrapperFloats(Collection coll) {
		return numbersToWrapperNumbers(coll, Float[]::new, (num) -> numberToFloat(num));
	}
	
	public static Integer[] numbersToWrapperInts(Collection coll) {
		return numbersToWrapperNumbers(coll, Integer[]::new, (num) -> numberToInt(num));
	}
	
	public static Long[] numbersToWrapperLongs(Collection coll) {
		return numbersToWrapperNumbers(coll, Long[]::new, (num) -> numberToLong(num));
	}
	
	public static Short[] numbersToWrapperShorts(Collection coll) {
		return numbersToWrapperNumbers(coll, Short[]::new, (num) -> numberToShort(num));
	}
	
	public static Byte numberToByte(Number number) {
		return convertNumber(number, (num) -> num.byteValue());
	}

	public static Character numberToChar(Number number) {
		return convertNumber(number, (num) -> (char)num.byteValue());
	}

	public static Double numberToDouble(Number number) {
		return convertNumber(number, (num) -> num.doubleValue());
	}

	public static Float numberToFloat(Number number) {
		return convertNumber(number, (num) -> num.floatValue());
	}

	public static Integer numberToInt(Number number) {
		return convertNumber(number, (num) -> num.intValue());
	}

	public static Long numberToLong(Number number) {
		return convertNumber(number, (num) -> num.longValue());
	}

	public static Short numberToShort(Number number) {
		return convertNumber(number, (num) -> num.shortValue());
	}
	
	public static <T> T convertNumber(Object number, EzyNumber<T> converter) {
		return converter.apply((Number)number);
	}
	
	public static <T> T convertNumber(Number number, EzyNumber<T> converter) {
		return converter.apply(number);
	}
	
}
