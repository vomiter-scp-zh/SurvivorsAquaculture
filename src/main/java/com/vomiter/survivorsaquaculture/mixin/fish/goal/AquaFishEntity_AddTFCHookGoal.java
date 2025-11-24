package com.vomiter.survivorsaquaculture.mixin.fish.goal;

import com.teammetallurgy.aquaculture.entity.AquaFishEntity;
import net.dries007.tfc.common.entities.ai.GetHookedGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AquaFishEntity.class, remap = true)
public class AquaFishEntity_AddTFCHookGoal {
    @Inject(method = "registerGoals", at = @At("RETURN"))
    private void addTFCHookGoal(CallbackInfo ci){
        var self = ((AquaFishEntity)(Object)this);
        self.goalSelector.addGoal(1, new GetHookedGoal(self));
    }
}
