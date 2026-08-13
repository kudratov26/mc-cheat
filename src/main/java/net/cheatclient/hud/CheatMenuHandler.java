package net.cheatclient.hud;

import net.cheatclient.CheatClientMod;
import net.cheatclient.feature.EnableableFeature;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

/**
 * Renders the in-game ArrayList HUD (top-left) listing every enabled cheat.
 */
@Environment(EnvType.CLIENT)
public final class CheatMenuHandler {
    private CheatMenuHandler() {
    }

    public static void render(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null || client.options.hudHidden) {
            return;
        }

        List<EnableableFeature> active = CheatClientMod.getFeatureManager().getActiveFeatures();
        if (active.isEmpty()) {
            return;
        }

        int x = 4;
        int y = 4;
        int lineHeight = 10;

        for (EnableableFeature feature : active) {
            String name = feature.getName();
            Formatting color = feature.isEnabled() ? Formatting.GREEN : Formatting.YELLOW;
            context.drawText(client.textRenderer, Text.literal(name).formatted(color), x, y, 0xFFFFFFFF, true);
            y += lineHeight;
        }
    }
}