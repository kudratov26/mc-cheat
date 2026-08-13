# Omni-Injector Cheat Mod

A complete Fabric-based Minecraft cheat mod for version 1.20.1 with advanced features including Auto Clicker, Kill Aura, Trigger Bot, Fly, Speed, and more.

## Features

- **Auto Clicker**: Automated clicking with configurable CPS
- **Kill Aura**: Automatically attacks nearby entities
- **Trigger Bot**: Auto-attacks when crosshair is on entity
- **Fly**: Flight mode with configurable speed
- **Speed**: Movement speed multiplier
- **No Fall Damage**: Prevents fall damage
- **Auto Soup**: Automatically eats soup when health is low

## Requirements

- Minecraft 1.20.1
- Fabric Loader 0.14.21+
- Java 17 or higher

## Building

1. Ensure Java 17+ is installed
2. Run the build command:
   ```bash
   gradlew.bat build
   ```
3. Find the compiled JAR in `build/libs/omni-injector-1.0.0.jar`

## Installation

1. Copy the JAR from `build/libs/` to your Minecraft mods folder
2. Launch Minecraft with Fabric Loader
3. Press **Right Shift** to open the cheat menu

## Configuration

Edit `src/main/java/com/cheatclient/config/CheatConfig.java` to customize:
- Fly speed
- Speed multiplier
- Kill aura range
- Auto clicker CPS
- Auto soup health threshold
- Anti-detection settings

## Usage

- **Right Shift**: Open/close cheat menu
- **Jump**: Fly upward (when Fly is enabled)
- **Sneak**: Fly downward (when Fly is enabled)

## Important Notes

⚠️ **This mod is for educational purposes only.** Using cheats on multiplayer servers may result in bans.

The mod includes TEMP_VALUE placeholders that need adjustment based on server-specific anti-cheat implementations. See `IMPLEMENTATION_NOTES.md` for detailed configuration guidance.

## Project Structure

```
src/main/java/com/cheatclient/
├── CheatClientMod.java          # Core manager & entry point
├── CheatMenuHandler.java        # GUI menu implementation
├── config/
│   └── CheatConfig.java         # Configuration values
└── mixin/
    ├── ClientPlayerEntityMixin.java  # Movement/physics
    └── PlayerTickMixin.java         # Combat/Utility
```

## Documentation

See `IMPLEMENTATION_NOTES.md` for:
- Detailed implementation walkthrough
- Vanilla method overrides
- TEMP_VALUE placeholder explanations
- Troubleshooting guide
- Architecture notes

## License

MIT License
