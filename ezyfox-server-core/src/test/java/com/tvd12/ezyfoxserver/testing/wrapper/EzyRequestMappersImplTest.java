package com.tvd12.ezyfoxserver.testing.wrapper;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.request.EzyRequestAppParams;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import com.tvd12.ezyfoxserver.wrapper.EzyRequestMappers;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyRequestMappersImpl;

public class EzyRequestMappersImplTest extends BaseCoreTest {

    @Test
    public void test() {
        EzyArray array = newArrayBuilder()
                .append(1)
                .append(newArrayBuilder().build())
                .build();
        EzyRequestMappers mappers = EzyRequestMappersImpl.builder()
                .build();
        EzyRequestAppParams request = mappers.toObject(EzyCommand.APP_REQUEST, array);
        assert request.getAppId() == 1;
    }
    
}
