package com.tvd12.ezyfoxserver.io;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.Lists;

/**
 * Support to convert data type
 * 
 * @author tavandung12
 *
 */

public final class EzyDataConverter {
    
    // prevent new instance
    private EzyDataConverter() {}
    
    /**
     * convert string value to char value
     * 
     * @param value string value 
     * @return char value
     */
    public static char stringToChar(String value) {
        if(value == null || value.length() == 0)
            return (char)0;
        return value.charAt(0);
    }
    
    /**
     * convert byte array value to char array value
     * 
     * @param value byte array value
     * @return char array value
     */
    public static char[] byteArrayToCharArray(byte[] value) {
        char[] result = new char[value.length];
        for(int i = 0 ; i < value.length ; i++)
            result[i] = (char)value[i];
        return result;
    }
    
    /**
     * convert char array value to byte array value
     * 
     * @param value char array value
     * @return byte array value
     */
    public static byte[] charArrayToByteArray(char[] value) {
        byte[] result = new byte[value.length];
        for(int i = 0 ; i < value.length ; i++)
            result[i] = (byte)value[i];
        return result;
    }
    
    // ===== collection to wrapper array =====
    /**
     * convert Boolean collection to Boolean array
     * 
     * @param value Boolean collection
     * @return Boolean array
     */
    public static Boolean[] collectionToWrapperBoolArray(
            Collection<Boolean> value) {
        return value.toArray(new Boolean[value.size()]);
    }
    
    /**
     * Convert Byte Collection to Byte array
     * 
     * @param value Byte Collection
     * @return Byte array
     */
    public static Byte[] collectionToWrapperByteArray(
            Collection<Byte> value) {
        return value.toArray(new Byte[value.size()]);
    }
    
    /**
     * Convert Character collection to Character array
     * 
     * @param value Character collection
     * @return Character array
     */
    public static Character[] collectionToWrapperCharArray(
            Collection<Character> value) {
        return value.toArray(new Character[value.size()]);
    }
    
    /**
     * Convert Double collection to Double array
     * 
     * @param value Double collection
     * @return Double array
     */
    public static Double[] collectionToWrapperDoubleArray(
            Collection<Double> value) {
        return value.toArray(new Double[value.size()]);
    }
    
    /**
     * Convert Float collection to Float array
     * 
     * @param value Float collection
     * @return Float value
     */
    public static Float[] collectionToWrapperFloatArray(
            Collection<Float> value) {
        return value.toArray(new Float[value.size()]);
    }
    
    /**
     * Convert Integer collection to Integer array
     * 
     * @param value Integer collecton
     * @return Integer array
     */
    public static Integer[] collectionToWrapperIntArray(
            Collection<Integer> value) {
        return value.toArray(new Integer[value.size()]);
    }
    
    /**
     * Convert Long collection to Long array
     * 
     * @param value Long collection
     * @return Long array
     */
    public static Long[] collectionToWrapperLongArray(
            Collection<Long> value) {
        return value.toArray(new Long[value.size()]);
    }
    
    /**
     * Convert Short collection to Short array
     * 
     * @param value Short collection
     * @return Short array
     */
    public static Short[] collectionToWrapperShortArray(
            Collection<Short> value) {
        return value.toArray(new Short[value.size()]);
    }
    // ===== end collection to wrapper array ====
    
    // ===== collection to primitive array =====
    /**
     * Convert Boolean collection to primitive boolean array
     * 
     * @param value Boolean collection
     * @return primitive boolean array
     */
    public static boolean[] collectionToPrimitiveBoolArray(
            Collection<Boolean> value) {
    	int count = 0;
    	boolean[] answer = new boolean[value.size()];
    	for(Boolean item : value)
    		answer[count ++] = item;
    	return answer;
    }
    
    /**
     * Convert Byte collection to primitive byte array
     * 
     * @param value Byte collection
     * @return primitive byte array
     */
    public static byte[] collectionToPrimitiveByteArray(
            Collection<Byte> value) {
    	int count = 0;
    	byte[] answer = new byte[value.size()];
    	for(Byte item : value)
    		answer[count ++] = item;
    	return answer;
    }
    
    /**
     * Convert Character collection to primitive char array
     * 
     * @param value Character collection
     * @return primitive char array
     */
    public static char[] collectionToPrimitiveCharArray(
            Collection<Character> value) {
    	int count = 0;
    	char[] answer = new char[value.size()];
    	for(Character item : value)
    		answer[count ++] = item;
    	return answer;
    }
    
    /**
     * Convert Character collection to primitive byte array
     * 
     * @param value Character collection
     * @return primitive byte array
     */
    public static byte[] charCollectionToPrimitiveByteArray(
            Collection<Character> value) {
    	int count = 0;
    	byte[] answer = new byte[value.size()];
    	for(Character item : value)
    		answer[count ++] = (byte) item.charValue();
    	return answer;
    }
    
