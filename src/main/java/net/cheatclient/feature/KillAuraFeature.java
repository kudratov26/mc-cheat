package net.cheatclient.feature;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.GameMode;
import org.lwjgl.glfw.GLFW;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class KillAuraFeature implements EnableableFeature {
    private boolean enabled;

    @Override
    public String getName() {
        return "KillAura";
    }

    @Override
    public Category getCategory() {
        return Category.COMBAT;
    }

    @Override
    public String getDescription() {
        return "Attacks the nearest hostile entity within range.";
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
        return GLFW.GLFW_KEY_R;
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity player = mc.player;
        if (player == null || mc.world == null) {
            return;
        }

        double range = 4.5D;
        double rangeSq = range * range;
        Predicate<Entity> valid = e -> {
            if (!(e instanceof LivingEntity living)) {
                return false;
            }
            if (!living.isAlive() || living.isRemoved() || e == player) {
                return false;
            }
            if (living.isInvulnerable()) {
                return false;
            }
            if (living instanceof PlayerEntity other && (other.isSpectator() || other.isCreative())) {
                return false;
            }
            return player.squaredDistanceTo(living) <= rangeSq;
        };

        List<LivingEntity> targets = mc.world.getOtherEntities(player, player.getBoundingBox().expand(range), valid)
                .stream()
                .map(e -> (LivingEntity) e)
                .min(Comparator.comparingDouble(player::squaredDistanceTo))
                .map(List::of)
                .orElse(List.of());

        if (targets.isEmpty()) {
            return;
        }

        LivingEntity target = targets.get(0);
        mc.attackCooldown = 0;
        mc.interactionManager.attackEntity(player, target);
        player.swingHand(Hand.MAIN_HAND);
    }
}