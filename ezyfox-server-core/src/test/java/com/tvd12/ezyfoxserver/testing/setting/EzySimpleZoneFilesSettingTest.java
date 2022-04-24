package com.tvd12.ezyfoxserver.testing.setting;

import com.tvd12.ezyfoxserver.setting.EzySimpleZoneFilesSetting;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import java.util.ArrayList;

public class EzySimpleZoneFilesSettingTest extends BaseTest {

    @Test
    public void test() {
        EzySimpleZoneFilesSetting setting = new EzySimpleZoneFilesSetting();
        setting.setZoneFiles(new ArrayList<>());
    }

}
