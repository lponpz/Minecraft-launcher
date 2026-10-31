package com.nexusclient;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public final class NexusClientMenuScreen extends Screen {
    public NexusClientMenuScreen() { super(Text.literal("Nexus Client — Laptop Edition")); }

    @Override
    protected void init() {
        clearChildren();
        int left = this.width / 2 - 115;
        int top = this.height / 2 - 58;
        addDrawableChild(ButtonWidget.builder(coordinateLabel(), button -> {
            NexusClientMod.showCoordinates = !NexusClientMod.showCoordinates;
            NexusClientMod.saveSettings();
            button.setMessage(coordinateLabel());
        }).dimensions(left, top, 230, 20).build());
        addDrawableChild(ButtonWidget.builder(hudLabel(), button -> {
            NexusClientMod.compactHud = !NexusClientMod.compactHud;
            NexusClientMod.saveSettings();
            button.setMessage(hudLabel());
        }).dimensions(left, top + 25, 230, 20).build());
        addDrawableChild(ButtonWidget.builder(healthLabel(), button -> {
            NexusClientMod.healthIndicator = !NexusClientMod.healthIndicator;
            NexusClientMod.saveSettings();
            button.setMessage(healthLabel());
        }).dimensions(left, top + 50, 230, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Close"), button -> close())
            .dimensions(this.width / 2 - 50, this.height - 32, 100, 20).build());
    }

    private static Text coordinateLabel() {
        return Text.literal("Coordinates HUD: " + (NexusClientMod.showCoordinates ? "ON" : "OFF"));
    }
    private static Text healthLabel() {
        return Text.literal("Health Indicator: " + (NexusClientMod.healthIndicator ? "ON" : "OFF"));
    }
    private static Text hudLabel() {
        return Text.literal("Compact Health/Food HUD: " + (NexusClientMod.compactHud ? "ON" : "OFF"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 18, 0xFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("Right Shift opens this menu"),
            width / 2, 36, 0xAAAAAA);
        context.drawCenteredTextWithShadow(textRenderer,
            Text.literal("Implemented: " + FeatureCatalog.implementedCount() + " / 752 planned features"),
            width / 2, height / 2 + 40, 0xFFCC66);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() { return false; }
}