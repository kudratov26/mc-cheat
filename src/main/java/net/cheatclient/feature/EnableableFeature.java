package net.cheatclient.feature;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

/**
 * A single togglable cheat feature. Instances are registered with the
 * {@link FeatureManager} which supplies their hotkey and tick loop.
 */
public interface EnableableFeature {
    enum Category {
        COMBAT("Combat", Formatting.RED),
        MOVEMENT("Movement", Formatting.AQUA),
        PLAYER("Player", Formatting.GREEN);

        private final String displayName;
        private final Formatting color;

        Category(String displayName, Formatting color) {
            this.displayName = displayName;
            this.color = color;
        }

        public String getDisplayName() {
            return displayName;
        }

        public Formatting getColor() {
            return color;
        }
    }

    /** The category this feature belongs to. */
    Category getCategory();

    /** Short display name shown in the ClickGUI and ArrayList HUD. */
    String getName();

    /** Longer description shown in the ClickGUI. */
    default String getDescription() {
        return "";
    }

    /** Whether the feature is currently active. */
    boolean isEnabled();

    /** Sets the active state, firing the enable/disable hooks on transition. */
    default void setEnabled(boolean enabled) {
        if (enabled == isEnabled()) {
            return;
        }
        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    /** Called once when the feature is turned on. */
    default void onEnable() {
    }

    /** Called once when the feature is turned off. */
    default void onDisable() {
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
                "Cheat Client");
    }
}