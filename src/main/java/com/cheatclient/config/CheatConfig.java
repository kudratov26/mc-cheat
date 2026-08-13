package com.cheatclient.config;

public class CheatConfig {
    // Movement configurations
    public static final double FLY_SPEED = 0.5; // Vertical fly speed
    public static final double SPEED_MULTIPLIER = 1.5; // Ground speed multiplier
    
    // Combat configurations
    public static final double KILL_AURA_RANGE = 4.0; // Blocks
    public static final double TRIGGER_BOT_RANGE = 3.5; // Blocks
    public static final int AUTO_CLICKER_CPS = 15; // Clicks per second
    
    // Utility configurations
    public static final int AUTO_SOUP_HEALTH_THRESHOLD = 4; // Hearts (out of 20)
    
    // TEMP_VALUE: Server-specific calculation placeholders
    // These values need to be adjusted based on server anti-cheat implementation
    public static final double TEMP_PACKET_DELTA_MULTIPLIER = 1.0; // Adjust based on server movement validation
    public static final int TEMP_ATTACK_DELAY_TICKS = 0; // Delay between attacks in ticks (0 = instant)
    public static final double TEMP_AIM_SMOOTHNESS = 0.5; // 0.0 = instant snap, 1.0 = very smooth
    public static final boolean TEMP_RANDOMIZE_TIMING = false; // Add randomness to avoid detection
    
    // Advanced anti-detection settings
    public static final boolean ENABLE_BLATANT_MODE = false; // If true, ignores all anti-detection measures
    public static final double MIN_FLY_HEIGHT = 0.1; // Minimum height above ground to avoid flagging
}
