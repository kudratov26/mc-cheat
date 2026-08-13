package net.cheatclient.feature;

import net.cheatclient.mixin.MinecraftClientAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.StewItem;
import org.lwjgl.glfw.GLFW;

public class AutoSoupFeature implements EnableableFeature {
    private boolean enabled;

    @Override
    public String getName() {
        return "AutoSoup";
    }

    @Override
    public Category getCategory() {
        return Category.PLAYER;
    }

    @Override
    public String getDescription() {
        return "Eats soup automatically when health gets low.";
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
        return GLFW.GLFW_KEY_J;
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity player = mc.player;
        if (player == null) {
            return;
        }
        // Only heal when significantly damaged.
        if (player.getHealth() > player.getMaxHealth() - 6.0F) {
            return;
        }
        if (player.isUsingItem()) {
            return;
        }

        int soupSlot = findSoupSlot(player);
        if (soupSlot < 0) {
            return;
        }

        player.getInventory().selectedSlot = soupSlot;
        ((MinecraftClientAccessor) mc).cheatclient$setItemUseCooldown(0);
        ((MinecraftClientAccessor) mc).cheatclient$invokeDoItemUse();
    }

    private int findSoupSlot(ClientPlayerEntity player) {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack.getItem() instanceof StewItem) {
                return i;
            }
        }
        return -1;
    }
}