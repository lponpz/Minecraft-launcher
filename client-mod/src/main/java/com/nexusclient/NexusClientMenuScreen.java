package com.nexusclient;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.ParticlesMode;
import net.minecraft.text.Text;

public final class NexusClientMenuScreen extends Screen {
    private String status = "Choose a profile or customize each HUD item.";
    public NexusClientMenuScreen() { super(Text.literal("Nexus Client — Laptop Edition")); }

    @Override
    protected void init() {
        clearChildren();
        int buttonWidth = Math.min(220, Math.max(120, (width - 36) / 2));
        int gap = 8;
        int totalWidth = buttonWidth * 2 + gap;
        int left = Math.max(8, (width - totalWidth) / 2);
        int right = left + buttonWidth + gap;
        int row = 22;
        int top = 54;

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

        int toolsY = top + row * 5;
        addDrawableChild(ButtonWidget.builder(Text.literal("HUD Up"), b -> { NexusClientMod.hudOffsetY = Math.max(-100, NexusClientMod.hudOffsetY - 5); ClientConfig.save(); status = "HUD vertical offset: " + NexusClientMod.hudOffsetY; })
            .dimensions(left, toolsY, buttonWidth, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("HUD Down"), b -> { NexusClientMod.hudOffsetY = Math.min(100, NexusClientMod.hudOffsetY + 5); ClientConfig.save(); status = "HUD vertical offset: " + NexusClientMod.hudOffsetY; })
            .dimensions(right, toolsY, buttonWidth, 20).build());
        addDrawableChild(ButtonWidget.builder(viewDistanceLabel(), b -> {
            var options = MinecraftClient.getInstance().options;
            int current = options.getViewDistance().getValue();
            int next = current >= 12 ? 2 : current >= 8 ? 12 : current >= 6 ? 8 : current >= 4 ? 6 : 4;
            options.getViewDistance().setValue(next);
            options.write();
            status = "Render distance set to " + next + " chunks.";
            b.setMessage(viewDistanceLabel());
        }).dimensions(left, toolsY + 23, buttonWidth, 20).build());
        addDrawableChild(ButtonWidget.builder(particlesLabel(), b -> {
            var option = MinecraftClient.getInstance().options.getParticles();
            ParticlesMode current = option.getValue();
            ParticlesMode next = current == ParticlesMode.ALL ? ParticlesMode.DECREASED
                : current == ParticlesMode.DECREASED ? ParticlesMode.MINIMAL : ParticlesMode.ALL;
            option.setValue(next);
            MinecraftClient.getInstance().options.write();
            status = "Particle setting: " + next.name().toLowerCase(java.util.Locale.ROOT) + ".";
            b.setMessage(particlesLabel());
        }).dimensions(right, toolsY + 23, buttonWidth, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Minimal Profile"), b -> { ClientConfig.applyProfile("minimal"); status = "Minimal profile applied."; clearAndInit(); })
            .dimensions(left, toolsY + 46, buttonWidth, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Balanced Profile"), b -> { ClientConfig.applyProfile("balanced"); status = "Balanced profile applied."; clearAndInit(); })
            .dimensions(right, toolsY + 46, buttonWidth, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Full HUD Profile"), b -> { ClientConfig.applyProfile("full"); status = "Full HUD profile applied."; clearAndInit(); })
            .dimensions(left, toolsY + 69, buttonWidth, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Copy Settings"), b -> {
            MinecraftClient c = MinecraftClient.getInstance();
            if (c.keyboard != null) { c.keyboard.setClipboard(ClientConfig.exportSettings()); status = "Settings copied to clipboard."; }
            else status = "Clipboard unavailable.";
        }).dimensions(right, toolsY + 69, buttonWidth, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Import Clipboard"), b -> {
            MinecraftClient c = MinecraftClient.getInstance();
            if (c.keyboard != null && ClientConfig.importSettings(c.keyboard.getClipboard())) { status = "Settings imported."; clearAndInit(); }
            else status = "Clipboard has no valid Nexus settings export.";
        }).dimensions(left, toolsY + 92, buttonWidth, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Friend Server Invites"), b -> this.client.setScreen(new ServerInviteScreen()))
            .dimensions(right, toolsY + 115, buttonWidth, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Reset Settings"), b -> { ClientConfig.resetDefaults(); status = "Settings reset."; clearAndInit(); })
            .dimensions(left, toolsY + 115, buttonWidth, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Close"), b -> close())
            .dimensions(right, toolsY + 92, buttonWidth, 20).build());
    }

    private ButtonWidget toggle(int x, int y, int w, Text label, Runnable action, java.util.function.Supplier<Text> supplier) {
        return ButtonWidget.builder(label, button -> { action.run(); ClientConfig.save(); button.setMessage(supplier.get()); })
            .dimensions(x, y, w, 20).build();
    }
    private static Text viewDistanceLabel() {
        return Text.literal("Render Distance: " + MinecraftClient.getInstance().options.getViewDistance().getValue() + " chunks");
    }
    private static Text particlesLabel() {
        return Text.literal("Particles: " + MinecraftClient.getInstance().options.getParticles().getValue().name().toLowerCase(java.util.Locale.ROOT));
    }
    private static String onOff(boolean value) { return value ? "ON" : "OFF"; }
    private static Text coordinateLabel() { return Text.literal("Coordinates HUD: " + onOff(NexusClientMod.showCoordinates)); }
    private static Text hudLabel() { return Text.literal("Compact Health/Food: " + onOff(NexusClientMod.compactHud)); }
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
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 16, 0xFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("Right Shift opens this menu"), width / 2, 32, 0xAAAAAA);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal(status), width / 2, height - 38, 0xFFCC66);
        super.render(context, mouseX, mouseY, delta);
    }
    @Override public boolean shouldPause() { return false; }
}