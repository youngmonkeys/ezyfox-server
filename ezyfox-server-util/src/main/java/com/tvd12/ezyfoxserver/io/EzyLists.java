/**
 * 
 */
package com.tvd12.ezyfoxserver.io;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

/**
 * @author tavandung12
 *
 */
public class EzyLists {

	// prevent new instance
    private EzyLists() {
    }
    
    /**
     * Combine some collections to one
     * 
     * @param <T> the value type
     * @param lists the collections to combine 
     * @return a new list
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> combine(Collection<T>... lists) {
        List<T> answer = new ArrayList<>();
        for(Collection<T> list : lists)
            answer.addAll(list);
        return answer;
    }
    
    /**
     * Transform for each item in a collection to new collection 
     * and add to new list
     * 
     * @param coll the collection
     * @param trans the transformer
     * @return the new list
     */
    public static <I, O> List<O> newHashSetByAddAll(
            Collection<I> coll, Function<I, Collection<O>> trans) {
    	List<O> set = new ArrayList<>();
        for(I input : coll)
            set.addAll(trans.apply(input));
        return set;
    }
    
    
    /**
     * Filter the collection and create a new list
     * 
     * @param <T> the value type
     * @param coll the collection
     * @param predicate the predicate
     * @return a new list
     */
    public static <T> List<T> filter(Collection<T> coll, Predicate<T> predicate) {
       return coll.stream().filter(predicate).collect(Collectors.toList());
    }
    
    /**
     * Remove some item from the collection and create a new list
     * 
     * @param <T> the value type
     * @param coll the collection
     * @param except the unexpected items
     * @return a new list
     */
    public static <T> List<T> newArrayList(Collection<T> coll, Collection<T> except) {
        List<T> answer = new ArrayList<>(coll);
        answer.removeAll(except);
        return answer;
    }
    
    /**
     * Remove some item from the collection and create a new list
     * 
     * @param <T> the value type
     * @param coll the collection
     * @param except the unexpected items
     * @return a new list
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> newArrayList(Collection<T> coll, T... except) {
        return newArrayList(coll, Arrays.asList(except));
    }
    
    /**
     * Transform an array to a new list
     * 
     * @param <I> the input type
     * @param <O> the output type
     * @param input the input
     * @param refactor the refactor
     * @return a new list
     */
    public static <I, O> List<O> newArrayList(I[] input, Function<I, O> refactor) {
        return newArrayList(Lists.newArrayList(input), refactor);
    }
    
    /**
     * Transform a collection to a new list
     * 
     * @param <I> the input type
     * @param <O> the output type
     * @param input the input
     * @param refactor the refactor
     * @return a new list
     */
    public static <I, O> List<O> newArrayList(Collection<I> input, Function<I, O> refactor) {
        List<O> answer = new ArrayList<>();
        for(I value : input)
            answer.add(refactor.apply(value));
        return answer;
    }
    
    /**
     * 
     * Transform a map to new list
     * 
     * @param <K> the key type
     * @param <V> the value type
     * @param <O> the output type
     * @param input the input
     * @param refactor the refactor
     * @return the new list
     */
    public static <K, V, O> List<O> newArrayList(Map<K, V> input, BiFunction<K, V, O> refactor) {
        List<O> answer = new ArrayList<>();
        for(K key : input.keySet())
            answer.add(refactor.apply(key, input.get(key)));
        return answer;
    }
    
    /**
     * Combine a collection and an array of elements to a new list
     * 
     * @param <T> the value type
     * @param coll the collection
     * @param elements the array of elements
     * @return the new list
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> addElementsToNewList(Collection<T> coll, T... elements) {
        List<T> answer = new ArrayList<>(coll);
        answer.addAll(Arrays.asList(elements));
        return answer;
    }
    
    /**
     * Resizes the container so that it contains n elements.
     * 
     * @param list the list to resize
     * @param n new container size, expressed in number of elements.
     * @param defValue value whose content is copied to the added elements in case that n is greater than the current container size
     */
    public static <T> void resize(List<T> list, int n, T defValue) {
		int size = list.size();
		if(size == n)
			return;
		if(size > n) {
			int offset = size - n;
			while((offset --) > 0)
				list.remove(list.size() - 1);
		}
		else {
			int offset = n - size;
			while((offset --) > 0)
				list.add(defValue);
		}
			
	}
    
}
