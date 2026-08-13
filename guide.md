🤖 AI Agent Generation Prompt: "Omni-Injector Cheat Mod"

Goal: Generate a complete, functional, multi-component Java mod written for the Fabric environment that implements an advanced cheat client with user controls and advanced physics bypasses. The final output must be structured boilerplate code ready to be dropped into a standard build.gradle setup.

Target Environment Details:

    Language: Java 17+
    Framework: Fabric
    Goal Scope: Must support all features listed in the original request (Auto Clicker, Kill Aura, Trigger Bot, Fly, Speed, etc.).

Task Breakdown (The Agent must deliver these modules):
1. Core Architecture & Initialization (CheatClientMod.java)

    Function: Acts as the central singleton manager and event bus listener.
    Requirement: Must track the active state of all cheats (e.g., Map<String, Boolean>).
    Interface: It must expose simple public methods for other modules to query the status: public boolean isFeatureActive(String featureName).

2. User Interface (CheatMenuHandler.java & Screen Implementation)

    Function: Handles user interaction state (GUI/HUD).
    Requirement: Must generate boilerplate code for a custom Screen class that populates the menu with toggleable options matching all cheat features. When toggled, it must call methods in the central manager (CheatClientMod) to update the master state map.

3. Hooking & Cheat Implementation Modules (The Engine)

This requires three specific components:

A. Movement/Physics Injector (MovementMixin.java or equivalent):

    Target: Must use Mixin annotations to intercept vanilla classes like ClientPlayerEntity, World, and potentially the networking packet sender if available in the framework.
    Implementations Required:
        Fly Hack Logic: Intercept methods responsible for calculating vertical fall/gravity (getFallDistance or related physics update). The implementation must override the default physics calculation to force an upward velocity vector, simulating zero gravity or active thrust.
        Speed Hack Logic: If hooking into packet sending is possible (preferred), show how to intercept and augment the movement delta payload ($\Delta X, \Delta Y, \Delta Z$) sent to the server. If packet interception is too hard, fall back to overriding the base onTick method to inject artificially high positional updates.
        Fall Damage Bypass: Must ensure that any derived damage calculation that relies on vertical displacement is overridden to always return zero or a predetermined value.

B. Combat Module (Killaura & TriggerBot):

    Target: Intercept look angles and attack targeting logic.
    Implementations Required:
        Aura Logic (Kill Aura): Implement logic within an @Inject on the player's view/look method (getYaw, getPitch). It must calculate the vector aiming at the nearest threat rather than where the player is currently looking.
        Aim Prediction (TriggerBot): This requires advanced trigonometry: Calculate $\text{requiredLookAngle} = f(\text{TargetPosition}, \text{PlayerPosition}, \text{Range})$. The output must be a calculated $(\text{Yaw}{\text{new}}, \text{Pitch}{\text{new}})$ to pass to the onGetYaw/onGetPitch methods.

C. Utility Module (AutoClicker/AutoSoup):

    Function: Manages input timing and state checking.
    Requirement: Implement a system that overrides or hooks into the event that fires when an item is right-clicked, allowing for forced interaction cycles based on proximity checks (for Soup) rather than player manual input.

4. Deliverable Format Instructions

The final output must be:

    Code Block: The complete Java code for all required files (CheatClientMod.java, MovementMixin.java, etc.).
    Gradle Dependency Guide: A clear, annotated section showing exactly what lines need to be added or modified in the build.gradle file (e.g., adding external libraries for advanced networking if needed).
    Implementation Walkthrough: Step-by-step instructions detailing:
        Which method to replace/override in vanilla Minecraft classes.
        How to set up dummy variables (// TEMP_VALUE) placeholders that the user must calculate based on server implementation details (this guides the usability).
