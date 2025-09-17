package com.codex.cant_sleep;

import com.codex.cant_sleep.config.CantSleepConfigController;
import com.codex.cant_sleep.config.CantSleepConfigValues;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public final class CantSleepClientClothScreenFactory {
    private CantSleepClientClothScreenFactory() {
    }

    public static Screen create(Screen parent) {
        CantSleepConfigValues current = CantSleepConfigController.loadIntoMemory();
        final float[] insomniaChance = { (float) current.insomniaChance() };

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("Can't Sleep"));
        builder.setSavingRunnable(() -> CantSleepConfigController.overwrite(insomniaChance[0]));

        ConfigCategory general = builder.getOrCreateCategory(Text.literal("General"));
        ConfigEntryBuilder entries = builder.entryBuilder();

        general.addEntry(entries.startFloatField(Text.literal("Insomnia Probability"), insomniaChance[0])
                .setDefaultValue((float) CantSleepModData.DEFAULT_INSOMNIA_PROBABILITY)
                .setMin(0.0F)
                .setMax(1.0F)
                .setTooltip(Text.literal("Chance between 0.0 and 1.0 inclusive that a night prevents sleep."))
                .setSaveConsumer(value -> insomniaChance[0] = value)
                .build());

        return builder.build();
    }
}
