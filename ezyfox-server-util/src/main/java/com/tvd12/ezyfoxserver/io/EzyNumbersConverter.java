package com.tvd12.ezyfoxserver.io;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import com.tvd12.ezyfoxserver.function.EzyNewArray;
import com.tvd12.ezyfoxserver.function.EzyNumber;

@SuppressWarnings({"rawtypes"})
public final class EzyNumbersConverter {

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
			answer[index ++] = objectToChar(obj);
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
	public static <I,O> O[] objectsToWrapperNumbers(
			Collection<I> coll, EzyNewArray<O> newer, Function<I, O> mapper) {
		return coll.stream().map(mapper).toArray((size) -> newer.apply(size));
	}
	
	public static <O> O[] numbersToWrapperNumbers(
			Collection<? extends Number> coll, EzyNewArray<O> applier, EzyNumber<O> converter) {
		return objectsToWrapperNumbers(coll, applier, (num) -> convertNumber(num, converter));
	}
	
	public static <I,O> O[] objectsToWrapperNumbers(
			I[] array, EzyNewArray<O> newer, Function<I, O> mapper) {
		return Arrays.stream(array).map(mapper).toArray((size) -> newer.apply(size));
	}
	
	public static <O> O[] numbersToWrapperNumbers(
			Number[] numbers, EzyNewArray<O> applier, EzyNumber<O> converter) {
		return objectsToWrapperNumbers(numbers, applier, (num) -> convertNumber(num, converter));
	}
	
	public static Byte[] numbersToWrapperBytes(Collection<? extends Number> coll) {
		return numbersToWrapperNumbers(coll, Byte[]::new, (num) -> numberToByte(num));
	}
	
	public static Character[] numbersToWrapperChars(Collection coll) {
		int index = 0;
		Character[] answer = new Character[coll.size()];
		for(Object obj : coll)
			answer[index ++] = objectToChar(obj);
		return answer;
	}
	
	public static Double[] numbersToWrapperDoubles(Collection<? extends Number> coll) {
		return numbersToWrapperNumbers(coll, Double[]::new, (num) -> numberToDouble(num));
	}
	
	public static Float[] numbersToWrapperFloats(Collection<? extends Number> coll) {
		return numbersToWrapperNumbers(coll, Float[]::new, (num) -> numberToFloat(num));
	}
	
	public static Integer[] numbersToWrapperInts(Collection<? extends Number> coll) {
		return numbersToWrapperNumbers(coll, Integer[]::new, (num) -> numberToInt(num));
	}
	
	public static Long[] numbersToWrapperLongs(Collection<? extends Number> coll) {
		return numbersToWrapperNumbers(coll, Long[]::new, (num) -> numberToLong(num));
	}
	
	public static Short[] numbersToWrapperShorts(Collection<? extends Number> coll) {
		return numbersToWrapperNumbers(coll, Short[]::new, (num) -> numberToShort(num));
	}
	
	//=================================================
	public static Byte[] numbersToWrapperBytes(Number[] array) {
		return numbersToWrapperNumbers(array, Byte[]::new, (num) -> numberToByte(num));
	}
	
	public static Character[] numbersToWrapperChars(Object[] value) {
		Character[] answer = new Character[value.length];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = objectToChar(value[i]);
		return answer;
	}
	
	public static Double[] numbersToWrapperDoubles(Number[] array) {
		return numbersToWrapperNumbers(array, Double[]::new, (num) -> numberToDouble(num));
	}
	
	public static Float[] numbersToWrapperFloats(Number[] array) {
		return numbersToWrapperNumbers(array, Float[]::new, (num) -> numberToFloat(num));
	}
	
	public static Integer[] numbersToWrapperInts(Number[] array) {
		return numbersToWrapperNumbers(array, Integer[]::new, (num) -> numberToInt(num));
	}
	
	public static Long[] numbersToWrapperLongs(Number[] array) {
		return numbersToWrapperNumbers(array, Long[]::new, (num) -> numberToLong(num));
	}
	
	public static Short[] numbersToWrapperShorts(Number[] array) {
		return numbersToWrapperNumbers(array, Short[]::new, (num) -> numberToShort(num));
	}
	//=================================================
	
