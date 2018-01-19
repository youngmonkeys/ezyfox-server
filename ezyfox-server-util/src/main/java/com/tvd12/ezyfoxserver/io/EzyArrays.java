package com.tvd12.ezyfoxserver.io;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Stream;

public final class EzyArrays {

	private EzyArrays() {
	}
	
	public static void copy(byte[] from, byte[] to, int toPos) {
		for(int i = 0 ; i < from.length ; i++)
			to[toPos + i] = from[i];
	}
	
	public static <I,O> O[] newArray(Collection<I> coll, 
            IntFunction<O[]> generator) {
		return coll.stream().toArray(generator);
	}
	
    public static <I,O> O[] newArray(Collection<I> coll, 
            IntFunction<O[]> generator, Function<I, O> applier) {
        int count = 0;
        O[] answer = generator.apply(coll.size());
        for(I input : coll)
            answer[count ++] = applier.apply(input);
        return answer;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T extends Comparable> T min(T[] array) {
    	return min(array, (a, b) -> a.compareTo(b));
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T extends Comparable> T max(T[] array) {
    	return max(array, (a, b) -> a.compareTo(b));
    }
    
    public static <T> T min(T[] array, Comparator<T> comparator) {
    	return min(Arrays.stream(array), comparator);
    }
    
    public static <T> T max(T[] array, Comparator<T> comparator) {
    	return max(Arrays.stream(array), comparator);
    }
    
    public static <T> T min(Stream<T> stream, Comparator<T> comparator) {
    	return stream.min(comparator).get();
    }
    
    public static <T> T max(Stream<T> stream, Comparator<T> comparator) {
    	return stream.max(comparator).get();
    }
    
}
