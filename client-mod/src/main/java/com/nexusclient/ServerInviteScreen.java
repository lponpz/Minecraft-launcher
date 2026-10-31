package com.nexusclient;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

/** Local clipboard-based server invite helper; it does not pretend to be an online relay. */
public final class ServerInviteScreen extends Screen {
    private static final String PREFIX = "NEXUS_INVITE|1|";
    private static final Pattern HOST_PATTERN =
        Pattern.compile("^(?=.{1,253}$)([A-Za-z0-9](?:[A-Za-z0-9.-]*[A-Za-z0-9])?)(?::([0-9]{1,5}))?$");

    private TextFieldWidget serverField;
    private TextFieldWidget friendField;
    private String status = "Enter a server address to prepare an invite.";

    public ServerInviteScreen() {
        super(Text.literal("Nexus Client — Friend Invites"));
    }

    @Override
    protected void init() {
        int w = Math.min(300, Math.max(160, width - 24));
        int left = (width - w) / 2;
        int top = Math.max(48, height / 2 - 78);

        friendField = new TextFieldWidget(textRenderer, left, top, w, 20, Text.literal("Friend name"));
        friendField.setMaxLength(32);
        friendField.setPlaceholder(Text.literal("Friend name (optional)"));
        addDrawableChild(friendField);

        serverField = new TextFieldWidget(textRenderer, left, top + 28, w, 20, Text.literal("Server address"));
        serverField.setMaxLength(255);
        serverField.setPlaceholder(Text.literal("play.example.net:25565"));
        addDrawableChild(serverField);

        addDrawableChild(ButtonWidget.builder(Text.literal("Copy Invite"), b -> createInvite())
            .dimensions(left, top + 56, w, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Paste Invite & Join"), b -> pasteAndJoin())
            .dimensions(left, top + 80, w, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Close"), b -> close())
            .dimensions(width / 2 - 50, height - 28, 100, 20).build());
    }

    private void createInvite() {
        String address = serverField.getText().trim();
        if (!validAddress(address)) {
            status = "Invalid address. Use hostname or hostname:port.";
            return;
        }
        String friend = friendField.getText().trim();
        if (friend.contains("|") || friend.contains("\n") || friend.contains("\r")) {
            status = "Friend name contains unsupported characters.";
            return;
        }
        String invite = PREFIX + (friend.isEmpty() ? "friend" : friend) + "|" + address;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.keyboard == null) {
            status = "Clipboard is unavailable on this screen.";
            return;
        }
        client.keyboard.setClipboard(invite);
        status = "Invite copied. Send the text to your friend.";
    }

    private void pasteAndJoin() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.keyboard == null) {
            status = "Clipboard is unavailable.";
            return;
        }
        String pasted = client.keyboard.getClipboard().trim();
        if (!pasted.startsWith(PREFIX)) {
            status = "No version 1 Nexus invite found in clipboard.";
            return;
        }
        String[] parts = pasted.split("\\|", 4);
        if (parts.length != 4 || !validAddress(parts[3])) {
            status = "Invite is malformed or contains an invalid server address.";
            return;
        }

        String address = parts[3].trim();
        status = "Connecting to " + address + "...";
        ServerAddress parsedAddress = ServerAddress.parse(address);
        ServerInfo info = new ServerInfo("Nexus friend server", address, ServerInfo.ServerType.OTHER);
        ConnectScreen.connect(this, client, parsedAddress, info, false, null);
    }

    private static boolean validAddress(String address) {
        if (address == null || address.isBlank() || address.contains("..")) return false;
        var matcher = HOST_PATTERN.matcher(address);
        if (!matcher.matches()) return false;
        String port = matcher.group(2);
        if (port != null) {
            try {
                int value = Integer.parseInt(port);
                return value >= 1 && value <= 65535;
            } catch (NumberFormatException ignored) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 20, 0xFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer,
            Text.literal("Clipboard invites only; no automatic relay is configured."),
            width / 2, 38, 0xAAAAAA);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal(status),
            width / 2, height - 48, 0xFFCC66);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() { return false; }
}
