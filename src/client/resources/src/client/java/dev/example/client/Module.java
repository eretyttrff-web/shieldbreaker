package dev.example.client;

import net.minecraft.client.MinecraftClient;

public abstract class Module {
    protected static final MinecraftClient mc = MinecraftClient.getInstance();
    public final String name;
    private boolean enabled;

    protected Module(String name) { this.name = name; }

    public boolean isEnabled() { return enabled; }
    public void toggle() {
        enabled = !enabled;
        if (enabled) onEnable(); else onDisable();
    }
    protected void onEnable() {}
    protected void onDisable() {}
    /** Called every client tick while enabled and in a world. */
    public void onTick() {}
}
