package com.codex.cant_sleep;

import com.codex.cant_sleep.config.CantSleepConfigController;
import com.codex.cant_sleep.config.CantSleepConfigValues;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class CantSleepSleepRules {
    private static final Text INSOMNIA_TEXT = Text.translatable("cant_sleep.message.insomnia");
    private static final long TICKS_PER_DAY = 24000L;
    private static final Map<UUID, PlayerNightState> NIGHT_CACHE = new ConcurrentHashMap<>();

    private CantSleepSleepRules() {
    }

    public static void register() {
        UseBlockCallback.EVENT.register(CantSleepSleepRules::handleBedUse);
    }

    private static ActionResult handleBedUse(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        if (!(player instanceof ServerPlayerEntity serverPlayer)) {
            return ActionResult.PASS;
        }

        if (hand != Hand.MAIN_HAND) {
            return ActionResult.PASS;
        }

        if (!(world instanceof ServerWorld serverWorld)) {
            return ActionResult.PASS;
        }

        BlockState state = serverWorld.getBlockState(hitResult.getBlockPos());
        if (!(state.getBlock() instanceof BedBlock)) {
            return ActionResult.PASS;
        }

        if (!serverWorld.getDimension().bedWorks()) {
            return ActionResult.PASS;
        }

        if (!isSleepWindow(serverWorld)) {
            return ActionResult.PASS;
        }

        if (!isInsomniaNight(serverPlayer, serverWorld)) {
            return ActionResult.PASS;
        }

        serverPlayer.sendMessage(INSOMNIA_TEXT, true);
        return ActionResult.FAIL;
    }

    private static boolean isSleepWindow(ServerWorld world) {
        return world.isNight() || world.isThundering();
    }

    private static boolean isInsomniaNight(ServerPlayerEntity player, ServerWorld world) {
        CantSleepConfigValues config = CantSleepConfigController.current();
        double chance = config.insomniaChance();
        if (chance <= 0.0D) {
            return false;
        }
        if (chance >= 1.0D) {
            return true;
        }

        long dayIndex = world.getTimeOfDay() / TICKS_PER_DAY;
        PlayerNightState state = NIGHT_CACHE.compute(player.getUuid(), (uuid, existing) -> {
            if (existing != null && existing.dayIndex == dayIndex) {
                return existing;
            }
            boolean insomnia = world.getRandom().nextDouble() < chance;
            return new PlayerNightState(dayIndex, insomnia);
        });
        return state.insomnia;
    }

    private record PlayerNightState(long dayIndex, boolean insomnia) {
    }
}
