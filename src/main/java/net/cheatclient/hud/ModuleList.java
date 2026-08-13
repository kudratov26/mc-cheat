package net.cheatclient.hud;

import net.cheatclient.CheatClientMod;
import net.cheatclient.feature.EnableableFeature;
import net.cheatclient.feature.FeatureManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The in-game ArrayList HUD. Enabled cheats slide in from the right edge of
 * the screen with a smooth easing animation (Haruka-style).
 */
public final class ModuleList {
    private static final List<AnimationEntry> ENTRIES = new ArrayList<>();
    private static int renderTick = 0;

    private ModuleList() {
    }

    public static void render(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null || client.options.hudHidden) {
            return;
        }

        FeatureManager fm = CheatClientMod.getFeatureManager();
        if (fm == null) {
            return;
        }

        List<EnableableFeature> active = fm.getActiveFeatures();
        renderTick++;

        // Remove entries whose feature has been disabled and has slid fully out.
        Iterator<AnimationEntry> it = ENTRIES.iterator();
        while (it.hasNext()) {
            AnimationEntry entry = it.next();
            if (!fm.getActiveFeatures().contains(entry.feature) && entry.offset <= -200) {
                it.remove();
            }
        }

        // Ensure every active feature has an entry.
        for (EnableableFeature feature : active) {
            if (ENTRIES.stream().noneMatch(e -> e.feature == feature)) {
                ENTRIES.add(new AnimationEntry(feature, -120));
            }
        }

        int screenWidth = client.getWindow().getScaledWidth();
        int y = 16;
        int lineHeight = 12;

        for (AnimationEntry entry : ENTRIES) {
            boolean isActive = active.contains(entry.feature);
            float target = isActive ? 0 : -140;
            entry.offset += (target - entry.offset) * 0.35F;
            if (entry.offset < -0.5F && !isActive) {
                continue;
            }

            String name = entry.feature.getName();
            Formatting color = entry.feature.getCategory().getColor();
            int textWidth = client.textRenderer.getWidth(name);
            int x = screenWidth - (int) entry.offset - textWidth - 8;

            // Background panel.
            context.fill(x - 2, y - 1, screenWidth - (int) entry.offset - 1, y + lineHeight - 3, 0x90091020);
            // Accent bar.
            context.fill(x - 2, y - 1, x + 1, y + lineHeight - 3, color.getColorValue());

            context.drawText(client.textRenderer, Text.literal(name).formatted(color), x, y, 0xFFFFFFFF, true);
            y += lineHeight;
        }
    }

    private static class AnimationEntry {
        final EnableableFeature feature;
        float offset;

        AnimationEntry(EnableableFeature feature, float offset) {
            this.feature = feature;
            this.offset = offset;
        }
    }
}