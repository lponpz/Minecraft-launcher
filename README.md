# Nexus Client

A lightweight Electron starter for a Minecraft launcher.

## Current working features
- Dark Blue Neon launcher interface and navigation.
- Create, rename, and remove instance records.
- Choose an existing folder for an instance.
- Basic read-only folder inspection.
- Local instance data persistence.
- GitHub Actions workflow that attempts to build a Windows installer.

## Not implemented yet
This is not a complete Minecraft launcher. Minecraft version detection and launch, official Microsoft authentication, Minecraft/Fabric downloads, mod browser, modpack installation, updates, and advanced performance tools still need implementation and testing.

Existing Minecraft folders are not deleted or overwritten by this starter. Removing an instance only removes its launcher record.

## Build a Windows installer using GitHub Actions
1. Open the repository's Actions tab.
2. Select Build Nexus Client for Windows.
3. Press Run workflow on the main branch.
4. Wait for the workflow to finish.
5. Download the Nexus-Client-Windows artifact from the completed run and extract it to access the installer.

The workflow must complete successfully before an installer is available. The artifact is a prototype, not a production-ready full Minecraft launcher.