    /**
     * Convert Double collection to primitive double array
     * 
     * @param value Double collection
     * @return primitive double array
     */
    public static double[] collectionToPrimitiveDoubleArray(
            Collection<Double> value) {
    	int count = 0;
    	double[] answer = new double[value.size()];
    	for(Double item : value)
    		answer[count ++] = item;
    	return answer;
    }
    
    /**
     * Convert Float collection to primitive float array
     * 
     * @param value Float collection
     * @return primitive float array
     */
    public static float[] collectionToPrimitiveFloatArray(
            Collection<Float> value) {
    	int count = 0;
    	float[] answer = new float[value.size()];
    	for(Float item : value)
    		answer[count ++] = item;
    	return answer;
    }
    
    /**
     * Convert Integer collection to primitive int array
     * 
     * @param value Integer collection
     * @return primitive int array
     */
    public static int[] collectionToPrimitiveIntArray(
            Collection<Integer> value) {
        return ArrayUtils.toPrimitive(value.toArray(new Integer[value.size()]));
    }
    
    /**
     * Convert Long collection to primitive long array
     * 
     * @param value Long collection
     * @return primitive long array
     */
    public static long[] collectionToPrimitiveLongArray(
            Collection<Long> value) {
    	int count = 0;
    	long[] answer = new long[value.size()];
    	for(Long item : value)
    		answer[count ++] = item;
    	return answer;
    }
    
    /**
     * Convert Short collection to primitive short array
     * 
     * @param value Short collection
     * @return primitive short array
     */
    public static short[] collectionToPrimitiveShortArray(
            Collection<Short> value) {
    	int count = 0;
    	short[] answer = new short[value.size()];
    	for(Short item : value)
    		answer[count ++] = item;
    	return answer;
    }
    
    /**
     * Convert String collection to String array
     * 
     * @param value String collection
     * @return String array
     */
    public static String[] collectionToStringArray(
            Collection<String> value) {
        return value.toArray(new String[value.size()]);
    }
    
    /**
     * Convert an array to a List
     * 
     * @param <T> the value type
     * @param value an object represent to an array
     * @return a list object
     */
    @SuppressWarnings("unchecked")
	public static <T> List<T> arrayToList(Object value) {
        List<T> answer = new ArrayList<>();
        int length = Array.getLength(value);
        for(int i = 0 ; i < length ; i++)
        	answer.add((T) Array.get(value, i));
        return answer;
    }
    
    /**
     * Convert an array to a List
     * 
     * @param <T> the value type
     * @param value the array to convert
     * @return a list object
     */
	public static <T> List<T> arrayToList(T[] value) {
    	return Lists.newArrayList(value);
    }
	
	/**
     * Convert boolean array to List
     * 
     * @param value the array to convert
     * @return a list object
     */
	public static List<Boolean> arrayToList(boolean[] value) {
		List<Boolean> answer = new ArrayList<>();
		for(boolean v : value)
			answer.add(v);
		return answer;
    }
	
	/**
     * Convert char array to List
     * 
     * @param value the array to convert
     * @return a list object
     */
	public static List<Character> arrayToList(char[] value) {
		List<Character> answer = new ArrayList<>();
		for(char v : value)
			answer.add(v);
		return answer;
    }
	
	/**
     * Convert double array to List
     * 
     * @param value the array to convert
     * @return a list object
     */
	public static List<Double> arrayToList(double[] value) {
		List<Double> answer = new ArrayList<>();
		for(double v : value)
			answer.add(v);
		return answer;
    }
	
	/**
     * Convert float array to List
     * 
     * @param value the array to convert
     * @return a list object
     */
	public static List<Float> arrayToList(float[] value) {
		List<Float> answer = new ArrayList<>();
		for(float v : value)
			answer.add(v);
		return answer;
    }
	
	/**
     * Convert int array to List
     * 
     * @param value the array to convert
     * @return a list object
     */
	public static List<Integer> arrayToList(int[] value) {
		List<Integer> answer = new ArrayList<>();
		for(int v : value)
			answer.add(v);
		return answer;
    }
	
	/**
     * Convert long array to List
     * 
     * @param value the array to convert
     * @return a list object
     */
	public static List<Long> arrayToList(long[] value) {
		List<Long> answer = new ArrayList<>();
		for(long v : value)
			answer.add(v);
		return answer;
    }
	
	/**
     * Convert short array to List
     * 
     * @param value the array to convert
     * @return a list object
     */
	public static List<Short> arrayToList(short[] value) {
		List<Short> answer = new ArrayList<>();
		for(short v : value)
			answer.add(v);
		return answer;
    }
    
    /**
     * Convert primitive byte array to Character collection
     * 
     * @param value primitive byte array
     * @return Character collection
     */
    public static List<Character> byteArrayToCharList(byte[] value) {
        List<Character> answer = new ArrayList<>();
        for(byte b : value)
        	answer.add((char)b);
        return answer;
    }
    
