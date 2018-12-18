package com.tvd12.ezyfoxserver.testing.service;

import java.io.File;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.json.EzySimpleJsonWriter;
import com.tvd12.ezyfox.mapping.jaxb.EzySimplXmlMapper;
import com.tvd12.ezyfox.mapping.jaxb.EzyXmlReader;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import com.tvd12.ezyfoxserver.testing.jaxberror.ClassA;
import com.tvd12.test.base.BaseTest;

public class EzySimpleXmlReaderTest extends BaseTest {

    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void test1() {
        EzyXmlReader reader = EzySimplXmlMapper.builder()
                .contextClass(ClassA.class)
                .build();
        reader.read(new File("pom.xml"), ClassB.class);
    }
    
    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void test2() {
        EzyXmlReader reader = EzySimplXmlMapper.builder()
                .contextPath("com.tvd12.ezyfoxserver.mapping")
                .classLoader(getClass().getClassLoader())
                .build();
        reader.read(new File("pom.xml"), ClassB.class);
    }
    
    @Test
    public void test3() {
        EzyXmlReader reader = EzySimplXmlMapper.builder()
                .contextPath("com.tvd12.ezyfoxserver")
                .classLoader(getClass().getClassLoader())
                .build();
        EzySimpleSettings settings = 
                reader.read(new File("src/main/resources/ezy-settings.xml"), EzySimpleSettings.class);
        System.out.println(new EzySimpleJsonWriter().writeAsString(settings));
    }
    
    public static class ClassB {
        
    }
    
}
