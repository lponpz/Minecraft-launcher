package com.nexusclient;

import java.util.List;

/**
 * First roadmap slice. Only entries explicitly marked IMPLEMENTED are live;
 * planned entries are not exposed as pretend-working toggles.
 */
public final class FeatureCatalog {
    public enum Status { IMPLEMENTED, IN_PROGRESS, PLANNED }
    public record Feature(int id, String category, String name, Status status) {}

    private static final List<Feature> FEATURES = List.of(
        new Feature(1, "HUD", "Coordinates HUD", Status.IMPLEMENTED),
        new Feature(2, "HUD", "Compact health and hunger HUD", Status.IMPLEMENTED),
        new Feature(3, "HUD", "HUD position editor", Status.PLANNED),
        new Feature(4, "HUD", "Armor status display", Status.PLANNED),
        new Feature(5, "HUD", "Durability display", Status.PLANNED),
        new Feature(6, "HUD", "FPS counter", Status.PLANNED),
        new Feature(7, "HUD", "Ping display", Status.PLANNED),
        new Feature(8, "HUD", "Biome display", Status.PLANNED),
        new Feature(9, "HUD", "Clock display", Status.PLANNED),
        new Feature(10, "HUD", "Custom HUD profiles", Status.PLANNED),
        new Feature(11, "Performance", "Low-overhead client tick handlers", Status.IN_PROGRESS),
        new Feature(12, "Performance", "Particle reduction", Status.PLANNED),
        new Feature(13, "Performance", "Animation reduction", Status.PLANNED),
        new Feature(14, "Performance", "Entity render distance presets", Status.PLANNED),
        new Feature(15, "Performance", "Performance profiles", Status.PLANNED),
        new Feature(16, "Controls", "Right Shift menu key", Status.IMPLEMENTED),
        new Feature(17, "Controls", "Custom keybind editor", Status.PLANNED),
        new Feature(18, "Controls", "Per-world keybind profiles", Status.PLANNED),
        new Feature(19, "Settings", "Persistent local settings", Status.IMPLEMENTED),
        new Feature(20, "Settings", "Import and export settings", Status.PLANNED)
    );

    private FeatureCatalog() {}

    public static List<Feature> all() {
        return FEATURES;
    }

    public static long implementedCount() {
        return FEATURES.stream().filter(f -> f.status() == Status.IMPLEMENTED).count();
    }
}
