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
        int buttonWidth = Math.min(220, Math.max(120, (width - 36) / 2));
        int gap = 8;
        int totalWidth = buttonWidth * 2 + gap;
        int left = Math.max(8, (width - totalWidth) / 2);
        int right = left + buttonWidth + gap;
        int row = 24;
        int top = 58;

        addDrawableChild(toggle(left, top, buttonWidth, coordinateLabel(), () -> NexusClientMod.showCoordinates = !NexusClientMod.showCoordinates, NexusClientMenuScreen::coordinateLabel));
        addDrawableChild(toggle(right, top, buttonWidth, hudLabel(), () -> NexusClientMod.compactHud = !NexusClientMod.compactHud, NexusClientMenuScreen::hudLabel));
        addDrawableChild(toggle(left, top + row, buttonWidth, healthLabel(), () -> NexusClientMod.healthIndicator = !NexusClientMod.healthIndicator, NexusClientMenuScreen::healthLabel));
        addDrawableChild(toggle(right, top + row, buttonWidth, fpsLabel(), () -> NexusClientMod.showFps = !NexusClientMod.showFps, NexusClientMenuScreen::fpsLabel));
        addDrawableChild(toggle(left, top + row * 2, buttonWidth, armorLabel(), () -> NexusClientMod.showArmor = !NexusClientMod.showArmor, NexusClientMenuScreen::armorLabel));
        addDrawableChild(toggle(right, top + row * 2, buttonWidth, durabilityLabel(), () -> NexusClientMod.showDurability = !NexusClientMod.showDurability, NexusClientMenuScreen::durabilityLabel));
        addDrawableChild(toggle(left, top + row * 3, buttonWidth, pingLabel(), () -> NexusClientMod.showPing = !NexusClientMod.showPing, NexusClientMenuScreen::pingLabel));
        addDrawableChild(toggle(right, top + row * 3, buttonWidth, directionLabel(), () -> NexusClientMod.showDirection = !NexusClientMod.showDirection, NexusClientMenuScreen::directionLabel));
        addDrawableChild(toggle(left, top + row * 4, buttonWidth, biomeLabel(), () -> NexusClientMod.showBiome = !NexusClientMod.showBiome, NexusClientMenuScreen::biomeLabel));
        addDrawableChild(toggle(right, top + row * 4, buttonWidth, clockLabel(), () -> NexusClientMod.showWorldClock = !NexusClientMod.showWorldClock, NexusClientMenuScreen::clockLabel));

        addDrawableChild(ButtonWidget.builder(Text.literal("Friend Server Invites"), button -> this.client.setScreen(new ServerInviteScreen()))
            .dimensions(left, top + row * 5, totalWidth, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Close"), button -> close())
            .dimensions(width / 2 - 50, height - 28, 100, 20).build());
    }

    private ButtonWidget toggle(int x, int y, int w, Text label, Runnable action, java.util.function.Supplier<Text> labelSupplier) {
        return ButtonWidget.builder(label, button -> {
            action.run();
            ClientConfig.save();
            button.setMessage(labelSupplier.get());
        }).dimensions(x, y, w, 20).build();
    }

    private static String onOff(boolean value) { return value ? "ON" : "OFF"; }
    private static Text coordinateLabel() { return Text.literal("Coordinates HUD: " + onOff(NexusClientMod.showCoordinates)); }
    private static Text hudLabel() { return Text.literal("Compact Health/Food HUD: " + onOff(NexusClientMod.compactHud)); }
    private static Text healthLabel() { return Text.literal("Health Indicator: " + onOff(NexusClientMod.healthIndicator)); }
    private static Text fpsLabel() { return Text.literal("FPS Counter: " + onOff(NexusClientMod.showFps)); }
    private static Text armorLabel() { return Text.literal("Armor Indicator: " + onOff(NexusClientMod.showArmor)); }
    private static Text durabilityLabel() { return Text.literal("Armor Durability: " + onOff(NexusClientMod.showDurability)); }
    private static Text pingLabel() { return Text.literal("Ping Display: " + onOff(NexusClientMod.showPing)); }
    private static Text directionLabel() { return Text.literal("Facing Direction: " + onOff(NexusClientMod.showDirection)); }
    private static Text biomeLabel() { return Text.literal("Biome Display: " + onOff(NexusClientMod.showBiome)); }
    private static Text clockLabel() { return Text.literal("World Clock: " + onOff(NexusClientMod.showWorldClock)); }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 18, 0xFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("Right Shift opens this menu"), width / 2, 36, 0xAAAAAA);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("Nexus Client | Minecraft Java 1.21.1"), width / 2, height - 48, 0xFFCC66);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() { return false; }
}