	public static Byte numberToByte(Number number) {
		return convertNumber(number, (num) -> num.byteValue());
	}

	public static Character numberToChar(Number number) {
		return convertNumber(number, (num) -> (char)num.byteValue());
	}
	
	public static Character objectToChar(Object object) {
		return (object instanceof Number) 
				? numberToChar((Number)object) : (Character) object;
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
	
	//================ wrapper to primitive array ===========
	public static boolean[] boolArrayWrapperToPrimitive(Boolean[] value) {
		boolean[] answer = new boolean[value.length];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = value[i];
		return answer;
	}
	
	public static byte[] numbersToPrimitiveBytes(Number[] value) {
		byte[] answer = new byte[value.length];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = value[i].byteValue();
		return answer;
	}
	
	public static char[] numbersToPrimitiveChars(Object[] value) {
		char[] answer = new char[value.length];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = objectToChar(value[i]);
		return answer;
	}
	
	public static double[] numbersToPrimitiveDoubles(Number[] value) {
		double[] answer = new double[value.length];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = value[i].doubleValue();
		return answer;
	}
	
	public static float[] numbersToPrimitiveFloats(Number[] value) {
		float[] answer = new float[value.length];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = value[i].floatValue();
		return answer;
	}
	
	public static int[] numbersToPrimitiveInts(Number[] value) {
		int[] answer = new int[value.length];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = value[i].intValue();
		return answer;
	}
	
	public static long[] numbersToPrimitiveLongs(Number[] value) {
		long[] answer = new long[value.length];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = value[i].longValue();
		return answer;
	}
	
	public static short[] numbersToPrimitiveShorts(Number[] value) {
		short[] answer = new short[value.length];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = value[i].shortValue();
		return answer;
	}
	
	//====================== primitive to wrapper array =========
	public static Boolean[] boolArrayPrimitiveToWrapper(boolean[] value) {
		Boolean[] answer = new Boolean[value.length];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = value[i];
		return answer;
	}
	
	public static Byte[] byteArrayPrimitiveToWrapper(byte[] value) {
		Byte[] answer = new Byte[value.length];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = value[i];
		return answer;
	}
	
	public static Character[] charArrayPrimitiveToWrapper(char[] value) {
		Character[] answer = new Character[value.length];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = value[i];
		return answer;
	}
	
	public static Double[] doubleArrayPrimitiveToWrapper(double[] value) {
		Double[] answer = new Double[value.length];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = value[i];
		return answer;
	}
	
	public static Float[] floatArrayPrimitiveToWrapper(float[] value) {
		Float[] answer = new Float[value.length];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = value[i];
		return answer;
	}
	
	public static Integer[] intArrayPrimitiveToWrapper(int[] value) {
		Integer[] answer = new Integer[value.length];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = value[i];
		return answer;
	}
	
	public static Long[] longArrayPrimitiveToWrapper(long[] value) {
		Long[] answer = new Long[value.length];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = value[i];
		return answer;
	}
	
	public static Short[] shortArrayPrimitiveToWrapper(short[] value) {
		Short[] answer = new Short[value.length];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = value[i];
		return answer;
	}
	
	//====================== two-dimensions wrapper array to primitive array =========
	public static boolean[][] boolArraysWrapperToPrimitive(Boolean[][] value) {
		boolean[][] answer = new boolean[value.length][];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = boolArrayWrapperToPrimitive(value[i]);
		return answer;
	}
	
	public static byte[][] numbersToPrimitiveByteArrays(Number[][] value) {
		byte[][] answer = new byte[value.length][];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = numbersToPrimitiveBytes(value[i]);
		return answer;
	}
	
	public static char[][] numbersToPrimitiveCharArrays(Object[][] value) {
		char[][] answer = new char[value.length][];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = numbersToPrimitiveChars(value[i]);
		return answer;
	}
	
	public static double[][] numbersToPrimitiveDoubleArrays(Number[][] value) {
		double[][] answer = new double[value.length][];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = numbersToPrimitiveDoubles(value[i]);
		return answer;
	}
	
	public static float[][] numbersToPrimitiveFloatArrays(Number[][] value) {
		float[][] answer = new float[value.length][];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = numbersToPrimitiveFloats(value[i]);
		return answer;
	}
	
	public static int[][] numbersToPrimitiveIntArrays(Number[][] value) {
		int[][] answer = new int[value.length][];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = numbersToPrimitiveInts(value[i]);
		return answer;
	}
	
	public static long[][] numbersToPrimitiveLongArrays(Number[][] value) {
		long[][] answer = new long[value.length][];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = numbersToPrimitiveLongs(value[i]);
		return answer;
	}
	
	public static short[][] numbersToPrimitiveShortArrays(Number[][] value) {
		short[][] answer = new short[value.length][];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = numbersToPrimitiveShorts(value[i]);
		return answer;
	}
	
	//====================== two-dimensions wrapper array to primitive array =========
	public static Boolean[][] boolArraysPrimitiveToWrapper(boolean[][] value) {
		Boolean[][] answer = new Boolean[value.length][];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = boolArrayPrimitiveToWrapper(value[i]);
		return answer;
	}
	
	public static Byte[][] byteArraysPrimitiveToWrapper(byte[][] value) {
		Byte[][] answer = new Byte[value.length][];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = byteArrayPrimitiveToWrapper(value[i]);
		return answer;
	}
	
	public static Character[][] charArraysPrimitiveToWrapper(char[][] value) {
		Character[][] answer = new Character[value.length][];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = charArrayPrimitiveToWrapper(value[i]);
		return answer;
	}
	
	public static Double[][] doubleArraysPrimitiveToWrapper(double[][] value) {
		Double[][] answer = new Double[value.length][];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = doubleArrayPrimitiveToWrapper(value[i]);
		return answer;
	}
	
	public static Float[][] floatArraysPrimitiveToWrapper(float[][] value) {
		Float[][] answer = new Float[value.length][];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = floatArrayPrimitiveToWrapper(value[i]);
		return answer;
	}
	
	public static Integer[][] intArraysPrimitiveToWrapper(int[][] value) {
		Integer[][] answer = new Integer[value.length][];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = intArrayPrimitiveToWrapper(value[i]);
		return answer;
	}
	
	public static Long[][] longArraysPrimitiveToWrapper(long[][] value) {
		Long[][] answer = new Long[value.length][];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = longArrayPrimitiveToWrapper(value[i]);
		return answer;
	}
	
	public static Short[][] shortArraysPrimitiveToWrapper(short[][] value) {
		Short[][] answer = new Short[value.length][];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = shortArrayPrimitiveToWrapper(value[i]);
		return answer;
	}
	
	//============ numbers to wrapper arrays ==========
	public static Byte[][] numbersToWrapperByteArrays(Number[][] value) {
		Byte[][] answer = new Byte[value.length][];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = numbersToWrapperBytes(value[i]);
		return answer;
	}
	
	public static Character[][] numbersToWrapperCharArrays(Object[][] value) {
		Character[][] answer = new Character[value.length][];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = numbersToWrapperChars(value[i]);
		return answer;
	}
	
	public static Double[][] numbersToWrapperDoubleArrays(Number[][] value) {
		Double[][] answer = new Double[value.length][];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = numbersToWrapperDoubles(value[i]);
		return answer;
	}
	
	public static Float[][] numbersToWrapperFloatArrays(Number[][] value) {
		Float[][] answer = new Float[value.length][];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = numbersToWrapperFloats(value[i]);
		return answer;
	}
	
	public static Integer[][] numbersToWrapperIntArrays(Number[][] value) {
		Integer[][] answer = new Integer[value.length][];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = numbersToWrapperInts(value[i]);
		return answer;
	}
	
	public static Long[][] numbersToWrapperLongArrays(Number[][] value) {
		Long[][] answer = new Long[value.length][];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = numbersToWrapperLongs(value[i]);
		return answer;
	}
	
	public static Short[][] numbersToWrapperShortArrays(Number[][] value) {
		Short[][] answer = new Short[value.length][];
		for(int i = 0 ; i < value.length ; i++)
			answer[i] = numbersToWrapperShorts(value[i]);
		return answer;
	}
}
