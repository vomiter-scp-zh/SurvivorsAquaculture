package com.vomiter.survivorsaquaculture.mixin.fish.spawn;

import com.teammetallurgy.aquaculture.entity.AquaFishEntity;
import com.vomiter.survivorsaquaculture.core.fish.spawn_condition.SAquaClimate;
import com.vomiter.survivorsaquaculture.data.SAquaEntityTypeTags;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.entities.EntityHelpers;
import net.dries007.tfc.util.climate.OverworldClimateModel;
import net.dries007.tfc.world.chunkdata.ChunkData;
import net.dries007.tfc.world.chunkdata.ForestType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = AquaFishEntity.class, remap = false)
public class AquaFishEntity_SpawnCondition {

    @Inject(method = "canSpawnHere", at = @At("HEAD"), cancellable = true)
    private static void setTFCSpawnCondition(
            EntityType<? extends AbstractFish> fish,
            LevelAccessor world,
            MobSpawnType spawnReason,
            BlockPos pos,
            RandomSource random,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (world.isClientSide()) return;

        var fluid = world.getFluidState(pos);
        if (!fluid.is(TFCTags.Fluids.ANY_WATER)) {
            cir.setReturnValue(false);
            return;
        }

        if (fish.is(SAquaEntityTypeTags.SALT_WATER_FISH) && fluid.is(TFCTags.Fluids.ANY_FRESH_WATER)) {
            cir.setReturnValue(false);
            return;
        }

        if (fish.is(SAquaEntityTypeTags.FRESH_WATER_FISH) && !fluid.is(TFCTags.Fluids.ANY_FRESH_WATER)) {
            cir.setReturnValue(false);
            return;
        }

        final ChunkData data = EntityHelpers.getChunkDataForSpawning((ServerLevelAccessor) world, pos);
        final float temperature = OverworldClimateModel.getAdjustedAverageTempByElevation(pos, data);
        final float rainfall = data.getRainfall(pos);
        final ForestType forestType = data.getForestType();

        if (fish.is(SAquaEntityTypeTags.JUNGLE_FISH)
                && (forestType.equals(ForestType.NONE) || forestType.equals(ForestType.SPARSE))) {
            cir.setReturnValue(false);
            return;
        }

        SAquaEntityTypeTags.FISH_CATEGORIES.forEach(tag -> {
            if (fish.is(tag)) {
                cir.setReturnValue(SAquaClimate.canSpawn(tag, (int) temperature, (int) rainfall));
                cir.cancel();
            }
        });
    }
}
