package com.cheatclient.mixin;

import com.cheatclient.CheatClientMod;
import com.cheatclient.CheatMenuHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ClientPlayerEntity.class)
public class PlayerTickMixin {
    
    private long lastAttackTime = 0;
    private long lastClickTime = 0;
    private static final double KILL_AURA_RANGE = 4.0;
    private static final double TRIGGER_BOT_RANGE = 3.5;
    private static final int AUTO_CLICKER_CPS = 15;
    
    @Inject(method = "tick", at = @At("HEAD"))
    private void onPlayerTick(CallbackInfo ci) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        
        if (player.getWorld() == null || !player.isAlive()) return;
        
        // Check for Right Shift key press to open menu
        MinecraftClient client = MinecraftClient.getInstance();
        long window = client.getWindow().getHandle();
        
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS) {
            if (!menuOpen) {
                menuOpen = true;
                if (client.player != null) {
                    client.setScreen(new CheatMenuHandler(client));
                }
            }
        } else {
            menuOpen = false;
        }
        
        // Kill Aura implementation
        if (CheatClientMod.getInstance().isFeatureActive(CheatClientMod.FEATURE_KILL_AURA)) {
            performKillAura(player);
        }
        
        // Trigger Bot implementation
        if (CheatClientMod.getInstance().isFeatureActive(CheatClientMod.FEATURE_TRIGGER_BOT)) {
            performTriggerBot(player);
        }
        
        // Auto Clicker implementation
        if (CheatClientMod.getInstance().isFeatureActive(CheatClientMod.FEATURE_AUTO_CLICKER)) {
            performAutoClicker(player);
        }
    }
    
    private void performKillAura(ClientPlayerEntity player) {
        // Find nearest hostile entity within range
        List<Entity> nearbyEntities = player.getWorld().getOtherEntities(player, player.getBoundingBox().expand(KILL_AURA_RANGE));
        
        LivingEntity target = null;
        double closestDistance = Double.MAX_VALUE;
        
        for (Entity entity : nearbyEntities) {
            if (entity instanceof LivingEntity && ((LivingEntity) entity).isAlive()) {
                double distance = player.squaredDistanceTo(entity);
                if (distance < closestDistance) {
                    closestDistance = distance;
                    target = (LivingEntity) entity;
                }
            }
        }
        
        if (target != null) {
            // Auto-attack target without aim assist - just hit regardless of crosshair
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastAttackTime > 100) {
                player.attack(target);
                lastAttackTime = currentTime;
            }
        }
    }
    
    private void performTriggerBot(ClientPlayerEntity player) {
        // Check if player is looking at an entity
        HitResult hitResult = player.raycast(TRIGGER_BOT_RANGE, 0, false);
        
        if (hitResult.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHit = (EntityHitResult) hitResult;
            Entity target = entityHit.getEntity();
            
            if (target instanceof LivingEntity && ((LivingEntity) target).isAlive()) {
                // Auto-attack without aim assist - just hit when crosshair is on target
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastAttackTime > 100) {
                    player.attack((LivingEntity) target);
                    lastAttackTime = currentTime;
                }
            }
        }
    }
    
    private void performAutoClicker(ClientPlayerEntity player) {
        long currentTime = System.currentTimeMillis();
        long clickInterval = 1000 / AUTO_CLICKER_CPS;
        
        if (currentTime - lastClickTime >= clickInterval) {
            // Simulate left click
            if (player.getAttackCooldownProgress(0) >= 1.0f) {
                // This would hook into the mouse click event
                // For now, this is a placeholder for where click injection would occur
                lastClickTime = currentTime;
            }
        }
    }
    
    private static boolean menuOpen = false;
}
