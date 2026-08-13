package net.cheatclient.feature;

import org.lwjgl.glfw.GLFW;

public class FastPlaceFeature implements EnableableFeature {
    private boolean enabled;

    @Override
    public String getName() {
        return "FastPlace";
    }

    @Override
    public Category getCategory() {
        return Category.PLAYER;
    }

    @Override
    public String getDescription() {
        return "Removes the item-use cooldown for rapid block placement.";
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
        return GLFW.GLFW_KEY_B;
    }
}