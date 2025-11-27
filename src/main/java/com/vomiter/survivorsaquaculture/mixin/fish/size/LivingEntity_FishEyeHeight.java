package com.vomiter.survivorsaquaculture.mixin.fish.size;

import com.vomiter.survivorsaquaculture.SAquaConfig;
import com.vomiter.survivorsaquaculture.core.fish.AquaFishes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(LivingEntity.class)
public class LivingEntity_FishEyeHeight {
    @Inject(method = "getEyeHeight", at = @At("RETURN"), cancellable = true)
    private void scaleEye(Pose p_21049_, EntityDimensions p_21050_, CallbackInfoReturnable<Float> cir) {
        if(!SAquaConfig.COMMON.ADJUST_FISH_SIZE.get()) return;
        Entity self = (Entity)(Object)this;
        AquaFishes.byEntityType(self.getType()).ifPresent(f -> {
            float s = f.getSizeScale();
            if (s > 1f) cir.setReturnValue(cir.getReturnValue() * 0.9f * s);
        });
    }
}
