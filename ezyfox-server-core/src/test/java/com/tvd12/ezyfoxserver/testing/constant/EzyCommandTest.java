package com.tvd12.ezyfoxserver.testing.constant;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzyCommandTest extends BaseTest {

    @Test
    public void test() {
        EzyCommand value = EzyCommand.APP_ACCESS;
        assert value.getId() == EzyCommand.APP_ACCESS.getId();
        assert value == EzyCommand.valueOf(EzyCommand.APP_ACCESS.getId());
        assert value.getName().equals("APP_ACCESS");
        EzyCommand.valueOf("APP_ACCESS");
    }

}
