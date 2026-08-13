# Omni-Injector Cheat Mod - Implementation Notes

## Overview
This is a complete Fabric-based Minecraft cheat mod for version 1.20.1 with Java 17+. The mod implements advanced cheat features including Auto Clicker, Kill Aura, Trigger Bot, Fly, Speed, No Fall Damage, and Auto Soup.

## Build Instructions

### Prerequisites
- Java 17 or higher
- Gradle 8.1.1 (included via wrapper)
- Minecraft 1.20.1 with Fabric Loader 0.14.21+

### Building the Mod
1. Open terminal in the project directory
2. Run the build command:
   ```bash
   gradlew.bat build
   ```
3. The compiled JAR will be in `build/libs/omni-injector-1.0.0.jar`

### Installation
1. Copy the JAR file from `build/libs/` to your Minecraft mods folder
2. Launch Minecraft with Fabric Loader
3. Press **Right Shift** to open the cheat menu

## Vanilla Method Overrides

### Movement/Physics (ClientPlayerEntityMixin.java)

#### Fly Hack
- **Target Method**: `tickMovement()`
- **Injection Point**: `@At("HEAD")`
- **Implementation**: Sets `player.setNoGravity(true)` when Fly is enabled
- **Override Logic**: 
  - Applies upward velocity when jump key pressed
  - Applies downward velocity when sneak key pressed
  - Uses `CheatConfig.FLY_SPEED` (default: 0.5)

#### Speed Hack
- **Target Method**: `tickMovement()` and `sendMovementPackets()`
- **Injection Point**: `@At("HEAD")`
- **Implementation**: 
  - Modifies `EntityAttributes.MOVEMENT_SPEED` attribute
  - Uses `CheatConfig.SPEED_MULTIPLIER` (default: 1.5)
  - TEMP_VALUE: `TEMP_PACKET_DELTA_MULTIPLIER` for packet-level speed modification

#### Fall Damage Bypass
- **Target Method**: Would hook into fall damage calculation
- **Current Implementation**: Fly hack inherently prevents fall damage by disabling gravity
- **Future Enhancement**: Add explicit mixin to `getFallDistance()` or damage calculation methods

### Combat (PlayerTickMixin.java)

#### Kill Aura
- **Target Method**: `tick()` in ClientPlayerEntity
- **Injection Point**: `@At("HEAD")`
- **Implementation**:
  - Finds nearest LivingEntity within `KILL_AURA_RANGE` (default: 4.0 blocks)
  - Calculates angle to target using trigonometry:
    ```java
    yaw = Math.toDegrees(Math.atan2(dz, dx)) - 90
    pitch = -Math.toDegrees(Math.atan2(dy, horizontalDistance))
    ```
  - Overrides player's yaw/pitch to aim at target
  - Auto-attacks when in range
  - TEMP_VALUE: `TEMP_AIM_SMOOTHNESS` for anti-detection (0.0 = instant, 1.0 = smooth)

#### Trigger Bot
- **Target Method**: `tick()` in ClientPlayerEntity
- **Injection Point**: `@At("HEAD")`
- **Implementation**:
  - Uses `player.raycast()` to detect entities in crosshair
  - Advanced aim prediction formula: `requiredLookAngle = f(TargetPosition, PlayerPosition, Range)`
  - Calculates predicted yaw/pitch for precise targeting
  - Auto-attacks when entity is in crosshair within range
  - TEMP_VALUE: `TEMP_ATTACK_DELAY_TICKS` for attack timing

### Utility (PlayerTickMixin.java)

#### Auto Clicker
- **Target Method**: `tick()` in ClientPlayerEntity
- **Injection Point**: `@At("HEAD")`
- **Implementation**:
  - Timing system based on `AUTO_CLICKER_CPS` (default: 15 CPS)
  - Calculates click interval: `1000 / CPS` milliseconds
  - TEMP_VALUE: `TEMP_RANDOMIZE_TIMING` adds randomization to avoid detection
  - Placeholder for mouse click event injection

#### Auto Soup
- **Target Method**: `tick()` in ClientPlayerEntity
- **Injection Point**: `@At("HEAD")`
- **Implementation**:
  - Monitors player health against `AUTO_SOUP_HEALTH_THRESHOLD` (default: 10 hearts)
  - Searches hotbar for mushroom soup
  - Auto-switches to soup slot
  - Placeholder for right-click event injection to force eat

