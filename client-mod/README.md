# Nexus Client Mod — Minecraft Java 1.21.1

Fabric client-side mod built for a lightweight experience on lower-end PCs.

## Included now
- Right Shift configuration menu
- HUD: coordinates/dimension, health/food, health bar, FPS, armor, armor durability, ping, direction, biome and world clock
- Persistent settings
- Minimal, Balanced and Full HUD profiles
- Copy settings to clipboard and import a previously copied Nexus settings export
- Local clipboard-based server invite creation and paste-to-join

## Build and install
- Minecraft Java 1.21.1
- Fabric Loader and Fabric API for 1.21.1
- Java 21 for building
- Run `gradle build` from this folder; output is in `build/libs/`.
- Put the built mod JAR in the matching Minecraft instance's `mods` folder.

## Honest scope
This is an actively developing client-mod foundation, not a completed all-features client. HUD profiles and settings import/export are implemented here; advanced HUD dragging, true performance optimization controls, online invite relay, per-world profiles and other planned features still require separate implementation and testing. Server invites currently use the clipboard only and do not create a network relay.
