package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfox.json.EzyJsonWriter;
import com.tvd12.ezyfox.json.EzySimpleJsonWriter;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.api.EzyResponseApi;
import com.tvd12.ezyfoxserver.api.EzyResponseApiAware;
import com.tvd12.ezyfoxserver.api.EzyStreamingApi;
import com.tvd12.ezyfoxserver.api.EzyStreamingApiAware;
import com.tvd12.ezyfoxserver.config.EzyConfig;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.statistics.EzyStatistics;
import com.tvd12.ezyfoxserver.wrapper.EzyServerControllers;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManagerAware;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 */
@Setter
@Getter
@SuppressWarnings("rawtypes")
public class EzySimpleServer
    extends EzyComponent
    implements EzyServer, EzyResponseApiAware, EzyStreamingApiAware, EzySessionManagerAware, EzyDestroyable {

    protected EzyConfig config;
    protected EzySettings settings;
    protected ClassLoader classLoader;
    protected EzyStatistics statistics;
    protected EzyServerControllers controllers;
    protected EzyResponseApi responseApi;
    protected EzyStreamingApi streamingApi;
    protected EzySessionManager sessionManager;
    protected Map<String, ClassLoader> appClassLoaders;

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public void destroy() {
        super.destroy();
        ((EzyDestroyable) sessionManager).destroy();
    }

    @Override
    public String toString() {
        EzyJsonWriter writer = new EzySimpleJsonWriter();
        String json = writer.writeAsString(this);
        return json;
    }

    @Override
    public Map<Object, Object> toMap() {
        Map<Object, Object> map = new HashMap<>();
        map.put("version", getVersion());
        map.put("config", config.toMap());
        map.put("settings", settings.toMap());
        return map;
    }
}
