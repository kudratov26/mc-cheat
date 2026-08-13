package com.cheatclient;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class CheatMenuHandler extends Screen {
    private final MinecraftClient client;
    private float animationProgress = 0f;
    private float targetAnimation = 1f;
    
    public CheatMenuHandler(MinecraftClient client) {
        super(Text.of("Omni-Injector Cheat Menu"));
        this.client = client;
    }
    
    @Override
    protected void init() {
        int buttonWidth = 220;
        int buttonHeight = 24;
        int startY = 60;
        int spacing = 30;
        
        // Auto Clicker
        this.addDrawableChild(createStyledButton(
            "Auto Clicker", CheatClientMod.FEATURE_AUTO_CLICKER,
            this.width / 2 - buttonWidth / 2, startY, buttonWidth, buttonHeight
        ));
        
        // Kill Aura
        this.addDrawableChild(createStyledButton(
            "Kill Aura", CheatClientMod.FEATURE_KILL_AURA,
            this.width / 2 - buttonWidth / 2, startY + spacing, buttonWidth, buttonHeight
        ));
        
        // Trigger Bot
        this.addDrawableChild(createStyledButton(
            "Trigger Bot", CheatClientMod.FEATURE_TRIGGER_BOT,
            this.width / 2 - buttonWidth / 2, startY + spacing * 2, buttonWidth, buttonHeight
        ));
        
        // Fly
        this.addDrawableChild(createStyledButton(
            "Fly", CheatClientMod.FEATURE_FLY,
            this.width / 2 - buttonWidth / 2, startY + spacing * 3, buttonWidth, buttonHeight
        ));
        
        // Speed
        this.addDrawableChild(createStyledButton(
            "Speed", CheatClientMod.FEATURE_SPEED,
            this.width / 2 - buttonWidth / 2, startY + spacing * 4, buttonWidth, buttonHeight
        ));
        
        // No Fall Damage
        this.addDrawableChild(createStyledButton(
            "No Fall", CheatClientMod.FEATURE_NO_FALL,
            this.width / 2 - buttonWidth / 2, startY + spacing * 5, buttonWidth, buttonHeight
        ));
        
        // Auto Soup
        this.addDrawableChild(createStyledButton(
            "Auto Soup", CheatClientMod.FEATURE_AUTO_SOUP,
            this.width / 2 - buttonWidth / 2, startY + spacing * 6, buttonWidth, buttonHeight
        ));
        
        // Close button
        this.addDrawableChild(ButtonWidget.builder(
            Text.of("§cClose"),
            button -> {
                targetAnimation = 0f;
            }
        ).dimensions(this.width / 2 - buttonWidth / 2, startY + spacing * 8, buttonWidth, buttonHeight).build());
    }
    
    private ButtonWidget createStyledButton(String name, String featureName, int x, int y, int width, int height) {
        boolean isActive = CheatClientMod.getInstance().isFeatureActive(featureName);
        String status = isActive ? "§a§lON" : "§c§lOFF";
        String buttonText = "§7" + name + " §8| " + status;
        
        return ButtonWidget.builder(
            Text.of(buttonText),
            button -> {
                CheatClientMod.getInstance().toggleFeature(featureName);
                boolean newState = CheatClientMod.getInstance().isFeatureActive(featureName);
                String newStatus = newState ? "§a§lON" : "§c§lOFF";
                button.setMessage(Text.of("§7" + name + " §8| " + newStatus));
            }
        ).dimensions(x, y, width, height).build();
    }
    
    @Override
    public void render(net.minecraft.client.gui.DrawContext context, int mouseX, int mouseY, float delta) {
        // Update animation
        animationProgress += (targetAnimation - animationProgress) * 0.15f * delta;
        
        // Close menu when animation completes
        if (targetAnimation == 0f && animationProgress < 0.01f) {
            this.close();
            return;
        }
        
        // Render animated background
        renderAnimatedBackground(context, delta);
        
        // Render title with animation
        float titleAlpha = Math.min(animationProgress * 1.5f, 1f);
        context.drawCenteredTextWithShadow(this.textRenderer, Text.of("§c§lOmni-Injector"), this.width / 2, (int)(30 * animationProgress), 0xFFFFFF);
        
        // Render buttons with animation
        for (int i = 0; i < this.children().size(); i++) {
            if (this.children().get(i) instanceof ButtonWidget button) {
                float buttonAnimation = Math.max(0, Math.min((animationProgress - i * 0.1f) * 2f, 1f));
                if (buttonAnimation > 0) {
                    button.setY((int)(button.getY() * buttonAnimation));
                }
            }
        }
        
        super.render(context, mouseX, mouseY, delta);
    }
    
    private void renderAnimatedBackground(net.minecraft.client.gui.DrawContext context, float delta) {
        int width = this.width;
        int height = this.height;
        float alpha = animationProgress * 0.85f;
        
        // Main background with gradient effect
        int bgColor = ((int)(alpha * 180) << 24) | 0x100010;
        context.fill(0, 0, width, height, bgColor);
        
        // Animated border
        int borderColor = ((int)(alpha * 255) << 24) | 0xFF3333;
        int borderThickness = 2;
        context.fill(0, 0, width, borderThickness, borderColor);
        context.fill(0, height - borderThickness, width, height, borderColor);
        context.fill(0, 0, borderThickness, height, borderColor);
        context.fill(width - borderThickness, 0, width, height, borderColor);
        
        // Animated corner accents
        int accentColor = ((int)(alpha * 255) << 24) | 0xFF5555;
        context.fill(0, 0, 50, 3, accentColor);
        context.fill(0, 0, 3, 50, accentColor);
        context.fill(width - 50, height - 3, width, height, accentColor);
        context.fill(width - 3, height - 50, width, height, accentColor);
    }
    
    @Override
    public boolean shouldPause() {
        return false;
    }
}
