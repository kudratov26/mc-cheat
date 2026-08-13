package net.cheatclient.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MinecraftClient.class)
public interface MinecraftClientAccessor {
    @Invoker("doAttack")
    boolean cheatclient$invokeDoAttack();

    @Invoker("doItemUse")
    void cheatclient$invokeDoItemUse();

    @Accessor("itemUseCooldown")
    void cheatclient$setItemUseCooldown(int cooldown);
}