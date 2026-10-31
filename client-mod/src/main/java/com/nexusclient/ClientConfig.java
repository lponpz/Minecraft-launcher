package com.nexusclient;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/** Small persistent configuration for the first implemented client features. */
public final class ClientConfig {
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("nexus-client.properties");

    private ClientConfig() {}

    public static void load() {
        Properties p = new Properties();
        if (Files.isRegularFile(FILE)) {
            try (InputStream in = Files.newInputStream(FILE)) {
                p.load(in);
            } catch (IOException ignored) {
                // Keep safe defaults if the config is damaged or unreadable.
            }
        }
        NexusClientMod.showCoordinates = Boolean.parseBoolean(p.getProperty("hud.coordinates", "false"));
        NexusClientMod.compactHud = Boolean.parseBoolean(p.getProperty("hud.compact", "false"));
        NexusClientMod.healthIndicator = Boolean.parseBoolean(p.getProperty("hud.health_indicator", "true"));
        NexusClientMod.showFps = Boolean.parseBoolean(p.getProperty("hud.fps", "false"));
        NexusClientMod.showArmor = Boolean.parseBoolean(p.getProperty("hud.armor", "false"));
        NexusClientMod.showDurability = Boolean.parseBoolean(p.getProperty("hud.durability", "false"));
    }

    public static void save() {
        Properties p = new Properties();
        p.setProperty("hud.coordinates", Boolean.toString(NexusClientMod.showCoordinates));
        p.setProperty("hud.compact", Boolean.toString(NexusClientMod.compactHud));
        p.setProperty("hud.health_indicator", Boolean.toString(NexusClientMod.healthIndicator));
        p.setProperty("hud.fps", Boolean.toString(NexusClientMod.showFps));
        p.setProperty("hud.armor", Boolean.toString(NexusClientMod.showArmor));
        p.setProperty("hud.durability", Boolean.toString(NexusClientMod.showDurability));
        try {
            Files.createDirectories(FILE.getParent());
            try (OutputStream out = Files.newOutputStream(FILE)) {
                p.store(out, "Nexus Client settings");
            }
        } catch (IOException e) {
            System.err.println("[Nexus Client] Could not save settings: " + e.getMessage());
        }
    }
}
