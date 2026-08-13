package net.cheatclient.feature;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import org.lwjgl.glfw.GLFW;

public class TriggerBotFeature implements EnableableFeature {
    private boolean enabled;

    @Override
    public String getName() {
        return "TriggerBot";
    }

    @Override
    public Category getCategory() {
        return Category.COMBAT;
    }

    @Override
    public String getDescription() {
        return "Attacks automatically when an entity is under your crosshair.";
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
        return GLFW.GLFW_KEY_T;
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity player = mc.player;
        if (player == null) {
            return;
        }
        if (mc.crosshairTarget instanceof EntityHitResult hit
                && hit.getEntity() instanceof LivingEntity target
                && target.isAlive()
                && !target.isRemoved()
                && target != player) {
            mc.attackCooldown = 0;
            mc.interactionManager.attackEntity(player, target);
            player.swingHand(Hand.MAIN_HAND);
        }
    }
}