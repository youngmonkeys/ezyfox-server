package com.tvd12.ezyfoxserver.setting;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.tvd12.ezyfox.io.EzyStrings;
import com.tvd12.ezyfox.util.EzyEquals;
import com.tvd12.ezyfox.util.EzyHashCodes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.NONE)
public abstract class EzyAbstractSetting 
        implements EzyBaseSetting, EzyZoneIdAware, EzyHomePathAware {

	protected final int id = newId();
	
	@XmlElement(name = "name")
	protected String name;
	
	@XmlElement(name = "folder")
    protected String folder;

	@XmlElement(name = "active-profiles")
    protected String activeProfiles;
	
	@XmlElement(name = "package-name")
    protected String packageName;
	
	protected int zoneId;
	
	@XmlElement(name = "entry-loader")
	protected String entryLoader;
	
	@XmlElement(name = "thread-pool-size")
	protected int threadPoolSize = 0;
	
	@XmlElement(name = "config-file")
	protected String configFile = "config.properties";
	
	protected String homePath = "";
	
	protected Object[] entryLoaderArgs;
	
	protected int newId() {
	    return getIdCounter().incrementAndGet();
	}
	
	public void setEntryLoader(Class<?> loaderClass) {
	    this.entryLoader = loaderClass.getName();
	}
	
	public void setEntryLoader(String loaderClass) {
        this.entryLoader = loaderClass;
    }
	
	public void setEntryLoaderArgs(Object[] args) {
	    this.entryLoaderArgs = args;
	}
	
	@Override
	public String getFolder() {
	    return EzyStrings.isNoContent(folder) ? name : folder;
	}
	
	@Override
	public String getLocation() {
	    return Paths.get(homePath, getParentFolder(), getFolder()).toString();
	}
	
	@Override
    public String getConfigFile() {
        return getConfigFile(false);
    }
	
	@Override
	public String getConfigFileInput() {
	    return getConfigFile(true);
	}
	
	@Override
	public String getConfigFile(boolean noParent) {
	    if(noParent)
	        return configFile;
	    return Paths.get(getLocation(), configFile).toString();
	}

	protected abstract String getParentFolder();
	protected abstract AtomicInteger getIdCounter();
	
	@Override
    public boolean equals(Object obj) {
        return new EzyEquals<EzyAbstractSetting>()
                .function(t -> t.id)
                .isEquals(this, obj);
    }
	
	@Override
	public int hashCode() {
	    return new EzyHashCodes().append(id).toHashCode();
	}
	
	@Override
	public Map<Object, Object> toMap() {
	    Map<Object, Object> map = new HashMap<>();
	    map.put("id", id);
	    map.put("name", name);
	    map.put("folder", getFolder());
	    map.put("packageName", packageName);
	    map.put("activeProfiles", activeProfiles);
	    map.put("entryLoader", entryLoader);
	    map.put("threadPoolSize", threadPoolSize);
	    map.put("configFile", configFile != null ? configFile : "");
	    return map;
	}
	
}
