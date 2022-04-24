package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.util.EzyToMap;

import java.util.List;

public interface EzyAdminsSetting extends EzyToMap {

    List<EzyAdminSetting> getAdmins();

    EzyAdminSetting getAdminByName(String username);

    EzyAdminSetting getAdminByAccessToken(String token);

    boolean containsAdminByName(String username);

    boolean containsAdminByAccessToken(String token);
}
