package com.tvd12.ezyfoxserver.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.tvd12.ezyfoxserver.stream.EzyClassPathInputStreamLoader;

public class EzyBannerPrinter {

    public String getBannerString() {
        return new String(getBannerBytes());
    }
    
    protected byte[] getBannerBytes() {
        return getBannerBytes(getBannerInputStream());
    }
    
    protected byte[] getBannerBytes(InputStream stream) {
        try {
            return IOUtils.toByteArray(stream);
        } catch (IOException e) {
            return new byte[0];
        }
    }
    
    protected InputStream getBannerInputStream() {
        return EzyClassPathInputStreamLoader.builder()
                .context(getClass())
                .build()
                .load("ezyfox-banner.txt");
    }
    
}
