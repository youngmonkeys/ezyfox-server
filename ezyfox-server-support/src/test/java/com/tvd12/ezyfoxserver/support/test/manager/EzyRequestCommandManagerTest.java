package com.tvd12.ezyfoxserver.support.test.manager;

import com.tvd12.ezyfoxserver.support.manager.EzyRequestCommandManager;
import com.tvd12.test.assertion.Asserts;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Collections;

public class EzyRequestCommandManagerTest {

    @Test
    public void test() {
        // given
        EzyRequestCommandManager sut = new EzyRequestCommandManager();
        sut.addCommand("a");
        sut.addPaymentCommand("d");
        sut.addManagementCommand("e");

        // when
        // then
        Asserts.assertTrue(sut.containsCommand("a"));
        Asserts.assertEquals(sut.getCommands(), Collections.singletonList("a"), false);
        Assert.assertTrue(sut.isPaymentCommand("d"));
        Asserts.assertEquals(sut.getPaymentCommands(), Collections.singletonList("d"), false);
        Assert.assertTrue(sut.isManagementCommand("e"));
        Asserts.assertEquals(sut.getManagementCommands(), Collections.singletonList("e"), false);
        sut.destroy();
    }
}
