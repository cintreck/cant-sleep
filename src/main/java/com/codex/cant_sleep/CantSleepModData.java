package com.codex.cant_sleep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Shared constants for the Can't Sleep mod.
 */
public final class CantSleepModData {
    public static final String MOD_ID = "cant_sleep";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final double DEFAULT_INSOMNIA_PROBABILITY = 0.15D;
    public static final String CONFIG_FILE_NAME = "cant_sleep.toml";

    private CantSleepModData() {
    }
}
