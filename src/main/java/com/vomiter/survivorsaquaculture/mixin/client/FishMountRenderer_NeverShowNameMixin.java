package com.vomiter.survivorsaquaculture.mixin.client;

import com.teammetallurgy.aquaculture.client.renderer.entity.FishMountRenderer;
import com.teammetallurgy.aquaculture.entity.FishMountEntity;
import com.vomiter.survivorsaquaculture.SAquaConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishMountRenderer.class)
public class FishMountRenderer_NeverShowNameMixin {
    @Inject(method = "shouldShowName(Lcom/teammetallurgy/aquaculture/entity/FishMountEntity;)Z", at = @At("HEAD"), cancellable = true, remap = false)
    private void neverShowNameTag(FishMountEntity fishMount, CallbackInfoReturnable<Boolean> cir){
        if(!SAquaConfig.COMMON.ADJUST_FISH_SIZE.get()) return;
        cir.setReturnValue(false);
    }
}
