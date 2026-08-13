package net.cheatclient.feature;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.lwjgl.glfw.GLFW;

public class FlyFeature implements EnableableFeature {
    private boolean enabled;

    @Override
    public String getName() {
        return "Fly";
    }

    @Override
    public String getCategory() {
        return FeatureManager.CATEGORY;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public int getDefaultKey() {
        return GLFW.GLFW_KEY_G;
    }

    @Override
    public void onTick() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) {
            return;
        }
        player.getAbilities().flying = true;
        player.getAbilities().allowFlying = true;
    }
}