package net.cheatclient.feature;

import net.cheatclient.mixin.MinecraftClientAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.lwjgl.glfw.GLFW;

public class AutoClickerFeature implements EnableableFeature {
    private boolean enabled;
    private int tickCounter;

    @Override
    public String getName() {
        return "AutoClicker";
    }

    @Override
    public Category getCategory() {
        return Category.COMBAT;
    }

    @Override
    public String getDescription() {
        return "Automatically left-clicks at a high rate.";
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        EnableableFeature.super.setEnabled(enabled);
        this.tickCounter = 0;
        this.enabled = enabled;
    }

    @Override
    public int getDefaultKey() {
        return GLFW.GLFW_KEY_K;
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity player = mc.player;
        if (player == null || player.isUsingItem()) {
            return;
        }

        tickCounter++;
        int interval = Math.max(1, 20 / 10);
        if (tickCounter % interval == 0) {
            mc.attackCooldown = 0;
            ((MinecraftClientAccessor) mc).cheatclient$invokeDoAttack();
        }
    }
}