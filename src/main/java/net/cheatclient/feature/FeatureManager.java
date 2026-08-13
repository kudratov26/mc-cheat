package net.cheatclient.feature;

import net.cheatclient.CheatClientMod;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Registry of cheat features. Owns each feature's hotkey and the global
 * tick loop that feeds {@link EnableableFeature#onTick()} to active modules.
 */
public class FeatureManager {
    public static final String CATEGORY = "Cheat Client";

    private final List<EnableableFeature> features = new ArrayList<>();
    private final List<EnableableFeature> activeFeatures = new ArrayList<>();
    private final List<KeyBinding> keyBindings = new ArrayList<>();

    /** Registers a feature and its hotkey with the client. */
    public <T extends EnableableFeature> T register(T feature) {
        features.add(feature);
        KeyBinding key = feature.createKeyBinding();
        KeyBindingHelper.registerKeyBinding(key);
        keyBindings.add(key);
        return feature;
    }

    /** Initialises registrations and the tick loop. Called from onInitialize. */
    public void clientInit() {
        register(new FlyFeature());
        register(new SpeedFeature());

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client == null || client.player == null) {
                return;
            }
            updateToggles();
            activeFeatures.forEach(EnableableFeature::onTick);
        });

        CheatClientMod.LOGGER.info("Registered {} features", features.size());
    }

    private void updateToggles() {
        for (int i = 0; i < features.size(); i++) {
            EnableableFeature feature = features.get(i);
            boolean wasEnabled = feature.isEnabled();
            if (keyBindings.get(i).wasPressed()) {
                feature.setEnabled(!wasEnabled);
            }
            boolean enabled = feature.isEnabled();
            if (enabled && !wasEnabled) {
                activeFeatures.add(feature);
            } else if (!enabled && wasEnabled) {
                activeFeatures.remove(feature);
            }
        }
    }

    /** Visible copy of the active features, for the HUD. */
    public List<EnableableFeature> getActiveFeatures() {
        return new ArrayList<>(activeFeatures);
    }

    /** Convenience lookup used by mixins. */
    @SuppressWarnings("unchecked")
    public <T extends EnableableFeature> T get(Class<T> type) {
        return (T) features.stream().filter(type::isInstance).findFirst().orElse(null);
    }
}