package com.tvd12.ezyfoxserver.testing.setting;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.setting.EzySimpleThreadPoolSizeSetting;

public class EzySimpleThreadPoolSizeSettingTest {

    @Test
    public void test() {
        EzySimpleThreadPoolSizeSetting setting = new EzySimpleThreadPoolSizeSetting();
        setting.setSocketDataReceiver(1);
        assert setting.getSocketDataReceiver() == 1;
        setting.setStatistics(2);
        assert setting.getStatistics() == 2;
        setting.setStreamHandler(3);
        assert setting.getStreamHandler() == 3;
        setting.setSystemRequestHandler(4);
        assert setting.getSystemRequestHandler() == 4;
        setting.setSocketDisconnectionHandler(5);
        assert setting.getSocketDisconnectionHandler() == 5;
        setting.setSocketUserRemovalHandler(6);
        assert setting.getSocketUserRemovalHandler() == 6;
        setting.setExtensionRequestHandler(8);
        assert setting.getExtensionRequestHandler() == 8;
    }
    
}
