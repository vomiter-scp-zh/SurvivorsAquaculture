package com.vomiter.survivorsaquaculture.mixin.fish.goal;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.teammetallurgy.aquaculture.api.AquacultureAPI;
import com.teammetallurgy.aquaculture.entity.AquaFishEntity;
import com.vomiter.survivorsaquaculture.core.fish.AquaFishes;
import net.dries007.tfc.common.entities.ai.GetHookedGoal;
import net.minecraft.world.entity.PathfinderMob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(GetHookedGoal.class)
public class GetHookedGoal_SetAquaFishLarge {
    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/dries007/tfc/util/Helpers;isEntity(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/tags/TagKey;)Z"))
    private boolean tickSetLarge(boolean original){
        var self = (MoveToBlockGoal_Accessor)this;
        if(self.getMob() instanceof AquaFishEntity fishEntity){
            Optional<AquaFishes> fishOptional = AquaFishes.byEntityType(fishEntity.getType());
            if(fishOptional.isPresent()){
                var fish = fishOptional.get();
                return AquacultureAPI.FISH_DATA.getFilletAmount(fish.item()) > 8;
            }
        }
        return original;
    }

    @ModifyExpressionValue(method = "findNearestBlock", at = @At(value = "INVOKE", target = "Lnet/dries007/tfc/util/Helpers;isEntity(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/tags/TagKey;)Z"))
    private boolean findSetLarge(boolean original){
        var self = (MoveToBlockGoal_Accessor)this;
        if(self.getMob() instanceof AquaFishEntity fishEntity){
            Optional<AquaFishes> fishOptional = AquaFishes.byEntityType(fishEntity.getType());
            if(fishOptional.isPresent()){
                var fish = fishOptional.get();
                return AquacultureAPI.FISH_DATA.getFilletAmount(fish.item()) > 8;
            }
        }
        return original;
    }


}
