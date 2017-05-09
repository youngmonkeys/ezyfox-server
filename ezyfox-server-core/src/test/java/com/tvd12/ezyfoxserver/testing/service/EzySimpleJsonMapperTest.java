package com.tvd12.ezyfoxserver.testing.service;

import org.testng.annotations.Test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tvd12.ezyfoxserver.mapping.jackson.EzyJsonMapper;
import com.tvd12.ezyfoxserver.mapping.jackson.EzySimpleJsonMapper;
import com.tvd12.test.base.BaseTest;

import lombok.Getter;
import lombok.Setter;

public class EzySimpleJsonMapperTest extends BaseTest {

    @Test
    public void test() {
        EzyJsonMapper mapper = EzySimpleJsonMapper.builder()
                .build();
        assert mapper.writeAsString(new ClassA()) == null;
    }
    
    @Setter
    @Getter
    public static class ClassA {
        
        @JsonProperty
        public String getValue() throws Exception {
            throw new Exception();
        }
        
    }
    
}
