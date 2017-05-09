package com.tvd12.ezyfoxserver.testing.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.reflect.EzyFields;
import com.tvd12.ezyfoxserver.reflect.EzyGenericSetterValidator;

import lombok.Data;

public class EzySetterValidatorTest {

	@Test
	public void test() throws Exception {
		System.out.println("\n======= begin validator ========\n");
		List<Field> fields = EzyFields.getFields(ClassC.class);
		for(Field field : fields) {
			System.out.println("field " + field.getName() +
					", valid = " + new EzyGenericSetterValidator().validate(field.getGenericType()) +
					", instance of class = " + (field.getGenericType() instanceof Class) +
					", instance of ParameterizedType " + (field.getGenericType() instanceof ParameterizedType) +
					", genericType = " + field.getGenericType().getTypeName());
		}
		System.out.println("\n======= end validator ========\n");
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
