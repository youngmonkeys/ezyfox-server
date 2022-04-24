package com.tvd12.ezyfoxserver.setting;

import static com.tvd12.ezyfoxserver.setting.EzyFolderNamesSetting.APPS;
import static com.tvd12.ezyfoxserver.setting.EzyFolderNamesSetting.ENTRIES;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "application")
public class EzySimpleAppSetting extends EzyAbstractSetting implements EzyAppSetting {

    @XmlElement(name = "max-users")
    protected int maxUsers = 999999;
    
    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    @Override
    protected AtomicInteger getIdCounter() {
        return COUNTER;
    }

    @Override
    protected String getParentFolder() {
        Path path = Paths.get(homePath, APPS, ENTRIES);
        return (Files.exists(path)
                ? Paths.get(APPS, ENTRIES)
                : Paths.get(APPS)).toString();
    }

    @Override
    public Map<Object, Object> toMap() {
         Map<Object, Object> map = super.toMap();
         map.put("maxUsers", maxUsers);
         return map;
    }
}
