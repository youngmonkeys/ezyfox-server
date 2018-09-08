package com.tvd12.ezyfoxserver.setting;

import static com.tvd12.ezyfoxserver.setting.EzyFolderNamesSetting.APPS;
import static com.tvd12.ezyfoxserver.setting.EzyFolderNamesSetting.ENTRIES;

import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "application")
@JsonIgnoreProperties(value = {"zoneId"}, ignoreUnknown = true)
@JsonPropertyOrder({"id", "name", "entryLoader"})
public class EzySimpleAppSetting extends EzyAbstractSetting implements EzyAppSetting {

    @XmlElement(name = "max-users")
    protected int maxUsers = 999999;
    
	private static final AtomicInteger COUNTER = new AtomicInteger(0);
	
	@Override
	protected AtomicInteger getIdCounter() {
		return COUNTER;
	}
	
	@Override
	public String getLocation() {
	    return Paths.get(homePath, APPS, ENTRIES, name).toString();
	}
}
