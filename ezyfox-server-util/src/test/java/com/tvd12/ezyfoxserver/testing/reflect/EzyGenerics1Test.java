package com.tvd12.ezyfoxserver.testing.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.reflect.EzyFields;

import lombok.Data;

public class EzyGenerics1Test {

	@Test
	public void test() {
		System.out.println("\n======= begin check getGenericType ========\n");
		List<Field> fields = EzyFields.getFields(ClassC.class);
		for(Field field : fields) {
			System.out.println("field " + field.getName() + ", genericType = " + field.getGenericType().getTypeName());
		}
		System.out.println("\n======= end check getGenericType ========\n");
		
		List<Field> validFields = new ArrayList<>();
		
		System.out.println("\n======= begin check is ParameterizedType ========\n");
		for(Field field : fields) {
			Type type = field.getGenericType();
			System.out.println("field " + field.getName() + ", is ParameterizedType = " + 
					(type instanceof ParameterizedType));
			if(type instanceof ParameterizedType)
				validFields.add(field);
		}
		System.out.println("\n======= end check getGenericType ========\n");
		
		System.out.println("\n======= begin check generic argument type ========\n");
		for(Field field : validFields) {
			Type type = field.getGenericType();
			Type[] types = ((ParameterizedType)type).getActualTypeArguments();
			System.out.println("field " + field.getName() + ", genericType = " + 
					Arrays.toString(types));
			System.out.println(types[0].getClass());
		}
		System.out.println("\n======= end check getGenericType ========\n");
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
