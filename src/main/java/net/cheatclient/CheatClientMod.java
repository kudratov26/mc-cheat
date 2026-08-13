package net.cheatclient;

import net.cheatclient.feature.FeatureManager;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheatClientMod implements ModInitializer {
    public static final String MOD_ID = "cheatclient";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static FeatureManager featureManager;

    @Override
    public void onInitialize() {
        featureManager = new FeatureManager();
        featureManager.clientInit();
        LOGGER.info("{} initialized", MOD_ID);
    }

    public static FeatureManager getFeatureManager() {
        return featureManager;
    }
}