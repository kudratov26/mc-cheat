package com.cheatclient.mixin;

import com.cheatclient.CheatClientMod;
import com.cheatclient.config.CheatConfig;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    
    @Inject(method = "tickMovement", at = @At("HEAD"), cancellable = true)
    private void onTickMovement(CallbackInfo ci) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        
        if (CheatClientMod.getInstance().isFeatureActive(CheatClientMod.FEATURE_FLY)) {
            // Fly hack: Override gravity and allow controlled flight
            player.setNoGravity(true);
            
            // Apply upward velocity when jump key is pressed
            if (player.input.jumping) {
                player.addVelocity(0, CheatConfig.FLY_SPEED, 0);
            }
            // Apply downward velocity when sneak key is pressed
            if (player.input.sneaking) {
                player.addVelocity(0, -CheatConfig.FLY_SPEED, 0);
            }
        } else {
            player.setNoGravity(false);
        }
        
        if (CheatClientMod.getInstance().isFeatureActive(CheatClientMod.FEATURE_SPEED)) {
            // Speed hack: Multiply movement speed
            float speedMultiplier = (float) CheatConfig.SPEED_MULTIPLIER;
            
            // Apply speed boost to movement attributes
            if (player.hasStatusEffect(StatusEffects.SPEED)) {
                player.getAttributeInstance(net.minecraft.entity.attribute.EntityAttributes.GENERIC_MOVEMENT_SPEED)
                    .setBaseValue(0.1f * speedMultiplier * 2.0f);
            } else {
                player.getAttributeInstance(net.minecraft.entity.attribute.EntityAttributes.GENERIC_MOVEMENT_SPEED)
                    .setBaseValue(0.1f * speedMultiplier);
            }
        }
    }
    
    @Inject(method = "sendMovementPackets", at = @At("HEAD"))
    private void onSendMovementPackets(CallbackInfo ci) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        
        if (CheatClientMod.getInstance().isFeatureActive(CheatClientMod.FEATURE_SPEED)) {
            // Speed hack: Augment movement delta in packets
            // TEMP_VALUE: This multiplier may need adjustment based on server validation
            double deltaMultiplier = CheatConfig.TEMP_PACKET_DELTA_MULTIPLIER;
            
            // The actual packet modification happens in the packet sending logic
            // This is a placeholder for where packet interception would occur
            // In a full implementation, you would hook into PlayerMoveC2SPacket
        }
    }
    
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void onDamage(net.minecraft.entity.damage.DamageSource source, float amount, org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<Boolean> cir) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        
        if (CheatClientMod.getInstance().isFeatureActive(CheatClientMod.FEATURE_NO_FALL)) {
            // Check if damage is from falling by checking the damage source name
            if (source.getName().equals("fall") || source.getName().equals("fall_damage")) {
                cir.setReturnValue(false); // Cancel fall damage
            }
        }
    }
}
