package com.tvd12.ezyfoxserver.io;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public final class EzyCollections {

	private EzyCollections() {
	}
	
	public static boolean isEmpty(Collection<?> coll) {
		return coll != null ? coll.isEmpty() : true;
	}
	
	/**
	 * Returns an array containing all of the elements in the collection
	 * 
	 * @param coll the collection
	 * @param applier the applier
	 * @return the new array
	 */
	public static <T> T[] toArray(Collection<T> coll, IntFunction<T[]> applier) {
		return coll.toArray(applier.apply(coll.size()));
	}
	
	/**
	 * 
	 * Check whether collection no.1 contains any item of collection no.2
	 * 
	 * @param c1 the collection no.1
	 * @param c2 the collection no.2
	 * @return true or false
	 */
	public static boolean containsAny(Collection<?> c1, Collection<?> c2) {
		return c2.stream().anyMatch((i) -> c1.contains(i));
    }
	
	/**
     * Filter the collection and count item
     * 
     * @param <T> the value type
     * @param coll the collection
     * @param predicate the predicate
     * @return the item count
     */
    public static <T> int countItems(Collection<T> coll, Predicate<T> predicate) {
        return (int) coll.stream().filter(predicate).count();
    }
    
    /**
     * Loop the collection and calculate sum of value related to item in the collection
     * 
     * @param <T> the value type
     * @param coll the collection
     * @param measurer the measurer
     * @return the sum value
     */
    public static <T> int flatMapToInt(Collection<T> coll, Function<T, Integer> measurer) {
    	return coll.stream().flatMapToInt((t) -> IntStream.of(measurer.apply(t))).sum();
    }
	
	/**
     * Filter the collection and get an item
     * 
     * @param <T> the value type
     * @param coll the collection
     * @param predicate the predicate
     * @return an item
     */
    public static <T> T getItem(Collection<T> coll, Predicate<T> predicate) {
        for(T t : coll) 
            if(predicate.test(t)) 
            	return t;
        return null;
    }
	
}
