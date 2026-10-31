# Nexus Client Mod (foundation build)

This is the first separate Fabric client-mod module for the Nexus Client project.

- Target: Minecraft Java 1.21.1
- Loader: Fabric
- Java: 21
- Open menu: Right Shift
- Intended install location: the matching Minecraft instance's `mods` folder

**Important:** This is a foundation/prototype, not the finished 752-feature client. The menu currently demonstrates the client menu structure and toggle UI; many entries are marked prototype/planned and do not yet alter game behavior. Features must be implemented and tested in stages. It requires Fabric Loader and Fabric API compatible with Minecraft 1.21.1.

Build with Gradle: `gradle build`. The remapped mod JAR should appear in `build/libs/`.
