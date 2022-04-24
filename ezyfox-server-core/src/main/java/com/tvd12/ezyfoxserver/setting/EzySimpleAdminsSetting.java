package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.io.EzyStrings;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Setter
@Getter
@ToString
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "administrators")
public class EzySimpleAdminsSetting implements EzyAdminsSetting {

    protected List<EzyAdminSetting> admins = new ArrayList<>();
    protected Map<String, EzyAdminSetting> adminsByName = new ConcurrentHashMap<>();
    protected Map<String, EzyAdminSetting> adminsByAccessToken = new ConcurrentHashMap<>();

    @XmlElement(name = "administrator")
    public void setItem(EzySimpleAdminSetting item) {
        if (!EzyStrings.isNoContent(item.getUsername())) {
            adminsByName.put(item.getUsername(), item);
        }
        if (!EzyStrings.isNoContent(item.getAccessToken())) {
            adminsByAccessToken.put(item.getAccessToken(), item);
        }
        admins.add(item);
    }

    @Override
    public List<EzyAdminSetting> getAdmins() {
        return new ArrayList<>(adminsByName.values());
    }

    @Override
    public EzyAdminSetting getAdminByName(String username) {
        return adminsByName.get(username);
    }

    @Override
    public EzyAdminSetting getAdminByAccessToken(String token) {
        return adminsByAccessToken.get(token);
    }

    @Override
    public boolean containsAdminByName(String username) {
        return adminsByName.containsKey(username);
    }

    @Override
    public boolean containsAdminByAccessToken(String token) {
        return adminsByAccessToken.containsKey(token);
    }

    @Override
    public Map<Object, Object> toMap() {
        Map<Object, Object> map = new HashMap<>();
        List<Object> adminMaps = new ArrayList<>();
        for (EzyAdminSetting admin : adminsByName.values()) {
            adminMaps.add(admin.toMap());
        }
        map.put("count", adminsByName.size());
        map.put("admins", adminMaps);
        return map;
    }
}
