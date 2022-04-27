package com.tvd12.ezyfoxserver.support.manager;

import com.tvd12.ezyfox.io.EzyMaps;
import com.tvd12.ezyfox.util.EzyDestroyable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EzyFeatureCommandManager implements EzyDestroyable {

    private final Map<String, String> featureByCommand =
        new ConcurrentHashMap<>();
    private final Map<String, Set<String>> commandsByFeature =
        new ConcurrentHashMap<>();

    public void addFeatureCommand(String feature, String command) {
        this.featureByCommand.put(command, feature);
        this.commandsByFeature.computeIfAbsent(
            feature,
            k -> ConcurrentHashMap.newKeySet()
        ).add(command);
    }

    public List<String> getFeatures() {
        return new ArrayList<>(commandsByFeature.keySet());
    }

    public String getFeatureByCommand(String command) {
        return featureByCommand.get(command);
    }

    public List<String> getCommandsByFeature(String feature) {
        return new ArrayList<>(commandsByFeature.get(feature));
    }

    public Map<String, String> getFeatureByCommandMap() {
        return new HashMap<>(featureByCommand);
    }

    public Map<String, List<String>> getCommandsByFeatureMap() {
        return EzyMaps.newHashMapNewValues(commandsByFeature, ArrayList::new);
    }

    @Override
    public void destroy() {
        this.featureByCommand.clear();
        this.commandsByFeature.clear();
    }
}
