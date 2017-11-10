package com.tvd12.ezyfoxserver.testing.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.reflect.EzyGenerics;

import lombok.Data;

public class EzyGenerics4Test {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void test() throws Exception {
		Field field = ClassC.class.getDeclaredField("map1");
		ParameterizedType type = (ParameterizedType)field.getGenericType();
		System.out.println(type.getRawType().getTypeName());
		System.out.println(field.getType().equals(type.getRawType()));
		System.out.println(Map.class.isAssignableFrom((Class)type.getRawType()));
		
		Map<String, String> map = new HashMap<>();
		Class<Map<String, String>> mapClass = (Class<Map<String, String>>)map.getClass();
		System.out.println("mapClass = " + mapClass);
		System.out.println(Arrays.toString(mapClass.getGenericInterfaces()));
		System.out.println(mapClass.getGenericSuperclass());
		Type mapType = mapClass.getGenericInterfaces()[0];
		Type mapType0 = ((ParameterizedType)mapType).getActualTypeArguments()[0];
		System.out.println("mapType0 is String.class = " + mapType0.equals(String.class));
		System.out.println(mapType0.getClass());
		
		Field list1 = ClassC.class.getDeclaredField("list1");
		Type list1Type = list1.getGenericType();
		Class list1ElementType = EzyGenerics.getOneGenericClassArgument(list1Type);
		System.out.println("list1Type: " + list1Type);
		System.out.println("list1ElementType: "  + list1ElementType);
	}
	
	@Data
    public static class ClassA {
        private String name;
        private List<String> abc = new ArrayList<>();
    }
	
    @Data
    public static class ClassB {
    	private String name;
    	
    	private int a;
    	private int b;
    	private int c;
    	private int d;
    	private int e;
    	private int f;
    	private int g;
    	private int h;
    	private int i;
    	private int j;
    }
    
    @Data
    @SuppressWarnings("rawtypes")
    public static class ClassC {
        public ClassB classB; 
        public ClassD classD;
        public boolean visible;
        
        public ClassD<String> classD1;
        
        public Map<String, String> map1;
        public List<List<String>> list1;
		public Map map2;
		public Map<String, ?> map3;
		public Map<?, ?> map4;
        public List list2;
    }
    
    public static class ClassD<T> {
        public ClassD(T value) {
            
        }
    }
	
}
