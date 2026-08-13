package net.cheatclient.gui;

import net.cheatclient.CheatClientMod;
import net.cheatclient.feature.EnableableFeature;
import net.cheatclient.feature.EnableableFeature.Category;
import net.cheatclient.feature.FeatureManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The in-game ClickGUI toggled with Right Shift. A translucent, animated
 * panel (Haruka-style) listing cheats grouped by category; clicking a module
 * toggles it.
 */
public class CheatMenuHandler extends Screen {
    private static final int WINDOW_W = 380;
    private static final int WINDOW_H = 300;
    private static final int TAB_W = 96;
    private static final int ROW_H = 38;

    private final Map<EnableableFeature, Float> hoverProgress = new HashMap<>();
    private int selectedTab = 0;
    private int windowX;
    private int windowY;

    public CheatMenuHandler() {
        super(Text.literal("Cheat Client"));
    }

    @Override
    protected void init() {
        windowX = (width - WINDOW_W) / 2;
        windowY = (height - WINDOW_H) / 2;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient client = MinecraftClient.getInstance();

        // Dim the world behind the panel.
        context.fill(0, 0, width, height, 0x66000000);

        // Window body.
        context.fill(windowX, windowY, windowX + WINDOW_W, windowY + WINDOW_H, 0xE0121420);
        context.fill(windowX, windowY, windowX + WINDOW_W, windowY + 2, 0xFF3d5bff);

        // Title.
        context.drawText(client.textRenderer,
                Text.literal("Cheat Client").formatted(Formatting.BOLD, Formatting.WHITE),
                windowX + 10, windowY + 10, 0xFFFFFFFF, true);

        FeatureManager fm = CheatClientMod.getFeatureManager();
        if (fm == null) {
            return;
        }

        Category[] categories = Category.values();
        if (selectedTab >= categories.length) {
            selectedTab = 0;
        }

        int tabY = windowY + 30;
        for (int i = 0; i < categories.length; i++) {
            Category cat = categories[i];
            boolean hovered = isMouseIn(mouseX, mouseY, windowX, tabY, TAB_W, 20);
            int bg = i == selectedTab ? 0x803d5bff : (hovered ? 0x40FFFFFF : 0x20000000);
            context.fill(windowX, tabY, windowX + TAB_W, tabY + 20, bg);
            context.drawText(client.textRenderer,
                    Text.literal(cat.getDisplayName()).formatted(cat.getColor(), Formatting.BOLD),
                    windowX + 6, tabY + 5, 0xFFFFFFFF, true);
            tabY += 22;
        }

        // Module list in the content area.
        List<EnableableFeature> modules = new ArrayList<>();
        for (EnableableFeature feature : fm.getFeatures()) {
            if (feature.getCategory() == categories[selectedTab]) {
                modules.add(feature);
            }
        }

        int contentX = windowX + TAB_W + 8;
        int contentW = WINDOW_W - TAB_W - 16;
        int y = windowY + 30;
        for (EnableableFeature module : modules) {
            if (y + ROW_H > windowY + WINDOW_H) {
                break;
            }
            boolean hovered = isMouseIn(mouseX, mouseY, contentX, y, contentW, ROW_H);
            float target = hovered ? 1.0F : 0.0F;
            float progress = hoverProgress.merge(module, target, (a, b) -> a + (b - a) * 0.35F);

            int bg = module.isEnabled() ? 0x604dff8c : 0x30000000;
            int offset = (int) (progress * 3);
            context.fill(contentX - offset, y, contentX + contentW, y + ROW_H, bg);

            Formatting catColor = module.getCategory().getColor();
            context.drawText(client.textRenderer,
                    Text.literal(module.getName()).formatted(catColor),
                    contentX + 8 - offset, y + 6, 0xFFFFFFFF, true);

            String status = module.isEnabled() ? "ON " : "OFF";
            Formatting statusColor = module.isEnabled() ? Formatting.GREEN : Formatting.GRAY;
            int statusW = client.textRenderer.getWidth(status);
            context.drawText(client.textRenderer,
                    Text.literal(status).formatted(statusColor, Formatting.BOLD),
                    contentX + contentW - statusW - 8, y + 6, 0xFFFFFFFF, true);

            String desc = module.getDescription();
            context.drawText(client.textRenderer,
                    Text.literal(desc).formatted(Formatting.GRAY),
                    contentX + 8 - offset, y + 22, 0xFFFFFFFF, false);

            y += ROW_H + 4;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Category[] categories = Category.values();

        // Check category tabs.
        int tabY = windowY + 30;
        for (int i = 0; i < categories.length; i++) {
            if (isMouseIn((int) mouseX, (int) mouseY, windowX, tabY, TAB_W, 20)) {
                selectedTab = i;
                return true;
            }
            tabY += 22;
        }

        // Check module rows.
        int contentX = windowX + TAB_W + 8;
        int contentW = WINDOW_W - TAB_W - 16;
        int y = windowY + 30;
        List<EnableableFeature> modules = getModules(categories[selectedTab]);
        for (EnableableFeature module : modules) {
            if (isMouseIn((int) mouseX, (int) mouseY, contentX, y, contentW, ROW_H)) {
                CheatClientMod.getFeatureManager().toggle(module);
                return true;
            }
            y += ROW_H + 4;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private List<EnableableFeature> getModules(Category category) {
        List<EnableableFeature> result = new ArrayList<>();
        for (EnableableFeature feature : CheatClientMod.getFeatureManager().getFeatures()) {
            if (feature.getCategory() == category) {
                result.add(feature);
            }
        }
        return result;
    }

    private boolean isMouseIn(int mouseX, int mouseY, int x, int y, int w, int h) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }
}