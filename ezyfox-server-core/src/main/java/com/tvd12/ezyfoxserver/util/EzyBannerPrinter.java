package com.tvd12.ezyfoxserver.util;

import java.io.IOException;
import java.io.InputStream;

import com.tvd12.ezyfox.stream.EzyClassPathInputStreamLoader;
import com.tvd12.ezyfox.stream.EzyInputStreams;

public class EzyBannerPrinter {

    public String getBannerText() {
        return new String(getBannerBytes());
    }
    
    protected byte[] getBannerBytes() {
        return getBannerBytes(getBannerInputStream());
    }
    
    protected byte[] getBannerBytes(InputStream stream) {
        try {
            return EzyInputStreams.toByteArray(stream);
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
