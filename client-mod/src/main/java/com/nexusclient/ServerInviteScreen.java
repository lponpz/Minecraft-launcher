package com.nexusclient;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

/**
 * Friend invite helper. This first version creates shareable server invites and
 * lets a recipient paste one to connect. Automatic cross-device delivery needs
 * a separately hosted relay service and is intentionally not faked here.
 */
public final class ServerInviteScreen extends Screen {
    private TextFieldWidget serverField;
    private TextFieldWidget friendField;
    private String status = "Enter a server address to prepare an invite.";

    public ServerInviteScreen() {
        super(Text.literal("Nexus Client — Friend Invites"));
    }

    @Override
    protected void init() {
        int w = Math.min(300, width - 30);
        int left = (width - w) / 2;
        int top = Math.max(50, height / 2 - 85);
        friendField = new TextFieldWidget(textRenderer, left, top, w, 20, Text.literal("Friend name"));
        friendField.setMaxLength(32);
        friendField.setPlaceholder(Text.literal("Friend's Minecraft name (optional)"));
        addDrawableChild(friendField);
        serverField = new TextFieldWidget(textRenderer, left, top + 30, w, 20, Text.literal("Server address"));
        serverField.setMaxLength(255);
        serverField.setPlaceholder(Text.literal("play.example.net:25565"));
        addDrawableChild(serverField);

        addDrawableChild(ButtonWidget.builder(Text.literal("Copy Invite"), b -> createInvite())
            .dimensions(left, top + 60, w, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Paste Invite & Join"), b -> pasteAndJoin())
            .dimensions(left, top + 85, w, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Close"), b -> close())
            .dimensions(width / 2 - 50, height - 32, 100, 20).build());
    }

    private void createInvite() {
        String address = serverField.getText().trim();
        if (address.isEmpty() || address.contains(" ")) {
            status = "Enter a valid server address first.";
            return;
        }
        String friend = friendField.getText().trim();
        String invite = "NEXUS_INVITE|" + (friend.isEmpty() ? "friend" : friend) + "|" + address;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.keyboard != null) {
            client.keyboard.setClipboard(invite);
            status = "Invite copied! Send it to your friend in chat.";
        } else {
            status = "Clipboard unavailable. Invite: " + invite;
        }
    }

    private void pasteAndJoin() {
        MinecraftClient client = MinecraftClient.getInstance();
        String pasted = client.keyboard == null ? "" : client.keyboard.getClipboard().trim();
        if (!pasted.startsWith("NEXUS_INVITE|")) {
            status = "Clipboard has no Nexus invite. Ask your friend to copy and send one.";
            return;
        }
        String[] parts = pasted.split("\\|", 3);
        if (parts.length != 3 || parts[2].isBlank() || parts[2].contains(" ")) {
            status = "Invite format is invalid.";
            return;
        }
        String address = parts[2].trim();
        client.setScreen(null);
        client.execute(() -> {
            var server = net.minecraft.client.network.ServerAddress.parse(address);
            var info = net.minecraft.client.network.ServerInfo("Friend's server", address,
                net.minecraft.client.network.ServerInfo.ServerType.OTHER);
            net.minecraft.client.gui.screen.multiplayer.ConnectScreen.connect(
                new net.minecraft.client.gui.screen.Screen(Text.literal("Connecting")) {
                    @Override protected void init() {}
                    @Override public void render(DrawContext context, int mouseX, int mouseY, float delta) {
                        renderBackground(context, mouseX, mouseY, delta);
                        context.drawCenteredTextWithShadow(textRenderer, Text.literal("Connecting to friend's server..."),
                            width / 2, height / 2, 0xFFFFFF);
                        super.render(context, mouseX, mouseY, delta);
                    }
                    @Override public boolean shouldPause() { return false; }
                }, client, server, info, false, null);
        });
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 20, 0xFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer,
            Text.literal("Copy the invite and send it to your friend."), width / 2, 38, 0xAAAAAA);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal(status),
            width / 2, height - 55, 0xFFCC66);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override public boolean shouldPause() { return false; }
}
