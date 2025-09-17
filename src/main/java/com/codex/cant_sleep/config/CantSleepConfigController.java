package com.codex.cant_sleep.config;

import com.codex.cant_sleep.CantSleepModData;

public final class CantSleepConfigController {
    private static volatile CantSleepConfigValues activeConfig = new CantSleepConfigValues(CantSleepModData.DEFAULT_INSOMNIA_PROBABILITY);

    private CantSleepConfigController() {
    }

    public static synchronized CantSleepConfigValues loadIntoMemory() {
        activeConfig = CantSleepConfigStorage.loadOrCreate();
        return activeConfig;
    }

    public static synchronized CantSleepConfigValues overwrite(double insomniaChance) {
        activeConfig = CantSleepConfigStorage.overwrite(insomniaChance);
        return activeConfig;
    }

    public static CantSleepConfigValues current() {
        return activeConfig;
    }
}
