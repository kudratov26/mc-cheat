package com.cheatclient;

import net.fabricmc.api.ClientModInitializer;

import java.util.HashMap;
import java.util.Map;

public class CheatClientMod implements ClientModInitializer {
    private static CheatClientMod instance;
    private final Map<String, Boolean> featureStates = new HashMap<>();
    
    public static final String FEATURE_AUTO_CLICKER = "autoClicker";
    public static final String FEATURE_KILL_AURA = "killAura";
    public static final String FEATURE_TRIGGER_BOT = "triggerBot";
    public static final String FEATURE_FLY = "fly";
    public static final String FEATURE_SPEED = "speed";
    public static final String FEATURE_NO_FALL = "noFall";
    public static final String FEATURE_AUTO_SOUP = "autoSoup";
    
    @Override
    public void onInitializeClient() {
        instance = this;
        
        // Initialize all features as disabled
        featureStates.put(FEATURE_AUTO_CLICKER, false);
        featureStates.put(FEATURE_KILL_AURA, false);
        featureStates.put(FEATURE_TRIGGER_BOT, false);
        featureStates.put(FEATURE_FLY, false);
        featureStates.put(FEATURE_SPEED, false);
        featureStates.put(FEATURE_NO_FALL, false);
        featureStates.put(FEATURE_AUTO_SOUP, false);
        
        System.out.println("[Omni-Injector] Cheat Client Mod initialized!");
    }
    
    public static CheatClientMod getInstance() {
        return instance;
    }
    
    public boolean isFeatureActive(String featureName) {
        return featureStates.getOrDefault(featureName, false);
    }
    
    public void setFeatureActive(String featureName, boolean active) {
        featureStates.put(featureName, active);
        System.out.println("[Omni-Injector] Feature " + featureName + " set to: " + active);
    }
    
    public void toggleFeature(String featureName) {
        boolean current = isFeatureActive(featureName);
        setFeatureActive(featureName, !current);
    }
}
