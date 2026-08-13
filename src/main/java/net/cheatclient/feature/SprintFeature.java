package net.cheatclient.feature;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.lwjgl.glfw.GLFW;

public class SprintFeature implements EnableableFeature {
    private boolean enabled;

    @Override
    public String getName() {
        return "Sprint";
    }

    @Override
    public Category getCategory() {
        return Category.MOVEMENT;
    }

    @Override
    public String getDescription() {
        return "Always sprint when moving forward.";
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
        return GLFW.GLFW_KEY_V;
    }

    @Override
    public void onTick() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            player.setSprinting(player.input.movementForward > 0);
        }
    }
}