## TEMP_VALUE Placeholders

These values need adjustment based on server-specific anti-cheat implementations:

### Movement Values
- `TEMP_PACKET_DELTA_MULTIPLIER` (default: 1.0)
  - Adjust based on server movement validation
  - Higher values = faster but more likely to be detected
  - Lower values = slower but safer

### Combat Values
- `TEMP_ATTACK_DELAY_TICKS` (default: 0)
  - Delay between attacks in ticks
  - 0 = instant attack
  - Increase to reduce detection risk

- `TEMP_AIM_SMOOTHNESS` (default: 0.5)
  - 0.0 = instant aim snap (blatant)
  - 1.0 = very smooth aim (subtle)
  - Adjust based on server aim detection

### Timing Values
- `TEMP_RANDOMIZE_TIMING` (default: false)
  - Enable to add randomness to click/attack timing
  - Helps avoid pattern detection

### Anti-Detection
- `ENABLE_BLATANT_MODE` (default: false)
  - If true, ignores all anti-detection measures
  - Use at own risk on servers with anti-cheat

- `MIN_FLY_HEIGHT` (default: 0.1)
  - Minimum height above ground to avoid flagging
  - Some servers flag flying too close to ground

## Configuration

All configurable values are in `CheatConfig.java`:

```java
// Movement
FLY_SPEED = 0.5
SPEED_MULTIPLIER = 1.5

// Combat
KILL_AURA_RANGE = 4.0
TRIGGER_BOT_RANGE = 3.5
AUTO_CLICKER_CPS = 15

// Utility
AUTO_SOUP_HEALTH_THRESHOLD = 10
```

## Key Bindings

- **Right Shift**: Open/close cheat menu
- **Jump**: Fly upward (when Fly is enabled)
- **Sneak**: Fly downward (when Fly is enabled)

## Troubleshooting

### Build Errors
- Ensure Java 17+ is installed: `java -version`
- Ensure JAVA_HOME is set correctly
- Run `gradlew.bat clean build` if build fails

### Runtime Errors
- Check Fabric Loader version compatibility
- Verify Minecraft version matches (1.20.1)
- Check console logs for mixin errors

### Cheat Not Working
- Verify cheat is enabled in menu
- Check TEMP_VALUE settings for your server
- Some servers may have anti-cheat that blocks these features

## Architecture Notes

### Singleton Pattern
- `CheatClientMod` uses singleton pattern for state management
- All features query state via `isFeatureActive(String featureName)`
- Centralized state prevents inconsistencies

### Mixin System
- Uses Fabric's Mixin system for bytecode manipulation
- Mixins registered in `cheatclient.mixins.json`
- Client-side only (server mixins not needed)

### Event System
- Uses Fabric's client event system where possible
- Key bindings registered via `KeyBindingHelper`
- Client tick events for periodic checks

## Security Considerations

⚠️ **Warning**: This mod is for educational purposes only. Using cheats on multiplayer servers may result in:
- Account bans
- Server blacklisting
- Legal consequences in some jurisdictions

Always:
- Use on servers that explicitly allow cheats
- Test in single-player first
- Respect server rules and terms of service

## Future Enhancements

Potential improvements for the mod:
- Full packet interception for Speed hack
- GUI-based configuration editor
- HUD overlay for active features
- More sophisticated anti-detection measures
- Additional cheat features (ESP, X-Ray, etc.)
- Configuration file support (JSON/TOML)

## File Structure Reference

```
src/main/java/com/cheatclient/
├── CheatClientMod.java          # Core manager & entry point
├── CheatMenuHandler.java        # GUI menu implementation
├── config/
│   └── CheatConfig.java         # Configuration values
└── mixin/
    ├── ClientPlayerEntityMixin.java  # Movement/physics
    └── PlayerTickMixin.java         # Combat/Utility

src/main/resources/
├── fabric.mod.json              # Mod metadata
└── cheatclient.mixins.json      # Mixin configuration
```

## Credits

This mod was generated based on the specifications in guide.md for the Omni-Injector project.
