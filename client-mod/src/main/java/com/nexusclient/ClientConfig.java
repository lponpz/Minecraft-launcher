package com.nexusclient;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/** Persistent, validated configuration for Nexus Client. */
public final class ClientConfig {
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("nexus-client.properties");
    private ClientConfig() {}

    public static void load() {
        Properties p = new Properties();
        if (Files.isRegularFile(FILE)) {
            try (InputStream in = Files.newInputStream(FILE)) {
                p.load(in);
            } catch (IOException e) {
                System.err.println("[Nexus Client] Could not read settings; using defaults: " + e.getMessage());
            }
        }
        NexusClientMod.showCoordinates = bool(p, "hud.coordinates", false);
        NexusClientMod.compactHud = bool(p, "hud.compact", false);
        NexusClientMod.healthIndicator = bool(p, "hud.health_indicator", true);
        NexusClientMod.showFps = bool(p, "hud.fps", false);
        NexusClientMod.showArmor = bool(p, "hud.armor", false);
        NexusClientMod.showDurability = bool(p, "hud.durability", false);
        NexusClientMod.showPing = bool(p, "hud.ping", false);
        NexusClientMod.showDirection = bool(p, "hud.direction", false);
    }

    private static boolean bool(Properties p, String key, boolean fallback) {
        String value = p.getProperty(key);
        if (value == null) return fallback;
        if ("true".equalsIgnoreCase(value)) return true;
        if ("false".equalsIgnoreCase(value)) return false;
        return fallback;
    }

    public static void save() {
        Properties p = new Properties();
        p.setProperty("hud.coordinates", Boolean.toString(NexusClientMod.showCoordinates));
        p.setProperty("hud.compact", Boolean.toString(NexusClientMod.compactHud));
        p.setProperty("hud.health_indicator", Boolean.toString(NexusClientMod.healthIndicator));
        p.setProperty("hud.fps", Boolean.toString(NexusClientMod.showFps));
        p.setProperty("hud.armor", Boolean.toString(NexusClientMod.showArmor));
        p.setProperty("hud.durability", Boolean.toString(NexusClientMod.showDurability));
        p.setProperty("hud.ping", Boolean.toString(NexusClientMod.showPing));
        p.setProperty("hud.direction", Boolean.toString(NexusClientMod.showDirection));
        try {
            Files.createDirectories(FILE.getParent());
            Path temp = FILE.resolveSibling(FILE.getFileName() + ".tmp");
            try (OutputStream out = Files.newOutputStream(temp)) {
                p.store(out, "Nexus Client settings");
            }
            try {
                Files.move(temp, FILE, java.nio.file.StandardCopyOption.REPLACE_EXISTING,
                    java.nio.file.StandardCopyOption.ATOMIC_MOVE);
            } catch (java.nio.file.AtomicMoveNotSupportedException ignored) {
                Files.move(temp, FILE, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            System.err.println("[Nexus Client] Could not save settings: " + e.getMessage());
        }
    }
}
