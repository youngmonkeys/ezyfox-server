package com.tvd12.ezyfoxserver.testing.util;

import com.tvd12.ezyfoxserver.util.EzyBannerPrinter;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;

public class EzyBannerPrinterTest extends BaseTest {

    @Test
    public void test() {
        new EzyBannerPrinter() {
            @Override
            protected InputStream getBannerInputStream(String file) {
                return new InputStream() {

                    @Override
                    public int read() throws IOException {
                        throw new IOException();
                    }
                };
            }
        }.getBannerText("ezyfox-banner.txt");
    }
}
