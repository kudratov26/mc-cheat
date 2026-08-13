package net.cheatclient.feature;

import net.cheatclient.CheatClientMod;
import net.cheatclient.gui.CheatMenuHandler;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

/**
 * Registry of cheat features. Owns each feature's hotkey, the ClickGUI
 * hotkey (Right Shift) and the global tick loop that feeds
 * {@link EnableableFeature#onTick()} to active modules.
 */
public class FeatureManager {
    public static final String CATEGORY = "Cheat Client";
    public static final int MENU_KEY = GLFW.GLFW_KEY_RIGHT_SHIFT;

    private final List<EnableableFeature> features = new ArrayList<>();
    private final List<EnableableFeature> activeFeatures = new ArrayList<>();
    private final List<KeyBinding> keyBindings = new ArrayList<>();
    private KeyBinding menuKeyBinding;

    /** Registers a feature and its hotkey with the client. */
    public <T extends EnableableFeature> T register(T feature) {
        features.add(feature);
        KeyBinding key = feature.createKeyBinding();
        KeyBindingHelper.registerKeyBinding(key);
        keyBindings.add(key);
        return feature;
    }

    /** Registers the ClickGUI toggle hotkey (Right Shift). */
    private void registerMenuKey() {
        menuKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.cheatclient.menu",
                InputUtil.Type.KEYSYM,
                MENU_KEY,
                CATEGORY));
    }

    /** Initialises registrations and the tick loop. Called from onInitialize. */
    public void clientInit() {
        registerMenuKey();

        register(new FlyFeature());
        register(new SpeedFeature());
        register(new SprintFeature());
        register(new NoFallFeature());

        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);

        CheatClientMod.LOGGER.info("Registered {} features", features.size());
    }

    private void onTick(MinecraftClient client) {
        if (client.player != null) {
            updateToggles();
            activeFeatures.forEach(EnableableFeature::onTick);
        }
        toggleMenu(client);
    }

    private void toggleMenu(MinecraftClient client) {
        if (menuKeyBinding.wasPressed()) {
            if (client.currentScreen instanceof CheatMenuHandler) {
                client.setScreen(null);
            } else if (client.currentScreen == null) {
                client.setScreen(new CheatMenuHandler());
            }
        }
    }

    private void updateToggles() {
        for (int i = 0; i < features.size(); i++) {
            EnableableFeature feature = features.get(i);
            if (keyBindings.get(i).wasPressed()) {
                toggle(feature);
            }
        }
    }

    /** Turns a feature on or off, keeping the active set in sync. */
    public void setEnabled(EnableableFeature feature, boolean enabled) {
        boolean wasEnabled = feature.isEnabled();
        if (wasEnabled == enabled) {
            return;
        }
        feature.setEnabled(enabled);
        if (enabled) {
            activeFeatures.add(feature);
        } else {
            activeFeatures.remove(feature);
        }
    }

    /** Flips a feature's active state. */
    public void toggle(EnableableFeature feature) {
        setEnabled(feature, !feature.isEnabled());
    }

    /** Visible copy of the active features, for the HUD. */
    public List<EnableableFeature> getActiveFeatures() {
        return new ArrayList<>(activeFeatures);
    }

    /** All registered features. */
    public List<EnableableFeature> getFeatures() {
        return new ArrayList<>(features);
    }

    /** Convenience lookup used by mixins. */
    @SuppressWarnings("unchecked")
    public <T extends EnableableFeature> T get(Class<T> type) {
        return (T) features.stream().filter(type::isInstance).findFirst().orElse(null);
    }
}