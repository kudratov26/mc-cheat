package net.cheatclient.feature;

import org.lwjgl.glfw.GLFW;

public class NoFallFeature implements EnableableFeature {
    private boolean enabled;

    @Override
    public String getName() {
        return "NoFall";
    }

    @Override
    public Category getCategory() {
        return Category.MOVEMENT;
    }

    @Override
    public String getDescription() {
        return "Prevents fall damage.";
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        EnableableFeature.super.setEnabled(enabled);
        this.enabled = enabled;
    }

    @Override
    public int getDefaultKey() {
        return GLFW.GLFW_KEY_F;
    }
}