package com.nexusclient;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public final class NexusClientMod implements ClientModInitializer {
    private static KeyBinding openMenuKey;
    public static boolean showCoordinates = false;
    public static boolean compactHud = false;

    @Override
    public void onInitializeClient() {
        ClientConfig.load();
        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.nexusclient.open_menu", InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT, KeyBinding.Category.MISC));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.wasPressed()) {
                if (client.currentScreen == null) client.setScreen(new NexusClientMenuScreen());
            }
        });

        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null || client.world == null) return;
            int y = 6;
            if (showCoordinates) {
                var pos = client.player.getBlockPos();
                drawContext.drawTextWithShadow(client.textRenderer,
                    Text.literal("XYZ: " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ()),
                    6, y, 0xFFFFFF);
                y += 12;
            }
            if (compactHud) {
                drawContext.drawTextWithShadow(client.textRenderer,
                    Text.literal("HP: " + Math.round(client.player.getHealth()) + " | Food: " +
                        client.player.getHungerManager().getFoodLevel()), 6, y, 0xFFFFFF);
            }
        });
    }

    public static void saveSettings() { ClientConfig.save(); }
}