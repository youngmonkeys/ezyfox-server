package com.tvd12.ezyfoxserver.testing.setting;

import com.tvd12.ezyfoxserver.setting.EzySimpleSslConfigSetting;
import com.tvd12.ezyfoxserver.setting.EzySslConfigSettingBuilder;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.util.RandomUtil;
import org.testng.annotations.Test;

public class EzySslConfigSettingBuilderTest {

    @Test
    public void test() {
        // given
        String file = RandomUtil.randomShortAlphabetString();
        String loader = RandomUtil.randomShortAlphabetString();
        String contextFactoryBuilder = RandomUtil.randomShortAlphabetString();
        EzySslConfigSettingBuilder sut = new EzySslConfigSettingBuilder()
            .file(file)
            .loader(loader)
            .contextFactoryBuilder(contextFactoryBuilder);

        // when
        EzySimpleSslConfigSetting setting = sut.build();

        // then
        EzySimpleSslConfigSetting expectation = new EzySimpleSslConfigSetting();
        expectation.setFile(file);
        expectation.setLoader(loader);
        expectation.setContextFactoryBuilder(contextFactoryBuilder);
        Asserts.assertEquals(expectation, setting);
    }
}
