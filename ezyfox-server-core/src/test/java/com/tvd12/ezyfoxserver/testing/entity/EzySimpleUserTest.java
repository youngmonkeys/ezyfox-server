package com.tvd12.ezyfoxserver.testing.entity;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.test.base.BaseTest;

public class EzySimpleUserTest extends BaseTest {

    @Test
    public void test() {
        EzySimpleUser user = new EzySimpleUser();
        user.setId(1);
        user.setPassword("abc");
        assert user.getPassword().equals("abc");
        user.setName("dungtv1");
        
        EzySimpleUser user2 = new EzySimpleUser();
        user2.setPassword("abc");
        user2.setId(2);
        assert user2.getPassword().equals("abc");
        user.setName("dungtv2");
        
        assert !user.equals(user2);
        assert user.hashCode() != user2.hashCode();
    }
    
}
