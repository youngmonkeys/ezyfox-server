package com.tvd12.ezyfoxserver.testing.factory;

import com.tvd12.ezyfoxserver.setting.EzySimpleSessionManagementSetting;
import com.tvd12.ezyfoxserver.testing.MyTestSessionFactory;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzyAbstractSessionFactoryTest extends BaseTest {

    @Test
    public void test() {
        MyTestSessionFactory factory = new MyTestSessionFactory();
        factory.setMaxRequestPerSecond(new EzySimpleSessionManagementSetting.EzySimpleMaxRequestPerSecond());
        assert factory.newProduct() != null;
    }
}
