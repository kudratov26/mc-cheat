package com.cheatclient;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class CheatMenuHandler extends Screen {
    private final MinecraftClient client;
    
    public CheatMenuHandler(MinecraftClient client) {
        super(Text.of("Omni-Injector Cheat Menu"));
        this.client = client;
    }
    
    @Override
    protected void init() {
        int buttonWidth = 200;
        int buttonHeight = 20;
        int startY = 50;
        int spacing = 25;
        
        // Auto Clicker
        this.addDrawableChild(ButtonWidget.builder(
            Text.of("Auto Clicker: " + (CheatClientMod.getInstance().isFeatureActive(CheatClientMod.FEATURE_AUTO_CLICKER) ? "ON" : "OFF")),
            button -> {
                CheatClientMod.getInstance().toggleFeature(CheatClientMod.FEATURE_AUTO_CLICKER);
                button.setMessage(Text.of("Auto Clicker: " + (CheatClientMod.getInstance().isFeatureActive(CheatClientMod.FEATURE_AUTO_CLICKER) ? "ON" : "OFF")));
            }
        ).dimensions(this.width / 2 - buttonWidth / 2, startY, buttonWidth, buttonHeight).build());
        
        // Kill Aura
        this.addDrawableChild(ButtonWidget.builder(
            Text.of("Kill Aura: " + (CheatClientMod.getInstance().isFeatureActive(CheatClientMod.FEATURE_KILL_AURA) ? "ON" : "OFF")),
            button -> {
                CheatClientMod.getInstance().toggleFeature(CheatClientMod.FEATURE_KILL_AURA);
                button.setMessage(Text.of("Kill Aura: " + (CheatClientMod.getInstance().isFeatureActive(CheatClientMod.FEATURE_KILL_AURA) ? "ON" : "OFF")));
            }
        ).dimensions(this.width / 2 - buttonWidth / 2, startY + spacing, buttonWidth, buttonHeight).build());
        
        // Trigger Bot
        this.addDrawableChild(ButtonWidget.builder(
            Text.of("Trigger Bot: " + (CheatClientMod.getInstance().isFeatureActive(CheatClientMod.FEATURE_TRIGGER_BOT) ? "ON" : "OFF")),
            button -> {
                CheatClientMod.getInstance().toggleFeature(CheatClientMod.FEATURE_TRIGGER_BOT);
                button.setMessage(Text.of("Trigger Bot: " + (CheatClientMod.getInstance().isFeatureActive(CheatClientMod.FEATURE_TRIGGER_BOT) ? "ON" : "OFF")));
            }
        ).dimensions(this.width / 2 - buttonWidth / 2, startY + spacing * 2, buttonWidth, buttonHeight).build());
        
        // Fly
        this.addDrawableChild(ButtonWidget.builder(
            Text.of("Fly: " + (CheatClientMod.getInstance().isFeatureActive(CheatClientMod.FEATURE_FLY) ? "ON" : "OFF")),
            button -> {
                CheatClientMod.getInstance().toggleFeature(CheatClientMod.FEATURE_FLY);
                button.setMessage(Text.of("Fly: " + (CheatClientMod.getInstance().isFeatureActive(CheatClientMod.FEATURE_FLY) ? "ON" : "OFF")));
            }
        ).dimensions(this.width / 2 - buttonWidth / 2, startY + spacing * 3, buttonWidth, buttonHeight).build());
        
        // Speed
        this.addDrawableChild(ButtonWidget.builder(
            Text.of("Speed: " + (CheatClientMod.getInstance().isFeatureActive(CheatClientMod.FEATURE_SPEED) ? "ON" : "OFF")),
            button -> {
                CheatClientMod.getInstance().toggleFeature(CheatClientMod.FEATURE_SPEED);
                button.setMessage(Text.of("Speed: " + (CheatClientMod.getInstance().isFeatureActive(CheatClientMod.FEATURE_SPEED) ? "ON" : "OFF")));
            }
        ).dimensions(this.width / 2 - buttonWidth / 2, startY + spacing * 4, buttonWidth, buttonHeight).build());
        
        // No Fall Damage
        this.addDrawableChild(ButtonWidget.builder(
            Text.of("No Fall: " + (CheatClientMod.getInstance().isFeatureActive(CheatClientMod.FEATURE_NO_FALL) ? "ON" : "OFF")),
            button -> {
                CheatClientMod.getInstance().toggleFeature(CheatClientMod.FEATURE_NO_FALL);
                button.setMessage(Text.of("No Fall: " + (CheatClientMod.getInstance().isFeatureActive(CheatClientMod.FEATURE_NO_FALL) ? "ON" : "OFF")));
            }
        ).dimensions(this.width / 2 - buttonWidth / 2, startY + spacing * 5, buttonWidth, buttonHeight).build());
        
        // Auto Soup
        this.addDrawableChild(ButtonWidget.builder(
            Text.of("Auto Soup: " + (CheatClientMod.getInstance().isFeatureActive(CheatClientMod.FEATURE_AUTO_SOUP) ? "ON" : "OFF")),
            button -> {
                CheatClientMod.getInstance().toggleFeature(CheatClientMod.FEATURE_AUTO_SOUP);
                button.setMessage(Text.of("Auto Soup: " + (CheatClientMod.getInstance().isFeatureActive(CheatClientMod.FEATURE_AUTO_SOUP) ? "ON" : "OFF")));
            }
        ).dimensions(this.width / 2 - buttonWidth / 2, startY + spacing * 6, buttonWidth, buttonHeight).build());
        
        // Close button
        this.addDrawableChild(ButtonWidget.builder(
            Text.of("Close"),
            button -> this.close()
        ).dimensions(this.width / 2 - buttonWidth / 2, startY + spacing * 8, buttonWidth, buttonHeight).build());
    }
    
    @Override
    public void render(net.minecraft.client.gui.DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }
    
    @Override
    public boolean shouldPause() {
        return false;
    }
}
