package com.example.forcesettings;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.ParticleStatus;

public class ForceSettings implements ClientModInitializer {

    private static final int MAX_FPS = 210;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> enforce(client));
    }

    private static void enforce(Minecraft client) {
        Options o = client.options;
        if (o == null) return;

        boolean changed = false;

        // Clouds OFF
        if (o.cloudStatus().get() != CloudStatus.OFF) {
            o.cloudStatus().set(CloudStatus.OFF);
            changed = true;
        }

        // Max framerate 210
        if (o.framerateLimit().get() != MAX_FPS) {
            o.framerateLimit().set(MAX_FPS);
            changed = true;
        }

        // See-through (cutout) leaves OFF
        if (o.cutoutLeaves().get()) {
            o.cutoutLeaves().set(false);
            changed = true;
        }

        // Particles: Decreased
        if (o.particles().get() != ParticleStatus.DECREASED) {
            o.particles().set(ParticleStatus.DECREASED);
            changed = true;
        }

        if (changed) {
            o.save();
        }
    }
}
