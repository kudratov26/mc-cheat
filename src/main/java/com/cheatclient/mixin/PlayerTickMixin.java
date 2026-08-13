package com.cheatclient.mixin;

import com.cheatclient.CheatClientMod;
import com.cheatclient.config.CheatConfig;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(ClientPlayerEntity.class)
public class PlayerTickMixin {
    
    private long lastAttackTime = 0;
    private long lastClickTime = 0;
    
    @Inject(method = "tick", at = @At("HEAD"))
    private void onPlayerTick(CallbackInfo ci) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        
        if (player.getWorld() == null || !player.isAlive()) return;
        
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
        
        // Auto Soup implementation
        if (CheatClientMod.getInstance().isFeatureActive(CheatClientMod.FEATURE_AUTO_SOUP)) {
            performAutoSoup(player);
        }
    }
    
    private void performKillAura(ClientPlayerEntity player) {
        // Find nearest hostile entity within range
        List<Entity> nearbyEntities = player.getWorld().getOtherEntities(player, player.getBoundingBox().expand(CheatConfig.KILL_AURA_RANGE));
        
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
            if (currentTime - lastAttackTime > (CheatConfig.TEMP_ATTACK_DELAY_TICKS * 50)) {
                player.attack(target);
                lastAttackTime = currentTime;
            }
        }
    }
    
    private void performTriggerBot(ClientPlayerEntity player) {
        // Check if player is looking at an entity
        HitResult hitResult = player.raycast(CheatConfig.TRIGGER_BOT_RANGE, 0, false);
        
        if (hitResult.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHit = (EntityHitResult) hitResult;
            Entity target = entityHit.getEntity();
            
            if (target instanceof LivingEntity && ((LivingEntity) target).isAlive()) {
                // Auto-attack without aim assist - just hit when crosshair is on target
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastAttackTime > (CheatConfig.TEMP_ATTACK_DELAY_TICKS * 50)) {
                    player.attack((LivingEntity) target);
                    lastAttackTime = currentTime;
                }
            }
        }
    }
    
    private void performAutoClicker(ClientPlayerEntity player) {
        long currentTime = System.currentTimeMillis();
        long clickInterval = 1000 / CheatConfig.AUTO_CLICKER_CPS;
        
        // TEMP_VALUE: Add randomization to avoid detection
        if (CheatConfig.TEMP_RANDOMIZE_TIMING) {
            clickInterval += (long) (Math.random() * 50 - 25);
        }
        
        if (currentTime - lastClickTime >= clickInterval) {
            // Simulate left click
            if (player.getAttackCooldownProgress(0) >= 1.0f) {
                // This would hook into the mouse click event
                // For now, this is a placeholder for where click injection would occur
                lastClickTime = currentTime;
            }
        }
    }
    
    private void performAutoSoup(ClientPlayerEntity player) {
        // Check if health is below threshold (4 hearts)
        if (player.getHealth() <= CheatConfig.AUTO_SOUP_HEALTH_THRESHOLD) {
            // Find mushroom soup in hotbar
            int soupSlot = -1;
            for (int i = 0; i < 9; i++) {
                if (player.getInventory().getStack(i).getItem() == Items.MUSHROOM_STEW) {
                    soupSlot = i;
                    break;
                }
            }
            
            if (soupSlot != -1) {
                // Switch to soup slot
                player.getInventory().selectedSlot = soupSlot;
                
                // Auto-eat soup (this would hook into right-click event)
                // For now, this is a placeholder for where item usage would be injected
                if (player.getMainHandStack().getItem() == Items.MUSHROOM_STEW) {
                    // Force eat soup
                    // This would typically be done through item usage mixin
                    // After eating, the bowl would be in hand - need to drop it
                }
            } else {
                // No soup in hotbar, try to find in inventory and move to hotbar
                int inventorySoupSlot = -1;
                for (int i = 9; i < 36; i++) {
                    if (player.getInventory().getStack(i).getItem() == Items.MUSHROOM_STEW) {
                        inventorySoupSlot = i;
                        break;
                    }
                }
                
                if (inventorySoupSlot != -1) {
                    // Find empty hotbar slot
                    int emptyHotbarSlot = -1;
                    for (int i = 0; i < 9; i++) {
                        if (player.getInventory().getStack(i).isEmpty()) {
                            emptyHotbarSlot = i;
                            break;
                        }
                    }
                    
                    if (emptyHotbarSlot != -1) {
                        // Move soup from inventory to hotbar
                        player.getInventory().main.set(inventorySoupSlot, player.getInventory().main.get(emptyHotbarSlot));
                        player.getInventory().main.set(emptyHotbarSlot, new net.minecraft.item.ItemStack(Items.MUSHROOM_STEW));
                    }
                }
            }
        }
    }
}
