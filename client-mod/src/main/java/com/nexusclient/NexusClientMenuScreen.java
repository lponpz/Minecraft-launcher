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
        int top = Math.max(42, this.height / 2 - 88);
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
        addDrawableChild(ButtonWidget.builder(fpsLabel(), button -> {
            NexusClientMod.showFps = !NexusClientMod.showFps;
            NexusClientMod.saveSettings();
            button.setMessage(fpsLabel());
        }).dimensions(left, top + 75, 230, 20).build());
        addDrawableChild(ButtonWidget.builder(armorLabel(), button -> {
            NexusClientMod.showArmor = !NexusClientMod.showArmor;
            NexusClientMod.saveSettings();
            button.setMessage(armorLabel());
        }).dimensions(left, top + 100, 230, 20).build());
        addDrawableChild(ButtonWidget.builder(durabilityLabel(), button -> {
            NexusClientMod.showDurability = !NexusClientMod.showDurability;
            NexusClientMod.saveSettings();
            button.setMessage(durabilityLabel());
        }).dimensions(left, top + 125, 230, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Close"), button -> close())
            .dimensions(this.width / 2 - 50, this.height - 32, 100, 20).build());
    }

    private static Text coordinateLabel() {
        return Text.literal("Coordinates HUD: " + (NexusClientMod.showCoordinates ? "ON" : "OFF"));
    }
    private static Text healthLabel() {
        return Text.literal("Health Indicator: " + (NexusClientMod.healthIndicator ? "ON" : "OFF"));
    }
    private static Text fpsLabel() {
        return Text.literal("FPS Counter: " + (NexusClientMod.showFps ? "ON" : "OFF"));
    }
    private static Text armorLabel() {
        return Text.literal("Armor Indicator: " + (NexusClientMod.showArmor ? "ON" : "OFF"));
    }
    private static Text durabilityLabel() {
        return Text.literal("Armor Durability: " + (NexusClientMod.showDurability ? "ON" : "OFF"));
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
            width / 2, height - 48, 0xFFCC66);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() { return false; }
}