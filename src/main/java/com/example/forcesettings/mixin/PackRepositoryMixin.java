package com.example.forcesettings.mixin;

import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Mixin(PackRepository.class)
public abstract class PackRepositoryMixin {

    @Shadow
    private Map<String, Pack> available;

    @ModifyVariable(method = "rebuildSelected", at = @At("HEAD"), argsOnly = true)
    private Collection<String> forcesettings$forceAll(Collection<String> requested) {
        List<String> vanilla = new ArrayList<>();
        List<String> builtIn = new ArrayList<>();
        List<String> userPacks = new ArrayList<>();

        for (Map.Entry<String, Pack> entry : this.available.entrySet()) {
            String id = entry.getKey();
            Pack pack = entry.getValue();

            String title = "";
            try {
                title = pack.getTitle().getString();
            } catch (Throwable ignored) {
            }

            // Packs that must stay OFF
            if (forcesettings$isBlocked(id) || forcesettings$isBlocked(title)) continue;

            if (id.equals("vanilla")) {
                // Default Minecraft pack: goes at the very bottom
                vanilla.add(id);
            } else if (id.startsWith("file/")) {
                // Your resource packs from the resourcepacks folder
                userPacks.add(id);
            } else {
                // Mod built-in packs
                builtIn.add(id);
            }
        }

        // First in the list = bottom of the stack
        List<String> all = new ArrayList<>();
        all.addAll(vanilla);
        all.addAll(builtIn);
        all.addAll(userPacks);
        return all;
    }

    private static boolean forcesettings$isBlocked(String name) {
        String n = name.toLowerCase().replace("_", " ").replace("-", " ");
        return n.contains("programmer art")
                || n.contains("high contrast")
                || n.contains("classic icons")
                || n.contains("white icons");
    }
}
