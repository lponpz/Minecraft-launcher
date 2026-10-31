package com.nexusclient;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.LinkedHashMap;
import java.util.Map;

public final class NexusClientMenuScreen extends Screen {
    private final Map<String, Boolean> toggles = new LinkedHashMap<>();
    private int page = 0;

    public NexusClientMenuScreen() {
        super(Text.literal("Nexus Client — Mod Menu"));
        toggles.put("Compact HUD (prototype)", false);
        toggles.put("Reduced visual effects (prototype)", false);
        toggles.put("Show coordinates (prototype)", false);
        toggles.put("Performance profile (prototype)", false);
        toggles.put("Custom HUD layout (planned)", false);
        toggles.put("Feature search (planned)", false);
        toggles.put("Profiles (planned)", false);
        toggles.put("Keybind editor (planned)", false);
    }

    @Override
    protected void init() {
        clearChildren();
        int left = this.width / 2 - 120;
        int top = this.height / 2 - 70;

        int index = 0;
        for (String name : toggles.keySet()) {
            final String key = name;
            int y = top + (index * 24);
            if (y > this.height - 45) break;
            addDrawableChild(ButtonWidget.builder(
                    Text.literal(name + ": " + (toggles.get(name) ? "ON" : "OFF")),
                    button -> {
                        toggles.put(key, !toggles.get(key));
                        button.setMessage(Text.literal(key + ": " + (toggles.get(key) ? "ON" : "OFF")));
                    }
            ).dimensions(left, y, 240, 20).build());
            index++;
        }

        addDrawableChild(ButtonWidget.builder(Text.literal("Close"), button -> close())
                .dimensions(this.width / 2 - 50, this.height - 30, 100, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 18, 0xFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer,
                Text.literal("Foundation build — not all planned features are implemented"),
                width / 2, 34, 0xFFCC66);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
