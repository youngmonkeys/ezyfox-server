package com.tvd12.ezyfoxserver.testing.ccl;

import com.tvd12.ezyfox.util.EzyDirectories;
import com.tvd12.ezyfoxserver.ccl.EzyAppClassLoader;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

public class EzyAppClassLoaderTest extends BaseTest {

    @Test
    public void test() {
        EzyAppClassLoader loader = new EzyAppClassLoader(
            new File("test-data/apps/entries/ezyfox-chat"),
            getClass().getClassLoader());
        loader.findResource("ezyfox-chat.jar");
        try {
            loader.close();
        } catch (IOException e) {
        }
    }

    @Test(expectedExceptions = {InvocationTargetException.class})
    public void test1() throws Exception {
        Method method = EzyAppClassLoader.class
            .getDeclaredMethod("getURLsByPath", EzyDirectories.class);
        method.setAccessible(true);
        method.invoke(EzyAppClassLoader.class, new EzyDirectories() {
            @Override
            public URL[] getURLs() throws IOException {
                throw new IOException();
            }
        });
    }

    @Test
    public void test2() throws Exception {
        try {
            EzyAppClassLoader loader = new EzyAppClassLoader(
                new File("test-data/apps/entries/ezyfox-chat") {
                    private static final long serialVersionUID = -4357308555875682046L;

                    @Override
                    public boolean isDirectory() {
                        throw new RuntimeException();
                    }

                    @Override
                    public File[] listFiles() {
                        throw new RuntimeException();
                    }
                },
                getClass().getClassLoader());
            loader.findResource("ezyfox-chat.jar");
            try {
                loader.close();
            } catch (IOException e) {
            }
        } catch (Exception e) {

        }
    }

}
