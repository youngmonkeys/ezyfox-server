package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfoxserver.setting.EzyAppSetting;
import com.tvd12.ezyfoxserver.wrapper.EzyAppUserManager;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzySimpleApplication implements EzyApplication {

    protected EzyAppSetting setting;
    protected EzyAppUserManager userManager;
    
}
