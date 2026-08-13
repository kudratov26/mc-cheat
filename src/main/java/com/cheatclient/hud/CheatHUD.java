package com.cheatclient.hud;

import com.cheatclient.CheatClientMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class CheatHUD {
    private static CheatHUD instance;
    private final MinecraftClient client;
    private final List<HUDFeature> features = new ArrayList<>();
    private float animationProgress = 0f;
    private boolean visible = true;
    
    public CheatHUD() {
        this.client = MinecraftClient.getInstance();
        initializeFeatures();
    }
    
    public static CheatHUD getInstance() {
        if (instance == null) {
            instance = new CheatHUD();
        }
        return instance;
    }
    
    private void initializeFeatures() {
        features.add(new HUDFeature("Auto Clicker", CheatClientMod.FEATURE_AUTO_CLICKER, 0xFFFF5555));
        features.add(new HUDFeature("Kill Aura", CheatClientMod.FEATURE_KILL_AURA, 0xFFFF5555));
        features.add(new HUDFeature("Trigger Bot", CheatClientMod.FEATURE_TRIGGER_BOT, 0xFFFF5555));
        features.add(new HUDFeature("Fly", CheatClientMod.FEATURE_FLY, 0xFF55FFFF));
        features.add(new HUDFeature("Speed", CheatClientMod.FEATURE_SPEED, 0xFF55FF55));
        features.add(new HUDFeature("No Fall", CheatClientMod.FEATURE_NO_FALL, 0xFF55FF55));
        features.add(new HUDFeature("Auto Soup", CheatClientMod.FEATURE_AUTO_SOUP, 0xFFFFAA00));
    }
    
    public void render(DrawContext context, float delta) {
        if (!visible || client.options.hudHidden) return;
        
        // Update animation
        animationProgress += delta * 0.1f;
        if (animationProgress > 1f) animationProgress = 1f;
        
        TextRenderer textRenderer = client.textRenderer;
        int x = 5;
        int y = 5;
        int spacing = 15;
        
        // Draw header
        drawHeader(context, textRenderer, x, y);
        y += 20;
        
        // Draw active features with animation
        for (HUDFeature feature : features) {
            if (CheatClientMod.getInstance().isFeatureActive(feature.featureName)) {
                float alpha = Math.min(animationProgress, 1f);
                drawFeature(context, textRenderer, feature, x, y, alpha);
                y += spacing;
            }
        }
    }
    
    private void drawHeader(DrawContext context, TextRenderer textRenderer, int x, int y) {
        String header = "§c§lOmni-Injector";
        context.drawText(textRenderer, Text.of(header), x, y, 0xFFFFFFFF, true);
    }
    
    private void drawFeature(DrawContext context, TextRenderer textRenderer, HUDFeature feature, int x, int y, float alpha) {
        String text = "§7" + feature.name + " §a§lON";
        int color = feature.color;
        
        // Apply alpha to color
        int alphaColor = ((int) (alpha * 255) << 24) | (color & 0x00FFFFFF);
        
        // Draw background with rounded corners effect
        int textWidth = textRenderer.getWidth(text);
        context.fill(x - 2, y - 1, x + textWidth + 2, y + 9, 0x80000000);
        
        // Draw text
        context.drawText(textRenderer, Text.of(text), x, y, alphaColor, true);
    }
    
    public void toggle() {
        visible = !visible;
        animationProgress = 0f; // Reset animation
    }
    
    public boolean isVisible() {
        return visible;
    }
    
    private static class HUDFeature {
        final String name;
        final String featureName;
        final int color;
        
        HUDFeature(String name, String featureName, int color) {
            this.name = name;
            this.featureName = featureName;
            this.color = color;
        }
    }
}
