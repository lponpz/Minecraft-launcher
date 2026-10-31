package com.nexusclient;

import java.util.List;

/**
 * Honest status registry for the currently tracked Nexus Client features.
 * This is a growing implementation registry, not a claim that the full roadmap is complete.
 */
public final class FeatureCatalog {
    public enum Status { IMPLEMENTED, IN_PROGRESS, PLANNED }
    public record Feature(int id, String category, String name, Status status) {}

    private static final List<Feature> FEATURES = List.of(
        new Feature(1, "HUD", "Coordinates HUD", Status.IMPLEMENTED),
        new Feature(2, "HUD", "Compact health and hunger HUD", Status.IMPLEMENTED),
        new Feature(3, "HUD", "HUD position editor", Status.PLANNED),
        new Feature(4, "HUD", "Armor status display", Status.IMPLEMENTED),
        new Feature(5, "HUD", "Armor durability display", Status.IMPLEMENTED),
        new Feature(6, "HUD", "FPS counter", Status.IMPLEMENTED),
        new Feature(7, "HUD", "Ping display", Status.IMPLEMENTED),
        new Feature(8, "HUD", "Biome display", Status.PLANNED),
        new Feature(9, "HUD", "Clock display", Status.PLANNED),
        new Feature(10, "HUD", "Custom HUD profiles", Status.PLANNED),
        new Feature(11, "Performance", "Low-overhead HUD rendering", Status.IN_PROGRESS),
        new Feature(12, "Performance", "Particle reduction controls", Status.PLANNED),
        new Feature(13, "Performance", "Animation reduction controls", Status.PLANNED),
        new Feature(14, "Performance", "Entity render distance presets", Status.PLANNED),
        new Feature(15, "Performance", "Performance profiles", Status.PLANNED),
        new Feature(16, "Controls", "Right Shift menu key", Status.IMPLEMENTED),
        new Feature(17, "Controls", "Custom keybind editor", Status.PLANNED),
        new Feature(18, "Controls", "Per-world keybind profiles", Status.PLANNED),
        new Feature(19, "Settings", "Persistent local settings", Status.IMPLEMENTED),
        new Feature(20, "Settings", "Import and export settings", Status.PLANNED),
        new Feature(21, "HUD", "Facing direction display", Status.IMPLEMENTED),
        new Feature(22, "Invites", "Copy server invite to clipboard", Status.IMPLEMENTED),
        new Feature(23, "Invites", "Validate versioned clipboard invite", Status.IMPLEMENTED),
        new Feature(24, "Invites", "Join server from clipboard invite", Status.IN_PROGRESS),
        new Feature(25, "Invites", "Automatic online invite relay", Status.PLANNED),
        new Feature(26, "UI", "Responsive main menu layout", Status.IMPLEMENTED),
        new Feature(27, "Settings", "Atomic configuration writes", Status.IMPLEMENTED),
        new Feature(28, "Settings", "Invalid boolean fallback validation", Status.IMPLEMENTED)
    );

    private FeatureCatalog() {}

    public static List<Feature> all() { return FEATURES; }

    public static long implementedCount() {
        return FEATURES.stream().filter(f -> f.status() == Status.IMPLEMENTED).count();
    }

    public static long testedCount() {
        // Automated/in-game test evidence is not yet tracked per feature.
        return 0;
    }
}
