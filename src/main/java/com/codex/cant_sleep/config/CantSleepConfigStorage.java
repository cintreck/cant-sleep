package com.codex.cant_sleep.config;

import com.codex.cant_sleep.CantSleepModData;
import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class CantSleepConfigStorage {
    private static final Logger LOGGER = LoggerFactory.getLogger("cant_sleep-config");
    private static final double MIN_CHANCE = 0.0D;
    private static final double MAX_CHANCE = 1.0D;

    private CantSleepConfigStorage() {
    }

    public static CantSleepConfigValues loadOrCreate() {
        CantSleepConfigValues defaults = normalized(CantSleepModData.DEFAULT_INSOMNIA_PROBABILITY);
        Path path = configPath();
        if (!Files.exists(path)) {
            writeConfig(path, defaults);
            return defaults;
        }

        try (CommentedFileConfig file = openFile(path)) {
            file.load();
            CantSleepConfigValues loaded = fromToml(file, defaults);
            writeToFile(file, loaded);
            return loaded;
        } catch (Throwable t) {
            LOGGER.error("Failed to load {}: {}", CantSleepModData.CONFIG_FILE_NAME, t.toString(), t);
            writeConfig(path, defaults);
            return defaults;
        }
    }

    public static CantSleepConfigValues overwrite(double insomniaChance) {
        CantSleepConfigValues updated = normalized(insomniaChance);
        writeConfig(configPath(), updated);
        return updated;
    }

    private static CantSleepConfigValues fromToml(CommentedConfig config, CantSleepConfigValues defaults) {
        double chance = defaults.insomniaChance();
        Object rawChance = config.get("insomnia_probability");
        if (rawChance instanceof Number number) {
            chance = number.doubleValue();
        }
        return normalized(chance);
    }

    private static void writeConfig(Path path, CantSleepConfigValues config) {
        try {
            Files.createDirectories(path.getParent());
        } catch (IOException e) {
            LOGGER.error("Failed to create config directory for {}", CantSleepModData.CONFIG_FILE_NAME, e);
            return;
        }

        try (CommentedFileConfig file = openFile(path)) {
            file.load();
            writeToFile(file, config);
        } catch (Throwable t) {
            LOGGER.error("Failed to write {}: {}", CantSleepModData.CONFIG_FILE_NAME, t.toString(), t);
        }
    }

    private static CommentedFileConfig openFile(Path path) {
        return CommentedFileConfig.builder(path)
                .preserveInsertionOrder()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();
    }

    private static void writeToFile(CommentedFileConfig file, CantSleepConfigValues config) {
        file.set("#", "Can't Sleep - Config");
        file.set("#1", "Controls the chance that a night becomes unrestful.");

        file.set("insomnia_probability", config.insomniaChance());
        file.setComment("insomnia_probability", "Value between 0.0 and 1.0. 0.15 means 15% of nights cause insomnia.");

        file.save();
    }

    private static CantSleepConfigValues normalized(double insomniaChance) {
        double sanitized = Math.max(MIN_CHANCE, Math.min(MAX_CHANCE, insomniaChance));
        return new CantSleepConfigValues(sanitized);
    }

    private static Path configPath() {
        return FabricLoader.getInstance().getConfigDir().resolve(CantSleepModData.CONFIG_FILE_NAME);
    }
}
