package com.nexusclient;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
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
        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.nexusclient.open_menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                KeyBinding.Category.MISC
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new NexusClientMenuScreen());
                }
            }
        });

        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
            if (clientPlayerPresent()) {
                var client = net.minecraft.client.MinecraftClient.getInstance();
                if (showCoordinates && client.player != null) {
                    var pos = client.player.getBlockPos();
                    drawContext.drawTextWithShadow(client.textRenderer,
                            Text.literal("XYZ: " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ()),
                            6, 6, 0xFFFFFF);
                }
                if (compactHud && client.player != null) {
                    drawContext.drawTextWithShadow(client.textRenderer,
                            Text.literal("HP: " + Math.round(client.player.getHealth()) +
                                    " | Food: " + client.player.getHungerManager().getFoodLevel()),
                            6, showCoordinates ? 18 : 6, 0xFFFFFF);
                }
            }
        });
    }

    private static boolean clientPlayerPresent() {
        var client = net.minecraft.client.MinecraftClient.getInstance();
        return client != null && client.player != null && client.world != null;
    }
}
