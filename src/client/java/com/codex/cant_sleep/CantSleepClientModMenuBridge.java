package com.codex.cant_sleep;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.loader.api.FabricLoader;

public final class CantSleepClientModMenuBridge implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            if (FabricLoader.getInstance().isModLoaded("cloth-config2")) {
                return CantSleepClientClothScreenFactory.create(parent);
            }
            return parent;
        };
    }
}