    //===== primitive array to wrapper array =====
    /**
     * Convert primitive boolean array to wrapper Boolean array 
     * 
     * @param value primitive boolean array
     * @return wrapper Boolean array
     */
    public static Boolean[] toBoolWrapperArray(boolean[] value) {
        return ArrayUtils.toObject(value);
    }
    
    /**
     * Convert primitive byte array to wrapper Byte array
     * 
     * @param value primitive byte array
     * @return wrapper Boolean array
     */
    public static Byte[] toByteWrapperArray(byte[] value) {
        return ArrayUtils.toObject(value);
    }
    //===== end primitive array to wrapper array =====
    
    /**
     * Convert primitive byte array to wrapper Character array
     * 
     * @param value primitive byte array
     * @return wrapper Character array
     */
    public static Character[] toCharWrapperArray(
            byte[] value) {
        return ArrayUtils.toObject(byteArrayToCharArray(value));
    }
    
    /**
     * Convert wrapper Byte array to primitive byte array
     * 
     * @param value wrapper Byte array
     * @return primitive byte array
     */
    public static byte[] toPrimitiveByteArray(
            Byte[] value) {
        return ArrayUtils.toPrimitive(value);
    }
    
    /**
     * Convert wrapper Character array to primitive char array
     * 
     * @param value wrapper Character array
     * @return primitive char array
     */
    public static char[] toPrimitiveCharArray(
            Character[] value) {
        return ArrayUtils.toPrimitive(value);
    }
    
    /**
     * Convert wrapper Character array to primitive byte array
     * 
     * @param value wrapper Character array
     * @return primitive byte array
     */
    public static byte[] charWrapperArrayToPrimitiveByteArray(
            Character[] value) {
        return charArrayToByteArray(toPrimitiveCharArray(value));
    }
    
    /**
     * Convert primitive boolean array to Boolean collection
     * 
     * @param value primitive boolean array
     * @return Boolean collection
     */
    public static Collection<Boolean> primitiveArrayToBoolCollection(
            boolean[] value) {
        return Arrays.asList(ArrayUtils.toObject(value));
    }
    
    /**
     * Convert primitive byte array to Byte collection
     * 
     * @param value primitive byte array
     * @return Byte collection
     */
    public static Collection<Byte> primitiveArrayToByteCollection(
            byte[] value) {
        return Arrays.asList(ArrayUtils.toObject(value));
    }
    
    /**
     * Convert primitive char array to Character collection
     * 
     * @param value primitive char array
     * @return Character collection
     */
    public static Collection<Character> primitiveArrayToCharCollection(
            char[] value) {
        return Arrays.asList(ArrayUtils.toObject(value));
    }
    
    /**
     * Convert primitive double array to Double collection
     * 
     * @param value primitive double array
     * @return Double collection
     */
    public static Collection<Double> primitiveArrayToDoubleCollection(
            double[] value) {
        return Arrays.asList(ArrayUtils.toObject(value));
    }
    
    /**
     * Convert primitive float array to Float collection
     * 
     * @param value primitive float array
     * @return Float collection
     */
    public static Collection<Float> primitiveArrayToFloatCollection(
            float[] value) {
        return Arrays.asList(ArrayUtils.toObject(value));
    }
    
    /**
     * Convert primitive int array to Integer collection
     * 
     * @param value primitive int array 
     * @return Integer collection
     */
    public static Collection<Integer> primitiveArrayToIntCollection(
            int[] value) {
        return Arrays.asList(ArrayUtils.toObject(value));
    }
    
    /**
     * Convert primitive long array to Long collection
     * 
     * @param value primitive long array
     * @return Long collection
     */
    public static Collection<Long> primitiveArrayToLongCollection(
            long[] value) {
        return Arrays.asList(ArrayUtils.toObject(value));
    }
    
    /**
     * Convert primitive short array to Short collection
     * 
     * @param value primitive short array
     * @return Short collection
     */
    public static Collection<Short> primitiveArrayToShortCollection(
            short[] value) {
        return Arrays.asList(ArrayUtils.toObject(value));
    }
    
    /**
     * Convert String array to String collection
     * 
     * @param value String array
     * @return String collection
     */
    public static Collection<String> stringArrayToCollection(
            String[] value) {
        return Arrays.asList(value);
    }
    
    /**
    * Convert object array to object collection
    * 
    * @param <T> the object type
    * @param value object array
    * @return object collection
    */
    public static <T> Collection<T> wrapperArrayToCollection(
            T[] value) {
        return Arrays.asList(value);
    }
    
    /**
     * Convert object array to object collection
     * 
     * @param <T> the object type
     * @param value object represent to array
     * @return object collection
     */
    @SuppressWarnings("unchecked")
    public static <T> Collection<T> wrapperArrayToCollection(
            Object value) {
        return Arrays.asList((T[])value);
    }
    
}
