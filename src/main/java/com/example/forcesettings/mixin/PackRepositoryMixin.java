package com.example.forcesettings.mixin;

import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Mixin(PackRepository.class)
public abstract class PackRepositoryMixin {

    @Shadow
    private Map<String, Pack> available;

    /**
     * Whenever the repository rebuilds its selected list (on load, reload,
     * or when you press Done in the pack screen), replace the requested
     * list with EVERY available pack.
     */
    @ModifyVariable(method = "rebuildSelected", at = @At("HEAD"), argsOnly = true)
    private Collection<String> forcesettings$forceAll(Collection<String> requested) {
        List<String> all = new ArrayList<>();
        for (String id : this.available.keySet()) {
            // Built-in accessibility packs replace vanilla textures, so skip them.
            // Delete this if you really want literally everything forced on.
            if (id.contains("programmer_art") || id.contains("high_contrast")) continue;
            all.add(id);
        }
        return all;
    }
}
