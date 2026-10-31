package com.nexusclient;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public final class NexusClientMenuScreen extends Screen {
    public NexusClientMenuScreen() {
        super(Text.literal("Nexus Client — Laptop Edition"));
    }

    @Override
    protected void init() {
        clearChildren();
        int buttonWidth = Math.min(220, Math.max(120, (width - 36) / 2));
        int gap = 8;
        int totalWidth = buttonWidth * 2 + gap;
        int left = Math.max(8, (width - totalWidth) / 2);
        int right = left + buttonWidth + gap;
        int rowHeight = 24;
        int top = 58;

        addDrawableChild(toggle(left, top, buttonWidth, coordinateLabel(),
            () -> NexusClientMod.showCoordinates = !NexusClientMod.showCoordinates));
        addDrawableChild(toggle(right, top, buttonWidth, hudLabel(),
            () -> NexusClientMod.compactHud = !NexusClientMod.compactHud));

        addDrawableChild(toggle(left, top + rowHeight, buttonWidth, healthLabel(),
            () -> NexusClientMod.healthIndicator = !NexusClientMod.healthIndicator));
        addDrawableChild(toggle(right, top + rowHeight, buttonWidth, fpsLabel(),
            () -> NexusClientMod.showFps = !NexusClientMod.showFps));

        addDrawableChild(toggle(left, top + rowHeight * 2, buttonWidth, armorLabel(),
            () -> NexusClientMod.showArmor = !NexusClientMod.showArmor));
        addDrawableChild(toggle(right, top + rowHeight * 2, buttonWidth, durabilityLabel(),
            () -> NexusClientMod.showDurability = !NexusClientMod.showDurability));

        addDrawableChild(ButtonWidget.builder(Text.literal("Friend Server Invites"),
                button -> this.client.setScreen(new ServerInviteScreen()))
            .dimensions(left, top + rowHeight * 3, totalWidth, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Close"), button -> close())
            .dimensions(width / 2 - 50, height - 28, 100, 20).build());
    }

    private ButtonWidget toggle(int x, int y, int width, Text label, Runnable action) {
        return ButtonWidget.builder(label, button -> {
            action.run();
            ClientConfig.save();
            button.setMessage(labelFor(button.getMessage().getString()));
        }).dimensions(x, y, width, 20).build();
    }

    private Text labelFor(String current) {
        if (current.startsWith("Coordinates HUD:")) return coordinateLabel();
        if (current.startsWith("Compact Health/Food HUD:")) return hudLabel();
        if (current.startsWith("Health Indicator:")) return healthLabel();
        if (current.startsWith("FPS Counter:")) return fpsLabel();
        if (current.startsWith("Armor Indicator:")) return armorLabel();
        if (current.startsWith("Armor Durability:")) return durabilityLabel();
        return Text.literal(current);
    }

    private static Text coordinateLabel() {
        return Text.literal("Coordinates HUD: " + onOff(NexusClientMod.showCoordinates));
    }
    private static Text healthLabel() {
        return Text.literal("Health Indicator: " + onOff(NexusClientMod.healthIndicator));
    }
    private static Text fpsLabel() {
        return Text.literal("FPS Counter: " + onOff(NexusClientMod.showFps));
    }
    private static Text armorLabel() {
        return Text.literal("Armor Indicator: " + onOff(NexusClientMod.showArmor));
    }
    private static Text durabilityLabel() {
        return Text.literal("Armor Durability: " + onOff(NexusClientMod.showDurability));
    }
    private static Text hudLabel() {
        return Text.literal("Compact Health/Food HUD: " + onOff(NexusClientMod.compactHud));
    }
    private static String onOff(boolean value) { return value ? "ON" : "OFF"; }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 18, 0xFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("Right Shift opens this menu"),
            width / 2, 36, 0xAAAAAA);
        context.drawCenteredTextWithShadow(textRenderer,
            Text.literal("Roadmap: 752 features | implemented features are tracked separately"),
            width / 2, height - 48, 0xFFCC66);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() { return false; }
}
