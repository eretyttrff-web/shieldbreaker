package dev.example.client;

import dev.example.client.modules.ShieldBreaker;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class HackClient implements ClientModInitializer {
    public static final List<Module> MODULES = new ArrayList<>();
    private static final boolean[] wasDown = new boolean[3];

    // Toggle key for ShieldBreaker (F8). Edit freely.
    private static final int[] KEYS = { GLFW.GLFW_KEY_F8 };

    @Override
    public void onInitializeClient() {
        MODULES.add(new ShieldBreaker());

        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            if (mc.player == null) return;
            // Hotkeys (only when no screen is open)
            if (mc.currentScreen == null) {
                long win = mc.getWindow().getHandle();
                for (int i = 0; i < MODULES.size(); i++) {
                    boolean down = GLFW.glfwGetKey(win, KEYS[i]) == GLFW.GLFW_PRESS;
                    if (down && !wasDown[i]) MODULES.get(i).toggle();
                    wasDown[i] = down;
                }
            }
            for (Module m : MODULES) if (m.isEnabled()) m.onTick();
        });

        // Simple "arraylist" HUD of enabled modules
        HudRenderCallback.EVENT.register((ctx, tick) -> {
            MinecraftClient mc = MinecraftClient.getInstance();
            int y = 4;
            for (Module m : MODULES) {
                if (!m.isEnabled()) continue;
                ctx.drawTextWithShadow(mc.textRenderer, m.name, 4, y, 0xFF55FF55);
                y += 10;
            }
        });
    }
}
