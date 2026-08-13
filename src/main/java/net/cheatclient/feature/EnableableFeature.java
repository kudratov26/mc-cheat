package net.cheatclient.feature;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

/**
 * A single togglable cheat feature. Instances are registered with the
 * {@link FeatureManager} which supplies their hotkey.
 */
public interface EnableableFeature {
    /** Category used when registering the feature's hotkey in controls. */
    String getCategory();

    /** Short display name shown in the ArrayList HUD. */
    String getName();

    /** Whether the feature is currently active. */
    boolean isEnabled();

    /** Sets the active state. */
    void setEnabled(boolean enabled);

    /** Whether a hotkey should be allocated for this feature. */
    default boolean hasKeybind() {
        return true;
    }

    /** The key the feature is toggled with by default (scancode). */
    default int getDefaultKey() {
        return GLFW.GLFW_KEY_UNKNOWN;
    }

    /** Fired every client tick while the feature is enabled. */
    default void onTick() {
    }

    /** Creates the hotkey for this feature. */
    default KeyBinding createKeyBinding() {
        return new KeyBinding(
                "key.cheatclient." + getName().toLowerCase().replace(' ', '_'),
                InputUtil.Type.KEYSYM,
                getDefaultKey(),
                getCategory());
    }
}