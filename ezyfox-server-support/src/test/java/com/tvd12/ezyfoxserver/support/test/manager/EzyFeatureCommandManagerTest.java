package com.tvd12.ezyfoxserver.support.test.manager;

import java.util.Arrays;
import java.util.Collections;

import org.testng.annotations.Test;
import org.testng.collections.Sets;

import com.tvd12.ezyfox.util.EzyMapBuilder;
import com.tvd12.ezyfoxserver.support.manager.EzyFeatureCommandManager;
import com.tvd12.test.assertion.Asserts;

public class EzyFeatureCommandManagerTest {

    @Test
    public void test() {
        // given
        EzyFeatureCommandManager sut = new EzyFeatureCommandManager();
        sut.addFeatureCommand("hello", "/a");
        sut.addFeatureCommand("hello", "/b");
        sut.addFeatureCommand("world", "/c");
        
        // when
        Asserts.assertEquals(
            sut.getFeatures(),
            Sets.newHashSet("hello", "world"),
            false
        );
        Asserts.assertEquals(sut.getFeatureByCommand("/a"), "hello");
        Asserts.assertEquals(sut.getFeatureByCommand("/c"), "world");
        Asserts.assertEquals(sut.getCommandsByFeature("hello"), Arrays.asList("/a", "/b"), false);
        Asserts.assertEquals(
            sut.getFeatureByCommandMap(),
            EzyMapBuilder.mapBuilder()
                .put("/a", "hello")
                .put("/b", "hello")
                .put("/c", "world")
                .build()
        );
        Asserts.assertEquals(
            sut.getCommandsByFeatureMap(),
            EzyMapBuilder.mapBuilder()
                .put("hello", Arrays.asList("/a", "/b"))
                .put("world", Collections.singletonList("/c"))
                .build(),
            false
        );
        sut.destroy();
    }
}
