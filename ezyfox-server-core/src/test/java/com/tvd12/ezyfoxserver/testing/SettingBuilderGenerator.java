package com.tvd12.ezyfoxserver.testing;

import com.tvd12.ezyfox.tool.EzyBuilderCreator;
import com.tvd12.ezyfoxserver.setting.EzySimpleUserManagementSetting;

public class SettingBuilderGenerator {

    public static void main(String[] args) throws Exception {
        System.out.println(new EzyBuilderCreator()
                .buildBySetter(true)
                .create(EzySimpleUserManagementSetting.class, "EzyUserManagementSettingBuilder"));
    }
    
}
