package net.cheatclient.feature;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.world.GameMode;
import org.lwjgl.glfw.GLFW;

public class FlyFeature implements EnableableFeature {
    private boolean enabled;

    @Override
    public String getName() {
        return "Fly";
    }

    @Override
    public Category getCategory() {
        return Category.MOVEMENT;
    }

    @Override
    public String getDescription() {
        return "Creative-style flight. Jump/Sneak to ascend and descend.";
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
        return GLFW.GLFW_KEY_G;
    }

    @Override
    public void onEnable() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            player.getAbilities().allowFlying = true;
        }
    }

    @Override
    public void onDisable() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) {
            return;
        }
        GameMode mode = MinecraftClient.getInstance().interactionManager == null
                ? GameMode.SURVIVAL
                : MinecraftClient.getInstance().interactionManager.getCurrentGameMode();
        if (mode != GameMode.CREATIVE) {
            player.getAbilities().flying = false;
            player.getAbilities().allowFlying = false;
        }
    }
}