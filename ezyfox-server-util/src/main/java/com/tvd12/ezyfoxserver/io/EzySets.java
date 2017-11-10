package com.tvd12.ezyfoxserver.io;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

public final class EzySets {

	private EzySets() {
    }
    
	/**
	 * Combine some set of items
	 * 
	 * @param sets some set of items
	 * @return the new HashSet
	 */
    @SuppressWarnings({ "unchecked"})
    public static <E> Set<E> combine(Collection<E>... sets) {
        Set<E> all = new HashSet<>();
        for(Collection<E> set : sets)
            all.addAll(set);
        return all;
    }
    
    /**
     * Transform for each item in a collection to new collection 
     * and add to new HashSet
     * 
     * @param coll the collection
     * @param trans the transformer
     * @return the new HashSet
     */
    public static <I, O> Set<O> newHashSetByAddAll(
            Collection<I> coll, Function<I, Collection<O>> trans) {
        Set<O> set = new HashSet<>();
        for(I input : coll)
            set.addAll(trans.apply(input));
        return set;
    }
    
    /**
     * Filter the collection and create a new hash set
     * 
     * @param <T> the value type
     * @param coll the collection
     * @param predicate the predicate
     * @return the new hash set
     */
    public static <T> Set<T> filter(Collection<T> coll, Predicate<T> predicate) {
       return coll.stream().filter(predicate).collect(Collectors.toSet());
    }
    
    /**
     * Remove some item from the collection and create a new hash set
     * 
     * @param <T> the value type
     * @param coll the collection
     * @param except the unexpected items
     * @return a new hash set
     */
    public static <T> Set<T> newHashSet(Collection<T> coll, Collection<T> except) {
        Set<T> answer = new HashSet<>(coll);
        answer.removeAll(except);
        return answer;
    }
    
    /**
     * Remove some item from the collection and create a new hash set
     * 
     * @param <T> the value type
     * @param coll the collection
     * @param except the unexpected items
     * @return a new hash set
     */
    @SuppressWarnings("unchecked")
    public static <T> Set<T> newHashSet(Collection<T> coll, T... except) {
        return newHashSet(coll, Arrays.asList(except));
    }
    
    /**
     * Transform an array to a new hash set
     * 
     * @param <I> the input type
     * @param <O> the output type
     * @param input the input
     * @param refactor the refactor
     * @return a new hash set
     */
    public static <I, O> Set<O> newHashSet(I[] input, Function<I, O> refactor) {
        return newHashSet(Sets.newHashSet(input), refactor);
    }
    
    /**
     * Transform a collection to a new hash set
     * 
     * @param <I> the input type
     * @param <O> the output type
     * @param input the input
     * @param refactor the refactor
     * @return a new hash set
     */
    public static <I, O> Set<O> newHashSet(Collection<I> input, Function<I, O> refactor) {
    	Set<O> answer = new HashSet<>();
        for(I value : input)
            answer.add(refactor.apply(value));
        return answer;
    }
    
    /**
     * 
     * Transform a map to new hash set
     * 
     * @param <K> the key type
     * @param <V> the value type
     * @param <O> the output type
     * @param input the input
     * @param refactor the refactor
     * @return the new hash set
     */
    public static <K, V, O> Set<O> newHashSet(Map<K, V> input, BiFunction<K, V, O> refactor) {
    	Set<O> answer = new HashSet<>();
        for(K key : input.keySet())
            answer.add(refactor.apply(key, input.get(key)));
        return answer;
    }
    
    /**
     * Combine a collection and an array of elements to a new hash set
     * 
     * @param <T> the value type
     * @param coll the collection
     * @param elements the array of elements
     * @return the new hash set
     */
    @SuppressWarnings("unchecked")
    public static <T> Set<T> addElementsToNewSet(Collection<T> coll, T... elements) {
        Set<T> answer = new HashSet<>(coll);
        answer.addAll(Arrays.asList(elements));
        return answer;
    }
    
}
