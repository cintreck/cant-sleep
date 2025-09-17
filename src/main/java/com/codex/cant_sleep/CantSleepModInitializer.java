package com.codex.cant_sleep;

import com.codex.cant_sleep.config.CantSleepConfigController;
import net.fabricmc.api.ModInitializer;

public final class CantSleepModInitializer implements ModInitializer {
    @Override
    public void onInitialize() {
        CantSleepConfigController.loadIntoMemory();
        CantSleepSleepRules.register();
        CantSleepModData.LOGGER.info("Can't Sleep ready");
    }
}
