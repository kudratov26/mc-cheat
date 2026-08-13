package net.cheatclient.mixin;

import net.cheatclient.CheatClientMod;
import net.cheatclient.feature.FeatureManager;
import net.cheatclient.feature.FlyFeature;
import net.cheatclient.feature.SpeedFeature;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity extends LivingEntity {
    protected MixinClientPlayerEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void cheatclient$applyCheats(CallbackInfo ci) {
        FeatureManager fm = CheatClientMod.getFeatureManager();
        if (fm == null) {
            return;
        }

        ClientPlayerEntity self = (ClientPlayerEntity) (Object) this;
        FlyFeature fly = fm.get(FlyFeature.class);
        SpeedFeature speed = fm.get(SpeedFeature.class);

        boolean moving = self.input.movementForward != 0 || self.input.movementSideways != 0;

        // Fly: behave like creative flight.
        if (fly != null && fly.isEnabled()) {
            self.getAbilities().flying = true;
            self.getAbilities().allowFlying = true;
            self.getAbilities().setFlySpeed(0.08F);

            double motionY;
            if (self.input.jumping) {
                motionY = 0.6D;
            } else if (self.input.sneaking) {
                motionY = -0.6D;
            } else {
                motionY = 0.0D;
            }
            setVelocity(getVelocity().x, motionY, getVelocity().z);
            self.fallDistance = 0.0F;
        }

        // Speed: scale horizontal velocity while moving.
        if (speed != null && speed.isEnabled() && moving) {
            double boost = 1.25D;
            setVelocity(getVelocity().x * boost, getVelocity().y, getVelocity().z * boost);
            self.setSprinting(true);
        }
    }
}