package com.tvd12.ezyfoxserver.testing.factory;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.testing.MyTestSessionFactory;
import com.tvd12.test.base.BaseTest;

public class EzyAbstractSessionFactoryTest extends BaseTest {

    @Test
    public void test() {
        MyTestSessionFactory factory = new MyTestSessionFactory();
        assert factory.newProduct() != null;
    }
    
}
