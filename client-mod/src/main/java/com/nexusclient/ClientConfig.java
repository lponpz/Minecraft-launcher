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
            try (InputStream in = Files.newInputStream(FILE)) { p.load(in); }
            catch (IOException e) { System.err.println("[Nexus Client] Could not read settings; using defaults: " + e.getMessage()); }
        }
        apply(p);
    }

    private static void apply(Properties p) {
        NexusClientMod.showCoordinates = bool(p, "hud.coordinates", false);
        NexusClientMod.compactHud = bool(p, "hud.compact", false);
        NexusClientMod.healthIndicator = bool(p, "hud.health_indicator", true);
        NexusClientMod.showFps = bool(p, "hud.fps", false);
        NexusClientMod.showArmor = bool(p, "hud.armor", false);
        NexusClientMod.showDurability = bool(p, "hud.durability", false);
        NexusClientMod.showPing = bool(p, "hud.ping", false);
        NexusClientMod.showDirection = bool(p, "hud.direction", false);
        NexusClientMod.showBiome = bool(p, "hud.biome", false);
        NexusClientMod.showWorldClock = bool(p, "hud.world_clock", false);
    }

    public static void applyProfile(String profile) {
        resetValues();
        switch (profile) {
            case "minimal" -> { NexusClientMod.healthIndicator = true; NexusClientMod.showFps = true; }
            case "full" -> {
                NexusClientMod.showCoordinates = true;
                NexusClientMod.compactHud = true;
                NexusClientMod.healthIndicator = true;
                NexusClientMod.showFps = true;
                NexusClientMod.showArmor = true;
                NexusClientMod.showDurability = true;
                NexusClientMod.showPing = true;
                NexusClientMod.showDirection = true;
                NexusClientMod.showBiome = true;
                NexusClientMod.showWorldClock = true;
            }
            default -> {
                NexusClientMod.healthIndicator = true;
                NexusClientMod.showFps = true;
                NexusClientMod.showPing = true;
                NexusClientMod.compactHud = true;
            }
        }
        save();
    }

    private static void resetValues() {
        NexusClientMod.showCoordinates = false;
        NexusClientMod.compactHud = false;
        NexusClientMod.healthIndicator = false;
        NexusClientMod.showFps = false;
        NexusClientMod.showArmor = false;
        NexusClientMod.showDurability = false;
        NexusClientMod.showPing = false;
        NexusClientMod.showDirection = false;
        NexusClientMod.showBiome = false;
        NexusClientMod.showWorldClock = false;
    }

    public static void resetDefaults() {
        resetValues();
        NexusClientMod.healthIndicator = true;
        save();
    }

    private static boolean bool(Properties p, String key, boolean fallback) {
        String value = p.getProperty(key);
        if (value == null) return fallback;
        if ("true".equalsIgnoreCase(value)) return true;
        if ("false".equalsIgnoreCase(value)) return false;
        return fallback;
    }

    public static String exportSettings() {
        return "NEXUS_CLIENT_SETTINGS_V1\n"
            + "hud.coordinates=" + NexusClientMod.showCoordinates + "\n"
            + "hud.compact=" + NexusClientMod.compactHud + "\n"
            + "hud.health_indicator=" + NexusClientMod.healthIndicator + "\n"
            + "hud.fps=" + NexusClientMod.showFps + "\n"
            + "hud.armor=" + NexusClientMod.showArmor + "\n"
            + "hud.durability=" + NexusClientMod.showDurability + "\n"
            + "hud.ping=" + NexusClientMod.showPing + "\n"
            + "hud.direction=" + NexusClientMod.showDirection + "\n"
            + "hud.biome=" + NexusClientMod.showBiome + "\n"
            + "hud.world_clock=" + NexusClientMod.showWorldClock;
    }

    public static boolean importSettings(String text) {
        if (text == null || !text.startsWith("NEXUS_CLIENT_SETTINGS_V1\n") || text.length() > 4096) return false;
        Properties p = new Properties();
        try {
            p.load(new java.io.StringReader(text.substring("NEXUS_CLIENT_SETTINGS_V1\n".length())));
            apply(p);
            save();
            return true;
        } catch (IOException | IllegalArgumentException e) {
            return false;
        }
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
        p.setProperty("hud.biome", Boolean.toString(NexusClientMod.showBiome));
        p.setProperty("hud.world_clock", Boolean.toString(NexusClientMod.showWorldClock));
        try {
            Files.createDirectories(FILE.getParent());
            Path temp = FILE.resolveSibling(FILE.getFileName() + ".tmp");
            try (OutputStream out = Files.newOutputStream(temp)) { p.store(out, "Nexus Client settings"); }
            try {
                Files.move(temp, FILE, java.nio.file.StandardCopyOption.REPLACE_EXISTING, java.nio.file.StandardCopyOption.ATOMIC_MOVE);
            } catch (java.nio.file.AtomicMoveNotSupportedException ignored) {
                Files.move(temp, FILE, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) { System.err.println("[Nexus Client] Could not save settings: " + e.getMessage()); }
    }
}