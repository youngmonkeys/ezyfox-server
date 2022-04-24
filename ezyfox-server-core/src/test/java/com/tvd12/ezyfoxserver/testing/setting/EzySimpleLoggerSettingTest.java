package com.tvd12.ezyfoxserver.testing.setting;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.setting.EzySimpleLoggerSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleLoggerSetting.EzySimpleIgnoredCommandsSetting;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzySimpleLoggerSettingTest extends BaseTest {

    @Test
    public void test() {
        EzySimpleIgnoredCommandsSetting commandsSetting = new EzySimpleIgnoredCommandsSetting();
        commandsSetting.setCommand(EzyCommand.APP_ACCESS.toString());
        EzySimpleLoggerSetting setting = new EzySimpleLoggerSetting();
        setting.setIgnoredCommands(commandsSetting);
    }

}
